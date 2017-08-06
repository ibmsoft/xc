package com.xzsoft.xc.rep.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

/**
 * @ClassName: EnterpriseSheetService 
 * @Description: EnterpriseSheet模板格式处理服务接口
 * @author linp
 * @date 2016年9月1日 上午9:26:21 
 *
 */
public interface EnterpriseSheetService {
	
	/**
	 * @Title: findTemplate 
	 * @Description: 按科目体系查询模板格式信息
	 * @param hrcyId
	 * @return
	 * @throws Exception    设定文件
	 */
	public Map<String, Object> findAllTemplates(String hrcyId) throws Exception ;
	
	/**
	 * @Title: findAllTemplateById 
	 * @Description: 按照模板ID查询模板格式信息
	 * @param tabId
	 * @return
	 * @throws Exception    设定文件
	 */
	public Map<String, Object> findAllTemplateById(String tabId) throws Exception ;
	
	/**
	 * @Title: findTemplateByOrder 
	 * @Description: 按模板序号查询模板信息
	 * @param hrcyId
	 * @param tabOrder
	 * @return
	 * @throws Exception    设定文件
	 */
	public Map<String, Object> findTemplateByOrder(String hrcyId, int tabOrder) throws Exception ;
	
	/**
	 * @Title: createTemplate 
	 * @Description: 新增报表模板
	 * @param paramJson
	 * @throws Exception    设定文件
	 */
	public JSONObject createTemplate(String paramJson) throws Exception ;
	
	/**
	 * @Title: deleteTemplate 
	 * @Description: 删除报表模板
	 * @param hrcyId
	 * @param tabCode
	 * @throws Exception    设定文件
	 */
	public void deleteTemplate(String hrcyId, int tabOrder) throws Exception ;
	
	/**
	 * @Title: deleteTemplate 
	 * @Description: 按模板ID删除模板信息
	 * @param tabId
	 * @throws Exception    设定文件
	 */
	public void deleteTemplate(String tabId) throws Exception ;
	
	/**
	 * @Title: saveTemplate 
	 * @Description: 保存报表模板信息
	 * <p>
	 * 	hrcyId : 科目体系ID
	 *  dataJson : 表格数据信息 
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
	 * </p>
	 * @param hrcyId 
	 * @param dataJson
	 * @throws Exception    设定文件
	 */
	public void saveTemplate(String hrcyId, int sheetId, String dataJson) throws Exception ;
	
	/**
	 * @Title: uploadTemplate 
	 * @Description: 上传模板格式
	 * @param paramJson
	 * @throws Exception    设定文件
	 */
	public void uploadTemplate(String paramJson) throws Exception ;
	
	/**
	 * @Title: saveSaveOrUpdateRowItems 
	 * @Description: 保存或更新行指标信息
	 * @param accHrcyId
	 * @param tabId
	 * @param tabOrder
	 * @param rowArray
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<Map<String,Object>> saveSaveOrUpdateRowItems(String accHrcyId,String tabId,int tabOrder, String rowArray) throws Exception ;
	
	/**
	 * @Title: saveSaveOrUpdateColItems 
	 * @Description: 保存或更新列指标信息
	 * @param accHrcyId
	 * @param tabOrder
	 * @param colArray
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<Map<String,Object>> saveSaveOrUpdateColItems(String accHrcyId,String tabId,int tabOrder, String colArray) throws Exception ;
	
	/**
	 * @Title: copyTemplates 
	 * @Description: 模板复制格式
	 * @param paramJson
	 * @throws Exception    设定文件
	 */
	public void copyTemplates(String paramJson) throws Exception ;
	
	/**
	 * @Title: expTemplates 
	 * @Description: 模板导出
	 * @param accHrcyId
	 * @param paramJson
	 * <p> 返回值参数格式说明：
	 *	{ 
	 *		 accHrcyId : '',
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
	 * @throws Exception    设定文件
	 */
	public String expTemplates(String accHrcyId, String paramJson) throws Exception ;
	
	/**
	 * @Title: impTemplates 
	 * @Description: 模板接收
	 * <p> 参数格式说明：
	 *	{ 
	 *		 accHrcyId : '',
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
	 * @throws Exception    设定文件
	 */
	public void impTemplates(String tabsJson,String accHrcyId) throws Exception ;
}
