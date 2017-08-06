/**
 * 
 */
package com.xzsoft.xc.rep.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.xzsoft.xc.rep.dao.XCReportModuleDao;
import com.xzsoft.xc.rep.mapper.XCReportModuleMapper;
import com.xzsoft.xc.rep.modal.CellFormula;
import com.xzsoft.xc.rep.modal.ReportCellFormula;
import com.xzsoft.xc.rep.modal.ReportSheetBean;
import com.xzsoft.xc.rep.modal.ReportTabBean;
import com.xzsoft.xc.rep.modal.ReportTabCellBean;
import com.xzsoft.xc.rep.modal.ReportTabElementBean;
import com.xzsoft.xc.rep.util.XCRepFormulaUtil;
import com.xzsoft.xip.platform.util.PlatformUtil;

@Repository("xCReportModuleDao")
public class XCReportModuleDaoImpl implements XCReportModuleDao {
	@Resource
	private XCReportModuleMapper xCReportModuleMapper;
	
	/* (non-Javadoc)
	 * @name     方法名
	 * @author   tangxl
	 * @date     2016年9月2日
	 * @注释                   TODO
	 * @version  1.0
	 * @see com.xzsoft.xc.rep.dao.XCReportModuleDao#loadReportModule(java.util.HashMap)
	 */
	@Override
	public Map<String, Object> loadReportModule(HashMap<String, String> map) throws Exception {
		Map<String, Object> results = Maps.newHashMap();

		//获取报表列表
		String searchTabOrder = map.get("tabOrder");
		List<ReportTabBean> tabList = xCReportModuleMapper.getTabByAccHrcyId(map.get("accHrcyId"), Integer.valueOf(map.get("tabOrder")));
		
		if(tabList != null && tabList.size()>0){
			String tabName = "";
			String tabId = "";
			int tabOrder = 0;         
			if("-1".equals(map.get("tabOrder"))){
				//非Enterprise Sheet 的tabChange事件
				tabName = tabList.get(0).getName();
				tabId = tabList.get(0).getTabId();
				tabOrder = tabList.get(0).getId();
			}else{
				//Enterprise Sheet 的tabChange事件
				for(ReportTabBean t:tabList){
					if(searchTabOrder.equals(t.getId()+"")){
						tabName = t.getName();
						tabId = t.getTabId();
						tabOrder = t.getId();
						tabList.remove(t);
				        tabList.add(0, t);
						break;
					}
				}
			}
			//获取表格列信息
			List<ReportTabCellBean> cellList = xCReportModuleMapper.getCellByTabId(tabId,tabOrder);
			//获取表格列的格式信息
			List<ReportTabElementBean> elementList = xCReportModuleMapper.getElementByTabId(tabId,tabOrder);
			//获取表格列的公式内容
			map.put("dbType", PlatformUtil.getDbType());
			map.put("tabId", tabId);
			map.put("sheet", tabOrder+"");
			List<ReportCellFormula> formulaList = xCReportModuleMapper.getFormulaByTabId(map);
			//清除查询报表的列的批注信息---->批注为rowItemId和colItemId组合字符串，现已存到属性 vname中
			if(cellList!=null && cellList.size()>0)
				XCRepFormulaUtil.clearCellAnnotation(cellList);
			results.put("fileName", tabName);
			results.put("sheets", tabList);
			results.put("cells", cellList);
			results.put("floatings", elementList);
			results.put("formulas", formulaList);
		}else{
			results.put("fileName", "sheet");
			results.put("sheets", new ArrayList<ReportTabBean>());
			results.put("cells", new ArrayList<ReportTabCellBean>());
			results.put("floatings", new ArrayList<ReportTabElementBean>());
			results.put("formulas", new ArrayList<ReportCellFormula>());
		}
		return results;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getTabFormulas</p> 
	 * <p>Description: 查询单表取数公式信息  </p> 
	 * @param accHrcyId
	 * @param ledgerId
	 * @param tabId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.XCReportModuleDao#getTabFormulas(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public HashMap<String,CellFormula> getTabFormulas(String accHrcyId,String ledgerId,String tabId) throws Exception {
		// 查询公式信息
		HashMap<String,String> map = new HashMap<String,String>() ;
		map.put("accHrcyId", accHrcyId) ;
		map.put("ledgerId", ledgerId) ;
		map.put("tabId", tabId) ;
		
		List<CellFormula> formulas = xCReportModuleMapper.getCellFormulas(map) ;
		
		// 处理返回值信息
		HashMap<String,CellFormula> result = null ; 
		if(formulas != null && formulas.size() >0){
			result = new HashMap<String,CellFormula>() ;
			for(int i=0; i<formulas.size(); i++){
				CellFormula formula = formulas.get(i) ;
				
				String key = formula.getRowitemId().concat("/").concat(formula.getColitemId()) ;
				
				result.put(key, formula) ;
			}
		}
		
		return result ;
	}
	
