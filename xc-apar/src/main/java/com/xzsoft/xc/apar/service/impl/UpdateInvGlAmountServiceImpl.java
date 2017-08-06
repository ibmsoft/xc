package com.xzsoft.xc.apar.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xzsoft.xc.apar.dao.ApArDao;
import com.xzsoft.xc.apar.dao.ApArTransDao;
import com.xzsoft.xc.apar.dao.UpdateInvGlAmountDao;
import com.xzsoft.xc.apar.modal.ApDocumentHBean;
import com.xzsoft.xc.apar.modal.ApInvTransBean;
import com.xzsoft.xc.apar.modal.ArDocumentHBean;
import com.xzsoft.xc.apar.modal.ArInvTransBean;
import com.xzsoft.xc.apar.service.UpdateInvGlAmountService;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xip.platform.util.XipUtil;
/**
 * 
  * @ClassName UpdateInvGlAmountServiceImpl
  * @Description 更新应收单和应收单中的相应金额
  * @author RenJianJian
  * @date 2016年9月29日 上午9:46:17
 */
@Service("updateInvGlAmountService")
public class UpdateInvGlAmountServiceImpl implements UpdateInvGlAmountService {
	public static final Logger log = Logger.getLogger(UpdateInvGlAmountServiceImpl.class);
	@Resource
	private ApArTransDao apArTransDao;
	@Resource
	private ApArDao apArDao;
	@Resource
	private UpdateInvGlAmountDao updateInvGlAmountDao;
	/*
	 * 
	  * <p>Title updateApInvGlAmount</p>
	  * <p>Description 通过应付单ID查询交易明细表中的金额数据，并更新应付单中相应的金额</p>
	  * @param apInvGlIdList
	  * @throws Exception
	  * @see com.xzsoft.xc.apar.service.UpdateInvGlAmountService#updateApInvGlAmount(java.util.List)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void updateApInvGlAmount(List<String> apInvGlIdList) throws Exception {
		try {
			List<ApDocumentHBean> apDocumentHList = new ArrayList<ApDocumentHBean>();
			for(int j = 0;j < apInvGlIdList.size();j++){
				double PAID_AMT = 0;//已付金额
				double CANCEL_AMT = 0;//核销金额
				double OCCUPY_AMT  = 0;//占用金额
				double NO_PAY_AMT = 0;//未付金额
				double ADJ_AMT = 0;//调整金额
				//通过应付单ID得到交易明细表中的数据
				List<ApInvTransBean> arInvTransList = apArTransDao.getApInvTransByGlId(apInvGlIdList.get(j));
				//通过应付单ID得到应付单信息
				ApDocumentHBean apInvGlHBean = apArDao.getApInvGlH(apInvGlIdList.get(j));
				for (int i = 0;i < arInvTransList.size();i++) {
					//通过来源表对数据进行分组
					if("XC_AP_PAY_H".equals(arInvTransList.get(i).getSOURCE_TAB())){//来源是付款单，对应的金额为已付金额
						if(arInvTransList.get(i).getREQ_AMT() != 0){
							BigDecimal OCCUPY_DR_AMT = new BigDecimal(arInvTransList.get(i).getREQ_AMT());
							BigDecimal OCCUPY_DR_AMTS = OCCUPY_DR_AMT.setScale(2,BigDecimal.ROUND_HALF_UP);
							OCCUPY_AMT = OCCUPY_AMT + OCCUPY_DR_AMTS.doubleValue();//占用金额等于付款申请单生成的交易明细中的申请金额
						}
						BigDecimal PAID_DR_AMT = new BigDecimal(arInvTransList.get(i).getDR_AMT());
						BigDecimal PAID_DR_AMTS = PAID_DR_AMT.setScale(2,BigDecimal.ROUND_HALF_UP);
						PAID_AMT = PAID_AMT + PAID_DR_AMTS.doubleValue();//已付金额等于付款单生成的交易明细中的借方金额
					}
					if("XC_AP_CANCEL_L".equals(arInvTransList.get(i).getSOURCE_TAB()) || "XC_AP_CANCEL_H".equals(arInvTransList.get(i).getSOURCE_TAB())){//来源是核销单，对应的金额为核销金额
						BigDecimal CANCEL_DR_AMT = new BigDecimal(arInvTransList.get(i).getDR_AMT());
						BigDecimal CANCEL_DR_AMTS = CANCEL_DR_AMT.setScale(2,BigDecimal.ROUND_HALF_UP);
						CANCEL_AMT = CANCEL_AMT + CANCEL_DR_AMTS.doubleValue();//核销金额等于核销单生成的交易明细中的借方金额
					}
					if("XC_AP_PAY_REQ_H".equals(arInvTransList.get(i).getSOURCE_TAB())){//来源是申请单，对应的金额为占用金额
						BigDecimal OCCUPY_DR_AMT = new BigDecimal(arInvTransList.get(i).getREQ_AMT());
						BigDecimal OCCUPY_DR_AMTS = OCCUPY_DR_AMT.setScale(2,BigDecimal.ROUND_HALF_UP);
						OCCUPY_AMT = OCCUPY_AMT + OCCUPY_DR_AMTS.doubleValue();//占用金额等于付款申请单生成的交易明细中的申请金额金额
					}
					if("XC_AP_INV_GL_ADJ".equals(arInvTransList.get(i).getSOURCE_TAB())){//来源是调整单，对应的金额为调整金额
						BigDecimal ADJ_CR_AMT = new BigDecimal(arInvTransList.get(i).getCR_AMT());
						BigDecimal ADJ_CR_AMTS = ADJ_CR_AMT.setScale(2,BigDecimal.ROUND_HALF_UP);
						ADJ_AMT = ADJ_AMT + ADJ_CR_AMTS.doubleValue();//调整金额金额等于调整单生成的交易明细中的贷方金额
					}
					BigDecimal CR_AMT = new BigDecimal(arInvTransList.get(i).getCR_AMT());
					BigDecimal CR_AMTS = CR_AMT.setScale(2,BigDecimal.ROUND_HALF_UP);
					BigDecimal DR_AMT = new BigDecimal(arInvTransList.get(i).getDR_AMT());
					BigDecimal DR_AMTS = DR_AMT.setScale(2,BigDecimal.ROUND_HALF_UP);
					NO_PAY_AMT = NO_PAY_AMT + (CR_AMTS.doubleValue() - DR_AMTS.doubleValue());//未付金额等于交易明细表中贷方合计减去借方合计
					
				}
				if("YFD".equals(apInvGlHBean.getAP_DOC_CAT_CODE()) && NO_PAY_AMT < 0)
					//throw new Exception("蓝字应付单：" + apInvGlHBean.getAP_INV_GL_H_CODE() + "未付金额小于0，不能进行其他操作，请重新选择！");
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_AP_INV_GL_L", null)+"【"+apInvGlHBean.getAP_INV_GL_H_CODE()+"】"+XipUtil.getMessage(XCUtil.getLang(), "XC_AP_INV_GL_L_AMOUNT", null));
				if("YFD-H".equals(apInvGlHBean.getAP_DOC_CAT_CODE()) && NO_PAY_AMT > 0)
					//throw new Exception("红字应付单：" + apInvGlHBean.getAP_INV_GL_H_CODE() + "未付金额大于0，不能进行其他操作，请重新选择！");
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_AP_INV_GL_H", null)+"【"+apInvGlHBean.getAP_INV_GL_H_CODE()+"】"+XipUtil.getMessage(XCUtil.getLang(), "XC_AP_INV_GL_H_AMOUNT", null));
				
				ApDocumentHBean apDocumentHBean = new ApDocumentHBean();
				apDocumentHBean.setAP_INV_GL_H_ID(apInvGlIdList.get(j));
				apDocumentHBean.setNO_PAY_AMT(NO_PAY_AMT);
				apDocumentHBean.setPAID_AMT(PAID_AMT);
				apDocumentHBean.setCANCEL_AMT(CANCEL_AMT);
				apDocumentHBean.setADJ_AMT(ADJ_AMT);
				apDocumentHBean.setOCCUPY_AMT(OCCUPY_AMT);
				apDocumentHList.add(apDocumentHBean);
			}
			if(apDocumentHList.size() > 0 && apDocumentHList != null)
				updateInvGlAmountDao.updateApInvGlAmount(apDocumentHList);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}

	}
	/*
	 * 
	  * <p>Title updateArInvGlAmount</p>
	  * <p>Description 通过应收单ID查询交易明细表中的金额数据，并更新应收单中相应的金额</p>
	  * @param arInvGlIdList
	  * @throws Exception
	  * @see com.xzsoft.xc.apar.service.UpdateInvGlAmountService#updateArInvGlAmount(java.util.List)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void updateArInvGlAmount(List<String> arInvGlIdList) throws Exception {
		try {
			List<ArDocumentHBean> arDocumentHList = new ArrayList<ArDocumentHBean>();
			for(int j = 0;j < arInvGlIdList.size();j++){
				double PAID_AMT = 0;//已收金额
				double CANCEL_AMT = 0;//核销金额
				double ADJ_AMT  = 0;//调整金额
				double NO_PAY_AMT = 0;//未收金额
				//通过应收单ID得到交易明细表中的数据
				List<ArInvTransBean> arInvTransList = apArTransDao.getArInvTransByGlId(arInvGlIdList.get(j));
				//通过应收单ID得到应收单信息
				ArDocumentHBean arInvGlHBean = apArDao.getArInvGlH(arInvGlIdList.get(j));
				for (int i = 0;i < arInvTransList.size();i++) {
					//通过来源表对数据进行分组
					if("XC_AR_PAY_H".equals(arInvTransList.get(i).getSOURCE_TAB())){//来源是收款单，对应的金额为已收金额
						BigDecimal PAID_CR_AMT = new BigDecimal(arInvTransList.get(i).getCR_AMT());
						BigDecimal PAID_CR_AMTS = PAID_CR_AMT.setScale(2,BigDecimal.ROUND_HALF_UP);
						PAID_AMT = PAID_AMT + PAID_CR_AMTS.doubleValue();//已收金额等于收款单生成的交易明细中的借方金额
					}
					if("XC_AR_CANCEL_L".equals(arInvTransList.get(i).getSOURCE_TAB()) || "XC_AR_CANCEL_H".equals(arInvTransList.get(i).getSOURCE_TAB())){//来源是核销单，对应的金额为核销金额
						BigDecimal CANCEL_CR_AMT = new BigDecimal(arInvTransList.get(i).getCR_AMT());
						BigDecimal CANCEL_CR_AMTS = CANCEL_CR_AMT.setScale(2,BigDecimal.ROUND_HALF_UP);
						CANCEL_AMT = CANCEL_AMT + CANCEL_CR_AMTS.doubleValue();//核销金额等于核销单生成的交易明细中的借方金额
					}
					if("XC_AR_INV_GL_ADJ".equals(arInvTransList.get(i).getSOURCE_TAB())){//来源是调整单，对应的金额为调整金额
						BigDecimal ADJ_DR_AMT = new BigDecimal(arInvTransList.get(i).getDR_AMT());
						BigDecimal ADJ_DR_AMTS = ADJ_DR_AMT.setScale(2,BigDecimal.ROUND_HALF_UP);
						ADJ_AMT = ADJ_AMT + ADJ_DR_AMTS.doubleValue();//调整金额金额等于调整单生成的交易明细中的贷方金额
					}
					BigDecimal CR_AMT = new BigDecimal(arInvTransList.get(i).getCR_AMT());
					BigDecimal CR_AMTS = CR_AMT.setScale(2,BigDecimal.ROUND_HALF_UP);
					BigDecimal DR_AMT = new BigDecimal(arInvTransList.get(i).getDR_AMT());
					BigDecimal DR_AMTS = DR_AMT.setScale(2,BigDecimal.ROUND_HALF_UP);
					NO_PAY_AMT = NO_PAY_AMT + (DR_AMTS.doubleValue() - CR_AMTS.doubleValue());//未收金额等于交易明细表中贷方合计减去借方合计
					
				}
				if("YSD".equals(arInvGlHBean.getAR_DOC_CAT_CODE()) && NO_PAY_AMT < 0)
					//throw new Exception("蓝字应收单：" + arInvGlHBean.getAR_INV_GL_H_CODE() + "未收金额小于0，不能进行其他操作，请重新选择！");
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_AR_INV_GL_L", null)+"【"+arInvGlHBean.getAR_INV_GL_H_CODE()+"】"+XipUtil.getMessage(XCUtil.getLang(), "XC_AR_INV_GL_L_AMOUNT", null));
				if("YSD-HZ".equals(arInvGlHBean.getAR_DOC_CAT_CODE()) && NO_PAY_AMT > 0)
					//throw new Exception("红字应收单：" + arInvGlHBean.getAR_INV_GL_H_CODE() + "未收金额大于0，不能进行其他操作，请重新选择！");
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_AR_INV_GL_H", null)+"【"+arInvGlHBean.getAR_INV_GL_H_CODE()+"】"+XipUtil.getMessage(XCUtil.getLang(), "XC_AR_INV_GL_H_AMOUNT", null));
				
				ArDocumentHBean arDocumentHBean = new ArDocumentHBean();
				arDocumentHBean.setAR_INV_GL_H_ID(arInvGlIdList.get(j));
				arDocumentHBean.setNO_PAY_AMT(NO_PAY_AMT);
				arDocumentHBean.setPAID_AMT(PAID_AMT);
				arDocumentHBean.setCANCEL_AMT(CANCEL_AMT);
				arDocumentHBean.setADJ_AMT(ADJ_AMT);
				arDocumentHList.add(arDocumentHBean);
			}
			if(arDocumentHList.size() > 0 && arDocumentHList != null)
				updateInvGlAmountDao.updateArInvGlAmount(arDocumentHList);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
}