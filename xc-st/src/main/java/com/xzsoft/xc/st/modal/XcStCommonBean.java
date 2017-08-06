package com.xzsoft.xc.st.modal;

import java.util.Date;

/**
 * 
  * @ClassName XcStCommonBean
  * @Description 库存模块的公共字段对应的实体类
  * @author RenJianJian
  * @date 2016年12月20日 下午12:55:09
 */
public class XcStCommonBean {
	private String tableName;		//操作的表
	private String priKey;			//操作的主键对应的列
	private String priKeyId;		//操作的主键数据
	private String status;			//状态
	private Date lastUpdateDate;	//最后更新日期
	private String lastUpdatedBy;	//最后更新人
	private double AMOUNT;
	private double QTY;
	private double PRICE;
	public XcStCommonBean() {
		super();
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getPriKey() {
		return priKey;
	}
	public void setPriKey(String priKey) {
		this.priKey = priKey;
	}
	public String getPriKeyId() {
		return priKeyId;
	}
	public void setPriKeyId(String priKeyId) {
		this.priKeyId = priKeyId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public double getAMOUNT() {
		return AMOUNT;
	}
	public void setAMOUNT(double aMOUNT) {
		AMOUNT = aMOUNT;
	}
	public double getQTY() {
		return QTY;
	}
	public void setQTY(double qTY) {
		QTY = qTY;
	}
	public double getPRICE() {
		return PRICE;
	}
	public void setPRICE(double pRICE) {
		PRICE = pRICE;
	}
}
