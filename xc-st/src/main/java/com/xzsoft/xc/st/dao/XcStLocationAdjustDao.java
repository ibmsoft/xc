package com.xzsoft.xc.st.dao;

import java.util.List;

import com.xzsoft.xc.st.modal.Dto;

/**
 * 
  * @ClassName XcStLocationAdjustDao
  * @Description 处理货位调整单的dao接口
  * @author RenJianJian
  * @date 2016年12月7日 下午4:52:16
 */
public interface XcStLocationAdjustDao {
	/**
	 * 
	  * @Title selectXcStLocationAdjustH 方法名
	  * @Description 根据货位调整单主键ID得到相应数据信息
	  * @param LO_ADJUST_H_ID
	  * @return
	  * @throws Exception
	  * @return Dto 返回类型
	 */
	public Dto selectXcStLocationAdjustH(String LO_ADJUST_H_ID) throws Exception;
	/**
	 * 
	  * @Title selectXcStLocationAdjustL 方法名
	  * @Description 根据货位调整单行ID得到相应数据信息
	  * @param LO_ADJUST_L_ID
	  * @return
	  * @throws Exception
	  * @return Dto 返回类型
	 */
	public Dto selectXcStLocationAdjustL(String LO_ADJUST_L_ID) throws Exception;
	/**
	 * 
	  * @Title selectXcStLocationAdjustLByHId 方法名
	  * @Description 根据货位调整单主键ID得到行表信息
	  * @param LO_ADJUST_H_ID
	  * @return
	  * @throws Exception
	  * @return List<Dto> 返回类型
	 */
	public List<Dto> selectXcStLocationAdjustLByHId(String LO_ADJUST_H_ID) throws Exception;
	/**
	 * 
	  * @Title insertXcStLocationAdjustH 方法名
	  * @Description 插入货位调整单主表
	  * @param pDto
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void insertXcStLocationAdjustH(Dto pDto) throws Exception;
	/**
	 * 
	  * @Title updateXcStLocationAdjustH 方法名
	  * @Description 更新货位调整单主表
	  * @param pDto
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void updateXcStLocationAdjustH(Dto pDto) throws Exception;
	/**
	 * 
	  * @Title deleteXcStLocationAdjustH 方法名
	  * @Description 批量删除货位调整单主表
	  * @param stLocationAdjustHIds
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void deleteXcStLocationAdjustH(List<String> stLocationAdjustHIds) throws Exception;
}
