package com.xzsoft.xc.st.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.xzsoft.xc.st.modal.Dto;
import com.xzsoft.xc.st.service.XcStDocsService;
import com.xzsoft.xip.wf.common.util.WFUtil;

/**
 * 
  * @ClassName XcStDocsAction
  * @Description 库存模块:前台和后台交互的action方法
  * @author RenJianJian
  * @date 2016年12月2日 下午12:31:12
 */
@Controller
@RequestMapping("/xcStDocsAction.do")
public class XcStDocsAction extends XcStCommonAction{
	public static final Logger log = Logger.getLogger(XcStDocsAction.class);
	@Resource
	private XcStDocsService xcStDocsService;
	/**
	 * 
	  * @Title saveOrUpdateStWhEntry 方法名
	  * @Description 对入库及退货进行增加/修改操作
	  * @param request
	  * @param response
	  * @throws Exception
	  * @return void 返回类型
	 */
	@SuppressWarnings({ "static-access", "unchecked" })
	@RequestMapping(params="method=saveOrUpdateXcStDocs",method=RequestMethod.POST)
	public void saveOrUpdateXcStDocs(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			Dto pDto = this.getPraramsAsDto(request);
			pDto.put("insertDtlList", getDtoList(pDto.getAsString("create")));
			pDto.put("updateDtlList", getDtoList(pDto.getAsString("update")));
			pDto.put("deleteDtlList", getDtoList(pDto.getAsString("destroy")));
			jo = xcStDocsService.saveOrUpdateXcStDocs(pDto);
			jo.put("flag", "0");
		} catch (Exception e) {
			log.error(e.getMessage());
			jo.put("flag", "1");
			jo.put("msg", e.getMessage());
		}
		WFUtil.outPrint(response, jo.toString());
	}
	/**
	 * 
	  * @Title deleteXcStDocs 方法名
	  * @Description 删除操作
	  * @param request
	  * @param response
	  * @throws Exception
	  * @return void 返回类型
	 */
	@RequestMapping(params="method=deleteXcStDocs")
	public void deleteXcStDocs(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			String hIds = request.getParameter("hIds");
			String docClassCode = request.getParameter("docClassCode");
			jo = xcStDocsService.deleteXcStDocs(hIds,docClassCode);
			jo.put("flag","0");
		} catch (Exception e) {
			log.error(e.getMessage());
			jo.put("flag","1");
			jo.put("msg",e.getMessage());
		}
		WFUtil.outPrint(response, jo.toString());
	}
	/**
	 * 
	  * @Title accountOrCancelAccount 方法名
	  * @Description 记账取消记账操作
	  * @param request
	  * @param response
	  * @throws Exception
	  * @return void 返回类型
	 */
	@RequestMapping(params="method=accountOrCancelAccount")
	public void accountOrCancelAccount(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			String stDocId = request.getParameter("stDocId");
			String opType = request.getParameter("opType");
			jo = xcStDocsService.accountOrCancelAccount(stDocId,opType);
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
	  * @Title acceptDoc 方法名
	  * @Description 物质调拨单接受的方法
	  * @param request
	  * @param response
	  * @throws Exception
	  * @return void 返回类型
	 */
	@RequestMapping(params="method=acceptDoc")
	public void acceptDoc(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			String TR_H_ID = request.getParameter("TR_H_ID");
			jo = xcStDocsService.acceptDoc(TR_H_ID);
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
	  * @Title closeXcStUseApply 方法名
	  * @Description 关闭（打开）申请单
	  * @param request
	  * @param response
	  * @throws Exception
	  * @return void 返回类型
	 */
	@RequestMapping(params="method=closeXcStUseApply")
	public void closeXcStUseApply(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			String hIds = request.getParameter("hIds");
			jo = xcStDocsService.closeXcStUseApply(hIds);
			jo.put("flag","0");
		} catch (Exception e) {
			log.error(e.getMessage());
			jo.put("flag","1");
			jo.put("msg",e.getMessage());
		}
		WFUtil.outPrint(response, jo.toString());
	}
}