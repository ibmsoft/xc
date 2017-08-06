package com.xzsoft.xc.ar.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.ar.dao.ArContractBaseDao;
import com.xzsoft.xc.ar.mapper.ArContractBaseMapper;
import com.xzsoft.xc.ar.modal.ArContractBean;

/** 
 * @className	ArContractBaseDaoImpl
 * @Description 合同管理Dao实现类
 * @author 韦才文
 * @version 创建时间：2016年7月7日 下午4:09:01 
 */
@Repository("arContractBaseDao")
public class ArContractBaseDaoImpl implements ArContractBaseDao {
	@Resource
	ArContractBaseMapper mapper;
	/*
	 * 
	  * <p>Title addContractInfo</p>
	  * <p>Description 新增合同</p>
	  * @param bean
	  * @see com.xzsoft.xc.ar.dao.ArContractBaseDao#addContractInfo(com.xzsoft.xc.ar.modal.ArContractBean)
	 */
	@Override
	public void addContractInfo(ArContractBean bean) {
		mapper.addContractInfo(bean);
	}
	/*
	 * 
	  * <p>Title editContractInfo</p>
	  * <p>Description 修改合同</p>
	  * @param bean
	  * @see com.xzsoft.xc.ar.dao.ArContractBaseDao#editContractInfo(com.xzsoft.xc.ar.modal.ArContractBean)
	 */
	@Override
	public void editContractInfo(ArContractBean bean) {
		mapper.editContractInfo(bean);
	}
	/*
	 * 
	  * <p>Title delContractInfo</p>
	  * <p>Description 删除合同</p>
	  * @param ids
	  * @see com.xzsoft.xc.ar.dao.ArContractBaseDao#delContractInfo(java.util.List)
	 */
	@Override
	public void delContractInfo(List<String> ids) {
		mapper.delContractInfo(ids);
	}
	/*
	 * 
	  * <p>Title checExistDel</p>
	  * <p>Description 查询是否合同是否存在明细（删除）</p>
	  * @param ids
	  * @return
	  * @see com.xzsoft.xc.ar.dao.ArContractBaseDao#checExistDel(java.util.List)
	 */
	@Override
	public String checExistDel(List<String> ids) {
		return mapper.checExistDel(ids);
	}
	/*
	 * 
	  * <p>Title insertArContractHis</p>
	  * <p>Description 合同变更插入历史表</p>
	  * @param bean
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.ArContractBaseDao#insertArContractHis(com.xzsoft.xc.ar.modal.ArContractBean)
	 */
	@Override
	public void insertArContractHis(ArContractBean bean) throws Exception {
		mapper.insertArContractHis(bean);
	}
	/*
	 * 
	  * <p>Title updateArContractHis</p>
	  * <p>Description 更新合同历史表</p>
	  * @param bean
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.ArContractBaseDao#updateArContractHis(com.xzsoft.xc.ar.modal.ArContractBean)
	 */
	@Override
	public void updateArContractHis(ArContractBean bean) throws Exception {
		mapper.updateArContractHis(bean);
	}
	/*
	 * 
	  * <p>Title getArContractHis</p>
	  * <p>Description 查询已经变更的合同对应的历史信息</p>
	  * @param AR_CONTRACT_ID
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.ArContractBaseDao#getArContractHis(java.lang.String)
	 */
	@Override
	public List<ArContractBean> getArContractHis(String AR_CONTRACT_ID) throws Exception {
		return mapper.getArContractHis(AR_CONTRACT_ID);
	}
	/*
	 * 
	  * <p>Title getArContractHisByHisId</p>
	  * <p>Description 查询已经变更的合同对应的历史信息（根据合同历史ID）</p>
	  * @param AR_CONTRACT_HIS_ID
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.ArContractBaseDao#getArContractHisByHisId(java.lang.String)
	 */
	@Override
	public ArContractBean getArContractHisByHisId(String AR_CONTRACT_HIS_ID) throws Exception {
		return mapper.getArContractHisByHisId(AR_CONTRACT_HIS_ID);
	}
	/*
	 * 
	  * <p>Title getArContractById</p>
	  * <p>Description 根据合同ID得到当前合同信息</p>
	  * @param AR_CONTRACT_ID
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.ArContractBaseDao#getArContractById(java.lang.String)
	 */
	@Override
	public ArContractBean getArContractById(String AR_CONTRACT_ID) throws Exception {
		return mapper.getArContractById(AR_CONTRACT_ID);
	}
	@Override
	public void updateArContractStatus(ArContractBean bean) throws Exception {
		mapper.updateArContractStatus(bean);
	}
}