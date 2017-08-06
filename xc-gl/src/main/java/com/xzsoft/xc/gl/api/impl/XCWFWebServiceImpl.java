package com.xzsoft.xc.gl.api.impl;

import java.util.HashMap;

import javax.jws.WebService;

import org.springframework.stereotype.Component;

import com.xzsoft.xc.bg.util.XCBGConstants;
import com.xzsoft.xc.gl.api.XCWFWebServiceWS;
import com.xzsoft.xc.gl.service.XCWFFetchDataService;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xc.util.common.XConstants;
import com.xzsoft.xip.framework.util.AppContext;

/**
 * @ClassName: XCWFWebServiceImpl 
 * @Description: 云ERP单据审批流程订制webservice服务实现类
 * @author tangxl
 * @date 2016年5月23日 上午10:30:13 
 */
@Component
@WebService(endpointInterface = "com.xzsoft.xc.gl.api.XCWFWebServiceWS",targetNamespace = "http://ws.xzsoft.com")
public class XCWFWebServiceImpl implements XCWFWebServiceWS {
	
	private XCWFFetchDataService getXCWFFetchDataService() {
		return (XCWFFetchDataService) AppContext.getApplicationContext().getBean("xCWFFetchDataService") ;
	}
	
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getBillQuota</p> 
	 * <p>Description: 获取云ERP内置流程审批节点的判断条件</p> 
	 * @param ledgerId
	 * @param billType
	 * @param actCode
	 * @param userName
	 * @param userPwd
	 * @return 
	 */
	@Override
	public String getBillQuota(String ledgerId, String billType,String actCode,String userName, String userPwd) {
		StringBuffer buffer = new StringBuffer();
		String lang = "";
		try {
			lang = XCUtil.getLang();//语言值
			HashMap<String,Object> map = this.getXCWFFetchDataService().getBillQuota(ledgerId, billType, actCode);
			String limitedVal = "0";
			if(map != null && map.get("LIMITED_VAL") != null)
				limitedVal =  map.get("LIMITED_VAL").toString();
			buffer.append("<result>");
			buffer.append("<flag>").append("0").append("</flag>");
			switch (lang) {
			case XConstants.ZH:
				buffer.append("<msg>").append("获取阈值成功！").append("</msg>");
				break;
			case XConstants.EN:
				buffer.append("<msg>").append("Gain threshold success").append("</msg>");
				break;
			default:
				break;
			}
			buffer.append("<data>").append(limitedVal).append("</data>");
			buffer.append("</result>");
		} catch (Exception e) {
			buffer.append("<result>");
			buffer.append("<flag>").append("1").append("</flag>");
			switch (lang) {
			case XConstants.ZH:
				buffer.append("<msg>").append("获取阈值失败！").append("</msg>");
				break;
			case XConstants.EN:
				buffer.append("<msg>").append("Gain threshold fail").append("</msg>");
				break;
			default:
				break;
			}
			buffer.append("<data>").append(e.getMessage()).append("</data>");
			buffer.append("</result>");
		}
		
		return buffer.toString();
	}
	
