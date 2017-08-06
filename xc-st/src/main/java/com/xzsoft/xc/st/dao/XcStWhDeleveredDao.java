package com.xzsoft.xc.st.dao;

import java.util.List;

import com.xzsoft.xc.st.modal.Dto;

/**
 * 
  * @ClassName XcStWhDeleveredDao
  * @Description 处理出库及退库主表的dao接口
  * @author RenJianJian
  * @date 2016年12月2日 下午12:58:31
 */
public interface XcStWhDeleveredDao {
	/**
	 * 
	  * @Title selectXcStWhDeleveredH 方法名
	  * @Description 根据入库及退库主键ID得到相应数据信息
	  * @param DELEVERED_H_ID
	  * @throws Exception
	  * @return
	  * @return Dto 返回类型
	 */
	public Dto selectXcStWhDeleveredH(String DELEVERED_H_ID) throws Exception;
	/**
	 * 
	  * @Title selectXcStWhDeleveredL 方法名
	  * @Description 根据入库及退库行ID得到相应数据信息
	  * @param DELEVERED_L_ID
	  * @return
	  * @throws Exception
	  * @return Dto 返回类型
	 */
	public Dto selectXcStWhDeleveredL(String DELEVERED_L_ID) throws Exception;
	/**
	 * 
	  * @Title selectXcStWhDeleveredLByHId 方法名
	  * @Description 根据出库及退库主键ID得到行表信息
	  * @param DELEVERED_H_ID
	  * @return
	  * @throws Exception
	  * @return List<Dto> 返回类型
	 */
	public List<Dto> selectXcStWhDeleveredLByHId(String DELEVERED_H_ID) throws Exception;
	/**
	 * 
	  * @Title insertXcStWhDeleveredH 方法名
	  * @Description 插入出库及退库主表
	  * @param pDto
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void insertXcStWhDeleveredH(Dto pDto) throws Exception;
	/**
	 * 
	  * @Title updateXcStWhDeleveredH 方法名
	  * @Description 更新出库及退库主表
	  * @param pDto
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void updateXcStWhDeleveredH(Dto pDto) throws Exception;
	
	/**
	 * 
	  * @Title deleteXcStWhDeleveredH 方法名
	  * @Description 批量删除出库及退库主表
	  * @param stWhDeleveredHIds
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void deleteXcStWhDeleveredH(List<String> stWhDeleveredHIds) throws Exception;
}
