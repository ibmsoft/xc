package com.xzsoft.xc.gl.service;

import java.util.HashMap;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

/**
 * @ClassName: SumBalanceService 
 * @Description: 余额汇总处理服务类
 * @author linp
 * @date 2016年1月7日 上午10:47:30 
 *
 */
public interface XCGLSumBalanceService {

	/**
	 * @Title: sumGLBalances 
	 * @Description: 余额汇总处理
	 * @param ja	凭证头ID数组
	 * @param isAccount	 是否执行过账处理
	 * @param isSumCash 是否统计现金流量项
	 * @param userId  当前用户ID
	 * @param isCanceled 是否为撤回/驳回处理汇总
	 * @return
	 * @throws Exception    设定文件
	 */
	public JSONObject sumGLBalances(JSONArray ja, String isAccount, String isSumCash, String userId, String isCanceled) throws Exception ;
	
	/**
	 * @Title: sumDataByVoucher 
	 * @Description: 按凭证汇总数据
	 * @param headId
	 * @param hrcyId
	 * @param isAccount
	 * @param isSumCash
	 * @param userId
	 * @param isCanceled
	 * @throws Exception    设定文件
	 */
	public void sumDataByVoucher(String headId,String hrcyId ,String isAccount, String isSumCash, String userId, String isCanceled) throws Exception  ;
	
	/**
	 * @Title: sumInitBalances 
	 * @Description: 建账与取消建账时修改科目期初余额
	 * @param map
	 * @return
	 * @throws Exception    设定文件
	 */
	public JSONObject sumInitBalances(HashMap<String,String> map) throws Exception ; 
	
	/**
	 * @Title: sumInitCash 
	 * @Description: 统计现金流量项目
	 * @param ledgerId
	 * @return
	 * @throws Exception    设定文件
	 */
	public JSONObject sumInitCash(String ledgerId,String isCanceled) throws Exception ; 

	/**
	 * @Title: sumGLBal 
	 * @Description: 汇总总账余额表
	 * @param ja
	 * @param isAccount
	 * @param isSumCash
	 * @param userId
	 * @param isCanceled
	 * @return
	 * @throws Exception    设定文件
	 */
	public void sumGLBal(JSONArray ja, String hrcyId, String isAccount, String isSumCash, String userId, String isCanceled) throws Exception ;
	
}
