package com.xzsoft.xc.st.mapper;

import java.util.List;

import com.xzsoft.xc.st.modal.Dto;

/**
 * 
  * @ClassName XcStLocationAdjustMapper
  * @Description 处理货位调整单的mapper接口
  * @author RenJianJian
  * @date 2016年12月7日 下午4:51:12
 */
public interface XcStLocationAdjustMapper {
	/**
	 * 
	  * @Title selectXcStLocationAdjustH 方法名
	  * @Description 根据货位调整单主键ID得到相应数据信息
	  * @param LO_ADJUST_H_ID
	  * @return
	  * @return Dto 返回类型
	 */
	public Dto selectXcStLocationAdjustH(String LO_ADJUST_H_ID);
	/**
	 * 
	  * @Title selectXcStLocationAdjustL 方法名
	  * @Description 根据货位调整单行ID得到相应数据信息
	  * @param LO_ADJUST_L_ID
	  * @return
	  * @return Dto 返回类型
	 */
	public Dto selectXcStLocationAdjustL(String LO_ADJUST_L_ID);
	/**
	 * 
	  * @Title selectXcStLocationAdjustLByHId 方法名
	  * @Description 根据货位调整单主键ID得到行表信息
	  * @param LO_ADJUST_H_ID
	  * @return
	  * @return List<Dto> 返回类型
	 */
	public List<Dto> selectXcStLocationAdjustLByHId(String LO_ADJUST_H_ID);
	/**
	 * 
	  * @Title insertXcStLocationAdjustH 方法名
	  * @Description 插入货位调整单主表
	  * @param pDto
	  * @return void 返回类型
	 */
	public void insertXcStLocationAdjustH(Dto pDto);
	/**
	 * 
	  * @Title updateXcStLocationAdjustH 方法名
	  * @Description 更新货位调整单主表
	  * @param pDto
	  * @return void 返回类型
	 */
	public void updateXcStLocationAdjustH(Dto pDto);
	/**
	 * 
	  * @Title insertXcStLocationAdjustL 方法名
	  * @Description 保存货位调整单明细
	  * @param pDto
	  * @return void 返回类型
	 */
	public void insertXcStLocationAdjustL(Dto pDto);
	/**
	 * 
	  * @Title updateXcStLocationAdjustL 方法名
	  * @Description 更新货位调整单明细
	  * @param pDto
	  * @return void 返回类型
	 */
	public void updateXcStLocationAdjustL(Dto pDto);
	/**
	 * 
	  * @Title deleteXcStLocationAdjustH 方法名
	  * @Description 批量删除货位调整单主表
	  * @param lists
	  * @return void 返回类型
	 */
	public void deleteXcStLocationAdjustH(List<String> lists);
	/**
	 * 
	  * @Title deleteXcStLocationAdjustLByHId 方法名
	  * @Description 根据货位调整单主表ID批量删除货位调整单行表信息
	  * @param lists
	  * @return void 返回类型
	 */
	public void deleteXcStLocationAdjustLByHId(List<String> lists);
	/**
	 * 
	  * @Title deleteXcStLocationAdjustLByLId 方法名
	  * @Description 根据货位调整单行表ID批量删除货位调整单行表信息
	  * @param pDto
	  * @return void 返回类型
	 */
	public void deleteXcStLocationAdjustLByLId(Dto pDto);
}
