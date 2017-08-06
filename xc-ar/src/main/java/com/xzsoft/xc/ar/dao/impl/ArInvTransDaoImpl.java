/**
 * @Title:ArInvTransDaoImpl.java
 * @package:com.xzsoft.xc.ar.dao.impl
 * @Description:TODO
 * @date:2016年7月28日上午10:24:40
 * @version V1.0
 */
package com.xzsoft.xc.ar.dao.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.apar.modal.ArInvTransBean;
import com.xzsoft.xc.ar.dao.ArInvTransDao;
import com.xzsoft.xc.ar.mapper.ArInvTransMapper;
import com.xzsoft.xc.ar.modal.ArPayLBean;
import com.xzsoft.xip.platform.util.PlatformUtil;

/**
 * @ClassName: ArInvTransDaoImpl
 * @Description: TODO
 * @author:zhenghy
 * @date: 2016年7月28日 上午10:24:40
 */
@Repository("arInvTransDao")
public class ArInvTransDaoImpl implements ArInvTransDao {

	@Resource
	private ArInvTransMapper arInvTransMapper;
	/*
	 * (non-Javadoc)
	 * <p>Title: getArInvTran</p>
	 * <p>Description: 获得一条交易明细对象</p>
	 * @param source_id 来源id
	 * @param source_dtl_id 来源明细id
	 * @param ar_doc_cat_code 单据编码
	 * @return ArInvTransBean 明细对象
	 * @throws Exception
	 * @see com.xzsoft.xc.ar.dao.ArInvTransDao#getArInvTran(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public ArInvTransBean getArInvTran(String source_id, String source_dtl_id, String ar_doc_cat_code) throws Exception{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("SOURCE_ID", source_id);
		map.put("SOURCE_DTL_ID", source_dtl_id);
		map.put("AR_DOC_CAT_CODE", ar_doc_cat_code);
		return arInvTransMapper.selectArInvTran(map);
	}
	/*
	 * (non-Javadoc)
	 * <p>Title: insertArInvTran</p>
	 * <p>Description: 新增一条交易明细</p>
	 * @param arInvTransBean 交易明细对象
	 * @throws Exception
	 * @see com.xzsoft.xc.ar.dao.ArInvTransDao#insertArInvTran(com.xzsoft.xc.ar.modal.ArInvTransBean)
	 */
	@Override
	public void insertArInvTran(ArInvTransBean arInvTransBean) throws Exception{
		arInvTransMapper.insertArInvTran(arInvTransBean);
	}
	/*
	 * (non-Javadoc)
	 * <p>Title: insertArInvTrans</p>
	 * <p>Description: 批量新增交易明细对象</p>
	 * @param arInvTransBeans 交易明细对象集合
	 * @throws Exception
	 * @see com.xzsoft.xc.ar.dao.ArInvTransDao#insertArInvTrans(java.util.List)
	 */
	@Override
	public void insertArInvTrans(List<ArInvTransBean> arInvTransBeanList) throws Exception {
		if (arInvTransBeanList.size() > 0 && arInvTransBeanList != null) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", arInvTransBeanList);
			arInvTransMapper.insertArInvTrans(map);
		}
		
	}
	/* (non-Javadoc)
	 * <p>Title: deleteArInvTrans</p>
	 * <p>Description: </p>
	 * @param arInvTransBeanList
	 * @throws Exception
	 * @see com.xzsoft.xc.ar.dao.ArInvTransDao#deleteArInvTrans(java.util.List)
	 */
	@Override
	public void deleteArInvTrans(List<ArPayLBean> arPayLBeanList) throws Exception {
		if (arPayLBeanList.size() > 0 && arPayLBeanList != null) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", arPayLBeanList);
			arInvTransMapper.deleteArInvTransByPay(map);
		}
	}
	/* (non-Javadoc)
	 * <p>Title: deleteArInvTrans</p>
	 * <p>Description: </p>
	 * @param source_id
	 * @throws Exception
	 * @see com.xzsoft.xc.ar.dao.ArInvTransDao#deleteArInvTrans(java.lang.String)
	 */
	@Override
	public void deleteArInvTrans(String source_id) throws Exception {
		arInvTransMapper.deleteArInvTransBySourceId(source_id);
	}

}
