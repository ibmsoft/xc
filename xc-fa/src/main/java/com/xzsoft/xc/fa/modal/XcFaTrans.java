package com.xzsoft.xc.fa.modal;

import java.util.Date;

/**
 * ClassName:XcFaTrans Function: 资产事务
 *
 * @author GuoXiuFeng
 * @version Ver 1.0
 * @since Ver 1.0
 * @Date 2016 2016年7月29日 上午9:29:30
 *
 */
public class XcFaTrans {
	// 资产ID
	private String assetId;
	// 账簿ID
	private String ledgerId;
	// 期间编码
	private String periodCode;
	// 事务日期
	private Date transDate;
	// 事务类型编码
	private String transCode;
	// 来源主ID
	private String srcId;
	// 来源子ID
	private String srcDtlId;
	// 来源表
	private String srcTab;
	// 事务说明
	private String bzsm;
	// 过账标记
	private String postFlag;
	// 创建日期
	private Date creationDate;
	// 创建人
	private String createdBy;
	// 最后更新日期
	private Date lastUpdateDate;
	// 最后更新人
	private String lastUpdateBy;
	// 发生数
	private double quantity;
	// 原值借方
	private double yzdr;
	// 原值贷方
	private double yzcr;
	// 累计折旧借方
	private double ljzjdr;
	// 累计折旧贷方
	private double ljzjcr;
	// 减值准备借方
	private double jzzbdr;
	// 减值准备贷方
	private double jzzbcr;
	// 成本中心
	private String cbzx;
	// 资产分类
	private String catCode;

	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

	public String getLedgerId() {
		return ledgerId;
	}

	public void setLedgerId(String ledgerId) {
		this.ledgerId = ledgerId;
	}

	public String getPeriodCode() {
		return periodCode;
	}

	public void setPeriodCode(String periodCode) {
		this.periodCode = periodCode;
	}

	public Date getTransDate() {
		return transDate;
	}

	public void setTransDate(Date transDate) {
		this.transDate = transDate;
	}

	public String getTransCode() {
		return transCode;
	}

	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}

	public String getSrcId() {
		return srcId;
	}

	public void setSrcId(String srcId) {
		this.srcId = srcId;
	}

	public String getSrcDtlId() {
		return srcDtlId;
	}

	public void setSrcDtlId(String srcDtlId) {
		this.srcDtlId = srcDtlId;
	}

	public String getSrcTab() {
		return srcTab;
	}

	public void setSrcTab(String srcTab) {
		this.srcTab = srcTab;
	}

	public String getBzsm() {
		return bzsm;
	}

	public void setBzsm(String bzsm) {
		this.bzsm = bzsm;
	}

	public String getPostFlag() {
		return postFlag;
	}

	public void setPostFlag(String postFlag) {
		this.postFlag = postFlag;
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

	public String getLastUpdateBy() {
		return lastUpdateBy;
	}

	public void setLastUpdateBy(String lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public double getYzdr() {
		return yzdr;
	}

	public void setYzdr(double yzdr) {
		this.yzdr = yzdr;
	}

	public double getYzcr() {
		return yzcr;
	}

	public void setYzcr(double yzcr) {
		this.yzcr = yzcr;
	}

	public double getLjzjdr() {
		return ljzjdr;
	}

	public void setLjzjdr(double ljzjdr) {
		this.ljzjdr = ljzjdr;
	}

	public double getLjzjcr() {
		return ljzjcr;
	}

	public void setLjzjcr(double ljzjcr) {
		this.ljzjcr = ljzjcr;
	}

	public double getJzzbdr() {
		return jzzbdr;
	}

	public void setJzzbdr(double jzzbdr) {
		this.jzzbdr = jzzbdr;
	}

	public double getJzzbcr() {
		return jzzbcr;
	}

	public void setJzzbcr(double jzzbcr) {
		this.jzzbcr = jzzbcr;
	}

	public String getCbzx() {
		return cbzx;
	}

	public void setCbzx(String cbzx) {
		this.cbzx = cbzx;
	}

	public String getCatCode() {
		return catCode;
	}

	public void setCatCode(String catCode) {
		this.catCode = catCode;
	}
	
}
