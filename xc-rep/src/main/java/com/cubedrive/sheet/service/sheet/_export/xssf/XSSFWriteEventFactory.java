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
package com.cubedrive.sheet.service.sheet._export.xssf;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.POIXMLException;
import org.apache.poi.POIXMLFactory;
import org.apache.poi.POIXMLRelation;
import org.apache.poi.openxml4j.opc.PackagePart;


public class XSSFWriteEventFactory extends POIXMLFactory{
    
    private XSSFWriteEventFactory(){

    }
    
    private static final XSSFWriteEventFactory inst = new XSSFWriteEventFactory();

    public static XSSFWriteEventFactory getInstance(){
        return inst;
    }

    @Override
    public POIXMLDocumentPart createDocumentPart(POIXMLDocumentPart parent, PackagePart part) {
        throw new UnsupportedOperationException();
    }

    @Override
    public POIXMLDocumentPart newDocumentPart(POIXMLRelation descriptor) {
        try {
            Class<? extends POIXMLDocumentPart> cls = descriptor.getRelationClass();
            Constructor<? extends POIXMLDocumentPart> constructor = cls.getDeclaredConstructor();
            return constructor.newInstance();
        } catch (Exception e){
            throw new POIXMLException(e);
        }
    }

    @Override
    protected POIXMLDocumentPart createDocumentPart(
            Class<? extends POIXMLDocumentPart> cls, Class<?>[] classes,
            Object[] values) throws SecurityException, NoSuchMethodException,
                    InstantiationException, IllegalAccessException,
                    InvocationTargetException {
        Constructor<? extends POIXMLDocumentPart> constructor = cls.getDeclaredConstructor(classes);
        return constructor.newInstance(values);
    }

    @Override
    protected POIXMLRelation getDescriptor(String relationshipType) {
        return XSSFWriteEventRelation.getInstance(relationshipType);
    }

}
