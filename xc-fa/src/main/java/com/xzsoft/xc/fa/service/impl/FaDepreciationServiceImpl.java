package com.xzsoft.xc.fa.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xzsoft.xc.fa.api.DeprMethodService;
import com.xzsoft.xc.fa.dao.FaAdditionDao;
import com.xzsoft.xc.fa.dao.FaDepreciationDao;
import com.xzsoft.xc.fa.modal.FaAddition;
import com.xzsoft.xc.fa.modal.FaDepreciation;
import com.xzsoft.xc.fa.modal.XcFaDepr;
import com.xzsoft.xc.fa.modal.XcFaTrans;
import com.xzsoft.xc.fa.service.FaDepreciationService;
import com.xzsoft.xc.gl.dao.VInterfaceDao;
import com.xzsoft.xc.gl.modal.VInterface;
import com.xzsoft.xc.gl.service.VoucherHandlerService;
import com.xzsoft.xc.util.common.XConstants;
import com.xzsoft.xip.platform.util.XipUtil;

@Service("faDepreciationService")
public class FaDepreciationServiceImpl implements FaDepreciationService {
    private static Logger log = Logger.getLogger(FaDepreciationServiceImpl.class);
    @Resource
    private FaDepreciationDao faDepreciationDao;
    
    @Resource
    private VInterfaceDao vInterfaceDao;
    
    @Resource
    private FaAdditionDao faAdditionDao;
    
