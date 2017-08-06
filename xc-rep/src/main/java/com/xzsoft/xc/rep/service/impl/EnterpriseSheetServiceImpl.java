package com.xzsoft.xc.rep.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cubedrive.base.utils.JsonUtil;
import com.google.common.collect.Maps;
import com.wb.util.SysUtil;
import com.xzsoft.xc.rep.dao.EnterpriseSheetDAO;
import com.xzsoft.xc.rep.modal.ESColItemBean;
import com.xzsoft.xc.rep.modal.ESColItemRefBean;
import com.xzsoft.xc.rep.modal.ESRowItemBean;
import com.xzsoft.xc.rep.modal.ESRowItemRefBean;
import com.xzsoft.xc.rep.modal.ESTabBean;
import com.xzsoft.xc.rep.modal.ESTabCellBean;
import com.xzsoft.xc.rep.modal.ESTabElementBean;
import com.xzsoft.xc.rep.service.EnterpriseSheetService;
import com.xzsoft.xc.rep.service.TabImpAndExpService;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xip.framework.util.JSONUtil;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.platform.util.XipUtil;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;

/**
 * @ClassName: EnterpriseSheetServiceImpl 
 * @Description: EnterpriseSheet模板格式处理服务接口实现类
 * @author linp
 * @date 2016年9月1日 上午9:26:53 
 *
 */
