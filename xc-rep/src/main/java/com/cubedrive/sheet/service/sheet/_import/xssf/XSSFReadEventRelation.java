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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.POIXMLRelation;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.openxml4j.opc.PackagePartName;
import org.apache.poi.openxml4j.opc.PackageRelationship;
import org.apache.poi.openxml4j.opc.PackageRelationshipCollection;
import org.apache.poi.openxml4j.opc.PackagingURIHelper;
import org.apache.poi.xssf.model.CalculationChain;
import org.apache.poi.xssf.model.CommentsTable;
import org.apache.poi.xssf.model.MapInfo;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.SingleXmlCells;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFChartSheet;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPictureData;
import org.apache.poi.xssf.usermodel.XSSFTable;

public class XSSFReadEventRelation extends POIXMLRelation{
    
    protected static Map<String, XSSFReadEventRelation> _table = new HashMap<String, XSSFReadEventRelation>();


    public static final XSSFReadEventRelation WORKBOOK = new XSSFReadEventRelation(
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml",
            "http://schemas.openxmlformats.org/officeDocument/2006/relationships/workbook",
            "/xl/workbook.xml",
            null
    );
    public static final XSSFReadEventRelation MACROS_WORKBOOK = new XSSFReadEventRelation(
            "application/vnd.ms-excel.sheet.macroEnabled.main+xml",
            "http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument",
            "/xl/workbook.xml",
            null
    );
    public static final XSSFReadEventRelation TEMPLATE_WORKBOOK = new XSSFReadEventRelation(
              "application/vnd.openxmlformats-officedocument.spreadsheetml.template.main+xml",
              "http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument",
              "/xl/workbook.xml",
              null
    );
    public static final XSSFReadEventRelation MACRO_TEMPLATE_WORKBOOK = new XSSFReadEventRelation(
              "application/vnd.ms-excel.template.macroEnabled.main+xml",
              "http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument",
              "/xl/workbook.xml",
              null
    );
    public static final XSSFReadEventRelation MACRO_ADDIN_WORKBOOK = new XSSFReadEventRelation(
              "application/vnd.ms-excel.addin.macroEnabled.main+xml",
              "http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument",
              "/xl/workbook.xml",
              null
    );
    public static final XSSFReadEventRelation WORKSHEET = new XSSFReadEventRelation(
            "application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml",
            "http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet",
            "/xl/worksheets/sheet#.xml",
            XSSFReadEventSheet.class
    );
    public static final XSSFReadEventRelation CHARTSHEET = new XSSFReadEventRelation(
            "application/vnd.openxmlformats-officedocument.spreadsheetml.chartsheet+xml",
            "http://schemas.openxmlformats.org/officeDocument/2006/relationships/chartsheet",
            "/xl/chartsheets/sheet#.xml",
            XSSFChartSheet.class
    );
    public static final XSSFReadEventRelation SHARED_STRINGS = new XSSFReadEventRelation(
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sharedStrings+xml",
            "http://schemas.openxmlformats.org/officeDocument/2006/relationships/sharedStrings",
            "/xl/sharedStrings.xml",
            SharedStringsTable.class
    );
    public static final XSSFReadEventRelation STYLES = new XSSFReadEventRelation(
            "application/vnd.openxmlformats-officedocument.spreadsheetml.styles+xml",
            "http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles",
            "/xl/styles.xml",
            XSSFReadEventStylesTable.class
    );
    public static final XSSFReadEventRelation DRAWINGS = new XSSFReadEventRelation(
            "application/vnd.openxmlformats-officedocument.drawing+xml",
            "http://schemas.openxmlformats.org/officeDocument/2006/relationships/drawing",
            "/xl/drawings/drawing#.xml",
            XSSFDrawing.class
    );
    public static final XSSFReadEventRelation VML_DRAWINGS = new XSSFReadEventRelation(
            "application/vnd.openxmlformats-officedocument.vmlDrawing",
            "http://schemas.openxmlformats.org/officeDocument/2006/relationships/vmlDrawing",
            "/xl/drawings/vmlDrawing#.vml",
            XSSFReadEventVMLDrawing.class
    );
   public static final XSSFReadEventRelation CHART = new XSSFReadEventRelation(
         "application/vnd.openxmlformats-officedocument.drawingml.chart+xml",
         "http://schemas.openxmlformats.org/officeDocument/2006/relationships/chart",
         "/xl/charts/chart#.xml",
         XSSFChart.class
   );

