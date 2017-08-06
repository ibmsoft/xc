package com.xzsoft.xc.fa.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xzsoft.xc.fa.dao.FaAdditionDao;
import com.xzsoft.xc.fa.dao.FaDivisionDao;
import com.xzsoft.xc.fa.modal.FaAddition;
import com.xzsoft.xc.fa.modal.FaConstant;
import com.xzsoft.xc.fa.modal.FaDivision;
import com.xzsoft.xc.fa.modal.FaDivisionH;
import com.xzsoft.xc.fa.modal.FaDivisionL;
import com.xzsoft.xc.fa.modal.XcFaTrans;
import com.xzsoft.xc.fa.service.FaDepreciationService;
import com.xzsoft.xc.fa.service.FaDivisionService;
import com.xzsoft.xc.fa.util.XcFaUtil;
import com.xzsoft.xc.gl.dao.VInterfaceDao;
import com.xzsoft.xc.gl.modal.VInterface;
import com.xzsoft.xc.util.service.RuleService;
import com.xzsoft.xip.framework.common.SessionVariable;
import com.xzsoft.xip.platform.util.XipUtil;

@Service("faDivisionService")
public class FaDivisionServiceImpl  implements  FaDivisionService{
	
	 private static Logger log = Logger.getLogger(FaDivisionServiceImpl.class);
	