@Service("esService")
public class EnterpriseSheetServiceImpl implements EnterpriseSheetService {
	@Autowired
	private EnterpriseSheetDAO esDAO ;
	@Autowired
	private TabImpAndExpService tabImpAndExpService ;

	
	/*
	 * (非 Javadoc) 
	 * <p>Title: findTemplate</p> 
	 * <p>Description: 查询科目体系模板信息 </p> 
	 *		{
	 *		        "sheets": [
	 *		           {"id":0,"name":"Sheet1",actived:true},
	 *		           {"id":1,"name":"Sheet2"},
	 *		           {"id":2,"name":"Sheet3"}
	 *		        ],
	 *
	 *		        "cells":[
	 *					{
	 *				    	sheet: 1, 
	 *				    	row:1, 
	 *				    	col:1, 
	 *				    	cal:true, 
	 *				    	json:{data:"=sum(1,2)"},
	 *		        	},
	 *					{ sheet: 1, row: 8, col: 2, json: { data: "Defined: rangeData" } },
     *       			{ sheet: 1, row: 8, col: 3, json: { data: "B4:C6" } },
	 *	            	{ sheet: 1, row: 9, col: 2, json: { data: "sum(rangeData, 1)" } },
	 *	            	{ sheet: 1, row: 9, col: 3, json: { data: "=sum(rangeData, 1)", cal: "true" } },
	 *	            	{ sheet: 1, row: 10, col: 2, json: { data: "average(rangeData)" } },
	 *	            	{ sheet: 1, row: 10, col: 3, json: { data: "=average(rangeData)", cal: "true" } },				
	 *				...],
	 *
	 *		        "floatings":[
	 *					{
	 *			        	sheet:1,       
	 *			        	name: "aha",  
	 *			        	ftype: "meg",  
	 *			        	json:{}
	 *					}
	 *				...],
	 *
	 *		        "fileConfig":[
	 *					{name: "rangeData", ctype: "named_func", json: "{\"cal\":\"'name mgr'!$B$4:$C$6\"}" }
	 *					...
	 *				]
	 *		}
	 * @param hrcyId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.service.EnterpriseSheetService#findTemplate(java.lang.String)
	 */
	@Override
	public Map<String, Object> findAllTemplates(String hrcyId) throws Exception {
		Map<String, Object> result = Maps.newHashMap() ;
		
		try {
			List<ESTabBean> sheets = esDAO.getAllESTabs(hrcyId,-1) ;
			if(sheets != null && sheets.size()>0){
				
				List<Map<String,Object>> sheetList = new ArrayList<Map<String,Object>>();
				List<Map<String,Object>> cellList = new ArrayList<Map<String,Object>>();
				List<Map<String,Object>> elementList = new ArrayList<Map<String,Object>>();
				
				for(ESTabBean sheet : sheets){
					// {"id":0,"name":"Sheet1",actived:true}
					Map<String,Object> sheetMap = new HashMap<String,Object>() ;
					sheetMap.put("id", sheet.getTabOrder()) ;
					sheetMap.put("name", sheet.getTabName()) ;
					if(sheet.getIsActive() == 1){
						sheetMap.put("actived", true) ;
						result.put("activedSheetId", sheet.getTabOrder());
					}
					sheetList.add(sheetMap) ;
					
					// 单元格信息
					List<ESTabCellBean> cells = sheet.getCells() ;
					if(cells != null && cells.size() >0){
						for(ESTabCellBean cell : cells){
							// { sheet: 1, row: 8, col: 2, json: { data: "Defined: rangeData" } }
							Map<String,Object> cellMap = new HashMap<String,Object>() ;
							cellMap.put("sheet", sheet.getTabOrder()) ;
							cellMap.put("row", cell.getX()) ;
							cellMap.put("col", cell.getY()) ;
							cellMap.put("cal", cell.getIsCal()==0 ? false : true) ;
							cellMap.put("json", cell.getContent()) ;
							
							cellList.add(cellMap) ;
						}
					}
					
					// 元素信息
					List<ESTabElementBean> elements = sheet.getElements() ;
					if(elements != null && elements.size() >0){
						for(ESTabElementBean element : elements){
							// {sheet:1, name: "aha", ftype: "meg", json:{}}
							Map<String,Object> floatingMap = new HashMap<String,Object>() ;
							floatingMap.put("sheet", sheet.getTabOrder()) ;
							floatingMap.put("name", element.getName()) ;
							floatingMap.put("ftype", element.getEtype()) ;
							floatingMap.put("json", element.getContent()) ;
							
							elementList.add(floatingMap) ;
						}
					}
				}
				// 返回值
				result.put("fileName", XipUtil.getMessage(XCUtil.getLang(), "XC_REP_TPL_QRY_003", null));
				result.put("sheets", sheetList);
				result.put("cells", cellList);
				result.put("floatings", elementList);
				
				result.put("flag", "0") ; // 标志位
			}
			
		} catch (Exception e) {
			result.clear();
			result.put("flag", "1") ;
			result.put("msg", e.getMessage()) ;
		}
		
		return result ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: findAllTemplateById</p> 
	 * <p>Description: 按照模板ID查询模板格式信息 </p> 
	 * @param tabId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.service.EnterpriseSheetService#findAllTemplateById(java.lang.String)
	 */
	@Override
	public Map<String, Object> findAllTemplateById(String tabId) throws Exception {
		Map<String, Object> result = Maps.newHashMap() ;
		
		try {
			List<Map<String,Object>> sheetList = new ArrayList<Map<String,Object>>();
			List<Map<String,Object>> cellList = new ArrayList<Map<String,Object>>();
			List<Map<String,Object>> elementList = new ArrayList<Map<String,Object>>();
			
			// 查询模板信息
			ESTabBean sheet = esDAO.getESTabIncludeFormat(tabId) ;
			
			if(sheet != null){
				// {"id":0,"name":"Sheet1",actived:true}
				Map<String,Object> sheetMap = new HashMap<String,Object>() ;
				sheetMap.put("id", sheet.getTabOrder()) ;
				sheetMap.put("name", sheet.getTabName()) ;
				sheetMap.put("actived", true) ;
				sheetList.add(sheetMap) ;

				result.put("activedSheetId", sheet.getTabOrder());
				
				// 单元格信息
				List<ESTabCellBean> cells = sheet.getCells() ;
				if(cells != null && cells.size() >0){
					for(ESTabCellBean cell : cells){
						// { sheet: 1, row: 8, col: 2, json: { data: "Defined: rangeData" } }
						Map<String,Object> cellMap = new HashMap<String,Object>() ;
						cellMap.put("sheet", sheet.getTabOrder()) ;
						cellMap.put("row", cell.getX()) ;
						cellMap.put("col", cell.getY()) ;
						cellMap.put("cal", cell.getIsCal()==0 ? false : true) ;
						cellMap.put("json", cell.getContent()) ;
						
						cellList.add(cellMap) ;
					}
				}
				
				// 元素信息
				List<ESTabElementBean> elements = sheet.getElements() ;
				if(elements != null && elements.size() >0){
					for(ESTabElementBean element : elements){
						// {sheet:1, name: "aha", ftype: "meg", json:{}}
						Map<String,Object> floatingMap = new HashMap<String,Object>() ;
						floatingMap.put("sheet", sheet.getTabOrder()) ;
						floatingMap.put("name", element.getName()) ;
						floatingMap.put("ftype", element.getEtype()) ;
						floatingMap.put("json", element.getContent()) ;
						
						elementList.add(floatingMap) ;
					}
				}
				
				// 返回值
				result.put("fileName", XipUtil.getMessage(XCUtil.getLang(), "XC_REP_TPL_QRY_003", null));
				result.put("sheets", sheetList);
				result.put("cells", cellList);
				result.put("floatings", elementList);
				
			}else{ // 空模板信息
				for(int i=0; i<3;i++){
					Map<String,Object> sheetMap = new HashMap<String,Object>() ;
					sheetMap.put("id", i) ;
					sheetMap.put("name", "sheet"+i+1) ;
					if(i==0){
						sheetMap.put("actived", true) ;
						result.put("activedSheetId", i);
					}
					sheetList.add(sheetMap) ;
				}
				result.put("fileName", XipUtil.getMessage(XCUtil.getLang(), "XC_REP_TPL_QRY_003", null));
				result.put("sheets", sheetList);
				result.put("cells", cellList);
				result.put("floatings", elementList);
			}
			
			result.put("flag", "0") ; // 标志位
			
		} catch (Exception e) {
			result.clear();
			result.put("flag", "1") ;
			result.put("msg", e.getMessage()) ;
		}
		
		return result ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: findTemplateByOrder</p> 
	 * <p>Description: 按模板序号查询模板信息 </p> 
	 * @param hrcyId
	 * @param tabOrder
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.service.EnterpriseSheetService#findTemplateByOrder(java.lang.String, int)
	 */
	@Override
	public Map<String, Object> findTemplateByOrder(String hrcyId, int tabOrder) throws Exception {
		Map<String, Object> result = Maps.newHashMap() ;
		
		try {
			List<ESTabBean> sheets = esDAO.getAllESTabs(hrcyId,tabOrder) ;
			if(sheets != null && sheets.size()>0){
				
				List<Map<String,Object>> sheetList = new ArrayList<Map<String,Object>>();
				List<Map<String,Object>> cellList = new ArrayList<Map<String,Object>>();
				List<Map<String,Object>> elementList = new ArrayList<Map<String,Object>>();
				
				for(ESTabBean sheet : sheets){
					// {"id":0,"name":"Sheet1",actived:true}
					Map<String,Object> sheetMap = new HashMap<String,Object>() ;
					sheetMap.put("id", sheet.getTabOrder()) ;
					sheetMap.put("name", sheet.getTabName()) ;
					sheetMap.put("actived", true) ;
					sheetList.add(sheetMap) ;
					
					// 单元格信息
					List<ESTabCellBean> cells = sheet.getCells() ;
					if(cells != null && cells.size() >0){
						for(ESTabCellBean cell : cells){
							// { sheet: 1, row: 8, col: 2, json: { data: "Defined: rangeData" } }
							Map<String,Object> cellMap = new HashMap<String,Object>() ;
							cellMap.put("sheet", sheet.getTabOrder()) ;
							cellMap.put("row", cell.getX()) ;
							cellMap.put("col", cell.getY()) ;
							cellMap.put("cal", cell.getIsCal()==0 ? false : true) ;
							cellMap.put("json", cell.getContent()) ;
							
							cellList.add(cellMap) ;
						}
					} 
					
					// 元素信息
					List<ESTabElementBean> elements = sheet.getElements() ;
					if(elements != null && elements.size() >0){
						for(ESTabElementBean element : elements){
							// {sheet:1, name: "aha", ftype: "meg", json:{}}
							Map<String,Object> floatingMap = new HashMap<String,Object>() ;
							floatingMap.put("sheet", sheet.getTabOrder()) ;
							floatingMap.put("name", element.getName()) ;
							floatingMap.put("ftype", element.getEtype()) ;
							floatingMap.put("json", element.getContent()) ;
							
							elementList.add(floatingMap) ;
						}
					}
				}
				
				// 补充其他的页签信息
				List<ESTabBean> otherTabBeans = esDAO.getAllESTabBeans(hrcyId) ;
				if(otherTabBeans != null && otherTabBeans.size() >0){
					for(ESTabBean sheet : otherTabBeans){
						Map<String,Object> sheetMap = new HashMap<String,Object>() ;
						sheetMap.put("id", sheet.getTabOrder()) ;
						sheetMap.put("name", sheet.getTabName()) ;
						sheetList.add(sheetMap) ;
					}
				}
				
				// 返回值
				result.put("sheets", sheetList);
				result.put("cells", cellList);
				result.put("floatings", elementList);
				
				result.put("flag", "0") ; // 标志位
			}
			
		} catch (Exception e) {
			result.clear();
			result.put("flag", "1") ;
			result.put("msg", e.getMessage()) ;
		}
		
		return result ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: createTemplate</p> 
	 * <p>Description: 新增报表模板 </p> 
	 * @param paramJson
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.service.XCRepTemplateService#createTemplate(java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public JSONObject createTemplate(String paramJson) throws Exception {
		JSONObject newTabJo = JSONObject.fromObject(paramJson) ;
		
		if(!newTabJo.has("accHrcyId")){
			throw new Exception("参数中未指定科目体系") ;
		}
		
		// 科目体系ID
		String hrcyId = newTabJo.getString("accHrcyId") ;
		
		if(!newTabJo.has("tabCode")){
			throw new Exception("参数中未包含模板编码") ;
		}else{
			// 判断编码是否重复
			String tabCode = newTabJo.getString("tabCode") ;
			if(esDAO.isExistsTabCode(hrcyId, tabCode)){
				throw new Exception("模板编码 "+tabCode+" 已经存在") ;
			}
		}
		
		if(!newTabJo.has("tabName")){
			throw new Exception("参数中未包含模板名称") ;
		}else{
			// 判断名称是否重复
			String tabName = newTabJo.getString("tabName") ;
			if(esDAO.isExistsTabCode(hrcyId, tabName)){
				throw new Exception("模板名称 "+tabName+" 已经存在") ;
			}
		}
		
		// 计算页签序号
		int tabOrder = esDAO.getNextTabOrder(hrcyId) ;
		
		// 执行新增处理
		ESTabBean tabBean = JSONUtil.jsonToBean(paramJson, ESTabBean.class) ;
		tabBean.setTabOrder(tabOrder);
		
		// 是否活动页签
		if(tabBean.getTabOrder() == 0){
			tabBean.setIsActive(1); 
		}else{
			tabBean.setIsActive(0); 	
		}
		tabBean.setColor("green");	
		
		// 执行保存
		esDAO.saveTabSheet(tabBean);
		
		// 处理返回值
		newTabJo.put("tabOrder", tabOrder) ;
		newTabJo.put("actived", tabBean.getIsActive()) ;
		newTabJo.put("color", "green") ;
		
		return newTabJo ;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: deleteTemplate</p> 
	 * <p>Description: 删除报表模板 </p> 
	 * @param hrcyId
	 * @param tabCode
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.service.XCRepTemplateService#deleteTemplate(java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void deleteTemplate(String hrcyId, int tabOrder) throws Exception {
		// 查询模板信息
		ESTabBean tabBean = esDAO.getESTabBeanByOrder(hrcyId, tabOrder) ;
		
		// 如果模板信息存在则执行删除
		if(tabBean != null){
			// 删除前校验
			esDAO.beforeDelCheck(tabBean) ;
			
			// 执行删除处理
			esDAO.delTabSheet(tabBean);
		}
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: deleteTemplate</p> 
	 * <p>Description: 按模板ID删除模板信息 </p> 
	 * @param tabId
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.service.EnterpriseSheetService#deleteTemplate(java.lang.String)
	 */
	@Override
	public void deleteTemplate(String tabId) throws Exception {
		ESTabBean tabBean = esDAO.getESTabBeanById(tabId) ;
		
		// 如果模板信息存在则执行删除
		if(tabBean != null){
			// 删除前校验
			esDAO.beforeDelCheck(tabBean) ;
			
			// 执行删除处理
			esDAO.delTabSheet(tabBean);
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: saveTemplate</p> 
	 * <p>Description: 保存报表模板信息  </p> 
	 * @param paramJson
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.service.XCRepTemplateService#saveTemplate(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void saveTemplate(String hrcyId, int sheetId, String dataJson) throws Exception {
		// 页签模板JSON数据
		Map<String, Object> dataJo = JsonUtil.getJsonObj(dataJson);
		
		if(sheetId != -1){
			ESTabBean tabBean = this.saveActivedTemplate(hrcyId, sheetId, dataJo);
			// 执行保存
			esDAO.saveTabFormat("single", tabBean, null, null, null);
			
		}else{
			Map<String,List<?>> retMap = this.saveAllTemplates(hrcyId, dataJo);
			
			List<ESTabBean> tabs = (List<ESTabBean>) retMap.get("tabs") ;
			List<ESTabCellBean> cells = (List<ESTabCellBean>) retMap.get("cells") ;
			List<ESTabElementBean> elements = (List<ESTabElementBean>) retMap.get("elements") ;
			
			// 执行保存
			esDAO.saveTabFormat("batch", null, tabs, cells, elements);
		}
	}

	/**
	 * @Title: saveActivedTemplate 
	 * @Description: 保存当前模板信息
	 * @param hrcyId
	 * @param sheetId
	 * @param dataJo
	 * @return
	 * @throws Exception    设定文件
	 */
	@SuppressWarnings("unchecked")
	private ESTabBean saveActivedTemplate(String hrcyId, int sheetId, Map<String, Object> dataJo) throws Exception {
		// 模板信息
		ESTabBean tabBean = new ESTabBean() ;
		List<ESTabCellBean> cells = new ArrayList<ESTabCellBean>() ;
		List<ESTabElementBean> elements = new ArrayList<ESTabElementBean>() ;

		// 当前操作人和操作时间
		String userId = CurrentSessionVar.getUserId() ;
		Date cdate = CurrentUserUtil.getCurrentDate() ;
		
		// 页签信息
		List<Object> sheetsList = (List<Object>) dataJo.get("sheets") ;
		for(int i=0; i<sheetsList.size(); i++){
			// 数据格式：{"id":0,"name":"Sheet1",actived:true}
			Map<String,Object> sheetJo = (Map<String, Object>) sheetsList.get(i) ;
			
			// 页签序号
			int currentSheetId = Integer.parseInt(String.valueOf(sheetJo.get("id"))) ;
			
			if(currentSheetId == sheetId){	// 保存当前页签
				tabBean.setTabOrder(currentSheetId);
				
				tabBean = esDAO.getESTabBeanByOrder(hrcyId, currentSheetId) ;
				
				tabBean.setLastUpdateDate(cdate);
				tabBean.setLastUpdatedBy(userId);	
				tabBean.setTabName(String.valueOf(sheetJo.get("name")));
				break ;	
				
			}else{
				continue ;
			}
		}
		
		// 页签单元格信息
		List<Object> cellsList = (List<Object>) dataJo.get("cells") ;
		if(cellsList != null && cellsList.size() >0){
			for(int j=0; j<cellsList.size(); j++){
				// {sheet: 1, row:1, col:1, cal:true, json:{data:"=sum(1,2)"}, ...}
				Map<String,Object> cellJo = (Map<String, Object>) cellsList.get(j) ;
				
				// 本页签单元格
				int _id1 = Integer.parseInt(String.valueOf(cellJo.get("sheet"))) ;
				
				if(sheetId != _id1) continue ;
				
				int x = Integer.parseInt(String.valueOf(cellJo.get("row"))) ;
				int y = Integer.parseInt(String.valueOf(cellJo.get("col"))) ;
				String contentJson = String.valueOf(cellJo.get("json"))  ;	// 单元格内容信息
				String cal = String.valueOf(cellJo.get("cal")) ;
				
				ESTabCellBean cellBean = new ESTabCellBean() ;
				cellBean.setCellId(UUID.randomUUID().toString());
				cellBean.setTabId(tabBean.getTabId());
				cellBean.setX(x);
				cellBean.setY(y);
				if(Boolean.parseBoolean(cal)){
					cellBean.setIsCal(1);
				}else{
					cellBean.setIsCal(0);
				}
				cellBean.setContent(contentJson);
				
				// 单元格内容JSON对象
				Map<String,Object> contentJo = JsonUtil.getJsonObj(contentJson) ;
				if(contentJo.containsKey("data")){
					cellBean.setRawData(String.valueOf(contentJo.get("data")));
				}
				
				/*
				 * 单元批注信息：
				 * 	     第一类固定批注：getcorp 、getperiod、rowno、rowitem、
				 *    第二类动态批注：row>>行指标编码、col>>列指标编码、data>>行指标ID/列指标ID
				 */
				if(contentJo.containsKey("comment")){
					String comment = String.valueOf(contentJo.get("comment")) ;
					
					if(comment != null && !"".equals(comment)){
						switch(comment){
						case "getcorp" :	// 公司
							cellBean.setComment("getcorp");
							cellBean.setCellType("fixed");
							break ;
							
						case "getperiod" :	// 期间
							cellBean.setComment("getperiod");
							cellBean.setCellType("fixed");
							break ;
							
						case "rowno" :	// 行次
							cellBean.setComment("rowno");
							cellBean.setCellType("fixed");
							break ;
							
						case "rowitem" :	// 行项目
							cellBean.setComment("rowitem");
							cellBean.setCellType("fixed");
							break ;
							
						default :	// 数据区域批注
							String[] arr = comment.split(">>");
							// 指标数据区单元格处理
							if(arr.length == 2){
								// 批注类型：row>行指标单元格、col>列指标单元格、data>数据区单元格
								String type = arr[0] ;
								cellBean.setCellType(type);
								cellBean.setComment(arr[1]);
								if("data".equals(type)){
									// 将批注信息注入在变量中，在填报时可以将数据区单元格删除，然后采用变量与指标对应
									contentJo.put("vname", arr[1]) ;
									contentJo.put("vnameEdit", "hide") ;
								}
								cellBean.setContent(JsonUtil.toJson(contentJo));
							}
							break ;
						}
					}
				}
				cellBean.setCreatedBy(userId);
				cellBean.setCreationDate(cdate);
				cellBean.setLastUpdatedBy(userId);
				cellBean.setLastUpdateDate(cdate);
				
				cells.add(cellBean) ;
			}
			
			tabBean.setCells(cells);
		}
		
		// 页签元素信息
		List<Object> floatingList = (List<Object>) dataJo.get("floatings") ;
		if(floatingList != null && floatingList.size() >0){
			for(int k=0; k<floatingList.size(); k++){
				// {sheet:1, name: "aha", ftype: "meg", json:{}}
				Map<String,Object> floatingJo = (Map<String, Object>) floatingList.get(k) ;
				
				// 页签序号
				int _id2 = Integer.parseInt(String.valueOf(floatingJo.get("sheet"))) ;
				
				if(sheetId != _id2) continue ;
				
				ESTabElementBean elementBean = new ESTabElementBean() ;
				elementBean.setElementId(UUID.randomUUID().toString());
				elementBean.setTabId(tabBean.getTabId());
				elementBean.setName(String.valueOf(floatingJo.get("name")));
				elementBean.setEtype(String.valueOf(floatingJo.get("ftype")));
				elementBean.setContent(String.valueOf(floatingJo.get("json")));
				
				elementBean.setCreatedBy(userId);
				elementBean.setCreationDate(cdate);
				elementBean.setLastUpdatedBy(userId);
				elementBean.setLastUpdateDate(cdate);
				
				elements.add(elementBean) ;
			}
			
			tabBean.setElements(elements);
		}
		
		return tabBean ;
	}
	
	/**
	 * @Title: saveAllTemplates 
	 * @Description: 保存所有模板信息
	 * @param hrcyId
	 * @param sheetId
	 * @param dataJson
	 * @throws Exception    设定文件
	 */
	@SuppressWarnings("unchecked")
	private Map<String,List<?>> saveAllTemplates(String hrcyId, Map<String, Object> dataJo) throws Exception {
		
		Map<String,List<?>> retMap = new HashMap<String,List<?>>() ;
		
		// 模板信息
		List<ESTabBean> tabs = new ArrayList<ESTabBean>();
		List<ESTabCellBean> cells = new ArrayList<ESTabCellBean>() ;
		List<ESTabElementBean> elements = new ArrayList<ESTabElementBean>() ;
		
		// 当前操作人和操作时间
		String userId = CurrentSessionVar.getUserId() ;
		Date cdate = CurrentUserUtil.getCurrentDate() ;
		
		// 页签信息
		HashMap<Integer,ESTabBean> tabMap = new HashMap<Integer,ESTabBean>() ;
		List<Object> sheetsList = (List<Object>) dataJo.get("sheets") ;
		for(int i=0; i<sheetsList.size(); i++){
			// 数据格式：{"id":0,"name":"Sheet1",actived:true}
			Map<String,Object> sheetJo = (Map<String, Object>) sheetsList.get(i) ;
			
			// 页签序号
			int id = Integer.parseInt(String.valueOf(sheetJo.get("id"))) ;
			
			ESTabBean tabBean = new ESTabBean() ;
			tabBean.setTabOrder(id);
			
			tabBean = esDAO.getESTabBeanByOrder(hrcyId, id) ;
			
			tabBean.setLastUpdateDate(cdate);
			tabBean.setLastUpdatedBy(userId);	
			tabBean.setTabName(String.valueOf(sheetJo.get("name")));
			
			tabs.add(tabBean) ;
			
			tabMap.put(id, tabBean) ;
		}
		
		// 页签单元格信息
		List<Object> cellsList = (List<Object>) dataJo.get("cells") ;
		if(cellsList != null && cellsList.size() >0){
			for(int j=0; j<cellsList.size(); j++){
				// {sheet: 1, row:1, col:1, cal:true, json:{data:"=sum(1,2)"}, ...}
				Map<String,Object> cellJo = (Map<String, Object>) cellsList.get(j) ;
				
				// 本页签单元格
				int sheetId = Integer.parseInt(String.valueOf(cellJo.get("sheet"))) ;
				
				int x = Integer.parseInt(String.valueOf(cellJo.get("row"))) ;
				int y = Integer.parseInt(String.valueOf(cellJo.get("col"))) ;
				String contentJson = String.valueOf(cellJo.get("json"))  ;	// 单元格内容信息
				String cal = String.valueOf(cellJo.get("cal")) ;
				
				ESTabCellBean cellBean = new ESTabCellBean() ;
				cellBean.setCellId(UUID.randomUUID().toString());
				cellBean.setTabId(tabMap.get(sheetId).getTabId());
				cellBean.setX(x);
				cellBean.setY(y);
				if(Boolean.parseBoolean(cal)){
					cellBean.setIsCal(1);
				}else{
					cellBean.setIsCal(0);
				}
				cellBean.setContent(contentJson);
				
				// 单元格内容JSON对象
				Map<String,Object> contentJo = JsonUtil.getJsonObj(contentJson) ;
				if(contentJo.containsKey("data")){
					cellBean.setRawData(String.valueOf(contentJo.get("data")));
				}
				
				/*
				 * 单元批注信息：
				 * 	     第一类固定批注：getcorp 、getperiod、rowno、rowitem、
				 *    第二类动态批注：row>>行指标编码、col>>列指标编码、data>>行指标ID/列指标ID
				 */
				if(contentJo.containsKey("comment")){
					String comment = String.valueOf(contentJo.get("comment")) ;
					
					if(comment != null && !"".equals(comment)){
						switch(comment){
						case "getcorp" :	// 公司
							cellBean.setComment("getcorp");
							cellBean.setCellType("fixed");
							break ;
							
						case "getperiod" :	// 期间
							cellBean.setComment("getperiod");
							cellBean.setCellType("fixed");
							break ;
							
						case "rowno" :	// 行次
							cellBean.setComment("rowno");
							cellBean.setCellType("fixed");
							break ;
							
						case "rowitem" :	// 行项目
							cellBean.setComment("rowitem");
							cellBean.setCellType("fixed");
							break ;
							
						default :	// 数据区域批注
							String[] arr = comment.split(">>");
							// 指标数据区单元格处理
							if(arr.length == 2 && "data".equals(arr[0])){
								cellBean.setCellType("data");
								cellBean.setComment(arr[1]);
								// 将批注信息注入在变量中，在填报时可以将数据区单元格删除，然后采用变量与指标对应
								contentJo.put("vname", arr[1]) ;
								contentJo.put("vnameEdit", "hide") ;
								
								cellBean.setContent(JsonUtil.toJson(contentJo));
							}
							break ;
						}
					}
				}
				cellBean.setCreatedBy(userId);
				cellBean.setCreationDate(cdate);
				cellBean.setLastUpdatedBy(userId);
				cellBean.setLastUpdateDate(cdate);
				
				cells.add(cellBean) ;
			}
		}
		
		// 页签元素信息
		List<Object> floatingList = (List<Object>) dataJo.get("floatings") ;
		if(floatingList != null && floatingList.size() >0){
			for(int k=0; k<floatingList.size(); k++){
				// {sheet:1, name: "aha", ftype: "meg", json:{}}
				Map<String,Object> floatingJo = (Map<String, Object>) floatingList.get(k) ;
				
				int sheetId = Integer.parseInt(String.valueOf(floatingJo.get("sheet"))) ;
				
				ESTabElementBean elementBean = new ESTabElementBean() ;
				elementBean.setElementId(UUID.randomUUID().toString());
				elementBean.setTabId(tabMap.get(sheetId).getTabId());
				elementBean.setName(String.valueOf(floatingJo.get("name")));
				elementBean.setEtype(String.valueOf(floatingJo.get("ftype")));
				elementBean.setContent(String.valueOf(floatingJo.get("json")));
				
				elementBean.setCreatedBy(userId);
				elementBean.setCreationDate(cdate);
				elementBean.setLastUpdatedBy(userId);
				elementBean.setLastUpdateDate(cdate);
				
				elements.add(elementBean) ;

			}
		}	
		
		// 返回值
		retMap.put("tabs", tabs) ;
		retMap.put("cells", cells) ;
		retMap.put("elements", elements) ;
		
		return retMap ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: uploadTemplate</p> 
	 * <p>Description: 上传模板格式 </p> 
	 * @param tabsJson
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.service.XCRepTemplateService#impTemplate(java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void uploadTemplate(String paramJson) throws Exception {
		try {
			
		} catch (Exception e) {
			throw e ;
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: saveSaveOrUpdateRowItems</p> 
	 * <p>Description: 保存或更新行指标信息 </p> 
	 * @param accHrcyId
	 * @param tabId
	 * @param tabOrder
	 * @param rowArray
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.service.EnterpriseSheetService#saveSaveOrUpdateItems(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED, propagation=Propagation.REQUIRED,rollbackFor={Exception.class}) 
	public List<Map<String,Object>> saveSaveOrUpdateRowItems(String accHrcyId, String tabId, int tabOrder, String rowArray) throws Exception {
		List<Map<String,Object>> cellsMap = new ArrayList<Map<String,Object>>() ;
		
		try {
			// 当前用户ID和时间
			String userId = CurrentSessionVar.getUserId() ;
			Date cdate = CurrentUserUtil.getCurrentDate() ;
			
			// 行指标信息
			JSONArray rowitemJa = JSONArray.fromObject(rowArray) ;
			if(rowitemJa != null && rowitemJa.size() >0){
				// 存储行指标信息
				List<ESRowItemBean> rowitemList = new ArrayList<ESRowItemBean>() ;
				// 存储行指标引用关系
				List<ESRowItemRefBean> rowitemRefList = new ArrayList<ESRowItemRefBean>() ;
				// 当前栏次
				Object lanno = rowitemJa.getJSONObject(0).get("LANNO") ;
				int currentLanNo = Integer.parseInt(String.valueOf(lanno))  ;
				// 存储指标名称
				List<String> itemNames = new ArrayList<String>() ;

				ESTabBean tabBean = null ;
				if(tabId != null && !"".equals(tabId)){
					tabBean = esDAO.getESTabBeanById(tabId) ;
				}else{
					tabBean = esDAO.getESTabBeanByOrder(accHrcyId, tabOrder) ;
				}
				// 获取已存在的所有行指标信息
				Map<String,ESRowItemBean> itemMap = esDAO.getRowItemsByHrcyId(accHrcyId) ;
				
				for(int i=0; i<rowitemJa.size(); i++){
					JSONObject rowitemJo = rowitemJa.getJSONObject(i) ;
					
					// 坐标位置信息
					int x = Integer.parseInt(String.valueOf(rowitemJo.get("X"))) ;	// 行指标横坐标
					int y = Integer.parseInt(String.valueOf(rowitemJo.get("Y"))) ;	// 行指标列坐标
					int rownoy = Integer.parseInt(String.valueOf(rowitemJo.get("ROWNO_Y"))) ;	// 行指标对应的行次列坐标
					
					// 待更新的单元格信息
					Map<String,Object> cell = Maps.newHashMap() ;
					cell.put("sheetId", tabOrder) ;
					cell.put("row", x) ;
					cell.put("col", rownoy) ;
					
					
					if(rowitemJo.has("ROWITEM_NAME")){
						String rowitemName = rowitemJo.getString("ROWITEM_NAME") ; // 行指标名称
						// 判断指标名称是否重复
						if(itemNames.indexOf(rowitemName) != -1) {
							throw new Exception("行指标名称【"+rowitemName+"】重复，请确认!") ;
						}else{
							itemNames.add(rowitemName) ;
						}
						
						int rowno = Integer.parseInt(String.valueOf(rowitemJo.get("ROWNO"))) ; // 行次号
						ESRowItemBean item = itemMap.get(rowitemName) ;
						
						String rowitemId = null ;
						
						// 指标不存在
						if(item == null){
							rowitemId = UUID.randomUUID().toString() ;
							
							ESRowItemBean rowitem = new ESRowItemBean() ;
							rowitem.setRowitemId(rowitemId);
							rowitem.setAccHrcyId(accHrcyId);
							rowitem.setRowitemCode(SysUtil.getId());
							rowitem.setRowitemName(rowitemName);
							rowitem.setRowitemDesc(null);
							rowitem.setRowitemAlias(null);
							rowitem.setUpcode(tabBean.getTabCode());
							rowitem.setCreatedBy(userId);
							rowitem.setCreationDate(cdate) ;
							rowitem.setLastUpdateDate(cdate);
							rowitem.setLastUpdatedBy(userId);
							
							rowitemList.add(rowitem) ;
							
							// 记录单元格批注信息
							JSONObject rowcellJo = new JSONObject() ;
							
							rowcellJo.put("comment", "row>>".concat(rowitem.getRowitemCode().concat("/").concat(rowitem.getRowitemId()))) ;
							rowcellJo.put("commentEdit", "hide") ;
							rowcellJo.put("dsd", "ed") ; //禁用单元格
							rowcellJo.put("data", rowno) ; // 行次值
							cell.put("json", rowcellJo.toString()) ;
							
						}else{
							JSONObject rowcellJo = new JSONObject() ;
							rowcellJo.put("comment", "row>>".concat(item.getRowitemCode().concat("/").concat(item.getRowitemId()))) ;
							rowcellJo.put("commentEdit", "hide") ;
							rowcellJo.put("dsd", "ed") ; //禁用单元格
							rowcellJo.put("data", rowno) ; // 行次值
							cell.put("json", rowcellJo.toString()) ;
						}
						cellsMap.add(cell) ;
						
						// 行指标引用关系
						ESRowItemRefBean rowitemRef = new ESRowItemRefBean() ;
						rowitemRef.setRmRefId(UUID.randomUUID().toString());
						rowitemRef.setTabId(tabBean.getTabId());
						rowitemRef.setRowitemId(rowitemId==null ? item.getRowitemId() : rowitemId);
						rowitemRef.setRowno(rowno);
						rowitemRef.setLanno(currentLanNo);
						rowitemRef.setEnabled("Y");
						rowitemRef.setX(x);
						rowitemRef.setY(y);
						
						rowitemRef.setCreatedBy(userId);
						rowitemRef.setCreationDate(cdate) ;
						rowitemRef.setLastUpdateDate(cdate);
						rowitemRef.setLastUpdatedBy(userId);
						
						rowitemRefList.add(rowitemRef) ;						
					}
					
					cellsMap.add(cell) ;
				}
				
				// 执行行指标信息保存处理
				esDAO.saveRowItemAndRefInfo(tabBean, rowitemList, rowitemRefList, currentLanNo);
				
			}
		} catch (Exception e) {
			throw e ;
		}
		
		return cellsMap ;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: saveSaveOrUpdateColItems</p> 
	 * <p>Description: 保存或更新列指标信息 </p> 
	 * @param accHrcyId
	 * @param tabOrder
	 * @param colArray
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.service.EnterpriseSheetService#saveSaveOrUpdateColItems(java.lang.String, int, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED, propagation=Propagation.REQUIRED, rollbackFor={Exception.class}) 
	public List<Map<String, Object>> saveSaveOrUpdateColItems(String accHrcyId, String tabId, int tabOrder, String colArray) throws Exception {
		List<Map<String,Object>> cellsMap = new ArrayList<Map<String,Object>>() ;
		
		try {
			// 当前用户ID和时间
			String userId = CurrentSessionVar.getUserId() ;
			Date cdate = CurrentUserUtil.getCurrentDate() ;
			
			// 行指标信息
			JSONArray colitemJa = JSONArray.fromObject(colArray) ;
			if(colitemJa != null && colitemJa.size() >0){
				// 存储行指标信息
				List<ESColItemBean> colitemList = new ArrayList<ESColItemBean>() ;
				// 存储行指标引用关系
				List<ESColItemRefBean> colitemRefList = new ArrayList<ESColItemRefBean>() ;
				
				// 模板信息
				ESTabBean tabBean = null ;
				if(tabId != null && !"".equals(tabId)){
					tabBean = esDAO.getESTabBeanById(tabId) ;
				}else{
					tabBean = esDAO.getESTabBeanByOrder(accHrcyId, tabOrder) ;
				}
				// 获取已存在的所有行指标信息
				Map<String,ESColItemBean> itemMap = esDAO.getColItemsByHrcyId(accHrcyId) ;
				
				for(int i=0; i<colitemJa.size(); i++){
					JSONObject colitemJo = colitemJa.getJSONObject(i) ;
					
					// 列指标列坐标
					int y = Integer.parseInt(String.valueOf(colitemJo.get("Y"))) ;	
					
					// 待更新的单元格信息
					Map<String,Object> cell = Maps.newHashMap() ;
					cell.put("sheetId", tabOrder) ;
					cell.put("row", 1) ;
					cell.put("col", y) ;
					
					if(colitemJo.has("COLITEM_NAME")){
						String colitemName = colitemJo.getString("COLITEM_NAME") ; // 列指标名称
						ESColItemBean item = itemMap.get(colitemName) ;
						
						String colitemId = null ;
						
						// 指标不存在
						if(item == null){
							colitemId = UUID.randomUUID().toString() ;
							
							ESColItemBean colitem = new ESColItemBean() ;
							colitem.setColitemId(colitemId);
							colitem.setAccHrcyId(accHrcyId);
							colitem.setColitemCode(SysUtil.getId());
							colitem.setColitemName(colitemName);
							colitem.setColitemDesc(null);
							colitem.setColitemAlias(null);
							colitem.setUpcode(tabBean.getTabCode());
							colitem.setDatatype(String.valueOf(colitemJo.get("DATATYPE")));
							colitem.setOrderby(i+1); 
							
							colitem.setCreatedBy(userId);
							colitem.setCreationDate(cdate) ;
							colitem.setLastUpdateDate(cdate);
							colitem.setLastUpdatedBy(userId);
							
							colitemList.add(colitem) ;
							// 记录列指标信息
							itemMap.put(colitemName, colitem) ;
							
							// 记录单元格批注信息
							JSONObject rowcellJo = new JSONObject() ;
							rowcellJo.put("comment", "col>>".concat(colitem.getColitemCode().concat("/").concat(colitem.getColitemId()))) ;
							rowcellJo.put("commentEdit", "hide") ;
							rowcellJo.put("dsd", "ed") ; 	// 禁用单元格
							rowcellJo.put("data", y) ; 		// 列次值
							
							cell.put("json", rowcellJo.toString()) ;
							
						}else{
							JSONObject rowcellJo = new JSONObject() ;
							rowcellJo.put("comment", "col>>".concat(item.getColitemCode().concat("/").concat(item.getColitemId()))) ;
							rowcellJo.put("commentEdit", "hide") ;
							rowcellJo.put("dsd", "ed") ; 	// 禁用单元格
							rowcellJo.put("data", y) ; 		// 列次值
							
							cell.put("json", rowcellJo.toString()) ;
						}
						
						cellsMap.add(cell) ;
						
						// 列指标引用关系
						ESColItemRefBean colitemRef = new ESColItemRefBean() ;
						colitemRef.setColRefId(UUID.randomUUID().toString());
						colitemRef.setTabId(tabBean.getTabId());
						colitemRef.setColitemId(colitemId==null ? item.getColitemId() : colitemId);
						colitemRef.setColno(y);
						colitemRef.setLanno(Integer.parseInt(String.valueOf(colitemJo.get("LANNO"))));
						colitemRef.setEnabled("Y");
						
						colitemRef.setCreatedBy(userId);
						colitemRef.setCreationDate(cdate) ;
						colitemRef.setLastUpdateDate(cdate);
						colitemRef.setLastUpdatedBy(userId);
						
						colitemRefList.add(colitemRef) ;						
					}
					
					cellsMap.add(cell) ;
				}
				
				// 执行行指标信息保存处理
				esDAO.saveColItemAndRefInfo(tabBean, colitemList, colitemRefList);
			}
		} catch (Exception e) {
			throw e ;
		}
		
		return cellsMap;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: copyTemplates</p> 
	 * <p>Description: 模板复制格式 </p> 
	 * @param paramJson
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.service.EnterpriseSheetService#copyTemplates(java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void copyTemplates(String paramJson) throws Exception {
		try {
			// 参数信息
			JSONObject jo = JSONObject.fromObject(paramJson) ;
			
			String srcHrcyId = jo.getString("srcHrcyId") ;		 	// 被复制的模板
			String targetHrcyId = jo.getString("targetHrcyId") ;	// 模板的模板
			String tabArray = jo.getString("tabArray") ;			// 被复制的模板ID
			
			// 复制处理
			JSONArray ja = JSONArray.fromObject(tabArray) ;
			if(ja != null && ja.size() >0){
				for(int i=0; i<ja.size(); i++){
					JSONObject tabJo = ja.getJSONObject(i) ;
					String tabId = tabJo.getString("TAB_ID") ;
					String tabCode = tabJo.getString("TAB_CODE") ;
					String tabName = tabJo.getString("TAB_NAME") ;
					
					// 是否合法
					esDAO.isExistsTemplates(targetHrcyId, tabCode, tabName);
					
					// 复制
					esDAO.copyTemplate(srcHrcyId, targetHrcyId, tabId);
				}
			}
			
		} catch (Exception e) {
			throw e ;
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: expTemplates</p> 
	 * <p>Description: 模板导出处理 </p> 
	 * @param accHrcyId
	 * @param paramJson
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.service.EnterpriseSheetService#expTemplates(java.lang.String, java.lang.String)
	 */
	@Override
	public String expTemplates(String accHrcyId, String paramJson) throws Exception {
		String tabJson = null ;
		
		// 参数
		JSONArray ja = null ;
		try {
			ja = JSONArray.fromObject(paramJson) ;
		} catch (Exception e) {
			throw new Exception("参数不合法正确格式为[{'TAB_ID':''},...]") ;
		}
		
		if(ja != null && ja.size() >0){
			List<String> tabs = new ArrayList<String>() ;
			for(int i=0; i<ja.size(); i++){
				JSONObject jo = ja.getJSONObject(i) ;
				if(jo.has("TAB_ID")){
					tabs.add(jo.getString("TAB_ID")) ;
				}
			}
			
			// 执行导出
			Map<String,Object> retMap = tabImpAndExpService.expTabs(accHrcyId, tabs) ;
			tabJson = JsonUtil.toJson(retMap) ;
		}
		
		return tabJson ;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: impTemplates</p> 
	 * <p>Description: 模板接收处理 </p> 
	 * <p> 参数格式说明：
	 *	{ 
	 *		 accHrcyCode : '',
	 *		 accHrcyName : '',
	 *		 rowitems  : [{},{},...], 
	 *		 colitems  : [{},{},...], 
	 *		 functions : [{},{},...],
	 *		 formulas  : [{},{},...],
	 *		 tabs : [{ 
	 *				tabCode : '',
	 *				tabName : '',
	 *				cells :[{},{},...], 
	 *				floatings :[{},{},...], 
	 *				rowitemsRef :[{},{},...], 
	 *				colitemsRef :[{},{},...]
	 *			},{
	 *				tabCode : '',
	 *				tabName : '',
	 *				cells :[{},{},...], 
	 *				floatings :[{},{},...], 
	 *				rowitemsRef :[{},{},...], 
	 *				colitemsRef :[{},{},...]
	 *			},...
	 *		 ] 
	 *	}
	 * </p>
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.service.EnterpriseSheetService#impTemplates()
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void impTemplates(String tabsJson,String accHrcyId) throws Exception {
		try {
			// 参数转换
			Map<String,Object> map = JsonUtil.getJsonObj(tabsJson) ;
			map.put("accHrcyId", accHrcyId) ;
			
			// 执行导入
			tabImpAndExpService.impTabs(map);
			
		} catch (Exception e) {
			throw e ;
		}
	}
	
}
