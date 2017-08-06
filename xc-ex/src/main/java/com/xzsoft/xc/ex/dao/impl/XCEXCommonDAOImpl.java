package com.xzsoft.xc.ex.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.xzsoft.xc.ex.dao.XCEXCommonDAO;
import com.xzsoft.xc.ex.mapper.XCEXCommonMapper;
import com.xzsoft.xc.ex.modal.ACCDocBean;
import com.xzsoft.xc.ex.modal.EXDocBean;
import com.xzsoft.xc.ex.modal.EXDocDtlBean;
import com.xzsoft.xc.ex.modal.PayDocBean;
import com.xzsoft.xc.ex.modal.PayDocDtlBean;

/**
 * @ClassName: XCEXCommonDAOImpl 
 * @Description: 费用报销系统通用查询
 * @author linp
 * @date 2016年3月17日 上午10:17:07 
 *
 */
@Repository("xcexCommonDAO")
public class XCEXCommonDAOImpl implements XCEXCommonDAO {
	// 日记记录器
	private static final Logger log = Logger.getLogger(XCEXCommonDAOImpl.class.getName()) ;
	@Resource
	private  XCEXCommonMapper xcexCommonMapper ;

	/*
	 * (非 Javadoc) 
	 * <p>Title: getEXDoc</p> 
	 * <p>Description: 报销单查询 </p> 
	 * @param docId
	 * @param isContainDtl
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.dao.XCEXCommonDAO#getEXDoc(java.lang.String, boolean)
	 */
	@Override
	public EXDocBean getEXDoc(String docId,boolean isContainDtl) throws Exception {
		EXDocBean docBean = new EXDocBean() ;
		
		try {
			// 查询基础信息
			docBean = xcexCommonMapper.getEXDoc(docId) ;
			
			// 查询单据明细信息
			if(isContainDtl){
				if(docBean != null){
					List<EXDocDtlBean> docDtl = xcexCommonMapper.getEXDocDtls(docId) ;
					docBean.setDocDtl(docDtl);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
		
		return docBean ;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: getEXDocs</p> 
	 * <p>Description: 报销单批量查询 </p> 
	 * @param docIds
	 * @param isContainDtl
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.dao.XCEXCommonDAO#getEXDocs(java.util.List, boolean)
	 */
	@Override
	public List<EXDocBean> getEXDocs(List<String> docIds,boolean isContainDtl) throws Exception {
		List<EXDocBean> list = new ArrayList<EXDocBean>();
		try {
			for(int i=0;i<docIds.size();i++){
				String docId = docIds.get(i) ;
				
				EXDocBean docBean = this.getEXDoc(docId, isContainDtl) ;
				
				list.add(i, docBean);	
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
		
		return list; 
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: getPayDoc</p> 
	 * <p>Description: 支付单查询  </p> 
	 * @param docId
	 * @param isContainDtl
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.dao.XCEXCommonDAO#getPayDoc(java.lang.String, boolean)
	 */
	@Override
	public PayDocBean getPayDoc(String docId,boolean isContainDtl) throws Exception {
		PayDocBean docBean = new PayDocBean() ;
		
		try {
			// 查询基础信息
			docBean = xcexCommonMapper.getPayDoc(docId) ;
			
			// 查询单据明细信息
			if(isContainDtl){
				if(docBean != null){
					List<PayDocDtlBean> payDtl = xcexCommonMapper.getPayDocDtls(docId) ;
					docBean.setPayDtl(payDtl);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}		
		
		return docBean;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getPayDocs</p> 
	 * <p>Description: 批量查询支付单 </p> 
	 * @param docIds
	 * @param isContainDtl
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.dao.XCEXCommonDAO#getPayDocs(java.util.List, boolean)
	 */
	@Override
	public List<PayDocBean> getPayDocs(List<String> docIds,boolean isContainDtl) throws Exception {
		List<PayDocBean> list = new ArrayList<PayDocBean>();
		try {
			for(int i=0;i<docIds.size();i++){
				String docId = docIds.get(i) ;
				
				PayDocBean docBean = this.getPayDoc(docId, isContainDtl) ;
				list.add(docBean) ;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
		
		return list; 
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: getAccDoc</p> 
	 * <p>Description: 费用报销单查询 </p> 
	 * @param docId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.dao.XCEXCommonDAO#getAccDoc(java.lang.String)
	 */
	@Override
	public ACCDocBean getAccDoc(String docId) throws Exception {
		ACCDocBean doc = new ACCDocBean();
		try {
			//查询报销单基础信息
			doc = xcexCommonMapper.getAccDoc(docId);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return doc;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getExDocV</p> 
	 * <p>Description: 查询单张报销单据信息 </p> 
	 * @param docId
	 * @return 
	 * @see com.xzsoft.xc.ex.dao.XCEXCommonDAO#getExDocV(java.lang.String)
	 */
	@Override
	public EXDocBean getExDocV(String docId) {
		EXDocBean exBean = xcexCommonMapper.getExDocV(docId);
		return exBean;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getCtrlDimByBgItem</p> 
	 * <p>Description: 查询预算项目的控制维度 </p> 
	 * <p>控制维度：1-预算项目，2-预算项目+成本中心</p>
	 * @param ledgerId
	 * @param bgItemId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.dao.XCEXCommonDAO#getCtrlDimByBgItem(java.lang.String, java.lang.String)
	 */
	@Override
	public String getCtrlDimByBgItem(String ledgerId,String bgItemId)throws Exception {
		HashMap<String,String> map = new HashMap<String,String>() ;
		map.put("ledgerId", ledgerId) ;
		map.put("bgItemId", bgItemId) ;
		
		return xcexCommonMapper.getBgCtrlDim4BgItem(map) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getCtrlDimByProject</p> 
	 * <p>Description: 查询PA项目的控制维度 </p> 
	 * <p>控制维度：1-项目，2-项目+预算项目，3-项目+预算项目+成本中心</p>
	 * @param ledgerId
	 * @param projectId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.dao.XCEXCommonDAO#getCtrlDimByProject(java.lang.String, java.lang.String)
	 */
	@Override
	public String getCtrlDimByProject(String ledgerId,String projectId)throws Exception {
		HashMap<String,String> map = new HashMap<String,String>() ;
		map.put("ledgerId", ledgerId) ;
		map.put("projectId", projectId) ;
		
		return xcexCommonMapper.getBgCtrlDim4Prj(map) ;
	}
}
