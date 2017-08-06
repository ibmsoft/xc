package com.xzsoft.xc.ar.dao;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.apar.modal.ApInvTransBean;
import com.xzsoft.xc.apar.modal.ArInvTransBean;
import com.xzsoft.xc.ar.modal.ArCancelHBean;
import com.xzsoft.xc.ar.modal.ArCancelLBean;

/** 
 * @className ArCancelBaseDao
 * @Description 核销单Dao
 * @author 韦才文
 * @version 创建时间：2016年7月13日 上午9:46:58 
 */
public interface ArCancelBaseDao {
	/**
	 * @Title:addArCancelHInfo
	 * @Description: 添加核销单主表信息
	 * 参数格式:
	 * @param com.xzsoft.xc.ar.modal.ArCancelHBean
	 * @return
	 * @throws 
	 */
	public void addArCancelHInfo(ArCancelHBean cancelHBean);
	/**
	 * @Title:addInvTrans
	 * @Description: 添加应收交易明细信息
	 * 参数格式:
	 * @param com.xzsoft.xc.apar.modal.ArInvTransBean
	 * @return
	 * @throws 
	 */

	
	public void addInvTrans(ArInvTransBean arInvTransBean);
	/**
	 * @Title:editArCancelHInfo
	 * @Description: 更新核销单主表信息
	 * 参数格式:
	 * @param com.xzsoft.xc.ar.modal.ArCancelHBean
	 * @return
	 * @throws 
	 */
	public void editArCancelHInfo(ArCancelHBean cancelHBean);
	/**
	 * @Title:editInvTrans
	 * @Description: 更新应收交易明细信息
	 * 参数格式:
	 * @param com.xzsoft.xc.apar.modal.ArInvTransBean
	 * @return
	 * @throws 
	 */
	public void editInvTrans(ArInvTransBean arInvTransBean);
	/**
	 * @Title:delInvTransInfo
	 * @Description: 删除应收交易明细信息
	 * 参数格式:
	 * @param java.util.HashMap
	 * @return
	 * @throws 
	 */
	public void delInvTransInfo(HashMap<String, Object> map);
	/**
	 * @Title:getCancelLInfo
	 * @Description: 查询核销单行表信息
	 * 参数格式:
	 * @param java.util.HashMap
	 * @return com.xzsoft.xc.ar.modal.ArCancelLBean
	 * @throws 
	 */
	public ArCancelLBean getCancelLInfo(HashMap<String, String> map);
	/**
	 * @Title:addCancelLInfo
	 * @Description: 添加核销单行表信息
	 * 参数格式:
	 * @param com.xzsoft.xc.ar.modal.ArCancelLBean
	 * @return 
	 * @throws 
	 */
	public void addCancelLInfo(ArCancelLBean arCancelLBean);
	/**
	 * @Title:editCancelLInfo
	 * @Description: 更新核销单行表信息
	 * 参数格式:
	 * @param com.xzsoft.xc.ar.modal.ArCancelLBean
	 * @return 
	 * @throws 
	 */
	public void editCancelLInfo(ArCancelLBean arCancelLBean);
	/**
	 * @Title:delCancelLInfo
	 * @Description: 删除核销单行表信息
	 * 参数格式:
	 * @param com.xzsoft.xc.ar.modal.ArCancelLBean
	 * @return 
	 * @throws 
	 */
	public void delCancelLInfo(String cancelLId);
	/**
	 * @Title:isVerify
	 * @Description: 查询核销单是否审核通过
	 * 参数格式:
	 * @param java.lang.String
	 * @return java.lang.String
	 * @throws 
	 */
	public String isVerify(String cancelHId);
	/**
	 * @Title:getArCancelInfo
	 * @Description: 查询核销单主表信息
	 * 参数格式:
	 * @param java.lang.String
	 * @return com.xzsoft.xc.ar.modal.ArCancelHBean
	 * @throws 
	 */
	public ArCancelHBean getArCancelInfo(String cancelHId);
	/**
	 * @Title:addApInvTrans
	 * @Description: 添加应付交易明细信息
	 * 参数格式:
	 * @param com.xzsoft.xc.apar.modal.ApInvTransBean
	 * @return 
	 * @throws 
	 */
	public void addApInvTrans(ApInvTransBean apInvTransBean);
	/**
	 * @Title:editApInvTrans
	 * @Description: 修改应付交易明细
	 * 参数格式:
	 * @param com.xzsoft.xc.ap.modal.ApInvTransBean
	 * @return
	 * @throws 
	 */
	public void editApInvTrans(ApInvTransBean apInvTransBean);
	/**
	 * @Title:delApInvTransInfo
	 * @Description: 删除应付交易明细
	 * 参数格式:
	 * @param java.util.HashMap<String, String>
	 * @return
	 * @throws 
	 */
	public void delApInvTransInfo(HashMap<String, String> map);
	/**
	 * @Title:updateArInvGlHAmt
	 * @Description: 更新应收单金额
	 * 参数格式:
	 * @param java.util.HashMap
	 * @return
	 * @throws 
	 */
	public void updateArInvGlHAmt(HashMap<String, Object> map);
	/**
	 * @Title:updateApInvGlHAmt
	 * @Description: 更新应付单金额
	 * 参数格式:
	 * @param java.util.HashMap
	 * @return
	 * @throws 
	 */
	public void updateApInvGlHAmt(HashMap<String, Object> map);
	/**
	 * @Title:updatePayHAmt
	 * @Description: 更新收款单金额
	 * 参数格式:
	 * @param java.util.HashMap
	 * @return
	 * @throws 
	 */
	public void updatePayLAmt(HashMap<String, Object> map);
	/**
	 * @Title:
	 * @Description: 通过核销单主表ID查询核销单行表信息
	 * 参数格式:
	 * @param java.lang.String
	 * @return java.util.List
	 * @throws 
	 */
	public List<HashMap<String,Object>>  getCancelLByHId(String cancelHId);
	/**
	 * @Title:
	 * @Description: 通过核销单行表ID查询核销单行表信息
	 * 参数格式:
	 * @param java.lang.String
	 * @return com.xzsoft.xc.ar.modal.ArCancelLBean
	 * @throws 
	 */
	public ArCancelLBean getCancelLByLId(String cancelLId);

}
