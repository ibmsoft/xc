package com.xzsoft.xc.ap.modal;
import java.util.Date;
/**
 * 
  * @ClassName: ApPayModeBean
  * @Description: 账簿收付款方式
  * @author 任建建
  * @date 2016年4月11日 下午1:21:44
 */
public class ApPayModeBean {
	
	private String ledgerId;		//账簿ID
	private String ldModeId;		
	private String payAccId;		//付款科目
	private String recAccId;		//收款科目
	private String modeId;			//收付款ID
	private String modeType;		//收付款分类
	private String modeName; 		//收付款名称
	private String modeDesc;		//收付款描述
	private Date creationDate;		//创建日期
	private String createdBy;		//申请人
	private Date lastUpdateDate;	//最后更新日期
	private String lastUpdatedBy;	//最后更新人
	public String getLedgerId() {
		return ledgerId;
	}
	public void setLedgerId(String ledgerId) {
		this.ledgerId = ledgerId;
	}
	public String getLdModeId() {
		return ldModeId;
	}
	public void setLdModeId(String ldModeId) {
		this.ldModeId = ldModeId;
	}
	public String getPayAccId() {
		return payAccId;
	}
	public void setPayAccId(String payAccId) {
		this.payAccId = payAccId;
	}
	public String getRecAccId() {
		return recAccId;
	}
	public void setRecAccId(String recAccId) {
		this.recAccId = recAccId;
	}
	public String getModeId() {
		return modeId;
	}
	public void setModeId(String modeId) {
		this.modeId = modeId;
	}
	public String getModeType() {
		return modeType;
	}
	public void setModeType(String modeType) {
		this.modeType = modeType;
	}
	public String getModeName() {
		return modeName;
	}
	public void setModeName(String modeName) {
		this.modeName = modeName;
	}
	public String getModeDesc() {
		return modeDesc;
	}
	public void setModeDesc(String modeDesc) {
		this.modeDesc = modeDesc;
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