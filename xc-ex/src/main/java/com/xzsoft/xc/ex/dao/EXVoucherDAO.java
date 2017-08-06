package com.xzsoft.xc.ex.dao;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.ex.modal.EXDocDtlBean;
import com.xzsoft.xc.ex.modal.PayBankModeBean;
import com.xzsoft.xc.ex.modal.PayModeBean;

/**
 * @ClassName: EXVoucherDAO 
 * @Description: 单据复核、单据支付的凭证处理数据层接口
 * @author linp
 * @date 2016年3月10日 下午4:55:31 
 *
 */
public interface EXVoucherDAO {

	/**
	 * @Title: getPayMode 
	 * @Description: 查找收付款方式 
	 * @param modeId
	 * @param ledgerId
	 * @return
	 * @throws Exception    设定文件
	 */
	public PayModeBean getPayMode(String modeId,String ledgerId) throws Exception ;
	
	/**
	 * @Title: getPayBank 
	 * @Description: 查找账簿开户行信息
	 * @param depositBankId
	 * @throws Exception    设定文件
	 */
	public PayBankModeBean getPayBank(String depositBankId) throws Exception ;
	
	/**
	 * @Title: getExItemAccs 
	 * @Description: 查询费用项目对应的科目信息
	 * @param docDtls
	 * @param ledgerId
	 * @param docId
	 * @return
	 * @throws Exception    设定文件
	 */
	public HashMap<String,Object> getExItemAccs(List<EXDocDtlBean> docDtls,String ledgerId,String docId) throws Exception ;
}
