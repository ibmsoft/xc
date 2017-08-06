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
package com.cubedrive.sheet.service.sheet._export.xssf;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.util.Internal;
import org.apache.xmlbeans.XmlOptions;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPageMargins;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPrintSettings;
import org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId;
import static org.apache.poi.POIXMLTypeLoader.DEFAULT_XML_OPTIONS;

public class XSSFWriteEventChart extends POIXMLDocumentPart{
    
    /**
     * Parent graphic frame.
     */
    private XSSFWriteEventGraphicFrame frame;

    /**
     * Root element of the SpreadsheetML Chart part
     */
    private CTChartSpace chartSpace;
    /**
     * The Chart within that
     */
    private CTChart chart;
    
    protected XSSFWriteEventChart() {
        super();
        createChart();
    }
    
    private void createChart() {
        chartSpace = CTChartSpace.Factory.newInstance();
        chart = chartSpace.addNewChart();

        CTPrintSettings printSettings = chartSpace.addNewPrintSettings();
        printSettings.addNewHeaderFooter();

        CTPageMargins pageMargins = printSettings.addNewPageMargins();
        pageMargins.setB(0.75);
        pageMargins.setL(0.70);
        pageMargins.setR(0.70);
        pageMargins.setT(0.75);
        pageMargins.setHeader(0.30);
        pageMargins.setFooter(0.30);
        printSettings.addNewPageSetup();
    }
    
    /**
     * Return the underlying CTChartSpace bean, the root element of the SpreadsheetML Chart part.
     *
     * @return the underlying CTChartSpace bean
     */
    @Internal
    public CTChartSpace getCTChartSpace(){
        return chartSpace;
    }

    /**
     * Return the underlying CTChart bean, within the Chart Space
     *
     * @return the underlying CTChart bean
     */
    @Internal
    public CTChart getCTChart(){
        return chart;
    }

    @Override
    protected void commit() throws IOException {
        XmlOptions xmlOptions = new XmlOptions(DEFAULT_XML_OPTIONS);

        /*
           Saved chart space must have the following namespaces set:
           <c:chartSpace
              xmlns:c="http://schemas.openxmlformats.org/drawingml/2006/chart"
              xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main"
              xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships">
         */
        xmlOptions.setSaveSyntheticDocumentElement(new QName(CTChartSpace.type.getName().getNamespaceURI(), "chartSpace", "c"));

        PackagePart part = getPackagePart();
        OutputStream out = part.getOutputStream();
        chartSpace.save(out, xmlOptions);
        out.close();
    }

    /**
     * Returns the parent graphic frame.
     * @return the graphic frame this chart belongs to
     */
    public XSSFWriteEventGraphicFrame getGraphicFrame() {
        return frame;
    }

    /**
     * Sets the parent graphic frame.
     */
    public void setGraphicFrame(XSSFWriteEventGraphicFrame frame) {
        this.frame = frame;
    }


}
