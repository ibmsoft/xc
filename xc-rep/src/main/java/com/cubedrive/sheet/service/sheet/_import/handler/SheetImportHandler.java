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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCol;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCols;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHeaderFooter;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMergeCell;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageMargins;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheet;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet;

import com.cubedrive.base.utils.DoSomethingUtil;
import com.cubedrive.base.utils.DoSomethingUtil.OnException;
import com.cubedrive.base.utils.JsonUtil;
import com.cubedrive.base.utils.Pair;
import com.cubedrive.sheet.domain.sheet.SheetCell;
import com.cubedrive.sheet.domain.sheet.SheetTab;
import com.cubedrive.sheet.domain.sheet.SheetTabElement;
import com.cubedrive.sheet.service.sheet._import.SpreadsheetImportHelper;
import com.cubedrive.sheet.service.sheet._import.ctx.SheetImportContext;
import com.cubedrive.sheet.service.sheet._import.ctx.WorkbookImportContext;
import com.cubedrive.sheet.service.sheet._import.handler.cell.ReadEventCellStyle;
import com.cubedrive.sheet.service.sheet._import.handler.sheet.ReadEventSheetAutoFilter;
import com.cubedrive.sheet.service.sheet._import.handler.sheet.ReadEventSheetChart;
import com.cubedrive.sheet.service.sheet._import.handler.sheet.ReadEventSheetColumnGroup;
import com.cubedrive.sheet.service.sheet._import.handler.sheet.ReadEventSheetConditionalFormatting;
import com.cubedrive.sheet.service.sheet._import.handler.sheet.ReadEventSheetControl;
import com.cubedrive.sheet.service.sheet._import.handler.sheet.ReadEventSheetDataValidations;
import com.cubedrive.sheet.service.sheet._import.handler.sheet.ReadEventSheetExtension;
import com.cubedrive.sheet.service.sheet._import.handler.sheet.ReadEventSheetFormatPreference;
import com.cubedrive.sheet.service.sheet._import.handler.sheet.ReadEventSheetPicture;
import com.cubedrive.sheet.service.sheet._import.handler.sheet.ReadEventSheetRowGroup;
import com.cubedrive.sheet.service.sheet._import.handler.sheet.ReadEventSheetTables;
import com.cubedrive.sheet.service.sheet._import.handler.sheet.ReadEventSheetView;
import com.cubedrive.sheet.service.sheet._import.xssf.XSSFReadEventSheet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class SheetImportHandler {
    
    private static final SheetImportHandler _instance = new SheetImportHandler();
    
    private SheetImportHandler(){
        
    }

    public void startSheet(final SheetImportContext sheetImportContext) {
        DoSomethingUtil.doSomething( 
            new DoSomethingUtil.DoSomething(){
                public void doIt() {
                    loadColumnSetting(sheetImportContext);
                }
                public OnException onException() {
                    return DoSomethingUtil.OnException.ignore;
                }
            },
            new DoSomethingUtil.DoSomething(){
                public void doIt() {
                    loadSheetView(sheetImportContext);
                }
                public OnException onException() {
                    return DoSomethingUtil.OnException.ignore;
                }
                
            },
            new DoSomethingUtil.DoSomething(){
                public void doIt() {
                    loadMergeCells(sheetImportContext);
                }
                public OnException onException() {
                    return DoSomethingUtil.OnException.ignore;
                }
            },
            
            new DoSomethingUtil.DoSomething(){
                public void doIt() {
                    loadTables(sheetImportContext);
                }
                public OnException onException() {
                    return DoSomethingUtil.OnException.ignore;
                }
            },
            new DoSomethingUtil.DoSomething() {
            	
            	public void doIt() {
					loadAutoFilter(sheetImportContext);
				}
				public OnException onException() {
					return DoSomethingUtil.OnException.ignore;
				}
				
			},
			new DoSomethingUtil.DoSomething() {
				
				@Override
				public void doIt() {
					loadAlternateContents(sheetImportContext);
				}
				@Override
				public OnException onException() {
					return DoSomethingUtil.OnException.ignore;
				}
				
			}
            
        );
    }

    public void endSheet(final SheetImportContext sheetImportContext) {
        DoSomethingUtil.doSomething( 
            new DoSomethingUtil.DoSomething(){
                public void doIt() {
                    loadPictures(sheetImportContext);
                }
                public OnException onException() {
                    return DoSomethingUtil.OnException.ignore;
                }
            },
            
            new DoSomethingUtil.DoSomething(){
                public void doIt() {
                    loadConditionalFormattings(sheetImportContext);
                }
                public OnException onException() {
                    return DoSomethingUtil.OnException.ignore;
                }
            },
            
            new DoSomethingUtil.DoSomething(){
                public void doIt() {
                	loadCharts(sheetImportContext);
                }
                public OnException onException() {
                    return DoSomethingUtil.OnException.ignore;
                }
            },
            
            new DoSomethingUtil.DoSomething() {
				@Override
				public void doIt() {
					loadColumnGroups(sheetImportContext);
				}
				
				@Override
				public OnException onException() {
					 return DoSomethingUtil.OnException.ignore;
				}
			},
			
			 new DoSomethingUtil.DoSomething() {
				@Override
				public void doIt() {
					loadRowGroups(sheetImportContext);
				}
				
				@Override
				public OnException onException() {
					 return DoSomethingUtil.OnException.ignore;
				}
			},
			
			new DoSomethingUtil.DoSomething() {
				@Override
				public void doIt() {
					loadExtensions(sheetImportContext);
				}
				@Override
				public OnException onException() {
					return DoSomethingUtil.OnException.ignore;
				}
			},
			
			new DoSomethingUtil.DoSomething() {
				@Override
				public void doIt() {
					loadDataValidations(sheetImportContext);
				}
				@Override
				public OnException onException() {
					return DoSomethingUtil.OnException.ignore;
				}
			},
            new DoSomethingUtil.DoSomething(){

                @Override
                public void doIt() {
                    loadHiddens(sheetImportContext);
                }

                @Override
                public OnException onException() {
                    return DoSomethingUtil.OnException.ignore;
                }
            },
			new DoSomethingUtil.DoSomething() {
				@Override
				public void doIt() {
					addSheetExtraInfo(sheetImportContext);
				}
				@Override
				public OnException onException() {
					return DoSomethingUtil.OnException.ignore;
				}
			}
			
        );
    }

    private void loadSheetView(SheetImportContext sheetImportContext){
        XSSFReadEventSheet readEventSheet = sheetImportContext.sheet();
        CTWorksheet worksheet = readEventSheet.worksheet();
        boolean sheetProtection = worksheet.isSetSheetProtection();
        Map<String,Object> contentMap = new HashMap<>();
        ReadEventSheetView sheetView = ReadEventSheetView.build(sheetImportContext);
        if(!sheetView.getSetting().isEmpty()){
            String viewSetting = JsonUtil.toJson(sheetView.getSetting());
            contentMap.put("config", viewSetting);
        }
        ReadEventSheetFormatPreference sheetFormatPreference = 
                ReadEventSheetFormatPreference.build(sheetImportContext);
        contentMap.put("dw", sheetFormatPreference.getDefaultColWidth());
        contentMap.put("dh", sheetFormatPreference.getDefaultRowHeight());
        Map<String,Object> sheetCellStyleSetting = sheetImportContext.getSheetStyleSetting();
        if(sheetCellStyleSetting != null && !sheetCellStyleSetting.isEmpty()){
        	contentMap.putAll(sheetCellStyleSetting);
        }
        
        
        if(sheetProtection){
        	sheetImportContext.setSheetProtection(true);
        	contentMap.put("dsd", "ed");
        }
        
        
        CTSheet sheet = readEventSheet.sheet();
        SheetTab sheetTab = sheetImportContext.parentContext().getSheetTabById((int) sheet.getSheetId());
        if(sheetFormatPreference.getTabColor()!=null){
        	sheetTab.setColor(sheetFormatPreference.getTabColor());
        	sheetImportContext.sheetImportEnvironment().sheetTabMapper().updateSheetTab(sheetTab);
        }
        SheetCell configCell = new SheetCell();
        configCell.setX(0);
        configCell.setY(0);
        configCell.setCal(false);
        configCell.setContent(JsonUtil.toJson(contentMap));
        /**
         * start
         * added by zhousr 2016-2-19 [添加报表3.0新代码]为SheetCell增加3个属性赋值FORMAT_ID，CREATION_DATE，CREATED_BY
         */
        configCell.setFORMAT_ID(UUID.randomUUID().toString());
        configCell.setCREATION_DATE(sheetTab.getDocumentFile().getCREATION_DATE());
        configCell.setCREATED_BY(sheetTab.getDocumentFile().getCREATED_BY());
        /**
         * end
         * added by zhousr 2016-2-19
         */
        sheetImportContext.sheetImportEnvironment().sheetCellMapper().
            insertSheetCell(sheetTab.getId(), configCell, sheetTab.getCellTable());
        
    }
    

    private void loadColumnSetting(SheetImportContext sheetImportContext) {
        WorkbookImportContext workbookImportContext = sheetImportContext.parentContext();
        XSSFReadEventSheet readEventSheet = sheetImportContext.sheet();
        CTSheet sheet = readEventSheet.sheet();
        CTWorksheet worksheet = readEventSheet.worksheet();
        if(!worksheet.getColsList().isEmpty()){
            SheetTab sheetTab = sheetImportContext.parentContext().getSheetTabById((int) sheet.getSheetId());
            CTCols ctCols = worksheet.getColsList().get(0);
            List<SheetCell> row1Cells = Lists.newArrayList();
            List<CTCol> colList = ctCols.getColList();
            int colListLength = colList.size();
            int lastColIndex = colListLength -1 ;
            for(int i=0; i<colListLength; i++){
            	CTCol ctCol = colList.get(i);
            	if(i == lastColIndex && ctCol.getMax()==16384){
            		Map<String,Object> sheetStyleSetting = getColumnStyleSetting(ctCol,-1,sheetImportContext);
            		sheetImportContext.setSheetStyleSetting(sheetStyleSetting);
            		break;
            	}
                if(ctCol.isSetHidden() && ctCol.getHidden()){
                    sheetImportContext.addHiddenColumns((int)ctCol.getMin(), (int)ctCol.getMax());
                }
                for (int col = (int) ctCol.getMin(); col <= (int)ctCol.getMax(); col++) {
                    Map<String,Object> colSetting = getColumnStyleSetting(ctCol,col,sheetImportContext);
                    
                    if(!colSetting.isEmpty()){
//                        Integer styleId = workbookImportContext.getWorkbookStylesTable().getOrCreateStyle(colSetting);
                        Map<String,Object> contentMap = new HashMap<>();
//                        contentMap.put("style", styleId);
                        contentMap.putAll(colSetting);
                        
                        SheetCell cell = new SheetCell();
                        cell.setX(0);
                        cell.setY(col);
                        cell.setCal(false);
                        cell.setContent(JsonUtil.toJson(contentMap));
                        /**
                         * start
                         * added by zhousr 2016-2-19 [添加报表3.0新代码]为SheetCell增加3个属性赋值FORMAT_ID，CREATION_DATE，CREATED_BY
                         */
                        cell.setFORMAT_ID(UUID.randomUUID().toString());
                        cell.setCREATION_DATE(sheetTab.getDocumentFile().getCREATION_DATE());
                        cell.setCREATED_BY(sheetTab.getDocumentFile().getCREATED_BY());
                        /**
                         * end 
                         * added by zhousr 2016-2-19 
                         */
                        row1Cells.add(cell);
                    }
                    if(row1Cells.size()==50){
                        sheetImportContext.sheetImportEnvironment().sheetCellMapper().
                            batchInsertSheetCell(sheetTab.getId(), row1Cells, sheetTab.getCellTable());
                        row1Cells.clear();
                    }
                    
                }
            }
            if(!row1Cells.isEmpty())
                sheetImportContext.sheetImportEnvironment().sheetCellMapper().
                    batchInsertSheetCell(sheetTab.getId(), row1Cells, sheetTab.getCellTable());
        }
    }
    
    private Map<String,Object> getColumnStyleSetting(CTCol ctCol, int col,SheetImportContext sheetImportContext){
    	 Map<String, Object> colSetting = Maps.newHashMap();
         if(ctCol.isSetWidth()){
             int width = (int)(ctCol.getWidth() * 8);
             if(width != SpreadsheetImportHelper.default_column_width_px){
                 colSetting.put("width",width);
                 colSetting.put("woff", 0);
             }
         }
//         if(ctCol.isSetHidden() && ctCol.getHidden()){
//             colSetting.put("hidden", true);
//         }
         if(ctCol.isSetStyle()){
         	int styleIndx = (int)ctCol.getStyle();
         	XSSFCellStyle style = sheetImportContext.parentContext().workbook().stylesSource().getStyleAt(styleIndx);
         	ReadEventCellStyle readEventCellStyle = ReadEventCellStyle.build(style, -1, sheetImportContext);
      		Map<String, Object> styleSettings = readEventCellStyle.getStyles();
      		if(col == -1){
      			styleSettings.remove("dsd");
      		}
      		
      		String dsd = (String)styleSettings.get("dsd");
      		if(col != -1 && "".equals(dsd)){
      			sheetImportContext.addNoProtectedColumn(col);
      		}
      		
      		if(! styleSettings.isEmpty()){
      			colSetting.putAll(styleSettings);
      		}
         }
         return colSetting;
    }
    

    private void loadMergeCells(SheetImportContext sheetImportContext) {
        XSSFReadEventSheet readEventSheet = sheetImportContext.sheet();
        CTSheet sheet = readEventSheet.sheet();
        CTWorksheet worksheet = readEventSheet.worksheet();
        SheetTab sheetTab = sheetImportContext.parentContext().getSheetTabById((int) sheet.getSheetId());
        if (worksheet.isSetMergeCells()) {
            List<SheetTabElement> mergeCellElements = new ArrayList<>(worksheet.getMergeCells().getMergeCellList().size());
            for (CTMergeCell ctMergeCell : worksheet.getMergeCells().getMergeCellList()) {
                String ref = ctMergeCell.getRef();
                CellRangeAddress rangeAddress = CellRangeAddress.valueOf(ref);
                SheetTabElement mergeCellElement = new SheetTabElement();
                mergeCellElement.setName(sheetTab.getId() + "_" + ref.replace(":", "_"));
                mergeCellElement.setTab(sheetTab);
                mergeCellElement.setEtype("meg");
                mergeCellElement.setContent(JsonUtil.toJson(
                        Lists.newArrayList(
                                rangeAddress.getFirstRow() + 1, rangeAddress.getFirstColumn() + 1,
                                rangeAddress.getLastRow() + 1, rangeAddress.getLastColumn() + 1
                        )));
                /**
                 * start
                 * added by zhousr 2016-2-19 [添加报表3.0新代码]为SheetTabElement增加3个属性赋值ELEMENT_ID，CREATION_DATE，CREATED_BY
                 */
                mergeCellElement.setELEMENT_ID(UUID.randomUUID().toString());
                mergeCellElement.setCREATION_DATE(sheetTab.getDocumentFile().getCREATION_DATE());
                mergeCellElement.setCREATED_BY(sheetTab.getDocumentFile().getCREATED_BY());
                /**
                 * end
                 * added by zhousr 2016-2-19 
                 */
                mergeCellElements.add(mergeCellElement);
            }
            sheetImportContext.sheetImportEnvironment().sheetTabElementMapper().batchInsert(mergeCellElements);
        }

    }
    
    
    private void loadPictures(SheetImportContext sheetImportContext){
        ReadEventSheetPicture sheetPicture = ReadEventSheetPicture.build(sheetImportContext);
        if(!sheetPicture.getPictureSettings().isEmpty()){
            XSSFReadEventSheet readEventSheet = sheetImportContext.sheet();
            CTSheet sheet = readEventSheet.sheet();
            SheetTab sheetTab = sheetImportContext.parentContext().getSheetTabById((int) sheet.getSheetId());
            List<SheetTabElement> pictureElements = new ArrayList<>(sheetPicture.getPictureSettings().size());
            for(Map<String,Object> setting:sheetPicture.getPictureSettings()){
                String uid = UUID.randomUUID().toString();
                setting.put("id", uid);
                setting.put("sheetId", sheetTab.getId());
                
                SheetTabElement pictureElement = new SheetTabElement();
                pictureElement.setName(uid);
                pictureElement.setTab(sheetTab);
                pictureElement.setEtype("floor");
                pictureElement.setContent(JsonUtil.toJson(setting));
                /**
                 * start
                 * added by zhousr 2016-2-19 [添加报表3.0新代码]为SheetTabElement增加3个属性赋值ELEMENT_ID，CREATION_DATE，CREATED_BY
                 */
                pictureElement.setELEMENT_ID(UUID.randomUUID().toString());
                pictureElement.setCREATION_DATE(sheetTab.getDocumentFile().getCREATION_DATE());
                pictureElement.setCREATED_BY(sheetTab.getDocumentFile().getCREATED_BY());
                /**
                 * end
                 * added by zhousr 2016-2-19 
                 */
                pictureElements.add(pictureElement);
            }
            sheetImportContext.sheetImportEnvironment().sheetTabElementMapper().batchInsert(pictureElements);
        }
        
    }
    
    private void loadExtensions(SheetImportContext sheetImportContext){
    	ReadEventSheetExtension sheetExtension = ReadEventSheetExtension.build(sheetImportContext);
    	List<ReadEventSheetExtension.ExtensionObject> extensionList = sheetExtension.getExtensionList();
    	if(!extensionList.isEmpty()){
    		XSSFReadEventSheet readEventSheet = sheetImportContext.sheet();
            CTSheet sheet = readEventSheet.sheet();
            SheetTab sheetTab = sheetImportContext.parentContext().getSheetTabById((int) sheet.getSheetId());
            List<SheetTabElement> extensionElements = new ArrayList<>(extensionList.size());
            for(ReadEventSheetExtension.ExtensionObject extensionObject:extensionList){
            	if(ReadEventSheetExtension.EXTENSION_MINICHART.equals(extensionObject.getName())){
	            	 String uid = UUID.randomUUID().toString();
	            	 Map<String,Object> setting = extensionObject.getExtensionSetting();
	            	 setting.put("id", uid);
	                 
	                 SheetTabElement extensionElement = new SheetTabElement();
	                 extensionElement.setName(uid);
	                 extensionElement.setTab(sheetTab);
	                 extensionElement.setEtype("cdt");
	                 extensionElement.setContent(JsonUtil.toJson(setting));
	                 /**
	                  * start
	                  * added by zhousr 2016-2-19 [添加报表3.0新代码]为SheetTabElement增加3个属性赋值ELEMENT_ID，CREATION_DATE，CREATED_BY
	                  */
	                 extensionElement.setELEMENT_ID(UUID.randomUUID().toString());
	                 extensionElement.setCREATION_DATE(sheetTab.getDocumentFile().getCREATION_DATE());
	                 extensionElement.setCREATED_BY(sheetTab.getDocumentFile().getCREATED_BY());
	                 /**
	                  * end
	                  * added by zhousr 2016-2-19 
	                  */
	                 extensionElements.add(extensionElement);
            	}
            }
            sheetImportContext.sheetImportEnvironment().sheetTabElementMapper().batchInsert(extensionElements);
            
    	}
    	
    }
    
    private void loadDataValidations(SheetImportContext sheetImportContext){
    	ReadEventSheetDataValidations readEventSheetDataValidations= ReadEventSheetDataValidations.build(sheetImportContext);
    	List<Map<String,Object>> dataValidationSettings = readEventSheetDataValidations.getValidationSettings();
    	if(!dataValidationSettings.isEmpty()){
    		XSSFReadEventSheet readEventSheet = sheetImportContext.sheet();
            CTSheet sheet = readEventSheet.sheet();
            SheetTab sheetTab = sheetImportContext.parentContext().getSheetTabById((int) sheet.getSheetId());
            List<SheetTabElement> extensionElements = new ArrayList<>(dataValidationSettings.size());
            for(Map<String,Object> dataValidationSetting:dataValidationSettings){
            	 String uid = UUID.randomUUID().toString();
            	 dataValidationSetting.put("id", uid);
                 SheetTabElement dataValidationElement = new SheetTabElement();
                 dataValidationElement.setName(uid);
                 dataValidationElement.setTab(sheetTab);
                 dataValidationElement.setEtype("cdt");
                 dataValidationElement.setContent(JsonUtil.toJson(dataValidationSetting));
                 /**
                  * start
                  * added by zhousr 2016-2-19 [添加报表3.0新代码]为SheetTabElement增加3个属性赋值ELEMENT_ID，CREATION_DATE，CREATED_BY
                  */
                 dataValidationElement.setELEMENT_ID(UUID.randomUUID().toString());
                 dataValidationElement.setCREATION_DATE(sheetTab.getDocumentFile().getCREATION_DATE());
                 dataValidationElement.setCREATED_BY(sheetTab.getDocumentFile().getCREATED_BY());
                 /**
                  * end
                  * added by zhousr 2016-2-19 
                  */
                 extensionElements.add(dataValidationElement);
            }
            sheetImportContext.sheetImportEnvironment().sheetTabElementMapper().batchInsert(extensionElements);
    	}
    }
    
    
    private void loadTables(SheetImportContext sheetImportContext){
        ReadEventSheetTables readEventSheetTables = ReadEventSheetTables.build(sheetImportContext);
        sheetImportContext.setReadEventSheetTables(readEventSheetTables);
    }
    
    
    private void loadAutoFilter(SheetImportContext sheetImportContext){
    	ReadEventSheetAutoFilter readEventSheetAutoFilter = ReadEventSheetAutoFilter.build(sheetImportContext);
    	sheetImportContext.setReadEventSheetAutoFilter(readEventSheetAutoFilter);
    }
    
    private void loadConditionalFormattings(SheetImportContext sheetImportContext){
        ReadEventSheetConditionalFormatting conditionalFormatting = ReadEventSheetConditionalFormatting.build(sheetImportContext);
        if(!conditionalFormatting.getCdtObjectList().isEmpty()){
            XSSFReadEventSheet readEventSheet = sheetImportContext.sheet();
            CTSheet sheet = readEventSheet.sheet();
            SheetTab sheetTab = sheetImportContext.parentContext().getSheetTabById((int) sheet.getSheetId());
            List<SheetTabElement> cdtElements = new ArrayList<>(conditionalFormatting.getCdtObjectList().size());
            for(Map<String,Object> cdtObject:conditionalFormatting.getCdtObjectList()){
                String uid = UUID.randomUUID().toString();
                cdtObject.put("id", uid);
                
                SheetTabElement cdtElement = new SheetTabElement();
                cdtElement.setName(uid);
                cdtElement.setTab(sheetTab);
                cdtElement.setEtype("cdt");
                cdtElement.setContent(JsonUtil.toJson(cdtObject));
                /**
                 * start
                 * added by zhousr 2016-2-19 [添加报表3.0新代码]为SheetTabElement增加3个属性赋值ELEMENT_ID，CREATION_DATE，CREATED_BY
                 */
                cdtElement.setELEMENT_ID(UUID.randomUUID().toString());
                cdtElement.setCREATION_DATE(sheetTab.getDocumentFile().getCREATION_DATE());
                cdtElement.setCREATED_BY(sheetTab.getDocumentFile().getCREATED_BY());
                /**
                 * end
                 * added by zhousr 2016-2-19 
                 */
                cdtElements.add(cdtElement);
            }
            sheetImportContext.sheetImportEnvironment().sheetTabElementMapper().batchInsert(cdtElements);
            
        }
    }
    
    private void loadCharts(SheetImportContext sheetImportContext){
    	ReadEventSheetChart sheetChart = ReadEventSheetChart.build(sheetImportContext);
    	if(! sheetChart.getChartSettings().isEmpty()){
    		XSSFReadEventSheet readEventSheet = sheetImportContext.sheet();
    		CTSheet sheet = readEventSheet.sheet();
    		SheetTab sheetTab = sheetImportContext.parentContext().getSheetTabById((int) sheet.getSheetId());
    		List<SheetTabElement> chartElements = new ArrayList<>(sheetChart.getChartSettings().size());
    		for(Map<String,Object> chartSetting: sheetChart.getChartSettings()){
    			String uid = UUID.randomUUID().toString();
    			chartSetting.put("id", uid);
    			chartSetting.put("sheetId", sheetTab.getId());
                
                SheetTabElement chartElement = new SheetTabElement();
                chartElement.setName(uid);
                chartElement.setTab(sheetTab);
                chartElement.setEtype("floor");
                chartElement.setContent(JsonUtil.toJson(chartSetting));
                /**
                 * start
                 * added by zhousr 2016-2-19 [添加报表3.0新代码]为SheetTabElement增加3个属性赋值ELEMENT_ID，CREATION_DATE，CREATED_BY
                 */
                chartElement.setELEMENT_ID(UUID.randomUUID().toString());
                chartElement.setCREATION_DATE(sheetTab.getDocumentFile().getCREATION_DATE());
                chartElement.setCREATED_BY(sheetTab.getDocumentFile().getCREATED_BY());
                /**
                 * end
                 * added by zhousr 2016-2-19
                 */
                chartElements.add(chartElement);
    		}
    		sheetImportContext.sheetImportEnvironment().sheetTabElementMapper().batchInsert(chartElements);
    	}
    }
    
    private void loadColumnGroups(SheetImportContext sheetImportContext){
    	ReadEventSheetColumnGroup readEventSheetColumnGroup = ReadEventSheetColumnGroup.build(sheetImportContext);
    	List<Map<String,Object>> columnGroups = readEventSheetColumnGroup.getGroups();
    	if(!columnGroups.isEmpty()){
    		XSSFReadEventSheet readEventSheet = sheetImportContext.sheet();
    		CTSheet sheet = readEventSheet.sheet();
    		SheetTab sheetTab = sheetImportContext.parentContext().getSheetTabById((int) sheet.getSheetId());
    		SheetTabElement columnGroupElement = new SheetTabElement();
    		columnGroupElement.setTab(sheetTab);
    		columnGroupElement.setName("colGroups");
    		columnGroupElement.setEtype("colgroup");
    		columnGroupElement.setContent(JsonUtil.toJson(columnGroups));
            /**
             * start
             * added by zhousr 2016-2-19 [添加报表3.0新代码]为SheetTabElement增加3个属性赋值ELEMENT_ID，CREATION_DATE，CREATED_BY
             */
    		columnGroupElement.setELEMENT_ID(UUID.randomUUID().toString());
    		columnGroupElement.setCREATION_DATE(sheetTab.getDocumentFile().getCREATION_DATE());
    		columnGroupElement.setCREATED_BY(sheetTab.getDocumentFile().getCREATED_BY());
            /**
             * end
             * added by zhousr 2016-2-19
             */
    		sheetImportContext.sheetImportEnvironment().sheetTabElementMapper().insert(columnGroupElement);
    	}
    }
    
    private void loadRowGroups(SheetImportContext sheetImportContext){
    	ReadEventSheetRowGroup readEventSheetRowGroup = sheetImportContext.getReadEventSheetRowGroup();
    	List<Map<String,Object>> rowGroups = readEventSheetRowGroup.getGroups();
    	if(! rowGroups.isEmpty()){
    		XSSFReadEventSheet readEventSheet = sheetImportContext.sheet();
    		CTSheet sheet = readEventSheet.sheet();
    		SheetTab sheetTab = sheetImportContext.parentContext().getSheetTabById((int) sheet.getSheetId());
    		SheetTabElement rowGroupElement = new SheetTabElement();
    		rowGroupElement.setTab(sheetTab);
    		rowGroupElement.setName("rowGroups");
    		rowGroupElement.setEtype("rowgroup");
    		rowGroupElement.setContent(JsonUtil.toJson(rowGroups));
            /**
             * start
             * added by zhousr 2016-2-19 [添加报表3.0新代码]为SheetTabElement增加3个属性赋值ELEMENT_ID，CREATION_DATE，CREATED_BY
             */
    		rowGroupElement.setELEMENT_ID(UUID.randomUUID().toString());
    		rowGroupElement.setCREATION_DATE(sheetTab.getDocumentFile().getCREATION_DATE());
    		rowGroupElement.setCREATED_BY(sheetTab.getDocumentFile().getCREATED_BY());
            /**
             * end
             * added by zhousr 2016-2-19 
             */
    		sheetImportContext.sheetImportEnvironment().sheetTabElementMapper().insert(rowGroupElement);
    	}
    }
    
    private void addSheetExtraInfo(SheetImportContext sheetImportContext){
    	Map<String,Object> extraInfoSetting = new HashMap<>();
    	if(sheetImportContext.isSheetProtection()){
    		extraInfoSetting.put("status", "protected");
    	}
    	
    	CTWorksheet ctWorksheet= sheetImportContext.sheet().worksheet();
    	Map<String,Object> printSetting = new HashMap<>();
    	if(ctWorksheet.isSetPageMargins()){
    	    CTPageMargins ctPageMargins = ctWorksheet.getPageMargins();
    	    Map<String,Double> pageMarginsSettings = new HashMap<>();
    	    pageMarginsSettings.put("bottom", ctPageMargins.getBottom());
    	    pageMarginsSettings.put("footer", ctPageMargins.getFooter());
    	    pageMarginsSettings.put("header", ctPageMargins.getHeader());
    	    pageMarginsSettings.put("left", ctPageMargins.getLeft());
    	    pageMarginsSettings.put("right", ctPageMargins.getRight());
    	    pageMarginsSettings.put("top", ctPageMargins.getTop());
    	    printSetting.put("pageMargins", pageMarginsSettings);
    	}
    	
    	if(ctWorksheet.isSetPageSetup()){
    	    CTPageSetup ctPageSetup = ctWorksheet.getPageSetup();
    	    Map<String,Object> pageSetupSettings = new HashMap<>();
    	    if(ctPageSetup.isSetOrientation()){
    	        pageSetupSettings.put("orientation", ctPageSetup.getOrientation().toString());
    	    }
    	    if(ctPageSetup.isSetPageOrder()){
    	        pageSetupSettings.put("pageOrder", ctPageSetup.getPageOrder().toString());
    	    }
    	    
    	    if(ctPageSetup.isSetPaperSize()){
    	        pageSetupSettings.put("paperSize", ctPageSetup.getPaperSize());
    	    }
    	    if(ctPageSetup.isSetFitToHeight()){
    	        pageSetupSettings.put("fitToHeight", ctPageSetup.getFitToHeight());
    	    }
    	    if(ctPageSetup.isSetFitToWidth()){
    	        pageSetupSettings.put("fitToWidth", ctPageSetup.getFitToWidth());
    	    }
    	    if(ctPageSetup.isSetScale()){
    	        pageSetupSettings.put("scale", ctPageSetup.getScale());
    	    }
    	    if(ctPageSetup.isSetUseFirstPageNumber()){
    	        pageSetupSettings.put("useFirstPageNumber", ctPageSetup.getUseFirstPageNumber());
    	    }
    	    if(ctPageSetup.isSetUsePrinterDefaults()){
    	        pageSetupSettings.put("usePrinterDefaults", ctPageSetup.getUsePrinterDefaults());
    	    }
    	    if(ctPageSetup.isSetBlackAndWhite()){
    	        pageSetupSettings.put("blackAndWhite", ctPageSetup.getBlackAndWhite());
    	    }
    	    if(ctPageSetup.isSetCellComments()){
    	        pageSetupSettings.put("cellComments", ctPageSetup.getCellComments().toString());
    	    }
    	    printSetting.put("pageSetup", pageSetupSettings);
    	    
    	}
    	
    	if(ctWorksheet.isSetHeaderFooter()){
    	    CTHeaderFooter ctHeaderFooter = ctWorksheet.getHeaderFooter();
    	    Map<String,Object> headerFooterSettings = new HashMap<>();
    	    if(ctHeaderFooter.isSetFirstHeader()){
    	        headerFooterSettings.put("firstHeader", ctHeaderFooter.getFirstHeader());
    	    }
    	    if(ctHeaderFooter.isSetFirstFooter()){
    	        headerFooterSettings.put("firstFooter", ctHeaderFooter.getFirstFooter());
    	    }
    	    if(ctHeaderFooter.isSetOddHeader()){
    	        headerFooterSettings.put("oddHeader", ctHeaderFooter.getOddHeader());
    	    }
    	    if(ctHeaderFooter.isSetOddFooter()){
    	        headerFooterSettings.put("oddFooter", ctHeaderFooter.getOddFooter());
    	    }
    	    if(ctHeaderFooter.isSetEvenHeader()){
    	        headerFooterSettings.put("eventHeader", ctHeaderFooter.getEvenHeader());
    	    }
    	    if(ctHeaderFooter.isSetEvenFooter()){
    	        headerFooterSettings.put("evenFooter", ctHeaderFooter.getEvenFooter());
    	    }
    	    if(ctHeaderFooter.isSetScaleWithDoc()){
    	        headerFooterSettings.put("scaleWithDoc", ctHeaderFooter.getScaleWithDoc());
    	    }
    	    if(ctHeaderFooter.isSetDifferentFirst()){
    	        headerFooterSettings.put("differentFirst", ctHeaderFooter.getDifferentFirst());
    	    }
    	    if(ctHeaderFooter.isSetDifferentOddEven()){
    	        headerFooterSettings.put("differentOddEven", ctHeaderFooter.getDifferentOddEven());
    	    }
    	    if(ctHeaderFooter.isSetAlignWithMargins()){
    	        headerFooterSettings.put("alignWithMargins", ctHeaderFooter.getAlignWithMargins());
    	    }
    	    
    	    printSetting.put("headerFooter", headerFooterSettings);
    	}
    	
    	if(!printSetting.isEmpty()){
    	    extraInfoSetting.put("printSetting", printSetting);
    	}
    	
    	if(!extraInfoSetting.isEmpty()){
    		String extraInfo = JsonUtil.toJson(extraInfoSetting);
    		/**
    		 * start
    		 * zhousr 2016-2-19 [注释ES原代码]注释掉ES向数据库修改sheetTab的方法调用，报表3.0没有页签表
    		 */
    		//sheetImportContext.sheetImportEnvironment().sheetTabMapper().addTabExtraInfo(sheetImportContext.getTabId(), extraInfo);
    		/**
    		 * end
    		 * zhousr 2016-2-19 
    		 */
    	}
    }
    
    private void loadAlternateContents(SheetImportContext sheetImportContext){
    	ReadEventSheetControl readEventSheetControl = ReadEventSheetControl.build(sheetImportContext);
    	sheetImportContext.setReadEventSheetControl(readEventSheetControl);
    }


    private void loadHiddens(SheetImportContext sheetImportContext){
        XSSFReadEventSheet readEventSheet = sheetImportContext.sheet();
        CTSheet sheet = readEventSheet.sheet();
        SheetTab sheetTab = sheetImportContext.parentContext().getSheetTabById((int) sheet.getSheetId());
        List<Pair<Integer,Integer>> rowHiddenRanges = sheetImportContext.getRowHiddenRanges();
        if(!rowHiddenRanges.isEmpty()){
            SheetTabElement rowHiddensElement = new SheetTabElement();
            rowHiddensElement.setTab(sheetTab);
            rowHiddensElement.setName("rowHiddens");
            rowHiddensElement.setEtype("row");
            rowHiddensElement.setContent(JsonUtil.toJson(rowHiddenRanges));
            /**
             * start
             * added by zhousr 2016-2-19 [添加报表3.0新代码]为SheetTabElement增加3个属性赋值ELEMENT_ID，CREATION_DATE，CREATED_BY
             */
            rowHiddensElement.setELEMENT_ID(UUID.randomUUID().toString());
            rowHiddensElement.setCREATION_DATE(sheetTab.getDocumentFile().getCREATION_DATE());
            rowHiddensElement.setCREATED_BY(sheetTab.getDocumentFile().getCREATED_BY());
            /**
             * end
             * added by zhousr 2016-2-19 
             */
            sheetImportContext.sheetImportEnvironment().sheetTabElementMapper().insert(rowHiddensElement);
        }
        List<Pair<Integer, Integer>> colHiddenRanges = sheetImportContext.getColumnHiddenRanges();
        if(!colHiddenRanges.isEmpty()){
            SheetTabElement colHiddensElement = new SheetTabElement();
            colHiddensElement.setTab(sheetTab);
            colHiddensElement.setName("colHiddens");
            colHiddensElement.setEtype("col");
            colHiddensElement.setContent(JsonUtil.toJson(colHiddenRanges));
            /**
             * start
             * added by zhousr 2016-2-19 [添加报表3.0新代码]为SheetTabElement增加3个属性赋值ELEMENT_ID，CREATION_DATE，CREATED_BY
             */
            colHiddensElement.setELEMENT_ID(UUID.randomUUID().toString());
            colHiddensElement.setCREATION_DATE(sheetTab.getDocumentFile().getCREATION_DATE());
            colHiddensElement.setCREATED_BY(sheetTab.getDocumentFile().getCREATED_BY());
            /**
             * end
             * added by zhousr 2016-2-19 
             */
            sheetImportContext.sheetImportEnvironment().sheetTabElementMapper().insert(colHiddensElement);
        }
    }
    
    
    
    public static SheetImportHandler instance(){
        return _instance;
    }
}
