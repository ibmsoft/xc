package com.xzsoft.xc.rep.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xzsoft.xc.rep.modal.ESColItemBean;
import com.xzsoft.xc.rep.modal.ESColItemRefBean;
import com.xzsoft.xc.rep.modal.ESRowItemBean;
import com.xzsoft.xc.rep.modal.ESRowItemRefBean;
import com.xzsoft.xc.rep.modal.ESTabBean;
import com.xzsoft.xc.rep.modal.ESTabCellBean;
import com.xzsoft.xc.rep.modal.ESTabElementBean;

/**
 * @ClassName: EnterpriseSheetDAO 
 * @Description: EnterpriseSheet 数据处理层接口
 * @author linp
 * @date 2016年9月1日 上午9:18:30 
 *
 */
public interface EnterpriseSheetDAO {

	/**
	 * @Title: getAllESTabs 
	 * @Description: 根据科目体系下模板信息
	 * @param accHrcyId 必需项
	 * @param tabOrder 可选项，如果值为-1则查询全部，如果为0或正整数则查询指定的报表
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<ESTabBean> getAllESTabs(String accHrcyId, int tabOrder) throws Exception ;
	
	/**
	 * @Title: getESTabIncludeFormat 
	 * @Description: 按照模板ID查询模板格式信息
	 * @param tabId
	 * @return
	 * @throws Exception    设定文件
	 */
	public ESTabBean getESTabIncludeFormat(String tabId) throws Exception ;
	
	/**
	 * @Title: getAllESTabsExcludeComment 
	 * @Description: 根据科目体系下模板信息,并去除相关批注信息
	 * @param accHrcyId 必需项
	 * @param tabOrder 可选项，如果值为-1则查询全部，如果为0或正整数则查询指定的报表
	 * @return
	 * @throws Exception    设定文件
	 */
	public HashMap<String,Object> getAllESTabsExcludeComment(String accHrcyId, int tabOrder) throws Exception ;
	
	/**
	 * @Title: getAllESTabBeans 
	 * @Description: 查询科目体系下所有的模板基础信息
	 * @param accHrcyId
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<ESTabBean> getAllESTabBeans(String accHrcyId) throws Exception ;
	
	/**
	 * @Title: getAllESTabBeans4Map 
	 * @Description: 查询科目体系下所有的模板基础信息
	 * @param accHrcyId
	 * @return
	 * @throws Exception    设定文件
	 */
	public Map<String,ESTabBean> getAllESTabBeans4Map(String accHrcyId) throws Exception ;
	
	/**
	 * @Title: getNextTabOrder 
	 * @Description: 获取下一个模板页签序号
	 * @param accHrcyId
	 * @return
	 * @throws Exception    设定文件
	 */
	public int getNextTabOrder(String accHrcyId) throws Exception ;
	
	/**
	 * @Title: getMaxTabOrder 
	 * @Description: 获取模板下最大页签序号
	 * @param accHrcyId
	 * @return
	 * @throws Exception    设定文件
	 */
	public int getMaxTabOrder(String accHrcyId) throws Exception ;
	
	/**
	 * @Title: isExistsTabCode 
	 * @Description: 页签编码是否已存在
	 * @param accHrcyId
	 * @param tabCode
	 * @return
	 * @throws Exception    设定文件
	 */
	public boolean isExistsTabCode(String accHrcyId, String tabCode) throws Exception ;
	
	/**
	 * @Title: isExistsTabName 
	 * @Description: 页签名称是否已存在
	 * @param accHrcyId
	 * @param tabName
	 * @return
	 * @throws Exception    设定文件
	 */
	public boolean isExistsTabName(String accHrcyId, String tabName) throws Exception ;
	
	/**
	 * @Title: getESTabBeanByOrder 
	 * @Description: 按tabOrder查询ESTabBean
	 * @param accHrcyId
	 * @param tabOrder
	 * @return
	 * @throws Exception    设定文件
	 */
	public ESTabBean getESTabBeanByOrder(String accHrcyId, int tabOrder) throws Exception ;
	
