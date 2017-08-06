package com.xzsoft.xc.gl.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fr.base.core.UUID;
import com.xzsoft.xc.bg.api.impl.BudegetDataManageApi;
import com.xzsoft.xc.bg.modal.BgFactBean;
import com.xzsoft.xc.bg.util.XCBGConstants;
import com.xzsoft.xc.gl.common.AbstractVoucherHandler;
import com.xzsoft.xc.gl.dao.VoucherHandlerDAO;
import com.xzsoft.xc.gl.dao.XCGLCommonDAO;
import com.xzsoft.xc.gl.modal.VHead;
import com.xzsoft.xc.gl.service.GLVoucherHandlerService;
import com.xzsoft.xc.gl.service.VoucherHandlerService;
import com.xzsoft.xc.gl.util.XCGLConstants;
import com.xzsoft.xc.util.common.XCDateUtil;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xc.util.modal.Ledger;
import com.xzsoft.xc.util.service.RuleService;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.platform.util.XipUtil;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;

/**
 * @ClassName: GLVoucherHandlerServiceImpl 
 * @Description: 总账凭证处理接口服务类
 * @author linp
 * @date 2016年7月4日 下午5:05:28 
 *
 */
@Service("glVoucherHandlerService") 
public class GLVoucherHandlerServiceImpl extends VoucherHandlerServiceImpl implements GLVoucherHandlerService {
	// 日志记录器
	private static Logger log = Logger.getLogger(GLVoucherHandlerServiceImpl.class.getName()) ;
	
