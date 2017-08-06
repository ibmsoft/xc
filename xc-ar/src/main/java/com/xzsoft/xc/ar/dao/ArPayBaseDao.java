/**
 * @Title:ArPayBaseDao.java
 * @package:com.xzsoft.xc.ar.dao
 * @Description:TODO
 * @date:2016年7月11日上午9:06:30
 * @version V1.0
 */
package com.xzsoft.xc.ar.dao;

import java.util.List;

import com.xzsoft.xc.apar.modal.ArInvTransBean;
import com.xzsoft.xc.ar.modal.ArPayHBean;
import com.xzsoft.xc.ar.modal.ArPayLBean;

/**
 * @ClassName: ArPayBaseDao
 * @Description: TODO
 * @author:zhenghy
 * @date 2016年7月11日 上午9:06:30
 */
public interface ArPayBaseDao {


	/**
	 * @Title: insertArPayH2
	 * @Description: 新增收款单主信息
	 * @param arPayHBean
	 * @throws Exception  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void insertArPayH2(ArPayHBean arPayHBean) throws Exception;	

	/**
	 * @Title: updateArPayH2
	 * @Description: 修改收款单主信息
	 * @param arPayHBean
	 * @throws Exception  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void updateArPayH2(ArPayHBean arPayHBean) throws Exception;
	/**
	 * @Title: deleteArPayH
	 * @Description: 删除收款单主信息
	 * @param @param arPayHBean
	 * @param @throws Exception 
	 * @return 
	 * @throws
	 */
	public void deleteArPayH(List<String> list) throws Exception;
	/**
	 * 
	 * @Title: getArPayLs  
	 * @Description: 根据收款单id集合查找收款单明细 
	 * @param list
	 * @throws Exception     
	 * @return: List<ArPayLBean>    
	 *
	 */
	public List<ArPayLBean> getArPayLs(List<String> list) throws Exception;

	/**
	 * @Title: insertArPayLs
	 * @Description: 批量新增收款单行 
	 * @param arPayLBeanList
	 * @throws Exception  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void insertArPayLs(List<ArPayLBean> arPayLBeanList)throws Exception;
	/**
	 * @Title: updateArPayLs
	 * @Description: 批量修改收款单行
	 * @param arPayLBeanList
	 * @throws Exception  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void updateArPayLs(List<ArPayLBean> arPayLBeanList)throws Exception;
	/**
	 * @Title: deleteArPayLs
	 * @Description: 批量删除收款单行
	 * @param arPayLBeanList
	 * @throws Exception  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void deleteArPayLs(List<ArPayLBean> arPayLBeanList)throws Exception;
	
	/**
	 * 
	 * @Title: updateArInvGLPayAmt
	 * @Description: 修改应收单的收款金额（包括未收款金额）
	 * @param opType
	 * @param arPayHBean
	 * @param arPayLBeanList
	 * @throws Exception  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void updateArInvGLPayAmt(String opType,String ar_doc_cat_code,List<ArPayLBean> arPayLBeanList)throws Exception; 
	/**
	 * 
	 * @Title: updateArPayLCancelAmt
	 * @Description: 修改预收款行核销金额（包括未核销金额）
	 * @param opType
	 * @param ar_doc_cat_code
	 * @param arPayLBeanList
	 * @throws Exception  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void updateArPayLCancelAmt(String opType,String ar_doc_cat_code,List<ArPayLBean> arPayLBeanList)throws Exception;
	/**
	 * 
	 * @Title: insertTrans
	 * @Description: 新增交易明细
	 * @param arInvTransBeanList
	 * @throws Exception  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void insertTrans(List<ArInvTransBean> arInvTransBeanList)throws Exception;
	/**
	 * 
	 * @Title: updateTrans
	 * @Description: 修改交易明细
	 * @param arInvTransBeanList
	 * @throws Exception  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void updateTrans(List<ArInvTransBean> arInvTransBeanList)throws Exception;
	/**
	 * 
	 * @Title: deleteTrans
	 * @Description: 删除交易明细
	 * @param arInvTransBeanList
	 * @throws Exception  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void deleteTrans(List<ArInvTransBean> arInvTransBeanList)throws Exception;
	/**
	 * @Title: updateArPayAmt
	 * @Description: 修改收款单金额（预收款红蓝）
	 * @param arPayIdList
	 * @throws Exception  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void updateArPayAmt(List<String> arPayIdList) throws Exception;
}
