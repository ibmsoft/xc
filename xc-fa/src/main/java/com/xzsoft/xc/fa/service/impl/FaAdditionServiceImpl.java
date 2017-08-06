package com.xzsoft.xc.fa.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xzsoft.xc.fa.dao.FaAdditionDao;
import com.xzsoft.xc.fa.modal.FaAddition;
import com.xzsoft.xc.fa.modal.FaConstant;
import com.xzsoft.xc.fa.modal.XcFaTrans;
import com.xzsoft.xc.fa.service.FaAdditionService;
import com.xzsoft.xc.fa.util.XcFaUtil;
import com.xzsoft.xc.gl.dao.VInterfaceDao;
import com.xzsoft.xc.gl.modal.VInterface;
import com.xzsoft.xc.gl.service.VoucherHandlerService;
import com.xzsoft.xc.util.common.XConstants;
import com.xzsoft.xc.util.service.RuleService;
import com.xzsoft.xip.framework.common.SessionVariable;
import com.xzsoft.xip.platform.util.XipUtil;

@Service("faAdditionService")
public class FaAdditionServiceImpl implements FaAdditionService {

    private static Logger log = Logger.getLogger(FaAdditionServiceImpl.class);

    @Resource
    private FaAdditionDao faAdditionDao;
    
    @Resource
    private VInterfaceDao vInterfaceDao;
    
    @Resource
    private RuleService ruleService;
    
