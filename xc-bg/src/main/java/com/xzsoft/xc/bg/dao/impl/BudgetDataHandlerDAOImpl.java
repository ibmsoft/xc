package com.xzsoft.xc.bg.dao.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.xzsoft.xc.bg.dao.BudgetDataHandlerDAO;
import com.xzsoft.xc.bg.mapper.BudgetDataHandlerMapper;
import com.xzsoft.xc.bg.modal.BgBillBalance;
import com.xzsoft.xc.bg.modal.BgCostPrjBalBean;
import com.xzsoft.xc.bg.modal.BgFactBalance;
import com.xzsoft.xc.bg.modal.BgFactBean;
import com.xzsoft.xc.bg.util.XCBGConstants;
import com.xzsoft.xc.util.modal.Project;
import com.xzsoft.xip.platform.util.PlatformUtil;

/**
 * @ClassName: BudgetDataHandlerDAOImpl 
 * @Description: 预算余额处理数据层接口实现类
 * @author linp
 * @date 2016年3月10日 上午9:40:35 
 *
 */
@Repository("budgetDataHandlerDAO")
public class BudgetDataHandlerDAOImpl implements BudgetDataHandlerDAO {
	private static final Logger log = Logger.getLogger(BudgetDataHandlerDAOImpl.class);
	@Resource
	private BudgetDataHandlerMapper budgetDataHandlerMapper;
	
