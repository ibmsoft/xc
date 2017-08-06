package com.xzsoft.xc.po.dao;

import java.util.List;

import com.xzsoft.xc.po.modal.PoArrivalHBean;
import com.xzsoft.xc.po.modal.PoArrivalLBean;

/** 
 * @ClassName: PoArrivalDao
 * @Description: 到货单Dao层
 * @author weicw
 * @date 2016年12月22日 下午1:46:08 
 * 
 */
public interface PoArrivalDao {
	/**
	 * 
	  * @Title ifExistH 方法名
	  * @Description 判断到货单主表信息新增或修改
	  * @param java.lang.String
	  * @return com.xzsoft.xc.po.modal.PoArrivalHBean
	 */
	public PoArrivalHBean ifExistH(String id);
	/**
	 * 
	  * @Title addPoArrivalHInfo 方法名
	  * @Description 新增到货单主表信息
	  * @param com.xzsoft.xc.po.modal.PoArrivalHBean
	  * @return 
	 */
	public void addPoArrivalHInfo(PoArrivalHBean bean);
	/**
	 * 
	  * @Title editPoArrivalHInfo 方法名
	  * @Description 修改到货单主表信息
	  * @param com.xzsoft.xc.po.modal.PoArrivalHBean
	  * @return 
	 */
	public void editPoArrivalHInfo(PoArrivalHBean bean);
	/**
	 * 
	  * @Title deletePoArrivalHInfo 方法名
	  * @Description 删除到货单主表信息 
	  * @param java.util.List
	  * @return 
	 */
	public void deletePoArrivalHInfo(List<String> ids);
	/**
	 * 
	  * @Title ifExistL 方法名
	  * @Description 判断到货单行表信息新增或删除 
	  * @param java.lang.String
	  * @return com.xzsoft.xc.po.modal.PoArrivalLBean
	 */
	public PoArrivalLBean ifExistL(String id);
	/**
	 * 
	  * @Title addPoArrivalLInfo 方法名
	  * @Description 新增到货单行表信息 
	  * @param com.xzsoft.xc.po.modal.PoArrivalLBean
	  * @return 
	 */
	public void addPoArrivalLInfo(PoArrivalLBean bean);
	/**
	 * 
	  * @Title editPoArrivalLInfo 方法名
	  * @Description 修改到货单行表信息 
	  * @param com.xzsoft.xc.po.modal.PoArrivalLBean
	  * @return 
	 */
	public void editPoArrivalLInfo(PoArrivalLBean bean);
	/**
	 * 
	  * @Title deletePoArrivalLInfo 方法名
	  * @Description 删除到货单行表信息 
	  * @param java.util.List
	  * @return 
	 */
	public void deletePoArrivalLInfo(List<String> ids);
	/**
	 * 
	  * @Title deletePoArrivalLInfoByHId 方法名
	  * @Description 删除到货单行表信息(通过主表ID)
	  * @param java.util.List
	  * @return 
	 */
	public void deletePoArrivalLInfoByHId(List<String> ids);
}
