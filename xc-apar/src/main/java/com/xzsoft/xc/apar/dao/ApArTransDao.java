package com.xzsoft.xc.apar.dao;

import java.util.List;

import com.xzsoft.xc.apar.modal.ApInvTransBean;
import com.xzsoft.xc.apar.modal.ArInvTransBean;

/**
 * 
  * @ClassName：ApArTransDao
  * @Description：应付和应收模块交易明细表处理
  * @author：RenJianJian
  * @date：2016年9月29日 上午9:36:59
 */
public interface ApArTransDao {
	/**
	 * @Title: getApInvTransByGlId 方法名
	 * @Description：根据应付单ID查询应付模块交易明细表信息
	 * @param：@param AP_INV_GL_H_ID
	 * @param：@return
	 * @param：@throws Exception 
	 * @return List<ApInvTransBean> 返回类型
	 */
	public List<ApInvTransBean> getApInvTransByGlId(String AP_INV_GL_H_ID) throws Exception;
	/**
	 * @Title: insertApTrans
	 * @Description: 批量新增交易明细-应付
	 * @param list 交易明细集合
	 * @throws Exception 
	 * @return  void 返回类型 
	 */
	public void insertApTrans(List<ApInvTransBean> list) throws Exception;
	/**
	 * @Title: updateApTrans
	 * @Description: 批量修改交易明细-应付
	 * @param list
	 * @throws Exception 
	 * @return  void 返回类型 
	 */
	public void updateApTrans(List<ApInvTransBean> list) throws Exception;
	/**
	 * @Title: deleteApTrans
	 * @Description: 批量删除交易明细-应付
	 * @param list 交易明细集合
	 * @throws Exception 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void deleteApTrans(List<ApInvTransBean> list) throws Exception;
	/**
	 * @Title: deleteApTrans2
	 * @Description: 批量删除交易明细-应付(根据状态)
	 * @param list
	 * @throws Exception  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void deleteApTrans2(List<ApInvTransBean> list) throws Exception;
	
	/**
	 * @Title: getArInvTransByGlId
	 * @Description: 查询交易明细表（根据应收单ID）-应收
	 * @param AR_INV_GL_H_ID 应收单id
	 * @throws Exception 
	 * @return  List<ArInvTransBean> 返回类型 
	 */
	public List<ArInvTransBean> getArInvTransByGlId(String AR_INV_GL_H_ID) throws Exception;
	/**
	 * @Title: insertArTrans
	 * @Description: 新增交易明细-应收
	 * @param list
	 * @throws Exception  设定文件 
	 * @return  void 返回类型 
	 */
	public void insertArTrans(List<ArInvTransBean> list) throws Exception;
	/**
	 * @Title: updateArTrans
	 * @Description: 修改交易明细-应收
	 * @param list
	 * @throws Exception  设定文件 
	 * @return  void 返回类型 
	 */
	public void updateArTrans(List<ArInvTransBean> list)throws Exception;
	/**
	 * @Title: deleteArTrans
	 * @Description: 删除交易明细-应收
	 * @param list
	 * @throws Exception  设定文件 
	 * @return  void 返回类型 
	 */
	public void deleteArTrans(List<ArInvTransBean> list) throws Exception;
	/**
	 * @Title: updateArTransByCancel
	 * @Description: 修改交易明细-应收(核销单)
	 * @param list
	 * @throws Exception  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void updateArTransByCancel(List<ArInvTransBean> list) throws Exception;
	/**
	 * @Title: updateArTransByDocH
	 * @Description: 修改应收交易明细（当单据主信息发生修改而行信息没有发生修改时，也可以修改交易明细的业务日期和摘要） 
	 * @param arInvTransBean
	 * @throws Exception  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void updateArTransByDocH(ArInvTransBean arInvTransBean) throws Exception;
}

