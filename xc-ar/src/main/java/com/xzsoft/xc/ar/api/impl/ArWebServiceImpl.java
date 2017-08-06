package com.xzsoft.xc.ar.api.impl;

import javax.jws.WebService;

import org.springframework.stereotype.Component;

import com.xzsoft.xc.ar.api.ArWebService;
import com.xzsoft.xc.ar.service.ArAuditService;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xip.framework.util.AppContext;


/**
 * 
 * @ClassName ArWebServiceImpl
 * @Description 应收WebService服务接口实现类
 * @author 任建建
 * @date 2016年7月7日 下午12:52:31
 */
@Component
@WebService(endpointInterface = "com.xzsoft.xc.ar.api.ArWebService",targetNamespace = "http://ws.xzsoft.com")
public class ArWebServiceImpl implements ArWebService {
	
	private ArAuditService getArAuditService(){
		return (ArAuditService) AppContext.getApplicationContext().getBean("arAuditService") ;
	}
	
	/*
	 * 
	  * <p>Title updateArInsCode</p>
	  * <p>Description 更新审批流程实例编码</p>
	  * @param arCatCode
	  * @param insCode
	  * @param bizId
	  * @param loginName
	  * @param password
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.api.ArWebService#updateArInsCode(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String updateArInsCode(String arCatCode,String insCode,String bizId,String loginName,String password) throws Exception{
		StringBuffer xmlstr = new StringBuffer();
		
        try {
        	XCUtil.setSessionUserId(loginName,password);
        } catch (Exception e) {
            return "<result><flag>1</flag><msg>传入的用户名" + loginName + "在平台中不存在，请确认。</msg></result>";
        }
		
		try {
			this.getArAuditService().updateArInsCode(arCatCode,insCode,bizId);
			
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
	  * <p>Title updateArAuditStatus</p>
	  * <p>Description 更新审批流程审批状态</p>
	  * @param arCatCode
	  * @param bizId
	  * @param bizStatusCat
	  * @param bizStatusCatDesc
	  * @param loginName
	  * @param password
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.api.ArWebService#updateArAuditStatus(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String updateArAuditStatus(String arCatCode,String bizId,String bizStatusCat,String bizStatusCatDesc,String loginName,String password) throws Exception{
		StringBuffer xmlstr = new StringBuffer();
		
        try {
        	XCUtil.setSessionUserId(loginName,password);
        } catch (Exception e) {
            return "<result><flag>1</flag><msg>传入的用户名" + loginName + "在平台中不存在，请确认。</msg></result>";
        }
		
		try {
			this.getArAuditService().updateArAuditStatus(arCatCode,bizStatusCat,bizStatusCatDesc,bizId);
			
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
	  * <p>Title updateArAuditDate</p>
	  * <p>Description 更新审批通过日期</p>
	  * @param arCatCode
	  * @param bizId
	  * @param loginName
	  * @param password
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.api.ArWebService#updateArAuditDate(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String updateArAuditDate(String arCatCode,String bizId, String loginName,
			String password) throws Exception {
		StringBuffer xmlstr = new StringBuffer();
		
        try {
        	XCUtil.setSessionUserId(loginName,password);
        } catch (Exception e) {
            return "<result><flag>1</flag><msg>传入的用户名" + loginName + "在平台中不存在，请确认。</msg></result>";
        }
		
		try {
			this.getArAuditService().updateArAuditDate(arCatCode,bizId);
			
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