    @Resource
    private VoucherHandlerService voucherHandlerService;

	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public FaAddition saveFaAddition(FaAddition faAddition, HashMap<String, Object> extraParams) throws Exception {
		String lang = extraParams.get("language").toString();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if("_added".equalsIgnoreCase(faAddition.getOperate())){
			/*String year = Calendar.getInstance().get(Calendar.YEAR) + "";
			
			String month = "";
			if(Calendar.getInstance().get(Calendar.MONTH) + 1 < 10){
				month = "0" + (Calendar.getInstance().get(Calendar.MONTH) + 1);
			}else{
				month = Calendar.getInstance().get(Calendar.MONTH) + 1 + "";
			}*/
			String period = faAddition.getPeriodCode();
			String year = period.substring(0, 4);
			String month = period.substring(5, 7);
			
			net.sf.json.JSONObject json = faAdditionDao.getFaParams(faAddition.getLedgerId());
			faAddition.setAssetId(UUID.randomUUID().toString());
			faAddition.setZcbm(ruleService.getNextSerialNum(json.getString("zc"), faAddition.getLedgerId(), "", year, month, extraParams.get(SessionVariable.userId).toString()));
			faAddition.setCreatedBy(extraParams.get("userId").toString());
			faAddition.setCreationDate(new Date());
			faAddition.setTyqj(XcFaUtil.calcEndPeriod(faAddition.getQyrq().substring(0,7), faAddition.getTxgz(), faAddition.getSynx()));
			if("I".equalsIgnoreCase(faAddition.getLrfs())){
				faAddition.setKsjtqj(XcFaUtil.calcEndPeriod(faAddition.getQyrq().substring(0,7), faAddition.getTxgz(), 1));
			}else{
				faAddition.setKsjtqj(XcFaUtil.calcEndPeriod(faAddition.getPeriodCode(), faAddition.getTxgz(), 1));
			}
			faAddition.setYjtje(0);
			faAdditionDao.insertFaAddition(faAddition);
			
			// 事务表写数据
			XcFaTrans xcFaTran = new XcFaTrans();
			xcFaTran.setAssetId(faAddition.getAssetId());
			xcFaTran.setLedgerId(faAddition.getLedgerId());
			xcFaTran.setPeriodCode(faAddition.getPeriodCode());
			
			if("I".equalsIgnoreCase(faAddition.getLrfs())){
				xcFaTran.setTransCode(FaConstant.TRANS_TYPE_CSHZC);
				HashMap<String, String> tokens = new HashMap<String, String>();
				tokens.put("assetCode", faAddition.getZcbm());
				xcFaTran.setBzsm(XipUtil.getMessage(lang, "XC_FA_INIT", tokens));
				xcFaTran.setTransDate(sdf.parse(faAddition.getPeriodCode()+"-01"));
			}else{
				xcFaTran.setTransCode(FaConstant.TRANS_TYPE_XZZC);
				HashMap<String, String> tokens = new HashMap<String, String>();
				tokens.put("assetCode", faAddition.getZcbm());
				xcFaTran.setBzsm(XipUtil.getMessage(lang, "XC_FA_ADD", tokens));
				xcFaTran.setTransDate(sdf.parse(faAddition.getQyrq()));
			}
			xcFaTran.setSrcId("");
			xcFaTran.setSrcDtlId("");
			xcFaTran.setPostFlag("N");
			xcFaTran.setCreatedBy(extraParams.get(SessionVariable.userId).toString());
			xcFaTran.setCreationDate(new Date());
			xcFaTran.setLastUpdateBy(extraParams.get(SessionVariable.userId).toString());
			xcFaTran.setLastUpdateDate(new Date());
			xcFaTran.setCatCode(faAddition.getCatCode());
			xcFaTran.setCbzx(faAddition.getCbzx());
			
			xcFaTran.setQuantity(faAddition.getSl());
			xcFaTran.setYzdr(faAddition.getYz());
			xcFaTran.setLjzjcr(faAddition.getLjzjLjtx());
			xcFaTran.setJzzbcr(faAddition.getJzzb());
			
			faAdditionDao.insertFaTrans(xcFaTran);
			 
			//资产管理新增同步添加会计凭证
			if("M".equals(faAddition.getLrfs())){
				
				VInterface vInterface=new VInterface();
				vInterface.setvInterfaceId(UUID.randomUUID().toString());
				vInterface.setLedgerId(faAddition.getLedgerId());
				vInterface.setPeriodCode(faAddition.getPeriodCode());
				vInterface.setTransCode(FaConstant.TRANS_TYPE_XZZC);
				vInterface.setDeptId(faAddition.getCbzx());
				vInterface.setProjectId(faAddition.getProjectId());
				
				HashMap<String, String> segments = new HashMap<String, String>();
				if(faAddition.getCbzx()!=null && !"".equals(faAddition.getCbzx())){
					segments.put(XConstants.XIP_PUB_DETPS, faAddition.getCbzx());
				}
				if(faAddition.getProjectId()!=null && !"".equals(faAddition.getProjectId())){
					segments.put(XConstants.XC_PM_PROJECTS, faAddition.getProjectId());
				}
				
				vInterface.setDrAcct(faAddition.getZckm());
				// 查询科目id
				String drKmId = faAdditionDao.getKmIdByCode(faAddition.getZckm(), faAddition.getLedgerId());
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
				
				String crAcct = faAdditionDao.getZjfsKmByAsset(faAddition.getAssetId());
				vInterface.setCrAcct(crAcct);
				if(!"".equals(crAcct)){
					// 查询科目id
					String crKmId = faAdditionDao.getKmIdByCode(crAcct, faAddition.getLedgerId());
					// 查询科目组id
					if(drKmId!=null){
						try{
							JSONObject crKmObject = voucherHandlerService.handlerCCID(faAddition.getLedgerId(), crKmId, segments);
							if(crKmObject!=null){
								vInterface.setCrCcid(crKmObject.getString("ccid"));
							}
						}catch(Exception e){
							vInterface.setCrCcid("");
						}
					}
				}
				
				vInterface.setAmt(Double.valueOf(faAddition.getYz()).doubleValue());
				
				HashMap<String, String> tokens = new HashMap<String, String>();
				tokens.put("assetCode", faAddition.getZcbm());
				vInterface.setSummary(XipUtil.getMessage(lang, "XC_FA_ADD", tokens));
				
				vInterface.setBusNo(faAddition.getZcbm());
				vInterface.setSrcHid(faAddition.getAssetId());
				vInterface.setCreatedBy(extraParams.get("userId").toString());
				vInterface.setCreationDate(new Date());
				vInterface.setLastUpdateBy(extraParams.get(SessionVariable.userId).toString());
				vInterface.setLastUpdateDate(new Date());
				vInterfaceDao.insertVInterface(vInterface);
			}
		}else{
			//删除glInterface前判断是否已经导入总账
			List<VInterface> oldVInterfaces = vInterfaceDao.getVInterfaceBySrcHid(faAddition.getAssetId());
			
			if(oldVInterfaces!=null && oldVInterfaces.size()>0){
				for(VInterface oldVInterface : oldVInterfaces){
					if(oldVInterface.getiHeadId()!=null && !"".equals(oldVInterface.getiHeadId())){
						HashMap<String, String> tokens = new HashMap<String, String>();
						tokens.put("assetCode", faAddition.getZcbm());
						String excepitionMsg = XipUtil.getMessage(lang, "XC_FA_CHECK_DELETE_OF_IMP_GL", tokens);
						throw new Exception(excepitionMsg);
					}
				}
			}
			
			vInterfaceDao.deleteVInterfaceBySrcHid(faAddition.getAssetId());
			
			//删除事务
			/**if("I".equalsIgnoreCase(faAddition.getLrfs())){
				faAdditionDao.deleteFaTrans(faAddition.getAssetId(), FaConstant.TRANS_TYPE_CSHZC);
			}else{
				faAdditionDao.deleteFaTrans(faAddition.getAssetId(), FaConstant.TRANS_TYPE_XZZC);
			}**/
			//新增事务
			XcFaTrans xcFaTran = new XcFaTrans();
			xcFaTran.setAssetId(faAddition.getAssetId());
			xcFaTran.setLedgerId(faAddition.getLedgerId());
			xcFaTran.setPeriodCode(faAddition.getPeriodCode());
			if("I".equalsIgnoreCase(faAddition.getLrfs())){
				xcFaTran.setTransCode(FaConstant.TRANS_TYPE_CSHZC);
				HashMap<String, String> tokens = new HashMap<String, String>();
				tokens.put("assetCode", faAddition.getZcbm());
				xcFaTran.setBzsm(XipUtil.getMessage(lang, "XC_FA_INIT", tokens));
				xcFaTran.setTransDate(sdf.parse(faAddition.getPeriodCode()+"-01"));
			}else{
				xcFaTran.setTransCode(FaConstant.TRANS_TYPE_XZZC);
				HashMap<String, String> tokens = new HashMap<String, String>();
				tokens.put("assetCode", faAddition.getZcbm());
				xcFaTran.setBzsm(XipUtil.getMessage(lang, "XC_FA_ADD", tokens));
				xcFaTran.setTransDate(sdf.parse(faAddition.getQyrq()));
			}
			xcFaTran.setSrcId("");
			xcFaTran.setSrcDtlId("");
			xcFaTran.setPostFlag("N");
			xcFaTran.setCreatedBy(extraParams.get(SessionVariable.userId).toString());
			xcFaTran.setCreationDate(new Date());
			xcFaTran.setLastUpdateBy(extraParams.get(SessionVariable.userId).toString());
			xcFaTran.setLastUpdateDate(new Date());
			xcFaTran.setCatCode(faAddition.getCatCode());
			xcFaTran.setCbzx(faAddition.getCbzx());
			
			xcFaTran.setQuantity(faAddition.getSl());
			xcFaTran.setYzdr(faAddition.getYz());
			xcFaTran.setLjzjcr(faAddition.getLjzjLjtx());
			xcFaTran.setJzzbcr(faAddition.getJzzb());
			
			//faAdditionDao.insertFaTrans(xcFaTran);
			faAdditionDao.updateFaTrans(xcFaTran);
			
			faAddition.setLastUpdatedBy(extraParams.get("userId").toString());
			faAddition.setLastUpdateDate(new Date());
			faAddition.setTyqj(XcFaUtil.calcEndPeriod(faAddition.getQyrq().substring(0,7), faAddition.getTxgz(), faAddition.getSynx()));
			if("I".equalsIgnoreCase(faAddition.getLrfs())){
				faAddition.setKsjtqj(XcFaUtil.calcEndPeriod(faAddition.getQyrq().substring(0,7), faAddition.getTxgz(), 1));
			}else{
				faAddition.setKsjtqj(XcFaUtil.calcEndPeriod(faAddition.getPeriodCode(), faAddition.getTxgz(), 1));
			}
			faAddition.setYjtje(0);
			faAdditionDao.updateFaAddition(faAddition);
			
			//资产管理新增同步添加会计凭证
			if("M".equals(faAddition.getLrfs())){
				VInterface vInterface=new VInterface();
				vInterface.setvInterfaceId(UUID.randomUUID().toString());
				vInterface.setLedgerId(faAddition.getLedgerId());
				vInterface.setPeriodCode(faAddition.getPeriodCode());
				vInterface.setTransCode(FaConstant.TRANS_TYPE_XZZC);
				vInterface.setDeptId(faAddition.getCbzx());
				vInterface.setProjectId(faAddition.getProjectId());
				vInterface.setDrAcct(faAddition.getZckm());
				vInterface.setAmt(Double.valueOf(faAddition.getYz()).doubleValue());
				
				HashMap<String, String> segments = new HashMap<String, String>();
				if(faAddition.getCbzx()!=null && !"".equals(faAddition.getCbzx())){
					segments.put(XConstants.XIP_PUB_DETPS, faAddition.getCbzx());
				}
				if(faAddition.getProjectId()!=null && !"".equals(faAddition.getProjectId())){
					segments.put(XConstants.XC_PM_PROJECTS, faAddition.getProjectId());
				}
				
				// 查询科目id
				String drKmId = faAdditionDao.getKmIdByCode(faAddition.getZckm(), faAddition.getLedgerId());
				// 查询科目组id
				if(drKmId!=null){
					JSONObject drKmObject = voucherHandlerService.handlerCCID(faAddition.getLedgerId(), drKmId, segments);
					if(drKmObject!=null){
						vInterface.setDrCcid(drKmObject.getString("ccid"));
					}
				}
				
				HashMap<String, String> tokens = new HashMap<String, String>();
				tokens.put("assetCode", faAddition.getZcbm());
				vInterface.setSummary(XipUtil.getMessage(lang, "XC_FA_EDIT", tokens));
				vInterface.setBusNo(faAddition.getZcbm());
				vInterface.setSrcHid(faAddition.getAssetId());
				vInterface.setCreatedBy(extraParams.get("userId").toString());
				vInterface.setCreationDate(new Date());
				vInterface.setLastUpdateBy(extraParams.get(SessionVariable.userId).toString());
				vInterface.setLastUpdateDate(new Date());
				vInterfaceDao.insertVInterface(vInterface);
			}
		}
		
		return faAddition;
	}
	
}
