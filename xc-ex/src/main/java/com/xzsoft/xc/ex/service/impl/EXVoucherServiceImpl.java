package com.xzsoft.xc.ex.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xzsoft.xc.ex.dao.EXVoucherDAO;
import com.xzsoft.xc.ex.dao.XCEXCommonDAO;
import com.xzsoft.xc.ex.modal.CheckAndPayDocDTO;
import com.xzsoft.xc.ex.modal.EXDocBean;
import com.xzsoft.xc.ex.service.EXVoucher4DocService;
import com.xzsoft.xc.ex.service.EXVoucherService;
import com.xzsoft.xc.ex.util.XCEXConstants;
import com.xzsoft.xc.gl.common.impl.VoucherHandlerCommonService;
import com.xzsoft.xc.gl.dao.XCGLCommonDAO;
import com.xzsoft.xc.gl.modal.VHead;
import com.xzsoft.xc.gl.modal.VSource;
import com.xzsoft.xc.gl.service.VoucherHandlerService;
import com.xzsoft.xc.util.common.XCDateUtil;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xc.util.modal.Ledger;
import com.xzsoft.xip.framework.util.AppContext;
import com.xzsoft.xip.platform.dao.RedisDao;
import com.xzsoft.xip.platform.util.XipUtil;

/**
 * @ClassName: EXVoucherServiceImpl 
 * @Description: 单据复核、单据支付的凭证处理接口实现类
 * @author linp
 * @date 2016年3月10日 下午4:55:19 
 *
 */
@Service("exVoucherService")
public class EXVoucherServiceImpl implements EXVoucherService {
	//日志记录器
	private static final Logger log = Logger.getLogger(EXVoucherServiceImpl.class.getName()) ;
	@Resource
	private EXVoucherDAO exVoucherDAO ;
	@Resource
	private RedisDao redisDaoImpl ;
	@Resource
	private XCGLCommonDAO xcglCommonDAO ;
	@Resource
	private XCEXCommonDAO xcexCommonDAO ;
	@Resource
	private VoucherHandlerService voucherHandlerService ;
	@Resource
	private VoucherHandlerCommonService voucherHandlerCommonService ;
	

