package com.xzsoft.xc.apar.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.xzsoft.xc.apar.dao.ApArDao;
import com.xzsoft.xc.apar.mapper.ApArMapper;
import com.xzsoft.xc.apar.modal.ApDocumentHBean;
import com.xzsoft.xc.apar.modal.ApDocumentLBean;
import com.xzsoft.xc.apar.modal.ArDocumentHBean;
import com.xzsoft.xc.apar.modal.ArDocumentLBean;
import com.xzsoft.xc.gl.dao.XCGLCommonDAO;
import com.xzsoft.xip.platform.dao.RedisDao;

/** 
 * @ClassName: ApDaoImpl 
 * @Description: TODO
 * @author weicw 
 * @date 2016年9月2日 下午4:26:10 
 * 
 * 
 */
@Repository("apArDao")
public class ApArDaoImpl implements ApArDao {
	@Resource
	ApArMapper mapper;
	//日志记录器
	private static final Logger log = Logger.getLogger(ApArDaoImpl.class.getName());
	@Resource
	private RedisDao redisDaoImpl;
	@Resource
	private XCGLCommonDAO xcglCommonDAO;
	/**
	 * @Title:getApInvGlH
	 * @Description: 查询应付单主表信息 （应收核应付）
	 * 参数格式:String
	 * @param String
	 * @return com.xzsoft.xc.ap.modal.ApDocumentHBean
	 * @throws 
	 */
	@Override
	public ApDocumentHBean getApInvGlH(String apInvGlHId) {
		ApDocumentHBean apInvGlH = new ApDocumentHBean();
		try {
			apInvGlH = mapper.getApInvGlH(apInvGlHId);
			if(apInvGlH != null){
				List<ApDocumentLBean> apDocumentLBean = mapper.getApInvGlL(apInvGlHId);
				apInvGlH.setApDocumentLBean(apDocumentLBean);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return apInvGlH;
	}
	/**
	 * @Title:getApInvGlL
	 * @Description: 查询应付单行表信息（应收核应付）
	 * 参数格式:String
	 * @param String
	 * @return java.util.List
	 * @throws 
	 */
	@Override 
	public List<ApDocumentLBean> getApInvGlL(String apInvGlHId) {
		return mapper.getApInvGlL(apInvGlHId);
	}
	/**
	 * @Title:getArInvGlL
	 * @Description: 查询应收单主表信息（应付核应收）
	 * 参数格式:String
	 * @param String
	 * @return java.util.List
	 * @throws 
	 */
	@Override
	public ArDocumentHBean getArInvGlH(String arInvGlHId) {
		ArDocumentHBean arInvGlH = new ArDocumentHBean();
		try {
			arInvGlH = mapper.getArInvGlH(arInvGlHId);
			if(arInvGlH != null){
				List<ArDocumentLBean> arDocumentLBean = mapper.getArInvGlL(arInvGlHId);
				arInvGlH.setAddDocument(arDocumentLBean);;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return arInvGlH;
	}
	/**
	 * @Title:getArInvGlL
	 * @Description: 查询应收单行表信息（应付核应收）
	 * 参数格式:String
	 * @param String
	 * @return java.util.List
	 * @throws 
	 */
	@Override 
	public List<ArDocumentLBean> getArInvGlL(String arInvGlHId) {
		return mapper.getArInvGlL(arInvGlHId);
	}
}

