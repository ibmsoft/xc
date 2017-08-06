package com.xzsoft.xc.st.mapper;

import java.util.List;

import com.xzsoft.xc.st.modal.Dto;

/**
 * 
  * @ClassName XcStTransshipmentMapper
  * @Description 处理物资调拨单的mapper接口
  * @author RenJianJian
  * @date 2016年12月7日 下午4:51:03
 */
public interface XcStTransshipmentMapper {
	/**
	 * 
	  * @Title selectXcStTransshipmentH 方法名
	  * @Description 根据物资调拨单主键ID得到相应数据信息
	  * @param TR_H_ID
	  * @return
	  * @return Dto 返回类型
	 */
	public Dto selectXcStTransshipmentH(String TR_H_ID);
	/**
	 * 
	  * @Title selectXcStTransshipmentL 方法名
	  * @Description 根据物资调拨单行ID得到相应数据信息
	  * @param TR_L_ID
	  * @return
	  * @return Dto 返回类型
	 */
	public Dto selectXcStTransshipmentL(String TR_L_ID);
	/**
	 * 
	  * @Title selectXcStTransshipmentLByHId 方法名
	  * @Description 根据物资调拨单主键ID得到行表信息
	  * @param TR_H_ID
	  * @return
	  * @return List<Dto> 返回类型
	 */
	public List<Dto> selectXcStTransshipmentLByHId(String TR_H_ID);
	/**
	 * 
	  * @Title insertXcStTransshipmentH 方法名
	  * @Description 插入物资调拨单主表
	  * @param pDto
	  * @return void 返回类型
	 */
	public void insertXcStTransshipmentH(Dto pDto);
	/**
	 * 
	  * @Title updateXcStTransshipmentH 方法名
	  * @Description 更新物资调拨单主表
	  * @param pDto
	  * @return void 返回类型
	 */
	public void updateXcStTransshipmentH(Dto pDto);
	/**
	 * 
	  * @Title insertXcStTransshipmentL 方法名
	  * @Description 保存物资调拨单明细
	  * @param pDto
	  * @return void 返回类型
	 */
	public void insertXcStTransshipmentL(Dto pDto);
	/**
	 * 
	  * @Title updateXcStTransshipmentL 方法名
	  * @Description 更新物资调拨单明细
	  * @param pDto
	  * @return void 返回类型
	 */
	public void updateXcStTransshipmentL(Dto pDto);
	/**
	 * 
	  * @Title deleteXcStTransshipmentH 方法名
	  * @Description 批量删除物资调拨单主表
	  * @param lists
	  * @return void 返回类型
	 */
	public void deleteXcStTransshipmentH(List<String> lists);
	/**
	 * 
	  * @Title deleteXcStTransshipmentLByHId 方法名
	  * @Description 根据物资调拨单主表ID批量删除物资调拨单行表信息
	  * @param lists
	  * @return void 返回类型
	 */
	public void deleteXcStTransshipmentLByHId(List<String> lists);
	/**
	 * 
	  * @Title deleteXcStTransshipmentLByLId 方法名
	  * @Description 根据物资调拨单行表ID批量删除物资调拨单行表信息
	  * @param pDto
	  * @return void 返回类型
	 */
	public void deleteXcStTransshipmentLByLId(Dto pDto);
}