	/*
	 * (非 Javadoc) 
	 * <p>Title: newVoucher</p> 
	 * <p>Description: 单据复核生成凭证处理 </p> 
	 * @param accDate   ：  凭证制单日期
	 * @param payMethod	: 支付方式：1-立即支付，2-挂账支付
	 * @param payType	：  付款方式
	 * @param bankId	: 付款账号
	 * @param caId		: 现金流量项目ID
	 * @param exDocId	：  报销单
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.service.EXVoucherService#newVoucher(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public String newVoucher(CheckAndPayDocDTO dto) throws Exception {
		String jsonStr = null ;
		try {
			// 获取参数信息 
			String accDate = XCDateUtil.date2Str(dto.getAccDate()) ; // 凭证制单日期
			String payMethod = dto.getPayMethod();			// 支付方式：1-立即支付，2-挂账支付
			String payType = dto.getPayType();				// 付款方式
			String bankId =  dto.getBankId() ; 				// 付款账号
			String caId = dto.getCashId();					// 现金流量项目ID
			String exDocId = dto.getExDocId() ;				// 报销单ID
			float cancelAmt = dto.getCancelAmt() ;
			
			// 查询业务单据信息
			EXDocBean docBean = xcexCommonDAO.getEXDoc(exDocId,true) ;
			
			// 单据分类:EX02-借款单,EX03-差旅报销单,EX04-费用报销单,EX05-还款单
			String docCat = docBean.getDocCatCode() ;
			
			// 支付方式校验
			if(XCEXConstants.EX_DOC_EX02.equals(docCat) || XCEXConstants.EX_DOC_EX05.equals(docCat)){ // 借款单或还款单
				// 判断支付是否为立即支付
				if(!payMethod.equals(XCEXConstants.PAY_METHOD_ONCE)){
					//throw new Exception("借款单/还款单的支付方式只能是立即支付方式,请确认!") ;
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_newVoucher_EXCEPTION_01", null));
				}
				// 如果是还款单,则重新设置还款金额
				if(XCEXConstants.EX_DOC_EX05.equals(docCat)){
					docBean.setAmount(dto.getPayAmt());
				}
			}else if(XCEXConstants.EX_DOC_EX03.equals(docCat) || XCEXConstants.EX_DOC_EX04.equals(docCat)){ // 差旅报销单或费用报销单
				// 差旅报销单
				Ledger ledger = xcglCommonDAO.getLedger(docBean.getLedgerId()) ; 
				String isCrPay = ledger.getIsCrPay() ;
				if(!"Y".equals(isCrPay) && "2".equals(payMethod)){
					//throw new Exception("账簿【"+ledger.getLedgerName()+"】不允许挂账支付!") ;
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_LEDGER", null)+"【"+ledger.getLedgerName()+"】"+XipUtil.getMessage(XCUtil.getLang(), "XC_EX_newVoucher_EXCEPTION_02", null));
				}
			}else{
			}
			
			// 查询账簿信息
			Ledger ledger = xcglCommonDAO.getLedger(docBean.getLedgerId()) ;
			
			// 查询内置的报销凭证来源信息
			VSource vs = xcglCommonDAO.getVSource(XCEXConstants.SRC_CODE_EX) ;
			if(vs == null){
				//throw new Exception("系统未初始化内置的凭证来源") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_newVoucher_EXCEPTION_03", null));
			}
			
			// 处理费用单据信息
			List<EXDocBean> docBeans = new ArrayList<EXDocBean>() ;
			docBeans.add(docBean) ;
			
			// 辅助参数
			JSONObject paramJo = new JSONObject() ;
			paramJo.put("accDate", accDate) ;
			paramJo.put("payMethod", payMethod==null ? "" : payMethod) ;
			paramJo.put("payType", payType==null ? "" : payType) ;
			paramJo.put("bankId", bankId==null ? "" : bankId) ;
			paramJo.put("caId", caId==null ? "" : caId) ;
			paramJo.put("cancelAmt", cancelAmt) ;
			String paramJson = paramJo.toString() ;
			
			// 组合凭证信息
			VHead head = new VHead() ;
			if(XCEXConstants.EX_DOC_EX02.equals(docCat)){
				// 借款单凭证
				EXVoucher4DocService borrowDocService = (EXVoucher4DocService) AppContext.getApplicationContext().getBean("borrowDocService") ;
				head = borrowDocService.mergeVoucher(docBeans, ledger, vs, paramJson) ;
				
			}else if(XCEXConstants.EX_DOC_EX05.equals(docCat)){
				// 还款单凭证
				EXVoucher4DocService repaymentDocService = (EXVoucher4DocService) AppContext.getApplicationContext().getBean("repaymentDocService") ;
				head = repaymentDocService.mergeVoucher(docBeans, ledger, vs, paramJson) ;
				
			}else if(XCEXConstants.EX_DOC_EX03.equals(docCat) || XCEXConstants.EX_DOC_EX04.equals(docCat)){
				// 费用报销单和差旅报销单凭证
				EXVoucher4DocService costDocService = (EXVoucher4DocService) AppContext.getApplicationContext().getBean("costDocService") ;
				head = costDocService.mergeVoucher(docBeans, ledger, vs, paramJson) ;
				
			}else{
				// 其他单据
			}
			
			// 将凭证信息转换成JSON串
			String jsonObject = voucherHandlerCommonService.headBean2JsonStr(head, null) ;
			
			// 保存凭证信息
			jsonStr = voucherHandlerService.doSaveVoucher(jsonObject) ;
		
		} catch (Exception e) {
			log.error(e.getMessage());
			
			JSONObject jo = new JSONObject() ;
			jo.put("flag", "1") ;
			//jo.put("msg", "凭证保存失败："+e.getMessage()) ;
			jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_EX_VOUCHER_SAVE_FAILED", null)+e.getMessage()) ;
			jo.put("headId", "") ;
			jsonStr = jo.toString() ;
		}
		
		return jsonStr ;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: newPayVoucher</p> 
	 * <p>Description: 单据支付生成凭证处理 </p> 
	 * @param accDate	: 凭证制单日期
	 * @param summary	: 摘要(付款说明)
	 * @param exPayId	：  支付单号
	 * @param payType	：  付款方式
	 * @param bankId	: 付款账号
	 * @param caId		: 现金流量项目ID
	 * @param headId	: 凭证头ID
	 * @param exDocIds  : 报销单列表
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.service.EXVoucherService#newVoucher(java.lang.String, java.lang.String, java.lang.String, java.util.List)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public String newPayVoucher(CheckAndPayDocDTO dto) throws Exception {
		String jsonStr = null ;
		
		try {
			// 获取参数信息
			String accDate = XCDateUtil.date2Str(dto.getAccDate()) ;		// 凭证制单日期
			String summary = dto.getExPayDesc() ;				// 摘要(付款说明)
			String exPayId = dto.getPayId() ;					// 支付单号
			String payType = dto.getPayType();					// 付款方式
			String bankId = dto.getBankId() ;					// 付款账号
			String caId = dto.getCashId() ;						// 现金流量项目ID
			String headId = dto.getHeadId() ;					// 凭证头ID[可选]
			List<String> exDocIds = dto.getExDocIds() ;			// 报销单列表
			
			// 查询报销单信息
			List<EXDocBean> docBeans = xcexCommonDAO.getEXDocs(exDocIds,true) ;
			
			// 查询账簿信息
			Ledger ledger = xcglCommonDAO.getLedger(dto.getLedgerId()) ;
			
			// 查询内置的付款凭证来源信息
			VSource vs = xcglCommonDAO.getVSource(XCEXConstants.SRC_CODE_PAY) ;
			if(vs == null){
				//throw new Exception("系统未初始化内置的凭证来源") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_newVoucher_EXCEPTION_03", null));
			}
			
			// 辅助参数
			JSONObject paramJo = new JSONObject() ;
			paramJo.put("accDate", accDate) ;
			paramJo.put("summary", summary==null ? "" : summary) ;
			paramJo.put("exPayId", exPayId==null ? "" : exPayId) ;
			paramJo.put("payType", payType==null ? "" : payType) ;
			paramJo.put("bankId", bankId==null ? "" : bankId) ;
			paramJo.put("caId", caId==null ? "" : caId) ;
			String paramJson = paramJo.toString() ;
			
			// 生成凭证信息
			EXVoucher4DocService paymentDocService = (EXVoucher4DocService) AppContext.getApplicationContext().getBean("paymentDocService") ;
			VHead head = paymentDocService.mergeVoucher(docBeans, ledger, vs, paramJson) ;
			
			// 将凭证信息转换成JSON串
			String jsonObject = voucherHandlerCommonService.headBean2JsonStr(head, null) ;
			
			// 删除并保存凭证信息
			EXVoucherService service = (EXVoucherService) AppContext.getApplicationContext().getBean("exVoucherService") ;
			jsonStr = service.delAndSaveVoucher(headId, jsonObject) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			
			JSONObject jo = new JSONObject() ;
			jo.put("flag", "1") ;
			//jo.put("msg", "生成凭证失败>>"+e.getMessage()) ;
			jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_EX_VOUCHER_SAVE_FAILED", null)+e.getMessage()) ;
			jo.put("headId", "") ;
			jsonStr = jo.toString() ;
		}
		
		return jsonStr ;
	}

	/*
	 * (non-Javadoc)
	 * @see com.xzsoft.xc.ex.service.EXVoucherService#delAndSaveVoucher(java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public String delAndSaveVoucher(String headId ,String jsonObject) throws Exception {
		String jsonStr = null ;
		try {
			// 删除凭证
			if(headId != null && !"".equals(headId)){
				JSONArray ja = new JSONArray() ;
				JSONObject vJo = new JSONObject() ;
				vJo.put("V_HEAD_ID", headId) ;
				ja.put(vJo) ;
				voucherHandlerService.doDelVouchers(ja.toString()) ;
			}
			
			// 保存凭证信息
			jsonStr = voucherHandlerService.doSaveVoucher(jsonObject) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
		
		return jsonStr ; 
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: cancelVoucher</p> 
	 * <p>Description: 取消复核 </p> 
	 * <p>
	 * 	取消复核相当于取消审核、撤回提交, 使凭证为草稿状态
	 *  此时凭证状态为已取消
	 * </p>
	 * @param headIds
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.service.EXVoucherService#cancelVoucher(java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public String cancelVoucher(List<String> headIds) throws Exception {
		JSONObject returnJo = new JSONObject() ;
		
		try {
			if(headIds != null && headIds.size()>0){
				JSONArray ja = new JSONArray() ;
				for(String headId : headIds){
					JSONObject jo = new JSONObject() ;
					jo.put("V_HEAD_ID", headId) ;
					ja.put(jo) ;
				}
				
				// 取消凭证审核
				String jsonStr = voucherHandlerService.doUncheckVouchers(ja.toString()) ;
				JSONObject jo = new JSONObject(jsonStr) ;
				
				// 取消凭证审核成功之后, 再执行取消凭证提交
				if("0".equals(jo.getString("flag"))){
					voucherHandlerService.doCancelSubmitVouchers(ja.toString()) ;
				}
			}
			
			// 提示信息
			returnJo.put("flag", "0") ;
			//returnJo.put("msg", "取消复核成功") ;
			throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC_CANCEL_FIN_SUCCESS", null));
			
		} catch (Exception e) {
			log.error(e.getMessage());
			returnJo.put("flag", "1") ;
			//returnJo.put("msg", "取消复核失败:"+e.getMessage()) ;
			returnJo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC_CANCEL_FIN_FAILED", null)+e.getMessage()) ;
		}
		
		return  returnJo.toString() ;
	}

}
