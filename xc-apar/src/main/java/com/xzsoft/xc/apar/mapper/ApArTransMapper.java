package com.xzsoft.xc.apar.mapper;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.apar.modal.ApInvTransBean;
import com.xzsoft.xc.apar.modal.ArInvTransBean;

/**
 * 
  * @ClassName：ApArTransMapper
  * @Description：应付和应收模块交易明细表处理
  * @author：RenJianJian
  * @date：2016年9月29日 上午9:33:30
 */
public interface ApArTransMapper {
	/**
	 * 
	 * @Title: getApInvTransByGlId 方法名
	 * @Description：根据应付单ID查询应付模块交易明细表信息
	 * @param：@param AP_INV_GL_H_ID
	 * @param：@return 设定文件
	 * @return List<ApInvTransBean> 返回类型
	 */
	public List<ApInvTransBean> getApInvTransByGlId(String AP_INV_GL_H_ID);
	/**
	 * @Title: insertApTrans
	 * @Description: 批量新增交易明细-应付
	 * @param: map  设定文件 
	 * @return:  void 返回类型 
	 * @throws
	 */
	public void insertApTrans(HashMap<String, Object> map);
	/**
	 * @Title: updateApTrans
	 * @Description: 批量修改交易明细-应付
	 * @param map  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void updateApTrans(HashMap<String, Object> map);
	/**
	 * @Title: deleteApTrans
	 * @Description: 批量删除交易明细-应付
	 * @param map  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void deleteApTrans(HashMap<String, Object> map);
	/**
	 * @Title: deleteApTrans2
	 * @Description: 批量删除交易明细-应付(根据状态)
	 * @param map  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void deleteApTrans2(HashMap<String, Object> map);
	
	/*----------------------------------------应收-------------------------------------*/
	/**
	 * @Title: getArInvTransByGlId
	 * @Description: 查询交易明细表（根据应收单ID）-应收
	 * @param AR_INV_GL_H_ID 应收单id
	 * @return  设定文件 
	 * @return  List<ArInvTransBean> 返回类型 
	 * @throws
	 */
	public List<ArInvTransBean> getArInvTransByGlId(String AR_INV_GL_H_ID);
	/**
	 * @Title: insertArTrans
	 * @Description: 新增交易明细-应收
	 * @param map  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void insertArTrans(HashMap<String, Object> map);
	/**
	 * @Title: updateArTrans
	 * @Description: 修改交易明细-应收
	 * @param map  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void updateArTrans(HashMap<String, Object> map);
	/**
	 * @Title: deleteArTrans
	 * @Description: 删除交易明细-应收
	 * @param map  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void deleteArTrans(HashMap<String, Object> map);
	/**
	 * @Title: updateArTransByCancel
	 * @Description: 修改交易明细-应收(核销单)
	 * @param map  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void updateArTransByCancel(HashMap<String, Object> map);
	/**
	 * @Title: updateArTransByDocH
	 * @Description: 修改应收交易明细（当单据主信息发生修改而行信息没有发生修改时，也可以修改交易明细的业务日期和摘要） 
	 * @param arInvTransBean  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void updateArTransByDocH(ArInvTransBean arInvTransBean);
}

