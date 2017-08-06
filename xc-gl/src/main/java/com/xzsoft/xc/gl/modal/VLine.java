package com.xzsoft.xc.gl.modal;

import java.util.Date;

/**
 * @ClassName: VLine 
 * @Description: 凭证分录行信息
 * @author linp
 * @date 2016年3月1日 下午1:29:54 
 *
 */
public class VLine {
	
	private String lineId ;			// 分录行ID
	private String headId ;  		// 凭证头ID
	private String summary ;  		// 摘要
	private String ccid ;			// 科目组合ID
	
	private String accId ;			// 科目ID
	private String accCode ;		// 科目编码
	private String vendorCode ;		// 供应商编码
	private String customerCode ;	// 客户编码 
	private String prodCode ;		// 产品编码
	private String orgCode ; 		// 内部往来编码
	private String empCode ;		// 个人往来编码	
	private String deptCode ;		// 成本中心编码
	private String prjCode ;		// 项目编码
	private String cust1Code ;		// 自定义1编码
	private String cust2Code ;		// 自定义2编码
	private String cust3Code ;		// 自定义3编码
	private String cust4Code ;		// 自定义4编码
	
	private double accountDR ;		// 折后借方金额
	private double accountCR ;		// 折后贷方金额
	private String currencyCode ;	// 币种
	private float exchangeRate ;	// 汇率
	private double enterDR ;		// 原币借方金额
	private double enterCR ;		// 原币贷方金额
	private double amount ;			// 数量
	private String dimId ;			// 计量单位ID
	private int orderBy ;			// 分录排序号
	private String caId ;			// 现金流量项目ID
	private String caCode ;			// 现金流量项目编码
	private String srcDtlId ;		// 来源明细ID
	private String bgItemId ;		// 预算项目ID
	private String projectId ;		// PA项目ID
	private String deptId ;			// 成功中心ID
	
	private String adr ;
	private String acr ;
	private String amt ;
	private String edr ;
	private String ecr ;
	
	private Date creationDate ;
	private String createdBy ;
	private Date lastUpdateDate ;
	private String lastUpdatedBy ;
	private Date synchronizeDate ;
	
	
	public String getBgItemId() {
		return bgItemId;
	}
	public void setBgItemId(String bgItemId) {
		this.bgItemId = bgItemId;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getAdr() {
		return adr;
	}
	public void setAdr(String adr) {
		this.adr = adr;
	}
	public String getAcr() {
		return acr;
	}
	public void setAcr(String acr) {
		this.acr = acr;
	}
	public String getAmt() {
		return amt;
	}
	public void setAmt(String amt) {
		this.amt = amt;
	}
	public String getEdr() {
		return edr;
	}
	public void setEdr(String edr) {
		this.edr = edr;
	}
	public String getEcr() {
		return ecr;
	}
	public void setEcr(String ecr) {
		this.ecr = ecr;
	}
	public String getLineId() {
		return lineId;
	}
	public void setLineId(String lineId) {
		this.lineId = lineId;
	}
	public String getHeadId() {
		return headId;
	}
	public void setHeadId(String headId) {
		this.headId = headId;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getCcid() {
		return ccid;
	}
	public void setCcid(String ccid) {
		this.ccid = ccid;
	}
	public String getAccId() {
		return accId;
	}
	public void setAccId(String accId) {
		this.accId = accId;
	}
	public String getAccCode() {
		return accCode;
	}
	public void setAccCode(String accCode) {
		this.accCode = accCode;
	}
	public String getVendorCode() {
		return vendorCode;
	}
	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}
	public String getCustomerCode() {
		return customerCode;
	}
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
	public String getProdCode() {
		return prodCode;
	}
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getEmpCode() {
		return empCode;
	}
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getPrjCode() {
		return prjCode;
	}
	public void setPrjCode(String prjCode) {
		this.prjCode = prjCode;
	}
	public String getCust1Code() {
		return cust1Code;
	}
	public void setCust1Code(String cust1Code) {
		this.cust1Code = cust1Code;
	}
	public String getCust2Code() {
		return cust2Code;
	}
	public void setCust2Code(String cust2Code) {
		this.cust2Code = cust2Code;
	}
	public String getCust3Code() {
		return cust3Code;
	}
	public void setCust3Code(String cust3Code) {
		this.cust3Code = cust3Code;
	}
	public String getCust4Code() {
		return cust4Code;
	}
	public void setCust4Code(String cust4Code) {
		this.cust4Code = cust4Code;
	}
	public double getAccountDR() {
		return accountDR;
	}
	public void setAccountDR(double accountDR) {
		this.accountDR = accountDR;
	}
	public double getAccountCR() {
		return accountCR;
	}
	public void setAccountCR(double accountCR) {
		this.accountCR = accountCR;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public float getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(float exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	public double getEnterDR() {
		return enterDR;
	}
	public void setEnterDR(double enterDR) {
		this.enterDR = enterDR;
	}
	public double getEnterCR() {
		return enterCR;
	}
	public void setEnterCR(double enterCR) {
		this.enterCR = enterCR;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getDimId() {
		return dimId;
	}
	public void setDimId(String dimId) {
		this.dimId = dimId;
	}
	public int getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(int orderBy) {
		this.orderBy = orderBy;
	}
	public String getCaId() {
		return caId;
	}
	public void setCaId(String caId) {
		this.caId = caId;
	}
	public String getCaCode() {
		return caCode;
	}
	public void setCaCode(String caCode) {
		this.caCode = caCode;
	}
	public String getSrcDtlId() {
		return srcDtlId;
	}
	public void setSrcDtlId(String srcDtlId) {
		this.srcDtlId = srcDtlId;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public Date getSynchronizeDate() {
		return synchronizeDate;
	}
	public void setSynchronizeDate(Date synchronizeDate) {
		this.synchronizeDate = synchronizeDate;
	}
	
}
