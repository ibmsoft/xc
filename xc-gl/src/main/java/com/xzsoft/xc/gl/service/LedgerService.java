package com.xzsoft.xc.gl.service;

import java.util.HashMap;

/**
 * @ClassName: LedgerService 
 * @Description: 账簿、账簿权限、账簿建账/取消建账 服务层接口
 * @author linp
 * @date 2016年5月13日 下午3:03:59 
 *
 */
public interface LedgerService {

	/**
	 * @Title: isCreateAccount 
	 * @Description: 是否建账
	 * @param ledgerId 账簿ID
	 * @return
	 * @throws Exception    设定文件
	 */
	public HashMap<String,String> isCreateAccount(String ledgerId) throws Exception ;
	
	/**
	 * @Title: updateCreateAccountFlag 
	 * @Description: 修改建账标志
	 * @param ledgerId
	 * @param flag
	 * @throws Exception    设定文件
	 */
	public void updateCreateAccountFlag(String ledgerId,String flag,String createType) throws Exception ;
	
	/**
	 * @Title: grantOrRevoke2UserRole 
	 * @Description: 账簿授权/取消授权
	 * @param ldJsonArray
	 * @param urJsonArray
	 * @param opType ： grant-授权, revoke-取消授权
	 * @throws Exception    设定文件
	 */
	public void grantOrRevoke2UserRole(String ldJsonArray, String urJsonArray, String opType) throws Exception ;
	
	/**
	 * @Title: batchDelSec 
	 * @Description: 批量删除授权信息
	 * @param secJsonArray
	 * @throws Exception    设定文件
	 */
	public void batchDelSec(String secJsonArray) throws Exception ;

}
