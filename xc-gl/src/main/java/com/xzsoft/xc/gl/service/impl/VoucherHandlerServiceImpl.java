package com.xzsoft.xc.gl.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xzsoft.xc.gl.common.AbstractVoucherHandler;
import com.xzsoft.xc.gl.dao.VoucherHandlerDAO;
import com.xzsoft.xc.gl.dao.XCGLCommonDAO;
import com.xzsoft.xc.gl.modal.Account;
import com.xzsoft.xc.gl.modal.VHead;
import com.xzsoft.xc.gl.modal.VLine;
import com.xzsoft.xc.gl.service.VoucherHandlerService;
import com.xzsoft.xc.gl.util.XCGLConstants;
import com.xzsoft.xc.util.common.XCDateUtil;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xc.util.common.XConstants;
import com.xzsoft.xc.util.service.RuleService;
import com.xzsoft.xip.platform.dao.RedisDao;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.platform.util.XipUtil;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;

/**
 * 
 * @ClassName: VoucherHandlerServiceImpl 
 * @Description: 记账凭证处理服务接口实现类 
 * @author linp
 * @date 2015年12月18日 下午6:11:46 
 *
 */
@Service("voucherHandlerService")
public class VoucherHandlerServiceImpl implements VoucherHandlerService {
	private static Logger log = Logger.getLogger(VoucherHandlerServiceImpl.class.getName()) ;
	
