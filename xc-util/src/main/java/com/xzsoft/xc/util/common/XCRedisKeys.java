package com.xzsoft.xc.util.common;

/**
 * @ClassName: XCRedisKeys 
 * @Description: Redis Key
 * @author linp
 * @date 2016年6月16日 下午2:13:08 
 *
 */
public class XCRedisKeys {

	
	/**
	 * @Title: getAccKey 
	 * @Description: 科目Redis主Key
	 * @param hrcyId
	 * @return
	 * @throws Exception    设定文件
	 */
	public static String getAccKey(String hrcyId) throws Exception{
		return XCUtil.getTenantId().concat(".").concat(XConstants.PROJECT_NAME).concat(".").concat(XConstants.XC_GL_ACCOUNTS).concat(".").concat(hrcyId) ; 
	}
	
	/**
	 * @Title: getLedgerSegKey 
	 * @Description: 账簿级参数Redis的主Key
	 * @param segCode
	 * @param ledgerId
	 * @throws Exception    设定文件
	 */
	public static String getLedgerSegKey(String segCode,String ledgerId) throws Exception {
		return XCUtil.getTenantId().concat(".").concat(XConstants.PROJECT_NAME).concat(".").concat(segCode).concat(".").concat(ledgerId) ;
	}
	
	/**
	 * @Title: getGlobalSegKey 
	 * @Description: 全局参数的Redis主Key
	 * @param segCode
	 * @return
	 * @throws Exception    设定文件
	 */
	public static String getGlobalSegKey(String segCode) throws Exception {
		return XCUtil.getTenantId().concat(".").concat(XConstants.PROJECT_NAME).concat(".").concat(segCode);
	}
	
	/**
	 * @Title: getRepCacheKey 
	 * @Description: 云ERP报表模块缓存Keys信息
	 * @param masterKey
	 * @param detailKey
	 * @return
	 * @throws Exception    设定文件
	 */
	public static String getRepCacheKey(String masterKey, String detailKey) throws Exception {
		String key = XCUtil.getTenantId().concat(".").concat(XConstants.REP_PRJ_NAME).concat(".").concat(masterKey) ;
		if(detailKey != null && !"".equals(detailKey)){
			key = key.concat(".").concat(detailKey) ;
		}
		return key ;
	}
	
}
