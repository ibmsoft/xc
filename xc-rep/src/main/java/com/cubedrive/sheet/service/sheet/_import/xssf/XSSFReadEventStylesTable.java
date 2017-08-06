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
import java.util.Collections;
import java.util.List;

import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.openxml4j.opc.PackageRelationship;
import org.apache.poi.xssf.model.StylesTable;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIndexedColors;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRgbColor;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTStylesheet;

public class XSSFReadEventStylesTable extends StylesTable {
	
	private List<CTRgbColor> indexedColors;
	
	public XSSFReadEventStylesTable(PackagePart part, PackageRelationship rel) throws IOException {
		super(part, rel);
	}
	
	public void readFrom(InputStream is) throws IOException {
		super.readFrom(is);
		loadIndexedColors();
	}
	
	private void loadIndexedColors(){
		CTStylesheet ctStylesheet = getCTStylesheet();
		if(ctStylesheet.isSetColors() && ctStylesheet.getColors().isSetIndexedColors()){
			CTIndexedColors ctIndexedColors = ctStylesheet.getColors().getIndexedColors();
			indexedColors = new ArrayList<>(ctIndexedColors.sizeOfRgbColorArray());
			indexedColors.addAll(ctIndexedColors.getRgbColorList());
		}else{
			indexedColors = Collections.emptyList();
		}
	}
	
	public List<CTRgbColor> getIndexedColors(){
		return this.indexedColors;
	}

}
