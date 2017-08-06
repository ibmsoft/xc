package com.xzsoft.xsr.core.service.impl;

import java.math.BigDecimal;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xzsoft.xsr.core.mapper.SumReportMapper;
import com.xzsoft.xsr.core.service.ProgressBarMsg;
/**
 *
 * ClassName:ProgressBarMsg
 * Function: 处理进度条动态信息提示的功能
 *
 * @author   lsj
 * @version  Ver 1.0
 * @since    Ver 1.0
 * @Date	 2012	2012-4-23		下午4:48:06
 *
 */
@Service
public  class ProgressBarMsgImpl implements ProgressBarMsg {

	private  Logger log = Logger.getLogger(ProgressBarMsgImpl.class.getName());
	@Autowired
	private  SumReportMapper sumReportMapper;
	/**
	 * @param session           :当前的会话session
	 * @param modalSheetId      :模板ID
	 * @param count             :当前处理的模板数目
	 * @param totalCount        :总共需要处理的模板数目
	 * @param flag       		:标志，用于判断条件  sjcjY==数据采集的报表采集   sjcjN==数据采集的表内公式计算和数据回存， bbys==报表运算
	 */
	public  void progressMsgIntoSession(HttpSession session,
			String modalSheetId, int count, int totalCount,String flag, String scrollBarMark) {
		String json = "";
		String modalName="";
			 try {
				modalName = sumReportMapper.getModalName(modalSheetId);
			} catch (Exception e) {
				log.error("查询模板名称出错");
			}
		// 计算进度
		BigDecimal i = new BigDecimal(count);
		BigDecimal t = new BigDecimal(totalCount);
		BigDecimal bd = i.divide(t,4,BigDecimal.ROUND_HALF_UP);
		bd = bd.multiply(new BigDecimal(100)).setScale(1,BigDecimal.ROUND_HALF_UP) ;
		String p = bd.toString().concat("%");
		// 封装进度条提示信息
		if ("sjcjY".equals(flag)) {
			Double.parseDouble(String.valueOf(count/totalCount));
			String msg = " 正在采集第 " + String.valueOf(count)+"/"+String.valueOf(totalCount) + " 张【" + modalName + "】 , 已完成 "+p;
			json = "{'count':"+count+",'totalcount':"+totalCount+",'msg':'"+msg+"'}";

		} else if("sjcjN".equals(flag)){
			String msg = " 正在计算第 "+String.valueOf(count)+"/"+String.valueOf(totalCount) + " 张【" + modalName + "】的表内公式 , 已完成 "+p;
			json = "{'count':"+count+",'totalcount':"+totalCount+",'msg':'"+msg+"'}";

		}else if("bbys".equals(flag)){
			String msg = " 正在运算第 " + String.valueOf(count)+"/"+String.valueOf(totalCount) + " 张【" + modalName + "】 , 已完成 "+p;
			json = "{'count':"+count+",'totalcount':"+totalCount+",'msg':'"+msg+"'}";

		}else if("bbhz".equals(flag)){
			String msg = " 正在汇总第 " + String.valueOf(count)+"/"+String.valueOf(totalCount) + " 张【" + modalName + "】 , 已完成 "+p;
			json = "{'count':"+count+",'totalcount':"+totalCount+",'msg':'"+msg+"'}";

		}else if("ksys".equals(flag)){
			String msg = "取数公式运算完成 , 马上运算报表表内公式 切勿关闭或刷新页面 , 请稍等！";
			json = "{'count':"+count+",'totalcount':"+totalCount+",'msg':'"+msg+"'}";
		}else if("hrchyCheck".equals(flag)) {
			String msg = " 正在检查第 " + String.valueOf(count)+"/"+String.valueOf(totalCount) + " 张【" + modalName + "】 , 已完成 "+p;
			json = "{'count':"+count+",'totalcount':"+totalCount+",'msg':'"+msg+"'}";
		}
		//session.setAttribute(session.getId(), json);
		session.setAttribute(scrollBarMark, json);
	}
}
