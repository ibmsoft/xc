package com.xzsoft.xc.ap.dao.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.xzsoft.xc.ap.dao.ApInvGlBaseDao;
import com.xzsoft.xc.ap.mapper.ApInvGlBaseMapper;
import com.xzsoft.xc.apar.modal.ApDocumentHBean;
/**
 * 
  * @ClassName ApInvGlBaseDaoImpl
  * @Description 应付单模块处理
  * @author RenJianJian
  * @date 2016年8月8日 下午5:56:43
 */
@Repository("apInvGlBaseDao")
public class ApInvGlBaseDaoImpl implements ApInvGlBaseDao {
	public static final Logger log = Logger.getLogger(ApInvGlBaseDaoImpl.class);
	@Resource
	ApInvGlBaseMapper apInvGlBaseMapper;
	//===================应付单模块
	/*
	 * 
	  * <p>Title saveApInvGl</p>
	  * <p>Description 保存应付单信息包括主信息、行信息</p>
	  * @param apDocumentH
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.ApDocsAndVoucherDao#saveApInvGl(com.xzsoft.xc.ap.modal.ApDocumentHBean)
	 */
	@Override
	public void saveApInvGl(ApDocumentHBean apDocumentH) throws Exception {
		try {
			//保存应付单主表信息
			apInvGlBaseMapper.saveApInvGlH(apDocumentH);
			//保存应付单明细表信息
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("list",apDocumentH.getAddDocument());
			apInvGlBaseMapper.saveApInvGlDtl(map);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
	/*
	 * 
	  * <p>Title updateApInvGl</p>
	  * <p>Description 更新应付单</p>
	  * @param apDocumentH
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.ApDocsAndVoucherDao#updateApInvGl(com.xzsoft.xc.ap.modal.ApDocumentHBean)
	 */
	@Override
	public void updateApInvGl(ApDocumentHBean apDocumentH) throws Exception {
		try {
			//更新应付单主表信息
			apInvGlBaseMapper.updateApInvGlH(apDocumentH);
			//新增应付单明细行
			if(apDocumentH.getAddDocument() != null && apDocumentH.getAddDocument().size() > 0) {
				HashMap<String,Object> addMap = new HashMap<String,Object>();
				addMap.put("list",apDocumentH.getAddDocument());
				apInvGlBaseMapper.saveApInvGlDtl(addMap);
			}
			//更新应付单明细行
			if(apDocumentH.getUpdateDocument() != null && apDocumentH.getUpdateDocument().size() > 0){
				HashMap<String,Object> updMap = new HashMap<String,Object>() ;
				updMap.put("list",apDocumentH.getUpdateDocument());
				apInvGlBaseMapper.updateApInvGlDtl(updMap);
			}
			//删除应付单明细行
			if(apDocumentH.getDeleteDocument() != null && apDocumentH.getDeleteDocument().size() > 0){
				apInvGlBaseMapper.deleteApInvGlDtl(apDocumentH.getDeleteDocument());
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
	/*
	 * 
	  * <p>Title deleteApInvGlDtl</p>
	  * <p>Description 删除应付单行表信息</p>
	  * @param dtlLists
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.ApDocsAndVoucherDao#deleteApInvGlDtl(java.util.List)
	 */
	@Override
	public void deleteApInvGlDtl(List<String> dtlLists) throws Exception {
		try {
			//删除应付单行表信息
			apInvGlBaseMapper.deleteApInvGlDtl(dtlLists);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
	/*
	 * 
	  * <p>Title deleteApInvGlH</p>
	  * <p>Description 删除应付单信息</p>
	  * @param lists
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.ApDocsAndVoucherDao#deleteApInvGlH(java.util.List)
	 */
	@Override
	public void deleteApInvGlH(List<String> lists) throws Exception {
		try {
			//删除应付单行表信息
			apInvGlBaseMapper.deleteApInvGlHDtls(lists);
			//删除应付单主表信息
			apInvGlBaseMapper.deleteApInvGlH(lists);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
	//===================应付单模块
}
