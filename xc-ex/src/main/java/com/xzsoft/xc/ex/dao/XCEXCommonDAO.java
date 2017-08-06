package com.xzsoft.xc.ex.dao;

import java.util.List;

import com.xzsoft.xc.ex.modal.ACCDocBean;
import com.xzsoft.xc.ex.modal.EXDocBean;
import com.xzsoft.xc.ex.modal.PayDocBean;

/**
 * @ClassName: XCEXCommonDAO 
 * @Description: 费用报销系统通用查询
 * @author linp
 * @date 2016年3月17日 上午10:16:48 
 *
 */
public interface XCEXCommonDAO {

	/**
	 * @Title: getEXDoc 
	 * @Description: 报销单查询
	 * @param docId
	 * @param isContainDtl
	 * @return
	 * @throws Exception    设定文件
	 */
	public EXDocBean getEXDoc(String docId,boolean isContainDtl) throws Exception ;
	
	/**
	 * @Title: getEXDocs 
	 * @Description: 批量查询报销单
	 * @param docIds
	 * @param isContainDtl
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<EXDocBean> getEXDocs(List<String> docIds,boolean isContainDtl) throws Exception ;
	
	/**
	 * @Title: getPayDoc 
	 * @Description: 支付单查询
	 * @param docId
	 * @param isContainDtl
	 * @return
	 * @throws Exception    设定文件
	 */
	public PayDocBean getPayDoc(String docId,boolean isContainDtl) throws Exception ;
	
	/**
	 * @Title: getPayDocs 
	 * @Description: 批量查询支付单
	 * @param docIds
	 * @param isContainDtl
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<PayDocBean> getPayDocs(List<String> docIds,boolean isContainDtl) throws Exception ;
	/**
	 * @Title: getAccDoc 
	 * @Description: 费用报销单查询
	 * @param docId
	 * @return
	 * @throws Exception
	 */
	public ACCDocBean getAccDoc(String docId) throws Exception;
	/**
	 * @Title: getEXDocV 
	 * @Description: 查询单张报销单据信息
	 * @param docId
	 * @return    设定文件
	 */
	public EXDocBean getExDocV(String docId);
	
	/**
	 * @Title: getCtrlDimByBgItem 
	 * @Description: 查询预算项目的控制维度
	 * @param ledgerId
	 * @param bgItemId
	 * @return
	 * @throws Exception    设定文件
	 */
	public String getCtrlDimByBgItem(String ledgerId,String bgItemId)throws Exception ;
	
	/**
	 * @Title: getCtrlDimByProject 
	 * @Description: 查询PA项目的控制维度
	 * @param ledgerId
	 * @param projectId
	 * @return
	 * @throws Exception    设定文件
	 */
	public String getCtrlDimByProject(String ledgerId,String projectId)throws Exception ;
	
}
