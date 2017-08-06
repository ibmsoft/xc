package com.xzsoft.xc.gl.service;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.xzsoft.xc.gl.modal.VHead;

/**
 * @ClassName: GLVoucherHandlerService 
 * @Description: 总账凭证处理接口实现类
 * @author linp
 * @date 2016年7月4日 下午5:03:37 
 *
 */
public interface GLVoucherHandlerService {
	
	/**
	 * @Title: doSaveAndSubmitVoucher 
	 * @Description: 保存并提交单张凭证信息
	 * @param jsonObject
	 * <p> 参数格式：
			{
				"ledgerCode":"",	
				"periodCode":"",
				"categoryCode":"",	
				"srcCode":"",
				"srcId":"",
				"serialNum":"",
				"attchTotal":"",
				"summary":"",
				"templateType":"",
				"vStatus":"",
				"createdBy":"",
				"creationDate":"",
				"lines": [{
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
				},{
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
				}]
			}
	 * </p>
	 * @return
	 * @throws Exception    设定文件
	 */
	public String doSaveAndSubmitVoucher(String jsonObject)throws Exception ;
	
	/**
	 * @Title: doSubmitGLVouchers 
	 * @Description: 提交凭证
	 * @param jsonArray
	 * <p>
	 * 	参数格式： [{"V_HEAD_ID":""},{"V_HEAD_ID":""},{"V_HEAD_ID":""}…]
	 * </p>
	 * @return
	 * @throws Exception    设定文件
	 */
	public String doSubmitGLVouchers(String jsonArray)throws Exception ;
	
	/**
	 * @Title: doCancelSubmitGLVouchers 
	 * @Description: 撤回提交
	 * @param jsonArray
	 * <p>
	 * 	参数格式： [{"V_HEAD_ID":""},{"V_HEAD_ID":""},{"V_HEAD_ID":""}…]
	 * </p>
	 * @return
	 * @throws Exception    设定文件
	 */
	public String doCancelSubmitGLVouchers(String jsonArray)throws Exception ;
	
	/**
	 * @Title: doNewAndSubmitVoucher 
	 * @Description: 新建并提交凭证
	 * @param head
	 * @throws Exception    设定文件
	 */
	public void doNewAndSubmitVoucher(VHead head)throws Exception ;
	
	/**
	 * @Title: doUpdateAndSubmitVoucher 
	 * @Description: 更新并提交凭证
	 * @param head
	 * @throws Exception    设定文件
	 */
	public void doUpdateAndSubmitVoucher(VHead head)throws Exception ;
	
	/**
	 * @Title: doImportVoucher 
	 * @Description: 导入凭证
	 * @param ledgerId
	 * @param sessionId
	 * @param workbook
	 * @return
	 * @throws Exception    设定文件
	 */
	public String doImportVoucher(String ledgerId, String sessionId,HSSFWorkbook workbook)throws Exception ;
	
	/**
	 * @Title: doExportVoucher 
	 * @Description: 导出Excel文件
	 * @param headId
	 * @param filePath
	 * @return
	 * @throws Exception    设定文件
	 */
	public HSSFWorkbook doExportVoucher(String headId,String filePath) throws Exception ;
	
}
