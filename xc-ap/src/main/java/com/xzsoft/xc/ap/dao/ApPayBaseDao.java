/**
 * @Title:ApPayBaseDao.java
 * @package:com.xzsoft.xc.ap.dao
 * @Description:TODO
 * @date:2016年8月5日下午6:04:14
 * @version V1.0
 */
package com.xzsoft.xc.ap.dao;

import java.util.List;

import com.xzsoft.xc.ap.modal.ApCancelHBean;
import com.xzsoft.xc.ap.modal.ApCancelLBean;
import com.xzsoft.xc.ap.modal.ApPayHBean;
import com.xzsoft.xc.ap.modal.ApPayLBean;
import com.xzsoft.xc.apar.modal.ApInvTransBean;

/**
 * @ClassName: ApPayBaseDao
 * @Description: 付款单相关操作
 * @author:zhenghy
 * @date: 2016年8月5日 下午6:04:14
 */
public interface ApPayBaseDao {

	/**
	 * 
	 * @Title: selectApPayHsByIds  
	 * @Description: 查询付款单
	 * @param list
	 * @throws Exception     
	 * @return: List<ApPayHBean>    
	 *
	 */
	public List<ApPayHBean> selectApPayHsByIds(List<String> list) throws Exception;
	/**
	 * 
	 * @Title: selectApPayLsByIds  
	 * @Description: 查询付款单明细
	 * @param list
	 * @throws Exception     
	 * @return: List<ApPayLBean>    
	 *
	 */
	public List<ApPayLBean> selectApPayLsByIds(List<String> list) throws Exception;
	/**
	 * 
	 * @Title: insertApPayH  
	 * @Description: 插入付款单 
	 * @param apPayHBean
	 * @throws Exception     
	 * @return: void    
	 *
	 */
	public void insertApPayH(ApPayHBean apPayHBean) throws Exception;
	/**
	 * 
	 * @Title: updateApPayH  
	 * @Description: 修改付款单
	 * @param apPayHBean
	 * @throws Exception     
	 * @return: void    
	 *
	 */
	public void updateApPayH(ApPayHBean apPayHBean) throws Exception;
	/**
	 * 
	 * @Title: deleteApPayH  
	 * @Description: 删除付款单  
	 * @param ap_pay_h_id
	 * @throws Exception     
	 * @return: void    
	 *
	 */
	public void deleteApPayH(String ap_pay_h_id) throws Exception;
	/**
	 * 
	 * @Title: deleteApPayH  
	 * @Description: 删除付款单
	 * @param apPayHBean
	 * @throws Exception     
	 * @return: void    
	 *
	 */
	public void deleteApPayH(ApPayHBean apPayHBean) throws Exception;
	/**
	 * 
	 * @Title: deleteApPayHs  
	 * @Description: 删除付款单 
	 * @param list
	 * @throws Exception     
	 * @return: void    
	 *
	 */
	public void deleteApPayHs(List<String> list) throws Exception;
	/**
	 * 
	 * @Title: insertApPayLs  
	 * @Description: 插入付款单行
	 * @param list
	 * @throws Exception     
	 * @return: void    
	 *
	 */
	public void insertApPayLs(List<ApPayLBean> list) throws Exception;
	/**
	 * 
	 * @Title: updateApPayLs  
	 * @Description: 修改付款单行
	 * @param list
	 * @throws Exception     
	 * @return: void    
	 *
	 */
	public void updateApPayLs(List<ApPayLBean> list) throws Exception;
	/**
	 * 
	 * @Title: deleteApPayls  
	 * @Description: 删除付款单
	 * @param list
	 * @throws Exception     
	 * @return: void    
	 *
	 */
	public void deleteApPayls(List<String> list) throws Exception;
	/**
	 * 
	 * @Title: deleteApPayLs  
	 * @Description: 删除付款单行 
	 * @param list
	 * @throws Exception     
	 * @return: void    
	 *
	 */
	public void deleteApPayLs(List<ApPayLBean> list) throws Exception;
	/**
	 * @Title: updateApPayLAmount
	 * @Description: 修改付款单行金额（核销金额，待核销金额）
	 * @param opType
	 * @param apPayLBean
	 * @throws Exception  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void updateApPayLCancelAmt(String opType,List<ApPayLBean> apPayLBeanList) throws Exception;
	//////////////////////////////////////////应付单相关操作//////////////////////////////////////////////////////
	/**
	 * 
	 * @Title: updateApInvHPaidAmount  
	 * @Description: 修改应付单已付款金额 
	 * @param list
	 * @throws Exception     
	 * @return: void    
	 *
	 */
	public void updateApInvHPaidAmount(String opType,List<ApPayLBean> list) throws Exception;
	/**
	 * @Title: updateApInvHPaidAmt_REQ
	 * @Description: 修改应付单付款金额、未付款金额、占用金额
	 * @param opType
	 * @param list
	 * @throws Exception  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void updateApInvHPaidAmt_REQ(String opType,List<ApPayLBean> list)throws Exception;
	/**
	 * 
	 * @Title: updateApInvHOccupyAmt  
	 * @Description: 修改应付单占用金额
	 * @param opType
	 * @param list
	 * @throws Exception     
	 * @return: void    
	 *
	 */
	public void updateApInvHOccupyAmt(String opType,List<ApPayLBean> list) throws Exception;
	////////////////////////////////////////申请单相关操作///////////////////////////////////////////////////////
	/**
	 * 
	 * @Title: updateApPayReqLPaidAmount  
	 * @Description: 修改申请单行付款金额
	 * @param opType
	 * @param list
	 * @throws Exception     
	 * @return: void    
	 *
	 */
	public void updateApPayReqLPaidAmount(String opType, List<ApPayLBean> list) throws Exception;
	/**
	 * 
	 * @Title: updateApPayReqHPaidAmount  
	 * @Description: 修改申请单付款金额
	 * @param opType
	 * @param list
	 * @throws Exception     
	 * @return: void    
	 *
	 */
	public void updateApPayReqHPaidAmount(String opType,List<ApPayLBean> list) throws Exception;
	
