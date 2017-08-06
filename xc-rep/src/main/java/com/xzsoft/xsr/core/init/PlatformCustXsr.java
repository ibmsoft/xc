package com.xzsoft.xsr.core.init;

import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.xzsoft.xip.framework.util.AppContext;
import com.xzsoft.xip.platform.cust.PlatformCustInterface;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xsr.core.mapper.SessionInitMapper;
import com.xzsoft.xsr.core.modal.DataCenter;
import com.xzsoft.xsr.core.util.XSRUtil;

/**
 * 实现平台提供PlatformCustInterface接口
 * 该接口作用：基于平台二次开发的客户化操作
 */
public class PlatformCustXsr implements PlatformCustInterface {
	// 日志记录器
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PlatformCustXsr.class) ;

	/**
	 * 自定义登录初始化方法
	 * 同时需要在参数管理页面配置[自定义工程启动时初始化(CUST_CONTEXT_INIT)]参数
	 * 添加该实现类的类路径
	 */
	@Override
	public void custLoginInit(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		//报表系统session属性
		removeXsrSession(session);
		//设置报表系统所需session属性
		initXsrSession(session);
	}

	/**
	 * WEB工程启动的时候要执行的初始化方法
	 */
	@Override
	public void custContextInitialized(ServletContextEvent event) {
	}

	/**
	 * WEB工程关闭的时候要执行的方法
	 */
	@Override
	public void custContextDestroyed(ServletContextEvent event) {
	}

	/**
	 * 清除报表系统session属性
	 */
	private void removeXsrSession(HttpSession session) {
		// 默认表套
		session.setAttribute(SessionVariableXsr.suitId, "");
		session.setAttribute(SessionVariableXsr.suitCode, "");
		session.setAttribute(SessionVariableXsr.suitName, "");
		// 默认数据中心
		session.setAttribute(SessionVariableXsr.defaultDcId, "");
		session.setAttribute(SessionVariableXsr.defaultDcCode, "");
		session.setAttribute(SessionVariableXsr.defaultDcName, "");
		// 默认期间
		session.setAttribute(SessionVariableXsr.defaultPeriodType, "");
		session.setAttribute(SessionVariableXsr.periodId, "");
		session.setAttribute(SessionVariableXsr.periodCode, "");
		session.setAttribute(SessionVariableXsr.periodName, "");
		// 公司
		session.setAttribute(SessionVariableXsr.entityId, "");
		session.setAttribute(SessionVariableXsr.entityCode, "");
		session.setAttribute(SessionVariableXsr.entityName, "");
		// 币种
		session.setAttribute(SessionVariableXsr.cnyId, "");
		session.setAttribute(SessionVariableXsr.cnyCode, "");
		session.setAttribute(SessionVariableXsr.cnyName, "");
		// 默认模板集
		session.setAttribute(SessionVariableXsr.modalTypeId, "");
		session.setAttribute(SessionVariableXsr.modalTypeCode, "");
		session.setAttribute(SessionVariableXsr.modalTypeName, "");
		// 默认模板集（编辑）
		session.setAttribute(SessionVariableXsr.modalTypeId_Mdl, "");
		session.setAttribute(SessionVariableXsr.modalTypeCode_Mdl, "");
		session.setAttribute(SessionVariableXsr.modalTypeName_Mdl, "");
	}
	
	/**
	 * @Title: initXsrSession 
	 * @Description: 设置报表系统所需session属性
	 * @param session    设定文件
	 */
	private void initXsrSession(HttpSession session) {
		String userId = CurrentSessionVar.getUserId() ;
		//采用 用户角色授权
		String roleId = CurrentSessionVar.getRoleId() ;
		
		// 2、对报表Session变量进行赋值
		SessionInitMapper sessionInitMapper = (SessionInitMapper) AppContext.getApplicationContext().getBean("sessionInitMapper");
				
		try {
			// 默认表套
			String suitId = "";
			String suitCode="";
			String suitName="";	
			String defaultPeriodType = "";
			List<HashMap> suitMap =sessionInitMapper.getSuitByUserId(userId);
			if (suitMap.size()==0) {
				suitMap =sessionInitMapper.getSuitByRoleId(roleId);
			}
			if (suitMap.size()>0) {
				suitId = suitMap.get(0).get("SUIT_ID").toString();
				suitCode=suitMap.get(0).get("SUIT_CODE").toString();
				suitName=suitMap.get(0).get("SUIT_NAME").toString();
				if(suitMap.get(0).size() > 3) {
					defaultPeriodType = suitMap.get(0).get("PERIOD_TYPE").toString();
				} else {
					defaultPeriodType = "M";
				}
			}
			session.setAttribute(SessionVariableXsr.suitId, suitId);
			session.setAttribute(SessionVariableXsr.suitCode, suitCode);
			session.setAttribute(SessionVariableXsr.suitName, suitName);
			session.setAttribute(SessionVariableXsr.defaultPeriodType, defaultPeriodType);
		
			// 默认数据中心
			if(null != suitId) {
				DataCenter dc = sessionInitMapper.getDefauleDcBySuitId(suitId);
				if(null != dc) {
					session.setAttribute(SessionVariableXsr.defaultDcId, dc.getDc_id());
					session.setAttribute(SessionVariableXsr.defaultDcCode, dc.getDc_code());
					session.setAttribute(SessionVariableXsr.defaultDcName, dc.getDc_name());
				}
			}
			
			// 默认期间
			String currPeriod = XSRUtil.getPeriodCode();
			String periodId="";
			String periodName="";
			HashMap params = new HashMap();
			params.put("suitId", suitId);
			params.put("periodCode", currPeriod);
			List<HashMap> periodMap =sessionInitMapper.getPeriodInfo(params);
			if (periodMap.size()>0) {
				periodId=periodMap.get(0).get("PERIOD_ID").toString();
				periodName=periodMap.get(0).get("PERIOD_NAME").toString();
			}
			session.setAttribute(SessionVariableXsr.periodId, periodId);
			session.setAttribute(SessionVariableXsr.periodCode, currPeriod);
			session.setAttribute(SessionVariableXsr.periodName, periodName);
			
			// 公司
			String entityId ="";
			String entityCode="";
			String entityName="";
			params.clear();
			params.put("suitId", suitId);
			params.put("userId", userId);
			params.put("roleId", roleId);
			List<HashMap> entityMap=sessionInitMapper.getEntityInfo(params);
			if (entityMap.size()>0) {
				entityId = entityMap.get(0).get("TARGET_ID").toString();
				entityCode=entityMap.get(0).get("ORG_CODE").toString();
				entityName=entityMap.get(0).get("ORG_NAME").toString();
			}
			session.setAttribute(SessionVariableXsr.entityId, entityId);
			session.setAttribute(SessionVariableXsr.entityCode, entityCode);
			session.setAttribute(SessionVariableXsr.entityName, entityName);
			
			// 币种
			String currencyId="";
			String currencyCode="";
			String currencyName="";
			List<HashMap> currencyMap=sessionInitMapper.getCurrencyInfo(suitId);
			if (currencyMap.size()>0) {
				currencyId = currencyMap.get(0).get("CURRENCY_ID").toString();
				currencyCode=currencyMap.get(0).get("CURRENCY_CODE").toString();
				currencyName=currencyMap.get(0).get("CURRENCY_NAME").toString();
			}
			session.setAttribute(SessionVariableXsr.cnyId, currencyId);
			session.setAttribute(SessionVariableXsr.cnyCode, currencyCode);
			session.setAttribute(SessionVariableXsr.cnyName, currencyName);
			
			// 默认模板集
			String mtId ="";
			String mtCode="";
			String mtName="";
			params.clear();
			params.put("suitId", suitId);
			params.put("userId", userId);
			params.put("roleId", roleId);
			params.put("mType", "MT");
			List<HashMap> mtMap=sessionInitMapper.getModalTypeInfo(params);
			if (mtMap.size()>0) {
				mtId = mtMap.get(0).get("TARGET_ID").toString();
				mtCode=mtMap.get(0).get("MODALTYPE_CODE").toString();
				mtName=mtMap.get(0).get("MODALTYPE_NAME").toString();
			}
			session.setAttribute(SessionVariableXsr.modalTypeId, mtId);
			session.setAttribute(SessionVariableXsr.modalTypeCode, mtCode);
			session.setAttribute(SessionVariableXsr.modalTypeName, mtName);
			
			// 默认模板集（编辑）
			String mteId ="";
			String mteCode="";
			String mteName="";
			params.put("mType", "MTE");
			List<HashMap> mteMap=sessionInitMapper.getModalTypeInfo(params);
			if (mteMap.size()>0) {
				mteId = mteMap.get(0).get("TARGET_ID").toString();
				mteCode=mteMap.get(0).get("MODALTYPE_CODE").toString();
				mteName=mteMap.get(0).get("MODALTYPE_NAME").toString();
			}
			session.setAttribute(SessionVariableXsr.modalTypeId_Mdl, mteId);
			session.setAttribute(SessionVariableXsr.modalTypeCode_Mdl, mteCode);
			session.setAttribute(SessionVariableXsr.modalTypeName_Mdl, mteName);
		//for test
		suitId=session.getAttribute(SessionVariableXsr.suitId).toString();
		suitCode=session.getAttribute(SessionVariableXsr.suitCode).toString();
		suitName=session.getAttribute(SessionVariableXsr.suitName).toString(); 
		defaultPeriodType = session.getAttribute(SessionVariableXsr.defaultPeriodType).toString(); 
		periodId=session.getAttribute(SessionVariableXsr.periodId).toString();
		currPeriod=session.getAttribute(SessionVariableXsr.periodCode).toString();
		periodName=session.getAttribute(SessionVariableXsr.periodName).toString(); 
		currencyId=session.getAttribute(SessionVariableXsr.cnyId).toString();
		currencyCode=session.getAttribute(SessionVariableXsr.cnyCode).toString();
		currencyName=session.getAttribute(SessionVariableXsr.cnyName).toString(); 
		
		
		log.debug("表套ID："+suitId+"; 表套code:"+suitId+"； 表套Name:"+suitName + "; 默认期间类型：" + defaultPeriodType);
		log.debug("期间ID："+periodId+"; 期间code:"+currPeriod+"；期间Name:"+periodName);
		log.debug("币种ID："+currencyId+"; 币种code:"+currencyCode+"；币种Name:"+currencyName);
			
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	@Override
	public void custRoleChanged(HttpServletRequest arg0, String arg1) {
	}

}
