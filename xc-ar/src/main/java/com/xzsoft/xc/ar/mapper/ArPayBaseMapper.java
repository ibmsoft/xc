package com.xzsoft.xc.ar.mapper;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.ar.modal.ArCancelHBean;
import com.xzsoft.xc.ar.modal.ArCancelLBean;
import com.xzsoft.xc.ar.modal.ArPayHBean;
import com.xzsoft.xc.ar.modal.ArPayLBean;

/**
 * @ClassName: ArPayBaseMapper
 * @Description: TODO
 * @author:zhenghy
 * @date 2016年7月8日 下午3:58:34
 */
public interface ArPayBaseMapper {
	/**
	 * @Title: ArPayBaseMapper
	 * @Description: 新增收款单主信息
	 * @param @param arPayHBean 
	 * @return 
	 * @throws
	 */
	public void insertArPayH(ArPayHBean arPayHBean);
	/**
	 * @Title: ArPayBaseMapper
	 * @Description: 修改收款单主信息
	 * @param @param arPayHBean 
	 * @return 
	 * @throws
	 */
	public void updateArPayH(ArPayHBean arPayHBean);
	/**
	 * @Title: ArPayBaseMapper
	 * @Description: 删除收款单主信息
	 * @param @param list 
	 * @return 
	 * @throws
	 */
	public void deleteArPayH(List<String> list);
	/**
	 * @Title: ArPayBaseMapper
	 * @Description: 新增收款单行信息
	 * @param @param list 
	 * @return 
	 * @throws
	 */
	public void insertArPayL(HashMap<String,Object> map);
	/**
	 * @Title: ArPayBaseMapper
	 * @Description: 修改收款单行信息
	 * @param @param list 
	 * @return 
	 * @throws
	 */
	public void updateArPayL(HashMap<String,Object> map);
	/**
	 * @Title: ArPayBaseMapper
	 * @Description: 删除收款单行信息
	 * @param @param list 
	 * @return 
	 * @throws
	 */
	public void deleteArPayLByLIds(List<String> list);
	/**
	 * 
	 * @Title: deleteArPayLByHId  
	 * @Description: 删除收款单行信息
	 * @param list     
	 * @return: void    
	 *
	 */
	public void deleteArPayLByHIds(List<String> list);
	/**
	 * 
	 * @Title: getArPayL  
	 * @Description: 根据主id集合查询收款单行信息
	 * @param list
	 * @return: List<ArPayLBean>    
	 *
	 */
	public List<ArPayLBean> getArPayLs(List<String> list);
	/**
	 * 
	 * @Title: getArPayLById  
	 * @Description: TODO(这里用一句话描述这个方法的作用)  
	 * @param ar_pay_h_id
	 * @return: List<ArPayLBean>    
	 *
	 */
	public List<ArPayLBean> getArPayLByHId(String ar_pay_h_id);
	
	/**
	 * @Title: updateArInvGLPayAmt
	 * @Description: 修改应收单的收款金额（包含未收款金额）
	 * @param map  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void updateArInvGLPayAmt(HashMap<String,Object> map);
	
	/**
	 * 
	 * @Title: updateArPayLCancelAmt
	 * @Description: 修改预收款的核销金额
	 * @param map  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void updateArPayLCancelAmt(HashMap<String,Object> map);
	
	/**
	 * 
	 * @Title: insertArTrans
	 * @Description: 新增交易明细
	 * @param map  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void insertArTrans(HashMap<String,Object> map);
	/**
	 * 
	 * @Title: updateArTrans
	 * @Description: 修改交易明细
	 * @param map  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void updateArTrans(HashMap<String,Object> map);
	/**
	 * 
	 * @Title: deleteArTrans
	 * @Description: 删除交易明细
	 * @param map  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void deleteArTrans(HashMap<String,Object> map);
	/**
	 * @Title: selectArPayFromPayL
	 * @Description: 从收款单行表中查询
	 * @param AR_PAY_H_ID
	 * @return  设定文件 
	 * @return  List<ArPayHBean> 返回类型 
	 * @throws
	 */
	public List<ArPayLBean> selectArPayLFromPay(String AR_PAY_H_ID);
	/**
	 * @Title: selectArPayFromCancelH
	 * @Description: 从核销单主表中查询、
	 * @param AR_PAY_H_ID
	 * @return  设定文件 
	 * @return  List<ArPayHBean> 返回类型 
	 * @throws
	 */
	public List<ArCancelHBean> selectArCancelHFromCancel(String AR_PAY_H_ID);
	/**
	 * @Title: selectArPayFromCancelL
	 * @Description: 从核销单行表中查询
	 * @param AR_PAY_H_ID
	 * @return  设定文件 
	 * @return  List<ArPayHBean> 返回类型 
	 * @throws
	 */
	public List<ArCancelLBean> selectArCancelLFromCancel(String AR_PAY_H_ID);
	/**
	 * @Title: selectArPayById
	 * @Description: 根据id查询收款单单据类型
	 * @param AR_PAY_H_ID
	 * @return  设定文件 
	 * @return  ArPayHBean 返回类型 
	 * @throws
	 */
	public String selectArDocCatCodeByArPayHId(String AR_PAY_H_ID);
	/**
	 * @Title: calculateArPayAmt
	 * @Description: 计算收款单核销金额
	 * @param AR_PAY_H_ID  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void calculateArPayAmt(HashMap<String, Object> map);
} 
