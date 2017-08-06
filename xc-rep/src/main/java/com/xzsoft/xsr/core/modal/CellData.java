package com.xzsoft.xsr.core.modal;

public class CellData {
	private String sheet;
	private String row;
	private String col;
	private String json;
	private String applyWay;

	public String getApplyWay() {
		return applyWay;
	}

	public void setApplyWay(String applyWay) {
		this.applyWay = applyWay;
	}

	public String getSheet() {
		return sheet;
	}

	public void setSheet(String sheet) {
		this.sheet = sheet;
	}

	public String getRow() {
		return row;
	}

	public void setRow(String row) {
		this.row = row;
	}

	public String getCol() {
		return col;
	}

	public void setCol(String col) {
		this.col = col;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	@Override
	public String toString() {
		return "CellData [sheet=" + sheet + ", row=" + row + ", col=" + col + ", json=" + json + ", applyWay="
				+ applyWay + "]";
	}


	
}
