package com.xzsoft.xc.bg.service.impl;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xzsoft.xc.bg.dao.BudgetDataHandlerDAO;
import com.xzsoft.xc.bg.dao.XCBGCommonDao;
import com.xzsoft.xc.bg.modal.BgBillBalance;
import com.xzsoft.xc.bg.modal.BgDocDtl;
import com.xzsoft.xc.bg.modal.BgFactBean;
import com.xzsoft.xc.bg.service.BgAuditService;
import com.xzsoft.xc.bg.service.BudgetDataHandlerService;
import com.xzsoft.xc.bg.util.XCBGCalculateBalUtil;
import com.xzsoft.xc.bg.util.XCBGConstants;
import com.xzsoft.xc.util.modal.Project;

/**
 * @ClassName: BudgetDataHandlerServiceImpl 
 * @Description: 预算余额处理服务接口实现类
 * @author linp
 * @date 2016年3月10日 上午9:41:40 
 *
 */
@Service("budgetDataHandlerService")
public class BudgetDataHandlerServiceImpl implements BudgetDataHandlerService {
	
	// 日志记录器
	private static final Logger log = Logger.getLogger(BudgetDataHandlerServiceImpl.class.getName());
	@Resource
	private XCBGCommonDao xcbgCommonDao;
	@Resource
	private BudgetDataHandlerDAO budgetDataHandlerDAO;
	@Resource
	private BgAuditService bgAuditService;
	/*
	 * 
	  * <p>Title: budgetDataInsert</p>
	  * <p>Description: </p>
	  * @param bgDocId
	  * @param opType
	  * @return
	  * @see com.xzsoft.xc.bg.service.BudgetDataHandlerService#budgetDataInsert(java.lang.String,java.lang.String)
	  * @modified：移除对预算为负数时的【 减少额度+发生额度>总额度】的判断，理由：预算即使减少也不会影响后续业务数据的正确性，并且判断会导致减少预算的业务无法进行
	  *            因此移除2.4步骤的预算减少判断。
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public String budgetDataInsert(String bizId,String loginName,String opType) throws Exception{
		try{
			//1-- 获取预算单及其明细信息
			List<BgDocDtl> bgDocDtlList = xcbgCommonDao.getDocDtl(bizId);
			//2--根据预算单的类型，获取对应预算单的余额信息<BG01-费用预算单  BG02-项目预算单>
			if(bgDocDtlList != null && bgDocDtlList.size() > 0){
				String ledgerId = bgDocDtlList.get(0).getLedgerId();
				String bgItemType = bgDocDtlList.get(0).getBgCatCode();
				String bgYear = "";
				String startDate = "";
				String endDate = "";
				HashMap<String, String> paramsMap = new HashMap<String, String>();
				paramsMap.put("bgType", bgItemType);
				paramsMap.put("ledgerId", ledgerId);
				if(XCBGConstants.BG_COST_TYPE.equalsIgnoreCase(bgItemType)){
					paramsMap.put("periodCode",bgDocDtlList.get(0).getPeriodCode());
					bgYear = bgDocDtlList.get(0).getPeriodCode().substring(0, 4);
					paramsMap.put("bgYear", bgYear);
					startDate = bgYear.concat("-01-01 00:00:00");
					endDate = bgYear.concat("-12-31 23:59:59");
				}else{
					paramsMap.put("projectId", bgDocDtlList.get(0).getProjectId());
					Project project = budgetDataHandlerDAO.getProjectById(bgDocDtlList.get(0).getProjectId());
					SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					startDate = sdf.format(project.getStartDate());
					endDate = sdf.format(project.getEndDate());
				}
				paramsMap.put("startDate", startDate);
				paramsMap.put("endDate", endDate);
				//2.1-- 根据【预算项目】和【成本中心】合并当前预算申请单下的明细行申请金额
				/*
				 *addList     业务表单对应的预算余额项 
				 *updateList  需要更新的预算余额项
				 */
				List<BgBillBalance> addList = XCBGCalculateBalUtil.sumBudgetItemBal(bgDocDtlList,bgItemType);
				//2.2-- 根据【预算申请单信息】获取预算汇总额度
				List<BgBillBalance> balancesList = budgetDataHandlerDAO.getBudgetBalance(paramsMap);
				//2.3-- 根据【预算申请单信息】获取预算实际发生额度
				//List<BgFactBalance> factBalanceList = budgetDataHandlerDAO.getFactBudgetAmount(paramsMap);
				if(balancesList!= null &&balancesList.size()>0){
					List<BgBillBalance> updateList = XCBGCalculateBalUtil.summaryBgBalances(addList,balancesList, bgDocDtlList.get(0).getBgCatCode());
					/*
					//2.4-- 判断【预算申请明细数】+【预算实际发生数】是否大于【预算汇总余额数】
					if(factBalanceList != null && factBalanceList.size()>0){
						XCBGCalculateBalUtil.judgeBgIsValid(updateList,factBalanceList,bgItemType);
					}*/
					//2.5-- 根据【预算项目】和【成本中心】 更新【预算汇总余额数】
					budgetDataHandlerDAO.updateBgItemBalances(updateList,addList,bgItemType);
				}else{
					//4.1 --无预算项目余额直接插入新的预算余额记录
					budgetDataHandlerDAO.updateBgItemBalances(null,addList,bgItemType);
				}
			}else {
				throw new Exception("当前预算单没有明细信息！");
			}
			//余额汇总成功之后更新审批通过日期
			bgAuditService.updateBgAuditDate(bizId,loginName);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw e;
		}
		return "";
	}
	
	
	/* (non-Javadoc)
	 * @name     incrOccupancyBudget
	 * @author   tangxl
	 * @date     2016年6月22日
	 * @注释                   新增预算占用数
	 * @version  1.0
	 * @see com.xzsoft.xc.bg.service.BudgetDataHandlerService#incrOccupancyBudget(java.util.List)
	 */
	@Override
	public void incrOccupancyBudget(List<BgFactBean> list) throws Exception {
		budgetDataHandlerDAO.incrOccupancyBudget(list);
	}
	/* (non-Javadoc)
	 * @name     updateFactBudget
	 * @author   tangxl
	 * @date     2016年6月22日
	 * @注释                   修改预算数类型<占用-->发生；发生-->占用>
	 * @version  1.0
	 * @see com.xzsoft.xc.bg.service.BudgetDataHandlerService#updateFactBudget(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void updateFactBudget(String srcId, String targetType,
			String originalType) throws Exception {
		budgetDataHandlerDAO.updateFactBudget(srcId, targetType, originalType);
	}
	/* (non-Javadoc)
	 * @name     deleteFactBudget
	 * @author   tangxl
	 * @date     2016年6月22日
	 * @注释                   删除预算数<删除预算占用数  删除预算发生数，理论上是不可能出现删除预算发生数的>
	 * @version  1.0
	 * @see com.xzsoft.xc.bg.service.BudgetDataHandlerService#deleteFactBudget(java.lang.String, java.lang.String)
	 */
	@Override
	public void deleteFactBudget(String srcId, String factType)
			throws Exception {
		budgetDataHandlerDAO.deleteFactBudget(srcId, factType);
	}
}
