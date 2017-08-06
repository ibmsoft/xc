package com.xzsoft.xc.bg.action;

import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xzsoft.xc.bg.service.BgValidationService;
import com.xzsoft.xc.bg.util.XCBGConstants;
import com.xzsoft.xip.platform.util.PlatformUtil;

/**
 * @ClassName: BgValidationAction 
 * @Description: 预算校验处理
 * @author tangxl
 * @date 2016年6月21日 上午10:45:15 
 *
 */
@Controller
@RequestMapping("/bgValidationAction.do")
public class BgValidationAction {
	private static Logger log = Logger.getLogger(BgValidationAction.class.getName()) ;
	@Resource
	private BgValidationService bgValidationService;
	/*
	 * 月份处理
	 * 
	 */
	@SuppressWarnings("serial")
	public static final HashMap<String, Integer> MONTH_MAP = new HashMap<String, Integer>(){
		{
			put("01", 1);
			put("03", 1);
			put("05", 1);
			put("07", 1);
			put("08", 1);
			put("10", 1);
			put("12", 1);
		}
	};
	/**
	 * 
	 * @methodName  checkBudgetIsValid
	 * @author      tangxl
	 * @date        2016年6月21日
	 * @describe    预算占用使用前校验
	 * <document>
	 *  <title>预算校验说明</title>
	 *  <p>费用预算校验</p>
	 *  1、只有账簿层的【预算检查】为【是】和预算项目层的【控制方式】不为【不控制】时，才进行预算校验。
	 *  2、账簿和预算项目都是【预算控制】时，根据账簿的【预算控制周期】和预算项目的【控制维度】来获取【预算余额】
	 *  3、取得【预算余额】后，若申请额度超过预算余额，并且预算项目的控制方式为【绝对控制】预算才不通过，否则仅给出余额溢出警告
	 *  <p>项目预算校验</p>
	 *  1、只有账簿层的【预算检查】为【是】以及项目层的【控制方式】不为【不控制】时，才进行预算校验
	 *  2、根据账簿层的【预算控制周期】和项目层的【控制维度】来获取【预算余额】
	 *  3、当申请额度超过超过预算余额时，项目层的控制方式为【绝对控制】时不通过校验，否则只给出余额溢出警告
	 *  <p>参数说明</p>
	 *  账簿id --   ledgerId
	 *  项目id --   projectId <projectId不为空时为项目预算校验  否则为费用预算校验>
	 *  预算项目id --itemId
	 *  成本中心      -- deptId
	 *  申请额度      -- amount
	 *  业务日期     -- businessDate
	 * </document>
	 * @param request
	 * @param response
	 * @变动说明   
	 * @return   JSONObject -->flag  -1 校验不通过或者校验异常；0 校验通过；1校验不通过，但预算不是绝对控制，给出警告提示        
	 *                      -->msg   对应的异常信息     
	 * @version     1.0
	 */
	@RequestMapping(params ="method=checkBudgetIsValid")
	public synchronized  void checkBudgetIsValid(HttpServletRequest request, HttpServletResponse response){
		String ledgerId = request.getParameter("ledgerId");
		String projectId = request.getParameter("projectId");
		String itemId = request.getParameter("itemId");
		String businessDate = request.getParameter("businessDate");
		String periodCode = "";
		String bgYear = "";
		if(projectId == null || "".equals(projectId)){
			periodCode = businessDate.substring(0, 7);
			bgYear = periodCode.substring(0, 4);
		}
		String deptId = request.getParameter("deptId");
		String amount = request.getParameter("amount");
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("ledgerId", ledgerId);
		map.put("itemId", itemId);
		map.put("deptId", deptId);
		map.put("amount", amount);
		map.put("businessDate", businessDate);
		if(deptId == null || "".equals(deptId))
			deptId = "A";
		if(itemId == null || "".equals(itemId))
			itemId = "A";
		JSONObject jsonObject = null;
		  try {
			if("".equals(bgYear)){
				//项目预算校验
				map.put("projectId", projectId);
				jsonObject = bgValidationService.checkProjectBgIsValid(map);
			}else{
				//费用预算校验
				map.put("periodCode", periodCode);
				map.put("bgYear", bgYear);
				if(MONTH_MAP.containsKey(periodCode.substring(5, 7))){
					map.put("bgDate", periodCode.concat("-31"));
				}else{
					map.put("bgDate", periodCode.concat("-30"));
				}
				jsonObject = bgValidationService.checkFeeBgIsValid(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			jsonObject.put("flag", "-1");
			jsonObject.put("msg", "校验异常，原因："+e.getMessage());
		}finally{
			PlatformUtil.outPrint(response, jsonObject.toString());
		}
	}
	/**
	 * 
	 * @methodName  changeFeeBudgetVersion
	 * @author      tangxl
	 * @date        2016年7月1日
	 * @describe    修改费用预算版本
	 * <document>
	 *   <title>切换费用预算版本时的注意点</title>
	 *   <p>
	 *    1、  切换费用预算版本时，需清空原始生效费用预算版本的余额   表:xc_bg_cost_bal
	 *    2、  从业务单据及业务单据明细表中将隶属于【切换后的预算版本】下的审批通过费用预算申请单的余额汇总到  xc_bg_cost_bal表中
	 *    3、 预算版本切换只针对费用预算有效
	 *    4、 汇总预算余额时，按照【账簿】、【会计期】、【会计年度】、【预算项目】、【成本中心】来分组
 	 *   </p>
 	 *   <p>
 	 *    参数说明：
 	 *    bgVersionId  被生效的预算版本号
 	 *    ledgerId     对应账簿id
 	 *   </p>
	 * </document>
	 * @param request
	 * @param response
	 * @变动说明       
	 * @version     1.0
	 */
	@RequestMapping(params ="method=changeFeeBudgetVersion")
	public void changeFeeBudgetVersion(HttpServletRequest request, HttpServletResponse response){
		String bgVersionId = request.getParameter("bgVersionId");
		String ledgerId = request.getParameter("ledgerId");
		JSONObject jsonObject = null;
		  try {
			  jsonObject = bgValidationService.changeFeeBudgetVersion(bgVersionId,ledgerId);
		  } catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			jsonObject.put("flag", "1");
			jsonObject.put("msg", "切换预算版本失败，原因："+e.getMessage());
		}finally{
			PlatformUtil.outPrint(response, jsonObject.toString());
		}
	}
	/**
	 * 
	 * @methodName  queryBudgetDetailBills
	 * @author      tangxl
	 * @date        2016年7月6日
	 * @describe    查看预算执行下的单据明细信息
	 * <document>
	 *   <title>查看预算关联单据明细注意点</title>
	 *   <p>
	 *    1、  预算执行分 【费用预算执行】和【项目预算执行】
	 *    2、 【费用预算执行】按 账簿、会计年份、预算项目和成本中心分类，【项目预算执行】只按账簿、项目和成本中心分类
	 *    3、 一类预算执行下关联了多个业务单据，其中业务单据包括 【报销类单据】、【应付类单据】、【应收类单据】等等
	 *    4、 不同类型的业务单对应的表和字段名都不相同，只展示几个公用的字段：【单据编码】、【单据名称】、【单据类别】、【单据金额】、【业务日期】、【经办人】、【审批状态】
 	 *   </p>
 	 *   <p>
 	 *    参数说明：
 	 *    budgetType   预算执行类型   BG01-费用   BG02-项目  @see XCBGConstants.BG_COST_TYPE
 	 *    ledgerId     对应账簿id
 	 *    projectId    项目id
 	 *    deptId       成本中心
 	 *    factType     预算发生类型 1-占用 2-发生  @see XCBGConstants.BG_OCCUR_TYPE
 	 *    
 	 *    bgItemId     预算项目id
 	 *    bgYear       预算年份
 	 *    
 	 *   </p>
	 * </document>
	 * @param request
	 * @param response
	 * @变动说明       
	 * @version     1.0
	 */
	@RequestMapping(params ="method=queryBudgetDetailBills")
	public void queryBudgetDetailBills(HttpServletRequest request, HttpServletResponse response){
		String budgetType = request.getParameter("budgetType");
		String ledgerId = request.getParameter("ledgerId");
		String bgItemId = "";
		String projectId = "";
		String bgYear = "";
		if(XCBGConstants.BG_COST_TYPE.equalsIgnoreCase(budgetType)){
			bgItemId = request.getParameter("bgItemId");
			bgYear = request.getParameter("bgYear");
		}else{
			projectId = request.getParameter("projectId");
		}
		HashMap<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("budgetType", budgetType);
		paramsMap.put("ledgerId", ledgerId);
		paramsMap.put("bgItemId", bgItemId);
		paramsMap.put("projectId", projectId);
		paramsMap.put("bgYear", bgYear);
		paramsMap.put("factType", request.getParameter("factType"));
		JSONObject jsonObject = new JSONObject();
		  try {
			  String querySql = bgValidationService.queryBudgetDetailBills(paramsMap);
			  if(querySql !=null && !"".equals(querySql)){
				  jsonObject.put("flag", "0");
				  jsonObject.put("msg", querySql);
			  }else{
				  jsonObject.put("flag", "1");
				  jsonObject.put("msg", "获取预算单据信息失败！");
			  }
		  } catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			jsonObject.put("flag", "1");
			jsonObject.put("msg", "查询预算执行单据信息失败，原因："+e.getMessage());
		}finally{
			PlatformUtil.outPrint(response, jsonObject.toString());
		}
	}
	/**
	 * 
	 * @methodName  checkBgItemBeforeDelete
	 * @author      tangxl
	 * @date        2016年7月12日
	 * @describe    账簿预算项目设置删除前校验
	 * <document>
	 *   <title>账簿预算项目设置删除前校验</title>
	 *   <p>
	 *    校验将要删除的预算项目是否已经产生预算实际发生数
 	 *   </p>
 	 *   <p>
 	 *    参数说明：
 	 *    bgItemIds    要删除的预算项目id
 	 *    ledgerId     对应账簿id
 	 *   </p>
	 * </document>
	 * @param request
	 * @param response
	 * @变动说明       
	 * @version     1.0
	 */
	@RequestMapping(params ="method=checkBgItemBeforeDelete")
	public void checkBgItemBeforeDelete(HttpServletRequest request, HttpServletResponse response){
		String bgItemIds = request.getParameter("bgItemIds");
		String ledgerId = request.getParameter("ledgerId");
		JSONObject object = new JSONObject();
		try {
			object = bgValidationService.checkBgItemBeforeDelete(bgItemIds,ledgerId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			object.put("flag", "1");
			object.put("msg", e.getMessage());
		}finally{
			PlatformUtil.outPrint(response, object.toString());
		}
		
	}
	/**
	 * 
	 * @methodName  方法名
	 * @author      tangxl
	 * @date        2016年9月18日
	 * @describe    
	 * 	 * <document>
	 *  <title>退回预算申请单</title>
	 *  <p>操作</p>
	 *  1、退回预算申请单时，必须对预算申请单的所有明细的申请金额与对应预算余额的值进行对比，只有所有明细在不超过总余额的前提下才能进行退回操作；
	 *  2、预算单退回后，置成起草状态；
	 * </document>
	 * @param request
	 * @param response
	 * @变动说明       
	 * @version     1.0
	 */
	@RequestMapping(params ="method=cancelBudgetBal")
	public synchronized  void cancelBudgetBal(HttpServletRequest request, HttpServletResponse response){
		String bgDocId = request.getParameter("bgDocId");
		String cancelReason = request.getParameter("cancelReason");
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("bgDocId", bgDocId);
		map.put("cancelReason", cancelReason);
		JSONObject jsonObject = null;
		  try {
				jsonObject = bgValidationService.cancelBudgetBal(map);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			jsonObject.put("flag", "-1");
			jsonObject.put("msg", "回退预算单失败，原因："+e.getMessage());
		}finally{
			PlatformUtil.outPrint(response, jsonObject.toString());
		}
	}
	
}
