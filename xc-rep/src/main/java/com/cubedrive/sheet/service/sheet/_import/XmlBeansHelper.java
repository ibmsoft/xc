/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */
package com.cubedrive.sheet.service.sheet._import;

import java.util.ArrayList;
import java.util.List;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;

public class XmlBeansHelper {
	
	public static final String office14_spreadsheet_namespace = "declare namespace x14='http://schemas.microsoft.com/office/spreadsheetml/2009/9/main';";
    public static final String office11_spreadsheet_namespace = "declare namespace x11='http://schemas.openxmlformats.org/spreadsheetml/2006/main';";
    public static final String office11_excel_namespace = "declare namespace xm='http://schemas.microsoft.com/office/excel/2006/main';";
	
	private XmlBeansHelper(){
		
	}
	
	public static String getAttributeValue(XmlObject xmlObject, String prefix, String localname){
        XmlObject attributeObject = xmlObject.selectAttribute(prefix, localname);
        String attrValue = null;
        if(attributeObject != null){
            XmlCursor cursor = attributeObject.newCursor();
            if(cursor != null){
                attrValue = cursor.getTextValue();
                cursor.dispose();
            }
        }
        return attrValue;
    }
	
	public static String getElementValue(XmlObject xmlObject, String namespace, String xpath){
        XmlCursor xpathCursor = xmlObject.newCursor();
        String path = String.format("%s %s", namespace, xpath);
        xpathCursor.selectPath(path);
        xpathCursor.toNextSelection();
        String pathValue = xpathCursor.getSelectionCount() > 0 ? xpathCursor.getTextValue() : null;
        xpathCursor.dispose();
        return pathValue;
    }
    
	public static List<String> getElementValues(XmlObject xmlObject,String namespace, String xpath){
        XmlCursor xpathCursor = xmlObject.newCursor();
        List<String> pathValues = new ArrayList<>();
        String path = String.format("%s %s", namespace, xpath);
        xpathCursor.selectPath(path);
        xpathCursor.toNextSelection();
        while(xpathCursor.toNextSelection()){
            String pathValue = xpathCursor.getSelectionCount() > 0 ? xpathCursor.getTextValue() : null;
            pathValues.add(pathValue);
        }
        xpathCursor.dispose();
        return pathValues;
    }
	
	public static XmlObject getElement(XmlObject xmlObject, String namespace, String xpath) {
        XmlCursor xpathCursor = xmlObject.newCursor();
        String path = String.format("%s %s", namespace, xpath);
        xpathCursor.selectPath(path);
        xpathCursor.toNextSelection();
        XmlObject pathElement = xpathCursor.getSelectionCount() > 0 ? xpathCursor.getObject() : null;
        xpathCursor.dispose();
        return pathElement;
    }
	
	public static List<XmlObject> getXPathElements(XmlObject xml, String namespace, String xpath) {
        XmlCursor xpathCursor = xml.newCursor();
        List<XmlObject> pathElements = new ArrayList<XmlObject>();
        String path = String.format("%s %s", namespace, xpath);
        xpathCursor.selectPath(path);
        while (xpathCursor.toNextSelection()) {
            pathElements.add(xpathCursor.getSelectionCount() > 0 ? xpathCursor.getObject() : null);
        }
        xpathCursor.dispose();
        return pathElements;
    }
	
	

}
