package com.xzsoft.xc.ar.dao.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.xzsoft.xc.apar.modal.ArDocumentHBean;
import com.xzsoft.xc.apar.modal.ArDocumentLBean;
import com.xzsoft.xc.ar.dao.XCARCommonDao;
import com.xzsoft.xc.ar.mapper.XCARCommonMapper;
import com.xzsoft.xc.ar.modal.ArCustomerBean;
import com.xzsoft.xc.ar.modal.ArInvoiceHBean;
/**
 * 
  * @ClassName XCARCommonDaoImpl
  * @Description 应收模块公共Dao层实现类
  * @author 任建建
  * @date 2016年7月6日 上午11:35:40
 */
@Repository("xcarCommonDao")
public class XCARCommonDaoImpl implements XCARCommonDao{
	private static final Logger log = Logger.getLogger(XCARCommonDaoImpl.class);
	@Resource
	XCARCommonMapper xcarCommonMapper;
	/*
	 * 
	  * <p>Title getArCode</p>
	  * <p>Description 获取单据编码规则</p>
	  * @param ledgerId
	  * @param arDocCatCode
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.XCARCommonDao#getArCode(java.lang.String, java.lang.String)
	 */
	@Override
	public String getArCode(String ledgerId, String arDocCatCode) throws Exception {
		String returnInfo = null;
		try {
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("ledgerId",ledgerId);
			map.put("arDocCatCode",arDocCatCode);
			returnInfo = xcarCommonMapper.getArCode(map);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return returnInfo;
	}
	/*
	 * 
	  * <p>Title getArInvoiceH</p>
	  * <p>Description 通过销售发票ID得到销售发票信息</p>
	  * @param AR_INV_H_ID
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.XCARCommonDao#getArInvoiceH(java.lang.String)
	 */
	@Override
	public ArInvoiceHBean getArInvoiceH(String AR_INV_H_ID) throws Exception {
		ArInvoiceHBean arInvoiceH = new ArInvoiceHBean();
		try {
			arInvoiceH = xcarCommonMapper.getArInvoiceH(AR_INV_H_ID);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return arInvoiceH;
	}
	/*
	 * 
	  * <p>Title getArInvGlHByInvId</p>
	  * <p>Description 通过销售发票ID查询应收单主表信息</p>
	  * @param AR_INV_H_ID
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.XCARCommonDao#getArInvGlHByInvId(java.lang.String)
	 */
	@Override
	public ArDocumentHBean getArInvGlHByInvId(String AR_INV_H_ID) throws Exception {
		ArDocumentHBean arInvGlH = new ArDocumentHBean();
		try {
			arInvGlH = xcarCommonMapper.getArInvGlHByInvId(AR_INV_H_ID);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return arInvGlH;
	}
	/*
	 * 
	  * <p>Title getArInvGlH</p>
	  * <p>Description 通过应收单ID查询应收单信息</p>
	  * @param AR_INV_GL_H_ID
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.XCARCommonDao#getArInvGlH(java.lang.String)
	 */
	@Override
	public ArDocumentHBean getArInvGlH(String AR_INV_GL_H_ID) throws Exception {
		ArDocumentHBean arInvGlH = new ArDocumentHBean();
		try {
			arInvGlH = xcarCommonMapper.getArInvGlH(AR_INV_GL_H_ID);
			if(arInvGlH != null){
				List<ArDocumentLBean> arDocumentLBean = xcarCommonMapper.getArInvGlL(AR_INV_GL_H_ID) ;
				arInvGlH.setAddDocument(arDocumentLBean);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return arInvGlH;
	}
	/*
	 * 
	  * <p>Title getArPayH</p>
	  * <p>Description 得到收款单信息</p>
	  * @param AR_PAY_H_ID
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.XCARCommonDao#getArPayH(java.lang.String)
	 */
	@Override
	public ArDocumentHBean getArPayH(String AR_PAY_H_ID) throws Exception {
		ArDocumentHBean arPayH = new ArDocumentHBean();
		try {
			arPayH = xcarCommonMapper.getArPayH(AR_PAY_H_ID);
			if(arPayH != null){
				List<ArDocumentLBean> arDocumentLBean = xcarCommonMapper.getArPayL(AR_PAY_H_ID) ;
				arPayH.setAddDocument(arDocumentLBean);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return arPayH;
	}
	/*
	 * 
	  * <p>Title getArPayL</p>
	  * <p>Description 通过收款单ID查询收款单行表信息</p>
	  * @param AR_PAY_H_ID
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.XCARCommonDao#getArPayL(java.lang.String)
	 */
	@Override
	public List<ArDocumentLBean> getArPayL(String AR_PAY_H_ID) throws Exception {
		List<ArDocumentLBean> arDocumentLBean = null;
		try {
			arDocumentLBean = xcarCommonMapper.getArPayL(AR_PAY_H_ID);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return arDocumentLBean;
	}
	/*
	 * 
	  * <p>Title getArCancelH</p>
	  * <p>Description 通过核销单ID查询核销单信息</p>
	  * @param AR_CANCEL_H_ID
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.XCARCommonDao#getArCancelH(java.lang.String)
	 */
	@Override
	public ArDocumentHBean getArCancelH(String AR_CANCEL_H_ID) throws Exception {
		ArDocumentHBean arCancelH = new ArDocumentHBean();
		try {
			arCancelH = xcarCommonMapper.getArCancelH(AR_CANCEL_H_ID);
			if(arCancelH != null){
				List<ArDocumentLBean> arDocumentLBean = xcarCommonMapper.getArCancelL(AR_CANCEL_H_ID) ;
				arCancelH.setAddDocument(arDocumentLBean);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return arCancelH;
	}
	/*
	 * 
	  * <p>Title getArCancelL</p>
	  * <p>Description 通过核销单ID查询核销单行信息</p>
	  * @param AR_CANCEL_H_ID
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.XCARCommonDao#getArCancelL(java.lang.String)
	 */
	@Override
	public List<ArDocumentLBean> getArCancelL(String AR_CANCEL_H_ID) throws Exception {
		List<ArDocumentLBean> arDocumentLBean = null;
		try {
			arDocumentLBean = xcarCommonMapper.getArCancelL(AR_CANCEL_H_ID);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return arDocumentLBean;
	}
	/*
	 * 
	  * <p>Title getArInvGlAdj</p>
	  * <p>Description 通过余额调整单ID查询余额调整单表信息</p>
	  * @param GL_ADJ_ID
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.XCARCommonDao#getArInvGlAdj(java.lang.String)
	 */
	@Override
	public ArDocumentHBean getArInvGlAdj(String GL_ADJ_ID) throws Exception {
		ArDocumentHBean arInvGlAdj = new ArDocumentHBean();
		try {
			arInvGlAdj = xcarCommonMapper.getArInvGlAdj(GL_ADJ_ID);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return arInvGlAdj;
	}
	/*
	 * 
	  * <p>Title getAccId</p>
	  * <p>Description </p>
	  * @param ccId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.XCARCommonDao#getAccId(java.lang.String)
	 */
	@Override
	public String getAccId(String ccId) throws Exception {
		return xcarCommonMapper.getAccId(ccId);
	}
	/*
	 * 
	  * <p>Title judgmentInvAmount</p>
	  * <p>Description 红字发票需要判断应收单的未付金额是否还够冲销当前红色发票</p>
	  * @param lArInvHId
	  * @param invAmount
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.XCARCommonDao#judgmentInvAmount(java.lang.String, double)
	 */
	@Override
	public String judgmentInvAmount(String lArInvHId, double invAmount) throws Exception {
		String returnStr = null;
		try {
			HashMap<String,Object> map = new HashMap<String,Object>();
			
			map.put("lArInvHId",lArInvHId);
			map.put("invAmount",invAmount);
			returnStr = xcarCommonMapper.judgmentInvAmount(map);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return returnStr;
	}
	@Override
	public String judgmentInvoiceAmount(String lArInvHId, double invAmount) throws Exception {
		String returnStr = null;
		try {
			HashMap<String,Object> map = new HashMap<String,Object>();
			
			map.put("lArInvHId",lArInvHId);
			map.put("invAmount",invAmount);
			returnStr = xcarCommonMapper.judgmentInvoiceAmount(map);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return returnStr;
	}
	/*
	 * 
	  * <p>Title getArCustomer</p>
	  * <p>Description 通过客户ID查询客户信息</p>
	  * @param CUSTOMER_ID
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.XCARCommonDao#getArCustomer(java.lang.String)
	 */
	@Override
	public ArCustomerBean getArCustomer(String CUSTOMER_ID) throws Exception {
		ArCustomerBean arCustomer = null;
		try {
			arCustomer = xcarCommonMapper.getArCustomer(CUSTOMER_ID);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return arCustomer;
	}
}