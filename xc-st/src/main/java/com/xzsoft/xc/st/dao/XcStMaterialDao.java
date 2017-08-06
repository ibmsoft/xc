package com.xzsoft.xc.st.dao;

import java.util.List;

import com.xzsoft.xc.st.modal.XcStMaterialBean;

/**
 * 
  * @ClassName XcStMaterialDao
  * @Description 处理物资信息的dao接口
  * @author RenJianJian
  * @date 2016年11月30日 上午11:50:47
 */
public interface XcStMaterialDao {
	/**
	 * 
	  * @Title insertXcStMaterial 方法名
	  * @Description 增加操作
	  * @param stMaterial
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void insertXcStMaterial(XcStMaterialBean stMaterial) throws Exception;
	/**
	 * 
	  * @Title updateXcStMaterial 方法名
	  * @Description 修改操作
	  * @param stMaterial
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void updateXcStMaterial(XcStMaterialBean stMaterial) throws Exception;
	/**
	 * 
	  * @Title deleteXcStMaterial 方法名
	  * @Description 批量删除操作
	  * @param materialIds
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void deleteXcStMaterial(List<String> materialIds) throws Exception;
	/**
	 * 
	  * @Title deleteXcStMaterialParams 方法名
	  * @Description 批量删除账簿级物资信息
	  * @param materialParamsIds
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void deleteXcStMaterialParams(List<String> materialParamsIds) throws Exception;
	/**
	 * 
	  * @Title xcStMaterialWhetherCited 方法名
	  * @Description 判断当前操作的物资是否被引用
	  * @param MATERIAL_ID
	  * @return
	  * @throws Exception
	  * @return int 返回类型
	 */
	public int xcStMaterialWhetherCited(String MATERIAL_ID) throws Exception;
	/**
	 * 
	  * @Title xcStMaterialParamsWhetherCited 方法名
	  * @Description 删除账簿级物料信息时判断是否被引用，使用的不能删除
	  * @param MATERIAL_ID
	  * @param ORG_ID
	  * @return
	  * @throws Exception
	  * @return String 返回类型
	 */
	public String xcStMaterialParamsWhetherCited(String MATERIAL_ID,String ORG_ID) throws Exception;
	/**
	 * 
	  * @Title stMaterialCodeOrNameIsExists 方法名
	  * @Description 验证物料编码和名称是否在同一个物料分类集中相同
	  * @param MATERIAL_CODE
	  * @param MATERIAL_NAME
	  * @param MAT_TYPE_SET_ID
	  * @param MATERIAL_ID
	  * @return
	  * @throws Exception
	  * @return String 返回类型
	 */
	public String stMaterialCodeOrNameIsExists(String MATERIAL_CODE,String MATERIAL_NAME,String MAT_TYPE_SET_ID,String MATERIAL_ID) throws Exception;
}
