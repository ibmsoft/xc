package com.xzsoft.xc.rep.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xzsoft.xc.rep.modal.CalFormulaBean;
import com.xzsoft.xc.rep.modal.ESColItemBean;
import com.xzsoft.xc.rep.modal.ESRowItemBean;
import com.xzsoft.xc.rep.modal.FuncBean;
import com.xzsoft.xc.rep.modal.RepCurrencyBean;
import com.xzsoft.xc.rep.modal.RepPeriodBean;
import com.xzsoft.xc.rep.modal.RepSheetBean;

/**
 * @ClassName: XCRepCommonDAO 
 * @Description: 报表通用查询数据层接口类
 * @author linp
 * @date 2016年8月4日 下午4:11:47 
 *
 */
public interface XCRepCommonDAO {
	
	/**
	 * @Title: getPeriodByCode 
	 * @Description: 按期间编码查询期间信息
	 * @param periodCode
	 * @return
	 * @throws Exception    设定文件
	 */
	public RepPeriodBean getPeriodByCode(String periodCode) throws Exception ;
	
	/**
	 * @Title: getRepCurrencyByCode 
	 * @Description: 按币种编码查询币种信息
	 * @param currencyCode
	 * @return
	 * @throws Exception    设定文件
	 */
	public RepCurrencyBean getRepCurrencyByCode(String currencyCode) throws Exception ;
	
	/**
	 * @Title: getRowItemById 
	 * @Description: 按行指标ID查询行指标信息
	 * @param rowItemId
	 * @return
	 * @throws Exception    设定文件
	 */
	public ESRowItemBean getRowItemById(String rowItemId) throws Exception ;
	
	/**
	 * @Title: getRowItemByCode 
	 * @Description: 按行指标编码查询指标信息
	 * @param accHrcyId
	 * @param rowItemCode
	 * @return
	 * @throws Exception    设定文件
	 */
	public ESRowItemBean getRowItemByCode(String accHrcyId, String rowItemCode) throws Exception ;
	
	/**
	 * @Title: getRowItemsByModalId 
	 * @Description: 查询模板下的行指标信息
	 * @param tabId
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<ESRowItemBean> getRowItemsByTab(String tabId) throws Exception ;
	
	/**
	 * @Title: getRowItemsByTabs 
	 * @Description: 查询指定模板下的所有行指标信息
	 * @param tabs
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<ESRowItemBean> getRowItemsByTabs(List<String> tabs) throws Exception ;
	
	/**
	 * @Title: getAllRowItems 
	 * @Description: 查询表套下所有的行指标信息
	 * @param accHrcyId
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<ESRowItemBean> getAllRowItems(String accHrcyId) throws Exception ;
	
	/**
	 * @Title: getAllRowitems4Map 
	 * @Description: 查询表套下所有的行指标信息
	 * @param accHrcyId
	 * @return
	 * @throws Exception    设定文件
	 */
	public Map<String,ESRowItemBean> getAllRowitems4Map(String accHrcyId,String opType) throws Exception ;
	
	/**
	 * @Title: getColItemById 
	 * @Description: 按列指标ID查询列指标信息
	 * @param colItemId
	 * @return
	 * @throws Exception    设定文件
	 */
	public ESColItemBean getColItemById(String colItemId) throws Exception ;
	
	/**
	 * @Title: getColItemByCode 
	 * @Description: 按列指标编码查询列指标信息
	 * @param accHrcyId
	 * @param colItemCode
	 * @return
	 * @throws Exception    设定文件
	 */
	public ESColItemBean getColItemByCode(String accHrcyId, String colItemCode) throws Exception ;

