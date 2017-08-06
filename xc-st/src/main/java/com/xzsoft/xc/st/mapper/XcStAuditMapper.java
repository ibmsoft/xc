package com.xzsoft.xc.st.mapper;

import java.util.HashMap;
/**
 * 
  * @ClassName XcStAuditMapper
  * @Description 更新库存模块单据流程信息Mapper
  * @author RenJianJian
  * @date 2016年12月8日 下午3:17:33
 */
public interface XcStAuditMapper {
	/**
	 * 
	  * @Title updateXcStInsCode 方法名
	  * @Description 更新审批流程实例编码
	  * @param map
	  * @return void 返回类型
	 */
	public void updateXcStInsCode(HashMap<String, String> map);
	/**
	 * 
	  * @Title updateXcStAuditStatus 方法名
	  * @Description 更新审批流程审批状态
	  * @param map
	  * @return void 返回类型
	 */
	public void updateXcStAuditStatus(HashMap<String, String> map);
	/**
	 * 
	  * @Title updateXcStAuditDate 方法名
	  * @Description 更新流程审批通过日期
	  * @param map
	  * @return void 返回类型
	 */
	public void updateXcStAuditDate(HashMap<String, String> map);
	/**
	 * 
	  * @Title revokeXcStCompleteProcess 方法名
	  * @Description 审批通过之后执行撤回，更新单据信息
	  * @param map
	  * @return void 返回类型
	 */
	public void revokeXcStCompleteProcess(HashMap<String, String> map);
}