    @Resource
    private VoucherHandlerService voucherHandlerService;
    
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public JSONObject calFaDepr(String ledgerId, String periodCode, String lang, String userId) throws Exception {
        JSONObject json = new JSONObject();
        List<FaAddition> faList = faDepreciationDao.findFaForDepr(ledgerId, periodCode);
        int cnt = 0;
        HashMap<String, DeprMethodService> deprMethods = null;
        try {
            deprMethods = faDepreciationDao.getDeprMethods();
        } catch (Exception e) {
            log.error(e);
            json.put("flag", 1);
            json.put("msg", XipUtil.getMessage(lang, "XC_FA_DEPR_METHOD_ERROR", null));
            return json;
        }
        for (FaAddition fa : faList) {
            cnt ++;
            double deprAmt = 0;
            double deprRate = 0;
            if (periodCode.equalsIgnoreCase(fa.getTyqj())) {
                // 如果是计提的最后一个期间，则当前净值就是本期计提金额
                deprAmt = fa.getYz() * (1 - fa.getCzl() / 100) - fa.getJzzb() - fa.getLjzjLjtx();
                deprRate = 1;
            } else {
                DeprMethodService deprMethod = deprMethods.get(fa.getZjff());
                try {
                	FaDepreciation faDepreciation = deprMethod.calcDepr(fa, periodCode);
                	
                    deprAmt = faDepreciation.getDepreciationAmount();
                    deprRate = faDepreciation.getDepreciationRate();
                } catch (Exception e) {
                    log.error(e);
                    HashMap<String, String> tokens = new HashMap<String, String>();
                    tokens.put("assetCode", fa.getZcbm());
                    tokens.put("periodCode", periodCode);
                    json.put("flag", 1);
                    json.put("msg", XipUtil.getMessage(lang, "XC_FA_DEPR_ERROR", tokens) + e.getMessage());
                    return json;
                }
            }
            
            XcFaDepr depr = new XcFaDepr();
            
            VInterface vInterface = new VInterface();
            
            HashMap<String, String> segments = new HashMap<String, String>();
            if (fa.getCbzx() != null && !"".equals(fa.getCbzx())) {
                segments.put(XConstants.XIP_PUB_DETPS, fa.getCbzx());
            }
            if (fa.getProjectId() != null && !"".equals(fa.getProjectId())) {
                segments.put(XConstants.XC_PM_PROJECTS, fa.getProjectId());
            }
            // 借资产卡的费用卡片
            String drKmId = faAdditionDao.getKmIdByCode(fa.getFykm(), ledgerId);
            // 查询科目组id
            if (drKmId != null) {
                try {
                    String drKmStr = voucherHandlerService.generateCCID(ledgerId, drKmId, segments);
                    JSONObject drKmObject = JSONObject.fromObject(drKmStr);
                    if (drKmObject != null) {
                        vInterface.setDrCcid(drKmObject.getString("ccid"));
                    }
                } catch (Exception e) {
                    log.error(e);
                    HashMap<String, String> tokens = new HashMap<String, String>();
                    tokens.put("assetCode", fa.getZcbm());
                    tokens.put("acctName", fa.getFykm());
                    json.put("flag", 1);
                    json.put("msg", XipUtil.getMessage(lang, "XC_FA_DEPR_DR_CCID_ERR", tokens) + e.getMessage());
                    return json;
                }
            }
            // 贷资产卡片累计折旧摊销科目
            String crKmId = faAdditionDao.getKmIdByCode(fa.getLjzjtxkm(), ledgerId);
            // 查询科目组id
            if (crKmId != null) {
                try {
                    String crKmStr = voucherHandlerService.generateCCID(ledgerId, crKmId, segments);
                    JSONObject crKmObject = JSONObject.fromObject(crKmStr);
                    if (crKmObject != null) {
                        vInterface.setCrCcid(crKmObject.getString("ccid"));
                    }
                } catch (Exception e) {
                    log.error(e);
                    HashMap<String, String> tokens = new HashMap<String, String>();
                    tokens.put("assetCode", fa.getZcbm());
                    tokens.put("acctName", fa.getLjzjtxkm());
                    json.put("flag", 1);
                    json.put("msg", XipUtil.getMessage(lang, "XC_FA_DEPR_CR_CCID_ERR", tokens) + e.getMessage());
                    return json;
                }
            }
            
            // 1写入计提折旧记录
            depr.setDeprId(UUID.randomUUID().toString());
            depr.setLedgerId(ledgerId);
            depr.setAssetId(fa.getAssetId());
            depr.setPeriodCode(periodCode);
            depr.setYz(fa.getYz());
            depr.setCzl(fa.getCzl());
            depr.setJzzb(fa.getJzzb());
            depr.setLjzjljtx(fa.getLjzjLjtx());
            depr.setBqzj(deprAmt);
            depr.setZjl(deprRate);
            depr.setCreationDate(new Date());
            depr.setCreatedBy(userId);
            depr.setLastUpdateDate(new Date());
            depr.setLastUpdateBy(userId);
            faAdditionDao.insertFaDepr(depr);
            
            // 2写入会计平台
            vInterface.setvInterfaceId(UUID.randomUUID().toString());
            vInterface.setLedgerId(fa.getLedgerId());
            vInterface.setPeriodCode(periodCode);
            vInterface.setTransCode("JTZJ");
            vInterface.setDeptId(fa.getCbzx());
            vInterface.setProjectId(fa.getProjectId());
            vInterface.setDrAcct(fa.getFykm());
            vInterface.setCrAcct(fa.getLjzjtxkm());
            vInterface.setAmt(deprAmt); 
            HashMap<String, String> tokens = new HashMap<String, String>();
            tokens.put("assetCode", fa.getZcbm());
            tokens.put("periodCode", periodCode);
            vInterface.setSummary(XipUtil.getMessage(lang, "XC_FA_DEPR_DESC", tokens));
            vInterface.setBusNo(fa.getZcbm());
            vInterface.setSrcHid(depr.getDeprId());
            vInterface.setCreatedBy(userId);
            vInterface.setCreationDate(new Date());
            vInterface.setLastUpdateBy(userId);
            vInterface.setLastUpdateDate(new Date());
            vInterfaceDao.insertVInterface(vInterface);
            
            String year = fa.getPeriodCode().substring(0, 4);
            String month = fa.getPeriodCode().substring(5, 7);
            if(!"10".equals(month) && !"11".equals(month) && !"12".equals(month)){
            	month = fa.getPeriodCode().substring(6, 7);
            }
            
            Calendar cal = Calendar.getInstance();
    		//设置年份
    		cal.set(Calendar.YEAR, Integer.parseInt(year));
    		//设置月份
    		cal.set(Calendar.MONTH, Integer.parseInt(month)-1);
    		//获取某月最大天数
    		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    		//设置日历中月份的最大天数
    		cal.set(Calendar.DAY_OF_MONTH, lastDay);
    		//格式化日期
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    		String lastDayOfMonth = sdf.format(cal.getTime());
    		Date lastDate = sdf.parse(lastDayOfMonth);
            
            // 3写入资产事务表
            XcFaTrans xcFaTran = new XcFaTrans();
            xcFaTran.setAssetId(fa.getAssetId());
            xcFaTran.setLedgerId(ledgerId);
            xcFaTran.setPeriodCode(fa.getPeriodCode());
            xcFaTran.setTransDate(lastDate);
            xcFaTran.setTransCode("JTZJ");
            xcFaTran.setBzsm(XipUtil.getMessage(lang, "XC_FA_DEPR_DESC", tokens));
            xcFaTran.setSrcId(depr.getDeprId());
            xcFaTran.setSrcDtlId("");
            xcFaTran.setPostFlag("N");
            xcFaTran.setCreatedBy(userId);
            xcFaTran.setCreationDate(new Date());
            xcFaTran.setLastUpdateBy(userId);
            xcFaTran.setLastUpdateDate(new Date());
            xcFaTran.setCatCode(fa.getCatCode());
            xcFaTran.setCbzx(fa.getCbzx());
            
            xcFaTran.setLjzjcr(deprAmt);
            
            faAdditionDao.insertFaTrans(xcFaTran);
            
            // 4备份资产卡片
            faDepreciationDao.createFaHistory(fa.getAssetId(), periodCode);
            
            // 5更新资产卡片
            fa.setJtqj(periodCode);
            fa.setZjje(deprAmt);
            fa.setLjzjLjtx(fa.getLjzjLjtx() + deprAmt);
            fa.setJz(fa.getYz() * (1 - fa.getCzl() / 100) - fa.getJzzb() - fa.getLjzjLjtx());
            fa.setZjl(deprRate);
            faDepreciationDao.updateDeprForFa(fa);
            json.put("flag", 0);
            json.put("msg", XipUtil.getMessage(lang, "XC_FA_DEPR_SUCCESS", null));
        }
        if(cnt ==0){
            json.put("flag", 0);
            json.put("msg", XipUtil.getMessage(lang, "XC_FA_DEPR_NO_FA", null));
        }
        return json;
    }
    
