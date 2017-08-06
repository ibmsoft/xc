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
package com.cubedrive.sheet.service.sheet._export;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGraphicalObjectFrame;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTTwoCellAnchor;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.STEditAs;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor;

import com.cubedrive.sheet.service.sheet._export.ctx.XSSFWorksheetExportContext;
import com.cubedrive.sheet.service.sheet._export.xssf.XSSFWriteEventChart;
import com.cubedrive.sheet.service.sheet._export.xssf.XSSFWriteEventFactory;
import com.cubedrive.sheet.service.sheet._export.xssf.XSSFWriteEventGraphicFrame;
import com.cubedrive.sheet.service.sheet._export.xssf.XSSFWriteEventRelation;

public class SpreadsheetExportHelper {
	
	public static final int excel_unit_per_px = 15; // in excel,1 px = 15
    
    public static final int default_row_height = 20;  // ui default row height 20 px
    
    public static final int default_column_width = 80; //ui default column width 80 px
    
    public static final String lock_cell_color = "#EEEEEE";
    
    private  static final Pattern rgb_pattern = Pattern.compile("rgb\\((\\d+)\\,(\\d+)\\,(\\d+)\\)");
    
    private static final Map<String,Color> colors = new HashMap<>();
    
    static{
        colors.put("black", Color.black);
        colors.put("blue", Color.blue);
        colors.put("cyan", Color.cyan);
        colors.put("darkGray", Color.darkGray);
        colors.put("gray", Color.gray);
        colors.put("green", Color.green);
        colors.put("yellow", Color.yellow);
        colors.put("lightGray", Color.lightGray);
        colors.put("magenta", Color.magenta);
        colors.put("orange",Color.orange);
        colors.put("pink",Color.pink);
        colors.put("red", Color.red);
        colors.put("white", Color.white);
    }
    
    private static final Map<String, Integer> pictureType = new HashMap<String, Integer>();
    
    static {
        pictureType.put("gif", XSSFWorkbook.PICTURE_TYPE_GIF);
        pictureType.put("emf", XSSFWorkbook.PICTURE_TYPE_EMF);
        pictureType.put("wmf", XSSFWorkbook.PICTURE_TYPE_WMF);
        pictureType.put("pict", XSSFWorkbook.PICTURE_TYPE_PICT);
        pictureType.put("jpeg", XSSFWorkbook.PICTURE_TYPE_JPEG);
        pictureType.put("jpg", XSSFWorkbook.PICTURE_TYPE_JPEG);
        pictureType.put("png", XSSFWorkbook.PICTURE_TYPE_PNG);
        pictureType.put("dib", XSSFWorkbook.PICTURE_TYPE_DIB);
    }
    
    
    public enum DataType{
        formula,bool,string,numeric,date,time,datetime,error,blank
    }

    private static final byte[] color_black = new byte[]{0,0,0};

    // this method will check string - url, email or LINK_DOCUMENT
    public static int checkURLType(String inUrl) {
        int linkType = -1;
        if(inUrl.contains("!") && inUrl.contains("$")){
        	linkType = Hyperlink.LINK_DOCUMENT;
        }else{
	        try{
	        	URL url = new URL(inUrl);
	        	String protocol = url.getProtocol();
	        	switch(protocol){
	        		case "mailto": linkType = Hyperlink.LINK_EMAIL;break;
	        		case "file": linkType= Hyperlink.LINK_FILE;break;
	        		default:linkType = Hyperlink.LINK_URL;break;
	        	}
	        }catch(MalformedURLException ex){
	        	
	        }
        }

        return linkType;
    }

    public static byte[] colorAsBytes(Integer[] ints){
    	byte[] rgb = new byte[3];
    	rgb[0] = ints[0].byteValue();
    	rgb[1] = ints[1].byteValue();
    	rgb[2] = ints[2].byteValue();
//    	if(rgb[0] ==-1 && rgb[1] == -1 && rgb[2] ==-1){
//            rgb = new byte[] { (byte) -1, (byte) 255, (byte) 255, (byte) 255 };
//        }else if(rgb[0] == 0 && rgb[1] == 0 && rgb[2] == 0){
//            rgb = new byte[] { (byte) -1, (byte) 0, (byte) 0, (byte) 0 };
//        }
    	return rgb;
    }
    
    
    public static byte[] colorAsBytes(String color){
        byte[] rgb =null ;
        if(color.startsWith("#")) {
            rgb =new byte[3];
            rgb[0] = (byte) Integer.parseInt(color.substring(1, 3), 16);
            rgb[1] = (byte) Integer.parseInt(color.substring(3, 5), 16);
            rgb[2] = (byte) Integer.parseInt(color.substring(5, 7), 16);
           
        }else if(color.startsWith("rgb")){
            Matcher matcher = rgb_pattern.matcher(color);
            if(matcher.matches()){
                rgb =new byte[3];
                rgb[0] =  (byte) Integer.parseInt(matcher.group(1));
                rgb[1]= (byte)Integer.parseInt(matcher.group(2));
                rgb[2] =(byte)Integer.parseInt(matcher.group(3));
            }
        }else if(colors.containsKey(color)){
            Color awtColor = colors.get(color);
            rgb =new byte[3];
            rgb[0] = (byte)awtColor.getRed();
            rgb[1] = (byte)awtColor.getGreen();
            rgb[2] = (byte)awtColor.getBlue();
        }
        
//        if(rgb != null){
//            if(rgb[0] ==-1 && rgb[1] == -1 && rgb[2] ==-1){
//                rgb = new byte[] { (byte) 255, (byte) 0, (byte) 0, (byte) 0 };
//            }else if(rgb[0] == 0 && rgb[1] == 0 && rgb[2] == 0){
//                rgb = new byte[] { (byte) 255, (byte)255, (byte) 255, (byte) 255 };
//            }
//        }
        return rgb;
        
    }
    
