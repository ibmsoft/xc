/**
 * 
 */
package com.xzsoft.xc.bg.api.impl;

import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.xzsoft.xc.bg.action.BgValidationAction;
import com.xzsoft.xc.bg.modal.BgFactBean;
import com.xzsoft.xc.bg.service.BgValidationService;
import com.xzsoft.xc.bg.service.BudgetDataHandlerService;
import com.xzsoft.xc.bg.util.XCBGConstants;
import com.xzsoft.xip.framework.util.AppContext;
/**
 * @className:BudegetDataManageApi
 * @describe :预算占用与发生数统一处理接口<校验、新增、修改、删除入口>
 * @author tangxl
 *
 */
public class BudegetDataManageApi {
	private static Logger log = Logger.getLogger(BgValidationAction.class.getName()) ;
	private static BudgetDataHandlerService budgetDataHandlerService;
	private static BgValidationService bgValidationService;
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
	static{
		budgetDataHandlerService = (BudgetDataHandlerService) AppContext.getApplicationContext().getBean("budgetDataHandlerService");
		bgValidationService = (BgValidationService) AppContext.getApplicationContext().getBean("bgValidationService");
	}
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
	 * @变动说明   
	 * @return   JSONObject -->flag  -1 校验不通过或者校验异常；0 校验通过；1校验不通过，但预算不是绝对控制，给出警告提示        
	 *                      -->msg   对应的异常信息     
	 * @version     1.0
	 */
	public synchronized static JSONObject checkBudgetIsValid(String ledgerId, String projectId, String itemId,
			String businessDate, String periodCode, String deptId, String amount) {
		String bgYear = "";
		if (projectId == null || "".equals(projectId) || "A".equalsIgnoreCase(projectId)) {
			periodCode = businessDate.substring(0, 7);
			bgYear = periodCode.substring(0, 4);
		}
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("ledgerId", ledgerId);
		map.put("itemId", itemId);
		map.put("deptId", deptId);
		map.put("amount", amount);
		map.put("businessDate", businessDate);
		if (deptId == null || "".equals(deptId))
			deptId = "A";
		if (itemId == null || "".equals(itemId))
			itemId = "A";
		JSONObject jsonObject = null;
		try {
			if ("".equals(bgYear)) {
				// 项目预算校验
				map.put("projectId", projectId);
				jsonObject = bgValidationService.checkProjectBgIsValid(map);
			} else {
				// 费用预算校验
				map.put("periodCode", periodCode);
				map.put("bgYear", bgYear);
				if (MONTH_MAP.containsKey(periodCode.substring(5, 7))) {
					map.put("bgDate", periodCode.concat("-31"));
				} else {
					map.put("bgDate", periodCode.concat("-30"));
				}
				jsonObject = bgValidationService.checkFeeBgIsValid(map);
			}
		} catch (Exception e) {
			log.error(e.getMessage());

			jsonObject = new JSONObject();
			jsonObject.put("flag", "-1");
			jsonObject.put("msg", "校验异常，原因：" + e.getMessage());
		}

		return jsonObject;
	}
	
	/**
	 * 
	 * @methodName  incrOccupancyBudget
	 * @author      tangxl
	 * @date        2016年6月22日
	 * @describe    新增预算占用数          要求设置的字段如下
	 *              factId       主键             手动生成
	 *              ledgerId     账簿id
	 *              projectId    项目id   <费用预算时请不要设置该字段的值>
	 *              bgItemId     预算项目id
	 *              deptId       部门id   <无部门时默认为A>
	 *              factDate     业务日期
	 *              amount       预算占用金额
	 *              factType     预算类型      1 -- 为预算占用数  2--为预算实际发生数  详见  --> XCBGConstants.BG_OCCUR_TYPE和XCBGConstants.BG_FACT_TYPE
	 *              srcId        来源业务单id
	 *              srcTab       来源表名
	 *              creationDate 创建日期
	 *              createdBy    创建人
	 *              lastUpdateDate 更新日期
	 *              lastUpdateBy 更新人
	 * @throws      Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public static void incrOccupancyBudget(List<BgFactBean> list) throws Exception {
		synchronized (BudegetDataManageApi.class) {
			budgetDataHandlerService.incrOccupancyBudget(list);
		}
	}
	/**
	 * 
	 * @methodName  updateFactBudget
	 * @author      tangxl
	 * @date        2016年6月22日
	 * @describe    更新预算占用<将预算占用状态更新为预算发生状态/将预算发生状态更改为占用状态>
	 * @param       srcId     账簿id
	 * @param       factType  要修改的预算状态
	 * @throws      Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public static void updateFactBudget(String srcId,String factType) throws Exception {
		synchronized (BudegetDataManageApi.class) {
			if(XCBGConstants.BG_OCCUR_TYPE.equals(factType)){
				budgetDataHandlerService.updateFactBudget(srcId, XCBGConstants.BG_FACT_TYPE, factType);
			}else{
				budgetDataHandlerService.updateFactBudget(srcId, XCBGConstants.BG_OCCUR_TYPE, factType);
			}
		}
	}
	/**
	 * 
	 * @methodName  updateFactBudget
	 * @author      tangxl
	 * @date        2016年6月22日
	 * @describe    删除预算占用数<factType 1--> 预算占用数，2-->实际发生数>
	 * @param       srcId     账簿id
     * @param       factType   要删除的预算数类型 
	 * @throws      Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public static void deleteFactBudget(String srcId,String factType) throws Exception {
		synchronized (BudegetDataManageApi.class) {
			budgetDataHandlerService.deleteFactBudget(srcId, factType);
		}
	}
}
