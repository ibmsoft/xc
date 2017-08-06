package com.xzsoft.xc.fa.dao;

import java.util.HashMap;
import java.util.List;


public interface LedgerCatSettingDao {
	/**
	 * getNotSyncFaCats:(获取未同步的资产分类)
	 *
	 * @return
	 * @author q p
	 */
	public List<HashMap<String, Object>> getNotSyncFaCats();
	
	/**
	 * 
	 * saveLedgerCatSetting:(保存资产分类到资产分类账簿设置表)
	 *
	 * @param ledgerCats
	 * @author q p
	 */
	public void saveLedgerCatSetting(List<HashMap<String, Object>> ledgerCats);
}
