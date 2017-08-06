package com.xzsoft.xc.st.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.st.dao.XcStCommonDao;
import com.xzsoft.xc.st.mapper.XcStCommonMapper;
import com.xzsoft.xc.st.modal.Dto;
import com.xzsoft.xc.st.modal.XcStCommonBean;
import com.xzsoft.xip.platform.util.PlatformUtil;
/**
 * 
  * @ClassName XcStCommonDaoImpl
  * @Description 处理库存模块的公共dao接口实现类
  * @author RenJianJian
  * @date 2016年12月20日 下午12:36:05
 */
@Repository("xcStCommonDao")
public class XcStCommonDaoImpl implements XcStCommonDao {
	@Resource
	private XcStCommonMapper xcStCommonMapper;
	@Override
	public void updateXcStDocStatus(List<XcStCommonBean> xcStCommonBeanList) throws Exception {
		try {
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("list",xcStCommonBeanList);
			map.put("dbType",PlatformUtil.getDbType());
			xcStCommonMapper.updateXcStDocStatus(map);
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public HashMap<String, String> getXcStLdProcessInfo(String stCatCode, String ledgerId) throws Exception {
		HashMap<String,String> returnMap = null;
		try {
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("stCatCode",stCatCode);
			map.put("ledgerId",ledgerId);
			
			returnMap = xcStCommonMapper.getXcStLdProcessInfo(map);
		} catch (Exception e) {
			throw e;
		}
		return returnMap;
	}
	@Override
	public HashMap<String, String> getXcStProcessInfo(String stCatCode) throws Exception {
		HashMap<String,String> returnMap = null;
		try {
			returnMap = xcStCommonMapper.getXcStProcessInfo(stCatCode);
		} catch (Exception e) {
			throw e;
		}
		return returnMap;
	}
	@Override
	public String createrIsOrNotWarehouseKeeper(String USER_ID,String WAREHOUSE_ID,String LEDGER_ID) throws Exception {
		try {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("EMP_ID",USER_ID);
			map.put("WAREHOUSE_ID",WAREHOUSE_ID);
			map.put("LEDGER_ID",LEDGER_ID);
			return xcStCommonMapper.createrIsOrNotWarehouseKeeper(map);
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public void updatePoOrderLInfo(List<XcStCommonBean> lists) throws Exception {
		try {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", lists);
			xcStCommonMapper.updatePoOrderLInfo(map);
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public void updateSoOrderLInfo(List<XcStCommonBean> lists) throws Exception {
		try {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", lists);
			xcStCommonMapper.updateSoOrderLInfo(map);
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public void updateXcStWhEntryHAmount(List<XcStCommonBean> lists) {
		try {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", lists);
			xcStCommonMapper.updateXcStWhEntryHAmount(map);
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public void updateXcStWhDeleveredHAmount(List<XcStCommonBean> lists) {
		try {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", lists);
			xcStCommonMapper.updateXcStWhDeleveredHAmount(map);
		} catch (Exception e) {
			throw e;
		}
		
	}
	@Override
	public void updateXcStWhEntryLAmount(List<XcStCommonBean> lists) throws Exception {
		try {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", lists);
			xcStCommonMapper.updateXcStWhEntryLAmount(map);
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public void updateXcStWhDeleveredLAmount(List<XcStCommonBean> lists) throws Exception {
		try {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", lists);
			xcStCommonMapper.updateXcStWhDeleveredLAmount(map);
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public String judgmentXcStWhEntryHAmount(String RE_WH_ENTRY_H_ID,double AMOUNT) throws Exception {
		try {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("RE_WH_ENTRY_H_ID",RE_WH_ENTRY_H_ID);
			map.put("AMOUNT",AMOUNT);
			return xcStCommonMapper.judgmentXcStWhEntryHAmount(map);
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public String judgmentXcStWhDeleveredHAmount(String RE_DELEVERED_H_ID,double AMOUNT) throws Exception {
		try {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("RE_DELEVERED_H_ID",RE_DELEVERED_H_ID);
			map.put("AMOUNT",AMOUNT);
			return xcStCommonMapper.judgmentXcStWhDeleveredHAmount(map);
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public void updateXcStUseApplyLAmount(List<XcStCommonBean> lists) throws Exception {
		try {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", lists);
			xcStCommonMapper.updateXcStUseApplyLAmount(map);
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public HashMap<String, String> selectUpPeriodStatusByLedger( String LEDGER_ID, Date BIZ_DATE) throws Exception {
		HashMap<String,String> returnMap = null;
		try {
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("LEDGER_ID",LEDGER_ID);
			map.put("BIZ_DATE",BIZ_DATE);
			map.put("dbType", PlatformUtil.getDbType());
			returnMap = xcStCommonMapper.selectUpPeriodStatusByLedger(map);
		} catch (Exception e) {
			throw e;
		}
		return returnMap;
	}
	@Override
	public void insertXcStWhBLocation(Dto pDto) throws Exception {
		try {
			xcStCommonMapper.insertXcStWhBLocation(pDto);
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public void updateXcStWhBLocation(Dto pDto) throws Exception {
		try {
			xcStCommonMapper.updateXcStWhBLocation(pDto);
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public HashMap<String, String> getMaterialParamsInfo(String MATERIAL_ID, String LEDGER_ID) throws Exception {
		try {
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("MATERIAL_ID",MATERIAL_ID);
			map.put("LEDGER_ID",LEDGER_ID);
			return xcStCommonMapper.getMaterialParamsInfo(map);
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public int weatherExistBatchOrSerial(String BUSINESS_H_ID, String BUSINESS_L_ID, String MATERIAL_ID) throws Exception {
		try {
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("BUSINESS_H_ID",BUSINESS_H_ID);
			map.put("BUSINESS_L_ID",BUSINESS_L_ID);
			map.put("MATERIAL_ID", MATERIAL_ID);
			return xcStCommonMapper.weatherExistBatchOrSerial(map);
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public void updateXcStWhBLocation_01(Dto pDto) throws Exception {
		try {
			xcStCommonMapper.updateXcStWhBLocation_01(pDto);
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public void updateXcStWhBLocation_02(Dto pDto) throws Exception {
		try {
			xcStCommonMapper.updateXcStWhBLocation_02(pDto);
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public void updateXcStLocationInventory(Dto pDto) throws Exception {
		try {
			xcStCommonMapper.updateXcStLocationInventory(pDto);
		} catch (Exception e) {
			throw e;
		}
	}
}
