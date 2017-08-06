package com.xzsoft.xc.st.mapper;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.st.modal.XcStMaterialBean;

/**
 * 
  * @ClassName XcStMaterialMapper
  * @Description 处理物资信息的mapper接口
  * @author RenJianJian
  * @date 2016年11月30日 上午11:22:08
 */
public interface XcStMaterialMapper {
	/**
	 * 
	  * @Title insertXcStMaterial 方法名
	  * @Description 增加操作
	  * @param stMaterial
	  * @return void 返回类型
	 */
	public void insertXcStMaterial(XcStMaterialBean stMaterial);
	/**
	 * 
	  * @Title updateXcStMaterial 方法名
	  * @Description 修改操作
	  * @param stMaterial
	  * @return void 返回类型
	 */
	public void updateXcStMaterial(XcStMaterialBean stMaterial);
	/**
	 * 
	  * @Title deleteXcStMaterial 方法名
	  * @Description 批量删除操作
	  * @param materialIds
	  * @return void 返回类型
	 */
	public void deleteXcStMaterial(List<String> materialIds);
	/**
	 * 
	  * @Title deleteXcStMaterialParams 方法名
	  * @Description 批量删除账簿级物资信息
	  * @param materialParamsIds
	  * @return void 返回类型
	 */
	public void deleteXcStMaterialParams(List<String> materialParamsIds);
	/**
	 * 
	  * @Title xcStMaterialWhetherCited 方法名
	  * @Description 判断当前操作的物资是否被引用
	  * @param MATERIAL_ID
	  * @return
	  * @return int 返回类型
	 */
	public int xcStMaterialWhetherCited(String MATERIAL_ID);
	/**
	 * 
	  * @Title xcStMaterialParamsWhetherCited 方法名
	  * @Description 删除账簿级物料信息时判断是否被引用，使用的不能删除
	  * @param map
	  * @return
	  * @return String 返回类型
	 */
	public String xcStMaterialParamsWhetherCited(HashMap<String,String> map);
	/**
	 * 
	  * @Title stMaterialCodeOrNameIsExists 方法名
	  * @Description 验证物料编码和名称是否在同一个物料分类集中相同
	  * @param map
	  * @return
	  * @return String 返回类型
	 */
	public String stMaterialCodeOrNameIsExists(HashMap<String,String> map);
}
