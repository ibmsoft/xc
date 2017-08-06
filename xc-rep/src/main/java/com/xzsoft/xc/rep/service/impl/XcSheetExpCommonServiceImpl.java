/**
 * 
 */
package com.xzsoft.xc.rep.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cubedrive.base.domain.document.DocumentFile;
import com.cubedrive.base.persistence.document.DocumentConfigMapper;
import com.cubedrive.sheet.domain.sheet.SheetCell;
import com.cubedrive.sheet.domain.sheet.SheetTab;
import com.cubedrive.sheet.domain.sheet.SheetTabElement;
import com.cubedrive.sheet.persistence.sheet.SheetTabMapper;
import com.cubedrive.sheet.service.sheet._export.DefaultXSSFWriteEventListener;
import com.cubedrive.sheet.service.sheet._export.SheetExportEnvironment;
import com.cubedrive.sheet.service.sheet._export.XSSFWorkbookExportlet;
import com.xzsoft.xc.rep.mapper.XCSheetCellMapper;
import com.xzsoft.xc.rep.mapper.XCSheetTabElementMapper;
import com.xzsoft.xc.rep.modal.ReportCellFormula;
import com.xzsoft.xc.rep.service.XcSheetExpCommonService;
import com.xzsoft.xc.rep.util.JsonUtil;

/**
 * @author tangxl
 *
 */
@Service("xcSheetExpCommonService")
public class XcSheetExpCommonServiceImpl implements XcSheetExpCommonService {
    @Resource
    private XCSheetCellMapper sheetCellMapper; 
    @Resource
    private XCSheetTabElementMapper sheetTabElementMapper;
    //沒有注入值，sheetTabMapper 等价于null
    private SheetTabMapper sheetTabMapper;
    //沒有注入值，documentConfigMapper 等价于null
    private DocumentConfigMapper documentConfigMapper;
    @Resource
    private XCSheetTabElementMapper xCSheetTabElementMapper;
	/* (non-Javadoc)
	 * @name     方法名
	 * @author   tangxl
	 * @date     2016年9月21日
	 * @注释                   TODO
	 * @version  1.0
	 * @see com.xzsoft.xc.rep.service.XcSheetExpCommonService#exportSheetByCondition(com.cubedrive.base.domain.document.DocumentFile, java.util.Locale)
	 */
	@Override
	public File exportSheetByCondition(DocumentFile documentFile,
			Locale userLocale) throws Exception {
		//后续查询单元格内容和格式以及合并单元格内容和格式要用到此类
		documentFile.setXcSheetExpCommonService(this);
        SheetExportEnvironment.SheetExportEnvironmentBuilder builder =
                new SheetExportEnvironment.SheetExportEnvironmentBuilder(documentFile, userLocale);
        builder.sheetTabMapper(sheetTabMapper)
        		.sheetCellMapper(sheetCellMapper)
        		.sheetTabElementMapper(sheetTabElementMapper)
        		.documentConfigMapper(documentConfigMapper);
        SheetExportEnvironment environment = builder.build();
        DefaultXSSFWriteEventListener writeEventListener = new DefaultXSSFWriteEventListener(environment);
        XSSFWorkbookExportlet exportlet = new XSSFWorkbookExportlet(environment,writeEventListener);
        File outputFile = new File(documentFile.getName());
        try(
        	BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(outputFile))){
            exportlet.export(os);
        }
        return outputFile;
	}
	/* (non-Javadoc)
	 * @name     方法名
	 * @author   tangxl
	 * @date     2016年9月21日
	 * @注释                   TODO
	 * @version  1.0
	 * @see com.xzsoft.xc.rep.service.XcSheetExpCommonService#getFixedCells(com.cubedrive.sheet.domain.sheet.SheetTab)
	 */
	@Override
	public List<SheetCell> getFixedCells(SheetTab sheetTab){
		try {
			List<SheetCell> cellList = null;
			//获取模板的所有列
			cellList = sheetCellMapper.getModalsheetFormatCellList(sheetTab.getTabId());
			//获取单元格的数据
			List<ReportCellFormula> DataList = sheetCellMapper.loadDataValue(sheetTab);
			Map<String, ReportCellFormula> fixedCellDataMap = new HashMap<String, ReportCellFormula>();
			StringBuffer fixedCellDataKey = new StringBuffer();
			//将主行数据放入到fixedCellDataMap中，key为CellData的row&col，value为CellData
			for(ReportCellFormula c : DataList){
				fixedCellDataKey.setLength(0);
				fixedCellDataKey.append(c.getRow()).append("&").append(c.getCol());
				fixedCellDataMap.put(fixedCellDataKey.toString(), c);
			}
			//循环所有的格式单元格，添加公司，期间；去除批注和excel公式，添加主行数据
			for (SheetCell msf : cellList) {
				String content = msf.getContent();
				Map<String, Object> jsonMap = JsonUtil.getJsonObj(content);
				//添加公司名称
				if(null != jsonMap.get("comment") && "getcorp".equals(jsonMap.get("comment"))) {
					String value = jsonMap.get("data") + sheetTab.getOrgName();
					jsonMap.put("data", value);
					jsonMap.remove("comment");
				}
				//添加期间
				if(null != jsonMap.get("comment") && "getperiod".equals(jsonMap.get("comment"))) {
					String value = "";
					if(jsonMap.get("data")!= null && !"null".equals(jsonMap.get("data"))){
						value +=sheetTab.getPeriodName();
					}else{
						value = sheetTab.getPeriodName();
					}
					jsonMap.put("data", value);
					jsonMap.remove("comment");
				}
				//清除批注
				if(null != jsonMap.get("comment")) {
					jsonMap.remove("comment");
				}
				//清除excel公式
				if(null != jsonMap.get("cal")){
					jsonMap.remove("cal");
					jsonMap.remove("value");
					jsonMap.put("data", "");
				}
				//给主行数据单元格赋值
				fixedCellDataKey.setLength(0);
				fixedCellDataKey.append(msf.getX()).append("&").append(msf.getY());
				if(fixedCellDataMap.containsKey(fixedCellDataKey.toString())) {
					String data = fixedCellDataMap.get(fixedCellDataKey.toString()).getJson();
					jsonMap.put("data", data);
				}
				msf.setContent(JsonUtil.toJson(jsonMap));
				msf.setCal(false);
			}
			return cellList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/* (non-Javadoc)
	 * @name     方法名
	 * @author   tangxl
	 * @date     2016年9月21日
	 * @注释                   TODO
	 * @version  1.0
	 * @see com.xzsoft.xc.rep.service.XcSheetExpCommonService#findElementsByTabId(java.lang.String)
	 */
	@Override
	public List<SheetTabElement> findElementsByTabId(String tabId){
		try {
			return xCSheetTabElementMapper.findElementsByTab(tabId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
