package com.xzsoft.xc.ex.action;

import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xzsoft.xc.ex.service.EXDocBaseService;
import com.xzsoft.xip.platform.util.PlatformUtil;
import com.xzsoft.xip.wf.common.util.WFUtil;
/**
 * @describe 获取账簿费用项目树
 * @author   tangxl
 *
 */
@Controller
@RequestMapping("/exItemTreeAction.do")
public class ExItemTreeAction {
	// 日志记录器
	private static Logger log = Logger.getLogger(ExItemTreeAction.class.getName());
	@Resource
	private EXDocBaseService eXDocBaseService;
	/**
	 * 
	 * @methodName  方法名
	 * @author      tangxl
	 * @date        2016年9月29日
	 * @describe    获取费用项目树
	 * <document>
	 *   <p>业务界面只能选着末级费用项目</p>
	 *   <p>对于所有的末级费用项目，必须反向查出该费用项目的完整树结构</P>
	 *   <p>提供树的模糊筛选功能(前端筛选or后端筛选)</p>
	 * </document>
	 * @param 		request
	 * @param 		response
	 * @throws 		Exception
	 * @变动说明       
	 * @version     1.0
	 */
	@RequestMapping(params = "method=getExItemTree")
	public void getExItemTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String treeJson = "";
		String ledgerId = request.getParameter("ledgerId");
		String exDocCat = request.getParameter("exDocCat");
		String exCodeName = request.getParameter("exCodeName");
		HashMap<String,String> map = new HashMap<String,String>();
    	String inconPath = request.getContextPath()+"/webbuilder/controls/ext/resources/themes/images/access/tree/leaf.gif";
    	map.put("ledgerId", ledgerId);
    	map.put("exDocCat", exDocCat);
    	map.put("exCodeName", exCodeName);
    	map.put("PATH", inconPath);
    	map.put("dbType", PlatformUtil.getDbType());
		try {
			treeJson = eXDocBaseService.getExItemTree(map);
		} catch (Exception e) {
			log.error("获取费用项目树失败，失败原因："+e.getMessage());
			treeJson = "[]";
		}
		WFUtil.outPrint(response,treeJson.toString());
	}
}
