package com.xzsoft.xc.ap.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.ap.dao.ApCancelDao;
import com.xzsoft.xc.ap.dao.XCAPCommonDao;
import com.xzsoft.xc.ap.mapper.ApCancelMapper;
import com.xzsoft.xc.ap.modal.ApCancelHBean;
import com.xzsoft.xc.ap.modal.ApCancelLBean;
import com.xzsoft.xc.ap.util.ApIDAmountBean;
import com.xzsoft.xc.ap.util.XCAPConstants;
import com.xzsoft.xc.apar.modal.ApDocumentHBean;
import com.xzsoft.xc.apar.modal.ApInvTransBean;
import com.xzsoft.xc.apar.modal.ArInvTransBean;
import com.xzsoft.xip.platform.util.PlatformUtil;
/**
 * @ClassName: ApCancelDaoImpl 
 * @Description: 核销单相关操作
 * @author zhenghy
 * @date 2016年8月19日 下午3:31:10
 */
@Repository("apCancelDao")
public class ApCancelDaoImpl implements ApCancelDao {

	@Resource
	private ApCancelMapper mapper;
	@Resource
	private XCAPCommonDao xcapCommonDao;
	
	/*
	 * (non-Javadoc)
	 * <p>Title: selectCancelH</p> 
	 * <p>Description: 查询核销单信息</p> 
	 * @param cancelHIdList
	 * @throws Exception 
	 * @see com.xzsoft.xc.ap.dao.ApCancelDao#selectCancelH(java.util.List)
	 */
	@Override
	public List<ApCancelHBean> selectCancelH(List<String> cancelHIdList) throws Exception{
		return mapper.selectCancelH(cancelHIdList);
	}
	/*
	 * (non-Javadoc)
	 * <p>Title: selectCancelL</p> 
	 * <p>Description: 查询核销单行信息</p> 
	 * @param cancelHIdList
	 * @throws Exception 
	 * @see com.xzsoft.xc.ap.dao.ApCancelDao#selectCancelL(java.util.List)
	 */
	@Override
	public List<ApCancelLBean> selectCancelLByCancelHId(String ap_cancel_h_id) throws Exception{
		return mapper.selectCancelLByCancelHId(ap_cancel_h_id);
	}
	/*
	 * (non-Javadoc)
	 * <p>Title: insertCancelH</p> 
	 * <p>Description: 新增核销单</p> 
	 * @param apCancelHBean 
	 * @see com.xzsoft.xc.ap.dao.ApCancelDao#insertCancelH(com.xzsoft.xc.ap.modal.ApCancelHBean)
	 */
	@Override
	public void insertCancelH(ApCancelHBean apCancelHBean) throws Exception{
		mapper.insertCancelH(apCancelHBean);
	}
	/*
	 * (non-Javadoc)
	 * <p>Title: updateCancelH</p> 
	 * <p>Description: 修改核销单</p> 
	 * @param apCancelHBean 
	 * @see com.xzsoft.xc.ap.dao.ApCancelDao#updateCancelH(com.xzsoft.xc.ap.modal.ApCancelHBean)
	 */
	@Override
	public void updateCancelH(ApCancelHBean apCancelHBean) throws Exception{
		mapper.updateCancelH(apCancelHBean);
	}


	/*
	 * (non-Javadoc)
	 * <p>Title: deleteCancelHs</p> 
	 * <p>Description: 批量删除核销单</p> 
	 * @param list 
	 * @see com.xzsoft.xc.ap.dao.ApCancelDao#deleteCancelHs(java.util.List)
	 */
	@Override
	public void deleteCancelHs(List<String> list) throws Exception{
		if (list.size() > 0 && list != null) {
			mapper.deleteCancelHs(list);
		}
	}

