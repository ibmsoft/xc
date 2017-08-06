package com.xzsoft.xc.gl.util;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.xzsoft.xc.gl.modal.VHead;
import com.xzsoft.xc.gl.service.LedgerPeriodManageService;
import com.xzsoft.xc.gl.service.LedgerService;
import com.xzsoft.xc.gl.service.VoucherHandlerService;
import com.xzsoft.xc.util.common.XCDateUtil;
import com.xzsoft.xc.util.common.XConstants;
import com.xzsoft.xc.util.service.RuleService;
import com.xzsoft.xip.framework.util.AppContext;

/**
 * @ClassName: XCGLWebBuilderUtil 
 * @Description: WebBuilder7的JAVA接口类
 * @author linp
 * @date 2015年12月18日 下午11:49:02 
 *
 */
public class XCGLWebBuilderUtil {
	// 日志记录器
	private static Logger log = Logger.getLogger(XCGLWebBuilderUtil.class.getName()) ;
	
	
	/**
	 * @Title: syncSharedSegments2Redis 
	 * @Description: 将共享辅助段信息同步到Redis数据库
	 * @param segCode
	 * 		共享辅助段包括以下五段：
	 * 					XC_AP_VENDORS		供应商;
	 * 					XC_AR_CUSTOMERS		客户;
	 * 					XIP_PUB_EMPS		个人往来;
	 * 					XIP_PUB_ORGS		内部往来;
	 * 					XC_GL_PRODUCTS		产品;
	 * @throws Exception    设定文件 
	 * @return String    返回类型
	 */
	public static String syncSharedSegments2Redis(String segCode) throws Exception {
		JSONObject jo = new JSONObject() ;
		
		try {
			// 执行共享辅助核算信息同步处理
			XCGLCacheUtil.syncBaseDataToRedis(null, null, segCode);
			
			// 提示信息
			jo.put("flag", "0") ;
			jo.put("msg", "");
			
		} catch (Exception e) {
			log.error(e.getMessage());
			// 提示信息
			jo.put("flag", "0") ;
			jo.put("msg", e.getMessage());
		}
		
		return jo.toString() ;
	}
	
	/**
	 * @Title: syncPrivateSegments2Redis 
	 * @Description: 将私有辅助核算段的信息同步到Redis数据库
	 * @param segCode
	 * 		私有辅助段包括以下六段：
	 * 					XIP_PUB_DEPTS		成本中心;
	 * 					XC_PM_PROJECTS		项目;
	 * 					XC_GL_CUSTOM1		自定义1;
	 * 					XC_GL_CUSTOM2		自定义2;
	 * 					XC_GL_CUSTOM3		自定义3;
	 * 					XC_GL_CUSTOM4		自定义4
	 * @throws Exception    设定文件 
	 * @return String    返回类型
	 */
	public static String syncPrivateSegments2Redis(String ledgerId, String segCode) throws Exception {
		JSONObject jo = new JSONObject() ;
		
		try {
			// 执行私有辅助核算信息同步处理
			XCGLCacheUtil.syncBaseDataToRedis(ledgerId, null, segCode);
			
			// 提示信息
			jo.put("flag", "0") ;
			jo.put("msg", "");
			
		} catch (Exception e) {
			log.error(e.getMessage());
			// 提示信息
			jo.put("flag", "0") ;
			jo.put("msg", e.getMessage());
		}
		
		return jo.toString() ;
	}
	
	/**
	 * @Title: syncAccHrcy2Redis 
	 * @Description: 将会计科目体系同步到Redis数据库
	 * @param accHrcyId
	 * @return
	 * @throws Exception
	 */
	public static String syncAccHrcy2Redis(String accHrcyId) throws Exception {
		JSONObject jo = new JSONObject() ;
		
		try {
			// 执行会计科目体系信息同步处理
			XCGLCacheUtil.syncBaseDataToRedis(null, accHrcyId, XConstants.XC_GL_ACCOUNTS);
			
			// 提示信息
			jo.put("flag", "0") ;
			jo.put("msg", "");
			
		} catch (Exception e) {
			log.error(e.getMessage());
			// 提示信息
			jo.put("flag", "0") ;
			jo.put("msg", e.getMessage());
		}
		
		return jo.toString() ;
	}
	
	/**
	 * @Title: syncLedgers 
	 * @Description: 同步账簿信息
	 * @return
	 * @throws Exception    设定文件
	 */
	public static String syncLedgers(String ledgerId) throws Exception {
		JSONObject jo = new JSONObject() ;
		
		try {
			// 执行账簿信息同步处理
			XCGLCacheUtil.syncLedgers(ledgerId);
			
			// 提示信息
			jo.put("flag", "0") ;
			jo.put("msg", "");
			
		} catch (Exception e) {
			log.error(e.getMessage());
			// 提示信息
			jo.put("flag", "0") ;
			jo.put("msg", e.getMessage());
		}
		
		return jo.toString() ;
	}
	
