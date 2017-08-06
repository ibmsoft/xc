package com.xzsoft.xc.ex.dao.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.ex.dao.EXDocBaseDAO;
import com.xzsoft.xc.ex.mapper.EXDocBaseMapper;
import com.xzsoft.xc.ex.modal.EXDocBean;
import com.xzsoft.xc.ex.modal.EXDocDtlBean;
import com.xzsoft.xc.ex.util.XCEXConstants;
import com.xzsoft.xip.platform.util.PlatformUtil;

/**
 * @ClassName: EXDocBaseDAOImpl 
 * @Description: 费用报销单据基础信息数据层接口实现类
 * @author linp
 * @date 2016年5月24日 下午4:29:37 
 *
 */
@Repository("exDocBaseDAO") 
public class EXDocBaseDAOImpl implements EXDocBaseDAO {
	@Resource
	private EXDocBaseMapper exDocBaseMapper ;

	/*
	 * (非 Javadoc) 
	 * <p>Title: getLdSecCatInfo</p> 
	 * <p>Description: 获取账簿下已启用的单据类型信息 </p> 
	 * @param ledgerId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.dao.EXDocBaseDAO#getLdSecCatInfo(java.lang.String)
	 */
	@Override
	public List<HashMap<String,String>> getLdSecCatInfo(String ledgerId) throws Exception {
		return exDocBaseMapper.getLdSecCatInfo(ledgerId) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getLedgerCatInfo</p> 
	 * <p>Description: 获取账簿级费用单据类型信息 </p> 
	 * @param ledgerId
	 * @param docCat
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.dao.EXDocBaseDAO#getLedgerCatInfo(java.lang.String, java.lang.String)
	 */
	@Override
	public HashMap<String, String> getLedgerCatInfo(String ledgerId,String docCat) throws Exception {
		HashMap<String,String>  catMap = null ;
				
		try {
			// 查询账簿级费用单据类型信息
			HashMap<String, String> map = new HashMap<String, String>() ;
			map.put("ledgerId", ledgerId) ;
			map.put("docCat", docCat) ;
			
			catMap = exDocBaseMapper.getLdCatInfo(map) ;
			
		} catch (Exception e) {
			throw e ;
		}
		
		return catMap;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: getGlobalCatInfo</p> 
	 * <p>Description: 获取全局费用单据类型信息 </p> 
	 * @param docCat
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.dao.EXDocBaseDAO#getGlobalCatInfo(java.lang.String)
	 */
	@Override
	public HashMap<String, String> getGlobalCatInfo(String docCat) throws Exception {
		return exDocBaseMapper.getGbCatInfo(docCat) ;
	}

	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getDocPrintUrlByCat</p> 
	 * <p>Description: 获取单据类型指定的打印表单信息 </p> 
	 * @param docCat
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.dao.EXDocBaseDAO#getDocPrintUrlByCat(java.lang.String)
	 */
	@Override
	public HashMap<String,String> getDocPrintUrlByCat(String docCat) throws Exception {
		HashMap<String,String> map = exDocBaseMapper.getPrintFormUrlByCat(docCat) ;
		return map ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getDocDtls</p> 
	 * <p>Description: 查询单据明细信息 </p> 
	 * @param docId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.dao.EXDocBaseDAO#getDocDtls(java.lang.String)
	 */
	@Override
	public List<EXDocDtlBean> getDocDtls(String docId) throws Exception {
		return exDocBaseMapper.getDocDtls(docId) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getEXDocBean</p> 
	 * <p>Description: 查询单据基础信息 </p> 
	 * @param docId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.dao.EXDocBaseDAO#getEXDocBean(java.lang.String)
	 */
	@Override
	public EXDocBean getEXDocBean(String docId) throws Exception {
		return exDocBaseMapper.getEXDocBean(docId) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getDocDtlByBgItemId</p> 
	 * <p>Description: 按预算项目统计单据明细信息 </p> 
	 * @param docId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.dao.EXDocBaseDAO#getDocDtlByBgItemId(java.lang.String)
	 */
	@Override
	public List<EXDocDtlBean> getDocDtlByBgItemId(String docId) throws Exception {
		HashMap<String,String> map = new HashMap<String,String>() ;
		map.put("docId", docId) ;
		map.put("dbType", PlatformUtil.getDbType()) ;
		return exDocBaseMapper.getDocDtlByBgItemId(map) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: saveDoc</p> 
	 * <p>Description: 保存单据信息 </p> 
	 * @param docBean
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.dao.EXDocBaseDAO#saveDoc(com.xzsoft.xc.ex.modal.EXDocBean)
	 */
	@Override
	public void saveDoc(EXDocBean docBean) throws Exception {
		try {
			// 保存主表信息
			exDocBaseMapper.saveDoc(docBean);
			
			// 保存明细表信息
			if(XCEXConstants.EX_DOC_EX03.equals(docBean.getDocCatCode())
					|| XCEXConstants.EX_DOC_EX04.equals(docBean.getDocCatCode())){
				
				// 定义参数
				HashMap<String,Object> map = new HashMap<String,Object>() ;
				map.put("dbType", PlatformUtil.getDbType()) ;
				map.put("list", docBean.getDocDtl()) ;
				
				// 保存单据明细行
				exDocBaseMapper.saveDocDtl(map);
			}
			
		} catch (Exception e) {
			throw e ;
		}
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: updateDoc</p> 
	 * <p>Description: 更新单据信息 </p> 
	 * @param docBean
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.dao.EXDocBaseDAO#updateDoc(com.xzsoft.xc.ex.modal.EXDocBean)
	 */
	@Override
	public void updateDoc(EXDocBean docBean) throws Exception {
		try {
			// 更新主表信息
			exDocBaseMapper.updateDoc(docBean);
			
			// 保存明细表信息
			if(XCEXConstants.EX_DOC_EX03.equals(docBean.getDocCatCode())
					|| XCEXConstants.EX_DOC_EX04.equals(docBean.getDocCatCode())){
				// 新增明细行
				if(docBean.getDocDtl() != null && docBean.getDocDtl().size()>0){
					HashMap<String,Object> addMap = new HashMap<String,Object>() ;
					addMap.put("dbType", PlatformUtil.getDbType()) ;
					addMap.put("list", docBean.getDocDtl()) ;
					exDocBaseMapper.saveDocDtl(addMap);
				}
				// 更新明细行
				if(docBean.getUpdLines() != null && docBean.getUpdLines().size()>0){
					HashMap<String,Object> updMap = new HashMap<String,Object>() ;
					updMap.put("dbType", PlatformUtil.getDbType()) ;
					updMap.put("list", docBean.getUpdLines()) ;
					exDocBaseMapper.updateDocDtl(updMap);
				}
				// 删除明细行
				if(docBean.getDocDtlIds() != null && docBean.getDocDtlIds().size()>0){
					exDocBaseMapper.deDocDtls(docBean.getDocDtlIds()) ;
				}				
			}
			
		} catch (Exception e) {
			throw e ;
		}
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: delDocById</p> 
	 * <p>Description: 删除单据 </p> 
	 * @param docId
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.dao.EXDocBaseDAO#delDocById(java.lang.String)
	 */
	@Override
	public void delDocById(String docId) throws Exception {
		try {
			// 删除单据明细表
			exDocBaseMapper.delDocDtlById(docId);
			
			// 删除单据主表
			exDocBaseMapper.delDocById(docId);
			
		} catch (Exception e) {
			throw e ;
		}
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: batchDelDoc</p> 
	 * <p>Description: 批量删除单据 </p> 
	 * @param docIds
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.dao.EXDocBaseDAO#delDocs(java.util.List)
	 */
	@Override
	public void batchDelDoc(List<String> docIds) throws Exception {
		try {
			// 定义参数
			HashMap<String,Object> map = new HashMap<String,Object>() ;
			map.put("dbType", PlatformUtil.getDbType()) ;
			map.put("list", docIds) ;
			
			// 删除单据明细表
			exDocBaseMapper.batchDelDocDtl(map);
			
			// 删除单据主表
			exDocBaseMapper.batchDelDoc(map);
			
		} catch (Exception e) {
			throw e ;
		}
	}

	/* (non-Javadoc)
	 * @name     方法名
	 * @author   tangxl
	 * @date     2016年9月29日
	 * @version  1.0
	 * @see com.xzsoft.xc.ex.dao.EXDocBaseDAO#getExItemTree(java.util.HashMap)
	 */
	@Override
	public HashMap<String, List<HashMap<String, String>>> getExItemTree(
			HashMap<String, String> map) throws Exception {
		HashMap<String, List<HashMap<String, String>>> result = new HashMap<String, List<HashMap<String,String>>>();
		result.put("archyExItem", exDocBaseMapper.getArchyExItemTree(map));
		result.put("selExItem", exDocBaseMapper.getExItemTree(map));
		return result;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getEXDocBeanByApplyDocId</p> 
	 * <p>Description: 根据费用申请单ID查询费用(差旅)报销单信息 </p> 
	 * @param ledgerId
	 * @param applyDocId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.dao.EXDocBaseDAO#isRelationBizDoc(java.lang.String)
	 */
	@Override
	public EXDocBean getEXDocBeanByApplyDocId(String ledgerId, String applyDocId) throws Exception {
		
		HashMap<String,String> map = new HashMap<String,String>() ;
		map.put("ledgerId", ledgerId) ;
		map.put("applyDocId", applyDocId) ;
		
		EXDocBean docBean = exDocBaseMapper.getEXDocBeanByApplyDocId(map) ;
		
		return docBean ;
	}
}