	/**
	 * @Title: getESTabBeanById 
	 * @Description: 按模板ID查询模板基础信息
	 * @param tabId
	 * @return
	 * @throws Exception    设定文件
	 */
	public ESTabBean getESTabBeanById(String tabId) throws Exception ; 
	
	/**
	 * @Title: saveTabSheet 
	 * @Description: 保存页签信息
	 * @param tabBean
	 * @throws Exception    设定文件
	 */
	public void saveTabSheet(ESTabBean tabBean) throws Exception ;
	
	/**
	 * @Title: beforeDelCheck 
	 * @Description: 
	 * @param tabBean
	 * @return
	 * @throws Exception    设定文件
	 */
	public void beforeDelCheck(ESTabBean tabBean) throws Exception ;
	
	/**
	 * @Title: delTabSheet 
	 * @Description: 删除模板信息
	 * @param tabId
	 * @throws Exception    设定文件
	 */
	public void delTabSheet(ESTabBean tabBean) throws Exception ;
	
	/**
	 * @Title: saveTabFormat 
	 * @Description: 保存模板格式信息
	 * @param tabs
	 * @param cells
	 * @param elements
	 * @throws Exception    设定文件
	 */
	public void saveTabFormat(String opMode, ESTabBean tabBean, List<ESTabBean> tabs, List<ESTabCellBean> cells, List<ESTabElementBean> elements) throws Exception ;
	
	/**
	 * @Title: getRowItemsByHrcyId 
	 * @Description: 查询模板行指标信息
	 * @param accHrcyId
	 * @param tabOrder
	 * @return
	 * @throws Exception    设定文件
	 */
	public Map<String,ESRowItemBean> getRowItemsByHrcyId(String accHrcyId) throws Exception ;
	
	/**
	 * @Title: saveRowItemAndRefInfo 
	 * @Description: 保存模板行指标及引用关系
	 * @param rowitemList
	 * @param rowitemRefList
	 * @param currentLanNo
	 * @throws Exception    设定文件
	 */
	public void saveRowItemAndRefInfo(ESTabBean tabBean,List<ESRowItemBean> rowitemList,List<ESRowItemRefBean> rowitemRefList,int currentLanNo) throws Exception ;
	
	/**
	 * @Title: getColItemsByHrcyId 
	 * @Description: 查询模板列指标信息
	 * @param accHrcyId
	 * @return
	 * @throws Exception    设定文件
	 */
	public Map<String,ESColItemBean> getColItemsByHrcyId(String accHrcyId) throws Exception ;
	
	/**
	 * @Title: saveColItemAndRefInfo 
	 * @Description: 保存模板列指标及引用关系
	 * @param tabBean
	 * @param colitemList
	 * @param colitemRefList
	 * @throws Exception    设定文件
	 */
	public void saveColItemAndRefInfo(ESTabBean tabBean,List<ESColItemBean> colitemList,List<ESColItemRefBean> colitemRefList) throws Exception ;

	/**
	 * @Title: isExistsTemplates 
	 * @Description: 模板是否存在
	 * @param targetHrcyId
	 * @param tabCode
	 * @param tabName
	 * @throws Exception    设定文件
	 */
	public void isExistsTemplates(String targetHrcyId, String tabCode, String tabName) throws Exception ;
	
	/**
	 * @Title: copyTemplate 
	 * @Description: 复制单个模板的格式信息
	 * @param srcHrcyId		源科目体系ID
	 * @param targetHrcyId	目标科目体系ID
	 * @param tabId			模板ID
	 * @throws Exception    设定文件
	 */
	public void copyTemplate(String srcHrcyId, String targetHrcyId, String tabId) throws Exception ;
	
	/**
	 * @Title: getESTabBeanByTabs 
	 * @Description: 按模板ID查询模板及行列指标引用关系
	 * @param accHrcyId
	 * @param tabs
	 * @throws Exception    设定文件
	 */
	public List<ESTabBean> getESTabBeanByTabs(String accHrcyId, List<String> tabs) throws Exception ;
	
}
