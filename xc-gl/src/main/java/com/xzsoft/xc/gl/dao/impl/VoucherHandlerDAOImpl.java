package com.xzsoft.xc.gl.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.xzsoft.xc.bg.modal.BgFactDTO;
import com.xzsoft.xc.gl.dao.VoucherHandlerDAO;
import com.xzsoft.xc.gl.mapper.VoucherCheckMapper;
import com.xzsoft.xc.gl.mapper.VoucherMapper;
import com.xzsoft.xc.gl.modal.VHead;
import com.xzsoft.xc.gl.modal.VLine;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xc.util.modal.Ledger;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.platform.util.PlatformUtil;
import com.xzsoft.xip.platform.util.XipUtil;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;

/**
 * 
 * @ClassName: VoucherHandlerDAOImpl 
 * @Description: 记账凭证处理数据层接口实现类
 * @author linp
 * @date 2015年12月18日 下午6:12:35 
 *
 */
@Repository("voucherHandlerDAO")
public class VoucherHandlerDAOImpl implements VoucherHandlerDAO {
	// 日志记录器
	private static final Logger log = LoggerFactory.getLogger(VoucherHandlerDAOImpl.class);
	
	@Resource
	private VoucherMapper voucherMapper ;
	@Resource
	private VoucherCheckMapper voucherCheckMapper ;
	

