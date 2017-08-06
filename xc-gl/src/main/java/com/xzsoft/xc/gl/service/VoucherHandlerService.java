package com.xzsoft.xc.gl.service;

import java.util.HashMap;
import java.util.List;

import org.codehaus.jettison.json.JSONObject;

import com.xzsoft.xc.gl.modal.VHead;
import com.xzsoft.xc.gl.modal.VLine;

/**
 * 
 * @ClassName: VoucherHandlerService 
 * @Description: 记账凭证处理服务接口
 * @author linp
 * @date 2015年12月18日 下午6:12:12 
 *
 */
public interface VoucherHandlerService {
	
	/**
	 * @Title: generateCCID 
	 * @Description: 凭证分录科目组合ID处理
	 * @param ledgerId
	 * @param accId
	 * @param segments
	 * @return
	 * @throws Exception    设定文件
	 */
	public String generateCCID(String ledgerId,String accId,HashMap<String,String> segments) throws Exception ;
	
	/**
	 * @Title: handlerCCID 
	 * @Description: 凭证分录科目组合ID处理
	 * @param @param ledgerId
	 * @param @param accId
	 * @param @param segments
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return JSONObject    返回类型 
	 * @throws
	 */
	public JSONObject handlerCCID(String ledgerId,String accId,HashMap<String,String> segments) throws Exception ;
	
	/**
	 * @Title: doSaveVoucher 
	 * @Description: 凭证信息
	 * @param jsonObject
	 * @return
	 * @throws Exception    设定文件
	 */
	public String doSaveVoucher(String jsonObject)throws Exception ;
	
	/**
	 * @Title: doSubmitVoucher 
	 * @Description: 提交凭证
	 * @param jsonArray
	 * <p>
	 * 	参数格式： [{"V_HEAD_ID":""},{"V_HEAD_ID":""},{"V_HEAD_ID":""}…]
	 * </p>
	 * @return
	 * @throws Exception    设定文件
	 */
	public String doSubmitVouchers(String jsonArray)throws Exception ;
	
	/**
	 * @Title: doCancelSubmitVoucher 
	 * @Description: 撤回提交
	 * @param jsonArray
	 * <p>
	 * 	参数格式： [{"V_HEAD_ID":""},{"V_HEAD_ID":""},{"V_HEAD_ID":""}…]
	 * </p>
	 * @return
	 * @throws Exception    设定文件
	 */
	public String doCancelSubmitVouchers(String jsonArray)throws Exception ;
	
	/**
	 * @Title: doDelVoucher 
	 * @Description: 删除凭证信息
	 * @param jsonArray
	 * <p>
	 * 	参数格式： [{"V_HEAD_ID":""},{"V_HEAD_ID":""},{"V_HEAD_ID":""}…]
	 * </p>
	 * @return
	 * @throws Exception    设定文件
	 */
	public String doDelVouchers(String jsonArray)throws Exception ;
	
	/**
	 * @Title: doCheckVoucher 
	 * @Description: 凭证审核
	 * @param jsonArray
	 * <p>
	 * 	参数格式： [{"V_HEAD_ID":""},{"V_HEAD_ID":""},{"V_HEAD_ID":""}…]
	 * </p>
	 * @return
	 * @throws Exception    设定文件
	 */
	public String doCheckVouchers(String jsonArray)throws Exception ;

	/**
	 * @Title: doUncheckVoucher 
	 * @Description: 取消凭证审核
	 * @param jsonArray
	 * <p>
	 * 	参数格式： [{"V_HEAD_ID":""},{"V_HEAD_ID":""},{"V_HEAD_ID":""}…]
	 * </p>
	 * @return
	 * @throws Exception    设定文件
	 */
	public String doUncheckVouchers(String jsonArray)throws Exception ;
	
	/**
	 * @Title: doRejectVoucher 
	 * @Description: 驳回凭证
	 * @param String jsonArray
	 * <p>
	 * 	参数格式： [{"V_HEAD_ID":""},{"V_HEAD_ID":""},{"V_HEAD_ID":""}…]
	 * </p>
	 * @return
	 * @throws Exception    设定文件
	 */
	public String doRejectVouchers(String jsonArray)throws Exception ;
	
	/**
	 * @Title: doSignVoucher 
	 * @Description: 出纳签字
	 * @param String jsonArray
	 * <p>
	 * 	参数格式： [{"V_HEAD_ID":""},{"V_HEAD_ID":""},{"V_HEAD_ID":""}…]
	 * </p>
	 * @return
	 * @throws Exception    设定文件
	 */
	public String doSignVouchers(String jsonArray)throws Exception ;
	
