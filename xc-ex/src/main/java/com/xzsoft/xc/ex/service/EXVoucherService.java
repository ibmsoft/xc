package com.xzsoft.xc.ex.service;

import java.util.List;

import com.xzsoft.xc.ex.modal.CheckAndPayDocDTO;

/**
 * @ClassName: EXVoucherService 
 * @Description: 单据复核、单据支付的凭证处理接口
 * @author linp
 * @date 2016年3月10日 下午4:54:52 
 *
 */
public interface EXVoucherService {
	
	/**
	 * @Title: newVoucher 
	 * @Description: 单据复核生成凭证处理
	 * <p>
	 * 	根据单个借款单、还款单、费用报销单和差旅报销单生成凭证
	 *  此时单据对应的凭证状态为已生成
	 * </p>
	 * @param accDate	: 凭证制单日期
	 * @param payMethod	: 支付方式：1-立即支付，2-挂账支付
	 * @param payType	：  付款方式
	 * @param bankId	: 付款账号
	 * @param caId		: 现金流量项目ID
	 * @param exDocId	: 报销单ID
	 * @throws Exception    设定文件
	 */
	public String newVoucher(CheckAndPayDocDTO dto) throws Exception ;
	
	/**
	 * @Title: newPayVoucher 
	 * @Description: 单据支付生成凭证处理
	 * <p>
	 * 	根据多个借款单、还款单、费用报销单和差旅报销单已挂账的单据生成凭证（此时多个单据必须是同种单据类型）
	 * 此时单据对应的凭证状态为已生成
	 * </p>
	 * @param accDate	: 凭证制单日期
	 * @param summary	: 摘要(付款说明)
	 * @param exPayId	：  支付单号
	 * @param payType	：  付款方式
	 * @param bankId	: 付款账号
	 * @param caId		: 现金流量项目ID
	 * @param headId	: 凭证头ID[可选]
	 * @param exDocIds  : 报销单列表
	 * @throws Exception    设定文件
	 */
	public String newPayVoucher(CheckAndPayDocDTO dto) throws Exception ;
	
	/**
	 * @Title: delAndSaveVoucher 
	 * @Description: 删除并保存凭证信息
	 * @param @param headId
	 * @param @param jsonObject
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return String    返回类型 
	 * @throws
	 */
	public String delAndSaveVoucher(String headId ,String jsonObject) throws Exception ;
	
	/**
	 * @Title: cancelVoucher 
	 * @Description: 取消复核
	 * <p>
	 * 	取消复核相当于取消审核、撤回提交, 使凭证为草稿状态
	 * 此时凭证状态为已取消
	 * </p>
	 * @param headIds
	 * @throws Exception    设定文件
	 */
	public String cancelVoucher(List<String> headIds) throws Exception ;

}
