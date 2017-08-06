package com.xzsoft.xc.ap.mapper;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.ap.modal.ApContractBean;
import com.xzsoft.xc.ap.modal.ApContractHisBean;

/** 
 * @ClassName: ApContractMapper 
 * @Description: 合同管理mapper层接口
 * @author weicw 
 * @date 2016年8月19日 下午3:06:44 
 * 
 * 
 */
public interface ApContractMapper {
	/**
	 * @Title:ifExist
	 * @Description:判断操作为新增还是修改
	 * 参数格式:
	 * @param  java.lang.String
	 * @return java.lang.String
	 * @throws 
	 */
	public String ifExist(String contractId);
	/**
	 * @Title:getContractInfo
	 * @Description:查找合同信息
	 * 参数格式:
	 * @param  java.lang.String
	 * @return  com.xzsoft.xc.ap.modal.ApContractBean
	 * @throws 
	 */
	public ApContractBean getContractInfo(String contractId);
	/**
	 * @Title:checkExistAdd
	 * @Description:判断同账簿下是否有同编码或同合同名称的合同 
	 * 参数格式:
	 * @param  java.util.HashMap
	 * @return java.lang.String
	 * @throws 
	 */
	public String checkExistAdd(HashMap<String, String> map);
	
	/**
	 * @Title:addContractInfo
	 * @Description:添加合同信息
	 * 参数格式:
	 * @param  com.xzsoft.xc.ap.modal.ApContractBean
	 * @return 
	 * @throws 
	 */
	public void addContractInfo(ApContractBean bean);
	
	/**
	 * @Title:checkExistDel
	 * @Description:查看合同是否存在明细（如果存在明细，则不能删除）
	 * 参数格式:
	 * @param  java.lang.String
	 * @return java.lang.String
	 * @throws 
	 */
	public String checkExistDel(String contractId);
	
	/**
	 * @Title:delContractInfo
	 * @Description:删除合同信息
	 * 参数格式:
	 * @param  java.util.List
	 * @return 
	 * @throws 
	 */
	public void delContractInfo(List<String> ids);
	
	/**
	 * @Title:editContractInfo
	 * @Description:修改合同信息
	 * 参数格式:
	 * @param  com.xzsoft.xc.ap.modal.ApContractBean
	 * @return 
	 * @throws 
	 */
	public void editContractInfo(ApContractBean bean);
	
	/**
	 * @Title:closeContract
	 * @Description:关闭合同 
	 * 参数格式:
	 * @param  java.util.HashMap
	 * @return 
	 * @throws 
	 */
	public void closeContract(HashMap<String, Object> map);
	
	/**
	 * @Title:openContract
	 * @Description:打开合同
	 * 参数格式:
	 * @param  java.util.HashMap
	 * @return 
	 * @throws 
	 */
	public void openContract(HashMap<String, Object> map);
	/**
	 * @Title:addContractHis
	 * @Description:变更合同
	 * 参数格式:
	 * @param  com.xzsoft.xc.ap.modal.ApContractHisBean
	 * @return 
	 * @throws 
	 */
	public void addContractHis(ApContractHisBean bean);
	/**
	 * @Title:getContractHisInfo
	 * @Description:查询变更合同历史表
	 * 参数格式:
	 * @param  java.lang.String
	 * @return com.xzsoft.xc.ap.modal.ApContractHisBean
	 * @throws 
	 */
	public ApContractHisBean getContractHisInfo(String id);
	/**
	 * @Title:ifChange
	 * @Description:判断合同是否已产生变更
	 * 参数格式:
	 * @param  java.lang.String
	 * @return java.lang.String
	 * @throws 
	 */
	public String ifChange(String id);
	/**
	 * @Title:editContractHisInfo
	 * @Description:修改合同变更历史信息 
	 * 参数格式:
	 * @param  com.xzsoft.xc.ap.modal.ApContractHisBean
	 * @return 
	 * @throws 
	 */
	public void editContractHisInfo(ApContractHisBean bean);
	/**
	 * @Title:updateSysAuditStatus
	 * @Description:更改合同的业务状态
	 * 参数格式:
	 * @param  com.xzsoft.xc.ap.modal.ApContractBean
	 * @return 
	 * @throws 
	 */
	public void updateSysAuditStatus(ApContractBean bean);
}

