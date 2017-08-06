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
public class JournalLedgerDataAction extends AbstractTableData{
	
	private static final long serialVersionUID = 2071834071205938076L;
	//枚举出当前报表所需要的列
	 private enum REPOTT_COLUMN_NAME {
		periodCode,serialNumber,orderNumber,accountCode,accountName,
		ccidName,currencyName,voucherSummary,accountDR,accountCR,
		enterDR,enterCR,dimensionName,amount,headId,amountOriginal,
		vendorName,customerName,orgName,deptName,empName,productName,
		projectName,custom1Name,custom2Name,custom3Name,custom4Name
		};
    // 列名数组，保存程序数据集所有列名  
    private String[] columnNames = new String[REPOTT_COLUMN_NAME.values().length];
    private List<Object[]> valueList = null;  
	public  JournalLedgerDataAction() {
		//构造函数，初始化FineReport数据集列字段等信息,获取报表传递的参数信息
		this.parameters = new Parameter[]{new Parameter("isFirst"),
				new Parameter("ledgerId"),new Parameter("startPeriodCode"),new Parameter("endPeriodCode"),
				new Parameter("isAccount")
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
	 * @methodName  fecthReportData
	 * @author      tangxl
	 * @date        2016年9月23日
	 * @describe    序时账取数函数
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
			try {
				valueList = new ArrayList<Object[]>();
				ReportAccountDetailService reportAccountDetailService = (ReportAccountDetailService) AppContext.getApplicationContext().getBean("reportAccountDetailService");
				List<JournalLedgerReport> journalLedgerList = reportAccountDetailService.getJournalLedgerData(map);
				//添加数据集的行
				if(journalLedgerList!=null && journalLedgerList.size()>0){
					for(JournalLedgerReport tmp:journalLedgerList){
						//生成行对象
						Object[] row = new Object[REPOTT_COLUMN_NAME.values().length];
						row[0] = tmp.getPeriodCode();
						row[1] = tmp.getSerialNumber();
						row[2] = tmp.getOrderNumber();
						row[3] = tmp.getAccountCode();
						row[4] = tmp.getAccountName();
						row[5] = tmp.getCcidName();
						row[6] = tmp.getCurrencyName();
						row[7] = tmp.getVoucherSummary();
						row[8] = tmp.getAccountDR();
						row[9] = tmp.getAccountCR();
						row[10] = tmp.getEnterDR();
						row[11] = tmp.getEnterCR();
						row[12] = tmp.getDimensionName();
						row[13] = tmp.getAmount();
						row[14] = tmp.getHeadId();
						row[15] = tmp.getAmountOriginal();
						row[16] = tmp.getVendorName().trim();
						row[17] = tmp.getCustomerName().trim();
						row[18] = tmp.getOrgName().trim();
						row[19] = tmp.getDeptName().trim();
						row[20] = tmp.getEmpName().trim();
						row[21] = tmp.getProductName().trim();
						row[22] = tmp.getProjectName().trim();
						row[23] = tmp.getCustom1Name().trim();
						row[24] = tmp.getCustom2Name().trim();
						row[25] = tmp.getCustom3Name().trim();
						row[26] = tmp.getCustom4Name().trim();
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
