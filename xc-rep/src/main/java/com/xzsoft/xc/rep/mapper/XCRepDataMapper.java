package com.xzsoft.xc.rep.mapper;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.rep.modal.CellValueBean;
import com.xzsoft.xc.rep.modal.RepSheetBean;

/**
 * @ClassName: XCRepDataMapper 
 * @Description: 报表数据处理Mapper
 * @author linp
 * @date 2016年8月4日 上午10:49:37 
 *
 */
public interface XCRepDataMapper {
	
	/**
	 * @Title: updRepStatus 
	 * @Description: 修改单张报表状态信息
	 * @param map    设定文件
	 */
	public void updRepStatus(RepSheetBean sheetBean) ;
	
	/**
	 * @Title: batchUpdRepStatus 
	 * @Description: 批量修改报表状态信息
	 * @param map    设定文件
	 */
	public void batchUpdRepStatus(HashMap<String,Object> map) ;
	
	/**
	 * @Title: getSheetStatus 
	 * @Description: 查询报表状态信息
	 * @param map
	 * @return    设定文件
	 */
	public HashMap<String,String> getSheetBaseInfo(RepSheetBean sheetBean) ;
	
	/**
	 * @Title: getRepSheetByTabId 
	 * @Description: 按模板ID查询单张报表基础信息
	 * @param map
	 * @return    设定文件
	 */
	public RepSheetBean getRepSheetByTabId(RepSheetBean sheetBean) ;
	
	/**
	 * @Title: getRepSheetByTabOrder 
	 * @Description: 按模板序号查询单张报表基础信息 
	 * @param sheetBean
	 * @return    设定文件
	 */
	public RepSheetBean getRepSheetByTabOrder(RepSheetBean sheetBean) ;
	
	/**
	 * @Title: getSheetsById 
	 * @Description: 根据报表ID查询报表基础信息
	 * @param list
	 * @return    设定文件
	 */
	public List<RepSheetBean> getSheetsByIds(List<String> list) ;
	
	/**
	 * @Title: getFixedCellValuesByTab 
	 * @Description: 按报表查询固定行指标值信息
	 * @param map
	 * @return    设定文件
	 */
	public List<CellValueBean> getFixedCellValuesByTab(HashMap<String,?> map) ;
	
	/**
	 * @Title: insertSheet 
	 * @Description: 创建报表基础信息
	 * @param sheetBean    设定文件
	 */
	public void insertSheet(RepSheetBean sheetBean)  ;
	
	/**
	 * @Title: updateSheet 
	 * @Description: 更新报表基础信息
	 * @param sheetBean    设定文件
	 */
	public void updateSheet(RepSheetBean sheetBean) ;
	
	/**
	 * @Title: deleteCellValues 
	 * @Description: 删除报表指标数据信息
	 * @param sheetBean    设定文件
	 */
	public void deleteCellValues(RepSheetBean sheetBean) ;
	
	/**
	 * @Title: insertCellValue 
	 * @Description: 新增指标数据
	 * @param bean    设定文件
	 */
	public void insertCellValue(CellValueBean bean) ;
	
	/**
	 * @Title: updateCellValue 
	 * @Description: 更新指标数据
	 * @param bean    设定文件
	 */
	public void updateCellValue(CellValueBean bean) ;
	
	/**
	 * @Title: deleteCellValue 
	 * @Description: 删除单个指标值
	 * @param bean    设定文件
	 */
	public void deleteCellValue(CellValueBean bean) ;
	
	/**
	 * @Title: getCellValueBean 
	 * @Description: 获取指标信息信息
	 * @param bean
	 * @return    设定文件
	 */
	public CellValueBean getCellValueBean(CellValueBean bean) ;

}