    //hexString: like #ffffff ,else return black
    public static XSSFColor colorAsXssfColor(String color) {
        XSSFColor xssfColor = null;
        byte[] rgb = colorAsBytes(color);
        if(rgb != null){
        	if(rgb[0]==-1 && rgb[1]==-1 && rgb[2]==-1){
        		xssfColor = new XSSFColor();
        		xssfColor.setIndexed(IndexedColors.WHITE.index);
        	}else if(rgb[0] == 0 && rgb[1]==0 && rgb[2]==0){
        		xssfColor = new XSSFColor();
        		xssfColor.setIndexed(IndexedColors.BLACK.index);
        	}else{
        		xssfColor = new XSSFColor(rgb);
        	}
        }
        return xssfColor;
    }
    
    public static void setCTColorByRgb(CTColor ctColor, byte[] rgb){
    	 if(rgb[0]==-1 && rgb[1]==-1 && rgb[2]==-1){
     		ctColor.setIndexed(IndexedColors.WHITE.index);
     	}else if(rgb[0] == 0 && rgb[1]==0 && rgb[2]==0){
     		ctColor.setIndexed(IndexedColors.BLACK.index);
     	}else{
     		ctColor.setRgb(rgb);
     	}
    }
    
    public static int getPictureType(String extension){
        Integer type = pictureType.get(extension);
        if(type == null)
            type = XSSFWorkbook.PICTURE_TYPE_JPEG;
        return type;
    }

    public static InputStream getInputStreamByUrl(String urlString) throws IOException{
        URL url = new URL(urlString);
        URLConnection con = url.openConnection();
        return con.getInputStream();
    }
    
    public static String filterFormulaIllegalCharacter(String formula){
    	StringBuilder buf = new StringBuilder();
    	for(int i=0;i<formula.length();i++){
    		char c = formula.charAt(i);
    		if (c == 160)continue;
    		buf.append(c);
    	}
    	return buf.toString();
    }
    
    public static XSSFWriteEventChart createChart(XSSFWorksheetExportContext worksheetExportContext,
            XSSFDrawing drawing,Map<String,Object>withLocationContentMap) {
        int chartNumber = drawing.getPackagePart().getPackage().
            getPartsByContentType(XSSFRelation.CHART.getContentType()).size() + 1;
        XSSFWriteEventChart chart = (XSSFWriteEventChart) drawing.createRelationship(
                XSSFWriteEventRelation.CHART, XSSFWriteEventFactory.getInstance(), chartNumber);
        String chartRelId = chart.getPackageRelationship().getId();
        XSSFWriteEventGraphicFrame frame = createGraphicFrame(worksheetExportContext,drawing,withLocationContentMap);
        frame.setChart(chart, chartRelId);
        return chart;
    }
    
    public static XSSFWriteEventGraphicFrame createGraphicFrame(XSSFWorksheetExportContext worksheetExportContext,
            XSSFDrawing xssfDrawing,Map<String,Object>withLocationContentMap) {
        XSSFClientAnchor clientAnchor = createClientAnchor(worksheetExportContext,withLocationContentMap);
        CTTwoCellAnchor ctAnchor = createTwoCellAnchor(xssfDrawing,clientAnchor);
        CTGraphicalObjectFrame ctGraphicFrame = ctAnchor.addNewGraphicFrame();
        ctGraphicFrame.set(XSSFWriteEventGraphicFrame.prototype());
        int frameId = worksheetExportContext.parentContext().getAndIncreaseGraphicFrame();
        XSSFWriteEventGraphicFrame graphicFrame = new XSSFWriteEventGraphicFrame(xssfDrawing, ctGraphicFrame);
        graphicFrame.setAnchor(clientAnchor);
        graphicFrame.setId(frameId);
        graphicFrame.setName("Diagramm" + frameId);
        return graphicFrame;
    }
    
