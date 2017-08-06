package com.xzsoft.xc.gl.api;

import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * @ClassName: XCGLWebService 
 * @Description: 总账系统对外服务API接口
 * @author linp
 * @date 2016年3月16日 上午10:30:13 
 *
 */
@WebService(targetNamespace = "http://ws.xzsoft.com")
public interface XCGLWebService {
	
	/**
	 * @Title: getLedgers 
	 * @Description: 同步账簿信息
	 * @param lastUpdateDate
	 * @param userName
	 * @param userPwd
	 * @return    设定文件
	 */
	public String getLedgers(@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "lastUpdateDate") String lastUpdateDate,
						@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "userName") String userName,
						@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "userPwd") String userPwd);
	
	/**
	 * @Title: getDimensions 
	 * @Description: 同步维度信息
	 * @param dimType
	 * @param lastUpdateDate
	 * @param ledgerId
	 * @param userName
	 * @param userPwd
	 * @return    设定文件
	 */
	public String getDimensions(@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "dimType") String dimType,
			    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "lastUpdateDate") String lastUpdateDate,
			    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "ledgerCode") String ledgerCode,
			    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "userName") String userName,
			    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "userPwd") String userPwd);
	
	/**
	 * @Title: getAccounts 
	 * @Description: 科目信息同步处理
	 * @param lastUpdateDate
	 * @param hrcyCode
	 * @param userName
	 * @param userPwd
	 * @return    设定文件
	 */
	public String getAccounts(@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "lastUpdateDate") String lastUpdateDate,
			    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "hrcyCode") String hrcyCode,
			    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "userName") String userName,
			    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "userPwd") String userPwd);
	
	/**
	 * @Title: getCCID 
	 * @Description: 同步科目组合信息
	 * @param lastUpdateDate
	 * @param ledgerId
	 * @param userName
	 * @param userPwd
	 * @return    设定文件
	 */
	public String getCCID(@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "lastUpdateDate") String lastUpdateDate,
			    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "ledgerCode") String ledgerCode,
			    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "userName") String userName,
			    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "userPwd") String userPwd);
	
	/**
	 * @Title: getBalance 
	 * @Description: 同步总账余额信息
	 * @param lastUpdateDate
	 * @param ledgerId
	 * @param userName
	 * @param userPwd
	 * @return    设定文件
	 */
	public String getBalance(@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "lastUpdateDate") String lastUpdateDate,
			    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "ledgerCode") String ledgerCode,
			    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "userName") String userName,
			    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "userPwd") String userPwd);
	
	/**
	 * @Title: getVouchers 
	 * @Description: 同步凭证信息
	 * @param lastUpdateDate
	 * @param ledgerId
	 * @param userName
	 * @param userPwd
	 * @return    设定文件
	 */
	public String getVouchers(@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "lastUpdateDate") String lastUpdateDate,
			    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "ledgerCode") String ledgerCode,
			    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "userName") String userName,
			    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "userPwd") String userPwd);
	/**
	 * @Title: getCashSum 
	 * @Description: 同步现金流信息
	 * @param lastUpdateDate
	 * @param ledgerCode
	 * @param userName
	 * @param userPwd
	 * @return    设定文件
	 */
	public String getCashSum(@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "lastUpdateDate") String lastUpdateDate,
			    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "ledgerCode") String ledgerCode,
			    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "userName") String userName,
			    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "userPwd") String userPwd);
	/**
	 * @Title: getCashItem 
	 * @Description: 同步现金流信息
	 * @param lastUpdateDate
	 * @param userName
	 * @param userPwd
	 * @return    设定文件
	 */
	public String getCashItem(@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "lastUpdateDate") String lastUpdateDate,
			    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "userName") String userName,
			    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "userPwd") String userPwd);
	
	/**
	 * @Title: doSaveVoucher 
	 * @Description: 保存凭证
	 * @param jsonObject
	 * @param userName
	 * @param userPwd
	 * @return    设定文件
	 */
	public String doSaveVoucher(@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "jsonObject") String jsonObject,
						@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "userName") String userName,
			    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "userPwd") String userPwd);
	
	/**
	 * @Title: doSaveAndSubmitVoucher 
	 * @Description:  保存并提交单张凭证信息
	 * @param jsonObject
	 * @param userName
	 * @param userPwd
	 * @return    设定文件
	 */
	public String doSaveAndSubmitVoucher(@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "jsonObject") String jsonObject,
						@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "userName") String userName,
			    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "userPwd") String userPwd) ;
	
	/**
	 * @Title: doSubmitVouchers 
	 * @Description: 提交凭证
	 * @param jsonArray
	 * @param userName
	 * @param userPwd
	 * <p>
	 * 	参数格式： [{"V_HEAD_ID":""},{"V_HEAD_ID":""},{"V_HEAD_ID":""}…]
	 * </p>
	 * @return    设定文件
	 */
	public String doSubmitVouchers(@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "jsonArray") String jsonArray,
						@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "userName") String userName,
			    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "userPwd") String userPwd);
	
	/**
	 * @Title: doCancelSubmitVouchers 
	 * @Description:撤回提交
	 * @param jsonArray
	 * @param userName
	 * @param userPwd
	 * <p>
	 * 	参数格式： [{"V_HEAD_ID":""},{"V_HEAD_ID":""},{"V_HEAD_ID":""}…]
	 * </p>
	 * @return    设定文件
	 */
	public String doCancelSubmitVouchers(@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "jsonArray") String jsonArray,
						@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "userName") String userName,
			    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "userPwd") String userPwd);
	
	/**
	 * @Title: doDelVouchers 
	 * @Description: 删除凭证信息
	 * @param jsonArray
	 * @param userName
	 * @param userPwd
	 * <p>
	 * 	参数格式： [{"V_HEAD_ID":""},{"V_HEAD_ID":""},{"V_HEAD_ID":""}…]
	 * </p>
	 * @return    设定文件
	 */
	public String doDelVouchers(@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "jsonArray") String jsonArray,
						@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "userName") String userName,
			    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "userPwd") String userPwd);
	
	/**
	 * @Title: doCheckVouchers 
	 * @Description: 凭证审核
	 * @param jsonArray
	 * @param userName
	 * @param userPwd
	 * <p>
	 * 	参数格式： [{"V_HEAD_ID":""},{"V_HEAD_ID":""},{"V_HEAD_ID":""}…]
	 * </p>
	 * @return    设定文件
	 */
	public String doCheckVouchers(@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "jsonArray") String jsonArray,
						@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "userName") String userName,
			    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "userPwd") String userPwd);
	
	/**
	 * @Title: doUncheckVouchers 
	 * @Description: 取消凭证审核
	 * @param jsonArray
	 * @param userName
	 * @param userPwd
	 * <p>
	 * 	参数格式： [{"V_HEAD_ID":""},{"V_HEAD_ID":""},{"V_HEAD_ID":""}…]
	 * </p>
	 * @return    设定文件
	 */
	public String doUncheckVouchers(@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "jsonArray") String jsonArray,
						@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "userName") String userName,
			    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "userPwd") String userPwd);
	
	/**
	 * @Title: doRejectVouchers 
	 * @Description: 驳回凭证
	 * @param jsonArray
	 * @param userName
	 * @param userPwd
	 * <p>
	 * 	参数格式： [{"V_HEAD_ID":""},{"V_HEAD_ID":""},{"V_HEAD_ID":""}…]
	 * </p>
	 * @return    设定文件
	 */
	public String doRejectVouchers(@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "jsonArray") String jsonArray,
						@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "userName") String userName,
			    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "userPwd") String userPwd);
	
	/**
	 * @Title: doSignVouchers 
	 * @Description: 出纳签字
	 * @param jsonArray
	 * @param userName
	 * @param userPwd
	 * <p>
	 * 	参数格式： [{"V_HEAD_ID":""},{"V_HEAD_ID":""},{"V_HEAD_ID":""}…]
	 * </p>
	 * @return    设定文件
	 */
	public String doSignVouchers(@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "jsonArray") String jsonArray,
						@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "userName") String userName,
			    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "userPwd") String userPwd);
	
	/**
	 * @Title: doUnsignVouchers 
	 * @Description: 取消签字
	 * @param jsonArray
	 * @param userName
	 * @param userPwd
	 * <p>
	 * 	参数格式： [{"V_HEAD_ID":""},{"V_HEAD_ID":""},{"V_HEAD_ID":""}…]
	 * </p>
	 * @return    设定文件
	 */
	public String doUnsignVouchers(@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "jsonArray") String jsonArray,
						@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "userName") String userName,
			    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "userPwd") String userPwd);
	
	/**
	 * @Title: doAccount 
	 * @Description: 凭证过账
	 * @param jsonArray
	 * @param userName
	 * @param userPwd
	 * @return    设定文件
	 */
	public String doAccount(@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "jsonArray") String jsonArray,
						@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "userName") String userName,
			    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "userPwd") String userPwd);
	
	/**
	 * @Title: doViewVoucher 
	 * @Description: 凭证查看
	 * @param jsonArray
	 * @param userName
	 * @param userPwd
	 * @return    设定文件
	 */
	public String doViewVoucher(@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "jsonArray") String jsonArray,
						@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "userName") String userName,
			    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "userPwd") String userPwd);
}
