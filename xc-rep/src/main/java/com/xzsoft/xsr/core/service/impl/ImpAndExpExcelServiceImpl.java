package com.xzsoft.xsr.core.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cubedrive.base.domain.document.DocumentFile;
import com.cubedrive.base.persistence.document.DocumentConfigMapper;
import com.cubedrive.sheet.domain.sheet.SheetCell;
import com.cubedrive.sheet.domain.sheet.SheetTab;
import com.cubedrive.sheet.domain.sheet.SheetTabElement;
import com.cubedrive.sheet.persistence.sheet.SheetStyleMapper;
import com.cubedrive.sheet.persistence.sheet.SheetTabMapper;
import com.cubedrive.sheet.service.sheet._export.DefaultXSSFWriteEventListener;
import com.cubedrive.sheet.service.sheet._export.SheetExportEnvironment;
import com.cubedrive.sheet.service.sheet._export.XSSFWorkbookExportlet;
import com.cubedrive.sheet.service.sheet._import.DefaultXSSFReadEventListener;
import com.cubedrive.sheet.service.sheet._import.SheetImportEnvironment;
import com.cubedrive.sheet.service.sheet._import.xssf.XSSFReadEventListener;
import com.cubedrive.sheet.service.sheet._import.xssf.XSSFReadEventWorkbook;
import com.xzsoft.xip.platform.util.PlatformUtil;
import com.xzsoft.xsr.core.mapper.FjMapper;
import com.xzsoft.xsr.core.mapper.MakeReportMapper;
import com.xzsoft.xsr.core.mapper.ModalsheetMapper;
import com.xzsoft.xsr.core.mapper.XSRSheetTabElementMapper;
import com.xzsoft.xsr.core.mapper.XSRSheetCellMapper;
import com.xzsoft.xsr.core.modal.CellData;
import com.xzsoft.xsr.core.modal.Colitem;
import com.xzsoft.xsr.core.service.FjService;
import com.xzsoft.xsr.core.service.ImpAndExpExcelService;
import com.xzsoft.xsr.core.util.JsonUtil;

/**
 * excel文件导入导出
 * 主要操作excel文件与EnterpriseSheet格式转换。
 * @author ZhouSuRong
 * @date 2016-2-19
 */
@Service
public class ImpAndExpExcelServiceImpl implements ImpAndExpExcelService {
	private static Logger log = Logger.getLogger(ImpAndExpExcelServiceImpl.class.getName());
    
    @Autowired
    private XSRSheetCellMapper sheetCellMapper; //继承ES的SheetCellMapper，重写相关方法
    @Autowired
    private XSRSheetTabElementMapper sheetTabElementMapper;//继承ES的SheetTabElementMapper，重写相关方法
    
    private SheetTabMapper sheetTabMapper; //实际没有赋值，以为报表3.0不需要sheetTab的转换
    private DocumentConfigMapper documentConfigMapper; //实际没有赋值，以为报表3.0不需要document的转换
    private SheetStyleMapper sheetStyleMapper; //实际没有赋值，以为报表3.0不需要sheetStyle的转换
    
    @Autowired
	private ModalsheetMapper modalsheetMapper;
    @Autowired
    private MakeReportMapper makeReportMapper;
    @Autowired
    private FjService fjService;
    @Autowired
    private FjMapper fjMapper;