    public static final XSSFReadEventRelation CUSTOM_XML_MAPPINGS = new XSSFReadEventRelation(
            "application/xml",
            "http://schemas.openxmlformats.org/officeDocument/2006/relationships/xmlMaps",
            "/xl/xmlMaps.xml",
            MapInfo.class
    );

    public static final XSSFReadEventRelation SINGLE_XML_CELLS = new XSSFReadEventRelation(
            "application/vnd.openxmlformats-officedocument.spreadsheetml.tableSingleCells+xml",
            "http://schemas.openxmlformats.org/officeDocument/2006/relationships/tableSingleCells",
            "/xl/tables/tableSingleCells#.xml",
            SingleXmlCells.class
    );

    public static final XSSFReadEventRelation TABLE = new XSSFReadEventRelation(
            "application/vnd.openxmlformats-officedocument.spreadsheetml.table+xml",
            "http://schemas.openxmlformats.org/officeDocument/2006/relationships/table",
            "/xl/tables/table#.xml",
            XSSFTable.class
    );

    public static final XSSFReadEventRelation IMAGES = new XSSFReadEventRelation(
            null,
            "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image",
            null,
            XSSFPictureData.class
    );
    public static final XSSFReadEventRelation IMAGE_EMF = new XSSFReadEventRelation(
            "image/x-emf",
            "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image",
            "/xl/media/image#.emf",
            XSSFPictureData.class
    );
    public static final XSSFReadEventRelation IMAGE_WMF = new XSSFReadEventRelation(
            "image/x-wmf",
            "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image",
            "/xl/media/image#.wmf",
            XSSFPictureData.class
    );
    public static final XSSFReadEventRelation IMAGE_PICT = new XSSFReadEventRelation(
            "image/pict",
            "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image",
            "/xl/media/image#.pict",
            XSSFPictureData.class
    );
    public static final XSSFReadEventRelation IMAGE_JPEG = new XSSFReadEventRelation(
            "image/jpeg",
            "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image",
            "/xl/media/image#.jpeg",
            XSSFPictureData.class
    );
    public static final XSSFReadEventRelation IMAGE_PNG = new XSSFReadEventRelation(
            "image/png",
            "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image",
            "/xl/media/image#.png",
            XSSFPictureData.class
    );
    public static final XSSFReadEventRelation IMAGE_DIB = new XSSFReadEventRelation(
            "image/dib",
            "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image",
            "/xl/media/image#.dib",
            XSSFPictureData.class
    );

    public static final XSSFReadEventRelation IMAGE_GIF = new XSSFReadEventRelation(
            "image/gif",
            "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image",
            "/xl/media/image#.gif",
            XSSFPictureData.class
    );

    public static final XSSFReadEventRelation IMAGE_TIFF = new XSSFReadEventRelation(
            "image/tiff",
            "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image",
            "/xl/media/image#.tiff",
            XSSFPictureData.class
    );
    public static final XSSFReadEventRelation IMAGE_EPS = new XSSFReadEventRelation(
            "image/x-eps",
            "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image",
            "/xl/media/image#.eps",
            XSSFPictureData.class
    );
    public static final XSSFReadEventRelation IMAGE_BMP = new XSSFReadEventRelation(
            "image/x-ms-bmp",
            "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image",
            "/xl/media/image#.bmp",
            XSSFPictureData.class
    );
    public static final XSSFReadEventRelation IMAGE_WPG = new XSSFReadEventRelation(
            "image/x-wpg",
            "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image",
            "/xl/media/image#.wpg",
            XSSFPictureData.class
    );

