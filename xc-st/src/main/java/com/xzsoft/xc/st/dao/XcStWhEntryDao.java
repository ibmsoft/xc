package com.xzsoft.xc.st.dao;

import java.util.List;

import com.xzsoft.xc.st.modal.Dto;

/**
 * 
  * @ClassName XcStWhEntryDao
  * @Description 处理入库及退货的dao接口
  * @author RenJianJian
  * @date 2016年12月2日 上午11:15:21
 */
public interface XcStWhEntryDao {
	/**
	 * 
	  * @Title selectXcStWhEntryH 方法名
	  * @Description 根据入库及退库主键ID得到相应数据信息
	  * @param ENTRY_H_ID
	  * @return
	  * @throws Exception
	  * @return Dto 返回类型
	 */
	public Dto selectXcStWhEntryH(String ENTRY_H_ID) throws Exception;
	/**
	 * 
	  * @Title selectXcStWhEntryL 方法名
	  * @Description 根据入库及退库行ID得到相应数据信息
	  * @param ENTRY_L_ID
	  * @return
	  * @throws Exception
	  * @return Dto 返回类型
	 */
	public Dto selectXcStWhEntryL(String ENTRY_L_ID) throws Exception;
	/**
	 * 
	  * @Title selectXcStWhEntryLByHId 方法名
	  * @Description 根据入库及退库主键ID得到行表信息
	  * @param ENTRY_H_ID
	  * @return
	  * @throws Exception
	  * @return List<Dto> 返回类型
	 */
	public List<Dto> selectXcStWhEntryLByHId(String ENTRY_H_ID) throws Exception;
	/**
	 * 
	  * @Title insertXcStWhEntryH 方法名
	  * @Description 插入入库及退货主表
	  * @param pDto
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void insertXcStWhEntryH(Dto pDto) throws Exception;
	/**
	 * 
	  * @Title updateXcStWhEntryH 方法名
	  * @Description 更新入库及退货主表
	  * @param pDto
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void updateXcStWhEntryH(Dto pDto) throws Exception;
	/**
	 * 
	  * @Title deleteXcStWhEntryH 方法名
	  * @Description 批量删除入库及退货主表
	  * @param stWhEntryHIds
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void deleteXcStWhEntryH(List<String> stWhEntryHIds) throws Exception;
	/**
	 * 
	  * @Title updateXcStWhEntryHStatusAndInit 方法名
	  * @Description 记账或者取消记账更新入库单状态和记账状态
	  * @param pDto
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void updateXcStWhEntryHStatusAndInit(Dto pDto) throws Exception;
	/**
	 * 
	  * @Title xcStWhEntryWhetherCited 方法名
	  * @Description 取消记账的时候判断入库单是否被引用，使用的不能取消记账
	  * @param ENTRY_H_ID
	  * @return
	  * @throws Exception
	  * @return String 返回类型
	 */
	public String xcStWhEntryWhetherCited(String ENTRY_H_ID) throws Exception;
}
