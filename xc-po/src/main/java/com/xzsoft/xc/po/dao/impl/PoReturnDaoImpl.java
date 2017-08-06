package com.xzsoft.xc.po.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.po.dao.PoReturnDao;
import com.xzsoft.xc.po.mapper.PoReturnMapper;
import com.xzsoft.xc.po.modal.PoReturnHBean;
import com.xzsoft.xc.po.modal.PoReturnLBean;

/** 
 * @ClassName: PoReturnDaoImpl
 * @Description: 退货单Dao层接口实现类
 * @author weicw
 * @date 2016年12月28日 上午11:03:46 
 * 
 */
@Repository("poReturnDao")
public class PoReturnDaoImpl implements PoReturnDao {
	@Resource
	PoReturnMapper mapper;
	/**
	 * @Title:ifExistH
	 * @Description:判断退货单主信息新增或修改 
	 * 参数格式:
	 * @param  java.lang.String
	 * @return com.xzsoft.xc.po.modal.PoReturnHBean
	 * @throws 
	 */
	@Override
	public PoReturnHBean ifExistH(String id) {
		return mapper.ifExistH(id);
	}
	/**
	 * @Title:addReturnHData
	 * @Description:新增退货单主信息 
	 * 参数格式:
	 * @param  com.xzsoft.xc.po.modal.PoReturnHBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void addReturnHData(PoReturnHBean bean) {
		mapper.addReturnHData(bean);
	}
	/**
	 * @Title:editReturnHData
	 * @Description:修改退货单主信息 
	 * 参数格式:
	 * @param  com.xzsoft.xc.po.modal.PoReturnHBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void editReturnHData(PoReturnHBean bean) {
		mapper.editReturnHData(bean);
	}
	/**
	 * @Title:delReturnHData
	 * @Description:删除退货单主信息 
	 * 参数格式:
	 * @param  java.util.List
	 * @return 
	 * @throws 
	 */
	@Override
	public void delReturnHData(List<String> list) {
		mapper.delReturnHData(list);
	}
	/**
	 * @Title:ifExistL
	 * @Description:判断退货单行信息新增或删除 
	 * 参数格式:
	 * @param  java.lang.String
	 * @return com.xzsoft.xc.po.modal.PoReturnLBean
	 * @throws 
	 */
	@Override
	public PoReturnLBean ifExistL(String id){
		return mapper.ifExistL(id);
	}
	/**
	 * @Title:addReturnLData
	 * @Description:新增退货单行信息 
	 * 参数格式:
	 * @param  com.xzsoft.xc.po.modal.PoReturnLBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void addReturnLData(PoReturnLBean bean) {
		mapper.addReturnLData(bean);
	}
	/**
	 * @Title:editReturnLData
	 * @Description:修改退货单行信息  
	 * 参数格式:
	 * @param  com.xzsoft.xc.po.modal.PoReturnLBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void editReturnLData(PoReturnLBean bean) {
		mapper.editReturnLData(bean);
	}
	/**
	 * @Title:delReturnLData
	 * @Description:删除退货单行信息  
	 * 参数格式:
	 * @param  java.util.List
	 * @return 
	 * @throws 
	 */
	@Override
	public void delReturnLData(List<String> list) {
		mapper.delReturnLData(list);
	}
	/**
	 * @Title:delReturnLDataByHId
	 * @Description:删除退货单行信息(通过退货单主ID)  
	 * 参数格式:
	 * @param  java.util.List
	 * @return 
	 * @throws 
	 */
	@Override
	public void delReturnLDataByHId(List<String> list) {
		mapper.delReturnLDataByHId(list);

	}

}
