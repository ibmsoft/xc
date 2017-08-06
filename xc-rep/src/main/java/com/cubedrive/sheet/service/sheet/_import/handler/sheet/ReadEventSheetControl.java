package com.cubedrive.sheet.service.sheet._import.handler.sheet;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.poi.POIXMLDocumentPart;
import org.apache.xmlbeans.XmlObject;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.cubedrive.sheet.service.sheet._import.XmlBeansHelper;
import com.cubedrive.sheet.service.sheet._import.ctx.SheetImportContext;

public class ReadEventSheetControl {

    private List<SheetControl> _controls = new ArrayList<>();

    public SheetControl getControl(int row, int column) {
        for (SheetControl _alternateContent : _controls) {
            if (_alternateContent._row == row
                    && _alternateContent._column == column) {
                return _alternateContent;
            }
        }
        return null;

    }
    
    private static SheetControl handleControl(XmlObject alternateContentXmlObject, SheetImportContext sheetImportContext) throws Exception{
        XmlObject controlObject =XmlBeansHelper.getElement(alternateContentXmlObject, XmlBeansHelper.office11_spreadsheet_namespace, "$this//x11:control");
        String rid = XmlBeansHelper.getAttributeValue(controlObject, "http://schemas.openxmlformats.org/officeDocument/2006/relationships", "id");
        XmlObject fromObject =XmlBeansHelper.getElement(controlObject, XmlBeansHelper.office11_spreadsheet_namespace, "$this//x11:from");
        String col = XmlBeansHelper.getElementValue(fromObject, "declare namespace xdr='http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing';", "$this//xdr:col");
        String row = XmlBeansHelper.getElementValue(fromObject, "declare namespace xdr='http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing';", "$this//xdr:row");
        POIXMLDocumentPart  ctrlPropsPart = sheetImportContext.sheet().getRelationById(rid);
        if(ctrlPropsPart != null){
            Map<String,Object> settings = getControlSetting(ctrlPropsPart);
            if(settings != null){
                SheetControl sheetControl = new SheetControl(Integer.parseInt(row)+1,Integer.parseInt(col)+1, settings);
                return sheetControl;
            }
        }
        return null;
        
    }
    
    private static Map<String,Object> getControlSetting(POIXMLDocumentPart ctrlPropsPart) throws Exception{
        try(InputStream in=ctrlPropsPart.getPackagePart().getInputStream()){
            Document document  = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
            Node formControlPrNode = document.getFirstChild();
            String objectType = formControlPrNode.getAttributes().getNamedItem("objectType").getNodeValue();
            String it = null;
            if("Radio".equals(objectType)){
                it = "radio";
            }else if("CheckBox".equals(objectType)){
                it = "checkbox";
            }
            Node checkedNode = formControlPrNode.getAttributes().getNamedItem("checked");
            boolean itchk = false;
            if(checkedNode != null && "Checked".equals(checkedNode.getNodeValue())){
                itchk = true;
            }
            Map<String,Object> settings = new HashMap<>();
            if(it != null){
                settings.put("it", it);
                settings.put("itchk",itchk);
                settings.put("itn", System.currentTimeMillis());
                return settings;
            }
            return null;
        }
        
    }
    

    public static ReadEventSheetControl build(SheetImportContext sheetImportContext) {
        ReadEventSheetControl readEventSheetAlternateContent = new ReadEventSheetControl();
        CTWorksheet ctWorksheet = sheetImportContext.sheet().worksheet();
        List<XmlObject> alternateContents = XmlBeansHelper.getXPathElements(ctWorksheet, "declare namespace mc='http://schemas.openxmlformats.org/markup-compatibility/2006';", "$this//mc:AlternateContent//mc:AlternateContent");
        for(XmlObject alternateContent:alternateContents){
            try {
                SheetControl sheetControll = handleControl(alternateContent,sheetImportContext);
                if(sheetControll != null)
                    readEventSheetAlternateContent._controls.add(sheetControll);
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
        return readEventSheetAlternateContent;

    }

    public static class SheetControl {
        private final int _row;
        private final int _column;
        private final Map<String, Object> _settings;

        SheetControl(int row, int column, Map<String, Object> settings) {
            this._row = row;
            this._column = column;
            this._settings = settings;
        }
        
        public Map<String,Object> getSettings(){
            return _settings;
        }

        public boolean equals(Object o) {
            if (o == this)
                return true;
            if (!(o instanceof SheetControl))
                return false;
            SheetControl other = (SheetControl) o;
            return Objects.equals(_row, other._row)
                    && Objects.equals(_column, other._column);
        }

        public int hashCode() {
            return Objects.hash(_row, _column);
        }
    }

}
