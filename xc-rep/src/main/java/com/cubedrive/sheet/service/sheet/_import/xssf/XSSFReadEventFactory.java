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
package com.cubedrive.sheet.service.sheet._import.xssf;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.POIXMLException;
import org.apache.poi.POIXMLFactory;
import org.apache.poi.POIXMLRelation;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.openxml4j.opc.PackageRelationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XSSFReadEventFactory extends POIXMLFactory{
    
    private static final Logger logger =LoggerFactory.getLogger(XSSFReadEventFactory.class);
    
    private XSSFReadEventFactory(){

    }
    
    private static final XSSFReadEventFactory inst = new XSSFReadEventFactory();

    public static XSSFReadEventFactory getInstance(){
        return inst;
    }

    @Override
    public POIXMLDocumentPart createDocumentPart(POIXMLDocumentPart parent, PackagePart part) {
        PackageRelationship rel = getPackageRelationship(parent, part);
        POIXMLRelation descriptor = XSSFReadEventRelation.getInstance(rel.getRelationshipType());
        if(descriptor == null || descriptor.getRelationClass() == null){
            logger.debug("using default POIXMLDocumentPart for " + rel.getRelationshipType());
            return new POIXMLDocumentPart(part, rel);
        }
     
        try {
            Class<? extends POIXMLDocumentPart> cls = descriptor.getRelationClass();
            Constructor<? extends POIXMLDocumentPart> constructor = cls.getDeclaredConstructor(PackagePart.class, PackageRelationship.class);
            return constructor.newInstance(part, rel);
        } catch (Exception e){
            throw new POIXMLException(e);
        }
    }

    @Override
    public POIXMLDocumentPart newDocumentPart(POIXMLRelation descriptor) {
        Class<? extends POIXMLDocumentPart> cls = descriptor.getRelationClass();
        try {
            return createDocumentPart(cls, null, null);
        } catch (Exception e) {
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
        return XSSFReadEventRelation.getInstance(relationshipType);
    }

}
