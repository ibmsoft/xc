/**
 * 
 */
package com.xzsoft.xc.rep.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.xzsoft.xc.rep.dao.XCReportModuleDao;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xc.util.common.XConstants;
/**
 * @author tangxl
 * @describe：报表单元格公式运行工具类
 */
public class XcRepFormulaExcuteUtil {
	/**
	 * 
	 * @methodName  getCellArithmetic
	 * @author      tangxl
	 * @date        2016年9月6日
	 * @describe    解析单元格公式
	 * @param       map
	 * @param       xCReportModuleDao
	 * @return
	 * @throws      Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public static String getCellArithmetic(HashMap<String, String> map,XCReportModuleDao xCReportModuleDao) throws Exception{
		String arr[]=new String[0];
		String formulaText = map.get("formulaText");
		String repFormulas[]=FormulaTranslate.getRepFormula(formulaText+" ", arr);
		String repFormula="";
		String repValue="0";
		String expFormulas[]=FormulaTranslate.getExpFormula(formulaText+" ", arr);
		String expFormula="";
		String expValue="0";
		//解析指标间取数公式
		for(int i=0;i<repFormulas.length;i++){
			repFormula=repFormulas[i];
			repValue=getRepValue(repFormula,map,xCReportModuleDao);
			repValue=repValue==null?"(0)":"("+repValue+")";
			formulaText=formulaText.replace(repFormula, repValue);
		}
		//解析自定义取数函数
		for(int i=0;i<expFormulas.length;i++){
			expFormula=expFormulas[i];
			expValue=getExpValue(expFormula,map,xCReportModuleDao);
			expValue=expValue==null?"(0)":"("+expValue+")";
			formulaText=formulaText.replace(expFormula, expValue);
		}
		String equation=formulaText;
		return equation;
	}
	/**
	 * 
	 * @methodName  方法名
	 * @author      tangxl
	 * @date        2016年9月6日
	 * @describe    报表单元格函数解析
	 * @param       repFormula
	 * @param 		map
	 * @return
	 * @throws 		Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public static String getRepValue(String repFormula,HashMap<String, String> conditionMap,XCReportModuleDao xCReportModuleDao) throws Exception{
		Map<String,String> map = FormulaTranslate.getAllCodes(repFormula);
		String periodCodeTest = conditionMap.get("periodCode");
		String periodCode=null;
	    /*1、conditionMap.get("periodCode") 是公式测试时传递的期间
	     *2、map.get("periodCode") 是运算公式本身所包含的期间编码 
	     *3、如果公式里的期间是 相对值（比如 当前期间 、上月期间等），此时根据测试所选择的期间按照相对值去计算出新的期间
		 *4、如果公式里的期间是 具体指 （比如 2015-07），此时直接使用该期间，跟测试所选择的期间无关
		 *5、账簿已经绑定组织，并且报表只考虑【本位币】报表，因此不再对【组织】和【币种】做判断
		 */
		if(map.get("periodCode").length()<=5){
			periodCode=FormulaTranslate.getPeriodCode(periodCodeTest, periodCodeTest, map.get("periodCode"));
		}else{
			periodCode=map.get("periodCode");
		}				
		map.put("periodCode", periodCode);
		map.put("ledgerId",conditionMap.get("ledgerId"));
		String repValue=xCReportModuleDao.getRepValue(map);
		return repValue;
     }
	/**
	 * 
	 * @methodName  getExpValue
	 * @author      tangxl
	 * @date        2016年9月6日
	 * @describe    自定义函数解析
	 * @param 		repFormula
	 * @param 		conditionMap
	 * @param       xCReportModuleDao
	 * @return
	 * @throws 		Exception
	 * @变动说明       
	 * @version     1.0
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String getExpValue(String repFormula,HashMap<String,String> conditionMap,XCReportModuleDao xCReportModuleDao) throws Exception{
		Map<String,Object> map=FormulaTranslate.getAllParam(repFormula);
		String repValue="0";
		String language = XConstants.ZH;
		try {
			language = XCUtil.getLang();
		} catch (Exception e) {
			language = XConstants.ZH;
		}
		if(map!=null){			
			String funcCode=map.get("funCode").toString();
			HashMap<String,String> funMap=xCReportModuleDao.getCellFunction(funcCode);
			String func=funMap.get("FUNC").toString().trim();
			String funcType=funMap.get("FUNC_TYPE").toString();
			if(funcType.equals("JAVA")){
				String clzName=func.substring(0, func.lastIndexOf("."));
				String methodName=func.substring(func.lastIndexOf(".")+1, func.length());
				if(map.containsKey("paramMap")){
					Map paramMap=(HashMap)map.get("paramMap");
					/*如果自定义函数中包含 【公司】、【期间】、【币种】时，优先以界面输入的公司 期间 币种为准
					 */
					if(!paramMap.containsKey("DW")||"XZDW".equals(paramMap.get("DW"))){
						paramMap.put("entity_id", conditionMap.get("orgId"));
						paramMap.remove("DW");
					}
					if(paramMap.containsKey("QJ")){
						String periodCodeTest= conditionMap.get("periodCode");
						String periodCode=null;
						if(paramMap.get("QJ").toString().length()<=5){
							periodCode=FormulaTranslate.getPeriodCode(periodCodeTest, periodCodeTest, paramMap.get("QJ").toString());
						}else{
							periodCode=paramMap.get("QJ").toString();
						}				
						paramMap.put("periodCode", periodCode);
					}
					paramMap.put("ledgerId", conditionMap.get("ledgerId")) ;
					if(!paramMap.containsKey("BZ")){
						paramMap.put("cnyId", conditionMap.get("cnyCode"));
						paramMap.remove("BZ");
					}
					/*反射调用说明：
					 *方法的参数必须为 Map<String,Object>
					 *默认提供了 ledgerId，与账簿相关的数据，可自行在方法中通过ledgerId查找 
					 */
					repValue=executeJavaMethod(clzName,methodName,paramMap);
				}else{
					if(XConstants.ZH.equals(language)){
						repValue="没有输入参数！";
					}else{
						repValue="Please input parameter!";
					}
				}				
			}else if(funcType.equals("DB")){
				String funName=func;
				if(map.containsKey("params")){
					String params=map.get("params").toString();
					params=params+","+conditionMap.get("orgId")+","+conditionMap.get("periodCode");
					Map<String,String> paramMap=new LinkedHashMap<String,String>();
					paramMap.put("funName", funName);
					paramMap.put("params", params);
					/*存储过程输出参数名称 o_result
					 *除了界面定义的参数外，将测试时选定的 orgId和periodCode传递到存储过程中 
					 */
					repValue=xCReportModuleDao.excuteProcedure(paramMap);
				}else{
					if(XConstants.ZH.equals(language)){
						repValue="没有输入参数！";
					}else{
						repValue="Please input parameter!";
					}
				}	

			}				
		}		
		return repValue;
	}
	/**
	 * 
	 * @methodName  executeJavaMethod
	 * @author      tangxl
	 * @date        2016年9月6日
	 * @describe    反射执行java方法
	 * @param       clzName
	 * @param       methodName
	 * @param       paramMap
	 * @return
	 * @throws Exception
	 * @变动说明       
	 * @version     1.0
	 */
	@SuppressWarnings("rawtypes")
	public static String executeJavaMethod(String clzName,String methodName,Map paramMap) throws Exception{
		Class<?> cls = Class.forName(clzName);  
		Method setMethod = cls.getDeclaredMethod(methodName,Map.class);  
		String returnValue = (String)setMethod.invoke(cls.newInstance(), paramMap);
		return returnValue;
	}
}
