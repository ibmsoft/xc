package com.xzsoft.xc.ap.dao;

import java.util.List;

import com.xzsoft.xc.ap.modal.ApCancelHBean;
import com.xzsoft.xc.ap.modal.ApCancelLBean;
import com.xzsoft.xc.apar.modal.ApInvTransBean;
import com.xzsoft.xc.apar.modal.ArInvTransBean;

/**
 * @ClassName: ApCancelDao 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author zhenghy
 * @date 2016年8月19日 下午3:25:15
 */
public interface ApCancelDao {

	/**
	 * @Title: selectCancelH
	 * @Description: 查询核销单信息
	 * @param list
	 * @throws Exception  设定文件 
	 * @return  List<ApCancelHBean> 返回类型 
	 * @throws
	 */
	public List<ApCancelHBean> selectCancelH(List<String> cancelHIdList) throws Exception;
	/**
	 * @Title: selectCancelL
	 * @Description: 查询核销单行信息
	 * @param list
	 * @return
	 * @throws Exception  设定文件 
	 * @return  List<ApCancelLBean> 返回类型 
	 * @throws
	 */
	public List<ApCancelLBean> selectCancelLByCancelHId(String ap_cancel_h_id) throws Exception;
	/**
	 * @Title: insertCancelH
	 * @Description: 新增核销单
	 * @param apCancelHBean  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void insertCancelH(ApCancelHBean apCancelHBean) throws Exception;
	/**
	 * @Title: updateCancelH
	 * @Description: 修改核销单
	 * @param apCancelHBean  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void updateCancelH(ApCancelHBean apCancelHBean) throws Exception;
	/**
	 * @Title: deleteCancelH
	 * @Description: 批量删除核销单
	 * @param list  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void deleteCancelHs(List<String> list) throws Exception;
	/**
	 * @Title: insertCancelL
	 * @Description: 批量新增核销单行
	 * @param map  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void insertCancelLs(List<ApCancelLBean> list) throws Exception;
	/**
	 * @Title: updateCancelL
	 * @Description: 批量修改核销单行
	 * @param map  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void updateCancelLs(List<ApCancelLBean> list) throws Exception;
	/**
	 * @Title: deleteCancelL
	 * @Description: 批量删除核销单行
	 * @param list  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void deleteCancelLs(List<ApCancelLBean> list) throws Exception;
	/**
	 * @Title: deleteCancelLsByCancelHIds
	 * @Description: 批量删除核销单行（根据核销单id）
	 * @param cancelHIdList
	 * @throws Exception  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void deleteCancelLsByCancelHIds(List<String> cancelHIdList) throws Exception;
	////////////////////////////////////////////应付单相关操作//////////////////////////////////////////////////////////

	/**
	 * @Title: updateInvHCancelAmt
	 * @Description: 修改行上面应付单的金额 
	 * @param opTyp
	 * @param apCancelLBeanList
	 * @throws Exception  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void updateInvHCancelAmt(String opType,List<ApCancelLBean> apCancelLBeanList)throws Exception;
	/**
	 * @Title: updateInvHCancelAmt
	 * @Description: 修改核销单应付单的金额
	 * @param opType 操作类型
	 * @param apCancelHBean 核销单主
	 * @param apCancelLBeanList 行信息
	 * @param hl H L 
	 * @throws Exception  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void updateInvHCancelAmt(String opType,ApCancelHBean apCancelHBean,List<ApCancelLBean> apCancelLBeanList,String hl)throws Exception;
	////////////////////////////////////////////付款单相关操作//////////////////////////////////////////////////////////
	/**
	 * @Title: updatePayCancelAmt
	 * @Description: 修改主上面的付款单的金额
	 * @param opType
	 * @param ap_pay_h_id
	 * @param apCancelLBeanList
	 * @throws Exception  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void updatePayCancelAmt(String opType,ApCancelHBean apCancelHBean,List<ApCancelLBean> apCancelLBeanList) throws Exception;
	/**
	 * @Title: updatePayCancelAmt
	 * @Description: 修改行上面的付款单的金额
	 * @param opType
	 * @param apCancelLBeanList
	 * @throws Exception  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void updatePayCancelAmt(String opType,List<ApCancelLBean> apCancelLBeanList) throws Exception;
	////////////////////////////////////////////应收单相关操作//////////////////////////////////////////////////////////
	/**
	 * @Title: updateArInvCancelAmt
	 * @Description: 批量修改应收单核销金额、余额
	 * @param list
	 * @throws Exception  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void updateArInvCancelAmt(String opType,List<ApCancelLBean> apCancelLBeanList) throws Exception;
	////////////////////////////////////////////应付交易明细相关操作//////////////////////////////////////////////////////////	
	/**
	 * @Title: insertApTrans
	 * @Description: 批量新增应付中的交易明细
	 * @param apInvTransBeanList 交易明细集合
	 * @throws Exception  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void insertApTrans(List<ApInvTransBean> apInvTransBeanList) throws Exception;
	/**
	 * @Title: deleteApTransByCancelLs
	 * @Description: 批量删除应付中的交易明细
	 * @param apCancelLBeanList 核销单行信息
	 * @throws Exception  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void deleteApTransByCancelLs(List<ApCancelLBean> apCancelLBeanList) throws Exception;
	/**
	 * @Title: deleteApTransByCancelHs
	 * @Description: 批量删除应付中的交易明细 
	 * @param apCancelHBeanList 核销单主信息
	 * @throws Exception  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void deleteApTransByCancelHs(List<ApCancelHBean> apCancelHBeanList) throws Exception;
	
	////////////////////////////////////////////应收交易明细相关操作//////////////////////////////////////////////////////////
	/**
	 * @Title: insertArTrans
	 * @Description: 批量新增应收中的交易明细
	 * @param apInvTransBeanList 交易明细集合
	 * @throws Exception  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void insertArTrans(List<ArInvTransBean> arInvTransBeanList) throws Exception;
	/**
	 * @Title: deleteArTransByCancelLs
	 * @Description: 批量删除应收中的交易明细
	 * @param apCancelLBeanList 核销单行信息
	 * @throws Exception  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void deleteArTransByCancelLs(List<ApCancelLBean> apCancelLBeanList) throws Exception;
	
}
