package com.xzsoft.xc.gl.modal;

import java.util.Date;

public class Custom {

	private String ledgerId ;
	private String ledgerCode;
	private String segCode ;
	private String segvalId ;
	private String segvalCode ;
	private String segvalName ;
	private String upSegvalId ;
	
	private Date startDate ;
	private Date endDate ;
	private Date synchronizeDate;
	private String isLeaf;
	private String upSegvalCode;
	private int segvalLevel;
	private String segvalDesc;	
	public String getLedgerId() {
		return ledgerId;
	}
	public void setLedgerId(String ledgerId) {
		this.ledgerId = ledgerId;
	}
	public String getSegCode() {
		return segCode;
	}
	public void setSegCode(String segCode) {
		this.segCode = segCode;
	}
	public String getSegvalId() {
		return segvalId;
	}
	public void setSegvalId(String segvalId) {
		this.segvalId = segvalId;
	}
	public String getSegvalCode() {
		return segvalCode;
	}
	public void setSegvalCode(String segvalCode) {
		this.segvalCode = segvalCode;
	}
	public String getSegvalName() {
		return segvalName;
	}
	public void setSegvalName(String segvalName) {
		this.segvalName = segvalName;
	}
	public String getUpSegvalId() {
		return upSegvalId;
	}
	public void setUpSegvalId(String upSegvalId) {
		this.upSegvalId = upSegvalId;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Date getSynchronizeDate() {
		return synchronizeDate;
	}
	public void setSynchronizeDate(Date synchronizeDate) {
		this.synchronizeDate = synchronizeDate;
	}
	public String getIsLeaf() {
		return isLeaf;
	}
	public void setIsLeaf(String isLeaf) {
		this.isLeaf = isLeaf;
	}
	public String getUpSegvalCode() {
		return upSegvalCode;
	}
	public void setUpSegvalCode(String upSegvalCode) {
		this.upSegvalCode = upSegvalCode;
	}
	public int getSegvalLevel() {
		return segvalLevel;
	}
	public void setSegvalLevel(int segvalLevel) {
		this.segvalLevel = segvalLevel;
	}
	public String getSegvalDesc() {
		return segvalDesc;
	}
	public void setSegvalDesc(String segvalDesc) {
		this.segvalDesc = segvalDesc;
	}
	public String getLedgerCode() {
		return ledgerCode;
	}
	public void setLedgerCode(String ledgerCode) {
		this.ledgerCode = ledgerCode;
	}
	
}
