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

public class XSSFReadEventRow {

    private Map<String, String> _values = new HashMap<String, String>();

    // attr:r
    public Integer rowIndex() {
    	String r = _values.get("r");
    	if(r != null && r.length()>0){
    		return Integer.parseInt(r);
    	}else{
    		return null;
    	}
    }

    // attr:spans
    public String spans() {
        return _values.get("spans");
    }

    // attr:s
    public Integer styleIndex() {
        String s = _values.get("s");
        if (s != null && s.length() > 0) {
            return Integer.parseInt(s);
        }else{
        	return null;
        }
        
    }

    // attr:customFormat
    public boolean customFormat() {
        String _customFormat = _values.get("customFormat");
        return "1".equals(_customFormat) || "true".equals(_customFormat);
    }

    // attr:ht
    public double rowHeight() {
        String _ht = _values.get("ht");
        if (_ht != null) {
            return Double.parseDouble(_ht);
        } else {
            return XSSFReadEventUtil.default_row_height;
        }
    }

    // attr:hidden
    public boolean hidden() {
        String _hidden = _values.get("hidden");
        return "1".equals(_hidden) || "true".equals(_hidden);
    }

    // attr:customHeight
    public boolean customHeight() {
        String _customHeight = _values.get("customHeight");
        return "1".equals(_customHeight) || "true".equals(_customHeight);
    }

    // attr:outlineLevel
    public Integer outlineLevel() {
        String _outlineLevel = _values.get("outlineLevel");
        if (_outlineLevel != null) {
            return Integer.parseInt(_outlineLevel);
        } else {
            return null;
        }
    }

    // attr:collapsed
    public boolean collapsed() {
        String _collapsed = _values.get("collapsed");
        return "1".equals(_collapsed) || "true".equals(_collapsed);
    }

    // attr:thickTop
    public boolean thickTop() {
        String _thickTop = _values.get("thickTop");
        return "1".equals(_thickTop) || "true".equals(_thickTop);
    }

    // attr:thickBot
    public boolean thickBottom() {
        String _thickBot = _values.get("thickBot");
        return "1".equals(_thickBot) || "true".equals(_thickBot);
    }

    // attr:ph
    public boolean showPhonetic() {
        String _ph = _values.get("ph");
        return "1".equals(_ph) || "true".equals(_ph);
    }

//    List<XSSFReadEventCell> _cells = new ArrayList<XSSFReadEventCell>();
//
//    public List<XSSFReadEventCell> cells() {
//        return _cells;
//    }

    private XSSFReadEventRow() {

    }

    static XSSFReadEventRow build(Attributes attributes) {
        XSSFReadEventRow row = new XSSFReadEventRow();
        for (int i = 0; i < attributes.getLength(); i++) {
            String attrName = attributes.getLocalName(i);
            if (attrName == null || "".equals(attrName)) {
                attrName = attributes.getQName(i);
            }
            if (attrName != null || !"".equals(attrName)) {
                row._values.put(attrName, attributes.getValue(i));
            }
        }
        return row;

    }

}
