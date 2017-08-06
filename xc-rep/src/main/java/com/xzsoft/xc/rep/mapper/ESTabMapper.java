package com.xzsoft.xc.rep.mapper;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.rep.modal.ESTabBean;

/**
 * @ClassName: EnterpriseSheetMapper 
 * @Description: EnterpriseSheet接口处理类
 * @author linp
 * @date 2016年9月1日 上午9:29:08 
 *
 */
public interface ESTabMapper {

	/**
	 * @Title: getAllESTabs 
	 * @Description: 根据科目体系ID查询所有模板信息
	 * @param hrcyId
	 * @return    设定文件
	 */
	public List<ESTabBean> getAllESTabs(HashMap<String,?> map) ;
	
	/**
	 * @Title: getESTabIncFormat 
	 * @Description: 按照模板ID查询模板格式信息
	 * @param tabId
	 * @return    设定文件
	 */
	public ESTabBean getESTabIncFormat(String tabId) ;
	
	/**
	 * @Title: getESTabIncFormatByTabs 
	 * @Description: 按照模板ID查询模板格式
	 * @param map
	 * @return    设定文件
	 */
	public List<ESTabBean> getESTabIncFormatByTabs(List<String> tabs) ;
	
	/**
	 * @Title: getAllESTabBeans 
	 * @Description: 查询所有的模板基础信息
	 * @param hrcyId
	 * @return    设定文件
	 */
	public List<ESTabBean> getAllESTabBeans(String accHrcyId) ;
	
	/**
	 * @Title: getESTab4ActivedOrMinOrder 
	 * @Description: 查询最小序号或活动的报表页签信息
	 * @param accHrcyId
	 * @return    设定文件
	 */
	public List<ESTabBean> getESTab4ActivedOrMinOrder(String accHrcyId) ;
	
	/**
	 * @Title: getMaxTabOrder 
	 * @Description: 查询报表模板最大顺序号
	 * @param hrcyId
	 * @return    设定文件
	 */
	public int getMaxTabOrder(String hrcyId) ;
	
	/**
	 * @Title: getMinTabOrder 
	 * @Description: 查询报表模板最小顺序号
	 * @param hrcyId
	 * @return    设定文件
	 */
	public int getMinTabOrder(String hrcyId) ;
	
	/**
	 * @Title: getTabById 
	 * @Description: 按TabId查询ESTabBean
	 * @param tabId
	 * @return    设定文件
	 */
	public ESTabBean getTabById(String tabId) ;
	
	/**
	 * @Title: getTabByOrder 
	 * @Description: 按TabOrder查询ESTabBean
	 * @param map
	 * @return    设定文件
	 */
	public ESTabBean getTabByOrder(HashMap<String,?> map) ;
	
	/**
	 * @Title: getTabByCode 
	 * @Description: 按TabCode查询ESTabBean
	 * @param map
	 * @return    设定文件
	 */
	public ESTabBean getTabByCode(HashMap<String,String> map) ;
	
	/**
	 * @Title: getTabByName 
	 * @Description: 按TabName查询ESTabBean
	 * @param map
	 * @return    设定文件
	 */
	public ESTabBean getTabByName(HashMap<String,String> map) ;
	
	/**
	 * @Title: insertTab 
	 * @Description: 保存页签基础信息
	 * @param bean    设定文件
	 */
	public void insertTab(ESTabBean bean) ;
	
	/**
	 * @Title: getSheetCount 
	 * @Description: 查询模板所关联的报表数量
	 * @param tabId
	 * @return    设定文件
	 */
	public int getSheetCount(String tabId) ;
	
	/**
	 * @Title: getFormulaCount 
	 * @Description: 查询模板所有关联的公式数量
	 * @param map
	 * @return    设定文件
	 */
	public int getFormulaCount(HashMap<String,?> map) ;
	
	/**
	 * @Title: delTabSheet 
	 * @Description: 删除页签基础信息
	 * @param tabId    设定文件
	 */
	public void delTabSheet(String tabId) ;
	
	/**
	 * @Title: updateTabSheet 
	 * @Description: 更新页签基础信息
	 * @param tabBean    设定文件
	 */
	public void updateTabSheet(ESTabBean tabBean) ;
	
	/**
	 * @Title: getSheetCountByCodeOrName 
	 * @Description: 按编码或名称判断是否存在
	 * @param map
	 * @return    设定文件
	 */
	public int getSheetCountByCodeOrName(HashMap<String,?> map) ;
	
	/**
	 * @Title: copyTab 
	 * @Description: 复制模板基础信息
	 * @param tabBean    设定文件
	 */
	public void copyTab(ESTabBean tabBean) ;
	
}
