package com.xzsoft.xc.ar.mapper;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.ar.modal.ArInvGlHBean;
import com.xzsoft.xc.ar.modal.ArInvGlLBean;
import com.xzsoft.xc.ar.modal.ArPayHBean;
import com.xzsoft.xc.ar.modal.ArPayLBean;

/** 
 * @className ArInvGlBaseMapper
 * @Description 应收单mapper
 * @author 韦才文
 * @version 创建时间：2016年7月9日 下午12:37:57 
 */
public interface ArInvGlBaseMapper {
	/**
	 * @Title:getQcDatas
	 * @Description: 应收客户期初数据
	 * 参数格式:
	 * @param java.util.HashMap
	 * @return java.util.HashMap
	 * @throws 
	 */
	public HashMap<String, Object> getQcDatas(HashMap<String, String> paramMap);
	/**
	 * @Title:addInvGlHInfo
	 * @Description: 添加应收单主表信息
	 * 参数格式:
	 * @param  com.xzsoft.xc.ar.modal.ArInvGlHBean
	 * @return 
	 * @throws 
	 */
	public void addInvGlHInfo(ArInvGlHBean arInvGlHBean);
	/**
	 * @Title:editInvGlHInfo
	 * @Description: 更新应收单主表信息
	 * 参数格式:
	 * @param  com.xzsoft.xc.ar.modal.ArInvGlHBean
	 * @return 
	 * @throws 
	 */
	public void editInvGlHInfo(ArInvGlHBean arInvGlHBean);
	/**
	 * @Title:delInvGlHInfo
	 * @Description: 删除应收单主表信息
	 * 参数格式:
	 * @param  java.util.List
	 * @return 
	 * @throws 
	 */
	public void delInvGlHInfo(List<String> idList);
	/**
	 * @Title:delInvGlHInfoByInvHId
	 * @Description: 删除应收单主表信息
	 * 参数格式:
	 * @param  java.util.List
	 * @return 
	 * @throws 
	 */
	public void delInvGlHInfoByInvHId(List<String> idList);
	/**
	 * @Title:ifGlCount
	 * @Description: 判断应收是否已经期初
	 * 参数格式:
	 * @param  java.util.HashMap
	 * @return java.lang.String
	 * @throws 
	 */
	public String ifGlCount(HashMap<String, String> map);
	/**
	 * @Title:updateGlCountInvL
	 * @Description: 更新应收行表信息
	 * 参数格式:
	 * @param   com.xzsoft.xc.ar.modal.ArInvGlLBean
	 * @return 
	 * @throws 
	 */
	public void updateGlCountInvL(ArInvGlLBean arInvGlLBean);
	/**
	 * @Title:addInvGlLInfo
	 * @Description: 添加应收单行表信息
	 * 参数格式:
	 * @param  com.xzsoft.xc.ar.modal.ArInvGlLBean
	 * @return 
	 * @throws 
	 */
	public void addInvGlLInfo(ArInvGlLBean arInvGlLBean);
	/**
	 * @Title:getInvGlHInfo
	 * @Description: 得到应收单主表信息
	 * 参数格式:
	 * @param  java.lang.String
	 * @return com.xzsoft.xc.ar.modal.ArInvGlHBean
	 * @throws 
	 */
	public ArInvGlHBean getInvGlHInfo(String invGlHId);
	
