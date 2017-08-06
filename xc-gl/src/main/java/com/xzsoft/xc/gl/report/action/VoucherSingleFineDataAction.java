package com.xzsoft.xc.gl.report.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fr.data.AbstractTableData;
import com.fr.data.TableDataException;
import com.fr.report.parameter.Parameter;
import com.xzsoft.xc.gl.report.modal.VDataReport;
import com.xzsoft.xc.gl.report.service.ReportAccountDetailService;
import com.xzsoft.xip.framework.util.AppContext;
/**
 * @fileName VoucherSingleFineDataAction
 * @desc A4凭证取数
 * @author tangxl
 * @date 2016年3月23日
 */
@SuppressWarnings("serial")
public class VoucherSingleFineDataAction extends AbstractTableData {
	 // 列名数组，保存程序数据集所有列名  
    private String[] columnNames = null;
    private List valueList = null; 
	private enum reportColumn {V_HEAD_ID,LEDGER_NAME,ORG_NAME,V_ATTCH_TOTAL,V_SERIAL_NUM,CREATION_DATE,
		SUMMARY,CCID_NAME,ACCOUNT_DR,ACCOUNT_CR,CREATED_NAME,
		VERIFIER,SIGNATORY,BOOKKEEPER,AMOUNT,T_ACCOUNT_DR,T_ACCOUNT_CR,T_AMOUNT,ENTER_AMOUNT,T_ENTER_AMOUNT,EXCHANGE_RATE,
		FOREIGN_NAME,DIMENSION_NAME,V_CATEGORY_NAME
	//sumDebit,sumCrebit,sumTDebit,sumTCrebit,isShow	
	};
    public VoucherSingleFineDataAction() {
		//构造函数初始化报表参数
    	//IS_CASH 为 Y时为现金日记账，否则为银行日记账
		this.parameters = new Parameter[]{new Parameter("isFirst"),
				new Parameter("ledgerId"),new Parameter("startPeriodCode"),new Parameter("endPeriodCode"),
				new Parameter("headerIds"),new Parameter("headerCatogery"),new Parameter("accCode"),
				new Parameter("startAmount"),new Parameter("endAmount"),new Parameter("headerStatus"),
				new Parameter("headerMaker"),new Parameter("headerSummary")
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
			//单张A4凭证取数
			map.put("isSingle", "Y");
			try {
				valueList = new ArrayList();
				ReportAccountDetailService reportAccountDetailService = (ReportAccountDetailService) AppContext.getApplicationContext().getBean("reportAccountDetailService");
				List<VDataReport>  vList = reportAccountDetailService.getVDataReport(map);
				//添加数据集的行
				if(vList!=null && vList.size()>0){
					for(VDataReport tmp:vList){
						Object[] row = new Object[columnNames.length];
						row[0] = tmp.getV_HEAD_ID();
						row[1] = tmp.getLEDGER_NAME(); 
						row[2] = tmp.getORG_NAME(); 	
						row[3] = tmp.getV_ATTCH_TOTAL();
						row[4] = tmp.getV_SERIAL_NUM(); 
						row[5] = tmp.getCREATION_DATE(); 
						row[6] = tmp.getSUMMARY(); 
						row[7] = tmp.getCCID_NAME();
						row[8] = tmp.getACCOUNT_DR(); 
						row[9] = tmp.getACCOUNT_CR();
						row[10] =tmp.getCREATED_NAME(); 
						row[11] =tmp.getVERIFIER(); 
						row[12] =tmp.getSIGNATORY(); 
						row[13] =tmp.getBOOKKEEPER();
						row[14] =tmp.getAMOUNT(); 
						row[15] =tmp.getT_ACCOUNT_DR();
						row[16] =tmp.getT_ACCOUNT_CR();
						row[17] =tmp.getT_AMOUNT();
						if("1".equals(tmp.getBALANCE_DIRECTION())){
							row[18] =tmp.getENTER_DR();
							row[19] =tmp.getT_ENTER_DR();
						}else{
							row[18] =tmp.getENTER_CR();
							row[19] =tmp.getT_ENTER_CR();
						}
						row[20] = tmp.getEXCHANGE_RATE();
						row[21] = tmp.getFOREIGN_NAME();
						row[22] = tmp.getDIMENSION_NAME();
						row[23] = tmp.getV_CATEGORY_NAME();
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
