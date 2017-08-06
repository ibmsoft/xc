package com.xzsoft.xc.ap.dao.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Repository;

import com.xzsoft.xc.ap.dao.ApContractDao;
import com.xzsoft.xc.ap.mapper.ApContractMapper;
import com.xzsoft.xc.ap.modal.ApContractBean;
import com.xzsoft.xc.ap.modal.ApContractHisBean;

/** 
 * @ClassName: ApContractDaoImpl 
 * @Description: 合同管理Dao层实现类
 * @author weicw 
 * @date 2016年8月19日 下午3:15:15 
 * 
 * 
 */
@Repository("apContractDao")
public class ApContractDaoImpl implements ApContractDao{
	@Resource
	ApContractMapper mapper;
	/**
	 * @Title:checkExistAdd
	 * @Description:判断同账簿下是否有同编码或同合同名称的合同 
	 * 参数格式:
	 * @param  java.util.HashMap
	 * @return java.lang.String
	 * @throws 
	 */
	@Override
	public String checkExistAdd(HashMap<String, String> map) {
		return mapper.checkExistAdd(map);
	}
	/**
	 * @Title:addContractInfo
	 * @Description:添加合同信息
	 * 参数格式:
	 * @param  com.xzsoft.xc.ap.modal.ApContractBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void addContractInfo(ApContractBean bean) {
		mapper.addContractInfo(bean);
		
	}
	/**
	 * @Title:checkExistDel
	 * @Description:查看合同是否存在明细（如果存在明细，则不能删除）
	 * 参数格式:
	 * @param  java.lang.String
	 * @return java.lang.String
	 * @throws 
	 */
	@Override
	public String checkExistDel(String contractId) {
		return mapper.checkExistDel(contractId);
	}
	/**
	 * @Title:delContractInfo
	 * @Description:删除合同信息
	 * 参数格式:
	 * @param  java.util.List
	 * @return 
	 * @throws 
	 */
	@Override
	public void delContractInfo(List<String> ids) {
		 mapper.delContractInfo(ids);
		
	}
	/**
	 * @Title:editContractInfo
	 * @Description:修改合同信息
	 * 参数格式:
	 * @param  com.xzsoft.xc.ap.modal.ApContractBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void editContractInfo(ApContractBean bean) {
		mapper.editContractInfo(bean);
		
	}
	/**
	 * @Title:closeContract
	 * @Description:关闭合同 
	 * 参数格式:
	 * @param  java.util.HashMap
	 * @return 
	 * @throws 
	 */
	@Override
	public void closeContract(HashMap<String, Object> map) {
		mapper.closeContract(map);
		
	}
	/**
	 * @Title:openContract
	 * @Description:打开合同
	 * 参数格式:
	 * @param  java.util.HashMap
	 * @return 
	 * @throws 
	 */
	@Override
	public void openContract(HashMap<String, Object> map) {
		mapper.openContract(map);
		
	}
	/**
	 * @Title:ifExist
	 * @Description:判断操作为新增还是修改
	 * 参数格式:
	 * @param  java.lang.String
	 * @return java.lang.String
	 * @throws 
	 */
	@Override
	public String ifExist(String contractId) {
		return mapper.ifExist(contractId);
	}
	/**
	 * @Title:getContractInfo
	 * @Description:查找合同信息
	 * 参数格式:
	 * @param  java.lang.String
	 * @return  com.xzsoft.xc.ap.modal.ApContractBean
	 * @throws 
	 */
	@Override
	public ApContractBean getContractInfo(String contractId){
		return mapper.getContractInfo(contractId);
	}
	/**
	 * @Title:addContractHis
	 * @Description: 变更合同
	 * 参数格式:
	 * @param com.xzsoft.xc.ap.modal.ApContractBean
	 * @return
	 * @throws 
	 */
	@Override
	public void addContractHis(ApContractHisBean bean) {
		mapper.addContractHis(bean);
	}
	/**
	 * @Title:addContractHis
	 * @Description:查询变更合同历史表
	 * 参数格式:
	 * @param  java.lang.String
	 * @return com.xzsoft.xc.ap.modal.ApContractHisBean
	 * @throws 
	 */
	@Override
	public ApContractHisBean getContractHisInfo(String id){
		return mapper.getContractHisInfo(id);
	}
	/**
	 * @Title:changeApContract
	 * @Description:变更，回写合同
	 * 参数格式:
	 * @param  java.lang.String
	 * @return 
	 * @throws 
	 */
	@Override
	public void changeApContract (String id) throws Exception{
			ApContractHisBean hisBean = getContractHisInfo(id);
			ApContractBean bean = new ApContractBean();
			bean.setAP_CONTRACT_ID(hisBean.getAP_CONTRACT_ID());
			bean.setAP_CONTRACT_CODE(hisBean.getAP_CONTRACT_CODE());
			bean.setAP_CONTRACT_NAME(hisBean.getAP_CONTRACT_NAME());
			bean.setAP_CONTRACT_TYPE(hisBean.getAP_CONTRACT_TYPE());
			bean.setLEDGER_ID(hisBean.getLEDGER_ID());
			bean.setVENDOR_ID(hisBean.getVENDOR_ID());
			bean.setAMOUNT(hisBean.getAMOUNT());
			bean.setDEPT_ID(hisBean.getDEPT_ID());
			bean.setPROJECT_ID(hisBean.getPROJECT_ID());
			bean.setCONTRACT_USER(hisBean.getCONTRACT_USER());
			bean.setDESCRIPTION(hisBean.getDESCRIPTION());
			bean.setSTART_DATE(hisBean.getSTART_DATE());
			bean.setEND_DATE(hisBean.getEND_DATE());
			bean.setLAST_UPDATE_DATE(hisBean.getLAST_UPDATE_DATE());
			bean.setLAST_UPDATED_BY(hisBean.getLAST_UPDATED_BY());
			bean.setORG_ID(hisBean.getORG_ID());
			bean.setTAX_AMT(hisBean.getTAX_AMT());
			bean.setINV_AMOUNT(hisBean.getINV_AMOUNT());
			bean.setVERSION(hisBean.getVERSION());
			bean.setINS_CODE(hisBean.getINS_CODE());
			bean.setAUDIT_DATE(hisBean.getAUDIT_DATE());
			bean.setSYS_AUDIT_STATUS(hisBean.getSYS_AUDIT_STATUS());
			bean.setSYS_AUDIT_STATUS_DESC(hisBean.getSYS_AUDIT_STATUS_DESC());
			editContractInfo(bean);
	}
	/**
	 * @Title:ifChange
	 * @Description:判断合同是否已产生变更
	 * 参数格式:
	 * @param  java.lang.String
	 * @return java.lang.String
	 * @throws 
	 */
	@Override
	public String ifChange(String id){
		return mapper.ifChange(id);
	}
	/**
	 * @Title:editContractHisInfo
	 * @Description:修改合同变更历史信息 
	 * 参数格式:
	 * @param  com.xzsoft.xc.ap.modal.ApContractHisBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void editContractHisInfo(ApContractHisBean bean){
		mapper.editContractHisInfo(bean);
	}
	/**
	 * @Title:updateSysAuditStatus
	 * @Description:更改合同的业务状态
	 * 参数格式:
	 * @param  com.xzsoft.xc.ap.modal.ApContractBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void updateSysAuditStatus(ApContractBean bean){
		mapper.updateSysAuditStatus(bean);
	}
}

