package com.xzsoft.xsr.core.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.xzsoft.xsr.core.service.ProgressBarMsg;
import com.xzsoft.xsr.core.service.SumReportService;

@Controller
@RequestMapping("/sumReportController.do")
public class SumReportController {
	private static Logger log = Logger.getLogger(CheckFormulaController.class.getName());
	@Autowired
	SumReportService sumReportService;
	@Autowired
	ProgressBarMsg progressBarMsg;

	/**
	 * 报表汇总
	 */
	@RequestMapping(params = "method=SumReport", method = RequestMethod.POST)
	public void SumReport(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		String userId = (String) request.getSession().getAttribute("userId");
		String suitId = (String) request.getSession().getAttribute("suitId");
		String periodId = request.getParameter("period"); // 期间ID
		String cnyId = request.getParameter("currency"); // 币种ID
		String hrchyId = request.getParameter("hrchyId"); // 组织ID
		String entityId = request.getParameter("entityId"); // 母公司ID
		String mdltypeId = request.getParameter("mdltypeId"); // 模板集ID
		String[] mdlshtlist = request.getParameter("mdlshtIdlist").split(","); // 模板列表
		String corpIdList = request.getParameter("corpIdList"); // 汇总单位范围
		String adjId = request.getParameter("adjId");// 调整单位
		String isOpFormula = request.getParameter("isOpFormulaValue");// 判断该汇总公司是否有汇总权限
		String scrollBarMark = request.getParameter("scrollBarMark"); // 滚动条
		String json = "";
		StringBuffer flag = new StringBuffer();
		// 前端判断该用户或职责是否对该汇总公司有权限
		// 循环汇总各模版
		if (mdlshtlist.length > 0) {
			for (int i = 0; i < mdlshtlist.length; i++) {
				String mdlshtId = mdlshtlist[i];
				// 记录报表汇总进度条信息
				progressBarMsg.progressMsgIntoSession(session, mdlshtId, i + 1, mdlshtlist.length, "bbhz",
						scrollBarMark);
				log.warn("功能名称：报表汇总--数据汇总，" + " 操作用户：" + userId + " 汇总模板：" + mdlshtId + " 期间ID:" + periodId + " 执行时间："
						+ DateFormat.getDateTimeInstance().format(new Date()));
				try {
					String msg = sumReportService.SumReport(userId, suitId, hrchyId, entityId, periodId, mdltypeId,
							mdlshtId, cnyId, corpIdList, adjId);
					flag.append(msg);
					if (msg.toString().equals("N")) {
						log.info("失败：模版" + mdlshtId + "已锁定");
					} else {
						log.info("成功：" + mdlshtId);
					}
				} catch (Exception e) {
					log.info("失败：" + mdlshtId);
				}
			}
			if (!flag.toString().equals("") || flag.toString() == "") {
				json = "{flag:0,msg:'数据汇总成功！<br/><font color=\"red\">注意：</font><br/>所选报表中存在“已锁定”状态报表，不进行汇总；<br/>如需汇总，请将该状态报表进行解锁，再汇总！'}";
			} else {
				json = "{flag:0,msg:'数据汇总成功！'}";
			}
		}
		// 暂时做不了
		if ("1".equals(isOpFormula)) {
			// === 执行表内公式运算
		}
		// 输出JSON
		response.setContentType("text/html;charset=utf-8");
		try {
			response.getWriter().write(json);
			response.getWriter().close();
		} catch (IOException e) {
			log.error("传输json串出错", e);
		}

	}
}
