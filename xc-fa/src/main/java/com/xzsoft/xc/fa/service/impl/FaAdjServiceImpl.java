package com.xzsoft.xc.fa.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xzsoft.xc.fa.dao.FaAdditionDao;
import com.xzsoft.xc.fa.dao.FaAdjDao;
import com.xzsoft.xc.fa.modal.FaAddition;
import com.xzsoft.xc.fa.modal.FaAdj;
import com.xzsoft.xc.fa.modal.FaAdjH;
import com.xzsoft.xc.fa.modal.FaAdjL;
import com.xzsoft.xc.fa.modal.FaConstant;
import com.xzsoft.xc.fa.modal.XcFaTrans;
import com.xzsoft.xc.fa.service.FaAdjService;
import com.xzsoft.xc.fa.service.FaDepreciationService;
import com.xzsoft.xc.fa.util.XcFaUtil;
import com.xzsoft.xc.gl.dao.VInterfaceDao;
import com.xzsoft.xc.gl.modal.VInterface;
import com.xzsoft.xc.gl.service.VoucherHandlerService;
import com.xzsoft.xc.util.common.XConstants;
import com.xzsoft.xc.util.service.RuleService;
import com.xzsoft.xip.framework.common.SessionVariable;
import com.xzsoft.xip.platform.util.XipUtil;

@Service("faAdjService")
public class FaAdjServiceImpl implements FaAdjService {

    private static Logger log = Logger.getLogger(FaAdjServiceImpl.class);

    @Resource
    private FaAdjDao faAdjDao;
    
    @Resource
    private RuleService ruleService;
    
    @Resource
    private FaAdditionDao faAdditionDao;
    
    @Resource
    private VInterfaceDao vInterfaceDao;
    
    @Resource
    private VoucherHandlerService voucherHandlerService;
    
    @Resource
    private FaDepreciationService faDepreciationService;

	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void saveFaAdj(FaAdj faAdj, HashMap<String, Object> extraParams)
			throws Exception {
		/*String year = Calendar.getInstance().get(Calendar.YEAR) + "";
		
		String month = "";
		if(Calendar.getInstance().get(Calendar.MONTH) + 1 < 10){
			month = "0" + (Calendar.getInstance().get(Calendar.MONTH) + 1);
		}else{
			month = Calendar.getInstance().get(Calendar.MONTH) + 1 + "";
		}*/
		
		String userId = extraParams.get(SessionVariable.userId).toString();
		String lang = extraParams.get("language").toString();
		String year = faAdj.getFaAdjH().getAdjDate().substring(0, 4);
		String month = faAdj.getFaAdjH().getAdjDate().substring(5, 7);
		
		net.sf.json.JSONObject json = faAdditionDao.getFaParams(faAdj.getFaAdjH().getLedgerId());
		
		// 保存头信息
		FaAdjH faAdjH = faAdj.getFaAdjH();
		if("_added".equals(faAdj.getOpType())){
			faAdjH.setDocNum(ruleService.getNextSerialNum(json.getString("tz"), faAdjH.getLedgerId(), "", year, month, userId));
			faAdjH.setCreatedBy(extraParams.get(SessionVariable.userId).toString());
			faAdjH.setCreationDate(new Date());
			faAdjH.setLastUpdatedBy(userId);
			faAdjH.setLastUpdateDate(new Date());
			faAdjH.setAuditStatus("C");
			faAdjDao.insertFaAdjH(faAdjH);
		}else{
			faAdjH.setLastUpdatedBy(userId);
			faAdjH.setLastUpdateDate(new Date());
			faAdjDao.updateFaAdjH(faAdjH);
		}
		
		// 保存行信息
		if(faAdj.getFaAdjLs()!=null && faAdj.getFaAdjLs().size()>0){
			
			List<FaAdjL> insertFaAdjLs = new ArrayList<FaAdjL>();
			List<FaAdjL> updateFaAdjLs = new ArrayList<FaAdjL>();
			
			for(FaAdjL faAdjL : faAdj.getFaAdjLs()){
				// 相同会计期做过计提后调整不能再做计提前调整
				int adj4After = faAdjDao.checkAdj4After(faAdjH.getAdjDate(), faAdjL.getAssetId());
				if("B".equals(faAdjH.getJtzjType()) && adj4After>0){
					HashMap<String, String> tokens = new HashMap<String, String>();
					tokens.put("assetCode", faAdjL.getAssetCode());
					String excepitionMsg = XipUtil.getMessage(lang, "XC_FA_CHECK_ADJ_4_AFTER", tokens);
					throw new Exception(excepitionMsg);
				}
				
				if("".equals(faAdjL.getAdjDid()) || faAdjL.getAdjDid()==null){
					String zcmc = faAdjDao.checkFaHasBeenAdj(faAdjH.getAdjDate(), faAdjL.getAdjType(), faAdjL.getAssetId(), "C");
					if(zcmc!=null && !"".equals(zcmc)){
						HashMap<String, String> tokens = new HashMap<String, String>();
						tokens.put("assetCode", faAdjL.getAssetCode());
						tokens.put("adjTypeName", faAdjL.getAdjViewType());
						String excepitionMsg = XipUtil.getMessage(lang, "XC_FA_CHECK_ADJ", tokens);
						throw new Exception(excepitionMsg);
					}
					
					faAdjL.setAdjDid(UUID.randomUUID().toString());
					faAdjL.setAdjHid(faAdjH.getAdjHid());
					faAdjL.setCreatedBy(userId);
					faAdjL.setCreationDate(new Date());
					faAdjL.setLastUpdatedBy(userId);
					faAdjL.setLastUpdateDate(new Date());
					faAdjL.setAdjSource("U");
					insertFaAdjLs.add(faAdjL);
				}else{
					String zcmc = faAdjDao.checkFaHasBeenAdjUpdate(faAdjH.getAdjDate(), faAdjL.getAdjType(), faAdjL.getAssetId(), faAdjL.getAdjDid());
					if(zcmc!=null && !"".equals(zcmc)){
						HashMap<String, String> tokens = new HashMap<String, String>();
						tokens.put("assetCode", faAdjL.getAssetCode());
						tokens.put("adjTypeName", faAdjL.getAdjViewType());
						String excepitionMsg = XipUtil.getMessage(lang, "XC_FA_CHECK_ADJ", tokens);
						throw new Exception(excepitionMsg);
					}
					faAdjL.setLastUpdatedBy(userId);
					faAdjL.setLastUpdateDate(new Date());
					updateFaAdjLs.add(faAdjL);
				}
			}
			
			if(insertFaAdjLs.size()>0){
				faAdjDao.insertFaAdjL(insertFaAdjLs);
			}
			
			if(updateFaAdjLs.size()>0){
				faAdjDao.updateFaAdjL(updateFaAdjLs);
			}
		}
		
	}
	
