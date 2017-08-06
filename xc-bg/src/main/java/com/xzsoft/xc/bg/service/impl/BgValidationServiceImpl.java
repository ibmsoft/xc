package com.xzsoft.xc.bg.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xzsoft.xc.bg.dao.BgValidationDAO;
import com.xzsoft.xc.bg.dao.XCBGCommonDao;
import com.xzsoft.xc.bg.modal.BgBillBalance;
import com.xzsoft.xc.bg.modal.BgDocDtl;
import com.xzsoft.xc.bg.modal.BgItemBean;
import com.xzsoft.xc.bg.service.BgValidationService;
import com.xzsoft.xc.bg.util.XCBGCalculateBalUtil;
import com.xzsoft.xc.bg.util.XCBGConstants;
import com.xzsoft.xc.util.service.XCWFService;
import com.xzsoft.xip.wf.dao.engine.ProcessInstanceDAO;

/**
 * @author tangxl
 *
 */
@Service("bgValidationService")
public class BgValidationServiceImpl implements BgValidationService {
	@Resource
	private BgValidationDAO bgValidationDAO;
	@Resource
	private XCBGCommonDao xcbgCommonDao;
	@Resource
	private ProcessInstanceDAO processInstanceDAO ;
	@Resource
	private XCWFService xcwfService ;
	
