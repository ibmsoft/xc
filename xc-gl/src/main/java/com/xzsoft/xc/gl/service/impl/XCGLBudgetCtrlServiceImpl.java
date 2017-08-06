package com.xzsoft.xc.gl.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.codehaus.jettison.json.JSONArray;
import org.springframework.stereotype.Service;

import com.xzsoft.xc.bg.modal.BgFactDTO;
import com.xzsoft.xc.bg.service.BudgetCtrlService;
import com.xzsoft.xc.gl.dao.VoucherHandlerDAO;
import com.xzsoft.xc.gl.service.XCGLBudgetCtrlService;

/**
 * @ClassName: XCGLBudgetCtrlServiceImpl 
 * @Description: 总账预算控制服务接口实现类
 * @author linp
 * @date 2016年4月8日 下午3:00:42 
 *
 */
@Service("xcglBudgetCtrlService")
public class XCGLBudgetCtrlServiceImpl implements XCGLBudgetCtrlService {
	@Resource 
	private VoucherHandlerDAO  voucherHandlerDAO ;
	@Resource
	private BudgetCtrlService budgetCtrlService ;
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: glBudgetCtrl</p> 
	 * <p>Description: 总账预算控制服务  
	 * 	返回值说明：
	 * 		flag   :  检查标记位：0-检查通过,1-检查不通过,2-检查警告,3-检查失败
	 * 		msg    :  如果flag为3, 则存储检查失败的原因
	 * 		result :  如果flag为0、1和2, 则记录预算检查结果描述信息
	 * </p> 
	 * @param ledgerId
	 * @param headId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.service.XCGLBudgetCtrlService#glBudgetCtrl(java.lang.String)
	 */
	@Override
	public String glBudgetCtrl(String headId)throws Exception {
		String jsonStr = null ;
		
		try {
			List<BgFactDTO> conditions = voucherHandlerDAO.getbudgetConditions(headId) ;
			if(conditions != null && conditions.size()>0){
				String json = budgetCtrlService.budgetCheck(conditions) ;
				// 解析返回结果
				JSONArray ja = new JSONArray(json) ;
				if(ja.length() >0){
					for(int i=0; i<ja.length(); i++){
						
					}
				}
			}
		} catch (Exception e) {
			
		}
		
		return jsonStr ;
	}

}
