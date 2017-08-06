package com.xzsoft.xc.rep.mapper;

import java.util.List;

import com.xzsoft.xc.rep.modal.ESTabElementBean;

/**
 * @ClassName: EnterpriseSheetMapper 
 * @Description: EnterpriseSheet接口处理类
 * @author linp
 * @date 2016年9月1日 上午9:29:08 
 *
 */
public interface ESTabElementMapper {
	
	/**
	 * @Title: delTabElements 
	 * @Description: 删除页签对应的元素信息
	 * @param tabId    设定文件
	 */
	public void delTabElements(String tabId) ;
	
	/**
	 * @Title: batchInsertTabElements 
	 * @Description: 批量插入单元格元素信息
	 * @param list    设定文件
	 */
	public void batchInsertTabElements(List<ESTabElementBean> list) ;
	
	/**
	 * @Title: getTabElements 
	 * @Description: 查询模板元素信息
	 * @param tabId
	 * @return    设定文件
	 */
	public List<ESTabElementBean> getTabElements(String tabId) ;
	
}