	/**
	 * newFaAdjL:(新建资产调整)
	 *
	 * @param assetId 资产id
	 * @param adjType 调整列
	 * @param newVal 新值
	 * @param userId 用户id
	 * @return
	 * @author q p
	 */
	private FaAdjL newFaAdjL(FaAdjL faAdjL, String adjType, String oldVal, String newVal, String userId){
		FaAdjL newFaAdj = new FaAdjL();
		
		newFaAdj.setAdjDid(UUID.randomUUID().toString());
		newFaAdj.setAdjHid(faAdjL.getAdjHid());
		newFaAdj.setAssetId(faAdjL.getAssetId());
		newFaAdj.setAdjType(adjType);
		newFaAdj.setOldVal(oldVal);
		newFaAdj.setNewVal(newVal);
		newFaAdj.setCreatedBy(userId);
		newFaAdj.setCreationDate(new Date());
		newFaAdj.setLastUpdatedBy(userId);
		newFaAdj.setLastUpdateDate(new Date());
		newFaAdj.setAdjSource("S");
		
		return newFaAdj;
	}
	
	/**
	 * getNextPeriod:(获取当前期间的下一个期间)
	 *
	 * @param currentPeriod 当前期间
	 * @param ledgerId 账簿id
	 * @return
	 * @author q p
	 */
	private String getNextPeriod(String currentPeriod, String ledgerId){
		//查询账簿下所有的期间
		List<String> periods = faAdjDao.getAllPeriodsByLedger(ledgerId);
		
		int index = periods.indexOf(currentPeriod);
		if(index!=-1){
			if(index+1<periods.size()){
				return periods.get(index+1);
			}
			return "";
		}
		return"";
	}

	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void submitFaAdj(String adjHid, HashMap<String, Object> extraParams)
			throws Exception {
		String userId = extraParams.get(SessionVariable.userId).toString();
		String lang = extraParams.get("language").toString();
		
		int checkFaInPeriod = faAdjDao.checkInPeriod(adjHid);
		if(checkFaInPeriod==0){
			throw new Exception(XipUtil.getMessage(lang, "XC_FA_ADJ_CHECK_IN_PERIOD", null));
		}
		
		String year = Calendar.getInstance().get(Calendar.YEAR) + "";
		String month = "";
		if(Calendar.getInstance().get(Calendar.MONTH) + 1 < 10){
			month = "0" + Calendar.getInstance().get(Calendar.MONTH) + 1;
		}else{
			month = Calendar.getInstance().get(Calendar.MONTH) + 1 + "";
		}
		
		FaAdjH faAdjH = faAdjDao.getAdjHById(adjHid);
		List<FaAdjL> faAdjLs = faAdjDao.getAdjLByHid(adjHid);
		
		faAdjDao.updateAdjHStatus(adjHid, "S");
		
		net.sf.json.JSONObject json = faAdditionDao.getFaParams(faAdjH.getLedgerId());
		
		String currentOpenPeriod = faAdjDao.getCurrentOpenedPeriod(faAdjH.getLedgerId());
		
		if(faAdjLs!=null && faAdjLs.size()>0){
			
			List<FaAdjL> resetValueAdjs = new ArrayList<FaAdjL>();
			
			HashMap<String, HashMap> jzParams = new HashMap<String, HashMap>();
			
			for(FaAdjL faAdjL : faAdjLs){
				/**
				 * 1、修改【是否折旧】、【折旧方法】时
				 * 		需要重新计算【开始计提期间】:如果开始计提期间小于当前打开的会计期则修改成当前打开的;
				 * 		需要重新计算【已计提金额】:【已计提金额】为累计折旧的金额
				 * 2、修改【是否折旧】、【折旧方法】、【使用年限】、【成本中心】时需要【清除折旧】
				 * 3、修改【使用年限】时需要重新计算【停用期间】
				 * 4、计提前调整，清除计提
				 */
				
				if("B".equals(faAdjH.getJtzjType())){
					net.sf.json.JSONObject returnJson = faDepreciationService.cancelDepr(faAdjL.getAssetId(), faAdjH.getAdjDate().substring(0, 7), lang, userId);
					if(returnJson.get("flag")!=null && "1".equals(returnJson.getString("flag"))){
						throw new Exception(returnJson.get("msg")==null ? "" : returnJson.getString("msg"));
					}
				}
				
				FaAddition faAddition = faAdditionDao.getFaAdditionWithId(faAdjL.getAssetId());
				
				if(FaConstant.TRANS_TYPE_SFZJ.equals(faAdjL.getAdjType()) || FaConstant.TRANS_TYPE_ZJFF.equals(faAdjL.getAdjType())){
					String currentOpenedPeriod = faAdjDao.getCurrentOpenedPeriod(faAdjH.getLedgerId());
					if(currentOpenedPeriod==null || "".equals(currentOpenedPeriod)){
						throw new Exception(XipUtil.getMessage(lang, "XC_FA_NOT_OPEN_PERIOD", null));
					}
					
					if(Integer.parseInt(faAddition.getKsjtqj().substring(0,4)) < Integer.parseInt(currentOpenedPeriod.substring(0,4))){
						if(Integer.parseInt(faAddition.getKsjtqj().substring(0,4)) == Integer.parseInt(currentOpenedPeriod.substring(0,4)) && Integer.parseInt(faAddition.getKsjtqj().substring(5,7)) == Integer.parseInt(currentOpenedPeriod.substring(5,7))){
							resetValueAdjs.add(newFaAdjL(faAdjL, "KSJTQJ", faAddition.getKsjtqj(), currentOpenedPeriod, userId));
						}
					}
					
					resetValueAdjs.add(newFaAdjL(faAdjL, "YJTJE", faAddition.getYjtje()+"", faAddition.getLjzjLjtx()+"", userId));
				}
				
				if(FaConstant.TRANS_TYPE_SYNX.equals(faAdjL.getAdjType())){
					resetValueAdjs.add(newFaAdjL(faAdjL, "TYQJ", faAddition.getTyqj(), XcFaUtil.calcEndPeriod(faAddition.getQyrq().substring(0,7), faAddition.getTxgz(), Integer.parseInt(faAdjL.getNewVal())), userId));
				}
				
				if(FaConstant.TRANS_TYPE_YZ.equals(faAdjL.getAdjType()) || FaConstant.TRANS_TYPE_LJZJ_LJTX.equals(faAdjL.getAdjType()) || FaConstant.TRANS_TYPE_JZZB.equals(faAdjL.getAdjType()) || FaConstant.TRANS_TYPE_CZL.equals(faAdjL.getAdjType())){
					if(jzParams.get(faAdjL.getAssetId())!=null){
						if(FaConstant.TRANS_TYPE_YZ.equalsIgnoreCase(faAdjL.getAdjType())){
							jzParams.get(faAdjL.getAssetId()).put(FaConstant.TRANS_TYPE_YZ, faAdjL.getNewVal());
						}else if(FaConstant.TRANS_TYPE_LJZJ_LJTX.equalsIgnoreCase(faAdjL.getAdjType())){
							jzParams.get(faAdjL.getAssetId()).put(FaConstant.TRANS_TYPE_LJZJ_LJTX, faAdjL.getNewVal());
						}else if(FaConstant.TRANS_TYPE_CZL.equalsIgnoreCase(faAdjL.getAdjType())){
							jzParams.get(faAdjL.getAssetId()).put(FaConstant.TRANS_TYPE_CZL, faAdjL.getNewVal());
						}else if(FaConstant.TRANS_TYPE_JZZB.equalsIgnoreCase(faAdjL.getAdjType())){
							jzParams.get(faAdjL.getAssetId()).put(FaConstant.TRANS_TYPE_JZZB, faAdjL.getNewVal());
						}
					}else{
						HashMap param = new HashMap();
						param.put("faAddition", faAddition);
						param.put("faAdjL", faAdjL);
						
						if(FaConstant.TRANS_TYPE_YZ.equalsIgnoreCase(faAdjL.getAdjType())){
							param.put(FaConstant.TRANS_TYPE_YZ, faAdjL.getNewVal());
						}else if(FaConstant.TRANS_TYPE_LJZJ_LJTX.equalsIgnoreCase(faAdjL.getAdjType())){
							param.put(FaConstant.TRANS_TYPE_LJZJ_LJTX, faAdjL.getNewVal());
						}else if(FaConstant.TRANS_TYPE_CZL.equalsIgnoreCase(faAdjL.getAdjType())){
							param.put(FaConstant.TRANS_TYPE_CZL, faAdjL.getNewVal());
						}else if(FaConstant.TRANS_TYPE_JZZB.equalsIgnoreCase(faAdjL.getAdjType())){
							param.put(FaConstant.TRANS_TYPE_JZZB, faAdjL.getNewVal());
						}
						
						jzParams.put(faAdjL.getAssetId(), param);
					}
				}
				
				Date transDate = null;
				
				// 事务表写数据
				XcFaTrans xcFaTran = new XcFaTrans();
				xcFaTran.setAssetId(faAdjL.getAssetId());
				xcFaTran.setLedgerId(faAdjH.getLedgerId());
				xcFaTran.setPeriodCode(faAdjH.getPeriodCode());
				if("B".equals(faAdjH.getJtzjType())){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					transDate = sdf.parse(faAdjH.getAdjDate());
					//xcFaTran.setTransDate(sdf.parse(faAdjH.getAdjDate()));
				}else{
					String tYear = faAdjH.getPeriodCode().substring(0, 4);
		            String tMonth = faAdjH.getPeriodCode().substring(5, 7);
		            if(!"10".equals(tMonth) && !"11".equals(tMonth) && !"12".equals(tMonth)){
		            	tMonth = faAdjH.getPeriodCode().substring(6, 7);
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
		    		transDate = lastDate;
		    		//xcFaTran.setTransDate(lastDate);
				}
				xcFaTran.setTransDate(transDate);
				xcFaTran.setTransCode(faAdjL.getAdjType());
				xcFaTran.setSrcId(faAdjH.getAdjHid());
				xcFaTran.setSrcDtlId(faAdjL.getAdjDid());
				HashMap<String, String> tokens = new HashMap<String, String>();
				tokens.put("assetCode", faAdjL.getAssetCode());
				tokens.put("adjViewType", faAdjL.getAdjViewType());
				tokens.put("adjOldVal", faAdjL.getOldViewVal());
				tokens.put("adjNewVal", faAdjL.getNewViewVal());
				String bzsm = XipUtil.getMessage(lang, "XC_FA_ADJ", tokens);
				xcFaTran.setBzsm(bzsm);
				xcFaTran.setPostFlag("N");
				xcFaTran.setCreatedBy(extraParams.get(SessionVariable.userId).toString());
				xcFaTran.setCreationDate(new Date());
				xcFaTran.setLastUpdateBy(extraParams.get(SessionVariable.userId).toString());
				xcFaTran.setLastUpdateDate(new Date());
				
				xcFaTran.setCatCode(faAddition.getCatCode());
				xcFaTran.setCbzx(faAddition.getCbzx());
				
				if(FaConstant.TRANS_TYPE_YZ.equals(faAdjL.getAdjType())){
					double adjVal = Double.parseDouble(faAdjL.getNewVal()) - faAddition.getYz();
					if(adjVal > 0){
						xcFaTran.setYzdr(adjVal);
					}else{
						xcFaTran.setYzcr(0 - adjVal);
					}
				}else if(FaConstant.TRANS_TYPE_LJZJ_LJTX.equals(faAdjL.getAdjType())){
					double adjVal = Double.parseDouble(faAdjL.getNewVal()) - faAddition.getLjzjLjtx();
					if(adjVal > 0){
						xcFaTran.setLjzjcr(adjVal);
					}else{
						xcFaTran.setLjzjdr(0 - adjVal);
					}
				}else if(FaConstant.TRANS_TYPE_JZZB.equals(faAdjL.getAdjType())){
					double adjVal = Double.parseDouble(faAdjL.getNewVal()) - faAddition.getJzzb();
					if(adjVal > 0){
						xcFaTran.setJzzbcr(adjVal);
					}else{
						xcFaTran.setYzcr(0 - adjVal);
					}
				}else if(FaConstant.TRANS_TYPE_CBZX.equals(faAdjL.getAdjType())){
					xcFaTran.setCbzx(faAdjL.getOldVal());
					xcFaTran.setCatCode(faAddition.getCatCode());
					xcFaTran.setYzcr(faAddition.getYz());
					xcFaTran.setQuantity(0 - faAddition.getSl());
					
					xcFaTran.setLjzjdr(faAddition.getLjzjLjtx());
					xcFaTran.setJzzbdr(faAddition.getJzzb());
					
					XcFaTrans newXcFaTran = new XcFaTrans();
					newXcFaTran.setAssetId(faAdjL.getAssetId());
					newXcFaTran.setLedgerId(faAdjH.getLedgerId());
					newXcFaTran.setPeriodCode(faAdjH.getPeriodCode());
					newXcFaTran.setTransCode(faAdjL.getAdjType());
					newXcFaTran.setTransDate(transDate);
					newXcFaTran.setSrcId(faAdjH.getAdjHid());
					newXcFaTran.setSrcDtlId(faAdjL.getAdjDid());
					newXcFaTran.setBzsm(bzsm);
					newXcFaTran.setPostFlag("N");
					newXcFaTran.setCreatedBy(userId);
					newXcFaTran.setCreationDate(new Date());
					newXcFaTran.setLastUpdateBy(userId);
					newXcFaTran.setLastUpdateDate(new Date());
					newXcFaTran.setCbzx(faAdjL.getNewVal());
					newXcFaTran.setCatCode(faAddition.getCatCode());
					newXcFaTran.setQuantity(faAddition.getSl());
					newXcFaTran.setYzdr(faAddition.getYz());
					newXcFaTran.setLjzjcr(faAddition.getLjzjLjtx());
					newXcFaTran.setJzzbcr(faAddition.getJzzb());
					faAdditionDao.insertFaTrans(newXcFaTran);
				}else if(FaConstant.TRANS_TYPE_CAT_CODE.equals(faAdjL.getAdjType())){
					xcFaTran.setCbzx(faAddition.getCbzx());
					xcFaTran.setCatCode(faAdjL.getOldVal());
					xcFaTran.setYzcr(faAddition.getYz());
					xcFaTran.setQuantity(0 - faAddition.getSl());
					
					xcFaTran.setLjzjdr(faAddition.getLjzjLjtx());
					xcFaTran.setJzzbdr(faAddition.getJzzb());
					
					XcFaTrans newXcFaTran = new XcFaTrans();
					newXcFaTran.setAssetId(faAdjL.getAssetId());
					newXcFaTran.setLedgerId(faAdjH.getLedgerId());
					newXcFaTran.setPeriodCode(faAdjH.getPeriodCode());
					newXcFaTran.setTransCode(faAdjL.getAdjType());
					newXcFaTran.setTransDate(transDate);
					newXcFaTran.setSrcId(faAdjH.getAdjHid());
					newXcFaTran.setSrcDtlId(faAdjL.getAdjDid());
					newXcFaTran.setBzsm(bzsm);
					newXcFaTran.setPostFlag("N");
					newXcFaTran.setCreatedBy(userId);
					newXcFaTran.setCreationDate(new Date());
					newXcFaTran.setLastUpdateBy(userId);
					newXcFaTran.setLastUpdateDate(new Date());
					newXcFaTran.setCbzx(faAddition.getCbzx());
					newXcFaTran.setCatCode(faAdjL.getNewVal());
					newXcFaTran.setQuantity(faAddition.getSl());
					newXcFaTran.setYzdr(faAddition.getYz());
					newXcFaTran.setLjzjcr(faAddition.getLjzjLjtx());
					newXcFaTran.setJzzbcr(faAddition.getJzzb());
					faAdditionDao.insertFaTrans(newXcFaTran);
				}
				
				faAdditionDao.insertFaTrans(xcFaTran);
				
				// 写凭证
				if("Y".equals(faAdjL.getVouCherFlag())){
					VInterface vInterface = new VInterface();
					vInterface.setvInterfaceId(UUID.randomUUID().toString());
					vInterface.setLedgerId(faAdjH.getLedgerId());
					vInterface.setPeriodCode(faAdjH.getPeriodCode());
					vInterface.setTransCode(faAdjL.getAdjType());
					vInterface.setDeptId(faAddition.getCbzx());
					vInterface.setProjectId(faAddition.getProjectId());
					
					HashMap<String, String> segments = new HashMap<String, String>();
					if(faAddition.getCbzx()!=null){
						segments.put(XConstants.XIP_PUB_DETPS, faAddition.getCbzx());
					}
					if(faAddition.getProjectId()!=null){
						segments.put(XConstants.XC_PM_PROJECTS, faAddition.getProjectId());
					}
					
					boolean isInterface = true;
					
					if("ZCKM".equals(faAdjL.getAdjType())){
						// 查询科目id
						String drKmId = faAdditionDao.getKmIdByCode(faAdjL.getNewVal(), faAdjH.getLedgerId());
						// 查询科目组id
						if(drKmId!=null){
							try{
								JSONObject drKmObject = voucherHandlerService.handlerCCID(faAdjH.getLedgerId(), drKmId, segments);
								if(drKmObject!=null){
									vInterface.setDrCcid(drKmObject.getString("ccid"));
								}
							}catch(Exception e){
								vInterface.setDrCcid("");
							}
						}
							
						// 查询科目id
						String crKmId = faAdditionDao.getKmIdByCode(faAdjL.getOldVal(), faAdjH.getLedgerId());
						// 查询科目组id
						if(crKmId!=null){
							try{
								JSONObject crKmObject = voucherHandlerService.handlerCCID(faAdjH.getLedgerId(), crKmId, segments);
								if(crKmObject!=null){
									vInterface.setCrCcid(crKmObject.getString("ccid"));
								}
							}catch(Exception e){
								vInterface.setCrCcid("");
							}
						}
						
						vInterface.setDrAcct(faAdjL.getNewVal());
						vInterface.setCrAcct(faAdjL.getOldVal());
						
						vInterface.setAmt(faAddition.getYz());
						if(faAddition.getYz()==0){
							isInterface = false;
						}
					}else if("LJZJTXKM".equals(faAdjL.getAdjType())){
						// 查询科目id
						String drKmId = faAdditionDao.getKmIdByCode(faAdjL.getOldVal(), faAdjH.getLedgerId());
						// 查询科目组id
						if(drKmId!=null){
							try{
								JSONObject drKmObject = voucherHandlerService.handlerCCID(faAdjH.getLedgerId(), drKmId, segments);
								if(drKmObject!=null){
									vInterface.setDrCcid(drKmObject.getString("ccid"));
								}
							}catch(Exception e){
								vInterface.setDrCcid("");
							}
						}
							
						// 查询科目id
						String crKmId = faAdditionDao.getKmIdByCode(faAdjL.getNewVal(), faAdjH.getLedgerId());
						// 查询科目组id
						if(crKmId!=null){
							try{
								JSONObject crKmObject = voucherHandlerService.handlerCCID(faAdjH.getLedgerId(), crKmId, segments);
								if(crKmObject!=null){
									vInterface.setCrCcid(crKmObject.getString("ccid"));
								}
							}catch(Exception e){
								vInterface.setCrCcid("");
							}
						}
						
						vInterface.setDrAcct(faAdjL.getOldVal());
						vInterface.setCrAcct(faAdjL.getNewVal());
						
						vInterface.setAmt(faAddition.getLjzjLjtx());
						if(faAddition.getLjzjLjtx()==0){
							isInterface = false;
						}
					}else if("JZZBKM".equals(faAdjL.getAdjType())){
						// 查询科目id
						String drKmId = faAdditionDao.getKmIdByCode(faAdjL.getOldVal(), faAdjH.getLedgerId());
						// 查询科目组id
						if(drKmId!=null){
							try{
								JSONObject drKmObject = voucherHandlerService.handlerCCID(faAdjH.getLedgerId(), drKmId, segments);
								if(drKmObject!=null){
									vInterface.setDrCcid(drKmObject.getString("ccid"));
								}
							}catch(Exception e){
								vInterface.setDrCcid("");
							}
						}
							
						// 查询科目id
						String crKmId = faAdditionDao.getKmIdByCode(faAdjL.getNewVal(), faAdjH.getLedgerId());
						// 查询科目组id
						if(crKmId!=null){
							try{
								JSONObject crKmObject = voucherHandlerService.handlerCCID(faAdjH.getLedgerId(), crKmId, segments);
								if(crKmObject!=null){
									vInterface.setCrCcid(crKmObject.getString("ccid"));
								}
							}catch(Exception e){
								vInterface.setCrCcid("");
							}
						}
						
						vInterface.setDrAcct(faAdjL.getOldVal());
						vInterface.setCrAcct(faAdjL.getNewVal());
						
						vInterface.setAmt(faAddition.getJzzb());
						if(faAddition.getJzzb()==0){
							isInterface = false;
						}
					}else if("YZ".equals(faAdjL.getAdjType())){
						String drKmId = faAdditionDao.getKmIdByCode(faAddition.getZckm(), faAdjH.getLedgerId());
						if(drKmId!=null){
							try{
								JSONObject drKmObject = voucherHandlerService.handlerCCID(faAdjH.getLedgerId(), drKmId, segments);
								if(drKmObject!=null){
									vInterface.setDrCcid(drKmObject.getString("ccid"));
								}
							}catch(Exception e){
								vInterface.setDrCcid("");
							}
						}
						
						String crKmId = faAdditionDao.getKmIdByCode(faAdjL.getDfkm(), faAdjH.getLedgerId());
						if(crKmId!=null){
							try{
								JSONObject crKmObject = voucherHandlerService.handlerCCID(faAdjH.getLedgerId(), crKmId, segments);
								if(crKmObject!=null){
									vInterface.setCrCcid(crKmObject.getString("ccid"));
								}
							}catch(Exception e){
								vInterface.setCrCcid("");
							}
						}
						
						vInterface.setDrAcct(faAddition.getZckm());
						vInterface.setCrAcct(faAdjL.getDfkm());
						
						vInterface.setAmt(Double.parseDouble(faAdjL.getNewVal()) - Double.parseDouble(faAdjL.getOldVal()));
					}else if("LJZJ_LJTX".equals(faAdjL.getAdjType())){
						String drKmId = faAdditionDao.getKmIdByCode(faAdjL.getDfkm(), faAdjH.getLedgerId());
						if(drKmId!=null){
							try{
								JSONObject drKmObject = voucherHandlerService.handlerCCID(faAdjH.getLedgerId(), drKmId, segments);
								if(drKmObject!=null){
									vInterface.setDrCcid(drKmObject.getString("ccid"));
								}
							}catch(Exception e){
								vInterface.setDrCcid("");
							}
						}
						
						String crKmId = faAdditionDao.getKmIdByCode(faAddition.getLjzjtxkm(), faAdjH.getLedgerId());
						if(crKmId!=null){
							try{
								JSONObject crKmObject = voucherHandlerService.handlerCCID(faAdjH.getLedgerId(), crKmId, segments);
								if(crKmObject!=null){
									vInterface.setCrCcid(crKmObject.getString("ccid"));
								}
							}catch(Exception e){
								vInterface.setCrCcid("");
							}
						}
						
						vInterface.setDrAcct(faAdjL.getDfkm());
						vInterface.setCrAcct(faAddition.getLjzjtxkm());
						
						vInterface.setAmt(Double.parseDouble(faAdjL.getNewVal()) - Double.parseDouble(faAdjL.getOldVal()));
					}else if("JZZB".equals(faAdjL.getAdjType())){
						String drKmId = faAdditionDao.getKmIdByCode(faAdjL.getDfkm(), faAdjH.getLedgerId());
						if(drKmId!=null){
							try{
								JSONObject drKmObject = voucherHandlerService.handlerCCID(faAdjH.getLedgerId(), drKmId, segments);
								if(drKmObject!=null){
									vInterface.setDrCcid(drKmObject.getString("ccid"));
								}
							}catch(Exception e){
								vInterface.setDrCcid("");
							}
						}
							
						String crKmId = faAdditionDao.getKmIdByCode(faAddition.getJzzbkm(), faAdjH.getLedgerId());
						if(crKmId!=null){
							try{
								JSONObject crKmObject = voucherHandlerService.handlerCCID(faAdjH.getLedgerId(), crKmId, segments);
								if(crKmObject!=null){
									vInterface.setCrCcid(crKmObject.getString("ccid"));
								}
							}catch(Exception e){
								vInterface.setCrCcid("");
							}
						}
						
						vInterface.setDrAcct(faAdjL.getDfkm());
						vInterface.setCrAcct(faAddition.getJzzbkm());
						
						vInterface.setAmt(Double.parseDouble(faAdjL.getNewVal()) - Double.parseDouble(faAdjL.getOldVal()));
					} 
					
					vInterface.setSummary(bzsm);
					vInterface.setBusNo(faAdjH.getDocNum());
					vInterface.setSrcHid(faAdjH.getAdjHid());
					vInterface.setSrcLid(faAdjL.getAdjDid());
					vInterface.setCreatedBy(extraParams.get(SessionVariable.userId).toString());
					vInterface.setCreationDate(new Date());
					vInterface.setLastUpdateBy(extraParams.get(SessionVariable.userId).toString());
					vInterface.setLastUpdateDate(new Date());
					
					if(isInterface==true){
						vInterfaceDao.insertVInterface(vInterface);
					}
				}
			}
			
			// 重新计算净值
			Iterator iter = jzParams.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String key = entry.getKey().toString();
				HashMap val = (HashMap) entry.getValue();
				
				FaAddition faAddition = (FaAddition) val.get("faAddition");
				FaAdjL faAdjL = (FaAdjL) val.get("faAdjL");
				
				double yz = val.get("YZ")==null ? faAddition.getYz() : Double.parseDouble(val.get("YZ").toString());
				double ljzjLjtx = val.get("LJZJ_LJTX")==null ? faAddition.getLjzjLjtx() : Double.parseDouble(val.get("LJZJ_LJTX").toString());
				double czl = val.get("CZL")==null ? faAddition.getCzl() : Double.parseDouble(val.get("CZL").toString());
				double jzzb = val.get("JZZB")==null ? faAddition.getJzzb() : Double.parseDouble(val.get("JZZB").toString());
				
				double jz = yz - yz*czl/100 - ljzjLjtx - jzzb;
				
				resetValueAdjs.add(newFaAdjL(faAdjL, "JZ", faAddition.getJz()+"", jz+"", userId));
			}
			
			if(resetValueAdjs!=null && resetValueAdjs.size()>0){
				faAdjDao.insertFaAdjL(resetValueAdjs);
				faAdjLs.addAll(resetValueAdjs);
			}
			
			
			faAdjDao.updateFaAddition(faAdjLs);
		}
		
	}

	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void revokeFaAdj(String adjHid, HashMap<String, Object> extraParams)
			throws Exception {
		String lang = extraParams.get("language").toString();
		
		int checkFaInPeriod = faAdjDao.checkInPeriod(adjHid);
		if(checkFaInPeriod==0){
			throw new Exception(XipUtil.getMessage(lang, "XC_FA_ADJ_CHECK_IN_PERIOD", null));
		}
		
		FaAdjH faAdjH = faAdjDao.getAdjHById(adjHid);
		
		// 删除glInterface前判断是否已经导入总账
		List<VInterface> oldVInterfaces = vInterfaceDao.getVInterfaceBySrcHid(adjHid);
		
		if(oldVInterfaces!=null && oldVInterfaces.size()>0){
			for(VInterface oldVInterface : oldVInterfaces){
				if(oldVInterface.getiHeadId()!=null && !"".equals(oldVInterface.getiHeadId())){
					FaAdjL faAdjL = faAdjDao.getAdjLByLid(oldVInterface.getSrcLid());
					
					HashMap<String, String> tokens = new HashMap<String, String>();
					tokens.put("adjCode", faAdjH.getDocNum());
					tokens.put("assetName", faAdjL.getAssetName());
					tokens.put("adjViewType", faAdjL.getAdjViewType());
					String excepitionMsg = XipUtil.getMessage(lang, "XC_FA_CHECK_REVOKE_OF_IMP_GL", tokens);
					throw new Exception(excepitionMsg);
				}
			}
		}
		
		// 获取调整明细
		List<FaAdjL> faAdjLs = faAdjDao.getAdjLByHid(adjHid);
		
		// 将调整后的资产还原
		if(faAdjLs!=null && faAdjLs.size()>0){
			faAdjDao.revokeFaAddition(faAdjLs);
			
			for(int i=0; i<faAdjLs.size(); i++){
				FaAdjL faAdjL = faAdjLs.get(i);
				//使用年限和折旧方法 修改已经计提后的资产要将折旧清除
				if(("SYNX".equalsIgnoreCase(faAdjL.getAdjType()) || 
				    "ZJFF".equalsIgnoreCase(faAdjL.getAdjType()) || 
				    FaConstant.TRANS_TYPE_SFZJ.equalsIgnoreCase(faAdjL.getAdjType()) || 
				    FaConstant.TRANS_TYPE_CBZX.equalsIgnoreCase(faAdjL.getAdjType())) && "B".equalsIgnoreCase(extraParams.get("jtType").toString())){
					//resetValueAdjs.add(newFaAdjL(faAdjL, "YJTJE", faAddition.getLjzjLjtx()+"", "0", userId));
					net.sf.json.JSONObject returnJson = faDepreciationService.cancelDepr(faAdjL.getAssetId(), faAdjH.getAdjDate().substring(0, 7), extraParams.get("language").toString(), extraParams.get(SessionVariable.userId).toString());
					if(returnJson.get("flag")!=null && "1".equals(returnJson.getString("flag"))){
						throw new Exception(returnJson.get("msg")==null ? "" : returnJson.getString("msg"));
					}
				}
			}
		}
		
		// 删除系统自动生成的调整单明细
		faAdjDao.delAutoCreateAdjLs(adjHid);
		
		// 删除glInterface
		vInterfaceDao.deleteVInterfaceBySrcHid(adjHid);
		
		// 删除xcFaTran
		faAdditionDao.deleteFaTrans(adjHid);
		
		// 将调整单状态修改成起草
		faAdjDao.updateAdjHStatus(adjHid, "C");
	}
}
