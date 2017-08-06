package com.xzsoft.xc.po.service;

import java.util.HashMap;
import java.util.List;

import org.codehaus.jettison.json.JSONObject;

/** 
 * @ClassName: PoOrderService
 * @Description: 采购订单Service接口
 * @author weicw
 * @date 2016年12月15日 下午3:01:02 
 * 
 */
public interface PoOrderService {
	/**
	 * @Title:saveOrder
	 * @Description:保存采购订单  
	 * 参数格式:
	 * @param  java.lang.String(主信息) java.lang.String(行信息) java.lang.String(删除的行) java.lang.String(行号的数组)
	 * @return 
	 * @throws 
	 * ,String lineNums
	 */
	public JSONObject saveOrder(String formStr,String gridStr,String deleteIds) throws Exception;
	/**
	 * @Title:updateOrerHStatus
	 * @Description:更改采购订单主信息是否关闭的状态 
	 * 参数格式:
	 * @param  java.lang.String(主信息ID) java.lang.String(是否关闭)
	 * @return 
	 * @throws 
	 */
	public JSONObject updateOrerHStatus(String ids,String operate) throws Exception;
	/**
	 * @Title:updateOrerLStatusByLId
	 * @Description:更改采购订单行信息是否关闭的状态 (根据行信息ID)
	 * 参数格式:
	 * @param  java.lang.String(行信息ID) java.lang.String(是否关闭)
	 * @return 
	 * @throws 
	 */
	public JSONObject updateOrerLStatusByLId(String ids,String operate) throws Exception;
	/**
	 * @Title:deleteOrderHInfo
	 * @Description:删除采购订单主表信息 
	 * 参数格式:
	 * @param  java.lang.String(主信息ID)
	 * @return 
	 * @throws 
	 */
	public JSONObject deleteOrderHInfo(String ids)  throws Exception;
	/**
	 * @Title:addOrderHHisInfo
	 * @Description:新增采购订单历史表信息
	 * 参数格式:
	 * @param  java.lang.String(主信息) java.lang.String(行信息) java.lang.String(行号)
	 * @return 
	 * @throws 
	 */
	public JSONObject addOrderHHisInfo(String formStr, String gridStr) throws Exception;
	/**
	 * @Title:editOrderHHisInfo
	 * @Description:修改采购订单历史表信息
	 * 参数格式:
	 * @param  java.lang.String(主信息) java.lang.String(行信息) java.lang.String(行号) java.lang.String(删除的行)
	 * @return 
	 * @throws 
	 */
	public JSONObject editOrderHHisInfo(String formStr, String gridStr,String deleteIds) throws Exception;
}
