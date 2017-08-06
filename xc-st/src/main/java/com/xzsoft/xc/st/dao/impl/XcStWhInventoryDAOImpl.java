package com.xzsoft.xc.st.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.st.dao.XcStWhInventoryDAO;
import com.xzsoft.xc.st.mapper.XcStWhInventoryMapper;
import com.xzsoft.xc.st.modal.Dto;

/**
 * @ClassName: XcStWhInventoryDAOImpl
 * @Description: 库存台账管理
 * @author panghd
 * @date 2017年1月16日 下午15:15:43
 *
 */
@Repository("XcStWhInventoryDAO")
public class XcStWhInventoryDAOImpl implements XcStWhInventoryDAO {
	@Resource
	private XcStWhInventoryMapper xcStWhInventoryMapper;

	@Override
	public Dto getStWhInventoryById(Dto pDto) throws Exception {
		return xcStWhInventoryMapper.xc_st_wh_inventory_select_by_key(pDto);
	}

	@Override
	public void saveWhInventory(Dto pDto) throws Exception {
		try {
			xcStWhInventoryMapper.xc_st_wh_inventory_insert(pDto);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void saveTrans(Dto pDto) throws Exception {
		try {
			xcStWhInventoryMapper.xc_st_trans_insert(pDto);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void updateWhInventory(Dto pDto) throws Exception {
		try {
			xcStWhInventoryMapper.xc_st_wh_inventory_update(pDto);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void delWhInventoryById(Dto pDto) throws Exception {
		try {
			xcStWhInventoryMapper.xc_st_wh_inventory_deleteByKey(pDto);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void delWhInventoryByEntryHId(Dto pDto) throws Exception {
		try {
			xcStWhInventoryMapper.xc_st_wh_inventory_delete_by_entry_h_id(pDto);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void delWhInventoryByEntryLId(Dto pDto) throws Exception {
		try {
			xcStWhInventoryMapper.xc_st_wh_inventory_delete_by_entry_l_id(pDto);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Dto getStWhInventoryByMaterialId(Dto pDto) throws Exception {
		try {
			return xcStWhInventoryMapper.xc_st_wh_inventory_select_by_mat(pDto);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Dto getStWhInventoryByWarehouseId(Dto pDto) throws Exception {
		try {
			return xcStWhInventoryMapper.xc_st_warehouse_select_by_key(pDto);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Dto getStWhEntryHById(Dto pDto) throws Exception {
		try {
			return xcStWhInventoryMapper.xc_st_wh_entry_h_select_by_key(pDto);
		} catch (Exception e) {
			throw e;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List getStWhEntryLByEntryHId(Dto pDto) throws Exception {
		try {
			return xcStWhInventoryMapper.xc_st_wh_entry_l_select_by_key(pDto);
		} catch (Exception e) {
			throw e;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List getStWhBSerByBusinessId(Dto pDto) throws Exception {
		try {
			return xcStWhInventoryMapper.xc_st_wh_b_ser_select_by_key(pDto);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void saveWhInventoryList(Dto pDto) throws Exception {
		try {
			xcStWhInventoryMapper.xc_st_wh_inventory_insert_list(pDto);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void saveTransList(Dto pDto) throws Exception {
		try {
			xcStWhInventoryMapper.xc_st_trans_insert_list(pDto);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Dto getStLdPeriodsByOpen(Dto pDto) throws Exception {
		try {
			return xcStWhInventoryMapper.xc_st_ld_periods_select_by_key(pDto);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void updateWhInventoryList(Dto pDto) throws Exception {
		try {
			xcStWhInventoryMapper.xc_st_wh_inventory_update_list(pDto);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Dto getStWhDeleveredHById(Dto pDto) throws Exception {
		try {
			return xcStWhInventoryMapper
					.xc_st_wh_delevered_h_select_by_key(pDto);
		} catch (Exception e) {
			throw e;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List getStWhDeleveredLById(Dto pDto) throws Exception {
		try {
			return xcStWhInventoryMapper
					.xc_st_wh_delevered_l_select_by_key(pDto);
		} catch (Exception e) {
			throw e;
		}
	}
	
	public Dto getStWhInventoryByEntryId(Dto pDto) throws Exception {
		try {
			return xcStWhInventoryMapper.xc_st_wh_inventory_select_by_entry_id(pDto);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Dto getAccIdByBusId(Dto pDto) throws Exception {
		try {
			return xcStWhInventoryMapper.xc_st_ld_business_type_select_by_busId(pDto);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Dto getAccIdByPurTypeId(Dto pDto) throws Exception {
		try {
			return xcStWhInventoryMapper.xc_st_wa_ld_set_select_by_purTypeId(pDto);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void inventory_update_costChange(Dto pDto) throws Exception {
		try {
			xcStWhInventoryMapper.inventory_update_costChange(pDto);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void xc_st_trans_delete_by_entry_h_id(Dto pDto) throws Exception {
		try {
			xcStWhInventoryMapper.xc_st_trans_delete_by_entry_h_id(pDto);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void updateStWhBSerInfo(Dto pDto) throws Exception {
		try {
			xcStWhInventoryMapper.updateStWhBSerInfo(pDto);
		} catch (Exception e) {
			throw e;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List getStWhBLocationByBusinessId(Dto pDto) throws Exception {
		try {
			return xcStWhInventoryMapper.xc_st_wh_b_location_by_key(pDto);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void updateStWhBSerInfoByOtherId(Dto pDto) throws Exception {
		try {
			xcStWhInventoryMapper.updateStWhBSerInfoByOtherId(pDto);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Dto getStWhBLocationByBusinessIdAndLocationId(Dto pDto) throws Exception {
		try {
			return xcStWhInventoryMapper.xc_st_wh_b_location_by_key_01(pDto);
		} catch (Exception e) {
			throw e;
		}
	}
}
