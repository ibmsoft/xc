package com.xzsoft.xc.ap.service;

import org.codehaus.jettison.json.JSONObject;

/**
 * @ClassName: ApCancelService 
 * @Description: 核销单相关操作
 * @author zhenghy
 * @date 2016年8月19日 下午3:51:33
 */
public interface ApCancelService {

	/**
	 * @Title: saveCancel
	 * @Description: 核销单保存（新增、修改）
	 * @param cancelH
	 * @param newRecs
	 * @param updateRecs
	 * @param deleteRecs
	 * @param opType
	 * @throws Exception  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public JSONObject saveCancel(String cancelH,String newRecs,String updateRecs,String deleteRecs,String opType,String language) throws Exception;
	
	/**
	 * @Title: deleteCancel
	 * @Description: 删除核销单
	 * @param deleteRecs
	 * @throws Exception  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public JSONObject deleteCancel(String deleteRecs,String language) throws Exception;
}
