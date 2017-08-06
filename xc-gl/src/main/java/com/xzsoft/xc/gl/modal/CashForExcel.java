
package com.xzsoft.xc.gl.modal;

import java.util.Date;

public class CashForExcel {
private String sum_id;     //主键
private String session_id;//会话id
private String ledgerId;//账簿id
private String period_code;//会计期编码
private String acc_id;//科目id
private String acc_code;//科目编码
private String acc_name;//科目名称
private String ca_id;//现金流量项id

private String ca_code;//现金流量项编码
private String ca_name;//现金流量项名称
private double ca_sum;//期初余额数
private String flag;//正确与否标志，正确为0
private String msg;//错误信息
private Date creationDate;   
private Date lastUpdateDate;
private String createdBy;
private String lastUpdatedBy;


public String getSum_id() {
	return sum_id;
}
public void setSum_id(String sum_id) {
	this.sum_id = sum_id;
}
public double getCa_sum() {
	return ca_sum;
}
public void setCa_sum(double ca_sum) {
	this.ca_sum = ca_sum;
}
public String getCa_id() {
	return ca_id;
}
public void setCa_id(String ca_id) {
	this.ca_id = ca_id;
}
public String getCa_code() {
	return ca_code;
}
public void setCa_code(String ca_code) {
	this.ca_code = ca_code;
}
public String getSession_id() {
	return session_id;
}
public void setSession_id(String session_id) {
	this.session_id = session_id;
}

public String getLedgerId() {
	return ledgerId;
}
public void setLedgerId(String ledgerId) {
	this.ledgerId = ledgerId;
}
public String getPeriod_code() {
	return period_code;
}
public void setPeriod_code(String period_code) {
	this.period_code = period_code;
}
public String getAcc_id() {
	return acc_id;
}
public void setAcc_id(String acc_id) {
	this.acc_id = acc_id;
}
public String getAcc_code() {
	return acc_code;
}
public void setAcc_code(String acc_code) {
	this.acc_code = acc_code;
}
public String getAcc_name() {
	return acc_name;
}
public void setAcc_name(String acc_name) {
	this.acc_name = acc_name;
}


public String getCa_name() {
	return ca_name;
}
public void setCa_name(String ca_name) {
	this.ca_name = ca_name;
}

public String getFlag() {
	return flag;
}
public void setFlag(String flag) {
	this.flag = flag;
}
public String getMsg() {
	return msg;
}
public void setMsg(String msg) {
	this.msg = msg;
}
public Date getCreationDate() {
	return creationDate;
}
public void setCreationDate(Date creationDate) {
	this.creationDate = creationDate;
}
public Date getLastUpdateDate() {
	return lastUpdateDate;
}
public void setLastUpdateDate(Date lastUpdateDate) {
	this.lastUpdateDate = lastUpdateDate;
}
public String getCreatedBy() {
	return createdBy;
}
public void setCreatedBy(String createdBy) {
	this.createdBy = createdBy;
}
public String getLastUpdatedBy() {
	return lastUpdatedBy;
}
public void setLastUpdatedBy(String lastUpdatedBy) {
	this.lastUpdatedBy = lastUpdatedBy;
}


}
