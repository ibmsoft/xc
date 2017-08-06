package com.xzsoft.xsr.core.init;

/**
 * 设置报表系统中所有导入导出需要生成的临时文件的路径
 * @author ZhouSuRong
 * @date 2016-2-16
 */
public class XSRFilepath {
	/**
	 * 模板导出，模板接收文件路径
	 * 模板导出时，将zip直接放在:报表应用路径/XSRFile/modal/下面。
	 * 模板接收时，将zip文件解压到:报表应用路径/XSRFile/modal/sessionId/下面。
	 */
	public static String modalImpExpPath = "XSRFile/modal";
	/**
	 * 模板格式文件导出，导入文件路径
	 */
	public static String excelModalImpExpPath = "XSRFile/ExcelModal";
	/**
	 * 报表导出：导出远光类型文件
	 */
	public static String ygDataPath = "XSRFile/YGReport";
	/**
	 * 导出报表稽核结果
	 */
	public static String checkResultPath = "XSRFile/CheckResult/temp";
	public static String chkFreemarkerPath = "XSRFile/CheckResult/";
	public static String chkFreemarkerName = "errors.ftl";
	/**
	 * 报表导出excel压缩文件的路径
	 */
	public static String repExcelDataPath = "XSRFile/ExcelData";
}
