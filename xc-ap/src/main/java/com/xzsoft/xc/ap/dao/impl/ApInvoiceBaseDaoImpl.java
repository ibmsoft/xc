package com.xzsoft.xc.ap.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.ap.dao.ApInvoiceBaseDao;
import com.xzsoft.xc.ap.dao.XCAPCommonDao;
import com.xzsoft.xc.ap.mapper.ApInvoiceBaseMapper;
import com.xzsoft.xc.ap.modal.ApInvoiceHBean;
import com.xzsoft.xc.ap.modal.ApInvoiceLBean;
import com.xzsoft.xc.ap.modal.ApVoucherHandlerBean;
import com.xzsoft.xc.ap.util.XCAPConstants;
import com.xzsoft.xc.apar.modal.ApDocumentLBean;
import com.xzsoft.xip.platform.util.PlatformUtil;
/**
 * 
  * @ClassName ApInvoiceBaseDaoImpl
  * @Description 应付模块发票基础信息数据层接口实现类
  * @author 任建建
  * @date 2016年6月14日 下午2:18:20
 */
@Repository("apInvoiceBaseDao")
public class ApInvoiceBaseDaoImpl implements ApInvoiceBaseDao {
	@Resource
	private ApInvoiceBaseMapper apInvoiceBaseMapper;
	@Resource
	private XCAPCommonDao xcapCommonDao;
	/*
	 * 
	  * <p>Title saveInvoice</p>
	  * <p>Description 保存采购发票信息</p>
	  * @param apInvoiceH
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.ApInvoiceBaseDao#saveInvoice(com.xzsoft.xc.ap.modal.ApInvoiceHBean)
	 */
	@Override
	public void saveInvoice(ApInvoiceHBean apInvoiceH) throws Exception {
		try {
			//保存采购发票主表信息
			apInvoiceBaseMapper.saveInvoice(apInvoiceH);
			//更新采购订单行的list
			List<ApVoucherHandlerBean> updateOrderLLists = new ArrayList<ApVoucherHandlerBean>();
			//保存采购发票明细表信息
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("dbType",PlatformUtil.getDbType());
			map.put("list",apInvoiceH.getApInvoiceL());
			apInvoiceBaseMapper.saveInvoiceDtl(map);
			//回写采购订单行里面的已开票金额
			if(XCAPConstants.CGFP_CGDD.equals(apInvoiceH.getAP_DOC_CAT_CODE()) || XCAPConstants.CGFP_CGDD_H.equals(apInvoiceH.getAP_DOC_CAT_CODE())){
				for (ApInvoiceLBean apInvoiceL : apInvoiceH.getApInvoiceL()) {
					ApVoucherHandlerBean xcApCommonBean = new ApVoucherHandlerBean();
					xcApCommonBean.setPriKey(apInvoiceL.getORDER_L_ID());
					xcApCommonBean.setAMOUNT(apInvoiceL.getAMOUNT());
					updateOrderLLists.add(xcApCommonBean);
				}
			}
			//更新采购订单行表金额
			if(updateOrderLLists != null && updateOrderLLists.size() > 0)
				xcapCommonDao.updatePoOrderLAmtInfo(updateOrderLLists);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/*
	 * 
	  * <p>Title updateInvoice</p>
	  * <p>Description 更新采购发票信息</p>
	  * @param apInvoiceH
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.ApInvoiceBaseDao#updateInvoice(com.xzsoft.xc.ap.modal.ApInvoiceHBean)
	 */
	@Override
	public void updateInvoice(ApInvoiceHBean apInvoiceH) throws Exception {
		try {
			//更新采购发票主表信息
			apInvoiceBaseMapper.updateInvoice(apInvoiceH);
			//更新采购订单行的list
			List<ApVoucherHandlerBean> updateOrderLLists = new ArrayList<ApVoucherHandlerBean>();
			//新增采购发票明细行
			if(apInvoiceH.getApInvoiceL() != null && apInvoiceH.getApInvoiceL().size() > 0) {
				HashMap<String,Object> addMap = new HashMap<String,Object>();
				addMap.put("dbType",PlatformUtil.getDbType());
				addMap.put("list",apInvoiceH.getApInvoiceL());
				apInvoiceBaseMapper.saveInvoiceDtl(addMap);
				//回写采购订单行里面的已开票金额
				if(XCAPConstants.CGFP_CGDD.equals(apInvoiceH.getAP_DOC_CAT_CODE()) || XCAPConstants.CGFP_CGDD_H.equals(apInvoiceH.getAP_DOC_CAT_CODE())){
					for (ApInvoiceLBean apInvoiceL : apInvoiceH.getApInvoiceL()) {
						ApVoucherHandlerBean xcApCommonBean = new ApVoucherHandlerBean();
						xcApCommonBean.setPriKey(apInvoiceL.getORDER_L_ID());
						xcApCommonBean.setAMOUNT(apInvoiceL.getAMOUNT());
						updateOrderLLists.add(xcApCommonBean);
					}
				}
			}
			//更新采购发票明细行
			if(apInvoiceH.getUpdateInvoiceL() != null && apInvoiceH.getUpdateInvoiceL().size() > 0){
				HashMap<String,Object> updMap = new HashMap<String,Object>() ;
				updMap.put("dbType",PlatformUtil.getDbType());
				updMap.put("list",apInvoiceH.getUpdateInvoiceL());
				//回写采购订单行里面的已开票金额
				if(XCAPConstants.CGFP_CGDD.equals(apInvoiceH.getAP_DOC_CAT_CODE()) || XCAPConstants.CGFP_CGDD_H.equals(apInvoiceH.getAP_DOC_CAT_CODE())){
					for (ApInvoiceLBean apInvoiceL : apInvoiceH.getUpdateInvoiceL()) {
						//根据采购发票行ID得到采购发票行信息
						ApDocumentLBean apDocumentL = xcapCommonDao.getApInvoiceLById(apInvoiceL.getAP_INV_L_ID());
						ApVoucherHandlerBean xcApCommonBean = new ApVoucherHandlerBean();
						xcApCommonBean.setPriKey(apInvoiceL.getORDER_L_ID());
						xcApCommonBean.setAMOUNT(apInvoiceL.getAMOUNT() - ((apDocumentL == null) ? 0 : apDocumentL.getAMOUNT()));
						updateOrderLLists.add(xcApCommonBean);
					}
				}
				apInvoiceBaseMapper.updateInvoiceDtl(updMap);
			}
			//删除采购发票明细行
			if(apInvoiceH.getApInvoiceLs() != null && apInvoiceH.getApInvoiceLs().size() > 0){
				//回写采购订单行里面的已开票金额
				if(XCAPConstants.CGFP_CGDD.equals(apInvoiceH.getAP_DOC_CAT_CODE()) || XCAPConstants.CGFP_CGDD_H.equals(apInvoiceH.getAP_DOC_CAT_CODE())){
					for (String apInvoiceLId : apInvoiceH.getApInvoiceLs()) {
						//根据采购发票行ID得到采购发票行信息
						ApDocumentLBean apDocumentL = xcapCommonDao.getApInvoiceLById(apInvoiceLId);
						ApVoucherHandlerBean xcApCommonBean = new ApVoucherHandlerBean();
						xcApCommonBean.setPriKey(apDocumentL.getORDER_L_ID());
						xcApCommonBean.setAMOUNT(apDocumentL.getAMOUNT() * -1);
						updateOrderLLists.add(xcApCommonBean);
					}
				}
				apInvoiceBaseMapper.deleteInvoiceDtl(apInvoiceH.getApInvoiceLs());
			}
			//更新采购订单行表金额
			if(updateOrderLLists != null && updateOrderLLists.size() > 0)
				xcapCommonDao.updatePoOrderLAmtInfo(updateOrderLLists);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/*
	 * 
	  * <p>Title deleteInvoiceDtl</p>
	  * <p>Description 根据采购发票行表ID批量删除采购发票行表信息</p>
	  * @param dtlLists
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.ApInvoiceBaseDao#deleteInvoiceDtl(java.util.List)
	 */
	@Override
	public void deleteInvoiceDtl(List<String> dtlLists) throws Exception {
		try {
			//删除采购发票行表信息
			apInvoiceBaseMapper.deleteInvoiceDtl(dtlLists);
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	/*
	 * 
	  * <p>Title deleteInvoices</p>
	  * <p>Description 批量删除采购发票信息</p>
	  * @param lists
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.ApInvoiceBaseDao#deleteInvoices(java.util.List)
	 */
	@Override
	public void deleteInvoices(List<String> lists) throws Exception {
		try {
			//删除采购发票行表信息
			apInvoiceBaseMapper.deleteInvoiceDtls(lists);
			//删除采购发票主表信息
			apInvoiceBaseMapper.deleteInvoices(lists);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/*
	 * 
	  * <p>Title getInvoicePreCount</p>
	  * <p>Description 判断采购发票是否核销预付过</p>
	  * @param apInvHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.ApInvoiceBaseDao#getInvoicePreCount(java.lang.String)
	 */
	@Override
	public int getInvoicePreCount(String apInvHId) throws Exception {
		try {
			return apInvoiceBaseMapper.getInvoicePreCount(apInvHId);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/*
	 * 
	  * <p>Title getLInvoiceCount</p>
	  * <p>Description 判断采购发票是否已经成为另一个红字发票的蓝字发票</p>
	  * @param apInvHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.ApInvoiceBaseDao#getLInvoiceCount(java.lang.String)
	 */
	@Override
	public int getLInvoiceCount(String apInvHId) throws Exception {
		try {
			return apInvoiceBaseMapper.getLInvoiceCount(apInvHId);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/*
	 * 
	  * <p>Title getInvGlHCount</p>
	  * <p>Description 判断采购发票是否已经被应付单引用</p>
	  * @param apInvHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.ApInvoiceBaseDao#getInvGlHCount(java.lang.String)
	 */
	@Override
	public int getInvGlHCount(String apInvHId) throws Exception {
		try {
			return apInvoiceBaseMapper.getInvGlHCount(apInvHId);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/*
	 * 
	  * <p>Title getInvoiceLdProcessInfo</p>
	  * <p>Description 查询采购发票相关流程信息</p>
	  * @param apCatCode
	  * @param ledgerId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.ApInvoiceBaseDao#getInvoiceLdProcessInfo(java.lang.String, java.lang.String)
	 */
	@Override
	public HashMap<String,String> getInvoiceLdProcessInfo(String apCatCode,String ledgerId) throws Exception {
		HashMap<String,String> returnMap = null;
		try {
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("apCatCode",apCatCode);
			map.put("ledgerId",ledgerId);
			
			returnMap = apInvoiceBaseMapper.getInvoiceLdProcessInfo(map);
		} catch (Exception e) {
			throw e;
		}
		return returnMap;
	}
	
	/*
	 * 
	  * <p>Title getInvoiceProcessInfo</p>
	  * <p>Description 查询采购发票相关流程信息</p>
	  * @param apCatCode
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.ApInvoiceBaseDao#getInvoiceProcessInfo(java.lang.String)
	 */
	@Override
	public HashMap<String, String> getInvoiceProcessInfo(String apCatCode)throws Exception {
		HashMap<String,String> returnMap = null;
		try {
			returnMap = apInvoiceBaseMapper.getInvoiceProcessInfo(apCatCode);
		} catch (Exception e) {
			throw e;
		}
		return returnMap;
	}
	/*
	 * 
	  * <p>Title signOrCancelApInvoice</p>
	  * <p>Description 对采购发票进行签收/取消签收处理</p>
	  * @param apInvoiceHList
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.ApInvoiceBaseDao#signOrCancelApInvoice(java.util.List)
	 */
	@Override
	public void signOrCancelApInvoice(List<ApInvoiceHBean> apInvoiceHList) throws Exception {
		try {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", apInvoiceHList);
			apInvoiceBaseMapper.signOrCancelApInvoice(map);
		} catch (Exception e) {
			throw e;
		}
	}
	/*
	 * 
	  * <p>Title cancelFinApInvoice</p>
	  * <p>Description 对采购发票进行取消复核处理</p>
	  * @param apInvoiceHList
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.ApInvoiceBaseDao#cancelFinApInvoice(java.util.List)
	 */
	@Override
	public void cancelFinApInvoice(List<ApInvoiceHBean> apInvoiceHList) throws Exception {
		try {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", apInvoiceHList);
			apInvoiceBaseMapper.cancelFinApInvoice(map);
		} catch (Exception e) {
			throw e;
		}
	}
}