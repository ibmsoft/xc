package com.xzsoft.xc.ar.dao.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.apar.modal.ApInvTransBean;
import com.xzsoft.xc.apar.modal.ArInvTransBean;
import com.xzsoft.xc.ar.dao.ArCancelBaseDao;
import com.xzsoft.xc.ar.mapper.ArCancelBaseMapper;
import com.xzsoft.xc.ar.modal.ArCancelHBean;
import com.xzsoft.xc.ar.modal.ArCancelLBean;

/** 
 * @className
 * @Description 
 * @author 韦才文
 * @version 创建时间：2016年7月13日 上午9:47:54 
 */
@Repository("arCancelBaseDao")
public class ArCancelBaseDaoImpl implements ArCancelBaseDao {
	@Resource
	ArCancelBaseMapper mapper;
	/**
	 * @Title:addArCancelHInfo
	 * @Description: 添加核销单主表信息
	 * 参数格式:
	 * @param com.xzsoft.xc.ar.modal.ArCancelHBean
	 * @return
	 * @throws 
	 */
	@Override
	public void addArCancelHInfo(ArCancelHBean cancelHBean) {
		mapper.addArCancelHInfo(cancelHBean);
	}
	/**
	 * @Title:editArCancelHInfo
	 * @Description: 更新核销单主表信息
	 * 参数格式:
	 * @param com.xzsoft.xc.ar.modal.ArCancelHBean
	 * @return
	 * @throws 
	 */
	@Override
	public void editArCancelHInfo(ArCancelHBean cancelHBean){
		mapper.editArCancelHInfo(cancelHBean);
	}
	/**
	 * @Title:editInvTrans
	 * @Description: 更新应收交易明细信息
	 * 参数格式:
	 * @param com.xzsoft.xc.apar.modal.ArInvTransBean
	 * @return
	 * @throws 
	 */
	@Override
	public void editInvTrans(ArInvTransBean arInvTransBean){
		mapper.editInvTrans(arInvTransBean);
	}
	/**
	 * @Title:addInvTrans
	 * @Description: 添加应收交易明细信息
	 * 参数格式:
	 * @param com.xzsoft.xc.apar.modal.ArInvTransBean
	 * @return
	 * @throws 
	 */
	@Override
	public void addInvTrans(ArInvTransBean arInvTransBean) {
		mapper.addInvTrans(arInvTransBean);
	}
	
	/**
	 * @Title:delInvTransInfo
	 * @Description: 删除应收交易明细信息
	 * 参数格式:
	 * @param java.util.HashMap
	 * @return
	 * @throws 
	 */
	@Override
	public void delInvTransInfo(HashMap<String, Object> map) {
		mapper.delInvTransInfo(map);
		
	}
	/**
	 * @Title:getCancelLInfo
	 * @Description: 查询核销单行表信息
	 * 参数格式:
	 * @param java.util.HashMap
	 * @return com.xzsoft.xc.ar.modal.ArCancelLBean
	 * @throws 
	 */
	@Override
	public ArCancelLBean getCancelLInfo(HashMap<String, String> map){
		return mapper.getCancelLInfo(map);
	}
	/**
	 * @Title:addCancelLInfo
	 * @Description: 添加核销单行表信息
	 * 参数格式:
	 * @param com.xzsoft.xc.ar.modal.ArCancelLBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void addCancelLInfo(ArCancelLBean arCancelLBean) {
		mapper.addCancelLInfo(arCancelLBean);
		
	}
	/**
	 * @Title:editCancelLInfo
	 * @Description: 更新核销单行表信息
	 * 参数格式:
	 * @param com.xzsoft.xc.ar.modal.ArCancelLBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void editCancelLInfo(ArCancelLBean arCancelLBean) {
		mapper.editCancelLInfo(arCancelLBean);
		
	}
	/**
	 * @Title:delCancelLInfo
	 * @Description: 删除核销单行表信息
	 * 参数格式:
	 * @param com.xzsoft.xc.ar.modal.ArCancelLBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void delCancelLInfo(String cancelLId) {
		mapper.delCancelLInfo(cancelLId);
		
	}
	/**
	 * @Title:isVerify
	 * @Description: 查询核销单是否审核通过
	 * 参数格式:
	 * @param java.lang.String
	 * @return java.lang.String
	 * @throws 
	 */
	@Override
	public String isVerify(String cancelHId) {
		return mapper.isVerify(cancelHId);
	}
	/**
	 * @Title:getArCancelInfo
	 * @Description: 查询核销单主表信息
	 * 参数格式:
	 * @param java.lang.String
	 * @return com.xzsoft.xc.ar.modal.ArCancelHBean
	 * @throws 
	 */
	@Override
	public ArCancelHBean getArCancelInfo(String cancelHId) {
		return mapper.getArCancelInfo(cancelHId);
	}
	/**
	 * @Title:addApInvTrans
	 * @Description: 添加应付交易明细信息
	 * 参数格式:
	 * @param com.xzsoft.xc.apar.modal.ApInvTransBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void addApInvTrans(ApInvTransBean apInvTransBean){
		mapper.addApInvTrans(apInvTransBean);
	}
	/**
	 * @Title:editApInvTrans
	 * @Description: 修改应付交易明细
	 * 参数格式:
	 * @param com.xzsoft.xc.ap.modal.ApInvTransBean
	 * @return
	 * @throws 
	 */
	@Override
	public void editApInvTrans(ApInvTransBean apInvTransBean) {
		mapper.editApInvTrans(apInvTransBean);
	}
	/**
	 * @Title:delApInvTransInfo
	 * @Description: 删除应付交易明细
	 * 参数格式:
	 * @param java.util.HashMap<String, String>
	 * @return
	 * @throws 
	 */
	@Override
	public void delApInvTransInfo(HashMap<String, String> map) {
		mapper.delApInvTransInfo(map);
	}
	/**
	 * @Title:updateArInvGlHAmt
	 * @Description: 更新应收单金额
	 * 参数格式:
	 * @param java.util.HashMap
	 * @return
	 * @throws 
	 */
	@Override
	public void updateArInvGlHAmt(HashMap<String, Object> map) {
		mapper.updateArInvGlHAmt(map);
	}
	/**
	 * @Title:updateApInvGlHAmt
	 * @Description: 更新应付单金额
	 * 参数格式:
	 * @param java.util.HashMap
	 * @return
	 * @throws 
	 */
	@Override
	public void updateApInvGlHAmt(HashMap<String, Object> map) {
		mapper.updateApInvGlHAmt(map);
	}
	/**
	 * @Title:updatePayHAmt
	 * @Description: 更新收款单金额
	 * 参数格式:
	 * @param java.util.HashMap
	 * @return
	 * @throws 
	 */
	@Override
	public void updatePayLAmt(HashMap<String, Object> map) {
		mapper.updatePayLAmt(map);
		
	}
	/**
	 * @Title:
	 * @Description: 通过核销单主表ID查询核销单行表信息
	 * 参数格式:
	 * @param java.lang.String
	 * @return java.util.List
	 * @throws 
	 */
	@Override
	public List<HashMap<String,Object>>  getCancelLByHId(String cancelHId) {
		return mapper.getCancelLByHId(cancelHId);
	}
	/**
	 * @Title:
	 * @Description: 通过核销单行表ID查询核销单行表信息
	 * 参数格式:
	 * @param java.lang.String
	 * @return com.xzsoft.xc.ar.modal.ArCancelLBean
	 * @throws 
	 */
	@Override
	public ArCancelLBean getCancelLByLId(String cancelLId){
		return mapper.getCancelLByLId(cancelLId);
	}
}
