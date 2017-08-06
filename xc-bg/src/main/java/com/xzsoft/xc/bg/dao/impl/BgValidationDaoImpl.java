package com.xzsoft.xc.bg.dao.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.bg.dao.BgValidationDAO;
import com.xzsoft.xc.bg.mapper.BgValidationMapper;
import com.xzsoft.xc.bg.modal.BgBillBalance;
import com.xzsoft.xc.bg.modal.BgDocDtl;
import com.xzsoft.xc.bg.modal.BgItemBean;
import com.xzsoft.xc.bg.util.XCBGConstants;
import com.xzsoft.xc.util.modal.Project;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.platform.util.PlatformUtil;

/**
 * @className: BgValidationDaoImpl
 * @description: 预算校验取数接口实现类
 * @author tangxl
 * @date 2016年6月21日 上午14:36:36
 */
@Repository("bgValidationDAO")
public class BgValidationDaoImpl implements BgValidationDAO {
	@Resource
	BgValidationMapper bgValidationMapper;
	private static BigDecimal ZERO_DECIMAL = new BigDecimal(0);
	/* (non-Javadoc)
	 * @name     方法名
	 * @author   tangxl
	 * @date     2016年6月21日
	 * @注释                   TODO
	 * @version  1.0
	 * @see com.xzsoft.xc.bg.dao.BgValidationDAO#getTotalBudgetVal(java.util.HashMap)
	 */
	@Override
	public HashMap<String,Object> getTotalBudgetVal(HashMap<String, String> map)
			throws Exception {
		/*
		 *预算余额不可能为负数
		 *若账簿预算控制方式为不控制，直接返回。
		 *账簿控制周期 
		 */
		StringBuffer msgTip = new StringBuffer();
		HashMap<String,Object> returnMap = new HashMap<String, Object>();
		String periodCode = map.get("periodCode");
		String controlCycle = "";
		String itemDimension = "";
		String projectDimension = "";
		String whereSql = "";
		HashMap<String, Object> controlMap = new HashMap<String, Object>();
		controlCycle = bgValidationMapper.getLedgerContralCycle(map.get("ledgerId"));
		if(controlCycle == null || "".equals(controlCycle)){
			returnMap.put("returnAmount", new BigDecimal("-1"));
			returnMap.put("isPass", "Y");
			return returnMap;
		}
		if(periodCode != null && !"".equals(periodCode)){
			/*费用预算
			 *若预算项目控制方式为不控制，说明也不需要做预算控制，直接返回
			 * 
			 */
			controlMap = bgValidationMapper.getItemContralDimension(map.get("ledgerId"), map.get("itemId"));
			if(controlMap == null || controlMap.get("BG_CTRL_DIM") ==null || "".equals(controlMap.get("BG_CTRL_DIM"))){
				returnMap.put("returnAmount", new BigDecimal("-1"));
				returnMap.put("isPass", "Y");
				return returnMap;
			}
			//根据账簿和预算项目控制维度，返回符合条件的预算总额  
			//controlCycle-->1 按年控制  controlCycle-->2 按月累计控制
			//itemDimension->1 预算项目  itemDimension-->2预算项目+成本中心
			//返回账簿以及预算项目的控制方式
			//itemControl 2-预警控制，3-绝对控制
			itemDimension = controlMap.get("BG_CTRL_DIM").toString();
			returnMap.put("ledgerControl", controlCycle);
			returnMap.put("itemDimension", itemDimension);
			returnMap.put("itemControl", controlMap.get("BG_CTRL_MODE").toString());
			if(XCBGConstants.LEDGER_YEAR_CONTROL.equals(controlCycle)){
				if(XCBGConstants.BG_EX_CTRL_DIM01.equals(itemDimension)){
					whereSql = " t.BG_YEAR = '"+map.get("bgYear")+"' AND t.BG_ITEM_ID = '"+map.get("itemId")+"'";
					if(!"A".equalsIgnoreCase(map.get("itemId"))){
						String itemName = bgValidationMapper.getAllNameById("SELECT t.BG_ITEM_NAME FROM xc_bg_items t, xc_bg_ld_items l WHERE t.BG_ITEM_ID = l.BG_ITEM_ID AND l.IS_ENABLED = 'Y' AND l.LEDGER_ID = '"+map.get("ledgerId")+"' AND t.BG_ITEM_ID = '"+map.get("itemId")+"'");
						if(itemName == null || "".equals(itemName)){
							msgTip.append("/【"+itemName+"】已停用或不存在！");
							returnMap.put("returnAmount", new BigDecimal("-1"));
							returnMap.put("isPass", "D");
							return returnMap;
							
						}
						msgTip.append("/【"+itemName+"】");
					}
				}else if(XCBGConstants.BG_EX_CTRL_DIM02.equals(itemDimension)){
					whereSql = " t.BG_YEAR = '"+map.get("bgYear")+"' AND t.BG_ITEM_ID = '"+map.get("itemId")+"' AND t.DEPT_ID = '"+map.get("deptId")+"'";
					if(!"A".equalsIgnoreCase(map.get("itemId"))){
						String itemName = bgValidationMapper.getAllNameById("SELECT t.BG_ITEM_NAME FROM xc_bg_items t, xc_bg_ld_items l WHERE t.BG_ITEM_ID = l.BG_ITEM_ID AND l.IS_ENABLED = 'Y' AND l.LEDGER_ID = '"+map.get("ledgerId")+"' AND t.BG_ITEM_ID = '"+map.get("itemId")+"'");
						if(itemName == null || "".equals(itemName)){
							msgTip.append("/【"+itemName+"】已停用！");
							returnMap.put("returnAmount", new BigDecimal("-1"));
							returnMap.put("isPass", "D");
							return returnMap;
							
						}
						msgTip.append("/【"+itemName+"】");
					}
					if(!"A".equalsIgnoreCase(map.get("deptId"))){
						String deptName = bgValidationMapper.getAllNameById(" SELECT DEPT_NAME FROM xip_pub_depts  WHERE DEPT_ID = '"+map.get("deptId")+"'");
						msgTip.append("/【"+deptName+"】");
					}
				}
				msgTip.append("/【按年控制】");
			}else{
				//按月累计控制
				if(XCBGConstants.BG_EX_CTRL_DIM01.equals(itemDimension)){
					whereSql = " t.BG_YEAR = '"+map.get("bgYear")
							+"' AND t.BG_ITEM_ID = '"+map.get("itemId")
							+"' AND t.PERIOD_CODE >='"+map.get("bgYear")+"-01"
							+"' AND t.PERIOD_CODE <='"+periodCode+"'";
					if(!"A".equalsIgnoreCase(map.get("itemId"))){
						String itemName = bgValidationMapper.getAllNameById("SELECT t.BG_ITEM_NAME FROM xc_bg_items t, xc_bg_ld_items l WHERE t.BG_ITEM_ID = l.BG_ITEM_ID AND l.IS_ENABLED = 'Y' AND l.LEDGER_ID = '"+map.get("ledgerId")+"' AND t.BG_ITEM_ID = '"+map.get("itemId")+"'");
						if(itemName == null || "".equals(itemName)){
							msgTip.append("/【"+itemName+"】已停用或不存在！");
							returnMap.put("returnAmount", new BigDecimal("-1"));
							returnMap.put("isPass", "D");
							return returnMap;
							
						}
						msgTip.append("/【"+itemName+"】");
					}
				}else if(XCBGConstants.BG_EX_CTRL_DIM02.equals(itemDimension)){
					whereSql = " t.BG_YEAR = '"+map.get("bgYear")
							+"' AND t.BG_ITEM_ID = '"+map.get("itemId")
							+"' AND t.DEPT_ID = '"+map.get("deptId")
							+"' AND t.PERIOD_CODE >='"+map.get("bgYear")+"-01"
							+"' AND t.PERIOD_CODE <='"+periodCode+"'";
					if(!"A".equalsIgnoreCase(map.get("itemId"))){
						String itemName = bgValidationMapper.getAllNameById("SELECT t.BG_ITEM_NAME FROM xc_bg_items t, xc_bg_ld_items l WHERE t.BG_ITEM_ID = l.BG_ITEM_ID AND l.IS_ENABLED = 'Y' AND l.LEDGER_ID = '"+map.get("ledgerId")+"' AND t.BG_ITEM_ID = '"+map.get("itemId")+"'");
						if(itemName == null || "".equals(itemName)){
							msgTip.append("/【"+itemName+"】已停用或不存在！");
							returnMap.put("returnAmount", new BigDecimal("-1"));
							returnMap.put("isPass", "D");
							return returnMap;
							
						}
						msgTip.append("/【"+itemName+"】");
					}
					if(!"A".equalsIgnoreCase(map.get("deptId"))){
						String deptName = bgValidationMapper.getAllNameById(" SELECT DEPT_NAME FROM xip_pub_depts  WHERE DEPT_ID = '"+map.get("deptId")+"'");
						msgTip.append("/【"+deptName+"】");
					}
				}
				msgTip.append("/【按月累计控制】");
			}
			BigDecimal returnDecimal = bgValidationMapper.getCostBudgetVal(map.get("ledgerId"), whereSql);
			if(returnDecimal == null){
				returnDecimal = new BigDecimal(0);
			}else if(returnDecimal.compareTo(ZERO_DECIMAL) == -1){
				returnMap.put("isPass", "N");
			}
			returnMap.put("msgTip", msgTip.toString());
			returnMap.put("returnAmount",returnDecimal);
			return returnMap;
		}else{
			/*项目预算
			 **若项目控制方式为不控制，说明也不需要做预算控制，直接返回  
			 */
			Project project = bgValidationMapper.getProjectInfoById(map.get("projectId"));
			Date startDate = project.getStartDate();
			Date endDate = project.getEndDate();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String businessDate = map.get("businessDate").concat(" 00:00:00");
			Date validDate = format.parse(businessDate);
			//检查当前业务日期是否处在项目有效期内
			if(validDate.getTime()<startDate.getTime() || validDate.getTime()>endDate.getTime()){
				throw new Exception("单据业务日期【"+map.get("businessDate")+"】不在项目【"+project.getProjectName()+"】的有效日期范围内!");
			}
			controlMap = bgValidationMapper.getProjectDimension(map.get("projectId"));
			if(controlMap == null || controlMap.get("PRJ_CTRL_DIM") == null || "".equals(controlMap.get("PRJ_CTRL_DIM"))){
				returnMap.put("returnAmount", new BigDecimal("-1"));
				returnMap.put("isPass", "Y");
				return returnMap;
			}
			//项目控制维度，返回符合条件的预算总额
			//projectDimension->1  项目  projectDimension-->2项目+预算项目  projectDimension-->3-项目+预算项目+成本中心
			//projectControl  -> 预算控制模式   1-不控制    2-预警控制     3-绝对控制
			//校验维度包含预算项目时，需要校验预算项目是否被停用
			projectDimension = controlMap.get("PRJ_CTRL_DIM").toString();
			returnMap.put("projectDimension", projectDimension);
			msgTip.append("【"+controlMap.get("PROJECT_NAME").toString()+"】");
			if(controlMap.get("PRJ_CTRM_MODE") == null){
				returnMap.put("projectControl", "1");
			}else{
				returnMap.put("projectControl", controlMap.get("PRJ_CTRM_MODE"));
			}
			whereSql = " t.PROJECT_ID = '"+map.get("projectId")+"'";
			if(XCBGConstants.BG_PRJ_CTRL_DIM02.equals(projectDimension)){
				whereSql = " t.PROJECT_ID = '"+map.get("projectId")+"' AND t.BG_ITEM_ID = '"+map.get("itemId")+"'";
				if(!"A".equalsIgnoreCase(map.get("itemId"))){
					String itemName = bgValidationMapper.getAllNameById("SELECT t.BG_ITEM_NAME FROM xc_bg_items t, xc_bg_ld_items l WHERE t.BG_ITEM_ID = l.BG_ITEM_ID AND l.IS_ENABLED = 'Y' AND l.LEDGER_ID = '"+map.get("ledgerId")+"' AND t.BG_ITEM_ID = '"+map.get("itemId")+"'");
					if(itemName == null || "".equals(itemName)){
						msgTip.append("/【"+itemName+"】已停用或不存在！");
						returnMap.put("returnAmount", new BigDecimal("-1"));
						returnMap.put("isPass", "D");
						return returnMap;
						
					}
					msgTip.append("/【"+itemName+"】");
				}
			}else if(XCBGConstants.BG_PRJ_CTRL_DIM03.equals(projectDimension)){
				whereSql = " t.PROJECT_ID = '"+map.get("projectId")+"' AND t.BG_ITEM_ID = '"+map.get("itemId")+"' AND t.DEPT_ID = '"+map.get("deptId")+"'";
				if(!"A".equalsIgnoreCase(map.get("itemId"))){
					String itemName = bgValidationMapper.getAllNameById("SELECT t.BG_ITEM_NAME FROM xc_bg_items t, xc_bg_ld_items l WHERE t.BG_ITEM_ID = l.BG_ITEM_ID AND l.IS_ENABLED = 'Y' AND l.LEDGER_ID = '"+map.get("ledgerId")+"' AND t.BG_ITEM_ID = '"+map.get("itemId")+"'");
					if(itemName == null || "".equals(itemName)){
						msgTip.append("/【"+itemName+"】已停用或不存在！");
						returnMap.put("returnAmount", new BigDecimal("-1"));
						returnMap.put("isPass", "D");
						return returnMap;
						
					}
					msgTip.append("/【"+itemName+"】");
				}
				if(!"A".equalsIgnoreCase(map.get("deptId"))){
					String deptName = bgValidationMapper.getAllNameById(" SELECT DEPT_NAME FROM xip_pub_depts  WHERE DEPT_ID = '"+map.get("deptId")+"'");
					msgTip.append("/【"+deptName+"】");
				}
			}
			BigDecimal returnDecimal = bgValidationMapper.getProjectBudgetVal(map.get("ledgerId"), whereSql);
			if(returnDecimal == null){
				returnDecimal = new BigDecimal(0);
			}else if(returnDecimal.compareTo(ZERO_DECIMAL) == -1){
				returnMap.put("isPass", "N");
			}
			returnMap.put("msgTip", msgTip.toString());
			returnMap.put("returnAmount", returnDecimal);
			return returnMap;
		}
	}
	/* (non-Javadoc)
	 * @name     getFeeFactAmount
	 * @author   tangxl
	 * @date     2016年6月21日
	 * @注释                   TODO
	 * @version  1.0
	 * @see com.xzsoft.xc.bg.dao.BgValidationDAO#getFeeFactAmount(java.util.HashMap)
	 */
	@Override
	public BigDecimal getFeeFactAmount(HashMap<String, String> map)
			throws Exception {
		//查询符合条件的预算占用数和发生数
		/*ledgerControl -- 账簿层预算周期控制   1 -- 按年控制  2 -- 按月累计控制
		 *itemDimension -- 预算项目维度控制       1 -- 预算项目  2 -- 预算项目+成本中心
		 */
		String ledgerControl = map.get("ledgerControl");
		String itemDimension = map.get("itemDimension");
		String startDate = "";
		String endDate = "";
		StringBuffer sqlBuffer = new StringBuffer("select SUM(t.AMOUNT)  from xc_bg_fact  t where t.LEDGER_ID = ");
		sqlBuffer.append("'").append(map.get("ledgerId")).append("'");
		sqlBuffer.append("AND t.BG_ITEM_ID = ").append("'").append(map.get("itemId")).append("'");
		if(XCBGConstants.LEDGER_YEAR_CONTROL.equals(ledgerControl)){
			startDate  = map.get("bgYear").concat("-01-01 00:00:00");
			endDate  = map.get("bgYear").concat("-12-31 23:59:59");
			if(XCBGConstants.BG_EX_CTRL_DIM02.equals(itemDimension))
				sqlBuffer.append("AND t.DEPT_ID = ").append("'").append(map.get("deptId")).append("'");
			//拼接时间条件
			if("mysql".equals(PlatformUtil.getDbType())){
				sqlBuffer.append("AND t.FACT_DATE BETWEEN ").append("'").append(startDate).append("'");
				sqlBuffer.append("AND ").append("'").append(endDate).append("'");
			}else{
				sqlBuffer.append("AND t.FACT_DATE BETWEEN to_date(").append("'").append(startDate).append("','yyyy-MM-dd HH24:MI:SS')");
				sqlBuffer.append("AND to_date(").append("'").append(endDate).append("','yyyy-MM-dd HH24:MI:SS')");
			}
		}else if(XCBGConstants.LEDGER_MONTH_CONTROL.equals(ledgerControl)){
			startDate = map.get("bgYear").concat("-01-01 00:00:00");
			endDate =  map.get("bgDate").concat("23:59:59");
			if(XCBGConstants.BG_EX_CTRL_DIM02.equals(itemDimension))
				sqlBuffer.append("AND t.DEPT_ID = ").append("'").append(map.get("deptId")).append("'");
			//拼接时间条件
			if("mysql".equals(PlatformUtil.getDbType())){
				sqlBuffer.append("AND t.FACT_DATE BETWEEN ").append("'").append(startDate).append("'");
				sqlBuffer.append("AND ").append("'").append(endDate).append("'");
			}else{
				sqlBuffer.append("AND t.FACT_DATE BETWEEN to_date(").append("'").append(startDate).append("','yyyy-MM-dd HH24:MI:SS')");
				sqlBuffer.append("AND to_date(").append("'").append(endDate).append("','yyyy-MM-dd HH24:MI:SS')");
			}
		}
		return bgValidationMapper.getFeeBgFactVal(sqlBuffer.toString());
	}
	/* (non-Javadoc)
	 * @name     getProjectFactAmount
	 * @author   tangxl
	 * @date     2016年6月22日
	 * @注释                   TODO
	 * @version  1.0
	 * @see com.xzsoft.xc.bg.dao.BgValidationDAO#getProjectFactAmount(java.util.HashMap)
	 */
	@Override
	public BigDecimal getProjectFactAmount(HashMap<String, String> map)
			throws Exception {
		//查询符合条件的预算占用数和发生数  
		//项目预算校验只用校验项目层  账簿以及预算项目层控制都是针对预算项目而言
		//projectDimension->1  项目  projectDimension-->2项目+预算项目  projectDimension-->3-项目+预算项目+成本中心
		String projectDimension = map.get("projectDimension");
		StringBuffer sqlBuffer = new StringBuffer("select SUM(t.AMOUNT)  from xc_bg_fact  t where t.LEDGER_ID = ");
		sqlBuffer.append("'").append(map.get("ledgerId")).append("'");
		sqlBuffer.append("AND t.PROJECT_ID = ").append("'").append(map.get("projectId")).append("'");
		if (XCBGConstants.BG_PRJ_CTRL_DIM02.equals(projectDimension))
			sqlBuffer.append("AND t.BG_ITEM_ID = ").append("'").append(map.get("itemId")).append("'");
		if(XCBGConstants.BG_PRJ_CTRL_DIM03.equals(projectDimension)){
			sqlBuffer.append("AND t.BG_ITEM_ID = ").append("'").append(map.get("itemId")).append("'");
			sqlBuffer.append("AND t.DEPT_ID = ").append("'").append(map.get("deptId")).append("'");
		}
		return bgValidationMapper.getFeeBgFactVal(sqlBuffer.toString());
	}
	/* (non-Javadoc)
	 * @name     getSummayFeeBal
	 * @author   tangxl
	 * @date     2016年7月1日
	 * @注释                   根据预算版本Id获取待统计的费用预算申请单
	 * @version  1.0
	 * @see com.xzsoft.xc.bg.dao.BgValidationDAO#getSummayFeeBal(java.lang.String)
	 */
	@Override
	public List<BgBillBalance> getSummayFeeBal(String bgVersionId)
			throws Exception {
		// TODO Auto-generated method stub
		return bgValidationMapper.getSummayFeeBal(bgVersionId);
	}
	/* (non-Javadoc)
	 * @name     updateFeeBgVersion
	 * @author   tangxl
	 * @date     2016年7月4日
	 * @注释                   TODO
	 * @version  1.0
	 * @see com.xzsoft.xc.bg.dao.BgValidationDAO#updateFeeBgVersion(java.lang.String)
	 */
	@Override
	public void updateFeeBgVersion(String bgVersionId,String ledgerId) throws Exception {
		//1 更新当前生效版本
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("dbType", PlatformUtil.getDbType());
		map.put("bgVersionId", bgVersionId);
		map.put("userId", CurrentSessionVar.getUserId());
		bgValidationMapper.updateFeeBgVersion(map);
		//2失效原始生效版本
		bgValidationMapper.annulsFeeBgVersion(bgVersionId,ledgerId);
		//3删除原预算版本的余额记录
		bgValidationMapper.delOldVersionFeeBal();
	}
	/* (non-Javadoc)
	 * @name     方法名
	 * @author   tangxl
	 * @date     2016年7月4日
	 * @注释                   TODO
	 * @version  1.0
	 * @see com.xzsoft.xc.bg.dao.BgValidationDAO#inserNewVersionFee(java.util.List)
	 */
	@Override
	public void inserNewVersionFee(List<BgBillBalance> summaryList)
			throws Exception {
		if("mysql".equalsIgnoreCase(PlatformUtil.getDbType())){
			bgValidationMapper.insertFeeMysql(summaryList);
		}else{
			bgValidationMapper.insertFeeOracle(summaryList);
		}
	}
	/* (non-Javadoc)
	 * @name     getBgBillTypeList
	 * @author   tangxl
	 * @date     2016年7月6日
	 * @注释                   获取一类预算执行下的单据类型
	 * @version  1.0
	 * @see com.xzsoft.xc.bg.dao.BgValidationDAO#getBgBillTypeList(java.util.HashMap)
	 */
	@Override
	public List<String> getBgBillTypeList(HashMap<String, String> paramsMap)
			throws Exception {
		 return bgValidationMapper.getBgBillTypeList(paramsMap);
	}
	/* (non-Javadoc)
	 * @name     方法名
	 * @author   tangxl
	 * @date     2016年7月12日
	 * @注释                   TODO
	 * @version  1.0
	 * @see com.xzsoft.xc.bg.dao.BgValidationDAO#getValidateBgItemList(java.lang.String, java.lang.String)
	 */
	@Override
	public List<BgItemBean> getValidateBgItemList(String bgItemsId,
			String ledgerId) throws Exception {
		// TODO Auto-generated method stub
		List<BgItemBean> validateList =  bgValidationMapper.getValidateBgItemList(bgItemsId,ledgerId);
		if(validateList == null || validateList.size() == 0){
			 bgValidationMapper.delLedgerBgItems(bgItemsId,ledgerId);
		}
		return validateList;
	}
	/* (non-Javadoc)
	 * @name     方法名
	 * @author   tangxl
	 * @date     2016年9月18日
	 * @注释                   TODO
	 * @version  1.0
	 * @see com.xzsoft.xc.bg.dao.BgValidationDAO#cancelBudgetBal(java.util.List)
	 */
	@Override
	public JSONObject cancelBudgetBal(List<BgDocDtl> list) throws Exception {
		String bgType = list.get(0).getBgCatCode();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("dbType", PlatformUtil.getDbType());
		map.put("list", list);
		JSONObject result = new JSONObject();
		/*削减预算：
		 *删减预算时，不用考虑已经发生的预算业务，直接减少对应预算余额的值就行，对于已经发生的预算相关业务而言，后续的数据在控制的条件下业务无法再运行，因为并不会造成业务数据的混乱 
		 */
		if(XCBGConstants.BG_COST_TYPE.equalsIgnoreCase(bgType)){
			//费用预算
			bgValidationMapper.deleteCostBal(map);
		}else{
			//项目预算
			bgValidationMapper.deletePrjBal(map);
		}
		//更新预算申请单的状态
		bgValidationMapper.updateBgBillStatus(list.get(0).getBgDocId());
		result.put("flag", "0");
		result.put("msg", "项目预算申请回退成功！");
		return result;
	}
}
