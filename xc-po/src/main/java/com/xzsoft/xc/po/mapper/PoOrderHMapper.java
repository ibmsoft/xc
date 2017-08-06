package com.xzsoft.xc.po.mapper;

import java.util.HashMap;
import java.util.List;

import org.springframework.expression.OperatorOverloader;

import com.xzsoft.xc.po.modal.PoOrderHBean;
import com.xzsoft.xc.po.modal.PoOrderHHisBean;
import com.xzsoft.xc.po.modal.PoOrderLBean;
import com.xzsoft.xc.po.modal.PoOrderLHisBean;
import com.xzsoft.xc.po.modal.PoPurApLBean;

/** 
 * @ClassName: PoOrderHMapper
 * @Description:采购订单Mapper层 
 * @author weicw
 * @date 2016年12月7日 下午4:49:50 
 * 
 */
public interface PoOrderHMapper {
	/**
	 * @Title:ifExistH
	 * @Description:判断主表新增还是修改  
	 * 参数格式:
	 * @param  java.lang.String
	 * @return 
	 * @throws 
	 */
	public PoOrderHBean ifExistH(String id);
	/**
	 * @Title:addOrderHInfo
	 * @Description:新增采购订单主表信息   
	 * 参数格式:
	 * @param  com.xzsoft.xc.po.modal.PoOrderHBean
	 * @return 
	 * @throws 
	 */
	public void addOrderHInfo(PoOrderHBean bean);
	/**
	 * @Title:editOrderHInfo
	 * @Description:修改采购订单主表信息 
	 * 参数格式:
	 * @param  com.xzsoft.xc.po.modal.PoOrderHBean
	 * @return 
	 * @throws 
	 */
	public void editOrderHInfo(PoOrderHBean bean);
	/**
	 * @Title:deleteOrderHInfo
	 * @Description:删除采购订单主表信息 
	 * 参数格式:
	 * @param  java.util.List
	 * @return 
	 * @throws 
	 */
	public void deleteOrderHInfo(List<String> list);
	/**
	 * @Title:ifExistL
	 * @Description:判断行表新增或修改 
	 * 参数格式:
	 * @param  java.lang.String
	 * @return 
	 * @throws 
	 */
	public PoOrderLBean ifExistL(String id);
	/**
	 * @Title:addOrderLInfo
	 * @Description:新增采购订单行表信息 
	 * 参数格式:
	 * @param  com.xzsoft.xc.po.modal.PoOrderLBean
	 * @return 
	 * @throws 
	 */
	public void addOrderLInfo(PoOrderLBean bean);
	/**
	 * @Title:editOrderLInfo
	 * @Description:修改采购订单行表信息 
	 * 参数格式:
	 * @param  com.xzsoft.xc.po.modal.PoOrderLBean
	 * @return 
	 * @throws 
	 */
	public void editOrderLInfo(PoOrderLBean bean);
	/**
	 * @Title:deleteOrderLInfo
	 * @Description:删除采购订单行表信息 
	 * 参数格式:
	 * @param  java.util.List
	 * @return 
	 * @throws 
	 */
	public void deleteOrderLInfo(List<String> list);
	/**
	 * @Title:updateOrerHStatus
	 * @Description:更改采购订单主信息是否关闭的状态 
	 * 参数格式:
	 * @param  java.util.HashMap
	 * @return 
	 * @throws 
	 */
	public void updateOrerHStatus(HashMap<String, Object> map);
	/**
	 * @Title:updateOrerLStatus
	 * @Description:更改采购订单行信息是否关闭的状态 (根据主信息ID)-
	 * 参数格式:
	 * @param  java.util.HashMap
	 * @return 
	 * @throws 
	 */
	public void updateOrerLStatus(HashMap<String, Object> map);
	/**
	 * @Title:updateOrerLStatusByLId
	 * @Description:更改采购订单行信息是否关闭的状态 (根据行信息ID)
	 * 参数格式:
	 * @param  java.util.HashMap
	 * @return 
	 * @throws 
	 */
	public void updateOrerLStatusByLId(HashMap<String, Object> map);
	/**
	 * @Title:ifChange
	 * @Description:判断采购订单是否为第一次变更  
	 * 参数格式:
	 * @param  java.lang.String
	 * @return 
	 * @throws 
	 */
	public PoOrderHHisBean ifChange(String id);
	/**
	 * @Title:ifExistHisH
	 * @Description:判断采购订单历史表主表新增还是修改  
	 * 参数格式:
	 * @param  java.lang.String
	 * @return 
	 * @throws 
	 */
	public PoOrderHHisBean ifExistHisH(String id);
	/**
	 * @Title:addOrderHHisInfo
	 * @Description:新增采购订单历史表主表信息  
	 * 参数格式:
	 * @param  com.xzsoft.xc.po.modal.PoOrderHHisBean
	 * @return 
	 * @throws 
	 */
	public void addOrderHHisInfo(PoOrderHHisBean bean);
	/**
	 * @Title:editOrderHHisInfo
	 * @Description:修改采购订单历史表主表信息
	 * 参数格式:
	 * @param  com.xzsoft.xc.po.modal.PoOrderHHisBean
	 * @return 
	 * @throws 
	 */
	public void editOrderHHisInfo(PoOrderHHisBean bean);
	/**
	 * @Title:ifExistLHis
	 * @Description:判断采购订单历史表行表新增或修改
	 * 参数格式:
	 * @param  java.lang.String
	 * @return 
	 * @throws 
	 */
	public PoOrderLHisBean ifExistLHis(String id);
	/**
	 * @Title:addOrderLHisInfo
	 * @Description:新增采购订单历史表行表信息 
	 * 参数格式:
	 * @param  com.xzsoft.xc.po.modal.PoOrderLHisBean
	 * @return 
	 * @throws 
	 */
	public void addOrderLHisInfo(PoOrderLHisBean bean);
	/**
	 * @Title:editOrderLHisInfo
	 * @Description:修改采购申请行表信息
	 * 参数格式:
	 * @param  com.xzsoft.xc.po.modal.PoOrderLHisBean
	 * @return 
	 * @throws 
	 */
	public void editOrderLHisInfo(PoOrderLHisBean bean);
	/**
	 * @Title:editPurInfo
	 * @Description:修改采购申请行表信息
	 * 参数格式:
	 * @param  	com.xzsoft.xc.po.modal.PoPurApLBean
	 * @return 
	 * @throws 
	 */
	public void editPurInfo(PoPurApLBean bean);
	/**
	 * @Title:editPurInfoByOrder
	 * @Description:修改采购申请行表信息(根据采购订单ID)
	 * 参数格式:
	 * @param  	com.xzsoft.xc.po.modal.PoPurApLBean
	 * @return 
	 * @throws 
	 */
	public void editPurInfoByOrder(PoPurApLBean bean);
	/**
	 * @return 
	 * @Title:getOrderHInfoByHId
	 * @Description:获取采购订单主信息通过主ID查询 
	 * 参数格式:
	 * @param  	java.lang.String
	 * @return 
	 * @throws 
	 */
	public PoOrderHBean getOrderHInfoByHId(String id);
	/**
	 * @Title:getOrderLInfoByHId
	 * @Description:获取采购订单行信息通过主ID查询 
	 * 参数格式:
	 * @param  	java.lang.String
	 * @return 
	 * @throws 
	 */
	public List<HashMap<String, Object>> getOrderLInfoByHId(String id);
	/**
	 * @Title:getOrderLInfoByHId2
	 * @Description:获取采购订单行信息通过主ID查询 
	 * 参数格式:
	 * @param  	java.lang.String
	 * @return 
	 * @throws 
	 */
	public List<PoOrderLBean> getOrderLInfoByHId2(String id);
	/**
	 * @Title:deleteOrderLHisInfo
	 * @Description:删除采购申请行表信息
	 * 参数格式:
	 * @param  	java.util.List
	 * @return 
	 * @throws 
	 */
	public void deleteOrderLHisInfo(List<String> ids);
	/**
	 * @Title:updateSysStatus
	 * @Description:更新采购订单主信息业务状态
	 * 参数格式:
	 * @param  	com.xzsoft.xc.po.modal.PoOrderHBean
	 * @return 
	 * @throws 
	 */
	public void updateSysStatus(PoOrderHBean bean);
	/**
	 * @Title:getOrderLHisInfoByHId
	 * @Description:获取采购订单历史行信息通过主ID查询
	 * 参数格式:
	 * @param  	java.lang.String
	 * @return 
	 * @throws 
	 */
	public List<HashMap<String, Object>> getOrderLHisInfoByHId(String id);
	/**
	 * @Title:ifExistLHisByLId
	 * @Description:判断采购订单历史表行表新增或修改(通过采购订单行ID)
	 * 参数格式:
	 * @param  	java.lang.String
	 * @return 
	 * @throws 
	 */
	public PoOrderLHisBean ifExistLHisByLId(String id);
	/**
	 * @Title:getOrderHHisById
	 * @Description:获得采购订单（变更）通过id（ORDER_H_HIS_ID）
	 * 参数格式:
	 * @param  	java.lang.String
	 * @return 
	 * @throws 
	 */
	public PoOrderHHisBean getOrderHHisById(String id);
	
	/**
	 * @Title:getOrderLHisByHId
	 * @Description:获得采购订单（变更）行数据，通过主id（ORDER_H_HIS_ID）
	 * 参数格式:
	 * @param  	java.lang.String
	 * @return 
	 * @throws 
	 */
	public List<PoOrderLHisBean> getOrderLHisByHId(String id);
}
