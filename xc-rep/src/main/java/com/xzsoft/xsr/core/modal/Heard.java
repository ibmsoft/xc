package com.xzsoft.xsr.core.modal;

public class Heard {

	private String RPT_NAME;//报表名称
	private String RPT_CODE;//报表代码
	private int DATASTARTIDX;//数据起始行
	private int DATAENDIDX;//数据终止行
	private int FJDATASTARTIDX;//附加数据起始行
	private int FJDATAENDIDX;//附加数据终止行
	
	public String getRPT_NAME() {
		return RPT_NAME;
	}
	public void setRPT_NAME(String rPT_NAME) {
		RPT_NAME = rPT_NAME;
	}
	public String getRPT_CODE() {
		return RPT_CODE;
	}
	public void setRPT_CODE(String rPT_CODE) {
		RPT_CODE = rPT_CODE;
	}
	public int getDATASTARTIDX() {
		return DATASTARTIDX;
	}
	public void setDATASTARTIDX(int dATASTARTIDX) {
		DATASTARTIDX = dATASTARTIDX;
	}
	public int getDATAENDIDX() {
		return DATAENDIDX;
	}
	public void setDATAENDIDX(int dATAENDIDX) {
		DATAENDIDX = dATAENDIDX;
	}
	public int getFJDATASTARTIDX() {
		return FJDATASTARTIDX;
	}
	public void setFJDATASTARTIDX(int fJDATASTARTIDX) {
		FJDATASTARTIDX = fJDATASTARTIDX;
	}
	public int getFJDATAENDIDX() {
		return FJDATAENDIDX;
	}
	public void setFJDATAENDIDX(int fJDATAENDIDX) {
		FJDATAENDIDX = fJDATAENDIDX;
	}
	
	
}
