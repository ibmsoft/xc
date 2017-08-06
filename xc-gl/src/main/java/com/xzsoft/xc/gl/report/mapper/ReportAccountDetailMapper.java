package com.xzsoft.xc.gl.report.mapper;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.gl.report.modal.AccountStaticReport;
import com.xzsoft.xc.gl.report.modal.CashFlowReport;
import com.xzsoft.xc.gl.report.modal.GlAccountDetail;
import com.xzsoft.xc.gl.report.modal.VDataReport;


/**
 * @fileName ReportAccountDetailMapper
 * @desc (总账明细账取数接口) 
 * @author tangxl
 * @date 2016年3月23日
 */
public interface ReportAccountDetailMapper {
	/**
	 * @title:  ReportAccountDetailMapper.java
	 * @desc:   现金流量报表取数
	 * @author  tangxl
	 * @date    2016年3月24日
	 * @param   map
	 * @return
	 */
	public List<CashFlowReport> getCashFlowReportData(HashMap<String, String> map);
	/**
	 * @title:  ReportAccountDetailMapper.java
	 * @desc:   现金流量明细报表取数
	 * @author  tangxl
	 * @date    2016年3月25日
	 * @param map
	 * @return
	 */
	public List<CashFlowReport> getCashFlowReportDetailData(HashMap<String, String> map);
	/**
	 * 
	 * @title:  getAccountantData
	 * @description:   总账分类取数接口
	 * @note 总账取数注意点：1、若账簿格式为非外币式的时候，此时不关心原币的处理问题，即不查询原币信息。
	 *                  2、若账簿格式为外币式的时候，此时不关心币种为何种外币，统一合并余额表原币行为原币列。
	 * @author  tangxl
	 * @date    2016年3月25日
	 * @param map
	 * @return
	 */
	public List<AccountStaticReport> getAccountantData(HashMap<String, String> map);
	/**
	 * 
	 * @title:  getBalancesData
	 * @description:   余额取数接口，类似于总分类账取数接口
	 * @author  tangxl
	 * @date    2016年3月30日
	 * @param map
	 * @return
	 */
	public List<AccountStaticReport> getBalancesData(HashMap<String, String> map);
	/**
	 * @title: getVDataReport
	 * @description : 获取记账凭证
	 * 
	 * @author zhenghy
	 * @date   2016年3月25日
	 * @param map
	 * @return
	 */
	public List<VDataReport> getVDataReport(HashMap<String, String> map);
	
	public List<VDataReport> getBatchVData(HashMap<String, String> map);
	

}
