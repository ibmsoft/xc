package com.xzsoft.xc.st.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xzsoft.xc.st.dao.XcStCommonDao;
import com.xzsoft.xc.st.dao.XcStCostChangeDao;
import com.xzsoft.xc.st.dao.XcStLocationAdjustDao;
import com.xzsoft.xc.st.dao.XcStLocationInventoryDao;
import com.xzsoft.xc.st.dao.XcStTransshipmentDao;
import com.xzsoft.xc.st.dao.XcStWhDeleveredDao;
import com.xzsoft.xc.st.dao.XcStWhEntryDao;
import com.xzsoft.xc.st.modal.BaseDto;
import com.xzsoft.xc.st.modal.Dto;
import com.xzsoft.xc.st.modal.XcStCommonBean;
import com.xzsoft.xc.st.service.XcStCommonService;
import com.xzsoft.xc.st.service.XcStDocsService;
import com.xzsoft.xc.st.service.XcStWhInventoryService;
import com.xzsoft.xc.st.util.XcStConstans;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xip.platform.util.PlatformUtil;
import com.xzsoft.xip.platform.util.XipUtil;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;
/**
 * 
  * @ClassName XcStCommonServiceImpl
  * @Description 库存模块公共处理的service接口实现类
  * @author RenJianJian
  * @date 2016年12月20日 下午1:15:11
 */
@Service("xcStCommonService")
public class XcStCommonServiceImpl implements XcStCommonService {
	public static final Logger log = Logger.getLogger(XcStCommonServiceImpl.class);
	@Resource
	private XcStCommonDao xcStCommonDao;
	@Resource
	private XcStWhInventoryService xcStWhInventoryService;
	@Resource
	private XcStLocationInventoryDao xcStLocationInventoryDao;
	@Resource
	private XcStWhEntryDao xcStWhEntryDao;
	@Resource
	private XcStWhDeleveredDao xcStWhDeleveredDao;
	@Resource
	private XcStCostChangeDao xcStCostChangeDao;
	@Resource
	private XcStTransshipmentDao xcStTransshipmentDao;
	@Resource
	private XcStLocationAdjustDao xcStLocationAdjustDao;
	@Resource
	private XcStDocsService xcStDocsService;
	
