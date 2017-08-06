package com.xzsoft.xc.rep.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.xzsoft.xc.gl.dao.XCGLCommonDAO;
import com.xzsoft.xc.gl.modal.Organization;
import com.xzsoft.xc.rep.dao.EnterpriseSheetDAO;
import com.xzsoft.xc.rep.dao.XCRepDataDAO;
import com.xzsoft.xc.rep.modal.CellValueBean;
import com.xzsoft.xc.rep.modal.ESTabBean;
import com.xzsoft.xc.rep.modal.ESTabCellBean;
import com.xzsoft.xc.rep.modal.ESTabElementBean;
import com.xzsoft.xc.rep.modal.RepSheetBean;
import com.xzsoft.xc.rep.service.AbstractRepDataHandlerService;
import com.xzsoft.xc.rep.service.XCRepDataService;
import com.xzsoft.xc.rep.util.JsonUtil;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xc.util.common.XConstants;
import com.xzsoft.xip.framework.util.AppContext;
import com.xzsoft.xip.framework.util.JSONUtil;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;

/**
 * @ClassName: XCRepDataServiceImpl 
 * @Description: 云ERP报表模块数据处理服务接口实现类
 * @author linp
 * @date 2016年7月21日 下午5:56:36 
 *
 */
@Service("xcRepDataService")
public class XCRepDataServiceImpl implements XCRepDataService {
	// 日志记录器
	public static Logger log = LoggerFactory.getLogger(XCRepDataServiceImpl.class) ;
	// 时间记录器
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
	
