package com.xzsoft.xc.gl.mapper;

import java.util.HashMap;
import java.util.List;

/**
 * @ClassName: SumInitBalanceMapper 
 * @Description: 建账与取消建账
 * @author linp
 * @date 2016年1月27日 下午4:21:55 
 *
 */
public interface SumInitBalanceMapper {

	/**
	 * @Title: impInitAccBalances 
	 * @Description: 将临时表中本位币和原币数据导入正式表
	 * @param map    设定文件
	 */
	public void impInitAccBalances(HashMap<String,Object> map) ;
	
	/**
	 * @Title: delInitAccBalances 
	 * @Description: 删除正式表中科目余额数据
	 * @param map    设定文件
	 */
	public void delInitAccBalances(HashMap<String,String> map) ;
	
	/**
	 * @Title: getSumAccounts 
	 * @Description: 查询待汇总的科目信息 
	 * @param map
	 * @return    设定文件
	 */
	public List<HashMap<String,String>> getSumAccounts(HashMap<String,String> map) ;
	
	/**
	 * @Title: sumB 
	 * @Description: 计算期初借方和贷方余额
	 * @param map
	 * @return    设定文件
	 */
	public HashMap<String,Object> sumB(HashMap<String,String> map) ;
	
	/**
	 * @Title: sumPTD 
	 * @Description: 计算本期借方和贷方发生额
	 * @param map
	 * @return    设定文件
	 */
	public HashMap<String,Object> sumPTD(HashMap<String,String> map) ;
	
	/**
	 * @Title: sumY 
	 * @Description: 计算年初借方和贷方余额
	 * @param map
	 * @return    设定文件
	 */
	public HashMap<String,Object> sumY(HashMap<String,String> map) ;
	
	/**
	 * @Title: sumYTD 
	 * @Description: 计算本年借方和贷方发生额
	 * @param map
	 * @return    设定文件
	 */
	public HashMap<String,Object> sumYTD(HashMap<String,String> map) ;
	
	/**
	 * @Title: sumPJTD 
	 * @Description: 计算期末借方和贷方余额
	 * @param map
	 * @return    设定文件
	 */
	public HashMap<String,Object> sumPJTD(HashMap<String,String> map) ;
	
	/**
	 * @Title: insInitAccBalances 
	 * @Description: 保存科目余额
	 * @param map    设定文件
	 */
	public void insInitAccBalances(HashMap<String,Object> map) ;
	
	/**
	 * @Title: updInitAccBalances 
	 * @Description: 修改科目余额
	 * @param map    设定文件
	 */
	public void updInitAccBalances(HashMap<String,Object> map) ;
	
	/**
	 * @Title: delInitAccBalancesByZero 
	 * @Description: 删除余额为0的数据
	 * @param map    设定文件
	 */
	public void delInitAccBalancesByZero(HashMap<String,Object> map) ;
	
	/**
	 * @Title: updateTmpAccBalancesStatus 
	 * @Description: 更改科目余额数据状态
	 * @param ledgerId    设定文件
	 */
	public void updateTmpAccBalancesStatus(HashMap<String,String> map) ;
	
}
