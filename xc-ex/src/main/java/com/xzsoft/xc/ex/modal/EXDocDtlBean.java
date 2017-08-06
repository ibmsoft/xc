package com.xzsoft.xc.ex.modal;

import java.util.Date;

/**
 * @ClassName: DocDtlBean 
 * @Description: 报销单明细表
 * @author linp
 * @date 2016年3月10日 下午5:43:55 
 *
 */
public class EXDocDtlBean {
    private String dtlId ;		// 单据明细ID
	private String docId ;		// 单据ID
	private String exItemId ;	// 费用项目ID
	private String exItemCode ;
	private String exItemName ;
	private String bgItemId ;	// 预算项目ID
	private String bgItemName ; // 预算项目
	private double dtlAmt ;		// 金额
	private String dtlDesc ;	// 单据描述
	private int docNum ;		// 单据数量
	private Date startDate ;	// 出发日期
	private String startPos ;	// 出发地点
	private Date endDate ;		// 结束日期		
	private String endPos ;		// 到达地点
    
	private Date creationDate ;
	private String createdBy ;
	private Date lastUpdateDate ;
	private String lastUpdatedBy ;
	
	
	public String getDtlId() {
		return dtlId;
	}
	public void setDtlId(String dtlId) {
		this.dtlId = dtlId;
	}
	public String getDocId() {
		return docId;
	}
	public void setDocId(String docId) {
		this.docId = docId;
	}
	public String getExItemId() {
		return exItemId;
	}
	public void setExItemId(String exItemId) {
		this.exItemId = exItemId;
	}
	public String getExItemCode() {
		return exItemCode;
	}
	public void setExItemCode(String exItemCode) {
		this.exItemCode = exItemCode;
	}
	public String getExItemName() {
		return exItemName;
	}
	public void setExItemName(String exItemName) {
		this.exItemName = exItemName;
	}
	public String getBgItemId() {
		return bgItemId;
	}
	public void setBgItemId(String bgItemId) {
		this.bgItemId = bgItemId;
	}
	public String getBgItemName() {
		return bgItemName;
	}
	public void setBgItemName(String bgItemName) {
		this.bgItemName = bgItemName;
	}
	public double getDtlAmt() {
		return dtlAmt;
	}
	public void setDtlAmt(double dtlAmt) {
		this.dtlAmt = dtlAmt;
	}
	public String getDtlDesc() {
		return dtlDesc;
	}
	public void setDtlDesc(String dtlDesc) {
		this.dtlDesc = dtlDesc;
	}
	public int getDocNum() {
		return docNum;
	}
	public void setDocNum(int docNum) {
		this.docNum = docNum;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public String getStartPos() {
		return startPos;
	}
	public void setStartPos(String startPos) {
		this.startPos = startPos;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getEndPos() {
		return endPos;
	}
	public void setEndPos(String endPos) {
		this.endPos = endPos;
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
}
