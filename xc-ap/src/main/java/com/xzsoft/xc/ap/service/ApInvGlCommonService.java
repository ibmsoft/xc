package com.xzsoft.xc.ap.service;

import java.util.HashMap;
import java.util.List;

import org.codehaus.jettison.json.JSONObject;

import com.xzsoft.xc.ap.modal.ApInvGlHBean;
import com.xzsoft.xc.ap.modal.ApInvGlLBean;

/** 
 * @className ApInvGlCommonService
 * @Description 
 * @author 韦才文
 * @version 创建时间：2016年7月9日 下午12:52:24 
 */
public interface ApInvGlCommonService {
	/**
	 * @Title glCount
	 * @Description: 应付期初总账取数
	 * 参数格式:
	 * @param java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	public JSONObject glCount(String invGlH) throws Exception;
	/**
	 * @Title split
	 * @Description: 应付期初拆分
	 * 参数格式:
	 * @param java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	public JSONObject split(String jsonStr) throws Exception;
	/**
	 * @Title deleteInfo
	 * @Description: 删除应付单
	 * 参数格式:
	 * @param java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	public JSONObject deleteInfo(String ids,String lang) throws Exception;
	/**
	 * @Title delInvGlHInfo
	 * @Description: 单独删除明细
	 * 参数格式:
	 * @param java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	public JSONObject delInvGlHInfo(String ids) throws Exception;
	
	/**
	 * @Title glCountYuF
	 * @Description: 预付期初总账取数
	 * 参数格式:
	 * @param java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	public JSONObject glCountYuF(String payH) throws Exception;
	/**
	 * @Title splitYuF
	 * @Description: 预付期初拆分
	 * 参数格式:
	 * @param java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	public JSONObject splitYuF(String jsonStr) throws Exception;
	/**
	 * @Title deleteInfoYuF
	 * @Description: 预付期初删除信息
	 * 参数格式:
	 * @param java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	public JSONObject deleteInfoYuF(String ids,String lang) throws Exception;
	/**
	 * @Title delPayHInfo
	 * @Description: 预付期初单独删除明细信息
	 * 参数格式:
	 * @param java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	public JSONObject delPayHInfo(String ids) throws Exception;
	/**
	 * @Title balanceInv
	 * @Description: 应付记账 
	 * 参数格式:
	 * @param java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	public JSONObject balanceInv(String ids) throws Exception;
	/**
	 * @Title cancelBalanceInv
	 * @Description: 应付取消记账 
	 * 参数格式:
	 * @param java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	public JSONObject cancelBalanceInv(String ids,String lang) throws Exception;
	/**
	 * @Title balanceInv
	 * @Description: 预付记账 
	 * 参数格式:
	 * @param  java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	public JSONObject balancePay(String ids)throws Exception ;
	/**
	 * @Title cancelBalanceInv
	 * @Description: 预付取消记账 
	 * 参数格式:
	 * @param  java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	public JSONObject cancelBalancePay(String ids,String lang) throws Exception;
}
