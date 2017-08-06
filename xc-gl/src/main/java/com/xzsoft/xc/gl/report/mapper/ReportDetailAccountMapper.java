package com.xzsoft.xc.gl.report.mapper;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.gl.report.modal.GlAccountDetail;


/**
 * @fileName ReportDetailAccountMapper
 * @desc 报表--->科目明细账
 * @author tangxl
 * @date 2016年3月23日
 */
public interface ReportDetailAccountMapper {
	/**
	 * @title:  getGlAccountDetail
	 * @desc:   科目明细账取数<已过账>
	 * @author  tangxl
	 * @date    2016年3月23日
	 * @param map
	 * @return
	 */
	public List<GlAccountDetail> getGlAccountDetail(HashMap<String, String> map);
	/**
	 * @title:  getGlAccountDetailT
	 * @desc:    科目明细账取数<未过账>
	 * @author  tangxl
	 * @date    2016年3月23日
	 * @param map
	 * @return
	 */
	public List<GlAccountDetail> getGlAccountDetailT(HashMap<String, String> map);
}
