package com.xzsoft.xc.gl.report.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fr.data.AbstractTableData;
import com.fr.data.TableDataException;
import com.fr.report.parameter.Parameter;
import com.xzsoft.xc.gl.report.modal.CashFlowReport;
import com.xzsoft.xc.gl.report.service.ReportAccountDetailService;
import com.xzsoft.xip.framework.util.AppContext;


/**
 * @fileName CashFlowFineDataAction
 * @desc   现金流量统计及明细数据集
 * @author tangxl
 * @date 2016年3月23日
 */
@SuppressWarnings("serial")
public class CashFlowFineDataAction extends AbstractTableData {
	 // 列名数组，保存程序数据集所有列名  
    private String[] columnNames = null;
    private List valueList = null;  
	private enum staticColumn {
		CA_CODE,CA_NAME,CA_DIRECTION,CA_DIRECTION_NAME,CA_SUM,T_CA_SUM
	};
	private enum detailColumn {
		GROUP_FLAG,V_HEAD_ID,LEDGER_ID,PERIOD_CODE,CREATION_DATE,V_SERIAL_NUM,CA_CODE,CA_NAME,SUMMARY,CA_DIRECTION,CA_DIRECTION_NAME,CA_SUM,T_CA_SUM,V_TEMPLATE_TYPE
	};
    public  CashFlowFineDataAction() {
		//构造函数初始化报表参数
		this.parameters = new Parameter[]{new Parameter("isFirst"),
				new Parameter("LEDGER_ID"),new Parameter("PERIOD_CODE_START"),new Parameter("PERIOD_CODE_END"),
				new Parameter("CA_CODE_START"),new Parameter("CA_CODE_END"),new Parameter("IS_ACCOUNT"),new Parameter("IS_STATIC"),
				new Parameter("includeAccount")
		};
	}
	@Override
	public int getColumnCount() throws TableDataException {
		if(columnNames == null)
			initColumns();
		return columnNames.length;
	}

	@Override
	public String getColumnName(int columnIndex) throws TableDataException {
		if(columnNames == null)
			initColumns();
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
	@SuppressWarnings("unchecked")
	public void fecthReportData(){
		//获取报表传递的参数
		HashMap<String, String> map = new HashMap<String, String>();
		if(parameters!=null && parameters.length>0){
			for(Parameter t:parameters){
				map.put(t.getName().toString(), t.getValue().toString());
			}
			try {
				valueList = new ArrayList();
				ReportAccountDetailService reportAccountDetailService = (ReportAccountDetailService) AppContext.getApplicationContext().getBean("reportAccountDetailService");
				List<CashFlowReport> cashFlowList = reportAccountDetailService.getCashFlowReportData(map);
				if(cashFlowList!=null && cashFlowList.size()>0){
					if(map.get("IS_STATIC").equalsIgnoreCase("Y")){
						for(CashFlowReport tmp:cashFlowList){
							Object[] row = new Object[columnNames.length];
							//确定的报表，可以逐列设置值，同样可以遍历数据模型字段，通过反射来设置各列的值
							row[0] = tmp.getCA_CODE();
							row[1] = tmp.getCA_NAME();
							row[2] = tmp.getCA_DIRECTION();
							row[3] = tmp.getCA_DIRECTION_NAME();
							row[4] = tmp.getCA_SUM();
							row[5] = tmp.getT_CA_SUM();
							valueList.add(row);
						}
					}else{
						for(CashFlowReport tmp:cashFlowList){
							Object[] row = new Object[columnNames.length];
							//"GROUP_FLAG","V_HEAD_ID","LEDGER_ID","PERIOD_CODE","CREATION_DATE","V_SERIAL_NUM",
							// "CA_CODE","CA_NAME","SUMMARY","CA_DIRECTION","CA_DIRECTION_NAME","CA_SUM","T_CA_SUM"
							//确定的报表，可以逐列设置值，同样可以遍历数据模型字段，通过反射来设置各列的值
							row[0] =  tmp.getGROUP_FLAG();
							row[1] =  tmp.getV_HEAD_ID();
							row[2] =  tmp.getLEDGER_ID();
							row[3] =  tmp.getPERIOD_CODE();
							row[4] =  tmp.getCREATION_DATE();
							row[5] =  tmp.getV_SERIAL_NUM();
							row[6] =  tmp.getCA_CODE();
							row[7] =  tmp.getCA_NAME();
							row[8] =  tmp.getSUMMARY();
							row[9] =  tmp.getCA_DIRECTION();
							row[10] = tmp.getCA_DIRECTION_NAME();
							row[11] = tmp.getCA_SUM();
							row[12] = tmp.getT_CA_SUM();
							row[13] = tmp.getV_TEMPLATE_TYPE();
							valueList.add(row);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				valueList = new ArrayList();
			}
		}
	}
	/**
	 *初始化列 
	 */
	public void initColumns(){
		String isStatic = this.parameters[7].getValue().toString();
		if("Y".equalsIgnoreCase(isStatic)){
			columnNames = new String[staticColumn.values().length];
			int i=0;
			for(staticColumn column:staticColumn.values()){
				columnNames[i] = column.toString();
				i++;
			}
		}else{
			columnNames = new String[detailColumn.values().length];
			int i=0;
			for(detailColumn column:detailColumn.values()){
				columnNames[i] = column.toString();
				i++;
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
