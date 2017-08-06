package com.xzsoft.xc.st.dao;

import java.util.List;

import com.xzsoft.xc.st.modal.Dto;


/**
 * @ClassName: XcStWhInventoryDAO 
 * @Description: 库存台账管理dao接口类
 * @author panghd
 * @date 2017年1月16日 下午15:15:43 
 *
 */
public interface XcStWhInventoryDAO {
	
	
	
	/**
	 * @Title: getStWhInventoryById 
	 * @Description: 根据事务主键查询事务详细信息
	 * @param docId
	 * @return
	 * @throws Exception    设定文件
	 */
	public Dto getStWhInventoryById(Dto pDto) throws Exception ;
	
	/**
	 * @Title: getStWhInventoryByMaterialId 
	 * @Description: 根据物资ID和仓库ID查询物资的库存情况(加权平均用)
	 * @param docId
	 * @return
	 * @throws Exception    设定文件
	 */
	public Dto getStWhInventoryByMaterialId(Dto pDto) throws Exception ;
	
	/**
	 * @Title: getStWhInventoryByEntryId 
	 * @Description: 根据物资ID和仓库ID入库单主id和入库单行ID查询物资的库存情况(个别计价，先进先出用)
	 * @param docId
	 * @return
	 * @throws Exception    设定文件
	 */
	public Dto getStWhInventoryByEntryId(Dto pDto) throws Exception ;
	
	/**
	 * @Title: getStWhEntryHById 
	 * @Description: 根据入库单ID获取入库单信息
	 * @param docId
	 * @return
	 * @throws Exception    设定文件
	 */
	public Dto getStWhEntryHById(Dto pDto) throws Exception ;
	
	/**
	 * @Title: getStWhEntryLByEntryHId 
	 * @Description: 根据入库单ID获取入库单行信息
	 * @param docId
	 * @return
	 * @throws Exception    设定文件
	 */
	@SuppressWarnings("rawtypes")
	public List getStWhEntryLByEntryHId(Dto pDto) throws Exception ;
	
	/**
	 * 
	  * @Title getStWhBSerByBusinessId 方法名
	  * @Description 根据业务主ID和行ID的批次和序列号信息
	  * @param pDto
	  * @return
	  * @throws Exception
	  * @return List 返回类型
	 */
	@SuppressWarnings("rawtypes")
	public List getStWhBSerByBusinessId(Dto pDto) throws Exception ;
	/**
	 * 
	  * @Title getStWhBLocationByBusinessId 方法名
	  * @Description 根据业务主ID和行ID的入库及退货货位表信息
	  * @param pDto
	  * @return
	  * @throws Exception
	  * @return List 返回类型
	 */
	@SuppressWarnings("rawtypes")
	public List getStWhBLocationByBusinessId(Dto pDto) throws Exception ;
	/**
	 * 
	  * @Title getStWhBLocationByBusinessIdAndLocationId 方法名
	  * @Description 根据业务主ID或者业务行ID,货位ID获取入库单或者出库单行信息对应的所有的批次或者序列号信息
	  * @param pDto
	  * @return
	  * @throws Exception
	  * @return Dto 返回类型
	 */
	public Dto getStWhBLocationByBusinessIdAndLocationId(Dto pDto) throws Exception ;
	
	/**
	 * @Title: getStWhDeleveredHById 
	 * @Description: 根据出库单ID获取出库单信息
	 * @param docId
	 * @return
	 * @throws Exception    设定文件
	 */
	public Dto getStWhDeleveredHById(Dto pDto) throws Exception ;
	
	/**
	 * @Title: getStWhEntryLByEntryHId 
	 * @Description: 根据出库单ID获取出库单行信息
	 * @param docId
	 * @return
	 * @throws Exception    设定文件
	 */
	@SuppressWarnings("rawtypes")
	public List getStWhDeleveredLById(Dto pDto) throws Exception ;
	
	/**
	 * @Title: getStLdPeriodsByOpen 
	 * @Description: 获取打开的库存区间
	 * @param pDto
	 * @return
	 * @throws Exception    设定文件
	 */
	public Dto getStLdPeriodsByOpen(Dto pDto) throws Exception ;
	