	@Resource
	private AbstractVoucherHandler voucherHandlerCommonService ;
	@Resource
	private RuleService ruleService ;
	@Resource
	private VoucherHandlerDAO voucherHandlerDAO ;
	@Resource
	private XCGLCommonDAO xcglCommonDAO ;
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate ;
	@Resource
	private VoucherHandlerService voucherHandlerService ;
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: doSaveAndSubmitVoucher</p> 
	 * <p>Description: 保存并提交单张凭证信息 </p> 
	 * @param jsonObject
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.service.impl.VoucherHandlerServiceImpl#doSaveAndSubmitVoucher(java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public String doSaveAndSubmitVoucher(String jsonObject) throws Exception {
		JSONObject jo = new JSONObject();
		try {
			// 将参数转换成JSONObject
			JSONObject paramJo = null ;
			try {
				paramJo = new JSONObject(jsonObject) ;
			} catch (Exception e) {
				//throw new Exception("参数格式不合法："+e.getMessage()) ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_CSGSBHF", null)) ;
			}
			// 参数合法时执行凭证保存操作
			if(paramJo != null){
				// 凭证头ID
				String headId = null ;			
				try {
					headId = jo.getString("headId") ;
				} catch (Exception e) {
					headId = null ;
				}
				// 被冲销凭证号
				String writeOffNum = null ;		
				try {
					writeOffNum = jo.getString("writeOffNum") ;
				} catch (Exception e) {
					writeOffNum = null ;	
				}
				
				// 将凭证信息加入JSON数组
				JSONArray ja = new JSONArray() ;
				ja.put(jsonObject) ;
				
				VHead bean = null ;
				
			    // 执行凭证保存或更新处理
				if(headId == null){ 
					/*
					 * <p>新建凭证校验规则说明：</p>
					 * <p> 
					 * 	1、账簿、会计日期、会计期、凭证分类不能为空。
					 * 	2、会计日期对应的账簿会计期是开启状态
					 * 	3、同一条凭证分录的借方和贷方不能同时录入数据 （一个凭证不能只有一条凭证分录信息）
					 * 	4、传入的科目组合必须合法
					 * 	5、凭证的借贷合计数是平衡的
					 * 	6、判断是否需要出纳签字
					 * 	7、外币凭证需要录入原币金额和汇率，本币需要录入折后金额
					 * </P>
					 */
					if(writeOffNum != null && !"".equals(writeOffNum)){ // 创建冲销凭证
						HashMap<String,Object> chkParamJo = voucherHandlerCommonService.checkParams(ja.toString(), "write_off") ;
						bean = voucherHandlerCommonService.jsonStr2headBean(chkParamJo.toString()) ;
						// 执行凭证冲销处理
						voucherHandlerDAO.saveVoucher(bean,"write_off");
						
					}else{ // 新建普通凭证
						HashMap<String,Object> chkParamJo = voucherHandlerCommonService.checkParams(ja.toString(), "save") ;
						bean = voucherHandlerCommonService.jsonStr2headBean(chkParamJo.toString()) ;
						// 执行凭证保存处理
						voucherHandlerDAO.saveVoucher(bean,"save");
					}
					
				}else{ 
					// 更新凭证
					HashMap<String,Object> chkParamJo = voucherHandlerCommonService.checkParams(ja.toString(), "update") ;
					bean = voucherHandlerCommonService.jsonStr2headBean(chkParamJo.toString()) ;
					// 执行凭证更新处理
					voucherHandlerDAO.updateVoucher(bean);
				}		
				
				// 处理凭证号
				String serialNum = bean.getSerialNum() ;
				if(serialNum == null || "".equals(serialNum)){ // 如果未生成凭证号,则生成凭证号
					
					String ledgerId = bean.getLedgerId() ;			// 账簿ID
					String categoryId = bean.getCategoryId() ;		// 凭证分类ID
					String periodCode = bean.getPeriodCode() ;		// 会计期
					String year = periodCode.substring(0, 4) ;		// 年份
					String month = periodCode.substring(5, 7) ;     // 月份
					String userId = CurrentSessionVar.getUserId() ;  
					String ruleCode = XCGLConstants.RUEL_TYPE_PZ ;
					try {
						serialNum = ruleService.getNextSerialNum(ruleCode, ledgerId,categoryId,year,month,userId) ;
						bean.setSerialNum(serialNum);
					} catch (Exception e) {
						log.error(e.getMessage());
						throw e ;
					}
				}	
				
				// 执行凭证提交处理
				List<VHead> headList = new ArrayList<VHead>() ;
				headList.add(bean) ;
				
				voucherHandlerDAO.submitVouchers(headList);
				
				// 记录预算实际发生数
				if(headList != null && headList.size()>0){
					for(VHead head : headList){
						this.RecordBudgetFactAmount(head.getHeadId());
					}
				}
				
				// 执行余额汇总处理
				List<String> list = new ArrayList<String>() ;
				list.add(bean.getHeadId()) ;
				
				voucherHandlerCommonService.sumGLBalances(list, "N", "ALL", CurrentSessionVar.getUserId(), "N");
				
				// 提示信息
				jo.put("flag", "0") ;
				//jo.put("msg", "保存并提交处理成功") ;
				jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_GL_BCBTJCLCG", null));
			}
			
		} catch (Exception e) {
			throw e ;
		}
		
		return jo.toString();
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: doSubmitGLVouchers</p> 
	 * <p>Description: 提交凭证 </p> 
	 * @param jsonArray
	 * <p>
	 * 	参数格式： [{"V_HEAD_ID":""},{"V_HEAD_ID":""},{"V_HEAD_ID":""}…]
	 * </p>
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.service.VoucherHandlerService#doSubmitVoucher(java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public String doSubmitGLVouchers(String jsonArray) throws Exception {
		JSONObject jo = new JSONObject();
		
		try {
			// 批量提交凭证
			String jsonStr = voucherHandlerService.doSubmitVouchers(jsonArray) ;
			
			// 记录预算实际发生数
			if(jsonStr != null){
				JSONObject headJo = new JSONObject(jsonStr) ;
				if(headJo.has("flag") && "0".equals(headJo.getString("flag"))){
					if(headJo.has("validV")){
						String str = (String) headJo.get("validV") ;
						JSONArray ja = new JSONArray(str) ;
						if(ja != null && ja.length() >0){
							for(int i=0; i<ja.length(); i++){
								JSONObject obj = ja.getJSONObject(i) ;
								String headId = obj.getString("V_HEAD_ID") ;
								this.RecordBudgetFactAmount(headId);
							}
						}
					}
				}
			}
			
			// 提示信息
			jo.put("flag", "0");
			//jo.put("msg", "提交成功") ;	
			jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_GL_TJCG", null)) ;	
			
		} catch (Exception e) {
			throw e ;
		}
		
