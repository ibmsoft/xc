package com.xzsoft.xc.ap.dao;

import java.util.List;

import com.xzsoft.xc.apar.modal.ApDocumentHBean;
/**
 * 
  * @ClassName ApInvGlBaseDao
  * @Description 应付模块应付单处理dao层
  * @author RenJianJian
  * @date 2016年8月8日 下午5:44:01
 */
public interface ApInvGlBaseDao {
	//===================应付单模块
	/**
	 * 
	  * @Title saveApInvGl 方法名
	  * @Description 保存应付单信息
	  * @param apDocumentH
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void saveApInvGl(ApDocumentHBean apDocumentH) throws Exception;
	
	/**
	 * 
	  * @Title updateApInvGl 方法名
	  * @Description 更新应付单信息
	  * @param apDocumentH
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void updateApInvGl(ApDocumentHBean apDocumentH) throws Exception;
	
	/**
	 * 
	  * @Title deleteApInvGlDtl 方法名
	  * @Description 根据应付单行表ID批量删除应付单行表信息
	  * @param dtlLists
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void deleteApInvGlDtl(List<String> dtlLists) throws Exception;
	
	/**
	 * 
	  * @Title deleteApInvGlH 方法名
	  * @Description 批量删除应付单
	  * @param lists
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void deleteApInvGlH(List<String> lists) throws Exception;
	//===================应付单模块
}