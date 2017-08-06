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

import org.apache.poi.ss.util.CellReference;
import org.xml.sax.Attributes;

public class XSSFReadEventCell {

    private final Map<String, String> _values = new HashMap<>();
    
    private CellReference cellReference;

    // attr:r
    public String reference() {
        return _values.get("r");
    }

    // attr:s
    public int styleIndex() {
        String _styleIndex = _values.get("s");
        if (_styleIndex != null) {
            return Integer.parseInt(_styleIndex);
        } else {
            return XSSFReadEventUtil.default_style_index;
        }
    }

    // attr:t
    public String dataType() {
        String _dataType = _values.get("t");
        if (_dataType != null) {
            return _dataType;
        } else {
            return XSSFReadEventUtil.default_cell_type;
        }
    }

    // child:v
    StringBuilder _valBuilder= new StringBuilder();

    // child:f
    XSSFReadEventFormula _formula;

    // child:is
    XSSFReadEventRichString _richString;


    private XSSFReadEventCell() {

    }


    public String val() {
        String _val = _valBuilder.toString();
        if(_val.length() == 0)
            return null;
        return _val;
    }

    public XSSFReadEventFormula formula() {
        return _formula;
    }

    public XSSFReadEventRichString richString() {
        return _richString;
    }

    

    public CellReference getCellReference() {
		return cellReference;
	}

	public void setCellReference(CellReference cellReference) {
		this.cellReference = cellReference;
	}

	static XSSFReadEventCell build(Attributes attributes) {
        XSSFReadEventCell cell = new XSSFReadEventCell();
        for (int i = 0; i < attributes.getLength(); i++) {
            String attrName = attributes.getLocalName(i);
            if (attrName == null || "".equals(attrName)) {
                attrName = attributes.getQName(i);
            }
            if (attrName != null || !"".equals(attrName)) {
                cell._values.put(attrName, attributes.getValue(i));
            }
        }
        return cell;
    }


}
