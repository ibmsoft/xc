package com.xzsoft.xc.ap.mapper;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.ap.modal.ApInvGlHBean;
import com.xzsoft.xc.ap.modal.ApInvGlLBean;
import com.xzsoft.xc.ap.modal.ApPayHBean;
import com.xzsoft.xc.ap.modal.ApPayLBean;
import com.xzsoft.xc.apar.modal.ApInvTransBean;

/** 
 * @className ApInvGlBaseMapper
 * @Description 应付单mapper
 * @author 韦才文
 * @version 创建时间：2016年7月9日 下午12:37:57 
 */
public interface ApInvGlCommonMapper {
	/**
	 * @Title getQcDatas
	 * @Description: 应付供应商期初数据
	 * 参数格式:
	 * @param  java.util.HashMap
	 * @return java.util.HashMap
	 * @throws 
	 */
	public HashMap<String, Object> getQcDatas(HashMap<String, String> paramMap);
	/**
	 * @Title addInvGlHInfo
	 * @Description: 添加应付单主表信息
	 * 参数格式:
	 * @param com.xzsoft.xc.ap.modal.ApInvGlHBean
	 * @return 
	 * @throws 
	 */
	public void addInvGlHInfo(ApInvGlHBean apInvGlHBean);
	
	/**
	 * @Title editInvGlHInfo
	 * @Description: 更新应付单主表信息 
	 * 参数格式:
	 * @param com.xzsoft.xc.ap.modal.ApInvGlHBean
	 * @return 
	 * @throws 
	 */
	public void editInvGlHInfo(ApInvGlHBean apInvGlHBean);
	
	/**
	 * @Title delInvGlHInfo
	 * @Description: 通过主键ID删除应付单主表信息 
	 * 参数格式:
	 * @param java.util.List
	 * @return 
	 * @throws 
	 */
	public void delInvGlHInfo(List<String> idList);
	
	/**
	 * @Title delInvGlHInfoByInvHId
	 * @Description: 通过发票ID删除应付单主表信息
	 * 参数格式:
	 * @param java.util.List
	 * @return 
	 * @throws 
	 */
	public void delInvGlHInfoByInvHId(List<String> idList);
	
	/**
	 * @Title delInvGlLInfo
	 * @Description: 删除应付单行表信息
	 * 参数格式:
	 * @param java.util.List
	 * @return 
	 * @throws 
	 */
	public void delInvGlLInfo(List<String> idList);
	
	/**
	 * @Title ifGlCount
	 * @Description: 查询科目是否已期初(条件：科目ID，客户ID,项目ID，部门ID) 
	 * 参数格式:
	 * @param java.util.HashMap
	 * @return java.lang.String
	 * @throws 
	 */
	public String ifGlCount(HashMap<String, String> map);
	/**
	 * @Title updateGlCountInvL
	 * @Description: 更新应付单行表
	 * 参数格式:
	 * @param com.xzsoft.xc.ap.modal.ApInvGlLBean
	 * @return 
	 * @throws 
	 */
	public void updateGlCountInvL(ApInvGlLBean apInvGlLBean);
	
	/**
	 * @Title addInvGlLInfo
	 * @Description: 添加应付单行表信息
	 * 参数格式:
	 * @param com.xzsoft.xc.ap.modal.ApInvGlLBean
	 * @return 
	 * @throws 
	 */
	public void addInvGlLInfo(ApInvGlLBean apInvGlLBean);
	
	/**
	 * @Title getInvGlHInfo
	 * @Description: 通过ID查询应收单主表信息
	 * 参数格式:
	 * @param java.lang.String
	 * @return com.xzsoft.xc.ap.modal.ApInvGlHBean
	 * @throws 
	 */
	public ApInvGlHBean  getInvGlHInfo(String invGlHId);
	
	
	/**
	 * @Title getQcDatasYuF
	 * @Description: 预付期初查询数据
	 * 参数格式:
	 * @param java.util.HashMap
	 * @return java.util.HashMap
	 * @throws 
	 */
	public HashMap<String, Object> getQcDatasYuF(HashMap<String, String> paramMap);
	
	/**
	 * @Title getPayHInfo
	 * @Description: 通过ID查询预付单主表信息
	 * 参数格式:
	 * @param java.lang.String
	 * @return com.xzsoft.xc.ap.modal.ApPayHBean
	 * @throws 
	 */
	public ApPayHBean getPayHInfo(String apPayHId);
	
	/**
	 * @Title ifGlCountYuF
	 * @Description: 查询科目是否已期初(预付)(条件：科目ID，客户ID,项目ID，部门ID) 
	 * 参数格式:
	 * @param java.util.HashMap
	 * @return java.lang.String
	 * @throws 
	 */
	public String ifGlCountYuF(HashMap<String, String> map);
	
