package com.xzsoft.xc.fa.modal;

import java.util.Date;

/**
 * 
 * ClassName:FaAddition
 * Function: 资产
 *
 * @author   q p
 * @Date	 2016	2016年7月22日		上午10:58:31
 *
 */
public class FaAddition {
	//操作标记
	private String operate;
	//资产ID
	private String assetId;
	//账簿ID
	private String ledgerId;
	//期间编码
	private String periodCode;
	//资产编码
	private String zcbm;
	//资产名称
	private String zcmc;
	//规格型号
	private String ggxh;
	//序列号
	private String xlh;
	//供应商
	private String vendorId;
	//制造商
	private String zzs;
	//资产属性
	private String faProperty;
	//资产分类
	private String catCode;
	//启用日期
	private String qyrq;
	//使用年限
	private int synx;
	//成本中心
	private String cbzx;
	//项目
	private String projectId;
	//资产用途
	private String zcyt;
	//采购发票
	private String cgfp;
	//增加方式
	private String zjfs;
	//录入方式
	private String lrfs;
	//存放地点
	private String cfdd;
	//保管人
	private String bgr;
	//资产说明
	private String zcsm;
	//原值
	private double yz;
	//累计折旧、累计分摊
	private double ljzjLjtx;
	//数量
	private double sl;
	//单位
	private String dw;
	//减值准备
	private double jzzb;
	//残值率
	private double czl;
	//是否折旧
	private String sfzj;
	//折旧方法
	private String zjff;
	//摊销规则
	private String txgz;
	//资产科目
	private String zckm;
	//累计折旧、累计摊销科目
	private String ljzjtxkm;
	//减值准备科目
	private String jzzbkm;
	//费用科目
	private String fykm;
	//清理科目
	private String qlkm;
	//来源资产ID
	private String upAssetId;
	//清理日期
	private Date qlrq;
	//清理财务期间
	private String qlqj;
	//清理原因
	private String qlyy;
	//折旧年限
	private String zjnx;
	//开始计提折旧期间
    private String ksjtqj;
    //已计提折旧金额
    private double yjtje;
	//创建人
	private String createdBy;
	//创建日期
	private Date creationDate;
	//最后更新人
	private String lastUpdatedBy;
	//最后更新日期
	private Date lastUpdateDate;
	//停用日期  启用日期加上使用期限就是停用日期
	private String tyqj;
	//计提折旧期间
	private String jtqj;
	//折旧金额
	private double zjje;
	//净值
	private double jz; 
	//折旧率
	private double zjl;
	public double getZjl() {
		return zjl;
	}
	public void setZjl(double zjl) {
		this.zjl = zjl;
	}
	public double getJz() {
		return jz;
	}
	public void setJz(double jz) {
		this.jz = jz;
	}
	public String getTyqj() {
		return tyqj;
	}
	public void setTyqj(String tyqj) {
		this.tyqj = tyqj;
	}
	public String getJtqj() {
		return jtqj;
	}
	public void setJtqj(String jtqj) {
		this.jtqj = jtqj;
	}
	public String getOperate() {
		return operate;
	}
	public void setOperate(String operate) {
		this.operate = operate;
	}
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
	public String getGgxh() {
		return ggxh;
	}
	public void setGgxh(String ggxh) {
		this.ggxh = ggxh;
	}
	public String getXlh() {
		return xlh;
	}
	public void setXlh(String xlh) {
		this.xlh = xlh;
	}
	public String getVendorId() {
		return vendorId;
	}
	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}
	public String getZzs() {
		return zzs;
	}
	public void setZzs(String zzs) {
		this.zzs = zzs;
	}
	public String getFaProperty() {
		return faProperty;
	}
	public void setFaProperty(String faProperty) {
		this.faProperty = faProperty;
	}
	public String getCatCode() {
		return catCode;
	}
	public void setCatCode(String catCode) {
		this.catCode = catCode;
	}
	public String getQyrq() {
		return qyrq;
	}
	public void setQyrq(String qyrq) {
		this.qyrq = qyrq;
	}
	public int getSynx() {
		return synx;
	}
	public void setSynx(int synx) {
		this.synx = synx;
	}
	public String getCbzx() {
		return cbzx;
	}
	public void setCbzx(String cbzx) {
		this.cbzx = cbzx;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getZcyt() {
		return zcyt;
	}
	public void setZcyt(String zcyt) {
		this.zcyt = zcyt;
	}
	public String getCgfp() {
		return cgfp;
	}
	public void setCgfp(String cgfp) {
		this.cgfp = cgfp;
	}
	public String getZjfs() {
		return zjfs;
	}
	public void setZjfs(String zjfs) {
		this.zjfs = zjfs;
	}
	public String getLrfs() {
		return lrfs;
	}
	public void setLrfs(String lrfs) {
		this.lrfs = lrfs;
	}
	public String getCfdd() {
		return cfdd;
	}
	public void setCfdd(String cfdd) {
		this.cfdd = cfdd;
	}
	public String getBgr() {
		return bgr;
	}
	public void setBgr(String bgr) {
		this.bgr = bgr;
	}
	public String getZcsm() {
		return zcsm;
	}
	public void setZcsm(String zcsm) {
		this.zcsm = zcsm;
	}
	public double getYz() {
		return yz;
	}
	public void setYz(double yz) {
		this.yz = yz;
	}
	public double getLjzjLjtx() {
		return ljzjLjtx;
	}
	public void setLjzjLjtx(double ljzjLjtx) {
		this.ljzjLjtx = ljzjLjtx;
	}
	public double getSl() {
		return sl;
	}
	public void setSl(double sl) {
		this.sl = sl;
	}
	public String getDw() {
		return dw;
	}
	public void setDw(String dw) {
		this.dw = dw;
	}
	public double getJzzb() {
		return jzzb;
	}
	public void setJzzb(double jzzb) {
		this.jzzb = jzzb;
	}
	public double getCzl() {
		return czl;
	}
	public void setCzl(double czl) {
		this.czl = czl;
	}
	public String getSfzj() {
		return sfzj;
	}
	public void setSfzj(String sfzj) {
		this.sfzj = sfzj;
	}
	public String getZjff() {
		return zjff;
	}
	public void setZjff(String zjff) {
		this.zjff = zjff;
	}
	public String getTxgz() {
		return txgz;
	}
	public void setTxgz(String txgz) {
		this.txgz = txgz;
	}
	public String getZckm() {
		return zckm;
	}
	public void setZckm(String zckm) {
		this.zckm = zckm;
	}
	public String getLjzjtxkm() {
		return ljzjtxkm;
	}
	public void setLjzjtxkm(String ljzjtxkm) {
		this.ljzjtxkm = ljzjtxkm;
	}
	public String getJzzbkm() {
		return jzzbkm;
	}
	public void setJzzbkm(String jzzbkm) {
		this.jzzbkm = jzzbkm;
	}
	public String getFykm() {
		return fykm;
	}
	public void setFykm(String fykm) {
		this.fykm = fykm;
	}
	public String getUpAssetId() {
		return upAssetId;
	}
	public void setUpAssetId(String upAssetId) {
		this.upAssetId = upAssetId;
	}
	public Date getQlrq() {
		return qlrq;
	}
	public void setQlrq(Date qlrq) {
		this.qlrq = qlrq;
	}
	public String getQlqj() {
		return qlqj;
	}
	public void setQlqj(String qlqj) {
		this.qlqj = qlqj;
	}
	public String getQlyy() {
		return qlyy;
	}
	public void setQlyy(String qlyy) {
		this.qlyy = qlyy;
	}
	public String getZjnx() {
		return zjnx;
	}
	public void setZjnx(String zjnx) {
		this.zjnx = zjnx;
	}
	public String getKsjtqj() {
		return ksjtqj;
	}
	public void setKsjtqj(String ksjtqj) {
		this.ksjtqj = ksjtqj;
	}
	public double getYjtje() {
		return yjtje;
	}
	public void setYjtje(double yjtje) {
		this.yjtje = yjtje;
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
	public double getZjje() {
		return zjje;
	}
	public void setZjje(double zjje) {
		this.zjje = zjje;
	}
	public String getQlkm() {
		return qlkm;
	}
	public void setQlkm(String qlkm) {
		this.qlkm = qlkm;
	}
	
}
