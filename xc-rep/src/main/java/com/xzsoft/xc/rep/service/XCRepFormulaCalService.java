package com.xzsoft.xc.rep.service;

import com.xzsoft.xc.rep.modal.PreFormulaBean;
import com.xzsoft.xc.rep.modal.RepSheetBean;

/**
 * @ClassName: XCRepFormulaCalService 
 * @Description: 公式计算服务接口类
 * @author linp
 * @date 2016年8月10日 下午1:58:46 
 *
 */
public interface XCRepFormulaCalService {

	/**
	 * @Title: doParseRepFormula 
	 * @Description: 运算GetRepValue公式
	 * @param preFormulaBean
	 * @param sheetBean
	 * @return
	 * @throws Exception    设定文件
	 */
	public String doParseRepFormula(PreFormulaBean preFormulaBean,RepSheetBean sheetBean) throws Exception ;
	
	/**
	 * @Title: doParseExpFormula 
	 * @Description: 运算GetExpValue公式
	 * @param preFormulaBean
	 * @param sheetBean
	 * @return
	 * @throws Exception    设定文件
	 */
	public String doParseExpFormula(PreFormulaBean preFormulaBean, RepSheetBean sheetBean) throws Exception ;
	
}
