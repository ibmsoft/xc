package com.xzsoft.xc.st.mapper;

import java.util.List;

import com.xzsoft.xc.st.modal.Dto;

/**
 * 
  * @ClassName XcStUseApplyMapper
  * @Description 处理领用申请的mapper接口
  * @author RenJianJian
  * @date 2016年12月7日 下午3:56:35
 */
public interface XcStUseApplyMapper {
	/**
	 * 
	  * @Title selectXcStUseApplyH 方法名
	  * @Description 根据领用申请主键ID得到相应数据信息
	  * @param USE_APPLY_H_ID
	  * @return
	  * @return Dto 返回类型
	 */
	public Dto selectXcStUseApplyH(String USE_APPLY_H_ID);
	/**
	 * 
	  * @Title selectXcStUseApplyL 方法名
	  * @Description 根据领用申请行ID得到相应数据信息
	  * @param USE_APPLY_L_ID
	  * @return
	  * @return Dto 返回类型
	 */
	public Dto selectXcStUseApplyL(String USE_APPLY_L_ID);
	/**
	 * 
	  * @Title selectXcStUseApplyLByHId 方法名
	  * @Description 根据领用申请主键ID得到行表信息
	  * @param USE_APPLY_H_ID
	  * @return
	  * @return List<Dto> 返回类型
	 */
	public List<Dto> selectXcStUseApplyLByHId(String USE_APPLY_H_ID);
	/**
	 * 
	  * @Title insertXcStUseApplyH 方法名
	  * @Description 插入领用申请主表
	  * @param pDto
	  * @return void 返回类型
	 */
	public void insertXcStUseApplyH(Dto pDto);
	/**
	 * 
	  * @Title updateXcStUseApplyH 方法名
	  * @Description 更新领用申请主表
	  * @param pDto
	  * @return void 返回类型
	 */
	public void updateXcStUseApplyH(Dto pDto);
	/**
	 * 
	  * @Title insertXcStUseApplyL 方法名
	  * @Description 保存领用申请明细
	  * @param pDto
	  * @return void 返回类型
	 */
	public void insertXcStUseApplyL(Dto pDto);
	/**
	 * 
	  * @Title updateXcStUseApplyL 方法名
	  * @Description 更新领用申请明细
	  * @param pDto
	  * @return void 返回类型
	 */
	public void updateXcStUseApplyL(Dto pDto);
	/**
	 * 
	  * @Title deleteXcStUseApplyH 方法名
	  * @Description 批量删除领用申请主表
	  * @param map
	  * @return void 返回类型
	 */
	public void deleteXcStUseApplyH(List<String> lists);
	/**
	 * 
	  * @Title deleteXcStUseApplyLByHId 方法名
	  * @Description 根据领用申请主表ID批量删除领用申请行表信息
	  * @param lists
	  * @return void 返回类型
	 */
	public void deleteXcStUseApplyLByHId(List<String> lists);
	/**
	 * 
	  * @Title deleteXcStUseApplyLByLId 方法名
	  * @Description 根据领用申请行表ID批量删除领用申请行表信息
	  * @param pDto
	  * @return void 返回类型
	 */
	public void deleteXcStUseApplyLByLId(Dto pDto);
	/**
	 * 
	  * @Title updateXcStUseApplyHCloseStatus 方法名
	  * @Description 更新领用申请主表是否关闭状态
	  * @param lists
	  * @return void 返回类型
	 */
	public void updateXcStUseApplyHCloseStatus(List<String> lists);
}
