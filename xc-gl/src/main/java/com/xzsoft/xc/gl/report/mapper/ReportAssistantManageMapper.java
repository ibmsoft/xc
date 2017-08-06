package com.xzsoft.xc.gl.report.mapper;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.gl.report.modal.AccountStaticReport;
import com.xzsoft.xc.gl.report.modal.AssistantDetailReport;
import com.xzsoft.xc.gl.report.modal.AssistantStaticReport;
import com.xzsoft.xc.gl.report.modal.CashFlowReport;
import com.xzsoft.xc.gl.report.modal.GlAccountDetail;


/**
 * @fileName ReportAccountDetailMapper
 * @desc (润乾报表--->总账明细账取数接口) 
 * @author tangxl
 * @date 2016年3月23日
 */
public interface ReportAssistantManageMapper {
	/**
	 * 
	 * @title:  getAssistStaticData
	 * @description:  辅助统计报表取数
	 * <p>
	 *   根据查询条件所选择的辅助项进行分组
	 * </p>
	 * @author  tangxl
	 * @date    2016年3月30日
	 * @param   map
	 * @return
	 */
	public List<AssistantStaticReport> getAssistStaticData(HashMap<String, String> map);
	/**
	 * 
	 * @title:  getGroupAssistStaticData
	 * @description:  分组辅助统计报表取数
	 * <p>
	 *   根据查询条件所选择的辅助项进行分组，如选择 供应商、客户、产品，按照科目、会计期、科目方向、供应商、客户、产品对余额表进行分组汇总.
	 * </p>
	 * @author  tangxl
	 * @date    2016年3月30日
	 * @param   map
	 * @return
	 */
	public List<AssistantStaticReport> getGroupAssistStaticData(HashMap<String, String> map);
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
	public List<AssistantDetailReport> getAssistDetailData(HashMap<String, String> map);
	

}
