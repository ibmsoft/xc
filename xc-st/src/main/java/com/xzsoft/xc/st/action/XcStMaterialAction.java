package com.xzsoft.xc.st.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.xzsoft.xc.st.service.XcStMaterialService;
import com.xzsoft.xip.wf.common.util.WFUtil;

/**
 * 
  * @ClassName XcStMaterialAction
  * @Description 前台和后台交互的action方法
  * @author RenJianJian
  * @date 2016年11月30日 下午12:31:33
 */
@Controller
@RequestMapping("/xcStMaterialAction.do")
public class XcStMaterialAction {
	public static final Logger log = Logger.getLogger(XcStMaterialAction.class);
	@Resource
	private XcStMaterialService xcStMaterialService;
	/**
	 * 
	  * @Title saveOrUpdateStMaterial 方法名
	  * @Description 对物资进行增加或者修改操作
	  * @param request
	  * @param response
	  * @throws Exception
	  * @return void 返回类型
	 */
	@RequestMapping(params="method=saveOrUpdateStMaterial",method=RequestMethod.POST)
	public void saveOrUpdateStMaterial(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			String stMaterialInfo = request.getParameter("stMaterialInfo");
			String opType = request.getParameter("opType");
			jo = xcStMaterialService.saveOrUpdateStMaterial(stMaterialInfo, opType);
			jo.put("flag", "0");
		} catch (Exception e) {
			jo.put("msg",e.getMessage());
			jo.put("flag","1");
			log.error(e.getMessage());
		}
		WFUtil.outPrint(response, jo.toString());
	}
	/**
	 * 
	  * @Title deleteXcStMaterial 方法名
	  * @Description 删除操作
	  * @param request
	  * @param response
	  * @throws Exception
	  * @return void 返回类型
	 */
	@RequestMapping(params="method=deleteXcStMaterial")
	public void deleteXcStMaterial(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			String materialIds = request.getParameter("materialIds");
			jo = xcStMaterialService.deleteXcStMaterial(materialIds);
			jo.put("flag", "0");
		} catch (Exception e) {
			jo.put("msg",e.getMessage());
			jo.put("flag","1");
			log.error(e.getMessage());
		}
		WFUtil.outPrint(response, jo.toString());
	}
	/**
	 * 
	  * @Title deleteXcStMaterialParams 方法名
	  * @Description 对账簿级物料删除操作
	  * @param request
	  * @param response
	  * @throws Exception
	  * @return void 返回类型
	 */
	@RequestMapping(params="method=deleteXcStMaterialParams")
	public void deleteXcStMaterialParams(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			String materialParamsIds = request.getParameter("materialParamsIds");
			jo = xcStMaterialService.deleteXcStMaterialParams(materialParamsIds);
			jo.put("flag", "0");
		} catch (Exception e) {
			jo.put("msg",e.getMessage());
			jo.put("flag","1");
			log.error(e.getMessage());
		}
		WFUtil.outPrint(response, jo.toString());
	}
}