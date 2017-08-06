package com.xzsoft.xc.rep.modal;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @ClassName: FuncParamsBean 
 * @Description: 自定义函数明细信息
 * @author linp
 * @date 2016年8月10日 下午3:12:57 
 *
 */
public class FuncParamsBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@JsonIgnore
	private String paramId ;		// 参数ID
	@JsonIgnore
	private String funId ;			// 扩展函数ID
	private String paramCode ;		// 参数编码
	private String paramName ;		// 参数名称
	private String paramDesc ;		// 参数描述
	private int paramOrder ;		// 排序号
	private String paramLov ;		// 参数值集
	private String enabled ;		// 是否启用
	private String mandatory ;		// 是否必录项
	
	private String funCode ;
	
	@JsonIgnore
	private String createdBy ;
	@JsonIgnore
	private Date creationDate ;
	@JsonIgnore
	private String lastUpdatedBy ;
	@JsonIgnore
	private Date lastUpdateDate ;
	
	public String getParamId() {
		return paramId;
	}
	public void setParamId(String paramId) {
		this.paramId = paramId;
	}
	public String getFunId() {
		return funId;
	}
	public void setFunId(String funId) {
		this.funId = funId;
	}
	public String getParamCode() {
		return paramCode;
	}
	public void setParamCode(String paramCode) {
		this.paramCode = paramCode;
	}
	public String getParamName() {
		return paramName;
	}
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	public String getParamDesc() {
		return paramDesc;
	}
	public void setParamDesc(String paramDesc) {
		this.paramDesc = paramDesc;
	}
	public int getParamOrder() {
		return paramOrder;
	}
	public void setParamOrder(int paramOrder) {
		this.paramOrder = paramOrder;
	}
	public String getParamLov() {
		return paramLov;
	}
	public void setParamLov(String paramLov) {
		this.paramLov = paramLov;
	}
	public String getEnabled() {
		return enabled;
	}
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
	public String getMandatory() {
		return mandatory;
	}
	public void setMandatory(String mandatory) {
		this.mandatory = mandatory;
	}
	public String getFunCode() {
		return funCode;
	}
	public void setFunCode(String funCode) {
		this.funCode = funCode;
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
