package com.xzsoft.xc.gl.util;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.xzsoft.xc.gl.report.modal.AssistantDetailReport;
import com.xzsoft.xc.gl.report.modal.GlAccountDetail;


public class LedgerPeriodUtil{
	private final static List<String> monthList1 = Arrays.asList("01","03","05","07","08","10","12");
	private final static List<String> monthList2 = Arrays.asList("04","06","08","09","11");
	/**
	 * 
	 * @title:  calculateNextPeriod
	 * @description:计算要追加的下一个自然会计期
	 * @author  tangxl
	 * @date    2016年3月28日
	 * @param   periodCode
	 * @return  java.lang.String
	 */
	public static String calculateNextPeriod(String periodCode){
		//获取会计期间的年份
		String yearCode = periodCode.substring(0, 4);
		String monthCode = periodCode.substring(periodCode.lastIndexOf("-")+1);
		if(periodCode.endsWith("-12")){
			monthCode = "-01";	
			yearCode = String.valueOf(Integer.parseInt(yearCode)+1);
		}else if(periodCode.endsWith("-Q4")){
			monthCode = "-01";
			yearCode = (Integer.valueOf(yearCode)+1)+"";
		}else if(periodCode.endsWith("-Q1")){
			monthCode = "-04";
		}else if(periodCode.endsWith("-Q2")){
			monthCode = "-07";
		}else if(periodCode.endsWith("-Q3")){
			monthCode = "-10";
		}else{
			int monthNum = Integer.valueOf(monthCode)+1;
			if(monthNum<10){
				monthCode = "-0"+monthNum+"";
			}else{
				monthCode = "-"+monthNum+"";
			}
		}
		return yearCode+monthCode;
	}
	/**
	 * 
	 * @title:  calculatePreviousPeriod
	 * @description:计算要追加调整期的上一个会计期间
	 * @author  tangxl
	 * @date    2016年3月28日
	 * @param   periodCode
	 * @return  java.lang.String
	 */
	public static String calculatePreviousPeriod(String periodCode){
		//获取会计期间的年份
		String yearCode = periodCode.substring(0, 4);
		String monthCode = periodCode.substring(periodCode.lastIndexOf("-")+1);
       if(periodCode.endsWith("-Q4")){
			monthCode = "-12";
			yearCode = (Integer.valueOf(yearCode)-1)+"";
		}else if(periodCode.endsWith("-Q1")){
			monthCode = "-03";
		}else if(periodCode.endsWith("-Q2")){
			monthCode = "-06";
		}else if(periodCode.endsWith("-Q3")){
			monthCode = "-09";
		}
		return yearCode+monthCode;
	}
	/**
	 * 
	 * @title:     sortAssistDetailReportData
	 * @description:
	 * <p>
	 * 处理辅助明细数据格式
	 * 1：合并期初数
	 * 2：计算每个期间的本期发生以及本年发生数
	 * 3：不显示无发生数的期间统计数<待定>
	 * 4:累加处理余额
	 * </p>
	 * @author     tangxl
	 * @date       2016年4月4日
	 * @return
	 */
	public static void sortAssistDetailReportData(List<AssistantDetailReport> detailList){
		//计算期间发生数及本年发生数的依据 periodCode
		//合并期初发生数的依据  ccid
		AssistantDetailReport initalDetail =detailList.get(0);
		AssistantDetailReport lastDetail = null;
		for(int i=0;i<detailList.size();i++){
			AssistantDetailReport assist = detailList.get(i);
			String isAdd = assist.getIsAddFlag();
			//==============计算每个会计期的本期发生数和本年累计发生数
			//==============计算本期发生数以及本年发生数前，需对已有的数据进行余额累加处理
			if ((!assist.getACC_CODE().equals(initalDetail.getACC_CODE())
					|| !assist.getPERIOD_CODE().equals(initalDetail.getPERIOD_CODE())) && "N".equals(isAdd)) {
				//-------------------添加本月、本年累计数-----------------------------
				AssistantDetailReport addDetail = new AssistantDetailReport();
				addDetail.setIsAddFlag("Y");
				addDetail.setPERIOD_CODE(initalDetail.getPERIOD_CODE());
				addDetail.setV_HEAD_ID("");
				addDetail.setACCOUNT_DATE(calcuteAccountDate(initalDetail.getPERIOD_CODE()));
				addDetail.setACC_ID(initalDetail.getACC_ID());
				addDetail.setACC_CODE(initalDetail.getACC_CODE());
				addDetail.setACC_NAME(initalDetail.getACC_NAME());
				addDetail.setVOUCHER_NUM("");
				addDetail.setSUMMARY("本月合计");
				addDetail.setJF_JE(initalDetail.getBQLJ_JF());
				addDetail.setT_JF_JE(initalDetail.getT_BQLJ_JF());
				addDetail.setBQLJ_DF(initalDetail.getBQLJ_DF());
				addDetail.setT_BQLJ_DF(initalDetail.getT_BQLJ_DF());
				addDetail.setQM_YE(initalDetail.getQM_YE());
				addDetail.setT_QM_YE(initalDetail.getT_QM_YE());
				addDetail.setBALANCE_DIRECTION_NAME("");
				detailList.add(i, addDetail);
				//------------------------添加本年累计数<期末金额是一致的>-----------
				AssistantDetailReport yearDetail = addDetail.clone();
				yearDetail.setSUMMARY("本年累计");
				yearDetail.setJF_JE(initalDetail.getBNLJ_JF());
				yearDetail.setT_JF_JE(initalDetail.getT_BNLJ_JF());
				yearDetail.setBQLJ_DF(initalDetail.getBNLJ_DF());
				yearDetail.setT_BQLJ_DF(initalDetail.getT_BNLJ_DF());
				detailList.add(i+1,yearDetail);
				//更改对比标准
				initalDetail = assist;
			}
		}
		lastDetail = detailList.get(detailList.size()-1);
		//处理最后一条记录所对应的  本月累计/本年累计
		if("N".equalsIgnoreCase(lastDetail.getIsAddFlag())){
			//-------------------添加本月、本年累计数-----------------------------
			AssistantDetailReport addDetail = new AssistantDetailReport();
			addDetail.setIsAddFlag("Y");
			addDetail.setPERIOD_CODE(initalDetail.getPERIOD_CODE());
			addDetail.setV_HEAD_ID("");
			addDetail.setACCOUNT_DATE(calcuteAccountDate(initalDetail.getPERIOD_CODE()));
			addDetail.setACC_ID(initalDetail.getACC_ID());
			addDetail.setACC_CODE(initalDetail.getACC_CODE());
			addDetail.setACC_NAME(initalDetail.getACC_NAME());
			addDetail.setVOUCHER_NUM("");
			addDetail.setSUMMARY("本月合计");
			addDetail.setJF_JE(initalDetail.getBQLJ_JF());
			addDetail.setT_JF_JE(initalDetail.getT_BQLJ_JF());
			addDetail.setBQLJ_DF(initalDetail.getBQLJ_DF());
			addDetail.setT_BQLJ_DF(initalDetail.getT_BQLJ_DF());
			addDetail.setQM_YE(initalDetail.getQM_YE());
			addDetail.setT_QM_YE(initalDetail.getT_QM_YE());
			addDetail.setBALANCE_DIRECTION_NAME("");
			detailList.add(addDetail);
			//------------------------添加本年累计数<期末金额是一致的>-----------
			AssistantDetailReport yearDetail = addDetail.clone();
			yearDetail.setSUMMARY("本年累计");
			yearDetail.setJF_JE(initalDetail.getBNLJ_JF());
			yearDetail.setT_JF_JE(initalDetail.getT_BNLJ_JF());
			yearDetail.setBQLJ_DF(initalDetail.getBNLJ_DF());
			yearDetail.setT_BQLJ_DF(initalDetail.getT_BNLJ_DF());
			detailList.add(yearDetail);
		}
		//合并相同科目组合的期初值，即：相同ccid值显示一个期初值,合并时要排除手动添加的记录
		//只允许将同一科目组合下只存在一个期初值。
		for(int j=0;j<detailList.size();j++){
			if(j<detailList.size()-1){
				//当前记录
				AssistantDetailReport currenctObj = detailList.get(j);
				//取得当前记录的下一条记录
				AssistantDetailReport nextAssist = detailList.get(j+1);
				if (currenctObj.getACC_CODE().equals(nextAssist.getACC_CODE())
						&& "N".equalsIgnoreCase(nextAssist.getIsAddFlag())
						&& "".equals(nextAssist.getACCOUNT_DATE())) {
					detailList.remove(nextAssist);
				}
			}
		}
	}
	/**
	 * 
	 * @title:     sortAccountDetailReportData
	 * @description:
	 * <p>
	 * 处理明细账数据格式
	 * 1：合并期初数
	 * 2：计算每个期间的本期发生以及本年发生数
	 * 3：不显示无发生数的期间统计数<待定>
	 * 4:累加处理余额
	 * </p>
	 * @author     tangxl
	 * @date       2016年4月4日
	 * @return
	 */
	public static void sortAccountDetailReportData(List<GlAccountDetail> detailList){
		//计算期间发生数及本年发生数的依据 periodCode
		//合并期初发生数的依据  ccid
		GlAccountDetail initalDetail =detailList.get(0);
		GlAccountDetail lastDetail = null;
		for(int i=0;i<detailList.size();i++){
			GlAccountDetail assist = detailList.get(i);
			String isAdd = assist.getIsAddFlag();
			//==============计算每个会计期的本期发生数和本年累计发生数
			if ((!assist.getAccCode().equals(initalDetail.getAccCode())
					|| !assist.getPeriodCode().equals(initalDetail.getPeriodCode())) && "N".equals(isAdd)) {
				//-------------------添加本月、本年累计数-----------------------------
				GlAccountDetail addDetail = new GlAccountDetail();
				addDetail.setIsAddFlag("Y");
				addDetail.setCcid("");
				addDetail.setPeriodCode(initalDetail.getPeriodCode());
				addDetail.setvHeadId("");
				addDetail.setAccountDate(calcuteAccountDate(initalDetail.getPeriodCode()));
				addDetail.setAccCode(initalDetail.getAccCode());
				addDetail.setAccName(initalDetail.getAccName());
				addDetail.setVoucherNum("");
				addDetail.setSummary("本月合计");
				addDetail.setJF_SL(initalDetail.getBQLJ_JF_SL());
				addDetail.setT_JF_SL(initalDetail.getT_BQLJ_JF_SL());
				addDetail.setJF_JE(initalDetail.getBQLJ_JF());
				addDetail.setT_JF_JE(initalDetail.getT_BQLJ_JF());
				addDetail.setJF_YB(initalDetail.getBQLJ_JF_YB());
				addDetail.setT_JF_YB(initalDetail.getT_BQLJ_JF_YB());
				addDetail.setDF_SL(initalDetail.getBQLJ_DF_SL());
				addDetail.setT_DF_SL(initalDetail.getT_BQLJ_DF_SL());
				addDetail.setDF_JE(initalDetail.getBQLJ_DF());
				addDetail.setT_DF_JE(initalDetail.getT_BQLJ_DF());
				addDetail.setDF_YB(initalDetail.getBQLJ_DF_YB());
				addDetail.setT_DF_YB(initalDetail.getT_BQLJ_DF_YB());
				addDetail.setBalanceDirection(initalDetail.getBalanceDirection());
				addDetail.setQC_SL(initalDetail.getQM_SL());
				addDetail.setT_QC_SL(initalDetail.getT_QM_SL());
				addDetail.setQC_JE(initalDetail.getQM_JE());
				addDetail.setT_QC_JE(initalDetail.getT_QM_JE());
				addDetail.setQC_YB(initalDetail.getQM_YB());
				addDetail.setT_QC_YB(initalDetail.getT_QM_YB());
				detailList.add(i, addDetail);
				GlAccountDetail yearDetail = new GlAccountDetail();
				//------------------------添加本年累计数<期末金额是一致的>-----------
				BeanUtils.copyProperties(addDetail, yearDetail);
				yearDetail.setSummary("本年累计");
				yearDetail.setJF_SL(initalDetail.getBNLJ_JF_SL());
				yearDetail.setT_JF_SL(initalDetail.getT_BNLJ_JF_SL());
				yearDetail.setJF_JE(initalDetail.getBNLJ_JF());
				yearDetail.setT_JF_JE(initalDetail.getT_BNLJ_JF());
				yearDetail.setJF_YB(initalDetail.getBNLJ_JF_YB());
				yearDetail.setT_JF_YB(initalDetail.getT_BNLJ_JF_YB());
				yearDetail.setDF_SL(initalDetail.getBNLJ_DF_SL());
				yearDetail.setT_DF_SL(initalDetail.getT_BNLJ_DF_SL());
				yearDetail.setDF_JE(initalDetail.getBNLJ_DF());
				yearDetail.setT_DF_JE(initalDetail.getT_BNLJ_DF());
				yearDetail.setDF_YB(initalDetail.getBNLJ_DF_YB());
				yearDetail.setT_DF_YB(initalDetail.getT_BNLJ_DF_YB());
				detailList.add(i+1,yearDetail);
				//更改对比标准
				initalDetail = assist;
			}
		}
		lastDetail = detailList.get(detailList.size()-1);
		//处理最后一条记录所对应的  本月累计/本年累计
		if("N".equalsIgnoreCase(lastDetail.getIsAddFlag())){
			//-------------------添加本月、本年累计数-----------------------------
			GlAccountDetail addDetail = new GlAccountDetail();
			addDetail.setIsAddFlag("Y");
			addDetail.setCcid("");
			addDetail.setPeriodCode(initalDetail.getPeriodCode());
			addDetail.setvHeadId("");
			addDetail.setAccountDate(calcuteAccountDate(initalDetail.getPeriodCode()));
			addDetail.setAccCode(initalDetail.getAccCode());
			addDetail.setAccName(initalDetail.getAccName());
			addDetail.setVoucherNum("");
			addDetail.setSummary("本月合计");
			addDetail.setJF_SL(initalDetail.getBQLJ_JF_SL());
			addDetail.setT_JF_SL(initalDetail.getT_BQLJ_JF_SL());
			addDetail.setJF_JE(initalDetail.getBQLJ_JF());
			addDetail.setT_JF_JE(initalDetail.getT_BQLJ_JF());
			addDetail.setJF_YB(initalDetail.getBQLJ_JF_YB());
			addDetail.setT_JF_YB(initalDetail.getT_BQLJ_JF_YB());
			addDetail.setDF_SL(initalDetail.getBQLJ_DF_SL());
			addDetail.setT_DF_SL(initalDetail.getT_BQLJ_DF_SL());
			addDetail.setDF_JE(initalDetail.getBQLJ_DF());
			addDetail.setT_DF_JE(initalDetail.getT_BQLJ_DF());
			addDetail.setDF_YB(initalDetail.getBQLJ_DF_YB());
			addDetail.setT_DF_YB(initalDetail.getT_BQLJ_DF_YB());
			addDetail.setQC_SL(initalDetail.getQM_SL());
			addDetail.setT_QC_SL(initalDetail.getT_QM_SL());
			addDetail.setQC_JE(initalDetail.getQM_JE());
			addDetail.setT_QC_JE(initalDetail.getT_QM_JE());
			addDetail.setQC_YB(initalDetail.getQM_YB());
			addDetail.setT_QC_YB(initalDetail.getT_QM_YB());
			addDetail.setBalanceDirection(initalDetail.getBalanceDirection());
			detailList.add(addDetail);
			GlAccountDetail yearDetail = new GlAccountDetail();
			//------------------------添加本年累计数<期末金额是一致的>-----------
			BeanUtils.copyProperties(addDetail, yearDetail);
			yearDetail.setSummary("本年累计");
			yearDetail.setJF_SL(initalDetail.getBNLJ_JF_SL());
			yearDetail.setT_JF_SL(initalDetail.getT_BNLJ_JF_SL());
			yearDetail.setJF_JE(initalDetail.getBNLJ_JF());
			yearDetail.setT_JF_JE(initalDetail.getT_BNLJ_JF());
			yearDetail.setJF_YB(initalDetail.getBNLJ_JF_YB());
			yearDetail.setT_JF_YB(initalDetail.getT_BNLJ_JF_YB());
			yearDetail.setDF_SL(initalDetail.getBNLJ_DF_SL());
			yearDetail.setT_DF_SL(initalDetail.getT_BNLJ_DF_SL());
			yearDetail.setDF_JE(initalDetail.getBNLJ_DF());
			yearDetail.setT_DF_JE(initalDetail.getT_BNLJ_DF());
			yearDetail.setDF_YB(initalDetail.getBNLJ_DF_YB());
			yearDetail.setT_DF_YB(initalDetail.getT_BNLJ_DF_YB());
			detailList.add(yearDetail);
		}
		//只允许将同一科目组合下只存在一个期初值,accountDate为空即为期初数据
		for(int j=0;j<detailList.size();j++){
			if(j<detailList.size()-1){
				//当前记录
				GlAccountDetail currenctObj = detailList.get(j);
				//取得当前记录的下一条记录
				GlAccountDetail nextAssist = detailList.get(j+1);
				if (currenctObj.getAccCode().equals(nextAssist.getAccCode())
						&& "N".equalsIgnoreCase(nextAssist.getIsAddFlag())
						&& "".equals(nextAssist.getAccountDate())) {
					detailList.remove(nextAssist);
				}
			}
		}
	}
	/**
	 * 
	 * @title: calcuteAccountDate
	 * @description:   计算本期发生以及本年发生的日期
	 * @author  tangxl
	 * @date    2016年4月27日
	 * @param periodCode
	 * @return
	 */
	public static String calcuteAccountDate(String periodCode){
		String lastMonthDay = "";
		String yearCode = periodCode.substring(0, 4);
		String monthCode = periodCode.substring(periodCode.lastIndexOf("-")+1);
		if(monthList1.contains(monthCode)){
			lastMonthDay =periodCode.concat("-31");
		}else if(monthList2.contains(monthCode)){
			lastMonthDay =periodCode.concat("-30");
		}else if("02".equals(monthCode)){
			if(Integer.valueOf(yearCode)%4 ==0){
				lastMonthDay = periodCode.concat("-29");
			}else{
				lastMonthDay = periodCode.concat("-28");
			}	
		}else if("Q1".equals(monthCode)){
			lastMonthDay = yearCode.concat("-03-31");
		}else if("Q2".equals(monthCode)){
			lastMonthDay = yearCode.concat("-06-30");
		}else if("Q3".equals(monthCode)){
			lastMonthDay = yearCode.concat("-09-30");
		}else{
			lastMonthDay = yearCode.concat("-12-31");
		}
		return lastMonthDay;
	}
}
