package com.xzsoft.xc.ex.mapper;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.ex.modal.ACCDocBean;
import com.xzsoft.xc.ex.modal.EXDocBean;
import com.xzsoft.xc.ex.modal.EXDocDtlBean;
import com.xzsoft.xc.ex.modal.PayDocBean;
import com.xzsoft.xc.ex.modal.PayDocDtlBean;

/**
 * @ClassName: XCEXDocMapper 
 * @Description: 费用报销系统通用查询处理
 * @author linp
 * @date 2016年3月17日 上午10:19:31 
 *
 */
public interface XCEXCommonMapper {
	
	/**
	 * @Title: getEXDoc 
	 * @Description: 查询单张报销单据信息
	 * @param docId
	 * @return    设定文件
	 */
	public EXDocBean getEXDoc(String docId) ;
	
	/**
	 * @Title: getEXDocV 
	 * @Description: 查询单张报销单据信息(审批查看用)
	 * @param docId
	 * @return    设定文件
	 */
	public EXDocBean getExDocV(String docId);
	
	/**
	 * @Title: getEXDocs 
	 * @Description: 批量查询报销单据信息
	 * @param docIds
	 * @return    设定文件
	 */
	public List<EXDocBean> getEXDocs(List<String> docIds) ;
	
	/**
	 * @Title: getEXDocDtls 
	 * @Description: 查询单据明细信息
	 * @param docId
	 * @return    设定文件
	 */
	public List<EXDocDtlBean> getEXDocDtls(String docId) ;
	
	/**
	 * @Title: getPayDoc 
	 * @Description: 查询单个支付单信息
	 * @param docId
	 * @return    设定文件
	 */
	public PayDocBean getPayDoc(String docId) ;
	
	/**
	 * @Title: getPayDocs 
	 * @Description: 批量查询支付单信息
	 * @param docIds
	 * @return    设定文件
	 */
	public List<PayDocBean> getPayDocs(List<String> docIds) ;
	
	/**
	 * @Title: getPayDocDtls 
	 * @Description: 支付单明细查询
	 * @param docId
	 * @return    设定文件
	 */
	public List<PayDocDtlBean> getPayDocDtls(String docId) ;
	/**
	 * @Title: getAccDoc
	 * @Description: 报销单查询
	 * @param docId
	 * @return
	 */
	public ACCDocBean getAccDoc(String docId);
	
	/**
	 * @Title: getBgCtrlDim4Prj 
	 * @Description: 获取PA项目的预算控制维度
	 * @param map
	 * @return    设定文件
	 */
	public String getBgCtrlDim4Prj(HashMap<String,?> map) ;
	
	/**
	 * @Title: getBgCtrlDim4BgItem 
	 * @Description: 获取预算项目的预算控制维度 
	 * @param map
	 * @return    设定文件
	 */
	public String getBgCtrlDim4BgItem(HashMap<String,?> map) ;
}
