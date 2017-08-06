package com.xzsoft.xc.gl.dao;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.gl.modal.Period;

/**
 * @ClassName: SumBalanceServiceDAO 
 * @Description: 余额汇总处理数据层接口
 * @author linp
 * @date 2016年1月7日 上午10:48:11 
 *
 */
public interface XCGLSumBalanceDAO {
	
	/**
	 * @Title: getSumAccounts 
	 * @Description: 凭证下待汇总的科目信息
	 * @param headId
	 * @param isContainForeign
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<HashMap<String,String>> getSumAccounts(String headId,String isContainForeign,String isAccount) throws Exception ;
	
	/**
	 * @Title: getAfterPeriods 
	 * @Description: 获取当前期间向下的期间
	 * @param ledgerId
	 * @param periodCode
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<Period> getAfterPeriods(String ledgerId,String periodCode)throws Exception ;
	
	/**
	 * @Title: getCurrency4Ledger 
	 * @Description: 获取账簿的本位币
	 * @param headId
	 * @return
	 * @throws Exception    设定文件
	 */
	public String getCurrency4Ledger(String headId) throws Exception ;
	
	/**
	 * @Title: getCurrency4Acc 
	 * @Description: 获取科目的计量币种
	 * @param accId
	 * @param ledgerId
	 * @return
	 * @throws Exception    设定文件
	 */
	public String getCurrency4Acc(String accId,String ledgerId)throws Exception ;
	
	/**
	 * @Title: getHrcyIdByHeadId 
	 * @Description: 根据凭证头ID查询科目体系ID
	 * @param headId
	 * @return
	 * @throws Exception
	 */
	public String getHrcyIdByHeadId(String headId) throws Exception ;

	/**
	 * @Title: getPrePeriodCode 
	 * @Description: 计算上期期间 
	 * @param periodCode
	 * @return
	 * @throws Exception    设定文件
	 */
	public String getPrePeriodCode(String periodCode)throws Exception ;

	/**
	 * @Title: sumAccDataByPeirod 
	 * @Description: 按期间汇总科目余额
	 * @param map
	 * @throws Exception    设定文件
	 */
	public void sumAccDataByPeriod(HashMap<String,String> map,List<Period> periods) throws Exception ;
	
	/**
	 * @Title: sumCashDataByPeirod 
	 * @Description: 统计现金流量项目
	 * @param map
	 * @param periods
	 * @throws Exception    设定文件
	 */
	public void sumCashDataByPeirod(HashMap<String,String> map) throws Exception ;
	
	/**
	 * @Title: handlerSumFlagAndSerialNo 
	 * @Description: 修改凭证汇总标志及凭证号
	 * @param headId
	 * @param userId
	 * @param isCanceled
	 * @throws Exception    设定文件
	 */
	public void handlerSumFlag(String headId,String userId,String isCanceled)throws Exception ;

	/**
	 * @Title: sumInitCashByCALevel 
	 * @Description:  期初现金流量余额统计
	 * @param ledgerId
	 * @throws Exception    设定文件
	 */
	public void sumInitCashByCALevel(String ledgerId)throws Exception ;
	
	/**
	 * @Title: delInitCash 
	 * @Description: 删除期初现金流量项目
	 * @param ledgerId
	 * @param periodCode
	 * @throws Exception    设定文件
	 */
	public void delInitCash(String ledgerId,String periodCode)throws Exception ;

	/**
	 * @Title: createAccount 
	 * @Description: 账簿建账
	 * @param map
	 * @throws Exception    设定文件
	 */
	public void createAccount(HashMap<String,String> map)throws Exception ;
	
	/**
	 * @Title: cancelAccount 
	 * @Description: 取消建账
	 * @param map
	 * @throws Exception    设定文件
	 */
	public void cancelAccount(HashMap<String,String> map)throws Exception ;
	
	/**
	 * @Title: updateCashBalStatus 
	 * @Description: 更新现金流量临时表数据状态信息
	 * @param ledgerId
	 * @param isVaild
	 * @throws Exception    设定文件
	 */
	public void updateCashBalStatus(String ledgerId,String isVaild)throws Exception ;
	
}
