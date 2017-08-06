package com.xzsoft.xc.gl.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormat;

import com.xzsoft.xc.gl.modal.AccountTemplate;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xip.platform.util.XipUtil;

public class AccountTemplateUtil {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	@SuppressWarnings("serial")
	private final static HashMap<String, Integer> modelMap = new HashMap<String, Integer>(){
		//具有树形结构的模板，需要上级编码的模板UP_CODE,科目表中包含了UP_ACC_CODE
		{
			put("XC_GL_CASH_ITEMS", 1);
			put("XC_PM_PROJECTS", 1);
			put("XC_GL_LD_CUST_ASS_SEGVALS", 1);
		    put("XC_EX_ITEMS", 1);
		    put("XC_BG_ITEMS", 1);
		}
	};
	/**
	 * @title:         createTemplateByModel
	 * @description:   根据模板配置信息返回总账的Excel模板
	 * @author         tangxl
	 * @date           2016年5月5日
	 * @param          list
	 * @return         InputStream
	 * @throws         Exception
	 */
	public static HSSFWorkbook createTemplateByModel(List<AccountTemplate> list,String sheetTitle,String templateCode) throws Exception{
		//1、------创建工作簿和表格<表格名称为模板名称>
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet(sheetTitle);
		//2、------创建表格的默认样式
		//sheet.setDefaultColumnWidth(20);
		HSSFCellStyle defaultStyle = workbook.createCellStyle();
		defaultStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont defaultFont = workbook.createFont();
		//宋体加粗11号
		defaultFont.setFontName("宋体");
		defaultFont.setFontHeightInPoints((short)11);
		defaultFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		defaultStyle.setFont(defaultFont);
		//3、------创建表格的标题行<生成模板>，标题行的内容为字段的别名，标题行的批注为字段的编码，别名一定会存在，并根据模板字段列的类型，设置列的格式
		//当前只处理3中数据类型 VARCHAR,NVARCHAR,DATETIME,DATE,NUMERIC,DECIMAL
		//创建绘图对象<添加批注>
		HSSFPatriarch part=sheet.createDrawingPatriarch();
		HSSFRow row = sheet.createRow(0);
		row.setHeightInPoints(28.5f);
		int i = 0;
		for (AccountTemplate t : list) {
			HSSFCell cell = row.createCell(i);
			// 格式处理，所有的格式一律按照字符串来进行
			DataFormat format = workbook.createDataFormat();
			sheet.setColumnWidth(i, 20 * 256);
			HSSFCellStyle style1 = workbook.createCellStyle();
			style1.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			HSSFFont font1 = workbook.createFont();
			// 宋体加粗11号
			font1.setFontName("宋体");
			font1.setFontHeightInPoints((short) 11);
			style1.setFont(font1);
			format = workbook.createDataFormat();
			style1.setDataFormat(format.getFormat("@"));
			sheet.setDefaultColumnStyle(i, style1);
			// 设置单元格的内容
			String columnName = t.getColumnAlias();
			if (columnName == null || "".equals(columnName)) {
				columnName = t.getColumnName();
			}
			HSSFRichTextString text = new HSSFRichTextString(columnName);
			cell.setCellValue(text);
			// 添加批注信息 ,前四个参数是位置，后四个参数批注框的大小
			HSSFComment comment = part.createComment(new HSSFClientAnchor(0, 0,
					0, 0, (short) 1, 1, (short) 2, 3));
			comment.setString(new HSSFRichTextString(t.getColumnCode()));
			cell.setCellComment(comment);
			i++;
		}
		//若导出模板为 科目信息、现金流项目、项目信息、辅助段管理，需手动添加上级编码 UP_CODE列 格式为文本
		if(modelMap.containsKey(templateCode)){
			//遍历模板最后一列后，i已经自增
			HSSFCell cell = row.createCell(i);
			DataFormat  format = workbook.createDataFormat();
			sheet.setColumnWidth(i, 20 * 256);
			HSSFCellStyle style = workbook.createCellStyle();
			style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			HSSFFont font = workbook.createFont();
			//宋体加粗11号
			font.setFontName("宋体");
			font.setFontHeightInPoints((short)11);
			style.setFont(font);
			format = workbook.createDataFormat();
			style.setDataFormat(format.getFormat("@"));
			sheet.setDefaultColumnStyle(i,style);	
			//设置单元格的内容
			HSSFRichTextString text = new HSSFRichTextString("上级编码");
			cell.setCellValue(text);
			//添加批注信息 ,前四个参数是位置，后四个参数批注框的大小
			HSSFComment comment=part.createComment(new HSSFClientAnchor(0,0,0,0,(short)1,1,(short)2,3));
			comment.setString(new HSSFRichTextString("UP_CODE"));
			cell.setCellComment(comment);
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
			//throw new Exception("文件上传失败！");
			throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_WJSCSB", null));
		}else{
			FileItem item = fileList.get(0);
			workbook = new HSSFWorkbook(item.getInputStream());
		}
		return workbook;
	}
	/**
	 * 
	 * @title:       checkTemplateChanges
	 * @description: 校验上传的模板与数据库模板信息是否有差异，存在差异时抛出异常，无差异返回key为列名，value为列类型的HashMap
	 * <p>
	 *   树形结构模板校验时，剔除额外添加列UP_CODE
	 * </p>
	 * @author       tangxl
	 * @date         2016年5月6日
	 * @param        workbook
	 * @param        list
	 * @return       map
	 * @throws       Exception
	 */
	public static HashMap<String, String> checkTemplateChanges(HSSFWorkbook workbook,List<AccountTemplate> list,String modelName) throws Exception{
		HashMap<String, String> map = new HashMap<String, String>();
		HSSFSheet excelSheet = workbook.getSheetAt(0);
		//获取标题行
		HSSFRow firstRow = excelSheet.getRow(0);
		//获取列数
		int totalCellNum = firstRow.getPhysicalNumberOfCells();
		//获取行数
		int totalRowNum = excelSheet.getLastRowNum();
		//列数不一致
		if(totalCellNum != list.size()){
			if(modelMap.containsKey(modelName) && totalCellNum !=list.size()+1){
				//throw new Exception("导入的Excel模板与系统定义的数据模板列数不一致！");
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_DRDEMB", null));
			}else if(!modelMap.containsKey(modelName)){
				//throw new Exception("导入的Excel模板与系统定义的数据模板列数不一致！");
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_DRDEMB", null));
			}
		}else if(totalRowNum == 0){
			//throw new Exception("Excel数据为空！");
			throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_ESJWK", null));
		}
	    //校验导入模板的列与系统模板的列是否一致
		List<String> sysColList = new ArrayList<String>();
		List<String> sysColTypeList = new ArrayList<String>();
		List<String> templateList = new ArrayList<String>();
		//获得列编码columnCode和列类型columnType
		for(AccountTemplate t:list){
			sysColList.add(t.getColumnCode());
			sysColTypeList.add(t.getColumnType());
			map.put(t.getColumnCode(), t.getColumnType());
		}
		for(int i=0;i<totalCellNum;i++){
			HSSFCell cell = firstRow.getCell(i);
			templateList.add(cell.getCellComment().getString().getString());
		}
		if(modelMap.containsKey(modelName))
			map.put("UP_CODE", "VARCHAR");
		if("XC_PM_PROJECTS".equalsIgnoreCase(modelName) || "XC_GL_LD_CUST_ASS_SEGVALS".equalsIgnoreCase(modelName)||"XC_GL_LD_CUST_ASS_SEGVALS".equalsIgnoreCase(modelName))
			map.put("LEDGER_ID", "VARCHAR");
		if("XC_GL_ACCOUNTS".equalsIgnoreCase(modelName)){
			map.put("ACC_CATEGORY_CODE", "VARCHAR");
			map.put("ACC_HRCY_ID", "VARCHAR");
		}
		if("XC_GL_LD_CUST_ASS_SEGVALS".equalsIgnoreCase(modelName))
			map.put("SEG_CODE", "VARCHAR");
		//费用项目和预算项目需要指定其 费用项目体系和预算项目体系
		if("XC_EX_ITEMS".equalsIgnoreCase(modelName))
			map.put("EX_HRCY_ID", "VARCHAR");
		if("XC_BG_ITEMS".equalsIgnoreCase(modelName))
			map.put("BG_HRCY_ID", "VARCHAR");
		//比较模板是否一致,非树形结构模板，列名必须一致，树形结构模板，Excel列只会比模板列多出一列UP_CODE列
		templateList.removeAll(sysColList);
		if(!modelMap.containsKey(modelName) && templateList.size() !=0){
			//throw new Exception("导入的Excel模板与系统定义数据模板的列名不一致");
			throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_DRDEMB1", null));
		}else if(modelMap.containsKey(modelName) && templateList.size() !=1){
			//throw new Exception("导入的Excel模板与系统定义数据模板的列名不一致");
			throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_DRDEMB1", null));
		}
		return map;
	}
	/**
	 * 
	 * @title:       convertExcelToList
	 * @description: 将Excel转换成对应的数据集合
	 * @author       tangxl
	 * @date         2016年5月9日
	 * @param        workbook --- Excel表单
	 * @param        map      --- 模板列和对象
	 * @param        sessionId--- 会话ID
	 * @return       List<HashMap>
	 * @throws       Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<HashMap> convertExcelToList(HSSFWorkbook workbook,HashMap<String, String> map, String sessionId,List<String> colNameList,String modelName,HashMap<String, String> paramsMap,int cashItemOrder)
			throws Exception {
		List<HashMap> dataList = new ArrayList<HashMap>();
		List<String> templateColList = new ArrayList<String>();
		//获取表单,只会有一个表单
		HSSFSheet sheet = workbook.getSheetAt(0);
		//从标题行中读取列编码
		HSSFRow firstRow = sheet.getRow(0);
		int totalRowNum = sheet.getLastRowNum();
		int totalColNum = firstRow.getPhysicalNumberOfCells();
		//取得模板对应的列编码
		for(int i=0;i<totalColNum;i++){
			HSSFCell cell = firstRow.getCell(i);
			templateColList.add(cell.getCellComment().getString().getString());
		}
		//遍历Excel文件
		for(int k = 1;k<=totalRowNum;k++){
			HSSFRow row = sheet.getRow(k);
			if(row == null)
				continue;
			HashMap rowMap = new HashMap();
			int hRow = k+1;
			for (int j = 0; j < totalColNum; j++) {
				HSSFCell cell = row.getCell(j);
				String colName = templateColList.get(j);
				// 获得Excel列在数据库中的类型
				String colType = map.get(colName);
				// Excel存储日期、时间均以数字的形式来存储，判断一个单元格的内容是否为日期，需先判断其是否为数字,日期格式 yyyy-MM-dd
				try {
					setColValue(rowMap,colType,cell,colName);
				} catch (Exception e) {
					e.printStackTrace();
					//throw new Exception("第"+hRow+"行，第"+(j+1)+"列数据格式不正确,原因:"+e.getMessage());
					HashMap<String, String> params = new HashMap<String, String>();
					params.put("param1", String.valueOf(hRow));
					params.put("param2", String.valueOf((j+1)));
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_D_HD_LSJGSBZQ", params));
				}
			}
			//数据校验标识、会话ID、数据行号以及临时主键
			rowMap.put("FLAG", "N");
			rowMap.put("SESSION_ID", sessionId);
			rowMap.put("ROW_NUM", "第"+hRow+"行");
			rowMap.put("TMP_ID", UUID.randomUUID().toString());
			//树形结构的数据，根据不同的表添加不同的必填项
			if("XC_PM_PROJECTS".equalsIgnoreCase(modelName)){
				rowMap.put("LEDGER_ID", paramsMap.get("LEDGER_ID"));
				rowMap.put("UP_PROJECT_ID", "-1");
				}
			if("XC_GL_ACCOUNTS".equalsIgnoreCase(modelName)){
				rowMap.put("ACC_HRCY_ID", paramsMap.get("ACC_HRCY_ID"));
				if("-1".equals(rowMap.get("UP_ACC_CODE").toString())){
					rowMap.put("ACC_LEVEL", 1);
					rowMap.put("IS_LEAF", "Y");
				}else{
					if(!map.containsKey("ACC_LEVEL"))
						rowMap.put("ACC_LEVEL", 0);
					rowMap.put("IS_LEAF", "Y");
				}
			}
			if("XC_GL_LD_CUST_ASS_SEGVALS".equalsIgnoreCase(modelName)){
				rowMap.put("SEG_CODE", paramsMap.get("SEG_CODE"));
				rowMap.put("LEDGER_ID", paramsMap.get("LEDGER_ID"));
				}
			//设置 费用项目和预算项目的  项目体系和父级ID
			if("XC_EX_ITEMS".equalsIgnoreCase(modelName)){
				rowMap.put("EX_HRCY_ID", paramsMap.get("EX_HRCY_ID"));
				rowMap.put("EX_ITEM_UP_ID", "-1");
				}
			if("XC_BG_ITEMS".equalsIgnoreCase(modelName)){
				rowMap.put("BG_HRCY_ID", paramsMap.get("BG_HRCY_ID"));
				rowMap.put("BG_ITEM_UP_ID", "-1");
				}
			//计算现金流项目的的排序号
			if("XC_GL_CASH_ITEMS".equalsIgnoreCase(modelName) && !map.containsKey("ORDERBY"))
				rowMap.put("ORDERBY", cashItemOrder++);
			//上级编码为-1是，层级默认为1 up_id默认为-1,IS_LEAF 默认为Y

			if(modelMap.containsKey(modelName) && "-1".equals(rowMap.get("UP_CODE").toString())){
				if("XC_GL_CASH_ITEMS".equalsIgnoreCase(modelName)){
					rowMap.put("CA_LEVEL", 1);
					rowMap.put("UP_ID", "-1");
					rowMap.put("IS_LEAF", "Y");
				}
				if("XC_GL_LD_CUST_ASS_SEGVALS".equalsIgnoreCase(modelName)){
					rowMap.put("SEGVAL_LEVEL", 1);
					rowMap.put("UP_CUST_SEGVAL_ID", "-1");
					rowMap.put("IS_LEAF", "Y");
				}
			}else{
				if("XC_GL_CASH_ITEMS".equalsIgnoreCase(modelName) && !map.containsKey("CA_LEVEL")){
					rowMap.put("CA_LEVEL", 0);
					rowMap.put("UP_ID", "-1");
					rowMap.put("IS_LEAF", "Y");
				}
				if("XC_GL_LD_CUST_ASS_SEGVALS".equalsIgnoreCase(modelName) && !map.containsKey("SEGVAL_LEVEL"))
					rowMap.put("SEGVAL_LEVEL", 0);
				    rowMap.put("UP_CUST_SEGVAL_ID", "-1");
				    rowMap.put("IS_LEAF", "Y");
			}
			dataList.add(rowMap);
		}
		//复制列名，将临时表的4个必填字段追加到插入列中
		colNameList.addAll(templateColList);
		colNameList.add("FLAG");
		colNameList.add("SESSION_ID");
		colNameList.add("ROW_NUM");
		colNameList.add("TMP_ID");
		//将追加列的类型添加到类型map中
		map.put("FLAG", "VARCHAR");
		map.put("SESSION_ID", "VARCHAR");
		map.put("ROW_NUM", "VARCHAR");
		map.put("TMP_ID", "VARCHAR");
		if("XC_PM_PROJECTS".equalsIgnoreCase(modelName)){
			colNameList.add("LEDGER_ID");
			map.put("LEDGER_ID", "VARCHAR");
			colNameList.add("UP_PROJECT_ID");
			map.put("UP_PROJECT_ID", "VARCHAR");
			}
		if("XC_GL_ACCOUNTS".equalsIgnoreCase(modelName)){
			colNameList.add("IS_LEAF");
			map.put("IS_LEAF", "VARCHAR");
			colNameList.add("ACC_HRCY_ID");
			map.put("ACC_HRCY_ID", "VARCHAR");
			if(!map.containsKey("ACC_LEVEL")){
				colNameList.add("ACC_LEVEL");
				map.put("ACC_LEVEL", "INTEGER");
			}
		}
		if("XC_GL_LD_CUST_ASS_SEGVALS".equalsIgnoreCase(modelName)){
			colNameList.add("LEDGER_ID");
			map.put("LEDGER_ID", "VARCHAR");
			colNameList.add("SEG_CODE");
			map.put("SEG_CODE", "VARCHAR");
			if(!map.containsKey("SEGVAL_LEVEL")){
				colNameList.add("SEGVAL_LEVEL");
				map.put("SEGVAL_LEVEL", "INTEGER");
			}
			colNameList.add("UP_CUST_SEGVAL_ID");
			map.put("UP_CUST_SEGVAL_ID", "VARCHAR");
			colNameList.add("IS_LEAF");
			map.put("IS_LEAF", "VARCHAR");
		}
		//预算项目和费用项目的  项目体系以及上级字段
		if("XC_EX_ITEMS".equalsIgnoreCase(modelName)){
			colNameList.add("EX_HRCY_ID");
			map.put("EX_HRCY_ID", "VARCHAR");
			colNameList.add("EX_ITEM_UP_ID");
			map.put("EX_ITEM_UP_ID", "VARCHAR");
			}
		if("XC_BG_ITEMS".equalsIgnoreCase(modelName)){
			colNameList.add("BG_HRCY_ID");
			map.put("BG_HRCY_ID", "VARCHAR");
			colNameList.add("BG_ITEM_UP_ID");
			map.put("BG_ITEM_UP_ID", "VARCHAR");
			}
		//添加现金流项目的排序号
		if("XC_GL_CASH_ITEMS".equalsIgnoreCase(modelName) && !map.containsKey("ORDERBY")){
			colNameList.add("ORDERBY");
			map.put("ORDERBY", "INTEGER");
			if(!map.containsKey("CA_LEVEL")){
				colNameList.add("CA_LEVEL");
				map.put("CA_LEVEL", "INTEGER");
			}
			colNameList.add("UP_ID");
			map.put("UP_ID", "VARCHAR");
			colNameList.add("IS_LEAF");
			map.put("IS_LEAF", "VARCHAR");
		}
		return dataList;
	}
	/**
	 * @title:      setColValue
	 * @description:单元格赋值
	 * @author      tangxl
	 * @date        2016年5月9日
	 * @param       rowMap
	 * @param       colType
	 * @param       cell
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void setColValue(HashMap rowMap,String colType,HSSFCell cell,String colName) throws Exception{
		//--- 列为空
		if(cell == null){
			if("VARCHAR".equalsIgnoreCase(colType) || "NVARCHAR".equalsIgnoreCase(colType)){
				rowMap.put(colName, "");
			}else if("NUMERIC".equalsIgnoreCase(colType) || "DECIMAL".equalsIgnoreCase(colType)){
				rowMap.put(colName, 0.0000);
			}else if("TIMESTAMP".equalsIgnoreCase(colType) || "DATETIME".equalsIgnoreCase(colType) || "DATE".equalsIgnoreCase(colType)){
				if(colName.startsWith("END")){
					rowMap.put(colName, sdf.parse("9999-12-31 23:59:59"));
				}else{
					rowMap.put(colName, new Date());
				}
			}else{
				//当成字符串处理
				rowMap.put(colName, "");
			}
		}else{
			DecimalFormat numberFormat = new DecimalFormat("0");
			//根据列类型获取对应的数据
			Object valObject = null;
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
			String colVal = "";
			if(valObject != null)
				colVal = valObject.toString();
			if(colVal == null || "".equals(colVal)){
				if("VARCHAR".equalsIgnoreCase(colType) || "NVARCHAR".equalsIgnoreCase(colType)){
					rowMap.put(colName, "");
				}else if("NUMERIC".equalsIgnoreCase(colType) || "DECIMAL".equalsIgnoreCase(colType)){
					rowMap.put(colName, 0.0000);
				}else if("TIMESTAMP".equalsIgnoreCase(colType) || "DATETIME".equalsIgnoreCase(colType) || "DATE".equalsIgnoreCase(colType)){
					if(colName.startsWith("END")){
						rowMap.put(colName, sdf.parse("9999-12-31 23:59:59"));
					}else{
						rowMap.put(colName, new Date());
					}
				}else{
					//当成字符串处理
					rowMap.put(colName, "");
				}
			}else{
				//数字类型统一使用double处理
				if("NUMERIC".equalsIgnoreCase(colType) || "DECIMAL".equalsIgnoreCase(colType)){
					rowMap.put(colName,Double.valueOf(colVal));
				}else if("TIMESTAMP".equalsIgnoreCase(colType) || "DATETIME".equalsIgnoreCase(colType) || "DATE".equalsIgnoreCase(colType)){
					if(colName.startsWith("END")){
						rowMap.put(colName, sdf.parse(colVal+" 23:59:59"));
					}else{
						rowMap.put(colName,sdf.parse(colVal+" 00:00:00"));
					}
				}else{
					rowMap.put(colName,colVal);
				}
				
			}
		}
	}
}
