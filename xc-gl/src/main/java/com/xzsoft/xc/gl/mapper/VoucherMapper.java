package com.xzsoft.xc.gl.mapper;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.bg.modal.BgFactDTO;
import com.xzsoft.xc.gl.modal.CCID;
import com.xzsoft.xc.gl.modal.VHead;
import com.xzsoft.xc.gl.modal.VLine;

/**
 * 
 * @ClassName: VoucherMapper 
 * @Description: 凭证处理Mapper
 * @author linp
 * @date 2015年12月18日 下午11:17:06 
 *
 */
public interface VoucherMapper {
	
	/**
	 * @Title: createCCID 
	 * @Description: 创建科目CCID
	 * @param ccid
	 * @return
	 */
	public void createCCID(CCID ccid) ;
	
	/**
	 * @Title: getVHead 
	 * @Description: 根据凭证头ID列表, 批量查询凭证头信息
	 * @param list
	 * @return    设定文件
	 */
	public List<VHead> getVHeads(List<String> list) ;
	
	/**
	 * @Title: getVoucher 
	 * @Description: 查询单张凭证头信息
	 * @param headId    设定文件
	 */
	public VHead getVHead(String headId) ;
	
	/**
	 * @Title: getVLines 
	 * @Description: 查询单张凭证分录行信息
	 * @param headId    设定文件
	 */
	public List<VLine> getVLines(String headId) ;
	
	/**
	 * @Title: saveVHead 
	 * @Description: 保存单张凭证头信息
	 * @param bean    设定文件
	 */
	public void saveVHead(VHead bean) ;
	
	/**
	 * @Title: updateVHead 
	 * @Description: 修改单张凭证头信息
	 * @param bean    设定文件
	 */
	public void updateVHead(VHead bean) ;
	
	/**
	 * @Title: delVHead 
	 * @Description: 删除单张凭头信息
	 * @param headId    设定文件
	 */
	public void delVHead(String headId) ;
	
	/**
	 * @Title: delVHead 
	 * @Description: 根据凭证头ID列表, 批量删除凭头信息
	 * @param headIds    设定文件
	 */
	public void delVHeads(List<String> headIds) ;
	
	/**
	 * @Title: saveVLine 
	 * @Description: 保存单条凭证分录行信息
	 * @param bean    设定文件
	 */
	public void saveVLine(VLine bean) ;
	
	/**
	 * @Title: saveVLines 
	 * @Description: 批量保存凭证分录行信息
	 * @param map    设定文件
	 */
	public void saveVLines(HashMap<String,Object> map) ;
	
	/**
	 * @Title: updateVLine 
	 * @Description: 更新单条凭证分录行
	 * @param bean    设定文件
	 */
	public void updateVLine(VLine bean) ;
	
	/**
	 * @Title: updateVLines 
	 * @Description: 批量更新单条凭证分录行
	 * @param map    设定文件
	 */
	public void updateVLines(HashMap<String,Object> map) ;
	
	/**
	 * @Title: delVLine 
	 * @Description: 按分录行ID, 批量删除分录信息
	 * @param lineIds    设定文件
	 */
	public void delVLineByLineId(List<String> lineIds) ;
	
	/**
	 * @Title: delVLineByHeadId 
	 * @Description: 删除单张凭证分录信息
	 * @param headId    设定文件
	 */
	public void delVLineByHeadId(String headId) ;
	
	/**
	 * @Title: delVLineByHeadId 
	 * @Description: 按凭证头ID列表, 批量删除分录信息
	 * @param headId    设定文件
	 */
	public void delVLineByHeadIds(List<String> headIds) ;
	
	/**
	 * @Title: submitVouchers 
	 * @Description: 提交凭证
	 * @param paramMap    设定文件
	 */
	public void submitVouchers(VHead head) ;
	
	/**
	 * @Title: cancelSubmitVouchers 
	 * @Description: 撤回凭证提交
	 * @param paramMap    设定文件
	 */
	public void cancelSubmitVouchers(HashMap<String,Object> paramMap) ;
	
	/**
	 * @Title: recordVJumpNum 
	 * @Description: 记录凭证断号信息
	 * @param paramMap    设定文件
	 */
	public void recordVJumpNum(HashMap<String,Object> paramMap) ;
	
	/**
	 * @Title: deVJumpNum 
	 * @Description: 删除凭证断号信息
	 * @param paramMap    设定文件
	 */
	public void deVJumpNum(HashMap<String,String> paramMap) ;
	
	/**
	 * @Title: cancelWriteOffVouchers 
	 * @Description: 取消与被冲销凭证的关联关系
	 * @param paramMap    设定文件
	 */
	public void cancelWriteOffVouchers(HashMap<String,Object> paramMap) ;
	
	/**
	 * @Title: recordWriteOffVouchers 
	 * @Description: 记录凭证被冲销
	 * @param paramMap    设定文件
	 */
	public void recordWriteOffVouchers(HashMap<String,Object> paramMap) ;
	
	/**
	 * @Title: checkVouchers 
	 * @Description: 凭证审核
	 * @param paramMap    设定文件
	 */
	public void checkVouchers(HashMap<String,Object> paramMap) ;
	
	/**
	 * @Title: uncheckVouchers 
	 * @Description: 取消凭证审核
	 * @param paramMap    设定文件
	 */
	public void uncheckVouchers(HashMap<String,Object> paramMap) ;
	
	/**
	 * @Title: signVouchers 
	 * @Description: 出纳签字
	 * @param paramMap    设定文件
	 */
	public void signVouchers(HashMap<String,Object> paramMap) ;
	
	/**
	 * @Title: unsignVouchers 
	 * @Description: 取消签字
	 * @param paramMap    设定文件
	 */
	public void unsignVouchers(HashMap<String,Object> paramMap) ;
	
	/**
	 * @Title: rejectVouchers 
	 * @Description: 驳回凭证
	 * @param paramMap    设定文件
	 */
	public void rejectVouchers(HashMap<String,Object> paramMap) ;
	
	/**
	 * @Title: account 
	 * @Description: 凭证记账处理 
	 * @param paramMap    设定文件
	 */
	public void account(HashMap<String,Object> paramMap) ;
	
	/**
	 * @Title: cancelAccount 
	 * @Description: 凭证取消记账
	 * @param paramMap    设定文件
	 */
	public void cancelAccount(HashMap<String,Object> paramMap) ;
	
	/**
	 * @Title: isUsedSerialNum 
	 * @Description: 凭证编号是否已经使用 
	 * @param paramMap
	 * @return    设定文件
	 */
	public VHead isUsedSerialNum(HashMap<String,String> paramMap) ;
	
	/**
	 * @Title: isWriteOffVoucher 
	 * @Description: 凭证是否已冲销
	 * @param writeOffNum
	 * @return    设定文件
	 */
	public VHead isWriteOffVoucher(String writeOffNum) ;
	
	/**
	 * @Title: isSubmitedVoucher 
	 * @Description: 凭证是否已经提交
	 * @param headId
	 * @return    设定文件
	 */
	public VHead isSubmitedVoucher(String headId) ;
	
	/**
	 * @Title: getbudgetConditions 
	 * @Description: 获取预算检查条件信息
	 * @param headId
	 * @return    设定文件
	 */
	public List<BgFactDTO> getbudgetConditions(String headId) ;
	
	/**
	 * @Title: isOpenPeriod 
	 * @Description: 判断账簿期间是否为打开状态
	 * @param list
	 * @return    设定文件
	 */
	public List<String> isOpenPeriod(List<String> list) ;
	
}
