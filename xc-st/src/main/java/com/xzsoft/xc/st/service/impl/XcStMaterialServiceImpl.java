package com.xzsoft.xc.st.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xzsoft.xc.st.dao.XcStMaterialDao;
import com.xzsoft.xc.st.modal.XcStMaterialBean;
import com.xzsoft.xc.st.service.XcStMaterialService;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.platform.util.XipUtil;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;
/**
 * 
  * @ClassName XcStMaterialServiceImpl
  * @Description 物料信息业务处理层实现类
  * @author RenJianJian
  * @date 2016年11月30日 下午12:10:06
 */
@Service("xcStMaterialService")
public class XcStMaterialServiceImpl implements XcStMaterialService {
	
	public static final Logger log = Logger.getLogger(XcStMaterialServiceImpl.class);
	@Resource
	private XcStMaterialDao xcStMaterialDao;
	/*
	 * 
	  * <p>Title saveOrUpdateStMaterial</p>
	  * <p>Description 对物料进行增加或者修改操作</p>
	  * @param stMaterialInfo
	  * @param opType
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.st.service.XcStMaterialHandelService#saveOrUpdateStMaterial(java.lang.String, java.lang.String)
	 */
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	@Override
	public JSONObject saveOrUpdateStMaterial(String stMaterialInfo,String opType) throws Exception {
		JSONObject jo = new JSONObject();
		try {
			JSONObject json = new JSONObject(stMaterialInfo);
			String MATERIAL_ID = json.getString("MATERIAL_ID");
			String MATERIAL_CODE = json.getString("MATERIAL_CODE");
			String MATERIAL_NAME = json.getString("MATERIAL_NAME");
			String MATERIAL_PRO = json.getString("MATERIAL_PRO");
			String MATERIAL_SET_ID = json.getString("MATERIAL_SET_ID");
			String MAT_TYPE_SET_ID = json.getString("MAT_TYPE_SET_ID");
			String MATERIAL_TYPE_ID = json.getString("MATERIAL_TYPE_ID");
			String FINACE_TYPE_ID = json.getString("FINACE_TYPE_ID");
			String SPECIFICATION = json.getString("SPECIFICATION");
			String DIM_ID = json.getString("DIM_ID");
			String IS_BATCH = json.getString("IS_BATCH");
			String IS_SERIAL = json.getString("IS_SERIAL");
			String IS_PURCHASE = json.getString("IS_PURCHASE");
			String IS_PURCHASE_EXCESS = json.getString("IS_PURCHASE_EXCESS");
			String IS_SALE = json.getString("IS_SALE");
			String IS_SALE_EXCESS = json.getString("IS_SALE_EXCESS");
			String PURCHASE_PRICE = json.getString("PURCHASE_PRICE");
			String SALE_PRICE = json.getString("SALE_PRICE");
			String SALE_TYPE_ID = json.getString("SALE_TYPE_ID");
			String IMAGES = json.getString("IMAGES");
			
			XcStMaterialBean stMaterial =  new XcStMaterialBean();
			stMaterial.setMATERIAL_ID(MATERIAL_ID);
			stMaterial.setMATERIAL_CODE(MATERIAL_CODE);
			stMaterial.setMATERIAL_NAME(MATERIAL_NAME);
			stMaterial.setMATERIAL_PRO(MATERIAL_PRO);
			stMaterial.setMATERIAL_SET_ID(MATERIAL_SET_ID);
			stMaterial.setMATERIAL_TYPE_ID(MATERIAL_TYPE_ID);
			stMaterial.setFINACE_TYPE_ID(FINACE_TYPE_ID);
			stMaterial.setSPECIFICATION(SPECIFICATION);
			stMaterial.setDIM_ID(DIM_ID);
			stMaterial.setIS_BATCH(IS_BATCH);
			stMaterial.setIS_SERIAL(IS_SERIAL);
			stMaterial.setIS_PURCHASE(IS_PURCHASE);
			stMaterial.setIS_PURCHASE_EXCESS(IS_PURCHASE_EXCESS);
			stMaterial.setIS_SALE(IS_SALE);
			stMaterial.setIS_SALE_EXCESS(IS_SALE_EXCESS);
			if(PURCHASE_PRICE != null && !"".equals(PURCHASE_PRICE))
				stMaterial.setPURCHASE_PRICE(Double.parseDouble(PURCHASE_PRICE));
			if(SALE_PRICE != null && !"".equals(SALE_PRICE))
				stMaterial.setSALE_PRICE(Double.parseDouble(SALE_PRICE));
			stMaterial.setSALE_TYPE_ID(SALE_TYPE_ID);
			stMaterial.setIMAGES(IMAGES);
			stMaterial.setCREATED_BY(CurrentSessionVar.getUserId());
			stMaterial.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
			stMaterial.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
			stMaterial.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
			String materialCode = xcStMaterialDao.stMaterialCodeOrNameIsExists(MATERIAL_CODE,MATERIAL_NAME,MAT_TYPE_SET_ID,MATERIAL_ID);
			if(materialCode != null && !"".equals(materialCode))
				//throw new Exception("物料编码或名称已经存在，请重新录入！");
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_ST_SERVICE_0005", null));
			if("Add".equals(opType))
				xcStMaterialDao.insertXcStMaterial(stMaterial);//调用新增的方法
			if("Update".equals(opType))
				xcStMaterialDao.updateXcStMaterial(stMaterial);//调用修改的方法
		} catch (Exception e) {
			log.error(e.getMessage());
			jo.put("msg", e.getMessage());
			throw e;
		}
		return jo;
	}
	/*
	 * 
	  * <p>Title deleteXcStMaterial</p>
	  * <p>Description 对物料进行删除操作</p>
	  * @param materialIds
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.st.service.XcStMaterialHandelService#deleteXcStMaterial(java.lang.String)
	 */
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	@Override
	public JSONObject deleteXcStMaterial(String materialIds) throws Exception {
		JSONObject jo = new JSONObject();
		try {
			JSONArray ja = new JSONArray(materialIds);
			List<String> materialIdLists = new ArrayList<String>();
			if(ja != null && ja.length() > 0){
				for(int i = 0;i < ja.length();i++){
					jo = ja.getJSONObject(i);
					String materialId = jo.getString("MATERIAL_ID");
					//判断当前操作的物料是否被引用
					int whetherCitedStr = xcStMaterialDao.xcStMaterialWhetherCited(materialId);
					if(whetherCitedStr > 0)
						//throw new Exception("物料已经被账簿引用，不能删除，请确认！");
						throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_ST_SERVICE_0003", null));
					materialIdLists.add(materialId);
				}
			}
			//对物料进行删除操作
			if(materialIdLists.size() > 0 && materialIdLists != null){
				xcStMaterialDao.deleteXcStMaterial(materialIdLists);
			}
		} catch (Exception e) {
			jo.put("msg", e.getMessage());
			log.error(e.getMessage());
			throw e;
		}
		return jo;
	}
	@Override
	public JSONObject deleteXcStMaterialParams(String materialParamsIds) throws Exception {
		JSONObject jo = new JSONObject();
		try {
			JSONArray ja = new JSONArray(materialParamsIds);
			List<String> materialParamsIdLists = new ArrayList<String>();
			if(ja != null && ja.length() > 0){
				for(int i = 0;i < ja.length();i++){
					jo = ja.getJSONObject(i);
					String materialId = jo.getString("MATERIAL_ID");
					String orgId = jo.getString("ORG_ID");
					String materialParamsId = jo.getString("MATERIAL_PARMS_ID");
					//判断当前操作的物料是否被引用
					String whetherCitedStr = xcStMaterialDao.xcStMaterialParamsWhetherCited(materialId,orgId);
					if(whetherCitedStr != null && !"".equals(whetherCitedStr))
						//throw new Exception("物料已经被引用，不能删除，请确认！");
						throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_ST_SERVICE_0004", null));
					materialParamsIdLists.add(materialParamsId);
				}
			}
			//对物料进行删除操作
			if(materialParamsIdLists.size() > 0 && materialParamsIdLists != null){
				xcStMaterialDao.deleteXcStMaterialParams(materialParamsIdLists);
			}
		} catch (Exception e) {
			jo.put("msg", e.getMessage());
			log.error(e.getMessage());
			throw e;
		}
		return jo;
	}
}