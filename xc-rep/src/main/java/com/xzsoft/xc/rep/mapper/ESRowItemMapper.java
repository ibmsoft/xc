package com.xzsoft.xc.rep.mapper;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.rep.modal.ESRowItemBean;
import com.xzsoft.xc.rep.modal.ESRowItemRefBean;
import com.xzsoft.xc.rep.modal.ESTabBean;

/**
 * @ClassName: ESRowItemMapper 
 * @Description: EnterpriseSheet Tab 行指标处理Mapper
 * @author linp
 * @date 2016年9月1日 上午9:29:08 
 *
 */
public interface ESRowItemMapper {
	
	/**
	 * @Title: delRowItemRef 
	 * @Description: 根据模板ID删除模板行指标引用关系
	 * @param tabId    设定文件
	 */
	public void delRowItemRef(String tabId)  ;
	
	/**
	 * @Title: delRowItemRefByLanno 
	 * @Description: 按模板及栏次删除行指标引用关系
	 * @param tabId
	 * @param lanno    设定文件
	 */
	public void delRowItemRefByLanno(HashMap<String,?> map) ;
	
	/**
	 * @Title: getRowItems 
	 * @Description: 查询科目体系下所有的行指标信息
	 * @param map
	 * @return    设定文件
	 */
	public List<ESRowItemBean> getRowItemsByHrcyId(String accHrcyId) ;
	
	/**
	 * @Title: getRowItemsAndRefByTab 
	 * @Description: 按模板查询行指标和行指标引用关系
	 * @param tabId
	 * @return    设定文件
	 */
	public ESTabBean getRowItemsAndRefByTab(String tabId) ;
	
	/**
	 * @Title: getTabRowitems 
	 * @Description: 查询模板行指标信息 
	 * @param tabId
	 * @return    设定文件
	 */
	public List<ESRowItemBean> getTabRowitems(String tabId) ;
	
	/**
	 * @Title: getTabRowitemsRef 
	 * @Description: 查询模板行指标引用关系
	 * @param tabId
	 * @return    设定文件
	 */
	public List<ESRowItemRefBean> getTabRowitemsRef(String tabId) ;
	
	/**
	 * @Title: getRowItemsAndRefByTabs 
	 * @Description: 批量查询模板行指标引用关系
	 * @param tabs
	 * @return    设定文件
	 */
	public List<ESTabBean> getRowItemsAndRefByTabs(List<String> tabs) ;
	
}
