package com.xzsoft.xc.st.service;

import org.codehaus.jettison.json.JSONObject;
/**
 * 
  * @ClassName XcStCommonService
  * @Description 
  * @author RenJianJian
  * @date 2016年12月20日 下午12:39:52
 */
public interface XcStCommonService {
	/**
	 * 
	  * @Title updateXcStDocStatus 方法名
	  * @Description 更新单据的提交状态
	  * @param stDocId			单据ID
	  * @param docClassCode		单据大类编码
	  * @throws Exception
	  * @return JSONObject 返回类型
	 */
	public JSONObject updateXcStDocStatus(String stDocId,String docClassCode) throws Exception;
	/**
	 * 
	  * @Title getXcStProcAndForms 方法名
	  * @Description 得到模块中的流程相关信息
	  * @param stCatCode
	  * @param ledgerId
	  * @return
	  * @throws Exception
	  * @return JSONObject 返回类型
	 */
	public JSONObject getXcStProcAndForms(String stCatCode,String ledgerId) throws Exception;
	/**
	 * 
	  * @Title getXcStAttCatCode 方法名
	  * @Description 得到库存的附近分类编码
	  * @param stCatCode
	  * @param ledgerId
	  * @return
	  * @throws Exception
	  * @return JSONObject 返回类型
	 */
	public String getXcStAttCatCode(String stCatCode,String ledgerId) throws Exception;
}
