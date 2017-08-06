package com.xzsoft.xc.ex.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xzsoft.xc.bg.api.impl.BudegetDataManageApi;
import com.xzsoft.xc.bg.util.XCBGConstants;
import com.xzsoft.xc.ex.dao.EXCheckAndPayDAO;
import com.xzsoft.xc.ex.dao.XCEXCommonDAO;
import com.xzsoft.xc.ex.modal.CheckAndPayDocDTO;
import com.xzsoft.xc.ex.modal.EXDocBean;
import com.xzsoft.xc.ex.modal.IAFactBean;
import com.xzsoft.xc.ex.modal.PayDocBean;
import com.xzsoft.xc.ex.modal.PayDocDtlBean;
import com.xzsoft.xc.ex.service.BorrowMoneyService;
import com.xzsoft.xc.ex.service.EXCheckAndPayService;
import com.xzsoft.xc.ex.service.EXDocBaseService;
import com.xzsoft.xc.ex.service.EXVoucherService;
import com.xzsoft.xc.ex.util.XCEXConstants;
import com.xzsoft.xc.gl.common.AbstractVoucherHandler;
import com.xzsoft.xc.gl.dao.VoucherHandlerDAO;
import com.xzsoft.xc.gl.modal.VHead;
import com.xzsoft.xc.gl.modal.VLine;
import com.xzsoft.xc.gl.service.VoucherHandlerService;
import com.xzsoft.xc.util.common.XCDateUtil;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xc.util.service.RuleService;
import com.xzsoft.xc.util.service.XCWFService;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.platform.util.XipUtil;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;
import com.xzsoft.xip.wf.dao.engine.ProcessInstanceDAO;

/**
 * @ClassName: EXCheckAndPayServiceImpl 
 * @Description: 单据复核、单据支付的处理接口
 * @author linp
 * @date 2016年3月23日 下午4:08:03 
 *
 */
@Service("exCheckAndPayService")
public class EXCheckAndPayServiceImpl implements EXCheckAndPayService {
	
	// 日志记录器
	private static final Logger log = Logger.getLogger(EXCheckAndPayServiceImpl.class.getName()) ;
	
	@Resource
	private EXCheckAndPayDAO exCheckAndPayDAO ;
	@Resource
	private XCEXCommonDAO xcexCommonDAO ;
	@Resource
	private VoucherHandlerDAO voucherHandlerDAO ;
	@Resource
	private AbstractVoucherHandler voucherHandlerCommonService ;
	@Resource
	private EXVoucherService exVoucherService ;
	@Resource
	private RuleService ruleService ;
	@Resource
	private XCWFService xcwfService ;
	@Resource
	private VoucherHandlerService voucherHandlerService ;
	@Resource
	private BorrowMoneyService borrowMoneyService ;
	@Resource
	private ProcessInstanceDAO processInstanceDAO ;
	@Resource
	private EXDocBaseService exDocBaseService ;

