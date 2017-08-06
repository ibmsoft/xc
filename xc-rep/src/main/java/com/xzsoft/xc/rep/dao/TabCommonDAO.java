package com.xzsoft.xc.rep.dao;

import java.util.List;

import com.xzsoft.xc.gl.modal.AccHrcy;
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

/**
 * @ClassName: TabCommonDAO 
 * @Description: 模板接收处理
 * @author linp
 * @date 2016年10月20日 上午10:06:53 
 *
 */
public interface TabCommonDAO {
	
	/**
	 * @Title: addFunctions 
	 * @Description: 新增自定义函数基础信息
	 * @param functionList
	 * @throws Exception    设定文件
	 */
	public void addFunctions(List<FuncBean> functionList) throws Exception ;

	/**
	 * @Title: updateFunctions 
	 * @Description: 修改自定义函数基础信息
	 * @param functionList
	 * @throws Exception    设定文件
	 */
	public void updateFunctions(List<FuncBean> functionList) throws Exception ;
	
	/**
	 * @Title: delFuncParams 
	 * @Description: 删除自定义函数参数信息
	 * @param functionList
	 * @throws Exception    设定文件
	 */
	public void delFuncParams(List<FuncBean> functionList) throws Exception ;
	
	/**
	 * @Title: delFuncParams 
	 * @Description: 新增自定义函数参数信息
	 * @param paramList
	 * @throws Exception    设定文件
	 */
	public void addFuncParams(List<FuncParamsBean> paramList, String funId) throws Exception ;
	
	/**
	 * @Title: addOrUpdateRowitems 
	 * @Description: 新增或更新行指标信息 
	 * @param rowitemList
	 * @param opType
	 * @param accHrcy
	 * @throws Exception    设定文件
	 */
	public void addOrUpdateRowitems(List<ESRowItemBean> rowitemList, String opType, AccHrcy accHrcy) throws Exception ;

	/**
	 * @Title: addOrUpdateColitems 
	 * @Description: 新增或更新列指标信息 
	 * @param colitemList
	 * @param opType
	 * @param accHrcy
	 * @throws Exception    设定文件
	 */
	public void addOrUpdateColitems(List<ESColItemBean> colitemList, String opType, AccHrcy accHrcy) throws Exception ;

	/**
	 * @Title: addFormulas 
	 * @Description: 新增指标公式信息
	 * @param formulaList
	 * @param accHrcy
	 * @throws Exception    设定文件
	 */
	public void addFormulas(List<CalFormulaBean> formulaList, AccHrcy accHrcy) throws Exception ;

	/**
	 * @Title: updateFormulas 
	 * @Description: 修改指标公式信息
	 * @param formulaList
	 * @param accHrcy
	 * @throws Exception    设定文件
	 */
	public void updateFormulas(List<CalFormulaBean> formulaList, AccHrcy accHrcy) throws Exception ;
	
	/**
	 * @Title: addPreFormulas 
	 * @Description: 新增公式预处理信息
	 * @param preFormulaList
	 * @param formulaId
	 * @throws Exception    设定文件
	 */
	public void addPreFormulas(List<PreFormulaBean> preFormulaList, String formulaId) throws Exception ;
	
	/**
	 * @Title: delPreFormulas 
	 * @Description: 删除公式预处理信息
	 * @param formulaList
	 * @throws Exception    设定文件
	 */
	public void delPreFormulas(List<CalFormulaBean> formulaList) throws Exception ;
	
	/**
	 * @Title: addTabs 
	 * @Description: 新增模板基础信息 
	 * @param tabList
	 * @throws Exception    设定文件
	 */
	public void addTabs(List<ESTabBean> tabList, AccHrcy accHrcy) throws Exception ;
	
	/**
	 * @Title: updateTabs 
	 * @Description: 修改模板基础信息
	 * @param tabList
	 * @throws Exception    设定文件
	 */
	public void updateTabs(List<ESTabBean> tabList) throws Exception ;
	
	/**
	 * @Title: addTabCells 
	 * @Description: 新增单元格信息
	 * @param tabCellList
	 * @throws Exception    设定文件
	 */
	public void addTabCells(List<ESTabCellBean> tabCellList, String tabId) throws Exception ;
	
	/**
	 * @Title: addTabElements 
	 * @Description: 新增模板元素信息
	 * @param tabElementList
	 * @throws Exception    设定文件
	 */
	public void addTabElements(List<ESTabElementBean> tabElementList, String tabId) throws Exception ;
	
	/**
	 * @Title: delTabCells 
	 * @Description: 删除模板单元格信息 
	 * @param tabList
	 * @throws Exception    设定文件
	 */
	public void delTabCells(List<ESTabBean> tabList) throws Exception ;
	
	/**
	 * @Title: delTabElements 
	 * @Description: 删除模板元素信息
	 * @param tabList
	 * @throws Exception    设定文件
	 */
	public void delTabElements(List<ESTabBean> tabList) throws Exception ;
	
	/**
	 * @Title: addAndDelRowitemRef 
	 * @Description: 新增和删除行指标引用
	 * @param rowitemsRefList
	 * @param tabId
	 * @throws Exception    设定文件
	 */
	public void addAndDelRowitemRef(List<ESRowItemRefBean> rowitemsRefList,String tabId) throws Exception ;
	
	/**
	 * @Title: addAndDelColitemRef 
	 * @Description: 新增和删除列指标引用 
	 * @param colitemsRefList
	 * @param tabId
	 * @throws Exception    设定文件
	 */
	public void addAndDelColitemRef(List<ESColItemRefBean> colitemsRefList,String tabId) throws Exception ;
	
	/**
	 * @Title: getItemIdByCode 
	 * @Description: 根据指标编码获取指标ID
	 * @param accHrcyId
	 * @param itemCode
	 * @param itemType
	 * @return
	 * @throws Exception    设定文件
	 */
	public String getItemIdByCode(String accHrcyId,String itemCode,String itemType) throws Exception ;
	
}
