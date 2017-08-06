package com.xzsoft.xc.st.mapper;

import java.util.List;

import com.xzsoft.xc.st.modal.Dto;

/**
 * 
  * @ClassName XcStWhDeleveredMapper
  * @Description 处理出库及退库主表的mapper接口
  * @author RenJianJian
  * @date 2016年12月2日 下午12:58:31
 */
public interface XcStWhDeleveredMapper {
	/**
	 * 
	  * @Title selectXcStWhDeleveredH 方法名
	  * @Description 根据出库及退库主键ID得到相应数据信息
	  * @param DELEVERED_H_ID
	  * @return
	  * @return Dto 返回类型
	 */
	public Dto selectXcStWhDeleveredH(String DELEVERED_H_ID);
	/**
	 * 
	  * @Title selectXcStWhDeleveredL 方法名
	  * @Description 根据出库及退库行ID得到相应数据信息
	  * @param DELEVERED_L_ID
	  * @return
	  * @return Dto 返回类型
	 */
	public Dto selectXcStWhDeleveredL(String DELEVERED_L_ID);
	/**
	 * 
	  * @Title selectXcStWhDeleveredLByHId 方法名
	  * @Description 根据出库及退库主键ID得到行表信息
	  * @param DELEVERED_H_ID
	  * @return
	  * @return List<Dto> 返回类型
	 */
	public List<Dto> selectXcStWhDeleveredLByHId(String DELEVERED_H_ID);
	/**
	 * 
	  * @Title insertXcStWhDeleveredH 方法名
	  * @Description 插入出库及退库主表
	  * @param pDto
	  * @return void 返回类型
	 */
	public void insertXcStWhDeleveredH(Dto pDto);
	/**
	 * 
	  * @Title updateXcStWhDeleveredH 方法名
	  * @Description 更新出库及退库主表
	  * @param pDto
	  * @return void 返回类型
	 */
	public void updateXcStWhDeleveredH(Dto pDto);
	/**
	 * 
	  * @Title insertXcStWhDeleveredL 方法名
	  * @Description 保存出库及退库明细
	  * @param pDto
	  * @return void 返回类型
	 */
	public void insertXcStWhDeleveredL(Dto pDto);
	/**
	 * 
	  * @Title updateXcStWhDeleveredL 方法名
	  * @Description 更新出库及退库明细
	  * @param pDto
	  * @return void 返回类型
	 */
	public void updateXcStWhDeleveredL(Dto pDto);
	/**
	 * 
	  * @Title deleteXcStWhDeleveredH 方法名
	  * @Description 批量删除出库及退库主表
	  * @param lists
	  * @return void 返回类型
	 */
	public void deleteXcStWhDeleveredH(List<String> lists);
	/**
	 * 
	  * @Title deleteXcStWhDeleveredLByHId 方法名
	  * @Description 根据出库及退库主表ID批量删除出库及退库行表信息
	  * @param lists
	  * @return void 返回类型
	 */
	public void deleteXcStWhDeleveredLByHId(List<String> lists);
	/**
	 * 
	  * @Title deleteXcStWhDeleveredLByLId 方法名
	  * @Description 根据出库及退库行表ID批量删除出库及退库行表信息
	  * @param pDto
	  * @return void 返回类型
	 */
	public void deleteXcStWhDeleveredLByLId(Dto pDto);
}
