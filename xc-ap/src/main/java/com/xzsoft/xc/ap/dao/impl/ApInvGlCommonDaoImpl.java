package com.xzsoft.xc.ap.dao.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.ap.dao.ApInvGlCommonDao;
import com.xzsoft.xc.ap.mapper.ApInvGlCommonMapper;
import com.xzsoft.xc.ap.modal.ApInvGlHBean;
import com.xzsoft.xc.ap.modal.ApInvGlLBean;
import com.xzsoft.xc.ap.modal.ApPayHBean;
import com.xzsoft.xc.ap.modal.ApPayLBean;
import com.xzsoft.xc.apar.modal.ApInvTransBean;
import com.xzsoft.xip.platform.util.PlatformUtil;

/** 
 * @className
 * @Description 
 * @author 韦才文
 * @version 创建时间：2016年7月9日 下午12:45:50 
 */
@Repository("apInvGlHDao")
public class ApInvGlCommonDaoImpl implements ApInvGlCommonDao {
	@Resource
	ApInvGlCommonMapper mapper;
	/**
	 * @Title getQcDatas
	 * @Description: 应付供应商期初数据
	 * 参数格式:
	 * @param  java.util.HashMap
	 * @return java.util.HashMap
	 * @throws 
	 */
	@Override
	public HashMap<String, Object> getQcDatas(HashMap<String, String> paramMap) {
		return mapper.getQcDatas(paramMap);
	}
	/**
	 * @Title addInvGlHInfo
	 * @Description: 添加应付单主表信息
	 * 参数格式:
	 * @param com.xzsoft.xc.ap.modal.ApInvGlHBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void addInvGlHInfo(ApInvGlHBean apInvGlHBean) {
		mapper.addInvGlHInfo(apInvGlHBean);

	}
	/**
	 * @Title addInvGlLInfo
	 * @Description: 添加应付单行表信息
	 * 参数格式:
	 * @param com.xzsoft.xc.ap.modal.ApInvGlLBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void addInvGlLInfo(ApInvGlLBean apInvGlLBean) {
		mapper.addInvGlLInfo(apInvGlLBean);

	}
	/**
	 * @Title editInvGlHInfo
	 * @Description: 更新应付单主表信息 
	 * 参数格式:
	 * @param com.xzsoft.xc.ap.modal.ApInvGlHBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void editInvGlHInfo(ApInvGlHBean apInvGlHBean) {
		mapper.editInvGlHInfo(apInvGlHBean);
		
	}
	/**
	 * @Title delInvGlHInfo
	 * @Description: 通过主键ID删除应付单主表信息 
	 * 参数格式:
	 * @param java.util.List
	 * @return 
	 * @throws 
	 */
	@Override
	public void delInvGlHInfo(List<String> idList) {
		mapper.delInvGlHInfo(idList);
		
	}
	/**
	 * @Title getInvGlHInfo
	 * @Description: 通过ID查询应收单主表信息
	 * 参数格式:
	 * @param java.lang.String
	 * @return com.xzsoft.xc.ap.modal.ApInvGlHBean
	 * @throws 
	 */
	@Override
	public ApInvGlHBean getInvGlHInfo(String invGlHId) {
		
		return mapper.getInvGlHInfo(invGlHId);
	}
	/**
	 * @Title delInvGlHInfoByInvHId
	 * @Description: 通过发票ID删除应付单主表信息
	 * 参数格式:
	 * @param java.util.List
	 * @return 
	 * @throws 
	 */
	@Override
	public void delInvGlHInfoByInvHId(List<String> idList) {
		mapper.delInvGlHInfoByInvHId(idList);
		
	}
	/**
	 * @Title getQcDatasYuF
	 * @Description: 预付期初查询数据
	 * 参数格式:
	 * @param java.util.HashMap
	 * @return java.util.HashMap
	 * @throws 
	 */
	@Override
	public HashMap<String, Object> getQcDatasYuF(
			HashMap<String, String> paramMap) {
		return mapper.getQcDatasYuF(paramMap);
	}
	/**
	 * @Title getPayHInfo
	 * @Description: 通过ID查询预付单主表信息
	 * 参数格式:
	 * @param java.lang.String
	 * @return com.xzsoft.xc.ap.modal.ApPayHBean
	 * @throws 
	 */
	@Override
	public ApPayHBean getPayHInfo(String apPayHId) {
		return mapper.getPayHInfo(apPayHId);
	}
	
	/**
	 * @Title addPayHInfo
	 * @Description: 添加预付单主表信息
	 * 参数格式:
	 * @param com.xzsoft.xc.ap.modal.ApPayHBean
	 * @return
	 * @throws 
	 */
	@Override
	public void addPayHInfo(ApPayHBean apPayHBean) {
		mapper.addPayHInfo(apPayHBean);
		
	}
	
