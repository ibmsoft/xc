package com.xzsoft.xc.ap.service;

import java.util.List;

import com.xzsoft.xc.apar.modal.ApDocumentHBean;
import com.xzsoft.xc.gl.modal.VHead;
import com.xzsoft.xc.gl.modal.VLine;
import com.xzsoft.xc.gl.modal.VSource;
import com.xzsoft.xc.util.modal.Ledger;

/**
 * 
  * @ClassName ApVoucherGroupService
  * @Description 应付模块的单据中的凭证组合接口
  * @author 任建建
  * @date 2016年4月7日 上午10:54:03
 */
public interface ApVoucherGroupService {
	/**
	 * 
	  * @Title mergeVoucher 方法名
	  * @Description 应付单组合凭证头信息
	  * @param apDocumentBean	单据信息
	  * @param ledger			账簿
	  * @param vs				凭证来源
	  * @param paramJson		日期参数
	  * @param language			当前语言
	  * @throws Exception 设定文件
	  * @return VHead 返回类型
	 */
	public VHead mergeVoucher(ApDocumentHBean apDocumentBean,Ledger ledger,VSource vs,String paramJson,String language) throws Exception;
	/**
	 * 
	  * @Title createVHead 方法名
	  * @Description 生成凭证头信息
	  * @param apDocumentBean	单据信息
	  * @param ledger			账簿
	  * @param vs				凭证来源
	  * @param paramJson		日期参数
	  * @param language			当前语言
	  * @throws Exception 设定文件
	  * @return VHead 返回类型
	 */
	public VHead createVHead(ApDocumentHBean apDocumentBean,Ledger ledger,VSource vs,String paramJson,String language) throws Exception;
	/**
	 * 
	  * @Title createVDebitLines 方法名
	  * @Description：生成凭证借方分录信息
	  * @param apDocumentBean	单据信息
	  * @param ledger			账簿
	  * @param vs				凭证来源
	  * @param paramJson		日期参数
	  * @param language			当前语言
	  * @throws Exception 设定文件
	  * @return List<VLine> 返回类型
	 */
	public List<VLine> createVDebitLines(ApDocumentHBean apDocumentBean,Ledger ledger,VSource vs,String paramJson,String language) throws Exception;
	/**
	 * 
	  * @Title createVCreditLines 方法名
	  * @Description 生成凭证贷方分录信息
	  * @param apDocumentBean	单据信息
	  * @param ledger			账簿
	  * @param vs				凭证来源
	  * @param paramJson		日期参数
	  * @param language			当前语言
	  * @throws Exception 设定文件
	  * @return List<VLine> 返回类型
	 */
	public List<VLine> createVCreditLines(ApDocumentHBean apDocumentBean,Ledger ledger,VSource vs,String paramJson,String language) throws Exception;
	
}
