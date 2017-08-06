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
public class VHeadDTO {

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
	private String isSigned ;		// 是否需要签字
	private String isWriteOff ;		// 是否冲销
	private String writeOffNum ;	// 冲销凭证号
	private double totalDR ;			// 借方合计数
	private double totalCR ;			// 贷方合计数
	private String summary ;		// 摘要
	private String templateType ;	// 凭证模板分类
	private String vStatus ;		// 凭证状态
	private String sumFlag ;		// 是否汇总
	
	private List<VLineDTO> lines ;
	
	private Date creationDate ;     // 制单日期
	private String createdBy ;      // 制单人ID
	private String createdName ;    // 制单人 
	
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
	public String getIsSigned() {
		return isSigned;
	}
	public void setIsSigned(String isSigned) {
		this.isSigned = isSigned;
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
	public List<VLineDTO> getLines() {
		return lines;
	}
	public void setLines(List<VLineDTO> lines) {
		this.lines = lines;
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
}
