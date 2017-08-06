package com.xzsoft.xc.gl.report.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fr.data.AbstractTableData;
import com.fr.data.TableDataException;
import com.fr.report.parameter.Parameter;
import com.xzsoft.xc.gl.report.modal.AssistantDetailReport;
import com.xzsoft.xc.gl.report.service.ReportAccountDetailService;
import com.xzsoft.xip.framework.util.AppContext;
/**
 * @desc 辅助报表统计-AssistantStaticFineDataAction（帆软报表程序数据集）
 * @author tangxl
 * @date 2016年4月14日
 *
 */
public class AssistantDetailFineDataAction extends AbstractTableData {
	private static final long serialVersionUID = -843676763147385446L;
	private String[] columnNames = null;
	private List valueList = null; 
    private enum reportColumn {PERIOD_CODE,V_HEAD_ID,ACCOUNT_DATE,
	    ACC_ID,ACC_CODE,ACC_NAME,VOUCHER_NUM,SUMMARY,
		JF_JE,T_JF_JE,DF_JE,T_DF_JE,BALANCE_DIRECTION,QC_YE,T_QC_YE,
		BQLJ_JF,T_BQLJ_JF,BQLJ_DF,T_BQLJ_DF,
		BNLJ_JF,T_BNLJ_JF,BNLJ_DF,T_BNLJ_DF,
		QM_YE,T_QM_YE,V_TEMPLATE_TYPE};
    public AssistantDetailFineDataAction() {
		this.parameters = new Parameter[]{new Parameter("isFirst"),
				new Parameter("ledgerId"),new Parameter("startPeriod"),new Parameter("endPeriod"),
				new Parameter("startAccCode"),new Parameter("endAccCode"),new Parameter("assistSegment"),
				new Parameter("startSegmentVal"),new Parameter("endSegmentVal"),new Parameter("includeAccount")
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
			//补全结束科目的空档值
			String endAccCode = map.get("endAccCode");
			if(endAccCode!=null && !"0".equals(endAccCode)){
				int charLength = 14-endAccCode.length();
				for(int i=0;i<charLength/2;i++){
					endAccCode = endAccCode+"00";
				}
				map.put("endAccCode", endAccCode);
			}
			try {
				valueList = new ArrayList();
				ReportAccountDetailService reportAccountDetailService = (ReportAccountDetailService) AppContext.getApplicationContext().getBean("reportAccountDetailService");
				 List<AssistantDetailReport> dataList = reportAccountDetailService.getAssistDetailData(map);
				if(dataList!=null && dataList.size()>0){
					for(AssistantDetailReport tmp:dataList){
						Object[] row = new Object[columnNames.length];
						//确定的报表，可以逐列设置值，同样可以遍历数据模型字段，通过反射来设置各列的值
						row[0] =tmp.getPERIOD_CODE();
						row[1] =tmp.getV_HEAD_ID();
						row[2] =tmp.getACCOUNT_DATE();
						row[3] =tmp.getACC_ID();
						row[4] =tmp.getACC_CODE();
						row[5] =tmp.getACC_NAME();
						row[6] =tmp.getVOUCHER_NUM();
						row[7] =tmp.getSUMMARY();
						row[8] =tmp.getJF_JE();
						row[9] =tmp.getT_JF_JE();
						row[10] =tmp.getDF_JE();
						row[11] =tmp.getT_DF_JE();
						row[12] =tmp.getBALANCE_DIRECTION();
						row[13] =tmp.getQC_YE();
						row[14] =tmp.getT_QC_YE();
						row[15] =tmp.getBQLJ_JF();
						row[16] =tmp.getT_BQLJ_JF();
						row[17] =tmp.getBQLJ_DF();
						row[18] =tmp.getT_BQLJ_DF();
						row[19] =tmp.getBNLJ_JF();
						row[20] =tmp.getT_BNLJ_JF();
						row[21] =tmp.getBNLJ_DF();
						row[22] =tmp.getT_BNLJ_DF();
						row[23] =tmp.getQM_YE();
						row[24] =tmp.getT_QM_YE();
						row[25] = tmp.getV_TEMPLATE_TYPE();
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
