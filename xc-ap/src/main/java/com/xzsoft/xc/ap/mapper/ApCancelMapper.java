package com.xzsoft.xc.ap.mapper;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.ap.modal.ApCancelHBean;
import com.xzsoft.xc.ap.modal.ApCancelLBean;

/**
 * @ClassName: ApCancelBaseMapper 
 * @Description: 应付核销单相关操作 
 * @author zhenghy
 * @date 2016年8月19日 下午2:45:18
 */
public interface ApCancelMapper {
	
	/**
	 * @Title: selectCancelH
	 * @Description: 查询核销单（根据id集合） 
	 * @param list  设定文件
	 * @return  void 返回类型 
	 * @throws
	 */
	public List<ApCancelHBean> selectCancelH(List<String> list);
	/**
	 * @Title: selectCancelL
	 * @Description: 查询核销单行信息（根据主id集合）
	 * @param list
	 * @return  设定文件 
	 * @return  List<ApCancelLBean> 返回类型 
	 * @throws
	 */
	public List<ApCancelLBean> selectCancelLByCancelHId(String ap_cancel_h_id);
	/**
	 * @Title: insertCancelH
	 * @Description: 新增核销单
	 * @param apCancelHBean  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void insertCancelH(ApCancelHBean apCancelHBean);
	/**
	 * @Title: updateCancelH
	 * @Description: 修改核销单
	 * @param apCancelHBean  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void updateCancelH(ApCancelHBean apCancelHBean);
	/**
	 * @Title: deleteCancelH
	 * @Description: 批量删除核销单
	 * @param list  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void deleteCancelHs(List<String> list);
	/**
	 * @Title: insertCancelL
	 * @Description: 批量新增核销单行
	 * @param map  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void insertCancelLs(HashMap<String, Object> map);
	/**
	 * @Title: updateCancelL
	 * @Description: 批量修改核销单行
	 * @param map  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void updateCancelLs(HashMap<String, Object> map);
	/**
	 * @Title: deleteCancelL
	 * @Description: 批量删除核销单行
	 * @param list  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void deleteCancelLs(List<String> list);
	/**
	 * @Title: deleteCancelLsByCancelHIds
	 * @Description: 批量删除核销单行（根据核销单id）
	 * @param cancelHIdList  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void deleteCancelLsByCancelHIds(List<String> list);
	//////////////////////////////////////////应付单相关操作////////////////////////////////////////////////////
	
	/**
	 * @Title: updateInvCancelAmt
	 * @Description: 修改应付单核销金额、余额
	 * @param map  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void updateInvCancelAmt(HashMap<String, Object> map);
	
	/////////////////////////////////////////////付款单相关操作//////////////////////////////////////////////////
	/**
	 * @Title: updatePayCancelAmt
	 * @Description: 修改付款单核销金额
	 * @param map  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void updatePayCancelAmt(HashMap<String, Object> map);
	
    /////////////////////////////////////////////应收单相关操作//////////////////////////////////////////////////
	
	/**
	 * @Title: updateArInvAmt
	 * @Description: 修改应收单核销金额、余额
	 * @param map  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void updateArInvCancelAmt(HashMap<String, Object> map);
    /////////////////////////////////////////////应付交易明细相关操作//////////////////////////////////////////////////
	/**
	 * @Title: insertApTrans
	 * @Description: 批量新增交易明细表
	 * @param map  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void insertApTrans(HashMap<String, Object> map);
	/**
	 * @Title: deleteApTrans
	 * @Description: 批量删除交易明细表
	 * @param list  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void deleteApTransByCancelLs(HashMap<String, Object> map);
	/**
	 * @Title: deleteApTransByCancelHIds
	 * @Description: 删除交易明细（根据核销单id）
	 * @param list  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void deleteApTransByCancelHs(HashMap<String, Object> map);
	
	/////////////////////////////////////////////应收交易明细相关操作//////////////////////////////////////////////////
	/**
	 * @Title: insertApTrans
	 * @Description: 批量新增交易明细表
	 * @param map  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void insertArTrans(HashMap<String, Object> map);
	/**
	 * @Title: deleteApTrans
	 * @Description: 批量删除交易明细表
	 * @param map  核销单行信息
	 * @return  void 返回类型 
	 * @throws
	 */
	public void deleteArTransByCancelLs(HashMap<String, Object> map);

	
	
}
