package com.xzsoft.xc.ap.service;

import java.util.HashMap;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.xzsoft.xc.ap.modal.LedgerVendorBean;

/** 
 * @ClassName: ApVendorService 
 * @Description: 供应商分配Service层
 * @author weicw 
 * @date 2016年11月24日 上午10:14:23 
 * 
 * 
 */
public interface ApVendorService {
	/**
	 * @Title:addLedgerVendor
	 * @Description:新增组织下的供应商
	 * 参数格式:
	 * @param java.lang.String
	 * @return 
	 * @throws 
	 */
	public JSONObject addLedgerVendor(String str,String orgId,String ledgerId) throws Exception;

	/**
	 * @Title: deleteLedgerVendor
	 * @Description: 删除账簿（组织）下的供应商
	 * @param ledgerId
	 * @param orgId
	 * @param vendorIds
	 * @throws Exception  设定文件 
	 * @return  JSONObject 返回类型 
	 */
	public JSONObject deleteLedgerVendor(String ledgerId,String orgId,String vendorIds) throws Exception;
}

