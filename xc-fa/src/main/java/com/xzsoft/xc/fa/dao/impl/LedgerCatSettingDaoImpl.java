package com.xzsoft.xc.fa.dao.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.xzsoft.xc.fa.dao.LedgerCatSettingDao;
import com.xzsoft.xc.fa.mapper.LedgerCatSettingMapper;

@Repository("ledgerCatSettingDao")
public class LedgerCatSettingDaoImpl implements LedgerCatSettingDao {

	private static final Logger log = Logger.getLogger(LedgerCatSettingDaoImpl.class);

	@Resource
	private LedgerCatSettingMapper ledgerCatSettingMapper;

	@Override
	public List<HashMap<String, Object>> getNotSyncFaCats() {
		return ledgerCatSettingMapper.getNotSyncFaCats();
	}

	@Override
	public void saveLedgerCatSetting(List<HashMap<String, Object>> ledgerCats) {
		ledgerCatSettingMapper.saveLedgerCatSetting(ledgerCats);
	}
	
}