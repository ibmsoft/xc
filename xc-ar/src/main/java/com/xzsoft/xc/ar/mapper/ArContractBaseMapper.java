package com.xzsoft.xc.ar.mapper;

import java.util.List;

import com.xzsoft.xc.ar.modal.ArContractBean;

/** 
 * @className ArContractBaseMapper
 * @Description 合同管理mapper层
 * @author 韦才文
 * @version 创建时间：2016年7月7日 下午3:47:32 
 */
public interface ArContractBaseMapper {
	/**
	 * @Title:checExistDel
	 * @Description: 查询是否合同是否存在明细（删除）
	 * 参数格式:
	 * @param  java.util.List
	 * @return java.lang.String
	 * @throws 
	 */
	public String checExistDel(List<String> ids);
	/**
	 * @Title:addContractInfo
	 * @Description: 新增合同
	 * 参数格式:
	 * @param  com.xzsoft.xc.ar.modal.ArContractBean
	 * @return 
	 * @throws 
	 */
	public void addContractInfo(ArContractBean bean);
	/**
	 * @Title:editContractInfo
	 * @Description: 修改合同
	 * 参数格式:
	 * @param  com.xzsoft.xc.ar.modal.ArContractBean
	 * @return 
	 * @throws 
	 */
	public void editContractInfo(ArContractBean bean);
	/**
	 * @Title:delContractInfo
	 * @Description: 删除合同
	 * 参数格式:
	 * @param  java.util.List
	 * @return 
	 * @throws 
	 */
	public void delContractInfo(List<String> ids);
	/**
	 * 
	  * @Title insertArContractHis 方法名
	  * @Description 合同变更
	  * @param bean
	  * @return void 返回类型
	 */
	public void insertArContractHis(ArContractBean bean);
	/**
	 * 
	  * @Title updateArContractHis 方法名
	  * @Description 更新已经变更的合同历史表
	  * @param bean
	  * @return void 返回类型
	 */
	public void updateArContractHis(ArContractBean bean);
	/**
	 * 
	  * @Title getArContractHis 方法名
	  * @Description 查询已经变更的合同对应的历史信息
	  * @param AR_CONTRACT_ID
	  * @return
	  * @return List<ArContractBean> 返回类型
	 */
	public List<ArContractBean> getArContractHis(String AR_CONTRACT_ID);
	/**
	 * 
	  * @Title getArContractHisByHisId 方法名
	  * @Description 查询已经变更的合同对应的历史信息（根据合同历史ID）
	  * @param AR_CONTRACT_HIS_ID
	  * @return
	  * @return ArContractBean 返回类型
	 */
	public ArContractBean getArContractHisByHisId(String AR_CONTRACT_HIS_ID);
	/**
	 * 
	  * @Title getArContractById 方法名
	  * @Description 根据合同ID得到当前合同信息
	  * @param AR_CONTRACT_ID
	  * @return
	  * @return ArContractBean 返回类型
	 */
	public ArContractBean getArContractById(String AR_CONTRACT_ID);
	/**
	 * 
	  * @Title updateArContractStatus 方法名
	  * @Description 更新合同状态为变更中
	  * @param bean
	  * @return void 返回类型
	 */
	public void updateArContractStatus(ArContractBean bean);
}
