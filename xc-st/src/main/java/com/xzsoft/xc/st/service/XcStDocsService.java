package com.xzsoft.xc.st.service;

import org.codehaus.jettison.json.JSONObject;

import com.xzsoft.xc.st.modal.Dto;

/**
 * 
  * @ClassName XcStDocsService
  * @Description 库存模块业务处理层
  * @author RenJianJian
  * @date 2016年12月2日 上午11:30:50
 */
public interface XcStDocsService {
	/**
	 * 
	  * @Title saveOrUpdateXcStDocs 方法名
	  * @Description 增加/修改操作
	  * @param pDto
	  * @return
	  * @throws Exception
	  * @return JSONObject 返回类型
	 */
	public JSONObject saveOrUpdateXcStDocs(Dto pDto) throws Exception;
	/**
	 * 
	  * @Title deleteXcStDocs 方法名
	  * @Description 批量删除
	  * @param hIds			主ID数组
	  * @param docClassCode	单据大类
	  * @return
	  * @throws Exception
	  * @return JSONObject 返回类型
	 */
	public JSONObject deleteXcStDocs(String hIds,String docClassCode) throws Exception;
	/**
	 * 
	  * @Title accountOrCancelAccount 方法名
	  * @Description 记账取消记账
	  * @param stDocId
	  * @param opType
	  * @return
	  * @throws Exception
	  * @return JSONObject 返回类型
	 */
	public JSONObject accountOrCancelAccount(String stDocId,String opType) throws Exception;
	/**
	 * 
	  * @Title acceptDoc 方法名
	  * @Description 物资调拨单接受
	  * @param TR_H_ID
	  * @return
	  * @throws Exception
	  * @return JSONObject 返回类型
	 */
	public JSONObject acceptDoc(String TR_H_ID) throws Exception;
	/**
	 * 
	  * @Title closeXcStUseApply 方法名
	  * @Description 批量关闭申请单
	  * @param hIds
	  * @return
	  * @throws Exception
	  * @return JSONObject 返回类型
	 */
	public JSONObject closeXcStUseApply(String hIds) throws Exception;
}
