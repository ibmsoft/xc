package com.xzsoft.xc.fa.modal;

import java.util.Date;

/**
 * ClassName:FaDivisionH
 * Function: 资产拆分头表
 *
 * @author   zhaoxin
 * @Date	 2016	2016年8月3日		下午2:28:18
 *
 */
public class FaDivisionH {

	// 拆分ID
		private String divisionId;
		// 账簿ID
		private String ledgerId;
		// 拆分日期
		private String divisionDate;
		// 期间
		private String periodCode;
		// 拆分原因
		private String bzsm;
		// 被拆分资产ID
		private String assetId;
		// 原值
		private double yz;
		// 累计折旧、累计分摊
		private double ljzjljtx;
		//减值准备
		private double jzzb;
		// 数量
		private double sl;		
		// 创建人
		private String createdBy;
		// 创建日期
		private Date creationDate;
		// 最后更新人
		private String lastUpdatedBy;
		// 最后更新日期
		private Date lastUpdateDate;
		public String getDivisionId() {
			return divisionId;
		}
		public void setDivisionId(String divisionId) {
			this.divisionId = divisionId;
		}
		public String getLedgerId() {
			return ledgerId;
		}
		public void setLedgerId(String ledgerId) {
			this.ledgerId = ledgerId;
		}
		public String getDivisionDate() {
			return divisionDate;
		}
		public void setDivisionDate(String divisionDate) {
			this.divisionDate = divisionDate;
		}
		public String getPeriodCode() {
			return periodCode;
		}
		public void setPeriodCode(String periodCode) {
			this.periodCode = periodCode;
		}
		public String getBzsm() {
			return bzsm;
		}
		public void setBzsm(String bzsm) {
			this.bzsm = bzsm;
		}
		public String getAssetId() {
			return assetId;
		}
		public void setAssetId(String assetId) {
			this.assetId = assetId;
		}
		public double getYz() {
			return yz;
		}
		public void setYz(double yz) {
			this.yz = yz;
		}
		public double getLjzjljtx() {
			return ljzjljtx;
		}
		public void setLjzjljtx(double ljzjljtx) {
			this.ljzjljtx = ljzjljtx;
		}
		
		public double getJzzb() {
			return jzzb;
		}
		public void setJzzb(double jzzb) {
			this.jzzb = jzzb;
		}
		public double getSl() {
			return sl;
		}
		public void setSl(double sl) {
			this.sl = sl;
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
