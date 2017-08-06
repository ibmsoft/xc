/**
 * @Title:ArPayBaseDaoImpl.java
 * @package:com.xzsoft.xc.ar.dao.impl
 * @Description:TODO
 * @date:2016年7月11日上午9:13:11
 * @version V1.0
 */
package com.xzsoft.xc.ar.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.apar.modal.ArInvTransBean;
import com.xzsoft.xc.ar.dao.ArPayBaseDao;
import com.xzsoft.xc.ar.mapper.ArPayBaseMapper;
import com.xzsoft.xc.ar.modal.ArCancelHBean;
import com.xzsoft.xc.ar.modal.ArCancelLBean;
import com.xzsoft.xc.ar.modal.ArPayHBean;
import com.xzsoft.xc.ar.modal.ArPayLBean;
import com.xzsoft.xc.ar.util.XCARConstants;
import com.xzsoft.xip.platform.util.PlatformUtil;

/**
 * @ClassName: ArPayBaseDaoImpl
 * @Description: TODO
 * @author:zhenghy
 * @date 2016年7月11日 上午9:13:11
 */
@Repository("ArPayBaseDao")
public class ArPayBaseDaoImpl implements ArPayBaseDao {

	@Resource
	ArPayBaseMapper arPayBaseMapper;


	/*
	 * (non-Javadoc)
	 * <p>Title: insertArPayH2</p> 
	 * <p>Description: 新增收款单主信息</p> 
	 * @param arPayHBean
	 * @throws Exception 
	 * @see com.xzsoft.xc.ar.dao.ArPayBaseDao#insertArPayH2(com.xzsoft.xc.ar.modal.ArPayHBean)
	 */
	public void insertArPayH2(ArPayHBean arPayHBean) throws Exception{
		arPayBaseMapper.insertArPayH(arPayHBean);//新增主信息
	}

	/*
	 * (non-Javadoc)
	 * <p>Title: updateArPayH2</p> 
	 * <p>Description: 修改收款单主信息</p> 
	 * @param arPayHBean
	 * @throws Exception 
	 * @see com.xzsoft.xc.ar.dao.ArPayBaseDao#updateArPayH2(com.xzsoft.xc.ar.modal.ArPayHBean)
	 */
	public void updateArPayH2(ArPayHBean arPayHBean) throws Exception{
		arPayBaseMapper.updateArPayH(arPayHBean);//修改主信息
	}
	/* (non-Javadoc)
	 * <p>Title: deleteArPayH</p>
	 * <p>Description: 删除收款单（包括明细行）</p>
	 * @param list 收款单主id集合
	 * @throws Exception
	 * @see com.xzsoft.xc.ar.dao.ArPayBaseDao#deleteArPayH(java.util.List)
	 */
	@Override
	public void deleteArPayH(List<String> list) throws Exception {

		if (list.size() > 0 && list != null) {
			//先删除主表，在删除行表
			arPayBaseMapper.deleteArPayH(list);
			arPayBaseMapper.deleteArPayLByHIds(list);
		}

	}
	/* (non-Javadoc)
	 * <p>Title: getArPayLs</p>
	 * <p>Description: 得到收款单行信息</p>
	 * @param list
	 * @return
	 * @throws Exception
	 * @return: ArPayBaseDao
	 * @see com.xzsoft.xc.ar.dao.ArPayBaseDao#getArPayLs(java.util.List)
	 */
	@Override
	public List<ArPayLBean> getArPayLs(List<String> list) throws Exception {

		if (list.size() > 0 && list != null)  {
			List<ArPayLBean> arPayLList =  arPayBaseMapper.getArPayLs(list);
			return arPayLList;
		}
			

		return null;
	}


