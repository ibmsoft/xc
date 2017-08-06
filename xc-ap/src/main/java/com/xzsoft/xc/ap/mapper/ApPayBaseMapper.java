/**
 * @Title:ApPayBaseMapper.java
 * @package:com.xzsoft.xc.ap.mapper
 * @Description:TODO
 * @date:2016年8月4日下午4:44:36
 * @version V1.0
 */
package com.xzsoft.xc.ap.mapper;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.ap.modal.ApCancelHBean;
import com.xzsoft.xc.ap.modal.ApCancelLBean;
import com.xzsoft.xc.ap.modal.ApPayHBean;
import com.xzsoft.xc.ap.modal.ApPayLBean;
import com.xzsoft.xc.apar.modal.ApInvTransBean;

/**
 * @ClassName: ApPayBaseMapper
 * @Description: 应付模块付款单相关操作
 * @author:zhenghy
 * @date: 2016年8月4日 下午4:44:36
 */
public interface ApPayBaseMapper {
	/**
	 * 
	 * @Title: selectApPayHsByIds  
	 * @Description: 查询付款单
	 * @param list
	 * @return: List<ApPayHBean>    
	 *
	 */
	public List<ApPayHBean> selectApPayHsByIds(List<String> list);
	/**
	 * 
	 * @Title: selectApPayLsByIds  
	 * @Description: 查询付款单明细 
	 * @param list
	 * @return: List<ApPayHBean>    
	 *
	 */
	public List<ApPayLBean> selectApPayLsByIds(List<String> list);
	/**
	 * 
	 * @Title: insertApPayH  
	 * @Description: 新增付款单
	 * @param apPayHBean     
	 * @return: void    
	 *
	 */
	public void insertApPayH(ApPayHBean apPayHBean);
	/**
	 * 
	 * @Title: updateApPayH  
	 * @Description: 修改付款单
	 * @param apPayHBean     
	 * @return: void    
	 *
	 */
	public void updateApPayH(ApPayHBean apPayHBean);
	/**
	 * 
	 * @Title: deleteApPayH  
	 * @Description: 删除付款单
	 * @param ap_pay_h_id     
	 * @return: void    
	 *
	 */
	public void deleteApPayH(String ap_pay_h_id);
	/**
	 * 
	 * @Title: deleteApPayHs  
	 * @Description: 删除付款单  
	 * @param list     
	 * @return: void    
	 *
	 */
	public void deleteApPayHs(List<String> list);
	/**
	 * 
	 * @Title: insertApPayLs  
	 * @Description: 批量新增付款单明细
	 * @param map     
	 * @return: void    
	 *
	 */
	public void insertApPayLs(HashMap<String, Object> map);
	/**
	 * 
	 * @Title: updateApPayLs  
	 * @Description: 批量修改付款单明细 
	 * @param map     
	 * @return: void    
	 *
	 */
	public void updateApPayLs(HashMap<String, Object> map);
	/**
	 * 
	 * @Title: deleteApPayLs  
	 * @Description: 批量删除付款单明细
	 * @param list     
	 * @return: void    
	 *
	 */
	public void deleteApPayLs(List<String> list);
	/**
	 * @Title: updateApPayLAmount
	 * @Description: 修改付款单金额
	 * @return  void 返回类型 
	 * @throws
	 */
	public void updateApPayLCancelAmt(HashMap<String, Object> map);
	///////////////////////////应付单相关操作///////////////////////////////////////////
	/**
	 * 
	 * @Title: updateApInvHPaidAmount  
	 * @Description: 修改应付单付款金额
	 * @param map     
	 * @return: void    
	 *
	 */
	public void updateApInvHPaidAmount(HashMap<String, Object> map);
	/**
	 * @Title: updateApInvHPaidAmtAndOccupyAmt
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param map  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void updateApInvHPaidAmt_REQ(HashMap<String, Object> map);
	/**
	 * 
	 * @Title: updateApInvHOccupyAmt  
	 * @Description: 修改应付单占用金额
	 * @param map     
	 * @return: void    
	 *
	 */
	public void updateApInvHOccupyAmt(HashMap<String, Object> map);
	
	/////////////////////////////////申请单相关操作//////////////////////////////////////
	/**
	 * 
	 * @Title: updateApPayReqPaidAmount  
	 * @Description: 修改申请单付款金额
	 * @param map     
	 * @return: void    
	 *
	 */
	public void updateApPayReqLPaidAmount(HashMap<String, Object> map);
	/**
	 * 
	 * @Title: updateApPayReqHPaidAmount  
	 * @Description: 修改申请单主付款金额
	 * @param map     
	 * @return: void    
	 *
	 */
	public void updateApPayReqHPaidAmount(HashMap<String, Object> map);
	
	/////////////////////////////////////交易明细////////////////////////////////////////
	/**
	 * 
	 * @Title: selectTransByPayId  
	 * @Description: 查询交易明细 
	 * @param list
	 * @return     
	 * @return: List<ApInvTransBean>    
	 *
	 */
	public List<ApInvTransBean> selectTransByPayId(List<String> list);
	/**
	 * 
	 * @Title: insertTrans  
	 * @Description: 插入交易明细 
	 * @param map     
	 * @return: void    
	 *
	 */
	public void insertTrans(HashMap<String, Object> map);
	/**
	 * 
	 * @Title: deleteTrans  
	 * @Description: 删除交易明细
	 * @param map     
	 * @return: void    
	 *
	 */
	public void deleteTrans(HashMap<String, Object> map);
	/**
	 * @Title: ifPayIdExistInPayL
	 * @Description: 查询出付款单被付款单引用的记录
	 * @param AP_PAY_H_ID
	 * @return  设定文件 
	 * @return  List<ApPayLBean> 返回类型 
	 * @throws
	 */
	public List<ApPayLBean> ifPayIdExistInPayL(String AP_PAY_H_ID);
	/**
	 * @Title: ifPayIdExistInCancelH
	 * @Description: 查询出付款单被核销单主引用的记录
	 * @param AP_PAY_H_ID
	 * @return  设定文件 
	 * @return  List<ApCancelHBean> 返回类型 
	 * @throws
	 */
	public List<ApCancelHBean> ifPayIdExistInCancelH(String AP_PAY_H_ID);
	/**
	 * @Title: ifPayIdExistInCancelL
	 * @Description: 查询出付款单被核销单行引用的记录
	 * @param AP_PAY_H_ID
	 * @return  设定文件 
	 * @return  List<ApCancelLBean> 返回类型 
	 * @throws
	 */
	public List<ApCancelLBean> ifPayIdExistInCancelL(String AP_PAY_H_ID);
	/**
	 * @Title: calculateApPayLCancelAmt
	 * @Description: 计算预付款单核销金额
	 * @param map  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void calculateApPayLCancelAmt(HashMap<String, Object> map);
	/**
	 * @Title: selectApDocCatCodeByApPayHId
	 * @Description: 根据id查询付款单单据类型
	 * @param AP_PAY_H_ID
	 * @return  设定文件 
	 * @return  String 返回类型 
	 * @throws
	 */
	public String selectApDocCatCodeByApPayHId(String AP_PAY_H_ID);
}
