/**
 * Enterprise Spreadsheet Solutions
 * Copyright(c) FeyaSoft Inc. All right reserved.
 * info@enterpriseSheet.com
 * http://www.enterpriseSheet.com
 * 
 * Licensed under the EnterpriseSheet Commercial License.
 * http://enterprisesheet.com/license.jsp
 * 
 * You need to have a valid license key to access this file.
 */
package com.cubedrive.sheet.domain.sheet;

import java.sql.Timestamp;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.cubedrive.base.domain.BaseDomain;

public class SheetCell extends BaseDomain implements Comparable<SheetCell> {

	private static final long serialVersionUID = -1061507017182835906L;

	@JsonIgnore
	private Integer id;
	
	@JsonIgnore
	private Integer tabId;

	private Integer x;

	private Integer y;

	private String content;
	
	private Boolean cal;
	
	private String rawData;
	
	//added by zhousr 2016-2-19
	@JsonIgnore
	private String FORMAT_ID; //FORMAT_ID
	@JsonIgnore
	private Timestamp CREATION_DATE;
	@JsonIgnore
	private String CREATED_BY;
	public String getFORMAT_ID() {
		return FORMAT_ID;
	}
	public void setFORMAT_ID(String fORMAT_ID) {
		FORMAT_ID = fORMAT_ID;
	}
	public Timestamp getCREATION_DATE() {
		return CREATION_DATE;
	}
	public void setCREATION_DATE(Timestamp cREATION_DATE) {
		CREATION_DATE = cREATION_DATE;
	}
	public String getCREATED_BY() {
		return CREATED_BY;
	}
	public void setCREATED_BY(String cREATED_BY) {
		CREATED_BY = cREATED_BY;
	}
    //added by zhousr 2016-2-19 end
	/**
	 * added by zhousr 2016-4-27 start
	 */
	@JsonIgnore
	private String CELL_COMMENT; //CELL_COMMENT
	@JsonIgnore
	private String CELL_COMMENT_TYPE; //CELL_COMMENT_TYPE
	public String getCELL_COMMENT() {
		return CELL_COMMENT;
	}
	public void setCELL_COMMENT(String cELL_COMMENT) {
		CELL_COMMENT = cELL_COMMENT;
	}
	public String getCELL_COMMENT_TYPE() {
		return CELL_COMMENT_TYPE;
	}
	public void setCELL_COMMENT_TYPE(String cELL_COMMENT_TYPE) {
		CELL_COMMENT_TYPE = cELL_COMMENT_TYPE;
	}
	//added by zhousr 2016-4-27 end
	public SheetCell(Integer x, Integer y, String content) {
		super();
		this.x = x;
		this.y = y;
		this.content = content;
	}

	public SheetCell(Integer tabId, Integer x, Integer y, String content) {
		super();
		this.tabId = tabId;
		this.x = x;
		this.y = y;
		this.content = content;
		
	}
	
	public SheetCell(Integer tabId, Integer x, Integer y, String content,
			String data, boolean cal) {
		super();
		this.tabId = tabId;
		this.x = x;
		this.y = y;
		this.content = content;
		this.rawData = data;
		this.cal = cal;
	}

	public SheetCell() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getX() {
		return x;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public Integer getY() {
		return y;
	}

	public void setY(Integer y) {
		this.y = y;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getTabId() {
		return tabId;
	}

	public void setTabId(Integer tabId) {
		this.tabId = tabId;
	}

	public Boolean getCal() {
	    if(cal == null)
	        return false;
	    else
		return cal;
	}

	public void setCal(Boolean cal) {
		this.cal = cal;
	}

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
	}
	

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SheetCell other = (SheetCell) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	public String toString() {
		return this.getTabId() + " -- " + this.getX() + " -- " + this.getY() + " -- " + this.getContent();
	}
	@Override
	public int compareTo(SheetCell c) {
		int xGap = x - c.getX();
		if(xGap != 0) {
			return xGap;
		} else {
			int yGap = y - c.getY();
			if(yGap != 0) {
				return yGap;
			}
		}
		return 0;
	}

}
