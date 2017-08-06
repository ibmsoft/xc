package com.xzsoft.xc.rep.service;

import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.xzsoft.xc.rep.modal.CalFormulaBean;
import com.xzsoft.xc.rep.modal.ESTabBean;
import com.xzsoft.xc.rep.modal.PreFormulaBean;
import com.xzsoft.xc.rep.modal.RepSheetBean;
import com.xzsoft.xip.framework.util.AppContext;

/**
 * @ClassName: AbstractRepDataService 
 * @Description: 报表数据处理服务抽象类
 * <p>
 * 	处理报表基础信息和报表固定行指标数据，并提供相应的公共处理方法
 * </p>
 * @author linp
 * @date 2016年8月5日 上午9:43:30 
 *
 */
public abstract class AbstractRepDataHandlerService {

	/**
	 * @Title: handlerRepSheet 
	 * @Description: 处理报表基础信息
	 * @param sheetBean
	 * @param tabBean
	 * @throws Exception    设定文件
	 */
	public abstract void handlerRepSheet(RepSheetBean sheetBean,ESTabBean tabBean) throws Exception ;

	/**
	 * @Title: handlerFixedCellValue 
	 * @Description: 处理报表固定行指标值信息
	 * @param map
	 * @throws Exception    设定文件
	 */
	public abstract void handlerFixedCellValue(RepSheetBean sheetBean,ESTabBean tabBean) throws Exception ;
	
	/**
	 * @Title: getArithmeticByFormula 
	 * @Description: 公式计算处理
	 * @param formula
	 * @param sheetBean
	 * @return
	 * @throws Exception    设定文件
	 */
	public String getArithmeticByFormula(CalFormulaBean formula, RepSheetBean sheetBean)throws Exception {
		// 公式串
		String dataFormula = formula.getFormula() ;

		try {
			// 公式计算服务类
			XCRepFormulaCalService calService = AppContext.getApplicationContext().getBean(XCRepFormulaCalService.class) ;
			
			// 公式预处理信息
			List<PreFormulaBean> preFormulaBeans = formula.getPreFormulaBeans() ;
			if(preFormulaBeans != null && preFormulaBeans.size() >0){
				
				for(PreFormulaBean preFormulaBean : preFormulaBeans){
					// 公式子串
					String formulaStr = preFormulaBean.getFormulaStr() ;
					// 公式计算
					String result = null ;
					if(formulaStr.contains("GetRepVal")){ 
						// 指标间取数处理
						result = "("+calService.doParseRepFormula(preFormulaBean, sheetBean)+")" ;
						
					}else if(formulaStr.contains("GetExpVal")){
						// 科目余额取数处理
						result = "("+calService.doParseExpFormula(preFormulaBean, sheetBean)+")" ;
						
					}else{
						result = "(0)" ;
					}
					dataFormula = dataFormula.replace(formulaStr, result) ;
				}
			}else{
				dataFormula += "" ;
			}
			
		} catch (Exception e) {
			throw e ;
		}
		
		return dataFormula ;
	}
	
	/**
	 * @Title: evalFormula 
	 * @Description: 四则运算表达式 
	 * @param equation
	 * @return    设定文件
	 */
	public String evalFormula(String equation){
		String result = "";
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("javascript");
		try {
			Object obj = engine.eval(equation);
			result=obj.toString();
		} catch (ScriptException e) {
			result="0";
		}
		return result;
	}
	
}
