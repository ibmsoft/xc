package com.xzsoft.xc.ex.modal;

import java.util.Date;
import java.util.List;

/**
 * @ClassName: DocBean 
 * @Description: 报销单
 * @author linp
 * @date 2016年3月10日 下午5:43:43 
 *
 */
public class EXDocBean {

	private String docId ;			// 单据ID
	private String docCode ;		// 单据号
	private String docCatCode ;		// 单据分类
	private String ledgerId ;		// 账簿
	private String ledgerName;		//账簿名称
	private String orgId ;			// 组织
	private String orgName;			//组织名称
	private Date bizDate ;			// 业务日期
	private String deptId ;			// 成本中心
	private String deptName;		//成本中心名称
	private String projectId ;		// 项目
	private String projectName;		//项目名称
	private String exReason ;		// 申请事由
	private String exItemId ;		// 费用项
	private String exItemName;		//费用项名称
	private String bgItemId ;		// 预算项
	private String bgItemName;		//预算项名称
	private double amount ;			// 单据金额
	private String applyDocId;		// 费用申请单ID
	private String applyDocCode;	// 费用申请单编码
	private double cancelAmt ;		// 核销金额
	
	private String headId ;			// 凭证头ID
	private String insCode ;		// 流程实例编码
	private String auditStatus ;	// 流程状态
	private String auditStatusDesc ;// 流程状态描述
	private Date auditStatusDate ;	// 流程审批通过日期
	private String auditUsers ;		// 当前审批人
	private String vstatus ;		// 凭证状态
	private String finUserId ;		// 复核人（凭证制单人）
	private Date finDate ;			// 复核日期
	private String verifierId ;		// 审核人（凭证提交人）
	private Date verifierDate ;		// 审核日期
	private String signStatus ;		// 签收状态：1-待签收，2-已签收，3-拒签
	private String signUserId ;		// 原始单据签收人
	private Date signDate ;			// 原始单据签收日期
	private String payMethod ;		// 支付方式：1-立即支付，2-挂账支付
	private String exPayId ;		// 支付单ID
	private int attachNum ;			// 附件张数
	private String caId ;			// 现金流量项目
	
	private List<EXDocDtlBean> docDtl ;	//单据明细 
	private List<EXDocDtlBean> updLines ; // 更新明细行
	private List<String> docDtlIds ;  // 待删除明细行
	
	private Date creationDate ;		// 创建日期
	private String createdBy ;		// 申请人
	private String empName;			//申请人名称
	private Date lastUpdateDate ;
	private String lastUpdatedBy ;
	
	private String updateMode ;		// 更新模式：once-立即支付时更新、credit-挂账支付时更新、payment-新建付款单时
	
	private String roleMode ; // 角色模式 ： FIN-财务模式，其他-普通模式
	
	private EXDocBean applyDocBean ;  // 费用申请单
	
