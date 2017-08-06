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
package com.cubedrive.sheet.service.sheet._import.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.poi.ss.util.CellReference;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheet;

import com.cubedrive.base.utils.DoSomethingUtil;
import com.cubedrive.base.utils.DoSomethingUtil.OnException;
import com.cubedrive.base.utils.JsonUtil;
import com.cubedrive.sheet.SheetAppConstants;
import com.cubedrive.sheet.domain.sheet.SheetCell;
import com.cubedrive.sheet.domain.sheet.SheetTab;
import com.cubedrive.sheet.service.sheet._import.SpreadsheetImportHelper;
import com.cubedrive.sheet.service.sheet._import.ctx.CellImportContext;
import com.cubedrive.sheet.service.sheet._import.ctx.SheetImportContext;
import com.cubedrive.sheet.service.sheet._import.ctx.WorkbookImportContext;
import com.cubedrive.sheet.service.sheet._import.handler.cell.NumericCellValueProcessor;
import com.cubedrive.sheet.service.sheet._import.handler.cell.ReadEventCellComment;
import com.cubedrive.sheet.service.sheet._import.handler.cell.ReadEventCellHyperlink;
import com.cubedrive.sheet.service.sheet._import.handler.cell.ReadEventCellStyle;
import com.cubedrive.sheet.service.sheet._import.handler.cell.ReadEventCellValue;
import com.cubedrive.sheet.service.sheet._import.handler.sheet.ReadEventSheetAutoFilter;
import com.cubedrive.sheet.service.sheet._import.handler.sheet.ReadEventSheetControl;
import com.cubedrive.sheet.service.sheet._import.handler.sheet.ReadEventSheetControl.SheetControl;
import com.cubedrive.sheet.service.sheet._import.handler.sheet.ReadEventSheetTables;
import com.cubedrive.sheet.service.sheet._import.xssf.XSSFReadEventCell;
import com.cubedrive.sheet.service.sheet._import.xssf.XSSFReadEventSheet;

public class CellImportHandler {

	private final static CellImportHandler _instance = new CellImportHandler();

	private CellImportHandler() {

	}

	public void startCell(CellImportContext cellImportContext) {
		boolean emptyCellValue = false;
		XSSFReadEventCell cell= cellImportContext.cell();
		
		if(cell.val() == null && cell.richString() == null && cell.formula() ==null){
			emptyCellValue = true;
		}
		if(!emptyCellValue ||
				 cellImportContext.parentContext().getCellCountOfRow() < SheetAppConstants.SHEET_COLUMN_MAX_EMPTY_CELL){
			SheetCell sheetCell = createSheetCell(cellImportContext);
			cellImportContext.setSheetCell(sheetCell);
		}
	}

	public void endCell(CellImportContext cellImportContext) {
		SheetCell sheetCell = cellImportContext.getSheetCell();
		if(sheetCell != null){
			SheetImportContext sheetImportContext = cellImportContext.parentContext().parentContext();
			sheetImportContext.addSheetCell(sheetCell);
		}
	}

