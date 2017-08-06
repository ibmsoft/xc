package com.xzsoft.xc.ap.mapper;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.apar.modal.ApDocumentHBean;
/**
 * 
  * @ClassName ApInvGlBaseMapper
  * @Description 应付模块应付单保存的mapper层
  * @author RenJianJian
  * @date 2016年8月8日 下午5:40:55
 */
public interface ApInvGlBaseMapper {
	//===================应付单模块
	/**
	 * 
	  * @Title saveApInvGlH 方法名
	  * @Description 保存应付单主信息
	  * @param apDocumentH
	  * @return void 返回类型
	 */
	public void saveApInvGlH(ApDocumentHBean apDocumentH);
	
	/**
	 * 
	  * @Title saveApInvGlDtl 方法名
	  * @Description 保存应付单行信息
	  * @param map
	  * @return void 返回类型
	 */
	public void saveApInvGlDtl(HashMap<String,Object> map);
	
	/**
	 * 
	  * @Title updateApInvGlH 方法名
	  * @Description 更新应付单主信息
	  * @param apDocumentH
	  * @return void 返回类型
	 */
	public void updateApInvGlH(ApDocumentHBean apDocumentH);
	
	/**
	 * 
	  * @Title updateApInvGlDtl 方法名
	  * @Description 更新应付单行信息
	  * @param map
	  * @return void 返回类型
	 */
	public void updateApInvGlDtl(HashMap<String,Object> map);
	
	/**
	 * 
	  * @Title deleteApInvGlHDtls 方法名
	  * @Description 根据应付单主表ID批量删除应付单行表信息
	  * @param lists
	  * @return void 返回类型
	 */
	public void deleteApInvGlHDtls(List<String> lists);
	
	/**
	 * 
	  * @Title deleteApInvGlDtl 方法名
	  * @Description 根据应付单行表ID批量删除应付单行表信息
	  * @param dtlLists
	  * @return void 返回类型
	 */
	public void deleteApInvGlDtl(List<String> dtlLists);
	
	/**
	 * 
	  * @Title deleteApInvGlH 方法名
	  * @Description 批量删除应付单主表信息
	  * @param lists
	  * @return void 返回类型
	 */
	public void deleteApInvGlH(List<String> lists);
	//===================应付单模块
}
