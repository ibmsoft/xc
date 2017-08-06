package com.xzsoft.xc.ar.dao.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.apar.modal.ApInvTransBean;
import com.xzsoft.xc.ar.dao.ArInvGlBaseDao;
import com.xzsoft.xc.ar.mapper.ArInvGlBaseMapper;
import com.xzsoft.xc.ar.modal.ArInvGlHBean;
import com.xzsoft.xc.ar.modal.ArInvGlLBean;
import com.xzsoft.xc.ar.modal.ArPayHBean;
import com.xzsoft.xc.ar.modal.ArPayLBean;
import com.xzsoft.xip.platform.util.PlatformUtil;

/** 
 * @className
 * @Description 
 * @author 韦才文
 * @version 创建时间：2016年7月9日 下午12:45:50 
 */
@Repository("arInvGlHDao")
public class ArInvGlBaseDaoImpl implements ArInvGlBaseDao {
	@Resource
	ArInvGlBaseMapper mapper;
	/**
	 * @Title:getQcDatas
	 * @Description: 应收客户期初数据
	 * 参数格式:
	 * @param java.util.HashMap
	 * @return java.util.HashMap
	 * @throws 
	 */
	@Override
	public HashMap<String, Object> getQcDatas(HashMap<String, String> paramMap) {
		return mapper.getQcDatas(paramMap);
	}
	/**
	 * @Title:addInvGlHInfo
	 * @Description: 添加应收单主表信息
	 * 参数格式:
	 * @param  com.xzsoft.xc.ar.modal.ArInvGlHBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void addInvGlHInfo(ArInvGlHBean arInvGlHBean) {
		mapper.addInvGlHInfo(arInvGlHBean);

	}
	
	/**
	 * @Title:addInvGlLInfo
	 * @Description: 添加应收单行表信息
	 * 参数格式:
	 * @param  com.xzsoft.xc.ar.modal.ArInvGlLBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void addInvGlLInfo(ArInvGlLBean arInvGlLBean) {
		mapper.addInvGlLInfo(arInvGlLBean);

	}
	/**
	 * @Title:editInvGlHInfo
	 * @Description: 更新应收单主表信息
	 * 参数格式:
	 * @param  com.xzsoft.xc.ar.modal.ArInvGlHBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void editInvGlHInfo(ArInvGlHBean arInvGlHBean) {
		mapper.editInvGlHInfo(arInvGlHBean);
		
	}
	/**
	 * @Title:delInvGlHInfo
	 * @Description: 删除应收单主表信息
	 * 参数格式:
	 * @param  java.util.List
	 * @return 
	 * @throws 
	 */
	@Override
	public void delInvGlHInfo(List<String> idList) {
		mapper.delInvGlHInfo(idList);
		
	}
	/**
	 * @Title:getInvGlHInfo
	 * @Description: 得到应收单主表信息
	 * 参数格式:
	 * @param  java.lang.String
	 * @return com.xzsoft.xc.ar.modal.ArInvGlHBean
	 * @throws 
	 */
	@Override
	public ArInvGlHBean getInvGlHInfo(String invGlHId) {
		
		return mapper.getInvGlHInfo(invGlHId);
	}
	/**
	 * @Title:delInvGlHInfoByInvHId
	 * @Description: 删除应收单主表信息
	 * 参数格式:
	 * @param  java.util.List
	 * @return 
	 * @throws 
	 */
	@Override
	public void delInvGlHInfoByInvHId(List<String> idList) {
		mapper.delInvGlHInfoByInvHId(idList);
		
	}
	/**
	 * @Title:getQcDatasYus
	 * @Description: 得到预收客户期初信息
	 * 参数格式:
	 * @param  java.util.HashMap
	 * @return java.util.HashMap
	 * @throws 
	 */
	@Override
	public HashMap<String, Object> getQcDatasYus(
			HashMap<String, String> paramMap) {
		return mapper.getQcDatasYus(paramMap);
	}
	/**
	 * @Title:getPayHInfo
	 * @Description: 查询预收单主表信息
	 * 参数格式:
	 * @param  java.lang.String
	 * @return com.xzsoft.xc.ar.modal.ArPayHBean
	 * @throws 
	 */
	@Override
	public ArPayHBean getPayHInfo(String arPayHId) {
		return mapper.getPayHInfo(arPayHId);
	}
	
