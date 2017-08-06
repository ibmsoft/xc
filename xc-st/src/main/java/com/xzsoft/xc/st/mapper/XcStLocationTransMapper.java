package com.xzsoft.xc.st.mapper;

import com.xzsoft.xc.st.modal.Dto;


/**
 * @ClassName: XcStTransMapper 
 * @Description: 货位变动事务处理记录
 * @author panghd
 * @date 2017年1月16日 下午15:15:43 
 *
 */
public interface XcStLocationTransMapper {
	
	/**
	 * @Title: xc_st_trans_select_by_key 
	 * @Description: 根据主键查询对应的货位变动事务处理记录
	 * @param pDto
	 * @return    单条货位变动事务处理记录
	 */
	public Dto xc_st_location_trans_select_by_key(Dto pDto) ;
	
	/**
	 * @Title: xc_st_location_trans_insert 
	 * @Description: 保存货位变动事务处理记录
	 * @param bean    设定文件
	 */
	public void xc_st_location_trans_insert(Dto pDto) ;
	
	/**
	 * @Title: xc_st_location_trans_update 
	 * @Description: 修改货位变动事务处理记录
	 * @param bean    设定文件
	 */
	public void xc_st_location_trans_update(Dto pDto) ;
	
	/**
	 * @Title: xc_st_location_trans_deleteByKey 
	 * @Description: 根据货位变动存事务处理记录主键删除记录
	 * @param bean    设定文件
	 */
	public void xc_st_location_trans_deleteByKey(Dto pDto) ;	
	
	/**
	 * @Title: xc_st_location_trans_delete_by_entry_h_id 
	 * @Description: 根据业务主ID删除货位变动存事务处理记录
	 * @param bean    设定文件
	 */
	public void xc_st_location_trans_delete_by_business_h_id(Dto pDto) ;
	
	/**
	 * @Title: xc_st_location_trans_delete_by_entry_l_id 
	 * @Description: 根据业务行ID删除货位变动存事务处理记录
	 * @param bean    设定文件
	 */
	public void xc_st_location_trans_delete_by_business_l_id(Dto pDto) ;
}
