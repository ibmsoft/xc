package com.xzsoft.xc.rep.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.xzsoft.xc.gl.modal.AccHrcy;
import com.xzsoft.xc.rep.dao.EnterpriseSheetDAO;
import com.xzsoft.xc.rep.dao.TabCommonDAO;
import com.xzsoft.xc.rep.dao.TabImpAndExpDAO;
import com.xzsoft.xc.rep.dao.XCRepCommonDAO;
import com.xzsoft.xc.rep.modal.CalFormulaBean;
import com.xzsoft.xc.rep.modal.ESColItemBean;
import com.xzsoft.xc.rep.modal.ESColItemRefBean;
import com.xzsoft.xc.rep.modal.ESRowItemBean;
import com.xzsoft.xc.rep.modal.ESRowItemRefBean;
import com.xzsoft.xc.rep.modal.ESTabBean;
import com.xzsoft.xc.rep.modal.ESTabCellBean;
import com.xzsoft.xc.rep.modal.ESTabElementBean;
import com.xzsoft.xc.rep.modal.FuncBean;
import com.xzsoft.xc.rep.modal.FuncParamsBean;
import com.xzsoft.xc.rep.modal.PreFormulaBean;
import com.xzsoft.xc.util.common.ClassUtils;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;
import com.xzsoft.xsr.core.util.JsonUtil;


/**
 * @ClassName: TabImportDAOImpl 
 * @Description: 模板导出与接收
 * @author linp
 * @date 2016年10月19日 下午4:07:53 
 *
 */
