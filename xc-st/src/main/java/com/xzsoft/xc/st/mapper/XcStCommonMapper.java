package com.xzsoft.xc.st.mapper;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.st.modal.Dto;

/**
 * 
  * @ClassName XcStCommonMapper
  * @Description 处理库存模块的公共mapper接口
  * @author RenJianJian
  * @date 2016年12月20日 下午12:24:39
 */
public interface XcStCommonMapper {
	/**
	 * 
	  * @Title updateXcStDocStatus 方法名
	  * @Description 更新单据的提交状态
	  * @param map
	  * @return void 返回类型
	 */
	public void updateXcStDocStatus(HashMap<String,Object> map);
	
	/**
	 * 
	  * @Title getXcStLdProcessInfo 方法名
	  * @Description 查询账簿级相关流程信息
	  * @param map
	  * @return
	  * @return HashMap<String,String> 返回类型
	 */
	public HashMap<String,String> getXcStLdProcessInfo(HashMap<String,String> map);
	
	/**
	 * 
	  * @Title getXcStProcessInfo 方法名
	  * @Description 查询全局的相关流程信息
	  * @param stCatCode
	  * @return
	  * @return HashMap<String,String> 返回类型
	 */
	public HashMap<String,String> getXcStProcessInfo(String stCatCode);
	/**
	 * 
	  * @Title deleteXcStWhBLocationByLId 方法名
	  * @Description 根据入库及退货（出库及退库）行ID批量删除入库及退货（出库及退库）货位表
	  * @param pDto
	  * @return void 返回类型
	 */
	public void deleteXcStWhBLocationByLId(Dto pDto);
	/**
	 * 
	  * @Title deleteXcStWhBSerByLId 方法名
	  * @Description 根据入库及退货（出库及退库）行ID批量删除入库及退货（出库及退库）批次OR序列号表
	  * @param pDto
	  * @return void 返回类型
	 */
	public void deleteXcStWhBSerByLId(Dto pDto);
	/**
	 * 
	  * @Title deleteXcStWhBLocationByHId 方法名
	  * @Description 根据入库及退货（出库及退库）主ID批量删除入库及退货（出库及退库）货位表
	  * @param lists
	  * @return void 返回类型
	 */
	public void deleteXcStWhBLocationByHId(List<String> lists);
	/**
	 * 
	  * @Title deleteXcStWhBSerByHId 方法名
	  * @Description 根据入库及退货（出库及退库）主ID批量删除入库及退货（出库及退库）批次OR序列号表
	  * @param lists
	  * @return void 返回类型
	 */
	public void deleteXcStWhBSerByHId(List<String> lists);

