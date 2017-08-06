/**
 * 
 */
package com.xzsoft.xc.rep.modal;

/**
 * @author tangxl
 *
 */
public class ReportCellFormula {
	private String sheet;
	private String row;
	private String col;
	private String json;
	//applyWay有个3个值：apply,applyif,clear
	private String applyWay;
	/**
	 * @return the sheet
	 */
	public String getSheet() {
		return sheet;
	}
	/**
	 * @param sheet the sheet to set
	 */
	public void setSheet(String sheet) {
		this.sheet = sheet;
	}
	/**
	 * @return the row
	 */
	public String getRow() {
		return row;
	}
	/**
	 * @param row the row to set
	 */
	public void setRow(String row) {
		this.row = row;
	}
	/**
	 * @return the col
	 */
	public String getCol() {
		return col;
	}
	/**
	 * @param col the col to set
	 */
	public void setCol(String col) {
		this.col = col;
	}
	/**
	 * @return the json
	 */
	public String getJson() {
		return json;
	}
	/**
	 * @param json the json to set
	 */
	public void setJson(String json) {
		this.json = json;
	}
	/**
	 * @return the applyWay
	 */
	public String getApplyWay() {
		return applyWay;
	}
	/**
	 * @param applyWay the applyWay to set
	 */
	public void setApplyWay(String applyWay) {
		this.applyWay = applyWay;
	}
}
