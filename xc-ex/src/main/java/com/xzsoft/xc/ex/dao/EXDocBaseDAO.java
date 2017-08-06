package com.xzsoft.xc.ex.dao;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.ex.modal.EXDocBean;
import com.xzsoft.xc.ex.modal.EXDocDtlBean;

/**
 * @ClassName: EXDocBaseDAO 
 * @Description: 费用报销单据基础信息数据层接口
 * @author linp
 * @date 2016年5月24日 下午4:29:05 
 *
 */
public interface EXDocBaseDAO {
	
	/**
	 * @Title: getLdSecCatInfo 
	 * @Description: 获取账簿下已启用的单据类型信息
	 * @param ledgerId
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<HashMap<String,String>> getLdSecCatInfo(String ledgerId) throws Exception ; 
	
	/**
	 * @Title: getLedgerCatInfo 
	 * @Description: 获取账簿级费用单据类型信息
	 * @param ledgerId
	 * @param docCat
	 * @return
	 * @throws Exception    设定文件
	 */
	public HashMap<String,String> getLedgerCatInfo(String ledgerId,String docCat) throws Exception ;
	
	/**
	 * @Title: getGlobalCatInfo 
	 * @Description: 获取全局费用单据类型信息
	 * @param docCat
	 * @return
	 * @throws Exception    设定文件
	 */
	public HashMap<String,String> getGlobalCatInfo(String docCat) throws Exception ;
	
	/**
	 * @Title: getDocPrintUrlByCat 
	 * @Description: 获取单据类型指定的打印表单信息
	 * @param docCat
	 * @return
	 * @throws Exception    设定文件
	 */
	public HashMap<String,String> getDocPrintUrlByCat(String docCat) throws Exception ;
	
	/**
	 * @Title: getDocDtls 
	 * @Description: 查询费用单据明细信息
	 * @param docId
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<EXDocDtlBean> getDocDtls(String docId) throws Exception ;
	
	/**
	 * @Title: getEXDocBean 
	 * @Description: 查询单据基础信息
	 * @param docId
	 * @return
	 * @throws Exception    设定文件
	 */
	public EXDocBean getEXDocBean(String docId) throws Exception ;
	
	/**
	 * @Title: getDocDtlByBgItemId 
	 * @Description: 按预算项目统计单据明细信息
	 * @param docId
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<EXDocDtlBean> getDocDtlByBgItemId(String docId) throws Exception ;
	
	/**
	 * @Title: saveDoc 
	 * @Description: 保存单据信息
	 * @param docBean
	 * @throws Exception    设定文件
	 */
	public void saveDoc(EXDocBean docBean) throws Exception ;
	
	/**
	 * @Title: updateDoc 
	 * @Description:更新单据信息
	 * @param docBean
	 * @throws Exception    设定文件
	 */
	public void updateDoc(EXDocBean docBean) throws Exception ;
	
	/**
	 * @Title: delDocById 
	 * @Description: 删除单据
	 * @param docId
	 * @throws Exception    设定文件
	 */
	public void delDocById(String docId) throws Exception ;
	
	/**
	 * @Title: batchDelDoc 
	 * @Description: 批量删除单据
	 * @param docIds
	 * @throws Exception    设定文件
	 */
	public void batchDelDoc(List<String> docIds) throws Exception ;
	/**
	 * 
	 * @methodName  getExItemTree
	 * @author      tangxl
	 * @date        2016年9月29日
	 * @describe    获取费用报销项目树
	 * @param 		map
	 * @return
	 * @throws 		Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public HashMap<String, List<HashMap<String, String>>> getExItemTree(HashMap<String, String> map) throws Exception;
	
	/**
	 * @Title: getEXDocBeanByApplyDocId 
	 * @Description: 根据费用申请单ID查询费用(差旅)报销单信息
	 * @param ledgerId
	 * @param applyDocId
	 * @return
	 * @throws Exception    设定文件
	 */
	public EXDocBean getEXDocBeanByApplyDocId(String ledgerId, String applyDocId) throws Exception ;
}
