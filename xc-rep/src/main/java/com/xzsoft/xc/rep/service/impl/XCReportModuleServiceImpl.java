/**
 * 
 */
package com.xzsoft.xc.rep.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.xzsoft.xc.gl.dao.XCGLCommonDAO;
import com.xzsoft.xc.gl.modal.Organization;
import com.xzsoft.xc.rep.dao.EnterpriseSheetDAO;
import com.xzsoft.xc.rep.dao.XCRepFormulaManageDao;
import com.xzsoft.xc.rep.dao.XCReportModuleDao;
import com.xzsoft.xc.rep.modal.CellFormula;
import com.xzsoft.xc.rep.modal.ESTabBean;
import com.xzsoft.xc.rep.modal.ESTabCellBean;
import com.xzsoft.xc.rep.modal.ESTabElementBean;
import com.xzsoft.xc.rep.service.XCReportModuleService;
import com.xzsoft.xc.rep.util.FormulaTranslate;
import com.xzsoft.xc.rep.util.JsonUtil;
import com.xzsoft.xc.rep.util.XCRepFormulaUtil;
import com.xzsoft.xc.rep.util.XcRepFormulaExcuteUtil;
import com.xzsoft.xip.platform.util.PlatformUtil;

@Service("xCReportModuleService")
public class XCReportModuleServiceImpl implements XCReportModuleService {
	@Resource
	private XCReportModuleDao xCReportModuleDao;
	@Resource
	XCRepFormulaManageDao xCRepFormulaManageDao;
	@Autowired
	EnterpriseSheetDAO esDAO ;
	@Autowired
	XCGLCommonDAO xcglCommonDAO ;
	
	
	
