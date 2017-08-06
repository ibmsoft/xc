package com.xzsoft.xc.rep.dao;

import java.util.List;

import com.xzsoft.xc.gl.modal.AccHrcy;


/**
 * @ClassName: TabImpAndExpDAO 
 * @Description: 模板导出与接收
 * @author linp
 * @date 2016年10月19日 下午4:06:51 
 *
 */
public interface TabImpAndExpDAO {
	
	/**
	 * @Title: saveOrUpdateFunctions 
	 * @Description: 保存或更新自定义函数信息
	 * @param functions
	 * @throws Exception    设定文件
	 */
	public void saveOrUpdateFunctions(List<Object> functions) throws Exception ;
	
	/**
	 * @Title: saveOrUpdateRowitems 
	 * @Description: 保存或更新行指标信息
	 * @param rowitems
	 * @throws Exception    设定文件
	 */
	public void saveOrUpdateRowitems(List<Object> rowitems,AccHrcy accHrcy) throws Exception ;
	
	/**
	 * @Title: saveOrUpdateColitems 
	 * @Description: 保存或更新列指标信息
	 * @param colitems
	 * @throws Exception    设定文件
	 */
	public void saveOrUpdateColitems(List<Object> colitems,AccHrcy accHrcy) throws Exception ;
	
	/**
	 * @Title: saveOrUpdateFormulas 
	 * @Description: 保存或更新公式信息
	 * @param formulas
	 * @throws Exception    设定文件
	 */
	public void saveOrUpdateFormulas(List<Object> formulas,AccHrcy accHrcy) throws Exception ;
	
	/**
	 * @Title: saveOrUpdateTabs 
	 * @Description: 保存或更新模板信息
	 * @param tabs
	 * @throws Exception    设定文件
	 */
	public void saveOrUpdateTabs(List<Object> tabs,AccHrcy accHrcy) throws Exception ;
	
}