	@Resource
    private FaDivisionDao faDivisionDao;
	@Resource
	private FaAdditionDao faAdditionDao;
	@Resource
    private RuleService ruleService;
	@Resource
    private FaDepreciationService faDepreciationService;
	@Resource
    private VInterfaceDao vInterfaceDao;
	
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void saveFaDivision(FaDivision faDivision, HashMap<String, Object> extraParams) throws Exception  {
		/*String year = Calendar.getInstance().get(Calendar.YEAR) + "";
		String month = Calendar.getInstance().get(Calendar.MONTH) + 1 + "";*/
		String lang = extraParams.get("language").toString();
		
		// 保存资产拆分头表信息
		FaDivisionH faDivisionH = faDivision.getFaDivisionH();
		String period = faDivisionH.getPeriodCode();
		String year = period.substring(0, 4);
		String month = period.substring(5, 7);
		
		//if("Y".equals(faDivision.getIsJt())){
		net.sf.json.JSONObject returnJson = faDepreciationService.cancelDepr(faDivisionH.getAssetId(), faDivisionH.getPeriodCode(), lang, extraParams.get(SessionVariable.userId).toString());
		if(returnJson.get("flag")!=null && "1".equals(returnJson.getString("flag"))){
			throw new Exception(returnJson.get("msg")==null ? "" : returnJson.getString("msg"));
		}
		//}
	    
		faDivisionH.setCreatedBy(extraParams.get(SessionVariable.userId).toString());
		faDivisionH.setCreationDate(new Date());
		faDivisionH.setLastUpdatedBy(extraParams.get(SessionVariable.userId).toString());
		faDivisionH.setLastUpdateDate(new Date());
		faDivisionDao.insertfaDivision(faDivisionH);
		
		//更新资产信息
		FaAddition faAddition = faDivision.getFaAdditionOne();
		faAddition.setLastUpdatedBy(extraParams.get(SessionVariable.userId).toString());
		faAddition.setLastUpdateDate(new Date());
		faDivisionDao.updateFaAddition(faAddition);
		
		FaAddition hFaAddition = faAdditionDao.getFaAdditionWithId(faAddition.getAssetId());
		
		StringBuilder sb = new StringBuilder();//资产编码集合字符串
		StringBuilder uuid = new StringBuilder();//拆分子表id集合字符串
		StringBuilder assetUuid = new StringBuilder();//拆分子表id集合字符串
		for( int i = 0;i<faDivision.getCountFa();i++){
			sb.append(ruleService.getNextSerialNum("FA_AS", faDivisionH.getLedgerId(), "", year, month, extraParams.get(SessionVariable.userId).toString())).append("/");
			uuid.append(UUID.randomUUID().toString()).append("/");
			assetUuid.append(UUID.randomUUID().toString()).append("/");	
	    }
		
		int index =0;
		//生成编码存放到数组中用index做编号
		String [] strs = sb.substring(0, sb.length()-1).toString().split("[/]");
		String [] uuids = uuid.substring(0, uuid.length()-1).toString().split("[/]");
		String [] assetUuids = uuid.substring(0, assetUuid.length()-1).toString().split("[/]");
		
		double divisionAmt = 0;
		double divisionYz = 0;
		double divisionLjzj = 0;
		double divisionJzzb = 0;
		
		// 保存资产拆分行表信息
		if(faDivision.getFaDivisionLs()!=null && faDivision.getFaDivisionLs().size()>0){
			for(FaDivisionL faDivisionL : faDivision.getFaDivisionLs()){
				faDivisionL.setDivDtlId(uuids[index]);
				faDivisionL.setZcbm(strs[index]);
				faDivisionL.setAssetId(assetUuids[index]);
				//循环结束index赋初值给下一个循环使用
				if(index<faDivision.getCountFa()){
					index++;
				}else{
					index=0;
				}
				faDivisionL.setDivisionId(faDivisionH.getDivisionId());
				faDivisionL.setCreatedBy(extraParams.get(SessionVariable.userId).toString());
				faDivisionL.setCreationDate(new Date( ));
				faDivisionL.setLastUpdatedBy(extraParams.get(SessionVariable.userId).toString());
				faDivisionL.setLastUpdateDate(new Date());
			}
			
			index= 0;
			faDivisionDao.insertFaDivisionL(faDivision.getFaDivisionLs());
			
			//保存资产信息
			if(faDivision.getFaAdditions()!=null && faDivision.getFaAdditions().size()>0){
				for(FaAddition faAdditions : faDivision.getFaAdditions()){
					faAdditions.setAssetId(assetUuids[index]);
				    faAdditions.setZcbm(strs[index]);
					faAdditions.setCreatedBy(extraParams.get(SessionVariable.userId).toString());
					faAdditions.setCreationDate(new Date());
					faAdditions.setTyqj(hFaAddition.getTyqj());
					/*if("I".equalsIgnoreCase(faAdditions.getLrfs())){
						faAdditions.setKsjtqj(faAdditions.getPeriodCode());
					}else{
						faAdditions.setKsjtqj(XcFaUtil.calcEndPeriod(faAdditions.getPeriodCode(), faAdditions.getTxgz(), 1));
					}*/
					faAdditions.setKsjtqj(hFaAddition.getKsjtqj());
					faAdditions.setYjtje(0);
					faAdditions.setJtqj(hFaAddition.getJtqj());
					
					double jz = faAdditions.getYz() - faAdditions.getYz() * faAdditions.getCzl() / 100 - faAdditions.getJzzb() - faAdditions.getLjzjLjtx(); 
					faAdditions.setJz(jz);
					
					divisionAmt = divisionAmt + faAdditions.getSl();
					divisionYz = divisionYz + faAdditions.getYz();
					divisionLjzj = divisionLjzj + faAdditions.getLjzjLjtx();
					divisionJzzb = divisionJzzb + faAdditions.getJzzb();
					
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					
					//同步资产到资产事务 ，保存行表拆分资产
					XcFaTrans xcFaTransDtl= new XcFaTrans();
					xcFaTransDtl.setAssetId(faAdditions.getAssetId());
					xcFaTransDtl.setLedgerId(faDivisionH.getLedgerId());
					xcFaTransDtl.setPeriodCode(faDivision.getFaDivisionH().getPeriodCode());
					xcFaTransDtl.setTransDate(sdf.parse(faDivision.getFaDivisionH().getDivisionDate()));
					xcFaTransDtl.setTransCode(FaConstant.TRANS_TYPE_CF);
					xcFaTransDtl.setSrcId(faDivisionH.getDivisionId());
					xcFaTransDtl.setSrcDtlId(uuids[index]);
					HashMap<String, String> tokens = new HashMap<String, String>();
					tokens.put("assetCode", faAdditions.getZcbm());
					xcFaTransDtl.setBzsm(XipUtil.getMessage(lang, "XC_FA_DIVISION_ADD", tokens));
					xcFaTransDtl.setPostFlag("N");
					xcFaTransDtl.setCreatedBy(extraParams.get(SessionVariable.userId).toString());
					xcFaTransDtl.setCreationDate(new Date());
					xcFaTransDtl.setLastUpdateBy(extraParams.get(SessionVariable.userId).toString());
					xcFaTransDtl.setLastUpdateDate(new Date());
					xcFaTransDtl.setCatCode(hFaAddition.getCatCode());
					xcFaTransDtl.setCbzx(hFaAddition.getCbzx());
					
					xcFaTransDtl.setQuantity(faAdditions.getSl());
					xcFaTransDtl.setYzdr(faAdditions.getYz());
					xcFaTransDtl.setLjzjcr(faAdditions.getLjzjLjtx());
					xcFaTransDtl.setJzzbcr(faAdditions.getJzzb());
					
					//循环结束index赋初值给下一个循环使用
					if(index<faDivision.getCountFa()){
						index++;
					}else{
						index=0;
					}
					 //单条新增资产插入资产事务表中  
					faAdditionDao.insertFaTrans(xcFaTransDtl);
				}
			faDivisionDao.insertFaAddition(faDivision.getFaAdditions());
			}
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//同步资产到资产事务 ，保存头表拆分资产
		XcFaTrans xcFaTrans= new XcFaTrans();
		xcFaTrans.setAssetId(faDivisionH.getAssetId());
		xcFaTrans.setLedgerId(faDivisionH.getLedgerId());
		xcFaTrans.setPeriodCode(faDivisionH.getPeriodCode());
		xcFaTrans.setTransDate(sdf.parse(faDivisionH.getPeriodCode()+"-01"));
		xcFaTrans.setTransCode(FaConstant.TRANS_TYPE_CF);
		xcFaTrans.setSrcId(faDivisionH.getDivisionId());
		HashMap<String, String> tokens = new HashMap<String, String>();
		tokens.put("assetCode", faDivision.getFaAdditionOne().getZcbm());
		xcFaTrans.setBzsm(XipUtil.getMessage(lang, "XC_FA_DIVISION", tokens));
		xcFaTrans.setPostFlag("N");
		xcFaTrans.setCreatedBy(extraParams.get(SessionVariable.userId).toString());
		xcFaTrans.setCreationDate(new Date());
		xcFaTrans.setLastUpdateBy(extraParams.get(SessionVariable.userId).toString());
		xcFaTrans.setLastUpdateDate(new Date());
		xcFaTrans.setCatCode(hFaAddition.getCatCode());
		xcFaTrans.setCbzx(hFaAddition.getCbzx());
		
		xcFaTrans.setQuantity(faAddition.getSl() - faDivisionH.getSl());
		xcFaTrans.setYzcr(divisionYz);
		xcFaTrans.setLjzjdr(divisionLjzj);
		xcFaTrans.setJzzbdr(divisionJzzb);
		
		faAdditionDao.insertFaTrans(xcFaTrans);
	}
	
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void deleteFaDivision(FaDivision faDivision, HashMap<String, Object> extraParams) throws Exception {
		String lang = extraParams.get("language").toString();
		String userId = extraParams.get(SessionVariable.userId).toString();
		
		int checkSubFaDivision = faDivisionDao.checkSubFaDivision(faDivision.getFaDivisionH().getDivisionId());
		if(checkSubFaDivision>0){
			throw new Exception(XipUtil.getMessage(lang, "XC_FA_DIVISION_CHECK_SUB_FA_DIVISION", null));
		}
		
		int checkFaInPeriod = faDivisionDao.checkFaInPeriod(faDivision.getFaDivisionH().getDivisionId());
		if(checkFaInPeriod==0){
			throw new Exception(XipUtil.getMessage(lang, "XC_FA_DIVISION_CHECK_FA_IN_PERIOD", null));
		}
		
		//根据DivisonId查询资产集合
		List<FaAddition> faAdditions = faDivisionDao.findAssetIdByDivisonId(faDivision.getFaDivisionH().getDivisionId());
		
		net.sf.json.JSONObject returnJson = faDepreciationService.cancelDepr(faDivision.getFaDivisionH().getAssetId(), faDivision.getFaDivisionH().getPeriodCode(), lang, userId);
		if(returnJson.get("flag")!=null && "1".equals(returnJson.getString("flag"))){
			throw new Exception(returnJson.get("msg")==null ? "" : returnJson.getString("msg"));
		}
		
		FaAddition hFaAddition = faAdditionDao.getFaAdditionWithId(faDivision.getFaDivisionH().getAssetId());
		
		//还原资产信息
		FaAddition faAddition = new FaAddition();
		faAddition.setAssetId(faDivision.getFaDivisionH().getAssetId());
		faAddition.setYz(faDivision.getFaDivisionH().getYz());
		faAddition.setLjzjLjtx(faDivision.getFaDivisionH().getLjzjljtx());
		faAddition.setSl(faDivision.getFaDivisionH().getSl());
		faAddition.setJzzb(faDivision.getFaDivisionH().getJzzb());
		faAddition.setJz(faDivision.getFaDivisionH().getYz() - faDivision.getFaDivisionH().getLjzjljtx() - faDivision.getFaDivisionH().getJzzb() - faDivision.getFaDivisionH().getYz() * hFaAddition.getCzl() / 100);
		faAddition.setLastUpdatedBy(extraParams.get(SessionVariable.userId).toString());
		faAddition.setLastUpdateDate(new Date());
		faDivisionDao.updateFaAddition(faAddition);
		
		//删除拆分资产头表
		faDivisionDao.deleteFaDivistionH(faDivision.getFaDivisionH().getDivisionId());
		
		//删除拆分资产行表
		faDivisionDao.deleteFaDivistionL(faDivision.getFaDivisionH().getDivisionId());
	
		//删除新增资产
		if(faAdditions!=null && faAdditions.size()>0){
			for(FaAddition faAdd : faAdditions){
				//校验生成凭证不能删除
				List<VInterface> oldVInterfaces = vInterfaceDao.getVInterfaceBySrcHid(faAdd.getAssetId());
				if(oldVInterfaces.size()>0){
					HashMap<String, String> tokens = new HashMap<String, String>();
					tokens.put("assetCode", faAdd.getZcbm());
					throw new Exception(XipUtil.getMessage(lang, "XC_FA_DIVISION_CHECK_SUB_FA_IMP_GL", tokens));
				}
				
				//校验调整过不能删除
				if(faAdditionDao.checkFaAdj(faAdd.getAssetId()) > 0){
					HashMap<String, String> tokens = new HashMap<String, String>();
					tokens.put("assetCode", faAdd.getZcbm());
					throw new Exception(XipUtil.getMessage(lang, "XC_FA_DIVISION_CHECK_SUB_FA_ADJ", tokens));
				}
				
				net.sf.json.JSONObject subReturnJson = faDepreciationService.cancelDepr(faAdd.getAssetId(), faDivision.getFaDivisionH().getPeriodCode(), lang, userId);
				if(subReturnJson.get("flag")!=null && "1".equals(subReturnJson.getString("flag"))){
					throw new Exception(subReturnJson.get("msg")==null ? "" : subReturnJson.getString("msg"));
				}
				
				/**
				//删除事务表
				faAdditionDao.deleteFaTransByAssetId(faAdd.getAssetId());
				
				//删除会计平台
				faAdditionDao.deleteGlInterfaceByAsset(faAdd.getAssetId());
				
				//删除折旧表
				faAdditionDao.delFaDeprWithId(faAdd.getAssetId());
				
				//删除历史表
				faAdditionDao.deleteFaHistoryByAssetId(faAdd.getAssetId());**/
				
				//删除资产
				faAdditionDao.deleteFaAddition(faAdd.getAssetId());	
			}	
		}
		
		//删除资产事务
		faDivisionDao.deleteXcFaTrans(faDivision.getFaDivisionH().getDivisionId());
	}
	
}
