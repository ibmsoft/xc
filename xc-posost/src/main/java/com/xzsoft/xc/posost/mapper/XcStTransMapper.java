package com.xzsoft.xc.posost.mapper;

import java.util.HashMap;

/**
 * 
  * @ClassName XcStTransMapper
  * @Description 处理库存交易明细模块的mapper接口
  * @author RenJianJian
  * @date 2016年12月20日 下午12:24:39
 */
public interface XcStTransMapper {
	/**
	 * 
	  * @Title insertXcStTrans 方法名
	  * @Description 批量新增库存交易明细
	  * @param map
	  * @return void 返回类型
	 */
	public void insertXcStTrans(HashMap<String, Object> map);
	/**
	 * 
	  * @Title updateXcStTrans 方法名
	  * @Description 批量修改库存交易明细
	  * @param map
	  * @return void 返回类型
	 */
	public void updateXcStTrans(HashMap<String, Object> map);
	/**
	 * 
	  * @Title deleteXcStTrans 方法名
	  * @Description 批量删除库存交易明细
	  * @param map
	  * @return void 返回类型
	 */
	public void deleteXcStTrans(HashMap<String, Object> map);
}