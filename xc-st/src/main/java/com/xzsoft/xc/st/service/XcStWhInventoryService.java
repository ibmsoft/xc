package com.xzsoft.xc.st.service;

import com.xzsoft.xc.st.modal.Dto;
/**
 * 
  * @ClassName XcStWhInventoryService
  * @Description 库存台账管理的公共service层
  * @author panghd
  * @date 2017年2月16日 下午3:43:35
 */
public interface XcStWhInventoryService {
	/**
	 * 
	  * @Title entry 方法名
	  * @Description 入库
	  * @param list
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void entry(Dto pDto) throws Exception;
	/**
	 * 
	  * @Title delevered 方法名
	  * @Description 出库
	  * @param list
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void delevered(Dto pDto) throws Exception;
	/**
	 * 
	  * @Title transshipment 方法名
	  * @Description 调拨
	  * @param list
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void transshipment(Dto pDto) throws Exception;
	/**
	 * 
	  * @Title costChange 方法名
	  * @Description 成本调整单
	  * @param pDto
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void costChange(Dto pDto) throws Exception;
}