package com.xzsoft.xc.gl.common;

import java.util.HashMap;
import java.util.List;

import net.sf.json.JsonConfig;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.codehaus.jettison.json.JSONObject;

import com.xzsoft.xc.gl.modal.CCID;
import com.xzsoft.xc.gl.modal.VHead;
import com.xzsoft.xc.util.modal.Ledger;

/**
 * @ClassName: AbstractVoucherHandler 
 * @Description: 凭证处理抽象类
 * @author linp
 * @date 2016年2月29日 上午11:38:46 
 *
 */
public abstract class AbstractVoucherHandler {
	
	/**
	 * @Title: getFullCode 
	 * @Description: 获取科目组合的全编码信息 
	 * 
	 * 	全编码组合顺序为:
	 * 	        科目.供应商.客户.产品.内部往来.个人往来.成本中心.项目.自定义1.自定义2.自定义3.自定义4
	 * 	   ACC_CODE.VENDOR_CODE.CUSTOMER_CODE.PRODUCT_CODE.ORG_CODE.EMP_CODE.DEPT_CODE.PROJECT_CODE.CUSTOM1_CODE.CUSTOM2_CODE.CUSTOM3_CODE.CUSTOM4_CODE
	 * 
	 * @param ledgerId	// 账簿ID
	 * @param accId		// 科目ID
	 * @param segments	// 科目辅助段信息
	 * @return
	 * @throws Exception    设定文件
	 */
	public abstract String getFullCode(String ledgerId, String accId, HashMap<String,String> segments)throws Exception ;
	
	/**
	 * @Title: getCCID 
	 * @Description: 取得科目CCID 
	 * @param fullCode		// 科目组合全编码
	 * @param ledgerId		// 账簿ID
	 * @param accId			// 科目ID
	 * @param segments		// 辅助段信息
	 * @return
	 * @throws Exception    设定文件
	 */
	public abstract JSONObject getCCID(String fullCode,String ledgerId, String accId, HashMap<String, String> segments) throws Exception ;
	
	/**
	 * @Title: newCCID 
	 * @Description: 生成CCID
	 * @param ledgerId
	 * @param hrcyId
	 * @param accId
	 * @param segments
	 * @param isLeaf
	 * @param jo
	 * @param ccids
	 * @throws Exception    设定文件
	 */
	public abstract void newCCID(String ledgerId, String hrcyId, String accId, 
			HashMap<String,String> segments, String isLeaf, JSONObject jo,List<CCID> ccids) throws Exception ;
	
	/**
	 * @Title: jsonArray2List 
	 * @Description: 将包含凭证头ID信息的JSON数组转换为List对象
	 * @param jsonArray
	 * @return
	 * @throws Exception    设定文件
	 */
	public abstract List<String> jsonArray2List(String jsonArray) throws Exception  ;
	
	/**
	 * @Title: jsonStr2headBean 
	 * @Description: 将单张凭证信息转换成JavaBean
	 * @param paramJson
	 * @return
	 * @throws Exception    设定文件
	 */
	public abstract VHead jsonStr2headBean(String paramJson) throws Exception ;
	
	/**
	 * @Title: headBean2JsonStr 
	 * @Description: 将单张凭证信息（JavaBean）转换成JSON串
	 * @param bean
	 * @param cfg
	 * @return
	 * @throws Exception    设定文件
	 */
	public abstract String headBean2JsonStr(VHead bean,JsonConfig cfg) throws Exception ;

	/**
	 * @Title: sumGLBalances 
	 * @Description:  执行余额汇总处理
	 * @param list   			// 凭证头ID列表
	 * @param isAccount  		// 当前操作是否为过账操作 ;  取值说明:Y-过账汇总,N-提交、撤回提交和驳回汇总
	 * @param isSumCash			// 是否统计现金流量项 ; 取值说明:ALL-汇总科目余额和统计现金流量项, CA-仅统计现金流量项, BA-仅汇总科目余额
	 * @param userId			// 当前用户ID
	 * @param isCanceled		// 是否为取消凭证汇总处理(撤回提交和驳回提交凭证时发生的余额汇总为取消汇总，提交凭证和过账发生的余额汇总为新增汇总); 取值说明:Y-是,N-否
	 * @throws Exception    设定文件
	 */
	public abstract void sumGLBalances(List<String> list, String isAccount, String isSumCash, String userId, String isCanceled) throws Exception ;

	/**
	 * @Title: checkParams 
	 * @Description: 验证参数的合法性
	 * @param jsonArray
	 * @param checkType
	 * <p> 
	 *	 checkType分类说明:
	 * 		save    - 凭证保存
	 *  	update  - 凭证修改
	 *  	writeOff- 凭证冲销
	 * 		submit  - 凭证提交
	 *  	cancel  - 撤回提交
	 * 		delete  - 凭证删除
	 *  	reject  - 凭证驳回
	 *  	check   - 凭证审核
	 *  	uncheck - 取消审核
	 *  	sign    - 出纳签字
	 *  	unsign  - 取消签字
	 *  	account - 凭证过账
	 * </p>
	 * @return
	 * @throws Exception    设定文件
	 */
	public abstract HashMap<String,Object> checkParams(String jsonArray, String checkType) throws Exception ;
	
	/**
	 * @Title: checkSingleVoucher 
	 * @Description: 单个凭证保存或提交审核时校验
	 * @param map
	 * @throws Exception    设定文件
	 */
	public abstract void checkSingleVoucher(HashMap<String,Object> map) throws Exception ;
	
	/**
	 * @Title: generateSQL 
	 * @Description: 生成SQL
	 * @param workbook
	 * @return
	 * @throws Exception    设定文件
	 */
	public abstract HashMap<String,Object> generateSQL(HSSFWorkbook workbook) throws Exception ; 
	
	/**
	 * @Title: parseExcel 
	 * @Description: 解析Excel文件
	 * @param fieldMap
	 * @param workbook
	 * @param sessionId
	 * @param ledger
	 * @return
	 * @throws Exception    设定文件
	 */
	public abstract List<HashMap<String,Object>> parseExcel(HashMap<Integer,String> fieldMap,HSSFWorkbook workbook,String sessionId,Ledger ledger) throws Exception ;
	
	/**
	 * @Title: checkVoucherBeforeSave 
	 * @Description: 凭证保存前校验处理
	 * @param bean
	 * @return
	 * @throws Exception    设定文件
	 */
	public abstract VHead checkVoucherBeforeSave(VHead bean) throws Exception ;
	
}