	/**
	 * @Title updateGlCountPayL
	 * @Description: 更新预付单行表
	 * 参数格式:
	 * @param com.xzsoft.xc.ap.modal.ApPayLBean
	 * @return java.lang.String
	 * @throws 
	 */
	public void updateGlCountPayL(ApPayLBean apPayLBean);
	
	/**
	 * @Title addPayHInfo
	 * @Description: 添加预付单主表信息
	 * 参数格式:
	 * @param com.xzsoft.xc.ap.modal.ApPayHBean
	 * @return
	 * @throws 
	 */
	public void addPayHInfo(ApPayHBean apPayHBean);
	/**
	 * @Title editPayHInfo
	 * @Description: 更新预付单主表信息
	 * 参数格式:
	 * @param com.xzsoft.xc.ap.modal.ApPayHBean
	 * @return
	 * @throws 
	 */
	public void editPayHInfo(ApPayHBean apPayHBean);
	
	/**
	 * @Title delPayHInfo
	 * @Description: 通过主键ID删除预付单主表信息
	 * 参数格式:
	 * @param java.util.List
	 * @return
	 * @throws 
	 */
	public void delPayHInfo(List<String> idList);
	
	/**
	 * @Title delPayHInfoByPayHId
	 * @Description: 通过发票ID删除预付单主表信息
	 * 参数格式:
	 * @param java.util.List
	 * @return
	 * @throws 
	 */
	public void delPayHInfoByPayHId(List<String> idList);
	
	/**
	 * @Title addPayLInfo
	 * @Description: 添加预付单行表信息
	 * 参数格式:
	 * @param com.xzsoft.xc.ap.modal.ApPayLBean
	 * @return
	 * @throws 
	 */
	public void addPayLInfo(ApPayLBean apPayLBean);
	
	/**
	 * @Title delPayLInfo
	 * @Description: 删除预收单行表
	 * 参数格式:
	 * @param java.util.List
	 * @return
	 * @throws 
	 */
	public void delPayLInfo(List<String> idList);
	/**
	 * @Title getPayHId
	 * @Description: 通过预付单主表ID查询行表信息
	 * 参数格式:
	 * @param java.lang.String
	 * @return java.util.List
	 * @throws 
	 */
	public List<HashMap<String, String>> getPayHId(String id);
	/**
	 * @Title getInvHId
	 * @Description: 通过应付单主表ID查询行表信息
	 * 参数格式:
	 * @param java.lang.String
	 * @return java.util.List
	 * @throws 
	 */
	public List<HashMap<String, String>> getInvHId(String id);
	/**
	 * @Title checkInvIfUse
	 * @Description: 查看拆分出來的应付单是否被使用 
	 * 参数格式:
	 * @param java.util.List
	 * @return java.lang.String
	 * @throws 
	 */
	public String checkInvIfUse(List<String> ids);
	/**
	 * @Title balanceInv
	 * @Description: 应付记账 
	 * 参数格式:
	 * @param java.util.List
	 * @return 
	 * @throws 
	 */
	public void balanceInv(List<String> ids);
	/**
	 * @Title cancelBalanceInv
	 * @Description: 应付取消记账 
	 * 参数格式:
	 * @param java.util.List
	 * @return 
	 * @throws 
	 */
	public void cancelBalanceInv(List<String> ids);
	/**
	 * @Title checkInvIfUse
	 * @Description: 查看拆分出來的预付单是否被使用 
	 * 参数格式:
	 * @param java.util.List
	 * @return java.lang.String
	 * @throws 
	 */
	public String checkPayIfUse(List<String> ids);
	/**
	 * @Title balanceInv
	 * @Description: 预付记账 
	 * 参数格式:
	 * @param java.util.List
	 * @return 
	 * @throws 
	 */
	public void balancePay(List<String> ids);
	/**
	 * @Title cancelBalanceInv
	 * @Description: 预付取消记账 
	 * 参数格式:
	 * @param java.util.List
	 * @return 
	 * @throws 
	 */
	public void cancelBalancePay(List<String> ids);
	/**
	 * @Title addInvTrans
	 * @Description: 插入交易明细
	 * 参数格式:
	 * @param com.xzsoft.xc.apar.modal.ApInvTransBean
	 * @return 
	 * @throws 
	 */
	public void addInvTrans(ApInvTransBean transBean);
	/**
	 * @Title updateInvtrans
	 * @Description: 更新交易明细
	 * 参数格式:
	 * @param com.xzsoft.xc.apar.modal.ApInvTransBean
	 * @return 
	 * @throws 
	 */
	public void updateInvtrans(ApInvTransBean transBean);
	/**
	 * @Title updateInvtrans
	 * @Description: 删除交易明细
	 * 参数格式:
	 * @param java.util.List
	 * @return 
	 * @throws 
	 */
	public void deleteInvTrans(List<String> ids);
	
}
