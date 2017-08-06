package com.xzsoft.xc.bg.modal;

/**
 * @ClassName: BgItemDTO 
 * @Description: 账簿预算项目数据
 * @author linp
 * @date 2016年4月7日 上午10:02:50 
 *
 */
public class BgItemDTO {

	private String bgItemId;	// 预算项目ID
	private String bgHrcyId;    // 预算体系ID
	private String bgItemCode;  // 预算项目编码
	private String bgItemName;  // 预算项目名称
	private String bgItemDesc;  // 预算项目描述
	private String bgItemUpId;  // 上级预算项
	private String bgItemProp;  // 收支属性：SY-损益类，CA-现金类
	private String bgItemType;  // 分类：1-收入，-1-支出
	private String startDate;   // 开始日期
	private String endDate;     // 结束日期 
	private String bgLdItemId;  // 账簿预算项ID
	private String ledgerId;    // 账簿ID
	private String bgCtrlDim;	// 控制维度：1-预算项目，2-预算项目+成本中心
	private String bgCtrlMode;  // 控制方式：1-不控制，2-预警控制，3-绝对控制
	private String isEnabled;   // 启用：Y-是，N-否
	
	public String getBgItemId() {
		return bgItemId;
	}
	public void setBgItemId(String bgItemId) {
		this.bgItemId = bgItemId;
	}
	public String getBgHrcyId() {
		return bgHrcyId;
	}
	public void setBgHrcyId(String bgHrcyId) {
		this.bgHrcyId = bgHrcyId;
	}
	public String getBgItemCode() {
		return bgItemCode;
	}
	public void setBgItemCode(String bgItemCode) {
		this.bgItemCode = bgItemCode;
	}
	public String getBgItemName() {
		return bgItemName;
	}
	public void setBgItemName(String bgItemName) {
		this.bgItemName = bgItemName;
	}
	public String getBgItemDesc() {
		return bgItemDesc;
	}
	public void setBgItemDesc(String bgItemDesc) {
		this.bgItemDesc = bgItemDesc;
	}
	public String getBgItemUpId() {
		return bgItemUpId;
	}
	public void setBgItemUpId(String bgItemUpId) {
		this.bgItemUpId = bgItemUpId;
	}
	public String getBgItemProp() {
		return bgItemProp;
	}
	public void setBgItemProp(String bgItemProp) {
		this.bgItemProp = bgItemProp;
	}
	public String getBgItemType() {
		return bgItemType;
	}
	public void setBgItemType(String bgItemType) {
		this.bgItemType = bgItemType;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getBgLdItemId() {
		return bgLdItemId;
	}
	public void setBgLdItemId(String bgLdItemId) {
		this.bgLdItemId = bgLdItemId;
	}
	public String getLedgerId() {
		return ledgerId;
	}
	public void setLedgerId(String ledgerId) {
		this.ledgerId = ledgerId;
	}
	public String getBgCtrlDim() {
		return bgCtrlDim;
	}
	public void setBgCtrlDim(String bgCtrlDim) {
		this.bgCtrlDim = bgCtrlDim;
	}
	public String getBgCtrlMode() {
		return bgCtrlMode;
	}
	public void setBgCtrlMode(String bgCtrlMode) {
		this.bgCtrlMode = bgCtrlMode;
	}
	public String getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
	}
	
}