	/**
	 * 
	  * @Title deleteXcStTransshipmentLoByLId 方法名
	  * @Description 根据物资调拨单行ID批量删除物资调拨单货位表
	  * @param pDto
	  * @return void 返回类型
	 */
	public void deleteXcStTransshipmentLoByLId(Dto pDto);
	/**
	 * 
	  * @Title deleteXcStTransshipmentSerByLId 方法名
	  * @Description 根据物资调拨单行ID批量删除物资调拨单批次OR序列号表
	  * @param pDto
	  * @return void 返回类型
	 */
	public void deleteXcStTransshipmentSerByLId(Dto pDto);
	/**
	 * 
	  * @Title deleteXcStTransshipmentLoByHId 方法名
	  * @Description 根据物资调拨单主ID批量删除物资调拨单货位表
	  * @param lists
	  * @return void 返回类型
	 */
	public void deleteXcStTransshipmentLoByHId(List<String> lists);
	/**
	 * 
	  * @Title deleteXcStTransshipmentSerByHId 方法名
	  * @Description 根据物资调拨单主ID批量删除物资调拨单批次OR序列号表
	  * @param lists
	  * @return void 返回类型
	 */
	public void deleteXcStTransshipmentSerByHId(List<String> lists);
	/**
	 * 
	  * @Title deleteXcStLoAdjustSerByLId 方法名
	  * @Description 根据货位调整单行ID批量删除货位调整单批次OR序列号表
	  * @param pDto
	  * @return void 返回类型
	 */
	public void deleteXcStLoAdjustSerByLId(Dto pDto);
	/**
	 * 
	  * @Title deleteXcStLoAdjustSerByHId 方法名
	  * @Description 根据货位调整单主ID批量删除货位调整单货位表
	  * @param lists
	  * @return void 返回类型
	 */
	public void deleteXcStLoAdjustSerByHId(List<String> lists);
	/**
	 * 
	  * @Title createrIsOrNotWarehouseKeeper 方法名
	  * @Description 判断制单人是否属于这个仓库的管理员
	  * @param map
	  * @return
	  * @return String 返回类型
	 */
	public String createrIsOrNotWarehouseKeeper(HashMap<String, Object> map);
	/**
	 * 
	  * @Title updatePoOrderLInfo 方法名
	  * @Description 采购入库选择采购订单保存时回写采购订单对应的已入库数量等信息
	  * @param map
	  * @return void 返回类型
	 */
	public void updatePoOrderLInfo(HashMap<String,Object> map);
	/**
	 * 
	  * @Title updateSoOrderLInfo 方法名
	  * @Description 销售出库选择销售订单保存时回写销售订单对应的已出库数量等信息
	  * @param map
	  * @return void 返回类型
	 */
	public void updateSoOrderLInfo(HashMap<String,Object> map);
	/**
	 * 
	  * @Title updateXcStWhEntryHAmount 方法名
	  * @Description 更新入库单主表金额
	  * @param map
	  * @return void 返回类型
	 */
	public void updateXcStWhEntryHAmount(HashMap<String, Object> map);
	/**
	 * 
	  * @Title updateXcStWhDeleveredHAmount 方法名
	  * @Description 更新出库单主表金额
	  * @param map
	  * @return void 返回类型
	 */
	public void updateXcStWhDeleveredHAmount(HashMap<String, Object> map);
	/**
	 * 
	  * @Title updateXcStWhEntryLAmount 方法名
	  * @Description 更新入库单行表数量、单价、金额
	  * @param map
	  * @return void 返回类型
	 */
	public void updateXcStWhEntryLAmount(HashMap<String, Object> map);
	/**
	 * 
	  * @Title updateXcStWhDeleveredLAmount 方法名
	  * @Description 更新出库单行表数量、单价、金额
	  * @param map
	  * @return void 返回类型
	 */
	public void updateXcStWhDeleveredLAmount(HashMap<String, Object> map);
	/**
	 * 
	  * @Title judgmentXcStWhEntryHAmount 方法名
	  * @Description 判断当前操作的红单对应的蓝单金额是否还够回冲
	  * @param map
	  * @return
	  * @return String 返回类型
	 */
	public String judgmentXcStWhEntryHAmount(HashMap<String, Object> map);
	/**
	 * 
	  * @Title judgmentXcStWhDeleveredHAmount 方法名
	  * @Description 判断当前操作的出库单-红单对应的蓝单金额是否还够回冲
	  * @param map
	  * @return
	  * @return String 返回类型
	 */
	public String judgmentXcStWhDeleveredHAmount(HashMap<String, Object> map);
	/**
	 * 
	  * @Title updateXcStUseApplyLAmount 方法名
	  * @Description 更新领用申请单主表金额（领用出库选择领用申请单的时候，回写）
	  * @param map
	  * @return void 返回类型
	 */
	public void updateXcStUseApplyLAmount(HashMap<String, Object> map);
	/**
	 * 
	  * @Title selectUpPeriodStatusByLedger 方法名
	  * @Description 根据账簿的单据业务日期得到上个会计期的状态
	  * @param map
	  * @return
	  * @return HashMap<String,String> 返回类型
	 */
	public HashMap<String,String> selectUpPeriodStatusByLedger(HashMap<String,Object> map);
	/**
	 * 
	  * @Title insertXcStWhBLocation 方法名
	  * @Description 保存的时候，需要往入库及退货货位表插入数据
	  * @param pDto
	  * @return void 返回类型
	 */
	public void insertXcStWhBLocation(Dto pDto);
	/**
	 * 
	  * @Title updateXcStWhBLocation 方法名
	  * @Description 保存的时候，需要往入库及退货货位表更新数据
	  * @param pDto
	  * @return void 返回类型
	 */
	public void updateXcStWhBLocation(Dto pDto);
	/**
	 * 
	  * @Title updateXcStLocationInventory 方法名
	  * @Description 货位调整提交时，更新货位信息
	  * @param pDto
	  * @return void 返回类型
	 */
	public void updateXcStLocationInventory(Dto pDto);
	/**
	 * 
	  * @Title updateXcStWhBLocation_01 方法名
	  * @Description 回冲的时候，需要往入库及退货货位表更新数据
	  * @param pDto
	  * @return void 返回类型
	 */
	public void updateXcStWhBLocation_01(Dto pDto);
	/**
	 * 
	  * @Title updateXcStWhBLocation_02 方法名
	  * @Description 回冲的时候，根据业主主ID和行ID更新货位现存量
	  * @param pDto
	  * @return void 返回类型
	 */
	public void updateXcStWhBLocation_02(Dto pDto);
	/**
	 * 
	  * @Title getMaterialParamsInfo 方法名
	  * @Description 通过物料ID来判断当前账簿下的物料是否启用序列号或批次
	  * @param map
	  * @return
	  * @return HashMap<String,String> 返回类型
	 */
	public HashMap<String,String> getMaterialParamsInfo(HashMap<String,Object> map);
	/**
	 * 
	  * @Title weatherExistBatchOrSerial 方法名
	  * @Description 通过物料ID、业务主ID、业务行ID来判断当前物料若启用序列号或者批次的时候，序列号或批次是否录入
	  * @param map
	  * @return
	  * @return int 返回类型
	 */
	public int weatherExistBatchOrSerial(HashMap<String,Object> map);
}