	/*
	 * 
	  * <p>Title updateXcStDocStatus</p>
	  * <p>Description 更新单据的提交状态</p>
	  * @param stDocId
	  * @param docClassCode
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.st.service.XcStCommonService#updateXcStDocStatus(java.lang.String, java.lang.String)
	 */
	@SuppressWarnings({ "unchecked" })
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public JSONObject updateXcStDocStatus(String stDocId, String docClassCode) throws Exception {
		JSONObject json = new JSONObject();
		List<XcStCommonBean> xcStCommonBeanList = new ArrayList<XcStCommonBean>();
		Dto pDto = new BaseDto();
		Dto pDtoInfo = new BaseDto();
		try {
			String tableName = null;	//表名
			String priKey = null;		//主键
			if(XcStConstans.RKD.equals(docClassCode)){	//操作的是入库单
				tableName = "xc_st_wh_entry_h";
				priKey = "ENTRY_H_ID";
				pDtoInfo = xcStWhEntryDao.selectXcStWhEntryH(stDocId);
				this.entrySubmitingUpdateDocInfo(stDocId);//入库单提交的时候更新被引用的单据信息

				pDto.put(priKey,stDocId);
				xcStWhInventoryService.entry(pDto);
			}
			else if(XcStConstans.CKD.equals(docClassCode)){			//操作的是出库单
				tableName = "xc_st_wh_delevered_h";
				priKey = "DELEVERED_H_ID";
				pDtoInfo = xcStWhDeleveredDao.selectXcStWhDeleveredH(stDocId);
				this.deleverdSubmitingUpdateDocInfo(stDocId);//出库单提交的时候更新被引用的单据信息
				
				pDto.put(priKey,stDocId);
				xcStWhInventoryService.delevered(pDto);
			}
			else if(XcStConstans.CBTZD.equals(docClassCode)){		//操作的是成本调整单
				tableName = "xc_st_cost_change_h";
				priKey = "COST_CHANGE_H_ID";
				pDtoInfo = xcStCostChangeDao.selectXcStCostChangeH(stDocId);
				
				pDto.put(priKey,stDocId);
				xcStWhInventoryService.costChange(pDto);
			}
			else if(XcStConstans.WZDBD.equals(docClassCode)){		//操作的是物资调拨单
				tableName = "xc_st_transshipment_h";
				priKey = "TR_H_ID";
				pDtoInfo = xcStTransshipmentDao.selectXcStTransshipmentH(stDocId);
				
				this.transshipmentSubmitingUpdateDocInfo(stDocId);//物资调拨单提交的时候更新被引用的单据信息
			}
			else if(XcStConstans.HWTZD.equals(docClassCode)){		//操作的是货位调整单
				tableName = "xc_st_location_adjust_h";
				priKey = "LO_ADJUST_H_ID";
				pDtoInfo = xcStLocationAdjustDao.selectXcStLocationAdjustH(stDocId);
				
				this.locationAdjSubmitingUpdateDocInfo(stDocId);//货位调整单提交的时候更新被引用的单据信息
			}
			Date BIZ_DATE = pDtoInfo.getAsDate("BIZ_DATE");
			String LEDGER_ID = pDtoInfo.getAsString("LEDGER_ID");
			//根据业务日期和账簿得到上一个会计期的状态，日常单据提交直接进入记账状态，上月会计期处于冻结状态则提交时进入已提交状态
			HashMap<String, String> map = xcStCommonDao.selectUpPeriodStatusByLedger(LEDGER_ID, BIZ_DATE);
			String PERIODS_STATUS = (map == null) ? null : map.get("PERIODS_STATUS");
			XcStCommonBean xcStCommonBean = new XcStCommonBean();
			xcStCommonBean.setTableName(tableName);
			xcStCommonBean.setPriKey(priKey);
			xcStCommonBean.setPriKeyId(stDocId);
			if("3".equals(PERIODS_STATUS))//冻结
				xcStCommonBean.setStatus("2");
			else//非冻结
				xcStCommonBean.setStatus("3");
			xcStCommonBeanList.add(xcStCommonBean);
			if(xcStCommonBeanList != null && xcStCommonBeanList.size() > 0){
				xcStCommonDao.updateXcStDocStatus(xcStCommonBeanList);
			}
		} catch (Exception e) {
			json.put("msg",e.getMessage());
			log.error(e.getMessage());
			throw e;
		}
		return json;
	}

