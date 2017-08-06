package com.xzsoft.xc.util.service;

/**
 * @ClassName: XCWFService 
 * @Description: 云ERP对工作流操作服务类
 * @author linp
 * @date 2016年6月28日 上午11:54:47 
 *
 */
public interface XCWFService {

	/**
	 * @Title: copyArchInstance 
	 * @Description: 复制归档实例信息
	 * @param insId
	 * @throws Exception    设定文件
	 */
	public void copyArchInstance(String insId) throws Exception ;
	
	/**
	 * @Title: delArchInstance 
	 * @Description: 删除归档实例信息 
	 * @param insId
	 * @throws Exception    设定文件
	 */
	public void delArchInstance(String insId) throws Exception ;
	
	/**
	 * @Title: convertCompletedInstance2Reject 
	 * @Description: 将审批完成的的实例转为驳回状态
	 * @param insId
	 * @throws Exception    设定文件
	 */
	public void convertCompletedInstance2Reject(String insId,String cancelReason) throws Exception ;
}
