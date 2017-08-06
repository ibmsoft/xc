package com.xzsoft.xc.rep.service;

import java.util.HashMap;

/**
 * @className XCRepFormulaManageService
 * @describe  报表公式处理接口
 * @author    tangxl
 * @date      2016-08-04
 */
public interface XCRepFormulaManageService {
	/**
	 * 
	 * @methodName  saveCellFormula
	 * @author      tangxl
	 * @date        2016年8月4日
	 * @describe    保存报表公司设置
	 * @param       map
	 * @throws 		Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public void saveCellFormula(HashMap<String, String> map) throws Exception;
}
