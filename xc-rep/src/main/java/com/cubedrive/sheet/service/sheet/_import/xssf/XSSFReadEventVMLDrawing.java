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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.namespace.QName;

import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.openxml4j.opc.PackageRelationship;
import org.apache.poi.xssf.util.EvilUnclosedBRFixingInputStream;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.w3c.dom.Node;
import static org.apache.poi.POIXMLTypeLoader.DEFAULT_XML_OPTIONS;
import com.microsoft.schemas.vml.CTShape;

public class XSSFReadEventVMLDrawing extends POIXMLDocumentPart{
	
	private static final QName QNAME_SHAPE_LAYOUT = new QName("urn:schemas-microsoft-com:office:office", "shapelayout");
    private static final QName QNAME_SHAPE_TYPE = new QName("urn:schemas-microsoft-com:vml", "shapetype");
    private static final QName QNAME_SHAPE = new QName("urn:schemas-microsoft-com:vml", "shape");
    private static final Pattern ptrn_shapeId = Pattern.compile("_x0000_s(\\d+)");

    private int _shapeId = 1024;
    private List<CTShape> _shapes = new ArrayList<CTShape>();

    public XSSFReadEventVMLDrawing(PackagePart part) throws IOException, XmlException {
        super(part);
        read(getPackagePart().getInputStream());
    }
    
    public XSSFReadEventVMLDrawing(PackagePart part, PackageRelationship rel) throws IOException, XmlException {
        this(part);
    }


    protected void read(InputStream is) throws IOException, XmlException {
        XmlObject root = XmlObject.Factory.parse(
              new EvilUnclosedBRFixingInputStream(is)
        );
        for(XmlObject obj : root.selectPath("$this/xml/*")) {
            Node nd = obj.getDomNode();
            QName qname = new QName(nd.getNamespaceURI(), nd.getLocalName());
            if (qname.equals(QNAME_SHAPE)) {
                CTShape shape = CTShape.Factory.parse(obj.xmlText(), DEFAULT_XML_OPTIONS);
                String id = shape.getId();
                if(id != null) {
                    Matcher m = ptrn_shapeId.matcher(id);
                    if(m.find()) _shapeId = Math.max(_shapeId, Integer.parseInt(m.group(1)));
                }
                _shapes.add(shape);
            } 
        }
    }

    public List<CTShape> getShapes(){
        return _shapes;
    }
    
    

}
