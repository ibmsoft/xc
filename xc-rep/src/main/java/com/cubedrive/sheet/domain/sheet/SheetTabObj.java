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

public class SheetTabObj {
	
    private String name;	
	private Integer id;
	private Boolean actived;
    private String color;
    private Integer maxRow;
    private Integer maxCol;
    private Integer totalCellNum;
    private String extraInfo;
    private Boolean allCellDataLoaded;
	
	public SheetTabObj(SheetTab tab) {
		this.name = tab.getName();
		this.id = tab.getId();
		this.actived = tab.getActive();
        this.color = tab.getColor();
        this.extraInfo = tab.getExtraInfo();
	}
	
	public SheetTabObj(String name, Integer id, boolean actived) {
		this.name = name;
		this.id = id;
		this.actived = actived;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getActived() {
		return actived;
	}

	public void setActived(Boolean actived) {
		this.actived = actived;
	}

    public Integer getMaxRow() {
        return maxRow;
    }
    
    public void setMaxRow(Integer maxRow) {
        this.maxRow = maxRow;
    }
    
    public Integer getMaxCol() {
        return maxCol;
    }
    
    public void setMaxCol(Integer maxCol) {
        this.maxCol = maxCol;
    }
    
    public Integer getTotalCellNum() {
        return totalCellNum;
    }
    
    public void setTotalCellNum(Integer totalCellNum) {
        this.totalCellNum = totalCellNum;
    }

	public String getExtraInfo() {
		return extraInfo;
	}

	public void setExtraInfo(String extraInfo) {
		this.extraInfo = extraInfo;
	}

    public Boolean getAllCellDataLoaded() {
        return allCellDataLoaded;
    }

    public void setAllCellDataLoaded(Boolean allCellDataLoaded) {
        this.allCellDataLoaded = allCellDataLoaded;
    }
	
    
    
}
