package com.xzsoft.xc.fa.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import com.xzsoft.xc.fa.dao.FaAdditionDao;
import com.xzsoft.xc.fa.dao.FaCleanDao;
import com.xzsoft.xc.fa.modal.FaAddition;
import com.xzsoft.xc.fa.modal.FaClean;
import com.xzsoft.xc.fa.modal.FaConstant;
import com.xzsoft.xc.fa.modal.XcFaTrans;
import com.xzsoft.xc.fa.service.FaCleanService;
import com.xzsoft.xc.fa.service.FaDepreciationService;
import com.xzsoft.xc.gl.dao.VInterfaceDao;
import com.xzsoft.xc.gl.modal.VInterface;
import com.xzsoft.xc.gl.service.VoucherHandlerService;
import com.xzsoft.xc.util.common.XConstants;
import com.xzsoft.xip.framework.common.SessionVariable;
import com.xzsoft.xip.platform.util.XipUtil;

@Service("FaCleanService")
public class FaCleanServiceImpl implements FaCleanService{
	 private static Logger log = Logger.getLogger(FaCleanServiceImpl.class);
	 
	@Resource
	private FaCleanDao faCleanDao;
	@Resource
	private VInterfaceDao vInterfaceDao;
	@Resource
	private FaAdditionDao faAdditionDao;
	@Resource
	private FaDepreciationService faDepreciationService;
	
