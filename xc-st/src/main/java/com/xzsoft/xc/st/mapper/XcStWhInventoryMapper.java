package com.xzsoft.xc.st.mapper;

import java.util.List;

import com.xzsoft.xc.st.modal.Dto;


/**
 * @ClassName: XcStWhInventoryMapper 
 * @Description: 库存台账处理记录
 * @author panghd
 * @date 2017年1月16日 下午15:15:43 
 *
 */
public interface XcStWhInventoryMapper {
	
	/**
	 * @Title: xc_st_wh_inventory_select_by_key 
	 * @Description: 根据主键查询对应的库存台账处理记录
	 * @param pDto
	 * @return    单条库存台账处理记录
	 */
	public Dto xc_st_wh_inventory_select_by_key(Dto pDto) ;
	
	/**
	 * @Title: xc_st_wh_inventory_select_by_mat 
	 * @Description: 根据物资ID和仓库查询对应的库存台账
	 * @param pDto
	 * @return    单条库存台账处理记录
	 */
	public Dto xc_st_wh_inventory_select_by_mat(Dto pDto) ;
	
	/**
	 * @Title: xc_st_wh_inventory_select_by_entry_id 
	 * @Description: 根据物资ID和仓库入库单和出库单ID查询对应的库存台账
	 * @param pDto
	 * @return    单条库存台账处理记录
	 */
	public Dto xc_st_wh_inventory_select_by_entry_id(Dto pDto) ;
	
	
	/**
	 * @Title: xc_st_wh_entry_h_select_by_key 
	 * @Description: 根据入库单ID查询入库单
	 * @param pDto
	 * @return    单条库存台账处理记录
	 */
	public Dto xc_st_wh_entry_h_select_by_key(Dto pDto) ;
	
	/**
	 * @Title: xc_st_wh_entry_l_select_by_key 
	 * @Description: 根据入库单ID查询入库单行信息
	 * @param List
	 * @return    多条入库单行信息
	 */
	@SuppressWarnings("rawtypes")
	public List xc_st_wh_entry_l_select_by_key(Dto pDto) ;
	
	
	/**
	 * @Title: xc_st_wh_b_ser_select_by_key 
	 * @Description: 根据入库单ID查询入库单序列号和批次信息
	 * @param List
	 * @return    多条入库单序列号和批次信息
	 */
	@SuppressWarnings("rawtypes")
	public List xc_st_wh_b_ser_select_by_key(Dto pDto) ;
	/**
	 * 
	  * @Title xc_st_wh_b_location_by_key 方法名
	  * @Description 查询入库及退货货位表
	  * @param pDto
	  * @return
	  * @return List 返回类型
	 */
	@SuppressWarnings("rawtypes")
	public List xc_st_wh_b_location_by_key(Dto pDto);
	/**
	 * 
	  * @Title xc_st_wh_b_location_by_key_01 方法名
	  * @Description 根据业务主ID或者业务行ID,货位ID获取入库单或者出库单行信息对应的所有的批次或者序列号信息
	  * @param pDto
	  * @return
	  * @return Dto 返回类型
	 */
	public Dto xc_st_wh_b_location_by_key_01(Dto pDto);
	
	/**
	 * @Title: xc_st_wh_delevered_h_select_by_key 
	 * @Description: 根据出库单ID查询出库单
	 * @param pDto
	 * @return    单条库存台账处理记录
	 */
	public Dto xc_st_wh_delevered_h_select_by_key(Dto pDto) ;
	
	/**
	 * @Title: xc_st_wh_delevered_l_select_by_key 
	 * @Description: 根据出库单ID查询出库单行信息
	 * @param List
	 * @return    多条入库单行信息
	 */
	@SuppressWarnings("rawtypes")
	public List xc_st_wh_delevered_l_select_by_key(Dto pDto) ;
	
	/**
	 * @Title: xc_st_ld_periods_select_by_key 
	 * @Description: 获取当前打开的库存区间
	 * @param pDto
	 * @return    一条区间信息
	 */
	public Dto xc_st_ld_periods_select_by_key(Dto pDto) ;
	
	
	/**
	 * @Title: xc_st_warehouse_select_by_key 
	 * @Description: 根据仓库ID和账簿ID查找库房信息
	 * @param pDto
	 * @return    单条库房信息
	 */
	public Dto xc_st_warehouse_select_by_key(Dto pDto) ;
	
