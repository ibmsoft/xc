package com.xzsoft.xc.bg.dao.impl;

import java.text.SimpleDateFormat;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.bg.dao.BudgetCtrlDAO;
import com.xzsoft.xc.bg.mapper.BudgetCtrlMapper;
import com.xzsoft.xc.bg.modal.BgFactBean;

/**
 * @ClassName: BudgetCtrlDAOImpl 
 * @Description: 预算控制处理数据层接口实现类
 * @author linp
 * @date 2016年3月10日 上午9:39:56 
 *
 */
@Repository("budgetCtrlDAO")
public class BudgetCtrlDAOImpl implements BudgetCtrlDAO {
	@Resource
	private BudgetCtrlMapper budgetCtrlMapper;
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getProjectBudgetBal</p> 
	 * <p>Description: 取得项目预算余额 </p> 
	 * @param factBean
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.bg.dao.BudgetCtrlDAO#getProjectBudgetBal(com.xzsoft.xc.bg.modal.BgFactBean)
	 */
	@Override
	public double getProjectBudgetBal(BgFactBean factBean) throws Exception {
		double bal = 0 ;
		
		// 查询项目预算总额
		double totalBal = 0;
		try {
			totalBal = budgetCtrlMapper.getPrjBgTotalBal(factBean);
			
		} catch (Exception e) {
			totalBal = 0 ;
		}
		
		if(totalBal>0){
			// 计算预算发生和占用数
			double factBal = 0 ;
			try {
				factBal = budgetCtrlMapper.getPrjBgFactBal(factBean);
				
			} catch (Exception e) {
				factBal = 0 ;
			}
			// 计算预算余额
			bal = totalBal - factBal ;
			
		}else{
			// 无预算
			bal = 0 ;
		}
		
		return bal ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getCostBudgetBal</p> 
	 * <p>Description: 取得费用预算余额 </p> 
	 * @param factBean
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.bg.dao.BudgetCtrlDAO#getCostBudgetBal(com.xzsoft.xc.bg.modal.BgFactBean)
	 */
	@Override
	public double getCostBudgetBal(BgFactBean factBean) throws Exception {
		double bal = 0 ;
		// 业务日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;
		String fd = sdf.format(factBean.getFactDate()) ;
		
		// 会计年度
		String year = fd.substring(0, 4) ;			
		factBean.setYear(year);
		// 会计期
		String periodCode = fd.substring(0, 7) ; 	
		factBean.setPeriodCode(periodCode);
		
		// 查询预算总额
		double totalBal = 0;
		try {
			totalBal = budgetCtrlMapper.getCostBgTotalBal(factBean) ;
			
		} catch (Exception e) {
			totalBal = 0 ;
		}

		if(totalBal>0){
			// 计算预算发生和占用数
			double factBal = 0 ;
			try {
				factBal = budgetCtrlMapper.getCostBgFactBal(factBean);
				
			} catch (Exception e) {
				factBal = 0 ;
			}
			// 计算预算余额
			bal = totalBal - factBal ;
			
		}else{
			bal = 0 ;
		}
		
		return bal ;
	}
}