		return jo.toString();
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: doCancelSubmitGLVouchers</p> 
	 * <p>Description: 撤回提交 </p> 
	 * @param jsonArray
	 * <p>
	 * 	参数格式： [{"V_HEAD_ID":""},{"V_HEAD_ID":""},{"V_HEAD_ID":""}…]
	 * </p> 
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.service.VoucherHandlerService#doCancelSubmitVoucher(java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public String doCancelSubmitGLVouchers(String jsonArray) throws Exception {
		JSONObject jo = new JSONObject();
		
		try {
			// 撤回凭证提交
			String jsonStr = voucherHandlerService.doCancelSubmitVouchers(jsonArray) ;
			
			// 删除预算发生数信息
			if(jsonStr != null){
				JSONObject headJo = new JSONObject(jsonStr) ;
				if(headJo.has("flag") && "0".equals(headJo.getString("flag"))){
					if(headJo.has("validV")){
						String str = (String) headJo.get("validV") ;
						JSONArray ja = new JSONArray(str) ;
						if(ja != null && ja.length() >0){
							for(int i=0; i<ja.length(); i++){
								JSONObject obj = ja.getJSONObject(i) ;
								String headId = obj.getString("V_HEAD_ID") ;
								BudegetDataManageApi.deleteFactBudget(headId, XCBGConstants.BG_FACT_TYPE);
							}
						}
					}
				}
			}
			// 提示信息
			jo.put("flag", "0");
			//jo.put("msg", "撤回提交成功") ;
			jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_GL_CHTJCG", null)) ;
			
		} catch (Exception e) {
			throw e ;
		}
		
