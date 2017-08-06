package com.xzsoft.xc.bg.mapper;

import java.util.HashMap;
/**
 * 
  * @ClassName: BgAuditMapper
  * @Description: 更行预算单流程信息Mapper
  * @author 任建建
  * @date 2016年3月28日 上午9:13:27
 */
public interface BgAuditMapper {
	/**
	 * 
	  * @Title: updateBgInsCode
	  * @Description: 更新审批流程实例编码
	  * @param @param map	设定文件
	  * @return void	返回类型
	 */
	public void updateBgInsCode(HashMap<String, String> map);
	/**
	 * 
	  * @Title: updateBgAuditStatus
	  * @Description: 更新审批流程审批状态
	  * @param @param map	设定文件
	  * @return void	返回类型
	 */
	public void updateBgAuditStatus(HashMap<String, String> map);
	/**
	 * 
	  * @Title: updateBgAuditDate
	  * @Description: 更新流程审批通过日期
	  * @param @param map	设定文件
	  * @return void	返回类型
	 */
	public void updateBgAuditDate(HashMap<String, String> map);
	/**
	 * 
	  * @Title: updateBgAuditUsers
	  * @Description: 更新当前审批人信息
	  * @param @param map	设定文件
	  * @return void	返回类型
	 */
	public void updateBgAuditUsers(HashMap<String, String> map);
}
