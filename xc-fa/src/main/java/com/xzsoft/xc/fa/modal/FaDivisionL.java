package com.xzsoft.xc.fa.modal;

import java.util.Date;

/**
 * ClassName:FaDivisionL
 * Function: 资产拆分行表
 *
 * @author   zhaoxin
 * @Date	 2016	2016年8月3日		下午2:28:18
 *
 */
public class FaDivisionL {
	// 资产拆分行ID
		private String divDtlId;
		// 资产拆分头ID
		private String divisionId;
		// 被拆分后资产ID
		private String assetId;
		// 资产编码
		private String zcbm;
		// 资产名称
		private String zcmc;
		// 原值
		private String yz;
		// 累计折旧、累计分摊
		private String ljzjljtx;
		// 数量
		private String sl;
		// 减值准备
		private String jzzb;
		// 创建人
		private String createdBy;
		// 创建日期
		private Date creationDate;
		// 最后更新人
		private String lastUpdatedBy;
		// 最后更新日期
		private Date lastUpdateDate;
		public String getDivDtlId() {
			return divDtlId;
		}
		public void setDivDtlId(String divDtlId) {
			this.divDtlId = divDtlId;
		}
		public String getDivisionId() {
			return divisionId;
		}
		public void setDivisionId(String divisionId) {
			this.divisionId = divisionId;
		}
		public String getAssetId() {
			return assetId;
		}
		public void setAssetId(String assetId) {
			this.assetId = assetId;
		}
		public String getZcbm() {
			return zcbm;
		}
		public void setZcbm(String zcbm) {
			this.zcbm = zcbm;
		}
		public String getZcmc() {
			return zcmc;
		}
		public void setZcmc(String zcmc) {
			this.zcmc = zcmc;
		}
		public String getYz() {
			return yz;
		}
		public void setYz(String yz) {
			this.yz = yz;
		}
		public String getLjzjljtx() {
			return ljzjljtx;
		}
		public void setLjzjljtx(String ljzjljtx) {
			this.ljzjljtx = ljzjljtx;
		}
		public String getSl() {
			return sl;
		}
		public void setSl(String sl) {
			this.sl = sl;
		}
		public String getJzzb() {
			return jzzb;
		}
		public void setJzzb(String jzzb) {
			this.jzzb = jzzb;
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
