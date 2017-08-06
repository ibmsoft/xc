package com.xzsoft.xc.ar.service;

import java.util.List;

import com.xzsoft.xc.apar.modal.ArDocumentHBean;
import com.xzsoft.xc.gl.modal.VHead;
import com.xzsoft.xc.gl.modal.VLine;
import com.xzsoft.xc.gl.modal.VSource;
import com.xzsoft.xc.util.modal.Ledger;

/**
 * 
  * @ClassName ArVoucherGroupService
  * @Description 应收模块的单据中的凭证组合接口
  * @author 任建建
  * @date 2016年7月8日 下午3:18:42
 */
public interface ArVoucherGroupService {
	/**
	 * 
	  * @Title mergeVoucher 方法名
	  * @Description 应收单组合凭证头信息
	  * @param arDocumentBean
	  * @param ledger
	  * @param vs
	  * @param paramJson
	  * @param language
	  * @return
	  * @throws Exception
	  * @return VHead 返回类型
	 */
	public VHead mergeVoucher(ArDocumentHBean arDocumentBean,Ledger ledger,VSource vs,String paramJson,String language) throws Exception;
	/**
	 * 
	  * @Title createVHead 方法名
	  * @Description 生成凭证头信息
	  * @param arDocumentBean
	  * @param ledger
	  * @param vs
	  * @param paramJson
	  * @param language
	  * @return
	  * @throws Exception
	  * @return VHead 返回类型
	 */
	public VHead createVHead(ArDocumentHBean arDocumentBean,Ledger ledger,VSource vs,String paramJson,String language) throws Exception;
	/**
	 * 
	  * @Title createVDebitLines 方法名
	  * @Description 生成凭证借方分录信息
	  * @param arDocumentBean
	  * @param ledger
	  * @param vs
	  * @param paramJson
	  * @param language
	  * @return
	  * @throws Exception
	  * @return List<VLine> 返回类型
	 */
	public List<VLine> createVDebitLines(ArDocumentHBean arDocumentBean,Ledger ledger,VSource vs,String paramJson,String language) throws Exception;
	/**
	 * 
	  * @Title createVCreditLines 方法名
	  * @Description 生成凭证贷方分录信息
	  * @param arDocumentBean
	  * @param ledger
	  * @param vs
	  * @param paramJson
	  * @param language
	  * @return
	  * @throws Exception
	  * @return List<VLine> 返回类型
	 */
	public List<VLine> createVCreditLines(ArDocumentHBean arDocumentBean,Ledger ledger,VSource vs,String paramJson,String language) throws Exception;
}