package com.xzsoft.xc.rep.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.xzsoft.xc.rep.dao.XCRepCommonDAO;
import com.xzsoft.xc.rep.mapper.XCRepCommonMapper;
import com.xzsoft.xc.rep.modal.CalFormulaBean;
import com.xzsoft.xc.rep.modal.ESColItemBean;
import com.xzsoft.xc.rep.modal.ESRowItemBean;
import com.xzsoft.xc.rep.modal.FuncBean;
import com.xzsoft.xc.rep.modal.RepCurrencyBean;
import com.xzsoft.xc.rep.modal.RepPeriodBean;
import com.xzsoft.xc.rep.modal.RepSheetBean;

/**
 * @ClassName: XCRepCommonDAOImpl 
 * @Description: 报表通用查询数据层接口实现类
 * @author linp
 * @date 2016年8月4日 下午4:12:16 
 *
 */
@Repository("xcRepCommonDAO") 
public class XCRepCommonDAOImpl implements XCRepCommonDAO {
	@Autowired
	private XCRepCommonMapper xcRepCommonMapper ;
	
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getPeriodByCode</p> 
	 * <p>Description: 按期间编码查询期间信息 </p> 
	 * @param suitId
	 * @param periodCode
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.XCRepCommonDAO#getPeriodByCode(java.lang.String, java.lang.String)
	 */
	@Override
	public RepPeriodBean getPeriodByCode(String periodCode) throws Exception {
		return xcRepCommonMapper.getPeriodByCode(periodCode) ;
	}
	/*
	 * (非 Javadoc) 
	 * <p>Title: getRepCurrencyByCode</p> 
	 * <p>Description: 按币种编码查询币种信息 </p> 
	 * @param currencyCode
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.XCRepCommonDAO#getRepCurrencyByCode(java.lang.String, java.lang.String)
	 */
	public RepCurrencyBean getRepCurrencyByCode(String currencyCode) throws Exception {
		return xcRepCommonMapper.getRepCurrencyByCode(currencyCode) ;
	}
	
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getRowItemById</p> 
	 * <p>Description: 按行指标ID查询行指标信息 </p> 
	 * @param rowItemId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.XCRepCommonDAO#getRowItemById(java.lang.String)
	 */
	@Override
	public ESRowItemBean getRowItemById(String rowItemId) throws Exception {
		return xcRepCommonMapper.getRowItemById(rowItemId) ;
	}
	/*
	 * (非 Javadoc) 
	 * <p>Title: getRowItemById</p> 
	 * <p>Description: 按行指标编码查询行指标信息 </p> 
	 * @param rowItemId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.XCRepCommonDAO#getRowItemById(java.lang.String)
	 */
	@Override
	public ESRowItemBean getRowItemByCode(String accHrcyId, String rowItemCode) throws Exception {
		
		HashMap<String,Object> map = new HashMap<String,Object>() ;
		map.put("accHrcyId", accHrcyId) ;
		map.put("rowItemCode", rowItemCode) ;
		
		return xcRepCommonMapper.getRowItemByCode(map) ;
	}
	/*
	 * (非 Javadoc) 
	 * <p>Title: getRowItemsByTab</p> 
	 * <p>Description: 查询模板下的行指标信息 </p> 
	 * @param tabId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.XCRepCommonDAO#getRowItemsByModalId(java.lang.String)
	 */
	@Override
	public List<ESRowItemBean> getRowItemsByTab(String tabId) throws Exception {
		return xcRepCommonMapper.getRowItemsByTab(tabId) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getRowItemsByTabs</p> 
	 * <p>Description: 查询指定模板下的所有行指标信息 </p> 
	 * @param tabs
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.XCRepCommonDAO#getRowItemsByTabs(java.util.List)
	 */
	@Override
	public List<ESRowItemBean> getRowItemsByTabs(List<String> tabs) throws Exception {
		return xcRepCommonMapper.getRowItemsByTabs(tabs) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getAllRowItems</p> 
	 * <p>Description: 查询表套下所有的行指标信息 </p> 
	 * @param accHrcyId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.XCRepCommonDAO#getAllRowItems(java.lang.String)
	 */
	@Override
	public List<ESRowItemBean> getAllRowItems(String accHrcyId) throws Exception {
		return xcRepCommonMapper.getAllRowItems(accHrcyId) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getAllRowitems4Map</p> 
	 * <p>Description: 查询表套下所有的行指标信息 </p> 
	 * @param accHrcyId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.XCRepCommonDAO#getAllRowitems4Map(java.lang.String)
	 */
	@Override
	public Map<String,ESRowItemBean> getAllRowitems4Map(String accHrcyId,String opType) throws Exception {
		Map<String,ESRowItemBean> map = new HashMap<String,ESRowItemBean>() ;
		
		List<ESRowItemBean> list = null ;
		
		switch(opType){
		case "query" :
			list = xcRepCommonMapper.getAllRowItems(accHrcyId) ;
			break;
			
		case "cache" :
			list = xcRepCommonMapper.getAllRowItems4Cache(accHrcyId) ;
			break ;
			
		default :
			break ;
		}
		
		if(list != null && list.size() >0){
			for(ESRowItemBean bean : list){
				map.put(bean.getRowitemCode(), bean) ;
			}
		}
		
		return map ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getColItemById</p> 
	 * <p>Description: 按列指标ID查询列指标信息 </p> 
	 * @param colItemId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.XCRepCommonDAO#getColItemById(java.lang.String)
	 */
	@Override
	public ESColItemBean getColItemById(String colItemId) throws Exception {
		return xcRepCommonMapper.getColItemById(colItemId) ;
	}
	/*
	 * (非 Javadoc) 
	 * <p>Title: getColItemByCode</p> 
	 * <p>Description: 按列指标ID查询列指标信息 </p> 
	 * @param accHrcyId
	 * @param colItemCode
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.XCRepCommonDAO#getColItemByCode(java.lang.String, java.lang.String)
	 */
	@Override
	public ESColItemBean getColItemByCode(String accHrcyId, String colItemCode) throws Exception {
		HashMap<String,String> map = new HashMap<String,String>() ;
		map.put("accHrcyId", accHrcyId) ;
		map.put("colItemCode", colItemCode) ;
		
		return  xcRepCommonMapper.getColItemByCode(map) ;
	}
	/*
	 * (非 Javadoc) 
	 * <p>Title: getColItemsByTab</p> 
	 * <p>Description: 查询模板下的列指标信息 </p> 
	 * @param tabId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.XCRepCommonDAO#getColItemsByModalId(java.lang.String)
	 */
	@Override
	public List<ESColItemBean> getColItemsByTab(String tabId) throws Exception {
		return xcRepCommonMapper.getColItemsByTab(tabId) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getColItemsByTabs</p> 
	 * <p>Description: 查询指定模板下的所有列指标信息 </p> 
	 * @param tabs
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.XCRepCommonDAO#getColItemsByTabs(java.util.List)
	 */
	@Override
	public List<ESColItemBean> getColItemsByTabs(List<String> tabs) throws Exception {
		return xcRepCommonMapper.getColItemsByTabs(tabs) ;
	}
	/*
	 * (非 Javadoc) 
	 * <p>Title: getAllColItems</p> 
	 * <p>Description: 查询科目体系下的列表指标信息 </p> 
	 * @param accHrcyId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.XCRepCommonDAO#getAllColItems(java.lang.String)
	 */
	@Override
	public List<ESColItemBean> getAllColItems(String accHrcyId) throws Exception {
		return xcRepCommonMapper.getAllColItems(accHrcyId) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getAllColItems4Map</p> 
	 * <p>Description: 查询科目体系下的列表指标信息 </p> 
	 * @param accHrcyId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.XCRepCommonDAO#getAllColItems4Map(java.lang.String)
	 */
	@Override
	public Map<String,ESColItemBean> getAllColItems4Map(String accHrcyId,String opType) throws Exception {
		Map<String,ESColItemBean> map = new HashMap<String,ESColItemBean>() ;
		
		List<ESColItemBean> list = null ;
		switch(opType){
		case "query" :
			list = xcRepCommonMapper.getAllColItems(accHrcyId) ;
			break;
			
		case "cache" :
			list = xcRepCommonMapper.getAllColItems4Cache(accHrcyId) ;
			break ;
			
		default :
			break ;
		}
		
		if(list != null && list.size() >0){
			for(ESColItemBean bean : list){
				map.put(bean.getColitemCode(), bean) ;
			}
		}
		
		return map ;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: getRepAllCalFormulas</p> 
	 * <p>Description: 查询所有指标计算公式 </p> 
	 * @param accHrcyId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.XCRepCommonDAO#getRepAllCalFormulas(java.lang.String)
	 */
	@Override
	public List<CalFormulaBean> getRepAllCalFormulas(String accHrcyId)throws Exception {
		return xcRepCommonMapper.getRepAllCalFormulas(accHrcyId) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getRepAllCalFormulasByTab</p> 
	 * <p>Description: 按模板查询报表指标计算公式信息  </p> 
	 * <p>
	 * 	注意：如果某个指标公共级和账簿级同时存在公式信息，则只采用账簿级公式、不再采用公共级公式
	 * </p>
	 * @param sheetBean
	 * @param isContainPreFormula
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.XCRepCommonDAO#getRepCalFormulas(java.util.HashMap, boolean)
	 */
	@Override
	public List<CalFormulaBean> getRepAllCalFormulasByTab(RepSheetBean sheetBean)throws Exception {
		return xcRepCommonMapper.getRepCalFormulasByTab(sheetBean) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getRepAllCalFormulasByTplAndFType</p> 
	 * <p>Description: 按模板及公式类别查询报表指标计算公式信息 </p> 
	 * @param map
	 * @param isContainPreFormula
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.XCRepCommonDAO#getRepAllCalFormulasByTplAndFType(java.util.HashMap, boolean)
	 */
	@Override
	public HashMap<String,List<CalFormulaBean>> getRepAllCalFormulasByTplAndFType(RepSheetBean sheetBean)throws Exception {
		HashMap<String,List<CalFormulaBean>> formulasMap = new HashMap<String,List<CalFormulaBean>>() ;
		
		// 查询报表指标公式信息 
		List<CalFormulaBean> formulaList = xcRepCommonMapper.getRepCalFormulasByTab(sheetBean) ;
		
		// 整合公式
		if(formulaList != null && formulaList.size()>0){
			
			// 定义数组存储公式信息：纯APP类、APP与REP混合类、纯REP类公式
			List<CalFormulaBean> appFormulas = new ArrayList<CalFormulaBean>() ;
			List<CalFormulaBean> mixedFormulas = new ArrayList<CalFormulaBean>() ;
			List<CalFormulaBean> repFormulas = new ArrayList<CalFormulaBean>() ;
			
			// 公式分类处理
			for(CalFormulaBean bean : formulaList){
				// 公式分类处理 ： REP-指标间公式，APP-取数公式，MIXED-混合公式
				String formulaType = bean.getFormulaType() ;
				switch(formulaType){
				case "APP" :
					appFormulas.add(bean) ;
					break ;
				case "REP" :
					repFormulas.add(bean) ;
					break ;
				case "MIXED" :
					mixedFormulas.add(bean) ;
					break ;
				default :
					break ;
				}
			}
			
			// 返回值
			formulasMap.put("appFormulas", appFormulas) ;
			formulasMap.put("repFormulas", repFormulas) ;
			formulasMap.put("mixedFormulas", mixedFormulas) ;
		}		
		
		return formulasMap ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getRepCalCommonFormulasByTpl</p> 
	 * @Description: 按模板查询报表公共级指标计算公式信息
	 * <p>
	 * 	查询报表公共级的指标公式信息
	 * 	isContainPreFormula：如果值为true,包含预处理后的指标公式信息；如果为false,则不包含预处理后的公式信息。
	 * </p>
	 * @param map
	 * @param isContainPreFormula
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.XCRepCommonDAO#getRepCalCommonFormulasByTpl(java.util.HashMap, boolean)
	 */
	@Override
	public List<CalFormulaBean> getRepCalCommonFormulasByTpl(HashMap<String,Object> map)throws Exception {
		map.put("formulaLevel", "common") ;
		return xcRepCommonMapper.getRepCalFormulasByLevel(map) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getRepCalLedgerFormulasByTpl</p> 
	 * <p>Description: 按模板查询报表账簿级指标计算公式信息 </p> 
	 * @param map
	 * @param isContainPreFormula
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.XCRepCommonDAO#getRepCalLedgerFormulasByTpl(java.util.HashMap, boolean)
	 */
	@Override
	public List<CalFormulaBean> getRepCalLedgerFormulasByTpl(HashMap<String,Object> map)throws Exception {
		map.put("formulaLevel", "ledger") ;
		return xcRepCommonMapper.getRepCalFormulasByLevel(map) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getCommonFormulasByTab</p> 
	 * <p>Description: 按模板查询公共级公式信息 </p> 
	 * @param accHrcyId
	 * @param tabs
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.XCRepCommonDAO#getCommonFormulasByTab(java.lang.String, java.util.List)
	 */
	@Override
	public List<CalFormulaBean> getCommonFormulasByTab(String accHrcyId,List<String> tabs) throws Exception {
		HashMap<String,Object> map = new HashMap<String,Object>() ;
		map.put("hrcyId", accHrcyId) ;
		map.put("list", tabs) ;
		
		return xcRepCommonMapper.getCommonFormulasByTab(map) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getCommonFormulas4Map</p> 
	 * <p>Description: 查询科目体系下所有的公共级公式 </p> 
	 * @param accHrcyId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.XCRepCommonDAO#getCommonFormulas4Map(java.lang.String)
	 */
	@Override
	public Map<String,CalFormulaBean> getCommonFormulas4Map(String accHrcyId) throws Exception {
		Map<String,CalFormulaBean> map = new HashMap<String,CalFormulaBean>() ;
		
		List<CalFormulaBean> list = xcRepCommonMapper.getCommonFormulas4Map(accHrcyId) ;
		if(list != null && list.size()>0){
			for(CalFormulaBean bean : list){
				String key = bean.getRowItemCode().concat("/").concat(bean.getColItemCode()) ;
				map.put(key, bean) ;
			}
		}
		
		return map ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getFunBeanById</p> 
	 * <p>Description: 根据函数ID查询自定义函数信息 </p> 
	 * @param funId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.XCRepCommonDAO#getFunBeanById(java.lang.String)
	 */
	@Override
	public FuncBean getFunBeanById(String funId) throws Exception {
		return xcRepCommonMapper.getFunBeanById(funId) ; 
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getFunBeanByCode</p> 
	 * <p>Description: 根据函数编码查询自定义函数信息 </p> 
	 * @param funCode
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.XCRepCommonDAO#getFunBeanByCode(java.lang.String)
	 */
	@Override
	public FuncBean getFunBeanByCode(String funCode) throws Exception {
		return xcRepCommonMapper.getFunBeanByCode(funCode) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getAllFuncBean</p> 
	 * <p>Description: 查询自定义函数信息 </p> 
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.XCRepCommonDAO#getAllFuncBean()
	 */
	@Override
	public List<FuncBean> getAllFuncBean() throws Exception {
		return xcRepCommonMapper.getAllFuncBean() ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getAllFunctions</p> 
	 * <p>Description: 查询所有自定义函数信息 </p> 
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.XCRepCommonDAO#getAllFunctions()
	 */
	@Override
	public Map<String,FuncBean> getAllFunctions() throws Exception {
		Map<String,FuncBean> map = new HashMap<String,FuncBean>() ;
		List<FuncBean> list = xcRepCommonMapper.getAllFuncBean() ;
		if(list != null && list.size() >0){
			for(FuncBean bean : list){
				map.put(bean.getFunCode(), bean) ;
			}
		}
		return map ;
	}
	
}