	private static BigDecimal static_val = new BigDecimal("0");
	/* (non-Javadoc)
	 * @name     checkFeeBgIsValid
	 * @author   tangxl
	 * @date     2016年6月21日
	 * @注释                   预算项目校验
	 * @version  1.0
	 * @see com.xzsoft.xc.bg.service.BgValidationService#checkBudgetIsValid(java.util.HashMap)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public JSONObject checkFeeBgIsValid(HashMap<String, String> map)
			throws Exception {
		JSONObject jsonObject = new JSONObject();
		HashMap<String, Object> returnMap = bgValidationDAO.getTotalBudgetVal(map);
		BigDecimal budgetVal = (BigDecimal) returnMap.get("returnAmount");
		String isPass = "";
		if(returnMap.containsKey("isPass"))
			isPass = returnMap.get("isPass").toString();
		//isPass 标识解释：1- 无需费用校验时，budgetVal：-1 isPass：Y
		//              2- 预算项目被停用，budgetVal:-1 isPass:D
	    //              3- 预算总额为负数时，budgetVal:<0 isPass:N  
		if(budgetVal.compareTo(static_val) == -1 && !"N".equals(isPass)){
			if("Y".equals(isPass)){
				jsonObject.put("flag", "0");
				jsonObject.put("msg", "校验通过！");
			}else if("D".equals(isPass)){
				jsonObject.put("flag", "-1");
				jsonObject.put("msg", returnMap.get("msgTip"));
			}
		}else{
			//根据条件获取对应的【预算发生数】和【预算占用数】
			map.put("ledgerControl", returnMap.get("ledgerControl").toString());
			map.put("itemDimension", returnMap.get("itemDimension").toString());
			BigDecimal factDecimal = bgValidationDAO.getFeeFactAmount(map);
			if(factDecimal == null)
				factDecimal = static_val;
			BigDecimal leaveDecimal = budgetVal.subtract(factDecimal);
			factDecimal = factDecimal.add(new BigDecimal(map.get("amount")));
			//实际占用数和发生数大于总数
			if(factDecimal.compareTo(budgetVal) != -1){
				//itemControl 2-预警控制，3-绝对控制
				String itemControl = returnMap.get("itemControl").toString();
				if(XCBGConstants.BG_CTRL_MODE02.equals(itemControl)){
					jsonObject.put("flag", "1");
					jsonObject.put("msg", returnMap.get("msgTip")+" 本次申请金额："+map.get("amount")+",剩余预算金额："+leaveDecimal+",本次申请已超预算！");
				}else{
					jsonObject.put("flag", "-1");
					jsonObject.put("msg", returnMap.get("msgTip")+" 本次申请金额："+map.get("amount")+",剩余预算金额："+leaveDecimal+",本次申请已超预算！");
				}
			}else{
				jsonObject.put("flag", "0");
				jsonObject.put("msg", "预算校验通过！");
			}
		}
		return jsonObject;
	}

	/* (non-Javadoc)
	 * @name     checkProjectBgIsValid
	 * @author   tangxl
	 * @date     2016年6月21日
	 * @注释                   TODO
	 * @version  1.0
	 * @see com.xzsoft.xc.bg.service.BgValidationService#checkProjectBgIsValid(java.util.HashMap)
	 */
	@Override
	public JSONObject checkProjectBgIsValid(HashMap<String, String> map)
			throws Exception {
		JSONObject jsonObject = new JSONObject();
		HashMap<String, Object> returnMap = bgValidationDAO.getTotalBudgetVal(map);
		BigDecimal budgetVal = (BigDecimal) returnMap.get("returnAmount");
		String isPass = "";
		if(returnMap.containsKey("isPass"))
			isPass = returnMap.get("isPass").toString();
		//isPass 标识解释：1- 无需费用校验时，budgetVal：-1 isPass：Y
		//              2- 预算项目被停用，budgetVal:-1 isPass:D
	    //              3- 预算总额为负数时，budgetVal:<0 isPass:N  
		if(budgetVal.compareTo(static_val) == -1 && !"N".equals(isPass)){
			if("Y".equals(isPass)){
				jsonObject.put("flag", "0");
				jsonObject.put("msg", "校验通过！");
			}else if("D".equals(isPass)){
				jsonObject.put("flag", "-1");
				jsonObject.put("msg", returnMap.get("msgTip"));
			}
		}else{
			//根据条件获取对应的【预算发生数】和【预算占用数】
			map.put("projectDimension", returnMap.get("projectDimension").toString());
			BigDecimal factDecimal = bgValidationDAO.getProjectFactAmount(map);
			if(factDecimal == null)
				factDecimal = static_val;
			BigDecimal leaveDecimal = budgetVal.subtract(factDecimal);
			//判断预算实际发生数和本次申请数是否超过预算总数
			factDecimal = factDecimal.add(new BigDecimal(map.get("amount")));
			if(factDecimal.compareTo(budgetVal) == 1){
				//projectControl 2-预警控制，3-绝对控制
				String projectControl = returnMap.get("projectControl").toString();
				if(XCBGConstants.BG_CTRL_MODE02.equals(projectControl)){
					jsonObject.put("flag", "1");
					jsonObject.put("msg", returnMap.get("msgTip")+" 本次申请金额："+map.get("amount")+",剩余预算金额："+leaveDecimal+",本次申请已超预算！");
				}else{
					jsonObject.put("flag", "-1");
					jsonObject.put("msg", returnMap.get("msgTip")+" 本次申请金额："+map.get("amount")+",剩余预算金额："+leaveDecimal+",本次申请已超预算！");
				}
			}else{
				jsonObject.put("flag", "0");
				jsonObject.put("msg", "预算校验通过！");
			}
		}
		return jsonObject;
	}

	/* (non-Javadoc)
	 * @name     changeFeeBudgetVersion
	 * @author   tangxl
	 * @date     2016年7月1日
	 * @注释                   TODO
	 * @version  1.0
	 * @see com.xzsoft.xc.bg.service.BgValidationService#changeFeeBudgetVersion(java.lang.String)
	 */
	@Override
	public JSONObject changeFeeBudgetVersion(String bgVersionId,String ledgerId)
			throws Exception {
		JSONObject jsonObject = new JSONObject();
		//1、获取要汇总的费用预算申请数
		List<BgBillBalance> summaryList = bgValidationDAO.getSummayFeeBal(bgVersionId);
		if(summaryList !=null && summaryList.size()>0){
			XCBGCalculateBalUtil.summaryBgBalance(summaryList);
		}
		//2、更新新的生效预算版本 并删除原始预算版本的余额数
		bgValidationDAO.updateFeeBgVersion(bgVersionId,ledgerId);
		//3、插入新的预算版本的余额数
		if(summaryList!=null && summaryList.size()>0){
			bgValidationDAO.inserNewVersionFee(summaryList);
		}
		jsonObject.put("flag", "0");
		jsonObject.put("msg", "预算版本更新成功!");
		return jsonObject;
	}

