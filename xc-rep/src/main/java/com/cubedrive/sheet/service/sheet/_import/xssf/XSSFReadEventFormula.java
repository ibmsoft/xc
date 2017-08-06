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

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;

public class XSSFReadEventFormula{

    private final Map<String, String> _values = new HashMap<String, String>();

    // attr:t
    public String formulaType() {
        String _type = _values.get("t");
        if (_type == null)
            return XSSFReadEventUtil.default_formula_type;
        else
            return _type;
    }

    // attr:aca
    public boolean alwaysCalculateArray() {
        String _alwaysCalculateArray = _values.get("aca");
        return "1".equals(_alwaysCalculateArray) || "true".equals(_alwaysCalculateArray);
    }

    // attr:ref
    public String reference() {
        return _values.get("ref");
    }

    // attr:dt2D
    public boolean dataTable2D() {
        String _dataTable2D = _values.get("dt2D");
        return "1".equals(_dataTable2D) || "true".equals(_dataTable2D);
    }

    // attr:dtr
    public boolean dataTableRow() {
        String _dataTableRow = _values.get("dtr");
        return "1".equals(_dataTableRow) || "true".equals(_dataTableRow);
    }

    // attr:del1
    public boolean input1Deleted() {
        String _input1Deleted = _values.get("del1");
        return "1".equals(_input1Deleted) || "true".equals(_input1Deleted);
    }

    // attr:del2
    public boolean input2Deleted() {
        String _input2Deleted = _values.get("del2");
        return "1".equals(_input2Deleted) || "true".equals(_input2Deleted);
    }

    // attr:r1
    public String dataTableCell1() {
        return _values.get("r1");
    }

    // attr:r2
    public String inputCell2() {
        return _values.get("r2");
    }

    // attr:ca
    public boolean calculateCell() {
        String _calculateCell = _values.get("ca");
        return "1".equals(_calculateCell) || "true".equals(_calculateCell);
    }

    // attr:si
    public Integer sharedGroupIndex() {
        String _shareGroupIndex = _values.get("si");
        if (_shareGroupIndex != null) {
            return Integer.parseInt(_shareGroupIndex);
        } else {
            return null;
        }
    }

    // attr:bx
    public boolean bindName() {
        String _bindName = _values.get("bx");
        return "1".equals(_bindName) || "true".equals(_bindName);
    }

    StringBuilder _valBuilder = new StringBuilder();

    public String val() {
        String _val = _valBuilder.toString();
        if(_val.length() == 0)
            return null;
        return _val;
    }

    private XSSFReadEventFormula() {

    }
    
    public XSSFReadEventFormula copy(){
        XSSFReadEventFormula _copy = new XSSFReadEventFormula();
        _copy._valBuilder = this._valBuilder;
        _copy._values.putAll(_values);
        return _copy;
    }
    
    public XSSFReadEventFormula copyWithReference(String reference){
        XSSFReadEventFormula _copy = new XSSFReadEventFormula();
        _copy._valBuilder = this._valBuilder;
        _copy._values.putAll(_values);
        _copy._values.put("ref", reference);
        return _copy;
    }


    static XSSFReadEventFormula build(Attributes attributes) {
        XSSFReadEventFormula formula = new XSSFReadEventFormula();
        for (int i = 0; i < attributes.getLength(); i++) {
            String attrName = attributes.getLocalName(i);
            if (attrName == null || "".equals(attrName)) {
                attrName = attributes.getQName(i);
            }
            if (attrName != null || !"".equals(attrName)) {
                formula._values.put(attrName, attributes.getValue(i));
            }
        }
        return formula;

    }

}
