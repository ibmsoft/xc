package com.xzsoft.xsr.core.webservice.internal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.jws.WebService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xzsoft.xip.framework.util.SpringDBHelp;
import com.xzsoft.xsr.core.mapper.FjMapper;
import com.xzsoft.xsr.core.mapper.InternalDataMapper;
import com.xzsoft.xsr.core.modal.Colitem;
import com.xzsoft.xsr.core.webservice.internal.InternalDataService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 报表系统内部接口实现类-基于cxf
 * 
 * @author ZhouSuRong 2016-4-6
 */
@Service
@WebService(endpointInterface = "com.xzsoft.xsr.core.webservice.internal.InternalDataService", targetNamespace = "http://ws.xsr.xzsoft.com")
public class InternalDataServiceImpl implements InternalDataService {
	public Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private InternalDataMapper interDataMapper;
	@Autowired
	private FjMapper fjmapper;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public String sendData(String json, String userName, String userPwd) {
		JSONObject jsonObject = JSONObject.fromObject(json);
		Map<String, Object> mapJson = JSONObject.fromObject(jsonObject);
		HashMap params = new HashMap();
		String keys = "";
		String suitcode = mapJson.get("suitCode").toString();
		String modaltypecode = mapJson.get("modaltypeCode").toString();
		String entitycode = mapJson.get("entityCode").toString();
		String periodcode = mapJson.get("periodCode").toString();
		String cnycode = mapJson.get("currencyCode").toString();
		String username = mapJson.get("userName").toString();
		String modalsData = mapJson.get("modalsData").toString();
		params.put("suitCode", suitcode);
		params.put("modalTypeCode", modaltypecode);
		params.put("entityCode", entitycode);
		params.put("periodCode", periodcode);
		params.put("cnyCode", cnycode);
		JSONArray modalsDataArray = JSONArray.fromObject(modalsData);
		for (int j = 0; j < modalsDataArray.size(); j++) {
			Map<String, String> Json = JSONObject.fromObject(modalsDataArray.get(j));
			String modalsheetcode = Json.get("modalsheetCode").toString();
			JSONArray fixdataArray = JSONArray.fromObject(Json.get("fixData"));
			JSONArray floatdataArray = JSONArray.fromObject(Json.get("floatData"));
			params.put("modalSheetCode", modalsheetcode);
			try {
				List<HashMap> repCodesMap = interDataMapper.getRepIds(params);
				if (repCodesMap.size() > 0) {
					String suitId = repCodesMap.get(0).get("SUIT_ID").toString();
					String modaltypeId = repCodesMap.get(0).get("MODALTYPE_ID").toString();
					String modalsheetId = repCodesMap.get(0).get("MODALSHEET_ID").toString();
					String entityId = repCodesMap.get(0).get("ENTITY_ID").toString();
					String periodId = repCodesMap.get(0).get("PERIOD_ID").toString();
					String cnyId = repCodesMap.get(0).get("CURRENCY_ID").toString();
					params.put("suitId", suitId);
					params.put("modalTypeId", modaltypeId);
					params.put("modalSheetId", modalsheetId);
					params.put("entityId", entityId);
					params.put("periodId", periodId);
					params.put("cnyId", cnyId);
					
				}
				params.put("userName", username);
				params.put("fixdataArray", fixdataArray);
				params.put("floatdataArray", floatdataArray);
				interDataMapper.deleteInnerFixTable(params);
				interDataMapper.deleteInnerFloatTable(params);
				if (fixdataArray.size()!=0) {
					interDataMapper.saveInnerFixData(params);
				}
				if (floatdataArray.size()!=0) {
					saveInnerFloatData(params);
				}
				interDataMapper.updateInnerFixItem(params);
				interDataMapper.updateInnerFloatItem(params);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error("插入临时表错误", e);
				return "{\"flag\":\"1\",\"msg\":\"上报失败\"}";
			}
		}
		return "{\"flag\":\"0\",\"msg\":\"上报成功\"}";
	}
    
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void saveInnerFloatData( HashMap params) throws Exception {
		Connection conn = SpringDBHelp.getConnection();
		List<Map<String, String>> mapListJson = (List<Map<String, String>>)params.get("floatdataArray");
		List<Colitem> colList = fjmapper.getFjColitemList(params.get("suitId").toString(), params.get("modalSheetId").toString(), params.get("modalTypeId").toString()); // 获取DATA与列指标的对应
		String keys = "";
		String val = "";
		for (int i = 0; i < colList.size(); i++) {
			Colitem col = colList.get(i);
			keys = keys + col.getDATA_COL() + ",";
		}
		if (keys.lastIndexOf(",") == keys.length() - 1) {
			keys = keys.substring(0, keys.length() - 1);
		}
		for (int i2 = 0; i2 < keys.split(",").length; i2++) {
			val += "?,";
		}
		if (val.lastIndexOf(",") == val.length()-1) {
			val = val.substring(0, val.length() - 1);
		}

		PreparedStatement pst = null;
		StringBuffer sql = new StringBuffer();
		sql.append(
				"INSERT INTO xsr_rep_inner_data__fj_temp ("
				+ "SUIT_ID,SUIT_CODE,MODALTYPE_ID,MODALTYPE_CODE,MODALSHEET_ID,MODALSHEET_CODE,"
				+ "ENTITY_ID,ENTITY_CODE,PERIOD_ID,PERIOD_CODE,CURRENCY_ID,CURRENCY_CODE,ROW_NO,"
				+ "ROWNUMDTL,LAST_UPDATE_DATE,LAST_UPDATED_BY,CREATION_DATE,CREATED_BY,")
				.append(keys).append(")VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,").append(val).append(")");
		try {
			pst = conn.prepareStatement(sql.toString());
			for (int i3 = 0; i3 <mapListJson.size(); i3++) {
				pst.setString(1, params.get("suitId").toString());
				pst.setString(2, params.get("suitCode").toString());
				pst.setString(3, params.get("modalTypeId").toString());
				pst.setString(4, params.get("modalTypeCode").toString());
				pst.setString(5, params.get("modalSheetId").toString());
				pst.setString(6, params.get("modalSheetCode").toString());
				pst.setString(7, params.get("entityId").toString());
				pst.setString(8, params.get("entityCode").toString());
				pst.setString(9, params.get("periodId").toString());
				pst.setString(10, params.get("periodCode").toString());
				pst.setString(11, params.get("cnyId").toString());
				pst.setString(12, params.get("cnyCode").toString());
				pst.setString(13, mapListJson.get(i3).get("ROWNO"));
				pst.setInt(14, Integer.parseInt(mapListJson.get(i3).get("ROWNUMDTL")));
				pst.setString(15, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				pst.setString(16, params.get("userName").toString());
				pst.setString(17, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				pst.setString(18, params.get("userName").toString());
				for (int i4 = 1; i4 <= colList.size(); i4++) {
					pst.setString(i4 + 18, mapListJson.get(i3).get(colList.get(i4 - 1).getDATA_COL()));
				}
				pst.addBatch();
				if (i3 % 1000 == 0) {
					pst.executeBatch();
				}
			}
			pst.executeBatch();
		} catch (SQLException e) {
			throw e;
		} finally {
			if (pst != null) {
				pst.close();
			}
		}
	}
}