	/**
	 * @Title: syncVCat 
	 * @Description: 同步凭证分类信息 
	 * @return
	 * @throws Exception    设定文件
	 */
	public static String syncVCat() throws Exception {
		JSONObject jo = new JSONObject() ;
		
		try {
			// 执行凭证分类信息同步处理
			XCGLCacheUtil.syncVCategory();
			
			// 提示信息
			jo.put("flag", "0") ;
			jo.put("msg", "");
			
		} catch (Exception e) {
			log.error(e.getMessage());
			// 提示信息
			jo.put("flag", "0") ;
			jo.put("msg", e.getMessage());
		}
		
		return jo.toString() ;
	}
	
	/**
	 * @Title: clearLedgerCachedData 
	 * @Description: 清除账簿的缓存数据
	 * @param ledgerId
	 * @throws Exception    设定文件 
	 * @return String    返回类型
	 */
	public static String clearLedgerCachedData(String ledgerId) throws Exception {
		JSONObject jo = new JSONObject() ;
		
		try {
			// 清除账簿的缓存数据
			XCGLCacheUtil.clearLedgerCachedData(ledgerId);
			
			// 提示信息
			jo.put("flag", "0") ;
			jo.put("msg", "");
			
		} catch (Exception e) {
			log.error(e.getMessage());
			// 提示信息
			jo.put("flag", "0") ;
			jo.put("msg", e.getMessage());
		}
		
		return jo.toString() ;
	}
	
	/**
	 * @Title: clearHrcyCachedData 
	 * @Description: 清除会计科目体系缓存数据
	 * @param hrcyId
	 * @throws Exception    设定文件 
	 * @return String    返回类型
	 */
	public static String clearHrcyCachedData(String hrcyId) throws Exception {
		JSONObject jo = new JSONObject() ;
		
		try {
			// 执行共享辅助核算信息同步处理
			XCGLCacheUtil.clearHrcyCachedData(hrcyId);
			
			// 提示信息
			jo.put("flag", "0") ;
			jo.put("msg", "");
			
		} catch (Exception e) {
			log.error(e.getMessage());
			// 提示信息
			jo.put("flag", "0") ;
			jo.put("msg", e.getMessage());
		}
		
		return jo.toString() ;
	}
	
	/**
	 * @Title: handlerEntryCCID 
	 * @Description: 处理凭证分录的CCID信息
	 * @param ledgerId
	 * @param accId
	 * @param segments
	 * @throws Exception    设定文件 
	 * @return String    返回类型
	 */
	public static String handlerEntryCCID(String ledgerId,String accId,String segments) throws Exception {
		JSONObject jo = new JSONObject() ;
		
		try {
			// 辅助核算段信息
			HashMap<String,String> segMap =  new HashMap<String,String>() ;
			
			// 将字符串转换为JSON数组
			JSONArray ja = new JSONArray(segments);
			if(ja.length() >0){
				for(int i=0; i<ja.length(); i++){
					String source = ja.getString(i) ;
					JSONObject obj = new JSONObject(source) ;
					segMap.put(obj.getString("SEG_CODE").toUpperCase(), obj.getString("SEG_VAL_ID")) ;
				}
			}
			
			// 获取凭证处理服务类
			VoucherHandlerService service = (VoucherHandlerService) AppContext.getApplicationContext().getBean("voucherHandlerService") ;
			
			// 处理凭证分录CCID
			jo = service.handlerCCID(ledgerId, accId, segMap) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			// 提示信息
			jo.put("flag", "1") ;
			jo.put("ccid", "") ;
			jo.put("ccid_name", "") ;
			jo.put("msg", e.getMessage()) ;
		}
		
		return jo.toString()  ;
	}
	
	/**
	 * @Title: getVoucherSerialNo 
	 * @Description:  获取凭证编号 
	 * @param ledgerId
	 * @param categoryId
	 * @param periodCode
	 * @param userId
	 * @return
	 * @throws Exception    设定文件
	 */
	public static String getVoucherSerialNo(String ledgerId,String categoryId,String periodCode,String userId) throws Exception {
		// 获取通用服务类
		RuleService ruleService = (RuleService) AppContext.getApplicationContext().getBean("ruleService") ;
		
		// 拆分期间
		String year = periodCode.substring(0, 4) ;
		String month = periodCode.substring(5, 7) ;
		
		// 计算凭证流水号
		String ruleCode = XCGLConstants.RUEL_TYPE_PZ ;
		String serialNum = ruleService.getNextSerialNum(ruleCode,ledgerId,categoryId,year,month,userId) ;
		
		return serialNum ;
	}
	
