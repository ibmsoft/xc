package com.xzsoft.xc.fa.modal;

public class XcAsset {
	/**
	 * 
	  * @ClassName: XcAsset
	  * @Description:  资产分类
	  * @author   wangwh
	 */
	private String catId;
	public String getFaProperty() {
		return faProperty;
	}
	public void setFaProperty(String faProperty) {
		this.faProperty = faProperty;
	}
	private String catCode;
	private String catName;
	private String upCode;
	private String faProperty;
	public String getCatId() {
		return catId;
	}
	public void setCatId(String catId) {
		this.catId = catId;
	}
	public String getCatCode() {
		return catCode;
	}
	public void setCatCode(String catCode) {
		this.catCode = catCode;
	}
	public String getCatName() {
		return catName;
	}
	public void setCatName(String catName) {
		this.catName = catName;
	}
	public String getUpCode() {
		return upCode;
	}
	public void setUpCode(String upCode) {
		this.upCode = upCode;
	}
	

}
