package com.xzsoft.xc.fa.service.impl;

import java.util.Date;
import java.util.HashMap;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fr.base.core.UUID;
import com.xzsoft.xc.fa.dao.GlInterfaceDao;
import com.xzsoft.xc.fa.service.GlInterfaceService;
import com.xzsoft.xc.gl.service.VoucherHandlerService;
import com.xzsoft.xip.framework.util.JSONUtil;

@Service("glInterfaceService")
public class GlInterfaceServiceImpl implements GlInterfaceService {

    private static Logger log = Logger.getLogger(GlInterfaceServiceImpl.class);

    @Resource
    private VoucherHandlerService voucherHandlerService;
    
    @Resource
    private GlInterfaceDao glInterfaceDao;

	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public String saveInterface(String glInterface, String glInterfaceIds, String glInterfaceHead, String userId) throws Exception {
		
		// 保存凭证
		String returnJson = voucherHandlerService.doSaveVoucher(glInterface);
		
		JSONObject jo = new JSONObject(returnJson);
		
		String headId = "";
		if(jo.get("headId") != null){
			headId = jo.getString("headId");
		}
		
		HashMap interfaceHeadJson = (HashMap) JSONUtil.jsonToMap(glInterfaceHead);
		
		String iHeadId = UUID.randomUUID().toString();
		interfaceHeadJson.put("V_HEAD_ID", headId);
		interfaceHeadJson.put("I_HEAD_ID", iHeadId);
		interfaceHeadJson.put("CREATED_BY", userId);
		interfaceHeadJson.put("CREATION_DATE", new Date());
		
		// 新增会计平台头
		glInterfaceDao.insertGlInterfaceHead(interfaceHeadJson);
		
		// 更新会计平台
		glInterfaceDao.updateGlInterface(iHeadId, glInterfaceIds, userId, new Date());
		
		return jo.toString();
	}

	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public String submitGlInterface(String vHeadId, String iHeadId) throws Exception {
		String jsonArray = "[{\"V_HEAD_ID\":\""+vHeadId+"\"}]";
		
		// 提交
		JSONObject submitJo = new JSONObject(voucherHandlerService.doSubmitVouchers(jsonArray));
		
		if("0".equals(submitJo.getString("flag"))){
			// 审核
			JSONObject checkJo = new JSONObject(voucherHandlerService.doCheckVouchers(jsonArray));
			
			return checkJo.toString();
		}
		
		return "";
	}

	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public String draftGlInterface(String vHeadId, String iHeadId) throws Exception {
		String jsonArray = "[{\"V_HEAD_ID\":\""+vHeadId+"\"}]";
		
		// 取消审核
		JSONObject checkJo = new JSONObject(voucherHandlerService.doUncheckVouchers(jsonArray));
		
		if("0".equals(checkJo.getString("flag"))){
			// 取消
			JSONObject draftJo = new JSONObject(voucherHandlerService.doCancelSubmitVouchers(jsonArray));
			
			return draftJo.toString();
		}
		
		return "";
	}

	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public String deleteGlInterface(String vHeadId, String iHeadId, String userId) throws Exception {
		String jsonArray = "[{\"V_HEAD_ID\":\""+vHeadId+"\"}]";
		
		// 取消
		JSONObject deleteJo = new JSONObject(voucherHandlerService.doDelVouchers(jsonArray));
		
		if("0".equals(deleteJo.getString("flag"))){
			glInterfaceDao.updateGlInterface(iHeadId, userId, new Date());
			
			glInterfaceDao.deleteGlInterfaceHead(iHeadId);
		}
		
		return deleteJo.toString();
	}

}
