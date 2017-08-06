/**
 * @Title:ApPayBaseDaoImpl.java
 * @package:com.xzsoft.xc.ap.dao.impl
 * @Description:TODO
 * @date:2016年8月5日下午6:04:50
 * @version V1.0
 */
package com.xzsoft.xc.ap.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.ap.dao.ApPayBaseDao;
import com.xzsoft.xc.ap.mapper.ApPayBaseMapper;
import com.xzsoft.xc.ap.modal.ApCancelHBean;
import com.xzsoft.xc.ap.modal.ApCancelLBean;
import com.xzsoft.xc.ap.modal.ApPayHBean;
import com.xzsoft.xc.ap.modal.ApPayLBean;
import com.xzsoft.xc.apar.modal.ApInvTransBean;
import com.xzsoft.xip.platform.util.PlatformUtil;

/**
 * @ClassName: ApPayBaseDaoImpl
 * @Description: 付款单相关操作
 * @author:zhenghy
 * @date: 2016年8月5日 下午6:04:50
 */
@Repository("apPayBaseDao")
public class ApPayBaseDaoImpl implements ApPayBaseDao {

	@Resource
	private ApPayBaseMapper apPayBaseMapper;
	
	/* (non-Javadoc)
	 * <p>Title: selectApPayHsByIds</p>
	 * <p>Description: 查询付款单</p>
	 * @param list
	 * @return
	 * @throws Exception
	 * @see com.xzsoft.xc.ap.dao.ApPayBaseDao#selectApPayHsByIds(java.util.List)
	 */
	@Override
	public List<ApPayHBean> selectApPayHsByIds(List<String> list) throws Exception {
		if ( list!=null &&  list.size()>0) {
			return apPayBaseMapper.selectApPayHsByIds(list);
		}
		return null;
	}
	/* (non-Javadoc)
	 * <p>Title: selectApPayLsByIds</p>
	 * <p>Description: 查询付款单明细</p>
	 * @param list
	 * @return
	 * @throws Exception
	 * @see com.xzsoft.xc.ap.dao.ApPayBaseDao#selectApPayLsByIds(java.util.List)
	 */
	@Override
	public List<ApPayLBean> selectApPayLsByIds(List<String> list) throws Exception {
		if ( list!=null &&  list.size()>0) {
			return apPayBaseMapper.selectApPayLsByIds(list);
		}
		return null;
	}
	/* (non-Javadoc)
	 * <p>Title: insertApPayH</p>
	 * <p>Description: </p>
	 * @param apPayHBean
	 * @throws Exception
	 * @see com.xzsoft.xc.ap.dao.ApPayBaseDao#insertApPayH(com.xzsoft.xc.ap.modal.ApPayHBean)
	 */
	@Override
	public void insertApPayH(ApPayHBean apPayHBean) throws Exception {
		apPayBaseMapper.insertApPayH(apPayHBean);
	}
	/* (non-Javadoc)
	 * <p>Title: updateApPayH</p>
	 * <p>Description: </p>
	 * @param apPayHBean
	 * @throws Exception
	 * @see com.xzsoft.xc.ap.dao.ApPayBaseDao#updateApPayH(com.xzsoft.xc.ap.modal.ApPayHBean)
	 */
	@Override
	public void updateApPayH(ApPayHBean apPayHBean) throws Exception {
		apPayBaseMapper.updateApPayH(apPayHBean);
		
	}
	/* (non-Javadoc)
	 * <p>Title: deleteApPayH</p>
	 * <p>Description: </p>
	 * @param ap_pay_h_id
	 * @throws Exception
	 * @see com.xzsoft.xc.ap.dao.ApPayBaseDao#deleteApPayH(java.lang.String)
	 */
	@Override
	public void deleteApPayH(String ap_pay_h_id) throws Exception {
		apPayBaseMapper.deleteApPayH(ap_pay_h_id);
	}
	/* (non-Javadoc)
	 * <p>Title: deleteApPayH</p>
	 * <p>Description: </p>
	 * @param apPayHBean
	 * @throws Exception
	 * @see com.xzsoft.xc.ap.dao.ApPayBaseDao#deleteApPayH(com.xzsoft.xc.ap.modal.ApPayHBean)
	 */
	@Override
	public void deleteApPayH(ApPayHBean apPayHBean) throws Exception {
		apPayBaseMapper.deleteApPayH(apPayHBean.getAP_PAY_H_ID());
	}
	/* (non-Javadoc)
	 * <p>Title: deleteApPayHs</p>
	 * <p>Description: 删除付款单</p>
	 * @param list
	 * @throws Exception
	 * @see com.xzsoft.xc.ap.dao.ApPayBaseDao#deleteApPayHs(java.util.List)
	 */
	@Override
	public void deleteApPayHs(List<String> list) throws Exception {
		if ( list!=null &&  list.size()>0){
			apPayBaseMapper.deleteApPayHs(list);
		}
	}
	/* (non-Javadoc)
	 * <p>Title: insertApPayLs</p>
	 * <p>Description: 插入付款单行</p>
	 * @param list
	 * @throws Exception
	 * @see com.xzsoft.xc.ap.dao.ApPayBaseDao#insertApPayLs(java.util.List)
	 */
	@Override
	public void insertApPayLs(List<ApPayLBean> list) throws Exception {
		if ( list!=null &&  list.size()>0) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", list);
			apPayBaseMapper.insertApPayLs(map);
		}
		
	}
	/* (non-Javadoc)
	 * <p>Title: updateApPayLs</p>
	 * <p>Description: 修改付款单行</p>
	 * @param list
	 * @throws Exception
	 * @see com.xzsoft.xc.ap.dao.ApPayBaseDao#updateApPayLs(java.util.List)
	 */
	@Override
	public void updateApPayLs(List<ApPayLBean> list) throws Exception {
		if ( list!=null &&  list.size()>0) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", list);
			apPayBaseMapper.updateApPayLs(map);
		}
		
	}
	/* (non-Javadoc)
	 * <p>Title: deleteApPayls</p>
	 * <p>Description: 删除付款单行</p>
	 * @param list
	 * @throws Exception
	 * @see com.xzsoft.xc.ap.dao.ApPayBaseDao#deleteApPayls(java.util.List)
	 */
	@Override
	public void deleteApPayls(List<String> list) throws Exception {
		if ( list!=null &&  list.size()>0) {
			apPayBaseMapper.deleteApPayLs(list);
		}
		
	}
	/* (non-Javadoc)
	 * <p>Title: deleteApPayLs</p>
	 * <p>Description: 删除付款单行</p>
	 * @param list
	 * @throws Exception
	 * @see com.xzsoft.xc.ap.dao.ApPayBaseDao#deleteApPayLs(java.util.List)
	 */
	@Override
	public void deleteApPayLs(List<ApPayLBean> list) throws Exception {
		if ( list!=null &&  list.size()>0) {
			List<String> list1 = new ArrayList<String>();
			for (int i = 0; i < list.size(); i++) {
				list1.add(list.get(i).getAP_PAY_L_ID());
			}
			apPayBaseMapper.deleteApPayLs(list1);
		}
		
	}
	/*
	 * (non-Javadoc)
	 * <p>Title: updateApPayLAmount</p> 
	 * <p>Description: 修改付款单金额</p> 
	 * @param opType
	 * @param apPayLBean
	 * @throws Exception 
	 * @see com.xzsoft.xc.ap.dao.ApPayBaseDao#updateApPayLAmount(java.lang.String, com.xzsoft.xc.ap.modal.ApPayLBean)
	 */
	public void updateApPayLCancelAmt(String opType,List<ApPayLBean> apPayLBeanList) throws Exception{
		if ( apPayLBeanList != null && apPayLBeanList.size() > 0) {//付款单 预付款-红  付款单
			if ("-".equals(opType)) {
				for (int i = 0; i < apPayLBeanList.size(); i++) {
					ApPayLBean bean = apPayLBeanList.get(i);
					bean.setAMOUNT(Math.abs(bean.getAMOUNT())*(-1));
				}
			}else{
				for (int i = 0; i < apPayLBeanList.size(); i++) {
					ApPayLBean bean = apPayLBeanList.get(i);
					bean.setAMOUNT(Math.abs(bean.getAMOUNT()));
				}
			}
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", apPayLBeanList);
			apPayBaseMapper.updateApPayLCancelAmt(map);
		} 
	}
	/*
	 * (non-Javadoc)
	 * <p>Title: ifPayIdExistInPayL</p> 
	 * <p>Description: 付款单，若被引用，查询出引用记录</p> 
	 * @param AP_PAY_H_ID
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.ap.dao.ApPayBaseDao#ifPayIdExistInPayL(java.lang.String)
	 */
	@Override
	public List<ApPayLBean> ifPayIdExistInPayL(String AP_PAY_H_ID) throws Exception{
		return apPayBaseMapper.ifPayIdExistInPayL(AP_PAY_H_ID);
	}
	/*
	 * (non-Javadoc)
	 * <p>Title: ifPayIdExistInCancelH</p> 
	 * <p>Description: 查询出付款单被核销单主引用的记录</p> 
	 * @param AP_PAY_H_ID
	 * @return 
	 * @see com.xzsoft.xc.ap.dao.ApPayBaseDao#ifPayIdExistInCancelH(java.lang.String)
	 */
	@Override
	public List<ApCancelHBean> ifPayIdExistInCancelH(String AP_PAY_H_ID){
		return apPayBaseMapper.ifPayIdExistInCancelH(AP_PAY_H_ID);
	}
	/*
	 * (non-Javadoc)
	 * <p>Title: ifPayIdExistInCancelL</p> 
	 * <p>Description: 查询出付款单被核销单行引用的记录</p> 
	 * @param AP_PAY_H_ID
	 * @return 
	 * @see com.xzsoft.xc.ap.dao.ApPayBaseDao#ifPayIdExistInCancelL(java.lang.String)
	 */
	public List<ApCancelLBean> ifPayIdExistInCancelL(String AP_PAY_H_ID){
		return apPayBaseMapper.ifPayIdExistInCancelL(AP_PAY_H_ID);
	}
	//////////////////////////////////////////应付单相关操作/////////////////////////////////////////////////////////
	/* (non-Javadoc)
	 * <p>Title: updateApInvHPaidAmount</p>
	 * <p>Description: 修改应付单金额</p>
	 * @param list
	 * @throws Exception
	 * @see com.xzsoft.xc.ap.dao.ApPayBaseDao#updateApInvHPaidAmount(java.util.List)
	 */
	@Override
	public void updateApInvHPaidAmount(String opType,List<ApPayLBean> list) throws Exception {
		if ( list != null && list.size() > 0) {
			if("".equals(opType) || "+".equals(opType)){
				// do nothing
			}else if ("-".equals(opType)){
				for (int i = 0; i < list.size(); i++) {
					ApPayLBean apPayLBean = list.get(i);
					apPayLBean.setAMOUNT(apPayLBean.getAMOUNT()*(-1));
				}
			}
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", list);
			apPayBaseMapper.updateApInvHPaidAmount(map);
		}
		
	}
	/*
	 * (non-Javadoc)
	 * <p>Title: updateApInvHPaidAmt_REQ</p> 
	 * <p>Description: 修改应付单付款金额、未付款金额、占用金额</p> 
	 * @param opType
	 * @param list
	 * @throws Exception 
	 * @see com.xzsoft.xc.ap.dao.ApPayBaseDao#updateApInvHPaidAmt_REQ(java.lang.String, java.util.List)
	 */
	@Override
	public void updateApInvHPaidAmt_REQ(String opType,List<ApPayLBean> list)throws Exception{
		if ( list != null && list.size() > 0) {//付款单 采购结算 申请单
			if ("-".equals(opType)) {
				for (int i = 0; i < list.size(); i++) {
					ApPayLBean apPayLBean = list.get(i);
					apPayLBean.setAMOUNT(Math.abs(apPayLBean.getAMOUNT())*(-1));
				}
			}
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", list);
			apPayBaseMapper.updateApInvHPaidAmt_REQ(map);
		} 
	}
	/* (non-Javadoc)
	 * <p>Title: updateApInvHOccupyAmt</p>
	 * <p>Description: 修改应付单占用金额</p>
	 * @param opType
	 * @param list
	 * @throws Exception
	 * @see com.xzsoft.xc.ap.dao.ApPayBaseDao#updateApInvHOccupyAmt(java.lang.String, java.util.List)
	 */
	@Override
	public void updateApInvHOccupyAmt(String opType, List<ApPayLBean> list) throws Exception {
		if ( list != null && list.size() > 0) {
			if ("-".equals(opType)){
				for (int i = 0; i < list.size(); i++) {
					ApPayLBean apPayLBean = list.get(i);
					apPayLBean.setAMOUNT(apPayLBean.getAMOUNT()*(-1));
				}
			}
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", list);
			apPayBaseMapper.updateApInvHOccupyAmt(map);
		}
		
		
	}
	////////////////////////////////////////申请单相关操作///////////////////////////////////////////////////////
	/* (non-Javadoc)
	 * <p>Title: updateApPayReqLPaidAmount</p>
	 * <p>Description: </p>
	 * @param opType
	 * @param list
	 * @throws Exception
	 * @see com.xzsoft.xc.ap.dao.ApPayBaseDao#updateApPayReqLPaidAmount(java.lang.String, java.util.List)
	 */
	@Override
	public void updateApPayReqLPaidAmount(String opType,List<ApPayLBean> list)throws Exception {
		if ( list != null && list.size() > 0) {
			if ("-".equals(opType)){
				for (int i = 0; i < list.size(); i++) {
					ApPayLBean apPayLBean = list.get(i);
					apPayLBean.setAMOUNT(Math.abs(apPayLBean.getAMOUNT())*(-1));
				}
			}else{
				for (int i = 0; i < list.size(); i++) {
					ApPayLBean apPayLBean = list.get(i);
					apPayLBean.setAMOUNT(Math.abs(apPayLBean.getAMOUNT()));
				}
			}
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", list);
			apPayBaseMapper.updateApPayReqLPaidAmount(map);
		}
		
		
	}
	/* (non-Javadoc)
	 * <p>Title: updateApPayReqHPaidAmount</p>
	 * <p>Description: 修改申请单主付款金额</p>
	 * @param opType
	 * @param list
	 * @throws Exception
	 * @see com.xzsoft.xc.ap.dao.ApPayBaseDao#updateApPayReqHPaidAmount(java.lang.String, java.util.List)
	 */
	@Override
	public void updateApPayReqHPaidAmount(String opType, List<ApPayLBean> list)	throws Exception {
		if ( list != null && list.size() > 0) {
			if ("-".equals(opType)){
				for (int i = 0; i < list.size(); i++) {
					ApPayLBean apPayLBean = list.get(i);
					apPayLBean.setAMOUNT(Math.abs(apPayLBean.getAMOUNT())*(-1));
				}
			}else{
				for (int i = 0; i < list.size(); i++) {
					ApPayLBean apPayLBean = list.get(i);
					apPayLBean.setAMOUNT(Math.abs(apPayLBean.getAMOUNT()));
				}
			}
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", list);
			apPayBaseMapper.updateApPayReqHPaidAmount(map);
		}
		
		
	}
	
	////////////////////////////////////////交易明细相关操作///////////////////////////////////////////////////////
	/* (non-Javadoc)
	 * <p>Title: insertTrans</p>
	 * <p>Description: 插入明细</p>
	 * @param list
	 * @throws Exception
	 * @see com.xzsoft.xc.ap.dao.ApPayBaseDao#insertTrans(java.util.List)
	 */
	@Override
	public void insertTrans(List<ApInvTransBean> list) throws Exception {
		if ( list != null && list.size() > 0) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", list);
			apPayBaseMapper.insertTrans(map);
		}
		
	}
	/* (non-Javadoc)
	 * <p>Title: deleteTrans</p>
	 * <p>Description: 删除明细</p>
	 * @param list
	 * @throws Exception
	 * @see com.xzsoft.xc.ap.dao.ApPayBaseDao#deleteTrans(java.util.List)
	 */
	@Override
	public void deleteTrans(List<ApInvTransBean> list) throws Exception {
		if ( list != null && list.size() > 0) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", list);
			apPayBaseMapper.deleteTrans(map);
		}
		
	}
	/* (non-Javadoc)
	 * <p>Title: deleteTransByPayLs</p>
	 * <p>Description: 删除明细</p>
	 * @param list
	 * @throws Exception
	 * @see com.xzsoft.xc.ap.dao.ApPayBaseDao#deleteTransByPayLs(java.util.List)
	 */
	@Override
	public void deleteTransByPayLs(List<ApPayLBean> list) throws Exception {
		if ( list != null && list.size() > 0) {
			List<ApInvTransBean> apInvTransBeanList = new ArrayList<ApInvTransBean>();//交易明细集合
			for (int i = 0; i < list.size(); i++) {
				ApPayLBean apPayLBean = list.get(i);
//				String ap_pay_req_h_id = apPayLBean.getAP_PAY_REQ_H_ID();//申请单id
//				String ap_inv_gl_h_id = apPayLBean.getAP_INV_GL_H_ID();//应付单id
				String ap_pay_h_id = apPayLBean.getAP_PAY_H_ID();//付款单id
				String ap_pay_l_id = apPayLBean.getAP_PAY_L_ID();//付款单行id
				ApInvTransBean apInvTransBean = new ApInvTransBean();
				apInvTransBean.setSOURCE_ID(ap_pay_h_id);
				apInvTransBean.setSOURCE_DTL_ID(ap_pay_l_id);
				apInvTransBean.setSOURCE_TAB("XC_AP_PAY_H");
//				//判断来源单据类型
//				if (!("".equals(ap_pay_req_h_id) || ap_pay_req_h_id == null)) {//来源是申请单
//					
//				} else if(!("".equals(ap_inv_gl_h_id) || ap_inv_gl_h_id == null)){//来源是应付单
//					
//				}
				apInvTransBeanList.add(apInvTransBean);
			}
			if (apInvTransBeanList.size() > 0) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("dbType", PlatformUtil.getDbType());
				map.put("list", apInvTransBeanList);
				apPayBaseMapper.deleteTrans(map);
			}
		}
		
	}
	/*
	 * (non-Javadoc)
	 * <p>Title: calculateApPayLCancelAmt</p> 
	 * <p>Description: 计算预付款单核销金额</p> 
	 * @param map 
	 * @see com.xzsoft.xc.ap.dao.ApPayBaseDao#calculateApPayLCancelAmt(java.util.HashMap)
	 */
	@Override
	public void calculateApPayLCancelAmt(String AP_PAY_H_ID , double cancelAmt) throws Exception{
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("AP_PAY_H_ID", AP_PAY_H_ID);
		map.put("CANCEL_AMT", cancelAmt);
		apPayBaseMapper.calculateApPayLCancelAmt(map);
	}
	/*
	 * (non-Javadoc)
	 * <p>Title: selectApDocCatCodeByApPayHId</p> 
	 * <p>Description: 根据id查询付款单单据类型</p> 
	 * @param AP_PAY_H_ID
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.ap.dao.ApPayBaseDao#selectApDocCatCodeByApPayHId(java.lang.String)
	 */
	@Override
	public String selectApDocCatCodeByApPayHId(String AP_PAY_H_ID) throws Exception {
		return apPayBaseMapper.selectApDocCatCodeByApPayHId(AP_PAY_H_ID);
	}
	
	
	
	
}
