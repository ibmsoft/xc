package com.xzsoft.xc.ex.api.impl;

import javax.jws.WebParam;
import javax.jws.WebService;

import org.springframework.stereotype.Component;

import com.xzsoft.xc.ex.api.XCEXWebService;
import com.xzsoft.xc.ex.service.AuditService;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xip.framework.util.AppContext;

/**
 * @ClassName: XCEXWebServiceImpl 
 * @Description: 费用报销对外服务API接口实现类
 * @author linp
 * @date 2016年3月10日 下午5:22:41 
 *
 */
@Component
@WebService(endpointInterface = "com.xzsoft.xc.ex.api.XCEXWebService",targetNamespace = "http://ws.xzsoft.com")
public class XCEXWebServiceImpl implements XCEXWebService {
	
	// 获取AuditService
	private AuditService getAuditService(){
		return (AuditService) AppContext.getApplicationContext().getBean("auditService");
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: updateInsCode</p> 
	 * <p>Description: 更新单据审批流程实例编码  </p> 
	 * @param insCode
	 * @param bizId
	 * @param loginName
	 * @return 
	 * @see com.xzsoft.xc.ex.api.XCEXWebService#updateInsCode(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String updateInsCode(String insCode, String bizId, String loginName,String password) {
		StringBuffer xmlstr = new StringBuffer() ;
		
        try {
        	XCUtil.setSessionUserId(loginName,password);
        } catch (Exception e) {
            return "<result><flag>1</flag><msg>传入的用户名" + loginName + "在平台中不存在，请确认。</msg></result>";
        }
		
		try {
			this.getAuditService().updateInsCode(insCode, bizId, loginName);
			
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
		
		return xmlstr.toString() ;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: updateAuditStatus</p> 
	 * <p>Description: 更新单据审批状态信息 </p> 
	 * @param bizId
	 * @param bizStatusCat
	 * @param bizStatusCatDesc
	 * @param catCode
	 * @param applyDocId
	 * @param loginName
	 * @param password
	 * @return 
	 * @see com.xzsoft.xc.ex.api.XCEXWebService#updateAuditStatus(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String updateAuditStatus(String bizId, String bizStatusCat, String bizStatusCatDesc, 
			String catCode,String applyDocId, String loginName, String password) {
		
		StringBuffer xmlstr = new StringBuffer() ;
		
        try {
        	XCUtil.setSessionUserId(loginName,password);
        } catch (Exception e) {
            return "<result><flag>1</flag><msg>传入的用户名" + loginName + "在平台中不存在，请确认。</msg></result>";
        }
		
		try {
			this.getAuditService().updateAuditStatus(bizId, bizStatusCat, bizStatusCatDesc, catCode, applyDocId, loginName) ;
			
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
		
		return xmlstr.toString() ;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: updateAuditDate</p> 
	 * <p>Description: 更新单据审批通过日期 </p> 
	 * @param bizId
	 * @param loginName
	 * @return 
	 * @see com.xzsoft.xc.ex.api.XCEXWebService#updateAuditDate(java.lang.String, java.lang.String)
	 */
	@Override
	public String updateAuditDate(String bizId, String loginName,String password) {
		StringBuffer xmlstr = new StringBuffer() ;
		
        try {
        	XCUtil.setSessionUserId(loginName,password);
        } catch (Exception e) {
            return "<result><flag>1</flag><msg>传入的用户名" + loginName + "在平台中不存在，请确认。</msg></result>";
        }
		
		try {
			this.getAuditService().updateAuditDate(bizId, loginName) ;
			
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
		
		return xmlstr.toString() ;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: updateAuditDateAndBudgetFact</p> 
	 * <p>Description: 更新单据审批通过日期并记录预算占用数 </p> 
	 * @param bizId
	 * @param loginName
	 * @param password
	 * @return 
	 * @see com.xzsoft.xc.ex.api.XCEXWebService#updateAuditDateAndBudgetFact(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String updateAuditDateAndBudgetFact(    		
			@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "bizId") String bizId,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "loginName") String loginName,
			@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "password") String password) {
		StringBuffer xmlstr = new StringBuffer() ;
		
        try {
        	XCUtil.setSessionUserId(loginName,password);
        } catch (Exception e) {
            return "<result><flag>1</flag><msg>传入的用户名" + loginName + "在平台中不存在，请确认。</msg></result>";
        }
		
		try {
			// 更新单据审批通过日期并记录预算占用数
			this.getAuditService().updateAuditDateAndBudgetFact(bizId, loginName);
			
			//返回信息
			xmlstr.append("<result>")
			.append("<flag>").append("0").append("</flag>")
			.append("<msg>").append("更新单据审批通过日期并记录预算占用数成功").append("</msg>")
			.append("</result>");
			
		} catch (Exception e) {
			
			xmlstr.append("<result>")
			.append("<flag>").append("1").append("</flag>")
			.append("<msg>更新单据审批通过日期并记录预算占用数失败：").append(e.getMessage()).append("</msg>")
			.append("</result>");
		}
		
		return xmlstr.toString() ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: updateAuditUsers</p> 
	 * <p>Description:更新单据当前审批人信息</p> 
	 * @param bizId
	 * @param nodeApprovers
	 * @param loginName
	 * @return 
	 * @see com.xzsoft.xc.ex.api.XCEXWebService#updateAuditUsers(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String updateAuditUsers(String bizId, String nodeApprovers,String loginName,String password) {
		StringBuffer xmlstr = new StringBuffer() ;
		
        try {
        	XCUtil.setSessionUserId(loginName,password);
        } catch (Exception e) {
            return "<result><flag>1</flag><msg>传入的用户名" + loginName + "在平台中不存在，请确认。</msg></result>";
        }
		
		try {
			this.getAuditService().updateAuditUsers(bizId, nodeApprovers, loginName) ;
			
			// 返回信息
			xmlstr.append("<result>")
			.append("<flag>").append("0").append("</flag>")
			.append("<msg>").append("更新当前审批人信息成功").append("</msg>")
			.append("</result>");
			
		} catch (Exception e) {
			xmlstr.append("<result>")
			.append("<flag>").append("1").append("</flag>")
			.append("<msg>更新当前审批人信息失败：").append(e.getMessage()).append("</msg>")
			.append("</result>");
		}
		
		return xmlstr.toString() ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: recordBudgetFact</p> 
	 * <p>Description: 记录预算占用数 </p> 
	 * @param bizId
	 * @param loginName
	 * @param password
	 * @return 
	 * @see com.xzsoft.xc.ex.api.XCEXWebService#recordBudgetFact(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String recordBudgetFact(    		
			@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "bizId") String bizId,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "loginName") String loginName,
			@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "password") String password) {
		StringBuffer xmlstr = new StringBuffer() ;
		
        try {
        	XCUtil.setSessionUserId(loginName,password);
        } catch (Exception e) {
            return "<result><flag>1</flag><msg>传入的用户名" + loginName + "在平台中不存在，请确认。</msg></result>";
        }
		
		try {
			// 记录预算占用数
			this.getAuditService().recordBudgetFact(bizId, loginName);
			
			//返回信息
			xmlstr.append("<result>")
			.append("<flag>").append("0").append("</flag>")
			.append("<msg>").append("记录预算占用数成功").append("</msg>")
			.append("</result>");
			
		} catch (Exception e) {
			
			xmlstr.append("<result>")
			.append("<flag>").append("1").append("</flag>")
			.append("<msg>记录预算占用数失败：").append(e.getMessage()).append("</msg>")
			.append("</result>");
		}
		
		return xmlstr.toString() ;
	}

}
