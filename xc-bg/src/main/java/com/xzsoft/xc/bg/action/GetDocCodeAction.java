package com.xzsoft.xc.bg.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.xzsoft.xc.util.service.RuleService;
import com.xzsoft.xip.wf.common.util.WFUtil;
/**
 * 
  * @ClassName: GetDocCodeAction
  * @Description: 预算单
  * @author 任建建
  * @date 2016年3月21日 上午9:04:57
 */
@Controller
@RequestMapping("/getDocCodeAction.do")
public class GetDocCodeAction {
	// 日志记录器
	private static Logger log = Logger.getLogger(GetDocCodeAction.class.getName());
	@Resource
	private RuleService ruleService ;
	/**
	 * 
	  * @Title: getDocCode
	  * @Description: 得到预算单编码
	  * @param @param request
	  * @param @param response	设定文件
	  * @return void	返回类型
	  * @throws
	 */
	@RequestMapping(params ="method=getDocCode",method=RequestMethod.POST)
	public void getDocCode(HttpServletRequest request,HttpServletResponse response){
		JSONObject jo = new JSONObject();
		try {
			// 获取参数
			String ruleCode =request.getParameter("ruleCode");//账簿ID
			String ledgerId = request.getParameter("ledgerId");//会计期
			String categoryId = request.getParameter("categoryId");//账簿ID
			String year = request.getParameter("year");//会计期
			String month = request.getParameter("month");//账簿ID
			String userId = request.getParameter("userId");//当前登录人的ID
			String bgDocCode = ruleService.getNextSerialNum(ruleCode, ledgerId, categoryId, year, month, userId);
			//提示信息
			jo.put("flag","0");
			jo.put("bgDocCode",bgDocCode);
			jo.put("msg","");
			
		} catch (Exception e) {
			log.error(e.getMessage());
			try {
				jo.put("flag","1");
				jo.put("msg",e.getMessage());
				jo.put("bgDocCode","");
			} catch (JSONException e1) {
				log.error(e.getMessage());
			}
		} 
		// 提示信息
		WFUtil.outPrint(response,jo.toString());
	}
}