		return jo.toString();
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: doNewAndSubmitVoucher</p> 
	 * <p>Description: 新建并提交凭证 </p> 
	 * @param head
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.service.VoucherHandlerService#doNewAndSubmitVoucher(com.xzsoft.xc.gl.modal.VHead)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void doNewAndSubmitVoucher(VHead head) throws Exception {
		try {
			// 新建凭证
			voucherHandlerService.doNewVoucher(head);
			
			// 提交凭证
			voucherHandlerService.doSubmitVoucher(head);
			
			// 记录预算实际发生数
			this.RecordBudgetFactAmount(head.getHeadId());
			
		} catch (Exception e) {
			throw e ;
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: doUpdateAndSubmitVoucher</p> 
	 * <p>Description: 更新并提交凭证 </p> 
	 * @param head
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.service.VoucherHandlerService#doUpdateAndSubmitVoucher(com.xzsoft.xc.gl.modal.VHead)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void doUpdateAndSubmitVoucher(VHead head)throws Exception {
		try {
			// 更新凭证
			voucherHandlerService.doUpdateVoucher(head);
			
			// 提交凭证
			voucherHandlerService.doSubmitVoucher(head);
			
			// 记录预算实际发生数
			this.RecordBudgetFactAmount(head.getHeadId());
			
		} catch (Exception e) {
			throw e ;
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: doImportVoucher</p> 
	 * <p>Description: 导入凭证 </p> 
	 * @param ledgerId
	 * @param sessionId
	 * @param workbook
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.service.VoucherHandlerService#doSaveAndCheckTmpVoucher(java.lang.String, java.lang.String, org.apache.poi.hssf.usermodel.HSSFWorkbook)
	 */
	@SuppressWarnings({"unchecked" })
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public String doImportVoucher(String ledgerId, String sessionId, HSSFWorkbook workbook) throws Exception {
		
		String jsonStr = null ;
		
		try {
			// 1.查询账簿信息
			Ledger ledger = xcglCommonDAO.getLedger(ledgerId) ;
			
			// 2.生成插入语句SQL
			HashMap<String,Object> map = voucherHandlerCommonService.generateSQL(workbook) ;
			String sql= (String) map.get("sql") ;
			HashMap<Integer,String> fieldMap = (HashMap<Integer, String>) map.get("fieldMap") ;
			
			// 3.解析excel文件
			List<HashMap<String,Object>> vals = voucherHandlerCommonService.parseExcel(fieldMap, workbook, sessionId, ledger) ;
			 
			// 4.数据保存至临时表
			int existsError = 0 ;
			if(vals != null && vals.size()>0){
				// 清除同一session下临时表数据
				voucherHandlerDAO.delTmpVoucher(sessionId);
				
				// 批量保存
				for(int i=0; i<vals.size(); i++){
					HashMap<String,Object> paramMap = vals.get(i) ; 
					
					if("N".equals(paramMap.get("FLAG"))) existsError++ ;
					
					namedParameterJdbcTemplate.update(sql, paramMap) ;
				}
			}
			
			// 如果数据完整,则继续完成凭证导入处理
			if(existsError == 0){
				// 判断本次是否只导入一张凭证
				int vCount = voucherHandlerDAO.isSameVoucher(sessionId) ;
				if(vCount > 1){
					//throw new Exception("一次只能导入单张凭证，即excel文件中每行记录的会计日期、会计期和凭证分类必须相同") ;
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_YCZNDRDZPZ", null)) ;
				}
				
				// 处理凭证格式
				existsError = voucherHandlerDAO.updateTemplateType(sessionId, ledger);
				
				if(existsError == 0){
					// 查询凭证信息
					VHead bean = voucherHandlerDAO.getTmpVoucher(sessionId) ;
					
					// 校验凭证信息
					bean = voucherHandlerCommonService.checkVoucherBeforeSave(bean) ;
					
					// 保存凭证
					voucherHandlerDAO.saveVoucher(bean, "save");
					
					// 清除本次会话所导入的数据
					voucherHandlerDAO.delTmpVoucher(sessionId);
					
					// 提示信息
					//jsonStr = "{\"flag\":\"0\",\"msg\":\"保存成功!\"}" ;
					jsonStr = "{\"flag\":\"0\",\"msg\":\""+XipUtil.getMessage(XCUtil.getLang(), "XC_GL_BCCG", null)+"\"}" ;
				}else{
					//jsonStr = "{\"flag\":\"1\",\"msg\":\"凭证数据存在非法记录，请查看错误描述信息!\"}" ;
					jsonStr = "{\"flag\":\"1\",\"msg\":\""+XipUtil.getMessage(XCUtil.getLang(), "XC_GL_PZHSJCZFFJL", null)+"\"}" ;
				}
				
			}else{
				//jsonStr = "{\"flag\":\"1\",\"msg\":\"凭证数据不完整，请查看错误描述信息!\"}" ;
				jsonStr = "{\"flag\":\"1\",\"msg\":\""+XipUtil.getMessage(XCUtil.getLang(), "XC_GL_PZSJBWC", null)+"\"}" ;
			}
			
		} catch (Exception e) {
			throw e ;
		}
		
		return jsonStr ;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: getHSSFWorkbook</p> 
	 * <p>Description: 导出Excel文件 </p> 
	 * @param headId
	 * @param filePath
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.service.VoucherHandlerService#getHSSFWorkbook(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public HSSFWorkbook doExportVoucher(String headId, String filePath) throws Exception {
		HSSFWorkbook workbook = null ;
		
		try {
			if(filePath != null){
				File file = new File(filePath) ;
				InputStream is = new FileInputStream(file) ;
				workbook = new HSSFWorkbook(is) ;
				
				HashMap<String,Object> map = voucherHandlerCommonService.generateSQL(workbook) ;
				HashMap<Integer,String> fieldMap = (HashMap<Integer, String>) map.get("fieldMap") ;
				
				// 如果凭证ID不为空，则导出凭证信息
				if(headId != null && !"".equals(headId)){
					// 凭证信息
					if(workbook != null){
						// 分录行
						List<HashMap<String,Object>> lines = voucherHandlerDAO.getExpVoucher(headId) ; 	
						// 工作页签
						HSSFSheet sheet = workbook.getSheetAt(0) ;	
						 // 数据起始行
						int rowIndex = 2 ;
						// 总列数
						int colNum = sheet.getRow(1).getPhysicalNumberOfCells(); 
						for(HashMap<String,Object> line : lines){
							HSSFRow dataRow = sheet.createRow(rowIndex);
							for(int i=0; i<colNum; i++){
								// 单元格
								HSSFCell dataCell = dataRow.createCell(i);
								// 列名
								String fieldName = fieldMap.get(i) ;
								switch(fieldName) {
								case "CREATION_DATE":	
									// 会计日期
									Date date = XCDateUtil.str2Date(String.valueOf(line.get(fieldName)))  ;
									dataCell.setCellValue(XCDateUtil.date2Str(date)); 
									break ;
								default: 	
									Object val = line.get(fieldName) ;
									if("AMOUNT".equals(fieldName)
											|| "EXCHANGE_RATE".equals(fieldName)
											|| "ENTER_DR".equals(fieldName)
											|| "ENTER_CR".equals(fieldName)
											|| "ACCOUNT_DR".equals(fieldName)
											|| "ACCOUNT_CR".equals(fieldName)){ 
										// 处理数值列
										if(val == null || (val != null && Double.parseDouble(String.valueOf(val)) == 0)){
											dataCell.setCellValue("");
										}else{
											dataCell.setCellValue(String.valueOf(line.get(fieldName))) ;
										}
										
									}else{	
										// 处理字符串列
										if(val == null || "00".equals(val)){
											dataCell.setCellValue("");
										}else{
											dataCell.setCellValue(String.valueOf(line.get(fieldName))) ;
										}
									}
									break ;
								}
							}
							rowIndex++ ;
						}
					}
				}
			}else{
				new HSSFWorkbook() ;
			}
		} catch (Exception e) {
			throw e ;
		}
		
		return workbook ;
	}
	
	/**
	 * @Title: RecordBudgetFactAmount 
	 * @Description: 凭证提交时记录预算实际发生数
	 * @param String
	 * @throws Exception    设定文件
	 */
	private void RecordBudgetFactAmount(String headId) throws Exception {
		try {
			List<HashMap<String,Object>> maps = voucherHandlerDAO.getBgCHKVoucher(headId) ;
			
			if(maps != null && maps.size() >0){
				// 当前日期和用户ID
				Date cdate = CurrentUserUtil.getCurrentDate() ;
				String userId = CurrentSessionVar.getUserId() ;
				
				List<BgFactBean> list = new ArrayList<BgFactBean>();
				for(HashMap<String,Object> map : maps){
					// 项目ID和预算项目ID
					String projectId = String.valueOf(map.get("projectId")) ;
					String bgItemId = String.valueOf(map.get("bgItemId")) ;
					String deptId = String.valueOf(map.get("deptId"));
					
					if((projectId != null && !"".equals(projectId))
							|| (bgItemId != null && !"".equals(bgItemId))){
						
						BgFactBean bean = new BgFactBean() ;
						
						bean.setFactId(UUID.randomUUID().toString());
						bean.setLedgerId(String.valueOf(map.get("ledgerId")));
						bean.setProjectId(projectId);
						bean.setBgItemId(bgItemId);
						bean.setDeptId(deptId==null ? "A" : deptId); 
						bean.setFactDate(XCDateUtil.str2Date(String.valueOf(map.get("factDate"))));
						bean.setAmount(Double.parseDouble(String.valueOf(map.get("amount"))));
						bean.setFactType(XCBGConstants.BG_FACT_TYPE); // 实际发生数
						bean.setSrcId(headId);
						bean.setSrcTab("XC_GL_V_HEADS");
						bean.setCreatedBy(userId);
						bean.setCreationDate(cdate);
						bean.setLastUpdateBy(userId);
						bean.setLastUpdateDate(cdate);
						
						list.add(bean) ;
					}
				}
				// 记录预算发生数
				BudegetDataManageApi.incrOccupancyBudget(list);
			}
			
		} catch (Exception e) {
			throw e ;
		}
	}

}