    private static CTTwoCellAnchor createTwoCellAnchor(XSSFDrawing xssfDrawing, XSSFClientAnchor anchor) {
        CTTwoCellAnchor ctAnchor = xssfDrawing.getCTDrawing().addNewTwoCellAnchor();
        ctAnchor.setFrom(anchor.getFrom());
        ctAnchor.setTo(anchor.getTo());
        ctAnchor.addNewClientData();
        STEditAs.Enum aditAs;
        switch(anchor.getAnchorType()) {
            case DONT_MOVE_AND_RESIZE: aditAs = STEditAs.ABSOLUTE; break;
            case MOVE_AND_RESIZE: aditAs = STEditAs.TWO_CELL; break;
            case MOVE_DONT_RESIZE: aditAs = STEditAs.ONE_CELL; break;
            default: aditAs = STEditAs.ONE_CELL;
        }
        ctAnchor.setEditAs(aditAs);
        return ctAnchor;
    }
    

    public static XSSFClientAnchor createClientAnchor(XSSFWorksheetExportContext worksheetExportContext,
            Map<String,Object> withLocationContentMap){
        int x_start=(int)withLocationContentMap.get("x");
        int y_start=(int)withLocationContentMap.get("y");
        int x_end = x_start + (int)withLocationContentMap.get("width");
        int y_end = y_start + (int)withLocationContentMap.get("height");
        
        int[] columnStartOffset = worksheetExportContext.getColumnOffset(x_start); 
        int[] rowStartOffset = worksheetExportContext.getRowOffset(y_start);
        
        int rowStartIndex = rowStartOffset[0];
        int rowStartRest = rowStartOffset[1];
        int colStartIndex = columnStartOffset[0];
        int colStartRest = columnStartOffset[1];
        
        int[] columnEndOffset = worksheetExportContext.getColumnOffset(x_end);
        int[] rowEndOffset = worksheetExportContext.getRowOffset(y_end);
        
        int rowEndIndex = rowEndOffset[0];
        int rowEndRest = rowEndOffset[1];
        int colEndIndex = columnEndOffset[0];
        int colEndRest = columnEndOffset[1];
        
        int[][] pictureLocation= new int[][]{
                {colStartIndex,colStartRest,rowStartIndex,rowStartRest},
                {colEndIndex,colEndRest,rowEndIndex,rowEndRest}
        };
        CreationHelper helper = worksheetExportContext.parentContext().xssfWorkbook().getCreationHelper();
        ClientAnchor anchor = helper.createClientAnchor();
        anchor.setCol1(pictureLocation[0][0]);
        anchor.setDx1(pictureLocation[0][1] * 9525);
        anchor.setRow1(pictureLocation[0][2]);
        anchor.setDy1(pictureLocation[0][3] * 9525);
        anchor.setCol2(pictureLocation[1][0]);
        anchor.setDx2(pictureLocation[1][1] * 9525);
        anchor.setRow2(pictureLocation[1][2]);
        anchor.setDy2(pictureLocation[1][3] * 9525);
        anchor.setAnchorType(ClientAnchor.MOVE_AND_RESIZE);
        return (XSSFClientAnchor)anchor;
    }
    
    
    public static String spanToSqref(List<Integer> span,boolean withDollarSymbol){
		int startRow = span.get(1);
		int startCol = span.get(2);
		int endRow = span.get(3);
		int endCol = span.get(4);
		if(startRow == endRow && startCol == endCol)
			return CellReference.convertNumToColString(startCol-1)+startRow;
		else{
			StringBuilder sb = new StringBuilder();
			if(withDollarSymbol){
				sb.append("$");
			}
			sb.append(CellReference.convertNumToColString(startCol-1));
			if(withDollarSymbol){
				sb.append("$");
			}
			sb.append(startRow);
			sb.append(":");
			if(withDollarSymbol){
				sb.append("$");
			}
			sb.append(CellReference.convertNumToColString(endCol-1));
			if(withDollarSymbol){
				sb.append("$");
			}
			sb.append(endRow);
			return sb.toString();
		}
    }
   
    public static String toJavaString(String s){
    	if(s != null && s.length() > 0){
    		return s.replaceAll("\u00a0", " ");
    	}
    	return s;
    }
    
    public static class ExtHashMap<K, V> extends HashMap<K, V> {

        public int hashCode() {
            if (isEmpty())
                return 0;
            String[] keys = keySet().toArray(new String[0]);
            Arrays.sort(keys);

            Object[] values = new Object[keys.length];
            for (int i = 0; i < keys.length; i++) {
                Object value = get(keys[i]);
                values[i] = value;
            }
            int keyHash = Objects.hash(keys);
            int valueHash = Objects.hash(values);
            return keyHash + valueHash;
        }

    }
    
    public static class OverIndexNullValueList<E> extends ArrayList<E>{
        private static final long serialVersionUID = 1050490242961983243L;

        @Override
        public E get(int index) {
            if(index < super.size())
                return super.get(index);
            return null;
        }
       
        
    }
    

}