    @Override
    public JSONObject cancelDepr(String assetId, String periodCode, String lang, String userId) {
        JSONObject json = new JSONObject();
        XcFaDepr depr = faAdditionDao.getDepr(assetId, periodCode);
        if (depr != null) {
            // 判断此条计提折旧的会计平台接口数据是否生成凭证，如果已生成凭证不允许取消计提折旧
            String vHeaderId = vInterfaceDao.getVHeaderId4Interface(depr.getDeprId(), null, null);
            if (vHeaderId == null || "".equals(vHeaderId)) {
                // 1根据计提折旧时保留的历史数据恢复资产卡片信息
                faDepreciationDao.recoverFaFromHistory(assetId, periodCode);
                // 2删除资产卡片备份
                faDepreciationDao.delFaHistory(assetId, periodCode);
                // 3删除计提折旧记录
                faAdditionDao.delFaDepr(assetId, periodCode);
                // 4删除计提折旧对应的事务记录
                faAdditionDao.deleteFaTrans(depr.getDeprId());
                // 5删除业务会计平台对应的记录
                vInterfaceDao.deleteVInterfaceBySrc(depr.getDeprId(), null, null);
                
                json.put("flag", 0);
                json.put("msg", XipUtil.getMessage(lang, "XC_FA_DEPR_CANCEL_SUCCESS", null));
            } else {
                json.put("flag", 1);
                json.put("msg", XipUtil.getMessage(lang, "XC_FA_DEPR_V_EXIST",null));
            }
        } else {
            json.put("flag", 2);
            json.put("msg", XipUtil.getMessage(lang, "XC_FA_DEPR_NOT_FOUND", null));
        }
        return json;
    }
    
    public static void main(String[] args){
    	String period = "2016-02-10";
    	
    	String year = period.substring(0, 4);
        String month = period.substring(5, 7);
        if(!"10".equals(month) && !"11".equals(month) && !"12".equals(month)){
        	month = period.substring(6, 7);
        }
    	
    	Calendar cal = Calendar.getInstance();
		//设置年份
		cal.set(Calendar.YEAR, Integer.parseInt(year));
		//设置月份
		cal.set(Calendar.MONTH, Integer.parseInt(month)-1);
		//获取某月最大天数
		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		//设置日历中月份的最大天数
		cal.set(Calendar.DAY_OF_MONTH, lastDay);
		//格式化日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String lastDayOfMonth = sdf.format(cal.getTime());
		
		System.out.println(lastDayOfMonth);
    }
    
}