	/*
	 * (非 Javadoc) 
	 * <p>Title: checkDoc</p> 
	 * <p>Description: 
	 * 单据复核处理:
	 * 		1、生成付款单信息
	 * 		2、生成付款或挂账凭证
	 * 		3、更新业务单据信息
	 * </p> 
	 * @param dto
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.service.EXCheckAndPayService#checkDoc(java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public JSONObject checkEXDoc(CheckAndPayDocDTO dto) throws Exception {
		JSONObject jo = new JSONObject() ;
		try {
			// 当前登录用户
			String userId = CurrentSessionVar.getUserId() ;
			// 当前日期
			Date cdate = CurrentUserUtil.getCurrentDate() ;
			
			// 查询单据信息
			EXDocBean docBean = xcexCommonDAO.getEXDoc(dto.getExDocId(), false) ;
			
			// 判断备用金余额与还款金额、核销金额
			IAFactBean factBean = this.getIAFactBean(dto.getLedgerId(), "1", docBean.getCreatedBy()) ;
			double iaBal = factBean.getIABal() ;
			String docCat = dto.getExCatCode() ;
			if(XCEXConstants.EX_DOC_EX05.equals(docCat)){ // 还款单
				// 如果最终还款金额大于剩余备用金时,则不能复核
				if(iaBal<dto.getPayAmt()){ 
					//throw new Exception("还款金额不能大于备用金余额; 当前备用金余额:"+iaBal+", 还款金额:"+dto.getPayAmt()) ;
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_checkEXDoc_EXCEPTION_01", null)+iaBal+XipUtil.getMessage(XCUtil.getLang(), "XC_EX_checkEXDoc_EXCEPTION_02", null)+dto.getPayAmt());
				}
			}else if(XCEXConstants.EX_DOC_EX03.equals(docCat) || XCEXConstants.EX_DOC_EX04.equals(docCat)){ // 费用报销单和差旅报销单
				// 如果最终核销金额大于剩余备用金时,则不能复核
				if(iaBal<dto.getCancelAmt()){ 
					//throw new Exception("核销金额不能大于备用金余额; 当前备用金余额:"+iaBal+", 核销金额:"+dto.getCancelAmt()) ;
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_checkEXDoc_EXCEPTION_03", null)+iaBal+XipUtil.getMessage(XCUtil.getLang(), "XC_EX_checkEXDoc_EXCEPTION_04", null)+dto.getCancelAmt());
				}
				
				// 如果本次支付金额为零, 则不能采用挂账支付
				if(docBean.getAmount() == dto.getCancelAmt() && XCEXConstants.PAY_METHOD_CREDIT.equals(dto.getPayMethod())){
					//throw new Exception("本次支付金额为零不能采用挂账支付; 单据金额："+docBean.getAmount()+", 备用金余额："+iaBal+", 核销金额："+dto.getCancelAmt()) ;
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_checkEXDoc_EXCEPTION_05", null)+docBean.getAmount()+XipUtil.getMessage(XCUtil.getLang(), "XC_EX_checkEXDoc_EXCEPTION_06", null)+iaBal+XipUtil.getMessage(XCUtil.getLang(), "XC_EX_checkEXDoc_EXCEPTION_04", null)+dto.getCancelAmt());
				}
				
			}else{ 
				// 扩展使用
			}
			
			if(XCEXConstants.PAY_METHOD_ONCE.equals(dto.getPayMethod())) {// 立即支付
				// 1.生成支付凭证
				String jsonStr = exVoucherService.newVoucher(dto);
				JSONObject vJo = new JSONObject(jsonStr) ;
				if("1".equals(vJo.getString("flag"))){
					throw new Exception(vJo.getString("msg")) ;
				}
				String headId = vJo.getString("headId") ;	// 凭证头ID
				
				// 2.生成付款单
				dto.setPayCat("auto");	// 支付单生成方式：new-手动创建,auto-自动生成
				this.newPayDoc(dto, headId,userId,cdate);
				
				//  3.更新报销单与凭证和支付单的对应关系
				docBean.setPayMethod(dto.getPayMethod());		// 支付方式
				docBean.setExPayId(dto.getPayId());				// 支付单ID
				if(XCEXConstants.EX_DOC_EX05.equals(docCat)){
					docBean.setAmount(dto.getPayAmt());    		// 还款单重新设置单据金额
				}
				docBean.setCancelAmt(dto.getCancelAmt());		// 核销金额	
				docBean.setHeadId(headId);						// 付款凭证ID(与付款单对应的凭证一致)
				docBean.setVstatus("2");						// 凭证状态
				docBean.setFinUserId(userId);					// 复核人（凭证制单人）
				docBean.setFinDate(dto.getAccDate());			// 复核日期
				docBean.setLastUpdateDate(cdate);
				docBean.setLastUpdatedBy(userId);
				docBean.setUpdateMode("once");					// 立即支付
				
				exCheckAndPayDAO.updEXDoc(docBean);
				
				// 返回参数
				jo.put("headId", headId) ;
				jo.put("payId", dto.getPayId()) ;
				
			}else if(XCEXConstants.PAY_METHOD_CREDIT.equals(dto.getPayMethod())) {// 挂账支付
				// 1.生成挂账凭证
				String jsonStr = exVoucherService.newVoucher(dto) ;
				JSONObject vJo = new JSONObject(jsonStr) ;
				if("1".equals(vJo.getString("flag"))){
					throw new Exception(vJo.getString("msg")) ;
				}
				String headId = vJo.getString("headId") ;		// 凭证头ID	
				
				// 2.更新报销单与凭证和支付单的对应关系
				docBean.setCancelAmt(dto.getCancelAmt());				// 核销金额	
				docBean.setHeadId(headId);								// 凭证头ID
				docBean.setVstatus("2"); 								// 凭证状态：1-凭证未生成，2-凭证已生成，3-凭证已复核，4-凭证已取消
				docBean.setFinUserId(userId);							// 复核人（凭证制单人）
				docBean.setFinDate(dto.getAccDate());					// 复核日期（凭证制制单日期）
				docBean.setPayMethod(dto.getPayMethod());				// 支付方式
				docBean.setLastUpdateDate(cdate);
				docBean.setLastUpdatedBy(userId);
				docBean.setUpdateMode("credit"); 						// 挂账支付模式
				
				exCheckAndPayDAO.updEXDoc(docBean);
				
				// 返回参数
				jo.put("headId", headId) ;
				jo.put("payId", dto.getPayId()) ;
				
			}else{
				// 扩展使用
			}
			
			// 核销备用金余额
			String exCatCode = dto.getExCatCode() ;	// 单据类型
			// 还款单、差旅报销单和费用报销单
			if(XCEXConstants.EX_DOC_EX03.equals(exCatCode) 
					|| XCEXConstants.EX_DOC_EX04.equals(exCatCode) 
					|| XCEXConstants.EX_DOC_EX05.equals(exCatCode)){ 
				// 设置备用金类型和核销金额
				float iaFactAmt = 0 ;
				String iaType = null ;
				if(XCEXConstants.EX_DOC_EX05.equals(exCatCode)){ // 还款单
					iaFactAmt = dto.getPayAmt() ;
					iaType = "3" ;
					
				}else{ // 费用报销单和差旅报销单
					iaFactAmt = dto.getCancelAmt() ;
					iaType = "1" ;
				}
				
				// 执行备用金额核销处理
				IAFactBean iaFactBean = new IAFactBean() ;
				iaFactBean.setIaFactId(UUID.randomUUID().toString());   // 备用金明细ID
				iaFactBean.setLedgerId(dto.getLedgerId());				// 账簿ID
				iaFactBean.setDocId(dto.getExDocId());					// 单据ID,<如果是查询个人备用金，则传空> 
				iaFactBean.setIAFactAmt(iaFactAmt); 					// 单据金额（凭证借方合计数）<如果是查询个人备用金，则传零> 
				iaFactBean.setExIAType(iaType);							// 备用金类型：1-核销款，2-借款，3-还款
				iaFactBean.setOperationType("2");  						// 操作类型:1-查询备用金 ,2-更新备用金
				iaFactBean.setExUserId(docBean.getCreatedBy()); 		// 当前用户ID
				iaFactBean.setCreatedBy(userId);
				iaFactBean.setCreationDate(cdate);
				
				borrowMoneyService.handlerUserImprest(iaFactBean);
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
		
		return jo ;
	}
	
	/**
	 * @Title: getIAFactBean 
	 * @Description: 获取当前备用金余额信息
	 * @param ledgerId
	 * @param docId
	 * @param userId
	 * @return
	 * @throws Exception    设定文件
	 */
	private IAFactBean getIAFactBean(String ledgerId,String optype,String userId) throws Exception {
		IAFactBean iaFactBean = new IAFactBean() ;
		try {
			iaFactBean.setLedgerId(ledgerId);
			iaFactBean.setOperationType(optype);
			iaFactBean.setExUserId(userId);
			
			borrowMoneyService.handlerUserImprest(iaFactBean);
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
		
		return iaFactBean ;
	}
	
	/**
	 * @Title: newPayDoc 
	 * @Description: 生成付款单
	 * @param dto
	 * @param headId
	 * @return
	 * @throws Exception    设定文件
	 */
	private void newPayDoc(CheckAndPayDocDTO dto,String headId,String userId, Date cdate) throws Exception {
		try {
			// 会计日期
			String accDate = XCDateUtil.date2Str(dto.getAccDate()) ;
			
			// 支付单ID
			if(dto.getPayId() == null){
				String exPayId = UUID.randomUUID().toString() ;	
				dto.setPayId(exPayId);
			}
			
			// 取得支付单编码规则
			String ruleCode = exCheckAndPayDAO.getRuleCode(dto.getExCatCode(), dto.getLedgerId()) ;
			if(ruleCode == null) {
				//throw new Exception("获取支付单编码规则失败>> 原因可能为：支付单未分配到当前账簿 或者 支付单未指定编码规则") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_newPayDoc_EXCEPTION_01", null));
			}
			
			// 取得支付单据编码
			String year = accDate.substring(0, 4) ;
			String month = accDate.substring(5, 7) ;
			month = String.valueOf(Integer.parseInt(month)) ;
			String exPayCode = ruleService.getNextSerialNum(ruleCode, dto.getLedgerId(), null, year, month, userId) ;
			if(exPayCode == null){
				//throw new Exception("生成支付单编码失败!") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_newPayDoc_EXCEPTION_02", null));
			}
			
			// 付款单头信息
			PayDocBean payDocBean = new PayDocBean() ;
			payDocBean.setExPayId(dto.getPayId());			// 支付单ID
			payDocBean.setLedgerId(dto.getLedgerId());		// 账簿ID
			payDocBean.setExPayCode(exPayCode);				// 支付单号
			payDocBean.setExPayDesc(dto.getExPayDesc());	// 付款说明
			payDocBean.setDepositBankId(dto.getBankId());	// 付款账号
			payDocBean.setExPayDate(dto.getAccDate());		// 支付日期
			payDocBean.setExPayTAmt(dto.getPayAmt());		// 支付总额
			payDocBean.setPayCat(dto.getPayCat()); 			// 支付单生成方式：new-手动创建,auto-自动生成
			payDocBean.setPayType(dto.getPayType());		// 付款方式
			payDocBean.setCaId(dto.getCashId());			// 现金流量项目
			payDocBean.setvHeadId(headId);					// 凭证头ID
			payDocBean.setvStatus("2");						// 凭证状态
			payDocBean.setPayStatus("N");					// 支付状态
			payDocBean.setFinUserId(userId);				// 复核人（凭证制单人）
			payDocBean.setFinDate(dto.getAccDate());		// 复核日期
			payDocBean.setCreatedBy(userId);
			payDocBean.setCreationDate(cdate);
			payDocBean.setLastUpdateDate(cdate);
			payDocBean.setLastUpdatedBy(userId);
			
			// 付款单明细信息
			List<PayDocDtlBean> payDocDtls = new ArrayList<PayDocDtlBean>() ;
			if(XCEXConstants.PAY_MODE_CHECK.equals(dto.getOpType())){ // 单据复核立即支付并自动创建付款单
				PayDocDtlBean payDocDtlBean = new PayDocDtlBean() ;
				payDocDtlBean.setExPayDtlId(UUID.randomUUID().toString());
				payDocDtlBean.setExPayId(dto.getPayId());
				payDocDtlBean.setExDocId(dto.getExDocId());
				payDocDtlBean.setPayAmt(dto.getPayAmt());
				payDocDtlBean.setCreationDate(cdate);
				payDocDtlBean.setCreatedBy(userId);
				payDocDtlBean.setLastUpdatedBy(userId);
				payDocDtlBean.setLastUpdateDate(cdate);
				
				payDocDtls.add(payDocDtlBean) ;
				
			}else if(XCEXConstants.PAY_MODE_PAYMENT.equals(dto.getOpType())){ // 单据挂账支付手动创建付款单
				List<String> docIds = dto.getExDocIds() ;
				if(docIds != null && docIds.size()>0){
					// 查询挂账支付的报销单信息
					List<EXDocBean> docBeans = xcexCommonDAO.getEXDocs(docIds, false) ;
					HashMap<String,Double> payMap = new HashMap<String,Double>() ;
					float totalPayAmt = 0  ;
					for(EXDocBean docBean : docBeans){
						String docId = docBean.getDocId() ;
						double docAmt = docBean.getAmount() ;		// 单据金额
						double cancelAmt = docBean.getCancelAmt() ;	// 核销金额
						double payAmt = docAmt - cancelAmt ; 		// 本次支付金额
						
						totalPayAmt += payAmt ;		// 付款明细本次支付金额之和
						
						payMap.put(docId, payAmt) ;
					}
					
					// 如果付款总额与明细行付款总额, 则不能保存
					if(dto.getPayAmt() != totalPayAmt){
						//throw new Exception("付款总额与付款明细的本次支付金额之和不相等,请确认!") ;
						throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_newPayDoc_EXCEPTION_03", null));
					}
					
					for(String docId : docIds) {
						PayDocDtlBean payDocDtlBean = new PayDocDtlBean() ;
						payDocDtlBean.setExPayDtlId(UUID.randomUUID().toString());
						payDocDtlBean.setExPayId(dto.getPayId());
						payDocDtlBean.setExDocId(docId);
						payDocDtlBean.setPayAmt(payMap.get(docId));
						payDocDtlBean.setCreationDate(cdate);
						payDocDtlBean.setCreatedBy(userId);
						payDocDtlBean.setLastUpdatedBy(userId);
						payDocDtlBean.setLastUpdateDate(cdate);
						
						payDocDtls.add(payDocDtlBean) ;
					}
				}else{
					//throw new Exception("付款单明细不能为空!") ;
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_newPayDoc_EXCEPTION_04", null));
				}
			}else{
				// 其他方式
			}
			payDocBean.setPayDtl(payDocDtls);
			
			// 执行保存处理
			exCheckAndPayDAO.createPayDoc(payDocBean);
			
		} catch (Exception e) {
			throw e ;
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: uncheckEXDoc</p> 
	 * <p>Description: 报销单取消复核处理 </p> 
	 * @param jsonObject
	 * @throws ja 
	 * @see com.xzsoft.xc.ex.service.EXCheckAndPayService#uncheckEXDoc(java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void uncheckEXDoc(JSONArray ja) throws Exception {
		try {
			// 取得报销单ID列表
			List<String> exdocIds = new ArrayList<String>() ;
			for(int i=0; i<ja.length(); i++){
				Object obj = ja.get(i) ;
				if(obj != null){
					// 费用单据ID
					JSONObject jo = new JSONObject(String.valueOf(obj)) ;
					String exDocId = jo.getString("EX_DOC_ID") ;
					String exCatCode = jo.getString("EX_CAT_CODE") ;
					if(XCEXConstants.EX_DOC_EX02.equals(exCatCode)){
						//throw new Exception("所选单据中包含借款单不能执行取消复核处理!") ;
						throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_uncheckEXDoc_EXCEPTION_01", null));
					}else{
						exdocIds.add(exDocId) ;
					}
				}
			}
			
			// 查询费用报销单信息
			List<EXDocBean> docBeans = xcexCommonDAO.getEXDocs(exdocIds, false) ;
			if(docBeans != null && docBeans.size()>0){
				
				// 存储报销单信息
				HashMap<String,EXDocBean> exMap = new HashMap<String,EXDocBean>();
				// 存储支付单与报销单的对应关系
				HashMap<String,String> peMap = new HashMap<String,String>();
				// 存储凭证与报销单的对应关系
				HashMap<String,String> heMap = new HashMap<String,String>();
				
				// 存储立即支付单据的支付单ID
				List<String> oncePayIds = new ArrayList<String>() ;		
				// 存储立即支付单据的凭证ID
				List<String> onceHeadIds = new ArrayList<String>() ; 	
				// 存储立即支付单据的报销单ID
				List<String> onceDocIds = new ArrayList<String>() ;	
				
				// 存储挂账支付单据的凭证ID
				List<String> creditHeadIds = new ArrayList<String>() ; 	
				// 存储挂账支付单据的报销单ID	
				List<String> creditDocIds = new ArrayList<String>() ;		
				
				// 循环报销单据信息, 得到取消条件
				for(EXDocBean bean : docBeans){
					String payMethod = bean.getPayMethod() ;	// 支付方式：1-立即支付,2-挂账支付
					String exPayId = bean.getExPayId() ;		// 支付单ID
					String exdocId = bean.getDocId() ;			// 报销单ID
					String headId = bean.getHeadId() ;			// 凭证头ID
					
					if(XCEXConstants.PAY_METHOD_CREDIT.equals(payMethod)){	// 挂账支付
						// 如果单据挂账支付且已经生成支付单,则不能取消复核处理
						if(exPayId != null && !"".equals(exPayId)){
							//throw new Exception("单据"+bean.getDocCode()+"支付方式为挂账支付且已生成付款单,不能取消复核处理!") ;
							throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC", null)+bean.getDocCode()+XipUtil.getMessage(XCUtil.getLang(), "XC_EX_uncheckEXDoc_EXCEPTION_02", null));
						}
						creditHeadIds.add(headId) ;
						creditDocIds.add(exdocId) ;
						
					}else if(XCEXConstants.PAY_METHOD_ONCE.equals(payMethod)){	// 立即支付
						oncePayIds.add(exPayId) ;
						onceHeadIds.add(headId) ;
						onceDocIds.add(exdocId) ;
						
						exMap.put(exdocId, bean) ;
						peMap.put(exPayId, exdocId) ;
						heMap.put(headId, exdocId) ;
						
					}
					
					// 增加备用金余额
					String exCatCode = bean.getDocCatCode();	// 单据类型
					// 还款单、差旅报销单和费用报销单
					if(XCEXConstants.EX_DOC_EX03.equals(exCatCode) 
							|| XCEXConstants.EX_DOC_EX04.equals(exCatCode) 
							|| XCEXConstants.EX_DOC_EX05.equals(exCatCode)){ 
						
						// 执行备用金增加处理
						IAFactBean iaFactBean = new IAFactBean() ;
						iaFactBean.setLedgerId(bean.getLedgerId());				// 账簿ID
						iaFactBean.setDocId(bean.getDocId());					// 单据ID,<如果是查询个人备用金，则传空> 
						iaFactBean.setOperationType("3");  						// 操作类型:1-查询备用金 ,2-更新备用金,3-删除备用金
						iaFactBean.setExUserId(bean.getCreatedBy());
						
						borrowMoneyService.handlerUserImprest(iaFactBean);
					}
					
					// 将单据预算发生数转换为预算占用数
					BudegetDataManageApi.updateFactBudget(exdocId, XCBGConstants.BG_FACT_TYPE);
				}
				
				// 对挂账支付单据执行取消复核处理
				if(!creditDocIds.isEmpty()){
					this.uncheckCreditDoc(creditHeadIds, creditDocIds);
				}
				
				// 对立即支付单据执行取消复核处理
				if(!onceDocIds.isEmpty()){
					this.uncheckOnceDoc(oncePayIds, onceHeadIds, onceDocIds, exMap, peMap, heMap);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: cancelDoc</p> 
	 * <p>Description: 取消单据 </p> 
	 * @param jsonStr
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.service.EXCheckAndPayService#cancelDoc(java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class}) 
	public void cancelDoc(String docId,String cancelReason) throws Exception {
		try {
			// 查询单据信息
			EXDocBean bean = xcexCommonDAO.getEXDoc(docId, false) ;
			String insCode = bean.getInsCode() ;
			String headId = bean.getHeadId() ;
			String vstatus = bean.getVstatus() ;	// 凭证状态:1-凭证未生成，2-凭证已生成，3-凭证已复核，4-凭证已取消
			String verifierId = bean.getVerifierId() ; // 审核人
			if("3".equals(vstatus)){
				//throw new Exception("单据已复核不能执行取消处理") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_cancelDoc_EXCEPTION_01", null));
			}
			if(verifierId != null && !"".equals(verifierId)){
				//throw new Exception("单据已审核不能执行取消处理") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_cancelDoc_EXCEPTION_02", null));
			}
			
			// 将单据流程审批状态设置为驳回状态
			bean.setAuditStatus("D");
			bean.setAuditStatusDesc("驳回");
			bean.setAuditStatusDate(CurrentUserUtil.getCurrentDate());
//			bean.setAuditUsers(CurrentSessionVar.getUserId());
			bean.setHeadId(null);
			bean.setVstatus("1");
			bean.setLastUpdatedBy(CurrentSessionVar.getUserId());
			bean.setLastUpdateDate(CurrentUserUtil.getCurrentDate());
			bean.setSignStatus("1");	// 待签收
			bean.setSignUserId(null);	// 签收人
			bean.setSignDate(null);		// 签收日期
			
			exCheckAndPayDAO.upEXDocAuditInfo(bean);
			
			// 删除草稿凭证
			if(headId != null && !"".equals(headId)){
				List<String> list = new ArrayList<String>() ;
				list.add(headId) ;
				
				voucherHandlerDAO.delVouchers(list);
			}
			
			// 重置流程实例(将归档流程实例重置运行时)
			if(insCode != null && !"".equals(insCode)){
				String insId = processInstanceDAO.getArchInsIdByCode(insCode) ;
				if(insId != null && !"".equals(insId)){
					xcwfService.copyArchInstance(insId);
					xcwfService.convertCompletedInstance2Reject(insId,cancelReason);
					xcwfService.delArchInstance(insId);
					
				}else{
					//throw new Exception("流程实例不存在, 实例编码为【"+insCode+"】") ;
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_cancelDoc_EXCEPTION_03", null)+"【"+insCode+"】");
				}
			}
			
			// 处理预算占用数
			exDocBaseService.deleteBudgetOccupancyAmount(docId,bean.getDocCatCode(),bean.getApplyDocId());
			
		} catch (Exception e) {
			throw e ;
		}
	}
	
	/**
	 * @Title: uncheckOnceDoc 
	 * @Description: 取消立即支付单据
	 * @param oncePayIds	存储立即支付单据的支付单ID
	 * @param onceHeadIds	存储立即支付单据的凭证ID
	 * @param onceDocIds	存储立即支付单据的报销单ID
	 * @param exMap			存储报销单信息
	 * @throws Exception    设定文件
	 */
	private void uncheckOnceDoc(List<String> oncePayIds, List<String> onceHeadIds,List<String> onceDocIds, 
				HashMap<String,EXDocBean> exMap,HashMap<String,String> peMap,HashMap<String,String> heMap) throws Exception {
		try {
			// 判断出纳是否已经对支付执行了签字处理
			List<PayDocBean> payments = xcexCommonDAO.getPayDocs(oncePayIds, false) ;
			for(PayDocBean payment : payments){
				// 支付单ID
				String payId = payment.getExPayId() ;
				// 签字人ID
				String signatoryId = payment.getSignatoryId() ;
				if(signatoryId != null &&  !"".equals(signatoryId)){
					String exDocId = peMap.get(payId) ;
					EXDocBean docBean = exMap.get(exDocId) ;
					//throw new Exception("单据"+docBean.getDocCode()+"出纳已签字,不能取消复核") ;
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC", null)+docBean.getDocCode()+XipUtil.getMessage(XCUtil.getLang(), "XC_EX_uncheckOnceDoc_EXCEPTION_01", null));
				}
			}
			
			// 判断支付凭证是否已经过账
			List<VHead> vouchers = voucherHandlerDAO.getVHeads(onceHeadIds, false) ;
			for(VHead head : vouchers){
				String headId = head.getHeadId() ;
				// 记账人ID
				String bookkeeperId = head.getBookkeeperId() ;
				if(bookkeeperId != null && !"".equals(bookkeeperId)){
					String exDocId = heMap.get(headId) ;
					EXDocBean docBean = exMap.get(exDocId) ;
					//throw new Exception("单据"+docBean.getDocCode()+"对应凭证已过账,不能取消复核") ;
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC", null)+docBean.getDocCode()+XipUtil.getMessage(XCUtil.getLang(), "XC_EX_uncheckOnceDoc_EXCEPTION_02", null));
				}
			}
			
			// 取消凭证复核相当于取消审核、撤回提交, 使凭证为草稿状态
			exVoucherService.cancelVoucher(onceHeadIds) ;
			
			// 删除凭证
			voucherHandlerDAO.delVouchers(onceHeadIds);
			
			// 删除支付单
			exCheckAndPayDAO.dePayDocs(oncePayIds);
			
			// 批量清除费用报销单与凭证和支付方式的对应关系
			exCheckAndPayDAO.batchClearEXDocs(onceDocIds);
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
	}

	/**
	 * @Title: uncheckCreditDoc 
	 * @Description: 取消挂账支付单据
	 * @param creditHeadIds	存储挂账支付单据的凭证ID
	 * @param creditDocIds 	存储挂账支付单据的报销单ID	
	 * @throws Exception    设定文件
	 */
	private void uncheckCreditDoc(List<String> creditHeadIds,List<String> creditDocIds) throws Exception {
		try {
			// 取消凭证复核相当于取消审核、撤回提交, 使凭证为草稿状态
			exVoucherService.cancelVoucher(creditHeadIds) ;
			
			// 删除凭证
			voucherHandlerDAO.delVouchers(creditHeadIds);
			
			// 批量清除费用报销单与凭证和支付方式的对应关系
			exCheckAndPayDAO.batchClearEXDocs(creditDocIds);
			
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: createPayDoc</p> 
	 * <p>Description: 
	 * 单据付款处理:
	 * 		1、生成付款单信息
	 * 		2、生成付款凭证
	 * 		3、更新业务单据信息
	 * </p> 
	 * @param dto
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.service.EXCheckAndPayService#createPayDoc(java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public JSONObject createPayDoc(CheckAndPayDocDTO dto) throws Exception {
		JSONObject jo = new JSONObject() ;
		try {
			String userId = CurrentSessionVar.getUserId() ;	// 当前登录用户
			Date cdate = CurrentUserUtil.getCurrentDate() ;	// 当前日期
			
			String exPayId = UUID.randomUUID().toString() ;	// 支付单ID
			dto.setPayId(exPayId);
			
			// 1.生成付款凭证
			String jsonStr = exVoucherService.newPayVoucher(dto) ;
			JSONObject vJo = new JSONObject(jsonStr) ;
			if("1".equals(vJo.getString("flag"))){
				//throw new Exception("生成付款单失败："+vJo.getString("msg")) ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC_SAVE_FAILED", null)+vJo.getString("msg"));
			}
			String headId = vJo.getString("headId") ;	// 凭证头ID
			
			// 2.生成付款单
			dto.setPayCat("new");;		// 支付单生成方式：new-手动创建,auto-自动生成
			this.newPayDoc(dto, headId, userId, cdate);
			
			// 3.更新报销单与支付单的对应关系
			EXDocBean docBean = new EXDocBean() ;	
			docBean.setExPayId(exPayId);    		// 支付单ID
			docBean.setLastUpdateDate(cdate);
			docBean.setLastUpdatedBy(userId);
			docBean.setUpdateMode("add");			// 手动创建付款单
			
			exCheckAndPayDAO.batchUpdEXDocs(dto.getExDocIds(), docBean);
			
			// 返回参数
			jo.put("headId", headId) ;
			jo.put("payId", exPayId) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
		return jo ;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: editPayDoc</p> 
	 * <p>Description: </p> 
	 * @param dto
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.service.EXCheckAndPayService#editPayDoc(java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public JSONObject editPayDoc(CheckAndPayDocDTO dto) throws Exception {
		JSONObject jo = new JSONObject() ;
		
		try {
			// 当前登录用户和当前日期
			String userId = CurrentSessionVar.getUserId() ;	
			Date cdate = CurrentUserUtil.getCurrentDate() ;	
			
			// 1.判断出纳是否已经对支付执行了签字处理
			PayDocBean payDocBean = xcexCommonDAO.getPayDoc(dto.getPayId(), true) ;
			// 签字人ID
			String signatoryId = payDocBean.getSignatoryId() ;
			if(signatoryId != null &&  !"".equals(signatoryId)){
				//throw new Exception("付款单"+dto.getPayCode()+"出纳已签字,不能修改") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_PAY_DOC", null)+dto.getPayCode()+XipUtil.getMessage(XCUtil.getLang(), "XC_EX_editPayDoc_EXCEPTION_01", null));
			}
			
			// 2.判断凭证是否已入账
			VHead head = voucherHandlerDAO.getVHead(dto.getHeadId(), false) ;
			// 记账人ID
			String bookkeeperId = head.getBookkeeperId() ;
			if(bookkeeperId != null && !"".equals(bookkeeperId)){
				//throw new Exception("付款单"+dto.getPayCode()+"对应支付凭证已过账,不能修改") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_PAY_DOC", null)+dto.getPayCode()+XipUtil.getMessage(XCUtil.getLang(), "XC_EX_editPayDoc_EXCEPTION_02", null));
			}
			
			// 3.生成付款凭证
			String jsonStr = exVoucherService.newPayVoucher(dto) ;
			JSONObject vJo = new JSONObject(jsonStr) ;
			if("1".equals(vJo.getString("flag"))){
				//throw new Exception("修改付款单失败："+vJo.getString("msg")) ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC_EDIT_FAILED", null)+vJo.getString("msg"));
			}
			String headId = vJo.getString("headId") ;	// 凭证头ID
			
			// 4.计算新增与减少的报销单信息
			List<PayDocDtlBean> addPayDtlBeans = new ArrayList<PayDocDtlBean>() ;
			List<String> addPayDtlIds = new ArrayList<String>() ;		// 存储新增付款单明细行
			List<String> delPayDtlIds = new ArrayList<String>() ;		// 存储删除付款单明细行
			
			List<String> docIds = dto.getExDocIds() ;					// 当前付款单明细信息
			HashMap<String,Object> amtMap = dto.getAmtMap() ;
			
			// 已存在付款单明细行信息
			List<PayDocDtlBean> existsPayDtls = payDocBean.getPayDtl() ;
			for(PayDocDtlBean dtlBean : existsPayDtls){
				String docId = dtlBean.getExDocId() ;
				if(!docIds.contains(docId)){ 
					// 记录待删除的付款单明细行
					delPayDtlIds.add(docId) ;
				}else{
					// 剔除存在的付款单明细行
					docIds.remove(docId) ;
				}
			}
			// 记录待新增的付款单明细行
			if(docIds != null && docIds.size()>0){
				for(String docId : docIds) {
					PayDocDtlBean dtlBean = new PayDocDtlBean() ;
					dtlBean.setExPayDtlId(UUID.randomUUID().toString());
					dtlBean.setExPayId(payDocBean.getExPayId());
					dtlBean.setExDocId(docId);
					dtlBean.setPayAmt(Float.parseFloat(String.valueOf(amtMap.get(docId))));
					dtlBean.setCreatedBy(userId);
					dtlBean.setCreationDate(cdate);
					dtlBean.setLastUpdateDate(cdate);
					dtlBean.setLastUpdatedBy(userId);
					
					addPayDtlIds.add(docId) ;
					addPayDtlBeans.add(dtlBean) ;
				}
				payDocBean.setAddPayDtlBeans(addPayDtlBeans);
			}
			
			// 5.更新付款单
			payDocBean.setExPayDesc(dto.getExPayDesc());	// 付款说明
			payDocBean.setDepositBankId(dto.getBankId());	// 付款账号
			payDocBean.setExPayDate(dto.getAccDate());		// 支付日期
			payDocBean.setExPayTAmt(dto.getPayAmt());		// 付款总额
			payDocBean.setvHeadId(headId);					// 支付凭证
			payDocBean.setvStatus("2");						// 凭证状态
			payDocBean.setPayStatus("N");					// 付款状态
			payDocBean.setPayCat("new");	 				// 支付单创建方式：new-手动创建,auto-自动创建
			payDocBean.setPayType(dto.getPayType());		// 付款方式
			payDocBean.setCaId(dto.getCashId());			// 现金流量项目ID
			payDocBean.setFinUserId(userId);				// 制单人
			payDocBean.setFinDate(cdate);					// 制单日期
			payDocBean.setLastUpdateDate(cdate);
			payDocBean.setLastUpdatedBy(userId);
			
			payDocBean.setDelPayDtlIds(delPayDtlIds);		// 删除付款单明细行
			
			exCheckAndPayDAO.updatePayDoc(payDocBean);
			
			// 6.更新报销单
			EXDocBean docBean = new EXDocBean() ;	
			docBean.setExPayId(payDocBean.getExPayId());    // 支付单ID
			docBean.setLastUpdateDate(cdate);
			docBean.setLastUpdatedBy(userId);
			
			// 建立报销单与付款单对应关系
			docBean.setUpdateMode("add");
			exCheckAndPayDAO.batchUpdEXDocs(addPayDtlIds, docBean);
			
			// 清除报销单与付款对应关系
			docBean.setUpdateMode("cancel");
			exCheckAndPayDAO.batchUpdEXDocs(delPayDtlIds, docBean);
			
			// 封装返回参数
			jo.put("headId", headId) ;
			jo.put("payId", payDocBean.getExPayId()) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
		
		return jo ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: uncheckPayDoc</p> 
	 * <p>Description: 取消付款单 </p> 
	 * @param ja
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.service.EXCheckAndPayService#uncheckPayDoc(org.codehaus.jettison.json.JSONArray)
	 */
	@Override
	public void uncheckPayDoc(JSONArray ja) throws Exception {
		try {
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: saveVoucher</p> 
	 * <p>Description: 保存凭证 </p> 
	 * @param headJson
	 * @param lineArray
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.service.EXCheckAndPayService#saveVoucher(java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void saveVoucher(String headJson,String lineArray) throws Exception {
		try {
			// 凭证头信息
			VHead head = voucherHandlerService.headJson2Bean(headJson) ;
			
			// 凭证分录行信息
			List<VLine> lines = voucherHandlerService.lineArray2Bean(lineArray) ;
			head.setLines(lines);
			
			// 执行凭证更新处理
			voucherHandlerService.doUpdateVoucher(head) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: submitVoucher</p> 
	 * <p>Description:  提交凭证 </p> 
	 * @param headJson
	 * @param lineArray
	 * @param paramJson
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.service.EXCheckAndPayService#submitVoucher(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void submitVoucher(String headJson, String lineArray, String paramJson) throws Exception {
		try {
			// 凭证头信息
			VHead head = voucherHandlerService.headJson2Bean(headJson) ;
			
			// 凭证分录行信息
			List<VLine> lines = voucherHandlerService.lineArray2Bean(lineArray) ;
			head.setLines(lines);
			
			// 凭证更新
			voucherHandlerService.doUpdateVoucher(head) ;
			
			// 凭证提交
			voucherHandlerService.doSubmitVoucher(head) ;
			
			// 凭证审核处理
			voucherHandlerService.doCheckVouchers(head) ;

			// 更新单据信息
			this.modifyDocInfo(paramJson);
			
			// 如果单据类型为借款单,则计算备用金余额
			JSONObject paramJo = new JSONObject(paramJson) ;
			String exCatCode = paramJo.getString("exCatCode") ;	// 单据类型
			// 借款单复核凭证提交时,记录个人备用金
			if(XCEXConstants.EX_DOC_EX02.equals(exCatCode)){ 
				
				String docId = paramJo.getString("docId") ;			// 报销单ID
				String exUserId = paramJo.getString("createdBy") ; 	// 报销人
				
				IAFactBean iaFactBean = new IAFactBean() ;
				iaFactBean.setIaFactId(UUID.randomUUID().toString());   		// 备用金明细ID
				iaFactBean.setLedgerId(head.getLedgerId());						// 账簿ID
				iaFactBean.setDocId(docId);										// 单据ID,<如果是查询个人备用金，则传空> 
				iaFactBean.setIAFactAmt(head.getTotalDR()); 					// 单据金额（凭证借方合计数）<如果是查询个人备用金，则传零> 
				iaFactBean.setExIAType("2");									// 备用金类型：1-核销款，2-借款，3-还款
				iaFactBean.setOperationType("2");  								// 操作类型:1-查询备用金 ,2-更新备用金
				iaFactBean.setExUserId(exUserId);
				iaFactBean.setCreatedBy(CurrentSessionVar.getUserId()); // 当前用户ID
				iaFactBean.setCreationDate(CurrentUserUtil.getCurrentDate());
				
				borrowMoneyService.handlerUserImprest(iaFactBean);
			}
			
			// 单据复核 -> 将预算占用数转换为预算发生数
			if(paramJo.has("bizType")){
				// 操作类型 ：EXCHK-报销单凭证提交,EXPAY-付款单凭证提交
				String bizType = paramJo.getString("bizType") ;	 
				
				// 单据复核
				if("EXCHK".equals(bizType)){ 
					String exDocId = paramJo.getString("docId") ;
					BudegetDataManageApi.updateFactBudget(exDocId, XCBGConstants.BG_OCCUR_TYPE);
				}
			}else{
				//throw new Exception("凭证提交时paramJson参数中未提供bizType参数") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_submitVoucher_EXCEPTION_01", null));
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
	}
	
	/**
	 * @Title: modifyDocInfo 
	 * @Description: 修改报销单和支付单信息
	 * @param paramJson
	 * @throws Exception    设定文件
	 */
	private void modifyDocInfo(String paramJson) throws Exception {
		try {
			// 当前登录用户
			String userId = CurrentSessionVar.getUserId() ;	
			// 当前日期
			Date cdate = CurrentUserUtil.getCurrentDate() ;	
			
			// 辅助参数
			JSONObject paramJo = new JSONObject(paramJson) ;
			
			// 操作类型 ：EXCHK-报销单凭证提交,EXPAY-付款单凭证提交
			String bizType = paramJo.getString("bizType") ;		
			
			// 修改单据凭证状态
			if("EXCHK".equals(bizType)){ 
				String payMethod = paramJo.getString("payMethod") ;	// 支付方式：1-立即支付,2-挂账支付
				if("1".equals(payMethod)){	 // 立即支付报销单凭证提交
					// 更新报销凭证状态、审核人和审核日期
					EXDocBean docBean = new EXDocBean() ;
					docBean.setDocId(paramJo.getString("docId"));
					docBean.setVstatus("3");
					docBean.setVerifierId(userId);
					docBean.setVerifierDate(cdate);
					docBean.setLastUpdateDate(cdate);
					docBean.setLastUpdatedBy(userId);
					exCheckAndPayDAO.updEXDocVoucher(docBean);
					
					// 更新付款凭证状态、审核认和审核日期和付款状态
					PayDocBean payDocBean = new PayDocBean() ;
					payDocBean.setExPayId(paramJo.getString("payId"));
					payDocBean.setvStatus("3");
					payDocBean.setVerifierId(userId);
					payDocBean.setVerfyDate(cdate);
					payDocBean.setPayStatus("Y"); 
					payDocBean.setLastUpdateDate(cdate);
					payDocBean.setLastUpdatedBy(userId);
					exCheckAndPayDAO.updPayDocVoucher(payDocBean);
					
				}else if("2".equals(payMethod)){  // 挂账支付报销单凭证提交
					// 更新报销凭证状态、审核人和审核日期
					EXDocBean docBean = new EXDocBean() ;
					docBean.setDocId(paramJo.getString("docId"));
					docBean.setVstatus("3");
					docBean.setVerifierId(userId);
					docBean.setVerifierDate(cdate);
					docBean.setLastUpdateDate(cdate);
					docBean.setLastUpdatedBy(userId);
					exCheckAndPayDAO.updEXDocVoucher(docBean);
					
				}else{
					// 扩展使用
				}
				
			}else if("EXPAY".equals(bizType)){ 
				// 付款单凭证提交 ,直接更新付款表中凭证状态、审核日期和审核人信息
				PayDocBean payDocBean = new PayDocBean() ;
				payDocBean.setExPayId(paramJo.getString("payId"));
				payDocBean.setvStatus("3");
				payDocBean.setVerifierId(userId);
				payDocBean.setVerfyDate(cdate);
				payDocBean.setPayStatus("Y"); 
				payDocBean.setLastUpdateDate(cdate);
				payDocBean.setLastUpdatedBy(userId);
				exCheckAndPayDAO.updPayDocVoucher(payDocBean);
				
			}else{
				// 扩展使用
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
	}
}
