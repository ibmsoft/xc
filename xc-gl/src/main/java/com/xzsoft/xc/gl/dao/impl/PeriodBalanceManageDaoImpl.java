package com.xzsoft.xc.gl.dao.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.xzsoft.xc.gl.dao.PeriodBalanceManageDao;
import com.xzsoft.xc.gl.mapper.PeriodBalanceManageMapper;
import com.xzsoft.xc.gl.mapper.XCGLCommonMapper;
import com.xzsoft.xc.gl.modal.BalanceForExcel;
import com.xzsoft.xc.gl.modal.CashForExcel;
import com.xzsoft.xc.util.modal.Ledger;
import com.xzsoft.xip.platform.util.PlatformUtil;

/**
* @Title: PeriodBalanceManageDaoImpl.java
* @Package com.xzsoft.xc.gl.dao.impl
* @Description: 余额管理取数实现类
* @author tangxl  
* @date 2016年1月20日 上午9:45:52
* @version V1.0
 */
@Repository("periodBalanceManageDao")
public class PeriodBalanceManageDaoImpl implements PeriodBalanceManageDao {
	// 日志记录器
	private static final Logger log = Logger.getLogger(PeriodBalanceManageDaoImpl.class.getName()) ;
	@Resource
	private PeriodBalanceManageMapper periodBalanceManageMapper ;
	@Resource
	private XCGLCommonMapper xcglCommonMapper;
	@Override
	public Ledger getLedgerInfoById(String ledgerId) {
		return xcglCommonMapper.getLedger(ledgerId);
	}
	@Override
	public void insertExcelDataToBalanceImp(List<BalanceForExcel> balanceList,String sessionId,String ledgerId)
			throws Exception {
		
		periodBalanceManageMapper.insertExcelDataToBalanceImp(balanceList);

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("sessionId", sessionId);
		map.put("dbType", PlatformUtil.getDbType());
		map.put("ledgerId", ledgerId);

		//校验科目段编码
		periodBalanceManageMapper.checkAccExist(map);
		//校验供应商
		periodBalanceManageMapper.checkVendorExist(map);
		//校验客户
		periodBalanceManageMapper.checkCustomerExist(map);
		//校验产品
		periodBalanceManageMapper.checkProductExist(map);
		//校验内部往来
		periodBalanceManageMapper.checkInnerOrgExist(map);
		//校验个人往来
		periodBalanceManageMapper.checkPersonalExist(map);
		//校验成本中心
		periodBalanceManageMapper.checkCostExist(map);
		//校验项目
		periodBalanceManageMapper.checkProjectExist(map);
		//校验自定义段的信息（1-4段）
		periodBalanceManageMapper.checkSD01Exist(map);
		periodBalanceManageMapper.checkSD02Exist(map);
		periodBalanceManageMapper.checkSD03Exist(map);
		periodBalanceManageMapper.checkSD04Exist(map);
	}
	/***
	 *获取Excel中不合法的导入数据
	 */
	@Override
	public List<HashMap> getInvalidImportData(HashMap<String, String> map) {
		return periodBalanceManageMapper.getInvalidImportData(map);
	}
	@Override
	public void instoreExcelData(HashMap<String, String> map) throws Exception{
		periodBalanceManageMapper.insertDataFromImp(map);
	}
	@Override
	public void deletePeriodBalances(String ledgerId, String sessionId)
			throws Exception {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("ledgerId", ledgerId);
		map.put("sessionId", sessionId);
		periodBalanceManageMapper.deletePeriodBalances(map);
	}
	@Override
	public List<HashMap> getExportBalanceData(HashMap<String, String> map)
			throws Exception {
		return periodBalanceManageMapper.getExportBalanceData(map);
	}
	
	@Override
	public void deleteCashTmp(String ledgerId, String sessionId)
			throws Exception {
		HashMap<String,String> map=new HashMap<String,String>();
		map.put("ledgerId", ledgerId);
		map.put("sessionId", sessionId);
		periodBalanceManageMapper.deleteCashTmp(map);
	}
	

	
	@Override
	public void insertCashTmp(List<CashForExcel> list) {
		periodBalanceManageMapper.insertCashTmp(list);
	}
	
	@Override
	public List<HashMap> getInvalidFromCashTmp(HashMap<String, String> map) {
	
		return periodBalanceManageMapper.getInvalidFromCashTmp(map);
	}

	@Override
	public void setCaIdAndAccIdByCode(List<CashForExcel> list) {
		
		HashMap<String,String> map=new HashMap<String,String>();
		for(CashForExcel cashForExcel:list){
			map.put("ledgerId",cashForExcel.getLedgerId());
			map.put("session_id",cashForExcel.getSession_id());	
			map.put("acc_code", cashForExcel.getAcc_code());
			map.put("ca_code", cashForExcel.getCa_code());
		}	
		
		periodBalanceManageMapper.setCaIdAndAccIdByCode(map);
		
	}

	@Override
	public void setFlag(String ledgerId,String session_id) {
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("ledgerId", ledgerId);
		map.put("session_id", session_id);
		map.put("dbType", PlatformUtil.getDbType());
		periodBalanceManageMapper.checkCaExists(map);
		periodBalanceManageMapper.checkAccExists(map);
		
	}

	@Override
	public void saveSumFormTmp(HashMap<String,String> map) {
		periodBalanceManageMapper.saveSumFormTmp(map);
	}
	@Override
	public void updateSumIfHave(HashMap<String, String> map) {
		periodBalanceManageMapper.updateSumIfHave(map);
	}
	
	@Override
	public String getStartPeriod_codeByLedger_id(String ledgerId) {
		
		return periodBalanceManageMapper.getStartPeriod_codeByLedger_id(ledgerId);
	}
	@Override
	public List<BalanceForExcel> getCheckBalanceList(HashMap<String, String> map)
			throws Exception {
		return periodBalanceManageMapper.getCheckBalanceList(map);
	}
}
