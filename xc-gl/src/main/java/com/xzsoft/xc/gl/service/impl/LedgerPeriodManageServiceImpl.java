package com.xzsoft.xc.gl.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import com.xzsoft.xc.gl.dao.LedgerPeriodManageDao;
import com.xzsoft.xc.gl.dao.XCGLCommonDAO;
import com.xzsoft.xc.gl.modal.Balances;
import com.xzsoft.xc.gl.modal.GlJzTpl;
import com.xzsoft.xc.gl.modal.GlJzTplDtl;
import com.xzsoft.xc.gl.modal.Period;
import com.xzsoft.xc.gl.service.LedgerPeriodManageService;
import com.xzsoft.xc.gl.service.VoucherHandlerService;
import com.xzsoft.xc.gl.util.LedgerPeriodUtil;
import com.xzsoft.xc.gl.util.XCGLBalCarryoverUtil;
import com.xzsoft.xc.gl.util.XCGLWebBuilderUtil;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xc.util.common.XConstants;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.platform.util.PlatformUtil;
import com.xzsoft.xip.platform.util.XipUtil;

/**
 * @ClassName: LedgerPeriodManageServiceImpl 
 * @Description: 账簿追加会计期处理类
 * @author tangxl
 * @date 2016年1月20日 上午10:47:43 
 *
 */
@Service("ledgerPeriodManageService")
public class LedgerPeriodManageServiceImpl implements LedgerPeriodManageService {
	@Resource
	LedgerPeriodManageDao ledgerPeriodManageDao;
	@Resource
	private XCGLCommonDAO xcglCommonDAO ;
	@Resource
	private VoucherHandlerService voucherHandlerService;
	
