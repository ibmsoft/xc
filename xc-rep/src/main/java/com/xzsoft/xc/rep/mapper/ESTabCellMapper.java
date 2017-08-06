package com.xzsoft.xc.rep.mapper;

import java.util.List;

import com.xzsoft.xc.rep.modal.ESTabCellBean;

/**
 * @ClassName: ESTabCellMapper 
 * @Description: EnterpriseSheet Cell接口处理类
 * @author linp
 * @date 2016年9月1日 上午9:29:08 
 *
 */
public interface ESTabCellMapper {
	
	/**
	 * @Title: delTabCells 
	 * @Description: 删除页签对应的单元格信息
	 * @param tabId    设定文件
	 */
	public void delTabCells(String tabId) ;
	
	/**
	 * @Title: batchInsertTabCells 
	 * @Description: 批量插入单元格信息
	 * @param list    设定文件
	 */
	public void batchInsertTabCells(List<ESTabCellBean> list) ;
	
	/**
	 * @Title: getTabCells 
	 * @Description: 查询模板单元格信息
	 * @param tabId
	 * @return    设定文件
	 */
	public List<ESTabCellBean> getTabCells(String tabId) ;
	
}