	private SheetCell createSheetCell(final CellImportContext cellImportContext) {
		final SheetImportContext sheetImportContext= cellImportContext.parentContext().parentContext();
		final WorkbookImportContext workbookImportContext = sheetImportContext.parentContext();
		try {
			final SheetCell sheetCell = new SheetCell();
			final Map<String, Object> contentMap = new HashMap<>();
			DoSomethingUtil.doSomething(new DoSomethingUtil.DoSomething() {
				public void doIt() {
					ReadEventCellStyle readEventCellStyle = ReadEventCellStyle.build(cellImportContext.getCellStyle(),cellImportContext.getCellType(),cellImportContext);
					Map<String, Object> styleSettings = readEventCellStyle.getStyles();
					cellImportContext.getStyleSettings().putAll(styleSettings);
				}

				public OnException onException() {
					return DoSomethingUtil.OnException.ignore;
				}
			}, 
			new DoSomethingUtil.DoSomething() {
				public void doIt() {
					ReadEventCellValue readEventCellValue = ReadEventCellValue.build(cellImportContext);
					handleCellValue(readEventCellValue, cellImportContext);
					
					if(cellImportContext.getData() != null){
					    contentMap.put("data", cellImportContext.getData());
					}
					String rawData = cellImportContext.getRawData() != null ?  cellImportContext.getRawData().toString(): null;
					if (rawData != null) {
						sheetCell.setRawData(rawData.toString());
					}
					Map<String,Object> styleSettings = cellImportContext.getStyleSettings();
//					Integer styleId = workbookImportContext.getWorkbookStylesTable().getOrCreateStyle(styleSettings);
//					contentMap.put("style", styleId);
					contentMap.putAll(styleSettings);

					if (cellImportContext.getCellType() == CellImportContext.CELL_TYPE_FORMULA) {
						sheetCell.setCal(true);
						String fv = cellImportContext.cell().val(); // formula cached value
						if(fv != null && fv.length()>0)
							contentMap.put("value", fv);
						
						Map<String,Object> arrayFormulaSetting = (Map<String,Object>)cellImportContext.getVariable("arrayFormulaSetting");
						if(arrayFormulaSetting != null){
							contentMap.putAll(arrayFormulaSetting);
						}
					}
					
				}

				public OnException onException() {
					return DoSomethingUtil.OnException.ignore;
				}
			}, 
			new DoSomethingUtil.DoSomething() {
				public void doIt() {
					ReadEventCellHyperlink readEventCellHyperlink = ReadEventCellHyperlink.build(cellImportContext);
					Map<String, Object> hyperlinkSettings = readEventCellHyperlink.getHyperlink();
					contentMap.putAll(hyperlinkSettings);
				}

				public OnException onException() {
					return DoSomethingUtil.OnException.ignore;
				}
			}, 
			new DoSomethingUtil.DoSomething() {
				public void doIt() {
					ReadEventCellComment readEventComment = ReadEventCellComment.build(cellImportContext);
					Map<String, Object> commentSetting = readEventComment.getComment();
					contentMap.putAll(commentSetting);
				}

				public OnException onException() {
					return DoSomethingUtil.OnException.ignore;
				}
			}, 
			new DoSomethingUtil.DoSomething() {
				
				public void doIt() {
					ReadEventSheetTables readEventSheetTables = sheetImportContext.getReadEventSheetTables();
					CellReference cellRef = cellImportContext.cellReference();
					ReadEventSheetTables.ReadEventSheetTable readEventSheetTable = readEventSheetTables.getReadEventSheetTable(cellRef);
					if (readEventSheetTable != null) {
						Map<String,Object> tableMap = new HashMap<>(readEventSheetTable.tpl());
						if(readEventSheetTable.isTableHeader(cellImportContext.cellReference())){
							tableMap.put("plain", sheetCell.getRawData());
						}
						String tplJson = JsonUtil.toJson(tableMap);
						contentMap.put("tpl",tplJson);
						if(!readEventSheetTable.isTableHeader(cellRef)){
							contentMap.remove("bgc");
						}
					}
				}

				public OnException onException() {
					return DoSomethingUtil.OnException.ignore;
				}
			},
			new DoSomethingUtil.DoSomething() {
				
				@Override
				public void doIt() {
					ReadEventSheetAutoFilter readEventSheetAutoFilter = sheetImportContext.getReadEventSheetAutoFilter();
					if(readEventSheetAutoFilter != null){
						Map<String,Object> autoFilterMap = readEventSheetAutoFilter.getAutoFilter(cellImportContext.cellReference());
						if(autoFilterMap != null){
							contentMap.putAll(autoFilterMap);
						}
					}
				}
				
				@Override
				public OnException onException() {
					return DoSomethingUtil.OnException.ignore;
				}
				
			},
			new DoSomethingUtil.DoSomething() {
                
                @Override
                public void doIt() {
                    ReadEventSheetControl readEventSheetControl = sheetImportContext.getReadEventSheetControl();
                    if(readEventSheetControl != null){
                        SheetControl sheetControl = readEventSheetControl.getControl(cellImportContext.cellReference().getRow() +1,
                                cellImportContext.cellReference().getCol() + 1);
                        if(sheetControl != null){
                            contentMap.putAll(sheetControl.getSettings());
                        }
                    }
                }
                
                @Override
                public OnException onException() {
                    return DoSomethingUtil.OnException.ignore;
                }
                
            });
			String contentJson = JsonUtil.toJson(contentMap);
			sheetCell.setX(cellImportContext.cellReference().getRow() + 1);
			sheetCell.setY(cellImportContext.cellReference().getCol() + 1);
			sheetCell.setContent(contentJson);
	        /**
	         * start
	         * added by zhousr 2016-2-19 [添加报表3.0新代码]为SheetCell增加3个属性赋值FORMAT_ID，CREATION_DATE，CREATED_BY
	         */
			XSSFReadEventSheet readEventSheet = sheetImportContext.sheet();
			CTSheet sheet = readEventSheet.sheet();
	        SheetTab sheetTab = sheetImportContext.parentContext().getSheetTabById((int) sheet.getSheetId());
			sheetCell.setFORMAT_ID(UUID.randomUUID().toString());
			sheetCell.setCREATION_DATE(sheetTab.getDocumentFile().getCREATION_DATE());
			sheetCell.setCREATED_BY(sheetTab.getDocumentFile().getCREATED_BY());
	        /**
	         * end
	         * added by zhousr 2016-2-19
	         */
			return sheetCell;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}

	}

