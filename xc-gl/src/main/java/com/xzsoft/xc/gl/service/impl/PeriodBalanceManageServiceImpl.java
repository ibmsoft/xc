package com.xzsoft.xc.gl.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xzsoft.xc.gl.dao.PeriodBalanceManageDao;
import com.xzsoft.xc.gl.modal.BalanceForExcel;
import com.xzsoft.xc.gl.modal.CashForExcel;
import com.xzsoft.xc.gl.service.PeriodBalanceManageService;
import com.xzsoft.xc.gl.service.VoucherHandlerService;
import com.xzsoft.xc.util.common.XCRedisKeys;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xc.util.common.XConstants;
import com.xzsoft.xc.util.modal.Ledger;
import com.xzsoft.xip.framework.util.JSONUtil;
import com.xzsoft.xip.platform.dao.RedisDao;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.platform.util.XipUtil;

/**
 * @ClassName: PeriodBalanceManageServiceImpl 
 * @Description: 账簿余额计算实现类
 * @author tangxl
 * @date 2016年1月20日 上午10:47:43 
 *
 */
@Service("periodBalanceManageService")
public class PeriodBalanceManageServiceImpl implements PeriodBalanceManageService {

	private static final Logger log = Logger.getLogger(PeriodBalanceManageServiceImpl.class.getName()) ;
	@Resource
	private PeriodBalanceManageDao periodBalanceManageDao ;
	@Resource 
	private VoucherHandlerService voucherHandlerService;
	@Resource
	private JdbcTemplate jdbcTemplate ;
	@Resource
	private RedisDao redisDao;
	private static final DecimalFormat moenyFormat=new DecimalFormat("0.00"); 
	/**
	 *保存Excel中的数据到临时账簿余额表中
	 *@throws Exception
	 */
	@SuppressWarnings("unused")
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class,RuntimeException.class})
	public String saveGlPeriodBalances(InputStream inputStream, String ledgerId,
			String balanceType, String periodCode,String excelVersion) throws Exception {
		//获取账簿信息
		Ledger ledgerObj = periodBalanceManageDao.getLedgerInfoById(ledgerId);
		String accKey = XCRedisKeys.getAccKey(ledgerObj.getAccHrcyId());
		if(ledgerObj == null){
			//throw new Exception("账簿不存在，请重新选择！");
			throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_ZBBCZQCXXZ", null));
		}
		int exceptionRow = 0;
		int exceptionCell = 0;
		//账簿本位币币种
		String currencyCode = ledgerObj.getCnyCode();
		//解析Excel文件中的内容
		HSSFWorkbook excelBook03= null;
		XSSFWorkbook excelBook07 = null;
		String stringVal = null;
		Double doubleVal = 0.000000d;
		List<BalanceForExcel> balanceList = new java.util.ArrayList<BalanceForExcel>();
		String userId = CurrentSessionVar.getUserId();
		String sessionId = CurrentSessionVar.getSession().getId();
		try {
			//xls 结尾的Excel文件
			if("2003".equals(excelVersion)){
				excelBook03 = new HSSFWorkbook(inputStream);
				//获得工作簿的表
				HSSFSheet excelSheet = excelBook03.getSheetAt(0);
				//导入模板的前两行为标题列，第三行开始为数据列
				HSSFRow row = null;
				HSSFCell cell = null;
				//检查导入账簿余额的期间是否已模板一致
				if(excelSheet.getLastRowNum() == 2){
					excelBook03.close();
					//throw new Exception("Excel数据为空，请重新选择");
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_ESJWKQCXXZ", null));
				}
				try {
					//年初开启的期间只录入期初余额即可，第三行为数据行
					for(int i=2;i<=excelSheet.getLastRowNum();i++){
						row = excelSheet.getRow(i);
						BalanceForExcel obj = new BalanceForExcel();
						//主键id
						obj.setId(UUID.randomUUID().toString());
						//账簿id
						obj.setLedgerId(ledgerId);
						//是否合法---默认为合法
						obj.setIsValid("Y");
						obj.setCreatedBy(userId);
						obj.setLastUpdatedBy(userId);
						obj.setCreationDate(new Date());
						obj.setLastUpdateDate(new Date());
						obj.setCurrencyCode(currencyCode);
						obj.setPeriodCode(periodCode);
						obj.setSessionId(sessionId);
						for(int j = 0;j<33;j++){
							cell = row.getCell(j);
							if(cell == null){
								stringVal = "00";
								doubleVal = 0.000000;
							}else{
								if(j>23){
									doubleVal = cell.getNumericCellValue();
									if(doubleVal ==null){
										doubleVal = 0.000000;
									}
								}else{
									switch (cell.getCellType()) {
									case HSSFCell.CELL_TYPE_NUMERIC:
										stringVal = (cell.getNumericCellValue()==0.0?"00":cell.getNumericCellValue()+"");
										break;
									case HSSFCell.CELL_TYPE_STRING :
										stringVal = cell.getStringCellValue();
										break;
									default:
										stringVal = "00";
										break;
									}
									if(stringVal == null || "".equals(stringVal) || "null".equals(stringVal)){
										stringVal = "00";
									}
								}
							}
							if(j == 0){
								obj.setSegment01(stringVal);
								JSONObject tmpJsonObject = JSONObject.fromObject(redisDao.hashGet(accKey,stringVal));
								obj.setBalanceDirection(tmpJsonObject.getString("direction"));
								continue;
							}else if(j == 1){
								obj.setSegment01Name(stringVal);
								continue;
							}else if(j == 2){
								obj.setSegment02(stringVal);
								continue;
							}else if(j == 3){
								obj.setSegment02Name(stringVal);
								continue;
							}else if(j == 4){
								obj.setSegment03(stringVal);
								continue;
							}else if(j == 5){
								obj.setSegment03Name(stringVal);
								continue;
							}else if(j == 6){
								obj.setSegment04(stringVal);
								continue;
							}else if(j == 7){
								obj.setSegment07Name(stringVal);
								continue;
							}else if(j == 8){
								obj.setSegment05(stringVal);
								continue;
							}else if(j == 9){
								obj.setSegment05Name(stringVal);
								continue;
							}else if(j == 10){
								obj.setSegment06(stringVal);
								continue;
							}else if(j == 11){
								obj.setSegment06Name(stringVal);
								continue;
							}else if(j == 12){
								obj.setSegment07(stringVal);
								continue;
							}else if(j == 13){
								obj.setSegment07Name(stringVal);
								continue;
							}else if(j == 14){
								obj.setSegment08(stringVal);
								continue;
							}else if(j == 15){
								obj.setSegment08Name(stringVal);
								continue;
							}else if(j == 16){
								obj.setSegment09(stringVal);
								continue;
							}else if(j == 17){
								obj.setSegment09Name(stringVal);
								continue;
							}else if(j == 18){
								obj.setSegment10(stringVal);
								continue;
							}else if(j == 19){
								obj.setSegment10Name(stringVal);
								continue;
							}else if(j == 20){
								obj.setSegment11(stringVal);
								continue;
							}else if(j == 21){
								obj.setSegment11Name(stringVal);
								continue;
							}else if(j == 22){
								obj.setSegment12(stringVal);
								continue;
							}else if(j == 23){
								obj.setSegment12Name(stringVal);
								continue;
							}else if(j == 30){
								obj.setPeriodAmount(doubleVal);
								continue;
							}else if(j == 31){
								obj.setPeriodCBalanceO(doubleVal);
								obj.setPeriodDBalanceO(doubleVal);
								continue;
							}else if(j == 32){
								obj.setPeriodCBalance(doubleVal);
								obj.setPeriodDBalance(doubleVal);
								if("INIT".equalsIgnoreCase(balanceType))
									continue;
							}
							//账簿的期间在年初打开，只需要设置期初数量、原币<借方、贷方>、金额<借方、贷方>
							if("MID".equalsIgnoreCase(balanceType)){
								//账簿的期间在年中打开，需要设置累借、累贷、期初的原币<借方、贷方>、金额<借方、贷方>
								//同时需要计算年初的余额<借方、贷方>、原币<借方、贷方>、数量
								if(j == 24){
									obj.setDebitAmount(doubleVal);
									continue;
								}else if(j == 25){
									obj.setDebitBalanceO(doubleVal);
									continue;
								}else if(j == 26){
									obj.setDebitBalance(doubleVal);
									continue;
								}else if(j == 27){
									obj.setCrebitAmount(doubleVal);
									continue;
								}else if(j == 28){
									obj.setCrebitBalanceO(doubleVal);
									continue;
								}else if(j == 29){
									obj.setCrebitBalance(doubleVal);
									continue;
								}
							}
						}
						//计算账簿期间在年中开启时的年初余额
						if("MID".equalsIgnoreCase(balanceType)){
							//非年初开启的期间需要在所有列遍历完毕后，对年初的余额、原币、以及数量进行计算
							//计算公式为  年初 = 期初-（累计借-累计贷）*方向
							Double yearAmount = obj.getPeriodAmount()-(obj.getDebitAmount()-obj.getCrebitAmount())*Double.valueOf(obj.getBalanceDirection());
							obj.setYearAmount(yearAmount);
							Double yearBalance = obj.getPeriodCBalance()-(obj.getDebitBalance()-obj.getCrebitBalance())*Double.valueOf(obj.getBalanceDirection());
							obj.setYearCBalance(yearBalance);
							obj.setYearDBalance(yearBalance);
							Double yearBalanceO = obj.getPeriodCBalanceO()-(obj.getDebitBalanceO()-obj.getCrebitBalanceO())*Double.valueOf(obj.getBalanceDirection());
							obj.setYearCBalanceO(yearBalanceO);
							obj.setYearDBalanceO(yearBalanceO);
							/*年中开启的期间：
							 *期初金额、数量、原币  = 年初金额、数量、原币 
							 *期末金额、数量、原币 根据科目的方向来重新计算
							 */
							if("1".equalsIgnoreCase(obj.getBalanceDirection())){
								obj.setInitDAmount(obj.getYearAmount());
								obj.setInitDOBalance(obj.getYearDBalanceO());
								obj.setInitDBalance(obj.getYearDBalance());
								//重新计算期末金额、数量、原币
								if(obj.getCrebitBalance() != 0){
									obj.setPeriodCBalance(obj.getCrebitBalance());
									obj.setPeriodDBalance(obj.getPeriodDBalance()+obj.getCrebitBalance());
								}
								if(obj.getCrebitAmount() != 0){
									obj.setPeriodAmount(obj.getPeriodAmount()+obj.getCrebitAmount());
								}
								if(obj.getCrebitBalanceO() !=0){
									obj.setPeriodCBalanceO(obj.getCrebitBalanceO());
									obj.setPeriodDBalanceO(obj.getPeriodDBalanceO()+obj.getCrebitBalanceO());
								}
							}else{
								obj.setInitCAmount(obj.getYearAmount());
								obj.setInitCOBalance(obj.getYearDBalanceO());
								obj.setInitCBalance(obj.getYearDBalance());
								//重新计算期末金额、数量、原币
								if(obj.getDebitBalance() != 0){
									obj.setPeriodDBalance(obj.getDebitBalance());
									obj.setPeriodCBalance(obj.getPeriodCBalance()+obj.getDebitBalance());
								}
								if(obj.getDebitAmount() != 0){
									obj.setPeriodAmount(obj.getPeriodAmount()+obj.getDebitAmount());
								}
								if(obj.getDebitBalanceO() !=0){
									obj.setPeriodDBalanceO(obj.getDebitBalanceO());
									obj.setPeriodCBalanceO(obj.getPeriodDBalanceO()+obj.getDebitBalanceO());
								}
							}
						}else{
							if("1".equalsIgnoreCase(obj.getBalanceDirection())){
								obj.setInitDAmount(obj.getPeriodAmount());
								obj.setInitDOBalance(obj.getPeriodDBalanceO());
								obj.setInitDBalance(obj.getPeriodDBalance());
							}else{
								obj.setInitCAmount(obj.getPeriodAmount());
								obj.setInitCOBalance(obj.getPeriodCBalanceO());
								obj.setInitCBalance(obj.getPeriodCBalance());
							}
						}
						balanceList.add(obj);
					}
				} catch (Exception e) {
					//String errorMsg = "第"+exceptionRow+"行,第"+exceptionCell+"列数据格式不正确";
					HashMap<String, String> params = new HashMap<String, String>();
					params.put("exceptionRow", String.valueOf(exceptionRow));
					params.put("exceptionCell", String.valueOf(exceptionCell));
					String errorMsg = XipUtil.getMessage(XCUtil.getLang(), "XC_GL_D_HD_LSJGEBZQ", params);
					
					e.printStackTrace();
					excelBook03.close();
					throw new Exception(errorMsg);
				}
				excelBook03.close();
			}else{
				//xlsx 结尾的Excel文件
				excelBook07 = new XSSFWorkbook(inputStream);
				XSSFSheet excelSheet = excelBook07.getSheetAt(0);
				//
				XSSFRow row = null;
				XSSFCell cell = null;
				if(excelSheet.getLastRowNum() == 2){
					excelBook07.close();
					//throw new Exception("Excel数据为空，请重新选择");
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_ESJWKQCXXZ", null));
				}
				//将Excel表中的数据转化成账簿余额对象
				for(int i=2;i<=excelSheet.getLastRowNum();i++){
					try {
						exceptionRow = i+1;
						row = excelSheet.getRow(i);
						BalanceForExcel obj = new BalanceForExcel();
						//主键id
						obj.setId(UUID.randomUUID().toString());
						//账簿id
						obj.setLedgerId(ledgerId);
						//是否合法---默认为合法
						obj.setIsValid("Y");
						obj.setCreatedBy(userId);
						obj.setLastUpdatedBy(userId);
						obj.setCreationDate(new Date());
						obj.setLastUpdateDate(new Date());
						obj.setCurrencyCode(currencyCode);
						obj.setPeriodCode(periodCode);
						obj.setSessionId(sessionId);
						for(int j=0;j<33;j++){
							exceptionCell = j+1;
							cell = row.getCell(j);
							if(cell == null){
								stringVal = "00";
								doubleVal = 0.000000;
							}else{
								if(j>23){
									doubleVal = cell.getNumericCellValue();
									if(doubleVal ==null){
										doubleVal = 0.000000;
									}
								}else{
									switch (cell.getCellType()) {
									case XSSFCell.CELL_TYPE_NUMERIC:
										stringVal = (cell.getNumericCellValue()==0.0?"00":cell.getNumericCellValue()+"");
										break;
									case XSSFCell.CELL_TYPE_STRING :
										stringVal = cell.getStringCellValue();
										break;
									default:
										stringVal = "00";
										break;
									}
									if(stringVal == null || "".equals(stringVal) || "null" .equals(stringVal)){
										stringVal = "00";
									}
								}
							} 
							if(j == 0){
								obj.setSegment01(stringVal);
								JSONObject tmpJsonObject = JSONObject.fromObject(redisDao.hashGet(accKey,stringVal));
								obj.setBalanceDirection(tmpJsonObject.getString("direction"));
								continue;
							}else if(j == 1){
								obj.setSegment01Name(stringVal);
								continue;
							}else if(j == 2){
								obj.setSegment02(stringVal);
								continue;
							}else if(j == 3){
								obj.setSegment02Name(stringVal);
								continue;
							}else if(j == 4){
								obj.setSegment03(stringVal);
								continue;
							}else if(j == 5){
								obj.setSegment03Name(stringVal);
								continue;
							}else if(j == 6){
								obj.setSegment04(stringVal);
								continue;
							}else if(j == 7){
								obj.setSegment04Name(stringVal);
								continue;
							}else if(j == 8){
								obj.setSegment05(stringVal);
								continue;
							}else if(j == 9){
								obj.setSegment05Name(stringVal);
								continue;
							}else if(j == 10){
								obj.setSegment06(stringVal);
								continue;
							}else if(j == 11){
								obj.setSegment06Name(stringVal);
								continue;
							}else if(j == 12){
								obj.setSegment07(stringVal);
								continue;
							}else if(j == 13){
								obj.setSegment07Name(stringVal);
								continue;
							}else if(j == 14){
								obj.setSegment08(stringVal);
								continue;
							}else if(j == 15){
								obj.setSegment08Name(stringVal);
								continue;
							}else if(j == 16){
								obj.setSegment09(stringVal);
								continue;
							}else if(j == 17){
								obj.setSegment09Name(stringVal);
								continue;
							}else if(j == 18){
								obj.setSegment10(stringVal);
								continue;
							}else if(j == 19){
								obj.setSegment10Name(stringVal);
								continue;
							}else if(j == 20){
								obj.setSegment11(stringVal);
								continue;
							}else if(j == 21){
								obj.setSegment11Name(stringVal);
								continue;
							}else if(j == 22){
								obj.setSegment12(stringVal);
								continue;
							}else if(j == 23){
								obj.setSegment12Name(stringVal);
								continue;
							}else if(j == 30){
								obj.setPeriodAmount(doubleVal);
								continue;
							}else if(j == 31){
								obj.setPeriodCBalanceO(doubleVal);
								obj.setPeriodDBalanceO(doubleVal);
								continue;
							}else if(j == 32){
								obj.setPeriodCBalance(doubleVal);
								obj.setPeriodDBalance(doubleVal);
								if("INIT".equalsIgnoreCase(balanceType))
									continue;
							}
							//账簿的期间在年初打开，只需要设置期初数量、原币<借方、贷方>、金额<借方、贷方>
							if("MID".equalsIgnoreCase(balanceType)){
								//账簿的期间在年中打开，需要设置累借、累贷、期初的原币<借方、贷方>、金额<借方、贷方>
								//同时需要计算年初的余额<借方、贷方>、原币<借方、贷方>、数量
								if(j == 24){
									obj.setDebitAmount(doubleVal);
									continue;
								}else if(j == 25){
									obj.setDebitBalanceO(doubleVal);
									continue;
								}else if(j == 26){
									obj.setDebitBalance(doubleVal);
									continue;
								}else if(j == 27){
									obj.setCrebitAmount(doubleVal);
									continue;
								}else if(j == 28){
									obj.setCrebitBalanceO(doubleVal);
									continue;
								}else if(j == 29){
									obj.setCrebitBalance(doubleVal);
									continue;
								}
							}
						}
						//计算账簿期间在年中开启时的年初余额
						if("MID".equalsIgnoreCase(balanceType)){
							//非年初开启的期间需要在所有列遍历完毕后，对年初的余额、原币、以及数量进行计算
							//计算公式为  年初 = 期初-（累计借-累计贷）*方向
							Double yearAmount = obj.getPeriodAmount()-(obj.getDebitAmount()-obj.getCrebitAmount())*Double.valueOf(obj.getBalanceDirection());
							obj.setYearAmount(yearAmount);
							Double yearBalance = obj.getPeriodCBalance()-(obj.getDebitBalance()-obj.getCrebitBalance())*Double.valueOf(obj.getBalanceDirection());
							obj.setYearCBalance(yearBalance);
							obj.setYearDBalance(yearBalance);
							Double yearBalanceO = obj.getPeriodCBalanceO()-(obj.getDebitBalanceO()-obj.getCrebitBalanceO())*Double.valueOf(obj.getBalanceDirection());
							obj.setYearCBalanceO(yearBalanceO);
							obj.setYearDBalanceO(yearBalanceO);
							/*年中开启的期间：
							 *期初金额、数量、原币  = 年初金额、数量、原币 
							 *期末金额、数量、原币 根据科目的方向来重新计算
							 */
							if("1".equalsIgnoreCase(obj.getBalanceDirection())){
								obj.setInitDAmount(obj.getYearAmount());
								obj.setInitDOBalance(obj.getYearDBalanceO());
								obj.setInitDBalance(obj.getYearDBalance());
								//重新计算期末金额、数量、原币
								if(obj.getCrebitBalance() != 0){
									obj.setPeriodCBalance(obj.getCrebitBalance());
									obj.setPeriodDBalance(obj.getPeriodDBalance()+obj.getCrebitBalance());
								}
								if(obj.getCrebitAmount() != 0){
									obj.setPeriodAmount(obj.getPeriodAmount()+obj.getCrebitAmount());
								}
								if(obj.getCrebitBalanceO() !=0){
									obj.setPeriodCBalanceO(obj.getCrebitBalanceO());
									obj.setPeriodDBalanceO(obj.getPeriodDBalanceO()+obj.getCrebitBalanceO());
								}
							}else{
								obj.setInitCAmount(obj.getYearAmount());
								obj.setInitCOBalance(obj.getYearDBalanceO());
								obj.setInitCBalance(obj.getYearDBalance());
								//重新计算期末金额、数量、原币
								if(obj.getDebitBalance() != 0){
									obj.setPeriodDBalance(obj.getDebitBalance());
									obj.setPeriodCBalance(obj.getPeriodCBalance()+obj.getDebitBalance());
								}
								if(obj.getDebitAmount() != 0){
									obj.setPeriodAmount(obj.getPeriodAmount()+obj.getDebitAmount());
								}
								if(obj.getDebitBalanceO() !=0){
									obj.setPeriodDBalanceO(obj.getDebitBalanceO());
									obj.setPeriodCBalanceO(obj.getPeriodDBalanceO()+obj.getDebitBalanceO());
								}
							}
						}else{
							if("1".equalsIgnoreCase(obj.getBalanceDirection())){
								obj.setInitDAmount(obj.getPeriodAmount());
								obj.setInitDOBalance(obj.getPeriodDBalanceO());
								obj.setInitDBalance(obj.getPeriodDBalance());
							}else{
								obj.setInitCAmount(obj.getPeriodAmount());
								obj.setInitCOBalance(obj.getPeriodCBalanceO());
								obj.setInitCBalance(obj.getPeriodCBalance());
							}
						}
						balanceList.add(obj);
					} catch (Exception e) {
						//String errorMsg = "第"+exceptionRow+"行,第"+exceptionCell+"列数据格式不正确";
						HashMap<String, String> params = new HashMap<String, String>();
						params.put("exceptionRow", String.valueOf(exceptionRow));
						params.put("exceptionCell", String.valueOf(exceptionCell));
						String errorMsg = XipUtil.getMessage(XCUtil.getLang(), "XC_GL_D_HD_LSJGEBZQ", params);
						
						excelBook07.close();
						excelBook07.close();
						throw new Exception(errorMsg);
					}
				}
				//关闭Excel读写
				excelBook07.close();
			}

		//-------------------------插入Excel数据--------------------------------------
			try {
				if(balanceList.size() == 0){
					//throw new Exception("读取Excel数据失败，记录不存在！");
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_DQESJSBJLBCZ", null));
				}
				periodBalanceManageDao.insertExcelDataToBalanceImp(balanceList,sessionId,ledgerId);
				//导入的Excel数据中存在不合法数据时，将不合法数据展示出来
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("sessionId", sessionId);
				map.put("ledgerId", ledgerId);
				map.put("isValid", "N");
				List<HashMap> invalidList = periodBalanceManageDao.getInvalidImportData(map);
				//返回不合法信息的sessionId
				if(invalidList !=null && invalidList.size()>0)
					return map.get("sessionId");
			} catch (Exception e) {
				e.printStackTrace();
				//throw new Exception("数据插入xz_gl_balances_imp表失败，原因："+e.getMessage());
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_SJCRBSBYY", null)+e.getMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new Exception(e.getMessage());
		}
		return null;
	}
	/***
	 * @Title: errorDataToStore 
	 * @Description: TODO(返回不合法的Excel导入数据,这里仅根据科目编码就能识别当前记录) 
	 * @param invalidList
	 * @return String    返回类型 
	 * @auther tangxl
	 * @throws
	 */
	public String errorDataToStore(List<HashMap> invalidList){
		JSONArray errorArray = new JSONArray();
		for(HashMap tmp:invalidList){
			JSONObject errorObject = new JSONObject();
			errorObject.putAll(tmp);
			errorArray.add(errorObject);
		}
		return errorArray.toString();
	}
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class,RuntimeException.class})
	public void deletePeriodBalances(String ledgerId, String sessionId)
			throws Exception {
		periodBalanceManageDao.deletePeriodBalances(ledgerId, sessionId);
		
	} 
	/**
	 * 根据科目段编码获取科目组合id
	 * @throws Exception 
	 */
	public List<HashMap<String, String>> getCCIDByCCode(List<HashMap> bakanceList,String ledgerId) throws Exception{
		List<HashMap<String, String>> updateList = new ArrayList<HashMap<String, String>>();
		for(HashMap p:bakanceList){
			HashMap<String, String> segments = new HashMap<String, String>();
			HashMap<String, String> ccidMap = new HashMap<String, String>();
			//科目段
			segments.put(XConstants.XC_GL_ACCOUNTS, p.get("accId")==null? "":p.get("accId").toString());
			//供应商
			if(p.get("vendorId") !=null && !"".equals(p.get("vendorId").toString()))
				segments.put(XConstants.XC_AP_VENDORS, p.get("vendorId").toString());
			//客户
			if(p.get("customerId") !=null && !"".equals(p.get("customerId").toString()))
				segments.put(XConstants.XC_AR_CUSTOMERS, p.get("customerId").toString());
			//个人往来
			if(p.get("empId") !=null && !"".equals(p.get("empId").toString()))
				segments.put(XConstants.XIP_PUB_EMPS,p.get("empId").toString());
			//内部往来
			if(p.get("orgId") !=null && !"".equals(p.get("orgId").toString()))
				segments.put(XConstants.XIP_PUB_ORGS,p.get("orgId").toString());
			//产品
			if(p.get("productId") !=null && !"".equals(p.get("productId").toString()))
				segments.put(XConstants.XC_GL_PRODUCTS,p.get("productId").toString());
			//成本中心
			if(p.get("deptId") !=null && !"".equals(p.get("deptId").toString()))
				segments.put(XConstants.XIP_PUB_DETPS, p.get("deptId").toString());
			//项目
			if(p.get("projectId") !=null && !"".equals(p.get("projectId").toString()))
				segments.put(XConstants.XC_PM_PROJECTS, p.get("projectId").toString());
			//自定义1
			if(p.get("custom1Id") !=null && !"".equals(p.get("custom1Id").toString()))
				segments.put(XConstants.XC_GL_CUSTOM1,p.get("custom1Id").toString());
			//自定义2
			if(p.get("custom2Id") !=null && !"".equals(p.get("custom2Id").toString()))
				segments.put(XConstants.XC_GL_CUSTOM2, p.get("custom2Id").toString());
			//自定义3
			if(p.get("custom3Id") !=null && !"".equals(p.get("custom3Id").toString()))
				segments.put(XConstants.XC_GL_CUSTOM3, p.get("custom3Id").toString());
			//自定义4
			if(p.get("custom4Id") !=null && !"".equals(p.get("custom4Id").toString()))
				segments.put(XConstants.XC_GL_CUSTOM4,p.get("custom4Id").toString());
			org.codehaus.jettison.json.JSONObject jsonObject =  voucherHandlerService.handlerCCID(ledgerId, p.get("accId").toString(), segments);
			if("".equals(jsonObject.getString("ccid"))){
				//throw new Exception("科目【"+p.get("segment01Name").toString()+"】获取科目组合信息失败，无法获取该科目段的组合信息！");
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("param1", p.get("segment01Name").toString());
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_KM_HQKMZHXXSB", params));
				
			}
			ccidMap.put("ID", p.get("id").toString());
			ccidMap.put("CCID", jsonObject.getString("ccid"));
			updateList.add(ccidMap);
		}
		return updateList;
	}
	/**
	 * 
	 * @Title: batchUpdateCcid 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param   
	 * @auther tangxl
	 * @throws
	 */
	public void batchUpdateCcid(final List<HashMap<String, String>> map) throws Exception{
		StringBuffer updateSql = new StringBuffer();
		updateSql.append(" update xc_gl_balance_imp ");
		updateSql.append(" set CCID = ? ");
		updateSql.append(" where ID = ? ");
		jdbcTemplate.batchUpdate(updateSql.toString(), new BatchPreparedStatementSetter(){
			@Override
			public int getBatchSize() {
				return map.size();
			}
			@Override
			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				HashMap<String, String> unitMap = map.get(i);
				ps.setString(1, unitMap.get("CCID"));
				ps.setString(2, unitMap.get("ID"));
			}
		});
	
		
	}
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class,RuntimeException.class})
	public void fillExcelDataCCID(HashMap<String, String> map)
			throws Exception {
		List<HashMap> validList = periodBalanceManageDao.getInvalidImportData(map);
	//科目组合的12段都存在，需再次对 【科目段】和【项目段】是否为末级的合法性进行校验
		List<HashMap<String, String>> ccidList = new ArrayList<HashMap<String,String>>();
		try {
			ccidList = getCCIDByCCode(validList,map.get("ledgerId"));
		} catch (Exception e) {
			e.printStackTrace();
			//throw new Exception("从redis中获取科目组合数据失败，请检查数据的完整性！");
			throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_CRZHQKMZHSJSB", null));
			
		}
		if(ccidList.size() == 0){
			//throw new Exception("无法获取科目组合信息，科目组合数据不存在！");
			throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_WFHQKMZHXX", null));
		}else{
			//更新ccid
			batchUpdateCcid(ccidList);
		}
	}
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class,RuntimeException.class})
	public void insertExcelDataToTmp(HashMap<String, String> map)
			throws Exception {
		periodBalanceManageDao.instoreExcelData(map);
	}
	@Override
	public List<HashMap> getExportBalanceData(HashMap<String, String> map)
			throws Exception {
		return periodBalanceManageDao.getExportBalanceData(map);
	}

	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class,RuntimeException.class})
	public void deleteCashTmp(String ledgerId, String sessionId)
			throws Exception {
		 periodBalanceManageDao.deleteCashTmp(ledgerId,sessionId);
		
	}

	/**
	 *@Title: saveCashTmp
	 *@Description: TODO(将excel的数据保存到临时表中)
	 *@param 2016年3月10日  设定文件
	 *@return String 返回类型
	 *@author lizhh
	 *@throws
	 */
	
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class,RuntimeException.class})
	public String saveCashTmp(InputStream inputStream, String ledgerId,String excelVersion,String session_id) throws Exception {
		//解析Excel文件中的内容
		HSSFWorkbook excelBook03= null;
		XSSFWorkbook excelBook07 = null;
		List<CashForExcel> list = new ArrayList<CashForExcel>();
		String userId = CurrentSessionVar.getUserId();
		try {
			if("2003".equals(excelVersion)){
			excelBook03 = new HSSFWorkbook(inputStream);
			HSSFSheet excelSheet = excelBook03.getSheetAt(0);
			if(excelSheet.getLastRowNum() == 1){
				excelBook03.close();
				//throw new Exception("Excel数据为空，请重新选择");
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_ESJWKQCXXZ", null));
			}
			HSSFRow row = null;
			HSSFCell cell = null;
			String stringVal = "";
			Double doubleVal = 0.000000d;
			//数据从第三行写的
			for(int i=2;i<=excelSheet.getLastRowNum();i++){
				row = excelSheet.getRow(i);
				CashForExcel cashForExcel = new CashForExcel();
				cashForExcel.setSum_id(UUID.randomUUID().toString());
				cashForExcel.setSession_id(session_id);
				cashForExcel.setCreationDate(new Date());
				cashForExcel.setCreatedBy(userId);
				cashForExcel.setLastUpdateDate(new Date());
				cashForExcel.setLastUpdatedBy(userId);
				cashForExcel.setLedgerId(ledgerId);
				cashForExcel.setPeriod_code(getStartPeriod_codeByLedger_id(ledgerId));    
				/*
				 *现金流项目编码必须录入
				 *会计科目可录、可不录 
				 */
				for(int j=0;j<5;j++){
					cell = row.getCell(j);
					//===================处理空的单元格======================
					if(cell == null && j==0){
						excelBook03.close();
						//throw new Exception("现金流项目不能为空！");
						throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_XJLXMBNWK", null));
					}else if(cell == null ){
						stringVal = "";
						doubleVal = 0.000000;
					}else{
						if(j==4){
							doubleVal = cell.getNumericCellValue();
							if(doubleVal ==null){
								doubleVal = 0.000000;
							}
						}else{
							stringVal = cell.getStringCellValue();
							if(stringVal == null){
								stringVal = "";
							}
						}
					}
					//=======================设置导入对象的值=============================
					if(j==0){
						cashForExcel.setCa_code(stringVal);	
						continue;
					}
					else if(j==1){
						cashForExcel.setCa_name(stringVal);				
						continue;
					}
					else if(j==2){
						cashForExcel.setAcc_code(stringVal);
						continue;
					}
					else if(j==3){
						cashForExcel.setAcc_name(stringVal);
						continue;
					}
					else if(j==4){
						cashForExcel.setCa_sum(doubleVal);
						continue;
					}
					
				}
				list.add(cashForExcel);
			}
			excelBook03.close();
			}
			
			if("2007".equals(excelVersion)){

			//为2007版的excel
			excelBook07 = new XSSFWorkbook(inputStream);

			//获得工作簿的表
			XSSFSheet excelSheet = excelBook07.getSheetAt(0);
			
			if(excelSheet.getLastRowNum() == 1){
				excelBook07.close();
				//throw new Exception("Excel数据为空，请重新选择");
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_ESJWKQCXXZ", null));
			}
			XSSFRow row = null;
			XSSFCell cell = null;
			String stringVal = "";
			Double doubleVal = 0.000000d;
			//数据从第三行写的
			for(int i=2;i<=excelSheet.getLastRowNum();i++){
				row = excelSheet.getRow(i);
				CashForExcel cashForExcel = new CashForExcel();
				cashForExcel.setSum_id(UUID.randomUUID().toString());
				cashForExcel.setSession_id(session_id);
				cashForExcel.setCreationDate(new Date());
				cashForExcel.setCreatedBy(userId);
				cashForExcel.setLastUpdateDate(new Date());
				cashForExcel.setLastUpdatedBy(userId);
				cashForExcel.setLedgerId(ledgerId);
				//根据账簿id查询出要插入的要插入的开始会计期
				cashForExcel.setPeriod_code(getStartPeriod_codeByLedger_id(ledgerId));
				for(int j = 0;j < 5;j++){
					cell = row.getCell(j);
					if(cell == null && j==0){
						excelBook07.close();
						//throw new Exception("现金流项目不能为空！");
						throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_XJLXMBNWK", null));
					}else if(cell == null){
						stringVal = "";
						doubleVal = 0.000000;
					}else{
						if(j == 4){
							doubleVal = cell.getNumericCellValue();
							if(doubleVal ==null){
								doubleVal = 0.000000;
							 }
						}else{
							stringVal = cell.getStringCellValue();
							if(stringVal == null){
								stringVal = "";
							}
						}
					}
					if(j==0){
						cashForExcel.setCa_code(stringVal);
						continue;
					}
					else if(j==1){
						cashForExcel.setCa_name(stringVal);
						continue;
					}
					else if(j==2){
						cashForExcel.setAcc_code(stringVal);
						continue;
					}
					else if(j==3){
						cashForExcel.setAcc_name(stringVal);
						continue;
					}
					else if(j==4){
						cashForExcel.setCa_sum(doubleVal);
						continue;
					}
				}
				list.add(cashForExcel);	
			}
			excelBook07.close();
			}
			//-------------------------插入Excel数据--------------------------------------
			try {
				if(list.size() == 0){
					//throw new Exception("读取Excel数据失败，记录不存在！");
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_DQESJSBJLBCZ", null));
				}
				else{
					//插入Excel数据到导入表中xc_gl_cash_imp
					periodBalanceManageDao.insertCashTmp(list);
					//校验现金流项目和会计科目是否合法
					periodBalanceManageDao.setFlag(ledgerId,session_id);
					HashMap<String,String> map=new HashMap<String,String>();
					map.put("session_id", session_id);
					map.put("ledgerId", ledgerId);
					map.put("flag", "1");
					//返回非法数据列表
					List<HashMap> invalidList=periodBalanceManageDao.getInvalidFromCashTmp(map);
					if(invalidList!=null&&invalidList.size()>0){
						return map.get("session_id");					
					}
				}
				}
			catch(Exception e){
				e.printStackTrace();
				//throw new Exception("导入现金流项目期初余额失败，原因："+e.getMessage());
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_DRXJLXMQCYESB", null)+e.getMessage());
			}
		}
		catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new Exception(e.getMessage());
		}
		return null;
	}	
	/**
	 *1、更新现金流项目的CA_ID 和 会计科目的 ACC_ID 
	 *2、判断是否存在重复导入的现金流项目数据，判断依据 ledgerId、periodCode、accId、caId
	 *3、通过步骤1、2，将数据从xc_gl_cash_imp表中复制到xc_gl_cash_tmp表中
	 * @throws Exception 
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class,RuntimeException.class})
	public void fillCashItemId(HashMap<String, String> map) throws Exception {
		//更新现金流项目的CA_ID 和 会计科目的 ACC_ID，无需校验现金流和科目的存在，因为之前的步骤已经校验
		periodBalanceManageDao.updateSumIfHave(map);		
		//插入记录到xc_gl_cash_tmp中，重复数据抛出
		periodBalanceManageDao.saveSumFormTmp(map);	
	}
	@Override
	public String getStartPeriod_codeByLedger_id(String ledgerId) {
		String getPeriod_code=periodBalanceManageDao.getStartPeriod_codeByLedger_id(ledgerId);
		String year = getPeriod_code.substring(0, 4);
	    String month = getPeriod_code.substring(5,7);
	    if("Q1".equals(month)){
	    	month = "03";
	    	return year+"-"+month;
	    }
	    if("Q2".equals(month)){
	    	month = "06";
	    	return year+"-"+month;
	    }
	    if("Q3".equals(month)){
	    	month = "09";
	    	return year+"-"+month;
	    }
	    if("Q4".equals(month)){
	    	month = "12";
	    	return year+"-"+month;
	    }
	    else{
	    	if("01".equals(month)){
	    		int year1 = Integer.parseInt(year)-1;
	    		return year1+"-"+"12";
	    	}
	    	else if("12".equals(month)||"11".equals(month)){
	    		int month1 = Integer.parseInt(month)-1;
	    		return year+"-"+month1;
	    	}
	    	else{
	    		int month1 = Integer.parseInt(month)-1;
	    		return year+"-0"+month1;	    		
	    	}
	    	
	    }
	    
	}
	@Override
	public String checkTheBalance(HashMap<String, String> map) throws Exception {
		List<BalanceForExcel> checkList = periodBalanceManageDao.getCheckBalanceList(map);
		//Ext.data.Types.FLOAT
		String metaData = "{'fields':[{'name':'CATOGERY_NAME','type':'string'},{'name':'CATOGERY_STATUS','type':'string'},{'name':'CATOGERY_AMOUNT','type':'float'}]}";
		if(checkList!=null && checkList.size()>0){
			StringBuffer storeBuffer = new StringBuffer();
			boolean isEqual = true;
			String assertFlag = "1";
			String debtFlag = "-1";
			String equityFlag = "-1";
			String costFlag = "1";
			String profitFlag = "1";
			double assetAmount = 0;  //资产  1
			double debtAmount = 0;   //负债  2 
			double equityAmount =0;  //所有者权益  4
			double costAmount = 0;   //费用   5
			double profitAmount = 0; //收益   6
			for(BalanceForExcel t:checkList){
				//对余额进行累加 ==   暂时用actualCode存放科目类别编码,periodAmount存放期末余额  借方-贷方
				//余额的计算方式：(借方-贷方)*科目类别方向
				if("1".equals(t.getActualCode())){
					assertFlag = t.getSumBalancesFlag();
					if("1".equals(t.getBalanceDirection())){
						assetAmount +=t.getPeriodAmount();
					}else{
						assetAmount -=t.getPeriodAmount();
					}
				}
				if("2".equals(t.getActualCode())){
					debtFlag = t.getSumBalancesFlag();
					if("1".equals(t.getBalanceDirection())){
						debtAmount +=t.getPeriodAmount();
					}else{
						debtAmount -=t.getPeriodAmount();
					}
				}
				if("4".equals(t.getActualCode())){
					equityFlag = t.getSumBalancesFlag();
					if("1".equals(t.getBalanceDirection())){
						equityAmount +=t.getPeriodAmount();
					}else{
						equityAmount -=t.getPeriodAmount();
					}
				}
				if("5".equals(t.getActualCode())){
					costFlag = t.getSumBalancesFlag();
					if("1".equals(t.getBalanceDirection())){
						costAmount +=t.getPeriodAmount();
					}else{
						costAmount -=t.getPeriodAmount();
					}
				}
				if("6".equals(t.getActualCode())){
					profitFlag = t.getSumBalancesFlag();
					if("1".equals(t.getBalanceDirection())){
						profitAmount +=t.getPeriodAmount();
					}else{
						profitAmount -=t.getPeriodAmount();
					}
				}
			}
			assetAmount = assetAmount * Integer.valueOf(assertFlag);
			debtAmount = debtAmount * Integer.valueOf(debtFlag);
			equityAmount = equityAmount * Integer.valueOf(equityFlag);
			costAmount = costAmount * Integer.valueOf(costFlag);
			profitAmount = profitAmount * Integer.valueOf(profitFlag);
			//修正钱的精度到两位
			assetAmount = new Double(moenyFormat.format(assetAmount));
			debtAmount = new Double(moenyFormat.format(debtAmount));
			equityAmount = new Double(moenyFormat.format(equityAmount));
			costAmount = new Double(moenyFormat.format(costAmount));
			profitAmount = new Double(moenyFormat.format(profitAmount));
			//转为科学计算
			BigDecimal assetDecimal = new BigDecimal(assetAmount);
			BigDecimal debtDecimal = new BigDecimal(debtAmount);
			BigDecimal equityDecimal = new BigDecimal(equityAmount);
			BigDecimal costDecimal = new BigDecimal(costAmount);
			BigDecimal profitDecimal = new BigDecimal(profitAmount);
			//平衡计算公式：（1）期初余额情况： 资产 = 负债+所有者权益；（2）期中余额情况：资产+费用 = 负债 + 所有者权益 + 收入
			if("Y".equalsIgnoreCase(map.get("isInitial"))){
				int subCheck = assetDecimal.subtract(debtDecimal).subtract(equityDecimal).intValue();
				if(subCheck == 0){
					storeBuffer.append("{\"CATOGERY_NAME\":").append("\"").append("资产").append("\",");
					storeBuffer.append("\"CATOGERY_STATUS\":").append("\"").append(1).append("\",");
					storeBuffer.append("\"CATOGERY_AMOUNT\":").append(assetAmount).append("}").append(",");	
					storeBuffer.append("{\"CATOGERY_NAME\":").append("\"").append("负债").append("\",");
					storeBuffer.append("\"CATOGERY_STATUS\":").append("\"").append(1).append("\",");
					storeBuffer.append("\"CATOGERY_AMOUNT\":").append(debtAmount).append("}").append(",");
					storeBuffer.append("{\"CATOGERY_NAME\":").append("\"").append("所有者权益").append("\",");
					storeBuffer.append("\"CATOGERY_STATUS\":").append("\"").append(1).append("\",");
					storeBuffer.append("\"CATOGERY_AMOUNT\":").append(equityAmount).append("}");	
				}else{
					isEqual = false;
					storeBuffer.append("{\"CATOGERY_NAME\":").append("\"").append("资产").append("\",");
					storeBuffer.append("\"CATOGERY_STATUS\":").append("\"").append(0).append("\",");
					storeBuffer.append("\"CATOGERY_AMOUNT\":").append(assetAmount).append("}").append(",");	
					storeBuffer.append("{\"CATOGERY_NAME\":").append("\"").append("负债").append("\",");
					storeBuffer.append("\"CATOGERY_STATUS\":").append("\"").append(0).append("\",");
					storeBuffer.append("\"CATOGERY_AMOUNT\":").append(debtAmount).append("}").append(",");
					storeBuffer.append("{\"CATOGERY_NAME\":").append("\"").append("所有者权益").append("\",");
					storeBuffer.append("\"CATOGERY_STATUS\":").append("\"").append(0).append("\",");
					storeBuffer.append("\"CATOGERY_AMOUNT\":").append(equityAmount).append("}");
				}
			}else{
				int subCheck = assetDecimal.add(costDecimal).subtract(debtDecimal).subtract(equityDecimal).subtract(profitDecimal).intValue();
				if(subCheck == 0){
					storeBuffer.append("{\"CATOGERY_NAME\":").append("\"").append("资产").append("\",");
					storeBuffer.append("\"CATOGERY_STATUS\":").append("\"").append(1).append("\",");
					storeBuffer.append("\"CATOGERY_AMOUNT\":").append(assetAmount).append("}").append(",");	
					storeBuffer.append("{\"CATOGERY_NAME\":").append("\"").append("负债").append("\",");
					storeBuffer.append("\"CATOGERY_STATUS\":").append("\"").append(1).append("\",");
					storeBuffer.append("\"CATOGERY_AMOUNT\":").append(debtAmount).append("}").append(",");
					
					storeBuffer.append("{\"CATOGERY_NAME\":").append("\"").append("费用").append("\",");
					storeBuffer.append("\"CATOGERY_STATUS\":").append("\"").append(1).append("\",");
					storeBuffer.append("\"CATOGERY_AMOUNT\":").append(costAmount).append("}").append(",");
					
					storeBuffer.append("{\"CATOGERY_NAME\":").append("\"").append("收入").append("\",");
					storeBuffer.append("\"CATOGERY_STATUS\":").append("\"").append(1).append("\",");
					storeBuffer.append("\"CATOGERY_AMOUNT\":").append(profitAmount).append("}").append(",");
					
					storeBuffer.append("{\"CATOGERY_NAME\":").append("\"").append("所有者权益").append("\",");
					storeBuffer.append("\"CATOGERY_STATUS\":").append("\"").append(1).append("\",");
					storeBuffer.append("\"CATOGERY_AMOUNT\":").append(equityAmount).append("}");	
				}else{
					isEqual = false;
					storeBuffer.append("{\"CATOGERY_NAME\":").append("\"").append("资产").append("\",");
					storeBuffer.append("\"CATOGERY_STATUS\":").append("\"").append(0).append("\",");
					storeBuffer.append("\"CATOGERY_AMOUNT\":").append(assetAmount).append("}").append(",");	
					storeBuffer.append("{\"CATOGERY_NAME\":").append("\"").append("负债").append("\",");
					storeBuffer.append("\"CATOGERY_STATUS\":").append("\"").append(0).append("\",");
					storeBuffer.append("\"CATOGERY_AMOUNT\":").append(debtAmount).append("}").append(",");
					
					storeBuffer.append("{\"CATOGERY_NAME\":").append("\"").append("费用").append("\",");
					storeBuffer.append("\"CATOGERY_STATUS\":").append("\"").append(0).append("\",");
					storeBuffer.append("\"CATOGERY_AMOUNT\":").append(costAmount).append("}").append(",");
					
					storeBuffer.append("{\"CATOGERY_NAME\":").append("\"").append("收入").append("\",");
					storeBuffer.append("\"CATOGERY_STATUS\":").append("\"").append(0).append("\",");
					storeBuffer.append("\"CATOGERY_AMOUNT\":").append(profitAmount).append("}").append(",");
					
					storeBuffer.append("{\"CATOGERY_NAME\":").append("\"").append("所有者权益").append("\",");
					storeBuffer.append("\"CATOGERY_STATUS\":").append("\"").append(0).append("\",");
					storeBuffer.append("\"CATOGERY_AMOUNT\":").append(equityAmount).append("}");
				}
			}
			//返回结果
			if(map.containsKey("isBuild")){
				if(isEqual){
					//return  "{\"flag\":\"1\",\"msg\":\"余额已平衡！\"}";
					return  "{\"flag\":\"1\",\"msg\":\""+XipUtil.getMessage(XCUtil.getLang(), "XC_GL_YEYPH", null)+"\"}";
				}else{
					//return  "{\"flag\":\"0\",\"msg\":\"余额不平衡！\"}";
					return  "{\"flag\":\"0\",\"msg\":\""+XipUtil.getMessage(XCUtil.getLang(), "XC_GL_YEBPH", null)+"\"}";
				}
			}else{
				return JSONUtil.parseStoreJsonForExt4(checkList.size(), "true", metaData, storeBuffer.toString());
			}
		}else{
			if(map.containsKey("isBuild")){
				//return  "{\"flag\":\"0\",\"msg\":\"不存在余额记录！\"}";
				return  "{\"flag\":\"0\",\"msg\":\""+XipUtil.getMessage(XCUtil.getLang(), "XC_GL_BCZYEJL", null)+"\"}";
			}else{
				return "{\"success\":true,\"metaData\":"+metaData+",\"rows\":[],\"total\":0}";
			}
			
		}
	}
}