	/* (non-Javadoc)
	 * @name     queryBudgetDetailBills
	 * @author   tangxl
	 * @date     2016年7月6日
	 * @注释                   TODO
	 * <p>
 	 *    参数说明：
 	 *    budgetType   预算执行类型   BG01-费用   BG02-项目  @see XCBGConstants.BG_COST_TYPE
 	 *    ledgerId     对应账簿id
 	 *    projectId    项目id
 	 *    deptId       成本中心
 	 *    factType     预算发生类型 1-占用 2-发生  @see XCBGConstants.BG_OCCUR_TYPE   
 	 *    bgItemId     预算项目id
 	 *    bgYear       预算年份
 	 *   </p>
	 * @version  1.0
	 * @see com.xzsoft.xc.bg.service.BgValidationService#queryBudgetDetailBills(java.util.HashMap)
	 */
	@Override
	public String queryBudgetDetailBills(HashMap<String, String> paramsMap)
			throws Exception {
		String billSql = "";
		List<String> billTypeList = bgValidationDAO.getBgBillTypeList(paramsMap);
		if(billTypeList !=null && billTypeList.size()>0){
			billSql = XCBGCalculateBalUtil.getBudgetBillSql(paramsMap,billTypeList);
		}else{
			throw new Exception("业务单据信息不存在！");
		}
		return billSql;
	}

	/* (non-Javadoc)
	 * @name     方法名
	 * @author   tangxl
	 * @date     2016年7月12日
	 * @注释                   TODO
	 * @version  1.0
	 * @see com.xzsoft.xc.bg.service.BgValidationService#checkBgItemBeforeDelete(java.lang.String, java.lang.String)
	 */
	@Override
	public JSONObject checkBgItemBeforeDelete(String bgItemsId, String ledgerId)
			throws Exception {
		JSONObject object = new JSONObject();
		List<BgItemBean> validateList = bgValidationDAO.getValidateBgItemList(bgItemsId,ledgerId);
		if(validateList != null && validateList.size()>0){
			String msg = "";
			for(BgItemBean t:validateList){
				msg = msg.concat("【").concat(t.getBgItemName()).concat("】").concat("已发生实际业务！<br>");
			}
			object.put("flag", "1");
			object.put("msg", msg);
		}else{
			object.put("flag", "0");
			object.put("msg", "预算项目删除成功！");
		}
		return object;
	}

	/* (non-Javadoc)
	 * @name     cancelBudgetBal
	 * @author   tangxl
	 * @date     2016年9月18日
	 * @注释                   TODO
	 * @version  1.0
	 * @see com.xzsoft.xc.bg.service.BgValidationService#cancelBudgetBal(java.util.HashMap)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public JSONObject cancelBudgetBal(HashMap<String, String> map)
			throws Exception {
		JSONObject jo = new JSONObject(); 
		List<BgDocDtl> bgDocDtlList = xcbgCommonDao.getDocDtl(map.get("bgDocId"));
		 if(bgDocDtlList != null && bgDocDtlList.size()>0){
			 //计算每个明细行的发生数和剩余数
			 String insCode = bgDocDtlList.get(0).getInsCode();
			 jo = bgValidationDAO.cancelBudgetBal(bgDocDtlList);
			 if(insCode != null && !"".equals(insCode)){
				String insId = processInstanceDAO.getArchInsIdByCode(insCode) ;
				if(insId != null && !"".equals(insId)){
					xcwfService.copyArchInstance(insId);
					xcwfService.convertCompletedInstance2Reject(insId,map.get("cancelReason"));
					xcwfService.delArchInstance(insId);
				}else{
					throw new Exception("流程实例不存在, 实例编码为【"+insCode+"】") ;
				}
			}
		 }
		return jo;
	}
}
