package com.xzsoft.xc.po.mapper;

import java.util.List;

import com.xzsoft.xc.po.modal.PoReturnHBean;
import com.xzsoft.xc.po.modal.PoReturnLBean;

/** 
 * @ClassName: PoReturnMapper
 * @Description: 退货单Mapper层接口 
 * @author weicw
 * @date 2016年12月28日 上午10:53:43 
 * 
 */
public interface PoReturnMapper {
	/**
	 * @Title:ifExistH
	 * @Description:判断退货单主信息新增或修改 
	 * 参数格式:
	 * @param  java.lang.String
	 * @return com.xzsoft.xc.po.modal.PoReturnHBean
	 * @throws 
	 */
	public PoReturnHBean ifExistH(String id);
	/**
	 * @Title:addReturnHData
	 * @Description:新增退货单主信息 
	 * 参数格式:
	 * @param  com.xzsoft.xc.po.modal.PoReturnHBean
	 * @return 
	 * @throws 
	 */
	public void addReturnHData(PoReturnHBean bean);
	/**
	 * @Title:editReturnHData
	 * @Description:修改退货单主信息 
	 * 参数格式:
	 * @param  com.xzsoft.xc.po.modal.PoReturnHBean
	 * @return 
	 * @throws 
	 */
	public void editReturnHData(PoReturnHBean bean);
	/**
	 * @Title:delReturnHData
	 * @Description:删除退货单主信息 
	 * 参数格式:
	 * @param  java.util.List
	 * @return 
	 * @throws 
	 */
	public void delReturnHData(List<String> list);
	/**
	 * @Title:ifExistL
	 * @Description:判断退货单行信息新增或删除 
	 * 参数格式:
	 * @param  java.lang.String
	 * @return com.xzsoft.xc.po.modal.PoReturnLBean
	 * @throws 
	 */
	public PoReturnLBean ifExistL(String id);
	/**
	 * @Title:addReturnLData
	 * @Description:新增退货单行信息 
	 * 参数格式:
	 * @param  com.xzsoft.xc.po.modal.PoReturnLBean
	 * @return 
	 * @throws 
	 */
	public void addReturnLData(PoReturnLBean bean);
	/**
	 * @Title:editReturnLData
	 * @Description:修改退货单行信息  
	 * 参数格式:
	 * @param  com.xzsoft.xc.po.modal.PoReturnLBean
	 * @return 
	 * @throws 
	 */
	public void editReturnLData(PoReturnLBean bean);
	/**
	 * @Title:delReturnLData
	 * @Description:删除退货单行信息  
	 * 参数格式:
	 * @param  java.util.List
	 * @return 
	 * @throws 
	 */
	public void delReturnLData(List<String> list);
	/**
	 * @Title:delReturnLDataByHId
	 * @Description:删除退货单行信息(通过退货单主ID)  
	 * 参数格式:
	 * @param  java.util.List
	 * @return 
	 * @throws 
	 */
	public void delReturnLDataByHId(List<String> list);
}
