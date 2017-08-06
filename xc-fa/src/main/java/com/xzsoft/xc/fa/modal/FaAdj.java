package com.xzsoft.xc.fa.modal;

import java.util.List;

public class FaAdj {
	// 头信息
	private FaAdjH faAdjH;
	// 行信息
	private List<FaAdjL> faAdjLs;
	// 操作类型
	private String opType;
	
	public FaAdjH getFaAdjH() {
		return faAdjH;
	}
	public void setFaAdjH(FaAdjH faAdjH) {
		this.faAdjH = faAdjH;
	}
	public List<FaAdjL> getFaAdjLs() {
		return faAdjLs;
	}
	public void setFaAdjLs(List<FaAdjL> faAdjLs) {
		this.faAdjLs = faAdjLs;
	}
	public String getOpType() {
		return opType;
	}
	public void setOpType(String opType) {
		this.opType = opType;
	}
}
