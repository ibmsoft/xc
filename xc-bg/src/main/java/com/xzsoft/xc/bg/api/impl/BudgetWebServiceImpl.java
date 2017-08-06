package com.xzsoft.xc.bg.api.impl;

import javax.jws.WebService;

import org.springframework.stereotype.Component;

import com.xzsoft.xc.bg.api.BudgetWebService;
import com.xzsoft.xc.bg.service.BgAuditService;
import com.xzsoft.xc.bg.service.BudgetDataHandlerService;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xip.framework.util.AppContext;

/**
 * @ClassName: BudgetWebServiceImpl 
 * @Description: 预算管理WebService服务接口实现类
 * @author linp
 * @date 2016年3月10日 上午9:43:12 
 *
 */
@Component
@WebService(endpointInterface = "com.xzsoft.xc.bg.api.BudgetWebService",targetNamespace = "http://ws.xzsoft.com")
public class BudgetWebServiceImpl implements BudgetWebService {
	
	private BudgetDataHandlerService getBudgetDataHandlerService(){
		return (BudgetDataHandlerService) AppContext.getApplicationContext().getBean("budgetDataHandlerService") ;
	}
	
	private BgAuditService getBgAuditService(){
		return (BgAuditService) AppContext.getApplicationContext().getBean("bgAuditService") ;
	}
	
	/*
	 * 
	  * <p>Title: budgetDataInsertAndAuditDate</p>
	  * <p>Description: 预算余额汇总和更新审批通过日期</p>
	  * @param bizId
	  * @param loginName
	  * @param password
	  * @param opType
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.bg.api.BudgetWebService#budgetDataInsertAndAuditDate(java.lang.String,java.lang.String,java.lang.String,java.lang.String)
	 */
	@Override
	public String budgetDataInsertAndAuditDate(String bizId,String loginName,String password,String opType) throws Exception {
		StringBuffer xmlstr = new StringBuffer();
		
        try {
            XCUtil.setSessionUserId(loginName,password);
        } catch (Exception e) {
            return "<result><flag>1</flag><msg>传入的用户名" + loginName + "在平台中不存在，请确认。</msg></result>";
        }
		
		try {
			this.getBudgetDataHandlerService().budgetDataInsert(bizId,loginName,opType);
			//提示信息
			xmlstr.append("<result>")
			.append("<flag>").append("0").append("</flag>")
			.append("<msg>").append("处理预算明细表并更新审批通过日期成功").append("</msg>")
			.append("</result>");
		} catch (Exception e) {
			//提示信息
			xmlstr.append("<result>")
			.append("<flag>").append("1").append("</flag>")
			.append("<msg>").append(e.getMessage()).append("</msg>")
			.append("</result>");
		}
		return  xmlstr.toString();
	}
	/*
	 * 
	  * <p>Title: updateBgInsCode</p>
	  * <p>Description: 更新审批流程实例编码</p>
	  * @param insCode
	  * @param bizId
	  * @param loginName
	  * @param password
	  * @return
	  * @see com.xzsoft.xc.bg.api.BudgetWebService#updateBgInsCode(java.lang.String,java.lang.String,java.lang.String,java.lang.String)
	 */
	@Override
	public String updateBgInsCode(String insCode,String bizId,String loginName,String password) throws Exception{
		StringBuffer xmlstr = new StringBuffer();
		
        try {
        	XCUtil.setSessionUserId(loginName,password);
        } catch (Exception e) {
            return "<result><flag>1</flag><msg>传入的用户名" + loginName + "在平台中不存在，请确认。</msg></result>";
        }
		
		try {
			this.getBgAuditService().updateBgInsCode(insCode,bizId,loginName);
			
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
	  * <p>Title: updateBgAuditStatus</p>
	  * <p>Description: 更新审批流程审批状态</p>
	  * @param bizId
	  * @param bizStatusCat
	  * @param bizStatusCatDesc
	  * @param loginName
	  * @param password
	  * @return
	  * @see com.xzsoft.xc.bg.api.BudgetWebService#updateBgAuditStatus(java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String)
	 */
	@Override
	public String updateBgAuditStatus(String bizId,String bizStatusCat,String bizStatusCatDesc,String loginName,String password) throws Exception{
		StringBuffer xmlstr = new StringBuffer();
		
        try {
        	XCUtil.setSessionUserId(loginName,password);
        } catch (Exception e) {
            return "<result><flag>1</flag><msg>传入的用户名" + loginName + "在平台中不存在，请确认。</msg></result>";
        }
		
		try {
			this.getBgAuditService().updateBgAuditStatus(bizId,bizStatusCat,bizStatusCatDesc,loginName);
			
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
	  * <p>Title: updateBgAuditDate</p>
	  * <p>Description: 更新流程审批通过日期</p>
	  * @param bizId
	  * @param loginName
	  * @param password
	  * @return
	  * @see com.xzsoft.xc.bg.api.BudgetWebService#updateBgAuditDate(java.lang.String,java.lang.String,java.lang.String)
	 */
	@Override
	public String updateBgAuditDate(String bizId,String loginName,String password) throws Exception{
		StringBuffer xmlstr = new StringBuffer();
		
        try {
        	XCUtil.setSessionUserId(loginName,password);
        } catch (Exception e) {
            return "<result><flag>1</flag><msg>传入的用户名" + loginName + "在平台中不存在，请确认。</msg></result>";
        }
		
		try {
			this.getBgAuditService().updateBgAuditDate(bizId,loginName);
			
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
	/*
	 * 
	  * <p>Title: updateBgAuditUsers</p>
	  * <p>Description: 更新当前审批人信息</p>
	  * @param bizId
	  * @param nodeApprovers
	  * @param loginName
	  * @param password
	  * @return
	  * @see com.xzsoft.xc.bg.api.BudgetWebService#updateBgAuditUsers(java.lang.String,java.lang.String,java.lang.String,java.lang.String)
	 */
	@Override
	public String updateBgAuditUsers(String bizId,String nodeApprovers,String loginName,String password) throws Exception{
		StringBuffer xmlstr = new StringBuffer();
		
        try {
        	XCUtil.setSessionUserId(loginName,password);
        } catch (Exception e) {
            return "<result><flag>1</flag><msg>传入的用户名" + loginName + "在平台中不存在，请确认。</msg></result>";
        }
		
		try {
			this.getBgAuditService().updateBgAuditUsers(bizId,nodeApprovers,loginName);
			
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
		
		return xmlstr.toString();
	}
	
}