	/**
	 * @Title: isCreateAccount 
	 * @Description: 判断当前账簿的建账标志
	 * 		1-已建账, 0-未建账
	 * @param ledgerId 账簿Id
	 * @param type 科目与现金流量期初建账： BA-科目期初，CA-现金流期初
	 * @return
	 * @throws Exception    设定文件
	 */
	public static int isCreateAccount(String ledgerId,String type)throws Exception {
		int flag = 0 ;
		
		try {
			// 查询账簿信息
			LedgerService service = (LedgerService) AppContext.getApplicationContext().getBean("ledgerService") ;
			HashMap<String,String> map = service.isCreateAccount(ledgerId) ;
			
			// 获取建账标志
			String val = null ;
			if("BA".equals(type)){
				val = map.get(XCGLConstants.IS_CREATE_BA) ;
				
			}else if("CA".equals(type)){
				val = map.get(XCGLConstants.IS_CREATE_CA) ;
				
			}else{
			}
			
			// 建账标志
			if("Y".equals(val)){
				flag = 1 ;
			}else{
				flag = 0 ;
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception("获取是否建账标志出错!") ;
		}
		
		return flag ;
	}
	
	/**
	 * @Title: getAccountDate 
	 * @Description: 根据账簿初始化会计日期
	 * @param ledgerId
	 * @return
	 * @throws Exception    设定文件
	 */
	public static String initialAccountDate(String ledgerId) throws Exception {
		LedgerPeriodManageService service = (LedgerPeriodManageService) AppContext.getApplicationContext().getBean("ledgerPeriodManageService") ;
		
		return service.getAccountDate(ledgerId) ;
	}
	
	/**
	 * @Title: viewVoucher 
	 * @Description: 查询凭证信息
	 * @param headId
	 * @return
	 * @throws Exception    设定文件
	 */
	public static String viewVoucher(String headId) throws Exception {
		String jsonStr = "{}" ;
		
		try {
			
			// 执行凭证查询
			VoucherHandlerService service = (VoucherHandlerService) AppContext.getApplicationContext().getBean("voucherHandlerService") ;
			VHead bean = service.getVHead(headId) ;

			// 凭证创建日期
			String accDate = XCDateUtil.date2StrBy24(bean.getCreationDate()) ;
			
			// 封装返回参数
			JSONObject jo = new JSONObject() ;

			jo.put("ledger", bean.getLedgerId()) ;
			jo.put("accountDate", accDate) ;
			jo.put("ldPeriod", bean.getPeriodCode()) ;
			jo.put("v_category", bean.getCategoryId()) ;								// 凭证分类
			jo.put("v_serial_no", bean.getSerialNum()==null?"":bean.getSerialNum()) ; 	// 凭证号
			jo.put("v_summary", bean.getSummary()==null?"":bean.getSummary()) ; 		// 凭证摘要
			jo.put("v_attchment_num", bean.getAttchTotal()) ;							// 附件张数
			jo.put("v_head_id", bean.getHeadId()) ;										// 凭证头ID
			jo.put("v_src_code", bean.getSrcCode()==null?"":bean.getSrcCode()) ;		// 凭证来源
			jo.put("v_write_off", bean.getIsWriteOff()==null?"":bean.getIsWriteOff()) ;	// 冲销标志
			jo.put("v_write_off_num", bean.getWriteOffNum()==null?"":bean.getWriteOffNum()) ;		// 冲销凭证号
			jo.put("v_total_dr", bean.getTotalDR()) ;
			jo.put("v_total_cr", bean.getTotalCR()) ;
			jo.put("v_is_signed", bean.getIsSigned()) ;	// 是否需要签字
			jo.put("v_status", bean.getVStatus()) ;	// 凭证状态
			jo.put("v_template_type", bean.getTemplateType()) ;	// 凭证模板类型
			jo.put("v_created_name", bean.getCreatedName()) ;
			jo.put("v_verifier", bean.getVerifier()==null?"":bean.getVerifier()) ;
			jo.put("v_signatory", bean.getSignatory()==null?"":bean.getSignatory()) ;
			jo.put("v_bookkeeper", bean.getBookkeeper()==null?"":bean.getBookkeeper()) ;
			
			jsonStr = jo.toString() ;
			
		} catch (Exception e) {
			jsonStr = "{}" ;
		}
		
		return jsonStr ;
	}
}
