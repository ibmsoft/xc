package com.xzsoft.xc.rep.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Maps;
import com.xzsoft.xc.rep.dao.EnterpriseSheetDAO;
import com.xzsoft.xc.rep.mapper.ESColItemMapper;
import com.xzsoft.xc.rep.mapper.ESRowItemMapper;
import com.xzsoft.xc.rep.mapper.ESTabCellMapper;
import com.xzsoft.xc.rep.mapper.ESTabElementMapper;
import com.xzsoft.xc.rep.mapper.ESTabMapper;
import com.xzsoft.xc.rep.modal.ESColItemBean;
import com.xzsoft.xc.rep.modal.ESColItemRefBean;
import com.xzsoft.xc.rep.modal.ESRowItemBean;
import com.xzsoft.xc.rep.modal.ESRowItemRefBean;
import com.xzsoft.xc.rep.modal.ESTabBean;
import com.xzsoft.xc.rep.modal.ESTabCellBean;
import com.xzsoft.xc.rep.modal.ESTabElementBean;
import com.xzsoft.xc.rep.util.JsonUtil;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;

/**
 * @ClassName: EnterpriseSheetDAOImpl 
 * @Description: EnterpriseSheet 数据处理层接口实现类
 * @author linp
 * @date 2016年9月1日 上午9:19:08 
 *
 */
