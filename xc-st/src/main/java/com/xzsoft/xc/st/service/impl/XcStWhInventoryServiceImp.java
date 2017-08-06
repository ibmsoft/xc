package com.xzsoft.xc.st.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xzsoft.xc.gl.dao.VInterfaceDao;
import com.xzsoft.xc.gl.dao.XCGLCommonDAO;
import com.xzsoft.xc.gl.modal.VInterface;
import com.xzsoft.xc.gl.service.VoucherHandlerService;
import com.xzsoft.xc.st.dao.XcStCommonDao;
import com.xzsoft.xc.st.dao.XcStCostChangeDao;
import com.xzsoft.xc.st.dao.XcStLocationInventoryDao;
import com.xzsoft.xc.st.dao.XcStWhInventoryDAO;
import com.xzsoft.xc.st.modal.BaseDto;
import com.xzsoft.xc.st.modal.Dto;
import com.xzsoft.xc.st.service.XcStWhInventoryService;
import com.xzsoft.xc.st.util.XcStConstans;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xc.util.common.XConstants;
import com.xzsoft.xip.platform.util.PlatformUtil;
import com.xzsoft.xip.platform.util.XipUtil;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;
/**
 * 
 * @ClassName XcStWhInventoryServiceImp
 * @Description 库存台账管理的公共service层
 * @author panghd
 * @date 2017年2月16日 下午3:34:20
 */