	/*
	 * 
	  * <p>Title: budgetDataSelect</p>
	  * <p>Description: 查询预算余额汇总是否有相同的数据</p>
	  * @param bgCostPrjBal
	  * @throws Exception
	  * @see com.xzsoft.xc.bg.dao.BudgetDataHandlerDAO#budgetDataSelect(com.xzsoft.xc.bg.modal.BgCostPrjBalBean)
	 */
	public float budgetDataSelect(BgCostPrjBalBean bgCostPrjBal) throws Exception{
		float returnInfo = 0;
		try {
			returnInfo = budgetDataHandlerMapper.budgetDataSelect(bgCostPrjBal);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return returnInfo;
	};
	/*
	 * 
	  * <p>Title: budgetDataInsert</p>
	  * <p>Description: 插入预算余额表</p>
	  * @param bgCostBal
	  * @see com.xzsoft.xc.bg.dao.BudgetDataHandlerDAO#budgetDataInsert(com.xzsoft.xc.bg.modal.BgCostBal)
	 */
	@Override
	public void budgetDataInsert(List<BgCostPrjBalBean> bgCostPrjBalLists) throws Exception{
		try {
			if(bgCostPrjBalLists != null && bgCostPrjBalLists.size() > 0){
				for(BgCostPrjBalBean balBean : bgCostPrjBalLists){
					budgetDataHandlerMapper.budgetDataInsert(balBean);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	/*
	 * 
	  * <p>Title: budgetDataUpdate</p>
	  * <p>Description: 更新预算余额表</p>
	  * @param map
	  * @throws Exception
	  * @see com.xzsoft.xc.bg.dao.BudgetDataHandlerDAO#budgetDataUpdate(java.util.HashMap)
	 */
	@Override
	public void budgetDataUpdate(List<BgCostPrjBalBean> bgCostPrjBalLists) throws Exception{
		try {
			if(bgCostPrjBalLists != null && bgCostPrjBalLists.size() > 0){
				for(BgCostPrjBalBean balBean : bgCostPrjBalLists){
					budgetDataHandlerMapper.budgetDataUpdate(balBean);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	/* (non-Javadoc)
	 * @name     方法名
	 * @author   tangxl
	 * @date     2016年6月17日
	 * @注释                   TODO
	 * @version  1.0
	 * @see com.xzsoft.xc.bg.dao.BudgetDataHandlerDAO#getBudgetBalance(java.util.HashMap)
	 */
	@Override
	public List<BgBillBalance> getBudgetBalance(HashMap<String, String> map)
			throws Exception {
		return budgetDataHandlerMapper.getBudgetBalance(map);
	}
	/* (non-Javadoc)
	 * @name     updateBgItemBalances
	 * @author   tangxl
	 * @date     2016年6月20日
	 * @注释                   更新预算余额记录
	 * @version  1.0
	 * @see com.xzsoft.xc.bg.dao.BudgetDataHandlerDAO#updateBgItemBalances(java.util.List, java.util.List)
	 */
	@Override
	public void updateBgItemBalances(List<BgBillBalance> updateList,
			List<BgBillBalance> insertList,String bgItemType) throws Exception {
		//1--新增余额记录
		if(insertList !=null && insertList.size()>0){
			if(XCBGConstants.BG_COST_TYPE.equalsIgnoreCase(bgItemType) && "mysql".equalsIgnoreCase(PlatformUtil.getDbType())){
				budgetDataHandlerMapper.insertCostBgBalMysql(insertList);
			}else if(XCBGConstants.BG_COST_TYPE.equalsIgnoreCase(bgItemType) && "oracle".equalsIgnoreCase(PlatformUtil.getDbType())){
				budgetDataHandlerMapper.insertCostBgBalOracle(insertList);
			}else if(XCBGConstants.BG_PRO_TYPE.equalsIgnoreCase(bgItemType) && "mysql".equalsIgnoreCase(PlatformUtil.getDbType())){
				budgetDataHandlerMapper.insertProjectBgBalMysql(insertList);
			}else{
				budgetDataHandlerMapper.insertProjectBgBalOracle(insertList);
			}
		}
		//2 --更新余额记录
		if(updateList !=null && updateList.size()>0){
			if(XCBGConstants.BG_COST_TYPE.equalsIgnoreCase(bgItemType) && "mysql".equalsIgnoreCase(PlatformUtil.getDbType())){
				budgetDataHandlerMapper.updateCostBgBalMysql(updateList);
			}else if(XCBGConstants.BG_COST_TYPE.equalsIgnoreCase(bgItemType) && "oracle".equalsIgnoreCase(PlatformUtil.getDbType())){
				budgetDataHandlerMapper.updateCostBgBalOracle(updateList);
			}else if(XCBGConstants.BG_PRO_TYPE.equalsIgnoreCase(bgItemType) && "mysql".equalsIgnoreCase(PlatformUtil.getDbType())){
				budgetDataHandlerMapper.updateProjectBgBalMysql(updateList);
			}else{
				budgetDataHandlerMapper.updateProjectBgBalOracle(updateList);
			}
		}
		
	}
	/* (non-Javadoc)
	 * @name     方法名
	 * @author   tangxl
	 * @date     2016年6月22日
	 * @注释                   TODO
	 * @version  1.0
	 * @see com.xzsoft.xc.bg.dao.BudgetDataHandlerDAO#incrOccupancyBudget(java.util.List)
	 */
	@Override
	public void incrOccupancyBudget(List<BgFactBean> list) throws Exception {
		HashMap<String,Object> map = new HashMap<String,Object>() ;
		map.put("list", list) ;
		
		if("mysql".equalsIgnoreCase(PlatformUtil.getDbType())){
			budgetDataHandlerMapper.increaseBudgetMysql(map);
		}else{
			budgetDataHandlerMapper.increaseBudgetOracle(map);
		}
	}
	/* (non-Javadoc)
	 * @name     方法名
	 * @author   tangxl
	 * @date     2016年6月22日
	 * @注释                   TODO
	 * @version  1.0
	 * @see com.xzsoft.xc.bg.dao.BudgetDataHandlerDAO#updateFactBudget(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void updateFactBudget(String srcId, String targetType,
			String originalType) throws Exception {
		budgetDataHandlerMapper.updateFactBudget(srcId,targetType,originalType,PlatformUtil.getDbType());
	}
	/* (non-Javadoc)
	 * @name     方法名
	 * @author   tangxl
	 * @date     2016年6月22日
	 * @注释                   TODO
	 * @version  1.0
	 * @see com.xzsoft.xc.bg.dao.BudgetDataHandlerDAO#deleteFactBudget(java.lang.String, java.lang.String)
	 */
	@Override
	public void deleteFactBudget(String srcId, String factType)
			throws Exception {
		budgetDataHandlerMapper.deleteFactBudget(srcId,factType);
		
	}
	/* (non-Javadoc)
	 * @name     方法名
	 * @author   tangxl
	 * @date     2016年6月24日
	 * @注释                   TODO
	 * @version  1.0
	 * @see com.xzsoft.xc.bg.dao.BudgetDataHandlerDAO#getAlltBalance(java.lang.String)
	 */
	@Override
	public List<BgFactBalance> getFactBudgetAmount(HashMap<String, String> map) throws Exception {
		map.put("dbType", PlatformUtil.getDbType());
		return budgetDataHandlerMapper.getFactBalance(map);
		
	}
	/* (non-Javadoc)
	 * @name     方法名
	 * @author   tangxl
	 * @date     2016年6月24日
	 * @注释                   TODO
	 * @version  1.0
	 * @see com.xzsoft.xc.bg.dao.BudgetDataHandlerDAO#getProjectById(java.lang.String)
	 */
	@Override
	public Project getProjectById(String projectId) throws Exception {
		// TODO Auto-generated method stub
		return budgetDataHandlerMapper.getProjectById(projectId);
	}
}