	/* (non-Javadoc)
	 * @see com.xzsoft.xc.gl.api.XCWFWebServiceWS#getPassFlag(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String getPassFlag(String ledgerId, String billType, String actCode,
			String userName, String userPwd) {
		StringBuffer buffer = new StringBuffer();
		String lang = "";
		try {
			lang = XCUtil.getLang();//语言值
			HashMap<String,Object> map = this.getXCWFFetchDataService().getBillQuota(ledgerId, billType, actCode);
			String isPass = "Y";
            if(map != null && map.get("IS_ENABLED") != null){
            	isPass =  map.get("IS_ENABLED").toString();
            	if("Y".equals(isPass)){
            		isPass = "N";
            	}else{
            		isPass = "Y";	
            	}
            }
			buffer.append("<result>");
			buffer.append("<flag>").append("0").append("</flag>");
			switch (lang) {
			case XConstants.ZH:
				buffer.append("<msg>").append("获取启用标志成功！").append("</msg>");
				break;
			case XConstants.EN:
				buffer.append("<msg>").append("Get enable flag success").append("</msg>");
				break;
			default:
				break;
			}
			buffer.append("<data>").append(isPass).append("</data>");
			buffer.append("</result>");

		} catch (Exception e) {
			buffer.append("<result>");
			buffer.append("<flag>").append("1").append("</flag>");
			switch (lang) {
			case XConstants.ZH:
				buffer.append("<msg>").append("获取启用标志失败！").append("</msg>");
				break;
			case XConstants.EN:
				buffer.append("<msg>").append("Get enable flag fail").append("</msg>");
				break;
			default:
				break;
			}
			buffer.append("<data>").append(e.getMessage()).append("</data>");
			buffer.append("</result>");
		}
		
		return buffer.toString();
	}
	/**
	 * 
	 * @methodName  生成预算单审批时的标题
	 * @author      tangxl
	 * @date        2016年7月25日
	 * @describe    TODO
	 * @param       opType
	 * @param 		projectName
	 * @param 		billAmount
	 * @param 		submitUserName
	 * @param 		loginName
	 * @param 		pwd
	 * @return
	 * @变动说明       
	 * @version     1.0
	 */
	public String createBgBillTitle(String opType,String projectName,String billAmount,String submitUserName,String loginName,String pwd) {
		StringBuffer buffer = new StringBuffer();
		String lang = "";
		try {
			lang = XCUtil.getLang();//语言值
		} catch (Exception e) {
		
		}
		buffer.append("<result>");
		buffer.append("<flag>").append("0").append("</flag>");
		switch (lang) {
		case XConstants.ZH:
			buffer.append("<msg>").append("获取标题信息成功！").append("</msg>");
			break;
		case XConstants.EN:
			buffer.append("<msg>").append("Get title information success").append("</msg>");
			break;
		default:
			break;
		}
		String title = "";
		//费用预算单
		if(XCBGConstants.BG_COST_TYPE.equalsIgnoreCase(opType)){
			switch (lang) {
			case XConstants.ZH:
				buffer.append("<msg>").append("获取标题信息成功！").append("</msg>");
				title =  "请审批".concat(submitUserName).concat("提交的费用预算单，金额").concat(billAmount).concat("元");
				break;
			case XConstants.EN:
				buffer.append("<msg>").append("Get title information success").append("</msg>");
				title =  "Please examine and approval".concat(submitUserName).concat("submitted the cost budget，amount is").concat(billAmount).concat("RMB");
				break;
			default:
				break;
			}
			
		}else{
		//项目预算单
			switch (lang) {
			case XConstants.ZH:
				title =  "请审批".concat(submitUserName).concat("提交的").concat(projectName).concat("项目预算单，金额").concat(billAmount).concat("元");
				break;
			case XConstants.EN:
				title =  "Please examine and approval".concat(submitUserName).concat("submit").concat(projectName).concat("Project budget，amount").concat(billAmount).concat("RMB");
				break;
			default:
				break;
			}
			
		}
		buffer.append("<data>").append(title).append("</data>");
		buffer.append("</result>");
		return buffer.toString();
	}
    /**
     * 
     * @methodName  方法名
     * @author      tangxl
     * @date        2016年7月26日
     * @describe    获取预算单审批表单
     * @param 		bgCatCode 预算单类型 BG01-费用预算；BG02-成本预算；BG03-专项预算；BG04-资金预算 @see XCBGConstants
     * @param 		formType 1-电脑端审批表单；2-电脑端填报表单；3-移动端审批表单；4-移动端填报表单
     * @return
     * @变动说明       
     * @version     1.0
     */
    public String getBgBillAuditPage(String bgCatCode,String formType){
    	StringBuffer buffer = new StringBuffer();
    	String lang = "";
		try {
			lang = XCUtil.getLang();//语言值
			buffer.append("<result>");
			buffer.append("<flag>").append("0").append("</flag>");
			switch (lang) {
			case XConstants.ZH:
				buffer.append("<msg>").append("获取表单信息成功！").append("</msg>");
				break;
			case XConstants.EN:
				buffer.append("<msg>").append("Get the success of the form information").append("</msg>");
				break;
			default:
				break;
			}
			String formUrl = this.getXCWFFetchDataService().getBgBillAuditPage(bgCatCode, formType);
			buffer.append("<data>").append(formUrl).append("</data>");
			buffer.append("</result>");
			
		} catch (Exception e) {
			buffer.append("<result>");
			buffer.append("<flag>").append("1").append("</flag>");
			switch (lang) {
			case XConstants.ZH:
				buffer.append("<msg>").append("获取表单信息失败！").append("</msg>");
				break;
			case XConstants.EN:
				buffer.append("<msg>").append("Get the fail of the form information").append("</msg>");
				break;
			default:
				break;
			}
			buffer.append("<data>").append(e.getMessage()).append("</data>");
			buffer.append("</result>");
		}
    	return buffer.toString();
    } 
}
