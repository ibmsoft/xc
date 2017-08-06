package com.xzsoft.xc.st.dao.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.st.dao.XcStLocationInventoryDao;
import com.xzsoft.xc.st.mapper.XcStLocationInventoryMapper;
import com.xzsoft.xc.st.modal.Dto;
/**
 * 
  * @ClassName XcStLocationInventoryDaoImpl
  * @Description 货位台账处理记录
  * @author RenJianJian
  * @date 2017年3月24日 上午11:54:01
 */
@Repository("xcStLocationInventoryDao")
public class XcStLocationInventoryDaoImpl implements XcStLocationInventoryDao {
	@Resource
	private XcStLocationInventoryMapper xcStLocationInventoryMapper;
	@Override
	public Dto getLocationInventoryByKey(Dto pDto) throws Exception {
		try {
			return xcStLocationInventoryMapper.xc_st_location_inventory_select_by_key(pDto);
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public void insertLocationInventory(Dto pDto) throws Exception {
		try {
			xcStLocationInventoryMapper.xc_st_location_inventory_insert(pDto);
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public void updateLocationInventory(Dto pDto) throws Exception {
		try {
			xcStLocationInventoryMapper.xc_st_location_inventory_update(pDto);
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public void deleteLocationInventory(Dto pDto) throws Exception {
		try {
			xcStLocationInventoryMapper.xc_st_location_inventory_deleteByKey(pDto);
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public Dto getLocationInventoryByWMSID(Dto pDto) throws Exception {
		try {
			return xcStLocationInventoryMapper.xc_st_location_inventory_select_by_WMSID(pDto);
		} catch (Exception e) {
			throw e;
		}
	}
}