	/**
	 * @Title: xc_st_ld_business_type_select_by_busId 
	 * @Description: 根据账簿ID和业务类型ID获取科目信息 
	 * @param pDto
	 * @return    科目信息
	 */
	public Dto xc_st_ld_business_type_select_by_busId(Dto pDto) ;
	
	/**
	 * @Title: xc_st_wa_ld_set_select_by_purTypeId 
	 * @Description: 根据账簿ID和仓库ID和采购类型ID获取科目信息
	 * @param pDto
	 * @return    科目信息
	 */
	public Dto xc_st_wa_ld_set_select_by_purTypeId(Dto pDto) ;
	
	
	/**
	 * @Title: xc_st_wh_inventory_insert 
	 * @Description: 保存库存台账处理记录
	 * @param bean    设定文件
	 */
	public void xc_st_wh_inventory_insert(Dto pDto) ;
	
	/**
	 * @Title: xc_st_trans_insert 
	 * @Description: 保存库存事务处理记录
	 * @param bean    设定文件
	 */
	public void xc_st_trans_insert(Dto pDto) ;
	
	/**
	 * @Title: xc_st_wh_inventory_insert 
	 * @Description: 保存库存台账处理记录
	 * @param bean    设定文件
	 */
	public void xc_st_wh_inventory_insert_list(Dto pDto) ;
	
	/**
	 * @Title: xc_st_trans_insert 
	 * @Description: 保存库存事务处理记录
	 * @param bean    设定文件
	 */
	public void xc_st_trans_insert_list(Dto pDto) ;
	
	/**
	 * @Title: xc_st_wh_inventory_update 
	 * @Description: 修改库存台账处理记录
	 * @param bean    设定文件
	 */
	public void xc_st_wh_inventory_update(Dto pDto) ;
	
	/**
	 * @Title: xc_st_wh_inventory_update_list 
	 * @Description: 修改库存台账处理记录
	 * @param bean    设定文件
	 */
	public void xc_st_wh_inventory_update_list(Dto pDto) ;
	
	
	/**
	 * @Title: xc_st_wh_inventory_deleteByKey 
	 * @Description: 根据库存存事务处理记录主键删除记录
	 * @param bean    设定文件
	 */
	public void xc_st_wh_inventory_deleteByKey(Dto pDto) ;
	
	/**
	 * @Title: xc_st_wh_inventory_delete_by_entry_h_id 
	 * @Description: 根据入库单主ID删除库存存事务处理记录
	 * @param bean    设定文件
	 */
	public void xc_st_wh_inventory_delete_by_entry_h_id(Dto pDto) ;
	
	/**
	 * @Title: xc_st_wh_inventory_delete_by_entry_l_id 
	 * @Description: 根据入库单行ID删除库存存事务处理记录
	 * @param bean    设定文件
	 */
	public void xc_st_wh_inventory_delete_by_entry_l_id(Dto pDto) ;
	/**
	 * 
	  * @Title inventory_update_costChange 方法名
	  * @Description 调整单提交时更新现存量表信息
	  * @param pDto
	  * @return void 返回类型
	 */
	public void inventory_update_costChange(Dto pDto);
	/**
	 * 
	  * @Title xc_st_trans_delete_by_entry_h_id 方法名
	  * @Description 根据入库单主ID删除库存存事务处理记录
	  * @param pDto
	  * @return void 返回类型
	 */
	public void xc_st_trans_delete_by_entry_h_id(Dto pDto);
	/**
	 * 
	  * @Title updateStWhBSerInfo 方法名
	  * @Description 出库的时候更新批次/序列号
	  * @param pDto
	  * @return void 返回类型
	 */
	public void updateStWhBSerInfo(Dto pDto);
	/**
	 * 
	  * @Title updateStWhBSerInfoByOtherId 方法名
	  * @Description 更新批次/序列号表的已出库数量（根据物料ID，批次号，货位表ID）
	  * @param pDto
	  * @return void 返回类型
	 */
	public void updateStWhBSerInfoByOtherId(Dto pDto);
}
