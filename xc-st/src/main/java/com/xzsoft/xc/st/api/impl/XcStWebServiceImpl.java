package com.xzsoft.xc.st.api.impl;

import javax.jws.WebService;

import org.springframework.stereotype.Component;

import com.xzsoft.xc.st.api.XcStWebService;
import com.xzsoft.xc.st.service.XcStAuditService;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xip.framework.util.AppContext;


/**
 * 
  * @ClassName XcStWebServiceImpl
  * @Description 库存WebService服务接口实现类
  * @author RenJianJian
  * @date 2016年12月8日 下午3:20:55
 */
@Component
@WebService(endpointInterface = "com.xzsoft.xc.st.api.XcStWebService",targetNamespace = "http://ws.xzsoft.com")
public class XcStWebServiceImpl implements XcStWebService {
	
	private XcStAuditService getXcStAuditService(){
		return (XcStAuditService) AppContext.getApplicationContext().getBean("xcStAuditService") ;
	}
	
	/*
	 * 
	  * <p>Title updateXcStInsCode</p>
	  * <p>Description 更新审批流程实例编码</p>
	  * @param stCatCode
	  * @param insCode
	  * @param bizId
	  * @param loginName
	  * @param password
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.st.api.XcStWebService#updateXcStInsCode(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String updateXcStInsCode(String stCatCode,String insCode,String bizId,String loginName,String password) throws Exception{
		StringBuffer xmlstr = new StringBuffer();
		
        try {
        	XCUtil.setSessionUserId(loginName,password);
        } catch (Exception e) {
            return "<result><flag>1</flag><msg>传入的用户名" + loginName + "在平台中不存在，请确认。</msg></result>";
        }
		
		try {
			this.getXcStAuditService().updateXcStInsCode(stCatCode, insCode, bizId);
			
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
	  * <p>Title updateXcStAuditStatus</p>
	  * <p>Description 更新审批流程审批状态</p>
	  * @param stCatCode
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
	public String updateXcStAuditStatus(String stCatCode,String bizId,String bizStatusCat,String bizStatusCatDesc,String loginName,String password) throws Exception{
		StringBuffer xmlstr = new StringBuffer();
		
        try {
        	XCUtil.setSessionUserId(loginName,password);
        } catch (Exception e) {
            return "<result><flag>1</flag><msg>传入的用户名" + loginName + "在平台中不存在，请确认。</msg></result>";
        }
		
		try {
			this.getXcStAuditService().updateXcStAuditStatus(stCatCode,bizStatusCat,bizStatusCatDesc,bizId);
			
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
	  * <p>Title updateXcStAuditDate</p>
	  * <p>Description 更新审批通过日期</p>
	  * @param stCatCode
	  * @param bizId
	  * @param loginName
	  * @param password
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.api.ArWebService#updateArAuditDate(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String updateXcStAuditDate(String stCatCode,String bizId, String loginName,
			String password) throws Exception {
		StringBuffer xmlstr = new StringBuffer();
		
        try {
        	XCUtil.setSessionUserId(loginName,password);
        } catch (Exception e) {
            return "<result><flag>1</flag><msg>传入的用户名" + loginName + "在平台中不存在，请确认。</msg></result>";
        }
		
		try {
			this.getXcStAuditService().updateXcStAuditDate(stCatCode,bizId);
			
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
