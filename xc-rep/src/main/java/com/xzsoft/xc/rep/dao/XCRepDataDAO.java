package com.xzsoft.xc.rep.dao;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.rep.modal.CellValueBean;
import com.xzsoft.xc.rep.modal.RepSheetBean;

/**
 * @ClassName: XCRepDataDAO 
 * @Description: 报表数据处理数据层接口
 * @author linp
 * @date 2016年8月3日 上午9:39:21 
 *
 */
public interface XCRepDataDAO {
	
	/**
	 * @Title: getSheetBaseInfo 
	 * @Description: 查询报表基础信息
	 * @param ledgerId
	 * @param periodCode
	 * @param accHrcyId
	 * @param tabOrder
	 * @return
	 * @throws Exception    设定文件
	 */
	public HashMap<String,String> getSheetBaseInfo(String ledgerId,String periodCode,String accHrcyId,int tabOrder)throws Exception ;
	
	/**
	 * @Title: getSheetBeanByTabId
	 * @Description: 按模板ID查询单张报表信息
	 * @param map
	 * @return
	 * @throws Exception    设定文件
	 */
	public RepSheetBean getSheetBeanByTabId(RepSheetBean sheetBean)throws Exception ;
	
	/**
	 * @Title: getSheetBeanByTabOrder 
	 * @Description: 模板序号查询单张报表信息
	 * @param sheetBean
	 * @return
	 * @throws Exception    设定文件
	 */
	public RepSheetBean getSheetBeanByTabOrder(RepSheetBean sheetBean)throws Exception ;
	
	/**
	 * @Title: getSheetsById 
	 * @Description: 根据报表ID查询报表基础信息
	 * @param list
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<RepSheetBean> getSheetsByIds(List<String> list) throws Exception ;
	
	/**
	 * @Title: getFixedCellValuesToList 
	 * @Description: 按模板查询固定行指标数据，List格式
	 * @param ledgerId
	 * @param periodCode
	 * @param tabId
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<CellValueBean> getFixedCellValuesToList(String ledgerId,String periodCode,String tabId)throws Exception ;
	
	/**
	 * @Title: getFixedCellValuesToMap 
	 * @Description: 按模板查询固定行指标数据，Map格式
	 * @param ledgerId
	 * @param periodCode
	 * @param tabId
	 * @return
	 * @throws Exception    设定文件
	 */
	public HashMap<String,CellValueBean> getFixedCellValuesToMap(String ledgerId,String periodCode,String tabId)throws Exception ;
	
	/**
	 * @Title: saveOrUpdateRepData 
	 * @Description: 保存或更新报表数据信息
	 * @param sheetBean
	 * @param fixedCellvalues
	 * @throws Exception    设定文件
	 */
	public void saveOrUpdateRepData(RepSheetBean sheetBean, List<CellValueBean> fixedCellvalues) throws Exception ;
	
	/**
	 * @Title: batchUpdRepStatus 
	 * @Description: 批量修改报表状态
	 * @param sheetBean
	 * @param sheetIds
	 * @param opMode
	 * @throws Exception    设定文件
	 */
	public void batchUpdRepStatus(RepSheetBean sheetBean, List<String> sheetIds, String opMode)throws Exception ;

	/**
	 * @Title: cleanReports 
	 * @Description: 清空报表
	 * @param sheetBean
	 * @param sheetIds
	 * @param opMode
	 * @throws Exception    设定文件
	 */
	public void cleanReports(RepSheetBean sheetBean, List<String> sheetIds, String opMode)throws Exception ;
	
	/**
	 * @Title: insertSheet 
	 * @Description: 新增报表基础表信息
	 * @param sheetBean
	 * @throws Exception    设定文件
	 */
	public void insertSheet(RepSheetBean sheetBean) throws Exception ;
	
	/**
	 * @Title: updateSheet 
	 * @Description: 更新报表基础信息
	 * @param sheetBean
	 * @throws Exception    设定文件
	 */
	public void updateSheet(RepSheetBean sheetBean) throws Exception ;
	
	/**
	 * @Title: insertItemValue 
	 * @Description: 新增指标数据
	 * @param bean
	 * @throws Exception    设定文件
	 */
	public void insertItemValue(CellValueBean bean) throws Exception ;
	
	/**
	 * @Title: updateItemValue 
	 * @Description: 更新指标数据
	 * @param bean
	 * @throws Exception    设定文件
	 */
	public void updateItemValue(CellValueBean bean) throws Exception ;
	
	/**
	 * @Title: deleteItemValue 
	 * @Description: 删除一个指标值 
	 * @param bean
	 * @throws Exception    设定文件
	 */
	public void deleteItemValue(CellValueBean bean) throws Exception ;
	
	/**
	 * @Title: getCellValueBean 
	 * @Description: 查询指标值信息
	 * @param bean
	 * @return    设定文件
	 */
	public CellValueBean getCellValueBean(CellValueBean bean) ;
	
}
