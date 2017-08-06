package com.xzsoft.xc.ar.dao.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.ar.dao.ArInvoiceBaseDao;
import com.xzsoft.xc.ar.mapper.ArInvoiceBaseMapper;
import com.xzsoft.xc.ar.modal.ArInvoiceHBean;
import com.xzsoft.xip.platform.util.PlatformUtil;
/**
 * 
  * @ClassName ArInvoiceBaseDaoImpl
  * @Description 应收模块发票基础信息数据层接口实现类
  * @author 任建建
  * @date 2016年7月6日 下午1:32:59
 */
@Repository("arInvoiceBaseDao")
public class ArInvoiceBaseDaoImpl implements ArInvoiceBaseDao {
	@Resource
	ArInvoiceBaseMapper arInvoiceBaseMapper;
	/*
	 * 
	  * <p>Title saveInvoice</p>
	  * <p>Description 保存销售发票信息</p>
	  * @param arInvoiceH
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.ArInvoiceBaseDao#saveInvoice(com.xzsoft.xc.ar.modal.ArInvoiceHBean)
	 */
	@Override
	public void saveInvoice(ArInvoiceHBean arInvoiceH) throws Exception {
		try {
			//保存销售发票主表信息
			arInvoiceBaseMapper.saveArInvoice(arInvoiceH);
			//保存销售发票明细表信息
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("dbType",PlatformUtil.getDbType());
			map.put("list",arInvoiceH.getAddInvoiceL());
			arInvoiceBaseMapper.saveArInvoiceDtl(map);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/*
	 * 
	  * <p>Title updateInvoice</p>
	  * <p>Description 更新销售发票信息</p>
	  * @param arInvoiceH
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.ArInvoiceBaseDao#updateInvoice(com.xzsoft.xc.ar.modal.ArInvoiceHBean)
	 */
	@Override
	public void updateInvoice(ArInvoiceHBean arInvoiceH) throws Exception {
		try {
			//更新销售发票主表信息
			arInvoiceBaseMapper.updateArInvoice(arInvoiceH);
			//新增销售发票明细行
			if(arInvoiceH.getAddInvoiceL() != null && arInvoiceH.getAddInvoiceL().size() > 0) {
				HashMap<String,Object> addMap = new HashMap<String,Object>();
				addMap.put("dbType",PlatformUtil.getDbType());
				addMap.put("list",arInvoiceH.getAddInvoiceL());
				arInvoiceBaseMapper.saveArInvoiceDtl(addMap);
			}
			//更新销售发票明细行
			if(arInvoiceH.getUpdateInvoiceL() != null && arInvoiceH.getUpdateInvoiceL().size() > 0){
				HashMap<String,Object> updMap = new HashMap<String,Object>() ;
				updMap.put("dbType",PlatformUtil.getDbType());
				updMap.put("list",arInvoiceH.getUpdateInvoiceL());
				arInvoiceBaseMapper.updateArInvoiceDtl(updMap);
			}
			//删除销售发票明细行
			if(arInvoiceH.getDeleteInvoiceLs() != null && arInvoiceH.getDeleteInvoiceLs().size() > 0){
				arInvoiceBaseMapper.deleteArInvoiceDtl(arInvoiceH.getDeleteInvoiceLs());
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	/*
	 * 
	  * <p>Title deleteInvoiceDtl</p>
	  * <p>Description 根据销售发票行表ID批量删除销售发票行表信息</p>
	  * @param dtlLists
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.ArInvoiceBaseDao#deleteInvoiceDtl(java.util.List)
	 */
	@Override
	public void deleteInvoiceDtl(List<String> dtlLists) throws Exception {
		try {
			//删除销售发票行表信息
			arInvoiceBaseMapper.deleteArInvoiceDtl(dtlLists);
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	/*
	 * 
	  * <p>Title deleteInvoices</p>
	  * <p>Description 批量删除销售发票信息</p>
	  * @param lists
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.ArInvoiceBaseDao#deleteInvoices(java.util.List)
	 */
	@Override
	public void deleteInvoices(List<String> lists) throws Exception {
		try {
			//删除销售发票行表信息
			arInvoiceBaseMapper.deleteArInvoiceDtls(lists);
			//删除销售发票主表信息
			arInvoiceBaseMapper.deleteArInvoices(lists);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/*
	 * 
	  * <p>Title printArInvoice</p>
	  * <p>Description 对销售发票进行开票处理</p>
	  * @param arInvoiceHList
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.ArInvoiceBaseDao#printArInvoice(java.util.List)
	 */
	@Override
	public void printArInvoice(List<ArInvoiceHBean> arInvoiceHList) throws Exception {
		try {
			//对销售发票进行开票处理
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", arInvoiceHList);
			arInvoiceBaseMapper.printArInvoice(map);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/*
	 * 
	  * <p>Title finArInvoice</p>
	  * <p>Description 对销售发票进行复核（取消复核）处理</p>
	  * @param arInvoiceHList
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.ArInvoiceBaseDao#finArInvoice(java.util.List)
	 */
	@Override
	public void finArInvoice(List<ArInvoiceHBean> arInvoiceHList) throws Exception {
		try {
			//对销售发票进行复核处理
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", arInvoiceHList);
			arInvoiceBaseMapper.finArInvoice(map);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/*
	 * 
	  * <p>Title getInvoicePreCount</p>
	  * <p>Description 判断销售发票是否核销预付过</p>
	  * @param arInvHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.ArInvoiceBaseDao#getInvoicePreCount(java.lang.String)
	 */
	@Override
	public int getInvoicePreCount(String arInvHId) throws Exception {
		return arInvoiceBaseMapper.getInvoicePreCount(arInvHId);
	}
	
	/*
	 * 
	  * <p>Title getLInvoiceCount</p>
	  * <p>Description 判断销售发票是否已经成为另一个红字发票的蓝字发票</p>
	  * @param arInvHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.ArInvoiceBaseDao#getLInvoiceCount(java.lang.String)
	 */
	@Override
	public int getLInvoiceCount(String arInvHId) throws Exception {
		return arInvoiceBaseMapper.getLInvoiceCount(arInvHId);
	}
	
	/*
	 * 
	  * <p>Title getInvGlHCount</p>
	  * <p>Description 判断销售发票是否已经被应付单引用</p>
	  * @param arInvHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.ArInvoiceBaseDao#getInvGlHCount(java.lang.String)
	 */
	@Override
	public int getInvGlHCount(String arInvHId) throws Exception {
		return arInvoiceBaseMapper.getInvGlHCount(arInvHId);
	}
	
	/*
	 * 
	  * <p>Title getInvoiceLdProcessInfo</p>
	  * <p>Description 查询销售发票相关流程信息</p>
	  * @param arCatCode
	  * @param ledgerId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.ArInvoiceBaseDao#getInvoiceLdProcessInfo(java.lang.String, java.lang.String)
	 */
	@Override
	public HashMap<String,String> getInvoiceLdProcessInfo(String arCatCode,String ledgerId) throws Exception {
		HashMap<String,String> returnMap = null;
		try {
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("arCatCode",arCatCode);
			map.put("ledgerId",ledgerId);
			
			returnMap = arInvoiceBaseMapper.getInvoiceLdProcessInfo(map);
		} catch (Exception e) {
			throw e;
		}
		return returnMap;
	}
	
	/*
	 * 
	  * <p>Title getInvoiceProcessInfo</p>
	  * <p>Description 查询销售发票相关流程信息</p>
	  * @param arCatCode
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.ArInvoiceBaseDao#getInvoiceProcessInfo(java.lang.String)
	 */
	@Override
	public HashMap<String, String> getInvoiceProcessInfo(String arCatCode)throws Exception {
		HashMap<String,String> returnMap = null;
		try {
			returnMap = arInvoiceBaseMapper.getInvoiceProcessInfo(arCatCode);
		} catch (Exception e) {
			throw e;
		}
		return returnMap;
	}
}