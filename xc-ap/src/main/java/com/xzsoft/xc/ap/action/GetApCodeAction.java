package com.xzsoft.xc.ap.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.xzsoft.xc.ap.dao.XCAPCommonDao;
import com.xzsoft.xc.util.service.RuleService;
import com.xzsoft.xip.wf.common.util.WFUtil;
/**
 * 
  * @ClassName: GetApCodeAction
  * @Description: 得到应付的单据编码
  * @author 任建建
  * @date 2016年4月5日 上午8:55:43
 */
@Controller
@RequestMapping("/getApCodeAction.do")
public class GetApCodeAction {
	private static final Logger log = Logger.getLogger(GetApCodeAction.class);
	@Resource
	private RuleService ruleService;
	@Resource
	private XCAPCommonDao xcapCommonDao;
	/**
	 * 
	  * @Title: getApCode
	  * @Description: 得到应付的单据编码
	  * @param @param request
	  * @param @param response	设定文件
	  * @return void	返回类型
	 */
	@RequestMapping(params ="method=getApCode",method=RequestMethod.POST)
	public void getApCode(HttpServletRequest request,HttpServletResponse response){
		JSONObject jo = new JSONObject();
		try {
			//获取前台传过来的参数信息
			String ledgerId = request.getParameter("ledgerId");				//账簿ID
			String apDocCatCode = request.getParameter("apDocCatCode");		//单据类型编码
			String date = request.getParameter("date");						//业务日期
			String userId = request.getParameter("userId");					//当前登录人
			
			String year = date.substring(0,4);								//年
			String month = date.substring(5,7);								//月
			
			//得到编码规则
			String ruleCode = null;
			if("PTFP".equals(apDocCatCode) || "ZZSFP".equals(apDocCatCode) || "PTFP-H".equals(apDocCatCode) || "ZZSFP-H".equals(apDocCatCode))
				ruleCode = "AP_INV";
			if("CGJS".equals(apDocCatCode) || "QTFK".equals(apDocCatCode) || "YUFK".equals(apDocCatCode))
				ruleCode = "AP_PAY_REQ";
			if("YFHYS".equals(apDocCatCode) || "YFHLDC".equals(apDocCatCode) || "YFHYUF".equals(apDocCatCode) || "YUFHLDC".equals(apDocCatCode))
				ruleCode = "AP_CANCEL";
			if("YFD".equals(apDocCatCode) || "YFD-H".equals(apDocCatCode))
				ruleCode = "AP_INV_GL";
			if("FKD_CGJS".equals(apDocCatCode) || "FKD_QTFK".equals(apDocCatCode) || "FKD_YUFK".equals(apDocCatCode) || "FKD_CGJS-H".equals(apDocCatCode) || "FKD_QTFK-H".equals(apDocCatCode) || "FKD_YUFK-H".equals(apDocCatCode))
				ruleCode = "AP_PAY";
			//得到单据编码
			String apDocCode = ruleService.getNextSerialNum(ruleCode, ledgerId, "", year, month, userId);
			//提示信息
			jo.put("flag","0");
			jo.put("apDocCode",apDocCode);
			jo.put("msg","");
			
		} catch (Exception e) {
			try {
				jo.put("flag","1");
				jo.put("msg",e.getMessage());
				jo.put("apDocCode","");
			} catch (JSONException e1) {
				log.error(e.getMessage());
			}
		} 
		// 提示信息
		WFUtil.outPrint(response,jo.toString());
	}
}
