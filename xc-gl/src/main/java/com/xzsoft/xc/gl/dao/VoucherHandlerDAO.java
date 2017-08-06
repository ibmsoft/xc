package com.xzsoft.xc.gl.dao;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.bg.modal.BgFactDTO;
import com.xzsoft.xc.gl.modal.VHead;
import com.xzsoft.xc.gl.modal.VLine;
import com.xzsoft.xc.util.modal.Ledger;

/**
 * @ClassName: VoucherHandlerDAO 
 * @Description: 记账凭证处理数据层接口
 * @author linp
 * @date 2015年12月18日 下午6:12:22 
 *
 */
public interface VoucherHandlerDAO {
	
	/**
	 * @Title: saveVoucher 
	 * @Description: 保存单张凭证信息
	 * @param bean
	 * @param chkType
	 * @throws Exception    设定文件
	 */
	public void saveVoucher(VHead bean,String chkType)throws Exception ; 
	
	/**
	 * @Title: updateVoucher 
	 * @Description: 修改单张凭证信息 
	 * @param bean
	 * @throws Exception    设定文件
	 */
	public void updateVoucher(VHead bean)throws Exception ;
	
	/**
	 * @Title: submitVoucher 
	 * @Description: 提交凭证
	 * @param list
	 * @throws Exception    设定文件
	 */
	public void submitVouchers(List<VHead> list)throws Exception ;
	
	/**
	 * @Title: cancelSubmitVoucher 
	 * @Description: 撤回提交
	 * @param list
	 * @throws Exception    设定文件
	 */
	public void cancelSubmitVouchers(List<String> list)throws Exception ;
	
	/**
	 * @Title: delVoucher 
	 * @Description: 删除凭证信息
	 * @param paramArray
	 * @throws Exception    设定文件
	 */
	public void delVouchers(List<String> list)throws Exception ;
	
	/**
	 * @Title: checkVoucher 
	 * @Description: 审核凭证
	 * @param list
	 * @throws Exception    设定文件
	 */
	public void checkVouchers(List<String> list)throws Exception ;
	
	/**
	 * @Title: uncheckVoucher 
	 * @Description: 取消审核
	 * @param list
	 * @throws Exception    设定文件
	 */
	public void uncheckVouchers(List<String> list)throws Exception ;
	
	/**
	 * @Title: signVoucher 
	 * @Description: 出纳签字
	 * @param list
	 * @throws Exception    设定文件
	 */
	public void signVouchers(List<String> list)throws Exception ;
	
	/**
	 * @Title: unsignVoucher 
	 * @Description: 取消签字
	 * @param list
	 * @throws Exception    设定文件
	 */
	public void unsignVouchers(List<String> list)throws Exception ;
	
	/**
	 * @Title: rejectVoucher 
	 * @Description: 驳回已提交的凭证
	 * @param list
	 * @throws Exception    设定文件
	 */
	public void rejectVouchers(List<String> list)throws Exception ;
	
	/**
	 * @Title: account 
	 * @Description: 凭证记账处理
	 * @param list
	 * @throws Exception    设定文件
	 */
	public void account(List<String> list)throws Exception ;
	
	/**
	 * @Title: cancelAccount 
	 * @Description:  凭证取消记账
	 * @param list
	 * @throws Exception    设定文件
	 */
	public void cancelAccount(List<String> list)throws Exception ;
	
	/**
	 * @Title: getVHead 
	 * @Description: 单凭证查询 
	 * @param headId		凭证头ID
	 * @param isContainLines	是否包含分录行信息
	 * @return
	 * @throws Exception    设定文件
	 */
	public VHead getVHead(String headId, boolean isContainLines)throws Exception ;
	
	/**
	 * @Title: getVLines 
	 * @Description: 查询凭证分录行信息
	 * @param headId
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<VLine> getVLines(String headId)throws Exception ;
	
	/**
	 * @Title: getVHeads 
	 * @Description: 批量查询凭证信息
	 * @param headIds
	 * @param isContainLines
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<VHead> getVHeads(List<String> headIds, boolean isContainLines) throws Exception ;
	
	/**
	 * @Title: getbudgetConditions 
	 * @Description: 获取预算检查条件信息
	 * @param headId
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<BgFactDTO> getbudgetConditions(String headId) throws Exception ;
	
	/**
	 * @Title: isSameVoucher 
	 * @Description: 判断本次导入的数据是否为同一凭证
	 * @param sessionId
	 * @return
	 * @throws Exception    设定文件
	 */
	public int isSameVoucher(String sessionId) throws Exception ;
	
	/**
	 * @Title: delTmpVoucher 
	 * @Description: 删除临时表的凭证信息
	 * @param sessionId
	 * @throws Exception    设定文件
	 */
	public void delTmpVoucher(String sessionId) throws Exception ;
	
	/**
	 * @Title: updateTemplateType 
	 * @Description: 处理凭证格式
	 * @param sessionId
	 * @param ledger
	 * @throws Exception    设定文件
	 */
	public int updateTemplateType(String sessionId,Ledger ledger) throws Exception ;
	
	/**
	 * @Title: generateJSONObject 
	 * @Description: 生成符合凭证格式的JSONObject
	 * @param sessionId
	 * @return
	 * @throws Exception    设定文件
	 */
	public VHead getTmpVoucher(String sessionId) throws Exception ;
	
	/**
	 * @Title: getExpVoucher 
	 * @Description: 导出凭证信息
	 * @param headId
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<HashMap<String,Object>> getExpVoucher(String headId) throws Exception ;
	
	/**
	 * @Title: getBgCHKVoucher 
	 * @Description: 查询凭证预算校验信息
	 * @param headId
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<HashMap<String,Object>> getBgCHKVoucher(String headId) throws Exception ;

}
