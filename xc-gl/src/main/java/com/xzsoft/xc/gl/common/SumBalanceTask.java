package com.xzsoft.xc.gl.common;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;

import com.xzsoft.xc.gl.service.XCGLSumBalanceService;
import com.xzsoft.xip.framework.util.AppContext;

/**
 * @ClassName: SumBalanceTask 
 * @Description: 余额汇总任务
 * @author linp
 * @date 2016年1月7日 下午5:26:30 
 *
 */
public class SumBalanceTask implements Runnable {
	// 日志记录器
	private static final Logger log = Logger.getLogger(SumBalanceTask.class.getName()) ;
	
	private JSONArray ja ; 
	private String isAccount ;
	private String isSumCash ;
	private String userId ;
	private String isCanceled ; 
	
	public SumBalanceTask(JSONArray ja, String isAccount, String isSumCash, String userId, String isCanceled){
		this.ja = ja ;
		this.isAccount = isAccount ;
		this.isSumCash = isSumCash ;
		this.userId = userId ;
		this.isCanceled = isCanceled ;
	}

	@Override
	public void run() {
		try {
			// 获取服务类
			XCGLSumBalanceService service = (XCGLSumBalanceService) AppContext.getApplicationContext().getBean("xcglSumBalanceService") ;
			
			// 执行余额汇总任务
			service.sumGLBalances(ja, isAccount, isSumCash, userId, isCanceled) ;
		
			// 记录汇总结果
			
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

}