	/**
	 * @Title:getQcDatasYus
	 * @Description: 得到预收客户期初信息
	 * 参数格式:
	 * @param  java.util.HashMap
	 * @return java.util.HashMap
	 * @throws 
	 */
	public HashMap<String, Object> getQcDatasYus(HashMap<String, String> paramMap);
	/**
	 * @Title:getPayHInfo
	 * @Description: 查询预收单主表信息
	 * 参数格式:
	 * @param  java.lang.String
	 * @return com.xzsoft.xc.ar.modal.ArPayHBean
	 * @throws 
	 */
	public ArPayHBean getPayHInfo(String arPayHId);
	/**
	 * @Title:ifGlCountYuS
	 * @Description: 判断预收是否已经期初
	 * 参数格式:
	 * @param  java.util.HashMap
	 * @return java.lang.String
	 * @throws 
	 */
	public String ifGlCountYuS(HashMap<String, String> map);
	/**
	 * @Title:updateGlCountPayL
	 * @Description: 更新预收行表信息
	 * 参数格式:
	 * @param  com.xzsoft.xc.ar.modal.ArPayLBean
	 * @return 
	 * @throws 
	 */
	public void updateGlCountPayL(ArPayLBean arPayHBean);
	/**
	 * @Title:addPayHInfo
	 * @Description: 添加预收单主表信息
	 * 参数格式:
	 * @param  com.xzsoft.xc.ar.modal.ArPayHBean
	 * @return 
	 * @throws 
	 */
	public void addPayHInfo(ArPayHBean arPayHBean);
	/**
	 * @Title:editPayHInfo
	 * @Description: 更新预收单主表信息
	 * 参数格式:
	 * @param  com.xzsoft.xc.ar.modal.ArPayHBean
	 * @return 
	 * @throws 
	 */
	public void editPayHInfo(ArPayHBean arPayHBean);
	/**
	 * @Title:delPayHInfoByPayHId
	 * @Description: 删除预收单主表信息
	 * 参数格式:
	 * @param  java.util.List
	 * @return 
	 * @throws 
	 */
	public void delPayHInfo(List<String> idList);
	/**
	 * @Title:delPayHInfoByPayHId
	 * @Description: 删除预收单主表信息
	 * 参数格式:
	 * @param  java.util.List
	 * @return 
	 * @throws 
	 */
	public void delPayHInfoByPayHId(List<String> idList);
	/**
	 * @Title:addPayLInfo
	 * @Description: 添加预收单行表信息
	 * 参数格式:
	 * @param  com.xzsoft.xc.ar.modal.ArPayLBean
	 * @return 
	 * @throws 
	 */
	public void addPayLInfo(ArPayLBean arPayLBean);
	/**
	 * @Title:delPayLInfo
	 * @Description: 删除预收行表信息
	 * 参数格式:
	 * @param java.util.List
	 * @return
	 * @throws 
	 */
	public void delPayLInfo(List<String> idList);
	/**
	 * @Title:delInvGlLInfo
	 * @Description: 删除应收行表信息
	 * 参数格式:
	 * @param java.util.List
	 * @return
	 * @throws 
	 */
	public void delInvGlLInfo(List<String> idList);
	/**
	 * @Title:getPayHId
	 * @Description: 通过预收主表ID查询行表信息
	 * 参数格式:
	 * @param java.lang.String
	 * @return java.util.List
	 * @throws 
	 */
	public List<HashMap<String, String>> getPayHId(String id);
	/**
	 * @Title:getInvHId
	 * @Description: 通过应收主表ID查询行表信息
	 * 参数格式:
	 * @param java.lang.String
	 * @return java.util.List
	 * @throws 
	 */
	public List<HashMap<String, String>> getInvHId(String id);
	/**
	 * 
	 * @Title: updateInvGlHs  
	 * @Description: 批量修改应收单收款金额
	 * @param map     
	 * @return: void    
	 *
	 */
	public void updateInvGlHsPaidAmt(HashMap<String, Object> map);
	/**
	 * 
	 * @Title: selectArInvGlHs  
	 * @Description: 根据应收单id集合查找应收单  
	 * @param list
	 * @return: List<ArInvGlHBean>    
	 *
	 */
	public List<ArInvGlHBean> selectArInvGlHsByIds(List<String> list);
	/**
	 * @Title checkInvIfUse
	 * @Description: 查看拆分出來的应收单是否被使用 
	 * 参数格式:
	 * @param java.util.List
	 * @return java.lang.String
	 * @throws 
	 */
	public String checkInvIfUse(List<String> ids);
	/**
	 * @Title balanceInv
	 * @Description: 应收记账 
	 * 参数格式:
	 * @param java.util.List
	 * @return 
	 * @throws 
	 */
	public void balanceInv(List<String> ids);
	/**
	 * @Title cancelBalanceInv
	 * @Description: 应收取消记账 
	 * 参数格式:
	 * @param java.util.List
	 * @return 
	 * @throws 
	 */
	public void cancelBalanceInv(List<String> ids);
	/**
	 * @Title checkInvIfUse
	 * @Description: 查看拆分出來的预收单是否被使用 
	 * 参数格式:
	 * @param java.util.List
	 * @return java.lang.String
	 * @throws 
	 */
	public String checkPayIfUse(List<String> ids);
	/**
	 * @Title balanceInv
	 * @Description: 预收记账 
	 * 参数格式:
	 * @param java.util.List
	 * @return 
	 * @throws 
	 */
	public void balancePay(List<String> ids);
	/**
	 * @Title cancelBalanceInv
	 * @Description: 预收取消记账 
	 * 参数格式:
	 * @param java.util.List
	 * @return 
	 * @throws 
	 */
	public void cancelBalancePay(List<String> ids);
}
