package com.xzsoft.xc.gl.service;

/**
 * @ClassName: XCGLBudgetCtrlService 
 * @Description: 总账预算控制服务接口
 * @author linp
 * @date 2016年4月8日 下午3:00:04 
 *
 */
public interface XCGLBudgetCtrlService {
	
	/**
	 * @Title: glBudgetCtrl 
	 * @Description: 总账凭证预算控制
	 * <p>
	 * 	返回值说明：
	 * 		flag   :  检查标记位：0-检查通过,1-检查不通过,2-检查警告,3-检查失败
	 * 		msg    :  如果flag为3, 则存储检查失败的原因
	 * 		result :  如果flag为0、1和2, 则记录预算检查结果描述信息
	 * </p>
	 * @param ledgerId
	 * @param headId
	 * @return
	 * @throws Exception    设定文件
	 */
	public String glBudgetCtrl(String headId)throws Exception ;
	
}
