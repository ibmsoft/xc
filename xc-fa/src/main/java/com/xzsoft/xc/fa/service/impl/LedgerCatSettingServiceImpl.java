package com.xzsoft.xc.fa.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.fr.base.core.UUID;
import com.xzsoft.xc.fa.dao.LedgerCatSettingDao;
import com.xzsoft.xc.fa.service.LedgerCatSettingService;

@Service("ledgerCatSettingServiceImpl")
public class LedgerCatSettingServiceImpl implements LedgerCatSettingService {

    private static Logger log = Logger.getLogger(LedgerCatSettingServiceImpl.class);

    @Resource
    private LedgerCatSettingDao ledgerCatSettingDao;

    @Override
	public void syncLedgerCatSetting(String userId, String ledgerId) throws Exception {
    	//获取未同步的资产分类
    	List<HashMap<String, Object>> notSyncFaCats = ledgerCatSettingDao.getNotSyncFaCats();
    	
    	if(notSyncFaCats!=null && notSyncFaCats.size()>0){
    		for(HashMap faCat : notSyncFaCats){
    			faCat.put("SETTING_ID", UUID.randomUUID().toString());
    			faCat.put("LEDGER_ID", ledgerId);
    			faCat.put("CREATION_DATE", new Date());
    			faCat.put("CREATED_BY", userId);
    			faCat.put("LAST_UPDATE_DATE", new Date());
    			faCat.put("LAST_UPDATED_BY", userId);
    		}
    		
    		ledgerCatSettingDao.saveLedgerCatSetting(notSyncFaCats);
    	}
	}

}
