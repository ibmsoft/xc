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

import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

class SheetDataContentHandler extends DefaultHandler {


    private final Stack<String> elements = new Stack<>();

    private XSSFReadEventRow row;

    private XSSFReadEventCell cell;

//    private final List<XSSFReadEventCell> rowCells = new ArrayList<>();
    
    private final XSSFReadEventListener readEventListener;


    SheetDataContentHandler(XSSFReadEventListener readEventListener) {
        this.readEventListener = readEventListener;
    }

    public void startElement(String uri, String localName,
                             String qName, Attributes attributes) {
        switch (qName) {
            case "row": {
                elements.push("row");
                row = XSSFReadEventRow.build(attributes);
                try {
                    readEventListener.onStartRow(row);
                } catch (XSSFReadEventSheetDataStopParseException ex) {
                    throw ex;
                } catch (Exception ex) {
                	ex.printStackTrace();
                    //eat it
                }
                break;
            }
            case "c": {
                elements.push("cell");
                try {
                    cell = XSSFReadEventCell.build(attributes);
                } catch (XSSFReadEventSheetDataStopParseException ex) {
                    throw ex;
                } catch (Exception ex) {
                    //eat it
                }
                break;
            }
            case "v": {
                if ("cell".equals(elements.peek())) {
                    elements.push("v");
                }
                break;
            }
            case "f": {
                if ("cell".equals(elements.peek())) {
                    elements.push("f");
                    XSSFReadEventFormula formula = XSSFReadEventFormula.build(attributes);
                    cell._formula = formula;
                }
                break;
            }
            case "is": {
                if ("cell".equals(elements.peek())) {
                    elements.push("is");
                    XSSFReadEventRichString richString = XSSFReadEventRichString.build(attributes);
                    cell._richString = richString;
                }
                break;
            }
            case "t": {
                if ("is".equals(elements.peek())) {
                    elements.push("t");
                }
                break;
            }

        }
    }


    public void characters(char[] ch, int start, int length) throws SAXException {
        String val = new String(ch, start, length);
        if(!elements.isEmpty()){
            String element = elements.peek();
            switch (element) {
                case "v": {
                    cell._valBuilder.append(val);
                    break;
                }
                case "f": {
                    cell._formula._valBuilder.append(val);
                    break;
                }
                case "t": {
                    cell._richString._strings.add(val);
                    break;
                }
            }
        }
    }


    public void endElement(String uri, String localName, String qName)
            throws SAXException {

        switch (qName) {
            case "row": {
                elements.pop();
                try {
                    readEventListener.onEndRow(row);
                } catch (XSSFReadEventSheetDataStopParseException ex) {
                    throw ex;
                } catch (Exception ex) {
                	ex.printStackTrace();
                    //eat it
                }
                row = null;
                break;
            }
            case "c": {
                elements.pop();
                try {
                    readEventListener.onStartCell(cell);
                    readEventListener.onEndCell(cell);
                    cell = null;
                } catch (XSSFReadEventSheetDataStopParseException ex) {
                    throw ex;
                } catch (Exception ex) {
                	ex.printStackTrace(); 
                    //eat it
                }
                break;
            }
            case "v":
            case "f":
            case "is":
            case "t": {
                elements.pop();
                break;
            }
            case "sheetData":{
            	break;
            }
                
            
        }
    }


}
