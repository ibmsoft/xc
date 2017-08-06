package com.xzsoft.xc.ex.mapper;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.ex.modal.EXDocBean;
import com.xzsoft.xc.ex.modal.EXDocDtlBean;

/**
 * @ClassName: EXDocBaseMapper 
 * @Description: 费用报销单据基础信息Mapper
 * @author linp
 * @date 2016年5月24日 下午4:48:24 
 *
 */
public interface EXDocBaseMapper {
	
	/**
	 * @Title: getLdSecCatInfo 
	 * @Description: 获取账簿下已启用的单据类型信息
	 * @param ledgerId
	 * @return    设定文件
	 */
	public List<HashMap<String,String>> getLdSecCatInfo(String ledgerId) ;

	/**
	 * @Title: getLdCatInfo 
	 * @Description: 获取账簿级费用单据类型信息
	 * @param map
	 * @return    设定文件
	 */
	public HashMap<String,String> getLdCatInfo(HashMap<String,String> map) ;
	
	/**
	 * @Title: getGbCatInfo 
	 * @Description: 获取全局费用单据类型信息
	 * @param docCat
	 * @return    设定文件
	 */
	public HashMap<String,String> getGbCatInfo(String docCat) ;
	
	/**
	 * @Title: getPrintFormUrlByCat 
	 * @Description: 获取报销单据打印表单地址 
	 * @param docCat
	 * @return    设定文件
	 */
	public HashMap<String,String> getPrintFormUrlByCat(String docCat) ;
	
	/**
	 * @Title: getDocDtls 
	 * @Description: 查询单据明细信息
	 * @param docId
	 * @return    设定文件
	 */
	public List<EXDocDtlBean> getDocDtls(String docId) ;
	
	/**
	 * @Title: getEXDocBean 
	 * @Description: 查询单据基础信息
	 * @param docId
	 * @return    设定文件
	 */
	public EXDocBean getEXDocBean(String docId) ;
	
	/**
	 * @Title: getDocDtlByBgItemId 
	 * @Description: 按预算项目统计单据明细信息
	 * @param docId
	 * @return    设定文件
	 */
	public List<EXDocDtlBean> getDocDtlByBgItemId(HashMap<String,String> map) ;
	
	/**
	 * @Title: saveDoc 
	 * @Description: 保存单据信息
	 * @param bean    设定文件
	 */
	public void saveDoc(EXDocBean bean) ;
	
	/**
	 * @Title: saveDocDtl 
	 * @Description: 保存单据明细
	 * @param map    设定文件
	 */
	public void saveDocDtl(HashMap<String,Object> map) ;
	
	/**
	 * @Title: updateDoc 
	 * @Description: 更新单据信息
	 * @param bean    设定文件
	 */
	public void updateDoc(EXDocBean bean) ;
	
	/**
	 * @Title: updateDocDtl 
	 * @Description: 更新单据明细
	 * @param map    设定文件
	 */
	public void updateDocDtl(HashMap<String,Object> map) ;
	
	/**
	 * @Title: deDocDtls 
	 * @Description: 根据明细ID批量删除单据明细
	 * @param dtlIds    设定文件
	 */
	public void deDocDtls(List<String> dtlIds) ;

	/**
	 * @Title: delDocById 
	 * @Description: 删除单据 
	 * @param docId    设定文件
	 */
	public void delDocById(String docId) ;
	
	/**
	 * @Title: delDocDtlById 
	 * @Description: 删除单据明细
	 * @param docId    设定文件
	 */
	public void delDocDtlById(String docId) ;
	
	/**
	 * @Title: batchEXDoc 
	 * @Description: 批量删除单据 
	 * @param map    设定文件
	 */
	public void batchDelDoc(HashMap<String,Object> map) ;
	
	/**
	 * @Title: batchDelDocDtl 
	 * @Description: 批量删除单据 明细
	 * @param map    设定文件
	 */
	public void batchDelDocDtl(HashMap<String,Object> map) ;
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
	public List<HashMap<String, String>> getExItemTree(HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * @methodName  getArchyExItemTree
	 * @author      tangxl
	 * @date        2016年9月29日
	 * @describe    获取账簿费用项目体系所对应的费用项目<费末级>
	 * @param 		map
	 * @return
	 * @throws 		Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public List<HashMap<String, String>> getArchyExItemTree(HashMap<String, String> map) throws Exception;
	
	/**
	 * @Title: getEXDocBeanByApplyDocId 
	 * @Description: 根据费用申请单ID查询报销单信息 
	 * @param map
	 * @return
	 * @throws Exception    设定文件
	 */
	public EXDocBean getEXDocBeanByApplyDocId(HashMap<String,String> map) throws Exception ;
	
}
