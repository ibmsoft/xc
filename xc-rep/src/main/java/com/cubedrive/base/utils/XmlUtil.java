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
package com.cubedrive.base.utils;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXTransformerFactory;

public class XmlUtil {
    
    public static final SAXTransformerFactory saxTransformerFactory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
    public static final SAXParserFactory saxNamespaceAwareParserFactory = SAXParserFactory.newInstance();
    public static final SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
    
    static{
        saxNamespaceAwareParserFactory.setNamespaceAware(true);
        saxNamespaceAwareParserFactory.setValidating(false);
        
        saxParserFactory.setNamespaceAware(false);
        
    }

}