  public static final XSSFReadEventRelation SHEET_COMMENTS = new XSSFReadEventRelation(
            "application/vnd.openxmlformats-officedocument.spreadsheetml.comments+xml",
            "http://schemas.openxmlformats.org/officeDocument/2006/relationships/comments",
            "/xl/comments#.xml",
            CommentsTable.class
    );
    public static final XSSFReadEventRelation SHEET_HYPERLINKS = new XSSFReadEventRelation(
            null,
            "http://schemas.openxmlformats.org/officeDocument/2006/relationships/hyperlink",
            null,
            null
    );
    public static final XSSFReadEventRelation OLEEMBEDDINGS = new XSSFReadEventRelation(
            null,
            POIXMLDocument.OLE_OBJECT_REL_TYPE,
            null,
            null
    );
    public static final XSSFReadEventRelation PACKEMBEDDINGS = new XSSFReadEventRelation(
            null,
            POIXMLDocument.PACK_OBJECT_REL_TYPE,
            null,
            null
    );

    public static final XSSFReadEventRelation VBA_MACROS = new XSSFReadEventRelation(
            "application/vnd.ms-office.vbaProject",
            "http://schemas.microsoft.com/office/2006/relationships/vbaProject",
            "/xl/vbaProject.bin",
            null
    );
    public static final XSSFReadEventRelation ACTIVEX_CONTROLS = new XSSFReadEventRelation(
            "application/vnd.ms-office.activeX+xml",
            "http://schemas.openxmlformats.org/officeDocument/2006/relationships/control",
            "/xl/activeX/activeX#.xml",
            null
    );
    public static final XSSFReadEventRelation ACTIVEX_BINS = new XSSFReadEventRelation(
            "application/vnd.ms-office.activeX",
            "http://schemas.microsoft.com/office/2006/relationships/activeXControlBinary",
            "/xl/activeX/activeX#.bin",
            null
    );
    public static final XSSFReadEventRelation THEME = new XSSFReadEventRelation(
            "application/vnd.openxmlformats-officedocument.theme+xml",
            "http://schemas.openxmlformats.org/officeDocument/2006/relationships/theme",
            "/xl/theme/theme#.xml",
            XSSFReadEventThemesTable.class
    );
    public static final XSSFReadEventRelation CALC_CHAIN = new XSSFReadEventRelation(
            "application/vnd.openxmlformats-officedocument.spreadsheetml.calcChain+xml",
            "http://schemas.openxmlformats.org/officeDocument/2006/relationships/calcChain",
            "/xl/calcChain.xml",
            CalculationChain.class
    );
    public static final XSSFReadEventRelation PRINTER_SETTINGS = new XSSFReadEventRelation(
          "application/vnd.openxmlformats-officedocument.spreadsheetml.printerSettings",
          "http://schemas.openxmlformats.org/officeDocument/2006/relationships/printerSettings",
          "/xl/printerSettings/printerSettings#.bin",
          null
   );


    private XSSFReadEventRelation(String type, String rel, String defaultName, Class<? extends POIXMLDocumentPart> cls) {
        super(type, rel, defaultName, cls);

        if(cls != null && !_table.containsKey(rel)) _table.put(rel, this);
    }

    /**
     *  Fetches the InputStream to read the contents, based
     *  of the specified core part, for which we are defined
     *  as a suitable relationship
     */
    public InputStream getContents(PackagePart corePart) throws IOException, InvalidFormatException {
        PackageRelationshipCollection prc =
            corePart.getRelationshipsByType(_relation);
        Iterator<PackageRelationship> it = prc.iterator();
        if(it.hasNext()) {
            PackageRelationship rel = it.next();
            PackagePartName relName = PackagingURIHelper.createPartName(rel.getTargetURI());
            PackagePart part = corePart.getPackage().getPart(relName);
            return part.getInputStream();
        }
        return null;
    }


    /**
     * Get POIXMLRelation by relation type
     *
     * @param rel relation type, for example,
     *    <code>http://schemas.openxmlformats.org/officeDocument/2006/relationships/image</code>
     * @return registered POIXMLRelation or null if not found
     */
    public static XSSFReadEventRelation getInstance(String rel){
        return _table.get(rel);
    }

}