	/*
	 * (非 Javadoc) 
	 * <p>Title: saveVoucher</p> 
	 * <p>Description: 保存单张凭证信息 </p> 
	 * @param bean
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.VoucherHandlerDAO#saveVoucher(org.codehaus.jettison.json.JSONObject)
	 */
	@Override
	public void saveVoucher(VHead bean,String chkType) throws Exception {
		try {
			// 将科学计数法字转换成字符串
			this.handlerScientificNotation(bean,chkType);
			
			Date lastUpdateDate = CurrentUserUtil.getCurrentDate() ;
			String lastUpdatedBy = CurrentSessionVar.getUserId() ;
			
			// 保存凭证头信息
			bean.setLastUpdateDate(lastUpdateDate);
			bean.setLastUpdatedBy(lastUpdatedBy);
			
			voucherMapper.saveVHead(bean);
			
			// 批量保存凭证行信息
			List<VLine> lines = bean.getLines() ;
			for(VLine line : lines){
				line.setHeadId(bean.getHeadId());
				line.setLastUpdateDate(lastUpdateDate);
				line.setLastUpdatedBy(lastUpdatedBy);
			}
			
			HashMap<String,Object> map = new HashMap<String,Object>() ;
			map.put("dbType", PlatformUtil.getDbType()) ;
			map.put("list", lines) ;
			voucherMapper.saveVLines(map);
			
			// 执行凭证冲销,记录凭证被冲销标志位
			if("write_off".equals(chkType)){
				HashMap<String,Object> paramMap = new HashMap<String,Object>() ;
				paramMap.put("userId", bean.getCreatedBy()) ;
				paramMap.put("cdate", bean.getLastUpdateDate()) ;
				paramMap.put("headId", bean.getHeadId()) ; // 冲销凭证头ID
				paramMap.put("writeOffHeadId", bean.getWriteOffNum()) ; // 被冲销的凭证头ID
				
				voucherMapper.recordWriteOffVouchers(paramMap);
				
			}else{
				// 如果采用了凭证断号,则减少断号库记录
				String serialNum = bean.getSerialNum() ;
				if(serialNum != null && !"".equals(serialNum)){
					HashMap<String,String> paramMap = new HashMap<String,String>();
					paramMap.put("ledgerId", bean.getLedgerId()) ;
					paramMap.put("ldPeriod", bean.getPeriodCode()) ;
					paramMap.put("serialNum", serialNum) ;
					
					voucherMapper.deVJumpNum(paramMap);
				}
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: updateVoucher</p> 
	 * <p>Description: 修改单张凭证信息  </p> 
	 * @param paramJo
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.VoucherHandlerDAO#updateVoucher(org.codehaus.jettison.json.JSONObject)
	 */
	@Override
	public void updateVoucher(VHead bean)throws Exception {
		try {
			// 将科学计数法字转换成字符串
			this.handlerScientificNotation(bean,"update");
			
			Date lastUpdateDate = CurrentUserUtil.getCurrentDate() ;
			String lastUpdatedBy = CurrentSessionVar.getUserId() ;
			
			// 更新凭证头信息
			bean.setLastUpdateDate(lastUpdateDate);
			bean.setLastUpdatedBy(lastUpdatedBy);
			
			voucherMapper.updateVHead(bean);
			
			// 处理新增的凭证分录
			List<VLine> addLines = bean.getAddLines() ;
			if(addLines != null && addLines.size()>0){
				for(VLine line : addLines){
					line.setLastUpdateDate(lastUpdateDate);
					line.setLastUpdatedBy(lastUpdatedBy);
				}
				
				// 批量保存凭证分录行信息
				HashMap<String,Object> map = new HashMap<String,Object>() ;
				map.put("dbType", PlatformUtil.getDbType()) ;
				map.put("list", addLines) ;		
				
				voucherMapper.saveVLines(map);
			}
			
			// 处理删除的凭证分录
			List<String> delLines = bean.getDelLines() ;
			if(delLines != null && delLines.size()>0){
				voucherMapper.delVLineByLineId(delLines);
			}
			
			// 处理更新的凭证分录
			List<VLine> updLines = bean.getUpdLines() ;
			if(updLines != null && updLines.size()>0){
				for(VLine line : updLines){
					line.setLastUpdateDate(lastUpdateDate);
					line.setLastUpdatedBy(lastUpdatedBy);
				}
				
				// 批量更新凭证分录信息
				HashMap<String,Object> map = new HashMap<String,Object>() ;
				map.put("dbType", PlatformUtil.getDbType()) ;
				map.put("list", updLines) ;
				voucherMapper.updateVLines(map);
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
	}
	
	/**
	 * @Title: handlerScientificNotation 
	 * @Description: 将科学计数法字转换成字符串,并重新计算借方和贷方合计数
	 * @param bean
	 * @param chkType : save：保存、write_off:冲销、update:修改
	 * @throws Exception    设定文件
	 */
	private void handlerScientificNotation(VHead bean,String chkType) throws Exception {
		try {
			// 计算凭证折后借方和贷方合计数
			BigDecimal tdr = new BigDecimal(0) ;
			BigDecimal tcr = new BigDecimal(0) ;
			
			if("update".equals(chkType)){ // 修改
				// 新增分录行
				List<VLine> addLines = bean.getAddLines() ;
				if(addLines != null && addLines.size()>0){
					for(VLine line : addLines){
						// 数量
						Object lAmt = line.getAmount() ;
						lAmt = (lAmt==null ? 0 : lAmt) ;
						// 折后借方金额
						Object lAdr = line.getAccountDR() ;
						lAdr = (lAdr==null ? 0 : lAdr) ;
						// 折后贷方金额
						Object lAcr = line.getAccountCR() ;
						lAcr = (lAcr==null ? 0 : lAcr) ;
						// 原币借方金额
						Object lEdr = line.getEnterDR() ;
						lEdr = (lEdr==null ? 0 : lEdr) ;
						// 原币贷方金额
						Object lEcr = line.getEnterCR() ;
						lEcr = (lEcr==null ? 0 : lEcr) ;
						
						// 转换
						BigDecimal amt = new BigDecimal(String.valueOf(lAmt)) ;
						BigDecimal adr = new BigDecimal(String.valueOf(lAdr)) ;
						BigDecimal acr = new BigDecimal(String.valueOf(lAcr)) ;
						BigDecimal edr = new BigDecimal(String.valueOf(lEdr)) ;
						BigDecimal ecr = new BigDecimal(String.valueOf(lEcr)) ;
						
						line.setAmt(amt.toPlainString());
						line.setAdr(adr.toPlainString());
						line.setAcr(acr.toPlainString());
						line.setEdr(edr.toPlainString());
						line.setEcr(ecr.toPlainString());
						
						line.setAmount(amt.doubleValue());
						line.setAccountDR(adr.doubleValue());
						line.setAccountCR(acr.doubleValue());
						line.setEnterDR(edr.doubleValue());
						line.setEnterCR(ecr.doubleValue());
						
						// 计算凭证借方和贷方合计数
						tdr = tdr.add(adr) ;
						tcr = tcr.add(acr) ;
					}
				}
				// 更新分录行
				List<VLine> updLines = bean.getUpdLines() ;
				if(updLines != null && updLines.size()>0){
					for(VLine line : updLines){
						// 数量
						Object lAmt = line.getAmount() ;
						lAmt = (lAmt==null ? 0 : lAmt) ;
						// 折后借方金额
						Object lAdr = line.getAccountDR() ;
						lAdr = (lAdr==null ? 0 : lAdr) ;
						// 折后贷方金额
						Object lAcr = line.getAccountCR() ;
						lAcr = (lAcr==null ? 0 : lAcr) ;
						// 原币借方金额
						Object lEdr = line.getEnterDR() ;
						lEdr = (lEdr==null ? 0 : lEdr) ;
						// 原币贷方金额
						Object lEcr = line.getEnterCR() ;
						lEcr = (lEcr==null ? 0 : lEcr) ;
						
						// 数值转换
						BigDecimal amt = new BigDecimal(String.valueOf(lAmt)) ;
						BigDecimal adr = new BigDecimal(String.valueOf(lAdr)) ;
						BigDecimal acr = new BigDecimal(String.valueOf(lAcr)) ;
						BigDecimal edr = new BigDecimal(String.valueOf(lEdr)) ;
						BigDecimal ecr = new BigDecimal(String.valueOf(lEcr)) ;
						
						line.setAmt(amt.toPlainString());
						line.setAdr(adr.toPlainString());
						line.setAcr(acr.toPlainString());
						line.setEdr(edr.toPlainString());
						line.setEcr(ecr.toPlainString());
						
						line.setAmount(amt.doubleValue());
						line.setAccountDR(adr.doubleValue());
						line.setAccountCR(acr.doubleValue());
						line.setEnterDR(edr.doubleValue());
						line.setEnterCR(ecr.doubleValue());
						
						// 计算凭证借方和贷方合计数
						tdr = tdr.add(adr) ;
						tcr = tcr.add(acr) ;
					}
				}
				
			}else{  
				
				// 保存（新建或冲销凭证）
				for(VLine line : bean.getLines()){
					// 数量
					Object lAmt = line.getAmount() ;
					lAmt = (lAmt==null ? 0 : lAmt) ;
					// 折后借方金额
					Object lAdr = line.getAccountDR() ;
					lAdr = (lAdr==null ? 0 : lAdr) ;
					// 折后贷方金额
					Object lAcr = line.getAccountCR() ;
					lAcr = (lAcr==null ? 0 : lAcr) ;
					// 原币借方金额
					Object lEdr = line.getEnterDR() ;
					lEdr = (lEdr==null ? 0 : lEdr) ;
					// 原币贷方金额
					Object lEcr = line.getEnterCR() ;
					lEcr = (lEcr==null ? 0 : lEcr) ;
					
					// 数值转换
					BigDecimal amt = new BigDecimal(String.valueOf(lAmt)) ;
					BigDecimal adr = new BigDecimal(String.valueOf(lAdr)) ;
					BigDecimal acr = new BigDecimal(String.valueOf(lAcr)) ;
					BigDecimal edr = new BigDecimal(String.valueOf(lEdr)) ;
					BigDecimal ecr = new BigDecimal(String.valueOf(lEcr)) ;
					
					line.setAmt(amt.toPlainString());
					line.setAdr(adr.toPlainString());
					line.setAcr(acr.toPlainString());
					line.setEdr(edr.toPlainString());
					line.setEcr(ecr.toPlainString());
					
					line.setAmount(amt.doubleValue());
					line.setAccountDR(adr.doubleValue());
					line.setAccountCR(acr.doubleValue());
					line.setEnterDR(edr.doubleValue());
					line.setEnterCR(ecr.doubleValue());
					
					// 计算凭证借方和贷方合计数
					tdr = tdr.add(adr) ;
					tcr = tcr.add(acr) ;
				}
			}
			
			// 判断凭证借贷合计数是否平衡
			double ddr = tdr.doubleValue() ;
			double dcr = tcr.doubleValue() ;
			
			if(ddr==dcr){
				bean.setTdr(tdr.toPlainString());
				bean.setTcr(tcr.toPlainString());
				bean.setTotalDR(ddr);
				bean.setTotalCR(dcr);
			}else{
				//throw new Exception("凭证有效分录行的借贷方合计数不相等,请确认!") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_PZYXFLHDJDFHJSBXD", null)) ;
			}

		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ; 
		}
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: submitVoucher</p> 
	 * <p>Description: 凭证提交 </p> 
	 * @param list
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.VoucherHandlerDAO#submitVoucher(org.codehaus.jettison.json.JSONArray)
	 */
	@Override
	public void submitVouchers(List<VHead> list) throws Exception {
		try {
			// 提交凭证
			if(list != null && list.size()>0){
				String userId = CurrentSessionVar.getUserId() ;
				Date cdate = CurrentUserUtil.getCurrentDate() ;
				for(VHead head : list){
					head.setLastUpdateDate(cdate);
					head.setLastUpdatedBy(userId);
					
					voucherMapper.submitVouchers(head);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: cancelSubmitVoucher</p> 
	 * <p>Description: 撤回提交 </p> 
	 * @param list
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.VoucherHandlerDAO#cancelSubmitVoucher(org.codehaus.jettison.json.JSONArray)
	 */
	@Override
	public void cancelSubmitVouchers(List<String> list) throws Exception {
		try {
			// 撤回提交
			HashMap<String,Object> paramMap = new HashMap<String,Object>() ;
			paramMap.put("list", list) ;
			paramMap.put("userId", CurrentSessionVar.getUserId()) ;
			paramMap.put("cdate", CurrentUserUtil.getCurrentDate()) ;
			
			voucherMapper.cancelSubmitVouchers(paramMap);
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: delVoucher</p> 
	 * <p>Description: 删除凭证</p> 
	 * @param paramArray
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.VoucherHandlerDAO#delVoucher(org.codehaus.jettison.json.JSONArray)
	 */
	@Override
	public void delVouchers(List<String> list) throws Exception {
		try {
			// 参数信息
			HashMap<String,Object> paramMap = new HashMap<String,Object>() ;
			paramMap.put("list", list) ;
			paramMap.put("userId", CurrentSessionVar.getUserId()) ;
			paramMap.put("cdate", CurrentUserUtil.getCurrentDate()) ;
			
			// 记录凭证断号信息
			voucherMapper.recordVJumpNum(paramMap);
			
			// 取消与被冲销凭证的关联关系
			voucherMapper.cancelWriteOffVouchers(paramMap);
			
			// 删除凭证行信息
			voucherMapper.delVLineByHeadIds(list);
			
			// 删除凭证头信息
			voucherMapper.delVHeads(list);
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: checkVoucher</p> 
	 * <p>Description: 审核凭证</p> 
	 * @param list
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.VoucherHandlerDAO#checkVoucher(java.util.List)
	 */
	@Override
	public void checkVouchers(List<String> list) throws Exception {
		try {
			// 参数信息
			HashMap<String,Object> paramMap = new HashMap<String,Object>() ;
			paramMap.put("list", list) ;
			paramMap.put("userId", CurrentSessionVar.getUserId()) ;
			paramMap.put("empName", CurrentSessionVar.getEmpName()) ;
			paramMap.put("cdate", CurrentUserUtil.getCurrentDate()) ;
			
			// 审核凭证
			voucherMapper.checkVouchers(paramMap);
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: uncheckVoucher</p> 
	 * <p>Description: 取消审核</p> 
	 * @param paramArray
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.VoucherHandlerDAO#uncheckVoucher(org.codehaus.jettison.json.JSONArray)
	 */
	@Override
	public void uncheckVouchers(List<String> list) throws Exception {
		try {
			// 参数信息
			HashMap<String,Object> paramMap = new HashMap<String,Object>() ;
			paramMap.put("list", list) ;
			paramMap.put("userId", CurrentSessionVar.getUserId()) ;
			paramMap.put("cdate", CurrentUserUtil.getCurrentDate()) ;
			
			// 取消审核
			voucherMapper.uncheckVouchers(paramMap);
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: signVoucher</p> 
	 * <p>Description: 出纳签字 </p> 
	 * @param paramArray
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.VoucherHandlerDAO#signVoucher(org.codehaus.jettison.json.JSONArray)
	 */
	@Override
	public void signVouchers(List<String> list) throws Exception {
		try {
			// 定义参数
			HashMap<String,Object> paramMap = new HashMap<String,Object>() ;
			paramMap.put("list", list) ;
			paramMap.put("userId", CurrentSessionVar.getUserId()) ;
			paramMap.put("empName", CurrentSessionVar.getEmpName()) ;
			paramMap.put("cdate", CurrentUserUtil.getCurrentDate()) ;
			
			// 出纳签字
			voucherMapper.signVouchers(paramMap);
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: unsignVoucher</p> 
	 * <p>Description: 取消签字 </p> 
	 * @param paramArray
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.VoucherHandlerDAO#unsignVoucher(org.codehaus.jettison.json.JSONArray)
	 */
	@Override
	public void unsignVouchers(List<String> list) throws Exception {
		try {
			// 定义参数
			HashMap<String,Object> paramMap = new HashMap<String,Object>() ;
			paramMap.put("list", list) ;
			paramMap.put("userId", CurrentSessionVar.getUserId()) ;
			paramMap.put("cdate", CurrentUserUtil.getCurrentDate()) ;
			
			// 取消签字
			voucherMapper.unsignVouchers(paramMap);
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: rejectVoucher</p> 
	 * <p>Description: 凭证驳回 </p> 
	 * @param paramArray
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.VoucherHandlerDAO#rejectVoucher(org.codehaus.jettison.json.JSONArray)
	 */
	@Override
	public void rejectVouchers(List<String> list) throws Exception {
		try {
			// 驳回已提交当未审核凭证
			HashMap<String,Object> paramMap = new HashMap<String,Object>() ;
			paramMap.put("list", list) ;
			paramMap.put("userId", CurrentSessionVar.getUserId()) ;
			paramMap.put("cdate", CurrentUserUtil.getCurrentDate()) ;
			
			voucherMapper.rejectVouchers(paramMap);
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: account</p> 
	 * <p>Description: 凭证记账处理 </p> 
	 * @param list
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.VoucherHandlerDAO#account(java.util.List)
	 */
	@Override
	public void account(List<String> list)throws Exception {
		try {
			// 记账
			HashMap<String,Object> paramMap = new HashMap<String,Object>() ;
			paramMap.put("list", list) ;
			paramMap.put("userId", CurrentSessionVar.getUserId()) ;
			paramMap.put("empName", CurrentSessionVar.getEmpName()) ;
			paramMap.put("cdate", CurrentUserUtil.getCurrentDate()) ;
			
			voucherMapper.account(paramMap);
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: cancelAccount</p> 
	 * <p>Description: 凭证取消记账 </p> 
	 * @param list
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.VoucherHandlerDAO#cancelAccount(java.util.List)
	 */
	@Override
	public void cancelAccount(List<String> list)throws Exception {
		try {
			// 参数信息
			HashMap<String,Object> paramMap = new HashMap<String,Object>() ;
			paramMap.put("list", list) ;
			paramMap.put("userId", CurrentSessionVar.getUserId()) ;
			paramMap.put("cdate", CurrentUserUtil.getCurrentDate()) ;
			
			voucherMapper.cancelAccount(paramMap);
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getVHead</p> 
	 * <p>Description: 单凭证查询</p> 
	 * @param headId
	 * @param isContainLines
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.VoucherHandlerDAO#viewVoucher(java.lang.String, boolean)
	 */
	@Override
	public VHead getVHead(String headId, boolean isContainLines) throws Exception {
		VHead head = new VHead() ;
		try {
			// 查询凭证头信息
			head = voucherMapper.getVHead(headId) ;
			
			// 查询凭证分录行信息
			if(isContainLines){
				List<VLine> lines = voucherMapper.getVLines(headId) ;
				head.setLines(lines);
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
		
		return head ;
	}
	/*
	 * (非 Javadoc) 
	 * <p>Title: getVLines</p> 
	 * <p>Description: 查询凭证分录行信息</p> 
	 * @param headId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.VoucherHandlerDAO#getVLines(java.lang.String)
	 */
	@Override
	public List<VLine> getVLines(String headId)throws Exception {
		List<VLine> lines = new ArrayList<VLine>() ;
		try {
			lines = voucherMapper.getVLines(headId) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
		return lines ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getVHeads</p> 
	 * <p>Description: 批量查询凭证信息 </p> 
	 * @param headIds
	 * @param isContainLines
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.VoucherHandlerDAO#getVHeads(java.util.List, boolean)
	 */
	@Override
	public List<VHead> getVHeads(List<String> headIds, boolean isContainLines) throws Exception {
		List<VHead> beans = new ArrayList<VHead>() ;
		
		try {
			// 查询凭证头信息
			beans = voucherMapper.getVHeads(headIds) ;
			
			// 查询凭证分录行信息
			if(isContainLines){
				if(beans != null && beans.size()>0){
					for(VHead bean : beans){
						List<VLine> lines = voucherMapper.getVLines(bean.getHeadId()) ;
						bean.setLines(lines);
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
		
		return beans ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getbudgetConditions</p> 
	 * <p>Description: 获取预算检查条件信息 </p> 
	 * @param headId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.VoucherHandlerDAO#getbudgetConditions(java.lang.String)
	 */
	@Override
	public List<BgFactDTO> getbudgetConditions(String headId) throws Exception {
		return voucherMapper.getbudgetConditions(headId) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: isSameVoucher</p> 
	 * <p>Description: 判断本次导入的数据是否为同一凭证 </p> 
	 * @param sessionId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.VoucherHandlerDAO#isSameVoucher(java.lang.String)
	 */
	@Override
	public int isSameVoucher(String sessionId) throws Exception {
		return voucherCheckMapper.isSameV(sessionId) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: delTmpVoucher</p> 
	 * <p>Description: 删除临时表中的凭证信息</p> 
	 * @param sessionId
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.VoucherHandlerDAO#delTmpVoucher(java.lang.String)
	 */
	@Override
	public void delTmpVoucher(String sessionId) throws Exception {
		voucherCheckMapper.delTmpVoucher(sessionId);
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: updateTemplateType</p> 
	 * <p>Description: 处理凭证格式 </p> 
	 * @param sessionId
	 * @param ledger
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.VoucherHandlerDAO#checkTempVoucher(java.lang.String, com.xzsoft.xc.util.modal.Ledger)
	 */
	@Override
	public int updateTemplateType(String sessionId,Ledger ledger) throws Exception {
		int existsError = 0 ; 
		
		try {
			// 定义参数
			HashMap<String,Object> map = new HashMap<String,Object>() ;
			map.put("dbType", PlatformUtil.getDbType());
			map.put("ledgerId", ledger.getLedgerId());
			map.put("sessionId", sessionId) ;
			
			// 校验科目是否在系统中存在
			voucherCheckMapper.checkAccIsExists(map);
			
			// 补全科目ID
			voucherCheckMapper.addAccId(map);
			
			// 会计科目在账簿下是否未启用或已失效
			voucherCheckMapper.isExistsAndEnabledAcc(map);
			
			// 校验供应商、客户、产品、内部往来、个人往来、项目、成本中心、自定义1、自定义2、自定义3、自定义4（即在系统中是否存在且已分配到账簿）
			
			
			// 判断是否存在错误记录
			existsError = voucherCheckMapper.getInvalidRecord(map) ;
			
			// 如果不存在错误记录则处理凭证格式
			if(existsError == 0){
				// 处理凭证格式：1-金额式，2-数量金额式，3-外币金额式
				List<HashMap<String,String>> maps = voucherCheckMapper.getVoucherAccInfo(map) ;
				if(maps != null && maps.size() >0){
					// 外币科目
					List<HashMap<String,String>> fcnyMaps = new ArrayList<HashMap<String,String>>() ; 
					// 数量科目
					List<HashMap<String,String>> amtMaps = new ArrayList<HashMap<String,String>>() ; 
					// 记录行号
					StringBuffer fRowNum = new StringBuffer() ;
					StringBuffer aRowNum = new StringBuffer() ;
					
					for(HashMap<String,String> accMap : maps){
						// 外币科目
						String isForeignCny = accMap.get("IS_FOREGIN_CNY") ;
						if(Integer.parseInt(isForeignCny) == 1){
							fcnyMaps.add(accMap) ;
							fRowNum.append(accMap.get("ROW_NUM")).append(", ") ;
						}
						// 数量科目
						String isAmt = accMap.get("IS_AMOUNT") ;
						if(Integer.parseInt(isAmt) == 1){
							amtMaps.add(accMap) ;
							aRowNum.append(accMap.get("ROW_NUM")).append(", ") ;
						}
					}
					// 外币金额式凭证
					if(!fcnyMaps.isEmpty() && amtMaps.isEmpty()){
						map.put("templateType", "3") ;
					}
					// 数量金额式凭证
					if(fcnyMaps.isEmpty() && amtMaps.isEmpty()){
						map.put("templateType", "2") ;
					}
					// 既存在数量科目也存在外币科目
					if(!fcnyMaps.isEmpty() && !amtMaps.isEmpty()){
						//String msg = "第"+fRowNum.toString()+"行为外币科目 ; " + "第"+aRowNum.toString()+"为数量科目 ; 目前系统不支持外币数量式凭证。" ;
						HashMap<String, String> params = new HashMap<String, String>();
						params.put("param1", fRowNum.toString());
						params.put("param2", aRowNum.toString());
						String msg = XipUtil.getMessage(XCUtil.getLang(), "XC_GL_D_HWWBKMD_WSLKM", params) ;
						throw new Exception(msg) ;
						
					}
				}else{
					// 金额式凭证
					map.put("templateType", "1") ;
				}
				
				voucherCheckMapper.updTemplateType(map);
			}
			
		} catch (Exception e) {
			throw e ;
		}
		
		return existsError ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: generateJSONObject</p> 
	 * <p>Description: 生成符合凭证格式的JSONObject </p> 
	 * @param sessionId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.VoucherHandlerDAO#generateJSONObject(java.lang.String)
	 */
	@Override
	public VHead getTmpVoucher(String sessionId) throws Exception {
		VHead head = new VHead() ;
		
		try {
			// 凭证头信息
			head = voucherCheckMapper.getVHead(sessionId) ;
			
			// 凭证分录行
			List<VLine> lines = voucherCheckMapper.getVLines(sessionId) ;
			
			head.setLines(lines);
			
		} catch (Exception e) {
			throw e ;
		}
		
		return head ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getExpVoucher</p> 
	 * <p>Description: 导出凭证信息 </p> 
	 * @param headId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.VoucherHandlerDAO#getExpVoucher(java.lang.String)
	 */
	@Override
	public List<HashMap<String,Object>> getExpVoucher(String headId) throws Exception {
		return voucherCheckMapper.getExpVoucher(headId) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getBgCHKVoucher</p> 
	 * <p>Description: 查询凭证预算校验信息 </p> 
	 * @param headId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.VoucherHandlerDAO#getBgCHKVoucher(java.lang.String)
	 */
	@Override
	public List<HashMap<String,Object>> getBgCHKVoucher(String headId) throws Exception {
		HashMap<String,String> map = new HashMap<String,String>() ;
		map.put("headId", headId) ;
		map.put("dbType", PlatformUtil.getDbType()) ;
		
		return voucherCheckMapper.getBgCHKVoucher(map) ;
	}
}