    /**
     * 接收excel文件，保存为EnterpriseSheet需要的形式
     */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void importXlsx(DocumentFile documentFile, File xlsxFile, Locale userLocale) throws Exception {
		
		if (modalsheetMapper.isModaLFormatExist(documentFile.getMODALFORMAT_ID().toString()) > 0) {
			// 如果模板格式已经存在，则删除模板格式，再插入
			modalsheetMapper.deleleModalsheetFormat(documentFile.getMODALFORMAT_ID().toString());
			modalsheetMapper.deleleModalsheetElement(documentFile.getMODALFORMAT_ID().toString());
		}
		/**
		 * 调用ES代码导入excel文件
		 */
		
		SheetImportEnvironment.SheetImportEnvironmentBuilder builder = new SheetImportEnvironment.SheetImportEnvironmentBuilder(
				documentFile, xlsxFile, userLocale);
		builder.sheetCellMapper(sheetCellMapper)
				.sheetTabMapper(sheetTabMapper)
				.sheetTabElementMapper(sheetTabElementMapper)
				.sheetStyleMapper(sheetStyleMapper)
				.documentConfigMapper(documentConfigMapper);
		builder.cellTable("xsr_rep_modalsheet_format");//ES中需要配置保存模板格式的表，但在报表3.0中可以不设置，因为我们在sql中写好了表名
		SheetImportEnvironment environment = builder.build();
		XSSFReadEventListener xssfReadEventListener = new DefaultXSSFReadEventListener(environment);
		XSSFReadEventWorkbook xssfReadEventWorkbook = null;
		try (InputStream in = new BufferedInputStream(new FileInputStream(xlsxFile))) {
			xssfReadEventWorkbook = new XSSFReadEventWorkbook(in,xssfReadEventListener);
			xssfReadEventWorkbook.readDocument();
		} catch (Exception e) {
			log.error("模板上传出错", e);
			throw e;
		} finally {
			if (xssfReadEventWorkbook != null) {
				xssfReadEventWorkbook.close();
			}
		}
	}
	/**
	 * 模板格式设置-下载模板
	 * 报表导出-导出excel文件
	 * 都调用该方法
	 */
	@Override
	public File exportSpreadsheet(DocumentFile documentFile, Locale userLocale)
			throws Exception {
		documentFile.setImpAndExpExcelService(this);
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
        try(BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(outputFile))){
            exportlet.export(os);
        }
        return outputFile;
	}
	
	/**
	 * 报表导出-查询固定行数据及格式
	 */
	@Override
	public List<SheetCell> getFixedCells(SheetTab sheetTab) {
		List<SheetCell> cellList = null;
		try {
			//获取模板的所有格式
			cellList = sheetCellMapper.getModalsheetFormatCellList(sheetTab.getId().toString());//获取主表格式
			//获取主行数据
			List<CellData> DataList = sheetCellMapper.loadDataValue(sheetTab.getSuitId(), sheetTab.getId().toString(),
					sheetTab.getOrgId(), sheetTab.getPeriodId(), sheetTab.getCnyId(), PlatformUtil.getDbType(),sheetTab.getLedgerId());
			Map<String, CellData> fixedCellDataMap = new HashMap<String, CellData>();
			StringBuffer fixedCellDataKey = new StringBuffer();
			//将主行数据放入到fixedCellDataMap中，key为CellData的row&col，value为CellData
			for(CellData c : DataList){
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
					String value = jsonMap.get("data") + sheetTab.getPeriodName();
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
		} catch(Exception e) {
			log.error("报表导出excel文件出错", e);
		}
		return cellList;
	}
	
	/**
	 * 报表导出-查询固定行及浮动行数据及格式
	 */
	@Override
	public List<SheetCell> getFixedAndFloatCells(SheetTab sheetTab) {
		List<SheetCell> newcellList = new ArrayList<SheetCell>();
		List<SheetCell> cellList = null;
		try {
			Integer rownoY = null;
			//模板主行的所有单元格存在到map中，存放形式:key为x位置，value为List<SheetCell>;一行上单元格对应一个map的一个key
			Map<Integer, List<SheetCell>> modalCellMap = new HashMap<Integer, List<SheetCell>>();
			//获取模板的所有格式
			cellList = sheetCellMapper.getModalsheetFormatCellList(sheetTab.getId().toString());
			//获取主行数据
			List<CellData> DataList = sheetCellMapper.loadDataValue(sheetTab.getSuitId(), sheetTab.getId().toString(),
					sheetTab.getOrgId(), sheetTab.getPeriodId(), sheetTab.getCnyId(), PlatformUtil.getDbType(),sheetTab.getLedgerId());
			Map<String, CellData> fixedCellDataMap = new HashMap<String, CellData>();
			StringBuffer fixedCellDataKey = new StringBuffer();
			//将主行数据放入到fixedCellDataMap中，key为CellData的row&col，value为CellData
			for(CellData c : DataList){
				fixedCellDataKey.setLength(0);
				fixedCellDataKey.append(c.getRow()).append("&").append(c.getCol());
				fixedCellDataMap.put(fixedCellDataKey.toString(), c);
			}

			//循环所有的格式单元格，添加公司，期间；去除批注和excel公式，主行添加数据
			for (SheetCell msf : cellList) {
				Integer keyX = msf.getX();
				String content = msf.getContent();
				Map<String, Object> jsonMap = JsonUtil.getJsonObj(content);
				//判断rowno批注的Y位置
				if(null == rownoY) {
					if(null != jsonMap.get("comment") && "rowno".equals(jsonMap.get("comment"))) {
						rownoY = msf.getY();
						jsonMap.remove("comment");
					}
				}
				//添加公司名称
				if(null != jsonMap.get("comment") && "getcorp".equals(jsonMap.get("comment"))) {
					String value = jsonMap.get("data") + sheetTab.getOrgName();
					jsonMap.put("data", value);
					jsonMap.remove("comment");
				}
				//添加期间
				if(null != jsonMap.get("comment") && "getperiod".equals(jsonMap.get("comment"))) {
					String value = jsonMap.get("data") + sheetTab.getPeriodName();
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
				//将主行单元格放入map中
				if(modalCellMap.containsKey(keyX)) {
					modalCellMap.get(keyX).add(msf);
				} else {
					List<SheetCell> modalCellList = new ArrayList<SheetCell>();
					modalCellList.add(msf);
					modalCellMap.put(keyX, modalCellList);
				}
			}
			
			//处理浮动行
			//是浮动行的行指标:行指标id,行指标code,X位置,map中的key：ROWITEM_ID，ROWITEM_CODE，ROW
			List<Map<String, Object>> fjRowitemList = sheetCellMapper.getFjRowitemInfo(sheetTab.getSuitId(), 
					sheetTab.getModaltypeId(), sheetTab.getModalsheetId());
			//浮动行DATA列与列指标映射
			List<Colitem> fjColSetList = fjMapper.getFjColitemList(sheetTab.getSuitId(), sheetTab.getModalsheetId(), 
					sheetTab.getModaltypeId());
			//获取模版下所有的浮动行数据,map中的key: ROWNUMDTL,ROWITEM_ID,ROWITEM_CODE,ROWNO,DATA列
			List<Map<String, String>> fjDataList = fjService.selRepFjDataAll(sheetTab.getSuitId(), 
					sheetTab.getModalsheetId(), sheetTab.getModaltypeId(), sheetTab.getOrgId(), 
					sheetTab.getPeriodId(), sheetTab.getCnyId()); 
			//将浮动行数据存放到map中。以ROWITEM_ID为key,对应的value为List<Map<String, String>>
			Map<String, List<Map<String, String>>> fjDataMap = new HashMap<String, List<Map<String, String>>>();
			
			//将浮动行数据map中的DATA列转换成与之对应的物理列位置y
			for (Map<String, String> fjData : fjDataList) {
				if(fjDataMap.containsKey(fjData.get("ROWITEM_ID"))) {
					Map<String, String> mapData = new HashMap<String, String>();
					mapData.put("ROWNUMDTL", fjData.get("ROWNUMDTL"));
					for (Colitem col : fjColSetList) {
						mapData.put(col.getCOL().toString(),fjData.get(col.getDATA_COL()));
					}
					fjDataMap.get(fjData.get("ROWITEM_ID")).add(mapData);
				} else {
					List<Map<String, String>> listData = new ArrayList<Map<String, String>>();
					Map<String, String> mapData = new HashMap<String, String>();
					mapData.put("ROWNUMDTL", fjData.get("ROWNUMDTL"));
					for (Colitem col : fjColSetList) {
						mapData.put(col.getCOL().toString(),fjData.get(col.getDATA_COL()));
					}
					listData.add(mapData);
					fjDataMap.put(fjData.get("ROWITEM_ID"), listData);
				}
			}
			//复制单元格及填充数据
			//按照模板中为浮动行的行指标进行循环：fjRowitemList中的行指标必须是有序的？？？？？
			for(Map<String, Object> fjRowitem : fjRowitemList) {
				//行指标id
				String rowitemId = fjRowitem.get("ROWITEM_ID").toString();
				//行指标对应的X位置
				Integer rowitemX = Integer.parseInt(fjRowitem.get("ROW").toString());
				//从浮动行数据map中通过rowitemId判断，行指标是否有对应的浮动行明细数据
				List<Map<String, String>> fjdataList = fjDataMap.get(rowitemId);
				if(null != fjdataList) {
					//按照有几个浮动行来复制格式, fjdataList中的浮动行明细数据必须是按照浮动行号有序的？？？？
					for(int i=0; i<fjdataList.size(); i++) {
						Map<String, String> fjdataMap = fjdataList.get(i);
						//通过行指标对应的X位置，找到X这一行的所有单元格格式
						if(modalCellMap.containsKey(rowitemX)) {
							List<SheetCell> rowCells = modalCellMap.get(rowitemX); //X这一行的所有单元格格式
							//复制单元格格式同时给单元格复制
							for(SheetCell c : rowCells) {
								String content = c.getContent();
								Map<String, Object> jsonMap = JsonUtil.getJsonObj(content);
								//行次列上的单元格数据变为行次+“-”+明细行号
								if(c.getY() == rownoY) {
									jsonMap.put("data", jsonMap.get("data")+"-"+fjdataMap.get("ROWNUMDTL"));
								}
								//单元格的Y位置!=行次列，如果单元格有data数据fjdataMap没有对应的数据就替换为“”？？？？
								if(c.getY() != rownoY) {
									if(fjdataMap.containsKey(c.getY().toString())) {
										jsonMap.put("data", fjdataMap.get(c.getY().toString()));
									} else {
										jsonMap.put("data", "");
									}
								}
								content = JsonUtil.toJson(jsonMap);
								SheetCell cp = new SheetCell(c.getTabId(), c.getX()+i+1, c.getY(), content);
								newcellList.add(cp);
							}
						}
					}
					//修改该主行后的所有主行的x位置
					Set<Integer> cellKeySet = modalCellMap.keySet();
					for(Integer key : cellKeySet) {
						if(key > rowitemX) {
							List<SheetCell> needModifedCells = modalCellMap.get(key);
							for(SheetCell nc : needModifedCells) {
								nc.setX(nc.getX()+fjdataList.size());
							}
						}
					}
				}
			}
			cellList.addAll(newcellList);
/*			for(SheetCell s: newcellList) {
				System.out.println(s);
			}*/
			Collections.sort(cellList);
		} catch(Exception e) {
			log.error("报表导出excel文件出错", e);
		}
		return cellList;
	}
	/**
	 * 报表导出-导出浮动行表时，因为浮动行的增加，导致合并单元格的位置需要修改
	 */
	@Override
	public List<SheetTabElement> getFloatModalMergeCells(SheetTab sheetTab) {
		List<SheetTabElement> tabElements = null;
		try {
			tabElements = sheetTabElementMapper.findElementsByTabAndType(sheetTab.getId(), "meg");
			int fjRowCount = sheetTabElementMapper.getFjModalDataCount(sheetTab.getOrgId(), sheetTab.getPeriodId(),
					sheetTab.getCnyId(), sheetTab.getModalsheetId());
			for (int i=0; i<tabElements.size(); i++) {
				SheetTabElement mse = tabElements.get(i);
				String type = mse.getEtype();
				String content = mse.getContent();
				if("meg".equals(type)){
					content = content.replace("[", "").replace("]", "");
					String[] cont = content.split(",");
					if(Integer.parseInt(cont[0])>Integer.parseInt(sheetTab.getTitleMaxRow())){
						String tempC="[";
						for(int j=0;j<cont.length;j++){
							if(j==0||j==2){
								cont[j]=(Integer.parseInt(cont[j])+fjRowCount)+"";
							}
							if(j==cont.length-1){
								tempC=tempC+cont[j]+"]";
							}else{
								tempC=tempC+cont[j]+",";
							}
						}
						mse.setContent(tempC);
					}
				}
			}
		} catch(Exception e) {
			log.error("报表导出excel文件出错", e);
		}
		return tabElements;
	}
	/**
	 * 此方法暂时不使用。此方法中浮动行处理方式同报表查询
	 * @param sheetTab
	 * @return
	 */
	public List<SheetCell> getFixedAndFloatCells2(SheetTab sheetTab) {
		List<SheetCell> newcellList = new ArrayList<SheetCell>();
		List<SheetCell> cellList = null;
		try {
			//获取主表格式
			cellList = sheetCellMapper.getModalsheetFormatCellList(sheetTab.getId().toString());
			//是浮动行的行指标:行指标id,行指标code
			List<Map<String, String>> fjRows = makeReportMapper.getFjRowitemInfo(sheetTab.getSuitId(), 
					sheetTab.getModaltypeId(), sheetTab.getModalsheetId());
			
			//获取模版下所有的浮动行数据
			List<Map<String, String>> recs = fjService.selRepFjDataAll(sheetTab.getSuitId(), 
					sheetTab.getModalsheetId(), sheetTab.getModaltypeId(), sheetTab.getOrgId(), 
					sheetTab.getPeriodId(), sheetTab.getCnyId());  
			//浮动行DATA列与列指标映射
			List<Colitem> colList = fjMapper.getFjColitemList(sheetTab.getSuitId(), sheetTab.getModalsheetId(), 
					sheetTab.getModaltypeId());
			//将浮动行数据map中的DATA列转换成与之对应的物理列位置y
			for (Map<String, String> map : recs) {
				for (Colitem col : colList) {
					map.put(col.getCOL().toString(),map.get(col.getDATA_COL()));
					map.remove(col.getDATA_COL());
				}
			}
			SheetCell modalsf = null;
			//获取主行数据
			Map<String,String> map=new HashMap<String,String>();
			map.put("suitId", sheetTab.getSuitId());
			map.put("msformatId", sheetTab.getId().toString());
			map.put("entityId", sheetTab.getOrgId());
			map.put("periodId", sheetTab.getPeriodId());
			map.put("cnyId", sheetTab.getCnyId());
			map.put("dbType", PlatformUtil.getDbType());
			map.put("ledgerId", sheetTab.getLedgerId());
			List<CellData> DataList = makeReportMapper.loadDataValue(map);

			String rowno_col="";
			String rowitem_col="";
			for (SheetCell msf : cellList) {
				if(msf.getCELL_COMMENT()!=null && msf.getCELL_COMMENT().equals("rowno")){
					rowno_col = msf.getY().toString();
				}
				if(msf.getCELL_COMMENT()!=null && msf.getCELL_COMMENT().equals("rowitem")){
					rowitem_col = msf.getY().toString();
				}
			}
			
			for (SheetCell msf : cellList) {
				//给主行数据单元格赋值
				for(int i=0; i<DataList.size(); i++){
					CellData celldata = DataList.get(i);
					String content = msf.getContent();
					Map<String, Object> jsonMap = JsonUtil.getJsonObj(content);
					if(jsonMap.get("cal")!=null && jsonMap.get("cal").toString().equals("true")){
						jsonMap.remove("cal");
						jsonMap.remove("value");
						jsonMap.put("data","");
					}
					if(msf.getY()==Integer.parseInt(celldata.getCol()) && msf.getX()==Integer.parseInt(celldata.getRow())){
						String data=celldata.getJson();
						data=data.replace("\'","\"");
						data=data.replace("data","\"data\"");
						Map<String, Object> dataMap = JsonUtil.getJsonObj(data);
						jsonMap.put("data",dataMap.get("data"));
						msf.setContent(JsonUtil.toJson(jsonMap));
						msf.setCal(false);
						break;
					}
					msf.setContent(JsonUtil.toJson(jsonMap));
					msf.setCal(false);
				}
				//把浮动行插入到对应主行下，根据模板有几个浮动行循环
				for(int i=0; i<fjRows.size(); i++){
					Map<String, String> fjRow = fjRows.get(i);
					String rowitemId = fjRow.get("ROWITEM_ID");
					String rowitemCode = fjRow.get("ROWITEM_CODE");
					if(msf.getY()==Integer.parseInt(rowno_col) && msf.getX()>Integer.parseInt(sheetTab.getTitleMaxRow())){
						if(msf.getCELL_COMMENT()!=null&&msf.getCELL_COMMENT().equals(rowitemCode)){
							int row = msf.getX();
							int cnt = fjMapper.countFjData(sheetTab.getSuitId(), sheetTab.getOrgId(), 
									sheetTab.getPeriodId(), sheetTab.getCnyId(), rowitemId, sheetTab.getModalsheetId());
							List<Map<String, String>> temps=new ArrayList<Map<String,String>>(); 
							Map<String, String> temp=null;
							for (SheetCell msf2 : cellList) {
								if(msf2.getX() == row){
									String content = msf2.getContent();
									Map<String, Object> jsonMap = JsonUtil.getJsonObj(content);
									if(jsonMap.get("cal")!=null&&jsonMap.get("cal").toString().equals("true")){
										jsonMap.remove("cal");
										jsonMap.remove("value");
										jsonMap.put("data","");
										msf2.setContent(JsonUtil.toJson(jsonMap));
										msf2.setCal(false);
									}
									if(msf2.getY()==Integer.parseInt(rowitem_col) || msf2.getY()==Integer.parseInt(rowno_col)){
										temp = new HashMap<String, String>();
										temp.put("col", msf2.getY().toString());
										temp.put("data", jsonMap.get("data").toString());
										temps.add(temp);
									}
									for(int i2=1; i2<=cnt; i2++){
										modalsf = new SheetCell(msf2.getTabId(),msf2.getX(),msf2.getY(),msf2.getContent());
										modalsf.setX(modalsf.getX()+i2);
										newcellList.add(modalsf);
									}
								} else if(msf2.getX()>row) {
									msf2.setX(msf2.getX()+cnt);
								}
							}
							//给浮动行赋值
							for (SheetCell nmsf : newcellList) {
								for (Map<String, String> rec : recs) {
									String rownumdtl=rec.get("ROWNUMDTL");
									if(rec.get("ROWITEM_CODE").equals(rowitemCode) && nmsf.getX()==row+Integer.parseInt(rownumdtl)){
										String content = nmsf.getContent();
										Map<String, Object> jsonMap = JsonUtil.getJsonObj(content);
										if(nmsf.getY()==Integer.parseInt(rowno_col)){
											for (Map<String, String> map2 : temps) {
												if(map2.get("col").equals(rowno_col)){
													jsonMap.put("data",map2.get("data")+"-"+rownumdtl);
												}
											}
										}else if(rec.get(nmsf.getY().toString())!=null){
											if(nmsf.getY()==Integer.parseInt(rowitem_col)){
												for (Map<String, String> map2 : temps) {
													if(map2.get("col").equals(rowitem_col)){
														jsonMap.put("data",map2.get("data")+"-"+rec.get(nmsf.getY().toString()));
													}
												}
											}else{
												jsonMap.put("data",rec.get(nmsf.getY().toString()));
												//jsonMap.put("itms", "1");
											}
										}
										nmsf.setContent(JsonUtil.toJson(jsonMap));
										break;
									}
								}
							}
							//同时改变主表数据区域的位置
							for(int i2=0; i2<DataList.size(); i2++){
								CellData celldata = DataList.get(i2);
								if(Integer.parseInt(celldata.getRow())>row){
									celldata.setRow(((Integer)(Integer.parseInt(celldata.getRow())+cnt)).toString());
								}
							}	
							break;
						}
					}
				}
			}
			cellList.addAll(newcellList);
			Collections.sort(cellList);
		} catch(Exception e) {
			log.error("报表导出excel文件出错", e);
		}
		return cellList;
	}

}
