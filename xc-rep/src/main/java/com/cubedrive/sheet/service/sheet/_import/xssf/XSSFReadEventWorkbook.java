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

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.POIXMLException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.openxml4j.opc.PackagePartName;
import org.apache.poi.openxml4j.opc.PackagingURIHelper;
import org.apache.poi.util.DocumentHelper;
import org.apache.poi.util.PackageHelper;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.ThemesTable;
import org.apache.poi.xssf.usermodel.helpers.XSSFIndexedColorsHelper;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDefinedName;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheet;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.WorkbookDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XSSFReadEventWorkbook extends POIXMLDocument {
    
    private String application;
    
    private String appVersion;
    
    private CTWorkbook workbook;

    private List<CTDefinedName> namedRanges;

    private SharedStringsTable sharedStringSource;

    private XSSFReadEventStylesTable stylesSource;

    private XSSFReadEventThemesTable theme;

    private final XSSFReadEventListener readEventListener;

    public XSSFReadEventWorkbook(OPCPackage pkg, XSSFReadEventListener readEventListener) throws IOException {
        super(pkg);
        readExtendedProperties(pkg);
        load(XSSFReadEventFactory.getInstance());
        this.readEventListener = readEventListener;
    }

    public XSSFReadEventWorkbook(InputStream is, XSSFReadEventListener readEventListener) throws IOException {
        this(PackageHelper.open(is), readEventListener);
    }
    
    
    public CTWorkbook workbook(){
        return this.workbook;
    }
    
    public List<CTDefinedName> namedRanges(){
        return this.namedRanges;
    }
    
    public XSSFReadEventStylesTable stylesSource(){
        return this.stylesSource;
    }
    
    public SharedStringsTable sharedStringSource(){
        return this.sharedStringSource;
    }
    
    public ThemesTable theme(){
        return this.theme;
    }
    
    public String application(){
        return this.application;
    }

    public String appVersion(){
        return this.appVersion;
    }
    
    public void readDocument() throws IOException {
        try {
            WorkbookDocument doc = WorkbookDocument.Factory.parse(getPackagePart().getInputStream());
            this.workbook = doc.getWorkbook();

            Map<String, XSSFReadEventSheet> shIdMap = new HashMap<String, XSSFReadEventSheet>();
            for (POIXMLDocumentPart p : getRelations()) {
                if (p instanceof SharedStringsTable) sharedStringSource = (SharedStringsTable) p;
                else if (p instanceof XSSFReadEventStylesTable) stylesSource = (XSSFReadEventStylesTable) p;
                else if (p instanceof XSSFReadEventThemesTable) theme = (XSSFReadEventThemesTable) p;
                else if (p instanceof XSSFReadEventSheet) {
                    shIdMap.put(p.getPackageRelationship().getId(), (XSSFReadEventSheet) p);
                }
            }
            stylesSource.setTheme(theme);
            XSSFIndexedColorsHelper.register(stylesSource.getIndexedColors());

            // process the named ranges
            namedRanges = new ArrayList<>();
            if (workbook.isSetDefinedNames()) {
                for (CTDefinedName ctName : workbook.getDefinedNames().getDefinedNameList()) {
                    namedRanges.add(ctName);
                }
            }
            readEventListener.onStartWorkbook(this);

            // process the sheets
            for (CTSheet ctSheet : this.workbook.getSheets().getSheetList()) {
                XSSFReadEventSheet sh = shIdMap.remove(ctSheet.getId());
                if (sh == null) {
                    continue;
                }
                sh.sheet = ctSheet;
                sh.readEventListener = readEventListener;
                sh.onDocumentRead();
            }
            readEventListener.onEndWorkbook(this);

        } catch (XmlException e) {
            throw new POIXMLException(e);
        }
    }

    @Override
    public List<PackagePart> getAllEmbedds() throws OpenXML4JException {
        throw new UnsupportedOperationException();
    }
    
    private void readExtendedProperties(OPCPackage pkg){
        try{
            PackagePartName extendedPropertiesPartName= 
                    PackagingURIHelper.createPartName(URI.create("/docProps/app.xml"));
            PackagePart extendedPropertiesPart = pkg.getPart(extendedPropertiesPartName);
            try(InputStream in =extendedPropertiesPart.getInputStream()){
                Document xmlDoc = DocumentHelper.readDocument(in);
                application = readElement(xmlDoc,"Application","http://schemas.openxmlformats.org/officeDocument/2006/extended-properties");
                appVersion = readElement(xmlDoc,"AppVersion","http://schemas.openxmlformats.org/officeDocument/2006/extended-properties");
            }
        }catch(Exception ex){
            
        }
    }
    
    /**
     * If you have problem in here (The method getTextContent() is undefined for the type Element):
     *      el.getTextContent(). 
     * 
     * Please go to your Eclipse project properties | Java Build Path | Order and Export tab, 
     * selected the JRE (which was at the bottom of the list) and clicked the "Top" button to move it to the top.
     * 
     * @param xmlDoc
     * @param localName
     * @param namespaceURI
     * @return
     */
    private String readElement(Document xmlDoc, String localName, String namespaceURI) {
        Element el = (Element)xmlDoc.getDocumentElement().getElementsByTagNameNS(namespaceURI, localName).item(0);
        if (el == null) {
            return null;
        }
        
        // see above comments if you have problem to compile this 
        // (The method getTextContent() is undefined for the type Element)
        return el.getTextContent();
    }
    
    public void close() throws IOException{
    	XSSFIndexedColorsHelper.clear();
        getPackage().revert();
    }

}
