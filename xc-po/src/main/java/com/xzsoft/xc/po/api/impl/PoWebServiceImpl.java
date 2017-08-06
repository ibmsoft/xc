package com.xzsoft.xc.po.api.impl;

import javax.jws.WebService;

import org.springframework.stereotype.Component;

import com.xzsoft.xc.po.api.PoWebService;
import com.xzsoft.xc.po.service.PoAuditService;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xip.framework.util.AppContext;

/**
 * @ClassName: PoWebServiceImpl
 * @Description: 采购模块webService服务接口实现类
 * @author weicw
 * @date 2016年12月9日 上午10:35:37
 * 
 */
@Component
@WebService(endpointInterface = "com.xzsoft.xc.po.api.PoWebService", targetNamespace = "http://ws.xzsoft.com")
public class PoWebServiceImpl implements PoWebService {
	private PoAuditService getPoAuditService() {
		return (PoAuditService) AppContext.getApplicationContext().getBean(
				"poAuditService");
	}

	/**
	 * 
	 * @Title: updatePoInsCode
	 * @Description: 更新审批流程实例编码
	 * @param @param poCatCode
	 * @param @param insCode
	 * @param @param bizId
	 * @param @param loginName
	 * @param @param password
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return String 返回类型
	 */
	@Override
	public String updatePoInsCode(String poCatCode, String insCode,
			String bizId, String loginName, String password) throws Exception {
		StringBuffer xmlstr = new StringBuffer();

		try {
			XCUtil.setSessionUserId(loginName, password);
		} catch (Exception e) {
			return "<result><flag>1</flag><msg>传入的用户名" + loginName
					+ "在平台中不存在，请确认。</msg></result>";
		}

		try {
			this.getPoAuditService().updatePoInsCode(poCatCode, insCode, bizId);

			// 返回信息
			xmlstr.append("<result>").append("<flag>").append("0")
					.append("</flag>").append("<msg>").append("更新审批流程实例编码成功")
					.append("</msg>").append("</result>");

		} catch (Exception e) {

			xmlstr.append("<result>").append("<flag>").append("1")
					.append("</flag>").append("<msg>流程实例编码更新失败：")
					.append(e.getMessage()).append("</msg>")
					.append("</result>");
		}

		return xmlstr.toString();
	}

	/**
	 * 
	 * @Title: updatePoAuditStatus
	 * @Description: 更新审批流程审批状态
	 * @param @param poCatCode
	 * @param @param bizId
	 * @param @param bizStatusCat
	 * @param @param bizStatusCatDesc
	 * @param @param loginName
	 * @param @param password
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return String 返回类型
	 */
	@Override
	public String updatePoAuditStatus(String poCatCode,String bizId,
			String bizStatusCat, String bizStatusCatDesc, String loginName,
			String password) throws Exception {

		StringBuffer xmlstr = new StringBuffer();

		try {
			XCUtil.setSessionUserId(loginName, password);
		} catch (Exception e) {
			return "<result><flag>1</flag><msg>传入的用户名" + loginName
					+ "在平台中不存在，请确认。</msg></result>";
		}

		try {
			this.getPoAuditService().updatePoAuditStatus(poCatCode,
					bizStatusCat, bizStatusCatDesc, bizId);

			// 返回信息
			xmlstr.append("<result>").append("<flag>").append("0")
					.append("</flag>").append("<msg>").append("更新审批流程审批状态成功")
					.append("</msg>").append("</result>");

		} catch (Exception e) {

			xmlstr.append("<result>").append("<flag>").append("1")
					.append("</flag>").append("<msg>更新审批流程审批状态失败：")
					.append(e.getMessage()).append("</msg>")
					.append("</result>");
		}

		return xmlstr.toString();
	}

	/**
	 * 
	 * @Title: updatePoAuditDate
	 * @Description: 更新流程审批通过日期
	 * @param @param poCatCode
	 * @param @param bizId
	 * @param @param loginName
	 * @param @param password
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return String 返回类型
	 */
	@Override
	public String updatePoAuditDate(String poCatCode, String bizId,
			String loginName, String password) throws Exception {
		StringBuffer xmlstr = new StringBuffer();

		try {
			XCUtil.setSessionUserId(loginName, password);
		} catch (Exception e) {
			return "<result><flag>1</flag><msg>传入的用户名" + loginName
					+ "在平台中不存在，请确认。</msg></result>";
		}

		try {
			this.getPoAuditService().updatePoAuditDate(poCatCode, bizId);

			// 返回信息
			xmlstr.append("<result>").append("<flag>").append("0")
					.append("</flag>").append("<msg>").append("更新流程审批通过日期成功")
					.append("</msg>").append("</result>");

		} catch (Exception e) {

			xmlstr.append("<result>").append("<flag>").append("1")
					.append("</flag>").append("<msg>更新流程审批通过日期失败：")
					.append(e.getMessage()).append("</msg>")
					.append("</result>");
		}

		return xmlstr.toString();
	}

}
