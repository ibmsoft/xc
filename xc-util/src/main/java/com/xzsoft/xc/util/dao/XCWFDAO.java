package com.xzsoft.xc.util.dao;

/**
 * @ClassName: XCWFDAO 
 * @Description: 云ERP对工作流操作数据层接口
 * @author linp
 * @date 2016年6月28日 下午1:49:21 
 *
 */
public interface XCWFDAO {

	/**
	 * @Title: delArchInstance 
	 * @Description: 删除归档待办信息
	 * @param instanceId
	 * @throws Exception    设定文件
	 */
	public void delArchInstance(String instanceId) throws Exception ;
	
	/**
	 * @Title: copyArchInstance 
	 * @Description: 复制归档实例
	 * @param instanceId
	 * @throws Exception    设定文件
	 */
	public void copyArchInstance(String instanceId) throws Exception ;
	
	/**
	 * @Title: convertCompletedInstance2Reject 
	 * @Description: 将审批完成的的实例转为驳回状态
	 * @param insId
	 * @throws Exception    设定文件
	 */
	public void convertCompletedInstance2Reject(String instanceId,String cancelReason) throws Exception ;
	
}
