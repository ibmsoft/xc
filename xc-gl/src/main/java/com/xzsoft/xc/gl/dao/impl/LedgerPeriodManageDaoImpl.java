package com.xzsoft.xc.gl.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.gl.dao.LedgerPeriodManageDao;
import com.xzsoft.xc.gl.mapper.LedgerPeriodManageMapper;
import com.xzsoft.xc.gl.mapper.XCGLCommonMapper;
import com.xzsoft.xc.gl.modal.Account;
import com.xzsoft.xc.gl.modal.Balances;
import com.xzsoft.xc.gl.modal.GlJzTpl;
import com.xzsoft.xc.gl.modal.GlJzTplDtl;
import com.xzsoft.xc.gl.modal.Period;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.platform.util.PlatformUtil;
import com.xzsoft.xip.platform.util.XipUtil;

/**
* @Title: LedgerPeriodManageDaoImpl
* @Description: 
* @author tangxl  
* @date 2016年1月20日 上午9:45:52
* @version V1.0
 */
@Repository("ledgerPeriodManageDao")
public class LedgerPeriodManageDaoImpl implements LedgerPeriodManageDao {
	@Resource
	LedgerPeriodManageMapper ledgerPeriodManageMapper;
	@Resource
	private XCGLCommonMapper xcglCommonMapper ;
	@Override
	public List<Period> getLedgerNextPeriod(String ledgerId,String currentPeriod) throws Exception {
		return null;
	}

	@Override
	public void appendPeriodByStartPeriod(String ledgerId,String periodCode) throws Exception {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("ledgerId", ledgerId);
		map.put("userId", CurrentSessionVar.getUserId());
		map.put("yearCode", periodCode.substring(0, 4));
		map.put("dbType", PlatformUtil.getDbType());
		map.put("periodCode", periodCode);
		//插入余额值
		ledgerPeriodManageMapper.appendPeriodByStartPeriod(map);
		//追加账簿期间
		ledgerPeriodManageMapper.appendPeriodToLedger(map);
	}

	@Override
	public Period checkNextPeriodExists(String periodCode) throws Exception {
		return ledgerPeriodManageMapper.getPeriodByCode(periodCode);
	}

	@Override
	public List<Balances> getLedgerBalances(HashMap<String, String> map)
			throws Exception {
		return ledgerPeriodManageMapper.getLedgerBalances(map);
	}

	@Override
	public void insertPeriodToLedger(HashMap<String, String> map)
			throws Exception {
		ledgerPeriodManageMapper.appendPeriodToLedger(map);
	}

	@Override
	public void appendLedgerPeriod(List<Balances> balancesList,HashMap<String, String> map)
			throws Exception {
		ledgerPeriodManageMapper.insertLedgerBalances(balancesList);
		ledgerPeriodManageMapper.appendPeriodToLedger(map);
	}

	@Override
	public String getLedgerMaxPeriodCode(String ledgerId) throws Exception {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("ledgerId", ledgerId);
		map.put("dbType", PlatformUtil.getDbType());
		return ledgerPeriodManageMapper.getLedgerMaxPeriodCode(map);
	}

