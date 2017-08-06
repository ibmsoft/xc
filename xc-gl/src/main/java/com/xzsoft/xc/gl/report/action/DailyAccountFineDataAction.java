package com.xzsoft.xc.gl.report.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fr.data.AbstractTableData;
import com.fr.data.TableDataException;
import com.fr.report.parameter.Parameter;
import com.xzsoft.xc.gl.report.modal.GlAccountDetail;
import com.xzsoft.xc.gl.report.service.ReportAccountDetailService;
import com.xzsoft.xip.framework.util.AppContext;
/**
 * @fileName DailyAccountFineDataAction
 * @desc 现金银行日记账取数
 * @author tangxl
 * @date 2016年3月23日
 */
@SuppressWarnings("serial")
public class DailyAccountFineDataAction extends AbstractTableData {
	 // 列名数组，保存程序数据集所有列名  
    private String[] columnNames = null;
    private List valueList = null; 
	private enum reportColumn {
		ACCOUNT_DATE,ACC_CODE,ACC_NAME,
		VOUCHER_NUM,SUMMARY,JF_SL,T_JF_SL,
		JF_YB,T_JF_YB,JF_JE,T_JF_JE,
		DF_SL,T_DF_SL,DF_YB,T_DF_YB,
		DF_JE,T_DF_JE,BALANCE_DIRECTION,
		QC_SL,T_QC_SL,QC_YB,T_QC_YB,
		QC_JE,T_QC_JE,V_HEAD_ID,PERIOD_CODE,
		V_TEMPLATE_TYPE
	};
    public DailyAccountFineDataAction() {
		//构造函数初始化报表参数
    	//IS_CASH 为 Y时为现金日记账，否则为银行日记账
		this.parameters = new Parameter[]{new Parameter("isFirst"),
				new Parameter("LEDGER_ID"),new Parameter("START_PERIOD_DATE"),new Parameter("END_PERIOD_DATE"),
				new Parameter("START_ACCOUNT"),new Parameter("END_ACCOUNT"),new Parameter("CURRENCY_CODE"),
				new Parameter("ACC_LEVEL"),new Parameter("IS_CASH"),new Parameter("DEFAULT_CURRENCY"),
				new Parameter("IS_CASHER")
		};
		columnNames = new String[reportColumn.values().length];
		int i=0;
		for(reportColumn column:reportColumn.values()){
			columnNames[i] = column.toString();
			i++;
		}
	}
	@Override
	public int getColumnCount() throws TableDataException {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int columnIndex) throws TableDataException {
		return columnNames[columnIndex];
	}

	@Override
	public int getRowCount() throws TableDataException {
		if("Y".equalsIgnoreCase(this.parameters[0].getValue().toString())){
			return 0;
		}else{
			if(valueList==null)
				fecthReportData();
			return valueList.size();
		}
	}
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if("Y".equalsIgnoreCase(this.parameters[0].getValue().toString())){
			return null;
		}else{
			if(valueList==null)
				fecthReportData();
	        if (columnIndex >=columnNames.length) {  
	            return null;  
	        }  
	        return ((Object[]) valueList.get(rowIndex))[columnIndex];  
		}
		
	}
	/**
	 * 
	 * @title:  fecthReportData
	 * @description:   自定义取数方法
	 * @author  tangxl
	 * @date    2016年4月8日
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void fecthReportData(){
		//获取报表传递的参数
		HashMap<String, String> map = new HashMap<String, String>();
		if(parameters!=null && parameters.length>0){
			for(Parameter t:parameters){
				map.put(t.getName().toString(), t.getValue().toString());
			}
			//非总分类账取数，无科目分组以及科目明细穿透而言
			map.put("ACC_CATEGORY", "");
			map.put("CCID", "");
			String defaultCurrency = map.get("DEFAULT_CURRENCY");
			String currencyCode = map.get("CURRENCY_CODE");
			if(currencyCode == null || "".equals(currencyCode) || defaultCurrency.equalsIgnoreCase(currencyCode)){
				map.put("isStandard", "Y");
			}else{
				map.put("isStandard", "N");
			}
			try {
				valueList = new ArrayList();
				ReportAccountDetailService reportAccountDetailService = (ReportAccountDetailService) AppContext.getApplicationContext().getBean("reportAccountDetailService");
				List<GlAccountDetail> accountDetailList = reportAccountDetailService.getAccountDetailData(map);
				if(accountDetailList!=null && accountDetailList.size()>0){
					for(GlAccountDetail tmp:accountDetailList){
						Object[] row = new Object[columnNames.length];
						//确定的报表，可以逐列设置值，同样可以遍历数据模型字段，通过反射来设置各列的值
						row[0] =tmp.getAccountDate();
						row[1] =tmp.getAccCode();
						row[2] =tmp.getAccName();
						row[3] =tmp.getVoucherNum();
						row[4] =tmp.getSummary();
						row[5] =tmp.getJF_SL();
						row[6] =tmp.getT_JF_SL();
						row[7] =tmp.getJF_YB();
						row[8] =tmp.getT_JF_YB();
						row[9] =tmp.getJF_JE();
						row[10] =tmp.getT_JF_JE();
						row[11] =tmp.getDF_SL();
						row[12] =tmp.getT_DF_SL();
						row[13] =tmp.getDF_YB();
						row[14] =tmp.getT_DF_YB();
						row[15] =tmp.getDF_JE();
						row[16] =tmp.getT_DF_JE();
						row[17] =tmp.getBalanceDirection();
						row[18] =tmp.getQC_SL();
						row[19] =tmp.getT_QC_SL();
						row[20] =tmp.getQC_YB();
						row[21] =tmp.getT_QC_YB();
						row[22] =tmp.getQC_JE();
						row[23] =tmp.getT_QC_JE();
						row[24] =tmp.getvHeadId();
						row[25] = tmp.getPeriodCode();
						row[26] = tmp.getV_TEMPLATE_TYPE();
						valueList.add(row);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				valueList = new ArrayList();
			}
		}
	}
	public void release() throws Exception {  
        super.release();  
        this.valueList = null;  
    } 
}
