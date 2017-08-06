package com.xzsoft.xc.st.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xzsoft.xc.posost.dao.XcStTransDao;
import com.xzsoft.xc.st.dao.XcStCommonDao;
import com.xzsoft.xc.st.dao.XcStCostChangeDao;
import com.xzsoft.xc.st.dao.XcStLocationAdjustDao;
import com.xzsoft.xc.st.dao.XcStTransshipmentDao;
import com.xzsoft.xc.st.dao.XcStUseApplyDao;
import com.xzsoft.xc.st.dao.XcStWhDeleveredDao;
import com.xzsoft.xc.st.dao.XcStWhEntryDao;
import com.xzsoft.xc.st.dao.XcStWhInventoryDAO;
import com.xzsoft.xc.st.modal.BaseDto;
import com.xzsoft.xc.st.modal.Dto;
import com.xzsoft.xc.st.modal.XcStCommonBean;
import com.xzsoft.xc.st.service.XcStDocsService;
import com.xzsoft.xc.st.service.XcStWhInventoryService;
import com.xzsoft.xc.st.util.XcStConstans;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xc.util.service.RuleService;
import com.xzsoft.xip.platform.util.XipUtil;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;
/**
 * 
  * @ClassName XcStDocsServiceServiceImpl
  * @Description 库存模块业务处理层实现类
  * @author RenJianJian
  * @date 2016年12月2日 上午11:34:20
 */
