package com.xzsoft.xc.fa.service;


public interface LedgerCatSettingService {
	
	/**
	 * syncLedgerCatSetting:(同步资产分类)
	 *
	 * @param userId 用户id
	 * @param ledgerId 账簿id
	 * @throws Exception
	 * @author q p
	 */
	public void syncLedgerCatSetting(String userId, String ledgerId) throws Exception;	
	
}
