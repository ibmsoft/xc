package com.xzsoft.xc.st.mapper;

import com.xzsoft.xc.st.modal.Dto;

/**
 * @ClassName: XcStLocationInventoryMapper
 * @Description: 货位台账处理记录
 * @author panghd
 * @date 2017年1月16日 下午15:15:43
 *
 */
public interface XcStLocationInventoryMapper {
	/**
	 * @Title: xc_st_location_inventory_select_by_key
	 * @Description: 根据主键查询对应的货位台账处理记录
	 * @param pDto
	 * @return 单条货位台账处理记录
	 */
	public Dto xc_st_location_inventory_select_by_key(Dto pDto);

	/**
	 * @Title: xc_st_location_inventory_insert
	 * @Description: 保存货位台账处理记录
	 * @param bean
	 *            设定文件
	 */
	public void xc_st_location_inventory_insert(Dto pDto);

	/**
	 * @Title: xc_st_location_inventory_update
	 * @Description: 修改货位台账处理记录
	 * @param bean
	 *            设定文件
	 */
	public void xc_st_location_inventory_update(Dto pDto);

	/**
	 * @Title: xc_st_location_inventory_deleteByKey
	 * @Description: 根据库存存事务处理记录主键删除记录
	 * @param bean
	 *            设定文件
	 */
	public void xc_st_location_inventory_deleteByKey(Dto pDto);
	/**
	 * 
	  * @Title xc_st_location_inventory_select_by_WMSID 方法名
	  * @Description 根据仓库ID，物料ID、批次ID、货位ID查询货位汇总表是否存在
	  * @param pDto
	  * @return Dto 返回类型
	 */
	public Dto xc_st_location_inventory_select_by_WMSID(Dto pDto);
}