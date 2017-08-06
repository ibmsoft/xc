package com.xzsoft.xsr.core.modal;

import java.util.ArrayList;
import java.util.List;

public class VerifyErrorModal {

	private String name;
	
	private List<VerifyErrorModal> subError =new ArrayList<VerifyErrorModal>();
	
	private List<VerifyErrorModal> detail=new ArrayList<VerifyErrorModal>();
	//公式说明
	private String desc;
	//公式描述
	private String description;
	//稽核结果
	private String result;
	//稽核差额
	private String amount;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public List<VerifyErrorModal> getSubError() {
		return subError;
	}

	public void setSubError(List<VerifyErrorModal> subError) {
		this.subError = subError;
	}

	public List<VerifyErrorModal> getDetail() {
		return detail;
	}

	public void setDetail(List<VerifyErrorModal> detail) {
		this.detail = detail;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "desc:  "+desc+";  description:   "+description+";  result:  "+result+";  amount:  "+amount+"; ";
	}
	
}
