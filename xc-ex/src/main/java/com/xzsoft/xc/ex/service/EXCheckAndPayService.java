package com.xzsoft.xc.ex.service;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.xzsoft.xc.ex.modal.CheckAndPayDocDTO;

/**
 * @ClassName: EXCheckAndPayService 
 * @Description: 单据复核、单据支付的处理接口
 * @author linp
 * @date 2016年3月23日 下午4:07:33 
 *
 */
public interface EXCheckAndPayService {

	/**
	 * @Title: checkEXDoc 
	 * @Description: 单据复核处理
	 * <p>
	 * 	1、生成付款单信息
	 * 	2、生成付款或挂账凭证
	 * 	3、更新业务单据信息
	 * </p>
	 * @param dto
	 * @throws Exception    设定文件
	 */
	public JSONObject checkEXDoc(CheckAndPayDocDTO dto) throws Exception ;
	
	/**
	 * @Title: uncheckEXDoc 
	 * @Description: 报销单取消复核处理
	 * <p>
	 * 	 报销单取消复核处理, 系统一次支持多张报销单据的取消复核处理
	 *  
	 *  传入参数格式为: [{"EX_DOC_ID":"","EX_DOC_ID":""...}]
	 *  
	 * 	如果单据为立即支付：删除凭证、删除支付单和更新费用报销单
	 *  如果单据为挂账支付：删除凭证和更新费用报销单
	 *  
	 * </p>
	 * @param exDocId
	 * @throws Exception    设定文件
	 */
	public void uncheckEXDoc(JSONArray ja) throws Exception ;
	
	/**
	 * @Title: cancelDoc 
	 * @Description: 取消单据
	 * @param docId
	 * @throws Exception    设定文件
	 */
	public void cancelDoc(String docId,String cancelReason) throws Exception ;
	
	/**
	 * @Title: payDoc 
	 * @Description: 新建付款单
	 * <p>
	 * 	1、生成付款单信息
	 * 	2、生成付款凭证
	 * 	3、更新业务单据信息
	 * </P>
	 * @param dto
	 * @throws Exception    设定文件
	 */
	public JSONObject createPayDoc(CheckAndPayDocDTO dto) throws Exception ;
	
	/**
	 * @Title: editPayDoc 
	 * @Description: 修改付款单
	 * @param dto
	 * @throws Exception    设定文件
	 */
	public JSONObject editPayDoc(CheckAndPayDocDTO dto) throws Exception ;
	
	/**
	 * @Title: uncheckPayDoc 
	 * @Description: 取消付款单
	 * <p>
	 * 		传入参数格式为: [{"EX_PAY_ID":"","EX_PAY_ID":""...}]
	 * </p>
	 * @param ja
	 * @throws Exception    设定文件
	 */
	public void uncheckPayDoc(JSONArray ja) throws Exception ;
	
	/**
	 * @Title: saveVoucher 
	 * @Description: 保存凭证
	 * @param headJson		凭证头信息
	 * @param lineArray		分录行信息
	 * @throws Exception    设定文件
	 */
	public void saveVoucher(String headJson,String lineArray) throws Exception ;
	
	/**
	 * @Title: submitVoucher 
	 * @Description: 提交凭证
	 * @param headJson  	 凭证头信息
	 * @param lineArray		 分录行信息
	 * @param paramJson		辅助参数：{包括：凭证来源、报销单ID、付款单ID和支付方式}
	 * @throws Exception    设定文件
	 */
	public void submitVoucher(String headJson, String lineArray, String paramJson) throws Exception ;
	
}
