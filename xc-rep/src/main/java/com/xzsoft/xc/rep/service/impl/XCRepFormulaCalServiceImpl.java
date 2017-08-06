package com.xzsoft.xc.rep.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import net.sf.json.JSONObject;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xzsoft.xc.gl.dao.XCGLCommonDAO;
import com.xzsoft.xc.rep.dao.XCRepCommonDAO;
import com.xzsoft.xc.rep.dao.XCRepDataDAO;
import com.xzsoft.xc.rep.modal.CellValueBean;
import com.xzsoft.xc.rep.modal.FuncBean;
import com.xzsoft.xc.rep.modal.PreFormulaBean;
import com.xzsoft.xc.rep.modal.RepSheetBean;
import com.xzsoft.xc.rep.service.XCRepFormulaCalService;
import com.xzsoft.xc.rep.util.XCExpFuncInvokeUtil;
import com.xzsoft.xc.rep.util.XCRepCacheVarsUtil;
import com.xzsoft.xc.rep.util.XCRepConstants;
import com.xzsoft.xc.util.common.XConstants;
import com.xzsoft.xc.util.modal.Ledger;


/**
 * @ClassName: XCRepFormulaCalServiceImpl 
 * @Description: 公式计算服务接口实现类
 * @author linp
 * @date 2016年8月10日 下午1:59:16 
 *
 */
@Service("xcRepFormulaCalService")
public class XCRepFormulaCalServiceImpl implements XCRepFormulaCalService {
	@Autowired
	private XCRepCommonDAO xcRepCommonDAO ;
	@Autowired
	private XCRepDataDAO xcRepDataDAO ;
	@Autowired
	private XCGLCommonDAO xcglCommonDAO ;

