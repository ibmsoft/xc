package com.xzsoft.xc.ap.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xzsoft.xc.ap.dao.ApVendorDao;
import com.xzsoft.xc.ap.modal.LedgerVendorBean;
import com.xzsoft.xc.ap.service.ApVendorService;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.platform.util.PlatformUtil;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;

/** 
 * @ClassName: ApVendorServiceImpl 
 * @Description: 供应商分配Service实现类
 * @author weicw 
 * @date 2016年11月24日 上午10:15:36 
 * 
 * 
 */
@Service("apVendorService")
public class ApVendorServiceImpl implements ApVendorService {
	//日志记录器	
	Logger log = Logger.getLogger(ApVendorServiceImpl.class);
	@Resource
	ApVendorDao dao;
	/**
	 * @Title:addLedgerVendor
	 * @Description:新增组织下的供应商
	 * 参数格式:
	 * @param  java.util.HashMap
	 * @return 
	 * @throws 
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public JSONObject addLedgerVendor(String str,String orgId,String ledgerId) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			JSONArray array = new JSONArray(str);
			List<LedgerVendorBean> list = new ArrayList<LedgerVendorBean>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject ob = array.getJSONObject(i);
				LedgerVendorBean bean = new LedgerVendorBean();
				bean.setLD_VENDOR_ID(java.util.UUID.randomUUID().toString());
				bean.setLEDGER_ID(ledgerId);
				bean.setVENDOR_ID(ob.getString("VENDOR_ID"));
				bean.setIS_ENABLED(1);
				bean.setORG_ID(orgId);
				bean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
				bean.setCREATED_BY(CurrentSessionVar.getUserId());
				bean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
				bean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
				list.add(bean);
			}
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("list", list);
			map.put("dbType", PlatformUtil.getDbType());
			dao.addLedgerVendor(map);
			jo.put("flag", "0");
		} catch (Exception e) {
			jo.put("flag", "1");
			jo.put("msg", e.getMessage());
			log.error(e.getMessage());
			throw e;
		}
		
		
		return jo;
		
	}
	/**
	 * @Title:deleteLedgerVendor
	 * @Description:删除组织下的供应商
	 * 参数格式:
	 * @param  java.util.List
	 * @return 
	 * @throws 
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public JSONObject deleteLedgerVendor(String ledgerId,String orgId,String vendorIds) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			JSONArray array = new JSONArray(vendorIds);
			List<String> vendorIdUsedList = new ArrayList<String>(); // 已被引用的供应商id
			List<String> vendorIdNoUsedList = new ArrayList<String>(); // 未被引用的供应商id
			String vendorId = "";
			HashMap<String, String> map = new HashMap<String,String>();
			boolean temp = false;
			
			// 验证该供应商是否已被业务引用,区分开已被引用的供应商id和未被引用的供应商id
			for (int i = 0; i < array.length(); i++) {
				vendorId = array.getJSONObject(i).getString("VENDOR_ID");
				map.clear();
				map.put("LEDGER_ID", ledgerId);
				map.put("VENDOR_ID",vendorId);
				temp = dao.validateLdVendorIsUsed(map);
				if (temp) {
					vendorIdUsedList.add(vendorId);
				} else {
					vendorIdNoUsedList.add(vendorId);
				}
			}
			 
			// 删除 未被引用的的数据
			if (vendorIdNoUsedList.size() > 0) {
				dao.deleteLedgerVendor2(ledgerId, vendorIdNoUsedList);
			}
			
			jo.put("flag", "0");
			jo.put("msg", "");
			jo.put("delCount", vendorIdNoUsedList.size());
		} catch (Exception e) {
			jo.put("flag", "1");
			jo.put("msg", e.getMessage());
			log.error(e.getMessage());
			throw e;
		}
		return jo;
	}

}