@Service("xcStWhInventoryService")
public class XcStWhInventoryServiceImp implements XcStWhInventoryService {
	public static final Logger log = Logger.getLogger(XcStWhInventoryServiceImp.class);
	@Resource
	private XcStWhInventoryDAO xcStWhInventoryDAO;
	@Resource
	private VInterfaceDao vInterfaceDao;
	@Resource
	private VoucherHandlerService voucherHandlerService;
	@Resource
	private XCGLCommonDAO xCGLCommonDAO;
	@Resource
	private XcStCostChangeDao xcStCostChangeDao;
	@Resource
	private XcStLocationInventoryDao xcStLocationInventoryDao;
	@Resource
	private XcStCommonDao xcStCommonDao;
	@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void entry(Dto pDto) throws Exception {
		try {
			// 获取入库单信息
			Dto entryHDto = xcStWhInventoryDAO.getStWhEntryHById(pDto);
			// 获取入库单行信息
			ArrayList entryLList = (ArrayList) xcStWhInventoryDAO.getStWhEntryLByEntryHId(pDto);
			// 单据类型
			String DOC_CAT_CODE = entryHDto.getAsString("DOC_CAT_CODE");
			// 获取入库单对应的仓库的信息
			Dto warehouseDto = xcStWhInventoryDAO.getStWhInventoryByWarehouseId(entryHDto);
			// 获取当前打开的库存区间
			Dto periodDto = xcStWhInventoryDAO.getStLdPeriodsByOpen(entryHDto);
	
			String userId = CurrentUserUtil.getCurrentUserId();// 当前登录人id
			Date createDate = CurrentUserUtil.getCurrentDate();// 当前创建日期
	
			if ("2".equals(entryHDto.getAsString("INIT")) &&
					(entryHDto.getAsTimestamp("BIZ_DATE").before(periodDto.getAsTimestamp("START_DATE")) || 
							periodDto.getAsTimestamp("END_DATE").before(entryHDto.getAsTimestamp("BIZ_DATE")))) {
				throw new Exception("业务日期不在库存区间内！");
			}
			else {
				for (int i = 0; i < entryLList.size(); i++) {
					ArrayList entryLInsertList = new ArrayList();//批次插入
					ArrayList entryLUpdateList = new ArrayList();//批次更新
					Dto entryLDto = (Dto) entryLList.get(i);
					entryLDto.put("TRANS_ID", java.util.UUID.randomUUID().toString());
					entryLDto.put("WAREHOUSE_ID",entryHDto.getAsString("WAREHOUSE_ID"));
					entryLDto.put("BUSINESS_H_ID", entryHDto.getAsString("ENTRY_H_ID"));
					entryLDto.put("BUSINESS_L_ID", entryLDto.getAsString("ENTRY_L_ID"));
					entryLDto.put("PERIOD_CODE", periodDto.getAsString("ST_PERIODS"));
					entryLDto.put("VALUATION_MODEL", warehouseDto.getAsString("VALUATION_MODEL"));
					entryLDto.put("STATUS", "Y");
					entryLDto.put("LOCATIONS", entryLDto.getAsString("LOCATION_ID"));
					entryLDto.put("SERIALS", "后续添加");
					entryLDto.put("LAST_UPDATE_DATE", createDate);
					entryLDto.put("LAST_UPDATED_BY", userId);
					Dto inventoryDto = null;
					//根据计价方式来判断改变现存量的方式
					if ("YDJQPJF".equals(warehouseDto.getAsString("VALUATION_MODEL"))
							|| "FJZK".equals(warehouseDto.getAsString("VALUATION_MODEL"))) {
						//根据物资ID和仓库ID查询物资的库存情况(加权平均用)
						inventoryDto = xcStWhInventoryDAO.getStWhInventoryByMaterialId(entryLDto);
					}
					if("GBJJF".equals(warehouseDto.getAsString("VALUATION_MODEL"))
							|| "XJXCF".equals(warehouseDto.getAsString("VALUATION_MODEL"))) {
						if(XcStConstans.QTRK_H.equals(entryHDto.getAsString("DOC_CAT_CODE")) || XcStConstans.CGTH.equals(entryHDto.getAsString("DOC_CAT_CODE"))){
							//根据物资ID和仓库ID入库单主id和入库单行ID查询物资的库存情况(个别计价，先进先出用)
							entryLDto.put("ENTRY_H_ID",entryLDto.getAsString("RE_ENTRY_H_ID"));
							entryLDto.put("ENTRY_L_ID",entryLDto.getAsString("RE_ENTRY_L_ID"));
							inventoryDto = xcStWhInventoryDAO.getStWhInventoryByEntryId(entryLDto);
						}
					}
					if (null == inventoryDto || "".equals(inventoryDto)) {
						entryLDto.put("INVENTORY_ID", java.util.UUID.randomUUID().toString());
						entryLDto.put("ORIGINAL_QTY", 0);
						entryLDto.put("ORIGINAL_PRICE", 0);
						entryLDto.put("ORIGINAL_AMOUNT", 0);
						entryLDto.put("NEWEST_QTY", entryLDto.getAsInteger("QTY"));
						entryLDto.put("NEWEST_PRICE", (entryLDto.getAsInteger("AMOUNT")) / (entryLDto.getAsInteger("QTY")));
						entryLDto.put("NEWEST_AMOUNT", entryLDto.getAsInteger("AMOUNT"));
						entryLDto.put("CREATION_DATE", createDate);
						entryLDto.put("CREATED_BY", userId);
						entryLInsertList.add(entryLDto);
					}
					else{
						entryLDto.put("INVENTORY_ID", inventoryDto.getAsString("INVENTORY_ID"));
						entryLDto.put("ORIGINAL_QTY", inventoryDto.getAsInteger("QTY"));
						entryLDto.put("ORIGINAL_PRICE", inventoryDto.getAsInteger("PRICE"));
						entryLDto.put("ORIGINAL_AMOUNT", inventoryDto.getAsInteger("AMOUNT"));
						entryLDto.put("NEWEST_QTY", entryLDto.getAsInteger("QTY") * entryLDto.getAsInteger("COEFFICIENT") +
								inventoryDto.getAsInteger("QTY"));
						if ((entryLDto.getAsInteger("QTY") * entryLDto.getAsInteger("COEFFICIENT") + inventoryDto .getAsInteger("QTY")) == 0) {
							entryLDto.put("NEWEST_PRICE", 0);
						} else {
							entryLDto.put("NEWEST_PRICE",(entryLDto.getAsInteger("AMOUNT") * entryLDto.getAsInteger("COEFFICIENT") + inventoryDto.getAsInteger("AMOUNT")) /
															(entryLDto.getAsInteger("QTY") * entryLDto.getAsInteger("COEFFICIENT") + inventoryDto.getAsInteger("QTY")));
						}
						entryLDto.put("NEWEST_AMOUNT", entryLDto.getAsInteger("AMOUNT") * entryLDto.getAsInteger("COEFFICIENT") +
															inventoryDto.getAsInteger("AMOUNT"));
						entryLDto.put("COEFFICIENT", 1);
						entryLUpdateList.add(entryLDto);
					}
					if(XcStConstans.QTRK.equals(entryHDto.getAsString("DOC_CAT_CODE")) || XcStConstans.CGRK.equals(entryHDto.getAsString("DOC_CAT_CODE"))){
						//通过行信息中的物料，判断当前物料是否启用批次/序列号，启用的话批次/序列号必须要有，并且给出提示
						ArrayList serList = (ArrayList) xcStWhInventoryDAO.getStWhBSerByBusinessId(entryLDto);
						HashMap<String,String> returnMap = xcStCommonDao.getMaterialParamsInfo(entryLDto.getAsString("MATERIAL_ID"),entryLDto.getAsString("LEDGER_ID"));
						if("Y".equals(returnMap.get("IS_BATCH")) || "Y".equals(returnMap.get("IS_SERIAL"))){
							if(serList == null || serList.size() <= 0)
								throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_ST_MATERIAL", null) + XipUtil.getMessage(XCUtil.getLang(), "XC_ST_DAO_0001", null));
						}
						for (int j = 0; j < serList.size(); j++) {//批次汇总
							Dto dtoInfo = new BaseDto();
							ArrayList insertLocationInventoryList_02 = new ArrayList();//批次汇总表插入
							ArrayList updateLocationInventoryList_02 = new ArrayList();//批次汇总表更新
							Dto locationInventoryDto = new BaseDto();
							entryLDto.put("SERIAL_BATCH_NUM",((Dto) serList.get(j)).getAsString("SERIAL_BATCH_NUM"));
							//根据仓库ID，物料ID，批次ID，货位ID查询货位汇总表是否存在
							Dto locationInventoryDto_02 = xcStLocationInventoryDao.getLocationInventoryByWMSID(entryLDto);
							if(locationInventoryDto_02 == null || "".equals(locationInventoryDto_02)){
								locationInventoryDto.put("LO_INVENTORY_ID",java.util.UUID.randomUUID().toString());
								locationInventoryDto.put("ORG_ID",entryLDto.getAsString("ORG_ID"));
								locationInventoryDto.put("LEDGER_ID",entryLDto.getAsString("LEDGER_ID"));
								locationInventoryDto.put("WAREHOUSE_ID",entryLDto.getAsString("WAREHOUSE_ID"));
								locationInventoryDto.put("MATERIAL_ID",entryLDto.getAsString("MATERIAL_ID"));
								locationInventoryDto.put("LOCATION_ID",entryLDto.getAsString("LOCATION_ID"));
								locationInventoryDto.put("SERIAL_BATCH_NUM",entryLDto.getAsString("SERIAL_BATCH_NUM"));
								locationInventoryDto.put("QTY",((Dto) serList.get(j)).getAsInteger("QTY"));
								locationInventoryDto.put("VALUATION_MODEL",entryLDto.getAsString("VALUATION_MODEL"));
								locationInventoryDto.put("CREATION_DATE", createDate);
								locationInventoryDto.put("CREATED_BY", userId);
								locationInventoryDto.put("LAST_UPDATE_DATE", createDate);
								locationInventoryDto.put("LAST_UPDATED_BY", userId);
								insertLocationInventoryList_02.add(locationInventoryDto);
							}
							else{
								locationInventoryDto.put("LO_INVENTORY_ID",locationInventoryDto_02.getAsString("LO_INVENTORY_ID"));
								locationInventoryDto.put("QTY",((Dto) serList.get(j)).getAsInteger("QTY"));
								locationInventoryDto.put("LAST_UPDATE_DATE", createDate);
								locationInventoryDto.put("LAST_UPDATED_BY", userId);
								updateLocationInventoryList_02.add(locationInventoryDto);
							}
							if (insertLocationInventoryList_02 != null && insertLocationInventoryList_02.size() > 0) {//插入批次汇总表
								dtoInfo.put("insertLocationInventoryList", insertLocationInventoryList_02);
								xcStLocationInventoryDao.insertLocationInventory(dtoInfo);
							}
							if (updateLocationInventoryList_02 != null && updateLocationInventoryList_02.size() > 0) {//更新批次汇总表
								dtoInfo.put("updateLocationInventoryList", updateLocationInventoryList_02);
								xcStLocationInventoryDao.updateLocationInventory(dtoInfo);
							}
						}
						if(!entryLDto.getAsString("LOCATION_ID").isEmpty() && serList.isEmpty()){//货位汇总
							Dto dtoInfo = new BaseDto();
							Dto locationInventoryDto = new BaseDto();
							ArrayList insertLocationInventoryList_01 = new ArrayList();//货位汇总表插入
							ArrayList updateLocationInventoryList_01 = new ArrayList();//货位汇总表更新
							//根据仓库ID，物料ID，货位ID查询货位汇总表是否存在
							Dto locationInventoryDto_01 = xcStLocationInventoryDao.getLocationInventoryByWMSID(entryLDto);
							if(locationInventoryDto_01 == null || "".equals(locationInventoryDto_01)){
								locationInventoryDto.put("LO_INVENTORY_ID",java.util.UUID.randomUUID().toString());
								locationInventoryDto.put("ORG_ID",entryLDto.getAsString("ORG_ID"));
								locationInventoryDto.put("LEDGER_ID",entryLDto.getAsString("LEDGER_ID"));
								locationInventoryDto.put("WAREHOUSE_ID",entryLDto.getAsString("WAREHOUSE_ID"));
								locationInventoryDto.put("MATERIAL_ID",entryLDto.getAsString("MATERIAL_ID"));
								locationInventoryDto.put("LOCATION_ID",entryLDto.getAsString("LOCATION_ID"));
								locationInventoryDto.put("SERIAL_BATCH_NUM",entryLDto.getAsString("SERIAL_BATCH_NUM"));
								locationInventoryDto.put("QTY",entryLDto.getAsInteger("QTY"));
								locationInventoryDto.put("VALUATION_MODEL",entryLDto.getAsString("VALUATION_MODEL"));
								locationInventoryDto.put("CREATION_DATE", createDate);
								locationInventoryDto.put("CREATED_BY", userId);
								locationInventoryDto.put("LAST_UPDATE_DATE", createDate);
								locationInventoryDto.put("LAST_UPDATED_BY", userId);
								insertLocationInventoryList_01.add(locationInventoryDto);
							}
							else{
								locationInventoryDto.put("LO_INVENTORY_ID",locationInventoryDto_01.getAsString("LO_INVENTORY_ID"));
								locationInventoryDto.put("QTY",entryLDto.getAsInteger("QTY"));
								locationInventoryDto.put("LAST_UPDATE_DATE", createDate);
								locationInventoryDto.put("LAST_UPDATED_BY", userId);
								updateLocationInventoryList_01.add(locationInventoryDto);
							}
							if (insertLocationInventoryList_01 != null && insertLocationInventoryList_01.size() > 0) {//插入货位汇总表
								dtoInfo.put("insertLocationInventoryList", insertLocationInventoryList_01);
								xcStLocationInventoryDao.insertLocationInventory(dtoInfo);
							}
							if (updateLocationInventoryList_01 != null && updateLocationInventoryList_01.size() > 0) {//更新货位汇总表
								dtoInfo.put("updateLocationInventoryList", updateLocationInventoryList_01);
								xcStLocationInventoryDao.updateLocationInventory(dtoInfo);
							}
						}
					}
					else{//回冲单据-红单
						ArrayList locationList = (ArrayList) xcStWhInventoryDAO.getStWhBLocationByBusinessId(entryLDto);
						if("Y".equals(warehouseDto.getAsString("IS_LOCATION"))){//仓库启用货位管理
							//判断行信息是否有仓库信息（根据业务主ID和行ID查询货位表）
							if(locationList == null || locationList.size() <= 0)
								throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_ST_WAREHOUSE", null) + XipUtil.getMessage(XCUtil.getLang(), "XC_ST_DAO_0002", null));
						}
						if(!entryLDto.getAsString("SERIAL_BATCH_NUM").isEmpty()){//存在批次
							Dto dtoInfo = new BaseDto();
							ArrayList updateLocationInventoryList_02 = new ArrayList();//批次汇总表更新
							ArrayList updateWhBSerList = new ArrayList();//批次现存量更新
							if(locationList == null || locationList.size() <= 0){//没有货位信息，则直接更新批次表
								Dto locationInventoryDto = new BaseDto();
								//更新批次现存量表
								Dto whBSerDto = new BaseDto();
								Dto locationInventoryDto_02 = xcStLocationInventoryDao.getLocationInventoryByWMSID(entryLDto);
								locationInventoryDto.put("LO_INVENTORY_ID",locationInventoryDto_02.getAsString("LO_INVENTORY_ID"));
								locationInventoryDto.put("QTY",entryLDto.getAsInteger("QTY"));
								locationInventoryDto.put("LAST_UPDATE_DATE", createDate);
								locationInventoryDto.put("LAST_UPDATED_BY", userId);
								updateLocationInventoryList_02.add(locationInventoryDto);
								
								whBSerDto.put("B_SERIAL_ID",entryLDto.getAsString("B_SERIAL_ID"));
								whBSerDto.put("DELEVERED_QTY",entryLDto.getAsInteger("QTY") * -1);
								whBSerDto.put("LAST_UPDATE_DATE", createDate);
								whBSerDto.put("LAST_UPDATED_BY", userId);
								updateWhBSerList.add(whBSerDto);
							}
							if (updateLocationInventoryList_02 != null && updateLocationInventoryList_02.size() > 0) {//更新批次汇总表
								dtoInfo.put("updateLocationInventoryList", updateLocationInventoryList_02);
								xcStLocationInventoryDao.updateLocationInventory(dtoInfo);
							}
							if (updateWhBSerList != null && updateWhBSerList.size() > 0) {//更新批次/序列号现存量
								dtoInfo.put("updateWhBSerList", updateWhBSerList);
								xcStWhInventoryDAO.updateStWhBSerInfo(dtoInfo);
							}
						}
						for(int j= 0;j < locationList.size();j++){//存在货位
							Dto dtoInfo = new BaseDto();
							Dto locationInventoryDto = new BaseDto();
							ArrayList updateLocationInventoryList_01 = new ArrayList();//货位汇总表更新
							ArrayList updateWhBSerList = new ArrayList();//批次现存量更新
							if(entryLDto.getAsString("SERIAL_BATCH_NUM").isEmpty()){//有货位，没有批次信息
								//根据仓库ID，物料ID，货位ID查询货位汇总表是否存在
								entryLDto.put("LOCATION_ID",((Dto) locationList.get(j)).getAsString("LOCATION_ID"));
								Dto locationInventoryDto_01 = xcStLocationInventoryDao.getLocationInventoryByWMSID(entryLDto);
								locationInventoryDto.put("LO_INVENTORY_ID",locationInventoryDto_01.getAsString("LO_INVENTORY_ID"));
								locationInventoryDto.put("QTY",((Dto) locationList.get(j)).getAsInteger("QTY") * -1);
								locationInventoryDto.put("LAST_UPDATE_DATE", createDate);
								locationInventoryDto.put("LAST_UPDATED_BY", userId);
								updateLocationInventoryList_01.add(locationInventoryDto);
								if(updateLocationInventoryList_01 != null && updateLocationInventoryList_01.size() > 0){
									//记录货位汇总表
									dtoInfo.put("dbType",PlatformUtil.getDbType());
									dtoInfo.put("updateLocationInventoryList",updateLocationInventoryList_01);
									xcStLocationInventoryDao.updateLocationInventory(dtoInfo);
								}
							}
							else{//货位、批次都存在，则根据货位信息更新批次
								entryLDto.put("LOCATION_ID",((Dto) locationList.get(j)).getAsString("LOCATION_ID"));
								Dto locationInventoryDto_02 = xcStLocationInventoryDao.getLocationInventoryByWMSID(entryLDto);
								locationInventoryDto.put("LO_INVENTORY_ID",locationInventoryDto_02.getAsString("LO_INVENTORY_ID"));
								locationInventoryDto.put("QTY",((Dto) locationList.get(j)).getAsInteger("QTY") * -1);
								locationInventoryDto.put("LAST_UPDATE_DATE", createDate);
								locationInventoryDto.put("LAST_UPDATED_BY", userId);
								updateLocationInventoryList_01.add(locationInventoryDto);
								
								Dto whBSerDto = new BaseDto();
								whBSerDto.put("MATERIAL_ID",entryLDto.getAsString("MATERIAL_ID"));
								whBSerDto.put("SERIAL_BATCH_NUM",entryLDto.getAsString("SERIAL_BATCH_NUM"));
								whBSerDto.put("B_LOCATION_ID",((Dto) locationList.get(j)).getAsString("RE_B_LOCATION_ID"));
								whBSerDto.put("DELEVERED_QTY",((Dto) locationList.get(j)).getAsInteger("QTY"));
								whBSerDto.put("LAST_UPDATE_DATE", createDate);
								whBSerDto.put("LAST_UPDATED_BY", userId);
								updateWhBSerList.add(whBSerDto);
								if (updateWhBSerList != null && updateWhBSerList.size() > 0) {//更新批次/序列号现存量
									dtoInfo.put("updateWhBSerList", updateWhBSerList);
									xcStWhInventoryDAO.updateStWhBSerInfoByOtherId(dtoInfo);
								}
								if(updateLocationInventoryList_01 != null && updateLocationInventoryList_01.size() > 0){
									//记录货位汇总表
									dtoInfo.put("dbType",PlatformUtil.getDbType());
									dtoInfo.put("updateLocationInventoryList",updateLocationInventoryList_01);
									xcStLocationInventoryDao.updateLocationInventory(dtoInfo);
								}
							}
							ArrayList bLocationInsertList = new ArrayList();//入库及退货货位表更新数据
							dtoInfo.put("B_LOCATION_ID",((Dto) locationList.get(j)).getAsString("RE_B_LOCATION_ID"));
							dtoInfo.put("DELEVERED_QTY",((Dto) locationList.get(j)).getAsInteger("QTY"));
							dtoInfo.put("LAST_UPDATE_DATE", createDate);
							dtoInfo.put("LAST_UPDATED_BY", userId);
							bLocationInsertList.add(dtoInfo);
							if(bLocationInsertList != null && bLocationInsertList.size() > 0){
								//记录入库入库及退货货位表
								dtoInfo.put("dbType",PlatformUtil.getDbType());
								dtoInfo.put("list",bLocationInsertList);
								xcStCommonDao.updateXcStWhBLocation_01(dtoInfo);
							}
						}
					}
					//会计平台
					VInterface vInterface = new VInterface();
					vInterface.setvInterfaceId(UUID.randomUUID().toString());
					vInterface.setLedgerId(entryHDto.getAsString("LEDGER_ID"));
					vInterface.setPeriodCode(periodDto.getAsString("ST_PERIODS"));
					vInterface.setTransCode("RKD");
					vInterface.setDeptId(entryHDto.getAsString("DEPT_ID"));
					vInterface.setProjectId(entryHDto.getAsString("PROJECT_ID"));
	
					HashMap<String, String> segments = new HashMap<String, String>();
					if (entryHDto.getAsString("DEPT_ID") != null && !"".equals(entryHDto.getAsString("DEPT_ID"))) {
						segments.put(XConstants.XIP_PUB_DETPS, entryHDto.getAsString("DEPT_ID"));
					}
					if (entryHDto.getAsString("PROJECT_ID") != null && !"".equals(entryHDto.getAsString("PROJECT_ID"))) {
						segments.put(XConstants.XC_PM_PROJECTS, entryHDto.getAsString("PROJECT_ID"));
					}
					entryHDto.put("MATERIAL_ID", entryLDto.getAsString("MATERIAL_ID"));
					Dto drAcctDto = xcStWhInventoryDAO.getAccIdByPurTypeId(entryHDto);
					Dto crAcctDto = xcStWhInventoryDAO.getAccIdByBusId(entryHDto);
					if(!"FJZK".equals(warehouseDto.getAsString("VALUATION_MODEL"))){
						if (drAcctDto == null || "".equals(drAcctDto.getAsString("AP_ACC_ID_INV"))||drAcctDto.getAsString("AP_ACC_ID_INV") == null) {
							throw new Exception("借方科目不存在！");
						}
						if (crAcctDto == null || "".equals(crAcctDto.getAsString("ACC_ID"))||crAcctDto.getAsString("ACC_ID")== null) {
							throw new Exception("贷方科目不存在！");
						}
					}
					// 查询科目组id
					if (drAcctDto.getAsString("AP_ACC_ID_INV") != null) {
						JSONObject drKmObject = voucherHandlerService .handlerCCID(entryHDto.getAsString("LEDGER_ID"), drAcctDto.getAsString("AP_ACC_ID_INV"), segments);
						JSONObject crKmObject = voucherHandlerService .handlerCCID(entryHDto.getAsString("LEDGER_ID"), crAcctDto.getAsString("ACC_ID"), segments);
						if (drKmObject != null) {
							vInterface.setDrCcid(drKmObject.getString("ccid"));
						}
						if (crKmObject != null) {
							vInterface.setCrCcid(crKmObject.getString("ccid"));
						}
					}
					vInterface.setDrAcct(xCGLCommonDAO.getAccountById( drAcctDto.getAsString("AP_ACC_ID_INV")).getAccCode());
					vInterface.setCrAcct(xCGLCommonDAO.getAccountById( crAcctDto.getAsString("ACC_ID")).getAccCode());
					vInterface.setAmt(Double.valueOf(entryLDto.getAsInteger("AMOUNT")));
					vInterface.setSummary(entryHDto.getAsString("DOC_CAT_CODE") + "/" + entryHDto.getAsString("ENTRY_H_CODE") + "/" + entryHDto.getAsString("ENTRY_H_NAME"));
					vInterface.setBusNo(entryHDto.getAsString("ENTRY_H_CODE"));
					vInterface.setSrcHid(entryHDto.getAsString("ENTRY_H_ID"));
					vInterface.setSrcLid(entryLDto.getAsString("ENTRY_L_ID"));
					vInterface.setCreatedBy(userId);
					vInterface.setCreationDate(createDate);
					vInterface.setLastUpdateBy(userId);
					vInterface.setLastUpdateDate(createDate);
					vInterfaceDao.insertVInterface(vInterface);
					if (entryLInsertList != null && entryLInsertList.size() > 0) {
						pDto.put("insertList", entryLInsertList);
						xcStWhInventoryDAO.saveWhInventoryList(pDto);
					}
					if (entryLUpdateList != null && entryLUpdateList.size() > 0) {
						pDto.put("updateList", entryLUpdateList);
						xcStWhInventoryDAO.updateWhInventoryList(pDto);
					}
				}
				pDto.put("list", entryLList);
				xcStWhInventoryDAO.saveTransList(pDto);//保存事物表
			}
		} catch (Exception e) {
			throw e;
		}
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void delevered(Dto pDto) throws Exception {
		try {
			// 获取出库单信息
			Dto deleveredHDto = xcStWhInventoryDAO.getStWhDeleveredHById(pDto);
			// 获取出库单行信息
			ArrayList deleveredLList = (ArrayList) xcStWhInventoryDAO.getStWhDeleveredLById(pDto);
			// 获取入库单对应的仓库的信息
			Dto warehouseDto = xcStWhInventoryDAO.getStWhInventoryByWarehouseId(deleveredHDto);
			// 获取当前打开的库存区间
			Dto periodDto = xcStWhInventoryDAO.getStLdPeriodsByOpen(deleveredHDto);
	
			String userId = CurrentUserUtil.getCurrentUserId();// 当前登录人id
			Date createDate = CurrentUserUtil.getCurrentDate();// 当前创建日期

			ArrayList entryLUpdateList = new ArrayList();
	
			if (deleveredHDto.getAsTimestamp("BIZ_DATE").before(periodDto.getAsTimestamp("START_DATE"))
					|| periodDto.getAsTimestamp("END_DATE").before(deleveredHDto.getAsTimestamp("BIZ_DATE"))) {
				throw new Exception("业务日期不在库存区间内！");
			} else {
				for (int i = 0; i < deleveredLList.size(); i++) {
					Dto deleveredLDto = (Dto) deleveredLList.get(i);
					deleveredLDto.put("TRANS_ID", java.util.UUID.randomUUID() .toString());
					deleveredLDto.put("WAREHOUSE_ID", deleveredHDto.getAsString("WAREHOUSE_ID"));
					deleveredLDto.put("BUSINESS_H_ID", deleveredHDto.getAsString("DELEVERED_H_ID"));
					deleveredLDto.put("BUSINESS_L_ID", deleveredLDto.getAsString("DELEVERED_L_ID"));
					deleveredLDto.put("PERIOD_CODE", periodDto.getAsString("ST_PERIODS"));
					deleveredLDto.put("STATUS", "Y");
					deleveredLDto.put("LOCATIONS", deleveredLDto.getAsString("LOCATION_ID"));
					deleveredLDto.put("SERIALS", "后续添加");
					Dto inventoryDto = null;
					if ("YDJQPJF".equals(warehouseDto.getAsString("VALUATION_MODEL"))
							|| "FJZK".equals(warehouseDto.getAsString("VALUATION_MODEL"))) {
						//根据物资ID和仓库ID查询物资的库存情况(加权平均用)
						inventoryDto = xcStWhInventoryDAO.getStWhInventoryByMaterialId(deleveredLDto);
					}
					if ("GBJJF".equals(warehouseDto.getAsString("VALUATION_MODEL"))
						|| "XJXCF".equals(warehouseDto.getAsString("VALUATION_MODEL"))) {
						//根据物资ID和仓库ID入库单主id和入库单行ID查询物资的库存情况(个别计价，先进先出用)
						inventoryDto = xcStWhInventoryDAO.getStWhInventoryByEntryId(deleveredLDto);
					}
					if (inventoryDto != null && inventoryDto.getAsInteger("QTY") < deleveredLDto.getAsInteger("QTY")) {
						throw new Exception("库存数量不足！");
					}
					deleveredLDto.put("INVENTORY_ID", inventoryDto.getAsString("INVENTORY_ID"));
					deleveredLDto.put("ORIGINAL_QTY", inventoryDto.getAsInteger("QTY"));
					deleveredLDto.put("ORIGINAL_PRICE", inventoryDto.getAsInteger("PRICE"));
					deleveredLDto.put("ORIGINAL_AMOUNT", inventoryDto.getAsInteger("AMOUNT"));
					deleveredLDto.put("NEWEST_QTY", deleveredLDto.getAsInteger("QTY") * deleveredLDto.getAsInteger("COEFFICIENT") +
													inventoryDto.getAsInteger("QTY"));
					if ((deleveredLDto.getAsInteger("QTY") * deleveredLDto.getAsInteger("COEFFICIENT") + inventoryDto .getAsInteger("QTY")) == 0) {
						deleveredLDto.put("NEWEST_PRICE", 0);
					} else {
						deleveredLDto.put("NEWEST_PRICE",(deleveredLDto.getAsInteger("AMOUNT") * deleveredLDto.getAsInteger("COEFFICIENT") + inventoryDto.getAsInteger("AMOUNT")) /
														(deleveredLDto.getAsInteger("QTY") * deleveredLDto.getAsInteger("COEFFICIENT") + inventoryDto.getAsInteger("QTY")));
					}
					deleveredLDto.put("NEWEST_AMOUNT", deleveredLDto.getAsInteger("AMOUNT") * deleveredLDto.getAsInteger("COEFFICIENT") +
														inventoryDto.getAsInteger("AMOUNT"));
					deleveredLDto.put("LAST_UPDATE_DATE", createDate);
					deleveredLDto.put("LAST_UPDATED_BY", userId);
					if(XcStConstans.QTCK_H.equals(deleveredHDto.getAsString("DOC_CAT_CODE")) || XcStConstans.XSTK.equals(deleveredHDto.getAsString("DOC_CAT_CODE")) || XcStConstans.LYTK.equals(deleveredHDto.getAsString("DOC_CAT_CODE")))
						deleveredLDto.put("COEFFICIENT", -1);
					entryLUpdateList.add(deleveredLDto);
					//蓝单
					if(XcStConstans.QTCK.equals(deleveredHDto.getAsString("DOC_CAT_CODE")) || XcStConstans.XSCK.equals(deleveredHDto.getAsString("DOC_CAT_CODE")) || XcStConstans.LYCK.equals(deleveredHDto.getAsString("DOC_CAT_CODE"))){
						ArrayList locationList = (ArrayList) xcStWhInventoryDAO.getStWhBLocationByBusinessId(deleveredLDto);
						if("Y".equals(warehouseDto.getAsString("IS_LOCATION"))){//仓库启用货位管理
							//判断行信息是否有仓库信息（根据业务主ID和行ID查询货位表）
							if(locationList == null ||locationList.size() <= 0)
								throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_ST_WAREHOUSE", null) + XipUtil.getMessage(XCUtil.getLang(), "XC_ST_DAO_0002", null));
						}
						if(!deleveredLDto.getAsString("SERIAL_BATCH_NUM").isEmpty()){//存在批次
							Dto dtoInfo = new BaseDto();
							Dto locationInventoryDto = new BaseDto();
							Dto whBSerDto = new BaseDto();
							ArrayList updateLocationInventoryList_02 = new ArrayList();//批次汇总表更新
							ArrayList updateWhBSerList = new ArrayList();//批次现存量更新
							
							if(locationList == null || locationList.size() <= 0){//没有货位信息，则直接根据行信息更新批次表
								Dto locationInventoryDto_02 = xcStLocationInventoryDao.getLocationInventoryByWMSID(deleveredLDto);
								locationInventoryDto.put("LO_INVENTORY_ID",locationInventoryDto_02.getAsString("LO_INVENTORY_ID"));
								locationInventoryDto.put("QTY",deleveredLDto.getAsInteger("QTY") * -1);
								locationInventoryDto.put("LAST_UPDATE_DATE", createDate);
								locationInventoryDto.put("LAST_UPDATED_BY", userId);
								updateLocationInventoryList_02.add(locationInventoryDto);

								whBSerDto.put("B_SERIAL_ID",deleveredLDto.getAsString("B_SERIAL_ID"));
								whBSerDto.put("DELEVERED_QTY",deleveredLDto.getAsInteger("QTY"));
								whBSerDto.put("LAST_UPDATE_DATE", createDate);
								whBSerDto.put("LAST_UPDATED_BY", userId);
								updateWhBSerList.add(whBSerDto);
							}
							if (updateLocationInventoryList_02 != null && updateLocationInventoryList_02.size() > 0) {//更新批次汇总表
								dtoInfo.put("updateLocationInventoryList", updateLocationInventoryList_02);
								xcStLocationInventoryDao.updateLocationInventory(dtoInfo);
							}
							if (updateWhBSerList != null && updateWhBSerList.size() > 0) {//更新批次/序列号现存量
								dtoInfo.put("updateWhBSerList", updateWhBSerList);
								xcStWhInventoryDAO.updateStWhBSerInfo(dtoInfo);
							}
						}
						for(int j= 0;j < locationList.size();j++){
							Dto dtoInfo = new BaseDto();
							Dto locationInventoryDto = new BaseDto();
							Dto whBSerDto = new BaseDto();
							ArrayList updateLocationInventoryList_01 = new ArrayList();//货位汇总表更新
							ArrayList updateWhBSerList = new ArrayList();//批次现存量更新
							//根据仓库ID，物料ID，货位ID查询货位汇总表是否存在
							deleveredLDto.put("LOCATION_ID",((Dto) locationList.get(j)).getAsString("LOCATION_ID"));
							Dto locationInventoryDto_01 = xcStLocationInventoryDao.getLocationInventoryByWMSID(deleveredLDto);
							locationInventoryDto.put("LO_INVENTORY_ID",locationInventoryDto_01.getAsString("LO_INVENTORY_ID"));
							locationInventoryDto.put("QTY",((Dto) locationList.get(j)).getAsInteger("QTY") * -1);
							locationInventoryDto.put("LAST_UPDATE_DATE", createDate);
							locationInventoryDto.put("LAST_UPDATED_BY", userId);
							updateLocationInventoryList_01.add(locationInventoryDto);
							if(!deleveredLDto.getAsString("SERIAL_BATCH_NUM").isEmpty()){//批次存在
								whBSerDto.put("MATERIAL_ID",deleveredLDto.getAsString("MATERIAL_ID"));
								whBSerDto.put("SERIAL_BATCH_NUM",deleveredLDto.getAsString("SERIAL_BATCH_NUM"));
								whBSerDto.put("B_LOCATION_ID",((Dto) locationList.get(j)).getAsString("RE_B_LOCATION_ID"));
								whBSerDto.put("DELEVERED_QTY",((Dto) locationList.get(j)).getAsInteger("QTY"));
								whBSerDto.put("LAST_UPDATE_DATE", createDate);
								whBSerDto.put("LAST_UPDATED_BY", userId);
								updateWhBSerList.add(whBSerDto);
							}
							ArrayList bLocationUpdateList = new ArrayList();//入库及退货货位表更新数据
							dtoInfo.put("B_LOCATION_ID",((Dto) locationList.get(j)).getAsString("RE_B_LOCATION_ID"));
							dtoInfo.put("DELEVERED_QTY",((Dto) locationList.get(j)).getAsInteger("QTY"));
							dtoInfo.put("LAST_UPDATE_DATE", createDate);
							dtoInfo.put("LAST_UPDATED_BY", userId);
							bLocationUpdateList.add(dtoInfo);
							//记录货位汇总表
							if(updateLocationInventoryList_01 != null && updateLocationInventoryList_01.size() > 0){
								dtoInfo.put("dbType",PlatformUtil.getDbType());
								dtoInfo.put("updateLocationInventoryList",updateLocationInventoryList_01);
								xcStLocationInventoryDao.updateLocationInventory(dtoInfo);
							}
							//更新批次/序列号现存量
							if (updateWhBSerList != null && updateWhBSerList.size() > 0) {
								pDto.put("updateWhBSerList", updateWhBSerList);
								xcStWhInventoryDAO.updateStWhBSerInfoByOtherId(pDto);
							}
							//记录入库入库及退货货位表
							if(bLocationUpdateList != null && bLocationUpdateList.size() > 0){
								dtoInfo.put("dbType",PlatformUtil.getDbType());
								dtoInfo.put("list",bLocationUpdateList);
								xcStCommonDao.updateXcStWhBLocation_01(dtoInfo);
							}
						}
					}
					else{//回冲出库单
						if(!deleveredLDto.getAsString("SERIAL_BATCH_NUM").isEmpty()){//存在批次
							Dto dtoInfo = new BaseDto();
							Dto locationInventoryDto = new BaseDto();
							Dto whBSerDto = new BaseDto();
							ArrayList insertLocationInventoryList_02 = new ArrayList();//货位汇总表插入
							ArrayList updateLocationInventoryList_02 = new ArrayList();//批次汇总表更新
							ArrayList updateWhBSerList = new ArrayList();//批次现存量更新
							
							whBSerDto.put("B_SERIAL_ID",deleveredLDto.getAsString("B_SERIAL_ID"));
							whBSerDto.put("DELEVERED_QTY",deleveredLDto.getAsInteger("QTY"));
							whBSerDto.put("LAST_UPDATE_DATE", createDate);
							whBSerDto.put("LAST_UPDATED_BY", userId);
							updateWhBSerList.add(whBSerDto);

							Dto locationInventoryDto_02 = xcStLocationInventoryDao.getLocationInventoryByWMSID(deleveredLDto);
							if(locationInventoryDto_02 == null || "".equals(locationInventoryDto_02)){
								locationInventoryDto.put("LO_INVENTORY_ID",java.util.UUID.randomUUID().toString());
								locationInventoryDto.put("ORG_ID",deleveredLDto.getAsString("ORG_ID"));
								locationInventoryDto.put("LEDGER_ID",deleveredLDto.getAsString("LEDGER_ID"));
								locationInventoryDto.put("WAREHOUSE_ID",deleveredLDto.getAsString("WAREHOUSE_ID"));
								locationInventoryDto.put("MATERIAL_ID",deleveredLDto.getAsString("MATERIAL_ID"));
								locationInventoryDto.put("LOCATION_ID",deleveredLDto.getAsString("LOCATION_ID"));
								locationInventoryDto.put("SERIAL_BATCH_NUM",deleveredLDto.getAsString("SERIAL_BATCH_NUM"));
								locationInventoryDto.put("QTY",deleveredLDto.getAsInteger("QTY") * -1);
								locationInventoryDto.put("VALUATION_MODEL",warehouseDto.getAsString("VALUATION_MODEL"));
								locationInventoryDto.put("CREATION_DATE", createDate);
								locationInventoryDto.put("CREATED_BY", userId);
								locationInventoryDto.put("LAST_UPDATE_DATE", createDate);
								locationInventoryDto.put("LAST_UPDATED_BY", userId);
								insertLocationInventoryList_02.add(locationInventoryDto);
							}
							else{
								locationInventoryDto.put("LO_INVENTORY_ID",locationInventoryDto_02.getAsString("LO_INVENTORY_ID"));
								locationInventoryDto.put("QTY",deleveredLDto.getAsInteger("QTY") * -1);
								locationInventoryDto.put("LAST_UPDATE_DATE", createDate);
								locationInventoryDto.put("LAST_UPDATED_BY", userId);
								updateLocationInventoryList_02.add(locationInventoryDto);
							}
							//插入批次汇总表
							if (insertLocationInventoryList_02 != null && insertLocationInventoryList_02.size() > 0) {
								dtoInfo.put("insertLocationInventoryList", insertLocationInventoryList_02);
								xcStLocationInventoryDao.insertLocationInventory(dtoInfo);
							}
							//更新批次汇总表
							if (updateLocationInventoryList_02 != null && updateLocationInventoryList_02.size() > 0) {
								dtoInfo.put("updateLocationInventoryList", updateLocationInventoryList_02);
								xcStLocationInventoryDao.updateLocationInventory(dtoInfo);
							}
							//更新批次/序列号现存量
							if (updateWhBSerList != null && updateWhBSerList.size() > 0) {
								dtoInfo.put("updateWhBSerList", updateWhBSerList);
								xcStWhInventoryDAO.updateStWhBSerInfo(dtoInfo);
							}
						}
						else{
							if(!deleveredLDto.getAsString("LOCATION_ID").isEmpty()){//存在货位
								Dto dtoInfo = new BaseDto();
								Dto locationInventoryDto = new BaseDto();
								ArrayList insertLocationInventoryList_01 = new ArrayList();//货位汇总表插入
								ArrayList updateLocationInventoryList_01 = new ArrayList();//货位汇总表更新

								//根据仓库ID，物料ID，货位ID查询货位汇总表是否存在
								Dto locationInventoryDto_01 = xcStLocationInventoryDao.getLocationInventoryByWMSID(deleveredLDto);
								if(locationInventoryDto_01 == null || "".equals(locationInventoryDto_01)){
									locationInventoryDto.put("LO_INVENTORY_ID",java.util.UUID.randomUUID().toString());
									locationInventoryDto.put("ORG_ID",deleveredLDto.getAsString("ORG_ID"));
									locationInventoryDto.put("LEDGER_ID",deleveredLDto.getAsString("LEDGER_ID"));
									locationInventoryDto.put("WAREHOUSE_ID",deleveredLDto.getAsString("WAREHOUSE_ID"));
									locationInventoryDto.put("MATERIAL_ID",deleveredLDto.getAsString("MATERIAL_ID"));
									locationInventoryDto.put("LOCATION_ID",deleveredLDto.getAsString("LOCATION_ID"));
									locationInventoryDto.put("SERIAL_BATCH_NUM",deleveredLDto.getAsString("SERIAL_BATCH_NUM"));
									locationInventoryDto.put("QTY",deleveredLDto.getAsInteger("QTY") * -1);
									locationInventoryDto.put("VALUATION_MODEL",warehouseDto.getAsString("VALUATION_MODEL"));
									locationInventoryDto.put("CREATION_DATE", createDate);
									locationInventoryDto.put("CREATED_BY", userId);
									locationInventoryDto.put("LAST_UPDATE_DATE", createDate);
									locationInventoryDto.put("LAST_UPDATED_BY", userId);
									insertLocationInventoryList_01.add(locationInventoryDto);
								}
								else{
									locationInventoryDto.put("LO_INVENTORY_ID",locationInventoryDto_01.getAsString("LO_INVENTORY_ID"));
									locationInventoryDto.put("QTY",deleveredLDto.getAsInteger("QTY") * -1);
									locationInventoryDto.put("LAST_UPDATE_DATE", createDate);
									locationInventoryDto.put("LAST_UPDATED_BY", userId);
									updateLocationInventoryList_01.add(locationInventoryDto);
								}
								//插入货位汇总表
								if (insertLocationInventoryList_01 != null && insertLocationInventoryList_01.size() > 0) {
									dtoInfo.put("insertLocationInventoryList", insertLocationInventoryList_01);
									xcStLocationInventoryDao.insertLocationInventory(dtoInfo);
								}
								//记录货位汇总表
								if(updateLocationInventoryList_01 != null && updateLocationInventoryList_01.size() > 0){
									dtoInfo.put("updateLocationInventoryList",updateLocationInventoryList_01);
									xcStLocationInventoryDao.updateLocationInventory(dtoInfo);
								}
							}
						}
					}
					//会计平台
					VInterface vInterface = new VInterface();
					vInterface.setvInterfaceId(UUID.randomUUID().toString());
					vInterface.setLedgerId(deleveredHDto.getAsString("LEDGER_ID"));
					vInterface.setPeriodCode(periodDto.getAsString("ST_PERIODS"));
					vInterface.setTransCode("CKD");
					vInterface.setDeptId(deleveredHDto.getAsString("DEPT_ID"));
					vInterface.setProjectId(deleveredHDto.getAsString("PROJECT_ID"));
					HashMap<String, String> segments = new HashMap<String, String>();
					if (deleveredHDto.getAsString("DEPT_ID") != null && !"".equals(deleveredHDto.getAsString("DEPT_ID"))) {
						segments.put(XConstants.XIP_PUB_DETPS, deleveredHDto.getAsString("DEPT_ID"));
					}
					if (deleveredHDto.getAsString("PROJECT_ID") != null && !"".equals(deleveredHDto.getAsString("PROJECT_ID"))) {
						segments.put(XConstants.XC_PM_PROJECTS, deleveredHDto.getAsString("PROJECT_ID"));
					}
					deleveredHDto.put("MATERIAL_ID", deleveredLDto.getAsString("MATERIAL_ID"));
					Dto drAcctDto = xcStWhInventoryDAO.getAccIdByBusId(deleveredHDto);
					Dto crAcctDto = xcStWhInventoryDAO.getAccIdByPurTypeId(deleveredHDto);
					if(!"FJZK".equals(warehouseDto.getAsString("VALUATION_MODEL"))){
						if (drAcctDto == null || "".equals(drAcctDto.getAsString("ACC_ID"))||drAcctDto.getAsString("ACC_ID") == null) {
							throw new Exception("借方科目不存在！");
						}
						if (crAcctDto == null || "".equals(crAcctDto.getAsString("AP_ACC_ID_INV"))||crAcctDto.getAsString("AP_ACC_ID_INV")== null) {
							throw new Exception("贷方科目不存在！");
						}
					}
					// 查询科目组id
					if (crAcctDto.getAsString("AP_ACC_ID_INV") != null) {
						JSONObject crKmObject = voucherHandlerService.handlerCCID(deleveredHDto.getAsString("LEDGER_ID"), crAcctDto.getAsString("AP_ACC_ID_INV"), segments);
						JSONObject drKmObject = voucherHandlerService.handlerCCID(deleveredHDto.getAsString("LEDGER_ID"), drAcctDto.getAsString("ACC_ID"), segments);
						if (drKmObject != null) {
							vInterface.setDrCcid(drKmObject.getString("ccid"));
						}
						if (crKmObject != null) {
							vInterface.setCrCcid(crKmObject.getString("ccid"));
						}
					}
					vInterface.setCrAcct(xCGLCommonDAO.getAccountById(crAcctDto.getAsString("AP_ACC_ID_INV")).getAccCode());
					vInterface.setDrAcct(xCGLCommonDAO.getAccountById(drAcctDto.getAsString("ACC_ID")).getAccCode());
					vInterface.setAmt(Double.valueOf(deleveredLDto.getAsInteger("AMOUNT")));
					vInterface.setSummary(deleveredHDto.getAsString("DOC_CAT_CODE") + "/" + deleveredHDto.getAsString("DELEVERED_H_CODE") + "/" + deleveredHDto.getAsString("DELEVERED_H_NAME"));
					vInterface.setBusNo(deleveredHDto.getAsString("DELEVERED_H_CODE"));
					vInterface.setSrcHid(deleveredHDto.getAsString("DELEVERED_H_ID"));
					vInterface.setSrcLid(deleveredLDto.getAsString("DELEVERED_L_ID"));
					vInterface.setCreatedBy(userId);
					vInterface.setCreationDate(createDate);
					vInterface.setLastUpdateBy(userId);
					vInterface.setLastUpdateDate(createDate);
					vInterfaceDao.insertVInterface(vInterface);
				}
			}
			pDto.put("list", entryLUpdateList);
			if (entryLUpdateList != null && entryLUpdateList.size() > 0) {
				pDto.put("updateList", entryLUpdateList);
				xcStWhInventoryDAO.updateWhInventoryList(pDto);
			}
			xcStWhInventoryDAO.saveTransList(pDto);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void transshipment(Dto pDto) throws Exception {

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void costChange(Dto pDto) throws Exception {
		//获取调整单信息
		Dto costChangeHDto = xcStCostChangeDao.selectXcStCostChangeH(pDto.getAsString("COST_CHANGE_H_ID"));
		//获取调整单行信息
		List<Dto> costChangeLList = xcStCostChangeDao.selectXcStCostChangeLByHId(pDto.getAsString("COST_CHANGE_H_ID"));
		//获取当前打开的库存区间
		Dto periodDto = xcStWhInventoryDAO.getStLdPeriodsByOpen(costChangeHDto);
		String userId = CurrentUserUtil.getCurrentUserId();// 当前登录人id
		Date createDate = CurrentUserUtil.getCurrentDate();// 当前创建日期
		ArrayList transInsertList = new ArrayList();
		ArrayList inventoryList = new ArrayList();

		if (costChangeHDto.getAsTimestamp("BIZ_DATE").before(periodDto.getAsTimestamp("START_DATE")) ||
				periodDto.getAsTimestamp("END_DATE").before(costChangeHDto.getAsTimestamp("BIZ_DATE"))) {
			throw new Exception("业务日期不在库存区间内！");
		}
		else{
			for (int i = 0;i < costChangeLList.size(); i++) {
				Dto costChangeLDto = (Dto) costChangeLList.get(i);
				Dto inventoryUpdataDto = new BaseDto();
				costChangeLDto.put("WAREHOUSE_ID",costChangeHDto.getAsString("WAREHOUSE_ID"));
				Dto inventoryDto = xcStWhInventoryDAO.getStWhInventoryByEntryId(costChangeLDto);
				inventoryUpdataDto.put("INVENTORY_ID",inventoryDto.getAsString("INVENTORY_ID"));
				inventoryUpdataDto.put("PRICE",costChangeLDto.getAsBigDecimal("NEWEST_PRICE"));
				inventoryUpdataDto.put("AMOUNT",costChangeLDto.getAsBigDecimal("NEWEST_AMOUNT"));
				
				costChangeLDto.put("TRANS_ID", java.util.UUID.randomUUID().toString());
				costChangeLDto.put("BUSINESS_H_ID",costChangeLDto.getAsString("COST_CHANGE_H_ID"));
				costChangeLDto.put("BUSINESS_L_ID",costChangeLDto.getAsString("COST_CHANGE_L_ID"));
				costChangeLDto.put("BUSINESS_TYPE_ID",costChangeHDto.getAsString("BUSINESS_TYPE_ID"));
				costChangeLDto.put("DOC_CAT_CODE",costChangeHDto.getAsString("DOC_CAT_CODE"));
				costChangeLDto.put("ORIGINAL_QTY",costChangeLDto.getAsBigDecimal("QTY"));
				costChangeLDto.put("ORIGINAL_PRICE",costChangeLDto.getAsBigDecimal("PRICE"));
				costChangeLDto.put("ORIGINAL_AMOUNT",costChangeLDto.getAsBigDecimal("AMOUNT"));
				costChangeLDto.put("NEWEST_QTY",0);
				costChangeLDto.put("NEWEST_PRICE",0);
				costChangeLDto.put("NEWEST_AMOUNT",0);
				costChangeLDto.put("COEFFICIENT","-1");
				costChangeLDto.put("PERIOD_CODE",periodDto.getAsString("ST_PERIODS"));
				costChangeLDto.put("STATUS","否");
				costChangeLDto.put("SERIALS","后续添加");
				costChangeLDto.put("CREATED_BY",userId);
				costChangeLDto.put("CREATION_DATE",createDate);
				costChangeLDto.put("LAST_UPDATED_BY",userId);
				costChangeLDto.put("LAST_UPDATE_DATE",createDate);
				transInsertList.add(costChangeLDto);
				inventoryList.add(inventoryUpdataDto);
			}
			pDto.put("list",costChangeLList);
			pDto.put("inventoryList",inventoryList);
			if(transInsertList != null && transInsertList.size() > 0){
				xcStWhInventoryDAO.inventory_update_costChange(pDto);
				xcStWhInventoryDAO.saveTransList(pDto);
			}
		}
	}
}