	@Resource
	private AbstractVoucherHandler voucherHandlerCommonService ;
	@Resource
	private RuleService ruleService ;
	@Resource
	private VoucherHandlerDAO voucherHandlerDAO ;
	@Resource
	private XCGLCommonDAO xcglCommonDAO ;
	@Resource
	private RedisDao redisDaoImpl ;
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: generateCCID</p> 
	 * <p>Description: 凭证分录科目组合ID处理 </p> 
	 * @param ledgerId
	 * @param accId
	 * @param segments
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.service.VoucherHandlerService#generateCCID(java.lang.String, java.lang.String, java.util.HashMap)
	 */
	@Override
	public String generateCCID(String ledgerId,String accId,HashMap<String,String> segments) throws Exception {
		JSONObject retJo = this.handlerCCID(ledgerId, accId, segments) ;
		return retJo.toString() ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: handlerCCID</p> 
	 * <p>Description: 凭证分录科目组合ID处理 </p> 
	 * @param ledgerId
	 * @param accId
	 * @param segments
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.service.VoucherHandlerService#handlerCCID(java.lang.String, java.lang.String, java.util.HashMap)
	 */
	@Override
	public JSONObject handlerCCID(String ledgerId, String accId, HashMap<String, String> segments) throws Exception {
		JSONObject jo = new JSONObject();
		
		try {
			// 根据科目所启用的辅助段信息获取合法的辅助段信息
			HashMap<String, String> requiredSegments = new HashMap<String, String>() ;
			// 查询科目启用的辅助核算段信息
			List<String> accSegList = xcglCommonDAO.getAccSeg(ledgerId, accId) ;
			if(accSegList != null && accSegList.size()>0){
				// 科目信息
				Account account = xcglCommonDAO.getAccountById(accId) ;	
				// 校验辅助段是否合法
				if(segments != null && segments.size()>0){
					for(String segCode : accSegList){
						String valId = segments.get(segCode) ;
						if(valId != null && !"".equals(valId)){
							requiredSegments.put(segCode, valId) ;
						}else{
							//String message = "科目【"+account.getAccCode()+"】"+account.getAccName()+"启用了" ;
							HashMap<String, String> params = new HashMap<String, String>();
							params.put("param1", account.getAccCode());
							params.put("param2", account.getAccName());
							String message = XipUtil.getMessage(XCUtil.getLang(), "XC_GL_KM__QYL", params) ;
							switch(segCode){
							case XConstants.XC_AP_VENDORS :
								//message.concat("供应商段,生成CCID时需指定供应商信息") ;
								message.concat(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_GYSD", null)) ;
								break ;
							case XConstants.XC_AR_CUSTOMERS :
								//message.concat("客户段,生成CCID时需指定客户信息") ;
								message.concat(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_KHD", null)) ;
								break ;
							case XConstants.XC_GL_PRODUCTS :
								//message.concat("产品段,生成CCID时需指定产品信息") ;
								message.concat(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_CPD", null)) ;
								break ;
							case XConstants.XIP_PUB_ORGS :
								//message.concat("内部往来段,生成CCID时需指定内部往来信息") ;
								message.concat(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_NBWLD", null)) ;
								break ;
							case XConstants.XIP_PUB_EMPS :
								//message.concat("个人往来段,生成CCID时需指定个人往来信息") ;
								message.concat(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_GRWLD", null)) ;
								break ;
							case XConstants.XIP_PUB_DETPS :
								//message.concat("成本中心段,生成CCID时需指定成本中心信息") ;
								message.concat(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_CBZXD", null)) ;
								break ;
							case XConstants.XC_PM_PROJECTS :
								//message.concat("项目段,生成CCID时需指定项目信息") ;
								message.concat(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_XMD", null)) ;
								break ;
							case XConstants.XC_GL_CUSTOM1 :
								//message.concat("自定义维度1段,生成CCID时需指定自定义维度1信息") ;
								message.concat(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_ZDYWD1D", null)) ;
								break ;
							case XConstants.XC_GL_CUSTOM2 :
								//message.concat("自定义维度2段,生成CCID时需指定自定义维度2信息") ;
								message.concat(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_ZDYWD2D", null)) ;
								break ;
							case XConstants.XC_GL_CUSTOM3 :
								//message.concat("自定义维度3段,生成CCID时需指定自定义维度3信息") ;
								message.concat(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_ZDYWD3D", null)) ;
								break ;
							case XConstants.XC_GL_CUSTOM4 :
								//message.concat("自定义维度4段,生成CCID时需指定自定义维度4信息") ;
								message.concat(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_ZDYWD4D", null)) ;
								break ;
							default :
								break ;
							}
							throw new Exception(message) ;
						}
					}
					
				}else{
					String message = "";
					for(String segCode : accSegList){
						switch(segCode){
						case XConstants.XC_AP_VENDORS :
							//message = message.concat("供应商段、") ;
							message = message.concat(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_GYSD_", null)) ;
							break ;
						case XConstants.XC_AR_CUSTOMERS :
							//message = message.concat("客户段、") ;
							message = message.concat(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_KHD_", null)) ;
							break ;
						case XConstants.XC_GL_PRODUCTS :
							//message = message.concat("产品段、") ;
							message = message.concat(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_CPD_", null)) ;
							break ;
						case XConstants.XIP_PUB_ORGS :
							//message = message.concat("内部往来段、") ;
							message = message.concat(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_NBWLD_", null)) ;
							break ;
						case XConstants.XIP_PUB_EMPS :
							//message = message.concat("个人往来段、") ;
							message = message.concat(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_GRWLD_", null)) ;
							break ;
						case XConstants.XIP_PUB_DETPS :
							//message = message.concat("成本中心段、") ;
							message = message.concat(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_CBZXD_", null)) ;
							break ;
						case XConstants.XC_PM_PROJECTS :
							//message = message.concat("项目段、") ;
							message = message.concat(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_XMD_", null)) ;
							break ;
						case XConstants.XC_GL_CUSTOM1 :
							//message = message.concat("自定义维度1段、") ;
							message = message.concat(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_ZDYWD1D_", null)) ;
							break ;
						case XConstants.XC_GL_CUSTOM2 :
							//message = message.concat("自定义维度2段、") ;
							message = message.concat(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_ZDYWD2D_", null)) ;
							break ;
						case XConstants.XC_GL_CUSTOM3 :
							//message = message.concat("自定义维度3段、") ;
							message = message.concat(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_ZDYWD3D_", null)) ;
							break ;
						case XConstants.XC_GL_CUSTOM4 :
							//message = message.concat("自定义维度4段、") ;
							message = message.concat(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_ZDYWD4D_", null)) ;
							break ;
						default :
							break ;
						}
					}
					message = message.substring(0, message.length()-1) ;
					//throw new Exception("科目【"+account.getAccCode()+"】"+account.getAccName()+"启用了辅助段：{"+message+"},生成CCID时需指定相应的辅助段信息") ;
					HashMap<String, String> params = new HashMap<String, String>();
					params.put("param1", account.getAccCode());
					params.put("param2", account.getAccName());
					params.put("param3", message);
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_KM__QYLFZD", params)) ;
				}
			}

			// 获取科目组合的全编码信息
			String fullCode = voucherHandlerCommonService.getFullCode(ledgerId, accId, requiredSegments) ;
			
			// 处理凭证分录的CCID信息
			JSONObject ccidJo = voucherHandlerCommonService.getCCID(fullCode,ledgerId, accId, requiredSegments) ;
			
			// 返回信息
			jo.put("flag", "0") ;
			jo.put("ccid", ccidJo.getString("id")) ;
			jo.put("ccid_name", ccidJo.getString("name")) ;
			jo.put("msg", "") ;
			
		} catch (Exception e) {
			throw e ;
		}
		
		return jo ;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: doSaveVoucher</p> 
	 * <p>Description: 凭证信息保存 </p> 
	 * @param jsonObject
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.service.VoucherHandlerService#doSaveVoucher(java.lang.String)
	 */
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	@Override
	public String doSaveVoucher(String jsonObject)throws Exception {
		JSONObject jo = new JSONObject();
		try {
			// 将参数转换成JSONObject
			JSONObject paramJo = null ;
			try {
				paramJo = new JSONObject(jsonObject) ;
			} catch (Exception e) {
				//throw new Exception("参数格式不合法："+e.getMessage()) ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_CSGSBHF", null)+e.getMessage()) ;
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
				
				// 执行凭证保存或更新处理
				JSONArray ja = new JSONArray() ;
				ja.put(jsonObject) ;
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
						VHead bean = (VHead) chkParamJo.get("head") ;
						// 执行凭证冲销处理
						voucherHandlerDAO.saveVoucher(bean,"write_off");
						
						jo.put("headId", bean.getHeadId()) ;
						
					}else{ // 新建普通凭证
						HashMap<String,Object> chkParamJo = voucherHandlerCommonService.checkParams(ja.toString(), "save") ;
						VHead bean = (VHead) chkParamJo.get("head") ;
						// 执行凭证保存处理
						voucherHandlerDAO.saveVoucher(bean,"save");
						
						jo.put("headId", bean.getHeadId()) ;
					}
					
				}else{ 
					// 更新凭证
					HashMap<String,Object> chkParamJo = voucherHandlerCommonService.checkParams(ja.toString(), "update") ;
					VHead bean = (VHead) chkParamJo.get("head") ;
					// 执行凭证更新处理
					voucherHandlerDAO.updateVoucher(bean);
					
					jo.put("headId", bean.getHeadId()) ;
				}
				
				// 提示信息
				jo.put("flag", "0") ;
				//jo.put("msg", "保存成功") ;
				jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_GL_BCCG", null));
			}
		} catch (Exception e) {
			throw e; 
		}
		
		return jo.toString() ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: doSubmitVouchers</p> 
	 * <p>Description: 提交凭证 </p> 
	 * @param jsonArray
	 * <p>
	 * 	参数格式： [{"V_HEAD_ID":""},{"V_HEAD_ID":""},{"V_HEAD_ID":""}…]
	 * </p>
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.service.VoucherHandlerService#doSubmitVoucher(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public String doSubmitVouchers(String jsonArray) throws Exception {
		JSONObject jo = new JSONObject();
		
		try {
			// 执行校验
			HashMap<String,Object> map = voucherHandlerCommonService.checkParams(jsonArray, "submit") ;

			// 是否存在符合处理条件的凭证
			if(!map.containsKey("validV") || map.get("validV") == null || "".equals(map.get("validV"))) {
				//throw new Exception("所选凭证不符合提交条件") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_SXPZBFHTJTJ", null)) ;
			};
			
			// 合法凭证信息
			List<String> list = (List<String>) map.get("validV") ;
			if(list != null && list.size() >0){
				// 处理凭证号
				List<VHead> headList = voucherHandlerDAO.getVHeads(list, false) ;
				if(headList != null && headList.size()>0){
					for(VHead head : headList){
						String serialNum = head.getSerialNum() ;
						// 如果未生成凭证号,则生成凭证号
						if(serialNum == null || "".equals(serialNum)){
							String ledgerId = head.getLedgerId() ;			// 账簿ID
							String categoryId = head.getCategoryId() ;		// 凭证分类ID
							String periodCode = head.getPeriodCode() ;		// 会计期期间
							String year = periodCode.substring(0, 4) ;		// 年份
							String month = periodCode.substring(5, 7); 		// 月份
							String userId = CurrentSessionVar.getUserId() ;
							String ruleCode = XCGLConstants.RUEL_TYPE_PZ ;
							
							try {
								serialNum = ruleService.getNextSerialNum(ruleCode, ledgerId,categoryId,year,month,userId) ;
								head.setSerialNum(serialNum);
							} catch (Exception e) {
								log.error(e.getMessage());
								throw e ;
							}
						}
					}
				}				
				
				// 提交凭证
				voucherHandlerDAO.submitVouchers(headList);
				
				// 汇总余额
				voucherHandlerCommonService.sumGLBalances(list, "N", "ALL", CurrentSessionVar.getUserId(), "N");
				
				// 提示信息
				jo.put("flag", "0");
				jo.put("validV", this.getJsonStr(list)) ;
				//jo.put("msg", "提交成功") ;	
				jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_GL_TJCG", null)) ;
				
			}else{
				//throw new Exception("所选凭证不符合提交条件") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_SXPZBFHTJTJ", null)) ;
			}
			
		} catch (Exception e) {
			throw e ;
		}
		
		return jo.toString();
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: doCancelSubmitVouchers</p> 
	 * <p>Description: 撤回提交 </p> 
	 * @param jsonArray
	 * <p>
	 * 	参数格式： [{"V_HEAD_ID":""},{"V_HEAD_ID":""},{"V_HEAD_ID":""}…]
	 * </p> 
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.service.VoucherHandlerService#doCancelSubmitVoucher(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public String doCancelSubmitVouchers(String jsonArray) throws Exception {
		JSONObject jo = new JSONObject();
		
		try {
			// 执行校验
			HashMap<String,Object> map = voucherHandlerCommonService.checkParams(jsonArray, "cancel") ;
			
			// 是否存在符合处理条件的凭证
			if(!map.containsKey("validV") || map.get("validV") == null || "".equals(map.get("validV"))) {
				//throw new Exception("所选凭证不符合撤回提交条件") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_SXPZBFHCHTJTJ", null)) ;
			}
			
			// 合法凭证信息
			List<String> list = (List<String>) map.get("validV") ;
			if(list != null && list.size() >0){
				// 撤回提交
				voucherHandlerDAO.cancelSubmitVouchers(list);
				
				// 汇总余额
				voucherHandlerCommonService.sumGLBalances(list, "N", "ALL", CurrentSessionVar.getUserId(), "Y");
				
				// 提示信息
				jo.put("flag", "0");
				jo.put("validV", this.getJsonStr(list)) ;
				//jo.put("msg", "撤回提交成功") ;
				jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_GL_CHTJCG", null)) ;
				
			}else{
				//throw new Exception("所选凭证不符合撤回提交条件") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_SXPZBFHCHTJTJ", null)) ;
			}
			
		} catch (Exception e) {
			throw e ;
		}
		
		return jo.toString();
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: doDelVoucher</p> 
	 * <p>Description: 删除凭证信息 </p> 
	 * @param jsonArray
	 * <p>
	 * 	参数格式： [{"V_HEAD_ID":""},{"V_HEAD_ID":""},{"V_HEAD_ID":""}…]
	 * </p>
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.service.VoucherHandlerService#doDelVoucher(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public String doDelVouchers(String jsonArray)throws Exception {
		JSONObject jo = new JSONObject();
		
		try {
			// 执行校验
			HashMap<String,Object> map = voucherHandlerCommonService.checkParams(jsonArray, "delete") ;
			
			// 是否存在符合处理条件的凭证
			if(!map.containsKey("validV") || map.get("validV") == null || "".equals(map.get("validV"))) {
				//throw new Exception("所选凭证不符合删除条件") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_SXPZBFHSCTJ", null)) ;
			}
			
			// 合法凭证信息
			List<String> list = (List<String>) map.get("validV") ;
			if(list != null && list.size() >0){
				// 执行删除处理
				voucherHandlerDAO.delVouchers(list) ;
				
				// 提示信息
				jo.put("flag", "0") ;
				//jo.put("msg", "删除成功") ;
				jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_GL_SCCG", null)) ;
				
			}else{
				//throw new Exception("所选凭证不符合删除条件") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_SXPZBFHSCTJ", null)) ;
			}
			
		} catch (Exception e) {
			throw e ;
		}
		
		return jo.toString();
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: doCheckVoucher</p> 
	 * <p>Description: 凭证审核</p> 
	 * @param jsonArray
	 * <p>
	 * 	参数格式： [{"V_HEAD_ID":""},{"V_HEAD_ID":""},{"V_HEAD_ID":""}…]
	 * </p>
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.service.VoucherHandlerService#doCheckVoucher(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public String doCheckVouchers(String jsonArray) throws Exception {
		JSONObject jo = new JSONObject();
		
		try {
			// 执行校验
			HashMap<String,Object> map = voucherHandlerCommonService.checkParams(jsonArray, "check") ;
			
			// 是否存在符合处理条件的凭证
			if(!map.containsKey("validV") || map.get("validV") == null || "".equals(map.get("validV"))) {
				//throw new Exception("所选凭证不符合审核条件") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_SXPZBFHSHTJ", null)) ;
			}
			
			// 合法凭证信息
			List<String> list = (List<String>) map.get("validV") ;
			if(list != null && list.size() >0){
				// 执行审核处理
				voucherHandlerDAO.checkVouchers(list) ;
				
				// 提示信息
				jo.put("flag", "0") ;
				//jo.put("msg", "审核成功") ;
				jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_GL_SHCG", null)) ;
				
			}else{
				//throw new Exception("所选凭证不符合审核条件") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_SXPZBFHSHTJ", null)) ;
			}
			
		} catch (Exception e) {
			throw e ;
		}
		
		return jo.toString();
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: doUncheckVoucher</p> 
	 * <p>Description: 取消凭证审核 </p> 
	 * @param jsonArray
	 * <p>
	 * 	参数格式： [{"V_HEAD_ID":""},{"V_HEAD_ID":""},{"V_HEAD_ID":""}…]
	 * </p>
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.service.VoucherHandlerService#doUncheckVoucher(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public String doUncheckVouchers(String jsonArray) throws Exception {
		JSONObject jo = new JSONObject();
		
		try {
			// 执行校验
			HashMap<String,Object> map = voucherHandlerCommonService.checkParams(jsonArray, "uncheck") ;
			
			// 是否存在符合处理条件的凭证
			if(!map.containsKey("validV") || map.get("validV") == null || "".equals(map.get("validV"))) {
				//throw new Exception("所选凭证不符合取消审核条件") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_SXPZBFHQXSHTJ", null)) ;
			}
			
			// 合法凭证信息
			List<String> list = (List<String>) map.get("validV") ;
			if(list != null && list.size()>0){
				// 取消审核处理
				voucherHandlerDAO.uncheckVouchers(list);
				
				// 提示信息
				jo.put("flag", "0") ;
				//jo.put("msg", "取消审核成功") ;
				jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_GL_QXSHCG", null)) ;
				
			}else{
				//throw new Exception("所选凭证不符合取消审核条件") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_SXPZBFHQXSHTJ", null)) ;
			}
			
		} catch (Exception e) {
			throw e ;
		}
		
		return jo.toString();
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: doRejectVoucher</p> 
	 * <p>Description: 驳回凭证 </p> 
	 * @param jsonArray
	 * <p>
	 * 	参数格式： [{"V_HEAD_ID":""},{"V_HEAD_ID":""},{"V_HEAD_ID":""}…]
	 * </p>
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.service.VoucherHandlerService#doRejectVoucher(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public String doRejectVouchers(String jsonArray) throws Exception {
		JSONObject jo = new JSONObject();
		
		try {
			// 执行校验
			HashMap<String,Object> map = voucherHandlerCommonService.checkParams(jsonArray, "reject") ;
			
			// 是否存在符合处理条件的凭证
			if(!map.containsKey("validV") || map.get("validV") == null || "".equals(map.get("validV"))) {
				//throw new Exception("所选凭证不符合驳回条件") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_SXPZBFHBHTJ", null)) ;
			}
			
			// 合法凭证信息
			List<String> list = (List<String>) map.get("validV") ;
			if(list != null && list.size()>0){
				// 驳回凭证处理
				voucherHandlerDAO.rejectVouchers(list);
				
				// 汇总余额
				voucherHandlerCommonService.sumGLBalances(list, "N", "ALL", CurrentSessionVar.getUserId(), "Y");
				
				// 提示信息
				jo.put("flag", "0") ;
				//jo.put("msg", "驳回成功") ;
				jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_GL_BHCG", null)) ;
				
			}else{
				//throw new Exception("所选凭证不符合驳回条件") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_SXPZBFHBHTJ", null)) ;
			}
			
		} catch (Exception e) {
			throw e ;
		}
		
		return jo.toString();
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: doSignVoucher</p> 
	 * <p>Description: 出纳签字 </p> 
	 * @param jsonArray
	 * <p>
	 * 	参数格式： [{"V_HEAD_ID":""},{"V_HEAD_ID":""},{"V_HEAD_ID":""}…]
	 * </p>
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.service.VoucherHandlerService#doSignVoucher(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public String doSignVouchers(String jsonArray) throws Exception {
		JSONObject jo = new JSONObject();
		
		try {
			// 执行校验
			HashMap<String,Object> map = voucherHandlerCommonService.checkParams(jsonArray, "sign") ;
			
			// 是否存在符合处理条件的凭证
			if(!map.containsKey("validV") || map.get("validV") == null || "".equals(map.get("validV"))) {
				//throw new Exception("所选凭证不符合签字条件") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_SXPZBFHQZTJ", null)) ;
			}
			
			// 合法凭证信息
			List<String> list = (List<String>) map.get("validV") ;
			if(list != null && list.size()>0){
				// 出纳签字处理
				voucherHandlerDAO.signVouchers(list);
				
				// 提示信息
				jo.put("flag", "0") ;
				//jo.put("msg", "签字成功") ;
				jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_GL_QZCG", null)) ;
			}else{
				//throw new Exception("所选凭证不符合签字条件") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_SXPZBFHQZTJ", null)) ;
			}
			
		} catch (Exception e) {
			throw e ;
		}
		
		return jo.toString();
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: doUnsignVoucher</p> 
	 * <p>Description:  取消签字 </p> 
	 * @param jsonArray
	 * <p>
	 * 	参数格式： [{"V_HEAD_ID":""},{"V_HEAD_ID":""},{"V_HEAD_ID":""}…]
	 * </p>
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.service.VoucherHandlerService#doUnsignVoucher(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public String doUnsignVouchers(String jsonArray)throws Exception {
		JSONObject jo = new JSONObject();
		
		try {
			// 执行校验
			HashMap<String,Object> map = voucherHandlerCommonService.checkParams(jsonArray, "unsign") ;
			
			// 是否存在符合处理条件的凭证
			if(!map.containsKey("validV") || map.get("validV") == null || "".equals(map.get("validV"))) {
				//throw new Exception("所选凭证不符合取消出纳签字条件") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_SXPZBFHQXCNQZTJ", null)) ;
			}
			
			// 合法凭证信息
			List<String> list = (List<String>) map.get("validV") ;
			if(list != null && list.size()>0){
				// 取消签字处理
				voucherHandlerDAO.unsignVouchers(list);
				
				// 提示信息
				jo.put("flag", "0") ;
				//jo.put("msg", "取消签字成功") ;
				jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_GL_QXQZCG", null)) ;
			}else{
				//throw new Exception("所选凭证不符合取消出纳签字条件") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_SXPZBFHQXCNQZTJ", null)) ;
			}
		} catch (Exception e) {
			throw e ;
		}
		
		return jo.toString();
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: doAccount</p> 
	 * <p>Description: 凭证过账 </p> 
	 * @param jsonArray
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.service.VoucherHandlerService#doAccount(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public String doAccount(String jsonArray) throws Exception {
		JSONObject jo = new JSONObject();
		
		try {
			// 执行校验
			HashMap<String,Object> map = voucherHandlerCommonService.checkParams(jsonArray, "account") ;
			
			// 是否存在符合处理条件的凭证
			if(!map.containsKey("validV") || map.get("validV") == null || "".equals(map.get("validV"))) {
				//throw new Exception("所选凭证不符合记账条件") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_SXPZBFHJZTJ", null)) ;
			}
			
			// 合法凭证信息
			List<String> list = (List<String>) map.get("validV") ;
			if(list != null && list.size()>0){
				// 取消签字处理
				voucherHandlerDAO.account(list);
				
				// 重新汇总已过账的余额
				voucherHandlerCommonService.sumGLBalances(list, "Y", "ALL", CurrentSessionVar.getUserId(), "N");	
				
				// 提示信息
				jo.put("flag", "0") ;
				//jo.put("msg", "记账成功") ;
				jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_GL_JZCG", null)) ;
				
			}else{
				//throw new Exception("所选凭证不符合记账条件") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_SXPZBFHJZTJ", null)) ;
			}
			
		} catch (Exception e) {
			throw e;
		}
		
		return jo.toString();
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: doCancelAccount</p> 
	 * <p>Description: 凭证取消记账 </p> 
	 * @param jsonArray
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.service.VoucherHandlerService#doCancelAccount(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public String doCancelAccount(String jsonArray)throws Exception {
		JSONObject jo = new JSONObject();
		
		try {
			// 执行校验
			HashMap<String,Object> map = voucherHandlerCommonService.checkParams(jsonArray, "unaccount") ;
			
			// 是否存在符合处理条件的凭证
			if(!map.containsKey("validV") || map.get("validV") == null || "".equals(map.get("validV"))) {
				//throw new Exception("所选凭证不符合取消记账条件") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_SXPZBFHQXJZTJ", null)) ;
			}
			
			// 合法凭证信息
			List<String> list = (List<String>) map.get("validV") ;
			if(list != null && list.size()>0){
				// 取消签字处理
				voucherHandlerDAO.cancelAccount(list);
				
				// 重新汇总已过账的余额
				voucherHandlerCommonService.sumGLBalances(list, "Y", "ALL", CurrentSessionVar.getUserId(), "N");	
				
				// 提示信息
				jo.put("flag", "0") ;
				//jo.put("msg", "取消记账成功") ;
				jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_GL_QXJZCG", null)) ;
			}else{
				//throw new Exception("所选凭证不符合取消记账条件") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_SXPZBFHQXJZTJ", null)) ;
			}
			
		} catch (Exception e) {
			throw e ;
		}
		
		return jo.toString();
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: doViewVoucher</p> 
	 * <p>Description: 凭证查看 </p> 
	 * @param jsonArray
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.service.VoucherHandlerService#doViewVoucher(java.lang.String)
	 */
	@Override
	public String doViewVoucher(String jsonObject) throws Exception {
		String jsonStr = "{}" ;
		
		try {
			// 取得凭证头ID
			JSONObject ja = new JSONObject(jsonObject) ;
			String headId = ja.getString("V_HEAD_ID") ;
			// 查询凭证信息
			VHead bean = voucherHandlerDAO.getVHead(headId,true) ;
			
			// 将凭证信息转换成JSON格式
			jsonStr = voucherHandlerCommonService.headBean2JsonStr(bean, null) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
		return jsonStr ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getVHead</p> 
	 * <p>Description: 查询凭证头信息 </p> 
	 * @param headId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.service.VoucherHandlerService#getVHead(java.lang.String)
	 */
	@Override
	public VHead getVHead(String headId)throws Exception {
		VHead bean = null ;
		try {
			// 查询凭证头信息
			bean = voucherHandlerDAO.getVHead(headId,false) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return bean ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: headJson2Bean</p> 
	 * <p>Description: 凭证头JSON串转换成JavaBean  </p> 
	 * @param headJson
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.service.VoucherHandlerService#headJson2Bean(java.lang.String)
	 */
	@Override
	public VHead headJson2Bean(String headJson) throws Exception {
		VHead head = new VHead() ;
		try {
			JSONObject jo = new JSONObject(headJson);
			
			Object accountDate = null ;
			if(jo.has("accountDate")){
				accountDate = jo.get("accountDate") ;
			}
			if(accountDate == null) {
				//throw new Exception("会计日期不能为空") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_KJRQBNWK", null)) ;
			}

			// 借方合计
			Object  tdr = null ;
			try {
				if(jo.has("v_total_dr")){
					tdr = jo.getString("v_total_dr");
					if(tdr == null || "".equals(tdr)) tdr = 0 ;
				}else{
					tdr = 0 ;
				}
			} catch (Exception e) {
				 tdr = 0 ;
			}
			// 贷方合计数
			Object tcr = null ;
			try {
				if(jo.has("v_total_cr")){
					tcr = jo.get("v_total_cr");
					if(tcr == null || "".equals(tcr)) tcr = 0 ;
				}else{
					tcr = 0 ;
				}
			} catch (Exception e) {
				tcr = 0 ;
			}
			// 附件数
			Object attchNum = null ;
			try {
				if(jo.has("v_attchment_num")){
					attchNum = jo.get("v_attchment_num");
					if(attchNum == null || "".equals(attchNum)) attchNum = 0 ;
				}else{
					attchNum = 0 ;
				}
			} catch (Exception e) {
				attchNum = 0 ;
			}
			// 凭证头ID
			try {
				if(jo.has("v_head_id")){
					head.setHeadId(jo.getString("v_head_id"));
				}else{
					head.setHeadId(null);
				}
			} catch (Exception e) {
				head.setHeadId(null);
			}
			// 是否冲销
			try {
				if(jo.has("v_write_off")){
					head.setIsWriteOff(jo.getString("v_write_off"));
				}else{
					head.setIsWriteOff(null);
				}
			} catch (Exception e) {
				head.setIsWriteOff(null);
			}
			// 冲销号
			try {
				if(jo.has("v_write_off_num")){
					head.setWriteOffNum(jo.getString("v_write_off_num"));
				}else{
					head.setWriteOffNum(null);
				}
			} catch (Exception e) {
				head.setWriteOffNum(null);
			}
			// 凭证头摘要
			try {
				if(jo.has("v_summary")){
					head.setSummary(jo.getString("v_summary"));
				}else{
					head.setSummary(null);
				}
			} catch (Exception e) {
				head.setSummary(null);
			}
			// 凭证编号
			try {
				if(jo.has("v_serial_no")){
					head.setSerialNum(jo.getString("v_serial_no"));
				}else{
					head.setSerialNum(null);
				}
			} catch (Exception e) {
				head.setSerialNum(null);
			}
			// 账簿
			if(jo.has("ledger")){
				head.setLedgerId(jo.getString("ledger"));
			}else{
				//throw new Exception("账簿不能为空") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_ZBBNWK", null)) ;
			}
			// 会计日期
			head.setCreationDate(XCDateUtil.str2Date(String.valueOf(accountDate)));
			// 会计期
			if(jo.has("ldPeriod")){
				head.setPeriodCode(jo.getString("ldPeriod"));
			}else{
				//throw new Exception("会计期不能为空") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_KJRQBNWK", null)) ;
			}
			// 借方和贷方合计数
			head.setTotalDR(Double.parseDouble(String.valueOf(tdr)));
			head.setTotalCR(Double.parseDouble(String.valueOf(tcr)));
			// 是否需要出纳签字
			if(jo.has("v_is_signed")){
				head.setIsSigned(jo.getString("v_is_signed"));
			}else{
				//throw new Exception("是否需要出纳签字不能为空") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_SFXYCNQZBNWK", null)) ;
			}
			// 凭证分类
			if(jo.has("v_category")){
				head.setCategoryId(jo.getString("v_category"));
			}else{
				//throw new Exception("凭证分类不能为空") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_PZFLBNWK", null)) ;
			}
			// 附件张数
			head.setAttchTotal(Integer.parseInt(String.valueOf(attchNum)));
			// 凭证状态
			if(jo.has("v_status")){
				head.setVStatus(jo.getString("v_status"));
			}else{
				//throw new Exception("凭证状态不能为空") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_PZZTBNWK", null)) ;
			}
			// 凭证类型
			if(jo.has("v_template_type")){
				head.setTemplateType(jo.getString("v_template_type"));
			}else{
				//throw new Exception("凭证类型不能为空") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_PZLXBNWK", null)) ;
			}
			
		} catch (Exception e) {
			throw e;
		}
		
		return head ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: lineArray2Bean</p> 
	 * <p>Description: 凭证分录行JSON串转换成JavaBean </p> 
	 * @param lineArray
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.service.VoucherHandlerService#lineArray2Bean(java.lang.String)
	 */
	@Override
	public List<VLine> lineArray2Bean(String lineArray) throws Exception {
		List<VLine> lines = new ArrayList<VLine>() ;
		
		try {
			JSONArray docArray = new JSONArray(lineArray);
			for(int i=0; i<docArray.length(); i++){
				JSONObject jo = docArray.getJSONObject(i) ;
				
				// 去除无效分录
				String ccid = null ;
				if(jo.has("CCID")){
					ccid = jo.getString("CCID") ;
				}
				if(ccid == null || "".equals(ccid)) continue ;
				
				VLine line = new VLine() ;
				// 分录数量
				Object amount = null ;
				try {
					if(jo.has("AMOUNT")){
						amount = jo.getString("AMOUNT");
						if(amount == null || "".equals(amount)) amount = 0 ;
					}else{
						amount = 0 ;
					}
				} catch (Exception e) {
					amount = 0 ;
				}
				// 汇率
				Object er = null ;
				try {
					if(jo.has("EXCHANGE_RATE")){
						er = jo.getString("EXCHANGE_RATE");
						if(er == null || "".equals(er)) er = 0 ;
					}else{
						er = 0 ;
					}
				} catch (Exception e) {
					er = 0 ;
				}
				//原币借方金额
				Object edr = null ;
				try {
					if(jo.has("ENTER_DR")){
						edr = jo.getString("ENTER_DR");
						if(edr == null || "".equals(edr)) edr = 0 ;
					}else{
						edr = 0 ;
					}
				} catch (Exception e) {
					edr = 0 ;
				}
				// 原币贷方金额
				Object ecr = null ;
				try {
					if(jo.has("ENTER_CR")){
						ecr = jo.getString("ENTER_CR");
						if(ecr == null || "".equals(ecr)) ecr = 0 ;
					}else{
						ecr = 0 ;
					}
				} catch (Exception e) {
					ecr = 0 ;
				}
				// 折后借方金额
				Object adr = null ;
				try {
					if(jo.has("ACCOUNT_DR")){
						adr = jo.getString("ACCOUNT_DR");
						if(adr == null || "".equals(adr)) adr = 0 ;
					}else{
						adr = 0 ;
					}
				} catch (Exception e) {
					adr = 0 ;
				}
				// 折后贷方金额
				Object acr = null ;
				try {
					if(jo.has("ACCOUNT_CR")){
						acr = jo.getString("ACCOUNT_CR");
						if(acr == null || "".equals(acr)) acr = 0 ;
					}else{
						acr = 0 ;
					}
				} catch (Exception e) {
					acr = 0 ;
				}
				// 行摘要
				if(jo.has("SUMMARY")){
					line.setSummary(jo.getString("SUMMARY"));
				}else{
					//throw new Exception("分录行摘要不能为空") ;
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_FLHZYBNWK", null)) ;
				}
				line.setCcid(ccid);
				// 分录行ID
				if(jo.has("LINE_ID")){
					line.setLineId(jo.getString("LINE_ID")); 
				}
				// 现金流量项目
				if(jo.has("CA_ID")){
					line.setCaId(jo.getString("CA_ID"));	
				}
				// 数量单位
				if(jo.has("DIM_ID")){
					line.setDimId(jo.getString("DIM_ID"));	
				}
				// 币种
				if(jo.has("CURRENCY_CODE")){
					line.setCurrencyCode(jo.getString("CURRENCY_CODE"));
				}
				// 预算项目
				if(jo.has("BG_ITEM_ID")){
					line.setBgItemId(jo.getString("BG_ITEM_ID"));
				}
				// PA项目
				if(jo.has("PROJECT_ID")){
					line.setProjectId(jo.getString("PROJECT_ID"));
				}
				// 成本中心
				if(jo.has("DEPT_ID")){
					line.setDeptId(jo.getString("DEPT_ID"));
				}
				line.setAmount(Double.parseDouble(String.valueOf(amount)));	// 数量
				line.setExchangeRate(Float.parseFloat(String.valueOf(er)));	// 汇率
				line.setEnterDR(Double.parseDouble(String.valueOf(edr)));	// 原币借方金额
				line.setEnterCR(Double.parseDouble(String.valueOf(ecr)));	// 原币贷方金额
				line.setAccountDR(Double.parseDouble(String.valueOf(adr)));	// 折后借方金额
				line.setAccountCR(Double.parseDouble(String.valueOf(acr)));	// 折后贷方金额
				line.setOrderBy(i+1);
				
				lines.add(line) ;
			}
		} catch (Exception e) {
			throw e ;
		}
		
		return lines ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: doNewVoucher</p> 
	 * <p>Description:凭证新增处理 </p> 
	 * @param head
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.service.VoucherHandlerService#doNewVoucher(com.xzsoft.xc.gl.modal.VHead)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void doNewVoucher(VHead head)throws Exception {
		try {
			// 当前用户和当前日期
			String userId = CurrentSessionVar.getUserId() ;
			String empName = CurrentSessionVar.getEmpName() ;
			java.util.Date cdate = CurrentUserUtil.getCurrentDate() ;
			
			// 处理凭证头信息
			head.setCreatedBy(userId);
			head.setCreationDate(head.getCreationDate());
			head.setCreatedName(empName);
			head.setLastUpdateDate(cdate);
			head.setLastUpdatedBy(userId);
			
			// 处理凭证分录行信息
			for(VLine line : head.getLines()){
				// 处理分录行ID和日志信息
				line.setLineId(UUID.randomUUID().toString());
				line.setHeadId(head.getHeadId());
				
				line.setCreatedBy(userId);
				line.setCreationDate(cdate);
				line.setLastUpdateDate(cdate);
				line.setLastUpdatedBy(userId);
			}
			
			// 执行凭证更新处理
			if(head.getOptype()!=null && !"".equals(head.getOptype())){
				voucherHandlerDAO.saveVoucher(head, head.getOptype());
			}else{
				//throw new Exception("未指定当前凭证的操作类型,<操作类型包括:新建或冲销>!") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_WZDDQPZDCZLX", null)) ;
			}
			
		} catch (Exception e) {
			throw e ;
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: doUpdateVoucher</p> 
	 * <p>Description: 保存凭证 </p> 
	 * @param head
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.service.VoucherHandlerService#doSaveVoucher(com.xzsoft.xc.gl.modal.VHead)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void doUpdateVoucher(VHead head)throws Exception {
		try {
			// 当前用户和当前日期
			String userId = CurrentSessionVar.getUserId() ;
			java.util.Date cdate = CurrentUserUtil.getCurrentDate() ;
			
			// 修改后凭证分录行信息
			List<VLine> newVLines = head.getLines() ;
			
			// 修改前凭证分录行信息
			VHead oldHead = voucherHandlerDAO.getVHead(head.getHeadId(), true) ;
			List<VLine> oldLines = oldHead.getLines() ;
			
			// 设置凭证头信息
			head.setLastUpdateDate(cdate);
			head.setLastUpdatedBy(userId);
			head.setSrcCode(oldHead.getSrcCode());
			head.setSrcId(oldHead.getSrcId());
			head.setTemplateType(oldHead.getTemplateType());
			head.setVStatus("1");
			head.setSumFlag("N");
			
			// 存储待删除的分录行ID
			List<String> delLines = new ArrayList<String>() ;
			HashMap<String,VLine> lineMap = new HashMap<String,VLine>() ;
			for(VLine line : oldLines){
				delLines.add(line.getLineId()) ;
				lineMap.put(line.getLineId(),line) ;
			}
			// 存储待更新的分录行信息
			List<VLine> updLines = new ArrayList<VLine>() ;
			// 存储待新增的分录行信息
			List<VLine> addLines = new ArrayList<VLine>() ;
			
			for(VLine line : newVLines){
				String lineId = line.getLineId() ;
				
				// 拆分新增、删除和修改的分录行信息
				if(lineId == null || "".equals(lineId)){
					line.setLineId(UUID.randomUUID().toString());
					line.setHeadId(head.getHeadId());
					line.setCreatedBy(userId);
					line.setCreationDate(cdate);
					line.setLastUpdateDate(cdate);
					line.setLastUpdatedBy(userId);
					
					addLines.add(line) ;
					
				}else{
					if(delLines.contains(lineId)){ // 存在
						VLine oldLine = lineMap.get(lineId) ;
						
						line.setSrcDtlId(oldLine.getSrcDtlId());
						line.setLastUpdateDate(cdate);
						line.setLastUpdatedBy(userId);
						
						// 记录更新分录行
						updLines.add(line) ;
						
						// 减少待删除的分录行项目
						delLines.remove(lineId) ;
					}
				}
			}
			
			// 记录分录行中新增、更新和删除的项目
			head.setAddLines(addLines);
			head.setUpdLines(updLines);
			head.setDelLines(delLines);
			
			// 执行凭证更新处理
			voucherHandlerDAO.updateVoucher(head);
			
		} catch (Exception e) {
			throw e ;
		}
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: doSubmitVouchers</p> 
	 * <p>Description: 提交凭证 </p> 
	 * @param head
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.service.VoucherHandlerService#doSubmitVouchers(com.xzsoft.xc.gl.modal.VHead)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void doSubmitVoucher(VHead head)throws Exception {
		try {
			if(head != null && head.getHeadId() != null && !"".equals(head.getHeadId())){
				// 如果未生成凭证号,则生成凭证号
				String serialNum = head.getSerialNum() ;
				if(serialNum == null || "".equals(serialNum)){
					String ledgerId = head.getLedgerId() ;			// 账簿ID
					String categoryId = head.getCategoryId() ;		// 凭证分类ID
					String periodCode = head.getPeriodCode() ;		// 会计期期间
					String year = periodCode.substring(0, 4) ;		// 年份
					String month = periodCode.substring(5, 7); 		// 月份
					String userId = CurrentSessionVar.getUserId() ;
					String ruleCode = XCGLConstants.RUEL_TYPE_PZ ;
					
					try {
						serialNum = ruleService.getNextSerialNum(ruleCode, ledgerId,categoryId,year,month,userId) ;
						head.setSerialNum(serialNum);
					} catch (Exception e) {
						//throw new Exception("生成凭证编号出错>"+e.getMessage()) ;
						throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_SCPZBHCC", null)+e.getMessage()) ;
					}
				}
				
				// 提交凭证
				List<VHead> headList = new ArrayList<VHead>() ;
				headList.add(head) ;
				voucherHandlerDAO.submitVouchers(headList);
				
				// 凭证头ID列表
				List<String> list = new ArrayList<String>() ;
				list.add(head.getHeadId()) ;
				
				// 汇总余额
				voucherHandlerCommonService.sumGLBalances(list, "N", "ALL", CurrentSessionVar.getUserId(), "N");
			}
			
		} catch (Exception e) {
			throw e ;
		}
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: doCheckVouchers</p> 
	 * <p>Description: 审核凭证 </p> 
	 * @param head
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.service.VoucherHandlerService#doCheckVouchers(com.xzsoft.xc.gl.modal.VHead)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void doCheckVouchers(VHead head)throws Exception {
		try {
			// 凭证头ID列表
			List<String> list = new ArrayList<String>() ;
			list.add(head.getHeadId()) ;
			
			// 审核凭证
			voucherHandlerDAO.checkVouchers(list);
			
		} catch (Exception e) {
			throw e ;
		}
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: doUncheckVouchers</p> 
	 * <p>Description: 取消复核 </p> 
	 * @param head
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.service.VoucherHandlerService#doUncheckVouchers(com.xzsoft.xc.gl.modal.VHead)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void doUncheckVouchers(VHead head)throws Exception {
		try {
			// 凭证头ID列表
			List<String> list = new ArrayList<String>() ;
			list.add(head.getHeadId()) ;
			
			// 取消凭证审核
			voucherHandlerDAO.uncheckVouchers(list);
			
		} catch (Exception e) {
			throw e ;
		}
	}
	
	/**
	 * @Title: getJsonStr 
	 * @Description: 将List数组转换为JSON字符串
	 * @param list
	 * @return
	 * @throws JSONException    设定文件
	 */
	private String getJsonStr(List<String> list) throws JSONException{
		if(list != null && list.size()>0){
			JSONArray ja = new JSONArray() ;
			for(String str : list){
				JSONObject jo = new JSONObject() ;
				jo.put("V_HEAD_ID", str) ;
				ja.put(jo) ;
			}
			return ja.toString() ;
		}else{
			return null ;
		}
	}
}
