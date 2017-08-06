package com.xzsoft.xc.gl.mapper;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.gl.modal.Period;

/**
 * @ClassName: SumBalanceMapper 
 * @Description: 凭证余额汇总处理
 * @author linp
 * @date 2016年1月7日 下午6:04:34 
 *
 */
public interface SumBalanceMapper {

	/**
	 * @Title: getAfterPeriods 
	 * @Description: 获取当前期间向下的期间
	 * @param map
	 * @return
	 */
	public List<Period> getAfterPeriods(HashMap<String,String> map) ;
	
	/**
	 * @Title: getAfterPeriods4CreateAccount 
	 * @Description: 建账时获取当前期间向下的期间
	 * @param map
	 * @return    设定文件
	 */
	public List<Period> getAfterPeriods4CreateAccount(HashMap<String,String> map) ; 
	
	/**
	 * @Title: getCurrency4Ledger 
	 * @Description: 获取账簿的本位币
	 * @param headId
	 * @return    设定文件
	 */
	public String getCurrency4Ledger(String headId) ;
	
	/**
	 * @Title: getCurrency4Acc 
	 * @Description: 获取科目的默认币种
	 * @param map
	 * @return    设定文件
	 */
	public String getCurrency4Acc(HashMap<String,String> map) ;
	
	/**
	 * @Title: getHrcyIdByHeadId 
	 * @Description: 根据凭证头ID查询科目体系ID
	 * @param headId
	 * @return
	 */
	public String getHrcyIdByHeadId(String headId) ;
	
	/**
	 * @Title: isEnabeAdj 
	 * @Description: 是否启用了第四季度调整期
	 * @param year
	 * @return    设定文件
	 */
	public String isEnabeQ4Adj(HashMap<String,String> map) ;
	
	/**
	 * @Title: getStartPeriod 
	 * @Description: 获取账簿的开启期间
	 * @param ledgerId
	 * @return    设定文件
	 */
	public String getStartPeriod(String ledgerId) ;
	
	/**
	 * @Title: getPeriodEndDate 
	 * @Description: 获取当前会计期的结束日期
	 * @param periodCode
	 * @return    设定文件
	 */
	public String getPeriodEndDate(String periodCode) ;
	
	/**
	 * @Title: getAllSumAccounts 
	 * @Description: 获取凭证中待汇总的科目信息
	 * @param headId
	 * @return
	 */
	public List<HashMap<String,String>> getAllSumAccounts(HashMap<String,String> map) ;
	
	/**
	 * @Title: getAllCashItems 
	 * @Description: 获取现金流量项
	 * @param map
	 * @return    设定文件
	 */
	public List<HashMap<String,String>> getAllCashItems(HashMap<String,String> map) ;
	
	/**
	 * @Title: delAccBalance 
	 * @Description: 删除科目余额数据
	 * @param map    设定文件
	 */
	public void delAccBalance(HashMap<String,String> map) ;
	
	/**
	 * @Title: calB 
	 * @Description: 计算期初借方和贷方余额
	 * @param map
	 * @return    设定文件
	 */
	public HashMap<String,Object> calB(HashMap<String,String> map) ;
	
	/**
	 * @Title: calPTD 
	 * @Description: 计算本期借方和贷方发生额
	 * @param map    设定文件
	 */
	public HashMap<String,Object> calPTD(HashMap<String,String> map) ;
	
	/**
	 * @Title: calY 
	 * @Description: 计算年初借方和贷方余额 
	 * @param map
	 * @return    设定文件
	 */
	public HashMap<String,Object> calY(HashMap<String,String> map) ;
	
	/**
	 * @Title: calYTD 
	 * @Description: 计算本年借方和贷方发生额
	 * @param map
	 * @return    设定文件
	 */
	public HashMap<String,Object> calYTD(HashMap<String,String> map) ;
	
	/**
	 * @Title: insBalances 
	 * @Description:保存总账余额
	 * @param map    设定文件
	 */
	public void insBalances(HashMap<String,Object> map) ;
	
	/**
	 * @Title: updBalances 
	 * @Description: 修改总账余额
	 * @param map    设定文件
	 */
	public void updBalances(HashMap<String,Object> map) ;
	
	/**
	 * @Title: calPTD 
	 * @Description: 计算现金流量项目本期发生数
	 * @param map    设定文件
	 */
	public HashMap<String,Object> calPTD4Cash(HashMap<String,String> map) ;
	
	/**
	 * @Title: insCashBalances 
	 * @Description: 保存现金流量项统计余额(凭证提交或过账)
	 * @param map    设定文件
	 */
	public void insCashBalances(HashMap<String,Object> map) ;
	
	/**
	 * @Title: insInitCashBalances 
	 * @Description: 保存期初现金流量统计余额
	 * @param map    设定文件
	 */
	public void insInitCashBalances(HashMap<String,Object> map) ;
	
	/**
	 * @Title: updCashBalances 
	 * @Description: 修改现金流量项统计余额(凭证过账)
	 * @param map    设定文件
	 */
	public void updCashBalances(HashMap<String,Object> map) ;
	
	/**
	 * @Title: updInitCashBalances 
	 * @Description: 更新期初现金流量统计余额
	 * @param map    设定文件
	 */
	public void updInitCashBalances(HashMap<String,Object> map) ;
	
	/**
	 * @Title: delCashBalances 
	 * @Description: 删除现金流量统计数据
	 * @param map    设定文件
	 */
	public void delCashBalances(HashMap<String,String> map) ;
	
	/**
	 * @Title: delInitCash 
	 * @Description: 删除账簿期初现金流量统计数据
	 * @param map    设定文件
	 */
	public void delInitCash(HashMap<String,String> map) ;
	
	/**
	 * @Title: copyTmpInitCash 
	 * @Description: 拷贝临时表中的期初现金流量数据
	 * @param copyMap    设定文件
	 */
	public void copyTmpInitCash(HashMap<String,Object> copyMap) ;
	
	/**
	 * @Title: getInitCash 
	 * @Description: 查询科目及现金流量项目
	 * @param map    设定文件
	 */
	public List<HashMap<String,Object>> getInitCash(HashMap<String,String> map) ;
	
	/**
	 * @Title: getInitCashByAcc 
	 * @Description: 按科目统计现金流项目 
	 * @param map
	 * @return    设定文件
	 */
	public HashMap<String,Object> getInitCashByAcc(HashMap<String,Object> map) ;
	
	/**
	 * @Title: handlerSumFlag 
	 * @Description: 处理凭证汇总标志及凭证号
	 * @param map    设定文件
	 */
	public void handlerSumFlag(HashMap<String,Object> map) ;
	
	/**
	 * @Title: updateTmpCashBalStatus 
	 * @Description: 更新临时表数据状态信息
	 * @param map    设定文件
	 */
	public void updateTmpCashBalStatus(HashMap<String,Object> map) ;
	
}
