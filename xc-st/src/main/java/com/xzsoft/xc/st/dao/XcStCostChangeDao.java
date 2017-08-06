package com.xzsoft.xc.st.dao;

import java.util.List;

import com.xzsoft.xc.st.modal.Dto;

/**
 * 
  * @ClassName XcStCostChangeDao
  * @Description 处理库存成本调整单的dao接口
  * @author RenJianJian
  * @date 2016年12月7日 下午4:52:23
 */
public interface XcStCostChangeDao {
	/**
	 * 
	  * @Title selectXcStCostChangeH 方法名
	  * @Description 根据库存成本调整单主键ID得到相应数据信息
	  * @param COST_CHANGE_H_ID
	  * @return
	  * @throws Exception
	  * @return Dto 返回类型
	 */
	public Dto selectXcStCostChangeH(String COST_CHANGE_H_ID) throws Exception;
	/**
	 * 
	  * @Title selectXcStCostChangeL 方法名
	  * @Description 根据库存成本调整单行ID得到相应数据信息
	  * @param COST_CHANGE_L_ID
	  * @return
	  * @throws Exception
	  * @return Dto 返回类型
	 */
	public Dto selectXcStCostChangeL(String COST_CHANGE_L_ID) throws Exception;
	/**
	 * 
	  * @Title selectXcStCostChangeLByHId 方法名
	  * @Description 根据库存成本调主键ID得到行表信息
	  * @param COST_CHANGE_H_ID
	  * @return
	  * @throws Exception
	  * @return List<Dto> 返回类型
	 */
	public List<Dto> selectXcStCostChangeLByHId(String COST_CHANGE_H_ID) throws Exception;
	/**
	 * 
	  * @Title insertXcStCostChangeH 方法名
	  * @Description 插入库存成本调整单主表
	  * @param pDto
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void insertXcStCostChangeH(Dto pDto) throws Exception;
	/**
	 * 
	  * @Title updateXcStCostChangeH 方法名
	  * @Description 更新库存成本调整单主表
	  * @param pDto
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void updateXcStCostChangeH(Dto pDto) throws Exception;
	/**
	 * 
	  * @Title deleteXcStCostChangeH 方法名
	  * @Description 批量删除库存成本调整单主表
	  * @param stCostChangeHIds
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void deleteXcStCostChangeH(List<String> stCostChangeHIds) throws Exception;
}