	/*
	 * (non-Javadoc)
	 * <p>Title: insertCancelLs</p> 
	 * <p>Description: 批量新增核销单行</p> 
	 * @param apCancelLList
	 * @throws Exception 
	 * @see com.xzsoft.xc.ap.dao.ApCancelDao#insertCancelLs(java.util.List)
	 */
	@Override
	public void insertCancelLs(List<ApCancelLBean> list) throws Exception {
		if (list.size() > 0 && list != null) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", list);
			mapper.insertCancelLs(map);
		}
	}
	/*
	 * (non-Javadoc)
	 * <p>Title: updateCancelLs</p> 
	 * <p>Description: 批量修改核销单行</p> 
	 * @param apCancelLList
	 * @throws Exception 
	 * @see com.xzsoft.xc.ap.dao.ApCancelDao#updateCancelLs(java.util.List)
	 */
	@Override
	public void updateCancelLs(List<ApCancelLBean> list) throws Exception {
		if (list.size() > 0 && list != null) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", list);
			mapper.updateCancelLs(map);
		}
		
	}
	/*
	 * (non-Javadoc)
	 * <p>Title: deleteCancelLs</p> 
	 * <p>Description: 批量删除核销单行</p> 
	 * @param list
	 * @throws Exception 
	 * @see com.xzsoft.xc.ap.dao.ApCancelDao#deleteCancelLs(java.util.List)
	 */
	@Override
	public void deleteCancelLs(List<ApCancelLBean> list) throws Exception {
		if (list.size() > 0 && list != null) {
			List<String> list1 = new ArrayList<String>();
			for (int i = 0; i < list.size(); i++) {
				list1.add(list.get(i).getAP_CANCEL_L_ID());
			}
			mapper.deleteCancelLs(list1);
		}
	}
	/*
	 * (non-Javadoc)
	 * <p>Title: deleteCancelLsByCancelHIds</p> 
	 * <p>Description: 批量删除核销单行（根据核销单id）</p> 
	 * @param cancelHIdList
	 * @throws Exception 
	 * @see com.xzsoft.xc.ap.dao.ApCancelDao#deleteCancelLsByCancelHIds(java.util.List)
	 */
	@Override
	public void deleteCancelLsByCancelHIds(List<String> cancelHIdList) throws Exception{
		if (cancelHIdList.size() > 0 && cancelHIdList != null) {
			mapper.deleteCancelLsByCancelHIds(cancelHIdList);
		}
	}
	
	///////////////////////////////////////////应付单相关操作/////////////////////////////////////////////////////////////
	/**
	 * @Title: updateInvHCancelAmt
	 * @Description: 修改行上面应付单的金额
	 * @param opTyp
	 * @param apCancelLBeanList
	 * @throws Exception  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void updateInvHCancelAmt(String opType,List<ApCancelLBean> apCancelLBeanList)throws Exception{
		if(apCancelLBeanList.size() > 0 && apCancelLBeanList != null){
			List<ApIDAmountBean> list = new ArrayList<ApIDAmountBean>();
			for (int i = 0; i < apCancelLBeanList.size(); i++) {
				ApCancelLBean apCancelLBean = apCancelLBeanList.get(i);
				ApIDAmountBean bean = new ApIDAmountBean();
				bean.setAP_DOC_ID(apCancelLBean.getTARGET_ID());
				if ("-".equals(opType)) {
					bean.setAMOUNT(apCancelLBean.getTARGET_AMT()*(-1));
				}else{
					bean.setAMOUNT(apCancelLBean.getTARGET_AMT());
				}
				list.add(bean);
			}
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", list);
			mapper.updateInvCancelAmt(map);
		}
	}
	/*
	 * (non-Javadoc)
	 * <p>Title: updateInvHCancelAmt</p>
	 * <p>Description: 修改应付单的金额</p> 
	 * @param opType 增行"+",删行"-"
	 * @param apCancelHBean 核销单主信息
	 * @param apCancelLBeanList 核销单行信息集合
	 * @param hl  头上应付单"H" 行上应付单"L"
	 * @throws Exception 
	 * @see com.xzsoft.xc.ap.dao.ApCancelDao#updateInvHCancelAmt(java.lang.String, java.lang.String, java.util.List)
	 */
	public void updateInvHCancelAmt(String opType,ApCancelHBean apCancelHBean,List<ApCancelLBean> apCancelLBeanList,String hl)throws Exception{
		if(apCancelLBeanList.size() > 0 && apCancelLBeanList != null){
			
			List<ApIDAmountBean> list = new ArrayList<ApIDAmountBean>();//单据id  和  单据金额  集合
			String ap_inv_gl_h_id;//应付单id
			
			for (int i = 0; i < apCancelLBeanList.size(); i++) {
				ApCancelLBean apCancelLBean = apCancelLBeanList.get(i);//核销单行
				
				if ("H".equals(hl)) {// "H"
					ap_inv_gl_h_id = apCancelHBean.getSRC_ID();// 头上的
				}else{// "L"
					ap_inv_gl_h_id = apCancelLBean.getTARGET_ID();// 行上的
				}
				
				ApDocumentHBean apDocumentHBean = xcapCommonDao.getApInvGlH(ap_inv_gl_h_id);//查找应付单
				String invType = apDocumentHBean.getAP_DOC_CAT_CODE();//应付单单据类型
				
				ApIDAmountBean bean = new ApIDAmountBean();
				bean.setAP_DOC_ID(ap_inv_gl_h_id);
				
				if (XCAPConstants.YFD_YFD.equals(invType)) {
					bean.setAMOUNT(Math.abs(apCancelLBean.getTARGET_AMT()));
				}else if(XCAPConstants.YFD_YFD_H.equals(invType)){
					bean.setAMOUNT(Math.abs(apCancelLBean.getTARGET_AMT())*(-1));
				}
				if ("-".equals(opType)) {
					bean.setAMOUNT(bean.getAMOUNT()*(-1));
				}
				list.add(bean);
			}
			
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", list);
			mapper.updateInvCancelAmt(map);
		}
		
	}
	///////////////////////////////////////////付款单相关操作/////////////////////////////////////////////////////////////
	/*
	 * (non-Javadoc)
	 * <p>Title: updateInvHCancelAmt</p> ---------------------
	 * <p>Description: 修改主上面付款单的金额</p> 
	 * @param opType
	 * @param apCancelHBean
	 * @param apCancelLBeanList
	 * @throws Exception 
	 * @see com.xzsoft.xc.ap.dao.ApCancelDao#updateInvHCancelAmt(java.lang.String, java.lang.String, java.util.List)
	 */
	@Override
	public void updatePayCancelAmt(String opType,ApCancelHBean apCancelHBean,List<ApCancelLBean> apCancelLBeanList) throws Exception {
		if (apCancelLBeanList.size() > 0 && apCancelLBeanList != null) {
			
			String srcId = apCancelHBean.getSRC_ID();//来源单据id
			String cancelType = apCancelHBean.getAP_CANCEL_TYPE();//核销类型
			List<ApIDAmountBean> list = new ArrayList<ApIDAmountBean>();//id amount集合
			
			for (int i = 0; i < apCancelLBeanList.size(); i++) {
				ApCancelLBean apCancelLBean = apCancelLBeanList.get(i);
				double targetAmt = Math.abs(apCancelLBean.getTARGET_AMT());
				ApIDAmountBean bean = new ApIDAmountBean();//id amount
				bean.setAP_DOC_ID(srcId);
				
				if (XCAPConstants.HXD_YUFHLDC.equals(cancelType)) {//预付红蓝对冲，预付红单的金额为负数
					targetAmt = targetAmt * (-1);
				}
				
				if ("-".equals(opType)) {
					bean.setAMOUNT(targetAmt*(-1));
				}else{
					bean.setAMOUNT(targetAmt);
				}
				
				list.add(bean);
			}
			
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", list);
			mapper.updatePayCancelAmt(map);
		}
	}
	/*
	 * (non-Javadoc)
	 * <p>Title: updateInvHCancelAmt</p> ---------------------
	 * <p>Description: 修改行上面付款单的金额</p> 
	 * @param opType
	 * @param ap_pay_h_id
	 * @param apCancelLBeanList
	 * @throws Exception 
	 * @see com.xzsoft.xc.ap.dao.ApCancelDao#updateInvHCancelAmt(java.lang.String, java.lang.String, java.util.List)
	 */
	@Override
	public void updatePayCancelAmt(String opType,List<ApCancelLBean> apCancelLBeanList) throws Exception {
		if (apCancelLBeanList.size() > 0 && apCancelLBeanList != null) {
			List<ApIDAmountBean> list = new ArrayList<ApIDAmountBean>();
			for (int i = 0; i < apCancelLBeanList.size(); i++) {
				ApCancelLBean apCancelLBean = apCancelLBeanList.get(i);
				ApIDAmountBean bean = new ApIDAmountBean();
				bean.setAP_DOC_ID(apCancelLBean.getTARGET_ID());
				if ("-".equals(opType)) {
					bean.setAMOUNT(apCancelLBean.getTARGET_AMT()*(-1));
				}else{
					bean.setAMOUNT(apCancelLBean.getTARGET_AMT());
				}
				list.add(bean);
			}
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", list);
			mapper.updatePayCancelAmt(map);
		}
		
	}
	
	///////////////////////////////////////////应收单相关操作/////////////////////////////////////////////////////////////
	/*
	 * (non-Javadoc)
	 * <p>Title: updateArInvCancelAmt</p> 
	 * <p>Description: 批量修改应收单核销金额、余额</p> 
	 * @param opType
	 * @param apCancelLBeanList
	 * @throws Exception 
	 * @see com.xzsoft.xc.ap.dao.ApCancelDao#updateArInvCancelAmt(java.lang.String, java.util.List)
	 */
	@Override
	public void updateArInvCancelAmt(String opType,List<ApCancelLBean> apCancelLBeanList) throws Exception {
		if (apCancelLBeanList.size() > 0 && apCancelLBeanList != null) {
			List<ApIDAmountBean> list = new ArrayList<ApIDAmountBean>();
			for (int i = 0; i < apCancelLBeanList.size(); i++) {
				ApCancelLBean apCancelLBean = apCancelLBeanList.get(i);
				ApIDAmountBean bean = new ApIDAmountBean();
				bean.setAP_DOC_ID(apCancelLBean.getTARGET_ID());
				if ("-".equals(opType)) {
					bean.setAMOUNT(apCancelLBean.getTARGET_AMT()*(-1));
				}else{
					bean.setAMOUNT(apCancelLBean.getTARGET_AMT());
				}
				list.add(bean);
			}
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", list);
			mapper.updateArInvCancelAmt(map);
		}
	}
	
	//////////////////////////////////////////应付交易明细相关操作/////////////////////////////////////////////////////////////	
	/*
	 * (non-Javadoc)
	 * <p>Title: insertApTrans</p> 
	 * <p>Description: 批量新增交易明细</p> 
	 * @throws Exception 
	 * @see com.xzsoft.xc.ap.dao.ApCancelDao#insertApTrans()
	 */
	@Override
	public void insertApTrans(List<ApInvTransBean> apInvTransBeanList) throws Exception {
		if (apInvTransBeanList.size() > 0 && apInvTransBeanList != null) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", apInvTransBeanList);
			mapper.insertApTrans(map);
		}
	}
	/*
	 * (non-Javadoc)
	 * <p>Title: deleteApTrans</p> 
	 * <p>Description: 批量删除交易明细（根据核销单行信息）</p> 
	 * @throws Exception 
	 * @see com.xzsoft.xc.ap.dao.ApCancelDao#deleteApTrans()
	 */
	@Override
	public void deleteApTransByCancelLs(List<ApCancelLBean> apCancelLBeanList) throws Exception {
		if (apCancelLBeanList.size() > 0 && apCancelLBeanList != null) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", apCancelLBeanList);
			mapper.deleteApTransByCancelLs(map);
		}
	}
	/*
	 * (non-Javadoc)
	 * <p>Title: deleteApTransBycancelHId</p> 
	 * <p>Description: 批量删除交易明细</p> 
	 * @param apCancelHBeanList 核销单主集合
	 * @throws Exception 
	 * @see com.xzsoft.xc.ap.dao.ApCancelDao#deleteApTransBycancelHId(java.lang.String)
	 */
	@Override
	public void deleteApTransByCancelHs(List<ApCancelHBean> apCancelHBeanList) throws Exception{
		if (apCancelHBeanList.size() > 0 && apCancelHBeanList != null) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", apCancelHBeanList);
			mapper.deleteApTransByCancelHs(map);
		}
	}
	
	//////////////////////////////////////////应收交易明细相关操作/////////////////////////////////////////////////////////////	
	/*
	 * (non-Javadoc)
	 * <p>Title: insertARTrans</p> 
	 * <p>Description: 批量插入应收单交易明细</p> 
	 * @param apInvTransBeanList
	 * @throws Exception 
	 * @see com.xzsoft.xc.ap.dao.ApCancelDao#insertARTrans(java.util.List)
	 */
	@Override
	public void insertArTrans(List<ArInvTransBean> arInvTransBeanList) throws Exception {
		if (arInvTransBeanList.size() > 0 && arInvTransBeanList != null) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", arInvTransBeanList);
			mapper.insertArTrans(map);
		}
		
	}
	/*
	 * (non-Javadoc)
	 * <p>Title: deleteARTrans</p> 
	 * <p>Description: 批量删除应收交易明细</p> 
	 * @param apCancelLBeanList 核销单行信息集合
	 * @throws Exception 
	 * @see com.xzsoft.xc.ap.dao.ApCancelDao#deleteARTrans(java.util.List)
	 */
	@Override
	public void deleteArTransByCancelLs(List<ApCancelLBean> apCancelLBeanList) throws Exception {
		if (apCancelLBeanList.size() > 0 && apCancelLBeanList != null) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", apCancelLBeanList);
			mapper.deleteArTransByCancelLs(map);
		}
		
	}
	
}