	/*
	 * (non-Javadoc)
	 * <p>Title: insertArPayLs</p> 
	 * <p>Description: 批量新增收款单行</p> 
	 * @param arPayLBeanList
	 * @throws Exception 
	 * @see com.xzsoft.xc.ar.dao.ArPayBaseDao#insertArPayLs(java.util.List)
	 */
	@Override
	public void insertArPayLs(List<ArPayLBean> arPayLBeanList)throws Exception{
		if (arPayLBeanList.size() > 0 && arPayLBeanList != null) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", arPayLBeanList);
			arPayBaseMapper.insertArPayL(map);
		}
	}
	/*
	 * (non-Javadoc)
	 * <p>Title: updateArPayLs</p> 
	 * <p>Description: 批量修改收款单行</p> 
	 * @param arPayLBeanList
	 * @throws Exception 
	 * @see com.xzsoft.xc.ar.dao.ArPayBaseDao#updateArPayLs(java.util.List)
	 */
	@Override
	public void updateArPayLs(List<ArPayLBean> arPayLBeanList)throws Exception{
		if (arPayLBeanList.size() > 0 && arPayLBeanList != null) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", arPayLBeanList);
			arPayBaseMapper.updateArPayL(map);
		}
	}
	/*
	 * (non-Javadoc)
	 * <p>Title: deleteArPayLs</p> 
	 * <p>Description: 批量删除收款单行</p> 
	 * @param arPayLBeanList
	 * @throws Exception 
	 * @see com.xzsoft.xc.ar.dao.ArPayBaseDao#deleteArPayLs(java.util.List)
	 */
	@Override
	public void deleteArPayLs(List<ArPayLBean> arPayLBeanList)throws Exception{
		if (arPayLBeanList.size() > 0 && arPayLBeanList != null) {
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < arPayLBeanList.size(); i++) {
				list.add(arPayLBeanList.get(i).getAR_PAY_L_ID());
			}
			arPayBaseMapper.deleteArPayLByLIds(list);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * <p>Title: updateArInvGLPayAmt</p> 
	 * <p>Description: 修改应收单的收款金额（包括未收款金额）</p> 
	 * @param arPayLBeanList
	 * @throws Exception 
	 * @see com.xzsoft.xc.ar.dao.ArPayBaseDao#updateArInvGLPayAmt(java.util.List)
	 */
	@Override
	public void updateArInvGLPayAmt(String opType,String ar_doc_cat_code,List<ArPayLBean> arPayLBeanList)throws Exception{
		if (arPayLBeanList.size() > 0 && arPayLBeanList != null) {
			if (XCARConstants.SKD_JSK.equals(ar_doc_cat_code)) {//结算款
				if ("-".equals(opType)) {//删除行
					for (int i = 0; i < arPayLBeanList.size(); i++) {
						ArPayLBean bean = arPayLBeanList.get(i);
						bean.setAMOUNT(Math.abs(bean.getAMOUNT())*(-1));
					}
				} else {
					for (int i = 0; i < arPayLBeanList.size(); i++) {
						ArPayLBean bean = arPayLBeanList.get(i);
						bean.setAMOUNT(Math.abs(bean.getAMOUNT()));
					}
				}
			}else if (XCARConstants.SKD_JSK_H.equals(ar_doc_cat_code)) {//结算款红
				if ("-".equals(opType)) {//删除行
					for (int i = 0; i < arPayLBeanList.size(); i++) {
						ArPayLBean bean = arPayLBeanList.get(i);
						bean.setAMOUNT(Math.abs(bean.getAMOUNT()));
					}
				} else {
					for (int i = 0; i < arPayLBeanList.size(); i++) {
						ArPayLBean bean = arPayLBeanList.get(i);
						bean.setAMOUNT(Math.abs(bean.getAMOUNT())*(-1));
					}
				}
			}
			
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", arPayLBeanList);
			arPayBaseMapper.updateArInvGLPayAmt(map);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * <p>Title: updateArPayLCancelAmt</p> 
	 * <p>Description: 修改预收款行核销金额（包括未核销金额）</p> 
	 * @param opType
	 * @param ar_doc_cat_code
	 * @param arPayLBeanList
	 * @throws Exception 
	 * @see com.xzsoft.xc.ar.dao.ArPayBaseDao#updateArPayLCancelAmt(java.lang.String, java.lang.String, java.util.List)
	 */
	@Override
	public void updateArPayLCancelAmt(String opType,String ar_doc_cat_code,List<ArPayLBean> arPayLBeanList)throws Exception{
		if (arPayLBeanList.size() > 0 && arPayLBeanList != null) {
			if (XCARConstants.SKD_YUSK_H.equals(ar_doc_cat_code)) {//预收款红
				if ("-".equals(opType)) {
					for (int i = 0; i < arPayLBeanList.size(); i++) {
						ArPayLBean bean = arPayLBeanList.get(i);
						bean.setAMOUNT(Math.abs(bean.getAMOUNT())*(-1));
					}
				}else{
					for (int i = 0; i < arPayLBeanList.size(); i++) {
						ArPayLBean bean = arPayLBeanList.get(i);
						bean.setAMOUNT(Math.abs(bean.getAMOUNT()));
					}
				}
			}
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", arPayLBeanList);
			arPayBaseMapper.updateArPayLCancelAmt(map);
		}
		
	}
	/*
	 * (non-Javadoc)
	 * <p>Title: insertTrans</p> 
	 * <p>Description: 新增交易明细</p> 
	 * @param arInvTransBeanList
	 * @throws Exception 
	 * @see com.xzsoft.xc.ar.dao.ArPayBaseDao#insertTrans(java.util.List)
	 */
	@Override
	public void insertTrans(List<ArInvTransBean> arInvTransBeanList)throws Exception{
		if (arInvTransBeanList.size() > 0 && arInvTransBeanList != null) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", arInvTransBeanList);
			arPayBaseMapper.insertArTrans(map);
		}
	}
	@Override
	public void updateTrans(List<ArInvTransBean> arInvTransBeanList) throws Exception {
		if (arInvTransBeanList.size() > 0 && arInvTransBeanList != null) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", arInvTransBeanList);
			arPayBaseMapper.updateArTrans(map);
		}
		
	}
	@Override
	public void deleteTrans(List<ArInvTransBean> arInvTransBeanList) throws Exception {
		if (arInvTransBeanList != null && arInvTransBeanList.size() > 0) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", arInvTransBeanList);
			arPayBaseMapper.deleteArTrans(map);
		}
		
	}

	/*
	 * (non-Javadoc)
	 * <p>Title: updateArPayAmt</p> 
	 * <p>Description: 修改收款单金额（预收款红蓝）</p> 
	 * @param arPayIdList
	 * @throws Exception 
	 * @see com.xzsoft.xc.ar.dao.ArPayBaseDao#updateArPayAmt(java.util.List)
	 */
	@Override
	public void updateArPayAmt(List<String> arPayIdList) throws Exception {
		if (arPayIdList != null && arPayIdList.size() > 0 ) {

			for (int i = 0; i < arPayIdList.size(); i++) {
				String arPayHId = arPayIdList.get(i);
				double cancelAmt = 0.00;
				//查询该收款单的单据类型
				String arDocCatCode = arPayBaseMapper.selectArDocCatCodeByArPayHId(arPayHId);
				
				if (XCARConstants.SKD_YUSK.equals(arDocCatCode)) {
					//查询是否被预收款红引用
					List<ArPayLBean> arPayLBeanList = arPayBaseMapper.selectArPayLFromPay(arPayHId);
					if (arPayLBeanList != null && arPayLBeanList.size() > 0) {
						for (int j = 0; j < arPayLBeanList.size(); j++) {
							cancelAmt += Math.abs(arPayLBeanList.get(j).getAMOUNT());
						}
					}
					//查询该是否被核销单行表引用（应收核预收，预收红蓝对红）
					List<ArCancelLBean> arCancelLBeanList = arPayBaseMapper.selectArCancelLFromCancel(arPayHId);
					if (arCancelLBeanList != null && arCancelLBeanList.size() > 0) {
						for (int j = 0; j < arCancelLBeanList.size(); j++) {
							cancelAmt += Math.abs(arCancelLBeanList.get(j).getTARGET_AMT());
						}
					}
				}else if (XCARConstants.SKD_YUSK_H.equals(arDocCatCode)) {
					//查询该id是否被核销单主表引用（预收红蓝对冲，主表引用预收款红）
					List<ArCancelHBean> arCancelHBeanList = arPayBaseMapper.selectArCancelHFromCancel(arPayHId);
					if (arCancelHBeanList != null && arCancelHBeanList.size() > 0) {
						for (int j = 0; j < arCancelHBeanList.size(); j++) {
							cancelAmt += Math.abs(arCancelHBeanList.get(j).getSRC_AMT()) * (-1);
						}
					}
					//查询该是否被核销单行表引用（应收核预收）
					List<ArCancelLBean> arCancelLBeanList = arPayBaseMapper.selectArCancelLFromCancel(arPayHId);
					if (arCancelLBeanList != null && arCancelLBeanList.size() > 0) {
						for (int j = 0; j < arCancelLBeanList.size(); j++) {
							cancelAmt += Math.abs(arCancelLBeanList.get(j).getTARGET_AMT()) * (-1);
						}
					}
				}
				
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("AR_PAY_H_ID", arPayHId);
				map.put("CANCEL_AMT", cancelAmt);
				arPayBaseMapper.calculateArPayAmt(map);
				
			}
		}
		
	}
	
	
}
