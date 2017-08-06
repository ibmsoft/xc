package com.xzsoft.xc.gl.report.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xzsoft.xc.gl.report.dao.ReportAccountDetailDao;
import com.xzsoft.xc.gl.report.modal.AccountStaticReport;
import com.xzsoft.xc.gl.report.modal.AssistantDetailReport;
import com.xzsoft.xc.gl.report.modal.AssistantStaticReport;
import com.xzsoft.xc.gl.report.modal.CashFlowReport;
import com.xzsoft.xc.gl.report.modal.GlAccountDetail;
import com.xzsoft.xc.gl.report.modal.JournalLedgerReport;
import com.xzsoft.xc.gl.report.modal.VDataReport;
import com.xzsoft.xc.gl.report.service.ReportAccountDetailService;
import com.xzsoft.xc.gl.util.LedgerPeriodUtil;
import com.xzsoft.xc.gl.util.VoucherA4PrintUtil;
@Service("reportAccountDetailService")
public class ReportAccountDetailServiceImp implements
		ReportAccountDetailService {
	@Resource
	private ReportAccountDetailDao reportAccountDetailDao;
	@Override
	public List<GlAccountDetail> getAccountDetailData(
			HashMap<String, String> map) throws Exception {
		List<GlAccountDetail> accountList = reportAccountDetailDao.getAccountDetailData(map);
		/*明细账余额累加
		 *根据accCode、periodCode区分明细账的余额，累加accCode、periodCode相同的QC_SL、T_QC_SL、QC_YB、T_QC_YB、QC_JE、T_QC_JE
		 *只累加明细行的上述数字，不累加期初值 
		 */
		if(accountList!=null && accountList.size()>0){
			for(int i=0;i<accountList.size();i++){
				GlAccountDetail tmp = accountList.get(i);
				if(i<accountList.size()-1){
					GlAccountDetail nextTmp = accountList.get(i+1);
					if(nextTmp.getAccountDate()!=null && !"".equals(nextTmp.getAccountDate())){
						nextTmp.setQC_SL(nextTmp.getQC_SL()+tmp.getQC_SL());
						nextTmp.setT_QC_SL(nextTmp.getT_QC_SL()+tmp.getT_QC_SL());
						nextTmp.setQC_YB(nextTmp.getQC_YB()+tmp.getQC_YB());
						nextTmp.setT_QC_YB(nextTmp.getT_QC_YB()+tmp.getT_QC_YB());
						nextTmp.setQC_JE(nextTmp.getQC_JE()+tmp.getQC_JE());
						nextTmp.setT_QC_JE(nextTmp.getT_QC_JE()+tmp.getT_QC_JE());
					}
				}
			}
			//追加 本月合计 和本年累计 数据
			LedgerPeriodUtil.sortAccountDetailReportData(accountList);
		}
		return accountList;
	}
	@Override
	public List<CashFlowReport> getCashFlowReportData(
			HashMap<String, String> map) throws Exception {
		return reportAccountDetailDao.getCashFlowReportData(map);
	}
	@Override
	public List<AccountStaticReport> getAccountantData(
			HashMap<String, String> map) throws Exception {
		List<AccountStaticReport> accountStaticList = reportAccountDetailDao.getAccountantData(map);
		String periorCcid = "";
        if(!map.containsKey("isBalance")){
    		if(accountStaticList!=null && accountStaticList.size()>0){
    			for(int i=0;i<accountStaticList.size();i++){
    				periorCcid = accountStaticList.get(i).getACC_CODE();
    				if(i>0 && periorCcid.equalsIgnoreCase(accountStaticList.get(i-1).getACC_CODE())){
    					accountStaticList.get(i).setSHOW_PERIOD("N");
    				}
    			}
    		}
        }
		return accountStaticList;
	}
	@Override
	public List<AssistantStaticReport> getAssistStaticData(
			HashMap<String, String> map) throws Exception {
		return reportAccountDetailDao.getAssistStaticData(map);
	}
	@Override
	public List<AssistantStaticReport> getGroupAssistData(
			HashMap<String, String> map) throws Exception {
		return reportAccountDetailDao.getGroupAssistData(map);
	}
	@Override
	public List<VDataReport> getVDataReport(HashMap<String, String> map) throws Exception {
		if(map.containsKey("isSingle")){
			List<VDataReport> singleList = reportAccountDetailDao.getVDataReport(map);
			if(singleList!=null && singleList.size()>0){
				VoucherA4PrintUtil.remedyVoucherData(singleList);
			}
			return singleList;
		}else{
		return reportAccountDetailDao.getVDataReport(map);
		}
	}
	/**
	 * 
	 * @title:  getAssistDetailData
	 * @description:
	 * <p>
	 *   获取辅助统计明细信息：
	 *   1：格式为：期初+明细信息<因为为明细行，因而只查询末级科目数据>
	 *   2：同一科目只存在一行期初记录
	 *   3：每一期间需计算当前期间的本期发生数 以及 本年发生数
	 * </p>
	 * @author  tangxl
	 * @date    2016年4月4日
	 * @param map
	 * @return
	 */
	@Override
	public List<AssistantDetailReport> getAssistDetailData(
			HashMap<String, String> map) throws Exception {
		List<AssistantDetailReport> detailList= reportAccountDetailDao.getAssistDetailData(map);
		//累加处理每一明细项的余额值
		if(detailList!=null && detailList.size()>0){
			for(int i=0;i<detailList.size();i++){
				AssistantDetailReport tmp = detailList.get(i);
				if(i<detailList.size()-1){
					AssistantDetailReport nextTmp = detailList.get(i+1);
					//科目相同，并且除了第一行记录期初数据以外的数据记录的余额做累加
					if(nextTmp.getACC_CODE().equals(tmp.getACC_CODE()) && nextTmp.getACCOUNT_DATE()!=null && !"".equals(nextTmp.getACCOUNT_DATE())){
						nextTmp.setQC_YE(nextTmp.getQC_YE()+tmp.getQC_YE());
						nextTmp.setT_QC_YE(nextTmp.getT_QC_YE()+tmp.getT_QC_YE());
					}
				}
			}
	 //计算本期本年累计发生数，合并期初数
		LedgerPeriodUtil.sortAssistDetailReportData(detailList);
		}
		return detailList;
	}
	/* (non-Javadoc)
	 * @see com.xzsoft.xc.gl.report.service.ReportAccountDetailService#printA4Report(java.util.HashMap)
	 */
	@Override
	public List<VDataReport> printA4Report(HashMap<String, String> map)
			throws Exception {
		List<VDataReport> voucherList =reportAccountDetailDao.getVDataReport(map);
		if (voucherList != null && voucherList.size() > 0) {
			String[] headerIds = map.get("headerIds").split(",");
			return VoucherA4PrintUtil.formatA4Print(voucherList,headerIds);
		}else{
			return voucherList;
		}
	}
	/* (non-Javadoc)
	 * @name     getJournalLedgerData
	 * @author   tangxl
	 * @date     2016年9月23日
	 * @注释                   TODO
	 * @version  1.0
	 * @see com.xzsoft.xc.gl.report.service.ReportAccountDetailService#getJournalLedgerData(java.util.HashMap)
	 */
	@Override
	public List<JournalLedgerReport> getJournalLedgerData(
			HashMap<String, String> map) throws Exception {
		return reportAccountDetailDao.getJournalLedgerData(map);
	}
}
