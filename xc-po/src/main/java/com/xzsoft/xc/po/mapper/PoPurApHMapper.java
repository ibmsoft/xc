package com.xzsoft.xc.po.mapper;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.po.modal.PoPurApHBean;
import com.xzsoft.xc.po.modal.PoPurApLBean;

/** 
 * @ClassName: PoPurApHMapper
 * @Description: 采购申请Mapper 
 * @author weicw
 * @date 2016年12月5日 上午10:40:18 
 * 
 */
public interface PoPurApHMapper {
	/**
	 * @Title:ifExistH
	 * @Description:判断采购申主表为新增或删除 
	 * 参数格式:
	 * @param  java.lang.String
	 * @return 
	 * @throws 
	 */
	public PoPurApHBean ifExistH(String id);
	/**
	 * @Title:addPurHInfo
	 * @Description:添加采购申请主表信息
	 * 参数格式:
	 * @param  com.xzsoft.xc.po.modal.PoPurApHBean
	 * @return 
	 * @throws 
	 */
	public void addPurHInfo(PoPurApHBean bean);
	/**
	 * @Title:editPurHInfo
	 * @Description:修改采购申请主表信息
	 * 参数格式:
	 * @param  com.xzsoft.xc.po.modal.PoPurApHBean
	 * @return 
	 * @throws 
	 */
	public void editPurHInfo(PoPurApHBean bean);
	/**
	 * @Title:deletePurHInfo
	 * @Description:删除采购申请主表信息 
	 * 参数格式:
	 * @param  java.util.List
	 * @return 
	 * @throws 
	 */
	public void deletePurHInfo(List<String> list);
	/**
	 * @Title:addPurLInfo
	 * @Description:添加采购申请行表信息
	 * 参数格式:
	 * @param  com.xzsoft.xc.po.modal.PoPurApLBean
	 * @return 
	 * @throws 
	 */
	public void addPurLInfo(PoPurApLBean bean);
	/**
	 * @Title:editPurLInfo
	 * @Description:修改采购申请单行表信息
	 * 参数格式:
	 * @param  com.xzsoft.xc.po.modal.PoPurApLBean
	 * @return 
	 * @throws 
	 */
	public void editPurLInfo(PoPurApLBean bean);
	/**
	 * @Title:deletePurLInfo
	 * @Description:删除采购申请行表信息
	 * 参数格式:
	 * @param  java.util.List
	 * @return 
	 * @throws 
	 */
	public void deletePurLInfo(List<String> list);
	/**
	 * @Title:deletePurLInfoByHId
	 * @Description:删除采购申请行表信息（通过采购申请主ID）
	 * 参数格式:
	 * @param  java.util.List
	 * @return 
	 * @throws 
	 */
	public void deletePurLInfoByHId(List<String> list);
	/**
	 * @Title:ifExistL
	 * @Description:判断采购申请行信息新增或修改 
	 * 参数格式:
	 * @param  java.lang.String
	 * @return 
	 * @throws 
	 */
	public PoPurApLBean ifExistL(String id);
	/**
	 * @Title:editPurIsClose
	 * @Description:关闭或打开采购申请单 
	 * 参数格式:
	 * @param  java.util.HashMap
	 * @return 
	 * @throws 
	 */
	public void editPurIsClose(HashMap<String, Object> map);
}
