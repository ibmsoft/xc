package com.xzsoft.xc.ap.dao;

import java.util.HashMap;
import java.util.List;

/** 
 * @ClassName: ApVendorDao 
 * @Description: 供应商分配Dao层
 * @author weicw 
 * @date 2016年11月24日 上午10:08:31 
 * 
 * 
 */
public interface ApVendorDao {
	/**
	 * @Title:addLedgerVendor
	 * @Description:新增组织下的供应商
	 * 参数格式:
	 * @param  java.util.HashMap
	 * @return 
	 * @throws 
	 */
	public void addLedgerVendor(HashMap<String, Object> map);
	/**
	 * @Title:deleteLedgerVendor
	 * @Description:删除组织下的供应商
	 * 参数格式:
	 * @param  java.util.List
	 * @return 
	 * @throws 
	 */
	public void deleteLedgerVendor(List<String> list);
	/**
	 * @Title: validateLdVendorIsUsed
	 * @Description: 验证账簿下供应商是否已被业务引用
	 * @param map {LEDGER_ID:"",ORG_ID:""}
	 * @return  String 返回类型 
	 * @throws
	 */
	public boolean validateLdVendorIsUsed(HashMap<String, String> map);
	/**
	 * @Title: deleteLedgerVendor2
	 * @Description: 删除账簿下的供应商（根据账簿id、供应商id） 
	 * @param ledgerId
	 * @param vendorIds  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void deleteLedgerVendor2(String ledgerId,List<String> vendorIds);

}

