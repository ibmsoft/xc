package com.xzsoft.xc.gl.report.dao;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.gl.report.modal.AccountStaticReport;
import com.xzsoft.xc.gl.report.modal.AssistantDetailReport;
import com.xzsoft.xc.gl.report.modal.AssistantStaticReport;
import com.xzsoft.xc.gl.report.modal.CashFlowReport;
import com.xzsoft.xc.gl.report.modal.GlAccountDetail;
import com.xzsoft.xc.gl.report.modal.JournalLedgerReport;
import com.xzsoft.xc.gl.report.modal.VDataReport;

/**
 * @fileName ReportAccountDetailService
 * @desc 润乾账表取数接口
 * @author tangxl
 * @date 2016年3月23日
 * 
 */
public interface ReportAccountDetailDao {
	/**
	 * @title:  getAccountDetailData
	 * @desc:   总账明细、现金日记账、银行日记账 取数接口
	 * @author  tangxl
	 * @date    2016年3月23日
	 * @param   map
	 * @return  List<GlAccountDetail>
	 * @throws  Exception
	 */
	public List<GlAccountDetail> getAccountDetailData(HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * @title:  ReportAccountDetailDao.java
	 * @desc:   现金流量报表统计
	 * @author  tangxl
	 * @date    2016年3月24日
	 * @param   map
	 * @return
	 * @throws Exception
	 */
	public List<CashFlowReport> getCashFlowReportData(HashMap<String, String> map) throws Exception;
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
	public List<AccountStaticReport> getAccountantData(HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * @title:  getAssistStaticData
	 * @description:  辅助统计报表取数
	 * <p>
	 *   根据查询条件所选择的辅助项进行分组，如选择 供应商、客户、产品，按照科目、会计期、科目方向、供应商、客户、产品对余额表进行分组汇总.
	 *   并对汇总后的结果按照 小计项<如果有小计项>升序、会计期升序、科目升序排列
	 * </p>
	 * @author  tangxl
	 * @date    2016年3月30日
	 * @param   map
	 * @return
	 */
	public List<AssistantStaticReport> getAssistStaticData(HashMap<String, String> map) throws Exception;
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
	public List<AssistantStaticReport> getGroupAssistData(HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * @title: getVDataReport
	 * @description:记账凭证取数
	 * @author zhenghy
	 * @date   2016年3月29日
	 * @param map
	 * @return
	 */
	public List<VDataReport> getVDataReport(HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * @title:  getAssistDetailData
	 * @description:   TODO
	 * @author  tangxl
	 * @date    2016年4月4日
	 * @param map
	 * @return
	 */
	public List<AssistantDetailReport> getAssistDetailData(HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * @methodName  getJournalLedgerData
	 * @author      tangxl
	 * @date        2016年9月23日
	 * @describe    TODO
	 * @param 		map
	 * @return
	 * @throws 		Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public List<JournalLedgerReport> getJournalLedgerData(HashMap<String, String> map) throws Exception;
}
