package com.xzsoft.xc.gl.util;

import org.apache.log4j.Logger;

import com.xzsoft.xc.gl.common.XCGLCommonService;
import com.xzsoft.xc.util.common.XConstants;
import com.xzsoft.xip.framework.util.AppContext;


/**
 * @ClassName: XCGLCacheUtil 
 * @Description: 总账系统缓存处理类
 * @author linp
 * @date 2015年12月22日 下午6:03:29 
 *
 */
public class XCGLCacheUtil {
	// 日志记录器
	private static Logger log = Logger.getLogger(XCGLCacheUtil.class.getName()) ;
	
	/**
	 * 
	 * @Title: syncBaseDataToRedis 
	 * @Description: 将总账基础数据同步到Redis数据库
	 * @param ledgerId		账簿ID
	 * @param hrcyId		会计科目体系ID
	 * @param segCode 		段编码
	 * 					XC_GL_ACCOUNTS		科目;
	 * 					XC_AP_VENDORS		供应商;
	 * 					XC_AR_CUSTOMERS		客户;
	 * 					XIP_PUB_EMPS		个人往来;
	 * 					XIP_PUB_ORGS		内部往来;
	 * 					XC_GL_PRODUCTS		产品;
	 * 					XIP_PUB_DETPS		成本中心;
	 * 					XC_PM_PROJECTS		项目;
	 * 					XC_GL_CUSTOM1		自定义1;
	 * 					XC_GL_CUSTOM2		自定义2;
	 * 					XC_GL_CUSTOM3		自定义3;
	 * 					XC_GL_CUSTOM4		自定义4
	 * @throws Exception    设定文件 
	 * @return void    返回类型
	 */
	public static void syncBaseDataToRedis(String ledgerId,String hrcyId, String segCode)throws Exception {
		try {
			// 获取通用服务类
			XCGLCommonService service = (XCGLCommonService) AppContext.getApplicationContext().getBean("xcglCommonService") ;
			
			// 执行数据同步处理
			if(segCode.equals(XConstants.XC_GL_ACCOUNTS)) {
				service.syncAccHrcy2Redis(hrcyId, "add") ;
				
			}else if(segCode.equals(XConstants.XC_AP_VENDORS)){
				service.syncVendors2Redis(segCode) ;
				
			}else if(segCode.equals(XConstants.XC_AR_CUSTOMERS)){
				service.syncCustomers2Redis(segCode) ;
				
			}else if(segCode.equals(XConstants.XIP_PUB_ORGS)){
				service.syncOrgs2Redis(segCode) ;
				
			}else if(segCode.equals(XConstants.XIP_PUB_EMPS)){
				service.syncEmps2Redis(segCode) ;
				
			}else if(segCode.equals(XConstants.XC_GL_PRODUCTS)){
				service.syncProducts2Redis(segCode) ;
				
			}else if(segCode.equals(XConstants.XIP_PUB_DETPS)){
				service.syncDepts2Redis(ledgerId, segCode, "add") ;
				
			}else if(segCode.equals(XConstants.XC_PM_PROJECTS)){
				service.syncProjects2Redis(ledgerId, segCode, "add") ;
				
			}else if(segCode.equals(XConstants.XC_GL_CUSTOM1)
					|| segCode.equals(XConstants.XC_GL_CUSTOM2)
					|| segCode.equals(XConstants.XC_GL_CUSTOM3)
					|| segCode.equals(XConstants.XC_GL_CUSTOM4)){
				service.syncCustoms2Redis(ledgerId, segCode, "add") ;
				
			}else{
				// 扩展使用
			}
			
		} catch (Exception e){
			log.error(e.getMessage());
			throw e ;
		}
	}
	
	/**
	 * @Title: syncCCID2Redis 
	 * @Description: 将CCID信息同步到Redis数据库
	 * @param ccids
	 * @throws Exception
	 */
	public static void syncCCID2Redis(String ledgerId) throws Exception {
		try {
			// 获取通用服务类
			XCGLCommonService service = (XCGLCommonService) AppContext.getApplicationContext().getBean("xcglCommonService") ;
					
			// 执行CCID同步处理
			service.syncCCID2Redis(ledgerId) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
	}
	
	/**
	 * @Title: syncCashItem2Redis 
	 * @Description: 将现金流量表项同步到Redis数据库
	 * @throws Exception    设定文件
	 */
	public static void syncCashItem2Redis()throws Exception {
		try {
			// 获取通用服务类
			XCGLCommonService service = (XCGLCommonService) AppContext.getApplicationContext().getBean("xcglCommonService") ;
					
			// 执行现金流量项同步处理
			service.syncCashItem2Redis() ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
	}
	
	/**
	 * @Title: syncLedgers 
	 * @Description: 将账簿信息缓存到Redis数据库
	 * @throws Exception    设定文件
	 */
	public static void syncLedgers(String ledgerId)throws Exception {
		try {
			// 获取通用服务类
			XCGLCommonService service = (XCGLCommonService) AppContext.getApplicationContext().getBean("xcglCommonService") ;
					
			// 执行账簿信息项同步处理
			service.syncLedgers(ledgerId,"ById") ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
	}
	
	/**
	 * @Title: syncVCategory 
	 * @Description: 将凭证分类信息缓存到Redis数据库
	 * @throws Exception    设定文件
	 */
	public static void syncVCategory()throws Exception {
		try {
			// 获取通用服务类
			XCGLCommonService service = (XCGLCommonService) AppContext.getApplicationContext().getBean("xcglCommonService") ;
					
			// 执行凭证分类信息同步处理
			service.syncVCategory() ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
	}
	
	/**
	 * @Title: clearLedgerCachedData 
	 * @Description: 清除账簿的缓存数据信息
	 * @param ledgerId
	 * @throws Exception    设定文件 
	 * @return void    返回类型
	 */
	public static void clearLedgerCachedData(String ledgerId) throws Exception {
		try {
			// 获取通用服务类
			XCGLCommonService service = (XCGLCommonService) AppContext.getApplicationContext().getBean("xcglCommonService") ;
						
			service.syncDepts2Redis(ledgerId, XConstants.XIP_PUB_DETPS, "clear") ;
			
			service.syncProjects2Redis(ledgerId, XConstants.XC_PM_PROJECTS, "clear") ;
			
			service.syncCustoms2Redis(ledgerId, XConstants.XC_GL_CUSTOM1, "clear") ;
			
			service.syncCustoms2Redis(ledgerId, XConstants.XC_GL_CUSTOM2, "clear") ;
			
			service.syncCustoms2Redis(ledgerId, XConstants.XC_GL_CUSTOM3, "clear") ;
			
			service.syncCustoms2Redis(ledgerId, XConstants.XC_GL_CUSTOM4, "clear") ;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
	}
	
	/**
	 * @Title: clearHrcyCachedData 
	 * @Description: 清除会计科目体系缓存数据
	 * @param hrcyId
	 * @throws Exception    设定文件 
	 * @return void    返回类型
	 */
	public static void clearHrcyCachedData(String hrcyId) throws Exception {
		try {
			// 获取通用服务类
			XCGLCommonService service = (XCGLCommonService) AppContext.getApplicationContext().getBean("xcglCommonService") ;
			
			service.syncAccHrcy2Redis(hrcyId, "clear") ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
	}
	
}
