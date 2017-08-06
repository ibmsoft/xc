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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.WorksheetDocument;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.cubedrive.base.utils.XmlUtil;

class WorksheetContentHandler extends DefaultHandler {
    
    private final XMLReader xmlReader;
    
    private StringWriter sheetInfoWriter;
    
    private SheetDataHandler sheetDataHandler;
    
    private TransformerHandler sheetInfoOutput;
    
    private CTWorksheet ctWorksheet;
    
    WorksheetContentHandler(XMLReader xmlReader){
        this.xmlReader = xmlReader;
        
    }
    
    private void init() throws TransformerException, SAXException{
        sheetInfoWriter = new StringWriter();
        Result sheetInfoResult = new StreamResult(sheetInfoWriter);
        sheetInfoOutput = XmlUtil.saxTransformerFactory.newTransformerHandler();
        Transformer transformer = sheetInfoOutput.getTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");// æ�¢è¡Œ
        sheetInfoOutput.setResult(sheetInfoResult);
        sheetInfoOutput.startDocument();
    }
    
    public void  startPrefixMapping(String prefix, String uri) throws SAXException{
        sheetInfoOutput.startPrefixMapping(prefix, uri);
    }
    
    public void endPrefixMapping(String prefix) throws SAXException{
        sheetInfoOutput.endPrefixMapping(prefix);
    }
    
    public void startDocument(){
        try{
           init();
        }catch(TransformerException|SAXException ex){
            throw new RuntimeException(ex);
        }
        try{
            sheetDataHandler = new SheetDataHandler();
            sheetDataHandler.init();
        }catch(TransformerException|SAXException|IOException ex){
            throw new RuntimeException(ex);
        }  
    }
    
    
    public void endDocument(){
        try {
            sheetInfoOutput.endDocument();
            ctWorksheet =  WorksheetDocument.Factory.parse(sheetInfoWriter.toString()).getWorksheet();
            sheetInfoWriter = null;
        } catch (SAXException|XmlException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        
        sheetInfoOutput.startElement(uri, localName, qName, attributes);
        if("sheetData".equals(localName)){
            xmlReader.setContentHandler(sheetDataHandler);
            sheetDataHandler.sheetDataOutput.startElement(uri, localName, qName, attributes);
        }
    }
    
    public void characters(char[] ch, int start, int length) throws SAXException {
        sheetInfoOutput.characters(ch, start, length);
    }
    
    
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        sheetInfoOutput.endElement(uri, localName, qName);
    }
    
    
    CTWorksheet getWorksheet(){
        return ctWorksheet;
    }
    
    
    File getSheetDataFile(){
        return sheetDataHandler.sheetDataFile;
    }
    
    private class SheetDataHandler extends DefaultHandler{
        
        private File sheetDataFile;
        
        private Writer writer;
        
        private int depth = 1;
        
        private TransformerHandler sheetDataOutput;
        
       
        
        SheetDataHandler(){}
        
        private void init()  throws IOException, TransformerException, SAXException{
            sheetDataFile = File.createTempFile("sheet_", ".xml");
            sheetDataFile.deleteOnExit();
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(sheetDataFile),Charset.forName("UTF-8")));
            Result sheetDataResult = new StreamResult(writer);
            sheetDataOutput = XmlUtil.saxTransformerFactory.newTransformerHandler();
            Transformer transformer = sheetDataOutput.getTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");// æ�¢è¡Œ
            sheetDataOutput.setResult(sheetDataResult);
            sheetDataOutput.startDocument();
        }
        
        public void startElement(String uri, String localName, String qName,
                Attributes attributes) throws SAXException {
            depth++;
            sheetDataOutput.startElement(uri, localName, qName, attributes);
        }
        
        public void characters(char[] ch, int start, int length) throws SAXException {
            sheetDataOutput.characters(ch, start, length);
        }
        
        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            depth--;
            sheetDataOutput.endElement(uri, localName,qName);
            if(0 == depth) {
                sheetDataOutput.endDocument();
                try {
					writer.close();
				} catch (IOException e) {
					throw new SAXException(e);
				}
                WorksheetContentHandler.this.xmlReader.setContentHandler(WorksheetContentHandler.this);
                WorksheetContentHandler.this.sheetInfoOutput.endElement(uri, localName, qName);
              
            }
        }
        
    }
    

}
