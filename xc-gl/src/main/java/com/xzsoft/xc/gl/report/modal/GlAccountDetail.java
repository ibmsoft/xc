package com.xzsoft.xc.gl.report.modal;
/**
 * @desc   总账明细账数据模型
 * @author tangxl
 * @date 2016年3月23日
 */
public class GlAccountDetail { 
	private String V_TEMPLATE_TYPE; //凭证类型
	private String groupFlag;
	private String periodCode;
	private String vHeadId;            
	private String ccid;         
	private String accountDate;  
	private String accCode;           
	private String accName;               
	private String vendorCode;               
	private String vendorName;            
	private String customerCode;            
	private String customerName;          
	private String projectCode;          
	private String projectName;           
	private String productCode;           
	private String productName;           
	private String orgCode;           
	private String orgName;               
	private String deptCode;               
	private String deptName;              
	private String empCode;              
	private String empName;               
	private String custom1Code;               
	private String custom1Name;           
	private String custom2Code;               
	private String custom2Name;          
	private String custom3Code;               
	private String custom3Name;             
	private String custom4Code;               
	private String custom4Name;  
	private String voucherNum;
	private String summary;            
	private double JF_SL; 
	private double T_JF_SL;
	private double JF_YB; 
	private double T_JF_YB;   
	private double JF_JE;
	private double T_JF_JE;     
	private double DF_SL; 
	private double T_DF_SL;         
	private double DF_YB; 
	private double T_DF_YB;
	private double DF_JE;
	private double T_DF_JE;        
	private String balanceDirection;    
	private String balanceDirectionName;
	private double QC_SL;
	private double T_QC_SL;
	private double QC_YB;  
	private double T_QC_YB;
	private double QC_JE;                         
	private double T_QC_JE;
	//=======本期发生数||本年发生数    数量、金额、原币 + 未过账 数量、金额、原币
	private double BQLJ_JF_SL;
	private double BQLJ_DF_SL;
	private double T_BQLJ_JF_SL;
	private double T_BQLJ_DF_SL;
	private double BQLJ_JF;
	private double BQLJ_DF;
	private double T_BQLJ_JF;
	private double T_BQLJ_DF;
	private double BQLJ_JF_YB;
	private double BQLJ_DF_YB;
	private double T_BQLJ_JF_YB;
	private double T_BQLJ_DF_YB;
	private double BNLJ_JF_SL;
	private double BNLJ_DF_SL;
	private double T_BNLJ_JF_SL;
	private double T_BNLJ_DF_SL;
	private double BNLJ_JF;
	private double BNLJ_DF;
	private double T_BNLJ_JF;
	private double T_BNLJ_DF;
	private double BNLJ_JF_YB;
	private double BNLJ_DF_YB;
	private double T_BNLJ_JF_YB;
	private double T_BNLJ_DF_YB;
	private double QM_SL;
	private double T_QM_SL;
	private double QM_JE;
	private double T_QM_JE;
	private double QM_YB;
	private double T_QM_YB;
	private String isAddFlag;  
	public String getGroupFlag() {
		return groupFlag;
	}
	public void setGroupFlag(String groupFlag) {
		this.groupFlag = groupFlag;
	}
	public String getPeriodCode() {
		return periodCode;
	}
	public void setPeriodCode(String periodCode) {
		this.periodCode = periodCode;
	}
	public String getvHeadId() {
		return vHeadId;
	}
	public void setvHeadId(String vHeadId) {
		this.vHeadId = vHeadId;
	}
	public String getCcid() {
		return ccid;
	}
	public void setCcid(String ccid) {
		this.ccid = ccid;
	}
	public String getAccountDate() {
		return accountDate;
	}
	public void setAccountDate(String accountDate) {
		this.accountDate = accountDate;
	}
	public String getAccCode() {
		return accCode;
	}
	public void setAccCode(String accCode) {
		this.accCode = accCode;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public String getVendorCode() {
		return vendorCode;
	}
	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public String getCustomerCode() {
		return customerCode;
	}
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getProjectCode() {
		return projectCode;
	}
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getEmpCode() {
		return empCode;
	}
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getCustom1Code() {
		return custom1Code;
	}
	public void setCustom1Code(String custom1Code) {
		this.custom1Code = custom1Code;
	}
	public String getCustom1Name() {
		return custom1Name;
	}
	public void setCustom1Name(String custom1Name) {
		this.custom1Name = custom1Name;
	}
	public String getCustom2Code() {
		return custom2Code;
	}
	public void setCustom2Code(String custom2Code) {
		this.custom2Code = custom2Code;
	}
	public String getCustom2Name() {
		return custom2Name;
	}
	public void setCustom2Name(String custom2Name) {
		this.custom2Name = custom2Name;
	}
	public String getCustom3Code() {
		return custom3Code;
	}
	public void setCustom3Code(String custom3Code) {
		this.custom3Code = custom3Code;
	}
	public String getCustom3Name() {
		return custom3Name;
	}
	public void setCustom3Name(String custom3Name) {
		this.custom3Name = custom3Name;
	}
	public String getCustom4Code() {
		return custom4Code;
	}
	public void setCustom4Code(String custom4Code) {
		this.custom4Code = custom4Code;
	}
	public String getCustom4Name() {
		return custom4Name;
	}
	public void setCustom4Name(String custom4Name) {
		this.custom4Name = custom4Name;
	}
	public String getVoucherNum() {
		return voucherNum;
	}
	public void setVoucherNum(String voucherNum) {
		this.voucherNum = voucherNum;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public double getJF_SL() {
		return JF_SL;
	}
	public void setJF_SL(double jF_SL) {
		JF_SL = jF_SL;
	}
	public double getT_JF_SL() {
		return T_JF_SL;
	}
	public void setT_JF_SL(double t_JF_SL) {
		T_JF_SL = t_JF_SL;
	}
	public double getJF_YB() {
		return JF_YB;
	}
	public void setJF_YB(double jF_YB) {
		JF_YB = jF_YB;
	}
	public double getT_JF_YB() {
		return T_JF_YB;
	}
	public void setT_JF_YB(double t_JF_YB) {
		T_JF_YB = t_JF_YB;
	}
	public double getJF_JE() {
		return JF_JE;
	}
	public void setJF_JE(double jF_JE) {
		JF_JE = jF_JE;
	}
	public double getT_JF_JE() {
		return T_JF_JE;
	}
	public void setT_JF_JE(double t_JF_JE) {
		T_JF_JE = t_JF_JE;
	}
	public double getDF_SL() {
		return DF_SL;
	}
	public void setDF_SL(double dF_SL) {
		DF_SL = dF_SL;
	}
	public double getT_DF_SL() {
		return T_DF_SL;
	}
	public void setT_DF_SL(double t_DF_SL) {
		T_DF_SL = t_DF_SL;
	}
	public double getDF_YB() {
		return DF_YB;
	}
	public void setDF_YB(double dF_YB) {
		DF_YB = dF_YB;
	}
	public double getT_DF_YB() {
		return T_DF_YB;
	}
	public void setT_DF_YB(double t_DF_YB) {
		T_DF_YB = t_DF_YB;
	}
	public double getDF_JE() {
		return DF_JE;
	}
	public void setDF_JE(double dF_JE) {
		DF_JE = dF_JE;
	}
	public double getT_DF_JE() {
		return T_DF_JE;
	}
	public void setT_DF_JE(double t_DF_JE) {
		T_DF_JE = t_DF_JE;
	}
	public String getBalanceDirection() {
		return balanceDirection;
	}
	public void setBalanceDirection(String balanceDirection) {
		this.balanceDirection = balanceDirection;
	}
	public String getBalanceDirectionName() {
		return balanceDirectionName;
	}
	public void setBalanceDirectionName(String balanceDirectionName) {
		this.balanceDirectionName = balanceDirectionName;
	}
	public double getQC_SL() {
		return QC_SL;
	}
	public void setQC_SL(double qC_SL) {
		QC_SL = qC_SL;
	}
	public double getT_QC_SL() {
		return T_QC_SL;
	}
	public void setT_QC_SL(double t_QC_SL) {
		T_QC_SL = t_QC_SL;
	}
	public double getQC_YB() {
		return QC_YB;
	}
	public void setQC_YB(double qC_YB) {
		QC_YB = qC_YB;
	}
	public double getT_QC_YB() {
		return T_QC_YB;
	}
	public void setT_QC_YB(double t_QC_YB) {
		T_QC_YB = t_QC_YB;
	}
	public double getQC_JE() {
		return QC_JE;
	}
	public void setQC_JE(double qC_JE) {
		QC_JE = qC_JE;
	}
	public double getT_QC_JE() {
		return T_QC_JE;
	}
	public void setT_QC_JE(double t_QC_JE) {
		T_QC_JE = t_QC_JE;
	}
	public double getBQLJ_JF_SL() {
		return BQLJ_JF_SL;
	}
	public void setBQLJ_JF_SL(double bQLJ_JF_SL) {
		BQLJ_JF_SL = bQLJ_JF_SL;
	}
	public double getBQLJ_DF_SL() {
		return BQLJ_DF_SL;
	}
	public void setBQLJ_DF_SL(double bQLJ_DF_SL) {
		BQLJ_DF_SL = bQLJ_DF_SL;
	}
	public double getT_BQLJ_JF_SL() {
		return T_BQLJ_JF_SL;
	}
	public void setT_BQLJ_JF_SL(double t_BQLJ_JF_SL) {
		T_BQLJ_JF_SL = t_BQLJ_JF_SL;
	}
	public double getT_BQLJ_DF_SL() {
		return T_BQLJ_DF_SL;
	}
	public void setT_BQLJ_DF_SL(double t_BQLJ_DF_SL) {
		T_BQLJ_DF_SL = t_BQLJ_DF_SL;
	}
	public double getBQLJ_JF() {
		return BQLJ_JF;
	}
	public void setBQLJ_JF(double bQLJ_JF) {
		BQLJ_JF = bQLJ_JF;
	}
	public double getBQLJ_DF() {
		return BQLJ_DF;
	}
	public void setBQLJ_DF(double bQLJ_DF) {
		BQLJ_DF = bQLJ_DF;
	}
	public double getT_BQLJ_JF() {
		return T_BQLJ_JF;
	}
	public void setT_BQLJ_JF(double t_BQLJ_JF) {
		T_BQLJ_JF = t_BQLJ_JF;
	}
	public double getT_BQLJ_DF() {
		return T_BQLJ_DF;
	}
	public void setT_BQLJ_DF(double t_BQLJ_DF) {
		T_BQLJ_DF = t_BQLJ_DF;
	}
	public double getBQLJ_JF_YB() {
		return BQLJ_JF_YB;
	}
	public void setBQLJ_JF_YB(double bQLJ_JF_YB) {
		BQLJ_JF_YB = bQLJ_JF_YB;
	}
	public double getBQLJ_DF_YB() {
		return BQLJ_DF_YB;
	}
	public void setBQLJ_DF_YB(double bQLJ_DF_YB) {
		BQLJ_DF_YB = bQLJ_DF_YB;
	}
	public double getT_BQLJ_JF_YB() {
		return T_BQLJ_JF_YB;
	}
	public void setT_BQLJ_JF_YB(double t_BQLJ_JF_YB) {
		T_BQLJ_JF_YB = t_BQLJ_JF_YB;
	}
	public double getT_BQLJ_DF_YB() {
		return T_BQLJ_DF_YB;
	}
	public void setT_BQLJ_DF_YB(double t_BQLJ_DF_YB) {
		T_BQLJ_DF_YB = t_BQLJ_DF_YB;
	}
	public double getBNLJ_JF_SL() {
		return BNLJ_JF_SL;
	}
	public void setBNLJ_JF_SL(double bNLJ_JF_SL) {
		BNLJ_JF_SL = bNLJ_JF_SL;
	}
	public double getBNLJ_DF_SL() {
		return BNLJ_DF_SL;
	}
	public void setBNLJ_DF_SL(double bNLJ_DF_SL) {
		BNLJ_DF_SL = bNLJ_DF_SL;
	}
	public double getT_BNLJ_JF_SL() {
		return T_BNLJ_JF_SL;
	}
	public void setT_BNLJ_JF_SL(double t_BNLJ_JF_SL) {
		T_BNLJ_JF_SL = t_BNLJ_JF_SL;
	}
	public double getT_BNLJ_DF_SL() {
		return T_BNLJ_DF_SL;
	}
	public void setT_BNLJ_DF_SL(double t_BNLJ_DF_SL) {
		T_BNLJ_DF_SL = t_BNLJ_DF_SL;
	}
	public double getBNLJ_JF() {
		return BNLJ_JF;
	}
	public void setBNLJ_JF(double bNLJ_JF) {
		BNLJ_JF = bNLJ_JF;
	}
	public double getBNLJ_DF() {
		return BNLJ_DF;
	}
	public void setBNLJ_DF(double bNLJ_DF) {
		BNLJ_DF = bNLJ_DF;
	}
	public double getT_BNLJ_JF() {
		return T_BNLJ_JF;
	}
	public void setT_BNLJ_JF(double t_BNLJ_JF) {
		T_BNLJ_JF = t_BNLJ_JF;
	}
	public double getT_BNLJ_DF() {
		return T_BNLJ_DF;
	}
	public void setT_BNLJ_DF(double t_BNLJ_DF) {
		T_BNLJ_DF = t_BNLJ_DF;
	}
	public double getBNLJ_JF_YB() {
		return BNLJ_JF_YB;
	}
	public void setBNLJ_JF_YB(double bNLJ_JF_YB) {
		BNLJ_JF_YB = bNLJ_JF_YB;
	}
	public double getBNLJ_DF_YB() {
		return BNLJ_DF_YB;
	}
	public void setBNLJ_DF_YB(double bNLJ_DF_YB) {
		BNLJ_DF_YB = bNLJ_DF_YB;
	}
	public double getT_BNLJ_JF_YB() {
		return T_BNLJ_JF_YB;
	}
	public void setT_BNLJ_JF_YB(double t_BNLJ_JF_YB) {
		T_BNLJ_JF_YB = t_BNLJ_JF_YB;
	}
	public double getT_BNLJ_DF_YB() {
		return T_BNLJ_DF_YB;
	}
	public void setT_BNLJ_DF_YB(double t_BNLJ_DF_YB) {
		T_BNLJ_DF_YB = t_BNLJ_DF_YB;
	}
	public String getIsAddFlag() {
		return isAddFlag;
	}
	public void setIsAddFlag(String isAddFlag) {
		this.isAddFlag = isAddFlag;
	}
	public double getQM_SL() {
		return QM_SL;
	}
	public void setQM_SL(double qM_SL) {
		QM_SL = qM_SL;
	}
	public double getT_QM_SL() {
		return T_QM_SL;
	}
	public void setT_QM_SL(double t_QM_SL) {
		T_QM_SL = t_QM_SL;
	}
	public double getQM_JE() {
		return QM_JE;
	}
	public void setQM_JE(double qM_JE) {
		QM_JE = qM_JE;
	}
	public double getT_QM_JE() {
		return T_QM_JE;
	}
	public void setT_QM_JE(double t_QM_JE) {
		T_QM_JE = t_QM_JE;
	}
	public double getQM_YB() {
		return QM_YB;
	}
	public void setQM_YB(double qM_YB) {
		QM_YB = qM_YB;
	}
	public double getT_QM_YB() {
		return T_QM_YB;
	}
	public void setT_QM_YB(double t_QM_YB) {
		T_QM_YB = t_QM_YB;
	}
	public String getV_TEMPLATE_TYPE() {
		return V_TEMPLATE_TYPE;
	}
	public void setV_TEMPLATE_TYPE(String v_TEMPLATE_TYPE) {
		V_TEMPLATE_TYPE = v_TEMPLATE_TYPE;
	}
	
}
