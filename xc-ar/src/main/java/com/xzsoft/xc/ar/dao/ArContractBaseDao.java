package com.xzsoft.xc.ar.dao;

import java.util.List;

import com.xzsoft.xc.ar.modal.ArContractBean;

/** 
 * @className ArContractBaseDao
 * @Description 合同dao层
 * @author 韦才文
 * @version 创建时间：2016年7月7日 下午4:06:55 
 */
public interface ArContractBaseDao {
	
	/**
	 * @Title:checExistDel
	 * @Description: 查询是否合同是否存在明细（删除）
	 * 参数格式:
	 * @param  java.util.List
	 * @return java.lang.String
	 * @throws 
	 */
	public String checExistDel(List<String> ids) throws Exception;
	/**
	 * @Title:addContractInfo
	 * @Description: 新增合同
	 * 参数格式:
	 * @param  com.xzsoft.xc.ar.modal.ArContractBean
	 * @return 
	 * @throws 
	 */
	public void addContractInfo(ArContractBean bean) throws Exception;
	/**
	 * @Title:editContractInfo
	 * @Description: 修改合同
	 * 参数格式:
	 * @param  com.xzsoft.xc.ar.modal.ArContractBean
	 * @return 
	 * @throws 
	 */
	public void editContractInfo(ArContractBean bean) throws Exception;
	/**
	 * @Title:delContractInfo
	 * @Description: 删除合同
	 * 参数格式:
	 * @param  java.util.List
	 * @return 
	 * @throws 
	 */
	public void delContractInfo(List<String> ids) throws Exception;
	/**
	 * 
	  * @Title insertArContractHis 方法名
	  * @Description 合同变更
	  * @param bean
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void insertArContractHis(ArContractBean bean) throws Exception;
	/**
	 * 
	  * @Title updateArContractHis 方法名
	  * @Description 更新已经变更的合同历史表 
	  * @param bean
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void updateArContractHis(ArContractBean bean) throws Exception;
	/**
	 * 
	  * @Title getArContractHis 方法名
	  * @Description 查询已经变更的合同对应的历史信息
	  * @param AR_CONTRACT_ID
	  * @return
	  * @throws Exception
	  * @return List<ArContractBean> 返回类型
	 */
	public List<ArContractBean> getArContractHis(String AR_CONTRACT_ID) throws Exception;
	/**
	 * 
	  * @Title getArContractHisByHisId 方法名
	  * @Description 查询已经变更的合同对应的历史信息（根据合同历史ID）
	  * @param AR_CONTRACT_HIS_ID
	  * @return
	  * @throws Exception
	  * @return ArContractBean 返回类型
	 */
	public ArContractBean getArContractHisByHisId(String AR_CONTRACT_HIS_ID) throws Exception;
	/**
	 * 
	  * @Title getArContractById 方法名
	  * @Description 根据合同ID得到当前合同信息
	  * @param AR_CONTRACT_ID
	  * @return
	  * @throws Exception
	  * @return ArContractBean 返回类型
	 */
	public ArContractBean getArContractById(String AR_CONTRACT_ID) throws Exception;
	/**
	 * 
	  * @Title updateArContractStatus 方法名
	  * @Description 更新合同状态为变更中
	  * @param bean
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void updateArContractStatus(ArContractBean bean) throws Exception;
}