	// 日志记录器
	private static final Logger log = Logger.getLogger(LedgerPeriodManageServiceImpl.class.getName());
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: appendledgerPeriod</p> 
	 * <p>Description: 追加账簿的会计期间，同时处理追加期间后的余额记录 </p> 
	 * @param ledgerId
	 * @param currentPeriod
	 * @param startPeriod
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.service.LedgerPeriodManageService#appendledgerPeriod(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class,RuntimeException.class})
	public void appendledgerPeriod(String ledgerId,String currentPeriod,String startPeriod)
			throws Exception {
		if("".equals(currentPeriod)){
			currentPeriod = ledgerPeriodManageDao.getLedgerMaxPeriodCode(ledgerId);
		}
		//如果当前账簿的追加会计期不存在，直接取账簿的开始期间为追加的会计期。
		//将账簿的期初余额数据累计到所追加的会计期间数上
		if(currentPeriod == null || "".equals(currentPeriod)){
			ledgerPeriodManageDao.appendPeriodByStartPeriod(ledgerId, startPeriod);
		}else{
			//计算出当前期间的下一个期间
			String nextPeriodCode = LedgerPeriodUtil.calculateNextPeriod(currentPeriod);
			Period period = ledgerPeriodManageDao.checkNextPeriodExists(nextPeriodCode);
			if(period == null){
				//throw new Exception("要追加的期间【"+nextPeriodCode+"】不存在！");
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("nextPeriodCode", nextPeriodCode);
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_YZJDQJ_BCZ", params));
				
			}else{
				//追加账簿期间操作
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("ledgerId", ledgerId);
				map.put("periodCode", currentPeriod);
				map.put("userId", CurrentSessionVar.getUserId());
				map.put("dbType", PlatformUtil.getDbType());
				List<Balances> balanceList = ledgerPeriodManageDao.getLedgerBalances(map);
				//取数完毕，插入新的期间余额，此时将会计期间设置为nextPeriodCode 
				map.put("periodCode", nextPeriodCode);
				if(balanceList == null || balanceList.size()==0){
					ledgerPeriodManageDao.insertPeriodToLedger(map);
				}else{
					 List<Balances> addList = new ArrayList<Balances>();
					//重新计算要添加期间的余额数值
					String yearCode = currentPeriod.substring(0, 4);
					String nextYearCode = nextPeriodCode.substring(0, 4);
					boolean isEqual = false;
					if(yearCode.equalsIgnoreCase(nextYearCode)){
						isEqual = true;
					}
					for(Balances tmp:balanceList){
						Balances obj = new Balances();
						obj.setLEDGER_ID(ledgerId);
						obj.setCCID(tmp.getCCID());
						obj.setPERIOD_CODE(nextPeriodCode);
						obj.setCURRENCY_CODE(tmp.getCURRENCY_CODE());
						obj.setB_DR(tmp.getPJTD_DR());
						obj.setB_CR(tmp.getPJTD_CR());
						if(isEqual){
							obj.setY_DR(tmp.getY_DR());
							obj.setY_CR(tmp.getY_CR());
							obj.setT_Y_DR(tmp.getT_Y_DR());
							obj.setT_Y_CR(tmp.getT_Y_CR());
							obj.setAMT_Y_DR(tmp.getAMT_Y_DR());
							obj.setAMT_Y_CR(tmp.getAMT_Y_CR());
							obj.setAMT_T_Y_DR(tmp.getAMT_T_Y_DR());
							obj.setAMT_T_Y_CR(tmp.getAMT_T_Y_CR());
						}else{
							obj.setY_DR(tmp.getPJTD_DR());
							obj.setY_CR(tmp.getPJTD_CR());
							obj.setT_Y_DR(tmp.getT_PJTD_DR());
							obj.setT_Y_CR(tmp.getT_PJTD_CR());
							obj.setAMT_Y_DR(tmp.getAMT_PJTD_DR());
							obj.setAMT_Y_CR(tmp.getAMT_PJTD_CR());
							obj.setAMT_T_Y_DR(tmp.getAMT_PJTD_DR());
							obj.setAMT_T_Y_CR(tmp.getAMT_PJTD_CR());
						}
						obj.setPJTD_DR(tmp.getPJTD_DR());
						obj.setPJTD_CR(tmp.getPJTD_CR());
						obj.setT_B_DR(tmp.getT_PJTD_DR());
						obj.setT_B_CR(tmp.getT_PJTD_CR());
						obj.setT_PJTD_DR(tmp.getT_PJTD_DR());
						obj.setT_PJTD_CR(tmp.getT_PJTD_CR());
						//数量
						obj.setAMT_B_DR(tmp.getAMT_PJTD_DR());
						obj.setAMT_B_CR(tmp.getAMT_PJTD_CR());
						obj.setAMT_T_B_DR(tmp.getAMT_T_PJTD_DR());
						obj.setAMT_T_B_CR(tmp.getAMT_T_PJTD_CR());
						obj.setAMT_T_PJTD_DR(tmp.getAMT_T_PJTD_DR());
						obj.setAMT_T_PJTD_CR(tmp.getAMT_T_PJTD_DR());
						obj.setCREATION_DATE(new Date());
						obj.setCREATED_BY(CurrentSessionVar.getUserId());
						obj.setLAST_UPDATE_DATE(new Date());
						obj.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
						addList.add(obj);
					}
				//保存账簿追加的余额及期间信息
					ledgerPeriodManageDao.appendLedgerPeriod(addList, map);
				}
			}
		}
		
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getAccountDate</p> 
	 * <p>Description: 计算凭证录入、复制和冲销时的会计日期  </p> 
	 * 会计日期：
	 * 		1、如果当前日期所在账簿会计期为打开状态, 则取当前日期。
	 * 		2、如果没有打开, 则取小于当前日期的最大打开会计期最后一天作为会计日期。
	 * 		3、如果上述两种情况都不满足，则会计日期为空。
	 * @param ledgerId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.service.LedgerPeriodManageService#getAccountDate(java.lang.String)
	 */
	@Override
	public String getAccountDate(String ledgerId) throws Exception {
		String accDate = null ;
		
		// 日期格式化
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
		
		try {
			// 计算当前会计期
			Period period = xcglCommonDAO.getCurrentPeriod(ledgerId) ;
			if(period != null) {
				// 取当前日期
				accDate = sdf.format(new Date()) ;
				
			}else{
				// 计算账簿打开状态最大的会计期
				period = xcglCommonDAO.getMaxOpenPeriod(ledgerId) ;
				if(period != null){
					accDate = sdf.format(period.getEndDate()) ;
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
		return accDate;
	}
	/*
	 * (非 Javadoc) 
	 * <p>Title: appendAdjustPeriod</p> 
	 * <p>Description: 追加账簿的调整会计期间，同时处理追加期间后的余额记录 </p> 
	 * @param ledgerId
	 * @param currentPeriod
	 * @param startPeriod
	 * @throws Exception 
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class,RuntimeException.class})
	public void appendAdjustPeriod(String ledgerId, String currentPeriod,
			String startPeriod) throws Exception {
		//账簿能追加的调整会计期处在【账簿开始会计期】和 【账簿当前最大会计期】之间
		//计算要追加调整期的上一个期间，判断该期间是否存在<会计期间理论上是连续的，不存在间断的情况，因此该判断可有可无！！>
		String previousPeriodCode = LedgerPeriodUtil.calculatePreviousPeriod(currentPeriod);
		Period period = ledgerPeriodManageDao.checkNextPeriodExists(previousPeriodCode);
		if(period == null){
			//throw new Exception("要追加的期间【"+previousPeriodCode+"】不存在！");
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("nextPeriodCode", previousPeriodCode);
			throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_YZJDQJ_BCZ", params));
			
		}else{
			//获取要追加调整会计期的上一会计期的余额信息
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("ledgerId", ledgerId);
			map.put("periodCode", previousPeriodCode);
			map.put("userId", CurrentSessionVar.getUserId());
			map.put("dbType", PlatformUtil.getDbType());
			List<Balances> balanceList = ledgerPeriodManageDao.getLedgerBalances(map);
			//取数完毕，插入新的期间余额，此时将会计期间设置为当前要追加的调整会计期 
			map.put("periodCode", currentPeriod);
			if(balanceList == null || balanceList.size()==0){
				//throw new Exception("无法获取要追加调整会计期【"+currentPeriod+"】的上一期间【"+previousPeriodCode+"】的余额信息！");
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("currentPeriod", currentPeriod);
				params.put("previousPeriodCode", previousPeriodCode);
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_WFHQYZJTZKJQ_DSYQJ_DYEXX", params));
				
			}else{
				 List<Balances> addList = new ArrayList<Balances>();
				//判断要追加的调整会计期与前一会计期是否处在同一年内。
				String yearCode = previousPeriodCode.substring(0, 4);
				String adjustYearCode = currentPeriod.substring(0, 4);
				boolean isEqual = false;
				if(yearCode.equalsIgnoreCase(adjustYearCode)){
					isEqual = true;
				}
				for(Balances tmp:balanceList){
					Balances obj = new Balances();
					obj.setLEDGER_ID(ledgerId);
					obj.setCCID(tmp.getCCID());
					obj.setPERIOD_CODE(currentPeriod);
					obj.setCURRENCY_CODE(tmp.getCURRENCY_CODE());
					obj.setB_DR(tmp.getPJTD_DR());
					obj.setB_CR(tmp.getPJTD_CR());
					if(isEqual){
						obj.setY_DR(tmp.getY_DR());
						obj.setY_CR(tmp.getY_CR());
						obj.setT_Y_DR(tmp.getT_Y_DR());
						obj.setT_Y_CR(tmp.getT_Y_CR());
						obj.setAMT_Y_DR(tmp.getAMT_Y_DR());
						obj.setAMT_Y_CR(tmp.getAMT_Y_CR());
						obj.setAMT_T_Y_DR(tmp.getAMT_T_Y_DR());
						obj.setAMT_T_Y_CR(tmp.getAMT_T_Y_CR());
					}else{
						obj.setY_DR(tmp.getPJTD_DR());
						obj.setY_CR(tmp.getPJTD_CR());
						obj.setT_Y_DR(tmp.getT_PJTD_DR());
						obj.setT_Y_CR(tmp.getT_PJTD_CR());
						obj.setAMT_Y_DR(tmp.getAMT_PJTD_DR());
						obj.setAMT_Y_CR(tmp.getAMT_PJTD_CR());
						obj.setAMT_T_Y_DR(tmp.getAMT_PJTD_DR());
						obj.setAMT_T_Y_CR(tmp.getAMT_PJTD_CR());
					}
					obj.setPJTD_DR(tmp.getPJTD_DR());
					obj.setPJTD_CR(tmp.getPJTD_CR());
					obj.setT_B_DR(tmp.getT_PJTD_DR());
					obj.setT_B_CR(tmp.getT_PJTD_CR());
					obj.setT_PJTD_DR(tmp.getT_PJTD_DR());
					obj.setT_PJTD_CR(tmp.getT_PJTD_CR());
					//数量
					obj.setAMT_B_DR(tmp.getAMT_PJTD_DR());
					obj.setAMT_B_CR(tmp.getAMT_PJTD_CR());
					obj.setAMT_T_B_DR(tmp.getAMT_T_PJTD_DR());
					obj.setAMT_T_B_CR(tmp.getAMT_T_PJTD_CR());
					obj.setAMT_T_PJTD_DR(tmp.getAMT_T_PJTD_DR());
					obj.setAMT_T_PJTD_CR(tmp.getAMT_T_PJTD_DR());
					obj.setCREATION_DATE(new Date());
					obj.setCREATED_BY(CurrentSessionVar.getUserId());
					obj.setLAST_UPDATE_DATE(new Date());
					obj.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
					addList.add(obj);
				}
			//保存账簿追加的余额及期间信息
				ledgerPeriodManageDao.appendLedgerPeriod(addList, map);
			}
		}
	
	
	}

	/* (non-Javadoc)
	 * @name     carryoverBalances
	 * @author   tangxl
	 * @date     2016年7月19日
	 * @注释                   期末余额结转
	 * @version  1.0
	 * @see com.xzsoft.xc.gl.service.LedgerPeriodManageService#carryoverBalances(java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class,RuntimeException.class})
	public JSONObject carryoverBalances(String jzTplId, String periodCode,String voucherDate)
			throws Exception {
		JSONObject object = new JSONObject();
		GlJzTpl glJzTpl = ledgerPeriodManageDao.getGlJzTplById(jzTplId,periodCode);
		//生成凭证
		List<GlJzTplDtl> dtlList = glJzTpl.getJzDtlList();
		if(dtlList !=null && dtlList.size()>0){
			String voucherJson = XCGLBalCarryoverUtil.carrayoverBalIntoVoucher(glJzTpl, periodCode,voucherDate,voucherHandlerService);
			//返回凭证ID
			String returnHeader = voucherHandlerService.doSaveVoucher(voucherJson);
			object.put("flag", "0");
			//object.put("msg", "凭证生成成功！");
			object.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_GL_PZSCCG", null));
			//记录结转日志
			HashMap<String, String> logMap = new HashMap<String, String>();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			//String logDesc = "【"+CurrentSessionVar.getEmpName()+"】在"+format.format(new Date())+"对【"+glJzTpl.getJzTplName()+"】模板生成了凭证!";
			String param1 = CurrentSessionVar.getEmpName();
			String param2 = format.format(new Date());
			String param3 = glJzTpl.getJzTplName();
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("param1", param1);
			params.put("param2", param2);
			params.put("param3", param3);
			String logDesc = XipUtil.getMessage(XCUtil.getLang(), "XC_GL__ZD_MBSCLPZ", params);
			
			logMap.put("JZ_LOG_ID", UUID.randomUUID().toString());
			logMap.put("JZ_TPL_ID", glJzTpl.getJzTplId());
			logMap.put("JZ_LOG_DESC", logDesc);
			logMap.put("CREATED_BY", CurrentSessionVar.getUserId());
			logMap.put("dbType",PlatformUtil.getDbType());
			try {
				ledgerPeriodManageDao.insertCarryoverLog(logMap);
			} catch (Exception e) {
				e.printStackTrace();
				//object.put("msg", "凭证生成成功,但插入结转日志异常！");
				object.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_GL_PZSCCGDCRJZRZYC", null));
			}
			JSONObject headerObj = JSONObject.fromObject(returnHeader);
			String headerId = headerObj.getString("headId");
			returnHeader = XCGLWebBuilderUtil.viewVoucher(headerId);
			headerObj = JSONObject.fromObject(returnHeader);
			if("1".equals(headerObj.get("v_template_type"))){
				object.put("vt", "normal");
			}else if("2".equals(headerObj.get("v_template_type"))) {
				object.put("vt", "amount");
			}else if("3".equals(headerObj.get("v_template_type"))) {
				object.put("vt", "foreign");
			}
			object.put("headId", headerId);
			object.put("ledgerId", headerObj.get("ledger"));
		}else{
			object.put("flag", "-1");
			//object.put("msg", "所选择的结转科目余额未过账！");
			object.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_GL_SXZDJZKMYEWGZ", null));
			
		}
		return object;
	}

}
