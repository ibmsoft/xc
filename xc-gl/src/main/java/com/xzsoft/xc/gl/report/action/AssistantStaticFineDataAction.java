package com.xzsoft.xc.gl.report.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fr.data.AbstractTableData;
import com.fr.data.TableDataException;
import com.fr.report.parameter.Parameter;
import com.xzsoft.xc.gl.report.modal.AssistantStaticReport;
import com.xzsoft.xc.gl.report.service.ReportAccountDetailService;
import com.xzsoft.xip.framework.util.AppContext;
/**
 * @desc 辅助报表统计-AssistantStaticFineDataAction（帆软报表程序数据集）
 * @author tangxl
 * @date 2016年4月14日
 *
 */
public class AssistantStaticFineDataAction extends AbstractTableData {
	private static final long serialVersionUID = -843676763147385446L;
	private String[] columnNames = null;
	private List valueList = null; 
    private enum reportColumn {ACC_CODE,ACC_NAME,BALANCE_DIRECTION,
		QC_SL,QCYB_JE,QC_JE, 
		BQJF_SL,BQJF_YB,BQJF_JE,
		BQDF_SL,BQDF_YB,BQDF_JE,
		BNLJJF_SL,BNLJJF_YB,BNLJJF_JE,
		BNLJDF_SL,BNLJDF_YB,BNLJDF_JE,
		QM_SL,QMYB_JE,QM_JE,
		T_QC_SL,T_QCYB_JE,T_QC_JE,
		T_BQJF_SL,T_BQJF_YB,T_BQJF_JE,
		T_BQDF_SL,T_BQDF_YB,T_BQDF_JE,
		T_BNLJJF_SL,T_BNLJJF_YB,T_BNLJJF_JE,
		T_BNLJDF_SL,T_BNLJDF_YB,T_BNLJDF_JE,
		T_QM_SL,T_QMYB_JE,T_QM_JE
		};
    public AssistantStaticFineDataAction() {
		this.parameters = new Parameter[]{new Parameter("isFirst"),
				new Parameter("ledgerId"),new Parameter("startPeriod"),new Parameter("endPeriod"),
				new Parameter("startAccCode"),new Parameter("endAccCode"),new Parameter("assistSegment"),
				new Parameter("startSegmentVal"),new Parameter("endSegmentVal"),new Parameter("isLeaf")
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
				List<AssistantStaticReport> dataList = reportAccountDetailService.getAssistStaticData(map);
				if(dataList!=null && dataList.size()>0){
					for(AssistantStaticReport tmp:dataList){
						Object[] row = new Object[columnNames.length];
						//确定的报表，可以逐列设置值，同样可以遍历数据模型字段，通过反射来设置各列的值
						row[0] =tmp.getACC_CODE();
						row[1] =tmp.getACC_NAME();
						row[2] =tmp.getBALANCE_DIRECTION();
						row[3] =tmp.getQC_SL();
						row[4] =tmp.getQCYB_JE();
						row[5] =tmp.getQC_JE();
						row[6] =tmp.getBQJF_SL();
						row[7] =tmp.getBQJF_YB();
						row[8] =tmp.getBQJF_JE();
						row[9] =tmp.getBQDF_SL();
						row[10] =tmp.getBQDF_YB();
						row[11] =tmp.getBQDF_JE();
						row[12] =tmp.getBNLJJF_SL();
						row[13] =tmp.getBNLJJF_YB();
						row[14] =tmp.getBNLJJF_JE();
						row[15] =tmp.getBNLJDF_SL();
						row[16] =tmp.getBNLJDF_YB();
						row[17] =tmp.getBNLJDF_JE();
						row[18] =tmp.getQM_SL();
						row[19] =tmp.getQMYB_JE();
						row[20] =tmp.getQM_JE();
						row[21] =tmp.getT_QC_SL();
						row[22] =tmp.getT_QCYB_JE();
						row[23] =tmp.getT_QC_JE();
						row[24] =tmp.getT_BQJF_SL();
						row[25] = tmp.getT_BQJF_YB();
						row[26] =tmp.getT_BQJF_JE();
						row[27] =tmp.getT_BQDF_SL();
						row[28] =tmp.getT_BQDF_YB();
						row[29] =tmp.getT_BQDF_JE();
						row[30] =tmp.getT_BNLJJF_SL();
						row[31] =tmp.getT_BNLJJF_YB();
						row[32] =tmp.getT_BNLJJF_JE();
						row[33] =tmp.getT_BNLJDF_SL();
						row[34] =tmp.getT_BNLJDF_YB();
						row[35] =tmp.getT_BNLJDF_JE();
						row[36] =tmp.getT_QM_SL();
						row[37] =tmp.getT_QMYB_JE();
						row[38] = tmp.getT_QM_JE();
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
