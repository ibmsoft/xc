package com.xzsoft.xc.gl.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * @ClassName: XCGLBalanceUtil 
 * @Description: TODO(余额处理类) 
 * @author tangxl 
 * @date 2016年2月22日 上午10:51:56
 */
public class XCGLBalanceUtil {
	/**
	 * 
	 * @Title: exprotBalance 
	 * @Description: TODO(导出余额Excel) 
	 * @param balanceList
	 * @return InputStream    返回类型 
	 * @throws Exception    设定文件 
	 * @auther tangxl
	 * @throws Exception
	 */
	public static InputStream exprotBalance(List<HashMap> balanceList) throws Exception{
		InputStream inputStream = null;
		//创建Excel03工作簿
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
		//生成表格
		HSSFSheet hssfSheet = hssfWorkbook.createSheet();
		HSSFFont font = hssfWorkbook.createFont();   
		// font.setColor(HSSFFont.COLOR_RED);   
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);   
		font.setFontHeightInPoints((short) 12);
		//设置单元格居中对齐
		HSSFCellStyle hssfCs = hssfWorkbook.createCellStyle();
		hssfCs.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		hssfCs.setFont(font);
		HSSFCellStyle hssfCsR = hssfWorkbook.createCellStyle();
		hssfCsR.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		hssfCsR.setFont(font);
		//设置单元格的默认宽度
		hssfSheet.setDefaultColumnWidth(12);
		//创建标题行（前两行）
		HSSFRow row1 = hssfSheet.createRow(0);
		HSSFRow row2 = hssfSheet.createRow(1);
		HSSFCell megerCell = null;
		HSSFCell normalCell = null;
		String[] headers = {"科目","","供应商","","客户","","产品","","内部往来","","个人往来","","成本中心","","项目","","自定义1","","自定义2","","自定义3","","自定义4","",};
		//创建33列
		for(int i = 0;i<33;i++){
			megerCell = row1.createCell(i);
			normalCell = row2.createCell(i);
			//设置单元格靠右对齐方式
			if(i<29){
				megerCell.setCellStyle(hssfCs);
				if(i%2 == 0){
					megerCell.setCellValue(headers[i]);
					normalCell.setCellValue("名称");
				}else{
					normalCell.setCellValue("编码");
				}
			}else{
				megerCell.setCellStyle(hssfCsR);
				if(i%3 == 0){
					normalCell.setCellValue("数量");
					if(i == 24){
						megerCell.setCellValue("累计借方");
					}else if(i == 27){
						megerCell.setCellValue("累计贷方");
					}else if(i == 30){
						megerCell.setCellValue("期初余额");
					}
				}else if(i%3 == 1){
					normalCell.setCellValue("原币");
				}else if(i%3 == 2){
					normalCell.setCellValue("金额");
				}
			}			
		}
		//开始合并前24列的单元格<org.apache.poi.ss.util.CellRangeAddress>
		//org.apache.poi.hssf.util.CellRangeAddress 继承了 org.apache.poi.ss.util.CellRangeAddress，前者已为deprecated
		CellRangeAddress rAddress = new CellRangeAddress(0, 0, 0, 1);
		hssfSheet.addMergedRegion(rAddress);
		rAddress = new CellRangeAddress(0, 0, 2, 3);
		hssfSheet.addMergedRegion(rAddress);
		rAddress = new CellRangeAddress(0, 0, 4, 5);
		hssfSheet.addMergedRegion(rAddress);
		rAddress = new CellRangeAddress(0, 0, 6, 7);
		hssfSheet.addMergedRegion(rAddress);
		rAddress = new CellRangeAddress(0, 0, 8, 9);
		hssfSheet.addMergedRegion(rAddress);
		rAddress = new CellRangeAddress(0, 0, 10, 11);
		hssfSheet.addMergedRegion(rAddress);
		rAddress = new CellRangeAddress(0, 0, 12, 13);
		hssfSheet.addMergedRegion(rAddress);
		rAddress = new CellRangeAddress(0, 0, 14, 15);
		hssfSheet.addMergedRegion(rAddress);
		rAddress = new CellRangeAddress(0, 0, 16, 17);
		hssfSheet.addMergedRegion(rAddress);
		rAddress = new CellRangeAddress(0, 0,18, 19);
		hssfSheet.addMergedRegion(rAddress);
		rAddress = new CellRangeAddress(0, 0, 20, 21);
		hssfSheet.addMergedRegion(rAddress);
		rAddress = new CellRangeAddress(0, 0, 22, 23);
		hssfSheet.addMergedRegion(rAddress);
		rAddress = new CellRangeAddress(0, 0, 24, 26);
		hssfSheet.addMergedRegion(rAddress);
		rAddress = new CellRangeAddress(0, 0, 27, 29);
		hssfSheet.addMergedRegion(rAddress);
		rAddress = new CellRangeAddress(0, 0, 30, 32);
		hssfSheet.addMergedRegion(rAddress);
		//填充余额内容
		for(int j = 0;j<balanceList.size();j++){
			row1 = hssfSheet.createRow(j+2);
			//迭代HashMap
		}
		return inputStream;
	}

}
