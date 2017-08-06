package com.xzsoft.xc.apar.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.apar.dao.ApArTransDao;
import com.xzsoft.xc.apar.mapper.ApArTransMapper;
import com.xzsoft.xc.apar.modal.ApInvTransBean;
import com.xzsoft.xc.apar.modal.ArInvTransBean;
import com.xzsoft.xip.platform.util.PlatformUtil;
/**
 * 
  * @ClassName：ApArTransDaoImpl
  * @Description：应付和应收模块交易明细表处理
  * @author：RenJianJian
  * @date：2016年9月29日 上午9:39:14
 */
@Repository("apArTransDao")
public class ApArTransDaoImpl implements ApArTransDao {
	@Resource
	ApArTransMapper mapper;
	/*
	 * 
	 * <p>Title: getApInvTransByGlId</p>
	 * <p>Description: 根据应付单ID查询应付模块交易明细表信息</p>
	 * @param AP_INV_GL_H_ID
	 * @return
	 * @throws Exception
	 * @see com.xzsoft.xc.apar.dao.ApArTransDao#getApInvTransByGlId(java.lang.String)
	 */
	@Override
	public List<ApInvTransBean> getApInvTransByGlId(String AP_INV_GL_H_ID) throws Exception {
		List<ApInvTransBean> apInvTransList = new ArrayList<ApInvTransBean>();
		try {
			apInvTransList = mapper.getApInvTransByGlId(AP_INV_GL_H_ID);
		} catch (Exception e) {
			throw e;
		}
		return apInvTransList;
	}
	/*
	 * (non-Javadoc)
	 * <p>Title: insertApTrans</p> 
	 * <p>Description: 批量新增交易明细-应付模块</p> 
	 * @param apInvTransBeanList
	 * @throws Exception 
	 * @see com.xzsoft.xc.apar.dao.ApArTransDao#insertApTrans(java.util.List)
	 */
	@Override
	public void insertApTrans(List<ApInvTransBean> list) throws Exception {
		if (list!=null && list.size()>0) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", list);
			mapper.insertApTrans(map);
		}
	}
	/*
	 * (non-Javadoc)
	 * <p>Title: updateApTrans</p> 
	 * <p>Description: 批量修改交易明细-应付</p> 
	 * @param list
	 * @throws Exception 
	 * @see com.xzsoft.xc.apar.dao.ApArTransDao#updateApTrans(java.util.List)
	 */
	@Override
	public void updateApTrans(List<ApInvTransBean> list) throws Exception{
		if (list != null && list.size() > 0) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", list);
			mapper.updateApTrans(map);
		} 
	}
	/*
	 * (non-Javadoc)
	 * <p>Title: deleteApTrans</p> 
	 * <p>Description: 批量删除交易明细-应付</p> 
	 * @param list
	 * @throws Exception 
	 * @see com.xzsoft.xc.apar.dao.ApArTransDao#deleteApTrans(java.util.List)
	 */
	@Override
	public void deleteApTrans(List<ApInvTransBean> list) throws Exception{
		if(list != null && list.size() > 0){
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", list);
			mapper.deleteApTrans(map);
		}
	}
	/*
	 * (non-Javadoc)
	 * <p>Title: deleteApTrans2</p> 
	 * <p>Description: 批量删除交易明细-应付(根据状态)</p> 
	 * @param list
	 * @throws Exception 
	 * @see com.xzsoft.xc.apar.dao.ApArTransDao#deleteApTrans2(java.util.List)
	 */
	@Override
	public void deleteApTrans2(List<ApInvTransBean> list) throws Exception{
		if(list != null && list.size() > 0){
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", list);
			mapper.deleteApTrans2(map);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * <p>Title: getArInvTransByGlId</p> 
	 * <p>Description: 查询交易明细表（根据应收单ID）-应收</p> 
	 * @param AR_INV_GL_H_ID
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.apar.dao.ApArTransDao#getArInvTransByGlId(java.lang.String)
	 */
	@Override
	public List<ArInvTransBean> getArInvTransByGlId(String AR_INV_GL_H_ID) throws Exception{
		return mapper.getArInvTransByGlId(AR_INV_GL_H_ID);
	}
	/*
	 * (non-Javadoc)
	 * <p>Title: insertArTrans</p> 
	 * <p>Description: 新增交易明细-应收</p> 
	 * @param list
	 * @throws Exception 
	 * @see com.xzsoft.xc.apar.dao.ApArTransDao#insertArTrans(java.util.List)
	 */
	@Override
	public void insertArTrans(List<ArInvTransBean> list) throws Exception {
		if (list != null && list.size() > 0) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", list);
			mapper.insertArTrans(map);
		}
		
	}
	/*
	 * (non-Javadoc)
	 * <p>Title: updateArTrans</p> 
	 * <p>Description: 修改交易明细-应收</p> 
	 * @param list
	 * @throws Exception 
	 * @see com.xzsoft.xc.apar.dao.ApArTransDao#updateArTrans(java.util.List)
	 */
	@Override
	public void updateArTrans(List<ArInvTransBean> list) throws Exception {
		if (list != null && list.size() > 0) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", list);
			mapper.updateArTrans(map);
		}
		
	}
	/*
	 * (non-Javadoc)
	 * <p>Title: deleteArTrans</p> 
	 * <p>Description: 删除交易明细-应收</p> 
	 * @param list
	 * @throws Exception 
	 * @see com.xzsoft.xc.apar.dao.ApArTransDao#deleteArTrans(java.util.List)
	 */
	@Override
	public void deleteArTrans(List<ArInvTransBean> list) throws Exception {
		if (list != null && list.size() > 0) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", list);
			mapper.deleteArTrans(map);
		}
		
	}
	/*
	 * (non-Javadoc)
	 * <p>Title: updateArTransByCancel</p> 
	 * <p>Description: 修改交易明细-应收(核销单)</p> 
	 * @param list
	 * @throws Exception 
	 * @see com.xzsoft.xc.apar.dao.ApArTransDao#updateArTransByCancel(java.util.List)
	 */
	@Override
	public void updateArTransByCancel(List<ArInvTransBean> list) throws Exception {
		if (list != null && list.size() > 0) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", list);
			mapper.updateArTransByCancel(map);
		}
		
	}
	/*
	 * (non-Javadoc)
	 * <p>Title: updateArTransByDocH</p> 
	 * <p>Description: 修改应收交易明细（当单据主信息发生修改而行信息没有发生修改时，也可以修改交易明细的业务日期和摘要）</p> 
	 * @param arInvTransBean
	 * @throws Exception 
	 * @see com.xzsoft.xc.apar.dao.ApArTransDao#updateArTransByDocH(com.xzsoft.xc.apar.modal.ArInvTransBean)
	 */
	@Override
	public void updateArTransByDocH(ArInvTransBean arInvTransBean) throws Exception {
		if (arInvTransBean != null) {
			mapper.updateArTransByDocH(arInvTransBean);
		}
		
	}
}