	@Autowired
	private XCRepDataDAO xcRepDataDAO ;
	@Autowired
	private EnterpriseSheetDAO enterpriseSheetDAO ;
	@Autowired
	private XCGLCommonDAO xcglCommonDAO ;
	@Autowired
	private AbstractRepDataHandlerService repDataHandlerServiceImpl ;

	
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getReportResult</p> 
	 * <p>Description: 查询报表结构集信息 </p> 
	 * @param queryParams
	 * <p>
	 * 	参数说明：
	 * 		{'accHrcyId':'','ledgerId':'','periodCode':'','orgId':'','cnyCode':'','sheetTabId':''}
	 * </p>
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.service.XCRepDataService#getReportResult(java.lang.String)
	 */
	@SuppressWarnings({ "unchecked" })
	@Override
	public Map<String, Object> getReportResult(String queryParams) throws Exception {
		// 返回参数
		Map<String,Object> result = Maps.newHashMap() ;
		
		// 查询参数
		JSONObject paramJo = JSONObject.fromObject(queryParams) ;
		
		String accHrcyId = paramJo.getString("accHrcyId") ;
		Object sheetTabId = paramJo.get("sheetTabId") ;
		int tabOrder = Integer.parseInt(String.valueOf(sheetTabId)) ;
		
		String ledgerId = paramJo.getString("ledgerId") ;
		String periodCode = paramJo.getString("periodCode") ;
		String orgId = paramJo.getString("orgId") ;
		
		// 加载参数模板信息
		HashMap<String,Object> tabMap = enterpriseSheetDAO.getAllESTabsExcludeComment(accHrcyId, tabOrder) ;
		
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
				// 查询组织信息
				Organization org = xcglCommonDAO.getOrgById(orgId) ;
				String orgName = org.getOrgName() ;
				
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
							data = data.concat(periodCode) ;
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
			// 指标单元格数据
			List<ESTabCellBean> dataCells = (List<ESTabCellBean>) tabMap.get("dataCells") ;
			if(dataCells != null && dataCells.size() >0){
				// 加载指标数据信息
				String tabId = activedTab.getTabId() ;
				HashMap<String,CellValueBean> cellvalues = xcRepDataDAO.getFixedCellValuesToMap(ledgerId, periodCode, tabId) ;
				
				// 当前报表状态
				HashMap<String,String> sheetMap = xcRepDataDAO.getSheetBaseInfo(ledgerId, periodCode, accHrcyId, activedTab.getTabOrder()) ;
				String appStatus = "" ;
				if(sheetMap != null && sheetMap.size() >0){
					appStatus = sheetMap.get("APP_STATUS") ;
				}
				switch(appStatus){
				case "A":
					result.put("activedSheetStatus", "A") ;
					break ;
				case "E":
					result.put("activedSheetStatus", "E") ;
					break ;
				default :
					result.put("activedSheetStatus", "NONE") ;
					break ;
				}
				
				// 处理指标数据
				for(ESTabCellBean cell : dataCells) {
					Map<String,Object> cfg = JsonUtil.fromJson(cell.getContent(), Map.class) ;
					
					// 是否为表内公式单元格
					boolean isCal = false ;
					if(cfg.containsKey("cal")){
						isCal= Boolean.parseBoolean(String.valueOf(cfg.get("cal"))) ;
					}
					
					if(cellvalues != null && cellvalues.size() >0){
						CellValueBean celldata = cellvalues.get(cell.getComment()) ;
						if(celldata != null && !"".equals(celldata)){
							if(isCal){
								cfg.put("value", celldata.getCellV()) ;
							}else{
								cfg.put("data", celldata.getCellV()) ;
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
					result.put("activedSheetStatus", "NONE") ;
				}
				sheets.add(sheet) ;
			}
		}
		
		// 按页签序号排序
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
		
		return result;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: saveReportData</p> 
	 * <p>Description: 报表数据保存或更新处理 </p> 
	 * @param paramJson 
	 * <p>
	 * 基础参数：
	 * {
	 *	  'ledgerId'   : '',
	 *	  'periodCode' : '',
	 *	  'orgId'      : '',
	 *	  'cnyCode'    : '',
	 *	  'accHrcyId'  : '',
	 *	  'sheetTabId' : ''
	 *	} 
	 * </p>
	 * @param dataJson
	 * <p>
	 * 报表参数：
	 * cells: [{
	 *	    sheet: 5, 
	 *	    row:1, 
	 *	    col:1, 
	 *	    cal:true, 
	 *	    json:{data:"=sum(1,2)"},
	 *	},....]
	 * </p>
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.service.XCRepDataService#saveReportData(java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED, propagation=Propagation.REQUIRED, rollbackFor={Exception.class})
	public void saveOrUpdateRepData(String paramJson,String dataJson) throws Exception {
		String errorMsg = "参数不合法！";
		String language = XCUtil.getLang();
		if(XConstants.EN.equalsIgnoreCase(language)){
			errorMsg = "Invalid Parameters!";
		}
		try {
			if(paramJson == null || "".equals(paramJson)) throw new Exception(errorMsg) ;
			
			// 当前日期和用户
			String userId = CurrentSessionVar.getUserId() ;
			Date cdate = CurrentUserUtil.getCurrentDate() ;
			
			// 报表模板信息
			JSONObject paramJo = JSONObject.fromObject(paramJson) ;
			String accHrcyId = paramJo.getString("accHrcyId") ;
			int tabOrder = Integer.parseInt(String.valueOf(paramJo.get("sheetTabId"))) ;
			ESTabBean activedTabBean = enterpriseSheetDAO.getESTabBeanByOrder(accHrcyId, tabOrder) ;
			
			// 主参数信息
			RepSheetBean sheetBean = JSONUtil.jsonToBean(paramJson, RepSheetBean.class) ;
			sheetBean.setTabId(activedTabBean.getTabId());
			
			// 判断报表是否存在
			RepSheetBean repSheetBean = xcRepDataDAO.getSheetBeanByTabId(sheetBean) ;
			if(repSheetBean == null){	// 如果报表尚未创建
				sheetBean.setCreatedBy(userId);
				sheetBean.setCreationDate(cdate);
				sheetBean.setSheetDesc(activedTabBean.getTabName());
				
			}else{
				sheetBean = repSheetBean ;
				// 报表状态：状态：A-草稿，E-锁定
				String appStatus = sheetBean.getAppStatus() ;
				if("E".equals(appStatus)){
					if(XConstants.ZH.equals(language)){
						throw new Exception("报表【"+activedTabBean.getTabName()+"】已锁定不能修改!") ;
					}else{
						throw new Exception("Can not modify the locked report:【"+activedTabBean.getTabName()+"】!") ;
					}
				}
			}
			sheetBean.setLastUpdateDate(cdate);
			sheetBean.setLastUpdatedBy(userId);
			
			// 指标数据信息
			Map<String,Object> reports = JsonUtil.getJsonObj(dataJson) ;
			List<Object> cellsList = (List<Object>) reports.get("cells") ;
			if(cellsList != null && cellsList.size() >0){
				
				// 定义数组存储指标值数据
				List<CellValueBean> fixedCellvalues = new ArrayList<CellValueBean>() ;
				
				for(Object obj :cellsList){
					Map<String, Object> cell = (Map<String, Object>) obj ;
					// 页签序号
					int sheet = Integer.parseInt(String.valueOf(cell.get("sheet"))) ;
					if(tabOrder == sheet) {
						// 是否为计算单元格
						boolean isCal = false ;
						if(cell.containsKey("cal")){
							isCal = Boolean.parseBoolean(String.valueOf(cell.get("cal"))) ;
						}
						// 单元内容
						String cellJson = String.valueOf(cell.get("json")) ;
						
						Map<String,Object> cellMap = JsonUtil.fromJson(cellJson, Map.class) ;
						
						if(!cellMap.containsKey("vname")) continue ;
						String vname = String.valueOf(cellMap.get("vname")) ; // 通过单元格变量元素获取行列指标ID
						
						Object data = cellMap.get("data")  ;
						if(isCal){ // 计算单元格
							if(cellMap.containsKey("value")){
								data = cellMap.get("value") ;
							}else{
								data = null ;
							}
						}
						if(data == null || "".equals(data)) continue ;
						
						if(vname != null && !"".equals(vname)){
							String[] items = vname.split("/") ;
							if(items.length == 2){
								String rowItemId = items[0] ;
								String colItemId = items[1] ;
								String cellv = String.valueOf(data) ;
								
								// 指标数据信息
								CellValueBean cellBean = new CellValueBean() ;
								cellBean.setValId(UUID.randomUUID().toString());
								cellBean.setLedgerId(sheetBean.getLedgerId());
								cellBean.setOrgId(sheetBean.getOrgId());
								cellBean.setRowItemId(rowItemId);
								cellBean.setColItemId(colItemId);
								cellBean.setPeriodCode(sheetBean.getPeriodCode());
								cellBean.setCnyCode(sheetBean.getCnyCode());
								
								try {
									// 指标值
									double cellData = Double.parseDouble(cellv) ;
									if(cellData == 0) continue ;
									cellBean.setCellV(cellData);
									
								} catch (Exception e) {
									continue ;
								}
								
								cellBean.setCellTextV(null);
								cellBean.setCreatedBy(userId);
								cellBean.setCreationDate(cdate);
								cellBean.setLastUpdatedBy(userId);
								cellBean.setLastUpdateDate(cdate);
								
								fixedCellvalues.add(cellBean) ;
							}
						}
					}
				}
				
				// 执行报表数据保存处理
				xcRepDataDAO.saveOrUpdateRepData(sheetBean, fixedCellvalues);
			}
		} catch (Exception e) {
			throw e ;
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: lockOrUnlockReports</p> 
	 * <p>Description: 锁定报表 </p> 
	 *  <p>
	 *  此功能将报表设置为锁定状态，对锁定之后的报表所做的数据修改操作将不会生效。
	 *  	单表锁定参数格式：
	 *  		{'ledgerId':'','periodCode':'','accHrcyId':'','tabOrder':0,'opMode':'single','opType':'lock'} ,
	 *  	多表锁定参数格式：
	 *  		{'ledgerId':'','periodCode':'','accHrcyId':'','tabOrder':0,'opMode':'batch','opType':'lock','data':[{'sheetId':''},...]}
	 *  </p>
	 * @param request
	 * @param response
	 * @throws Exception 
	 * @see com.xzsoft.cux.xc.service.XCRepDataService#lockReports(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void lockOrUnlockReports(String jsonStr)throws Exception {
		String errorMsg = "参数不合法！";
		String language = XCUtil.getLang();
		if(XConstants.EN.equalsIgnoreCase(language)){
			errorMsg = "Invalid Parameters!";
			}
		try {
			if(jsonStr == null || "".equals(jsonStr)) throw new Exception(errorMsg) ;
			JSONObject paramJo = JSONObject.fromObject(jsonStr) ;
			
			// 操作模式：single-锁定或解锁单表，batch-批量锁定或解锁报表
			String mode = paramJo.getString("opMode") ;
			
			// 操作类型：lock-锁定, unlock-解锁
			String execType = paramJo.getString("opType") ;
			
			// 存储报表信息
			List<String> sheetIds = new ArrayList<String>() ;
			if("batch".equals(mode)){
				String sheetArray = paramJo.getString("data") ;
				JSONArray ja = JSONArray.fromObject(sheetArray) ;
				if(ja != null && ja.size() >0){
					for(int i=0; i<ja.size(); i++){
						JSONObject jo = ja.getJSONObject(i) ;
						sheetIds.add(jo.getString("sheetId")) ;
					}
				}
				paramJo.remove("data") ;
			}
			RepSheetBean sheetBean = (RepSheetBean) JSONObject.toBean(paramJo, RepSheetBean.class) ;
			
			// execType >> lock-锁定, unlock-解锁
			switch(execType){
			case "lock" :
				sheetBean.setAppStatus("E");
				sheetBean.setOldAppStatus("A");
				break ;
			case "unlock" :
				sheetBean.setAppStatus("A");
				sheetBean.setOldAppStatus("E");
				break ;
			default :
				break ;
			}
			
			// 更新报表状态信息
			xcRepDataDAO.batchUpdRepStatus(sheetBean, sheetIds, mode);
			
		} catch (Exception e) {
			throw e ;
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: cleanReports</p> 
	 * <p>Description: 清除报表指标数据 </p> 
	 * <p>
	 *  1、删除已经生成的报表的指标数据，包固定行和浮动行指标数据
	 *  2、只能清除已生成状态的报表数据，报表状态分为：A-已生成；C-审批中；D-驳回；E-已锁定
	 *  
	 *  	单表锁定参数格式：
	 *  		{'ledgerId':'','periodCode':'','accHrcyId':'','tabOrder':0,'opMode':'single'} ,
	 *  	多表锁定参数格式：
	 *  		{'ledgerId':'','periodCode':'','accHrcyId':'','tabOrder':0, 'opMode':'batch','data':[{'sheetId':''},{'sheetId':''},...]}
	 * </p>
	 * @param request
	 * @param response
	 * @throws Exception 
	 * @see com.xzsoft.cux.xc.service.XCRepDataService#cleanReports(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void cleanReports(String jsonStr) throws Exception {
		String language = XCUtil.getLang();
		String errorMsg = "参数不合法";
		if(XConstants.EN.equalsIgnoreCase(language)){
			errorMsg = "Invalid Parameters!";
			}
		try {
			if(jsonStr == null || "".equals(jsonStr)) throw new Exception(errorMsg) ;
			JSONObject paramJo = JSONObject.fromObject(jsonStr) ;
			
			String dataArray = null ;
			if(paramJo.has("data")){
				dataArray = paramJo.getString("data") ;
				paramJo.remove("data") ;
			}
			
			if(paramJo.has("opMode")){
				// 主条件信息
				RepSheetBean sheetBean = (RepSheetBean) JSONObject.toBean(paramJo, RepSheetBean.class) ;
				// 模式
				String opmode = paramJo.getString("opMode") ;
				switch(opmode){
				case "single" : 	// 删除单表指标数据
					RepSheetBean sheet = xcRepDataDAO.getSheetBeanByTabOrder(sheetBean) ;
					if(sheet != null){
						if("E".equals(sheet.getAppStatus())){
							switch (language) {
							case XConstants.ZH:
								throw new Exception(sheet.getTabName()+"已锁定不能执行数据清空处理") ;
							case XConstants.EN:
								throw new Exception("Can not clear data,because "+sheet.getTabName()+" is locked!") ;
							default:
								break;
							}
						}else{
							xcRepDataDAO.cleanReports(sheet, null, "single");
						}
					}else{
						switch (language) {
						case XConstants.ZH:
							throw new Exception("报表未生成无须执行数据清空处理") ;
						case XConstants.EN:
							throw new Exception("There is no need to execute data clearing on an unfinished report!") ;
						default:
							break;
						}
						
					}
					break ;
					
				case "batch" :
					JSONArray sheetArray = JSONArray.fromObject(dataArray) ;
					if(sheetArray != null && sheetArray.size() >0){
						List<String> sheetIds = new ArrayList<String>() ;
						for(int i=0; i<sheetArray.size(); i++){
							JSONObject jo = sheetArray.getJSONObject(i) ;
							String sheetId = jo.getString("sheetId") ;
							sheetIds.add(sheetId) ;
						}
						xcRepDataDAO.cleanReports(sheetBean, sheetIds, "batch");
					}
					break ;
					
				default :
					break ;
				}
			}
		} catch (Exception e) {
			throw e ;
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: gatherRepData</p> 
	 * <p>Description: 批量报表数据采集处理 </p> 
	 * @param paramJson
	 * <p>
	 * 参数信息说明：
	 *  {
	 *  	'ledgerId'   : '',
	 *  	'accHrcyId'  : '',
	 *  	'periodCode' : '',
	 *  	'orgId'      : '',
	 *  	'cnyCode'    : '',
	 *  	'tabOrder'   : '',
	 *  	'opMode'     : 'single/batch'
	 *  	'data' : [{'tabId':''},...]
	 *  }
	 *  </p>
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.service.XCRepDataService#batchGatherRepData(java.lang.String)
	 */
	@Override
	public void gatherRepData(String paramJson) throws Exception {
		String language = XCUtil.getLang();
		String errorMsg = "参数不合法";
		if(XConstants.EN.equalsIgnoreCase(language)){
			errorMsg = "Invalid Parameters!";
			}
		if(paramJson == null || "".equals(paramJson)) throw new Exception(errorMsg) ;
		JSONObject paramJo = JSONObject.fromObject(paramJson) ;
		
		// 操作模式 : single-单表采集, batch-多表采集
		String mode = paramJo.getString("opMode") ;
		List<String> tabList = new ArrayList<String>() ;
		if("batch".equals(mode)){
			String data = paramJo.getString("data") ;
			JSONArray ja = JSONArray.fromObject(data) ;
			if(ja != null && ja.size() >0){
				for(int i=0; i<ja.size(); i++){
					JSONObject jo = ja.getJSONObject(i) ;
					tabList.add(jo.getString("tabId")) ;
				}
			}
			paramJo.remove("data") ;
		}
		
		// 将JSON字符串转换为JavaBean
		RepSheetBean sheetBean = (RepSheetBean) JSONObject.toBean(paramJo, RepSheetBean.class) ;
		
		// 按单表或批量模式执行报表数据采集
		XCRepDataService service = (XCRepDataService) AppContext.getApplicationContext().getBean("xcRepDataService") ;
		ESTabBean tabBean = null ;
		
		switch(mode){
		case "single" :
			// 查询模板信息
			tabBean = enterpriseSheetDAO.getESTabBeanByOrder(sheetBean.getAccHrcyId(), sheetBean.getTabOrder()) ;
			// 执行单表采集
			service.gatherSingleRepData(sheetBean, tabBean);
			break ;
			
		case "batch" :
			if(!tabList.isEmpty()){
				// 执行多表循环采集
				for (String tabId : tabList) {
					try {
						tabBean = enterpriseSheetDAO.getESTabBeanById(tabId) ;
						service.gatherSingleRepData(sheetBean, tabBean);
					} catch (Exception e) {
						log.error(e.getMessage());
					}
				}
			}
			break ;
			
		default :
			break ;
		}
	}

	/**
	 * @Title: gatherSingleRepData 
	 * @Description: 对单张报表执行数据采集处理
	 * @param sheetBean
	 * @throws Exception    设定文件
	 */
	@Transactional(isolation=Isolation.READ_COMMITTED, propagation=Propagation.REQUIRED, rollbackFor={Exception.class})
	@Override
	public void gatherSingleRepData(RepSheetBean sheetBean, ESTabBean tabBean) throws Exception {
		// 处理报表基础信息
		repDataHandlerServiceImpl.handlerRepSheet(sheetBean, tabBean);
		
		// 处理固定行指标数据
		repDataHandlerServiceImpl.handlerFixedCellValue(sheetBean, tabBean);
	}

}
