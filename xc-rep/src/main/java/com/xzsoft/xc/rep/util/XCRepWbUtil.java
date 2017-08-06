package com.xzsoft.xc.rep.util;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xc.util.common.XConstants;
import com.xzsoft.xip.framework.util.AppContext;
import com.xzsoft.xsr.core.service.CellFormulaService;

/**
 * @ClassName: XCRepWbUtil 
 * @Description: 云ＥＲＰ报表模块ＷＢ调用ＪＡＶＡ方法类
 * @author linp
 * @date 2016年7月27日 下午4:22:49 
 *
 */
public class XCRepWbUtil {
	// 日式记录器
	private static final Logger log = LoggerFactory.getLogger(XCRepWbUtil.class);
	 
	/**
	 * @Title: getItemNameById 
	 * @Description: 查询单元对应的指标信息
	 * @param rcItemId
	 * @param suitId
	 * @return
	 * @throws Exception    设定文件
	 */
	public static String getItemNameById(String modalsheetId,String rowItemId,String colItemId,String suitId) throws Exception {
		String rowColItemName = null ;
		String language = XConstants.ZH;
		try {
			language = XCUtil.getLang();
		} catch (Exception e) {
			language = XConstants.ZH;
		}
		try {
			// 查询指标
			CellFormulaService cellFormulaService = AppContext.getApplicationContext().getBean(CellFormulaService.class) ;
			
			HashMap<String,String> map = new HashMap<String,String>() ;
			map.put("suitId", suitId) ;
			map.put("modalsheetId", modalsheetId) ;
			map.put("rowItemId", rowItemId) ;
			map.put("colItemId", colItemId) ;
			
			rowColItemName = cellFormulaService.getRowAndColumnItemName(map) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			if(XConstants.EN.equals(language)){
				rowColItemName = "No Information Of Indicator" ;
			}else{
				rowColItemName = "无指标信息" ;
			}
		}
		
		return rowColItemName ; 
	}

}