	/*
	 * 
	  * <p>Title getXcStProcAndForms</p>
	  * <p>Description 得到库存流程信息</p>
	  * @param stCatCode
	  * @param ledgerId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.st.service.XcStCommonService#getXcStProcAndForms(java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public JSONObject getXcStProcAndForms(String stCatCode,String ledgerId) throws Exception {
		JSONObject jo = new JSONObject();
		try {
			//查询账簿级中的流程相关信息
			HashMap<String,String> xcStLdProMap = xcStCommonDao.getXcStLdProcessInfo(stCatCode,ledgerId);
			
			String processId = xcStLdProMap.get("PROCESS_ID");
			String processCode = xcStLdProMap.get("PROCESS_CODE");
			String ruleCode = xcStLdProMap.get("RULE_CODE");
			
			jo.put("PROCESS_ID",processId);
			jo.put("PROCESS_CODE",processCode);
			jo.put("RULE_CODE",ruleCode);
			
			//查询全局中的流程相关信息
			HashMap<String,String> xcStProMap = xcStCommonDao.getXcStProcessInfo(stCatCode);
			
			if(processId == null || "".equals(processId)){
				String gbProcId = xcStProMap.get("PROCESS_ID");
				String gbProcCode = xcStProMap.get("PROCESS_CODE");
				if(gbProcId == null || "".equals(gbProcId)){
					//throw new Exception("单据类型：" + xcStProMap.get("AR_CAT_NAME") + "未设置审批流程，请在单据类型设置功能处设置！");
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC_CAT", null)+xcStProMap.get("ST_CAT_NAME")+XipUtil.getMessage(XCUtil.getLang(), "XC_EX_PROCESS_IS_NOT_NULL", null));
				}
				else{
					jo.put("PROCESS_ID",gbProcId);
					jo.put("PROCESS_CODE",gbProcCode);
				}
			}
			jo.put("ST_CAT_CODE",xcStProMap.get("ST_CAT_CODE"));		//类型编码
			jo.put("ST_CAT_NAME",xcStProMap.get("ST_CAT_NAME"));		//类型名称
			jo.put("PC_W_FORM_ID",xcStProMap.get("PC_W_FORM_ID"));		//电脑端填报表单ID
			jo.put("PC_W_FORM_URL",xcStProMap.get("PC_W_FORM_URL")); 	//电脑端填报表单地址
			jo.put("PC_A_FORM_ID",xcStProMap.get("PC_A_FORM_ID"));  	//电脑端审批表单ID
			jo.put("PC_A_FORM_URL",xcStProMap.get("PC_A_FORM_URL"));	//电脑端审批表单地址
			jo.put("PC_P_FORM_ID",xcStProMap.get("PC_P_FORM_ID"));		//电脑端打印表单ID
			jo.put("PC_P_FORM_URL",xcStProMap.get("PC_P_FORM_URL"));	//电脑端打印表单地址
			jo.put("M_W_FORM_ID",xcStProMap.get("M_W_FORM_ID"));		//移动端填报表单ID
			jo.put("M_W_FORM_URL",xcStProMap.get("M_W_FORM_URL"));		//移动端填报表单地址
			jo.put("M_A_FORM_ID",xcStProMap.get("M_A_FORM_ID"));		//移动端审批表单ID
			jo.put("M_A_FORM_URL",xcStProMap.get("M_A_FORM_URL"));		//移动端审批表单地址
			jo.put("ORG_ID",xcStLdProMap.get("ORG_ID"));				//组织ID
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return jo;
	}
	/*
	 * 
	  * <p>Title getXcStAttCatCode</p>
	  * <p>Description 获取附件编码</p>
	  * @param stCatCode
	  * @param ledgerId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.st.service.XcStCommonService#getXcStAttCatCode(java.lang.String, java.lang.String)
	 */
	@Override
	public String getXcStAttCatCode(String stCatCode, String ledgerId) throws Exception {
		String attCatCode = null;
		try {//查询账簿级单据类型中的流程相关信息
			HashMap<String,String> xcStLdProMap = xcStCommonDao.getXcStLdProcessInfo(stCatCode,ledgerId);
			String stCatName = xcStLdProMap.get("ST_CAT_NAME");
			attCatCode = xcStLdProMap.get("ATT_CAT_CODE");
			if(attCatCode == null || "".equals(attCatCode)){
				//未设置附件分类，请在【单据类型设置】功能处设置！
				throw new Exception(stCatName + XipUtil.getMessage(XCUtil.getLang(), "XC_AP_ENCLOSURE_EXCEPTION", null));
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return attCatCode;
	}
	/**
	 * 
	  * @Title entrySubmitingUpdateDocInfo 方法名
	  * @Description 入库单提交的时候更新被引用的单据信息
	  * @param stDocId
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void entrySubmitingUpdateDocInfo(String stDocId) throws Exception{
		try {
			//更新入库单蓝单的list
			List<XcStCommonBean> updateEntryLDLists = new ArrayList<XcStCommonBean>();
			//更新反冲入库单行的list
			List<XcStCommonBean> updateEntryLLists = new ArrayList<XcStCommonBean>();
			//更新采购订单行的list
			List<XcStCommonBean> updateOrderLLists = new ArrayList<XcStCommonBean>();
			//通过入库单ID得到主信息
			Dto xcStWhEntryH = xcStWhEntryDao.selectXcStWhEntryH(stDocId);
			if(XcStConstans.QTRK_H.equals(xcStWhEntryH.getAsString("DOC_CAT_CODE")) || XcStConstans.CGTH.equals(xcStWhEntryH.getAsString("DOC_CAT_CODE"))){
				String judReturn = xcStCommonDao.judgmentXcStWhEntryHAmount(xcStWhEntryH.getAsString("RE_WH_ENTRY_H_ID"),xcStWhEntryH.getAsBigDecimal("AMOUNT").doubleValue());
				if(judReturn == null || "".equals(judReturn)){
					XcStCommonBean xcStCommonBean = new XcStCommonBean();
					xcStCommonBean.setAMOUNT(xcStWhEntryH.getAsBigDecimal("AMOUNT").doubleValue());
					xcStCommonBean.setPriKeyId(xcStWhEntryH.getAsString("RE_WH_ENTRY_H_ID"));
					updateEntryLDLists.add(xcStCommonBean);
				}
				else
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_ST_SERVICE_0002", null));
			}
			//通过入库单ID得到行信息
			List<Dto> xcStWhEntryLList = xcStWhEntryDao.selectXcStWhEntryLByHId(stDocId);
			for (Dto dto : xcStWhEntryLList) {
				//回写入库单行中的已出库数量
				if(XcStConstans.QTRK_H.equals(xcStWhEntryH.getAsString("DOC_CAT_CODE")) || XcStConstans.CGTH.equals(xcStWhEntryH.getAsString("DOC_CAT_CODE"))){
					XcStCommonBean xcStCommonBean = new XcStCommonBean();
					xcStCommonBean.setPriKeyId(dto.getAsString("RE_ENTRY_L_ID"));
					xcStCommonBean.setQTY(dto.getAsBigDecimal("QTY").doubleValue() * -1);
					updateEntryLLists.add(xcStCommonBean);
				}
				//回写采购订单
				if(XcStConstans.CGRK.equals(xcStWhEntryH.getAsString("DOC_CAT_CODE")) || XcStConstans.CGTH.equals(xcStWhEntryH.getAsString("DOC_CAT_CODE"))){
					XcStCommonBean xcStCommonBean = new XcStCommonBean();
					xcStCommonBean.setPriKeyId(dto.getAsString("ORDER_L_ID"));
					xcStCommonBean.setQTY(dto.getAsBigDecimal("QTY").doubleValue());
					updateOrderLLists.add(xcStCommonBean);
				}
			}
			//更新入库单主表金额
			if(updateEntryLDLists != null && updateEntryLDLists.size() > 0)
				xcStCommonDao.updateXcStWhEntryHAmount(updateEntryLDLists);
			//更新入库单行表已出库数量
			if(updateEntryLLists != null && updateEntryLLists.size() > 0)
				xcStCommonDao.updateXcStWhEntryLAmount(updateEntryLLists);
			//更新采购订单行表金额
			if(updateOrderLLists != null && updateOrderLLists.size() > 0)
				xcStCommonDao.updatePoOrderLInfo(updateOrderLLists);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
	/**
	 * 
	  * @Title deleverdSubmitingUpdateDocInfo 方法名
	  * @Description 出库单提交的时候更新被引用的单据信息
	  * @param stDocId
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void deleverdSubmitingUpdateDocInfo(String stDocId) throws Exception{
		try {
			//更新出库单蓝单的list
			List<XcStCommonBean> updateDeleveredLDLists = new ArrayList<XcStCommonBean>();
			//回写领用申请的list
			List<XcStCommonBean> updateUseApplyLists = new ArrayList<XcStCommonBean>();
			//回写入库单行表已出库数量的list
			List<XcStCommonBean> updateEntryLLists = new ArrayList<XcStCommonBean>();
			//回写出库单行表已入库数量的list
			List<XcStCommonBean> updateDeleveredLLists = new ArrayList<XcStCommonBean>();
			//通过出库单ID得到主信息
			Dto xcStWhDeleveredH = xcStWhDeleveredDao.selectXcStWhDeleveredH(stDocId);
			//更新出库单蓝（其它出库-红、销售退货）
			if(XcStConstans.QTCK_H.equals(xcStWhDeleveredH.getAsString("DOC_CAT_CODE")) || XcStConstans.XSTK.equals(xcStWhDeleveredH.getAsString("DOC_CAT_CODE")) || XcStConstans.LYTK.equals(xcStWhDeleveredH.getAsString("DOC_CAT_CODE"))){
				String judReturn = xcStCommonDao.judgmentXcStWhDeleveredHAmount(xcStWhDeleveredH.getAsString("RE_DELEVERED_H_ID"),xcStWhDeleveredH.getAsBigDecimal("AMOUNT").doubleValue());
				if(judReturn == null || "".equals(judReturn)){
					XcStCommonBean xcStCommonBean = new XcStCommonBean();
					xcStCommonBean.setAMOUNT(xcStWhDeleveredH.getAsBigDecimal("AMOUNT").doubleValue());
					xcStCommonBean.setPriKeyId(xcStWhDeleveredH.getAsString("RE_DELEVERED_H_ID"));
					updateDeleveredLDLists.add(xcStCommonBean);
				}
				else
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_ST_SERVICE_0002", null));
			}
			//通过出库单ID得到行信息
			List<Dto> xcStWhDeleveredLList = xcStWhDeleveredDao.selectXcStWhDeleveredLByHId(stDocId);
			for (Dto dto : xcStWhDeleveredLList) {
				//回写出库单行中的已入库数量
				if(!dto.getAsString("RE_DELEVERED_L_ID").isEmpty() && !dto.getAsString("RE_DELEVERED_H_ID").isEmpty()){
					XcStCommonBean xcStCommonBean = new XcStCommonBean();
					xcStCommonBean.setPriKeyId(dto.getAsString("RE_DELEVERED_L_ID"));
					xcStCommonBean.setQTY(dto.getAsBigDecimal("QTY").doubleValue() * -1);
					updateDeleveredLLists.add(xcStCommonBean);
				}
				//回写入库单行中的已出库数量
				if(!dto.getAsString("ENTRY_L_ID").isEmpty() && !dto.getAsString("ENTRY_H_ID").isEmpty()){
					XcStCommonBean xcStCommonBean = new XcStCommonBean();
					xcStCommonBean.setPriKeyId(dto.getAsString("ENTRY_L_ID"));
					xcStCommonBean.setQTY(dto.getAsBigDecimal("QTY").doubleValue());
					updateEntryLLists.add(xcStCommonBean);
				}
				//回写领用申请（领用）
				if(XcStConstans.LYCK.equals(xcStWhDeleveredH.getAsString("DOC_CAT_CODE")) || XcStConstans.LYTK.equals(xcStWhDeleveredH.getAsString("DOC_CAT_CODE"))){
					if(xcStWhDeleveredH.getAsString("NOTICE_H_ID") != null && !"".equals(xcStWhDeleveredH.getAsString("NOTICE_H_ID"))){
						XcStCommonBean xcStCommonBean = new XcStCommonBean();
						xcStCommonBean.setPriKeyId(dto.getAsString("NOTICE_L_ID"));
						xcStCommonBean.setQTY(dto.getAsBigDecimal("QTY").doubleValue());
						updateUseApplyLists.add(xcStCommonBean);
					}
				}
			}
			//更新出库单主表金额
			if(updateDeleveredLDLists != null && updateDeleveredLDLists.size() > 0)
				xcStCommonDao.updateXcStWhDeleveredHAmount(updateDeleveredLDLists);
			//更新出库单行表已入库数量
			if(updateDeleveredLLists != null && updateDeleveredLLists.size() > 0)
				xcStCommonDao.updateXcStWhDeleveredLAmount(updateDeleveredLLists);
			//更新入库单行表已出库数量
			if(updateEntryLLists != null && updateEntryLLists.size() > 0)
				xcStCommonDao.updateXcStWhEntryLAmount(updateEntryLLists);
			//更新领用申请单行表出库数量
			if(updateUseApplyLists != null && updateUseApplyLists.size() > 0)
				xcStCommonDao.updateXcStUseApplyLAmount(updateUseApplyLists);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
	/**
	 * 
	  * @Title transshipmentSubmitingUpdateDocInfo 方法名
	  * @Description 物资调拨单提交的时候更新被引用的单据信息
	  * @param stDocId
	  * @throws Exception
	  * @return void 返回类型
	 */
	@SuppressWarnings("unchecked")
	public void transshipmentSubmitingUpdateDocInfo(String stDocId) throws Exception{
		try {
			//根据调拨单ID得到调拨单信息
			Dto tDto = xcStTransshipmentDao.selectXcStTransshipmentH(stDocId);
			List<Dto> dtoList = xcStTransshipmentDao.selectXcStTransshipmentLByHId(stDocId);
			//物资调拨单调出提交的时候需要生成出库单
			tDto.put("IS_CREATE","Y");
			tDto.put("DELEVERED_H_ID",java.util.UUID.randomUUID().toString());
			tDto.put("DELEVERED_H_NAME",tDto.getAsString("TR_H_NAME"));
			tDto.put("docClassCode","CKD");
			tDto.put("DOC_CAT_CODE","QTCK");
			tDto.put("WAREHOUSE_ID",tDto.getAsString("OUT_WH_ID"));
			tDto.put("WH_KEEPER_ID",tDto.getAsString("OUT_WH_KEEPER_ID"));
			for (int i = 0;i < dtoList.size();i++) {
				Dto dto = dtoList.get(i);
				dto.put("DELEVERED_H_ID",tDto.getAsString("DELEVERED_H_ID"));
				dto.put("DELEVERED_L_ID",java.util.UUID.randomUUID().toString());
				dto.put("BUSINESS_TYPE_ID",tDto.getAsString("BUSINESS_TYPE_ID"));
				dto.put("DOC_CAT_CODE",tDto.getAsString("DOC_CAT_CODE"));
				dto.put("DELEVERED_L_CODE",i);
				dto.put("LOCATION_ID",dtoList.get(i).getAsString("OUT_LOCATION_ID"));
			}
			tDto.put("insertDtlList",dtoList);
			xcStDocsService.saveOrUpdateXcStDocs(tDto);
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 
	  * @Title locationAdjSubmitingUpdateDocInfo 方法名
	  * @Description 货位调整单提交的时候更新被引用的单据信息
	  * @param stDocId
	  * @throws Exception
	  * @return void 返回类型
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void locationAdjSubmitingUpdateDocInfo(String stDocId) throws Exception{
		try {
			//往入库及退货货位表更新数据
			ArrayList bLocationUpdateList = new ArrayList();
			Dto dtoBLocationUpdate = new BaseDto();
			//通过主ID查询行信息
			Dto mainDtoInfo = xcStLocationAdjustDao.selectXcStLocationAdjustH(stDocId);
			for (Dto dtlDto : xcStLocationAdjustDao.selectXcStLocationAdjustLByHId(stDocId)) {
				//先更新当前调整的货位现存量
				Dto dtlDtoOld = new BaseDto();
				dtlDtoOld.put("QTY",dtlDto.getAsInteger("ADJUST_QTY") * -1);
				dtlDtoOld.put("MATERIAL_ID",dtlDto.getAsString("MATERIAL_ID"));
				dtlDtoOld.put("WAREHOUSE_ID",mainDtoInfo.getAsString("WAREHOUSE_ID"));
				dtlDtoOld.put("LOCATION_ID",dtlDto.getAsString("LOCATION_ID"));
				dtlDtoOld.put("LAST_UPDATE_DATE",CurrentUserUtil.getCurrentDate());
				dtlDtoOld.put("LAST_UPDATED_BY",CurrentUserUtil.getCurrentUserId());
				bLocationUpdateList.add(dtlDtoOld);
				
				//调整后的货位
				Dto dtlDtoNew = new BaseDto();
				dtlDtoNew.put("MATERIAL_ID",dtlDto.getAsString("MATERIAL_ID"));
				dtlDtoNew.put("WAREHOUSE_ID",mainDtoInfo.getAsString("WAREHOUSE_ID"));
				dtlDtoNew.put("LOCATION_ID",dtlDto.getAsString("NEW_LOCATION_ID"));
				dtlDtoNew.put("SERIAL_BATCH_NUM",dtlDto.getAsString("SERIAL_BATCH_NUM"));
				
				//根据调整的货位查找货位汇总表中是否存在
				ArrayList insertLocationInventoryList_01 = new ArrayList();//货位汇总表插入
				ArrayList updateLocationInventoryList_01 = new ArrayList();//货位汇总表更新
				Dto locationInventoryDto = new BaseDto();
				//根据仓库ID，物料ID，货位ID查询货位汇总表是否存在
				Dto locationInventoryDto_01 = xcStLocationInventoryDao.getLocationInventoryByWMSID(dtlDtoNew);
				if(locationInventoryDto_01 == null || "".equals(locationInventoryDto_01)){
					locationInventoryDto.put("LO_INVENTORY_ID",java.util.UUID.randomUUID().toString());
					locationInventoryDto.put("ORG_ID",dtlDto.getAsString("ORG_ID"));
					locationInventoryDto.put("LEDGER_ID",dtlDto.getAsString("LEDGER_ID"));
					locationInventoryDto.put("WAREHOUSE_ID",mainDtoInfo.getAsString("WAREHOUSE_ID"));
					locationInventoryDto.put("MATERIAL_ID",dtlDto.getAsString("MATERIAL_ID"));
					locationInventoryDto.put("LOCATION_ID",dtlDto.getAsString("NEW_LOCATION_ID"));
					locationInventoryDto.put("SERIAL_BATCH_NUM",dtlDto.getAsString("SERIAL_BATCH_NUM"));
					locationInventoryDto.put("QTY",dtlDto.getAsInteger("ADJUST_QTY"));
					locationInventoryDto.put("VALUATION_MODEL",dtlDto.getAsString("VALUATION_MODEL"));
					locationInventoryDto.put("CREATION_DATE", CurrentUserUtil.getCurrentDate());
					locationInventoryDto.put("CREATED_BY", CurrentUserUtil.getCurrentUserId());
					locationInventoryDto.put("LAST_UPDATE_DATE", CurrentUserUtil.getCurrentDate());
					locationInventoryDto.put("LAST_UPDATED_BY", CurrentUserUtil.getCurrentUserId());
					insertLocationInventoryList_01.add(locationInventoryDto);
				}
				else{
					locationInventoryDto.put("LO_INVENTORY_ID",locationInventoryDto_01.getAsString("LO_INVENTORY_ID"));
					locationInventoryDto.put("QTY",dtlDto.getAsInteger("ADJUST_QTY"));
					locationInventoryDto.put("LAST_UPDATE_DATE", CurrentUserUtil.getCurrentDate());
					locationInventoryDto.put("LAST_UPDATED_BY", CurrentUserUtil.getCurrentUserId());
					updateLocationInventoryList_01.add(locationInventoryDto);
				}
				//插入货位汇总表
				if (insertLocationInventoryList_01 != null && insertLocationInventoryList_01.size() > 0) {
					locationInventoryDto.put("insertLocationInventoryList", insertLocationInventoryList_01);
					xcStLocationInventoryDao.insertLocationInventory(locationInventoryDto);
				}
				//更新货位汇总表
				if(updateLocationInventoryList_01 != null && updateLocationInventoryList_01.size() > 0){
					locationInventoryDto.put("updateLocationInventoryList",updateLocationInventoryList_01);
					xcStLocationInventoryDao.updateLocationInventory(locationInventoryDto);
				}
			}
			if(bLocationUpdateList != null && bLocationUpdateList.size() > 0){
				//记录入库入库及退货货位表
				dtoBLocationUpdate.put("dbType",PlatformUtil.getDbType());
				dtoBLocationUpdate.put("list",bLocationUpdateList);
				xcStCommonDao.updateXcStLocationInventory(dtoBLocationUpdate);
			}
		} catch (Exception e) {
			throw e;
		}
	}
}
