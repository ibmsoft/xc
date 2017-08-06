package com.xzsoft.xc.ap.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.ap.dao.ApVendorDao;
import com.xzsoft.xc.ap.mapper.ApVendorMapper;

/** 
 * @ClassName: ApVendorDaoImpl 
 * @Description: 供应商分配Dao实现类
 * @author weicw 
 * @date 2016年11月24日 上午10:10:06 
 * 
 * 
 */
@Repository("apVendorDao")
public class ApVendorDaoImpl implements ApVendorDao {
	@Resource
	ApVendorMapper mapper;
	
	/**
	 * @Title: addLedgerVendor
	 * @Description: 新增组织下供应商
	 * 参数格式:
	 * @param java.util.HashMap
	 * @return
	 * @throws 
	 */
	@Override
	public void addLedgerVendor(HashMap<String, Object> map) {
		mapper.addLedgerVendor(map);
	}
	/**
	 * @Title: deleteLedgerVendor
	 * @Description: 删除组织下供应商
	 * 参数格式:
	 * @param java.util.List
	 * @return
	 * @throws 
	 */
	@Override
	public void deleteLedgerVendor(List<String> list) {
		mapper.deleteLedgerVendor(list);
	}
	
	
	@Override
	public boolean validateLdVendorIsUsed(HashMap<String, String> map) {
		return mapper.validateLdVendorIsUsed(map);
	}
	
	@Override
	public void deleteLedgerVendor2(String ledgerId, List<String> vendorIds) {
		if (vendorIds != null ) {
			List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
			for (String vendorId : vendorIds) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("LEDGER_ID", ledgerId);
				map.put("VENDOR_ID", vendorId);
				list.add(map);
			}
			mapper.deleteLedgerVendor2(list);
		}
	}

}