	@Resource
	private VoucherHandlerService voucherHandlerService;   
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void cleanFaAddition(FaClean faClean, HashMap<String, Object> extraParams) throws Exception {
		String lang = extraParams.get("language").toString();
		
		for(FaAddition faAddition :faClean.getFaAdditions()){
			
			if("Y".equals(faClean.getCleanJt())){
				net.sf.json.JSONObject returnJson = faDepreciationService.cancelDepr(faAddition.getAssetId(), faAddition.getPeriodCode(), lang, extraParams.get("userId").toString());
				if(returnJson.get("flag")!=null && "1".equals(returnJson.getString("flag"))){
					throw new Exception(returnJson.get("msg")==null ? "" : returnJson.getString("msg"));
				}
			}
			
			//清理标记 Y清理 N取消清理
			String flag=faClean.getCleanFlag();
			
			if("Y".equals(flag)){
				faAddition.setQlrq(new Date());
				faCleanDao.cleanFaAddition(faAddition);
				
				faAddition = faAdditionDao.getFaAdditionWithId(faAddition.getAssetId());
				
				HashMap<String, String> segments = new HashMap<String, String>();
				if(faAddition.getCbzx()!=null){
					segments.put(XConstants.XIP_PUB_DETPS, faAddition.getCbzx());
				}
				if(faAddition.getProjectId()!=null){
					segments.put(XConstants.XC_PM_PROJECTS, faAddition.getProjectId());
				}
				
				if(faAddition.getLjzjLjtx()!=0){
					VInterface vInterface=new VInterface();
					vInterface.setvInterfaceId(UUID.randomUUID().toString());
					vInterface.setLedgerId(faAddition.getLedgerId());
					vInterface.setPeriodCode(faAddition.getQlqj());
					vInterface.setTransCode("ZCQL");
					vInterface.setDeptId(faAddition.getCbzx());
					vInterface.setProjectId(faAddition.getProjectId());
					
					// 查询科目id
					String drKmId = faAdditionDao.getKmIdByCode(faAddition.getLjzjtxkm(), faAddition.getLedgerId());
					// 查询科目组id
					if(drKmId!=null){
						try{
							JSONObject drKmObject = voucherHandlerService.handlerCCID(faAddition.getLedgerId(), drKmId, segments);
							if(drKmObject!=null){
								vInterface.setDrCcid(drKmObject.getString("ccid"));
							}
						}catch(Exception e){
							vInterface.setDrCcid("");
						}
					}
						
					// 查询科目id
					String crKmId = faAdditionDao.getKmIdByCode(faAddition.getZckm(), faAddition.getLedgerId());
					// 查询科目组id
					if(crKmId!=null){
						try{
							JSONObject crKmObject = voucherHandlerService.handlerCCID(faAddition.getLedgerId(), crKmId, segments);
							if(crKmObject!=null){
								vInterface.setCrCcid(crKmObject.getString("ccid"));
							}
						}catch(Exception e){
							vInterface.setCrCcid("");
						}
					}
					
					vInterface.setDrAcct(faAddition.getLjzjtxkm());
					vInterface.setCrAcct(faAddition.getZckm());
					vInterface.setAmt(faAddition.getLjzjLjtx());
					HashMap<String, String> tokens = new HashMap<String, String>();
					tokens.put("assetCode", faAddition.getZcbm());
					vInterface.setSummary(XipUtil.getMessage(lang, "XC_FA_CLEAN_LJZJTX", tokens));
					vInterface.setBusNo(faAddition.getZcbm());
					vInterface.setSrcHid(faAddition.getAssetId());
					vInterface.setCreatedBy(extraParams.get("userId").toString());
					vInterface.setCreationDate(new Date());
					vInterface.setLastUpdateBy(extraParams.get(SessionVariable.userId).toString());
					vInterface.setLastUpdateDate(new Date());
					vInterfaceDao.insertVInterface(vInterface);
				}
				
				if(faAddition.getJzzb()!=0){
					VInterface vInterface=new VInterface();
					vInterface.setvInterfaceId(UUID.randomUUID().toString());
					vInterface.setLedgerId(faAddition.getLedgerId());
					vInterface.setPeriodCode(faAddition.getQlqj());
					vInterface.setTransCode("ZCQL");
					vInterface.setDeptId(faAddition.getCbzx());
					vInterface.setProjectId(faAddition.getProjectId());
					
					// 查询科目id
					String drKmId = faAdditionDao.getKmIdByCode(faAddition.getJzzbkm(), faAddition.getLedgerId());
					// 查询科目组id
					if(drKmId!=null){
						try{
							JSONObject drKmObject = voucherHandlerService.handlerCCID(faAddition.getLedgerId(), drKmId, segments);
							if(drKmObject!=null){
								vInterface.setDrCcid(drKmObject.getString("ccid"));
							}
						}catch(Exception e){
							vInterface.setDrCcid("");
						}
					}
						
					// 查询科目id
					String crKmId = faAdditionDao.getKmIdByCode(faAddition.getZckm(), faAddition.getLedgerId());
					// 查询科目组id
					if(crKmId!=null){
						try{
							JSONObject crKmObject = voucherHandlerService.handlerCCID(faAddition.getLedgerId(), crKmId, segments);
							if(crKmObject!=null){
								vInterface.setCrCcid(crKmObject.getString("ccid"));
							}
						}catch(Exception e){
							vInterface.setCrCcid("");
						}
					}
					
					vInterface.setDrAcct(faAddition.getJzzbkm());
					vInterface.setCrAcct(faAddition.getZckm());
					vInterface.setAmt(faAddition.getJzzb());
					HashMap<String, String> tokens = new HashMap<String, String>();
					tokens.put("assetCode", faAddition.getZcbm());
					vInterface.setSummary(XipUtil.getMessage(lang, "XC_FA_CLEAN_JZZB", tokens));
					vInterface.setBusNo(faAddition.getZcbm());
					vInterface.setSrcHid(faAddition.getAssetId());
					vInterface.setCreatedBy(extraParams.get("userId").toString());
					vInterface.setCreationDate(new Date());
					vInterface.setLastUpdateBy(extraParams.get(SessionVariable.userId).toString());
					vInterface.setLastUpdateDate(new Date());
					vInterfaceDao.insertVInterface(vInterface);
				}
				
				if(faAddition.getYz()-faAddition.getLjzjLjtx()-faAddition.getJzzb()!=0){
					VInterface vInterface=new VInterface();
					vInterface.setvInterfaceId(UUID.randomUUID().toString());
					vInterface.setLedgerId(faAddition.getLedgerId());
					vInterface.setPeriodCode(faAddition.getQlqj());
					vInterface.setTransCode("ZCQL");
					vInterface.setDeptId(faAddition.getCbzx());
					vInterface.setProjectId(faAddition.getProjectId());
					
					// 查询科目id
					String drKmId = faAdditionDao.getKmIdByCode(faAddition.getQlkm(), faAddition.getLedgerId());
					// 查询科目组id
					if(drKmId!=null){
						try{
							JSONObject drKmObject = voucherHandlerService.handlerCCID(faAddition.getLedgerId(), drKmId, segments);
							if(drKmObject!=null){
								vInterface.setDrCcid(drKmObject.getString("ccid"));
							}
						}catch(Exception e){
							vInterface.setDrCcid("");
						}
					}
						
					// 查询科目id
					String crKmId = faAdditionDao.getKmIdByCode(faAddition.getZckm(), faAddition.getLedgerId());
					// 查询科目组id
					if(crKmId!=null){
						try{
							JSONObject crKmObject = voucherHandlerService.handlerCCID(faAddition.getLedgerId(), crKmId, segments);
							if(crKmObject!=null){
								vInterface.setCrCcid(crKmObject.getString("ccid"));
							}
						}catch(Exception e){
							vInterface.setCrCcid("");
						}
					}
					
					vInterface.setDrAcct(faAddition.getQlkm());
					vInterface.setCrAcct(faAddition.getZckm());
					vInterface.setAmt(faAddition.getYz()-faAddition.getLjzjLjtx()-faAddition.getJzzb());
					HashMap<String, String> tokens = new HashMap<String, String>();
					tokens.put("assetCode", faAddition.getZcbm());
					vInterface.setSummary(XipUtil.getMessage(lang, "XC_FA_CLEAN_JZ", tokens));
					vInterface.setBusNo(faAddition.getZcbm());
					vInterface.setSrcHid(faAddition.getAssetId());
					vInterface.setCreatedBy(extraParams.get("userId").toString());
					vInterface.setCreationDate(new Date());
					vInterface.setLastUpdateBy(extraParams.get(SessionVariable.userId).toString());
					vInterface.setLastUpdateDate(new Date());
					vInterfaceDao.insertVInterface(vInterface);
				}
				
				String tYear = faAddition.getQlqj().substring(0, 4);
	            String tMonth = faAddition.getQlqj().substring(5, 7);
	            if(!"10".equals(tMonth) && !"11".equals(tMonth) && !"12".equals(tMonth)){
	            	tMonth = faAddition.getQlqj().substring(6, 7);
	            }
	            
	            Calendar cal = Calendar.getInstance();
	    		//设置年份
	    		cal.set(Calendar.YEAR, Integer.parseInt(tYear));
	    		//设置月份
	    		cal.set(Calendar.MONTH, Integer.parseInt(tMonth)-1);
	    		//获取某月最大天数
	    		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	    		//设置日历中月份的最大天数
	    		cal.set(Calendar.DAY_OF_MONTH, lastDay);
	    		//格式化日期
	    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    		String lastDayOfMonth = sdf.format(cal.getTime());
	    		Date lastDate = sdf.parse(lastDayOfMonth);
				
				// 事务表写数据
				XcFaTrans xcFaTran = new XcFaTrans();
				xcFaTran.setAssetId(faAddition.getAssetId());
				xcFaTran.setLedgerId(faAddition.getLedgerId());
				xcFaTran.setPeriodCode(faAddition.getQlqj());
				xcFaTran.setTransDate(lastDate);
				xcFaTran.setTransCode(FaConstant.TRANS_TYPE_ZCQL);
				HashMap<String, String> tokens = new HashMap<String, String>();
				tokens.put("assetCode", faAddition.getZcbm());
				xcFaTran.setBzsm(XipUtil.getMessage(lang, "XC_FA_CLEAN", tokens));
				xcFaTran.setSrcId("");
				xcFaTran.setSrcDtlId("");
				xcFaTran.setPostFlag("N");
				xcFaTran.setCreatedBy(extraParams.get(SessionVariable.userId).toString());
				xcFaTran.setCreationDate(new Date());
				xcFaTran.setLastUpdateBy(extraParams.get(SessionVariable.userId).toString());
				xcFaTran.setLastUpdateDate(new Date());
				xcFaTran.setCatCode(faAddition.getCatCode());
				xcFaTran.setCbzx(faAddition.getCbzx());
				
				xcFaTran.setQuantity(0-faAddition.getSl());
				xcFaTran.setYzcr(faAddition.getYz());
				xcFaTran.setLjzjdr(faAddition.getLjzjLjtx());
				xcFaTran.setJzzbdr(faAddition.getJzzb());
				
				faAdditionDao.insertFaTrans(xcFaTran);
			}else if("N".equals(flag)){
				faCleanDao.cleanFaAddition(faAddition);
				
				faCleanDao.deleteInterface(faAddition);
				
				faAdditionDao.deleteFaTrans(faAddition.getAssetId(), FaConstant.TRANS_TYPE_ZCQL);
			}
			
		}
		
	}

}