	/**
	 * @Title editPayHInfo
	 * @Description: 更新预付单主表信息
	 * 参数格式:
	 * @param com.xzsoft.xc.ap.modal.ApPayHBean
	 * @return
	 * @throws 
	 */
	@Override
	public void editPayHInfo(ApPayHBean apPayHBean) {
		mapper.editPayHInfo(apPayHBean);
		
	}
	
	/**
	 * @Title delPayHInfoByPayHId
	 * @Description: 通过发票ID删除预付单主表信息
	 * 参数格式:
	 * @param java.util.List
	 * @return
	 * @throws 
	 */
	@Override
	public void delPayHInfoByPayHId(List<String> idList) {
		mapper.delPayHInfoByPayHId(idList);
		
	}
	
	/**
	 * @Title addPayLInfo
	 * @Description: 添加预付单行表信息
	 * 参数格式:
	 * @param com.xzsoft.xc.ap.modal.ApPayLBean
	 * @return
	 * @throws 
	 */
	@Override
	public void addPayLInfo(ApPayLBean apPayLBean) {
		mapper.addPayLInfo(apPayLBean);
		
	}
	/**
	 * @Title delPayHInfoByPayHId
	 * @Description: 通过发票ID删除预付单主表信息
	 * 参数格式:
	 * @param java.util.List
	 * @return
	 * @throws 
	 */
	@Override
	public void delPayHInfo(List<String> idList) {
		mapper.delPayHInfo(idList);
		
	}
	
	
	/**
	 * @Title ifGlCount
	 * @Description: 查询科目是否已期初(条件：科目ID，客户ID,项目ID，部门ID) 
	 * 参数格式:
	 * @param java.util.HashMap
	 * @return java.lang.String
	 * @throws 
	 */
	@Override
	public String ifGlCount(HashMap<String, String> map) {
		return mapper.ifGlCount(map);
	}
	/**
	 * @Title ifGlCountYuF
	 * @Description: 查询科目是否已期初(预付)(条件：科目ID，客户ID,项目ID，部门ID) 
	 * 参数格式:
	 * @param java.util.HashMap
	 * @return java.lang.String
	 * @throws 
	 */
	@Override
	public String ifGlCountYuF(HashMap<String, String> map) {
		return mapper.ifGlCountYuF(map);
	}
	/**
	 * @Title updateGlCountInvL
	 * @Description: 更新应付单行表
	 * 参数格式:
	 * @param com.xzsoft.xc.ap.modal.ApInvGlLBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void updateGlCountInvL(ApInvGlLBean apInvGlLBean) {
		mapper.updateGlCountInvL(apInvGlLBean);
		
	}
	/**
	 * @Title updateGlCountPayL
	 * @Description: 更新预付单行表
	 * 参数格式:
	 * @param com.xzsoft.xc.ap.modal.ApPayLBean
	 * @return java.lang.String
	 * @throws 
	 */
	@Override
	public void updateGlCountPayL(ApPayLBean apPayLBean) {
		mapper.updateGlCountPayL(apPayLBean);
		
	}
	/**
	 * @Title:delInvGlLInfo
	 * @Description: 删除应付单行表信息
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
	 * @Title:delPayLInfo
	 * @Description: 删除预付单行表信息
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
	 * @Title:getPayHId
	 * @Description: 通过预付单主表ID查询行表信息
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
	 * @Description: 通过应付单主表ID查询行表信息
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
	 * @Description: 查看拆分出來的应付单是否被使用 
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
	 * @Description: 应付记账 
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
	 * @Description: 应付取消记账 
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
	 * @Description: 查看拆分出來的预付单是否被使用 
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
	 * @Description: 预付记账 
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
	 * @Description: 预付取消记账 
	 * 参数格式:
	 * @param java.util.List
	 * @return 
	 * @throws 
	 */
	@Override
	public void cancelBalancePay(List<String> ids){
		mapper.cancelBalancePay(ids);
	}
	/**
	 * @Title addInvTrans
	 * @Description: 插入交易明细
	 * 参数格式:
	 * @param com.xzsoft.xc.apar.modal.ApInvTransBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void addInvTrans(ApInvTransBean transBean){
		mapper.addInvTrans(transBean);
	}
	/**
	 * @Title updateInvtrans
	 * @Description: 更新交易明细
	 * 参数格式:
	 * @param com.xzsoft.xc.apar.modal.ApInvTransBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void updateInvtrans(ApInvTransBean transBean){
		mapper.updateInvtrans(transBean);
	}
	/**
	 * @Title updateInvtrans
	 * @Description: 删除交易明细
	 * 参数格式:
	 * @param java.util.List
	 * @return 
	 * @throws 
	 */
	@Override
	public void deleteInvTrans(List<String> ids){
		mapper.deleteInvTrans(ids);
	}
}
