package com.xzsoft.xc.fa.service;

import net.sf.json.JSONObject;

public interface FaDepreciationService {
	/**
	 * calFaDepr:(计提折旧，账簿下，在本期间所有需要计提折旧的资产进行计提折旧)
	 *
	 * @param ledgerId 账簿ID
	 * @param periodCode 期间
	 * @param lang 语言
	 * @param userId 当前登录用户ID
	 * @return 返回Json 成功 {flag:0,msg:'成功提示'} 失败 {flag:1,msg:'失败提示'}
	 * @author GuoXiuFeng
	 * @version Ver 1.0
	 * @since   Ver 1.0
	*/
	public JSONObject calFaDepr(String ledgerId,String periodCode,String lang,String userId) throws Exception;
	
	/**
	 * cancelDepr:(取消计提折旧，将资产累计折旧恢复到即提前。删除资产事务，删除会计平台接口数据)
	 *
	 * @param assetId 资产ID
	 * @param periodCode 期间
	 * @param lang 语言
	 * @param userId 用户ID
	 * @return 返回Json 成功 {flag:0,msg:'成功提示'} 失败 {flag:1,msg:'失败提示'}
	 * @author GuoXiuFeng
	 * @version Ver 1.0
	 * @since   Ver 1.0
	*/
	public JSONObject cancelDepr(String assetId,String periodCode,String lang,String userId);
}