	/* (non-Javadoc)
	 * @name     loadReportModule
	 * @author   tangxl
	 * @date     2016年9月2日
	 * @注释                   报表采集公式设置 --> 加载公式设置报表
	 * @version  1.0
	 * @see com.xzsoft.xc.rep.service.XCReportModuleService#loadReportModule(java.util.HashMap)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> loadReportModule(HashMap<String, String> map) throws Exception {
		String ledgerId = map.get("ledgerId") ;
		String accHrcyId = map.get("accHrcyId") ;
		String tabOrder = map.get("tabOrder") ;
		
		Map<String,Object> result = Maps.newHashMap() ;
		
		HashMap<String,Object> tabMap = esDAO.getAllESTabsExcludeComment(accHrcyId, Integer.parseInt(tabOrder)) ;
		
		// 封装返回参数
		List<HashMap<String,Object>> sheets = new ArrayList<HashMap<String,Object>>() ;
		List<HashMap<String,Object>> cells = new ArrayList<HashMap<String,Object>>() ;
		List<HashMap<String,Object>> floatings = new ArrayList<HashMap<String,Object>>() ;
		
		// 处理sheets
		List<ESTabBean> tabs = (List<ESTabBean>) tabMap.get("tabs") ;
		if(tabs != null && tabs.size() >0){
			// 非活动页签
			for(ESTabBean tab : tabs){
				HashMap<String,Object> sheet = new HashMap<String,Object>() ;
				sheet.put("id", tab.getTabOrder()) ;
				sheet.put("name", tab.getTabName()) ;
				sheets.add(sheet) ;
			}
			// 活动页签
			ESTabBean activedTab = (ESTabBean) tabMap.get("activedTab") ;
			
			HashMap<String,Object> activedSheet = new HashMap<String,Object>() ;
			activedSheet.put("id", activedTab.getTabOrder()) ;
			activedSheet.put("name", activedTab.getTabName()) ;
			activedSheet.put("actived", true) ;
			sheets.add(activedSheet) ;
			
			
			// =================处理cells================
			// 固定单元格数据
			List<ESTabCellBean> normalCells = activedTab.getCells() ;
			if(normalCells != null && normalCells.size() >0){
				for(ESTabCellBean tabCell : normalCells){
					
					Map<String,Object> cfg = JsonUtil.fromJson(tabCell.getContent(), Map.class) ;
					cfg.put("dsd", "ed") ; // 禁用单元格编辑功能
					
					String _comment = String.valueOf(cfg.get("comment")) ;
					if("rowitem".equals(_comment) || "rowno".equals(_comment)){
						cfg.remove("comment") ;
						cfg.remove("commentEdit") ;
					}
					
					HashMap<String,Object> cell = new HashMap<String,Object>() ;
					cell.put("sheet", activedTab.getTabOrder()) ;
					cell.put("row", tabCell.getX()) ;
					cell.put("col", tabCell.getY()) ;
					cell.put("json", JsonUtil.toJson(cfg)) ;
					
					cells.add(cell) ;
				}
			}
			
			// 主数据单元格数据
			List<ESTabCellBean> fixedCells = (List<ESTabCellBean>) tabMap.get("fixedCells") ;
			if(fixedCells != null && fixedCells.size() >0){
				String orgName = "" ;
				if(!"-1".equals(ledgerId)){
					Organization org = xcglCommonDAO.getOrgByLedgerId(ledgerId) ;
					if(org != null) orgName = org.getOrgName() ;
				}
				
				for(ESTabCellBean cell : fixedCells) {
					HashMap<String,Object> fixedCell = new HashMap<String,Object>() ;
					
					String comment = cell.getComment() ;
					String content = cell.getContent() ;
					if("getcorp".equals(comment) || "getperiod".equals(comment)){
						Map<String,Object> cfg = JsonUtil.fromJson(content, Map.class) ;
						String data = (String) cfg.get("data") ;
						data = (data==null ? "" : data) ;
						
						if("getcorp".equals(comment)){
							data = data.concat(orgName) ;
							
						}else if("getperiod".equals(comment)){
							data = data.concat("") ;
						}
						
						cfg.put("data", data) ;
						cfg.remove("comment") ;
						cfg.remove("commentEdit") ;
						cfg.put("dsd", "ed") ; // 禁用单元格编辑功能
						cell.setContent(JsonUtil.toJson(cfg));
						
						// 返回cell
						fixedCell.put("sheet", activedTab.getTabOrder()) ;
						fixedCell.put("row", cell.getX()) ;
						fixedCell.put("col", cell.getY()) ;
						if(cell.getIsCal() == 1){
							fixedCell.put("cal", true) ;
						}
						fixedCell.put("json", cell.getContent()) ;
						
						cells.add(fixedCell) ;
					}
				}
			}
			
			// 公式单元格数据
			List<ESTabCellBean> dataCells = (List<ESTabCellBean>) tabMap.get("dataCells") ;
			if(dataCells != null && dataCells.size() >0){
				// 加载取数公式信息
				String tabId = activedTab.getTabId() ;
				HashMap<String,CellFormula> formulas = xCReportModuleDao.getTabFormulas(accHrcyId, ledgerId, tabId) ;
				
				// 处理取数公式
				for(ESTabCellBean cell : dataCells) {
					Map<String,Object> cfg = JsonUtil.fromJson(cell.getContent(), Map.class) ;
					
					// 是否为表内公式单元格
					boolean isCal = false ;
					if(cfg.containsKey("cal")){
						isCal= Boolean.parseBoolean(String.valueOf(cfg.get("cal"))) ;
					}
					
					if(formulas != null && formulas.size() >0){
						CellFormula cellFormula = formulas.get(cell.getComment()) ;
						if(cellFormula != null && !"".equals(cellFormula)){
							if(!isCal){
								cfg.put("data", cellFormula.getFormula()) ;
							}
						}
					}
					cfg.remove("comment") ;
					cfg.remove("commentEdit") ;
					cfg.remove("rtcorner") ;
					if(isCal){
						cfg.put("dsd", "ed") ;	
					}else{
						cfg.put("dsd", "") ;
					}
					
					// 返回参数
					HashMap<String,Object> itemCell = new HashMap<String,Object>() ;
					itemCell.put("sheet", activedTab.getTabOrder()) ;
					itemCell.put("row", cell.getX()) ;
					itemCell.put("col", cell.getY()) ;
					if(cell.getIsCal() == 1){
						itemCell.put("cal", true) ;
					}
					itemCell.put("json", JsonUtil.toJson(cfg)) ;
					
					cells.add(itemCell) ;
				}
			}
			
			
			// 处理floatings
			List<ESTabElementBean> elements = activedTab.getElements() ;
			if(elements != null && elements.size() >0){
				for(ESTabElementBean element : elements){
					HashMap<String,Object> floating = new HashMap<String,Object>() ;
					floating.put("sheet", activedTab.getTabOrder()) ;
					floating.put("name", element.getName()) ;
					floating.put("ftype", element.getEtype()) ;
					floating.put("json", element.getContent()) ;
					
					floatings.add(floating) ;
				}
			}
			
		}else{
			// 不存在模板信息
			for(int i=0; i<3; i++){
				HashMap<String,Object> sheet = new HashMap<String,Object>() ;
				sheet.put("id", i) ;
				sheet.put("name", "sheet"+i) ;
				if(i ==0){
					sheet.put("actived", true) ;
				}
				sheets.add(sheet) ;
			}
		}
		
		// 将报表页签序号执行排序处理
		Collections.sort(sheets, new Comparator<HashMap<String,Object>>(){
			@Override
			public int compare(HashMap<String, Object> map1, HashMap<String, Object> map2) {
				Integer order1 = Integer.parseInt(String.valueOf(map1.get("id"))) ;
				Integer order2 = Integer.parseInt(String.valueOf(map2.get("id"))) ;
				return order1.compareTo(order2) ;
			}
		});
		
		// 返回信息
		result.put("sheets", sheets) ;
		result.put("cells", cells) ;
		result.put("floatings", floatings) ;
		
		return result ;
		
//		return xCReportModuleDao.loadReportModule(map);
		
	}

	/* (non-Javadoc)
	 * @name     translateFormula
	 * @author   tangxl
	 * @date     2016年9月6日
	 * @注释                   公式翻译
	 * @version  1.0
	 * @see com.xzsoft.xc.rep.service.XCReportModuleService#translateFormula(java.lang.String, java.lang.String)
	 */
	@Override
	public String translateFormula(String formulaText, String accHrcyId,String ledgerId)
			throws Exception {
		return XCRepFormulaUtil.formulaTranslate(ledgerId,formulaText, accHrcyId, PlatformUtil.getDbType(), xCRepFormulaManageDao);
	}

	/* (non-Javadoc)
	 * @name     formulaTest
	 * @author   tangxl
	 * @date     2016年9月6日
	 * @注释                   试运算公式
	 * @version  1.0
	 * @see com.xzsoft.xc.rep.service.XCReportModuleService#formulaTest(java.util.HashMap)
	 */
	@Override
	public String formulaTest(HashMap<String, String> map) throws Exception {		//返回公式运算式
		//解析公式
		String equation=XcRepFormulaExcuteUtil.getCellArithmetic(map, xCReportModuleDao);
		//计算结果值
		String result=FormulaTranslate.evalFormula(equation);
		//返回表达式和结果值
		String resultData=equation+"="+result;
		return resultData;}

	/* (non-Javadoc)
	 * @name     loadSheet
	 * @author   tangxl
	 * @date     2016年9月14日
	 * @version  1.0
	 * @see com.xzsoft.xc.rep.service.XCReportModuleService#loadSheet(java.lang.String)
	 */
	@Override
	public Map<String, Object> loadSheet(String sheetIds,String querySheetId) throws Exception {
		return xCReportModuleDao.loadSheet(sheetIds,querySheetId);
	}

}
