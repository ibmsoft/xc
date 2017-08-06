/**
 * 
 */
package com.xzsoft.xc.rep.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.UUID;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.cubedrive.base.domain.document.DocumentFile;
import com.cubedrive.sheet.domain.sheet.SheetTab;
import com.xzsoft.xc.rep.dao.XcRepSheetExportDao;
import com.xzsoft.xc.rep.modal.ReportSheetBean;
import com.xzsoft.xc.rep.service.XcRepSheetExportService;
import com.xzsoft.xc.rep.service.XcSheetExpCommonService;
import com.xzsoft.xc.rep.util.FileUtil;
import com.xzsoft.xc.rep.util.XcRepFilepath;
import com.xzsoft.xip.platform.util.PlatformUtil;
@Service("xcRepSheetExportService")
public class XcRepSheetExportServiceImpl implements XcRepSheetExportService {
	@Resource
	private XcRepSheetExportDao xcRepSheetExportDao;
	@Resource
	private XcSheetExpCommonService xcSheetExpCommonService;

	/* (non-Javadoc)
	 * @name     方法名
	 * @author   tangxl
	 * @date     2016年9月21日
	 * @注释                   TODO
	 * @version  1.0
	 * @see com.xzsoft.xc.rep.service.XcRepSheetExportService#exportSheetExcelZip(java.util.HashMap)
	 */
	@Override
	public Object exportSheetExcelZip(HashMap<String, Object> paramsMap)
			throws Exception {
		String sheetIds = paramsMap.get("sheetIds").toString();
		String exportType = paramsMap.get("exportType").toString();
		String filePath = paramsMap.get("filePath").toString();
		List<ReportSheetBean> sheetList = xcRepSheetExportDao.getSheetListByIds(sheetIds);
		//生成最后压缩包的名称
		String randomCode = UUID.randomUUID().toString();
		String zipFileName = filePath + File.separator + randomCode + ".zip";
		//对要导出的报表按期间进行分类，每一个期间为一个小的压缩包
		HashMap<String, List<ReportSheetBean>> periodSheetMap = new HashMap<String, List<ReportSheetBean>>();
		for(ReportSheetBean sh:sheetList){
			String tmpPeriodCode = sh.getPeriodCode();
			if(periodSheetMap.containsKey(tmpPeriodCode)){
				periodSheetMap.get(tmpPeriodCode).add(sh);
			}else{
				List<ReportSheetBean> tmpSheetList = new ArrayList<ReportSheetBean>();
				tmpSheetList.add(sh);
				periodSheetMap.put(tmpPeriodCode,tmpSheetList);
			}
		}
		//记录所有生成的压缩包文件
		List<String> zipFileList = new ArrayList<String>();
		//创建压缩包
		Iterator<Entry<String, List<ReportSheetBean>>> periodIt = periodSheetMap.entrySet().iterator();
		while(periodIt.hasNext()){
			Entry<String, List<ReportSheetBean>> en = periodIt.next();
			String partialZipName = filePath + File.separator +  en.getKey() + ".zip";
			List<ReportSheetBean> partialFile = en.getValue();
			//存放Excel文件名称的Map,按账簿分类时key：ledgerId，value：ledgerName；按模板分类时key：tabId，value：tabName
			HashMap<String, String> excelNameMap = new HashMap<String, String>();  
			//存放每张Excel文件内容的查询条件,按账簿分类时key：ledgerId；按模板分类时key：tabId
			HashMap<String, List<ReportSheetBean>> queryConditionMap = new HashMap<String, List<ReportSheetBean>>();
			//记录所有生成的Excel文件
			List<String> fileList = new ArrayList<String>();
			//按账簿分类导出
			if(XcRepFilepath.LEDGER_EXPORT.equalsIgnoreCase(exportType)){
				for(ReportSheetBean t:partialFile){
					t.setExportTabName(t.getTabName());
					if(queryConditionMap.containsKey(t.getLedgerId())){
						queryConditionMap.get(t.getLedgerId()).add(t);
					}else{
						List<ReportSheetBean> conditionList = new ArrayList<ReportSheetBean>();
						conditionList.add(t);
						queryConditionMap.put(t.getLedgerId(), conditionList);
						//记录所有要生成的Excel文件名--ledgerName
						excelNameMap.put(t.getLedgerId(), t.getLedgerName());
					}
				}
			}else{
			//按模板分类导出
				for(ReportSheetBean t:partialFile){
					t.setExportTabName(t.getLedgerName());
					if(queryConditionMap.containsKey(t.getTabId())){
						queryConditionMap.get(t.getTabId()).add(t);
					}else{
						List<ReportSheetBean> conditionList = new ArrayList<ReportSheetBean>();
						conditionList.add(t);
						queryConditionMap.put(t.getTabId(), conditionList);
						//记录所有要生成的Excel文件名--tabName
						excelNameMap.put(t.getTabId(), t.getTabName());
					}
				}
			}
			//对每一个账簿/模板生成与之对应的Excel文件<Excel中包含多个tab页签>
			Iterator<Entry<String, String>> iterator = excelNameMap.entrySet().iterator();
			while(iterator.hasNext()){
				Entry<String, String> entry = iterator.next();
				String key = entry.getKey();
				String value = entry.getValue();
				List<ReportSheetBean> unitList = queryConditionMap.get(key);
				String excelName = filePath.concat(File.separator).concat(value).concat(".xlsx");
				DocumentFile documentFile = new DocumentFile();
				List<SheetTab> sheetTabList = new ArrayList<>();
				int count = 1;
				for(ReportSheetBean r : unitList) {
					SheetTab st = new SheetTab();
					st.setTabId(r.getTabId());
					st.setId(r.getId()); 
					st.setName(r.getExportTabName());
					st.setTabOrder(count);
					st.setOrgId(r.getOrgId());
					st.setPeriodId(r.getPeriodCode());
					st.setCnyId(r.getCnyCode());
					st.setDbType(PlatformUtil.getDbType());
					//这里只导出固定行数据
					st.setOperateType("ExpFixData");
					st.setActive((count == 1)? true: false);
					st.setOrgName(r.getOrgName());
					st.setPeriodName(r.getPeriodCode());
					st.setLedgerId(r.getLedgerId());
					sheetTabList.add(st);
					count++;
				}
				documentFile.setSheetTabList(sheetTabList);
				documentFile.setName(excelName);
				fileList.add(excelName);
				//生成Excel文档
				xcSheetExpCommonService.exportSheetByCondition(documentFile, (Locale) paramsMap.get("locale"));
			}
			//打包生成文件
			FileUtil.zipFile(partialZipName, fileList);
			zipFileList.add(partialZipName);
		}
		//生成最后的压缩包
		FileUtil.zipFile(zipFileName, zipFileList);
		JSONObject object = new JSONObject();
		object.put("flag", "0");
		object.put("fileName",randomCode);
		object.put("filePathAndName",zipFileName);
		object.put("msg","导出报表excel文件成功！");
		return object;
	}

}
