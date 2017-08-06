package com.xzsoft.xc.bg.service;

import java.util.List;

import com.xzsoft.xc.bg.modal.BgFactDTO;

/**
 * @ClassName: BudgetCtrlService 
 * @Description: 预算控制服务接口
 * @author linp
 * @date 2016年3月10日 上午9:41:17 
 *
 */
public interface BudgetCtrlService {
	
	/**
	 * @Title: budgetCheck 
	 * @Description: 预算检查处理
	 * 
	 * 	返回值说明：
	 * 		flag   :  检查标记位：0-检查通过,1-检查不通过,2-检查警告,3-检查失败
	 * 		msg    :  如果flag为3, 则存储检查失败的原因
	 * 		result :  如果flag为0、1和2, 则记录预算检查结果描述信息
	 * 
	 * @param conditions
	 * @return
	 * @throws Exception    设定文件
	 */
	public String budgetCheck(List<BgFactDTO> conditions) throws Exception ;
	
}
