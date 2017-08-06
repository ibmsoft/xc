package com.xzsoft.xc.st.mapper;

import java.util.List;

import com.xzsoft.xc.st.modal.Dto;

/**
 * 
  * @ClassName XcStWhEntryMapper
  * @Description 处理入库及退货的mapper接口
  * @author RenJianJian
  * @date 2016年12月2日 上午11:05:21
 */
public interface XcStWhEntryMapper {
	/**
	 * 
	  * @Title selectXcStWhEntryH 方法名
	  * @Description 根据入库及退库主键ID得到相应数据信息
	  * @param ENTRY_H_ID
	  * @return
	  * @return Dto 返回类型
	 */
	public Dto selectXcStWhEntryH(String ENTRY_H_ID);
	/**
	 * 
	  * @Title selectXcStWhEntryL 方法名
	  * @Description 根据入库及退库行ID得到相应数据信息
	  * @param ENTRY_L_ID
	  * @return
	  * @return Dto 返回类型
	 */
	public Dto selectXcStWhEntryL(String ENTRY_L_ID);
	/**
	 * 
	  * @Title selectXcStWhEntryLByHId 方法名
	  * @Description 根据入库及退库主键ID得到行表信息
	  * @param ENTRY_H_ID
	  * @return
	  * @return List<Dto> 返回类型
	 */
	public List<Dto> selectXcStWhEntryLByHId(String ENTRY_H_ID);
	/**
	 * 
	  * @Title insertXcStWhEntryH 方法名
	  * @Description 插入入库及退货主表
	  * @param pDto
	  * @return void 返回类型
	 */
	public void insertXcStWhEntryH(Dto pDto);
	/**
	 * 
	  * @Title updateXcStWhEntryH 方法名
	  * @Description 更新入库及退货主表
	  * @param pDto
	  * @return void 返回类型
	 */
	public void updateXcStWhEntryH(Dto pDto);
	/**
	 * 
	  * @Title insertXcStWhEntryL 方法名
	  * @Description 保存入库及退货明细
	  * @param pDto
	  * @return void 返回类型
	 */
	public void insertXcStWhEntryL(Dto pDto);
	/**
	 * 
	  * @Title updateXcStWhEntryL 方法名
	  * @Description 更新入库及退货明细
	  * @param pDto
	  * @return void 返回类型
	 */
	public void updateXcStWhEntryL(Dto pDto);
	/**
	 * 
	  * @Title deleteXcStWhEntryH 方法名
	  * @Description 批量删除入库及退货主表
	  * @param lists
	  * @return void 返回类型
	 */
	public void deleteXcStWhEntryH(List<String> lists);
	/**
	 * 
	  * @Title deleteXcStWhEntryLByHId 方法名
	  * @Description 根据入库及退货主表ID批量删除入库及退货行表信息
	  * @param lists
	  * @return void 返回类型
	 */
	public void deleteXcStWhEntryLByHId(List<String> lists);
	/**
	 * 
	  * @Title deleteXcStWhEntryLByLId 方法名
	  * @Description 根据入库及退货行表ID批量删除入库及退货行表信息
	  * @param pDto
	  * @return void 返回类型
	 */
	public void deleteXcStWhEntryLByLId(Dto pDto);
	/**
	 * 
	  * @Title updateXcStWhEntryHStatusAndInit 方法名
	  * @Description 记账或者取消记账更新入库单状态和记账状态
	  * @param pDto
	  * @return void 返回类型
	 */
	public void updateXcStWhEntryHStatusAndInit(Dto pDto);
	/**
	 * 
	  * @Title xcStWhEntryWhetherCited 方法名
	  * @Description 取消记账的时候判断入库单是否被引用，使用的不能取消记账
	  * @param ENTRY_H_ID
	  * @return
	  * @return String 返回类型
	 */
	public String xcStWhEntryWhetherCited(String ENTRY_H_ID);
}
