package com.xzsoft.xc.rep.modal;

import java.util.Date;

/**
 * @ClassName: RepCurrencyBean 
 * @Description: 报表币种信息
 * @author linp
 * @date 2016年8月8日 下午2:02:04 
 *
 */
public class RepCurrencyBean {
	private String cnyCode ;
	private String cnyName ;
	private String nation ;
	private int isEnabled ;
	private int pericison ;			// 默认小数点位数

	private String createdBy ;
	private Date creationDate ;
	private String lastUpdatedBy ;
	private Date lastUpdateDate ;
	
	public String getCnyCode() {
		return cnyCode;
	}
	public void setCnyCode(String cnyCode) {
		this.cnyCode = cnyCode;
	}
	public String getCnyName() {
		return cnyName;
	}
	public void setCnyName(String cnyName) {
		this.cnyName = cnyName;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public int getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(int isEnabled) {
		this.isEnabled = isEnabled;
	}
	public int getPericison() {
		return pericison;
	}
	public void setPericison(int pericison) {
		this.pericison = pericison;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	
}
