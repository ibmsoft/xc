package com.xzsoft.xc.ex.service;

import java.util.List;

import com.xzsoft.xc.ex.modal.EXDocBean;
import com.xzsoft.xc.gl.modal.VHead;
import com.xzsoft.xc.gl.modal.VLine;
import com.xzsoft.xc.gl.modal.VSource;
import com.xzsoft.xc.util.modal.Ledger;

/**
 * @ClassName: EXVoucher4DocCatService 
 * @Description: 按单据类型组合凭证信息接口
 * @author linp
 * @date 2016年4月1日 上午9:19:41 
 *
 */
public interface EXVoucher4DocService {
	
	/**
	 * @Title: mergeVoucher 
	 * @Description: 组合凭证信息
	 * @param docBeans
	 * @param ledger
	 * @param vs
	 * @param paramJson
	 * @return
	 * @throws Exception    设定文件
	 */
	public VHead mergeVoucher(List<EXDocBean> docBeans,Ledger ledger,VSource vs,String paramJson) throws Exception ;
	
	/**
	 * @Title: createVHead 
	 * @Description: 生成凭证头信息
	 * <p>
	 *	"ledgerCode":"",	
	 *	"periodCode":"",
	 *	"categoryCode":"",	
	 *	"srcCode":"",
	 *	"srcId":"",
	 *	"serialNum":"",
	 *	"attchTotal":"",
	 *	"summary":"",
	 *	"templateType":"",
	 *	"vStatus":"",
	 *	"createdBy":"",
	 *	"creationDate":"",
	 * </p>
	 * @param docBeans		// 报销单列表
	 * @param ledger		// 账簿
	 * @param vs			// 凭证来源
	 * @param paramJson 	// 辅助参数(包括：支付日期、付款单摘要和付款单ID)
	 * @return
	 * @throws Exception    设定文件
	 */
	public VHead createVHead(List<EXDocBean> docBeans,Ledger ledger,VSource vs,String paramJson) throws Exception  ;
	
	/**
	 * @Title: createVDebitLine 
	 * @Description: 生成凭证借方分录信息
	 * <p>
		"summary":"",
        "ccid":"",
		"accCode":"",
		"vendorCode":"00",
		"customerCode":"00",
		"prodCode":"00",
		"orgCode":"00",
		"empCode":"00",
		"deptCode":"00",
		"prjCode":"00",
		"cust1Code":"00",
		"cust2Code":"00",
		"cust3Code":"00",
		"cust4Code":"00",
		"caCode":"",
		"srcDtlId":"",
		"orderBy":"",
		"amount":0,
		"exchangeRate":1,
		"enterDR":0,
		"enterCR":0,
		"accountDR":1450,
		"accountCR":0
	 * </p>
	 * @param docBeans
	 * @param ledger
	 * @param vs
	 * @param paramJson
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<VLine> createVDebitLines(List<EXDocBean> docBeans,Ledger ledger,VSource vs,String paramJson) throws Exception  ;
	
	/**
	 * @Title: createVCreditLine 
	 * @Description: 生成凭证贷方分录信息
	 * <p>
	 	"summary":"",
        "ccid":"",
		"accCode":"",
		"vendorCode":"00",
		"customerCode":"00",
		"prodCode":"00",
		"orgCode":"00",
		"empCode":"00",
		"deptCode":"00",
		"prjCode":"00",
		"cust1Code":"00",
		"cust2Code":"00",
		"cust3Code":"00",
		"cust4Code":"00",
		"caCode":"",
		"srcDtlId":"",
		"orderBy":"",
		"amount":0,
		"exchangeRate":1,
		"enterDR":0,
		"enterCR":0,
		"accountDR":1450,
		"accountCR":0
	 * </p>
	 * @param docBeans		报销单列表
	 * @param ledger		账簿表
	 * @param vs			凭证来源
	 * @param paramJson		辅助参数(包括：支付方式、付款方式、付款账号、现金流量项目ID、凭证摘要)
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<VLine> createVCreditLines(List<EXDocBean> docBeans,Ledger ledger,VSource vs,String paramJson) throws Exception  ;
	
}
