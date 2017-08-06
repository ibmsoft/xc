package com.xzsoft.xc.ex.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xzsoft.xc.ex.modal.IAFactBean;
import com.xzsoft.xc.ex.service.BorrowMoneyService;
import com.xzsoft.xip.wf.common.util.WFUtil;

/**
 * 
 * @ClassName: BorrowMoneyAction
 * @Description: 备用金处理
 * @author weicw
 * @date 2015年3月10日 下午6:10:37
 *
 */
@Controller
@RequestMapping("/borrowMoneyAction.do")
public class BorrowMoneyAction {
	// 日志记录器
	private static Logger log = Logger.getLogger(BorrowMoneyAction.class.getName());
	@Resource
	private  BorrowMoneyService borrowMoneyService;
	
	/**
	 * @Title: getUserImprest
	 * @Description: 获取个人账簿备用金余额
	 * @param @param request
	 * @param @param response 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	@RequestMapping(params = "method=getUserImprest")
	public void getUserImprest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject jo = new JSONObject() ;
		try {
			String ledgerId = request.getParameter("ledgerId");
			
			String userId = request.getParameter("userId");
			IAFactBean iaFactBean = new IAFactBean();
			iaFactBean.setLedgerId(ledgerId);
			iaFactBean.setOperationType("1");
			iaFactBean.setExUserId(userId);
			borrowMoneyService.handlerUserImprest(iaFactBean);
			//获取备用金余额
			double iaBal =  iaFactBean.getIABal();
			jo.put("iaBal", iaBal);
		} catch (Exception e) {
			log.error(e.getMessage());
			jo.put("iaBal", 0);
		}
		// 输出响应信息
		WFUtil.outPrint(response,jo.toString());
	}
	
	
}
