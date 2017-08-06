package com.xzsoft.xc.po.dao.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.po.dao.PoPurApHDao;
import com.xzsoft.xc.po.mapper.PoPurApHMapper;
import com.xzsoft.xc.po.modal.PoPurApHBean;
import com.xzsoft.xc.po.modal.PoPurApLBean;

/** 
 * @ClassName: 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author weicw
 * @date 2016年12月5日 上午10:53:05 
 * 
 */
@Repository("poPurApHDao")
public class PoPurApHDaoImpl implements PoPurApHDao {
	@Resource
	PoPurApHMapper mapper;
	/**
	 * @Title:ifExistH
	 * @Description:判断采购申主表为新增或删除 
	 * 参数格式:
	 * @param  java.lang.String
	 * @return 
	 * @throws 
	 */
	@Override
	public PoPurApHBean ifExistH(String id){
		return mapper.ifExistH(id);
	}
	/**
	 * @Title:addPurHInfo
	 * @Description:添加采购申请主表信息
	 * 参数格式:
	 * @param  com.xzsoft.xc.po.modal.PoPurApHBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void addPurHInfo(PoPurApHBean bean){
		mapper.addPurHInfo(bean);
	}
	/**
	 * @Title:editPurHInfo
	 * @Description:修改采购申请主表信息
	 * 参数格式:
	 * @param  com.xzsoft.xc.po.modal.PoPurApHBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void editPurHInfo(PoPurApHBean bean){
		mapper.editPurHInfo(bean);
	}
	/**
	 * @Title:deletePurHInfo
	 * @Description:删除采购申请主表信息 
	 * 参数格式:
	 * @param  java.util.List
	 * @return 
	 * @throws 
	 */
	@Override
	public void deletePurHInfo(List<String> list){
		mapper.deletePurHInfo(list);
	}
	/**
	 * @Title:addPurLInfo
	 * @Description:添加采购申请行表信息
	 * 参数格式:
	 * @param  com.xzsoft.xc.po.modal.PoPurApLBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void addPurLInfo(PoPurApLBean bean){
		mapper.addPurLInfo(bean);
	}
	/**
	 * @Title:editPurLInfo
	 * @Description:修改采购申请单行表信息
	 * 参数格式:
	 * @param  com.xzsoft.xc.po.modal.PoPurApLBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void editPurLInfo(PoPurApLBean bean){
		mapper.editPurLInfo(bean);
	}
	/**
	 * @Title:deletePurLInfo
	 * @Description:删除采购申请行表信息
	 * 参数格式:
	 * @param  java.util.List
	 * @return 
	 * @throws 
	 */
	@Override
	public void deletePurLInfo(List<String> list){
		mapper.deletePurLInfo(list);
	}
	/**
	 * @Title:deletePurLInfoByHId
	 * @Description:删除采购申请行表信息（通过采购申请主ID）
	 * 参数格式:
	 * @param  java.util.List
	 * @return 
	 * @throws 
	 */
	@Override
	public void deletePurLInfoByHId(List<String> list){
		mapper.deletePurLInfoByHId(list);
	}
	/**
	 * @Title:ifExistL
	 * @Description:判断采购申请行信息新增或修改 
	 * 参数格式:
	 * @param  java.lang.String
	 * @return 
	 * @throws 
	 */
	@Override
	public PoPurApLBean ifExistL(String id){
		return mapper.ifExistL(id);
	}
	/**
	 * @Title:editPurIsClose
	 * @Description:关闭或打开采购申请单 
	 * 参数格式:
	 * @param  java.util.HashMap
	 * @return 
	 * @throws 
	 */
	@Override
	public void editPurIsClose(HashMap<String, Object> map){
		mapper.editPurIsClose(map);
	}

}
