package com.xzsoft.xc.st.dao;

import java.util.List;

import com.xzsoft.xc.st.modal.Dto;

/**
 * 
  * @ClassName XcStUseApplyDao
  * @Description 处理领用申请的dao接口
  * @author RenJianJian
  * @date 2016年12月7日 下午4:01:09
 */
public interface XcStUseApplyDao {
	/**
	 * 
	  * @Title selectXcStUseApplyH 方法名
	  * @Description 根据领用申请主键ID得到相应数据信息
	  * @param USE_APPLY_H_ID
	  * @return
	  * @throws Exception
	  * @return Dto 返回类型
	 */
	public Dto selectXcStUseApplyH(String USE_APPLY_H_ID) throws Exception;
	/**
	 * 
	  * @Title selectXcStUseApplyL 方法名
	  * @Description 根据领用申请行ID得到相应数据信息
	  * @param USE_APPLY_L_ID
	  * @return
	  * @throws Exception
	  * @return Dto 返回类型
	 */
	public Dto selectXcStUseApplyL(String USE_APPLY_L_ID) throws Exception;
	/**
	 * 
	  * @Title selectXcStUseApplyLByHId 方法名
	  * @Description 根据领用申请主键ID得到行表信息
	  * @param USE_APPLY_H_ID
	  * @return
	  * @throws Exception
	  * @return List<Dto> 返回类型
	 */
	public List<Dto> selectXcStUseApplyLByHId(String USE_APPLY_H_ID) throws Exception;
	/**
	 * 
	  * @Title insertXcStUseApplyH 方法名
	  * @Description 插入领用申请主表
	  * @param pDto
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void insertXcStUseApplyH(Dto pDto) throws Exception;
	/**
	 * 
	  * @Title updateXcStUseApplyH 方法名
	  * @Description 更新领用申请主表
	  * @param pDto
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void updateXcStUseApplyH(Dto pDto) throws Exception;
	/**
	 * 
	  * @Title deleteXcStUseApplyH 方法名
	  * @Description 批量删除领用申请主表
	  * @param stUseApplyHIds
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void deleteXcStUseApplyH(List<String> stUseApplyHIds) throws Exception;
	/**
	 * 
	  * @Title updateXcStUseApplyHCloseStatus 方法名
	  * @Description 批量关闭领用申请主表
	  * @param stUseApplyHIds
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void updateXcStUseApplyHCloseStatus(List<String> stUseApplyHIds) throws Exception;
}
