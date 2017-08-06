package com.xzsoft.xc.ex.service;

/**
 * @ClassName: XCEXBudgetService 
 * @Description: 费用报销预算控制服务接口
 * @author linp
 * @date 2016年4月8日 下午3:01:37 
 *
 */
public interface XCEXBudgetService {

	/**
	 * @Title: exBudgetCtrl 
	 * @Description: 费用单据预算控制服务
	 * <p>
	 * 	返回值说明：
	 * 		flag   :  检查标记位：0-检查通过,1-检查不通过,2-检查失败
	 * 		msg    :  如果flag为2, 则存储检查失败的原因
	 * 		result :  如果flag为0或1, 则记录预算检查结果描述信息
	 * </p>
	 * @param ledgerId
	 * @param docId
	 * @return
	 * @throws Exception    设定文件
	 */
	public String exBudgetCtrl(String ledgerId, String docId) throws Exception ;
}
