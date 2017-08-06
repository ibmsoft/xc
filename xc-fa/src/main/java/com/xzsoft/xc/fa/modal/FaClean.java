package com.xzsoft.xc.fa.modal;

import java.util.List;

public class FaClean {
	
	//资产信息集合
	private List<FaAddition> faAdditions;
	//清理标记 (Y 清理 N 取消清理)
	private String cleanFlag;
	// 是否清理计提
	private String cleanJt;

	public List<FaAddition> getFaAdditions() {
		return faAdditions;
	}

	public String getCleanFlag() {
		return cleanFlag;
	}

	public void setCleanFlag(String cleanFlag) {
		this.cleanFlag = cleanFlag;
	}

	public void setFaAdditions(List<FaAddition> faAdditions) {
		this.faAdditions = faAdditions;
	}

	public String getCleanJt() {
		return cleanJt;
	}

	public void setCleanJt(String cleanJt) {
		this.cleanJt = cleanJt;
	}
	
	
}
