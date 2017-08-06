package com.xzsoft.xc.ap.service;

import java.util.HashMap;
import java.util.List;

import org.codehaus.jettison.json.JSONObject;

import com.xzsoft.xc.ap.modal.ApPayReqHBean;
import com.xzsoft.xc.ap.modal.ApPayReqLBean;
import com.xzsoft.xc.apar.modal.ApInvTransBean;

/**
 * 
  * @ClassName: ApPayReqCommonService
  * @Description: 操作付款申请单Service
  * @author 韦才文
  * @date 2016年6月14日 上午16:15:15
 */
public interface ApPayReqCommonService {
	
	/**
	 * @Title savePay
	 * @Description:保存申请单
	 * 参数格式:
	 * @param  java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	public JSONObject savePay(String reqHMsg,String reqLMsg,String deleteDtl) throws Exception;
	/**
	 * @Title addPayReqH
	 * @Description:添加申请单
	 * 参数格式:
	 * @param  java.lang.String
	 * @return java.lang.String
	 * @throws 
	 */
	public String addPayReqH(String reqHMsg) throws Exception ;
	/**
	 * @Title editPayReqH
	 * @Description:修改申请单
	 * 参数格式:
	 * @param  java.lang.String
	 * @return 
	 * @throws 
	 */
	public void editPayReqH(String reqHMsg) throws Exception ;
	/**
	 * @Title delPayReqH
	 * @Description:删除申请单
	 * 参数格式:
	 * @param  java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	public JSONObject delPayReqH(String payReqHId) throws Exception ;
	/**
	 * @Title getPayReqL
	 * @Description:查询付款申请单行表信息 
	 * 参数格式:
	 * @param  java.lang.String
	 * @return com.xzsoft.xc.ap.modal.ApPayReqLBean
	 * @throws 
	 */
	public ApPayReqLBean getPayReqL(String payReqLId) throws Exception ;
	/**
	 * @Title getPayReqLByHId
	 * @Description:通过申请单主表ID查询行表信息 
	 * 参数格式:
	 * @param  java.lang.String
	 * @return java.util.List
	 * @throws 
	 */
	public List<HashMap<String, Object>> getPayReqLByHId(String payReqHId) throws Exception ;
	/**
	 * @Title addPayReqL
	 * @Description:添加申请单行表信息
	 * 参数格式:
	 * @param  org.codehaus.jettison.json.JSONObject
	 * @return java.lang.String
	 * @throws 
	 */
	public String addPayReqL(JSONObject reqLJo) throws Exception;
	/**
	 * @Title editPayReqL
	 * @Description:更新申请单行表信息
	 * 参数格式:
	 * @param  org.codehaus.jettison.json.JSONObject
	 * @return java.lang.String
	 * @throws 
	 */
	public void editPayReqL(JSONObject reqLJo) throws Exception;
	/**
	 * @Title delPayReqL
	 * @Description:删除申请单行表信息
	 * 参数格式:
	 * @param  java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	public JSONObject delPayReqL(String payReqLId) throws Exception;
	/**
	 * @Title: getProcAndFormsByCat 
	 * @Description: 根据费用单据类型获取审批流程和审批表单信息
	 * @param ledgerId
	 * @param docCat
	 * @return
	 * @throws Exception    设定文件
	 */
	public JSONObject getProcAndFormsByCat(String ledgerId,String docCat) throws Exception ;
	/**
	 * @Title closeDoc
	 * @Description:关闭单据 
	 * 参数格式:
	 * @param  java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	public JSONObject closeDoc(String ids,String lang) throws Exception;
	/**
	 * @Title updateOccupyAmt
	 * @Description:更新占用金额
	 * 参数格式:
	 * @param  java.util.List
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	public JSONObject updateOccupyAmt(List<String> list) throws Exception;
	/**
	 * @Title delPayReq
	 * @Description:删除请申请
	 * 参数格式:
	 * @param  java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	public JSONObject delPayReq(String idArray) throws Exception;
}