	/**
	 * @Title: getColItemsByTab 
	 * @Description: 查询模板下的列指标信息
	 * @param tabId
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<ESColItemBean> getColItemsByTab(String tabId) throws Exception ;
	
	/**
	 * @Title: getColItemsByTabs 
	 * @Description: 查询指定模板下的所有列指标信息
	 * @param tabs
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<ESColItemBean> getColItemsByTabs(List<String> tabs) throws Exception ;
	
	/**
	 * @Title: getAllColItems 
	 * @Description: 查询科目体系下的列表指标信息
	 * @param accHrcyId
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<ESColItemBean> getAllColItems(String accHrcyId) throws Exception ;
	
	/**
	 * @Title: getAllColItems4Map 
	 * @Description: 查询科目体系下的列表指标信息
	 * @param accHrcyId
	 * @return
	 * @throws Exception    设定文件
	 */
	public Map<String,ESColItemBean> getAllColItems4Map(String accHrcyId,String opType) throws Exception ;
	
	/**
	 * @Title: getRepAllCalFormulas 
	 * @Description: 查询所有指标计算公式
	 * @param accHrcyId
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<CalFormulaBean> getRepAllCalFormulas(String accHrcyId)throws Exception ;
	
	/**
	 * @Title: getRepAllCalFormulasByTab 
	 * @Description: 按模板查询报表指标计算公式信息
	 * <p>
	 * 	查询报表的指标公式信息,包含公共级和账簿级公式。
	 *  如果账簿级和公共级公式均存在时，则只保留取账簿级公式。
	 * </p>
	 * @param sheetBean
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<CalFormulaBean> getRepAllCalFormulasByTab(RepSheetBean sheetBean)throws Exception ;
	
	/**
	 * @Title: getRepAllCalFormulasByTplAndFType 
	 * @Description: 按模板及公式类别查询报表指标计算公式信息
	 * <p> 
	 * 	查询报表的指标公式信息,包含公共级和账簿级公式。
	 *  如果账簿级和公共级公式均存在时，则只保留取账簿级公式。
	 * </p>
	 * @param sheetBean
	 * @return
	 * @throws Exception    设定文件
	 */
	public HashMap<String,List<CalFormulaBean>> getRepAllCalFormulasByTplAndFType(RepSheetBean sheetBean)throws Exception ;
	
	/**
	 * @Title: getRepCalCommonFormulasByTpl 
	 * @Description: 按模板查询报表公共级指标计算公式信息
	 * @param map
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<CalFormulaBean> getRepCalCommonFormulasByTpl(HashMap<String,Object> map) throws Exception ;
	
	/**
	 * @Title: getRepCalLedgerFormulasByTpl 
	 * @Description: 按模板查询报表账簿级指标计算公式信息
	 * @param map
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<CalFormulaBean> getRepCalLedgerFormulasByTpl(HashMap<String,Object> map) throws Exception ;
	
	/**
	 * @Title: getCommonFormulasByTab 
	 * @Description: 按模板查询公共级公式信息
	 * @param accHrcyId
	 * @param tabs
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<CalFormulaBean> getCommonFormulasByTab(String accHrcyId,List<String> tabs) throws Exception ;
	
	/**
	 * @Title: getCommonFormulas4Map 
	 * @Description: 查询科目体系下所有的公共级公式
	 * @param accHrcyId
	 * @return
	 * @throws Exception    设定文件
	 */
	public Map<String,CalFormulaBean> getCommonFormulas4Map(String accHrcyId) throws Exception ;
	
	/**
	 * @Title: getFunBeanById 
	 * @Description: 根据函数ID查询自定义函数信息
	 * @param funId
	 * @return
	 * @throws Exception    设定文件
	 */
	public FuncBean getFunBeanById(String funId) throws Exception ;
	
	/**
	 * @Title: getFunBeanByCode 
	 * @Description: 根据函数编码查询自定义函数信息
	 * @param funCode
	 * @return
	 * @throws Exception    设定文件
	 */
	public FuncBean getFunBeanByCode(String funCode) throws Exception ;
	
	/**
	 * @Title: getAllFuncBean 
	 * @Description: 查询自定义函数信息
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<FuncBean> getAllFuncBean() throws Exception ;
	
	/**
	 * @Title: getAllFunctions 
	 * @Description: 查询所有自定义函数信息
	 * @return
	 * @throws Exception    设定文件
	 */
	public Map<String,FuncBean> getAllFunctions() throws Exception ;
}
