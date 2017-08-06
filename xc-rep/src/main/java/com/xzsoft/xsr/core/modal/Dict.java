package com.xzsoft.xsr.core.modal;

public class Dict implements Comparable<Dict>{

	private String DICT_NAME;  //字典名称
	private String DICT_DESC;  //字典描述
	private String DICT_TYPE;  //字典类型
	private String DICT_TABLE; //对应后台表名称
	private String ITEM_CODE;  //字典成员编码
	private String ITEM_NAME;  //字典成员名称
	private String ITEM_VAL;   //字典成员默认值
	private String ITEM_DESC;  //字典成员描述
	
	public String getDICT_NAME() {
		return DICT_NAME;
	}

	public void setDICT_NAME(String dICT_NAME) {
		DICT_NAME = dICT_NAME;
	}

	public String getDICT_DESC() {
		return DICT_DESC;
	}

	public void setDICT_DESC(String dICT_DESC) {
		DICT_DESC = dICT_DESC;
	}

	public String getDICT_TYPE() {
		return DICT_TYPE;
	}

	public void setDICT_TYPE(String dICT_TYPE) {
		DICT_TYPE = dICT_TYPE;
	}

	public String getDICT_TABLE() {
		return DICT_TABLE;
	}

	public void setDICT_TABLE(String dICT_TABLE) {
		DICT_TABLE = dICT_TABLE;
	}

	public String getITEM_CODE() {
		return ITEM_CODE;
	}

	public void setITEM_CODE(String iTEM_CODE) {
		ITEM_CODE = iTEM_CODE;
	}

	public String getITEM_NAME() {
		return ITEM_NAME;
	}

	public void setITEM_NAME(String iTEM_NAME) {
		ITEM_NAME = iTEM_NAME;
	}

	public String getITEM_VAL() {
		return ITEM_VAL;
	}

	public void setITEM_VAL(String iTEM_VAL) {
		ITEM_VAL = iTEM_VAL;
	}

	public String getITEM_DESC() {
		return ITEM_DESC;
	}

	public void setITEM_DESC(String iTEM_DESC) {
		ITEM_DESC = iTEM_DESC;
	}

	@Override
	public int compareTo(Dict o) {
		// TODO Auto-generated method stub
		return 0;
	}

}
