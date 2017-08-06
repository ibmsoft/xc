/**
 * 
 */
package com.xzsoft.xc.gl.report.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fr.data.AbstractTableData;
import com.fr.data.TableDataException;
import com.fr.report.parameter.Parameter;
import com.xzsoft.xc.gl.report.modal.JournalLedgerReport;
import com.xzsoft.xc.gl.report.service.ReportAccountDetailService;
import com.xzsoft.xip.framework.util.AppContext;

/**
 * @author tangxl
 *
 */
public class JournalLedgerTitleAction extends AbstractTableData{
	private static final long serialVersionUID = 5014771017644538418L;
	 private enum REPOTT_COLUMN_NAME {
		vendorTitle,customerTitle,orgTitle,deptTitle,empTitle,productTitle,
		projectTitle,custom1Title,custom2Title,custom3Title,custom4Title
		};
    private String[] columnNames = new String[REPOTT_COLUMN_NAME.values().length];
    private List<Object[]> valueList = null;  
	public  JournalLedgerTitleAction() {
		this.parameters = new Parameter[]{new Parameter("ledgerId"),new Parameter("isFirst")};
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
		if(valueList==null)
			fecthReportData();
		return valueList.size();
	}
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(valueList==null)
			fecthReportData();
        if (columnIndex >= REPOTT_COLUMN_NAME.values().length) {  
            return null;  
        }  
        return ((Object[]) valueList.get(rowIndex))[columnIndex];  
	}
	/**
	 * 
	 * @methodName  fecthReportData
	 * @author      tangxl
	 * @date        2016年9月23日
	 * @describe    序时账列标签
	 * @变动说明       
	 * @version     1.0
	 */
	public void fecthReportData(){
		//获取报表传递的参数
		HashMap<String, String> map = new HashMap<String, String>();
		if(parameters!=null && parameters.length>0){
			for(Parameter t:parameters){
				map.put(t.getName().toString(), t.getValue().toString());
			}
			map.put("fetchTitle","Y");
			if("Y".equals(map.get("isFirst"))){
				map.put("ledgerId", "-1");
			}
			try {
				valueList = new ArrayList<Object[]>();
				ReportAccountDetailService reportAccountDetailService = (ReportAccountDetailService) AppContext.getApplicationContext().getBean("reportAccountDetailService");
				List<JournalLedgerReport> journalLedgerList = reportAccountDetailService.getJournalLedgerData(map);
				if(journalLedgerList!=null && journalLedgerList.size()>0){
					for(JournalLedgerReport tmp:journalLedgerList){
						Object[] row = new Object[REPOTT_COLUMN_NAME.values().length];
						row[0] = tmp.getVendorName().trim();
						row[1] = tmp.getCustomerName().trim();
						row[2] = tmp.getOrgName().trim();
						row[3] = tmp.getDeptName().trim();
						row[4] = tmp.getEmpName().trim();
						row[5] = tmp.getProductName().trim();
						row[6] = tmp.getProjectName().trim();
						row[7] = tmp.getCustom1Name().trim();
						row[8] = tmp.getCustom2Name().trim();
						row[9] = tmp.getCustom3Name().trim();
						row[10] = tmp.getCustom4Name().trim();
						valueList.add(row);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				valueList = new ArrayList<Object[]>();
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
