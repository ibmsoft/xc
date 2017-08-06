package com.xzsoft.xsr.core.modal;

public class Chk_Scheme {
	private String CHK_SCHEME_ID;
	private String SUIT_ID;
	private String CHK_SCHEME_CODE;
	private String CHK_SCHEME_NAME;
	public String getCHK_SCHEME_ID() {
		return CHK_SCHEME_ID;
	}
	public void setCHK_SCHEME_ID(String cHK_SCHEME_ID) {
		CHK_SCHEME_ID = cHK_SCHEME_ID;
	}
	public String getSUIT_ID() {
		return SUIT_ID;
	}
	public void setSUIT_ID(String sUIT_ID) {
		SUIT_ID = sUIT_ID;
	}
	public String getCHK_SCHEME_CODE() {
		return CHK_SCHEME_CODE;
	}
	public void setCHK_SCHEME_CODE(String cHK_SCHEME_CODE) {
		CHK_SCHEME_CODE = cHK_SCHEME_CODE;
	}
	public String getCHK_SCHEME_NAME() {
		return CHK_SCHEME_NAME;
	}
	public void setCHK_SCHEME_NAME(String cHK_SCHEME_NAME) {
		CHK_SCHEME_NAME = cHK_SCHEME_NAME;
	}
	@Override
	public String toString() {
		return "Chk_Scheme [CHK_SCHEME_ID=" + CHK_SCHEME_ID + ", SUIT_ID=" + SUIT_ID + ", CHK_SCHEME_CODE="
				+ CHK_SCHEME_CODE + ", CHK_SCHEME_NAME=" + CHK_SCHEME_NAME + "]";
	}
	
}