	/**
	 * @Title: doUnsignVoucher 
	 * @Description: 取消签字
	 * @param String jsonArray
	 * <p>
	 * 	参数格式： [{"V_HEAD_ID":""},{"V_HEAD_ID":""},{"V_HEAD_ID":""}…]
	 * </p>
	 * @return
	 * @throws Exception    设定文件
	 */
	public String doUnsignVouchers(String jsonArray)throws Exception ;
	
	/**
	 * @Title: doAccount 
	 * @Description: 凭证过账
	 * @param jsonArray
	 * @return
	 * @throws Exception    设定文件
	 */
	public String doAccount(String jsonArray)throws Exception ;
	
	/**
	 * @Title: doCancelAccount 
	 * @Description: 凭证取消记账
	 * @param jsonArray
	 * @return
	 * @throws Exception    设定文件
	 */
	public String doCancelAccount(String jsonArray)throws Exception ;
	
	/**
	 * @Title: doViewVoucher 
	 * @Description: 凭证查看
	 * @param jsonArray
	 * <p>
	 * 	参数格式：{"V_HEAD_ID":""}
	 * </p>
	 * @return
	 * @throws Exception    设定文件
	 */
	public String doViewVoucher(String jsonObject)throws Exception ;
	
	/**
	 * @Title: getVHead 
	 * @Description: 查询凭证头信息
	 * @param headId
	 * @return
	 * @throws Exception    设定文件
	 */
	public VHead getVHead(String headId)throws Exception ;
	
	/**
	 * @Title: headJson2Bean 
	 * @Description: 凭证头JSON串转换成JavaBean
	 * 
	 * <p>
		{
			"ledger":"6ed099ca-f21c-474e-b32f-0ce4d3183753",
			"accountDate":"2016-03-30 00:00:00.000",
			"ldPeriod":"2016-03",
			"v_head_id":"2da7cfdf-d2e4-4800-bdaa-42cffbbdcf0b",
			"v_write_off":"",
			"v_write_off_num":"",
			"v_total_dr":1600,"v_total_cr":1600,
			"v_is_signed":"N",
			"v_category":"14937dde-a85d-11e5-bf98-00163e00024e",
			"v_serial_no":"",
			"v_summary":"石家庄一体化报表实施",
			"v_attchment_num":1
		}
	   </p>
	 * @param headJson
	 * @return
	 * @throws Exception    设定文件
	 */
	public VHead headJson2Bean(String headJson) throws Exception  ;
	
	/**
	 * @Title: lineArray2Bean 
	 * @Description: 凭证分录行JSON串转换成JavaBean
	 * <p>
		{
			"SUMMARY":"石家庄一体化报表实施",
			"CCID_FULL_NAME":"2701长期应付款/admin系统管理员/102A11兴竹同智.河北分公司/0102国电经贸项目",
			"LINE_ID":"9686dff6-aabe-4d15-9fa3-f8db9e506cb0",
			"CA_ID":"e00ca117-a85c-11e5-bf98-00163e00024e",
			"DIM_ID":"",
			"CCID":"04badc8e-6dec-40ed-850b-5e9c18d0784c",
			"AMOUNT":0,
			"EXCHANGE_RATE":1,
			"CURRENCY_CODE":
			"CNY",
			"ENTER_DR":0,
			"ENTER_CR":0,
			"ACCOUNT_DR":0,
			"ACCOUNT_CR":1600,
			"IS_BC_ACC":"N",
			"IS_FOREIGN":"N"
		}
	   </p>
	 * @param lineArray
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<VLine> lineArray2Bean(String lineArray) throws Exception ;
	
	/**
	 * @Title: doNewVoucher 
	 * @Description: 新增一张凭证
	 * @param head
	 * @return
	 * @throws Exception    设定文件
	 */
	public void doNewVoucher(VHead head)throws Exception ;
	
	/**
	 * @Title: doUpdateVoucher 
	 * @Description: 单凭证更新处理
	 * @param head
	 * @return
	 * @throws Exception    设定文件
	 */
	public void doUpdateVoucher(VHead head)throws Exception ;
	
	/**
	 * @Title: doSubmitVouchers 
	 * @Description: 单凭证提交处理
	 * @param head
	 * @return
	 * @throws Exception    设定文件
	 */
	public void doSubmitVoucher(VHead head)throws Exception ;
	
	/**
	 * @Title: doCheckVouchers 
	 * @Description: 单凭证复核处理
	 * @param head
	 * @return
	 * @throws Exception    设定文件
	 */
	public void doCheckVouchers(VHead head)throws Exception ;
	
	/**
	 * @Title: doUncheckVouchers 
	 * @Description: 单凭证取消复核处理
	 * @param head
	 * @return
	 * @throws Exception    设定文件
	 */
	public void doUncheckVouchers(VHead head)throws Exception ;
	
}
