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
package com.cubedrive.sheet.service.sheet._import.xssf;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.POIXMLException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.openxml4j.opc.PackageRelationship;
import org.apache.poi.openxml4j.opc.PackageRelationshipCollection;
import org.apache.poi.xssf.model.CommentsTable;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFTable;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDrawing;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHyperlink;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTLegacyDrawing;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheet;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.cubedrive.base.utils.XmlUtil;

public class XSSFReadEventSheet extends POIXMLDocumentPart {

    protected CTSheet sheet;

    protected XSSFReadEventListener readEventListener;

    private CTWorksheet worksheet;

    private CommentsTable sheetComments ;

    private TreeMap<String, XSSFTable> tables = new TreeMap<>();;

    private List<XSSFHyperlink> hyperlinks;

    private File sheetDataFile;


    public XSSFReadEventSheet(PackagePart part, PackageRelationship rel) {
        super(part, rel);
    }
    
    protected CTDrawing getCTDrawing() {
        return worksheet.getDrawing();
     }
     protected CTLegacyDrawing getCTLegacyDrawing() {
        return worksheet.getLegacyDrawing();
     }
    
    
    public CTSheet sheet(){
        return this.sheet;
    }
    
    public CTWorksheet worksheet(){
        return this.worksheet;
    }
    
    public CommentsTable sheetComments(){
        return this.sheetComments;
    }
    
    public TreeMap<String,XSSFTable> tables(){
        return this.tables;
    }
    
    public List<XSSFHyperlink> hyperlinks(){
        return this.hyperlinks;
    }
    
    public XSSFDrawing drawing() {
        XSSFDrawing drawing = null;
        CTDrawing ctDrawing = getCTDrawing();
        for(POIXMLDocumentPart p : getRelations()){
            if(p instanceof XSSFDrawing) {
                XSSFDrawing dr = (XSSFDrawing)p;
                String drId = dr.getPackageRelationship().getId();
                if(drId.equals(ctDrawing.getId())){
                    drawing = dr;
                    break;
                }
                break;
            }
        }
        return drawing;
    }
    
    public XSSFReadEventVMLDrawing vmlDrawing(){
    	XSSFReadEventVMLDrawing vmlDrawing = null;
    	for(POIXMLDocumentPart p : getRelations()){
    		if(p instanceof XSSFReadEventVMLDrawing){
    			vmlDrawing = (XSSFReadEventVMLDrawing)p;
    			break;
    		}
    	}
    	return vmlDrawing;
    }


    @Override
    protected void onDocumentRead() {
        try {
            read(getPackagePart().getInputStream());
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new POIXMLException(e);
        }


    }

    protected void read(InputStream in) throws IOException, ParserConfigurationException, SAXException {
        parseSheetDocument(in);
        for (POIXMLDocumentPart p : getRelations()) {
            if (p instanceof CommentsTable) {
                sheetComments = (CommentsTable) p;
                break;
            }
            if (p instanceof XSSFTable) {
                tables.put(p.getPackageRelationship().getId(), (XSSFTable) p);
            }
        }
        initHyperlinks();
        readEventListener.onStartSheet(this);
        parseSheetData();
        readEventListener.onEndSheet(this);
    }

    private void initHyperlinks() {
        hyperlinks = new ArrayList<>();
        if (!worksheet.isSetHyperlinks()) return;
        try {
            PackageRelationshipCollection hyperRels =
                    getPackagePart().getRelationshipsByType(XSSFReadEventRelation.SHEET_HYPERLINKS.getRelation());

            
            for (CTHyperlink hyperlink : worksheet.getHyperlinks().getHyperlinkList()) {
                PackageRelationship hyperRel = null;
                if (hyperlink.getId() != null) {
                    hyperRel = hyperRels.getRelationshipByID(hyperlink.getId());
                }

                hyperlinks.add(new XSSFHyperlink(hyperlink, hyperRel));
            }
        } catch (InvalidFormatException e) {
            throw new POIXMLException(e);
        }

    }

    private void parseSheetDocument(InputStream in) throws ParserConfigurationException, SAXException, IOException {
        SAXParser sp = XmlUtil.saxNamespaceAwareParserFactory.newSAXParser();
        XMLReader xr = sp.getXMLReader();
        WorksheetContentHandler worksheetHandler = new WorksheetContentHandler(xr);
        xr.setContentHandler(worksheetHandler);
        xr.parse(new InputSource(in));
        worksheet = worksheetHandler.getWorksheet();
        sheetDataFile = worksheetHandler.getSheetDataFile();
    }

    private void parseSheetData() throws ParserConfigurationException, SAXException, IOException {
        SAXParser sp = XmlUtil.saxParserFactory.newSAXParser();
        XMLReader xr = sp.getXMLReader();
        SheetDataContentHandler sheetDataContentHandler = new SheetDataContentHandler(readEventListener);
        xr.setContentHandler(sheetDataContentHandler);
        try (InputStream sheetDataIn = new BufferedInputStream(new FileInputStream(sheetDataFile))) {
            xr.parse(new InputSource(sheetDataIn));
        }


    }


}