@Repository("esDAO")
public class EnterpriseSheetDAOImpl implements EnterpriseSheetDAO {
	@Autowired
	private ESTabMapper eSTabMapper ;
	@Autowired
	private ESTabCellMapper eSTabCellMapper ;
	@Autowired
	private  ESTabElementMapper eSTabElementMapper ;
	@Autowired
	private ESRowItemMapper eSRowItemMapper ;
	@Autowired
	private ESColItemMapper eSColItemMapper ;
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate ;
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getAllESTabs</p> 
	 * <p>Description: 根据科目体系下模板信息 </p> 
	 * @param accHrcyId 必需项
	 * @param tabOrder 可选项，如果值为-1则查询全部，如果为0或正整数则查询指定的报表
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.EnterpriseSheetDAO#getAllESTabs(java.lang.String)
	 */
	@Override
	public List<ESTabBean> getAllESTabs(String accHrcyId, int tabOrder) throws Exception {
		HashMap<String,Object> map = new HashMap<String,Object>() ;
		map.put("hrcyId", accHrcyId) ;
		map.put("tabOrder", tabOrder) ;
		
		return eSTabMapper.getAllESTabs(map) ;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: getESTabIncludeFormat</p> 
	 * <p>Description: 按照模板ID查询模板格式信息 </p> 
	 * @param tabId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.EnterpriseSheetDAO#getESTabIncludeFormat(java.lang.String)
	 */
	@Override
	public ESTabBean getESTabIncludeFormat(String tabId) throws Exception {
		return eSTabMapper.getESTabIncFormat(tabId) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getAllESTabsExcludeComment</p> 
	 * <p>Description: 根据科目体系下模板信息,并去除相关批注信息 </p> 
	 * @param accHrcyId 必需项
	 * @param tabOrder 可选项，如果值为-1则查询全部，如果为0或正整数则查询指定的报表
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.EnterpriseSheetDAO#getAllESTabsNotIncComment(java.lang.String, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public HashMap<String,Object> getAllESTabsExcludeComment(String accHrcyId, int tabOrder) throws Exception {
		// 存储返回参数
		HashMap<String,Object> retMap = new HashMap<String,Object>() ;
		
		// 当前科目体系下所有的模板信息
		List<ESTabBean> tabs = eSTabMapper.getAllESTabBeans(accHrcyId) ;
 		
		if(tabs != null && tabs.size() >0){
			// 定义数组存放待处理的单元格信息
			List<ESTabCellBean> fixedCells = new ArrayList<ESTabCellBean>() ;
			List<ESTabCellBean> dataCells = new ArrayList<ESTabCellBean>() ;
			
			// 计算活动页签序号
			if(tabOrder == -1){
				List<ESTabBean> activedTabs = eSTabMapper.getESTab4ActivedOrMinOrder(accHrcyId) ;
				if(activedTabs.size() == 1){
					tabOrder = activedTabs.get(0).getTabOrder() ;
				}else{
					for(ESTabBean tab : activedTabs) {
						if(tab.getIsActive() == 1){
							tabOrder = tab.getTabOrder() ;
							break ;
						}
					}
				}
			}
			
			// 查询活动页签的格式信息
			List<ESTabBean> tabBeans = this.getAllESTabs(accHrcyId, tabOrder) ;
			ESTabBean tabBean = tabBeans.get(0) ;
			// 处理格式
			if(tabBean != null){
				
				List<ESTabCellBean> cells = tabBean.getCells() ;
				
				if(cells != null && cells.size()>0){
					// 记录待删除的单元格
					List<ESTabCellBean> delCells = new ArrayList<ESTabCellBean>() ;
					Map<String,Object> contentMap = Maps.newHashMap() ;
					
					for(ESTabCellBean cell : cells){
						String cellType = cell.getCellType() ;
						cellType = cellType==null ? "others" : cellType ;
						
						String comment = cell.getComment() ;
						String cellJson = cell.getContent() ;
						
						switch(cellType){
						case "fixed" :
							// 处理主区域单元格信息
							if("getcorp".equals(comment) || "getperiod".equals(comment)){
								fixedCells.add(cell) ;
								delCells.add(cell) ;
							}
							break ;
							
						case "data" :
							// 处理数据区单元格信息
							contentMap = JsonUtil.fromJson(cellJson, Map.class) ;
							contentMap.remove("comment") ;
							contentMap.remove("commentEdit") ;
							cell.setContent(JsonUtil.toJson(contentMap));
							
							dataCells.add(cell) ;
							delCells.add(cell) ;
							break ;
							
						default :
							// 清理非数据区单元格信息
							contentMap = JsonUtil.fromJson(cellJson, Map.class) ;
							contentMap.remove("comment") ;
							contentMap.remove("commentEdit") ;
							cell.setContent(JsonUtil.toJson(contentMap));
							
							break ;
						}
					}
					// 删除重复单元格
					cells.removeAll(delCells) ;
					tabBean.setCells(cells);
				}
				
				// 处理模板信息
				for(ESTabBean tab : tabs) {
					if(tab.getTabId().equals(tabBean.getTabId())){
						tabs.remove(tab) ;
						break ;
					}
				}
			}
			
			// 定义返回值
			retMap.put("tabs", tabs) ;
			retMap.put("activedTab", tabBean) ;
			retMap.put("fixedCells", fixedCells) ;
			retMap.put("dataCells", dataCells) ;
		}
		
		return retMap ;
		
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getAllESTabBeans</p> 
	 * <p>Description: 查询科目体系下所有的模板基础信息 </p> 
	 * @param accHrcyId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.EnterpriseSheetDAO#getAllESTabBeans(java.lang.String)
	 */
	@Override
	public List<ESTabBean> getAllESTabBeans(String accHrcyId) throws Exception {
		return eSTabMapper.getAllESTabBeans(accHrcyId) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getAllESTabBeans4Map</p> 
	 * <p>Description: 查询科目体系下所有的模板基础信息 </p> 
	 * @param accHrcyId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.EnterpriseSheetDAO#getAllESTabBeans4Map(java.lang.String)
	 */
	@Override
	public Map<String,ESTabBean> getAllESTabBeans4Map(String accHrcyId) throws Exception {
		Map<String,ESTabBean> map = new HashMap<String,ESTabBean>() ;
		
		List<ESTabBean> list = eSTabMapper.getAllESTabBeans(accHrcyId) ;
		for(ESTabBean bean : list){
			map.put(bean.getTabCode(), bean) ;
		}
		
		return map ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getNextTabOrder</p> 
	 * <p>Description: 获取下一个模板页签序号 </p> 
	 * @param accHrcyId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.EnterpriseSheetDAO#getMaxTabOrder(java.lang.String)
	 */
	@Override
	public int getNextTabOrder(String accHrcyId) throws Exception {
		int order = 0 ;
		
		try {
			order = eSTabMapper.getMaxTabOrder(accHrcyId) ;
			order += 1 ;
			
		} catch (Exception e) {
			order = 0 ;
		}
		
		return order ;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: getMinTabOrder</p> 
	 * <p>Description: 获取模板下最大页签序号 </p> 
	 * @param accHrcyId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.EnterpriseSheetDAO#getMinTabOrder(java.lang.String)
	 */
	@Override
	public int getMaxTabOrder(String accHrcyId) throws Exception {
		int order = 0 ;
		
		try {
			order = eSTabMapper.getMaxTabOrder(accHrcyId) ;
			
		} catch (Exception e) {
			order = 0 ;
		}
		
		return order ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: isExistsTabCode</p> 
	 * <p>Description: 页签编码是否已存在 </p> 
	 * @param accHrcyId
	 * @param tabCode
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.EnterpriseSheetDAO#isExistsTabCode(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isExistsTabCode(String accHrcyId, String tabCode) throws Exception {
		boolean flag = false ;
		
		HashMap<String,String> map = new HashMap<String,String>() ;
		map.put("accHrcyId", accHrcyId) ;
		map.put("tabCode", tabCode) ;
		
		ESTabBean tabBean = eSTabMapper.getTabByCode(map) ;
		
		if(tabBean != null) flag = true ;
		
		return flag ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: isExistsTabName</p> 
	 * <p>Description: 页签名称是否已存在 </p> 
	 * @param accHrcyId
	 * @param tabName
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.EnterpriseSheetDAO#isExistsTabName(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isExistsTabName(String accHrcyId, String tabName) throws Exception {
		boolean flag = false ;
		
		HashMap<String,String> map = new HashMap<String,String>() ;
		map.put("accHrcyId", accHrcyId) ;
		map.put("tabCode", tabName) ;
		
		ESTabBean tabBean = eSTabMapper.getTabByCode(map) ;
		
		if(tabBean != null) flag = true ;
		
		return flag ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getESTabBeanByOrder</p> 
	 * <p>Description: 按tabOrder查询ESTabBean </p> 
	 * @param accHrcyId
	 * @param tabOrder
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.EnterpriseSheetDAO#getESTabBeanByOrder(java.lang.String, int)
	 */
	@Override
	public ESTabBean getESTabBeanByOrder(String accHrcyId, int tabOrder) throws Exception {
		HashMap<String,Object> map = new HashMap<String,Object>() ;
		map.put("accHrcyId", accHrcyId) ;
		map.put("tabOrder", tabOrder) ;
		
		ESTabBean tabBean = eSTabMapper.getTabByOrder(map) ;
		
		return tabBean ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getESTabBeanById</p> 
	 * <p>Description: 按模板ID查询模板基础信息 </p> 
	 * @param tabId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.EnterpriseSheetDAO#getESTabBeanById(java.lang.String)
	 */
	@Override
	public ESTabBean getESTabBeanById(String tabId) throws Exception {
		return eSTabMapper.getTabById(tabId) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: saveTabSheet</p> 
	 * <p>Description: 保存页签信息 </p> 
	 * @param tabBean
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.EnterpriseSheetDAO#saveTabSheet(com.xzsoft.xc.rep.modal.ESTabBean)
	 */
	@Override
	public void saveTabSheet(ESTabBean tabBean) throws Exception {
		try {
			String userId = CurrentSessionVar.getUserId() ;
			Date cdate = CurrentUserUtil.getCurrentDate() ;
			
			// 主键
			tabBean.setTabId(UUID.randomUUID().toString());
			
			// set_who字段
			tabBean.setCreatedBy(userId);
			tabBean.setCreationDate(cdate);
			tabBean.setLastUpdateDate(cdate);
			tabBean.setLastUpdatedBy(userId);
			
			// 保存
			eSTabMapper.insertTab(tabBean);
			
		} catch (Exception e) {
			throw e ;
		}
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: beforeDelCheck</p> 
	 * <p>Description: 模板删除前的校验 </p> 
	 * @param tabBean
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.EnterpriseSheetDAO#beforeDelCheck(com.xzsoft.xc.rep.modal.ESTabBean)
	 */
	@Override
	public void beforeDelCheck(ESTabBean tabBean) throws Exception {
		
		String tabId = tabBean.getTabId() ;
		String accHrcyId = tabBean.getAccHrcyId() ;
		
		// 判断模板是否已经生成了报表
		HashMap<String,String> map = new HashMap<String,String>() ;
		map.put("accHrcyId", accHrcyId) ;
		map.put("tabId", tabId) ;
		
		int formulaCount = 0 ;
		try {
			formulaCount = eSTabMapper.getFormulaCount(map) ;
		} catch (Exception e) {
			formulaCount = 0 ;
		}
		if(formulaCount > 0){
			throw new Exception("当前模板下存在公式信息不能删除") ;
		}
		
		// 判断模板是否已经设置了公式信息
		int sheetCount = 0 ;
		try {
			sheetCount = eSTabMapper.getSheetCount(tabId) ;
		} catch (Exception e) {
			sheetCount = 0 ;
		}
		
		if(sheetCount >0){
			throw new Exception("当前模板已关联报表信息不能删除") ;
		}
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: delTabSheet</p> 
	 * <p>Description: 删除模板信息 </p> 
	 * @param tabId
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.EnterpriseSheetDAO#delTabSheet(java.lang.String, int)
	 */
	@Override
	public void delTabSheet(ESTabBean tabBean) throws Exception {
		// 模板ID
		String tabId = tabBean.getTabId() ;
		
		// 删除行指标引用关系
		eSRowItemMapper.delRowItemRef(tabId);
		
		// 删除列指标引用关系
		eSColItemMapper.delColItemRef(tabId);
		
		// 删除页签元素
		eSTabElementMapper.delTabElements(tabId);
		
		// 删除页签单元格
		eSTabCellMapper.delTabCells(tabId);
		
		// 删除页签
		eSTabMapper.delTabSheet(tabId);
	}
	
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: saveTabFormat</p> 
	 * <p>Description: 保存模板格式信息 </p> 
	 * @param opMode
	 * @param tabBean
	 * @param tabs
	 * @param cells
	 * @param elements
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.EnterpriseSheetDAO#saveTabFormat(java.lang.String, com.xzsoft.xc.rep.modal.ESTabBean, java.util.List, java.util.List, java.util.List)
	 */
	@Override
	public void saveTabFormat(String opMode, ESTabBean tabBean, List<ESTabBean> tabs, List<ESTabCellBean> cells, List<ESTabElementBean> elements) throws Exception {
		
		if("single".equals(opMode)){ //单表模式
			
			// 删除单元格信息
			eSTabCellMapper.delTabCells(tabBean.getTabId());
			// 删除元素信息
			eSTabElementMapper.delTabElements(tabBean.getTabId());
			// 更新基础信息
			eSTabMapper.updateTabSheet(tabBean);
			
			cells = tabBean.getCells() ;
			elements = tabBean.getElements() ;
			
		}else{ // 多表模式
			
			// 批量删除模板单元格
			String delCellSql = "delete from xc_rep_tab_cell where tab_id = :tabId " ;
			// 批量删除模板页签元素
			String delElementSql = "delete from xc_rep_tab_element where tab_id = :tabId " ;
			// 批量更新基础信息
			StringBuffer tabStr = new StringBuffer() ;
			tabStr.append(" UPDATE XC_REP_TAB T set T.TAB_NAME = :tabName, ") ;
			tabStr.append(" T.LAST_UPDATED_BY = :lastUpdatedBy , T.LAST_UPDATE_DATE = :lastUpdateDate ") ;
			tabStr.append(" WHERE T.TAB_ID = :tabId ") ;
			String delTabSql = tabStr.toString() ;
			
			// 执行批量删除处理
			if(tabs != null && tabs.size()>0){
				SqlParameterSource[] batchArgs = new SqlParameterSource[tabs.size()];
				int i = 0;
				for (ESTabBean tab : tabs) {
					batchArgs[i] = new BeanPropertySqlParameterSource(tab);
					i++ ;
				}
				
				namedParameterJdbcTemplate.batchUpdate(delCellSql, batchArgs) ;
				namedParameterJdbcTemplate.batchUpdate(delElementSql, batchArgs) ;
				namedParameterJdbcTemplate.batchUpdate(delTabSql, batchArgs) ;
			}
		}

		// 批量保存模板单元格信息
		StringBuffer cellStr = new StringBuffer() ;
		cellStr.append("insert into xc_rep_tab_cell(");
		cellStr.append("  cell_id,tab_id,x,y,content,is_cal,raw_data,cell_type,comment,");
		cellStr.append("  creation_date,created_by,last_update_date,last_updated_by");
		cellStr.append(")values(");
		cellStr.append("  :cellId,:tabId,:x,:y,:content,:isCal,:rawData,:cellType,:comment,");
		cellStr.append("  :creationDate,:createdBy,:lastUpdateDate,:lastUpdatedBy");
		cellStr.append(")");
		
		if(cells != null && cells.size()>0){
			SqlParameterSource[] batchArgs = new SqlParameterSource[cells.size()];
			int i = 0;
			for (ESTabCellBean cell : cells) {
				batchArgs[i] = new BeanPropertySqlParameterSource(cell);
				i++ ;
			}
			namedParameterJdbcTemplate.batchUpdate(cellStr.toString(), batchArgs) ;
		}
		
		// 批量保存模板元素信息
		StringBuffer elementStr = new StringBuffer() ;
		elementStr.append("insert into xc_rep_tab_element(") ;
		elementStr.append("  element_id,tab_id,name,etype,content,") ;
		elementStr.append("  creation_date,created_by,last_update_date,last_updated_by") ;
		elementStr.append(")values(") ;
		elementStr.append("  :elementId,:tabId,:name,:etype,:content,") ;
		elementStr.append("  :creationDate,:createdBy,:lastUpdateDate,:lastUpdatedBy") ;
		elementStr.append(")") ;
		
		if(elements != null && elements.size()>0){
			SqlParameterSource[] batchArgs = new SqlParameterSource[elements.size()];
			int i = 0;
			for (ESTabElementBean element : elements) {
				batchArgs[i] = new BeanPropertySqlParameterSource(element);
				i++ ;
			}
			namedParameterJdbcTemplate.batchUpdate(elementStr.toString(), batchArgs) ;
		}
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getRowItems</p> 
	 * <p>Description: </p> 
	 * @param accHrcyId
	 * @param tabOrder
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.EnterpriseSheetDAO#getRowItems(java.lang.String, int)
	 */
	@Override
	public Map<String,ESRowItemBean> getRowItemsByHrcyId(String accHrcyId) throws Exception {
		Map<String,ESRowItemBean> map = Maps.newHashMap() ;
		
		List<ESRowItemBean> items = eSRowItemMapper.getRowItemsByHrcyId(accHrcyId) ;
		if(items != null && items.size() >0){
			for(ESRowItemBean itemBean : items) {
				map.put(itemBean.getRowitemName(), itemBean) ;
			}
		}
		
		return map ;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: saveRowItemAndRefInfo</p> 
	 * <p>Description: 保存模板行指标及引用关系 </p> 
	 * @param rowitemList
	 * @param rowitemRefList
	 * @param currentLanNo
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.EnterpriseSheetDAO#saveRowItemAndRefInfo(java.util.List, java.util.List, int)
	 */
	@Override
	public void saveRowItemAndRefInfo(ESTabBean tabBean,List<ESRowItemBean> rowitemList,
				List<ESRowItemRefBean> rowitemRefList, int currentLanNo) throws Exception {
		try {
			// 保存行指标
			StringBuffer rowStr = new StringBuffer() ;
			rowStr.append("insert into xc_rep_rowitems ( ") ;
			rowStr.append(" rowitem_id, acc_hrcy_id, rowitem_code, rowitem_name, rowitem_desc, rowitem_alias, upcode, ") ;
			rowStr.append(" creation_date, created_by, last_update_date, last_updated_by ") ;
			rowStr.append(" )values(") ;
			rowStr.append(" :rowitemId, :accHrcyId, :rowitemCode, :rowitemName, :rowitemDesc, :rowitemAlias, :upcode, ") ;
			rowStr.append(" :creationDate, :createdBy, :lastUpdateDate, :lastUpdatedBy ") ;
			rowStr.append(")") ;
			SqlParameterSource[] rowArgs = new SqlParameterSource[rowitemList.size()];
			int i = 0;
			for (ESRowItemBean bean : rowitemList) {
				rowArgs[i] = new BeanPropertySqlParameterSource(bean);
				i++ ;
			}
			namedParameterJdbcTemplate.batchUpdate(rowStr.toString(), rowArgs) ;
			
			
			// 按栏次清除行指标引用关系
			HashMap<String,Object> delMap = new HashMap<String,Object>() ;
			delMap.put("tabId", tabBean.getTabId()) ;
			delMap.put("lanno", currentLanNo) ;
			eSRowItemMapper.delRowItemRefByLanno(delMap);
			
			// 创建行指标引用关系
			StringBuffer rowRefSql = new StringBuffer() ;
			rowRefSql.append("insert into xc_rep_rm_ref ( ") ;
			rowRefSql.append(" rm_ref_id,tab_id,rowitem_id,rowno,lanno,x,y,enabled, ") ;
			rowRefSql.append(" creation_date, created_by, last_update_date, last_updated_by ") ;
			rowRefSql.append(" )values(") ;
			rowRefSql.append(" :rmRefId, :tabId, :rowitemId, :rowno, :lanno, :x, :y, :enabled, ") ;
			rowRefSql.append(" :creationDate, :createdBy, :lastUpdateDate, :lastUpdatedBy ") ;
			rowRefSql.append(")") ;
			SqlParameterSource[] refArgs = new SqlParameterSource[rowitemRefList.size()];
			int j = 0;
			for (ESRowItemRefBean bean : rowitemRefList) {
				refArgs[j] = new BeanPropertySqlParameterSource(bean);
				j++ ;
			}
			namedParameterJdbcTemplate.batchUpdate(rowRefSql.toString(), refArgs) ;
			
		} catch (Exception e) {
			throw e ;
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: getColItemsByHrcyId</p> 
	 * <p>Description: 查询模板列指标信息 </p> 
	 * @param accHrcyId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.EnterpriseSheetDAO#getColItemsByHrcyId(java.lang.String)
	 */
	@Override
	public Map<String, ESColItemBean> getColItemsByHrcyId(String accHrcyId) throws Exception {
		Map<String,ESColItemBean> map = Maps.newHashMap() ;
		
		List<ESColItemBean> items = eSColItemMapper.getColItemsByHrcyId(accHrcyId) ;
		if(items != null && items.size() >0){
			for(ESColItemBean itemBean : items) {
				map.put(itemBean.getColitemName(), itemBean) ;
			}
		}
		
		return map ;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: saveColItemAndRefInfo</p> 
	 * <p>Description: 保存模板列指标及引用关系 </p> 
	 * @param tabBean
	 * @param colitemList
	 * @param colitemRefList
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.EnterpriseSheetDAO#saveColItemAndRefInfo(com.xzsoft.xc.rep.modal.ESTabBean, java.util.List, java.util.List)
	 */
	@Override
	public void saveColItemAndRefInfo(ESTabBean tabBean,
			List<ESColItemBean> colitemList,List<ESColItemRefBean> colitemRefList) throws Exception {
		try {
			// 保存行指标
			StringBuffer colStr = new StringBuffer() ;
			colStr.append("insert into xc_rep_colitems ( ") ;
			colStr.append(" colitem_id,acc_hrcy_id,colitem_code,colitem_name,colitem_desc,colitem_alias,upcode,datatype,orderby, ") ;
			colStr.append(" creation_date, created_by, last_update_date, last_updated_by ") ;
			colStr.append(" )values(") ;
			colStr.append(" :colitemId, :accHrcyId, :colitemCode, :colitemName, :colitemDesc, :colitemAlias, :upcode, :datatype, :orderby, ") ;
			colStr.append(" :creationDate, :createdBy, :lastUpdateDate, :lastUpdatedBy ") ;
			colStr.append(")") ;
			SqlParameterSource[] rowArgs = new SqlParameterSource[colitemList.size()];
			int i = 0;
			for (ESColItemBean bean : colitemList) {
				rowArgs[i] = new BeanPropertySqlParameterSource(bean);
				i++ ;
			}
			namedParameterJdbcTemplate.batchUpdate(colStr.toString(), rowArgs) ;
			
			// 按模板清除列指标引用关系
			eSColItemMapper.delColItemRef(tabBean.getTabId());
			
			// 创建行指标引用关系
			StringBuffer colRefSql = new StringBuffer() ;
			colRefSql.append("insert into xc_rep_cm_ref ( ") ;
			colRefSql.append(" col_ref_id,tab_id,colitem_id,colno,lanno,enabled, ") ;
			colRefSql.append(" creation_date, created_by, last_update_date, last_updated_by ") ;
			colRefSql.append(" )values(") ;
			colRefSql.append(" :colRefId, :tabId, :colitemId, :colno, :lanno, :enabled, ") ;
			colRefSql.append(" :creationDate, :createdBy, :lastUpdateDate, :lastUpdatedBy ") ;
			colRefSql.append(")") ;
			SqlParameterSource[] refArgs = new SqlParameterSource[colitemRefList.size()];
			int j = 0;
			for (ESColItemRefBean bean : colitemRefList) {
				refArgs[j] = new BeanPropertySqlParameterSource(bean);
				j++ ;
			}
			namedParameterJdbcTemplate.batchUpdate(colRefSql.toString(), refArgs) ;
			
		} catch (Exception e) {
			throw e ;
		}
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: isExistsTemplates</p> 
	 * <p>Description: </p> 
	 * @param targetHrcyId
	 * @param tabCode
	 * @param tabName
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.EnterpriseSheetDAO#isExistsTemplates(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void isExistsTemplates(String targetHrcyId, String tabCode, String tabName) throws Exception {
		try {
			// 参数信息
			HashMap<String,String> map = new HashMap<String,String>() ;
			map.put("hrcyId", targetHrcyId) ;
			map.put("tabCode", tabCode) ;
			map.put("tabName", tabName) ;
		
			// 判断模板是否存在
			int tabCount = 0 ;
			try {
				tabCount = eSTabMapper.getSheetCountByCodeOrName(map) ;
			} catch (Exception e) {
				tabCount = 0 ;
			}
			
			// 报表模板编码或名称已经存在
			if(tabCount > 0){
				throw new Exception("模板编码【"+tabCode+"】,或名称【"+tabName+"】在目标科目体系下已经存在!") ;
			}
			
		} catch (Exception e) {
			throw e ;
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: copyTemplate</p> 
	 * <p>Description: 复制单个模板的格式信息 </p> 
	 * @param srcHrcyId
	 * @param targetHrcyId
	 * @param tabId
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.EnterpriseSheetDAO#copyTemplate(java.lang.String, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void copyTemplate(String srcHrcyId, String targetHrcyId, String tabId) throws Exception {
		try {
			// 当前时间和当前登录人
			String userId = CurrentSessionVar.getUserId() ;
			Date cdate = CurrentUserUtil.getCurrentDate() ;
			
			// 计算页签序号 
			int tabOrder = this.getNextTabOrder(targetHrcyId) ;
			int isActive = 0 ;
			if(tabOrder == 0){
				isActive = 1 ;
			}
			
			// 查询模板基础信息
			ESTabBean tabBean = eSTabMapper.getESTabIncFormat(tabId) ;
			
			String targetTabId = UUID.randomUUID().toString() ;
			
			tabBean.setAccHrcyId(targetHrcyId);
			tabBean.setTabId(targetTabId);
			tabBean.setIsActive(isActive);
			tabBean.setCreatedBy(userId);
			tabBean.setCreationDate(cdate);
			tabBean.setLastUpdateDate(cdate);
			tabBean.setLastUpdatedBy(userId);
			
			// 复制模板基础信息	
			eSTabMapper.copyTab(tabBean);
			
			// 存储行指标ID对应关系
			HashMap<String,String> rowitemMap = new HashMap<String,String>() ;
			
			// 复制行指标及引用关系
			ESTabBean rowAndRefBean = eSRowItemMapper.getRowItemsAndRefByTab(tabId) ;
			if(rowAndRefBean != null){
				List<ESRowItemBean> rowitems = rowAndRefBean.getRowItems() ;
				List<ESRowItemRefBean> rowitemsRef = rowAndRefBean.getRowItemsRef() ;
				if(rowitems != null && rowitems.size() >0 && rowitemsRef != null && rowitemsRef.size()>0){
					// 行指标处理SQL
					StringBuffer rowStr = new StringBuffer() ;
					rowStr.append("INSERT INTO xc_rep_rowitems (") 
						.append(" rowitem_id, acc_hrcy_id, rowitem_code, rowitem_name, rowitem_desc, rowitem_alias, upcode, ") 
						.append(" creation_date, created_by, last_update_date, last_updated_by ") 
						.append(") VALUES (") 
						.append(" :rowitemId, :accHrcyId, :rowitemCode, :rowitemName, :rowitemDesc, :rowitemAlias, :upcode, ") 
						.append(" :creationDate, :createdBy, :lastUpdateDate, :lastUpdatedBy ")
						.append(")") ;
					String rowSql = rowStr.toString() ;
					
					// 行指标引用处理SQL
					StringBuffer refStr = new StringBuffer() ;
					refStr.append("INSERT INTO xc_rep_rm_ref (")
						.append(" rm_ref_id, tab_id, rowitem_id, rowno, lanno, x, y, enabled,  ")
						.append(" creation_date, created_by, last_update_date, last_updated_by ")
						.append(") VALUES (")
						.append(" :rmRefId, :tabId, :rowitemId, :rowno, :lanno, :x, :y, :enabled, ")
						.append(" :creationDate, :createdBy, :lastUpdateDate, :lastUpdatedBy ")
						.append(")") ;
					String rowRefSql = refStr.toString() ;
					
					// 行指标保存参数
					SqlParameterSource[] rowitemArgs = new SqlParameterSource[rowitems.size()];
					int i = 0 ;
					for(ESRowItemBean bean : rowitems){
						// 记录ID对应关系
						String oldItemId = bean.getRowitemId() ;
						String itemId = UUID.randomUUID().toString() ;
						rowitemMap.put(oldItemId, itemId) ;
						
						bean.setRowitemId(itemId);
						bean.setAccHrcyId(targetHrcyId);
						bean.setCreatedBy(userId);
						bean.setCreationDate(cdate);
						bean.setLastUpdateDate(cdate);
						bean.setLastUpdatedBy(userId);
						
						rowitemArgs[i] = new BeanPropertySqlParameterSource(bean);
						i++ ;
					}
					
					// 行指标引用保存参数
					SqlParameterSource[] rowRefArgs = new SqlParameterSource[rowitemsRef.size()];
					int j=0;
					for(ESRowItemRefBean bean : rowitemsRef) {
						bean.setRmRefId(UUID.randomUUID().toString());
						bean.setTabId(targetTabId);
						bean.setRowitemId(rowitemMap.get(bean.getRowitemId()));
						bean.setCreatedBy(userId);
						bean.setCreationDate(cdate);
						bean.setLastUpdateDate(cdate);
						bean.setLastUpdatedBy(userId);
						
						rowRefArgs[j] = new BeanPropertySqlParameterSource(bean);
						j++ ;
					}
					
					namedParameterJdbcTemplate.batchUpdate(rowSql, rowitemArgs) ;
					namedParameterJdbcTemplate.batchUpdate(rowRefSql, rowRefArgs) ;
				}
			}
			
			// 存储指标ID对应关系
			HashMap<String,String> colitemMap = new HashMap<String,String>() ;
			
			// 复制列指标及引用关系信息
			ESTabBean colAndRefBean = eSColItemMapper.getColItemsAndRefByTab(tabId) ;
			if(colAndRefBean != null){
				List<ESColItemBean> colitems = colAndRefBean.getColitems() ;
				List<ESColItemRefBean> colitemsRef = colAndRefBean.getColItemsRef() ;
				if(colitems != null && colitems.size() >0 && colitemsRef != null && colitemsRef.size()>0){
					// 列指标处理SQL
					StringBuffer colStr = new StringBuffer() ;
					colStr.append("INSERT INTO xc_rep_colitems (")
						.append(" colitem_id, acc_hrcy_id, colitem_code, colitem_name, colitem_desc, colitem_alias, upcode, datatype, orderby,")
						.append(" creation_date, created_by, last_update_date, last_updated_by ")
						.append(") VALUES (")
						.append(" :colitemId, :accHrcyId, :colitemCode, :colitemName, :colitemDesc, :colitemAlias, :upcode, :datatype, :orderby,")
						.append(" :creationDate, :createdBy, :lastUpdateDate, :lastUpdatedBy ")
						.append(")") ;
					String colSql = colStr.toString() ;
					
					// 列指标引用处理SQL
					StringBuffer refStr = new StringBuffer() ;
					refStr.append("INSERT INTO xc_rep_cm_ref ( ")
						.append(" col_ref_id, tab_id, colitem_id, colno, lanno, enabled, ")
						.append(" creation_date, created_by, last_update_date, last_updated_by ")
						.append(") VALUES (")
						.append(" :colRefId, :tabId, :colitemId, :colno, :lanno, :enabled,")
						.append(" :creationDate, :createdBy, :lastUpdateDate, :lastUpdatedBy ")
						.append(")") ;
					String colRefSql = refStr.toString() ;

					// 列指标保存参数
					SqlParameterSource[] colitemArgs = new SqlParameterSource[colitems.size()];
					int i=0 ;
					for(ESColItemBean bean : colitems) {
						String oldItemId = bean.getColitemId() ;
						String itemId = UUID.randomUUID().toString() ;
						colitemMap.put(oldItemId, itemId) ;
						
						bean.setColitemId(itemId);
						bean.setAccHrcyId(targetHrcyId);

						bean.setCreatedBy(userId);
						bean.setCreationDate(cdate);
						bean.setLastUpdateDate(cdate);
						bean.setLastUpdatedBy(userId);
						
						colitemArgs[i] = new BeanPropertySqlParameterSource(bean) ;
						i++ ;
					}
					
					// 列指标引用保存参数
					SqlParameterSource[] colRefArgs = new SqlParameterSource[colitemsRef.size()];
					int j=0 ;
					for(ESColItemRefBean bean : colitemsRef) {
						bean.setColRefId(UUID.randomUUID().toString());
						bean.setTabId(targetTabId);
						bean.setColitemId(colitemMap.get(bean.getColitemId()));
						bean.setCreatedBy(userId);
						bean.setCreationDate(cdate);
						bean.setLastUpdateDate(cdate);
						bean.setLastUpdatedBy(userId);
						
						colRefArgs[j] = new BeanPropertySqlParameterSource(bean) ;
						j++ ;
					}
					
					namedParameterJdbcTemplate.batchUpdate(colSql, colitemArgs) ;
					namedParameterJdbcTemplate.batchUpdate(colRefSql, colRefArgs) ;
				}
			}
			
			// 复制模板单元格信息
			List<ESTabCellBean> cells = tabBean.getCells() ;
			if(cells != null && cells.size() >0){
				// SQL语句
				StringBuffer cellStr = new StringBuffer() ;
				cellStr.append("insert into xc_rep_tab_cell ( ")
					.append(" cell_id, tab_id, x, y, content, is_cal, raw_data, cell_type, comment, ")
					.append(" creation_date, created_by, last_update_date, last_updated_by ")
					.append(") values (")
					.append(" :cellId, :tabId, :x, :y, :content, :isCal, :rawData, :cellType, :comment, ")
					.append(" :creationDate, :createdBy, :lastUpdateDate, :lastUpdatedBy ")
					.append(")");
				String cellSql = cellStr.toString() ;

				// 参数
				SqlParameterSource[] cellArgs = new SqlParameterSource[cells.size()];
				int i = 0;
				for (ESTabCellBean bean : cells) {
					// 单元格配置信息
					String json = bean.getContent() ;
					Map<String,Object> content = JsonUtil.fromJson(json, Map.class) ;
				
					// 处理单元格批注信息
					if(content != null && content.containsKey("comment")){
						// 单元格批注
						Object cfgComment = content.get("comment") ;
						if(cfgComment != null && !"".equals(cfgComment)) {
							// 单元格类型：fixed-固定格式批注、row-行指标单元批注、col-列指标单元格批注、data-数据区单元格批注
							String cellType = bean.getCellType() ;
							
							if("data".equals(cellType)) { 
								// 处理数据单元格所关联的指标信息
								String comment = bean.getComment() ;
								String[] a = comment.split("/") ;
								String rowitemId = a[0] ;
								String colitemId = a[1] ;
								String _rowitemId = rowitemMap.get(rowitemId) ;
								String _colitemId = colitemMap.get(colitemId) ;
								String newComment = _rowitemId.concat("/").concat(_colitemId) ;
								
								bean.setComment(newComment);

								// 单元格配置信息
								String configComment = String.valueOf(content.get("comment")) ;
								configComment = configComment.replace(comment, newComment) ;
								content.put("comment", configComment) ;
								
								String _vname = String.valueOf(content.get("vname")) ;
								_vname = _vname.replace(comment, newComment) ;
								content.put("vname", _vname) ;

								bean.setContent(JsonUtil.toJson(content));
								
							}else if("row".equals(cellType)){	
								// 处理行指标单元格所关联的指标信息
								String comment = bean.getComment() ;
								String[] a = comment.split("/") ;
								String rowitemId = a[1] ;
								String _rowitemId = rowitemMap.get(rowitemId) ;
								String newComment = comment.replace(rowitemId, _rowitemId) ;
								
								bean.setComment(newComment);
								
								// 单元格配置信息
								String configComment = String.valueOf(content.get("comment")) ;
								configComment = configComment.replace(rowitemId, _rowitemId) ;
								content.put("comment", configComment) ;
								
								bean.setContent(JsonUtil.toJson(content));
								
							}else if("col".equals(cellType)){	
								// 处理列指标单元格所关联的指标信息
								String comment = bean.getComment() ;
								String[] a = comment.split("/") ;
								String colitemId = a[1] ;
								String _colitemId = colitemMap.get(colitemId) ;
								String newComment = comment.replace(colitemId, _colitemId) ;
								
								bean.setComment(newComment);
								
								// 单元格配置信息
								String configComment = String.valueOf(content.get("comment")) ;
								configComment = configComment.replace(colitemId, _colitemId) ;
								content.put("comment", configComment) ;
								
								bean.setContent(JsonUtil.toJson(content));
								
							}else{ 
								// 扩展使用
							}
						}
					}
					
					bean.setCellId(UUID.randomUUID().toString());
					bean.setTabId(targetTabId);
					bean.setCreatedBy(userId);
					bean.setCreationDate(cdate);
					bean.setLastUpdateDate(cdate);
					bean.setLastUpdatedBy(userId);
					
					cellArgs[i] = new BeanPropertySqlParameterSource(bean);
					i++ ;
				}
				
				//保存
				namedParameterJdbcTemplate.batchUpdate(cellSql, cellArgs) ;
			}
			
			// 复制模板元素信息
			List<ESTabElementBean> elements = tabBean.getElements() ;
			if(elements != null && elements.size()>0){
				// sql语句
				StringBuffer elementStr = new StringBuffer() ;
				elementStr.append("insert into xc_rep_tab_element (")
					.append(" element_id, tab_id, name, etype, content, ")
					.append(" creation_date, created_by, last_update_date, last_updated_by ")
					.append(") values (")
					.append(" :elementId, :tabId, :name, :etype, :content, ")
					.append(" :creationDate, :createdBy, :lastUpdateDate, :lastUpdatedBy ")
					.append(")");
				String elementSql = elementStr.toString() ;
				
				// 参数信息
				SqlParameterSource[] elementArgs = new SqlParameterSource[elements.size()];
				int i = 0;
				for (ESTabElementBean bean : elements) {
					bean.setElementId(UUID.randomUUID().toString());
					bean.setTabId(targetTabId);
					bean.setCreatedBy(userId);
					bean.setCreationDate(cdate);
					bean.setLastUpdateDate(cdate);
					bean.setLastUpdatedBy(userId);
					
					elementArgs[i] = new BeanPropertySqlParameterSource(bean);
					i++ ;
				}
				// 批量保存
				namedParameterJdbcTemplate.batchUpdate(elementSql, elementArgs) ;
			}
			
		} catch (Exception e) {
			throw e ;
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: expTemplate</p> 
	 * <p>Description: 按模板ID查询模板及行列指标引用关系 </p> 
	 * @param tabId
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.EnterpriseSheetDAO#expTemplate(java.lang.String)
	 */
	@Override
	public List<ESTabBean> getESTabBeanByTabs(String accHrcyId, List<String> tabs) throws Exception {
		List<ESTabBean> tabBeans = new ArrayList<ESTabBean>() ;
		
		try {
			// 模板格式信息
			tabBeans = eSTabMapper.getESTabIncFormatByTabs(tabs) ;
			
			if(tabBeans != null && tabBeans.size() >0){
				// 行指标引用关系
				List<ESTabBean> rowRefTabBeans = eSRowItemMapper.getRowItemsAndRefByTabs(tabs) ;
				// 列指标引用关系
				List<ESTabBean> colRefTabBeans = eSColItemMapper.getColItemsAndRefByTabs(tabs) ;
				
				// 处理返回参数
				for(ESTabBean tabBean : tabBeans){
					String tabId = tabBean.getTabId() ;
					
					if(rowRefTabBeans != null && rowRefTabBeans.size() >0){
						for(ESTabBean bean : rowRefTabBeans){
							if(tabId.equals(bean.getTabId())){
								tabBean.setRowItems(bean.getRowItems());
								tabBean.setRowItemsRef(bean.getRowItemsRef());
								rowRefTabBeans.remove(bean) ;
								break ;
							}
						}
					}
					if(colRefTabBeans != null && colRefTabBeans.size() >0){
						for(ESTabBean bean : colRefTabBeans){
							if(tabId.equals(bean.getTabId())){
								tabBean.setColitems(bean.getColitems());
								tabBean.setColItemsRef(bean.getColItemsRef());
								colRefTabBeans.remove(bean) ;
								break ;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			throw e ;
		}
		
		return tabBeans ;
	}
	
}
