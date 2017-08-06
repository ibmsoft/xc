package com.xzsoft.xc.st.mapper;

import java.util.List;

import com.xzsoft.xc.st.modal.Dto;

/**
 * 
  * @ClassName XcStCostChangeMapper
  * @Description 处理库存成本调整单的mapper接口
  * @author RenJianJian
  * @date 2016年12月7日 下午4:51:34
 */
public interface XcStCostChangeMapper {
	/**
	 * 
	  * @Title selectXcStCostChangeH 方法名
	  * @Description 根据库存成本调整单主键ID得到相应数据信息
	  * @param COST_CHANGE_H_ID
	  * @return
	  * @return Dto 返回类型
	 */
	public Dto selectXcStCostChangeH(String COST_CHANGE_H_ID);
	/**
	 * 
	  * @Title selectXcStCostChangeL 方法名
	  * @Description 根据库存成本调整单行ID得到相应数据信息
	  * @param COST_CHANGE_L_ID
	  * @return
	  * @return Dto 返回类型
	 */
	public Dto selectXcStCostChangeL(String COST_CHANGE_L_ID);
	/**
	 * 
	  * @Title selectXcStCostChangeLByHId 方法名
	  * @Description 根据库存成本调主键ID得到行表信息
	  * @param COST_CHANGE_H_ID
	  * @return
	  * @return List<Dto> 返回类型
	 */
	public List<Dto> selectXcStCostChangeLByHId(String COST_CHANGE_H_ID);
	/**
	 * 
	  * @Title insertXcStCostChangeH 方法名
	  * @Description 插入库存成本调整单主表
	  * @param pDto
	  * @return void 返回类型
	 */
	public void insertXcStCostChangeH(Dto pDto);
	/**
	 * 
	  * @Title updateXcStCostChangeH 方法名
	  * @Description 更新库存成本调整单主表
	  * @param pDto
	  * @return void 返回类型
	 */
	public void updateXcStCostChangeH(Dto pDto);
	/**
	 * 
	  * @Title insertXcStCostChangeL 方法名
	  * @Description 保存库存成本调整单明细
	  * @param pDto
	  * @return void 返回类型
	 */
	public void insertXcStCostChangeL(Dto pDto);
	/**
	 * 
	  * @Title updateXcStCostChangeL 方法名
	  * @Description 更新库存成本调整单明细
	  * @param pDto
	  * @return void 返回类型
	 */
	public void updateXcStCostChangeL(Dto pDto);
	/**
	 * 
	  * @Title deleteXcStCostChangeH 方法名
	  * @Description 批量删除库存成本调整单主表
	  * @param lists
	  * @return void 返回类型
	 */
	public void deleteXcStCostChangeH(List<String> lists);
	/**
	 * 
	  * @Title deleteXcStCostChangeLByHId 方法名
	  * @Description 根据库存成本调整单主表ID批量删除库存成本调整单行表信息
	  * @param lists
	  * @return void 返回类型
	 */
	public void deleteXcStCostChangeLByHId(List<String> lists);
	/**
	 * 
	  * @Title deleteXcStCostChangeLByLId 方法名
	  * @Description 根据库存成本调整单行表ID批量删除库存成本调整单行表信息
	  * @param pDto
	  * @return void 返回类型
	 */
	public void deleteXcStCostChangeLByLId(Dto pDto);
}
