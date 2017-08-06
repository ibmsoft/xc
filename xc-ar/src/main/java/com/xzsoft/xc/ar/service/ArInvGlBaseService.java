package com.xzsoft.xc.ar.service;

import java.util.HashMap;
import java.util.List;

import org.codehaus.jettison.json.JSONObject;

import com.xzsoft.xc.ar.modal.ArInvGlHBean;
import com.xzsoft.xc.ar.modal.ArInvGlLBean;

/** 
 * @className ArInvGlHService
 * @Description 应收单Service
 * @author 韦才文
 * @version 创建时间：2016年7月9日 下午12:52:24 
 */
public interface ArInvGlBaseService {
	/**
	 * @Title:glCount
	 * @Description: 应收期初总账取数
	 * 参数格式:
	 * @param  java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	public JSONObject glCount(String invGlH) throws Exception;
	/**
	 * @Title:split
	 * @Description: 应收期初拆分
	 * 参数格式:
	 * @param  java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	public JSONObject split(String jsonStr) throws Exception;
	/**
	 * @Title:deleteInfo
	 * @Description: 应收期初删除数据
	 * 参数格式:
	 * @param  java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	public JSONObject deleteInfo(String ids,String lang) throws Exception;
	/**
	 * @Title:delInvGlHInfo
	 * @Description: 应收期初单独删除明细数据
	 * 参数格式:
	 * @param  java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	public JSONObject delInvGlHInfo(String ids) throws Exception;
	
	/**
	 * @Title:glCountYuS
	 * @Description: 预收期初总账取数
	 * 参数格式:
	 * @param  java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	public JSONObject glCountYuS(String payH) throws Exception;
	/**
	 * @Title:splitYuS
	 * @Description: 预收期初拆分
	 * 参数格式:
	 * @param  java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	public JSONObject splitYuS(String jsonStr) throws Exception;
	/**
	 * @Title:deleteInfoYuS
	 * @Description: 预收期初删除数据
	 * 参数格式:
	 * @param  java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	public JSONObject deleteInfoYuS(String ids,String lang) throws Exception;
	/**
	 * @Title:delPayHInfo
	 * @Description: 预收期初单独删除明细数据
	 * 参数格式:
	 * @param  java.lang.String
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
