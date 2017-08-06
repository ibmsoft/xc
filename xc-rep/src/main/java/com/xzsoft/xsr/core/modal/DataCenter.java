package com.xzsoft.xsr.core.modal;

public class DataCenter {
	
	private String dc_id;
	private String dc_code;
	private String dc_name;
	private String dc_type;
	private String parent_id;
	private String shortName;
	private String description;
	private String enabled;
	
	public String getDc_id() {
		return dc_id;
	}
	public void setDc_id(String dc_id) {
		this.dc_id = dc_id;
	}
	public String getDc_code() {
		return dc_code;
	}
	public void setDc_code(String dc_code) {
		this.dc_code = dc_code;
	}
	public String getDc_name() {
		return dc_name;
	}
	public void setDc_name(String dc_name) {
		this.dc_name = dc_name;
	}
	public String getDc_type() {
		return dc_type;
	}
	public void setDc_type(String dc_type) {
		this.dc_type = dc_type;
	}
	public String getParent_id() {
		return parent_id;
	}
	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getEnabled() {
		return enabled;
	}
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
	
	@Override
	public String toString() {
		return "DataCenter [dc_id=" + dc_id + ", dc_code=" + dc_code
				+ ", dc_name=" + dc_name + "]";
	}
	
}
