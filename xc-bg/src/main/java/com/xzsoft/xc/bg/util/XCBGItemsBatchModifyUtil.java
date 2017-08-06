package com.xzsoft.xc.bg.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormat;

import com.xzsoft.xc.bg.modal.BgItemDTO;
import com.xzsoft.xip.platform.session.CurrentSessionVar;


/**
 * @className XCBGItemsBatchModifyUtil
 * @author    tangxl
 * @date      2016年7月25日
 * @describe  合并汇总预算明细余额
 * @变动说明
 */
public class XCBGItemsBatchModifyUtil {
	public static HSSFWorkbook  getItemsExcel(String[] title,List<BgItemDTO> dataArray,String sheetTitle,String exportModel) throws Exception{
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet(sheetTitle);
		//1、创建表格的默认样式，单元格为字符串，宋体11号加粗，居中对齐
		HSSFCellStyle defaultStyle = workbook.createCellStyle();
		HSSFFont defaultFont = workbook.createFont();
		defaultFont.setFontName("宋体");
		defaultFont.setFontHeightInPoints((short)11);
		defaultStyle.setFont(defaultFont);
		DataFormat format = workbook.createDataFormat();
		defaultStyle.setDataFormat(format.getFormat("@"));
		//2、创建标题行
		HSSFRow row = sheet.createRow(0);
		for(int i=0;i<title.length;i++){
			HSSFCell cell = row.createCell(i);
			if(i ==0){
				sheet.setColumnWidth(i, 30 * 256);
			}else{
				sheet.setColumnWidth(i, 50 * 256);
			}
			// 设置单元格的内容
			String columnName = title[i];
			HSSFRichTextString text = new HSSFRichTextString(columnName);
			cell.setCellValue(text);
			sheet.setDefaultColumnStyle(i, defaultStyle);
		}
		if(dataArray != null && dataArray.size()>0){
			//3、前段记录不为空，导出所选择的记录
			//4、创建内容的默认样式，单元格为字符串，宋体11号，居左对齐
			HSSFCellStyle normalStyle = workbook.createCellStyle();
			normalStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			HSSFFont normalFont = workbook.createFont();
			normalFont.setFontName("宋体");
			normalFont.setFontHeightInPoints((short)11);
			normalStyle.setFont(normalFont);
			DataFormat normalFormat = workbook.createDataFormat();
			normalStyle.setDataFormat(normalFormat.getFormat("@"));
			int j = 1;
			if(XCBGConstants.BG_COST_TYPE.equalsIgnoreCase(exportModel)){
				for(BgItemDTO t:dataArray){
					HSSFRow dataRow = sheet.createRow(j);
					for(int k=0;k<title.length;k++){
						HSSFCell cell = dataRow.createCell(k);
						if(k ==0){
							sheet.setColumnWidth(k, 30 * 256);
						}else{
							sheet.setColumnWidth(k, 50 * 256);
						}
						sheet.setDefaultColumnStyle(k, normalStyle);
						String tmpContent = "";
						/*Excel 列说明
						 *费用项目
						 *第一列：费用项目名称
						 *第二列：会计科目
						 *第三列：预算项目
						 *第四列：是否启用
						 */
						switch (k) {
						case 0:
							tmpContent = t.getBgItemName();
							break;
						case 1:
							tmpContent = t.getBgItemDesc();
							break;
						case 2:
							tmpContent = t.getBgItemCode();
							break;
						case 3:
							tmpContent = t.getIsEnabled();
							break;
						default:
							tmpContent = "";
							break;
						}
						HSSFRichTextString text = new HSSFRichTextString(tmpContent);
						cell.setCellValue(text);
					}
					j++;
				}
			}else{
				for(BgItemDTO t:dataArray){
					HSSFRow dataRow = sheet.createRow(j);
					for(int k=0;k<title.length;k++){
						HSSFCell cell = dataRow.createCell(k);
						if(k ==0){
							sheet.setColumnWidth(k, 30 * 256);
						}else{
							sheet.setColumnWidth(k, 50 * 256);
						}
						sheet.setDefaultColumnStyle(k, normalStyle);
						String tmpContent = "";
						/*Excel 列说明
						 *项目预算 ：
						 *第一列：预算项目名称
						 *第二列：控制维度
						 *第三列：控制方式
						 *第四列：是否启用
						 *费用项目
						 *第一列：费用项目名称
						 *第二列：会计科目
						 *第三列：预算项目
						 *第四列：是否启用
						 */
						switch (k) {
						case 0:
							tmpContent = t.getBgItemName();
							break;
						case 1:
							tmpContent = t.getBgCtrlDim();
							break;
						case 2:
							tmpContent = t.getBgCtrlMode();
							break;
						case 3:
							tmpContent = t.getIsEnabled();
							break;
						default:
							tmpContent = "";
							break;
						}
						HSSFRichTextString text = new HSSFRichTextString(tmpContent);
						cell.setCellValue(text);
					}
					j++;
				}
			}
		}
		return workbook;
	}
	/**
	 * 
	 * @title:       parseIoStreamToExcel
	 * @description: 将上传文件解析成POI HSSFWorkbook对象
	 * @author       tangxl
	 * @date         2016年5月6日
	 * @param        request
	 * @return
	 * @throws       Exception
	 */
	public static HSSFWorkbook parseIoStreamToExcel(HttpServletRequest request) throws Exception{
		HSSFWorkbook workbook = null;
		List<FileItem> fileList = new ArrayList<FileItem>();
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload servletFileUpload = new ServletFileUpload(factory);
		fileList = servletFileUpload.parseRequest(request);
		if(fileList == null || fileList.size()<0){
			throw new Exception("文件上传失败！");
		}else{
			FileItem item = fileList.get(0);
			workbook = new HSSFWorkbook(item.getInputStream());
		}
		return workbook;
	}
	/**
	 * 
	 * @methodName  parseExcelToItemList
	 * @author      tangxl
	 * @date        2016年7月26日
	 * @describe    解析上传的Excel对象
	 * @param workbook
	 * @param beanType
	 * @return
	 * @throws Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public static List<HashMap<String, String>> parseExcelToItemList(HSSFWorkbook workbook,String beanType,String sessionId,String ledgerId) throws Exception{
		List<HashMap<String,String>> improtList = new ArrayList<HashMap<String,String>>();
		HSSFSheet sheet = workbook.getSheetAt(0);
		HSSFRow firstRow = sheet.getRow(0);
		int totalRowNum = sheet.getLastRowNum();
		if(totalRowNum == 0)
			throw new Exception("导入的Excel无记录！");
		int totalColNum = firstRow.getPhysicalNumberOfCells();
		//费用项目-BG01 
		//预算项目-BG02
		if(XCBGConstants.BG_COST_TYPE.equalsIgnoreCase(beanType)){
			for(int i=1;i<=totalRowNum;i++){
				HSSFRow row = sheet.getRow(i);
				if(row == null)
					continue;
				HashMap<String,String> rowMap = new HashMap<String,String>();
				String uuid =  UUID.randomUUID().toString();
				rowMap.put("TMP_ID",uuid);
				rowMap.put("ROW_NUM", "第"+i+"行");
				rowMap.put("SESSION_ID", sessionId);
				rowMap.put("CREATED_BY", CurrentSessionVar.getUserId());
				rowMap.put("LEDGER_ID", ledgerId);
				for (int j = 0; j < totalColNum; j++) {
					HSSFCell cell = row.getCell(j);
					String columnContent = getColValToString(cell);
					switch (j) {
					case 0:
						rowMap.put("EX_ITEM_NAME", columnContent);
						break;
					case 1:
						rowMap.put("ACC_NAME", columnContent);
						break;
					case 2:
						rowMap.put("BG_ITEM_NAME", columnContent);
						break;
					case 3:
						if("".equals(columnContent))
							columnContent = "Y";
						rowMap.put("IS_ENABLED", columnContent);
						break;
					default:
						break;
					}
				}
				improtList.add(rowMap);
			}
		}else{
			for(int i=1;i<=totalRowNum;i++){
				HSSFRow row = sheet.getRow(i);
				if(row == null)
					continue;
				HashMap<String,String> rowMap = new HashMap<String,String>();
				String uuid =  UUID.randomUUID().toString();
				rowMap.put("TMP_ID",uuid);
				rowMap.put("ROW_NUM", "第"+i+"行");
				rowMap.put("FLAG", "N");
				rowMap.put("SESSION_ID", sessionId);
				rowMap.put("CREATED_BY", CurrentSessionVar.getUserId());
				rowMap.put("LEDGER_ID", ledgerId);
				for (int j = 0; j < totalColNum; j++) {
					HSSFCell cell = row.getCell(j);
					String columnContent = getColValToString(cell);
					switch (j) {
					case 0:
						rowMap.put("BG_ITEM_NAME", columnContent);
						break;
					case 1:
						rowMap.put("BG_CTRL_DIM", columnContent);
						break;
					case 2:
						rowMap.put("BG_CTRL_MODE", columnContent);
						break;
					case 3:
						if("".equals(columnContent))
							columnContent = "Y";
						rowMap.put("IS_ENABLED", columnContent);
						break;
					default:
						break;
					}
				}
				improtList.add(rowMap);
			}
		}
		return improtList;
	}
	public static String getColValToString(HSSFCell cell) {
		if(cell == null)
			return "";
		Object valObject = null;
		DecimalFormat numberFormat = new DecimalFormat("0");
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING:
			valObject = cell.getStringCellValue();
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN:
			valObject = cell.getBooleanCellValue();
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:
			valObject = numberFormat.format(cell.getNumericCellValue());
			break;
		case HSSFCell.CELL_TYPE_BLANK:
			valObject = null;
			break;
		case HSSFCell.CELL_TYPE_ERROR:
			valObject = null;
			break;
		case HSSFCell.CELL_TYPE_FORMULA:
			valObject = null;
			break;
		default:
			valObject = cell.getStringCellValue();
			break;
		}
		return valObject == null? "" : valObject.toString().trim();
	}
}
