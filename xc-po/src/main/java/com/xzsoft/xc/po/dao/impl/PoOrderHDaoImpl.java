package com.xzsoft.xc.po.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xzsoft.xc.po.dao.PoOrderHDao;
import com.xzsoft.xc.po.mapper.PoOrderHMapper;
import com.xzsoft.xc.po.modal.PoOrderHBean;
import com.xzsoft.xc.po.modal.PoOrderHHisBean;
import com.xzsoft.xc.po.modal.PoOrderLBean;
import com.xzsoft.xc.po.modal.PoOrderLHisBean;
import com.xzsoft.xc.po.modal.PoPurApLBean;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;

/** 
 * @ClassName: PoOrderHDaoImpl
 * @Description: 采购订单Dao层实现类
 * @author weicw
 * @date 2016年12月8日 上午9:28:22 
 * 
 */
@Repository("poOrderHDao")
public class PoOrderHDaoImpl implements PoOrderHDao {
	@Resource
	PoOrderHMapper mapper;
	/**
	 * @Title:ifExistH
	 * @Description:判断主表新增还是修改  
	 * 参数格式:
	 * @param  java.lang.String
	 * @return 
	 * @throws 
	 */
	@Override
	public PoOrderHBean ifExistH(String id) {
		return mapper.ifExistH(id);
	}
	/**
	 * @Title:addOrderHInfo
	 * @Description:新增采购订单主表信息   
	 * 参数格式:
	 * @param  com.xzsoft.xc.po.modal.PoOrderHBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void addOrderHInfo(PoOrderHBean bean) {
		mapper.addOrderHInfo(bean);
	}
	/**
	 * @Title:editOrderHInfo
	 * @Description:修改采购订单主表信息 
	 * 参数格式:
	 * @param  com.xzsoft.xc.po.modal.PoOrderHBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void editOrderHInfo(PoOrderHBean bean) {
		mapper.editOrderHInfo(bean);
	}
	/**
	 * @Title:deleteOrderHInfo
	 * @Description:删除采购订单主表信息 
	 * 参数格式:
	 * @param  java.util.List
	 * @return 
	 * @throws 
	 */
	@Override
	public void deleteOrderHInfo(List<String> list) {
		mapper.deleteOrderHInfo(list);

	}
	/**
	 * @Title:updateOrerHStatus
	 * @Description:更改采购订单主信息是否关闭的状态 
	 * 参数格式:
	 * @param  java.util.HashMap
	 * @return 
	 * @throws 
	 */
	@Override
	public void updateOrerHStatus(HashMap<String, Object> map){
		mapper.updateOrerHStatus(map);
	}
	/**
	 * @Title:updateOrerLStatus
	 * @Description:更改采购订单行信息是否关闭的状态 (根据主信息ID)-
	 * 参数格式:
	 * @param  java.util.HashMap
	 * @return 
	 * @throws 
	 */
	@Override
	public void updateOrerLStatus(HashMap<String, Object> map){
		mapper.updateOrerLStatus(map);
	}
	/**
	 * @Title:updateOrerLStatusByLId
	 * @Description:更改采购订单行信息是否关闭的状态 (根据行信息ID)
	 * 参数格式:
	 * @param  java.util.HashMap
	 * @return 
	 * @throws 
	 */
	@Override
	public void updateOrerLStatusByLId(HashMap<String, Object> map){
		mapper.updateOrerLStatusByLId(map);
	}
	/**
	 * @Title:ifExistL
	 * @Description:判断行表新增或修改 
	 * 参数格式:
	 * @param  java.lang.String
	 * @return 
	 * @throws 
	 */
	@Override
	public PoOrderLBean ifExistL(String id) {
		return mapper.ifExistL(id);
	}
	/**
	 * @Title:addOrderLInfo
	 * @Description:新增采购订单行表信息 
	 * 参数格式:
	 * @param  com.xzsoft.xc.po.modal.PoOrderLBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void addOrderLInfo(PoOrderLBean bean) {
		mapper.addOrderLInfo(bean);
	}
	/**
	 * @Title:editOrderLInfo
	 * @Description:修改采购订单行表信息 
	 * 参数格式:
	 * @param  com.xzsoft.xc.po.modal.PoOrderLBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void editOrderLInfo(PoOrderLBean bean) {
		mapper.editOrderLInfo(bean);
	}
	/**
	 * @Title:deleteOrderLInfo
	 * @Description:删除采购订单行表信息 
	 * 参数格式:
	 * @param  java.util.List
	 * @return 
	 * @throws 
	 */
	@Override
	public void deleteOrderLInfo(List<String> list) {
		mapper.deleteOrderLInfo(list);
	}
	/**
	 * @Title:ifChange
	 * @Description:判断采购订单是否为第一次变更  
	 * 参数格式:
	 * @param  java.lang.String
	 * @return 
	 * @throws 
	 */
	@Override
	public PoOrderHHisBean ifChange(String id){
		return mapper.ifChange(id);
	}
	/**
	 * @Title:ifExistHisH
	 * @Description:判断采购订单历史表主表新增还是修改  
	 * 参数格式:
	 * @param  java.lang.String
	 * @return 
	 * @throws 
	 */
	@Override
	public PoOrderHHisBean ifExistHisH(String id) {
		return mapper.ifExistHisH(id);
	}
	/**
	 * @Title:addOrderHHisInfo
	 * @Description:新增采购订单历史表主表信息  
	 * 参数格式:
	 * @param  com.xzsoft.xc.po.modal.PoOrderHHisBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void addOrderHHisInfo(PoOrderHHisBean bean) {
		mapper.addOrderHHisInfo(bean);
	}
	/**
	 * @Title:editOrderHHisInfo
	 * @Description:修改采购订单历史表主表信息
	 * 参数格式:
	 * @param  com.xzsoft.xc.po.modal.PoOrderHHisBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void editOrderHHisInfo(PoOrderHHisBean bean) {
		mapper.editOrderHHisInfo(bean);
	}
	/**
	 * @Title:ifExistLHis
	 * @Description:判断采购订单历史表行表新增或修改
	 * 参数格式:
	 * @param  java.lang.String
	 * @return 
	 * @throws 
	 */
	@Override
	public PoOrderLHisBean ifExistLHis(String id) {
		return mapper.ifExistLHis(id);
	}
	/**
	 * @Title:addOrderLHisInfo
	 * @Description:新增采购订单历史表行表信息 
	 * 参数格式:
	 * @param  com.xzsoft.xc.po.modal.PoOrderLHisBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void addOrderLHisInfo(PoOrderLHisBean bean) {
		mapper.addOrderLHisInfo(bean);
	}
	/**
	 * @Title:editOrderLHisInfo
	 * @Description:修改采购订单历史表行表信息 
	 * 参数格式:
	 * @param  com.xzsoft.xc.po.modal.PoOrderLHisBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void editOrderLHisInfo(PoOrderLHisBean bean) {
		mapper.editOrderLHisInfo(bean);

	}
	/**
	 * @Title:editPurInfo
	 * @Description:修改采购申请行表信息
	 * 参数格式:
	 * @param  	com.xzsoft.xc.po.modal.PoPurApLBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void editPurInfo(PoPurApLBean bean) {
		mapper.editPurInfo(bean);
	}
	/**
	 * @Title:editPurInfoByOrder
	 * @Description:修改采购申请行表信息(根据采购订单ID)
	 * 参数格式:
	 * @param  	com.xzsoft.xc.po.modal.PoPurApLBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void editPurInfoByOrder(PoPurApLBean bean){
		mapper.editPurInfoByOrder(bean);
	}
	/**
	 * @Title:getOrderLInfoByHId
	 * @Description:获取采购订单行信息通过主ID查询 
	 * 参数格式:
	 * @param  	java.lang.String
	 * @return 
	 * @throws 
	 */
	@Override
	public PoOrderHBean getOrderHInfoByHId(String id) {
		return mapper.getOrderHInfoByHId(id);
	}
	/**
	 * @Title:getOrderLInfoByHId
	 * @Description:获取采购订单行信息通过主ID查询 
	 * 参数格式:
	 * @param  	java.lang.String
	 * @return 
	 * @throws 
	 */
	@Override
	public List<HashMap<String, Object>> getOrderLInfoByHId(String id){
		return mapper.getOrderLInfoByHId(id);
	}
	/**
	 * @Title:getOrderLInfoByHId2
	 * @Description:获取采购订单行信息通过主ID查询 
	 * 参数格式:
	 * @param  	java.lang.String
	 * @return 
	 * @throws 
	 */
	@Override
	public List<PoOrderLBean> getOrderLInfoByHId2(String id){
		return mapper.getOrderLInfoByHId2(id);
	}
	/**
	 * @Title:deleteOrderLHisInfo
	 * @Description:删除采购申请行表信息
	 * 参数格式:
	 * @param  	java.util.List
	 * @return 
	 * @throws 
	 */
	@Override
	public void deleteOrderLHisInfo(List<String> ids){
		mapper.deleteOrderLHisInfo(ids);
	}
	/**
	 * @Title:updateSysStatus
	 * @Description:更新采购订单主信息业务状态
	 * 参数格式:
	 * @param  com.xzsoft.xc.po.modal.PoOrderHBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void updateSysStatus(PoOrderHBean bean){
		mapper.updateSysStatus(bean);
	}
	/**
	 * @Title:changePoOrder
	 * @Description:采购订单变更审批通过后，回写采购订单
	 * 参数格式:
	 * @param  	java.lang.String
	 * @return 
	 * @throws 
	 */
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	@Override
	public void changePoOrder(String id) throws Exception{
		try {
			PoOrderHHisBean hisBean = mapper.ifExistHisH(id);//采购订单变更后的信息
			PoOrderHBean hbean = new PoOrderHBean();
			hbean.setORDER_H_ID(hisBean.getORDER_H_ID());
			hbean.setORDER_H_CODE(hisBean.getORDER_H_CODE());
			hbean.setORDER_H_NAME(hisBean.getORDER_H_NAME());
			hbean.setORDER_H_TYPE(hisBean.getORDER_H_TYPE());
			hbean.setCONTRACT_ID(hisBean.getCONTRACT_ID());
			hbean.setORG_ID(hisBean.getORG_ID());
			hbean.setLEDGER_ID(hisBean.getLEDGER_ID());
			hbean.setVENDOR_ID(hisBean.getVENDOR_ID());
			hbean.setPURCHASE_DEPT_ID(hisBean.getPURCHASE_DEPT_ID());
			hbean.setPURCHASE_PERSON(hisBean.getPURCHASE_PERSON());
			hbean.setVERSION(hisBean.getVERSION());
			hbean.setAMOUNT(hisBean.getAMOUNT());
			hbean.setINV_AMOUNT(hisBean.getINV_AMOUNT());
			hbean.setSYS_AUDIT_STATUS(hisBean.getSYS_AUDIT_STATUS());
			hbean.setSYS_AUDIT_STATUS_DESC(hisBean.getSYS_AUDIT_STATUS_DESC());
			hbean.setDOCUMENT_DATE(hisBean.getDOCUMENT_DATE());
			hbean.setLAST_UPDATE_DATE(hisBean.getLAST_UPDATE_DATE());
			hbean.setLAST_UPDATED_BY(hisBean.getLAST_UPDATED_BY());
			mapper.editOrderHInfo(hbean);//更新采购订单主信息
			List<HashMap<String, Object>> hisList = mapper.getOrderLHisInfoByHId(id);//采购订单历史行信息
			List<HashMap<String, Object>> list = mapper.getOrderLInfoByHId(hisBean.getORDER_H_ID());//采购订单行信息
			List<String> delList = new ArrayList<String>();//删除的采购订单行信息ID的List
				for (int j = 0; j < list.size(); j++) {
					delList.add(list.get(j).get("ORDER_L_ID").toString());
					PoPurApLBean purbean = new PoPurApLBean();
					purbean.setPUR_AP_L_ID(list.get(j).get("PUR_AP_L_ID").toString());
					purbean.setPO_ORDER_H_ID("");
					purbean.setPO_ORDER_L_ID("");
					mapper.editPurInfo(purbean);//更新采购申请行信息
				}
				mapper.deleteOrderLInfo(delList);//删除采购订单行
				for (int i = 0; i < hisList.size(); i++) {
					HashMap<String, Object> map = hisList.get(i);
						PoOrderLBean bean = new PoOrderLBean();
						bean.setORDER_L_ID(map.get("ORDER_L_ID").toString());
						bean.setORDER_H_ID(map.get("ORDER_H_ID").toString());
						bean.setORDER_L_NUM(Integer.valueOf(map.get("ORDER_L_NUM").toString()));
						bean.setMATERIAL_ID(map.get("MATERIAL_ID").toString());
						bean.setDEPT_ID(map.get("DEPT_ID").toString());
						bean.setPROJECT_ID(map.get("PROJECT_ID").toString());
						bean.setIS_CLOSE(map.get("IS_CLOSE").toString());
						bean.setDIM_ID(map.get("DIM_ID").toString());
						bean.setQTY(Double.valueOf(map.get("QTY").toString()));
						bean.setTAX(Double.valueOf(map.get("TAX").toString()));
						bean.setPRICE(Double.valueOf(map.get("PRICE").toString()));
						bean.setINV_PRICE(Double.valueOf(map.get("INV_PRICE").toString()));
						bean.setAMOUNT(Double.valueOf(map.get("AMOUNT").toString()));
						bean.setINV_AMOUNT(Double.valueOf(map.get("INV_AMOUNT").toString()));
						bean.setORG_ID(map.get("ORG_ID").toString());
						bean.setLEDGER_ID(map.get("LEDGER_ID").toString());
						bean.setDESCRIPTION(map.get("DESCRIPTION").toString());
						bean.setVERSION(Integer.valueOf(map.get("VERSION").toString()));
						bean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
						bean.setCREATED_BY(CurrentSessionVar.getUserId());
						bean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
						bean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
						mapper.addOrderLInfo(bean);//新增采购订单行信息
						if(!"".equals(map.get("PUR_AP_L_ID").toString())){
							PoPurApLBean purbean = new PoPurApLBean();
							purbean.setPUR_AP_L_ID(map.get("PUR_AP_L_ID").toString());
							purbean.setPO_ORDER_H_ID(map.get("ORDER_H_ID").toString());
							purbean.setPO_ORDER_L_ID(map.get("ORDER_L_ID").toString());
							mapper.editPurInfo(purbean);//修改采购申请行表
						}
					
				}
		} catch (Exception e) {
			throw e;
		}
		
	}
	/**
	 * @Title:getOrderHHisById
	 * @Description:获得采购订单（变更）通过id（ORDER_H_HIS_ID）
	 * 参数格式:
	 * @param  	java.lang.String
	 * @return 
	 * @throws 
	 */
	@Override
	public PoOrderHHisBean getOrderHHisById(String id) {
		return mapper.getOrderHHisById(id);
	}
	/**
	 * @Title:getOrderLHisByHId
	 * @Description:获得采购订单（变更）行数据，通过主id（ORDER_H_HIS_ID）
	 * 参数格式:
	 * @param  	java.lang.String
	 * @return 
	 * @throws 
	 */
	@Override
	public List<PoOrderLHisBean> getOrderLHisByHId(String id) {
		return mapper.getOrderLHisByHId(id);
	}
	
}
