package com.xzsoft.xc.ap.dao;
import java.util.HashMap;




import java.util.List;

/**
 * 
  * @ClassName: ApPayReqCommonDao
  * @Description: 操作付款申请单Dao
  * @author 韦才文
  * @date 2016年6月14日 上午16:15:15
 */
import com.xzsoft.xc.ap.modal.ApPayReqHBean;
import com.xzsoft.xc.ap.modal.ApPayReqLBean;
import com.xzsoft.xc.apar.modal.ApInvTransBean;

public interface ApPayReqCommonDao {
	/**
	 * @Title getPayReqHById
	 * @Description: 通过ID查询付款申请单主表信息 
	 * 参数格式:
	 * @param java.lang.String
	 * @return com.xzsoft.xc.ap.modal.ApPayReqHBean
	 * @throws 
	 */
	public ApPayReqHBean getPayReqHById(String payReqHId) ;
	/**
	 * @Title addPayReqH
	 * @Description: 新增付款申请单主表信息
	 * 参数格式:
	 * @param com.xzsoft.xc.ap.modal.ApPayReqHBean
	 * @return 
	 * @throws 
	 */
	public void addPayReqH(ApPayReqHBean payH);
	/**
	 * @Title editPayReqH
	 * @Description: 修改付款申请单主表信息
	 * 参数格式:
	 * @param com.xzsoft.xc.ap.modal.ApPayReqHBean
	 * @return 
	 * @throws 
	 */
	public void editPayReqH(ApPayReqHBean payH);
	/**
	 * @Title delPayReqH
	 * @Description: 删除付款申请单主表信息
	 * 参数格式:
	 * @param java.lang.String
	 * @return 
	 * @throws 
	 */
	public void delPayReqH(String payReqHId);
	/**
	 * @Title getPayReqL
	 * @Description: 查询付款申请单行表信息 
	 * 参数格式:
	 * @param java.lang.String
	 * @return com.xzsoft.xc.ap.modal.ApPayReqLBean
	 * @throws 
	 */
	public ApPayReqLBean getPayReqL(String payReqLId);
	/**
	 * @Title getPayReqLByHId
	 * @Description:  通过付款申请单主表ID查询付款申请单行表信息 
	 * 参数格式:
	 * @param java.lang.String
	 * @return java.util.List
	 * @throws 
	 */
	public List<HashMap<String, Object>> getPayReqLByHId(String payReqHId);
	/**
	 * @Title addPayReqL
	 * @Description:  新增付款申请单行表信息 
	 * 参数格式:
	 * @param com.xzsoft.xc.ap.modal.ApPayReqLBean
	 * @return 
	 * @throws 
	 */
	public void addPayReqL(ApPayReqLBean payReqLBean);
	/**
	 * @Title editPayReqL
	 * @Description:  修改付款申请单行表信息 
	 * 参数格式:
	 * @param com.xzsoft.xc.ap.modal.ApPayReqLBean
	 * @return 
	 * @throws 
	 */
	public void editPayReqL(ApPayReqLBean payReqLBean);
	/**
	 * @Title delPayReqL
	 * @Description: 删除付款申请单行表信息
	 * 参数格式:
	 * @param java.lang.String
	 * @return 
	 * @throws 
	 */
	public void delPayReqL(String payReqLId);
	/**
	 * @Title addInvTrans
	 * @Description:添加交易明细表信息 
	 * 参数格式:
	 * @param  com.xzsoft.xc.apar.modal.ApInvTransBean
	 * @return 
	 * @throws 
	 */
	public void addInvTrans(ApInvTransBean apInvTransBean);
	/**
	 * @Title editTrans
	 * @Description:修改交易明细表信息
	 * 参数格式:
	 * @param  java.util.HashMap
	 * @return 
	 * @throws 
	 */
	public void editTrans(HashMap<String, Object> map);
	/**
	 * @Title delInvTrans
	 * @Description:删除交易明细表信息
	 * 参数格式:
	 * @param  java.util.HashMap
	 * @return 
	 * @throws 
	 */
	public void delInvTrans(HashMap<String, String> map);
	/**
	 * @Title judgeAmount
	 * @Description:判断付款申请金额是否大于未付金额
	 * 参数格式:
	 * @param  java.util.HashMap
	 * @return java.lang.String
	 * @throws 
	 */

	public String judgeAmount(HashMap<String, Object> map);
	/**
	 * @Title: getLedgerCatInfo 
	 * @Description: 获取账簿级费用单据类型信息
	 * @param ledgerId
	 * @param docCat
	 * @return
	 * @throws Exception    设定文件
	 */
	public HashMap<String,String> getLedgerCatInfo(String ledgerId,String docCat) throws Exception ;
	
	/**
	 * @Title: getGlobalCatInfo 
	 * @Description: 获取全局费用单据类型信息
	 * @param docCat
	 * @return
	 * @throws Exception    设定文件
	 */
	public HashMap<String,String> getGlobalCatInfo(String docCat) throws Exception ;
	/**
	 * @Title updatePayReqByClose
	 * @Description:更新申请单（单据关闭时）
	 * 参数格式:
	 * @param  java.util.HashMap
	 * @return 
	 * @throws 
	 */
	public void updatePayReqByClose(HashMap<String, Object> map);
	/**
	 * @Title updateReqHAmout
	 * @Description:更新付款主表金额
	 * 参数格式:
	 * @param  java.util.List
	 * @return 
	 * @throws 
	 */
	public void updateReqHAmout(List<ApPayReqHBean> reqHBeanList);
	/**
	 * @Title updateReqLAmount
	 * @Description:更新付款行表金额
	 * 参数格式:
	 * @param  java.util.List
	 * @return 
	 * @throws 
	 */
	public void updateReqLAmount(List<ApPayReqLBean> reqLBeanList);
}