	/* (non-Javadoc)
	 * @name     方法名
	 * @author   tangxl
	 * @date     2016年7月19日
	 * @注释                   TODO
	 * @version  1.0
	 * @see com.xzsoft.xc.gl.dao.LedgerPeriodManageDao#getGlJzTplById(java.lang.String)
	 */
	@Override
	public GlJzTpl getGlJzTplById(String jzTplId,String periodCode) throws Exception {
		GlJzTpl glJzTpl =ledgerPeriodManageMapper.getGlJzTplById(jzTplId);
		List<GlJzTplDtl> dtlList = ledgerPeriodManageMapper.getJzDtlByTplId(jzTplId);
		List<GlJzTplDtl> removeList = new ArrayList<GlJzTplDtl>();
		boolean hasData = false;
		if(glJzTpl == null)
			//throw new Exception("结转模板信息不存在!");
			throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_JZMBXXBCZ", null));
		if(dtlList == null || dtlList.size() == 0)
			//throw new Exception("转出科目信息不存在！");
			throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_ZCKMXXBCZ", null));
		//1、计算转入科目辅助段
		List<String> list = ledgerPeriodManageMapper.getAccSegmentCode(jzTplId);
		if(list != null && list.size() > 0){
			glJzTpl.setAccSegmentCode(list);
		}
		BigDecimal decimal = new BigDecimal(0);
		for(GlJzTplDtl t:dtlList){
			if(t.getSumAccId() == null || "".equals(t.getSumAccId()))
				t.setSumAccId(t.getZcAccId());
			//2、计算转出科目的编码
			Account account = xcglCommonMapper.getAccount(t.getZcAccId());
			t.setAccCode(account.getAccCode());
			//====================================3、转入科目辅助段不存在======================================================
			if(list == null || list.size() == 0){
				HashMap<String, Object> map  = ledgerPeriodManageMapper.querySumConvertAmount(t.getSumAccId(),periodCode,glJzTpl.getLedgerId());
				try {
					//科目余额存在
					if(map !=null){
						//根据取数规则来计算凭证分录的金额
						/*取数规则 
						 *1、   借方取数，直接取【取数科目】/【转入科目】的借方数
						 *-1、贷方取数，直接取【取数科目】/【转入科目】的贷方数 
						 *0、   余额取数，根据【取数科目】/【转入科目】的方向取【借方-贷方】/【贷方-借方】的数
						 */
						hasData = true;
						switch (t.getBalRule()) {
						case "1":
							decimal = (BigDecimal) map.get("PTD_DR");
							break;
						case "-1":
							decimal = (BigDecimal) map.get("PTD_CR");
							break;
						default:
							if("1".equals(t.getBalDirection())){
								//此时取数科目为【贷方】,取数科目方向与分录方向相反
								BigDecimal crDecimal = (BigDecimal) map.get("PJTD_CR");
								BigDecimal drDecimal = (BigDecimal) map.get("PJTD_DR");
								decimal = crDecimal.subtract(drDecimal);
							}else{
								//此时取数科目为【借方】
								BigDecimal crDecimal = (BigDecimal) map.get("PJTD_CR");
								BigDecimal drDecimal = (BigDecimal) map.get("PJTD_DR");
								decimal = drDecimal.subtract(crDecimal);
							}
							break;
						}	
					}else{
						decimal = new BigDecimal(0);
					}
				} catch (Exception e) {
					decimal = new BigDecimal(0);
				}
				if(decimal == null)
					decimal = new BigDecimal(0);
				t.setConvertAmount(decimal);
				if(decimal.compareTo(new BigDecimal(0)) == 0)
					removeList.add(t);
			}else{
			//==============================================4、转入科目辅助段存在==========================================================
				List<HashMap<String, Object>> dataList = ledgerPeriodManageMapper.queryConvertAmount(t.getSumAccId(),periodCode,glJzTpl.getLedgerId());
				if(dataList != null && dataList.size()>0){
					hasData = true;
					List<HashMap<String, Object>> combineList = new ArrayList<HashMap<String,Object>>();
					for(HashMap<String, Object> map:dataList){
						try {
							//科目余额存在
							if(map !=null){
								//根据取数规则来计算凭证分录的金额
								/*取数规则 
								 *1、   借方取数，直接取【取数科目】/【转入科目】的借方数
								 *-1、贷方取数，直接取【取数科目】/【转入科目】的贷方数 
								 *0、   余额取数，根据【取数科目】/【转入科目】的方向取【借方-贷方】/【贷方-借方】的数
								 */
								switch (t.getBalRule()) {
								case "1":
									decimal = (BigDecimal) map.get("PTD_DR");
									break;
								case "-1":
									decimal = (BigDecimal) map.get("PTD_CR");
									break;
								default:
									if("1".equals(t.getBalDirection())){
										//此时取数科目为【贷方】,取数科目方向与分录方向相反
										BigDecimal crDecimal = (BigDecimal) map.get("PJTD_CR");
										BigDecimal drDecimal = (BigDecimal) map.get("PJTD_DR");
										decimal = crDecimal.subtract(drDecimal);
									}else{
										//此时取数科目为【借方】
										BigDecimal crDecimal = (BigDecimal) map.get("PJTD_CR");
										BigDecimal drDecimal = (BigDecimal) map.get("PJTD_DR");
										decimal = drDecimal.subtract(crDecimal);
									}
									break;
								}	
							}else{
								decimal = new BigDecimal(0);
							}
						} catch (Exception e) {
							decimal = new BigDecimal(0);
						}
						if(decimal != null && decimal.compareTo(new BigDecimal(0)) != 0){
							HashMap<String, Object> tmpMap = new HashMap<String, Object>();
							tmpMap.put("CCID", map.get("CCID"));
							tmpMap.put("CCID_CODE", map.get("CCID_CODE"));
							tmpMap.put("ACC_CODE", map.get("ACC_CODE"));
							tmpMap.put("CONVERT_AMOUNT", decimal);
							combineList.add(tmpMap);
						}
					}
					//设置余额结转明细信息
					if(combineList.size()>0){
						t.setCcidCombineList(combineList);
					}else{
					//待结转的科目余额不存在时，忽略该明细行
						removeList.add(t);
					}
				}
			}//if else end;
		}//dtlList for loop end;
		if(!hasData){
			//throw new Exception("所选择的结转科目余额未过账！");
			throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_SXZDJZKMYEWGZ", null));
		}
		dtlList.removeAll(removeList);
		glJzTpl.setJzDtlList(dtlList);
		return glJzTpl;
	}

	/* (non-Javadoc)
	 * @name     方法名
	 * @author   tangxl
	 * @date     2016年7月20日
	 * @注释                   TODO
	 * @version  1.0
	 * @see com.xzsoft.xc.gl.dao.LedgerPeriodManageDao#insertCarryoverLog(java.util.HashMap)
	 */
	@Override
	public void insertCarryoverLog(HashMap<String, String> logMap)
			throws Exception {
		ledgerPeriodManageMapper.insertCarryoverLog(logMap);
	}
}