	public String getDocId() {
		return docId;
	}
	public void setDocId(String docId) {
		this.docId = docId;
	}
	public String getDocCode() {
		return docCode;
	}
	public void setDocCode(String docCode) {
		this.docCode = docCode;
	}
	public String getDocCatCode() {
		return docCatCode;
	}
	public void setDocCatCode(String docCatCode) {
		this.docCatCode = docCatCode;
	}
	public String getLedgerId() {
		return ledgerId;
	}
	public void setLedgerId(String ledgerId) {
		this.ledgerId = ledgerId;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public Date getBizDate() {
		return bizDate;
	}
	public void setBizDate(Date bizDate) {
		this.bizDate = bizDate;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getExReason() {
		return exReason;
	}
	public void setExReason(String exReason) {
		this.exReason = exReason;
	}
	public String getExItemId() {
		return exItemId;
	}
	public void setExItemId(String exItemId) {
		this.exItemId = exItemId;
	}
	public String getBgItemId() {
		return bgItemId;
	}
	public void setBgItemId(String bgItemId) {
		this.bgItemId = bgItemId;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getApplyDocId() {
		return applyDocId;
	}
	public void setApplyDocId(String applyDocId) {
		this.applyDocId = applyDocId;
	}
	public String getApplyDocCode() {
		return applyDocCode;
	}
	public void setApplyDocCode(String applyDocCode) {
		this.applyDocCode = applyDocCode;
	}
	public double getCancelAmt() {
		return cancelAmt;
	}
	public void setCancelAmt(double cancelAmt) {
		this.cancelAmt = cancelAmt;
	}
	public String getHeadId() {
		return headId;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getExItemName() {
		return exItemName;
	}
	public void setExItemName(String exItemName) {
		this.exItemName = exItemName;
	}
	public String getBgItemName() {
		return bgItemName;
	}
	public void setBgItemName(String bgItemName) {
		this.bgItemName = bgItemName;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public void setHeadId(String headId) {
		this.headId = headId;
	}
	public String getInsCode() {
		return insCode;
	}
	public void setInsCode(String insCode) {
		this.insCode = insCode;
	}
	public String getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}
	public String getAuditStatusDesc() {
		return auditStatusDesc;
	}
	public void setAuditStatusDesc(String auditStatusDesc) {
		this.auditStatusDesc = auditStatusDesc;
	}
	public Date getAuditStatusDate() {
		return auditStatusDate;
	}
	public void setAuditStatusDate(Date auditStatusDate) {
		this.auditStatusDate = auditStatusDate;
	}
	public String getAuditUsers() {
		return auditUsers;
	}
	public void setAuditUsers(String auditUsers) {
		this.auditUsers = auditUsers;
	}
	public String getVstatus() {
		return vstatus;
	}
	public void setVstatus(String vstatus) {
		this.vstatus = vstatus;
	}
	public String getFinUserId() {
		return finUserId;
	}
	public void setFinUserId(String finUserId) {
		this.finUserId = finUserId;
	}
	public Date getFinDate() {
		return finDate;
	}
	public void setFinDate(Date finDate) {
		this.finDate = finDate;
	}
	public String getVerifierId() {
		return verifierId;
	}
	public void setVerifierId(String verifierId) {
		this.verifierId = verifierId;
	}
	public Date getVerifierDate() {
		return verifierDate;
	}
	public void setVerifierDate(Date verifierDate) {
		this.verifierDate = verifierDate;
	}
	public String getSignStatus() {
		return signStatus;
	}
	public void setSignStatus(String signStatus) {
		this.signStatus = signStatus;
	}
	public String getSignUserId() {
		return signUserId;
	}
	public void setSignUserId(String signUserId) {
		this.signUserId = signUserId;
	}
	public Date getSignDate() {
		return signDate;
	}
	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}
	public String getPayMethod() {
		return payMethod;
	}
	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}
	public String getExPayId() {
		return exPayId;
	}
	public void setExPayId(String exPayId) {
		this.exPayId = exPayId;
	}
	public int getAttachNum() {
		return attachNum;
	}
	public void setAttachNum(int attachNum) {
		this.attachNum = attachNum;
	}
	public String getCaId() {
		return caId;
	}
	public void setCaId(String caId) {
		this.caId = caId;
	}
	public List<EXDocDtlBean> getDocDtl() {
		return docDtl;
	}
	public void setDocDtl(List<EXDocDtlBean> docDtl) {
		this.docDtl = docDtl;
	}
	public List<EXDocDtlBean> getUpdLines() {
		return updLines;
	}
	public void setUpdLines(List<EXDocDtlBean> updLines) {
		this.updLines = updLines;
	}
	public List<String> getDocDtlIds() {
		return docDtlIds;
	}
	public void setDocDtlIds(List<String> docDtlIds) {
		this.docDtlIds = docDtlIds;
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
	public String getUpdateMode() {
		return updateMode;
	}
	public void setUpdateMode(String updateMode) {
		this.updateMode = updateMode;
	}
	public String getLedgerName() {
		return ledgerName;
	}
	public void setLedgerName(String ledgerName) {
		this.ledgerName = ledgerName;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getRoleMode() {
		return roleMode;
	}
	public void setRoleMode(String roleMode) {
		this.roleMode = roleMode;
	}
	public EXDocBean getApplyDocBean() {
		return applyDocBean;
	}
	public void setApplyDocBean(EXDocBean applyDocBean) {
		this.applyDocBean = applyDocBean;
	}
}