	/**
	 * @Title:addPayHInfo
	 * @Description: 添加预收单主表信息
	 * 参数格式:
	 * @param  com.xzsoft.xc.ar.modal.ArPayHBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void addPayHInfo(ArPayHBean arPayHBean) {
		mapper.addPayHInfo(arPayHBean);
		
	}
	
	/**
	 * @Title:editPayHInfo
	 * @Description: 更新预收单主表信息
	 * 参数格式:
	 * @param  com.xzsoft.xc.ar.modal.ArPayHBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void editPayHInfo(ArPayHBean arPayHBean) {
		mapper.editPayHInfo(arPayHBean);
		
	}
	
	/**
	 * @Title:delPayHInfoByPayHId
	 * @Description: 删除预收单主表信息
	 * 参数格式:
	 * @param  java.util.List
	 * @return 
	 * @throws 
	 */
	@Override
	public void delPayHInfoByPayHId(List<String> idList) {
		mapper.delPayHInfoByPayHId(idList);
		
	}
	
	/**
	 * @Title:addPayLInfo
	 * @Description: 添加预收单行表信息
	 * 参数格式:
	 * @param  com.xzsoft.xc.ar.modal.ArPayLBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void addPayLInfo(ArPayLBean arPayLBean) {
		mapper.addPayLInfo(arPayLBean);
		
	}
	/**
	 * @Title:delPayHInfo
	 * @Description: 删除预收单主表信息
	 * 参数格式:
	 * @param  java.util.List
	 * @return 
	 * @throws 
	 */
	@Override
	public void delPayHInfo(List<String> idList) {
		mapper.delPayHInfo(idList);
		
	}
	/* (non-Javadoc)
	 * <p>Title: updateInvGlHs</p>
	 * <p>Description: </p>
	 * @param arPayLBeanList
	 * @throws Exception
	 * @see com.xzsoft.xc.ar.dao.ArInvGlBaseDao#updateInvGlHs(java.util.List)
	 */
	@Override
	public void updateInvGlHsPaidAmt(String opType, List<ArPayLBean> arPayLBeanList) throws Exception {
		if (arPayLBeanList.size() > 0 && arPayLBeanList != null) {
			if ("+".equals(opType) || "".equals(opType) || null == opType){
				// do nothing
			}else if ("-".equals(opType)){
				for (int i = 0; i < arPayLBeanList.size(); i++) {
					ArPayLBean arPayLBean = arPayLBeanList.get(i);
					arPayLBean.setAMOUNT(arPayLBean.getAMOUNT()*(-1));
				}
			} 
			
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			//map.put("opType", opType);
			map.put("list", arPayLBeanList);
			mapper.updateInvGlHsPaidAmt(map);
		}
		
	}
	/* (non-Javadoc)
	 * <p>Title: selectArInvGlHsByIds</p>
	 * <p>Description: 根据id集合查找应收单</p>
	 * @param list
	 * @return
	 * @throws Exception
	 * @see com.xzsoft.xc.ar.dao.ArInvGlBaseDao#selectArInvGlHsByIds(java.util.List)
	 */
	@Override
	public List<ArInvGlHBean> getArInvGlHsByIds(List<String> list) throws Exception {
		return mapper.selectArInvGlHsByIds(list);
	}
	/**
	 * @Title:ifGlCount
	 * @Description: 判断应收是否已经期初
	 * 参数格式:
	 * @param  java.util.HashMap
	 * @return java.lang.String
	 * @throws 
	 */
	@Override
	public String ifGlCount(HashMap<String, String> map) {
		return mapper.ifGlCount(map);
	}
	/**
	 * @Title:ifGlCountYuS
	 * @Description: 判断预收是否已经期初
	 * 参数格式:
	 * @param  java.util.HashMap
	 * @return java.lang.String
	 * @throws 
	 */
	@Override
	public String ifGlCountYuS(HashMap<String, String> map) {
		return mapper.ifGlCountYuS(map);
	}
	/**
	 * @Title:updateGlCountInvL
	 * @Description: 更新应收行表信息
	 * 参数格式:
	 * @param   com.xzsoft.xc.ar.modal.ArInvGlLBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void updateGlCountInvL(ArInvGlLBean arInvGlLBean) {
		mapper.updateGlCountInvL(arInvGlLBean);
		
	}
	/**
	 * @Title:updateGlCountPayL
	 * @Description: 更新预收行表信息
	 * 参数格式:
	 * @param  com.xzsoft.xc.ar.modal.ArPayLBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void updateGlCountPayL(ArPayLBean arPayLBean) {
		mapper.updateGlCountPayL(arPayLBean);
		
	}
	/**
	 * @Title:delPayLInfo
	 * @Description: 删除预收行表信息
	 * 参数格式:
	 * @param java.util.List
	 * @return
	 * @throws 
	 */
	@Override
	public void delPayLInfo(List<String> idList) {
		mapper.delPayLInfo(idList);
	}
	/**
	 * @Title:delInvGlLInfo
	 * @Description: 删除应收行表信息
	 * 参数格式:
	 * @param java.util.List
	 * @return
	 * @throws 
	 */
	@Override
	public void delInvGlLInfo(List<String> idList) {
		mapper.delInvGlLInfo(idList);
	}
	/**
	 * @Title:getPayHId
	 * @Description: 通过预收主表ID查询行表信息
	 * 参数格式:
	 * @param java.lang.String
	 * @return java.util.List
	 * @throws 
	 */
	@Override
	public List<HashMap<String, String>> getPayHId(String id) {
		return mapper.getPayHId(id);
	}
	/**
	 * @Title:getInvHId
	 * @Description: 通过应收主表ID查询行表信息
	 * 参数格式:
	 * @param java.lang.String
	 * @return java.util.List
	 * @throws 
	 */
	@Override
	public List<HashMap<String, String>> getInvHId(String id) {
		return mapper.getInvHId(id);
	}
	/**
	 * @Title checkInvIfUse
	 * @Description: 查看拆分出來的应收单是否被使用 
	 * 参数格式:
	 * @param java.util.List
	 * @return java.lang.String
	 * @throws 
	 */
	@Override
	public String checkInvIfUse(List<String> ids){
		return mapper.checkInvIfUse(ids);
	}
	/**
	 * @Title balanceInv
	 * @Description: 应收记账 
	 * 参数格式:
	 * @param java.util.List
	 * @return 
	 * @throws 
	 */
	@Override
	public void balanceInv(List<String> ids){
		mapper.balanceInv(ids);
	}
	/**
	 * @Title cancelBalanceInv
	 * @Description: 应收取消记账 
	 * 参数格式:
	 * @param java.util.List
	 * @return 
	 * @throws 
	 */
	@Override
	public void cancelBalanceInv(List<String> ids){
		mapper.cancelBalanceInv(ids);
	}
	/**
	 * @Title checkInvIfUse
	 * @Description: 查看拆分出來的预收单是否被使用 
	 * 参数格式:
	 * @param java.util.List
	 * @return java.lang.String
	 * @throws 
	 */
	@Override
	public String checkPayIfUse(List<String> ids){
		return mapper.checkPayIfUse(ids);
	}
	/**
	 * @Title balanceInv
	 * @Description: 预收记账 
	 * 参数格式:
	 * @param java.util.List
	 * @return 
	 * @throws 
	 */
	@Override
	public void balancePay(List<String> ids){
		mapper.balancePay(ids);
	}
	/**
	 * @Title cancelBalanceInv
	 * @Description: 预收取消记账 
	 * 参数格式:
	 * @param java.util.List
	 * @return 
	 * @throws 
	 */
	@Override
	public void cancelBalancePay(List<String> ids){
		mapper.cancelBalancePay(ids);
	}
}
