package com.xzsoft.xc.gl.modal;

import java.util.Date;
import java.util.List;

/**
 * @ClassName: VHead 
 * @Description: 凭证头信息
 * @author linp
 * @date 2016年3月1日 下午1:30:08 
 *
 */
public class VHead {

	private String headId ;			// 凭证头ID
	private String ledgerId ;		// 账簿ID
	private String ledgerCode ;		// 账簿编码
	private String hrcyId ;			// 会计科目体系
	private String periodCode ;		// 会计期
	private String categoryId ;		// 凭证分类
	private String categoryCode ;	// 凭证分类编码
	private String srcCode ;		// 来源编码
	private String srcId ;			// 来源ID
	private String serialNum ;		// 凭证号
	private int attchTotal ;		// 附件数
	private String verifierId ;		// 审核人ID
	private String verifier;		// 审核人
	private Date verifyDate ;		// 审核日期
	private String isSigned ;		// 是否需要签字
	private String signatoryId ;	// 签字人ID
	private String signatory ;		// 签字人
	private Date signDate ;			// 签字日期
	private String bookkeeperId ;	// 记账人ID
	private String bookkeeper ;		// 记账人
	private Date bookDate ;			// 记账日期
	private String isWriteOff ;		// 是否冲销
	private String writeOffNum ;	// 冲销凭证号
	private double totalDR ;			// 借方合计数
	private double totalCR ;			// 贷方合计数
	private String summary ;		// 摘要
	private String templateType ;	// 凭证模板分类
	private String vStatus ;		// 凭证状态
	private String sumFlag ;		// 是否汇总
	private String optype;			// 操作模式
	
	private List<VLine> lines ;		// 凭证分录行
	
	private List<VLine> addLines ;	// 新增分录行
	private List<VLine> updLines ;	// 更新分录行
	private List<String> delLines ;	// 删除分录行
	
	private List<Account> accList ;	// 账簿会计科目信息
	private List<String> accIdList; // 账簿会计科目ID列表信息
	
	private Date creationDate ;     // 制单日期
	private String createdBy ;      // 制单人ID
	private String createdName ;    // 制单人 
	private Date lastUpdateDate ;   // 最后更新日期
	private String lastUpdatedBy ;  // 最后更新人
	private Date synchronizeDate ;
	
	private String tdr ;
	private String tcr ;
	
	public String getTdr() {
		return tdr;
	}
	public void setTdr(String tdr) {
		this.tdr = tdr;
	}
	public String getTcr() {
		return tcr;
	}
	public void setTcr(String tcr) {
		this.tcr = tcr;
	}
	public String getHeadId() {
		return headId;
	}
	public void setHeadId(String headId) {
		this.headId = headId;
	}
	public String getLedgerId() {
		return ledgerId;
	}
	public void setLedgerId(String ledgerId) {
		this.ledgerId = ledgerId;
	}
	public String getLedgerCode() {
		return ledgerCode;
	}
	public void setLedgerCode(String ledgerCode) {
		this.ledgerCode = ledgerCode;
	}
	public String getHrcyId() {
		return hrcyId;
	}
	public void setHrcyId(String hrcyId) {
		this.hrcyId = hrcyId;
	}
	public String getPeriodCode() {
		return periodCode;
	}
	public void setPeriodCode(String periodCode) {
		this.periodCode = periodCode;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	public String getSrcCode() {
		return srcCode;
	}
	public void setSrcCode(String srcCode) {
		this.srcCode = srcCode;
	}
	public String getSrcId() {
		return srcId;
	}
	public void setSrcId(String srcId) {
		this.srcId = srcId;
	}
	public String getSerialNum() {
		return serialNum;
	}
	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}
	public int getAttchTotal() {
		return attchTotal;
	}
	public void setAttchTotal(int attchTotal) {
		this.attchTotal = attchTotal;
	}
	public String getVerifierId() {
		return verifierId;
	}
	public void setVerifierId(String verifierId) {
		this.verifierId = verifierId;
	}
	public String getVerifier() {
		return verifier;
	}
	public void setVerifier(String verifier) {
		this.verifier = verifier;
	}
	public Date getVerifyDate() {
		return verifyDate;
	}
	public void setVerifyDate(Date verifyDate) {
		this.verifyDate = verifyDate;
	}
	public String getIsSigned() {
		return isSigned;
	}
	public void setIsSigned(String isSigned) {
		this.isSigned = isSigned;
	}
	public String getSignatoryId() {
		return signatoryId;
	}
	public void setSignatoryId(String signatoryId) {
		this.signatoryId = signatoryId;
	}
	public String getSignatory() {
		return signatory;
	}
	public void setSignatory(String signatory) {
		this.signatory = signatory;
	}
	public Date getSignDate() {
		return signDate;
	}
	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}
	public String getBookkeeperId() {
		return bookkeeperId;
	}
	public void setBookkeeperId(String bookkeeperId) {
		this.bookkeeperId = bookkeeperId;
	}
	public String getBookkeeper() {
		return bookkeeper;
	}
	public void setBookkeeper(String bookkeeper) {
		this.bookkeeper = bookkeeper;
	}
	public Date getBookDate() {
		return bookDate;
	}
	public void setBookDate(Date bookDate) {
		this.bookDate = bookDate;
	}
	public String getIsWriteOff() {
		return isWriteOff;
	}
	public void setIsWriteOff(String isWriteOff) {
		this.isWriteOff = isWriteOff;
	}
	public String getWriteOffNum() {
		return writeOffNum;
	}
	public void setWriteOffNum(String writeOffNum) {
		this.writeOffNum = writeOffNum;
	}
	public double getTotalDR() {
		return totalDR;
	}
	public void setTotalDR(double totalDR) {
		this.totalDR = totalDR;
	}
	public double getTotalCR() {
		return totalCR;
	}
	public void setTotalCR(double totalCR) {
		this.totalCR = totalCR;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getTemplateType() {
		return templateType;
	}
	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}
	public String getVStatus() {
		return vStatus;
	}
	public void setVStatus(String vStatus) {
		this.vStatus = vStatus;
	}
	public String getSumFlag() {
		return sumFlag;
	}
	public void setSumFlag(String sumFlag) {
		this.sumFlag = sumFlag;
	}
	public String getOptype() {
		return optype;
	}
	public void setOptype(String optype) {
		this.optype = optype;
	}
	public List<VLine> getLines() {
		return lines;
	}
	public void setLines(List<VLine> lines) {
		this.lines = lines;
	}
	public List<VLine> getAddLines() {
		return addLines;
	}
	public void setAddLines(List<VLine> addLines) {
		this.addLines = addLines;
	}
	public List<VLine> getUpdLines() {
		return updLines;
	}
	public void setUpdLines(List<VLine> updLines) {
		this.updLines = updLines;
	}
	public List<String> getDelLines() {
		return delLines;
	}
	public void setDelLines(List<String> delLines) {
		this.delLines = delLines;
	}
	public List<Account> getAccList() {
		return accList;
	}
	public void setAccList(List<Account> accList) {
		this.accList = accList;
	}
	public List<String> getAccIdList() {
		return accIdList;
	}
	public void setAccIdList(List<String> accIdList) {
		this.accIdList = accIdList;
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
	public String getCreatedName() {
		return createdName;
	}
	public void setCreatedName(String createdName) {
		this.createdName = createdName;
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
