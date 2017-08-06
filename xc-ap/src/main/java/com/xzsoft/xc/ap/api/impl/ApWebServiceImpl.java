package com.xzsoft.xc.ap.api.impl;

import javax.jws.WebService;

import org.springframework.stereotype.Component;

import com.xzsoft.xc.ap.api.ApWebService;
import com.xzsoft.xc.ap.service.ApAuditService;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xip.framework.util.AppContext;

/**
 * @ClassName: ApWebServiceImpl
 * @Description: 应付WebService服务接口实现类
 * @author 任建建
 * @date 2016年4月26日 上午10:56:00
 */
@Component
@WebService(endpointInterface = "com.xzsoft.xc.ap.api.ApWebService",targetNamespace = "http://ws.xzsoft.com")
public class ApWebServiceImpl implements ApWebService {
	
	private ApAuditService getApAuditService(){
		return (ApAuditService) AppContext.getApplicationContext().getBean("apAuditService") ;
	}
	
	/*
	 * 
	  * <p>Title: updateApInsCode</p>
	  * <p>Description: 更新审批流程实例编码</p>
	  * @param apCatCode
	  * @param insCode
	  * @param bizId
	  * @param loginName
	  * @param password
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.api.ApWebService#updateApInsCode(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String updateApInsCode(String apCatCode,String insCode,String bizId,String loginName,String password) throws Exception{
		StringBuffer xmlstr = new StringBuffer();
		
        try {
        	XCUtil.setSessionUserId(loginName,password);
        } catch (Exception e) {
            return "<result><flag>1</flag><msg>传入的用户名" + loginName + "在平台中不存在，请确认。</msg></result>";
        }
		
		try {
			this.getApAuditService().updateApInsCode(apCatCode,insCode,bizId);
			
			//返回信息
			xmlstr.append("<result>")
			.append("<flag>").append("0").append("</flag>")
			.append("<msg>").append("更新审批流程实例编码成功").append("</msg>")
			.append("</result>");
			
		} catch (Exception e) {
			
			xmlstr.append("<result>")
			.append("<flag>").append("1").append("</flag>")
			.append("<msg>流程实例编码更新失败：").append(e.getMessage()).append("</msg>")
			.append("</result>");
		}
		
		return xmlstr.toString();
	}
	/*
	 * 
	  * <p>Title: updateApAuditStatus</p>
	  * <p>Description: 更新审批流程审批状态</p>
	  * @param apCatCode
	  * @param bizId
	  * @param bizStatusCat
	  * @param bizStatusCatDesc
	  * @param loginName
	  * @param password
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.api.ApWebService#updateApAuditStatus(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String updateApAuditStatus(String apCatCode,String bizId,String bizStatusCat,String bizStatusCatDesc,String loginName,String password) throws Exception{
		StringBuffer xmlstr = new StringBuffer();
		
        try {
        	XCUtil.setSessionUserId(loginName,password);
        } catch (Exception e) {
            return "<result><flag>1</flag><msg>传入的用户名" + loginName + "在平台中不存在，请确认。</msg></result>";
        }
		
		try {
			this.getApAuditService().updateApAuditStatus(apCatCode,bizStatusCat,bizStatusCatDesc,bizId);
			
			//返回信息
			xmlstr.append("<result>")
			.append("<flag>").append("0").append("</flag>")
			.append("<msg>").append("更新审批流程审批状态成功").append("</msg>")
			.append("</result>");
			
		} catch (Exception e) {
			
			xmlstr.append("<result>")
			.append("<flag>").append("1").append("</flag>")
			.append("<msg>更新审批流程审批状态失败：").append(e.getMessage()).append("</msg>")
			.append("</result>");
		}
		
		return xmlstr.toString();
	}
	/*
	 * 
	  * <p>Title: updateApAuditDate</p>
	  * <p>Description: </p>
	  * @param bizId
	  * @param loginName
	  * @param password
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.api.ApWebService#updateApAuditDate(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String updateApAuditDate(String apCatCode,String bizId, String loginName,
			String password) throws Exception {
		StringBuffer xmlstr = new StringBuffer();
		
        try {
        	XCUtil.setSessionUserId(loginName,password);
        } catch (Exception e) {
            return "<result><flag>1</flag><msg>传入的用户名" + loginName + "在平台中不存在，请确认。</msg></result>";
        }
		
		try {
			this.getApAuditService().updateApAuditDate(apCatCode,bizId);
			
			//返回信息
			xmlstr.append("<result>")
			.append("<flag>").append("0").append("</flag>")
			.append("<msg>").append("更新流程审批通过日期成功").append("</msg>")
			.append("</result>");
			
		} catch (Exception e) {
			
			xmlstr.append("<result>")
			.append("<flag>").append("1").append("</flag>")
			.append("<msg>更新流程审批通过日期失败：").append(e.getMessage()).append("</msg>")
			.append("</result>");
		}
		
		return xmlstr.toString();
	}
	
}