@Repository("tabImpAndExpDAO") 
public class TabImportDAOImpl implements TabImpAndExpDAO, TabCommonDAO {
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate ;
	@Autowired
	private XCRepCommonDAO xcRepCommonDAO ;
	@Autowired
	private EnterpriseSheetDAO esDAO ;
	
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: saveOrUpdateFunctions</p> 
	 * <p>Description: 保存或更新自定义函数信息 </p> 
	 * @param functions
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.TabImpAndExpDAO#saveOrUpdateFunctions(java.util.List)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void saveOrUpdateFunctions(List<Object> functions) throws Exception {
		try {
			if(functions != null && functions.size() >0){
				// 当前用户和当前日期
				String userId = CurrentSessionVar.getUserId() ;
				Date cdate = CurrentUserUtil.getCurrentDate() ;
				
				// 新增和更新列表
				List<FuncBean> addList = new ArrayList<FuncBean>() ;
				List<FuncBean> updateList = new ArrayList<FuncBean>() ;
				
				// 查询系统内现存的所有自定义函数
				Map<String,FuncBean> funcMap = xcRepCommonDAO.getAllFunctions() ;
				
				for(Object bean : functions){
					Map<String,Object> impFuncMap = (Map<String, Object>) bean ;
					
					// 自定义函数编码
					String funCode = String.valueOf(impFuncMap.get("funCode")) ;
					if(funCode == null || "".equals(funCode)) continue ;
					
					// 自定义函数参数
					List<FuncParamsBean> paramList = new ArrayList<FuncParamsBean>() ;
					List<Object> params = (List<Object>) impFuncMap.get("params") ;
					if(params != null && params.size() >0){
						for(Object param : params){
							Map<String,Object> funcParamMap = (Map<String, Object>) param ;
							
							FuncParamsBean paramBean = new FuncParamsBean() ;
							ClassUtils.copyMapProperties(funcParamMap, paramBean);
							
							paramBean.setParamId(UUID.randomUUID().toString());
							
							paramBean.setCreatedBy(userId);
							paramBean.setCreationDate(cdate);
							paramBean.setLastUpdateDate(cdate);
							paramBean.setLastUpdatedBy(userId);
							
							paramList.add(paramBean) ;
						}
					}
					
					// 根据函数编码判断当前函数是否在系统已存在
					FuncBean existsFuncBean = funcMap.get(funCode) ;
					
					if(existsFuncBean != null){ 
						// 更新列表
						ClassUtils.copyMapProperties(impFuncMap, existsFuncBean);
						existsFuncBean.setLastUpdatedBy(userId);
						existsFuncBean.setLastUpdateDate(cdate);
						
						existsFuncBean.setParams(paramList);
						
						updateList.add(existsFuncBean) ;
						
					}else{
						// 新增列表
						FuncBean funcBean = new FuncBean() ;
						ClassUtils.copyMapProperties(impFuncMap, existsFuncBean);
						
						funcBean.setFunId(UUID.randomUUID().toString());
						
						funcBean.setCreatedBy(userId);
						funcBean.setCreationDate(cdate);
						funcBean.setLastUpdateDate(cdate);
						funcBean.setLastUpdatedBy(userId);
						
						funcBean.setParams(paramList);
						
						addList.add(funcBean) ;
					}
				}
				
				// 新增函数
				if(addList != null && addList.size() >0){
					this.addFunctions(addList);
				}
				
				// 更新函数
				if(updateList != null && updateList.size() >0){
					this.updateFunctions(updateList);
				}
			}
			
		} catch (Exception e) {
			throw e ;
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: saveOrUpdateRowitems</p> 
	 * <p>Description: 保存或更新行指标信息 </p> 
	 * @param rowitems
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.TabImpAndExpDAO#saveOrUpdateRowitems(java.util.List)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void saveOrUpdateRowitems(List<Object> rowitems,AccHrcy accHrcy) throws Exception {
		try {
			if(rowitems != null && rowitems.size() >0){
				// 当前用户和当前日期
				String userId = CurrentSessionVar.getUserId() ;
				Date cdate = CurrentUserUtil.getCurrentDate() ;
				
				// 科目体系下指标信息
				Map<String,ESRowItemBean> oldRowMap = xcRepCommonDAO.getAllRowitems4Map(accHrcy.getAccHrcyId(),"query") ;
				
				// 存储新增和更新的行指标信息
				List<ESRowItemBean> addList = new ArrayList<ESRowItemBean>() ;
				List<ESRowItemBean> updList = new ArrayList<ESRowItemBean>() ;
				
				for(Object bean : rowitems){
					Map<String,Object> rowMap = (Map<String, Object>) bean ;
					
					Object rowCode = rowMap.get("rowitemCode") ;
					if(rowCode == null || "".equals(rowCode)) continue ;
					
					String rowitemCode = String.valueOf(rowCode);
					
					ESRowItemBean oldBean = oldRowMap.get(rowitemCode) ;
					
					if(oldBean != null){
						// 更新列表
						ClassUtils.copyMapProperties(rowMap, oldBean);
						
						oldBean.setLastUpdateDate(cdate);
						oldBean.setLastUpdatedBy(userId);
						
						updList.add(oldBean) ;
						
					}else{
						// 新增列表
						ESRowItemBean newBean = new ESRowItemBean() ;
						
						ClassUtils.copyMapProperties(rowMap, newBean);
						
						newBean.setRowitemId(UUID.randomUUID().toString());
						newBean.setAccHrcyId(accHrcy.getAccHrcyId());
						
						newBean.setCreatedBy(userId);
						newBean.setCreationDate(cdate);
						newBean.setLastUpdateDate(cdate);
						newBean.setLastUpdatedBy(userId);
						
						addList.add(newBean) ;
					}
				}
				
				// 执行行指标的新增和更新处理
				if(!addList.isEmpty()) this.addOrUpdateRowitems(addList, "add", accHrcy);
				if(!updList.isEmpty()) this.addOrUpdateRowitems(updList, "update", accHrcy);
			}
		} catch (Exception e) {
			throw e ;
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: saveOrUpdateColitems</p> 
	 * <p>Description: 保存或更新列指标信息 </p> 
	 * @param colitems
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.TabImpAndExpDAO#saveOrUpdateColitems(java.util.List)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void saveOrUpdateColitems(List<Object> colitems,AccHrcy accHrcy) throws Exception {
		try {
			if(colitems != null && colitems.size() >0){
				// 当前用户和当前日期
				String userId = CurrentSessionVar.getUserId() ;
				Date cdate = CurrentUserUtil.getCurrentDate() ;
				
				// 科目体系下指标信息
				Map<String,ESColItemBean> oldColMap = xcRepCommonDAO.getAllColItems4Map(accHrcy.getAccHrcyId(),"query") ;
				
				// 存储新增和更新的列指标信息
				List<ESColItemBean> addList = new ArrayList<ESColItemBean>() ;
				List<ESColItemBean> updList = new ArrayList<ESColItemBean>() ;
				
				for(Object bean : colitems){
					Map<String,Object> colMap = (Map<String, Object>) bean ;
					
					Object colCode = colMap.get("colitemCode") ;
					if(colCode == null || "".equals(colCode)) continue ;
					
					String colitemCode = String.valueOf(colCode);
					
					ESColItemBean oldBean = oldColMap.get(colitemCode) ;
					
					if(oldBean != null){
						// 更新列表
						ClassUtils.copyMapProperties(colMap, oldBean);
						
						oldBean.setLastUpdateDate(cdate);
						oldBean.setLastUpdatedBy(userId);
						
						updList.add(oldBean) ;
						
					}else{
						// 新增列表
						ESColItemBean newBean = new ESColItemBean() ;
						
						ClassUtils.copyMapProperties(colMap, newBean);
						
						newBean.setColitemId(UUID.randomUUID().toString());
						newBean.setAccHrcyId(accHrcy.getAccHrcyId());
						
						newBean.setCreatedBy(userId);
						newBean.setCreationDate(cdate);
						newBean.setLastUpdateDate(cdate);
						newBean.setLastUpdatedBy(userId);
						
						addList.add(newBean) ;
					}
				}
				
				// 执行列指标的新增和更新处理
				if(!addList.isEmpty()) this.addOrUpdateColitems(addList, "add", accHrcy);
				if(!updList.isEmpty()) this.addOrUpdateColitems(updList, "update", accHrcy);
			}
			
		} catch (Exception e) {
			throw e ;
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: saveOrUpdateFormulas</p> 
	 * <p>Description: 保存或更新公式信息 </p> 
	 * @param formulas
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.TabImpAndExpDAO#saveOrUpdateFormulas(java.util.List)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void saveOrUpdateFormulas(List<Object> formulas,AccHrcy accHrcy) throws Exception {
		if(formulas != null && formulas.size() >0){
			// 当前用户和当前日期
			String userId = CurrentSessionVar.getUserId() ;
			Date cdate = CurrentUserUtil.getCurrentDate() ;
			
			// 系统内公共级公式信息
			Map<String,CalFormulaBean> oldCommonFormulaMap = xcRepCommonDAO.getCommonFormulas4Map(accHrcy.getAccHrcyId()) ;
			
			// 存储新增和更新的公式信息
			List<CalFormulaBean> addList = new ArrayList<CalFormulaBean>() ;
			List<CalFormulaBean> updList = new ArrayList<CalFormulaBean>() ;
			
			for(Object bean : formulas){
				Map<String,Object> formulaMap = (Map<String, Object>) bean ;
				
				Object rowCode = formulaMap.get("rowItemCode") ;
				Object colCode = formulaMap.get("colItemCode") ;
				if(rowCode== null || colCode == null) continue ;
				
				// 公式预处理信息
				List<PreFormulaBean> preFormulaBeans = new ArrayList<PreFormulaBean>() ;
				List<Object> preFormulas = (List<Object>) formulaMap.get("preFormulaBeans") ;
				if(preFormulas != null && preFormulas.size() >0){
					for(Object preFormula : preFormulas){
						Map<String,Object> preFormulaMap = (Map<String, Object>) preFormula ;
						
						PreFormulaBean preFormulaBean = new PreFormulaBean() ;
						ClassUtils.copyMapProperties(preFormulaMap, preFormulaBean);
						
						preFormulaBean.setPreFormulaId(UUID.randomUUID().toString());
						preFormulaBean.setCreatedBy(userId);
						preFormulaBean.setCreationDate(cdate);
						preFormulaBean.setLastUpdateDate(cdate);
						preFormulaBean.setLastUpdatedBy(userId);
						
						preFormulaBeans.add(preFormulaBean) ;
					}
				}
				
				// 公式信息
				String fkey = String.valueOf(rowCode).concat("/").concat(String.valueOf(colCode)) ;
				
				CalFormulaBean oldFormulaBean = oldCommonFormulaMap.get(fkey) ;
				if(oldFormulaBean != null){
					ClassUtils.copyMapProperties(formulaMap, oldFormulaBean);
					
					oldFormulaBean.setLastUpdateDate(cdate);
					oldFormulaBean.setLastUpdatedBy(userId);
					
					oldFormulaBean.setPreFormulaBeans(preFormulaBeans);
					
					updList.add(oldFormulaBean) ;
					
				}else{
					CalFormulaBean calFormulaBean = new CalFormulaBean() ;
					
					ClassUtils.copyMapProperties(formulaMap, calFormulaBean);
					
					calFormulaBean.setFormulaId(UUID.randomUUID().toString());
					calFormulaBean.setAccHrcyId(accHrcy.getAccHrcyId());
					
					// 行指标ID
					String rowitemId = this.getItemIdByCode(accHrcy.getAccHrcyId(), calFormulaBean.getRowItemCode(), "row") ;
					calFormulaBean.setRowItemId(rowitemId);
					// 列指标ID
					String colitemId = this.getItemIdByCode(accHrcy.getAccHrcyId(), calFormulaBean.getColItemCode(), "col") ;
					calFormulaBean.setColItemId(colitemId);
					// 公共级
					calFormulaBean.setLedgerId("-1");
					
					calFormulaBean.setPreFormulaBeans(preFormulaBeans);
					
					calFormulaBean.setCreatedBy(userId);
					calFormulaBean.setCreationDate(cdate);
					calFormulaBean.setLastUpdateDate(cdate);
					calFormulaBean.setLastUpdatedBy(userId);
					
					addList.add(calFormulaBean) ;
				}
			}
			
			// 执行新增或更新处理
			if(!addList.isEmpty()) this.addFormulas(addList, accHrcy);
			if(!updList.isEmpty()) this.updateFormulas(updList, accHrcy);
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: saveOrUpdateTabs</p> 
	 * <p>Description: 保存或更新模板信息 </p> 
	 * @param tabs
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.TabImpAndExpDAO#saveOrUpdateTabs(java.util.List)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void saveOrUpdateTabs(List<Object> tabs,AccHrcy accHrcy) throws Exception {
		if(tabs != null && tabs.size() >0){
			// 当前用户和当前日期
			String userId = CurrentSessionVar.getUserId() ;
			Date cdate = CurrentUserUtil.getCurrentDate() ;
			
			// 存在的模板
			Map<String,ESTabBean> oldTabsMap = esDAO.getAllESTabBeans4Map(accHrcy.getAccHrcyId()) ;
			
			// 模板页签序号
			int maxTabOrder = esDAO.getNextTabOrder(accHrcy.getAccHrcyId()) ;
			
			// 新增和修改的列表信息
			List<ESTabBean> addList = new ArrayList<ESTabBean>() ;
			List<ESTabBean> updList = new ArrayList<ESTabBean>() ;
			
			int i = 0 ;
			for(Object bean : tabs) {
				Map<String,Object> tabMap = (Map<String, Object>) bean ;
				
				Object tabCode = tabMap.get("tabCode") ;
				if(tabCode == null || "".equals(tabCode)) continue ;
				
				// 处理cells
				List<ESTabCellBean> cellList = new ArrayList<ESTabCellBean>() ;
				List<Object> cells = (List<Object>) tabMap.get("cells") ;
				if(cells != null && cells.size() >0){
					for(Object cell : cells){
						Map<String,Object> cellMap = (Map<String, Object>) cell ;
						
						// 单元格信息
						ESTabCellBean tabCellBean = new ESTabCellBean() ;
						ClassUtils.copyMapProperties(cellMap, tabCellBean);
						
						// 单元格类型
						Object cellType = cellMap.get("cellType") ;
						String _cellType = cellType==null ? "" : String.valueOf(cellType) ;
						
						Map<String,Object> content = JsonUtil.fromJson(String.valueOf(cellMap.get("content")), Map.class) ;
						
						// 行列指标ID
						String rowitemId = null ;
						String colitemId = null ;
						
						switch(_cellType){
						case "row" :
							String rowComment = String.valueOf(cellMap.get("comment")) ;
							String rowCode = rowComment.substring(0, rowComment.length()-1) ;
							// 计算行指标ID
							rowitemId = this.getItemIdByCode(accHrcy.getAccHrcyId(), rowCode, "row") ;
							
							tabCellBean.setComment(rowComment.concat(rowitemId));
							
							content.put("comment", rowComment.concat(rowitemId)) ;
							break ;
							
						case "col" :
							String colComment = String.valueOf(cellMap.get("comment")) ;
							String colCode = colComment.substring(0, colComment.length()-1) ;
							// 计算列指标ID
							colitemId = this.getItemIdByCode(accHrcy.getAccHrcyId(), colCode, "col") ;
							
							tabCellBean.setComment(colComment.concat(colitemId));
							
							content.put("comment", colComment.concat(colitemId)) ;
							break ;
							
						case "data" :
							String dataComment = String.valueOf(cellMap.get("comment")) ;
							String[] arr = dataComment.split("/") ;
							String _rowCode = arr[0] ;
							String _colCode = arr[1] ;
							// 计算行列指标ID
							rowitemId = this.getItemIdByCode(accHrcy.getAccHrcyId(), _rowCode, "row") ;
							colitemId = this.getItemIdByCode(accHrcy.getAccHrcyId(), _colCode, "col") ;
							
							String _comment = rowitemId.concat("/").concat(colitemId) ;
							tabCellBean.setComment(_comment);
							
							content.put("comment", "data>>".concat(_comment)) ;
							content.put("vname", _comment) ;
							break ;
							
						default :
							break ;
						}
						
						tabCellBean.setContent(JsonUtil.toJson(content));
						
						tabCellBean.setCellId(UUID.randomUUID().toString());
						
						tabCellBean.setCreatedBy(userId);
						tabCellBean.setCreationDate(cdate);
						tabCellBean.setLastUpdateDate(cdate);
						tabCellBean.setLastUpdatedBy(userId);
						
						cellList.add(tabCellBean) ;
					}
				}
				
				// 处理elemets
				List<ESTabElementBean> elementList = new ArrayList<ESTabElementBean>() ;
				List<Object> elements = (List<Object>) tabMap.get("elements") ;
				if(elements != null && elements.size() >0){
					for(Object element : elements){
						Map<String,Object> elementMap = (Map<String, Object>) element ;
						
						// 单元格信息
						ESTabElementBean tabElementBean = new ESTabElementBean() ;
						
						ClassUtils.copyMapProperties(elementMap, tabElementBean);

						tabElementBean.setElementId(UUID.randomUUID().toString());;
						
						tabElementBean.setCreatedBy(userId);
						tabElementBean.setCreationDate(cdate);
						tabElementBean.setLastUpdateDate(cdate);
						tabElementBean.setLastUpdatedBy(userId);
						
						elementList.add(tabElementBean) ;
					}
				}
				
				// 处理rowItemsRef
				List<ESRowItemRefBean> rowRefList = new ArrayList<ESRowItemRefBean>() ;
				List<Object> rowItemsRef = (List<Object>) tabMap.get("rowItemsRef") ;
				if(rowItemsRef != null && rowItemsRef.size() >0){
					for(Object rowRef : rowItemsRef){
						Map<String,Object> rowRefMap = (Map<String, Object>) rowRef ;
						// 行指标编码
						Object rowitemCode = rowRefMap.get("rowitemCode") ;
						if(rowitemCode == null || "".equals(rowitemCode)) continue ;
						// 行指标ID
						String rowitemId = this.getItemIdByCode(accHrcy.getAccHrcyId(), String.valueOf(rowitemCode), "row") ;
						
						ESRowItemRefBean rowRefBean = new ESRowItemRefBean() ;
						
						ClassUtils.copyMapProperties(rowRefMap, rowRefBean);
						
						rowRefBean.setRmRefId(UUID.randomUUID().toString());
						rowRefBean.setRowitemId(rowitemId);
						
						rowRefBean.setCreatedBy(userId);
						rowRefBean.setCreationDate(cdate);
						rowRefBean.setLastUpdateDate(cdate);
						rowRefBean.setLastUpdatedBy(userId);
						
						rowRefList.add(rowRefBean) ;
					}
				}
				
				// 处理colItemsRef
				List<ESColItemRefBean> colRefList = new ArrayList<ESColItemRefBean>() ;
				List<Object> colItemsRef = (List<Object>) tabMap.get("colItemsRef") ;
				if(colItemsRef != null && colItemsRef.size() >0){
					for(Object colRef : colItemsRef){
						Map<String,Object> colRefMap = (Map<String, Object>) colRef ;
						// 列指标编码
						Object colitemCode = colRefMap.get("colitemCode") ;
						if(colitemCode == null || "".equals(colitemCode)) continue ;
						// 列指标ID
						String colitemId = this.getItemIdByCode(accHrcy.getAccHrcyId(), String.valueOf(colitemCode), "col") ;
						
						ESColItemRefBean colRefBean = new ESColItemRefBean() ;
						
						ClassUtils.copyMapProperties(colRefMap, colRefBean);
						
						colRefBean.setColRefId(UUID.randomUUID().toString());
						colRefBean.setColitemId(colitemId);
						
						colRefBean.setCreatedBy(userId);
						colRefBean.setCreationDate(cdate);
						colRefBean.setLastUpdateDate(cdate);
						colRefBean.setLastUpdatedBy(userId);
						
						colRefList.add(colRefBean) ;
					}
				}
				
				// 原模板
				ESTabBean oldTabBean = oldTabsMap.get(String.valueOf(tabCode)) ;
				
				if(oldTabBean != null){
					ClassUtils.copyMapProperties(tabMap, oldTabBean);
					
					oldTabBean.setLastUpdateDate(cdate);
					oldTabBean.setLastUpdatedBy(userId);
					
					oldTabBean.setCells(cellList);
					oldTabBean.setElements(elementList);
					oldTabBean.setRowItemsRef(rowRefList);
					oldTabBean.setColItemsRef(colRefList);
					
					updList.add(oldTabBean) ;
					
				}else{
					ESTabBean tabBean = new ESTabBean() ;
					
					ClassUtils.copyMapProperties(tabMap, tabBean);
					
					tabBean.setTabId(UUID.randomUUID().toString());
					
					tabBean.setTabOrder(maxTabOrder+i);
					tabBean.setCells(cellList);
					tabBean.setElements(elementList);
					tabBean.setRowItemsRef(rowRefList);
					tabBean.setColItemsRef(colRefList);
					
					addList.add(tabBean) ;
				}
				i++ ;
			}
			// 执行新增和更新处理
			if(!addList.isEmpty()) this.addTabs(addList, accHrcy);
			if(!updList.isEmpty()) this.updateTabs(updList);
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: addFunctions</p> 
	 * <p>Description: 新增自定义函数基础信息 </p> 
	 * @param functionList
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.TabCommonDAO#addFunctions(java.util.List)
	 */
	@Override
	public void addFunctions(List<FuncBean> functionList) throws Exception {
		if(functionList != null && functionList.size() >0){
			// 新增自定义函数
			StringBuffer str = new StringBuffer() ;
			str.append("INSERT INTO xc_rep_func ( ") 
				.append(" func_id, func_type, func_code, func_name, func, func_desc, func_caltype, enabled, httplink, ")
				.append(" creation_date, created_by, last_update_date, last_updated_by ")
				.append(") VALUES ( ")
				.append(" :funId, :funType, :funCode, :funName, :func, :funDesc, :funCalType, :enabeld, :httpLink, ")
				.append(" :creationDate, :createdBy, :lastUpdateDate, :lastUpdatedBy ")
				.append(")")
			;
			String sql = str.toString() ;
			
			SqlParameterSource[] batchArgs = new SqlParameterSource[functionList.size()];
			int i = 0;
			for (FuncBean bean : functionList) {
				batchArgs[i] = new BeanPropertySqlParameterSource(bean);
				i++ ;
			}
			namedParameterJdbcTemplate.batchUpdate(sql, batchArgs) ;
			
			// 新增自定义函数信息
			for(FuncBean bean : functionList){
				this.addFuncParams(bean.getParams(),  bean.getFunId());
			}
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: updateFunctions</p> 
	 * <p>Description: 修改自定义函数基础信息 </p> 
	 * @param functionList
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.TabCommonDAO#updateFunctions(java.util.List)
	 */
	@Override
	public void updateFunctions(List<FuncBean> functionList) throws Exception {
		if(functionList != null && functionList.size() >0){
			// 更新自定义函数
			StringBuffer str = new StringBuffer() ;
			str.append("UPDATE xc_rep_func SET ")
				.append(" func_type = :funType, func_code = :funCode, func_name = :funName, ")
				.append(" func = :func, func_desc = :funDesc, func_caltype = :funCalType, enabled = :enabeld, httplink = :httpLink, ")
				.append(" last_update_date = :lastUpdateDate, last_updated_by = :lastUpdatedBy ")
				.append("WHERE func_id = :funId ") ;
			String sql = str.toString() ;
			
			SqlParameterSource[] batchArgs = new SqlParameterSource[functionList.size()];
			int i = 0;
			for (FuncBean bean : functionList) {
				batchArgs[i] = new BeanPropertySqlParameterSource(bean);
				i++ ;
			}
			namedParameterJdbcTemplate.batchUpdate(sql, batchArgs) ;
			
			// 删除自定义函数参数
			this.delFuncParams(functionList);
			
			// 新增自定义函数参数
			for(FuncBean bean : functionList){
				this.addFuncParams(bean.getParams(),  bean.getFunId());
			}
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: delFuncParams</p> 
	 * <p>Description: 删除自定义函数参数信息 </p> 
	 * @param functionList
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.TabCommonDAO#delFuncParams(java.util.List)
	 */
	@Override
	public void delFuncParams(List<FuncBean> functionList) throws Exception {
		if(functionList != null && functionList.size() >0){
			String sql = "delete from xc_rep_func_params where func_id = :funId" ;
			
			SqlParameterSource[] batchArgs = new SqlParameterSource[functionList.size()];
			int i = 0;
			for (FuncBean bean : functionList) {
				batchArgs[i] = new BeanPropertySqlParameterSource(bean);
				i++ ;
			}
			
			namedParameterJdbcTemplate.batchUpdate(sql, batchArgs) ;
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: addFuncParams</p> 
	 * <p>Description: 新增自定义函数参数信息 </p> 
	 * @param paramList
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.TabCommonDAO#addFuncParams(java.util.List)
	 */
	@Override
	public void addFuncParams(List<FuncParamsBean> paramList,String funId) throws Exception {
		if(paramList != null && paramList.size() >0){
			StringBuffer str = new StringBuffer() ;
			str.append("INSERT INTO xc_rep_func_params ( ") 
				.append(" param_id, func_id, param_code, param_name, param_desc, param_order, param_lov, enabled, mandatory, ")
				.append(" creation_date, created_by, last_update_date, last_updated_by ")
				.append(") VALUES ( ")
				.append(" :paramId, :funId, :paramCode, :paramName, :paramDesc, :paramOrder, :paramLov, :enabled, :mandatory,")
				.append(" :creationDate, :createdBy, :lastUpdateDate, :lastUpdatedBy ")
				.append(")")
			;
			String sql = str.toString() ;
			
			SqlParameterSource[] batchArgs = new SqlParameterSource[paramList.size()];
			int i = 0;
			for (FuncParamsBean bean : paramList) {
				bean.setFunId(funId);
				batchArgs[i] = new BeanPropertySqlParameterSource(bean);
				i++ ;
			}
			
			namedParameterJdbcTemplate.batchUpdate(sql, batchArgs) ;
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: addOrUpdateRowitems</p> 
	 * <p>Description: 新增或更新行指标信息  </p> 
	 * @param rowitemList
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.TabCommonDAO#addRowitems(java.util.List)
	 */
	@Override
	public void addOrUpdateRowitems(List<ESRowItemBean> rowitemList, String opType, AccHrcy accHrcy) throws Exception {
		if(rowitemList != null && rowitemList.size() >0){
			// SQL语句
			StringBuffer str = new StringBuffer() ;
			switch(opType){
			case "add":
				str.append("insert into xc_rep_rowitems (")
				.append(" rowitem_id, acc_hrcy_id, rowitem_code, rowitem_name, rowitem_desc, rowitem_alias, upcode,")
				.append(" creation_date, created_by, last_update_date, last_updated_by")
				.append(")values(")
				.append(" :rowitemId, :accHrcyId, :rowitemCode, :rowitemName, :rowitemDesc, :rowitemAlias, :upcode, ")
				.append(" :creationDate, :createdBy, :lastUpdateDate, :lastUpdatedBy")
				.append(")");
				break;
				
			case "update":
				str.append("UPDATE xc_rep_rowitems SET ")
				.append(" rowitem_code = :rowitemCode, rowitem_name = :rowitemName,")
				.append(" rowitem_desc = :rowitemDesc, rowitem_alias = :rowitemAlias, upcode = :upcode, ")
				.append(" last_update_date = :lastUpdateDate, last_updated_by = :lastUpdatedBy ")
				.append("WHERE rowitem_id = :rowitemId ") ;				
				break;
				
			default:
				break ;
			}
			String sql = str.toString() ;
			
			// 参数
			SqlParameterSource[] batchArgs = new SqlParameterSource[rowitemList.size()];
			int i = 0;
			for (ESRowItemBean bean : rowitemList) {
				bean.setAccHrcyId(accHrcy.getAccHrcyId());
				batchArgs[i] = new BeanPropertySqlParameterSource(bean);
				i++ ;
			}
			namedParameterJdbcTemplate.batchUpdate(sql, batchArgs) ;
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: addOrUpdateColitems</p> 
	 * <p>Description: 新增列指标信息 </p> 
	 * @param colitemList
	 * @param opType
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.TabCommonDAO#addOrUpdateColitems(java.util.List, java.lang.String)
	 */
	@Override
	public void addOrUpdateColitems(List<ESColItemBean> colitemList,String opType, AccHrcy accHrcy) throws Exception {
		if(colitemList != null && colitemList.size() >0){
			// SQL语句
			StringBuffer str = new StringBuffer() ;
			switch(opType){
			case "add":
				str.append("insert into xc_rep_colitems (")
				.append(" colitem_id, acc_hrcy_id, colitem_code, colitem_name, colitem_desc, colitem_alias, upcode, datatype, orderby, ")
				.append(" creation_date, created_by, last_update_date, last_updated_by ")
				.append(") values (")
				.append(" :colitemId, :accHrcyId, :colitemCode, :colitemName, :colitemDesc, :colitemAlias, :upcode, :datatype, :orderby,")
				.append(" :creationDate, :createdBy, :lastUpdateDate, :lastUpdatedBy")
				.append(")");
				break;
				
			case "update":
				str.append("UPDATE xc_rep_colitems SET ")
				.append(" colitem_code = :colitemCode, colitem_name = :colitemName, colitem_desc = :colitemDesc, ")
				.append(" colitem_alias = :colitemAlias, upcode = :upcode, datatype = :datatype, orderby = :orderby,")
				.append(" last_update_date = :lastUpdateDate, last_updated_by = :lastUpdatedBy ")
				.append("WHERE colitem_id = :colitemId");				
				break;
				
			default:
				break ;
			}
			String sql = str.toString() ;
			
			// 参数
			SqlParameterSource[] batchArgs = new SqlParameterSource[colitemList.size()];
			int i = 0;
			for (ESColItemBean bean : colitemList) {
				bean.setAccHrcyId(accHrcy.getAccHrcyId());
				batchArgs[i] = new BeanPropertySqlParameterSource(bean);
				i++ ;
			}
			namedParameterJdbcTemplate.batchUpdate(sql, batchArgs) ;
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: addFormulas</p> 
	 * <p>Description: 新增指标公式信息 </p> 
	 * @param formulaList
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.TabCommonDAO#addFormulas(java.util.List)
	 */
	@Override
	public void addFormulas(List<CalFormulaBean> formulaList, AccHrcy accHrcy) throws Exception {
		if(formulaList != null && formulaList.size() >0){
			// SQL语句
			StringBuffer str = new StringBuffer() ;
			str.append("insert into xc_rep_formulas( ")
			.append(" formula_id, acc_hrcy_id, rowitem_id, colitem_id, ledger_id, formula, formula_desc, formula_type, orderby, formula_level,")
			.append(" creation_date, created_by, last_update_date, last_updated_by")
			.append(") values (")
			.append(" :formulaId, :accHrcyId, :rowItemId, :colItemId, :ledgerId, :formula, :formulaDesc, :formulaType, :orderby, :formulaLevel,")
			.append(" :creationDate, :createdBy, :lastUpdateDate, :lastUpdatedBy ")
			.append(")") ;
			
			String sql = str.toString() ;
			
			// 参数
			SqlParameterSource[] batchArgs = new SqlParameterSource[formulaList.size()];
			int i = 0;
			for (CalFormulaBean bean : formulaList) {
				bean.setAccHrcyId(accHrcy.getAccHrcyId());
				batchArgs[i] = new BeanPropertySqlParameterSource(bean);
				i++ ;
			}
			// 执行
			namedParameterJdbcTemplate.batchUpdate(sql, batchArgs) ;
			
			// 新增预处理信息
			for(CalFormulaBean bean : formulaList){
				this.addPreFormulas(bean.getPreFormulaBeans(), bean.getFormulaId());
			}
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: updateFormulas</p> 
	 * <p>Description: 修改指标公式信息 </p> 
	 * @param formulaList
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.TabCommonDAO#updateFormulas(java.util.List)
	 */
	@Override
	public void updateFormulas(List<CalFormulaBean> formulaList, AccHrcy accHrcy)throws Exception {
		if(formulaList != null && formulaList.size() >0){
			// SQL语句
			StringBuffer str = new StringBuffer() ;
			str.append("UPDATE xc_rep_formulas SET ")
			.append(" rowitem_id = :rowItemId, colitem_id = :colItemId, ledger_id = :ledgerId, formula = :formula,")
			.append(" formula_desc = :formulaDesc, formula_type = :formulaType, orderby = :orderby, formula_level = :formulaLevel,")
			.append(" last_update_date = :lastUpdateDate, last_updated_by = :lastUpdatedBy ")
			.append("WHERE formula_id = :formulaId ");
			
			String sql = str.toString() ;
			
			// 参数
			SqlParameterSource[] batchArgs = new SqlParameterSource[formulaList.size()];
			int i = 0;
			for (CalFormulaBean bean : formulaList) {
				batchArgs[i] = new BeanPropertySqlParameterSource(bean);
				i++ ;
			}
			// 执行
			namedParameterJdbcTemplate.batchUpdate(sql, batchArgs) ;
			
			// 删除公式预处理信息
			this.delPreFormulas(formulaList);
			
			// 新增公式预处理信息
			for(CalFormulaBean bean : formulaList){
				this.addPreFormulas(bean.getPreFormulaBeans(), bean.getFormulaId());
			}
		}
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: addPreFormulas</p> 
	 * <p>Description: 新增公式预处理信息 </p> 
	 * @param preFormulaList
	 * @param formulaId
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.TabCommonDAO#addPreFormulas(java.util.List, java.lang.String)
	 */
	@Override
	public void addPreFormulas(List<PreFormulaBean> preFormulaList, String formulaId) throws Exception {
		if(preFormulaList != null && preFormulaList.size() >0){
			StringBuffer str = new StringBuffer() ;
			str.append("insert into xc_rep_pre_formulas( ")
			.append(" pre_formula_id, formula_cat, chk_f_exp_cat, formula_id, formula_str, formula_json, formula_type, orderby,")
			.append(" creation_date, created_by, last_update_date, last_updated_by")
			.append(") values (")
			.append(" :preFormulaId, :formulaCat, :chkFormulaType, :formulaId, :formulaStr, :formulaJson, :formulaType, :orderby,")
			.append(" :creationDate, :createdBy, :lastUpdateDate, :lastUpdatedBy ")
			.append(")") ;
			String sql = str.toString() ;
			
			SqlParameterSource[] batchArgs = new SqlParameterSource[preFormulaList.size()];
			int i = 0;
			for (PreFormulaBean bean : preFormulaList) {
				bean.setFormulaId(formulaId);
				batchArgs[i] = new BeanPropertySqlParameterSource(bean);
				i++ ;
			}
			
			namedParameterJdbcTemplate.batchUpdate(sql, batchArgs) ;
		}
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: delPreFormulas</p> 
	 * <p>Description: 删除公式预处理信息 </p> 
	 * @param formulaList
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.TabCommonDAO#delPreFormulas(java.util.List)
	 */
	@Override
	public void delPreFormulas(List<CalFormulaBean> formulaList) throws Exception {
		if(formulaList != null && formulaList.size() >0){
			String sql = "delete from xc_rep_pre_formulas where formula_id = :formulaId" ;
			
			SqlParameterSource[] batchArgs = new SqlParameterSource[formulaList.size()];
			int i = 0;
			for (CalFormulaBean bean : formulaList) {
				batchArgs[i] = new BeanPropertySqlParameterSource(bean);
				i++ ;
			}
			
			namedParameterJdbcTemplate.batchUpdate(sql, batchArgs) ;
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: addTabs</p> 
	 * <p>Description: 增加模板基础信息 </p> 
	 * @param tabList
	 * @param accHrcy
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.TabCommonDAO#addTabs(java.util.List, com.xzsoft.xc.gl.modal.AccHrcy)
	 */
	@Override
	public void addTabs(List<ESTabBean> tabList, AccHrcy accHrcy) throws Exception {
		if(tabList != null && tabList.size() >0){
			// 新增模板基础信息
			StringBuffer str = new StringBuffer() ;
			str.append("insert into xc_rep_tab (")
			.append(" tab_id, acc_hrcy_id, tab_code, tab_name, tab_order, is_active, color, ")
			.append(" creation_date, created_by, last_update_date, last_updated_by")
			.append(") values (")
			.append(" :tabId, :accHrcyId, :tabCode, :tabName, :tabOrder, :isActive, :color,")
			.append(" :creationDate, :createdBy, :lastUpdateDate, :lastUpdatedBy ")
			.append(")") ;
			String sql = str.toString() ;
			
			SqlParameterSource[] batchArgs = new SqlParameterSource[tabList.size()];
			int i = 0;
			for (ESTabBean bean : tabList) {
				bean.setAccHrcyId(accHrcy.getAccHrcyId());
				batchArgs[i] = new BeanPropertySqlParameterSource(bean);
				i++ ;
			}
			namedParameterJdbcTemplate.batchUpdate(sql, batchArgs) ;
			
			// 删除单元格信息
			this.delTabCells(tabList);
			
			// 删除模板元素信息
			this.delTabElements(tabList);
			
			// 新增单元格、元素、行指标引用和列指标引用关系
			for(ESTabBean tab : tabList){
				String tabId = tab.getTabId() ;
				this.addTabCells(tab.getCells(), tabId);
				this.addTabElements(tab.getElements(), tabId);
				this.addAndDelRowitemRef(tab.getRowItemsRef(), tabId);
				this.addAndDelColitemRef(tab.getColItemsRef(), tabId);
			}
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: updateTabs</p> 
	 * <p>Description: 修改模板基础信息 </p> 
	 * @param tabList
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.TabCommonDAO#updateTabs(java.util.List)
	 */
	@Override
	public void updateTabs(List<ESTabBean> tabList) throws Exception {
		if(tabList != null && tabList.size() >0){
			// 处理模板基础信息
			StringBuffer str = new StringBuffer() ;
			str.append("UPDATE xc_rep_tab SET ")
			.append(" tab_code = :tabCode, tab_name = :tabName, tab_order = :tabOrder, is_active = :isActive, color = :color, ")
			.append(" last_update_date = :lastUpdateDate, last_updated_by = :lastUpdatedBy ")
			.append(" WHERE tab_id = :tabId ") ;
			String sql = str.toString() ;
			
			SqlParameterSource[] batchArgs = new SqlParameterSource[tabList.size()];
			int i = 0;
			for (ESTabBean bean : tabList) {
				batchArgs[i] = new BeanPropertySqlParameterSource(bean);
				i++ ;
			}
			
			namedParameterJdbcTemplate.batchUpdate(sql, batchArgs) ;
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: addTabCells</p> 
	 * <p>Description: 增加模板单元格信息</p> 
	 * @param tabCellList
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.TabCommonDAO#addTabCells(java.util.List)
	 */
	@Override
	public void addTabCells(List<ESTabCellBean> tabCellList, String tabId) throws Exception {
		if(tabCellList != null && tabCellList.size() >0){
			StringBuffer str = new StringBuffer() ;
			str.append("insert into xc_rep_tab_cell ( ") 
			.append(" cell_id, tab_id, x, y, content, is_cal, raw_data, cell_type, comment, ")
			.append(" creation_date, created_by, last_update_date, last_updated_by ")
			.append(") values (")
			.append(" :cellId, :tabId, :x, :y, :content, :isCal, :rawData, :cellType, :comment, ")
			.append(" :creationDate, :createdBy, :lastUpdateDate, :lastUpdatedBy ")
			.append(")") ;
			String sql = str.toString() ;
			
			SqlParameterSource[] batchArgs = new SqlParameterSource[tabCellList.size()];
			int i = 0;
			for (ESTabCellBean bean : tabCellList) {
				bean.setTabId(tabId);
				batchArgs[i] = new BeanPropertySqlParameterSource(bean);
				i++ ;
			}
			
			namedParameterJdbcTemplate.batchUpdate(sql, batchArgs) ;
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: addTabElements</p> 
	 * <p>Description: 增加模板元素信息 </p> 
	 * @param tabElementList
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.TabCommonDAO#addTabElements(java.util.List)
	 */
	@Override
	public void addTabElements(List<ESTabElementBean> tabElementList, String tabId) throws Exception {
		if(tabElementList != null && tabElementList.size() >0){
			StringBuffer str = new StringBuffer() ;
			str.append("insert into xc_rep_tab_element (") 
			.append(" element_id, tab_id, name, etype, content, ")
			.append(" creation_date, created_by, last_update_date, last_updated_by ")
			.append(") values (")
			.append(" :elementId, :tabId, :name, :etype, :content, ")
			.append(" :creationDate, :createdBy, :lastUpdateDate, :lastUpdatedBy ")
			.append(")");
			String sql = str.toString() ;
			
			SqlParameterSource[] batchArgs = new SqlParameterSource[tabElementList.size()];
			int i = 0;
			for (ESTabElementBean bean : tabElementList) {
				bean.setTabId(tabId);
				batchArgs[i] = new BeanPropertySqlParameterSource(bean);
				i++ ;
			}
			
			namedParameterJdbcTemplate.batchUpdate(sql, batchArgs) ;
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: delTabCells</p> 
	 * <p>Description: 删除模板单元格信息  </p> 
	 * @param tabList
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.TabCommonDAO#delTabCells(java.util.List)
	 */
	@Override
	public void delTabCells(List<ESTabBean> tabList) throws Exception {
		if(tabList != null && tabList.size() >0){
			String sql = "delete from xc_rep_tab_cell where tab_id = :tabId " ;
			
			SqlParameterSource[] batchArgs = new SqlParameterSource[tabList.size()];
			int i = 0;
			for (ESTabBean bean : tabList) {
				batchArgs[i] = new BeanPropertySqlParameterSource(bean);
				i++ ;
			}
			
			namedParameterJdbcTemplate.batchUpdate(sql, batchArgs) ;
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: delTabElements</p> 
	 * <p>Description: 删除模板元素信息 </p> 
	 * @param tabList
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.TabCommonDAO#delTabElements(java.util.List)
	 */
	@Override
	public void delTabElements(List<ESTabBean> tabList) throws Exception {
		if(tabList != null && tabList.size() >0){
			String sql = "delete from xc_rep_tab_element where tab_id = :tabId" ;
			
			SqlParameterSource[] batchArgs = new SqlParameterSource[tabList.size()];
			int i = 0;
			for (ESTabBean bean : tabList) {
				batchArgs[i] = new BeanPropertySqlParameterSource(bean);
				i++ ;
			}
			
			namedParameterJdbcTemplate.batchUpdate(sql, batchArgs) ;
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: addAndDelRowitemRef</p> 
	 * <p>Description: 新增和删除行指标引用 </p> 
	 * @param rowitemsRefList
	 * @param tabId
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.TabCommonDAO#addAndDelRowitemRef(java.util.List, java.lang.String, java.util.HashMap)
	 */
	@Override
	public void addAndDelRowitemRef(List<ESRowItemRefBean> rowitemsRefList, String tabId) throws Exception {
		if(rowitemsRefList != null && rowitemsRefList.size() >0){
			// 删除
			String delSql = "delete from xc_rep_rm_ref where tab_id = :tabId " ;
			
			Map<String, Object> paramMap = new HashMap<String,Object>() ;
			paramMap.put("tabId", tabId) ;
			
			namedParameterJdbcTemplate.update(delSql,paramMap) ;
			
			// 新增
			StringBuffer str = new StringBuffer() ;
			str.append("insert into xc_rep_rm_ref( ")
			.append(" rm_ref_id, tab_id, rowitem_id, rowno, lanno, x, y, enabled,")
			.append(" creation_date, created_by, last_update_date, last_updated_by")
			.append(") values (")
			.append(" :rmRefId, :tabId, :rowitemId, :rowno, :lanno, :x, :y, :enabled,")
			.append(" :creationDate, :createdBy, :lastUpdateDate, :lastUpdatedBy")
			.append(")") ;
			String addSql = str.toString() ;
			
			SqlParameterSource[] batchArgs = new SqlParameterSource[rowitemsRefList.size()];
			int i = 0;
			for (ESRowItemRefBean bean : rowitemsRefList) {
				bean.setTabId(tabId);
				batchArgs[i] = new BeanPropertySqlParameterSource(bean);
				i++ ;
			}
			
			namedParameterJdbcTemplate.batchUpdate(addSql, batchArgs) ;
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: addAndDelColitemRef</p> 
	 * <p>Description: 新增和删除列指标引用  </p> 
	 * @param colitemsRefList
	 * @param tabId
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.TabCommonDAO#addAndDelColitemRef(java.util.List, java.lang.String, java.util.HashMap)
	 */
	@Override
	public void addAndDelColitemRef(List<ESColItemRefBean> colitemsRefList, String tabId) throws Exception {
		if(colitemsRefList != null && colitemsRefList.size() >0){
			// 删除
			String delSql = "delete from xc_rep_cm_ref where tab_id = '"+tabId+"'" ;
			
			Map<String, Object> paramMap = new HashMap<String,Object>() ;
			paramMap.put("tabId", tabId) ;
			
			namedParameterJdbcTemplate.update(delSql,paramMap) ;
			
			// 新增
			StringBuffer str = new StringBuffer() ;
			str.append("insert into xc_rep_cm_ref( ")
			.append(" col_ref_id, tab_id, colitem_id, colno, lanno, enabled,")
			.append(" creation_date, created_by, last_update_date, last_updated_by")
			.append(") values (")
			.append(" :colRefId, :tabId, :colitemId, :colno, :lanno, :enabled, ")
			.append(" :creationDate, :createdBy, :lastUpdateDate, :lastUpdatedBy")
			.append(")") ;
			String addSql = str.toString() ;
			
			SqlParameterSource[] batchArgs = new SqlParameterSource[colitemsRefList.size()];
			int i = 0;
			for (ESColItemRefBean bean : colitemsRefList) {
				bean.setTabId(tabId);
				batchArgs[i] = new BeanPropertySqlParameterSource(bean);
				i++ ;
			}
			
			namedParameterJdbcTemplate.batchUpdate(addSql, batchArgs) ;
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: getItemIdByCode</p> 
	 * <p>Description: 根据指标编码获取指标ID </p> 
	 * @param accHrcyId
	 * @param itemCode
	 * @param itemType
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.TabCommonDAO#getItemIdByCode(java.lang.String, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String getItemIdByCode(String accHrcyId, String itemCode, String itemType) throws Exception {
		String itemId = null ;
		
		HttpSession session = CurrentSessionVar.getSession() ;
		String userName = CurrentSessionVar.getUserName() ;
		
		String rowKey = "$".concat(userName).concat(session.getId()).concat("$row") ;
		String colKey = "$".concat(userName).concat(session.getId()).concat("$col") ;
		
		// 指标ID
		switch(itemType){
		case "row" :
			// 加载数据
			Map<String,ESRowItemBean> rowMap = (Map<String, ESRowItemBean>) session.getAttribute(rowKey) ;
			if(rowMap != null && rowMap.size() >0){
				ESRowItemBean rowItemBean = rowMap.get(itemCode) ;
				itemId = rowItemBean.getRowitemId() ;
			}else{
				Map<String,ESRowItemBean> rowitems = xcRepCommonDAO.getAllRowitems4Map(accHrcyId,"cache") ;
				session.setAttribute(rowKey, rowitems);
				itemId = rowitems.get(itemCode).getRowitemId() ;
			}
			break;
			
		case "col" :
			// 加载数据
			Map<String,ESColItemBean> colMap = (Map<String, ESColItemBean>) session.getAttribute(colKey) ;
			// 列指标ID
			if(colMap != null && colMap.size() >0){
				ESColItemBean colItemBean = colMap.get(itemCode) ;
				itemId = colItemBean.getColitemId() ;
			}else{
				Map<String,ESColItemBean> colitems = xcRepCommonDAO.getAllColItems4Map(accHrcyId,"cache") ;
				session.setAttribute(colKey, colitems);
				itemId = colitems.get(itemCode).getColitemId() ;
			}
			break;
			
		default :
			break ;
		}
		
		return itemId;
	}
	
}
