package com.xzsoft.xc.ex.dao.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.ex.dao.EXCheckAndPayDAO;
import com.xzsoft.xc.ex.mapper.EXCheckAndPayMapper;
import com.xzsoft.xc.ex.modal.EXDocBean;
import com.xzsoft.xc.ex.modal.PayDocBean;
import com.xzsoft.xc.ex.modal.PayDocDtlBean;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xip.platform.util.XipUtil;

/**
 * @ClassName: EXCheckAndPayDAOImpl 
 * @Description: 单据复核、单据支付的数据处理层接口实现类
 * @author linp
 * @date 2016年3月25日 下午2:38:54 
 *
 */
@Repository("exCheckAndPayDAO") 
public class EXCheckAndPayDAOImpl implements EXCheckAndPayDAO {
	@Resource
	private EXCheckAndPayMapper exCheckAndPayMapper ;

	/*
	 * (非 Javadoc) 
	 * <p>Title: dePayDocs</p> 
	 * <p>Description: 根据付款单ID, 批量删除付款单 </p> 
	 * @param payIds
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.dao.EXCheckAndPayDAO#dePayDocs(java.util.List)
	 */
	@Override
	public void dePayDocs(List<String> payIds) throws Exception {
		try {
			if(payIds != null && payIds.size() >0){
				// 批量删除付款单明细
				exCheckAndPayMapper.batchDelPayDocDtl(payIds);
				
				// 批量删除付款单信息
				exCheckAndPayMapper.batchDelPayDoc(payIds);
			}
		} catch (Exception e) {
			//throw new Exception("批量删除付款单失败:"+e.getMessage()) ;
			throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC_DELETE_FAILED", null)+e.getMessage());
		}
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: createPayDoc</p> 
	 * <p>Description: 创建付款单 </p> 
	 * @param payDocBean
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.dao.EXCheckAndPayDAO#createPayDoc(com.xzsoft.xc.ex.modal.PayDocBean)
	 */
	@Override
	public void createPayDoc(PayDocBean payDocBean) throws Exception {
		try {
			// 生成付款单头信息
			exCheckAndPayMapper.savePayDoc(payDocBean);
			
			// 生成付款单明细信息
			List<PayDocDtlBean> payDocDtlBeans = payDocBean.getPayDtl() ;
			for(PayDocDtlBean payDocDtlBean : payDocDtlBeans){
				exCheckAndPayMapper.savePayDocDtl(payDocDtlBean);
			}
			
		} catch (Exception e) {
			//throw new Exception("创建付款单失败:"+e.getMessage()) ;
			throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC_SAVE_FAILED", null)+e.getMessage());
		}
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: updatePayDoc</p> 
	 * <p>Description: 修改付款单信息 </p> 
	 * @param payDocBean
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.dao.EXCheckAndPayDAO#updatePayDoc(com.xzsoft.xc.ex.modal.PayDocBean)
	 */
	@Override
	public void updatePayDoc(PayDocBean payDocBean) throws Exception {
		try {
			// 修改付款单信息
			exCheckAndPayMapper.updPayDoc(payDocBean);
			
			// 删除付款单明细行
			exCheckAndPayMapper.delPayDocs(payDocBean.getDelPayDtlIds());
			
			// 新增付款单明细行
			List<PayDocDtlBean> payDocDtlBeans = payDocBean.getAddPayDtlBeans() ;
			if(payDocDtlBeans != null && payDocDtlBeans.size()>0){
				for(PayDocDtlBean payDocDtlBean : payDocDtlBeans){
					exCheckAndPayMapper.savePayDocDtl(payDocDtlBean);
				}
			}
			
		} catch (Exception e) {
			throw e ;
		}
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getRuleCode</p> 
	 * <p>Description: 取得单据的编码规则 </p> 
	 * @param docCat
	 * @param ledgerId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.dao.EXCheckAndPayDAO#getRuleCode(java.lang.String, java.lang.String)
	 */
	@Override
	public String getRuleCode(String docCat,String ledgerId) throws Exception {
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("docCat", docCat) ;
		map.put("ledgerId", ledgerId) ;
		
		return exCheckAndPayMapper.getRuleCode(map) ; 
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: updEXDoc</p> 
	 * <p>Description: 更新单张报销单与凭证和支付单的对应关系 </p> 
	 * @param docBean
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.dao.EXCheckAndPayDAO#updEXDoc(com.xzsoft.xc.ex.modal.EXDocBean)
	 */
	@Override
	public void updEXDoc(EXDocBean docBean) throws Exception {
		try {
			exCheckAndPayMapper.updEXDoc(docBean);
			
		} catch (Exception e) {
			throw e ;
		}
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: batchUpdEXDocs</p> 
	 * <p>Description: 批量更新报销单与支付单的对应关系 </p> 
	 * @param exDocIds
	 * @param docBean
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.dao.EXCheckAndPayDAO#batchUpdEXDocs(java.util.List, java.lang.String)
	 */
	@Override
	public void batchUpdEXDocs(List<String> exDocIds, EXDocBean docBean) throws Exception {
		try {
			HashMap<String,Object> map = new HashMap<String,Object>() ;
			map.put("list", exDocIds) ;
			map.put("bean", docBean) ;
			map.put("updMode", docBean.getUpdateMode()) ;
			
			exCheckAndPayMapper.batchUpdEXDocs(map);
			
		} catch (Exception e) {
			throw e ;
		}
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: batchClearEXDocs</p> 
	 * <p>Description:  批量清除费用报销单与凭证和支付的对应关系 </p> 
	 * @param docIds
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.dao.EXCheckAndPayDAO#updEXDocs(java.util.List)
	 */
	@Override
	public void batchClearEXDocs(List<String> docIds) throws Exception {
		try {
			// 定义参数
			HashMap<String,Object> map = new HashMap<String,Object>() ;
			map.put("list", docIds) ;
			
			// 执行批量清除
			exCheckAndPayMapper.batchClearEXDocs(map);
			
		} catch (Exception e) {
			throw e ;
		}
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: updEXDocVoucher</p> 
	 * <p>Description: 更新报销单凭证审核信息 </p> 
	 * @param docBean
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.dao.EXCheckAndPayDAO#updEXDocVoucher(com.xzsoft.xc.ex.modal.EXDocBean)
	 */
	@Override
	public void updEXDocVoucher(EXDocBean docBean) throws Exception {
		try {
			// 执行更新
			exCheckAndPayMapper.updEXDocVoucher(docBean);
			
		} catch (Exception e) {
			throw e ;
		}
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: updPayDocVoucher</p> 
	 * <p>Description: 更新付款单凭证审核信息</p> 
	 * @param payDocBean
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.dao.EXCheckAndPayDAO#updPayDocVoucher(com.xzsoft.xc.ex.modal.PayDocBean)
	 */
	@Override
	public void updPayDocVoucher(PayDocBean payDocBean) throws Exception {
		try {
			// 执行更新
			exCheckAndPayMapper.updPayDocVoucher(payDocBean);
			
		} catch (Exception e) {
			throw e ;
		}
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: upEXDocAuditInfo</p> 
	 * <p>Description: 修改业务单据审批信息 </p> 
	 * @param bean
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.dao.EXCheckAndPayDAO#upEXDocAuditInfo(com.xzsoft.xc.ex.modal.EXDocBean)
	 */
	public void upEXDocAuditInfo(EXDocBean bean) throws Exception {
		try {
			exCheckAndPayMapper.upEXDocAuditInfo(bean);
		} catch (Exception e) {
			throw e ;
		}
	}
}
