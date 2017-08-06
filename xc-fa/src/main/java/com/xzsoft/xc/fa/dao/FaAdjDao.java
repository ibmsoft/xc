package com.xzsoft.xc.fa.dao;

import java.util.List;

import com.xzsoft.xc.fa.modal.FaAdjH;
import com.xzsoft.xc.fa.modal.FaAdjL;

public interface FaAdjDao {
	
	/**
	 * 
	 * insertFaAdjH:(保存头信息)
	 *
	 * @param faAdjH
	 * @author q p
	 */
	public void insertFaAdjH(FaAdjH faAdjH);
	
	/**
	 * 
	 * updateFaAdjH:(修改头信息)
	 *
	 * @param faAdjH
	 * @author q p
	 */
	public void updateFaAdjH(FaAdjH faAdjH);
	
	/**
	 * 
	 * insertFaAdjL:(保存行信息)
	 *
	 * @param faAdjLs
	 * @author q p
	 */
	public void insertFaAdjL(List<FaAdjL> faAdjLs);
	
	/**
	 * 
	 * updateFaAdjL:(保存行信息)
	 *
	 * @param faAdjLs
	 * @author q p
	 */
	public void updateFaAdjL(List<FaAdjL> faAdjLs);
	
	/**
	 * 
	 * updateFaAddition:(保存修改资产信息)
	 *
	 * @param updateSqlStrs
	 * @author q p
	 */
	public void updateFaAddition(List<FaAdjL> faAdjLs);
	
	/**
	 * 
	 * getCurrentOpenedPeriod:(获取账簿下当前打开的期间编码)
	 *
	 * @param ledgerId 账簿id
	 * @return
	 * @author q p
	 */
	public String getCurrentOpenedPeriod(String ledgerId);
	
	/**
	 * getAllPeriodsByLedger:(查询账簿下所有的期间)
	 *
	 * @param ledgerId
	 * @return
	 * @author q p
	 */
	public List<String> getAllPeriodsByLedger(String ledgerId);
	
	/**
	 * 
	 * getAdjHById:(查询资产调整头)
	 *
	 * @param adjHid 调整头id
	 * @return
	 * @author q p
	 */
	public FaAdjH getAdjHById(String adjHid);
	
	/**
	 * 
	 * getAdjLByHid:(查询资产调整明细)
	 *
	 * @param adjHid 调整明细id
	 * @return
	 * @author q p
	 */
	public List<FaAdjL> getAdjLByHid(String adjHid);
	
	/**
	 * 
	 * updateAdjHStatus:(这里用一句话描述这个方法的作用)
	 *
	 * @param hId
	 * @param status
	 * @author q p
	 */
	public void updateAdjHStatus(String hId, String status);
	
	/**
	 * 
	 * revokeFaAddition:(撤回修改资产信息)
	 *
	 * @param updateSqlStrs
	 * @author q p
	 */
	public void revokeFaAddition(List<FaAdjL> faAdjLs);
	
	/**
	 * 
	 * checkFaHasBeenAdj:(检查资产是否被调整)
	 *
	 * @param adjDate
	 * @param adjType
	 * @param assetId
	 * @return
	 * @author q p
	 */
	public String checkFaHasBeenAdj(String adjDate, String adjType, String assetId, String status);
	
	/**
	 * 
	 * checkFaHasBeenAdjUpdate:(检查资产是否被调整)
	 *
	 * @param adjDate
	 * @param adjType
	 * @param assetId
	 * @param adjDid
	 * @return
	 * @author q p
	 */
	public String checkFaHasBeenAdjUpdate(String adjDate, String adjType, String assetId, String adjDid);
	
	/**
	 * 
	 * delAutoCreateAdjLs:(删除系统自动生成的调整单明细)
	 *
	 * @param adjHid
	 * @author q p
	 */
	public void delAutoCreateAdjLs(String adjHid);
	
	/**
	 * 
	 * getAdjLByHid:(查询资产调整明细)
	 *
	 * @param adjLid 调整明细id
	 * @return
	 * @author q p
	 */
	public FaAdjL getAdjLByLid(String adjLid);
	
	/**
	 * 
	 * checkFaInPeriod:(是否在当前会计期内)
	 *
	 * @param adjHid
	 * @return
	 * @author q p
	 */
	public int checkInPeriod(String adjHid);
	
	/**
	 * 
	 * checkAdj4Before:(检验当前期间是否做过计提后调整)
	 *
	 * @param adjDate
	 * @param assetId
	 * @return
	 * @author q p
	 */
	public int checkAdj4After(String adjDate, String assetId);
}
