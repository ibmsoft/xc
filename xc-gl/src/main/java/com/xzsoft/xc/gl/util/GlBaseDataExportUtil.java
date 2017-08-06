package com.xzsoft.xc.gl.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormat;

public class GlBaseDataExportUtil {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
	/**
	 * @title:         exportGlBaseData
	 * @description:   导出总账基础数据
	 * @notice         poi的HSSFCellStyle HSSFFont等单元格式对象不能重复定义多个对象，不要频繁的创建上述对象，否则会导致数据格式错乱问题
	 * @author         tangxl
	 * @date           2016年5月23日
	 * @param          list
	 * @return         InputStream
	 * @throws         Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HSSFWorkbook exportGlBaseData(List<HashMap> dataList,
			List<String> columnNameList,List<String> codeOrderList, HashMap<String, String> columnTypeMap,
			String sheetTitle) throws Exception {
		//1、------创建工作簿和表格<表格名称为模板名称>
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet(sheetTitle);
		//2、------创建表格的标题格式
		HSSFCellStyle defaultStyle = workbook.createCellStyle();
		defaultStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont defaultFont = workbook.createFont();
		defaultFont.setFontName("宋体");
		defaultFont.setFontHeightInPoints((short)11);
		defaultFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		defaultStyle.setFont(defaultFont);
		DataFormat  defaultFormat = workbook.createDataFormat();
		defaultStyle.setDataFormat(defaultFormat.getFormat("@"));
		//2.1、----创建字符串格式
		HSSFCellStyle stringStyle = workbook.createCellStyle();
		HSSFFont stringFont = workbook.createFont();
		stringFont.setFontName("宋体");
		stringFont.setFontHeightInPoints((short)11);
		stringFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		DataFormat  stringFormat = workbook.createDataFormat();
		stringStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		stringStyle.setFont(stringFont);
		stringStyle.setDataFormat(stringFormat.getFormat("@"));
		
		//2.2、----创建数字格式
		HSSFCellStyle numberStyle = workbook.createCellStyle();
		HSSFFont numberFont = workbook.createFont();
		numberFont.setFontName("宋体");
		numberFont.setFontHeightInPoints((short)11);
		numberFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		DataFormat  numberFormat = workbook.createDataFormat();
		numberStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		numberStyle.setFont(stringFont);
		numberStyle.setDataFormat(numberFormat.getFormat("#,##0.00"));
		//2.3、----创建日期格式
		HSSFCellStyle dateStyle = workbook.createCellStyle();
		HSSFFont dateFont = workbook.createFont();
		dateFont.setFontName("宋体");
		dateFont.setFontHeightInPoints((short)11);
		dateFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		DataFormat  dateFormat = workbook.createDataFormat();
		dateStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		dateStyle.setFont(dateFont);
		dateStyle.setDataFormat(dateFormat.getFormat("yyyy-mm-dd"));
		//3、------生成标题行
		HSSFRow row = sheet.createRow(0);
		HSSFPatriarch part=sheet.createDrawingPatriarch();
		row.setHeightInPoints(28.5f);
		int i = 0;
		for(String t:columnNameList){
			HSSFCell cell = row.createCell(i);
		    sheet.setColumnWidth(i, 20 * 256);
		    sheet.setDefaultColumnStyle(i, stringStyle);
//		    cell.setCellStyle(defaultStyle);
			HSSFRichTextString text = new HSSFRichTextString(t);
			cell.setCellValue(text);
			//添加批注信息 ,前四个参数是位置，后四个参数批注框的大小
			HSSFComment comment=part.createComment(new HSSFClientAnchor(0,0,0,0,(short)1,1,(short)2,3));
			comment.setString(new HSSFRichTextString(codeOrderList.get(i)));
			cell.setCellComment(comment);
			i++;
		}
		if(dataList !=null){
			//生成数据行
			i = 1;
			for(HashMap m:dataList){
				HSSFRow dataRow = sheet.createRow(i);
				int j = 0;
				for(String c:codeOrderList){
					HSSFCell dataCell = dataRow.createCell(j);
					Object val = m.get(c);
					String colType = columnTypeMap.get(c);
					if("VARCHAR".equalsIgnoreCase(colType) || "NVARCHAR".equalsIgnoreCase(colType)){
						dataCell.setCellStyle(stringStyle);
					    if(val == null){
					    	dataCell.setCellValue(new HSSFRichTextString(""));
					    }else {
					    	dataCell.setCellValue(new HSSFRichTextString(val.toString()));
						}
					}else if("NUMERIC".equalsIgnoreCase(colType) || "DECIMAL".equalsIgnoreCase(colType)){
//						numberStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
//						numberStyle.setFont(numberFont);
//						numberStyle.setDataFormat(numberFormat.getFormat("#,##0.00"));
						dataCell.setCellStyle(stringStyle);
					    if(val == null){
					    	dataCell.setCellValue(new HSSFRichTextString(""));
					    }else {
					    	if(val instanceof BigDecimal){
					    		BigDecimal decimal = (BigDecimal) val;
					    		dataCell.setCellValue(new HSSFRichTextString(decimal.toString()));
					    	}else{
					    		dataCell.setCellValue(val.toString());
					    	}
						}
					}else if("DATETIME".equalsIgnoreCase(colType) || "DATE".equalsIgnoreCase(colType)){
//						dateStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
//						dateStyle.setFont(dateFont);
//						dateStyle.setDataFormat(dateFormat.getFormat("yyyy-mm-dd"));
						dataCell.setCellStyle(stringStyle);
						if(val == null){
							dataCell.setCellValue("");
						}else{
							dataCell.setCellValue(new HSSFRichTextString(sdf.format(val)));
						}
					}else{
						dataCell.setCellStyle(stringStyle);
					    if(val == null){
					    	dataCell.setCellValue(new HSSFRichTextString(""));
					    }else {
					    	dataCell.setCellValue(new HSSFRichTextString(val.toString()));
						}
					}
					j++;
				}
				i++;
			}
		}
		return workbook;
	}
}
