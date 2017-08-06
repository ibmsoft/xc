package com.xzsoft.xc.util.modal;

import java.util.Date;
import java.util.List;

/**
 * @ClassName: Rule 
 * @Description: 规则信息
 * @author linp
 * @date 2016年1月1日 下午10:31:05 
 *
 */
public class Rule {

	private String ruleCode ;
	private String ruleDesc ;
	private String fixedPrefix ;
	private int serialLength ;
	private String serialCategory ;
	
	private Date creationDate ;
	private String createdBy ;
	private Date lastUpdateDate ;
	private String lastUpdatedBy ;
	
	private List<RuleDtl> ruleDtls ;
	private RuleMaxNum ruleMaxNum ;
	
	public String getRuleCode() {
		return ruleCode;
	}
	public void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode;
	}
	public String getRuleDesc() {
		return ruleDesc;
	}
	public void setRuleDesc(String ruleDesc) {
		this.ruleDesc = ruleDesc;
	}
	public String getFixedPrefix() {
		return fixedPrefix;
	}
	public void setFixedPrefix(String fixedPrefix) {
		this.fixedPrefix = fixedPrefix;
	}
	public int getSerialLength() {
		return serialLength;
	}
	public void setSerialLength(int serialLength) {
		this.serialLength = serialLength;
	}
	public String getSerialCategory() {
		return serialCategory;
	}
	public void setSerialCategory(String serialCategory) {
		this.serialCategory = serialCategory;
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
	public List<RuleDtl> getRuleDtls() {
		return ruleDtls;
	}
	public void setRuleDtls(List<RuleDtl> ruleDtls) {
		this.ruleDtls = ruleDtls;
	}
	public RuleMaxNum getRuleMaxNum() {
		return ruleMaxNum;
	}
	public void setRuleMaxNum(RuleMaxNum ruleMaxNum) {
		this.ruleMaxNum = ruleMaxNum;
	}
	
}
