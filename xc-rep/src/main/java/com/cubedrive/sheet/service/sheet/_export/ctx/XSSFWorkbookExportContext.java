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
package com.cubedrive.sheet.service.sheet._export.ctx;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellProtection;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDxf;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPatternFill;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXf;

import com.cubedrive.sheet.service.sheet._export.SheetExportEnvironment;
import com.cubedrive.sheet.service.sheet._export.SpreadsheetExportHelper;
import com.google.common.base.Strings;


public class XSSFWorkbookExportContext extends SSExportContext {
	

    private final Map<Integer, XSSFCellStyle> _cellStyles = new HashMap<>();
    private final Map<Integer, XSSFFont> _fonts = new HashMap<>();
    private final Map<String, Short> _dataFormats = new HashMap<>();
    private final Map<Integer, Integer> _ctDxfs = new HashMap<>();

    private XSSFWorkbook _xssfWorkbook;

    private SXSSFWorkbook _sxssfWorkbook;

    private XSSFCellStyle _hyperlinkStyle;

    private XSSFCellStyle _nonProtectionStyle;
    
    private int _numOfGraphicFrames = 0;
    
    public XSSFWorkbookExportContext(SheetExportEnvironment sheetExportEnvironment) {
        super(sheetExportEnvironment);
    }

    public void setWorkbook(XSSFWorkbook xssfWorkbook, SXSSFWorkbook sxssfWorkbook) {
        _xssfWorkbook = xssfWorkbook;
        _sxssfWorkbook = sxssfWorkbook;
    }
    

    public XSSFWorkbook xssfWorkbook() {
        return _xssfWorkbook;
    }

    public SXSSFWorkbook sxssfWorkbook() {
        return _sxssfWorkbook;
    }


    public Map<Integer, XSSFFont> fonts() {
        return _fonts;
    }

    public Map<Integer, XSSFCellStyle> cellStyles() {
        return _cellStyles;
    }


    public Map<String, Short> dataFormats() {
        return _dataFormats;
    }

    public XSSFCellStyle getHyperlinkStyle() {
        if (_hyperlinkStyle == null) {
            _hyperlinkStyle = _xssfWorkbook.createCellStyle();
            XSSFFont hlink_font = _xssfWorkbook.createFont();
            hlink_font.setUnderline(Font.U_SINGLE);
            hlink_font.setColor(IndexedColors.BLUE.getIndex());
            _hyperlinkStyle.setFont(hlink_font);
        }
        return _hyperlinkStyle;
    }
    
    
    public XSSFCellStyle getNonProtectionStyle(){
    	if(_nonProtectionStyle == null){
    		_nonProtectionStyle = _xssfWorkbook.createCellStyle();
            CTXf xf = _nonProtectionStyle.getCoreXf();
            xf.setApplyProtection(true);
            CTCellProtection protection = xf.addNewProtection();
            protection.setLocked(false);
    	}
    	return _nonProtectionStyle;
    }
    
    public int getOrCreateDxf(Map<String,Object> dxfMap){
        int dxfMapHashcode  = dxfMap.hashCode();
        Integer dxfId = _ctDxfs.get(dxfMapHashcode);
        if(dxfId == null){
            CTDxf ctDxf = CTDxf.Factory.newInstance();
            String ccolor = (String)dxfMap.get("ccolor");
            if(! Strings.isNullOrEmpty(ccolor)){
                byte[] colorBytes = SpreadsheetExportHelper.colorAsBytes(ccolor);
                if(colorBytes != null){
                    CTColor ctColor = ctDxf.addNewFont().addNewColor();
                    SpreadsheetExportHelper.setCTColorByRgb(ctColor, colorBytes);
                }
            }
            
            String cbgc = (String)dxfMap.get("cbgc");
            if(! Strings.isNullOrEmpty(cbgc)){
                byte[] colorBytes = SpreadsheetExportHelper.colorAsBytes(cbgc);
                if(colorBytes != null){
                    CTPatternFill ctPatternFill = ctDxf.addNewFill().addNewPatternFill();
                    CTColor ctColor = ctPatternFill.addNewBgColor();
                    SpreadsheetExportHelper.setCTColorByRgb(ctColor, colorBytes);
                }
            }
            StylesTable styles = _xssfWorkbook.getStylesSource();
            dxfId = styles.putDxf(ctDxf) - 1;
        }
        return dxfId;
    }
    
    public int getAndIncreaseGraphicFrame(){
        return _numOfGraphicFrames ++;
    }
    
    public XSSFWorkbookExportContext parentContext() {
        return this;
    }


}