	private void handleCellValue(ReadEventCellValue readEventCellValue, CellImportContext cellImportContext) {
		int cellType = cellImportContext.getCellType();
		Object cellValue = readEventCellValue.getCellValue();
		Object data = null;
		Object rawData = null;
		Map<String,Object> valueFmtMap = new HashMap<>();
		switch (cellType) {
    		case CellImportContext.CELL_TYPE_STRING:
    			data = SpreadsheetImportHelper.toJavascriptCompatibleString((String)cellValue);
    			break;
    		case CellImportContext.CELL_TYPE_ERROR:
    			data = cellValue;
    			break;
    		case CellImportContext.CELL_TYPE_BOOLEAN:
    			data = cellValue;
    			valueFmtMap.put("fm", "bool");
    			break;
    		case CellImportContext.CELL_TYPE_NUMERIC: {
    			NumericCellValueProcessor numericCellValueProcessor = NumericCellValueProcessor.build((Number) cellValue, cellImportContext);
    			Map<String,Object> rv = numericCellValueProcessor.getCellValue();
    			if (rv != null) {
    				data = rv.remove("data");
    				rawData =rv.remove("rawData");
    			}    			
    			valueFmtMap.putAll(rv);
    			break;
    		}
    		case CellImportContext.CELL_TYPE_FORMULA: {
    			String val = cellImportContext.cell().val();
    			if (val != null && val.length() > 0) {
    				try {
    					double formulaResult = Double.parseDouble(val);
    					NumericCellValueProcessor numericCellValueProcessor = NumericCellValueProcessor.build(formulaResult, cellImportContext);
    					Map<String,Object> rv = numericCellValueProcessor.getCellValue();
    					data = rv.remove("data");
    					rawData =rv.remove("rawData");
    					valueFmtMap.putAll(rv);
    				} catch (NumberFormatException ex) {
    					// eat it
    				}
    			}
    			data =cellValue;
    			valueFmtMap.put("cal", true);
    			break;
    		}
		}
		if(data == null){
		    data = cellValue;
		}
		if(rawData == null){
		    rawData = cellValue;
		}
		cellImportContext.setData(data);
		cellImportContext.setRawData(rawData);
		cellImportContext.getStyleSettings().putAll(valueFmtMap);
		
		
		
	}

	public static CellImportHandler instance() {
		return _instance;
	}
}
