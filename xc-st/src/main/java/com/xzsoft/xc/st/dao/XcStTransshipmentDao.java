package com.xzsoft.xc.st.dao;

import java.util.List;

import com.xzsoft.xc.st.modal.Dto;

/**
 * 
  * @ClassName XcStTransshipmentDao
  * @Description 处理物资调拨单的dao接口
  * @author RenJianJian
  * @date 2016年12月7日 下午4:52:08
 */
public interface XcStTransshipmentDao {
	/**
	 * 
	  * @Title selectXcStTransshipmentH 方法名
	  * @Description 根据物资调拨单主键ID得到相应数据信息
	  * @param TR_H_ID
	  * @return
	  * @throws Exception
	  * @return Dto 返回类型
	 */
	public Dto selectXcStTransshipmentH(String TR_H_ID) throws Exception;
	/**
	 * 
	  * @Title selectXcStTransshipmentL 方法名
	  * @Description 根据物资调拨单行ID得到相应数据信息
	  * @param TR_L_ID
	  * @return
	  * @throws Exception
	  * @return Dto 返回类型
	 */
	public Dto selectXcStTransshipmentL(String TR_L_ID) throws Exception;
	/**
	 * 
	  * @Title selectXcStTransshipmentLByHId 方法名
	  * @Description 根据物资调拨单主键ID得到行表信息
	  * @param TR_H_ID
	  * @return
	  * @throws Exception
	  * @return List<Dto> 返回类型
	 */
	public List<Dto> selectXcStTransshipmentLByHId(String TR_H_ID) throws Exception;
	/**
	 * 
	  * @Title insertXcStTransshipmentH 方法名
	  * @Description 插入物资调拨单主表
	  * @param pDto
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void insertXcStTransshipmentH(Dto pDto) throws Exception;
	/**
	 * 
	  * @Title updateXcStTransshipmentH 方法名
	  * @Description 更新物资调拨单主表
	  * @param pDto
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void updateXcStTransshipmentH(Dto pDto) throws Exception;
	/**
	 * 
	  * @Title deleteXcStTransshipmentH 方法名
	  * @Description 批量删除物资调拨单主表
	  * @param stTransshipmentHIds
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void deleteXcStTransshipmentH(List<String> stTransshipmentHIds) throws Exception;
}
