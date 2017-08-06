package com.xzsoft.xc.po.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xzsoft.xc.po.dao.PoContractTypeDao;
import com.xzsoft.xc.po.modal.PoContractTypeBean;
import com.xzsoft.xc.po.service.PoContractTypeService;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.platform.util.PlatformUtil;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;

/** 
 * @ClassName: PoContractTypeServiceImpl 
 * @Description: TODO
 * @author weicw 
 * @date 2016年11月29日 下午4:49:13 
 * 
 * 
 */
@Service("poContractTypeService")
public class PoContractTypeServiceImpl implements PoContractTypeService{
	@Resource
	private PoContractTypeDao dao;
	//日志记录
	private static Logger log = Logger.getLogger(PoContractTypeServiceImpl.class.getName());
	/**
	 * @Title:saveContractType
	 * @Description: 保存合同类型
	 * 参数格式:
	 * @param java.lang.String
	 * @return
	 * @throws 
	 */
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	@Override
	public JSONObject saveContractType(String recordsStr,String deleteRecords) throws Exception {
		JSONObject jo = new JSONObject();
		try {
			
			JSONArray recordsArray = new JSONArray(recordsStr);
			List<PoContractTypeBean> addList = new ArrayList<PoContractTypeBean>();
			HashMap<String, Object> addMap = new HashMap<String, Object>(); 
			List<PoContractTypeBean> editList = new ArrayList<PoContractTypeBean>();
			HashMap<String, Object> editMap = new HashMap<String, Object>(); 
			for (int i = 0; i < recordsArray.length(); i++) {
				JSONObject recordsJo = recordsArray.getJSONObject(i);
				if(!"1".equalsIgnoreCase(dao.ifExist(recordsJo.getString("CONTRACT_TYPE_ID")))){//新增
					PoContractTypeBean bean = new PoContractTypeBean();
					bean.setCONTRACT_TYPE_ID(recordsJo.getString("CONTRACT_TYPE_ID"));
					bean.setCONTRACT_TYPE_CODE(recordsJo.getString("CONTRACT_TYPE_CODE"));
					bean.setCONTRACT_TYPE_NAME(recordsJo.getString("CONTRACT_TYPE_NAME"));
					bean.setDESCRIPTION(recordsJo.getString("DESCRIPTION"));
					bean.setSTATUS(recordsJo.getString("STATUS"));
					bean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
					bean.setCREATED_BY(CurrentSessionVar.getUserId());
					bean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
					bean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
					addList.add(bean);
				}else{//修改
					PoContractTypeBean bean = new PoContractTypeBean();
					bean.setCONTRACT_TYPE_ID(recordsJo.getString("CONTRACT_TYPE_ID"));
					bean.setCONTRACT_TYPE_CODE(recordsJo.getString("CONTRACT_TYPE_CODE"));
					bean.setCONTRACT_TYPE_NAME(recordsJo.getString("CONTRACT_TYPE_NAME"));
					bean.setDESCRIPTION(recordsJo.getString("DESCRIPTION"));
					bean.setSTATUS(recordsJo.getString("STATUS"));
					bean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
					bean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
					editList.add(bean);
				}
				
			}
			//新增
			addMap.put("dbType", PlatformUtil.getDbType());
			addMap.put("list", addList);
			if(addList.size()>0){
				dao.addContractType(addMap);
			}
			//修改
			editMap.put("dbType", PlatformUtil.getDbType());
			editMap.put("list", editList);
			if(editList.size()>0){
				dao.editContractType(editMap);
			}
			//删除
			JSONArray deleteArray = new JSONArray(deleteRecords);
			List<String> deleteList = new ArrayList<String>();
			for (int i = 0; i < deleteArray.length(); i++) {
				deleteList.add(deleteArray.getString(i));
			}
			if(deleteList.size()>0){
				dao.deleteContractType(deleteList);	
			}
				
			jo.put("flag", "0");
		} catch (Exception e) {
			jo.put("flag", "1");
			jo.put("msg", e.getMessage());
			log.error(e.getMessage());
			throw e;
		}
		
		return jo;
		
	}

}

