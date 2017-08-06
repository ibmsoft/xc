package com.xzsoft.xc.gl.report.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.gl.report.dao.ReportAccountDetailDao;
import com.xzsoft.xc.gl.report.mapper.ReportAccountDetailMapper;
import com.xzsoft.xc.gl.report.mapper.ReportAssistantManageMapper;
import com.xzsoft.xc.gl.report.mapper.ReportDetailAccountMapper;
import com.xzsoft.xc.gl.report.mapper.ReportJournalMapper;
import com.xzsoft.xc.gl.report.modal.AccountStaticReport;
import com.xzsoft.xc.gl.report.modal.AssistantDetailReport;
import com.xzsoft.xc.gl.report.modal.AssistantStaticReport;
import com.xzsoft.xc.gl.report.modal.CashFlowReport;
import com.xzsoft.xc.gl.report.modal.GlAccountDetail;
import com.xzsoft.xc.gl.report.modal.JournalLedgerReport;
import com.xzsoft.xc.gl.report.modal.VDataReport;
import com.xzsoft.xip.platform.util.PlatformUtil;
@Repository("reportAccountDetailDao")
public class ReportAccountDetailDaoImpl implements ReportAccountDetailDao {
	@Resource
	private ReportAccountDetailMapper reportAccountDetailMapper;
	@Resource
	private ReportAssistantManageMapper reportAssistantManageMapper;
	@Resource
	private ReportDetailAccountMapper reportDetailAccountMapper;
	@Resource
	private ReportJournalMapper reportJournalMapper;
	/**
	 *获取总账明细、现金日记账、银行日记账 <过账和未过账>
	 */
	@Override
	public List<GlAccountDetail> getAccountDetailData(
			HashMap<String, String> map) throws Exception {
		map.put("dbType", PlatformUtil.getDbType().toLowerCase());
		if("N".equalsIgnoreCase(map.get("isAccount"))){
			return reportDetailAccountMapper.getGlAccountDetail(map);
		}else{
			return reportDetailAccountMapper.getGlAccountDetailT(map);
		}
	}
	@Override
	public List<CashFlowReport> getCashFlowReportData(
			HashMap<String, String> map) throws Exception {
		map.put("dbType", PlatformUtil.getDbType().toLowerCase());
		if("Y".equalsIgnoreCase(map.get("IS_STATIC"))){
			return reportAccountDetailMapper.getCashFlowReportData(map);
		}else{
			return reportAccountDetailMapper.getCashFlowReportDetailData(map);
		}
	}
	/*余额、总账取数规则：
	 *1、币种必须选，默认值为账簿的本位币
	 *2、如果所选币种与账簿本位币币种一致，此时只能查询金额式的账表格式
	 *3、如果所选币种与账簿币种不一致，此时币种并不是余额的筛选条件，而是账簿科目的筛选条件
	 */
	@Override
	public List<AccountStaticReport> getAccountantData(
			HashMap<String, String> map) {
		map.put("dbType", PlatformUtil.getDbType().toLowerCase());
		if(map.containsKey("isBalance")){
			//查询余额
			return reportAccountDetailMapper.getBalancesData(map);
		}else{
			//查询总账
			return reportAccountDetailMapper.getAccountantData(map);
		}
		
	}
	/**
	 * 
	 * @title:  getAccountantData
	 * @description:总分类账取数
	 * @note: 1、账簿为非外币式，即金额式的时候，不关心原币情形
	 *        2、账簿为外币式的时，不关心币种的筛选条件问题
	 * @author  tangxl
	 * @date    2016年3月28日
	 * @param map
	 * @return
	 */
	@Override
	public List<AssistantStaticReport> getAssistStaticData(
			HashMap<String, String> map) throws Exception {
		map.put("dbType", PlatformUtil.getDbType());
		//分组辅助统计报表
		if(map.containsKey("isGroupStatic")){
			return reportAssistantManageMapper.getGroupAssistStaticData(map);
		}else{
		//普通辅助统计报表
			return reportAssistantManageMapper.getAssistStaticData(map);
		}
		
	}
	/**
	 * 
	 * @title:  getGroupAssistData
	 * @description:辅助报表取数
	 * <p>
	 *   根据统计项对余额表进行汇总，汇总字段固定位 科目、会计期、科目方向以及11个辅助段中的一个
	 * </p>
	 * @author  tangxl
	 * @date    2016年3月31日
	 * @param map
	 * @return
	 */
	@Override
	public List<AssistantStaticReport> getGroupAssistData(
			HashMap<String, String> map) throws Exception {
		map.put("dbType", PlatformUtil.getDbType());
		return reportAssistantManageMapper.getGroupAssistStaticData(map);
	}
	/**
	 * 获取记账凭证
	 */
	@Override
	public List<VDataReport> getVDataReport(HashMap<String, String> map)
			throws Exception {
		map.put("dbType", PlatformUtil.getDbType().toLowerCase());
		if(map.containsKey("BATCH_PRINT")){
			return reportAccountDetailMapper.getBatchVData(map);
		}else{
			return reportAccountDetailMapper.getVDataReport(map) ;
		}
		
	}
	/**
	 * 
	 * @title:  getAssistDetailData
	 * @description:辅助报表取数
	 * <p>
	 *   统计余额明细记录
	 * </p>
	 * @author  tangxl
	 * @date    2016年3月31日
	 * @param map
	 * @return
	 */
	@Override
	public List<AssistantDetailReport> getAssistDetailData(
			HashMap<String, String> map) throws Exception{
		map.put("dbType", PlatformUtil.getDbType().toLowerCase());
		return reportAssistantManageMapper.getAssistDetailData(map);
	}
	/* (non-Javadoc)
	 * @name     getJournalLedgerData
	 * @author   tangxl
	 * @date     2016年9月23日
	 * @注释                   TODO
	 * @version  1.0
	 * @see com.xzsoft.xc.gl.report.dao.ReportAccountDetailDao#getJournalLedgerData(java.util.HashMap)
	 */
	@Override
	public List<JournalLedgerReport> getJournalLedgerData(
			HashMap<String, String> map) throws Exception {
		if(!map.containsKey("fetchTitle")){
			return reportJournalMapper.getJournalLedgerData(map);
		}else{
			List<JournalLedgerReport> reportList = new ArrayList<JournalLedgerReport>();
			List<HashMap<String, String>> list = reportJournalMapper.getJournalLedgerTitle(map);
			if(list != null && list.size()>0){
				JournalLedgerReport j = new JournalLedgerReport();
				for(HashMap<String, String> t:list){
					String key = t.get("segCode");
					String value = t.get("segName");
					switch (key) {
					case "XC_AP_VENDORS":
						j.setVendorName(value);
						break;
					case "XC_AR_CUSTOMERS":
						j.setCustomerName(value);
						break;
					case "XIP_PUB_EMPS":
						j.setEmpName(value);
						break;
					case "XIP_PUB_ORGS":
						j.setOrgName(value);
						break;
					case "XC_GL_PRODUCTS":
						j.setProductName(value);
						break;
					case "XIP_PUB_DEPTS":
						j.setDeptName(value);
						break;
					case "XC_PM_PROJECTS":
						j.setProjectName(value);
						break;
					case "XC_GL_CUSTOM1":
						j.setCustom1Name(value);
						break;
					case "XC_GL_CUSTOM2":
						j.setCustom2Name(value);
						break;
					case "XC_GL_CUSTOM3":
						j.setCustom3Name(value);
						break;
					case "XC_GL_CUSTOM4":
						j.setCustom4Name(value);
						break;
					default:
						break;
					}
				}
			reportList.add(j);
			} 
			return reportList;
		}
	}
}