	/*
	 * (非 Javadoc) 
	 * <p>Title: doParseRepFormula</p> 
	 * <p>Description: 运算GetRepValue公式 </p> 
	 * @param formula
	 * @param sheetBean
	 * <p>
	 *	公式信息： 	GetRepVal("|QJ|=|XZRQ:1|,|DW|=|XZDW|,|ROW|=|A01-0001-0002|,|COL|=|A01-0001-0002|,|BZ|=|CNY|")
	 * 	参数信息(JSON格式): {"entityCode":"XZDW","cnyCode":"CNY","rowCode":"A01-0002-0007","periodCode":"XZRQ","colCode":"A01-0001-0002"}
	 * </p>
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.service.XCRepFormulaCalService#doParseRepFormula(com.xzsoft.xc.rep.modal.CalFormulaBean, java.util.HashMap)
	 */
	@Override
	public String doParseRepFormula(PreFormulaBean preFormulaBean, RepSheetBean sheetBean) throws Exception {
		// 结果值
		String result = null ;
		
		// 条件信息
		CellValueBean cellValueBean = new CellValueBean() ;
		BeanUtils.copyProperties(sheetBean, cellValueBean);
		
		// 公式信息
		String params = preFormulaBean.getFormulaJson() ;
		JSONObject paramJo = JSONObject.fromObject(params) ;
		// 期间
		if(paramJo.has("periodCode")){
			// 期间字符串
			String textStr = paramJo.getString("periodCode") ;
			String[] arr = textStr.split(":"); 
			// 期间变量
			String val = arr[0] ;
			// 期间偏移量
			String offset = null ;
			if(arr.length == 2){
				offset = arr[1] ;
			}
			// 当前期间
			String currentPeriod =  sheetBean.getPeriodCode() ;
			int year = Integer.parseInt(currentPeriod.substring(0, 4)) ;	// 年份
			int month = Integer.parseInt(currentPeriod.substring(5, 7)) ;	// 月份

			switch(val){
			case "BNQC" :	// 本年年初数
				String BNQC = this.getNewPeriod(year, 1, offset) ;
				cellValueBean.setPeriodCode(BNQC);
				break;
				
			case "SNNQM" :	// 上年期末(年期间): 针对年期间处理:即2016年上年期末未2015，2015年为2014，...
				int newYear = year - 1 ;
				if(offset != null){
					newYear += Integer.parseInt(offset) ;
				}
				String SNNQM = String.valueOf(newYear) ;
				cellValueBean.setPeriodCode(SNNQM);
				break;
				
			case "SNQM" :	// 上年期末数：xxxx-12
				String SNQM = this.getNewPeriod(year-1, 12, offset) ;
				cellValueBean.setPeriodCode(SNQM);
				break;
				
			case "SNTQ" :	// 上年同期数
				String SNTQ = this.getNewPeriod(year-1, month, offset) ;
				cellValueBean.setPeriodCode(SNTQ);
				break;
				
			case "SYBN" :	// 上月同期数-年内:(如果期间涉及跨年后则结果值清零)
				if(month == 1){
					cellValueBean.setPeriodCode("-1");
				}else{
					String SYBN = this.getNewPeriod(year, month-1, offset) ;
					cellValueBean.setPeriodCode(SYBN);
				}
				break;
				
			case "SYTQ" :	// 上月同期数
				String SYTQ = this.getNewPeriod(year, month-1, offset) ;
				cellValueBean.setPeriodCode(SYTQ);
				break;
				
			case "XZRQ" :	// 当前报表日期
				String XZRQ = this.getNewPeriod(year, month, offset) ;
				cellValueBean.setPeriodCode(XZRQ);
				break;
				
			default:
				// 期间编码（yyyy-MM）
				cellValueBean.setPeriodCode(val);
				break ;
			}
		}
		// 单位
		if(paramJo.has("entityCode")){
			String val = paramJo.getString("entityCode") ;
			if(!"XZDW".equals(val)){
				JSONObject entityJo = XCRepCacheVarsUtil.getCachedData(XConstants.XIP_PUB_ORGS, null, val) ;
				cellValueBean.setOrgId("-1");
				if(entityJo != null && entityJo.has("id")){
					String orgId = entityJo.getString("id") ;
					cellValueBean.setOrgId(orgId);
					Ledger ledger = xcglCommonDAO.getLedgerByOrg(orgId) ;
					cellValueBean.setLedgerId(ledger.getLedgerId());
				}
			}
		}
		// 币种
		if(paramJo.has("cnyCode")){
			cellValueBean.setCnyCode(paramJo.getString("cnyCode"));
		}
		// 行指标
		if(paramJo.has("rowCode")){
			String rowItemCode = paramJo.getString("rowCode") ;
			JSONObject rowItemJo = XCRepCacheVarsUtil.getCachedData(XCRepConstants.XC_REP_ROWITEM, sheetBean.getAccHrcyId(), rowItemCode) ;
			cellValueBean.setRowItemId("-1");
			if(rowItemJo != null && rowItemJo.has("rowitemId")){
				cellValueBean.setRowItemId(rowItemJo.getString("rowitemId"));
			}
		}
		// 列指标
		if(paramJo.has("colCode")){
			String colItemCode = paramJo.getString("colCode") ;
			JSONObject colItemJo = XCRepCacheVarsUtil.getCachedData(XCRepConstants.XC_REP_COLITEM, sheetBean.getAccHrcyId(), colItemCode) ;
			cellValueBean.setColItemId("-1");
			if(colItemJo != null && colItemJo.has("colitemId")){
				cellValueBean.setColItemId(colItemJo.getString("colitemId"));
			}
		}
		
		// 查询指标值
		cellValueBean = xcRepDataDAO.getCellValueBean(cellValueBean) ;
		if(cellValueBean == null){
			result = "(0)" ;
		}else{
			BigDecimal cellv = new BigDecimal(cellValueBean.getCellV()) ;
			result = cellv.toPlainString() ;
		}
		
		return result ;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: doParseExpFormula</p> 
	 * <p>Description: 运算GetExpValue公式 </p> 
	 * @param formula
	 * @param map
	 * <p>
	 * 	公式串：GetExpVal("getGLBalValue>>BAL_TYPE=T_PJTD_JE,XC_GL_ACCOUNTS=1002"...)
	 *  预处理后：{"BAL_TYPE":"T_PJTD_JE","XC_GL_ACCOUNTS":"1002"}
	 * </p>
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.service.XCRepFormulaCalService#doParseExpFormula(com.xzsoft.xc.rep.modal.CalFormulaBean, java.util.HashMap)
	 */
	@Override
	public String doParseExpFormula(PreFormulaBean preFormulaBean, RepSheetBean sheetBean) throws Exception {
		// 结果值
		String result = null ;
		
		// 公式信息
		String formulaStr = preFormulaBean.getFormulaStr() ;
		
		// 自定义函数编码
		String formulaArr[] = formulaStr.split(">>") ;
		String funTextStr = formulaArr[0] ;
		String funCode = funTextStr.substring(funTextStr.indexOf("(\"")+2, funTextStr.length()) ;
		
		// 自定义函数信息
		FuncBean funcBean = xcRepCommonDAO.getFunBeanByCode(funCode) ;
		if(funcBean != null){
			// 预处理信息
			String params = preFormulaBean.getFormulaJson() ;
			// 函数类型：JAVA-java方法、PROC-存储过程、WebService-webservice服务接口
			String funType = funcBean.getFunType() ;
			switch(funType){
			case XCRepConstants.FUNC_TYPE_JAVA:
				result = XCExpFuncInvokeUtil.invokeJava(funcBean, params, sheetBean) ;
				break ;
			case XCRepConstants.FUNC_TYPE_PROC:
				result = XCExpFuncInvokeUtil.invokeProc(funcBean, params, sheetBean) ;
				break ;
			case XCRepConstants.FUNC_TYPE_WS:
				result = XCExpFuncInvokeUtil.invokeWebService(funcBean, params, sheetBean) ;
				break ;
			default:
				result = "(0)" ;
				break ;
			}
		}else{
			result = "(0)" ;
		}
		
		return result;
	}
	
	/**
	 * @Title: getNewPeriod 
	 * @Description: 获取新期间
	 * @param currentPeriod
	 * @param offset : 期间偏移量：负数向后偏移、正数向前偏移
	 * @return
	 * @throws Exception    设定文件
	 */
	private String getNewPeriod(int year,int month, String offset) throws Exception {
		// 期间偏移量
		int n = 0 ;
		if(offset != null) {
			n = Integer.parseInt(offset) ;
		}
		// 组合期间
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);
		c.add(Calendar.MONTH, -n-1);
		
		return sdf.format(c.getTime()) ;
	}
	
}
