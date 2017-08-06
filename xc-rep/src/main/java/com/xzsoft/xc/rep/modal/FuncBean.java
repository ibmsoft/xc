package com.xzsoft.xc.rep.modal;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @ClassName: FuncBean 
 * @Description: 自定义函数信息
 * @author linp
 * @date 2016年8月10日 下午3:12:44 
 *
 */
public class FuncBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@JsonIgnore
	private String funId ;		// 函数ID
	private String funType ;	// 函数类型(java接口，存储过程)
	private String funCode ;	// 函数编码
	private String funName ;    // 函数名称
	private String func ;		// 存储过程或JAVA方法名称
	private String funDesc ;	// 描述
	private String funCalType ;	// 运算类型:REP-表间,APP-数据中心,EXP-业务系统
	private String enabeld ;	// 是否启用
	private String httpLink ;
	
	@JsonIgnore
	private String createdBy ;
	@JsonIgnore
	private Date creationDate ;
	@JsonIgnore
	private String lastUpdatedBy ;
	@JsonIgnore
	private Date lastUpdateDate ;
	
	private List<FuncParamsBean> params ; // 参数信息

	public String getFunId() {
		return funId;
	}

	public void setFunId(String funId) {
		this.funId = funId;
	}

	public String getFunType() {
		return funType;
	}

	public void setFunType(String funType) {
		this.funType = funType;
	}

	public String getFunCode() {
		return funCode;
	}

	public void setFunCode(String funCode) {
		this.funCode = funCode;
	}

	public String getFunName() {
		return funName;
	}

	public void setFunName(String funName) {
		this.funName = funName;
	}

	public String getFunc() {
		return func;
	}

	public void setFunc(String func) {
		this.func = func;
	}

	public String getFunDesc() {
		return funDesc;
	}

	public void setFunDesc(String funDesc) {
		this.funDesc = funDesc;
	}

	public String getFunCalType() {
		return funCalType;
	}

	public void setFunCalType(String funCalType) {
		this.funCalType = funCalType;
	}

	public String getEnabeld() {
		return enabeld;
	}

	public void setEnabeld(String enabeld) {
		this.enabeld = enabeld;
	}

	public String getHttpLink() {
		return httpLink;
	}

	public void setHttpLink(String httpLink) {
		this.httpLink = httpLink;
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

	public List<FuncParamsBean> getParams() {
		return params;
	}

	public void setParams(List<FuncParamsBean> params) {
		this.params = params;
	}
	
}
