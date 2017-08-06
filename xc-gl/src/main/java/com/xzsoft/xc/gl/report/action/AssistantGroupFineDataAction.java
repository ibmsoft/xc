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
 * @desc 辅助报表分组统计-AssistantGroupFineDataAction
 * @author tangxl
 * @date 2016年4月14日
 *
 */
public class AssistantGroupFineDataAction extends AbstractTableData {
	private static final long serialVersionUID = -843676763147385446L;
	private String[] columnNames = null;
	private List valueList = null; 
    private enum reportColumn {
    	ACC_CODE,ACC_NAME,BALANCE_DIRECTION,
		QC_JE, 
		BQJF_JE,
		BQDF_JE,
		BNLJJF_JE,
		BNLJDF_JE,
		QM_JE,
		T_QC_JE,
	    T_BQJF_JE,
		T_BQDF_JE,
		T_BNLJJF_JE,
		T_BNLJDF_JE,
		T_QM_JE,
		VENDOR_CODE,VENDOR_NAME,CUSTOMER_CODE,CUSTOMER_NAME,
		PRODUCT_CODE,PRODUCT_NAME,PROJECT_CODE,PROJECT_NAME,
		ORG_CODE,ORG_NAME,DEPT_CODE,DEPT_NAME,
		EMP_CODE,EMP_NAME,CUSTOM1_CODE,CUSTOM1_NAME,
		CUSTOM2_CODE,CUSTOM2_NAME,CUSTOM3_CODE,CUSTOM3_NAME,
		CUSTOM4_CODE,CUSTOM4_NAME
    };
    public AssistantGroupFineDataAction() {
		this.parameters = new Parameter[]{new Parameter("isFirst"),
				new Parameter("ledgerId"),new Parameter("startPeriod"),new Parameter("endPeriod"),
				new Parameter("startAccCode"),new Parameter("endAccCode"),new Parameter("assistSegment"),
				new Parameter("startSegmentVal"),new Parameter("endSegmentVal"),new Parameter("isLeaf"),
				new Parameter("XC_AP_VENDORS"),new Parameter("XC_AR_CUSTOMERS"),new Parameter("XC_GL_PRODUCTS"),
				new Parameter("XC_PM_PROJECTS"),new Parameter("XIP_PUB_ORGS"),new Parameter("XIP_PUB_DEPTS"),
				new Parameter("XIP_PUB_EMPS"),new Parameter("XC_GL_CUSTOM1"),new Parameter("XC_GL_CUSTOM2"),
				new Parameter("XC_GL_CUSTOM3"),new Parameter("XC_GL_CUSTOM4")
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
			//===============分组辅助统计，对分组参数进行处理======================
			if(map.get("XC_AP_VENDORS")!=null && "Y".equalsIgnoreCase(map.get("XC_AP_VENDORS"))){
				map.put("showVendor", "Y");
			}else{
				map.put("showVendor", "N");
			}
			
			if(map.get("XC_AR_CUSTOMERS")!=null && "Y".equalsIgnoreCase(map.get("XC_AR_CUSTOMERS"))){
				map.put("showCustomer", "Y");
			}else{
				map.put("showCustomer", "N");
			}
			
			if(map.get("XC_GL_PRODUCTS")!=null && "Y".equalsIgnoreCase(map.get("XC_GL_PRODUCTS"))){
				map.put("showProduct", "Y");
			}else{
				map.put("showProduct", "N");
			}
			
			if(map.get("XC_PM_PROJECTS")!=null && "Y".equalsIgnoreCase(map.get("XC_PM_PROJECTS"))){
				map.put("showProject", "Y");
			}else{
				map.put("showProject", "N");
			}
			
			if(map.get("XIP_PUB_ORGS")!=null && "Y".equalsIgnoreCase(map.get("XIP_PUB_ORGS"))){
				map.put("showOrg", "Y");
			}else{
				map.put("showOrg", "N");
			}
			
			if(map.get("XIP_PUB_DEPTS")!=null && "Y".equalsIgnoreCase(map.get("XIP_PUB_DEPTS"))){
				map.put("showDept", "Y");
			}else{
				map.put("showDept", "N");
			}
			
			if(map.get("XIP_PUB_EMPS")!=null && "Y".equalsIgnoreCase(map.get("XIP_PUB_EMPS"))){
				map.put("showEmp", "Y");
			}else{
				map.put("showEmp", "N");
			}
			
			if(map.get("XC_GL_CUSTOM1")!=null && "Y".equalsIgnoreCase(map.get("XC_GL_CUSTOM1"))){
				map.put("showCustom1", "Y");
			}else{
				map.put("showCustom1", "N");
			}
			
			if(map.get("XC_GL_CUSTOM2")!=null && "Y".equalsIgnoreCase(map.get("XC_GL_CUSTOM2"))){
				map.put("showCustom2", "Y");
			}else{
				map.put("showCustom2", "N");
			}
			
			if(map.get("XC_GL_CUSTOM3")!=null && "Y".equalsIgnoreCase(map.get("XC_GL_CUSTOM3"))){
				map.put("showCustom3", "Y");
			}else{
				map.put("showCustom3", "N");
			}
			
			if(map.get("XC_GL_CUSTOM4")!=null && "Y".equalsIgnoreCase(map.get("XC_GL_CUSTOM4"))){
				map.put("showCustom4", "Y");
			}else{
				map.put("showCustom4", "N");
			}
			try {
				valueList = new ArrayList();
				ReportAccountDetailService reportAccountDetailService = (ReportAccountDetailService) AppContext.getApplicationContext().getBean("reportAccountDetailService");
				List<AssistantStaticReport> dataList = reportAccountDetailService.getGroupAssistData(map);
				if(dataList!=null && dataList.size()>0){
					for(AssistantStaticReport tmp:dataList){
						Object[] row = new Object[columnNames.length];
						//-------------设置数据-----------------
						row[0] = tmp.getACC_CODE();
						row[1] = tmp.getACC_NAME();
						row[2] = tmp.getBALANCE_DIRECTION();
						row[3] = tmp.getQC_JE();
						row[4] = tmp.getBQJF_JE();
						row[5] = tmp.getBQDF_JE();
						row[6] = tmp.getBNLJJF_JE();
						row[7] = tmp.getBNLJDF_JE();
						row[8] = tmp.getQM_JE();
						row[9] = tmp.getT_QC_JE();
						row[10] = tmp.getT_BQJF_JE();
						row[11] = tmp.getT_BQDF_JE();
						row[12] = tmp.getT_BNLJJF_JE();
						row[13] = tmp.getT_BNLJDF_JE();
						row[14] = tmp.getT_QM_JE();
						//分组辅助统计根据前段选择的分组条件，动态设置分组内容
						if("Y".equals(map.get("showVendor"))){
							row[15] = tmp.getVENDOR_CODE();
							row[16] = tmp.getVENDOR_NAME();
						}else{
							row[15] = "";
							row[16] = "";
						}
						if("Y".equals(map.get("showCustomer"))){
							row[17] = tmp.getCUSTOMER_CODE();
							row[18] = tmp.getCUSTOMER_NAME();
						}else{
							row[17] = "";
							row[18] = "";
						}
						
						if("Y".equals(map.get("showProduct"))){
							row[19] = tmp.getPRODUCT_CODE();
							row[20] = tmp.getPRODUCT_NAME();
						}else{
							row[19] = "";
							row[20] = "";
						}
						
						if("Y".equals(map.get("showProject"))){
							row[21] = tmp.getPROJECT_CODE();
							row[22] = tmp.getPROJECT_NAME();
						}else{
							row[21] = "";
							row[22] = "";
						}
						
						if("Y".equals(map.get("showOrg"))){
							row[23] = tmp.getORG_CODE();
							row[24] = tmp.getORG_NAME();
						}else{
							row[23] = "";
							row[24] = "";
						}
						
						if("Y".equals(map.get("showDept"))){
							row[25] = tmp.getDEPT_CODE();
							row[26] = tmp.getDEPT_NAME();
						}else{
							row[25] = "";
							row[26] = "";
						}
						
						if("Y".equals(map.get("showEmp"))){
							row[27] = tmp.getEMP_CODE();
							row[28] = tmp.getEMP_NAME();
						}else{
							row[27] = "";
							row[28] = "";
						}
						
						if("Y".equals(map.get("showCustom1"))){
							row[29] = tmp.getCUSTOM1_CODE();
							row[30] = tmp.getCUSTOM1_NAME();
						}else{
							row[29] = "";
							row[30] = "";
						}
						
						if("Y".equals(map.get("showCustom2"))){
							row[31] = tmp.getCUSTOM2_CODE();
							row[32] = tmp.getCUSTOM2_NAME();
						}else{
							row[31] = "";
							row[32] = "";
						}
						
						if("Y".equals(map.get("showCustom3"))){
							row[33] = tmp.getCUSTOM3_CODE();
							row[34] = tmp.getCUSTOM3_NAME();
						}else{
							row[33] = "";
							row[34] = "";
						}
						
						if("Y".equals(map.get("showCustom4"))){
							row[35] = tmp.getCUSTOM4_CODE();
							row[36] = tmp.getCUSTOM4_NAME();
						}else{
							row[35] = "";
							row[36] = "";
						}
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
