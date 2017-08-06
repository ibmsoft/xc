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
package com.cubedrive.sheet.service.sheet._export;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.cubedrive.base.domain.document.DocumentConfig;
import com.cubedrive.base.utils.DoSomethingUtil;
import com.cubedrive.base.utils.DoSomethingUtil.OnException;
import com.cubedrive.base.utils.JsonUtil;
import com.cubedrive.sheet.domain.sheet.SheetCell;
import com.cubedrive.sheet.domain.sheet.SheetTab;
import com.cubedrive.sheet.domain.sheet.SheetTabElement;
import com.cubedrive.sheet.service.sheet._export.ctx.XSSFCellExportContext;
import com.cubedrive.sheet.service.sheet._export.ctx.XSSFWorkbookExportContext;
import com.cubedrive.sheet.service.sheet._export.ctx.XSSFWorksheetExportContext;
import com.cubedrive.sheet.service.sheet._export.handler.CellExportHandler;
import com.cubedrive.sheet.service.sheet._export.handler.WorkbookExportHandler;
import com.cubedrive.sheet.service.sheet._export.handler.WorksheetExportHandler;

public class DefaultXSSFWriteEventListener implements XSSFWriteEventListener{
	
	private final SheetExportEnvironment sheetExportEnvironment;
	
	private XSSFWorkbookExportContext  workbookExportContext;
	
	private XSSFWorksheetExportContext worksheetExportContext;
	
	private int sheetSeq = 0;
	
	public DefaultXSSFWriteEventListener(SheetExportEnvironment sheetExportEnvironment){
		this.sheetExportEnvironment = sheetExportEnvironment;
	}

	@Override
	public void onStartWorkbook() {
		workbookExportContext = new XSSFWorkbookExportContext(sheetExportEnvironment);
		workbookExportContext.init();
		WorkbookExportHandler.instance().startWorkbook(workbookExportContext);
	}

	@Override
	public void onWorkbookElement(final DocumentConfig documentConfig) {
	    DoSomethingUtil.doSomething(new DoSomethingUtil.DoSomething() {
            public void doIt() {
                WorkbookExportHandler.instance().addName(documentConfig, workbookExportContext);
            }
            public OnException onException() {
                return DoSomethingUtil.OnException.ignore;
            }
        });
	    
	}

	@Override
	public void onEndWorkbook() {
	    WorkbookExportHandler.instance().endWorkbook(workbookExportContext);
	}

	@Override
	public void onStartSheet(SheetTab sheetTab) {
		worksheetExportContext = new XSSFWorksheetExportContext(sheetExportEnvironment,
			workbookExportContext,sheetTab, sheetSeq);
		worksheetExportContext.init();
		sheetSeq ++;
		WorksheetExportHandler.instance().startWorksheet(worksheetExportContext);
	}

	@Override
	public void onSheetCell(final SheetCell sheetCell) {
		DoSomethingUtil.doSomething(new DoSomethingUtil.DoSomething() {
			public void doIt() {
				Map<String,Object> contentMap = JsonUtil.getJsonObj(sheetCell.getContent());
				XSSFCellExportContext xssfCellExportContext = new XSSFCellExportContext(
						sheetExportEnvironment,worksheetExportContext,sheetCell,contentMap);
				xssfCellExportContext.init();
				CellExportHandler.instance().handleCell(xssfCellExportContext);
			}
			public OnException onException() {
				return DoSomethingUtil.OnException.ignore;
			}
		});
	}
	
	public void afterIterateSheetCell(){
		DoSomethingUtil.doSomething(new DoSomethingUtil.DoSomething() {
			public void doIt() {
				WorksheetExportHandler.instance().postProcessIterateSheetCell(worksheetExportContext);
			}
			public OnException onException() {
				return DoSomethingUtil.OnException.ignore;
			}
		});
	}

	@Override
	public void onSheetTabElement(final SheetTabElement sheetTabElement) {
		DoSomethingUtil.doSomething(new DoSomethingUtil.DoSomething() {
			public void doIt() {
				WorksheetExportHandler.instance().addSheetElement(sheetTabElement, worksheetExportContext);
			}
			
			public OnException onException() {
				return DoSomethingUtil.OnException.ignore;
			}
		});
	}
	
	public void afterIterateSheetTabElement(){
		DoSomethingUtil.doSomething(new DoSomethingUtil.DoSomething() {
			public void doIt() {
				WorksheetExportHandler.instance().postProcessIterateSheetTabElement(worksheetExportContext);
				
			}
			
			public OnException onException() {
				return DoSomethingUtil.OnException.ignore;
			}
		});
	    
	}

	@Override
	public void onEndSheet() {
	    
	    DoSomethingUtil.doSomething(new DoSomethingUtil.DoSomething() {
			public void doIt() {
				WorksheetExportHandler.instance().endWorksheet(worksheetExportContext);
			}
			
			public OnException onException() {
				return DoSomethingUtil.OnException.ignore;
			}
		});
		
	}
	
	public void writeOutputStream(OutputStream outputStream) throws IOException{
	    SXSSFWorkbook sxssfWorkbook = workbookExportContext.sxssfWorkbook();
	    sxssfWorkbook.write(outputStream);
	    sxssfWorkbook.close();
	}

	
}
