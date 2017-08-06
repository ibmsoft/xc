package com.xzsoft.xc.posost.dao;

import java.util.List;

import com.xzsoft.xc.posost.modal.XcStTransBean;
/**
 * 
  * @ClassName XcStTransDao
  * @Description 处理库存交易明细模块的dao接口
  * @author RenJianJian
  * @date 2016年12月20日 下午12:33:37
 */
public interface XcStTransDao {
	/**
	 * 
	  * @Title insertXcStTrans 方法名
	  * @Description 批量新增交易明细
	  * @param list
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void insertXcStTrans(List<XcStTransBean> list) throws Exception;
	/**
	 * 
	  * @Title updateXcStTrans 方法名
	  * @Description 批量修改交易明细
	  * @param list
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void updateXcStTrans(List<XcStTransBean> list) throws Exception;
	/**
	 * 
	  * @Title deleteXcStTrans 方法名
	  * @Description 批量删除交易明细
	  * @param list
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void deleteXcStTrans(List<XcStTransBean> list) throws Exception;
}
