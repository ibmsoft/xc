package com.xzsoft.xsr.core.webservice.dc;

import org.springframework.context.ApplicationContext;
import com.xzsoft.xip.framework.util.AppContext;


public class XCDateReceive {
	//接受BA或CA
    public String receiveBAOrCA(String dcId, String suitId, String mod , String lastUpdateDate) {
    	//String dcId, String suitId,String mod,String lastUpdateDate
    	ApplicationContext context = AppContext.getApplicationContext();
    	XCReceive xCReceive = (XCReceive) context.getBean("xCReceive");
    	String msg="";
    		try {
				msg=xCReceive.XCBAReceive(dcId,suitId,mod,lastUpdateDate);
			} catch (Exception e) {
				return "<result><flag>1</flag><msg>" + e + "</msg></result>";
			}
        return "<result><flag>0</flag><msg>" + msg + "</msg></result>";
    }
    
  	public String ReceiveDim(String dcId,String suitId) {
  		ApplicationContext context = AppContext.getApplicationContext();
    	XCReceive xCReceive = (XCReceive) context.getBean("xCReceive");
    	String str1 = "";
  		try {
  			str1 = xCReceive.XCDIMReceive(dcId, suitId);
  		} catch (Exception e) {
  			return "<result><flag>1</flag><msg>" + e + "</msg></result>";
  		}
  		return "<result><flag>0</flag><msg>" + str1 + "</msg></result>";
  	}
  	/**
	 *一家一家数据抽取
	 * @param dcId 默认数据中心
	 * @param entityId 公司ID
	 * @param periodId 期间ID
	 * @param suitId   表套ID
	 */
  	public String extractData(String dcId,String entityId,String suitId) {
  		ApplicationContext context = AppContext.getApplicationContext();
    	XCReceive xCReceive = (XCReceive) context.getBean("xCReceive");
    	String str1 = "";
  		try {
  			str1 = xCReceive.extractData(dcId,entityId,suitId);
  		} catch (Exception e) {
  			return "<result><flag>1</flag><msg>" + e + "</msg></result>";
  		}
  		return "<result><flag>0</flag><msg>" + str1 + "</msg></result>";
  	}
}
