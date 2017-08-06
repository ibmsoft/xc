package com.xzsoft.xc.po.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.po.dao.PoArrivalDao;
import com.xzsoft.xc.po.mapper.PoArrivalMapper;
import com.xzsoft.xc.po.modal.PoArrivalHBean;
import com.xzsoft.xc.po.modal.PoArrivalLBean;

/** 
 * @ClassName: PoArrivalDaoImpl
 * @Description: 到货单Dao层实现类
 * @author weicw
 * @date 2016年12月22日 下午1:47:29 
 * 
 */
@Repository("poArrivalDao")
public class PoArrivalDaoImpl implements PoArrivalDao{
	@Resource
	PoArrivalMapper mapper;
	/**
	 * 
	  * @Title ifExistH 方法名
	  * @Description 判断到货单主表信息新增或修改
	  * @param java.lang.String
	  * @return com.xzsoft.xc.po.modal.PoArrivalHBean
	 */
	@Override
	public PoArrivalHBean ifExistH(String id){
		return mapper.ifExistH(id);
	}
	/**
	 * 
	  * @Title addPoArrivalHInfo 方法名
	  * @Description 新增到货单主表信息
	  * @param com.xzsoft.xc.po.modal.PoArrivalHBean
	  * @return 
	 */
	@Override
	public void addPoArrivalHInfo(PoArrivalHBean bean){
		mapper.addPoArrivalHInfo(bean);
	}
	/**
	 * 
	  * @Title editPoArrivalHInfo 方法名
	  * @Description 修改到货单主表信息
	  * @param com.xzsoft.xc.po.modal.PoArrivalHBean
	  * @return 
	 */
	@Override
	public void editPoArrivalHInfo(PoArrivalHBean bean){
		mapper.editPoArrivalHInfo(bean);
	}
	/**
	 * 
	  * @Title deletePoArrivalHInfo 方法名
	  * @Description 删除到货单主表信息 
	  * @param java.util.List
	  * @return 
	 */
	@Override
	public void deletePoArrivalHInfo(List<String> ids){
		mapper.deletePoArrivalHInfo(ids);
	}
	/**
	 * 
	  * @Title ifExistL 方法名
	  * @Description 判断到货单行表信息新增或删除 
	  * @param java.lang.String
	  * @return com.xzsoft.xc.po.modal.PoArrivalLBean
	 */
	@Override
	public PoArrivalLBean ifExistL(String id){
		return mapper.ifExistL(id);
	}
	/**
	 * 
	  * @Title addPoArrivalLInfo 方法名
	  * @Description 新增到货单行表信息 
	  * @param com.xzsoft.xc.po.modal.PoArrivalLBean
	  * @return 
	 */
	@Override
	public void addPoArrivalLInfo(PoArrivalLBean bean){
		mapper.addPoArrivalLInfo(bean);
	}
	/**
	 * 
	  * @Title editPoArrivalLInfo 方法名
	  * @Description 修改到货单行表信息 
	  * @param com.xzsoft.xc.po.modal.PoArrivalLBean
	  * @return 
	 */
	@Override
	public void editPoArrivalLInfo(PoArrivalLBean bean){
		mapper.editPoArrivalLInfo(bean);
	}
	/**
	 * 
	  * @Title deletePoArrivalLInfo 方法名
	  * @Description 删除到货单行表信息 
	  * @param java.util.List
	  * @return 
	 */
	@Override
	public void deletePoArrivalLInfo(List<String> ids){
		mapper.deletePoArrivalLInfo(ids);
	}
	/**
	 * 
	  * @Title deletePoArrivalLInfoByHId 方法名
	  * @Description 删除到货单行表信息(通过主表ID)
	  * @param java.util.List
	  * @return 
	 */
	@Override
	public void deletePoArrivalLInfoByHId(List<String> ids){
		mapper.deletePoArrivalLInfoByHId(ids);
	}
}