	/* (non-Javadoc)
	 * @name     方法名
	 * @author   tangxl
	 * @date     2016年9月6日
	 * @注释                   TODO
	 * @version  1.0
	 * @see com.xzsoft.xc.rep.dao.XCReportModuleDao#getRepValue(java.util.Map)
	 */
	@Override
	public String getRepValue(Map<String, String> map) throws Exception {
		return xCReportModuleMapper.getRepValue(map);
	}
	
	/* (non-Javadoc)
	 * @name     getCellFunction
	 * @author   tangxl
	 * @date     2016年9月6日
	 * @注释                   TODO
	 * @version  1.0
	 * @see com.xzsoft.xc.rep.dao.XCReportModuleDao#getCellFunction(java.lang.String)
	 */
	@Override
	public HashMap<String, String> getCellFunction(String funcCode) throws Exception {
		return xCReportModuleMapper.getCellFunction(funcCode);
	}
	
	/* (non-Javadoc)
	 * @name     方法名
	 * @author   tangxl
	 * @date     2016年9月6日
	 * @注释                   TODO
	 * @version  1.0
	 * @see com.xzsoft.xc.rep.dao.XCReportModuleDao#excuteProcedure(java.util.Map)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class,RuntimeException.class})
	public String excuteProcedure(Map<String, String> map) throws Exception {
		xCReportModuleMapper.excuteProcedure(map);
		return map.get("o_result");
	}
	/* (non-Javadoc)
	 * @name     loadSheet
	 * @author   tangxl
	 * @date     2016年9月14日
	 * @注释                   加载已生成的报表
	 * @version  1.0
	 * @see com.xzsoft.xc.rep.dao.XCReportModuleDao#loadSheet(java.lang.String)
	 */
	@Override
	public Map<String, Object> loadSheet(String sheetIds,String querySheetId) throws Exception {
		List<String> sheetIdList = new ArrayList<String>();
		Collections.addAll(sheetIdList, sheetIds.split(","));
		//获取报表信息
		List<ReportSheetBean> sheetList = xCReportModuleMapper.getSheetById(sheetIdList);
		Map<String, Object> results = Maps.newHashMap();
		//构造报表sheetId和报表序号id映射关系的json串
		String sheetReflectJson = "";
		//确定报表的名称
		if(sheetIdList.size() == 1){
			sheetList.get(0).setId(0);
			sheetList.get(0).setName(sheetList.get(0).getTabName());
			sheetReflectJson = "[{\"sheetId\":\""+sheetList.get(0).getSheetId()+"\",\"tabOrder\":\"0\"}]";
		}else{
			sheetReflectJson = XCRepFormulaUtil.renameReprotName(sheetList,querySheetId);
		}
		String tabName = "报表查询";
		String tabId = sheetList.get(0).getTabId();
		//获取报表的列信息
		List<ReportTabCellBean> cellList = xCReportModuleMapper.getCellByTabId(tabId,0);
		//获取报表的格式信息
		List<ReportTabElementBean> elementList = xCReportModuleMapper.getElementByTabId(tabId,0);
		HashMap<String, String> map = new HashMap<String, String>();
		//获取报表的内容
		map.put("dbType", PlatformUtil.getDbType());
		map.put("tabId", tabId);
		map.put("periodCode",sheetList.get(0).getPeriodCode());
		map.put("ledgerId", sheetList.get(0).getLedgerId());
		map.put("cnyCode", sheetList.get(0).getCnyCode());
		map.put("sheet", sheetList.get(0).getId()+"");
		map.put("orgName",sheetList.get(0).getOrgName());
		map.put("orgId",sheetList.get(0).getOrgId());
		map.put("periodCode",sheetList.get(0).getPeriodCode());
		List<ReportCellFormula> formulaList = xCReportModuleMapper.getCellValByTabId(map);
		//清除查询报表的列的批注信息---->批注为rowItemId和colItemId组合字符串，现已存到属性 vname中
		if(cellList!=null && cellList.size()>0)
			XCRepFormulaUtil.clearCellAnnotation(cellList);
		results.put("fileName", tabName);
		results.put("sheets", sheetList);
		results.put("cells", cellList);
		results.put("floatings", elementList);
		results.put("formulas", formulaList);
		results.put("sheetReflect", sheetReflectJson);
		return results;
	}
}
