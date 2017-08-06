package com.xzsoft.xc.rep.mapper;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.rep.modal.CalFormulaBean;
import com.xzsoft.xc.rep.modal.ESColItemBean;
import com.xzsoft.xc.rep.modal.ESRowItemBean;
import com.xzsoft.xc.rep.modal.FuncBean;
import com.xzsoft.xc.rep.modal.FuncParamsBean;
import com.xzsoft.xc.rep.modal.RepCurrencyBean;
import com.xzsoft.xc.rep.modal.RepPeriodBean;
import com.xzsoft.xc.rep.modal.RepSheetBean;

/**
 * @ClassName: XCRepCommonMapper 
 * @Description: 报表通用处理Mapper
 * @author linp
 * @date 2016年8月4日 上午10:50:03 
 *
 */
public interface XCRepCommonMapper {
	
	/**
	 * @Title: getPeriodByCode 
	 * @Description: 按期间编码查询期间信息
	 * @param map
	 * @return    设定文件
	 */
	public RepPeriodBean getPeriodByCode(String periodCode) ;
	
	/**
	 * @Title: getRepCurrencyByCode 
	 * @Description: 按币种编码查询币种信息
	 * @param map
	 * @return    设定文件
	 */
	public RepCurrencyBean getRepCurrencyByCode(String cnyCode) ;
	
	/**
	 * @Title: getRowItemById 
	 * @Description: 按行指标ID查询行指标信息
	 * @param rowItemId
	 * @return    设定文件
	 */
	public ESRowItemBean getRowItemById(String rowItemId) ;
	
	/**
	 * @Title: getRowItemById 
	 * @Description: 按行指标编码查询行指标信息
	 * @param rowItemId
	 * @return    设定文件
	 */
	public ESRowItemBean getRowItemByCode(HashMap<String,?> map) ;
	
	/**
	 * @Title: getRowItemsByTab 
	 * @Description: 查询模板下的行指标信息
	 * @param modalsheetId
	 * @return    设定文件
	 */
	public List<ESRowItemBean> getRowItemsByTab(String tabId)  ;
	
	/**
	 * @Title: getRowItemsByTabs 
	 * @Description: 查询指定模板下的所有行指标信息
	 * @param tabs
	 * @return    设定文件
	 */
	public List<ESRowItemBean> getRowItemsByTabs(List<String> tabs)  ;
	
	/**
	 * @Title: getAllRowItems 
	 * @Description: 查询表套下所有的行指标信息
	 * @param accHrcyId
	 * @return    设定文件
	 */
	public List<ESRowItemBean> getAllRowItems(String accHrcyId) ;
	
	/**
	 * @Title: getAllRowItems4Cache 
	 * @Description: 查询表套下所有的行指标信息
	 * @param accHrcyId
	 * @return    设定文件
	 */
	public List<ESRowItemBean> getAllRowItems4Cache(String accHrcyId) ;

	/**
	 * @Title: getColItemById 
	 * @Description: 按列指标ID查询列指标信息
	 * @param colItemId
	 * @return    设定文件
	 */
	public ESColItemBean getColItemById(String colItemId) ;
	
	/**
	 * @Title: getColItemByCode 
	 * @Description: 按列指标编码查询列指标信息
	 * @param map
	 * @return    设定文件
	 */
	public ESColItemBean getColItemByCode(HashMap<String,?> map) ;
	
	/**
	 * @Title: getColItemsByTab 
	 * @Description: 查询模板下的列指标信息 
	 * @param  tabId
	 * @return    设定文件
	 */
	public List<ESColItemBean> getColItemsByTab(String tabId) ;
	
	/**
	 * @Title: getColItemsByTabs 
	 * @Description: 查询指定模板下的所有列指标信息 
	 * @param tabId
	 * @return    设定文件
	 */
	public List<ESColItemBean> getColItemsByTabs(List<String> tabs) ;
	
	/**
	 * @Title: getAllColItems 
	 * @Description: 查询表套下的列表指标信息
	 * @param suitId
	 * @return    设定文件
	 */
	public List<ESColItemBean> getAllColItems(String accHrcyId) ;
	
	/**
	 * @Title: getAllColItems4Cache 
	 * @Description: 查询表套下的列表指标信息
	 * @param accHrcyId
	 * @return    设定文件
	 */
	public List<ESColItemBean> getAllColItems4Cache(String accHrcyId) ;
	
	/**
	 * @Title: getRepAllCalFormulas 
	 * @Description: 查询某科目体系下所有计算公式
	 * @param accHrcyId
	 * @return    设定文件
	 */
	public List<CalFormulaBean> getRepAllCalFormulas(String accHrcyId) ;
	
	/**
	 * @Title: getRepCalFormulasByTab 
	 * @Description: 按模板查询指标取数公式(公共级别和账簿级)
	 * @param map
	 * @return    设定文件
	 */
	public List<CalFormulaBean> getRepCalFormulasByTab(RepSheetBean sheetBean) ;
	
	/**
	 * @Title: getRepCalFormulasByLevel 
	 * @Description: 按模板和级别查询指标取数公式(公共级或账簿级)
	 * @param map
	 * @return    设定文件
	 */
	public List<CalFormulaBean> getRepCalFormulasByLevel(HashMap<String,?> map) ;
	
	/**
	 * @Title: getCommonFormulasByTab 
	 * @Description: 按模板查询公共级公式信息
	 * @param map
	 * @return    设定文件
	 */
	public List<CalFormulaBean> getCommonFormulasByTab(HashMap<String,?> map) ;
	
	/**
	 * @Title: getCommonFormulas4Map 
	 * @Description: 查询科目体系下所有的公共级公式
	 * @param hrcyId
	 * @return    设定文件
	 */
	public List<CalFormulaBean> getCommonFormulas4Map(String hrcyId) ;
	
	/**
	 * @Title: getFunBeanById 
	 * @Description: 根据函数ID查询自定义函数信息 
	 * @param funcId
	 * @return    设定文件
	 */
	public FuncBean getFunBeanById(String funcId)  ;
	
	/**
	 * @Title: getFunBeanByCode 
	 * @Description: 根据函数编码查询自定义函数信息
	 * @param funCode
	 * @return    设定文件
	 */
	public FuncBean getFunBeanByCode(String funCode) ;
	
	/**
	 * @Title: getFunParams 
	 * @Description: 根据函数ID查询自定义函数参数信息
	 * @param funcId
	 * @return    设定文件
	 */
	public List<FuncParamsBean> getFunParams(String funcId) ;
	
	/**
	 * @Title: getAllFuncBean 
	 * @Description: 查询所有自定义函数信息
	 * @return    设定文件
	 */
	public List<FuncBean> getAllFuncBean() ;
}
