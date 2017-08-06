package com.xzsoft.xc.st.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.st.modal.Dto;
import com.xzsoft.xc.st.modal.XcStCommonBean;
/**
 * 
  * @ClassName XcStCommonDao
  * @Description 处理库存模块的公共dao接口
  * @author RenJianJian
  * @date 2016年12月20日 下午12:33:37
 */
public interface XcStCommonDao {
	/**
	 * 
	  * @Title updateXcStDocStatus 方法名
	  * @Description 更新单据的提交状态
	  * @param xcStCommonBeanList
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void updateXcStDocStatus(List<XcStCommonBean> xcStCommonBeanList) throws Exception;
	
	/**
	 * 
	  * @Title getXcStLdProcessInfo 方法名
	  * @Description 查询账簿级相关流程信息
	  * @param stCatCode
	  * @param ledgerId
	  * @return
	  * @throws Exception
	  * @return HashMap<String,String> 返回类型
	 */
	public HashMap<String,String> getXcStLdProcessInfo(String stCatCode,String ledgerId) throws Exception;
	
	/**
	 * 
	  * @Title getXcStProcessInfo 方法名
	  * @Description 查询全局相关流程信息
	  * @param stCatCode
	  * @return
	  * @throws Exception
	  * @return HashMap<String,String> 返回类型
	 */
	public HashMap<String,String> getXcStProcessInfo(String stCatCode) throws Exception;
	/**
	 * 
	  * @Title createrIsOrNotWarehouseKeeper 方法名
	  * @Description 判断制单人是否属于这个仓库的管理员
	  * @param USER_ID
	  * @param WAREHOUSE_ID
	  * @param LEDGER_ID
	  * @return
	  * @throws Exception
	  * @return String 返回类型
	 */
	public String createrIsOrNotWarehouseKeeper(String USER_ID,String WAREHOUSE_ID,String LEDGER_ID) throws Exception;
	/**
	 * 
	  * @Title updatePoOrderLInfo 方法名
	  * @Description 采购入库选择采购订单保存时回写采购订单对应的已入库数量等信息
	  * @param lists
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void updatePoOrderLInfo(List<XcStCommonBean> lists) throws Exception;
	/**
	 * 
	  * @Title updateSoOrderLInfo 方法名
	  * @Description 销售出库选择销售订单保存时回写销售订单对应的已出库数量等信息
	  * @param lists
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void updateSoOrderLInfo(List<XcStCommonBean> lists) throws Exception;
	/**
	 * 
	  * @Title updateXcStWhEntryHAmount 方法名
	  * @Description 更新入库单主表金额
	  * @param lists
	  * @return void 返回类型
	 */
	public void updateXcStWhEntryHAmount(List<XcStCommonBean> lists);
	/**
	 * 
	  * @Title updateXcStWhDeleveredHAmount 方法名
	  * @Description 更新出库单主表金额
	  * @param lists
	  * @return void 返回类型
	 */
	public void updateXcStWhDeleveredHAmount(List<XcStCommonBean> lists);
	/**
	 * 
	  * @Title updateXcStWhEntryLAmount 方法名
	  * @Description 更新入库单行表数量、单价、金额
	  * @param lists
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void updateXcStWhEntryLAmount(List<XcStCommonBean> lists) throws Exception;
	/**
	 * 
	  * @Title updateXcStWhDeleveredLAmount 方法名
	  * @Description 更新出库单行表数量、单价、金额
	  * @param lists
	  * @return void 返回类型
	 */
	public void updateXcStWhDeleveredLAmount(List<XcStCommonBean> lists) throws Exception;
	/**
	 * 
	  * @Title judgmentXcStWhEntryHAmount 方法名
	  * @Description 判断当前操作的红单对应的蓝单金额是否还够回冲
	  * @param RE_WH_ENTRY_H_ID
	  * @param AMOUNT
	  * @return
	  * @throws Exception
	  * @return String 返回类型
	 */
	public String judgmentXcStWhEntryHAmount(String RE_WH_ENTRY_H_ID,double AMOUNT) throws Exception;
	/**
	 * 
	  * @Title judgmentXcStWhDeleveredHAmount 方法名
	  * @Description 判断当前操作的出库单-红单对应的蓝单金额是否还够回冲
	  * @param RE_DELEVERED_H_ID
	  * @param AMOUNT
	  * @return
	  * @throws Exception
	  * @return String 返回类型
	 */
	public String judgmentXcStWhDeleveredHAmount(String RE_DELEVERED_H_ID,double AMOUNT) throws Exception;
	/**
	 * 
	  * @Title updateXcStUseApplyLAmount 方法名
	  * @Description 更新领用申请单主表金额（领用出库选择领用申请单的时候，回写）
	  * @param lists
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void updateXcStUseApplyLAmount(List<XcStCommonBean> lists) throws Exception;
	/**
	 * 
	  * @Title selectUpPeriodStatusByLedger 方法名
	  * @Description 根据账簿的单据业务日期得到上个会计期的状态
	  * @param LEDGER_ID
	  * @param BIZ_DATE
	  * @return
	  * @throws Exception
	  * @return HashMap<String,String> 返回类型
	 */
	public HashMap<String,String> selectUpPeriodStatusByLedger(String LEDGER_ID,Date BIZ_DATE) throws Exception;
	/**
	 * 
	  * @Title insertXcStWhBLocation 方法名
	  * @Description 提交的时候，需要往入库及退货货位表插入数据
	  * @param pDto
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void insertXcStWhBLocation(Dto pDto) throws Exception;
	/**
	 * 
	  * @Title updateXcStWhBLocation 方法名
	  * @Description 保存的时候，需要往入库及退货货位表更新数据
	  * @param pDto
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void updateXcStWhBLocation(Dto pDto) throws Exception;
	/**
	 * 
	  * @Title updateXcStLocationInventory 方法名
	  * @Description 货位调整提交时，更新货位信息
	  * @param pDto
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void updateXcStLocationInventory(Dto pDto) throws Exception;
	/**
	 * 
	  * @Title updateXcStWhBLocation_01 方法名
	  * @Description 回冲的时候，需要往入库及退货货位表更新数据
	  * @param pDto
	  * @return void 返回类型
	 */
	public void updateXcStWhBLocation_01(Dto pDto) throws Exception;
	/**
	 * 
	  * @Title updateXcStWhBLocation_02 方法名
	  * @Description 回冲的时候，根据业主主ID和行ID更新货位现存量
	  * @param pDto
	  * @return void 返回类型
	 */
	public void updateXcStWhBLocation_02(Dto pDto) throws Exception;
	/**
	 * 
	  * @Title getMaterialParamsInfo 方法名
	  * @Description 通过物料ID来判断当前账簿下的物料是否启用序列号或批次
	  * @param MATERIAL_ID
	  * @param LEDGER_ID
	  * @return
	  * @return HashMap<String,String> 返回类型
	 */
	public HashMap<String,String> getMaterialParamsInfo(String MATERIAL_ID,String LEDGER_ID) throws Exception;
	/**
	 * 
	  * @Title weatherExistBatchOrSerial 方法名
	  * @Description 通过物料ID、业务主ID、业务行ID来判断当前物料若启用序列号或者批次的时候，序列号或批次是否录入
	  * @param BUSINESS_H_ID
	  * @param BUSINESS_L_ID
	  * @param MATERIAL_ID
	  * @return
	  * @return int 返回类型
	 */
	public int weatherExistBatchOrSerial(String BUSINESS_H_ID,String BUSINESS_L_ID,String MATERIAL_ID) throws Exception;
}
