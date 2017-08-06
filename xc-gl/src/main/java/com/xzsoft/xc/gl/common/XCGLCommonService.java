package com.xzsoft.xc.gl.common;

import java.util.HashMap;

import org.codehaus.jettison.json.JSONObject;

import com.xzsoft.xc.gl.modal.Account;

/**
 * 
 * @ClassName: XCGLCommonService 
 * @Description: 云ERP平台之总账系统通用服务接口
 * @author linp
 * @date 2015年12月9日 下午2:33:27 
 *
 */
public interface XCGLCommonService {

	/**
	 * @Title: syncVendors2Redis 
	 * @Description: 将供应商同步到Redis数据库
	 * @param segCode
	 * @return
	 * @throws Exception
	 */
	public JSONObject syncVendors2Redis(String segCode)throws Exception ;
	
	/**
	 * @Title: syncCustomers2Redis 
	 * @Description: 将客户同步到Redis数据库
	 * @param segCode
	 * @throws Exception    设定文件 
	 * @return JSONObject    返回类型
	 */
	public JSONObject syncCustomers2Redis(String segCode)throws Exception ;
	
	/**
	 * @Title: syncOrgs2Redis 
	 * @Description: 将内部往来同步到Redis数据库
	 * @param segCode
	 * @throws Exception    设定文件 
	 * @return JSONObject    返回类型
	 */
	public JSONObject syncOrgs2Redis(String segCode)throws Exception ;
	
	/**
	 * @Title: syncEmps2Redis 
	 * @Description: 将个人往来同步到Redis数据库
	 * @param segCode
	 * @throws Exception    设定文件 
	 * @return JSONObject    返回类型
	 */
	public JSONObject syncEmps2Redis(String segCode)throws Exception ;
	
	/**
	 * @Title: syncProducts2Redis 
	 * @Description: 将产品同步到Redis数据库
	 * @param segCode
	 * @throws Exception    设定文件 
	 * @return JSONObject    返回类型
	 */
	public JSONObject syncProducts2Redis(String segCode)throws Exception ;
	
	/**
	 * @Title: syncProjects2Redis 
	 * @Description: 将项目同步到Redis数据库或从Redis数据库清除
	 * @param ledgerId
	 * @param segCode
	 * @param opType
	 * @throws Exception    设定文件 
	 * @return JSONObject    返回类型
	 */
	public JSONObject syncProjects2Redis(String ledgerId, String segCode, String opType)throws Exception ;
	
	/**
	 * @Title: syncDepts2Redis 
	 * @Description: 将成本中心同步到Redis数据库 
	 * @param ledgerId
	 * @param segCode
	 * @param opType
	 * @throws Exception    设定文件 
	 * @return JSONObject    返回类型
	 */
	public JSONObject syncDepts2Redis(String ledgerId, String segCode, String opType)throws Exception ;
	
	/**
	 * @Title: syncCustoms2Redis 
	 * @Description:  将自定义段同步到Redis数据库
	 * @param ledgerId
	 * @param segCode
	 * @param opType
	 * @throws Exception    设定文件 
	 * @return JSONObject    返回类型
	 */
	public JSONObject syncCustoms2Redis(String ledgerId, String segCode, String opType)throws Exception ;
	
	/**
	 * @Title: syncAccHrcy2Redis 
	 * @Description: 将会计科目体系同步到Redis数据库 
	 * @param hrcyId
	 * @param opType
	 * @return
	 * @throws Exception
	 */
	public JSONObject syncAccHrcy2Redis(String hrcyId, String opType)throws Exception ;
	
	/**
	 * @Title: syncAccount2Redis 
	 * @Description: 科目增量同步处理
	 * @param accounts
	 * @param accId
	 * @throws Exception    设定文件
	 */
	public void syncAccount2Redis(HashMap<String,Account> accounts, String accId)throws Exception ; 
	
	/**
	 * @Title: syncCCID2Redis 
	 * @Description: 将CCID信息同步到Redis数据库
	 * @return
	 * @throws Exception
	 */
	public JSONObject syncCCID2Redis(String ledgerId)throws Exception ;
	
	/**
	 * @Title: syncCashItem2Redis 
	 * @Description: 将现金流量表项同步到Redis数据库
	 * @return
	 * @throws Exception    设定文件
	 */
	public JSONObject syncCashItem2Redis()throws Exception ;
	
	/**
	 * @Title: syncLedgers 
	 * @Description: 将账簿信息缓存到Redis数据库
	 * @return
	 * @throws Exception    设定文件
	 */
	public JSONObject syncLedgers(String ledgerIdOrCode,String cat)throws Exception ;
	
	/**
	 * @Title: syncVCategory 
	 * @Description: 将凭证分类信息缓存到Redis数据库
	 * @return
	 * @throws Exception    设定文件
	 */
	public JSONObject syncVCategory()throws Exception ;
}
