package com.xzsoft.xc.ex.service.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;

import com.xzsoft.xc.ex.service.XCEXBudgetService;
import com.xzsoft.xc.gl.dao.XCGLCommonDAO;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xc.util.modal.Ledger;
import com.xzsoft.xip.platform.util.XipUtil;

/**
 * @ClassName: XCEXBudgetServiceImpl 
 * @Description: 费用报销预算控制服务接口实现类
 * @author linp
 * @date 2016年4月8日 下午3:01:57 
 *
 */
public class XCEXBudgetServiceImpl implements XCEXBudgetService {
	// 日志记录器
	private static final Logger log = Logger.getLogger(XCEXBudgetServiceImpl.class.getName()) ;
	@Resource
	private XCGLCommonDAO xcglCommonDAO ;
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: exBudgetCtrl</p> 
	 * <p>Description: 费用单据预算控制服务
	 * 	返回值说明：
	 * 		flag   :  检查标记位：0-检查通过,1-检查不通过,2-检查失败
	 * 		msg    :  如果flag为2, 则存储检查失败的原因
	 * 		result :  如果flag为0或1, 则记录预算检查结果描述信息
	 *  </p> 
	 * @param ledgerId
	 * @param docId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.service.XCEXBudgetService#exBudgetCtrl(java.lang.String)
	 */
	@Override
	public String exBudgetCtrl(String ledgerId, String docId) throws Exception {
		JSONObject jo = new JSONObject() ;
		
		try {
			Ledger ledger = xcglCommonDAO.getLedger(ledgerId) ;
			if(!"Y".equals(ledger.getBgIsChk())){
				jo.put("flag", "0") ;
				jo.put("msg", "") ;
				//jo.put("result", "账簿【"+ledger.getLedgerName()+"】不需要执行预算检查") ;
				jo.put("result", XipUtil.getMessage(XCUtil.getLang(), "XC_EX_LEDGER", null)+"【"+ledger.getLedgerName()+"】"+XipUtil.getMessage(XCUtil.getLang(), "XC_EX_NOT_BG_CHECK", null)) ;
				
			}else{
				/*
				 * 如果费用单据涉及到项目则执行项目预算检查; 如果不涉及项目则执行费用预算检查
				 */
				
				// 检查项目预算
				
				
				// 费用预算检查
				
				
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
		
		return jo.toString() ;
	}
}