@Service("xcStDocsService")
public class XcStDocsServiceServiceImpl implements XcStDocsService {
	public static final Logger log = Logger.getLogger(XcStDocsServiceServiceImpl.class);
	@Resource
	private XcStWhEntryDao xcStWhEntryDao;
	@Resource
	private XcStWhDeleveredDao xcStWhDeleveredDao;
	@Resource
	private XcStUseApplyDao xcStUseApplyDao;
	@Resource
	private XcStCostChangeDao xcStCostChangeDao;
	@Resource
	private XcStLocationAdjustDao xcStLocationAdjustDao;
	@Resource
	private XcStTransshipmentDao xcStTransshipmentDao;
	@Resource
	private RuleService ruleService;
	@Resource
	private XcStCommonDao xcStCommonDao;
	@Resource
	private XcStTransDao xcStTransDao;
	@Resource
	private XcStWhInventoryService xcStWhInventoryService;
	@Resource
	private XcStWhInventoryDAO xcStWhInventoryDAO;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	/*
	 * 
	  * <p>Title saveOrUpdateXcStDocs</p>
	  * <p>Description 增加/修改操作</p>
	  * @param pDto
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.st.service.XcStDocsService#saveOrUpdateXcStDocs(com.xzsoft.xc.st.modal.Dto)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public JSONObject saveOrUpdateXcStDocs(Dto pDto) throws Exception {
		JSONObject json = new JSONObject();
		try {
			String docCode = null;
			String userId = CurrentUserUtil.getCurrentUserId();//当前登录人id
			Date createDate = CurrentUserUtil.getCurrentDate();//当前创建日期
			String LEDGER_ID = pDto.getAsString("LEDGER_ID");
			String docClassCode = pDto.getAsString("docClassCode");//单据类型
			pDto.put("LAST_UPDATE_DATE",createDate);
			pDto.put("LAST_UPDATED_BY",userId);
			if("Y".equals(pDto.get("IS_CREATE"))){
				//判断制单人是否属于这个仓库的管理员
				String createrInfo = xcStCommonDao.createrIsOrNotWarehouseKeeper(userId,(pDto.getAsString("WAREHOUSE_ID").isEmpty()) ? pDto.getAsString("OUT_WH_ID") : pDto.getAsString("WAREHOUSE_ID"),LEDGER_ID);
				if(createrInfo == null || "".equals(createrInfo))
					//throw new Exception("当前制单人不是该仓库下的库管员，请重新选择！");
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_ST_SERVICE_0001", null));
				pDto.put("CREATION_DATE",createDate);
				pDto.put("CREATED_BY",userId);
			}
			String year = pDto.getAsString("BIZ_DATE").substring(0,4);								//年
			String month = pDto.getAsString("BIZ_DATE").substring(5,7);								//月
			
			if(XcStConstans.RKD.equals(docClassCode)){				//操作的是入库单
				if("Y".equals(pDto.get("IS_CREATE"))){
					//得到单据编码
					docCode = ruleService.getNextSerialNum("ST_WH_ENTRY", LEDGER_ID, "", year, month, userId);
					pDto.put("ENTRY_H_CODE",docCode);
					xcStWhEntryDao.insertXcStWhEntryH(pDto);
				}
				else{
					docCode = pDto.getAsString("ENTRY_H_CODE");
					xcStWhEntryDao.updateXcStWhEntryH(pDto);
				}
			}
			else if(XcStConstans.CKD.equals(docClassCode)){			//操作的是出库单
				if("Y".equals(pDto.get("IS_CREATE"))){
					//得到单据编码
					docCode = ruleService.getNextSerialNum("ST_WH_DELE", LEDGER_ID, "", year, month, userId);
					pDto.put("DELEVERED_H_CODE",docCode);
					xcStWhDeleveredDao.insertXcStWhDeleveredH(pDto);
				}
				else{
					docCode = pDto.getAsString("DELEVERED_H_CODE");
					xcStWhDeleveredDao.updateXcStWhDeleveredH(pDto);
				}
			}
			else if(XcStConstans.SQD.equals(docClassCode)){		//操作的是领用申请单
				if("Y".equals(pDto.get("IS_CREATE"))){
					//得到单据编码
					docCode = ruleService.getNextSerialNum("ST_USE_APPLY", LEDGER_ID, "", year, month, userId);
					pDto.put("USE_APPLY_H_CODE",docCode);
					xcStUseApplyDao.insertXcStUseApplyH(pDto);
				}
				else{
					docCode = pDto.getAsString("USE_APPLY_H_CODE");
					xcStUseApplyDao.updateXcStUseApplyH(pDto);
				}
			}
			else if(XcStConstans.CBTZD.equals(docClassCode)){		//操作的是成本调整单
				if("Y".equals(pDto.get("IS_CREATE"))){
					//得到单据编码
					docCode = ruleService.getNextSerialNum("ST_COST_CHANGE", LEDGER_ID, "", year, month, userId);
					pDto.put("COST_CHANGE_H_CODE",docCode);
					xcStCostChangeDao.insertXcStCostChangeH(pDto);
				}
				else{
					docCode = pDto.getAsString("COST_CHANGE_H_CODE");
					xcStCostChangeDao.updateXcStCostChangeH(pDto);
				}
			}
			else if(XcStConstans.WZDBD.equals(docClassCode)){		//操作的是物资调拨单
				if("Y".equals(pDto.get("IS_CREATE"))){
					//得到单据编码
					docCode = ruleService.getNextSerialNum("ST_TRANSSHIPMENT", LEDGER_ID, "", year, month, userId);
					pDto.put("TR_H_CODE",docCode);
					xcStTransshipmentDao.insertXcStTransshipmentH(pDto);
				}
				else{
					docCode = pDto.getAsString("TR_H_CODE");
					xcStTransshipmentDao.updateXcStTransshipmentH(pDto);
				}
			}
			else if(XcStConstans.HWTZD.equals(docClassCode)){		//操作的是货位调整单
				if("Y".equals(pDto.get("IS_CREATE"))){
					//得到单据编码
					docCode = ruleService.getNextSerialNum("ST_LO_ADJUST", LEDGER_ID, "", year, month, userId);
					pDto.put("LO_ADJUST_H_CODE",docCode);
					xcStLocationAdjustDao.insertXcStLocationAdjustH(pDto);
				}
				else{
					docCode = pDto.getAsString("LO_ADJUST_H_CODE");
					xcStLocationAdjustDao.updateXcStLocationAdjustH(pDto);
				}
			}
			else{}//扩展
			json.put("docCode",docCode);
		} catch (Exception e) {
			json.put("msg",e.getMessage());
			log.error(e.getMessage());
			throw e;
		}
		return json;
	}
	/*
	 * 
	  * <p>Title deleteXcStDocs</p>
	  * <p>Description 删除</p>
	  * @param hIds			主ID数组
	  * @param docClassCode	单据大类
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.st.service.XcStDocsService#deleteXcStDocs(java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public JSONObject deleteXcStDocs(String hIds,String docClassCode) throws Exception {
		JSONObject json = new JSONObject();
		//更新入库单主表金额
		try {
			JSONArray ja = new JSONArray(hIds);
			if(XcStConstans.RKD.equals(docClassCode)){				//操作的是入库单
				List<String> stWhEntryHIds = new ArrayList<String>();
				for(int i = 0;i < ja.length();i++){
					JSONObject jo = ja.getJSONObject(i);
					String ENTRY_H_ID = jo.getString("stDocId");
					stWhEntryHIds.add(ENTRY_H_ID);
				}
				if(stWhEntryHIds != null && stWhEntryHIds.size() > 0)
					xcStWhEntryDao.deleteXcStWhEntryH(stWhEntryHIds);
			}
			else if(XcStConstans.CKD.equals(docClassCode)){				//操作的是入库单
				List<String> stWhDeleveredHIds = new ArrayList<String>();
				for(int i = 0;i < ja.length();i++){
					JSONObject jo = ja.getJSONObject(i);
					String DELEVERED_H_ID = jo.getString("stDocId");
					stWhDeleveredHIds.add(DELEVERED_H_ID);
				}
				if(stWhDeleveredHIds != null && stWhDeleveredHIds.size() > 0)
					xcStWhDeleveredDao.deleteXcStWhDeleveredH(stWhDeleveredHIds);
			}
			else if(XcStConstans.SQD.equals(docClassCode)){			//操作的是领用申请单
				List<String> stUseApplyHIds = new ArrayList<String>();
				for(int i = 0;i < ja.length();i++){
					JSONObject jo = ja.getJSONObject(i);
					String USE_APPLY_H_ID = jo.getString("stDocId");
					stUseApplyHIds.add(USE_APPLY_H_ID);
				}
				if(stUseApplyHIds != null && stUseApplyHIds.size() > 0)
					xcStUseApplyDao.deleteXcStUseApplyH(stUseApplyHIds);
			}
			else if(XcStConstans.CBTZD.equals(docClassCode)){		//操作的是成本调整单
				List<String> stCostChangeHIds = new ArrayList<String>();
				for(int i = 0;i < ja.length();i++){
					JSONObject jo = ja.getJSONObject(i);
					String COST_CHANGE_H_ID = jo.getString("stDocId");
					stCostChangeHIds.add(COST_CHANGE_H_ID);
				}
				if(stCostChangeHIds != null && stCostChangeHIds.size() > 0)
					xcStCostChangeDao.deleteXcStCostChangeH(stCostChangeHIds);
			}
			else if(XcStConstans.WZDBD.equals(docClassCode)){		//操作的是物资调拨单
				List<String> stTransshipmentHIds = new ArrayList<String>();
				for(int i = 0;i < ja.length();i++){
					JSONObject jo = ja.getJSONObject(i);
					String TR_H_ID = jo.getString("stDocId");
					stTransshipmentHIds.add(TR_H_ID);
				}
				if(stTransshipmentHIds != null && stTransshipmentHIds.size() > 0){
					xcStTransshipmentDao.deleteXcStTransshipmentH(stTransshipmentHIds);
				}
			}
			else if(XcStConstans.HWTZD.equals(docClassCode)){		//操作的是货位调整单
				List<String> stLocationAdjustHIds = new ArrayList<String>();
				for(int i = 0;i < ja.length();i++){
					JSONObject jo = ja.getJSONObject(i);
					String LO_ADJUST_H_ID = jo.getString("stDocId");
					stLocationAdjustHIds.add(LO_ADJUST_H_ID);
				}
				if(stLocationAdjustHIds != null && stLocationAdjustHIds.size() > 0)
					xcStLocationAdjustDao.deleteXcStLocationAdjustH(stLocationAdjustHIds);
			}
			else{}//扩展
		} catch (Exception e) {
			json.put("msg",e.getMessage());
			log.error(e.getMessage());
			throw e;
		}
		return json;
	}
	/*
	 * 
	  * <p>Title accountOrCancelAccount</p>
	  * <p>Description 记账取消记账处理</p>
	  * @param stDocId
	  * @param opType
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.st.service.XcStWhEntryService#accountOrCancelAccount(java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public JSONObject accountOrCancelAccount(String stDocId,String opType) throws Exception {
		JSONObject json = new JSONObject();
		try {
			Dto pDto = new BaseDto();
			pDto.put("ENTRY_H_ID",stDocId);
			if("1".equals(opType)){//记账处理
				pDto.put("STATUS","3");
				pDto.put("INIT","1");
				xcStWhInventoryService.entry(pDto);
			}
			if("0".equals(opType)){//取消记账处理
				//发生业务不能取消记账
				String whetherCitedStr = xcStWhEntryDao.xcStWhEntryWhetherCited(stDocId);
				if(whetherCitedStr != null && !"".equals(whetherCitedStr))
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_ST_SERVICE_0006", null));
				//删除已经记账现存量信息和事物信息
				xcStWhInventoryDAO.delWhInventoryByEntryHId(pDto);
				xcStWhInventoryDAO.xc_st_trans_delete_by_entry_h_id(pDto);
				pDto.put("STATUS","1");
				pDto.put("INIT","0");
			}
			//执行入库单记账、取消记账处理
			xcStWhEntryDao.updateXcStWhEntryHStatusAndInit(pDto);
		} catch (Exception e) {
			json.put("msg",e.getMessage());
			log.error(e.getMessage());
			throw e;
		}
		return json;
	}
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject acceptDoc(String TR_H_ID) throws Exception {
		JSONObject json = new JSONObject();
		try {
			List<XcStCommonBean> xcStCommonBeanList = new ArrayList<XcStCommonBean>();
			//根据调拨单ID得到调拨单信息
			Dto tDto = xcStTransshipmentDao.selectXcStTransshipmentH(TR_H_ID);
			List<Dto> dtoList = xcStTransshipmentDao.selectXcStTransshipmentLByHId(TR_H_ID);
			tDto.put("IS_CREATE","Y");
			tDto.put("ENTRY_H_ID",java.util.UUID.randomUUID().toString());
			tDto.put("ENTRY_H_NAME",tDto.getAsString("TR_H_NAME"));
			tDto.put("docClassCode","RKD");
			tDto.put("DOC_CAT_CODE","QTRK");
			tDto.put("WAREHOUSE_ID",tDto.getAsString("IN_WH_ID"));
			tDto.put("WH_KEEPER_ID",tDto.getAsString("IN_WH_KEEPER_ID"));
			tDto.put("INIT","2");
			tDto.put("STATUS","1");
			for (int i = 0;i < dtoList.size();i++) {
				Dto dto = dtoList.get(i);
				dto.put("ENTRY_H_ID",tDto.getAsString("ENTRY_H_ID"));
				dto.put("ENTRY_L_ID",java.util.UUID.randomUUID().toString());
				dto.put("BUSINESS_TYPE_ID",tDto.getAsString("BUSINESS_TYPE_ID"));
				dto.put("DOC_CAT_CODE",tDto.getAsString("DOC_CAT_CODE"));
				dto.put("ENTRY_L_CODE",i);
				dto.put("LOCATION_ID",dtoList.get(i).getAsString("IN_LOCATION_ID"));
			}
			tDto.put("insertDtlList",dtoList);
			this.saveOrUpdateXcStDocs(tDto);
			//更新调拨单状态为已接受
			XcStCommonBean xcStCommonBean = new XcStCommonBean();
			xcStCommonBean.setTableName("xc_st_transshipment_h");
			xcStCommonBean.setPriKey("TR_H_ID");
			xcStCommonBean.setPriKeyId(TR_H_ID);
			xcStCommonBean.setStatus("4");
			xcStCommonBeanList.add(xcStCommonBean);
			xcStCommonDao.updateXcStDocStatus(xcStCommonBeanList);
		} catch (Exception e) {
			json.put("msg",e.getMessage());
			log.error(e.getMessage());
			throw e;
		}
		return json;
	}
	@Override
	public JSONObject closeXcStUseApply(String hIds) throws Exception {
		JSONObject json = new JSONObject();
		//更新入库单主表金额
		try {
			JSONArray ja = new JSONArray(hIds);
			List<String> stUseApplyHIds = new ArrayList<String>();
			for(int i = 0;i < ja.length();i++){
				JSONObject jo = ja.getJSONObject(i);
				String USE_APPLY_H_ID = jo.getString("stDocId");
				stUseApplyHIds.add(USE_APPLY_H_ID);
			}
			if(stUseApplyHIds != null && stUseApplyHIds.size() > 0)
				xcStUseApplyDao.updateXcStUseApplyHCloseStatus(stUseApplyHIds);
		} catch (Exception e) {
			json.put("msg",e.getMessage());
			log.error(e.getMessage());
			throw e;
		}
		return json;
	}
}