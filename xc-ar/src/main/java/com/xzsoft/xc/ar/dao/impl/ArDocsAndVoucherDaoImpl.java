package com.xzsoft.xc.ar.dao.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.xzsoft.xc.apar.modal.ArDocumentHBean;
import com.xzsoft.xc.ar.dao.ArDocsAndVoucherDao;
import com.xzsoft.xc.ar.mapper.ArDocsAndVoucherMapper;
import com.xzsoft.xc.ar.modal.ArVoucherHandlerBean;
import com.xzsoft.xip.platform.util.PlatformUtil;
/**
 * 
  * @ClassName ArDocsAndVoucherDaoImpl
  * @Description 应收模块保存单据和生成凭证的Dao层实现类
  * @author 任建建
  * @date 2016年7月8日 下午12:19:26
 */
@Repository("arDocsAndVoucherDao")
public class ArDocsAndVoucherDaoImpl implements ArDocsAndVoucherDao {
	public static final Logger log = Logger.getLogger(ArDocsAndVoucherDaoImpl.class);
	@Resource
	ArDocsAndVoucherMapper arDocsAndVoucherMapper;
	/*
	 * 
	  * <p>Title saveNewArVoucher</p>
	  * <p>Description 更新单据中的凭证状态</p>
	  * @param arVoucherHandler
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.ArDocsAndVoucherDao#saveNewArVoucher(com.xzsoft.xc.ar.modal.ArVoucherHandlerBean)
	 */
	@Override
	public void saveNewArVoucher(ArVoucherHandlerBean arVoucherHandler) throws Exception {
		try {
			//保存新增--单据凭证信息
			arDocsAndVoucherMapper.saveNewArVoucher(arVoucherHandler);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
	/*
	 * 
	  * <p>Title saveCheckArVoucher</p>
	  * <p>Description 审核凭证</p>
	  * @param avhvList
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.ArDocsAndVoucherDao#saveCheckArVoucher(java.util.List)
	 */
	@Override
	public void saveCheckArVoucher(List<ArVoucherHandlerBean> avhbList) throws Exception {
		try {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", avhbList);
			//审核凭证
			arDocsAndVoucherMapper.saveCheckArVoucher(map);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
	/*
	 * 
	  * <p>Title saveSignArVoucher</p>
	  * <p>Description 签字</p>
	  * @param avhbList
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.ArDocsAndVoucherDao#saveSignArVoucher(java.util.List)
	 */
	@Override
	public void saveSignArVoucher(List<ArVoucherHandlerBean> avhbList) throws Exception {
		try {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", avhbList);
			//签字
			arDocsAndVoucherMapper.saveSignArVoucher(map);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
	//===================应收单模块
	/*
	 * 
	  * <p>Title saveArInvGl</p>
	  * <p>Description 保存应收单信息包括主信息、行信息</p>
	  * @param arDocumentH
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.ArDocsAndVoucherDao#saveArInvGl(com.xzsoft.xc.apar.modal.ArDocumentHBean)
	 */
	@Override
	public void saveArInvGl(ArDocumentHBean arDocumentH) throws Exception {
		try {
			//保存应收单主表信息
			arDocsAndVoucherMapper.saveArInvGlH(arDocumentH);
			//保存应收单明细表信息
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("list",arDocumentH.getAddDocument());
			arDocsAndVoucherMapper.saveArInvGlDtl(map);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
	/*
	 * 
	  * <p>Title updateArInvGl</p>
	  * <p>Description 更新应付单</p>
	  * @param arDocumentH
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.ArDocsAndVoucherDao#updateArInvGl(com.xzsoft.xc.apar.modal.ArDocumentHBean)
	 */
	@Override
	public void updateArInvGl(ArDocumentHBean arDocumentH) throws Exception {
		try {
			//更新应收单主表信息
			arDocsAndVoucherMapper.updateArInvGlH(arDocumentH);
			//新增应收单明细行
			if(arDocumentH.getAddDocument() != null && arDocumentH.getAddDocument().size() > 0) {
				HashMap<String,Object> addMap = new HashMap<String,Object>();
				addMap.put("list",arDocumentH.getAddDocument());
				arDocsAndVoucherMapper.saveArInvGlDtl(addMap);
			}
			//更新应收单明细行
			if(arDocumentH.getUpdateDocument() != null && arDocumentH.getUpdateDocument().size() > 0){
				HashMap<String,Object> updMap = new HashMap<String,Object>() ;
				updMap.put("list",arDocumentH.getUpdateDocument());
				arDocsAndVoucherMapper.updateArInvGlDtl(updMap);
			}
			//删除应收单明细行
			if(arDocumentH.getDeleteDocument() != null && arDocumentH.getDeleteDocument().size() > 0){
				arDocsAndVoucherMapper.deleteArInvGlDtl(arDocumentH.getDeleteDocument());
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
	/*
	 * 
	  * <p>Title deleteArInvGlDtl</p>
	  * <p>Description 删除应收单行表信息</p>
	  * @param dtlLists
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.ArDocsAndVoucherDao#deleteArInvGlDtl(java.util.List)
	 */
	@Override
	public void deleteArInvGlDtl(List<String> dtlLists) throws Exception {
		try {
			//删除应收单行表信息
			arDocsAndVoucherMapper.deleteArInvGlDtl(dtlLists);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
	/*
	 * 
	  * <p>Title deleteArInvGlH</p>
	  * <p>Description 删除应收单信息</p>
	  * @param lists
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.ArDocsAndVoucherDao#deleteArInvGlH(java.util.List)
	 */
	@Override
	public void deleteArInvGlH(List<String> lists) throws Exception {
		try {
			//删除应收单行表信息
			arDocsAndVoucherMapper.deleteArInvGlHDtls(lists);
			//删除应收单主表信息
			arDocsAndVoucherMapper.deleteArInvGlH(lists);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
	//===================应收单模块
	/*
	 * 
	  * <p>Title: deleteArDoc</p>
	  * <p>Description: 删除单据表</p>
	  * @param arVoucherHandler
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.ArDocsAndVoucherDao#deleteArDoc(com.xzsoft.xc.ar.modal.ArVoucherHandlerBean)
	 */
	@Override
	public void deleteArDoc(ArVoucherHandlerBean arVoucherHandler) throws Exception {
		try {
			//删除余额调整单
			arDocsAndVoucherMapper.deleteArDoc(arVoucherHandler);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
}