package com.xzsoft.xc.st.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.xzsoft.xc.st.modal.BaseDto;
import com.xzsoft.xc.st.modal.Dto;
import com.xzsoft.xc.st.service.XcStCommonService;
import com.xzsoft.xip.wf.common.util.WFUtil;

/**
 * 
  * @ClassName XcStCommonAction
  * @Description 前台和后台交互的action方法
  * @author RenJianJian
  * @date 2016年12月20日 下午1:11:53
 */
@Controller
@RequestMapping("/xcStCommonAction.do")
public class XcStCommonAction {
	public static final Logger log = Logger.getLogger(XcStCommonAction.class);
	@Resource
	private XcStCommonService xcStCommonService;
	/**
	 * 
	  * @Title updateXcStDocStatus 方法名
	  * @Description 更新状态
	  * @param request
	  * @param response
	  * @throws Exception
	  * @return void 返回类型
	 */
	@RequestMapping(params="method=updateXcStDocStatus",method=RequestMethod.POST)
	public void updateXcStDocStatus(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			String stDocId = request.getParameter("stDocId");//账簿ID
			String docClassCode = request.getParameter("docClassCode");//库存编码
			jo = xcStCommonService.updateXcStDocStatus(stDocId,docClassCode);
			jo.put("flag","0");
		} catch (Exception e) {
			jo.put("flag","1");
			jo.put("msg",e.getMessage());
		}
		WFUtil.outPrint(response,jo.toString());
	}
	/**
	 * 
	  * @Title getXcStProcAndForms 方法名
	  * @Description 得到库存模块中的流程相关信息
	  * @param request
	  * @param response 设定文件
	  * @return void 返回类型
	 */
	@RequestMapping(params="method=getXcStProcAndForms",method=RequestMethod.POST)
	public void getXcStProcAndForms(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			String ledgerId = request.getParameter("LEDGER_ID");//账簿ID
			String stCatCode = request.getParameter("DOC_CAT_CODE");//库存编码
			
			jo = xcStCommonService.getXcStProcAndForms(stCatCode,ledgerId);
			jo.put("flag","0");
		} catch (Exception e) {
			jo.put("flag","1");
			jo.put("msg",e.getMessage());
		}
		WFUtil.outPrint(response,jo.toString());
	}
	/**
	 * 
	  * @Title getXcStAttCatCode 方法名
	  * @Description 得到库存模块的附件分类编码
	  * @param request
	  * @param response
	  * @throws Exception
	  * @return void 返回类型
	 */
	@RequestMapping(params="method=getXcStAttCatCode")
	public void getXcStAttCatCode(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			String ledgerId = request.getParameter("LEDGER_ID");//账簿ID
			String stCatCode = request.getParameter("DOC_CAT_CODE");//单据类型
			
			String attCatCode = xcStCommonService.getXcStAttCatCode(stCatCode,ledgerId);
			jo.put("flag","0");
			jo.put("attCatCode",attCatCode);
		} catch (Exception e) {
			log.error(e.getMessage());
			jo.put("flag","1");
			jo.put("msg",e.getMessage());
		}
		WFUtil.outPrint(response,jo.toString());
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected static Dto getPraramsAsDto(HttpServletRequest request) {
		Dto dto = new BaseDto();
		Map map = request.getParameterMap();
		Iterator keyIterator = map.keySet().iterator();
		while (keyIterator.hasNext()) {
			String key = (String) keyIterator.next();
			String value = ((String[]) map.get(key))[0];
			dto.put(key, value);
		}
		return dto;
	}
	@SuppressWarnings({ "rawtypes", "static-access", "deprecation" })
	public List getDtoList(String jsonStr){
		List list = new ArrayList();
		JSONArray jsonArray = JSONArray.fromObject(jsonStr);
		list = jsonArray.toList(jsonArray, BaseDto.class);
		return list;
	}
}