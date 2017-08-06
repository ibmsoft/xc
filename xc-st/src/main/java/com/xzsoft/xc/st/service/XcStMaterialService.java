package com.xzsoft.xc.st.service;

import org.codehaus.jettison.json.JSONObject;

/**
 * 
  * @ClassName XcStMaterialService
  * @Description 物资信息业务处理层
  * @author RenJianJian
  * @date 2016年11月30日 下午12:05:19
 */
public interface XcStMaterialService {
	/**
	 * 
	  * @Title saveOrUpdateStMaterial 方法名
	  * @Description 对物资进行增加或者修改操作
	  * @param stMaterialInfo
	  * @param opType
	  * @return
	  * @throws Exception
	  * @return JSONObject 返回类型
	 */
	public JSONObject saveOrUpdateStMaterial(String stMaterialInfo,String opType) throws Exception;
	/**
	 * 
	  * @Title deleteXcStMaterial 方法名
	  * @Description 对物资进行删除操作
	  * @param materialIds
	  * @return
	  * @return JSONObject 返回类型
	 */
	public JSONObject deleteXcStMaterial(String materialIds) throws Exception;
	/**
	 * 
	  * @Title deleteXcStMaterialParams 方法名
	  * @Description 删除账簿级物料信息
	  * @param materialParamsIds
	  * @return
	  * @throws Exception
	  * @return JSONObject 返回类型
	 */
	public JSONObject deleteXcStMaterialParams(String materialParamsIds) throws Exception;
}
