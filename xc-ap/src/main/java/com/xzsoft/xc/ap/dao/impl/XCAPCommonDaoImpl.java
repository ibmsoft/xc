package com.xzsoft.xc.ap.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.xzsoft.xc.ap.action.GetApCodeAction;
import com.xzsoft.xc.ap.dao.XCAPCommonDao;
import com.xzsoft.xc.ap.mapper.XCAPCommonMapper;
import com.xzsoft.xc.ap.modal.ApPayReqHBean;
import com.xzsoft.xc.ap.modal.ApVoucherHandlerBean;
import com.xzsoft.xc.apar.modal.ApDocumentHBean;
import com.xzsoft.xc.apar.modal.ApDocumentLBean;
import com.xzsoft.xip.platform.util.PlatformUtil;
/**
 * 
  * @ClassName XCAPCommonDaoImpl
  * @Description 应付模块公共Dao层实现类
  * @author 任建建
  * @date 2016年4月5日 上午9:30:54
 */
@Repository("xcapCommonDao")
public class XCAPCommonDaoImpl implements XCAPCommonDao{
	private static final Logger log = Logger.getLogger(GetApCodeAction.class);
	@Resource
	private XCAPCommonMapper xcapCommonMapper;
	/*
	 * 
	  * <p>Title getApCode</p>
	  * <p>Description 获取单据编码</p>
	  * @param ledgerId
	  * @param apDocCatCode
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.XCAPCommonDao#getApCode(java.lang.String, java.lang.String)
	 */
	@Override
	public String getApCode(String ledgerId, String apDocCatCode) throws Exception {
		String returnInfo = null;
		try {
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("ledgerId",ledgerId);
			map.put("apDocCatCode",apDocCatCode);
			returnInfo = xcapCommonMapper.getApCode(map);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return returnInfo;
	}
	/*
	 * 
	  * <p>Title getApInvGlH</p>
	  * <p>Description 查询应付单主表信息</p>
	  * @param apInvGlHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.XCAPCommonDao#getApInvGlH(java.lang.String)
	 */
	@Override
	public ApDocumentHBean getApInvGlH(String apInvGlHId) throws Exception {
		ApDocumentHBean apInvGlH = new ApDocumentHBean();
		try {
			apInvGlH = xcapCommonMapper.getApInvGlH(apInvGlHId);
			if(apInvGlH != null){
				List<ApDocumentLBean> apDocumentLBean = xcapCommonMapper.getApInvGlL(apInvGlHId);
				apInvGlH.setApDocumentLBean(apDocumentLBean);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return apInvGlH;
	}
	/*
	 * 
	  * <p>Title getApInvGlHByInvId</p>
	  * <p>Description 根据采购发票id得到应付单信息</p>
	  * @param apInvId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.XCAPCommonDao#getApInvGlHByInvId(java.lang.String)
	 */
	@Override
	public ApDocumentHBean getApInvGlHByInvId(String apInvId) throws Exception {
		ApDocumentHBean apInvGlH = new ApDocumentHBean();
		try {
			apInvGlH = xcapCommonMapper.getApInvGlHByInvId(apInvId);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return apInvGlH;
	}
	/*
	 * 
	  * <p>Title getApInvGlL</p>
	  * <p>Description 查询应付单行表信息</p>
	  * @param apInvGlHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.XCAPCommonDao#getApInvGlL(java.lang.String)
	 */
	@Override
	public List<ApDocumentLBean> getApInvGlL(String apInvGlHId) throws Exception {
		List<ApDocumentLBean> apDocumentLBean = null;
		try {
			apDocumentLBean = xcapCommonMapper.getApInvGlL(apInvGlHId);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return apDocumentLBean;
	}
	/*
	 * 
	  * <p>Title getApCancelH</p>
	  * <p>Description 查询核销单主表信息</p>
	  * @param apCancelHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.XCAPCommonDao#getApCancelH(java.lang.String)
	 */
	@Override
	public ApDocumentHBean getApCancelH(String apCancelHId) throws Exception {
		ApDocumentHBean apCancelH = new ApDocumentHBean();
		try {
			apCancelH = xcapCommonMapper.getApCancelH(apCancelHId);
			if(apCancelH != null){
				List<ApDocumentLBean> apDocumentLBean = xcapCommonMapper.getApCancelL(apCancelHId) ;
				apCancelH.setApDocumentLBean(apDocumentLBean);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return apCancelH;
	}
	/*
	 * 
	  * <p>Title getApPayH</p>
	  * <p>Description 查询付款单主表信息</p>
	  * @param apPayHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.XCAPCommonDao#getApPayH(java.lang.String)
	 */
	@Override
	public ApDocumentHBean getApPayH(String apPayHId) throws Exception {
		ApDocumentHBean apPayH = new ApDocumentHBean();
		try {
			apPayH = xcapCommonMapper.getApPayH(apPayHId);
			if(apPayH != null){
				List<ApDocumentLBean> apDocumentLBean = xcapCommonMapper.getApPayL(apPayHId) ;
				apPayH.setApDocumentLBean(apDocumentLBean);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return apPayH;
	}
	/*
	 * 
	  * <p>Title getApPayL</p>
	  * <p>Description 查询付款单行表信息</p>
	  * @param apPayHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.XCAPCommonDao#getApPayL(java.lang.String)
	 */
	@Override
	public List<ApDocumentLBean> getApPayL(String apPayHId) throws Exception {
		List<ApDocumentLBean> apDocumentLBean = null;
		try {
			apDocumentLBean = xcapCommonMapper.getApPayL(apPayHId);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return apDocumentLBean;
	}
	/*
	 * 
	  * <p>Title getApInvAdj</p>
	  * <p>Description 查询余额调整单信息</p>
	  * @param glAdjId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.XCAPCommonDao#getApInvAdj(java.lang.String)
	 */
	@Override
	public ApDocumentHBean getApInvAdj(String glAdjId) throws Exception {
		ApDocumentHBean apInvAdj = new ApDocumentHBean();
		try {
			apInvAdj = xcapCommonMapper.getApInvAdj(glAdjId);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return apInvAdj;
	}
	/*
	 * 
	  * <p>Title getApPayReqH</p>
	  * <p>Description 查询付款申请单主表信息</p>
	  * @param apPayReqGId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.XCAPCommonDao#getApPayReqH(java.lang.String)
	 */
	@Override
	public ApPayReqHBean getApPayReqH(String apPayReqGId) throws Exception {
		ApPayReqHBean apPayReqH = new ApPayReqHBean();
		try {
			apPayReqH = xcapCommonMapper.getApPayReqH(apPayReqGId);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return apPayReqH;
	}
	/*
	 * 
	  * <p>Title getApInvoiceH</p>
	  * <p>Description 查询采购发票主表信息</p>
	  * @param apInvoiceHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.XCAPCommonDao#getApInvoiceH(java.lang.String)
	 */
	@Override
	public ApDocumentHBean getApInvoiceH(String apInvoiceHId) throws Exception {
		ApDocumentHBean apInvoiceH = new ApDocumentHBean();
		try {
			apInvoiceH = xcapCommonMapper.getApInvoiceH(apInvoiceHId);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return apInvoiceH;
	}
	/*
	 * 
	  * <p>Title getApInvoiceL</p>
	  * <p>Description 查询采购发票行表信息</p>
	  * @param apInvoiceHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.XCAPCommonDao#getApInvoiceL(java.lang.String)
	 */
	@Override
	public List<ApDocumentLBean> getApInvoiceL(String apInvoiceHId) throws Exception {
		List<ApDocumentLBean> apInvoiceL = new ArrayList<ApDocumentLBean>();
		try {
			apInvoiceL = xcapCommonMapper.getApInvoiceL(apInvoiceHId);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return apInvoiceL;
	}
	/*
	 * 
	  * <p>Title getApInvoiceLById</p>
	  * <p>Description </p>
	  * @param apInvoiceLId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.XCAPCommonDao#getApInvoiceLById(java.lang.String)
	 */
	@Override
	public ApDocumentLBean getApInvoiceLById(String apInvoiceLId) throws Exception {
		ApDocumentLBean apInvoiceL = new ApDocumentLBean();
		try {
			apInvoiceL = xcapCommonMapper.getApInvoiceLById(apInvoiceLId);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return apInvoiceL;
	}
	/*
	 * 
	  * <p>Title deleteApDoc</p>
	  * <p>Description 删除单据表</p>
	  * @param apVoucherHandler
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.XCAPCommonDao#deleteApDoc(com.xzsoft.xc.ap.modal.ApVoucherHandlerBean)
	 */
	@Override
	public void deleteApDoc(ApVoucherHandlerBean apVoucherHandler) throws Exception {
		try {
			xcapCommonMapper.deleteApDoc(apVoucherHandler);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
	/*
	 * 
	  * <p>Title getAccId</p>
	  * <p>Description 通过科目组合ID得到科目ID</p>
	  * @param ccId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.XCAPCommonDao#getAccId(java.lang.String)
	 */
	@Override
	public String getAccId(String ccId) throws Exception {
		return xcapCommonMapper.getAccId(ccId);
	}
	/*
	 * 
	  * <p>Title judgmentInvAmount</p>
	  * <p>Description 红字发票需要判断应付单的未付金额是否还够冲销当前红色发票</p>
	  * @param lApInvHId
	  * @param invAmount
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.XCAPCommonDao#judgmentInvAmount(java.lang.String, double)
	 */
	@Override
	public String judgmentInvAmount(String lApInvHId, double invAmount) throws Exception {
		String returnStr = null;
		try {
			HashMap<String,Object> map = new HashMap<String,Object>();
			
			map.put("lApInvHId",lApInvHId);
			map.put("invAmount",invAmount);
			returnStr = xcapCommonMapper.judgmentInvAmount(map);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return returnStr;
	}
	@Override
	public String judgmentInvoiceAmount(String lApInvHId, double invAmount) throws Exception {
		String returnStr = null;
		try {
			HashMap<String,Object> map = new HashMap<String,Object>();
			
			map.put("lApInvHId",lApInvHId);
			map.put("invAmount",invAmount);
			returnStr = xcapCommonMapper.judgmentInvoiceAmount(map);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return returnStr;
	}
	/*
	 * 
	  * <p>Title getApDocLByBgItemId</p>
	  * <p>Description 按预算项目统计单据明细信息</p>
	  * @param apDocId
	  * @param apDocCat
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.XCAPCommonDao#getApDocLByBgItemId(java.lang.String, java.lang.String)
	 */
	@Override
	public List<ApDocumentLBean> getApDocLByBgItemId(String apDocId,String apDocCat) throws Exception {
		List<ApDocumentLBean> budgetList= null;
		try {
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("apDocId", apDocId);
			map.put("apDocCat", apDocCat);
			map.put("dbType", PlatformUtil.getDbType());
			budgetList = xcapCommonMapper.getApDocLByBgItemId(map);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return budgetList;
	}
	@Override
	public void updatePoOrderLAmtInfo(List<ApVoucherHandlerBean> lists) throws Exception {
		try {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", lists);
			xcapCommonMapper.updatePoOrderLAmtInfo(map);
		} catch (Exception e) {
			throw e;
		}
	}
}
