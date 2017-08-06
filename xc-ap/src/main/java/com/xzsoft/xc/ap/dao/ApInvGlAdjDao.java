package com.xzsoft.xc.ap.dao;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.ap.modal.ApInvGlAdj;
import com.xzsoft.xc.ap.modal.ApInvGlHBean;
import com.xzsoft.xc.apar.modal.ApInvTransBean;
/**
 * 
  * @ClassName：ApInvGlAdjDao
  * @Description：应付单Dao
  * @author：韦才文
  * @date：2016年7月22日 下午9:55:26
 */

public interface ApInvGlAdjDao {
	/**
	 * @Title:addApAdjInfo
	 * @Description:添加调整单信息
	 * 参数格式:
	 * @param  com.xzsoft.xc.ap.modal.ApInvGlAdj
	 * @return 
	 * @throws 
	 */
	public void addApAdjInfo(ApInvGlAdj apInvGlAdj);
	/**
	 * @Title:addInvTrans
	 * @Description:添加交易明细表信息
	 * 参数格式:
	 * @param  com.xzsoft.xc.apar.modal.ApInvTransBean
	 * @return 
	 * @throws 
	 */
	public void addInvTrans(ApInvTransBean apInvTransBean);
	/**
	 * @Title:isAddOrEdit
	 * @Description:判断新增或修改调整单
	 * 参数格式:
	 * @param  java.lang.String
	 * @return java.lang.String
	 * @throws 
	 */
	public String isAddOrEdit(String glAdjId);
	/**
	 * @Title:updateInvGlH
	 * @Description:更新应付单
	 * 参数格式:
	 * @param  java.util.HashMap
	 * @return 
	 * @throws 
	 */
	public void updateInvGlH(HashMap<String, Object> map);
	/**
	 * @Title:updateInvGlhadj
	 * @Description:修改调整单
	 * 参数格式:
	 * @param  com.xzsoft.xc.ap.modal.ApInvGlAdj
	 * @return 
	 * @throws 
	 */
	public void updateInvGlhadj(ApInvGlAdj apInvGLAdj);
	/**
	 * @TitleupdateInvtrans
	 * @Description:修改往来明细
	 * 参数格式:
	 * @param  com.xzsoft.xc.apar.modal.ApInvTransBean
	 * @return 
	 * @throws 
	 */
	public void updateInvtrans(ApInvTransBean apInvTransBean);
	/**
	 * @Title updateInvglh
	 * @Description: 更新应付单
	 * 参数格式:
	 * @param  com.xzsoft.xc.ap.modal.ApInvGlHBean
	 * @return 
	 * @throws 
	 */
	public void updateInvglh(ApInvGlHBean apInvGlHBean);
	/**
	 * @Title delApInvAdj
	 * @Description: 删除调整单
	 * 参数格式:
	 * @param  java.util.List
	 * @return 
	 * @throws 
	 */
	public void delApInvAdj(List<String> list);
	/**
	 * @Title delApInvTrans
	 * @Description: 删除交易明细
	 * 参数格式:
	 * @param  java.util.List
	 * @return 
	 * @throws 
	 */
	public void delApInvTrans(List<String> list);
	/**
	 * @Title updateInvByDel
	 * @Description: 更新应付单（删除时）
	 * 参数格式:
	 * @param  java.util.HashMap
	 * @return 
	 * @throws 
	 */
	public void updateInvByDel(HashMap<String, Object> map);
	/**
	 * @Title getInvAdjInfo
	 * @Description:查询调整单
	 * 参数格式:
	 * @param  java.lang.String
	 * @return com.xzsoft.xc.ap.modal.ApInvGlAdj
	 * @throws 
	 */
	public ApInvGlAdj getInvAdjInfo(String invAdjId);
	
}

