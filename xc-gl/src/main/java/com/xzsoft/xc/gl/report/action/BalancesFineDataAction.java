package com.xzsoft.xc.gl.report.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fr.data.AbstractTableData;
import com.fr.data.TableDataException;
import com.fr.report.parameter.Parameter;
import com.xzsoft.xc.gl.report.modal.AccountStaticReport;
import com.xzsoft.xc.gl.report.service.ReportAccountDetailService;
import com.xzsoft.xip.framework.util.AppContext;

/**
 * @fileName BalancesFineDataAction
 * @description   余额表取数管理，余额所取列数与总分类账的列取数是一致的，复用总账数据存数bean类 <AccountStaticReport>
 * @author tangxl
 * @date 2016年3月23日
 */
@SuppressWarnings("serial")
public class BalancesFineDataAction extends AbstractTableData {
	//枚举出当前报表所需要的列
	 private enum REPOTT_COLUMN_NAME {
		CCID,ACC_CODE,ACC_NAME,PERIOD_CODE,CURRENCY_CODE,BALANCE_DIRECTION,
		QC_SL,QCYB_JE,QC_JE, BQJF_SL,BQJF_YB,BQJF_JE,BQDF_SL,BQDF_YB,BQDF_JE,
		BNLJJF_SL,BNLJJF_YB,BNLJJF_JE,BNLJDF_SL,BNLJDF_YB,BNLJDF_JE,
		QM_SL,QMYB_JE,QM_JE,T_QC_SL,T_QCYB_JE,T_QC_JE,T_BQJF_SL,T_BQJF_YB,T_BQJF_JE,
		T_BQDF_SL,T_BQDF_YB,T_BQDF_JE,T_BNLJJF_SL,T_BNLJJF_YB,T_BNLJJF_JE,
		T_BNLJDF_SL,T_BNLJDF_YB,T_BNLJDF_JE,T_QM_SL,T_QMYB_JE,T_QM_JE,judgeDirection
	};
    // 列名数组，保存程序数据集所有列名  
    private String[] columnNames = new String[REPOTT_COLUMN_NAME.values().length];
    private List valueList = null;  
	public  BalancesFineDataAction() {
		//构造函数，初始化FineReport数据集列字段等信息,获取报表传递的参数信息
		this.parameters = new Parameter[]{new Parameter("isFirst"),
				new Parameter("ledgerId"),new Parameter("startPeriodCode"),new Parameter("endPeriodCode"),
				new Parameter("accGroupId"),new Parameter("summaryLevel"),new Parameter("startAccCode"),
				new Parameter("endAccCode"),new Parameter("currencyCode"),new Parameter("defaultCurrency")
				
		};
		int i=0;
		for(REPOTT_COLUMN_NAME column:REPOTT_COLUMN_NAME.values()){
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
	        if (columnIndex >= REPOTT_COLUMN_NAME.values().length) {  
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
	@SuppressWarnings("unchecked")
	public void fecthReportData(){
		//获取报表传递的参数
		HashMap<String, String> map = new HashMap<String, String>();
		if(parameters!=null && parameters.length>0){
			for(Parameter t:parameters){
				map.put(t.getName().toString(), t.getValue().toString());
			}
			String currencyCode = map.get("currencyCode");
			String defaultCurrency = map.get("defaultCurrency");
			if(currencyCode == null || "".equals(currencyCode) || currencyCode.equalsIgnoreCase(defaultCurrency)){
				map.put("isStandard", "Y");
			}else{
				map.put("isStandard", "N");
			}
			//与总账共用相同的取数接口，通过标志位区分
			map.put("isBalance", "Y"); 
			try {
				valueList = new ArrayList();
				ReportAccountDetailService reportAccountDetailService = (ReportAccountDetailService) AppContext.getApplicationContext().getBean("reportAccountDetailService");
				List<AccountStaticReport> accountStaticList = reportAccountDetailService.getAccountantData(map);
				//添加数据集的行
				if(accountStaticList!=null && accountStaticList.size()>0){
					for(AccountStaticReport tmp:accountStaticList){
						//生成行对象
						Object[] row = new Object[REPOTT_COLUMN_NAME.values().length];
						row[0] = "";
						row[1] = tmp.getACC_CODE();
						row[2] = tmp.getACC_NAME();
						row[3] = tmp.getPERIOD_CODE();
						row[4] = tmp.getCURRENCY_CODE();
						row[5] = tmp.getBALANCE_DIRECTION();
						row[6] = tmp.getQC_SL();
						row[7] = tmp.getQCYB_JE();
						row[8] = tmp.getQC_JE();
						row[9] = tmp.getBQJF_SL();
						row[10] = tmp.getBQJF_YB();
						row[11] = tmp.getBQJF_JE();
						row[12] = tmp.getBQDF_SL();
						row[13] = tmp.getBQDF_YB();
						row[14] = tmp.getBQDF_JE();
						row[15] = tmp.getBNLJJF_SL();
						row[16] = tmp.getBNLJJF_YB();
						row[17] = tmp.getBNLJJF_JE();
						row[18] = tmp.getBNLJDF_SL();
						row[19] = tmp.getBNLJDF_YB();
						row[20] = tmp.getBNLJDF_JE();
						row[21] = tmp.getQM_SL();
						row[22] = tmp.getQMYB_JE();
						row[23] = tmp.getQM_JE();
						row[24] = tmp.getT_QC_SL();
						row[25] = tmp.getT_QCYB_JE();
						row[26] = tmp.getT_QC_JE();
						row[27] = tmp.getT_BQJF_SL();
						row[28] = tmp.getT_BQJF_YB();
						row[29] = tmp.getT_BQJF_JE();
						row[30] = tmp.getT_BQDF_SL();
						row[31] = tmp.getT_BQDF_YB();
						row[32] = tmp.getT_BQDF_JE();
						row[33] = tmp.getT_BNLJJF_SL();
						row[34] = tmp.getT_BNLJJF_YB();
						row[35] = tmp.getT_BNLJJF_JE();
						row[36] = tmp.getT_BNLJDF_SL();
						row[37] = tmp.getT_BNLJDF_YB();
						row[38] = tmp.getT_BNLJDF_JE();
						row[39] = tmp.getT_QM_SL();
						row[40] = tmp.getT_QMYB_JE();
						row[41] = tmp.getT_QM_JE();
						row[42] = tmp.getJudgeDirection();
						valueList.add(row);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				valueList = new ArrayList();
			}
		}
	}
	/**
	 *帆软默认资源释放函数？
	 */
	public void release() throws Exception {  
        super.release();  
        this.valueList = null;  
    }  
}
