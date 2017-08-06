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
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.openxml4j.opc.PackageRelationship;
import org.apache.poi.xssf.model.ThemesTable;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.openxmlformats.schemas.drawingml.x2006.main.CTColor;
import org.openxmlformats.schemas.drawingml.x2006.main.CTColorScheme;

public class XSSFReadEventThemesTable extends ThemesTable{
	
	private final List<byte[]> themeColors = new ArrayList<>();
	
	public XSSFReadEventThemesTable(PackagePart part, PackageRelationship rel) throws IOException {
        super(part,rel);
        initThemeColors();
    }

	private void initThemeColors(){
		CTColorScheme ctColorScheme = theme.getTheme().getThemeElements().getClrScheme();
		CTColor lt1Color = ctColorScheme.getLt1();
		if(lt1Color.isSetSysClr() && lt1Color.getSysClr().isSetLastClr()){  // 0
			byte[] rgb = lt1Color.getSysClr().getLastClr();
			themeColors.add(rgb);
		}else{
			themeColors.add(new byte[]{(byte)255,(byte)255,(byte)255});
		}
		
		CTColor dk1Color = ctColorScheme.getDk1();
		if(dk1Color.isSetSysClr() && dk1Color.getSysClr().isSetLastClr()){  // 1
			byte[] rgb = dk1Color.getSysClr().getLastClr();
			themeColors.add(rgb);
		}else{
			themeColors.add(new byte[]{0,0,0});
		}
		
		CTColor lt2Color = ctColorScheme.getLt2();
		themeColors.add(lt2Color.isSetSrgbClr() ? lt2Color.getSrgbClr().getVal() : null);  // 2
		
		CTColor dk2Color = ctColorScheme.getDk2();
		themeColors.add(dk2Color.isSetSrgbClr() ? dk2Color.getSrgbClr().getVal() : null); // 3
		
		CTColor accent1Color = ctColorScheme.getAccent1();
		themeColors.add(accent1Color.isSetScrgbClr() ? accent1Color.getSrgbClr().getVal() : null); //4
		
		CTColor accent2Color = ctColorScheme.getAccent2();
		themeColors.add(accent2Color.isSetSrgbClr() ? accent2Color.getSrgbClr().getVal() : null); //5
		
		CTColor accent3Color = ctColorScheme.getAccent3();
		themeColors.add(accent3Color.isSetSrgbClr() ? accent3Color.getSrgbClr().getVal() : null); //6
		
		CTColor accent4Color = ctColorScheme.getAccent4();
		themeColors.add(accent4Color.isSetSrgbClr() ? accent4Color.getSrgbClr().getVal() : null); //7
		
		CTColor accent5Color = ctColorScheme.getAccent5();
		themeColors.add(accent5Color.isSetSrgbClr() ? accent5Color.getSrgbClr().getVal() : null); //8
		
		CTColor accent6Color = ctColorScheme.getAccent6();
		themeColors.add(accent6Color.isSetSrgbClr() ? accent6Color.getSrgbClr().getVal() : null); //9
		
		CTColor hlinkColor = ctColorScheme.getHlink();
		if(hlinkColor != null && hlinkColor.isSetSrgbClr()){
			themeColors.add(hlinkColor.getSrgbClr().getVal());
		}
		
		CTColor folHlinkColor = ctColorScheme.getFolHlink();
		if(folHlinkColor != null && folHlinkColor.isSetSrgbClr()){
			themeColors.add(folHlinkColor.getSrgbClr().getVal());
		}
	}
	
	
	public void inheritFromThemeAsRequired(XSSFColor color) {
	       if(color == null) {
	          // Nothing for us to do
	          return;
	       }
	       if(! color.getCTColor().isSetTheme()) {
	          // No theme set, nothing to do
	          return;
	       }
	       
	       // Get the theme colour
//	       XSSFColor themeColor = getThemeColor(color.getTheme());
	       // Set the raw colour, not the adjusted one
	       // Do a raw set, no adjusting at the XSSFColor layer either
	       int themeIdx = color.getTheme();
	       byte[] rgb =null;
	       if(themeIdx < themeColors.size())
	    	   rgb = themeColors.get(themeIdx);
	       if(rgb == null){
	    	   rgb = getThemeColor(themeIdx).getCTColor().getRgb();
	       }
	       color.getCTColor().setRgb(rgb);
	       
    }
	

}
