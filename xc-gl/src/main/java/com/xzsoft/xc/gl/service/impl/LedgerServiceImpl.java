package com.xzsoft.xc.gl.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xzsoft.xc.gl.dao.LedgerDAO;
import com.xzsoft.xc.gl.modal.LedgerSecDTO;
import com.xzsoft.xc.gl.service.LedgerService;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;

/**
 * @ClassName: LedgerServiceImpl 
 * @Description: 账簿、账簿权限、账簿建账/取消建账 服务层实现类
 * @author linp
 * @date 2016年5月13日 下午2:54:19 
 *
 */
@Service("ledgerService")
public class LedgerServiceImpl implements LedgerService {
	
	@Resource
	private LedgerDAO ledgerDAO ;

	/*
	 * (非 Javadoc) 
	 * <p>Title: isCreateAccount</p> 
	 * <p>Description:  是否建账
	 * @Description: 是否建账
	 * </p> 
	 * @param ledgerId		账簿ID
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.service.XCGLSumBalanceService#isCreateAccount(java.lang.String, java.lang.String)
	 */
	@Override
	public HashMap<String,String> isCreateAccount(String ledgerId) throws Exception {
		return ledgerDAO.getCreateFlag(ledgerId) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: updateCreateAccountFlag</p> 
	 * <p>Description: 修改建账标志</p> 
	 * @param ledgerId
	 * @param flag
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.service.XCGLSumBalanceService#updateCreateAccountFlag(java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED, propagation=Propagation.REQUIRED, rollbackFor={Exception.class}) 
	public void updateCreateAccountFlag(String ledgerId,String flag,String createType) throws Exception {
		ledgerDAO.updateCreateFlag(ledgerId, flag,createType);
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: grantOrRevoke2UR</p> 
	 * <p>Description: 账簿授权/取消授权 </p> 
	 * @param ldJsonArray
	 * @param urJsonArray
	 * @param opType : grant-授权, revoke-取消授权
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.service.LedgerService#grantOrRevoke2UR(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED, propagation=Propagation.REQUIRED, rollbackFor={Exception.class}) 
	public void grantOrRevoke2UserRole(String ldJsonArray, String urJsonArray, String opType) throws Exception {
		try {
			if("grant".equals(opType)){ // 授权
				JSONArray ja = new JSONArray(ldJsonArray) ;
				List<String> list = new ArrayList<String>() ;
				if(ja.length() >0){
					for(int i=0; i<ja.length(); i++){
						JSONObject jo = ja.getJSONObject(i) ;
						list.add(jo.getString("LEDGER_ID")) ;
					}
					// 获取已授权的信息
					List<String> secList = ledgerDAO.getLedgerSec(list) ;
					
					// 授权处理
					JSONArray urJa = new JSONArray(urJsonArray) ;
					if(urJa.length() > 0){
						// 待新增授权
						List<LedgerSecDTO> addSecList = new ArrayList<LedgerSecDTO>() ;
						
						Date cdate = CurrentUserUtil.getCurrentDate() ;
						
						for(String ledgerId : list){
							for(int i=0; i<urJa.length(); i++){
								JSONObject jo = urJa.getJSONObject(i) ;
								String userId = jo.getString("USER_ID") ;
								String roleId = jo.getString("ROLE_ID") ;
								
								String secStr = ledgerId.concat("/").concat(userId).concat("/").concat(roleId) ;
								if(!secList.contains(secStr)){
									LedgerSecDTO dto = new LedgerSecDTO() ;
									dto.setLedgerId(ledgerId);
									dto.setUserId(userId);
									dto.setRoleId(roleId);
									
									dto.setLdSecId(UUID.randomUUID().toString());
									dto.setCreationDate(cdate);
									dto.setCreatedBy(CurrentSessionVar.getUserId());
									
									addSecList.add(dto) ;
								}
							}
						}
						// 保存授权信息
						if(!addSecList.isEmpty()){
							ledgerDAO.addLedgerSec(addSecList);
						}
					}
				}
				
			}else{ // 取消授权
				List<LedgerSecDTO> delSecList = new ArrayList<LedgerSecDTO>() ;
				
				JSONArray ja = new JSONArray(ldJsonArray) ;
				for(int i=0; i<ja.length(); i++){
					JSONObject jo = ja.getJSONObject(i) ;
					String ledgerId = jo.getString("LEDGER_ID") ;
					
					JSONArray urJa = new JSONArray(urJsonArray) ;
					for(int j=0; j<urJa.length(); j++){
						JSONObject urJo = urJa.getJSONObject(j) ;
						String userId = urJo.getString("USER_ID") ;
						String roleId = urJo.getString("ROLE_ID") ;
						
						// 记录待删除记录
						LedgerSecDTO dto = new LedgerSecDTO() ;
						dto.setLedgerId(ledgerId);
						dto.setUserId(userId);
						dto.setRoleId(roleId);
						
						delSecList.add(dto) ;
					}
				}
				
				// 取消授权
				if(!delSecList.isEmpty()){
					ledgerDAO.delLedgerSec(delSecList);
				}
			}
			
		} catch (Exception e) {
			throw e ;
		}
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: batchDelSec</p> 
	 * <p>Description: 批量删除授权信息 </p> 
	 * @param secJsonArray
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.service.LedgerService#batchDelSec(java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED, propagation=Propagation.REQUIRED, rollbackFor={Exception.class}) 
	public void batchDelSec(String secJsonArray) throws Exception {
		try {
			// 封装参数
			List<String> list = new ArrayList<String>() ;
			JSONArray ja = new JSONArray(secJsonArray) ;
			if(ja.length() >0){
				for(int i=0;i<ja.length();i++){
					JSONObject jo = ja.getJSONObject(i) ;
					String ldSecId = jo.getString("LD_SEC_ID") ;
					list.add(ldSecId) ;
				}
			}
			
			// 执行批量删除
			if(!list.isEmpty()){
				ledgerDAO.batchDelSec(list);
			}
			
		} catch (Exception e) {
			throw e ;
		}
	}
}