	////////////////////////////////////////交易明细相关操作///////////////////////////////////////////////////////
	/**
	 * 
	 * @Title: insertTrans  
	 * @Description: 插入明细
	 * @param list
	 * @throws Exception     
	 * @return: void    
	 *
	 */
	public void insertTrans(List<ApInvTransBean> list) throws Exception;
	/**
	 * 
	 * @Title: deleteTrans  
	 * @Description: 删除明细  
	 * @param list
	 * @throws Exception     
	 * @return: void    
	 *
	 */
	public void deleteTrans(List<ApInvTransBean> list) throws Exception;
	/**
	 * 
	 * @Title: deleteTransByPayLs  
	 * @Description: 删除明细
	 * @param list
	 * @throws Exception     
	 * @return: void    
	 *
	 */
	public void deleteTransByPayLs(List<ApPayLBean> list) throws Exception;
	/**
	 * @Title: ifPayIdExistInPayL
	 * @Description: 付款单，若被引用，查询出引用记录
	 * @param AP_PAY_H_ID
	 * @throws Exception  设定文件 
	 * @return  List<ApPayLBean> 返回类型 
	 * @throws
	 */
	public List<ApPayLBean> ifPayIdExistInPayL(String AP_PAY_H_ID) throws Exception;
	/**
	 * @Title: ifPayIdExistInCancelH
	 * @Description: 查询出付款单被核销单主引用的记录
	 * @param AP_PAY_H_ID
	 * @return  设定文件 
	 * @return  List<ApCancelHBean> 返回类型 
	 * @throws
	 */
	public List<ApCancelHBean> ifPayIdExistInCancelH(String AP_PAY_H_ID) throws Exception;
	/**
	 * @Title: ifPayIdExistInCancelL
	 * @Description: 查询出付款单被核销单行引用的记录
	 * @param AP_PAY_H_ID
	 * @return  设定文件 
	 * @return  List<ApCancelLBean> 返回类型 
	 * @throws
	 */
	public List<ApCancelLBean> ifPayIdExistInCancelL(String AP_PAY_H_ID) throws Exception;
	/**
	 * @Title: calculateApPayLCancelAmt
	 * @Description: 计算预付款单核销金额
	 * @param AP_PAY_H_ID
	 * @param cancelAmt
	 * @throws Exception  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void calculateApPayLCancelAmt(String AP_PAY_H_ID , double cancelAmt) throws Exception;
	/**
	 * @Title: selectApDocCatCodeByApPayHId
	 * @Description: 根据id查询付款单单据类型
	 * @param AP_PAY_H_ID
	 * @throws Exception  设定文件 
	 * @return  String 返回类型 
	 * @throws
	 */
	public String selectApDocCatCodeByApPayHId(String AP_PAY_H_ID) throws Exception;
}

