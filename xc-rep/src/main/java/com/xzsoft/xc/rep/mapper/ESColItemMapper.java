package com.xzsoft.xc.rep.mapper;

import java.util.List;

import com.xzsoft.xc.rep.modal.ESColItemBean;
import com.xzsoft.xc.rep.modal.ESColItemRefBean;
import com.xzsoft.xc.rep.modal.ESTabBean;

/**
 * @ClassName: ESColItemMapper 
 * @Description: EnterpriseSheet 列指标处理Mapper
 * @author linp
 * @date 2016年9月1日 上午9:29:08 
 *
 */
public interface ESColItemMapper {
	
	/**
	 * @Title: delColItemRef 
	 * @Description: 根据模板ID删除模板列指标引用关系
	 * @param tabId    设定文件
	 */
	public void delColItemRef(String tabId) ;
	
	/**
	 * @Title: getColItemsByHrcyId 
	 * @Description: 查询科目体系下所有的列指标信息
	 * @param accHrcyId
	 * @return    设定文件
	 */
	public List<ESColItemBean> getColItemsByHrcyId(String accHrcyId) ;
	
	/**
	 * @Title: getColItemsAndRefByTab 
	 * @Description: 查询模板列指标和引用关系 
	 * @param tabId
	 * @return    设定文件
	 */
	public ESTabBean getColItemsAndRefByTab(String tabId) ;
	
	/**
	 * @Title: getTabColitems 
	 * @Description: 查询模板列指标信息
	 * @param tabId
	 * @return    设定文件
	 */
	public List<ESColItemBean> getTabColitems(String tabId) ;
	
	/**
	 * @Title: getTabColitemsRef 
	 * @Description: 查询列指标引用关系
	 * @param tabId
	 * @return    设定文件
	 */
	public List<ESColItemRefBean> getTabColitemsRef(String tabId) ;
	
	/**
	 * @Title: getColItemsAndRefByTabs 
	 * @Description: 批量查询模板列指标和引用关系 
	 * @param tabId
	 * @return    设定文件
	 */
	public List<ESTabBean> getColItemsAndRefByTabs(List<String> tabs) ;
}