	/**
	 * @Title: getStWhInventoryByWarehouseId 
	 * @Description: 根据库房ID和账簿ID查询库房详细信息
	 * @param docId
	 * @return
	 * @throws Exception    设定文件
	 */
	public Dto getStWhInventoryByWarehouseId(Dto pDto) throws Exception ;
	
	/**
	 * @Title: getAccIdByBusId 
	 * @Description: 根据账簿ID和业务ID获取科目ID
	 * @param docId
	 * @return
	 * @throws Exception    设定文件
	 */
	public Dto getAccIdByBusId(Dto pDto) throws Exception ;
	
	/**
	 * @Title: getAccIdByPurTypeId 
	 * @Description: 根据库房ID和采购类型ID获取科目ID
	 * @param docId
	 * @return
	 * @throws Exception    设定文件
	 */
	public Dto getAccIdByPurTypeId(Dto pDto) throws Exception ;
	
	/**
	 * @Title: saveWhInventory 
	 * @Description: 保存库存事务信息
	 * @param docBean
	 * @throws Exception    设定文件
	 */
	public void saveWhInventory(Dto pDto) throws Exception ;
	
	/**
	 * @Title: saveTrans 
	 * @Description: 保存库存事务信息
	 * @param docBean
	 * @throws Exception    设定文件
	 */
	public void saveTrans(Dto pDto) throws Exception ;
	
	/**
	 * @Title: saveWhInventory 
	 * @Description: 保存库存事务信息
	 * @param docBean
	 * @throws Exception    设定文件
	 */
	public void saveWhInventoryList(Dto pDto) throws Exception ;
	
	/**
	 * @Title: saveTrans 
	 * @Description: 保存库存事务信息
	 * @param docBean
	 * @throws Exception    设定文件
	 */
	public void saveTransList(Dto pDto) throws Exception ;
	
	/**
	 * @Title: updateWhInventory 
	 * @Description:更新库存事务信息
	 * @param docBean
	 * @throws Exception    设定文件
	 */
	public void updateWhInventory(Dto pDto) throws Exception ;
	
	/**
	 * @Title: updateWhInventoryList 
	 * @Description:更新库存事务信息
	 * @param docBean
	 * @throws Exception    设定文件
	 */
	public void updateWhInventoryList(Dto pDto) throws Exception ;
	
	/**
	 * @Title: delWhInventoryById 
	 * @Description: 根据库存事务ID删除库存事务信息
	 * @param docId
	 * @throws Exception    设定文件
	 */
	public void delWhInventoryById(Dto pDto) throws Exception ;
	
	/**
	 * @Title: delWhInventoryByEntryHId 
	 * @Description: 根据入库单主ID删除库存事务信息
	 * @param docId
	 * @throws Exception    设定文件
	 */
	public void delWhInventoryByEntryHId(Dto pDto) throws Exception ;
	
	/**
	 * @Title: delWhInventoryByEntryLId 
	 * @Description: 根据入库单行ID删除库存事务信息
	 * @param docId
	 * @throws Exception    设定文件
	 */
	public void delWhInventoryByEntryLId(Dto pDto) throws Exception ;
	/**
	 * 
	  * @Title inventory_update_costChange 方法名
	  * @Description 调整单提交时更新现存量表信息
	  * @param pDto
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void inventory_update_costChange(Dto pDto) throws Exception ;
	/**
	 * 
	  * @Title xc_st_trans_delete_by_entry_h_id 方法名
	  * @Description 根据入库单主ID删除库存存事务处理记录
	  * @param pDto
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void xc_st_trans_delete_by_entry_h_id(Dto pDto) throws Exception ;
	/**
	 * 
	  * @Title updateStWhBSerInfo 方法名
	  * @Description TODO
	  * @param pDto
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void updateStWhBSerInfo(Dto pDto) throws Exception;
	/**
	 * 
	  * @Title updateStWhBSerInfoByOtherId 方法名
	  * @Description 更新批次/序列号表的已出库数量（根据物料ID，批次号，货位表ID）
	  * @param pDto
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void updateStWhBSerInfoByOtherId(Dto pDto) throws Exception;
}
