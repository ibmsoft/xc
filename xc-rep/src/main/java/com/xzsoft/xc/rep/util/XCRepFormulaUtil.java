package com.xzsoft.xc.rep.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.xzsoft.xc.rep.dao.XCRepFormulaManageDao;
import com.xzsoft.xc.rep.modal.CellFormula;
import com.xzsoft.xc.rep.modal.CellPreFormula;
import com.xzsoft.xc.rep.modal.DataDTO;
import com.xzsoft.xc.rep.modal.ReportSheetBean;
import com.xzsoft.xc.rep.modal.ReportTabCellBean;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xc.util.common.XConstants;
/**
 * @describe 公式处理工具类
 * @author tangxl
 */
public class XCRepFormulaUtil {
	/**
	 * 
	 * @methodName  formulaTranslate
	 * @author      tangxl
	 * @date        2016年8月4日
	 * @describe    公式翻译
	 * @param       formulaText  --- 待翻译的公式
	 * @param       accHrcyId    --- 科目体系id          
	 * @param       dbType       --- 数据库类型
	 * @param       xCRepFormulaManageDao  --- 数据查询类
	 * @return
	 * @throws      Exception
	 * @变动说明      	去掉表套suitId体系，报表与会计科目直接绑定
	 * @version     1.0
	 */
	public static String formulaTranslate(String ledgerId,String formulaText,String accHrcyId,String dbType,XCRepFormulaManageDao xCRepFormulaManageDao) throws Exception {
		//事先将formulaText分解成好几个单个的公式项数组，比如()+(),分解成[(),()]
		if(formulaText.contains("GetRepVal")){
			//翻译所有的GetRepVal()公式
			String formulasTmp[]=new String[0];
			//取出公式串中的所有GetRepVal()公式
			String formulas[]=FormulaTranslate.getRepFormula(formulaText+" ", formulasTmp);
			String formula="";
			for(int i=0;i<formulas.length;i++){
				formula=formulas[i];
				formulaText=formulaText.replace(formula, repFormulaTranslate(formula, accHrcyId,dbType,xCRepFormulaManageDao));
			}
		}
		
		if(formulaText.contains("GetExpVal")){
			//formulaText = formulaText.replaceAll("GetExpVal", "自定义取数");
			String ExpformulaArry[]=new String[0];
			String formulas[]=FormulaTranslate.getExpFormula(formulaText+"", ExpformulaArry);
			String formula="";
			for(int i=0;i<formulas.length;i++){
				formula=formulas[i];
				formulaText=formulaText.replace(formula, expFormulaTranslate(ledgerId,formula, accHrcyId,dbType,xCRepFormulaManageDao));
			}
		}
		return formulaText;
	} 
	/**
	 * 
	 * @methodName  repFormulaTranslate
	 * @author      tangxl
	 * @date        2016年8月4日
	 * @describe    自定义公式翻译
	 * <p>自定义公式只翻译java类型的公式：getGLBalValue 和  getGLCashValue</p>
	 * @param       formula
	 * @param       accHrcyId          -- 科目体系ID
	 * @param       dbType
	 * @param       xCRepFormulaManageDao
	 * @return
	 * @throws      Exception
	 * @变动说明       
	 * @version     1.0
	 */
	@SuppressWarnings("unchecked")
	public static String expFormulaTranslate(String ledgerId,String formula,String accHrcyId,String dbType,XCRepFormulaManageDao xCRepFormulaManageDao) throws Exception {
		String language = XConstants.ZH;
		try {
			language = XCUtil.getLang();
		} catch (Exception e) {
			language = XConstants.ZH;
		}
		//获取自定义函数的所有参数
		Map<String,Object> map=FormulaTranslate.getAllParam(formula);
		/*
		 *map结构：funcode：函数编码
		 *       paramMap：参数名和值得键值对
		 */
		if(map != null){
			String funCode = map.get("funCode").toString();
			Map<String, String> paramsMap = (Map<String, String>) map.get("paramMap");
			Iterator<Entry<String, String>> it = paramsMap.entrySet().iterator();
			String paramsSql = "";
			String tmpName = "";
			if("getGLBalValue".equalsIgnoreCase(funCode)){
				if(XConstants.ZH.equalsIgnoreCase(language)){
					formula = formula.replace(funCode, "云ERP科目余额取数函数");
				}
				
				//替换参数函数参数名以及参数值对应的名称
				while(it.hasNext()){
					Entry<String, String> entry = it.next();
					String key = entry.getKey();
					String value = entry.getValue();
					switch (key) {
					case "BAL_TYPE":
						paramsSql = "SELECT t.VAL_CODE CODE, t.VAL_NAME NAME FROM xip_pub_val_set_dtl t WHERE t.VAL_SET_CODE = 'XC_REP_BAL_TYPE' AND t.VAL_CODE = '"+value.trim()+"'";
						break;
					case "XC_GL_ACCOUNTS":
						paramsSql = "SELECT ga.ACC_CODE CODE,ga.ACC_NAME NAME FROM xc_gl_accounts ga where ga.ACC_HRCY_ID = '"+accHrcyId.trim()+"' AND ga.ACC_CODE='"+value.trim()+"'";
						break;
					case "XC_AP_VENDORS":
						paramsSql = "SELECT t.VENDOR_CODE CODE,t.VENDOR_NAME NAME FROM xc_ap_vendors t WHERE t.VENDOR_CODE = '"+value.trim()+"'";
						break;
					case "XC_AR_CUSTOMERS":
						paramsSql = "SELECT t.CUSTOMER_CODE CODE,t.CUSTOMER_NAME NAME FROM xc_ar_customers t WHERE t.CUSTOMER_CODE = '"+value.trim()+"'";
						break;
					case "XIP_PUB_ORGS":
						paramsSql = "SELECT t.ORG_CODE CODE,t.ORG_NAME NAME FROM xip_pub_orgs t WHERE t.ORG_CODE ='"+value.trim()+"'";
						break;
					case "XIP_PUB_EMPS":
						paramsSql = "SELECT T.EMP_CODE CODE,T.EMP_NAME NAME FROM xip_pub_emps T WHERE T.EMP_CODE = '"+value.trim()+"'";
						break;
					case "XC_GL_PRODUCTS":
						paramsSql = "SELECT T.PRODUCT_CODE CODE,T.PRODUCT_NAME NAME FROM xc_gl_products T WHERE T.PRODUCT_CODE ='"+value.trim()+"'";
						break;
					case "XC_PM_PROJECTS":
						paramsSql = "SELECT  DISTINCT T.PROJECT_CODE CODE,T.PROJECT_NAME NAME FROM xc_pm_projects T WHERE T.PROJECT_CODE ='"+value.trim()+"' AND T.LEDGER_ID = '"+ledgerId+"'";
						break;
					case "XIP_PUB_DEPTS":
						paramsSql = "SELECT T.DEPT_CODE CODE,T.DEPT_NAME NAME FROM xip_pub_depts T WHERE T.DEPT_CODE = '"+value.trim()+"'";
						break;
					case "XC_GL_CUSTOM1":
						paramsSql = "SELECT T.LD_CUST_SEGVAL_CODE CODE,T.LD_CUST_SEGVAL_NAME NAME  FROM xc_gl_ld_cust_ass_segvals T  where T.LEDGER_ID = '"+ledgerId.trim()+"' AND T.SEG_CODE = 'XC_GL_CUSTOM1'  AND T.LD_CUST_SEGVAL_CODE='"+value.trim()+"'";
						break;
					case "XC_GL_CUSTOM2":
						paramsSql = "SELECT T.LD_CUST_SEGVAL_CODE CODE,T.LD_CUST_SEGVAL_NAME NAME  FROM xc_gl_ld_cust_ass_segvals T  where T.LEDGER_ID = '"+ledgerId.trim()+"' AND T.SEG_CODE = 'XC_GL_CUSTOM2'  AND T.LD_CUST_SEGVAL_CODE='"+value.trim()+"'";
						break;
					case "XC_GL_CUSTOM3":
						paramsSql = "SELECT T.LD_CUST_SEGVAL_CODE CODE,T.LD_CUST_SEGVAL_NAME NAME  FROM xc_gl_ld_cust_ass_segvals T  where T.LEDGER_ID = '"+ledgerId.trim()+"' AND T.SEG_CODE = 'XC_GL_CUSTOM3'  AND T.LD_CUST_SEGVAL_CODE='"+value.trim()+"'";
						break;
					case "XC_GL_CUSTOM4":
						paramsSql = "SELECT T.LD_CUST_SEGVAL_CODE CODE,T.LD_CUST_SEGVAL_NAME NAME  FROM xc_gl_ld_cust_ass_segvals T  where T.LEDGER_ID = '"+ledgerId.trim()+"' AND T.SEG_CODE = 'XC_GL_CUSTOM4'  AND T.LD_CUST_SEGVAL_CODE='"+value.trim()+"'";
						break;
					default:
						paramsSql = "";
						break;
					}
					//查询参数名值对应的名称
					if(StringUtils.isNotEmpty(paramsSql)){
						HashMap<String, String> tmpMap = xCRepFormulaManageDao.queryTranslateName(paramsSql);
						formula = formula.replace(tmpMap.get("CODE"), tmpMap.get("NAME"));
					}
				}
			}else if("getGLCashValue".equalsIgnoreCase(funCode)){
				formula = formula.replace(funCode, "云ERP现金流量取数函数");
				//替换参数函数参数名以及参数值对应的名称
				while (it.hasNext()) {
					Entry<String, String> entry = it.next();
					String key = entry.getKey();
					String value = entry.getValue();
					switch (key) {
					case "XC_GL_CASH_ITEMS":
						if(XConstants.ZH.equalsIgnoreCase(language)){
							tmpName = "现金流量项目";
						}else{
							tmpName = "Cash Flow Item";
						}
						paramsSql = "select t.CA_CODE CODE,t.CA_NAME NAME from xc_gl_cash_items t WHERE t.CA_CODE =  '"+value.trim()+"'";
						break;
					case "CA_TYPE":
						if(XConstants.ZH.equalsIgnoreCase(language)){
							tmpName = "现金流量汇总类型";
						}else{
							tmpName = "Cash Flow Category";
						}
						paramsSql = "select t.VAL_CODE CODE,t.VAL_NAME NAME from xip_pub_val_set_dtl t where t.VAL_SET_CODE = 'XC_REP_CA_TYPE' AND t.VAL_CODE = '"+value.trim()+"'";
						break;
					default:
						paramsSql = "";
						break;
					}
					formula = formula.replace(key, tmpName);
					//查询参数名值对应的名称
					if(StringUtils.isNotEmpty(paramsSql)){
						HashMap<String, String> tmpMap = xCRepFormulaManageDao.queryTranslateName(paramsSql);
						formula = formula.replace(tmpMap.get("CODE"), tmpMap.get("NAME"));
					}
				}
			}
		}
		String temp=formula;
		if(XConstants.ZH.equalsIgnoreCase(language)){
			temp = temp.replace("GetExpVal", "自定义取数:");
		}else{
			temp = temp.replace("GetExpVal", "Self-define Formula:");
		}
		temp = temp.replace("(", "【").replace(")", "】").replaceAll("\\|", "");
		return temp;
	}
	/**
	 * 
	 * @methodName  repFormulaTranslate
	 * @author      tangxl
	 * @date        2016年8月4日
	 * @describe    标间公式翻译
	 * @param       formula
	 * @param       accHrcyId          -- 科目体系ID
	 * @param       dbType
	 * @param       xCRepFormulaManageDao
	 * @return
	 * @throws      Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public static String repFormulaTranslate(String formula,String accHrcyId,String dbType,XCRepFormulaManageDao xCRepFormulaManageDao) throws Exception {
		//获取科目、行指标和列指标code
		Map<String,String> map=FormulaTranslate.getAllCodes(formula);
		String language = XConstants.ZH;
		try {
			language = XCUtil.getLang();
		} catch (Exception e) {
			language = XConstants.ZH;
		}
		List<DataDTO> list=new ArrayList<DataDTO>();
		//期间
		DataDTO periodDto = xCRepFormulaManageDao.getPeriodByCode(accHrcyId, map.get("periodCode")) ;
		if(periodDto != null){
			list.add(periodDto);
		}
		//单位
		if(map.containsKey("entityCode")){
			if(!map.get("entityCode").equals("XZDW")){
				DataDTO ledgerDto = xCRepFormulaManageDao.getLedgerByCode(map.get("entityCode")) ;
				list.add(ledgerDto);
			}else{
				if(XConstants.ZH.equalsIgnoreCase(language)){
					formula.replace("XZDW", "当前报表账簿");
				}else{
					formula.replace("XZDW", "Current Report Ledger");
				}
			}				
		}
		// 行列指标
		if(map.get("colCode").equals("<$$>")||map.get("rowCode").equals("<$$>")){
			list.add(new DataDTO("<$$>","|<$$>|"));
		}else{
			DataDTO rowDto = xCRepFormulaManageDao.getRowByCode(accHrcyId,map.get("rowCode"),dbType) ;
			DataDTO colDto = xCRepFormulaManageDao.getColByCode(accHrcyId,map.get("colCode"),dbType) ;
			
			if(rowDto != null)list.add(rowDto);
			if(colDto != null)list.add(colDto);
		}
		// 币种
		if(map.containsKey("cnyCode")){
			DataDTO cnyDto = xCRepFormulaManageDao.getCnyByCode(accHrcyId,map.get("cnyCode")) ;
			if(cnyDto != null) list.add(cnyDto) ;
		}
		//创正则表达式的匹配串
		Object[] object	= FormulaTranslate.patternArray(list);

		String temp=formula;
		//如果公式为自定义取数公式，则不做翻译
		if(temp.indexOf("GetExpVal")>0){
			if(XConstants.ZH.equalsIgnoreCase(language)){
				temp = temp.replace("GetExpVal", "自定义取数:");
			}else{
				temp = temp.replace("GetExpVal", "Self-define Formula:");
			}
		}else{
			for(int j=0;j<object.length;j++){
				String[] result=(String[])object[j];
				if(result[0] != null){
					Pattern pattern = Pattern.compile(result[0]);
					Matcher matcher = pattern.matcher(temp);
					temp=matcher.replaceAll(result[1]);
				}
			}
		}
		temp = temp.replace("(", "【").replace(")", "】").replaceAll("\\|", "");
		return temp;
	}
	/**
	 * 
	 * @methodName  getRepFormula
	 * @author      tangxl
	 * @date        2016年8月5日
	 * @describe    获取指标取数公式内容
	 * @param       oldStr
	 * @param 		formulas
	 * @return
	 * @变动说明       
	 * @version     1.0
	 */
	public static  String[] getRepFormula(String oldStr,String[] formulas){		
		String formula="";
		int i=formulas.length;
		if(oldStr.contains("GetRepVal")){	
			oldStr=oldStr.substring(oldStr.indexOf("GetRepVal"), oldStr.length());
			formula=oldStr.substring(oldStr.indexOf("GetRepVal"), oldStr.indexOf(")")+1);
			formulas=(String[]) ArrayUtils.add(formulas,formula);
			oldStr=oldStr.substring(formulas[i].length(), oldStr.length());
			formulas=getRepFormula(oldStr,formulas);
		}
		return formulas;
	}
	/**
	 * 
	 * @methodName  getExpFormula
	 * @author      tangxl
	 * @date        2016年8月5日
	 * @describe    获取自定义指标取数内容
	 * @param 		oldStr
	 * @param 		formulas
	 * @return
	 * @变动说明       
	 * @version     1.0
	 */
	public static  String[] getExpFormula(String oldStr,String[] formulas){		
		String formula="";
		int i=formulas.length;
		if(oldStr.contains("GetExpVal")){	
			oldStr=oldStr.substring(oldStr.indexOf("GetExpVal"), oldStr.length());
			formula=oldStr.substring(oldStr.indexOf("GetExpVal"), oldStr.indexOf(")")+1);
			formulas=(String[]) ArrayUtils.add(formulas,formula);
			oldStr=oldStr.substring(formulas[i].length(), oldStr.length());
			formulas=getExpFormula(oldStr,formulas);
		}
		return formulas;
	}
	/**
	 * 
	 * @methodName  evalFormula
	 * @author      tangxl
	 * @date        2016年8月5日
	 * @describe    公式解析
	 * @param 		equation
	 * @return
	 * @变动说明       
	 * @version     1.0
	 */
	public static String evalFormula(String equation){
		String result="";
		String language = XConstants.ZH;
		try {
			language = XCUtil.getLang();
		} catch (Exception e) {
			language = XConstants.ZH;
		}
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("javascript");
		try {
			Object obj = engine.eval(equation);
			result=obj.toString();
		} catch (ScriptException e) {
			if(XConstants.EN.equalsIgnoreCase(language)){
				result="Formula Error!";
			}else{
				result="公式有误!";
			}
		}
		return result;
	}
	
	public static String[] spiltStrBySpace(String oldStr){
		Pattern pattern = Pattern.compile("[,\"\"]");
		String[] strs = pattern.split(oldStr);
		return strs;
	}
	
	public static boolean periodStart(String oldStr){
		Pattern pattern = Pattern.compile("^\\|QJ\\|=\\|.*\\|");
		Matcher matcher = pattern.matcher(oldStr);
		boolean b= matcher.matches();
		return b;
	}
	
	public static boolean entityStart(String oldStr){
		Pattern pattern = Pattern.compile("^\\|DW\\|=\\|.*\\|");
		Matcher matcher = pattern.matcher(oldStr);
		boolean b= matcher.matches();
		return b;
	}
	
	public static boolean kmStart(String oldStr){
		Pattern pattern = Pattern.compile("^\\|KM\\|=\\|.*\\|");
		Matcher matcher = pattern.matcher(oldStr);
		boolean b= matcher.matches();
		return b;
	}
	
	public static boolean bzStart(String oldStr){
		Pattern pattern = Pattern.compile("^\\|BZ\\|=\\|.*\\|");
		Matcher matcher = pattern.matcher(oldStr);
		boolean b= matcher.matches();
		return b;
	}

	public static boolean rowStart(String oldStr){
		Pattern pattern = Pattern.compile("^\\|ROW\\|=\\|.*\\|");
		Matcher matcher = pattern.matcher(oldStr);
		boolean b= matcher.matches();
		return b;
	}
	
	public static boolean colStart(String oldStr){
		Pattern pattern = Pattern.compile("^\\|COL\\|=\\|.*\\|");
		Matcher matcher = pattern.matcher(oldStr);
		boolean b= matcher.matches();
		return b;
	}
	
	public static Map<String,String> getAllCodes(String oldStr){
		Map<String,String> map=new HashMap<String,String>();

		String[] s = spiltStrBySpace(oldStr);

		for(int i=0;i<s.length;i++){
			if(periodStart(s[i])){
				String period=s[i].substring(6, s[i].length()-1);
				if(s[i].contains(":")){
					period=s[i].split(":")[0];
					period=period.substring(6, period.length());
					map.put("PYQJ", s[i].split(":")[1]);
				}
				map.put("periodCode", period);
			}
			if(entityStart(s[i])){
				map.put("entityCode", s[i].substring(6, s[i].length()-1));
			}			
			if(rowStart(s[i])){
				map.put("rowCode", s[i].substring(7, s[i].length()-1));
			}
			if(colStart(s[i])){
				map.put("colCode", s[i].substring(7, s[i].length()-1));
			}
			if(bzStart(s[i])){
				map.put("cnyCode", s[i].substring(6, s[i].length()-1));
			}
		}
		return map;
	}
	/**
	 * 
	 * @methodName  getAllParam
	 * @author      tangxl
	 * @date        2016年8月5日
	 * @describe    获取公式中参数
	 * @param 		oldStr
	 * @return
	 * @变动说明       
	 * @version     1.0
	 */
	public static Map<String,Object> getAllParam(String oldStr){
		if(oldStr.contains("GetExpVal")){
			Map<String,Object> map=new HashMap<String,Object>();
			String famulaArr[]=oldStr.split(">>");
			String funText=famulaArr[0];//GetExpVal("FunCode
			String funCode=funText.substring(funText.indexOf("(\"")+2,funText.length());//FunCode
			map.put("funCode", funCode);

			String paramText=famulaArr[1];//param1=AAA,param2=BBB,param3=CCC")
			paramText=paramText.substring(0, paramText.indexOf("\")"));//param1=AAA,param2=BBB,param3=CCC
			String paramTextArr[]=paramText.split(",");
			String paramArr[]=new String[paramTextArr.length];//自定义取数函数java参数
			Map<String,String> paramMap=new LinkedHashMap<String,String>();//自定义取数函数存储过程参数
			String params="";
			String arr[]=null;
			String paramCode;
			String param;
			for(int i=0;i<paramTextArr.length;i++){
				arr=paramTextArr[i].split("=");				
				paramCode=arr[0];//param1
				if(arr.length==1){
					param=" ";
				}else{
					param=arr[1];//AAA
				}			
				paramArr[i]=param;
				paramMap.put(paramCode, param);
				params+=","+param;
			}
			map.put("paramArr", paramArr);
			map.put("paramMap", paramMap);
			map.put("params", params);
			return map;
		}else{
			return null;
		}

	}
	/**
	 * 
	 * @methodName  getInsertFormulaData
	 * @author      tangxl
	 * @date        2016年8月5日
	 * @describe    模板公式预处理
	 * @param 		insertList
	 * @param 		updateList
	 * @param 		map
	 * @return
	 * @throws Exception
	 * @变动说明       
	 * @version     1.0
	 */
	@SuppressWarnings("unchecked")
	public static List<CellPreFormula> getInsertFormulaData(List<CellFormula> insertList,List<CellFormula> updateList,HashMap<String, String> map) throws Exception{
		String formulaCat = map.get("formulaCat");
		List<CellPreFormula> newList = new ArrayList<CellPreFormula>();
		String formulaContent = "";
		String arr[]=new String[0];
		if(insertList !=null && insertList.size()>0){
			for(CellFormula t:insertList){
				formulaContent = t.getFormula();
				String repFormulas[] = getRepFormula(formulaContent+" ", arr);
				String expFormulas[] = getExpFormula(formulaContent+" ", arr);
				int i = 1;
				//指标取数公式
				for(String str : repFormulas){
					CellPreFormula cp = new CellPreFormula();
					cp.setPreFormulaId(UUID.randomUUID().toString());
					cp.setFormulaCat(formulaCat);
					cp.setFormulaId(t.getFormulaId());
					cp.setFormulaStr(str);
					JSONObject object = JSONObject.fromObject(getAllCodes(str));
					cp.setFormulaJson(object.toString());
					cp.setOrderBy(i);
					i++;
					cp.setCreatedBy(map.get("userId"));
					cp.setLastUpdatedBy(map.get("userId"));
					cp.setCreationDate(new Date());
					cp.setLastUpdateDate(new Date());
					cp.setFormulaType("REP");
					newList.add(cp);
				}
				//自定义取数公式
				for(String exp : expFormulas){
					CellPreFormula cp = new CellPreFormula();
					cp.setPreFormulaId(UUID.randomUUID().toString());
					cp.setFormulaCat(formulaCat);
					cp.setFormulaId(t.getFormulaId());
					cp.setFormulaStr(exp);
					Map<String,String> paramMap = (Map<String, String>) getAllParam(exp).get("paramMap");
					JSONObject object = JSONObject.fromObject(paramMap);
					cp.setFormulaJson(object.toString());
					cp.setOrderBy(i);
					i++;
					cp.setCreatedBy(map.get("userId"));
					cp.setLastUpdatedBy(map.get("userId"));
					cp.setCreationDate(new Date());
					cp.setLastUpdateDate(new Date());
					cp.setFormulaType("EXP");
					newList.add(cp);
				}
				
			}
		}
		if(updateList !=null && updateList.size()>0){
			for(CellFormula t:updateList){
				formulaContent = t.getFormula();
				String repFormulas[] = getRepFormula(formulaContent+" ", arr);
				String expFormulas[] = getExpFormula(formulaContent+" ", arr);
				int i = 1;
				//指标取数公式
				for(String str : repFormulas){
					CellPreFormula cp = new CellPreFormula();
					cp.setPreFormulaId(UUID.randomUUID().toString());
					cp.setFormulaCat(formulaCat);
					cp.setFormulaId(t.getFormulaId());
					cp.setFormulaStr(str);
					JSONObject object = JSONObject.fromObject(getAllCodes(str));
					cp.setFormulaJson(object.toString());
					cp.setOrderBy(i);
					i++;
					cp.setCreatedBy(map.get("userId"));
					cp.setLastUpdatedBy(map.get("userId"));
					cp.setCreationDate(new Date());
					cp.setLastUpdateDate(new Date());
					cp.setFormulaType("REP");
					newList.add(cp);
				}
				//自定义取数公式
				for(String exp : expFormulas){
					CellPreFormula cp = new CellPreFormula();
					cp.setPreFormulaId(UUID.randomUUID().toString());
					cp.setFormulaCat(formulaCat);
					cp.setFormulaId(t.getFormulaId());
					cp.setFormulaStr(exp);
					Map<String,String> paramMap = (Map<String, String>) getAllParam(exp).get("paramMap");
					JSONObject object = JSONObject.fromObject(paramMap);
					cp.setFormulaJson(object.toString());
					cp.setOrderBy(i);
					i++;
					cp.setCreatedBy(map.get("userId"));
					cp.setLastUpdatedBy(map.get("userId"));
					cp.setCreationDate(new Date());
					cp.setLastUpdateDate(new Date());
					cp.setFormulaType("EXP");
					newList.add(cp);
				}
				
			}
		}
		return newList;
	}
	/**
	 * 
	 * @methodName  replaceBlank
	 * @author      tangxl
	 * @date        2016年9月7日
	 * @describe    去空格
	 * @param str
	 * @return
	 * @变动说明       
	 * @version     1.0
	 */
	public static String replaceBlank(String str)
	  {
     Pattern p = Pattern.compile("[\\s\\p{Zs}]");
	   Matcher m = p.matcher(str);
	   String after = m.replaceAll("");
	   return after;
	}
	/**
	 * 
	 * @methodName  clearCellAnnotation
	 * @author      tangxl
	 * @date        2016年9月14日
	 * @describe    清除单元格的批注
	 * @param 		cellList
	 * @变动说明       
	 * @version     1.0
	 */
	public static void clearCellAnnotation(List<ReportTabCellBean> cellList) throws Exception{
		if(cellList != null && cellList.size()>0){
			for(ReportTabCellBean t:cellList){
				Map<String, Object> mapObject = JsonUtil.getJsonObj(t.getJson());
				//剔除cell单元格中存在的comment和commentEdit属性
				if(mapObject.containsKey("comment"))
					mapObject.remove("comment");
				if(mapObject.containsKey("commentEdit"))
					mapObject.remove("commentEdit");
				if(mapObject.containsKey("rtcorner"))
					mapObject.remove("rtcorner") ;
				t.setJson(JsonUtil.toJson(mapObject));
			}
		}
	}
	/**
	 * 
	 * @methodName  报表重命名和排序
	 * @author      tangxl
	 * @date        2016年9月14日
	 * @describe    
	 * <document>
	 *     <title>命名规则说明</title>
	 *     <p>
     *      1、不同的公司，同一类报表<公司为A,B,C；报表为【资产负债表】>，打开报表后的页签名为【公司名称】
     *		2、同一个公司，不同类报表<公司为A；表报为【资产负债表】、【利润表】>，打开报表后的页签名为【报表名】
     *		3、不同公司，不同类报表，打开报表后的页签名为【公司名-报表名】
     *		4、单张报表，打开后的页签名为【报表名】
     *      5、重新命名前，需先对报表进行排序，将指定选取的报表放置到首位
     *      </p>
     *      <return>
     *       返回报表sheetId和其序号对应关系的字符串
     *      </return>
	 * </document>
	 * @param 		sheetList
	 * @param       querySheetId <查询后显示的表>
	 * @throws 		Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public static String renameReprotName(List<ReportSheetBean> sheetList,String querySheetId) throws Exception{
		HashMap<String, Integer> orgMap = new HashMap<String, Integer>();
		HashMap<String, Integer> tabMap = new HashMap<String, Integer>();
		int i = 0;
		String duplicateOrg = "";
		String duplicateTab = "";
		ReportSheetBean selectBean = null;
		for(ReportSheetBean x:sheetList){
			if(x.getSheetId().equalsIgnoreCase(querySheetId)){
				selectBean = x;
				sheetList.remove(x);
				break;
			}
		}
		if(selectBean != null)
			sheetList.add(0, selectBean);
		for(ReportSheetBean t:sheetList){
			duplicateOrg = t.getOrgId();
			duplicateTab = t.getTabId();
			t.setId(i);
			if(!orgMap.containsKey(duplicateOrg)){
				orgMap.put(duplicateOrg, 1);
			}
			if(!tabMap.containsKey(duplicateTab)){
				tabMap.put(duplicateTab, 1);
			}
			i++;
		}
		int  renameRule = 4;
		//校验组织以及模板map的长度,确定重命名规则
		if(orgMap.size() == 1 && tabMap.size() == 1){
			renameRule = 1;
		}else if (orgMap.size() == 1 && tabMap.size() > 1) {
			renameRule = 2;
		}else if (orgMap.size() > 1 && tabMap.size() > 1) {
			renameRule = 3;
		}else{
			renameRule = 4;
		}
		JSONArray array = new JSONArray();
		//确定重命名规则
		for(ReportSheetBean r:sheetList){
			JSONObject object = new JSONObject();
			object.put("sheetId", r.getSheetId());
			object.put("tabOrder", r.getId()+"");
			array.add(object);
			switch (renameRule) {
			case 1:
				r.setName(r.getOrgName());
				break;
			case 2:
				r.setName(r.getTabName());
				break;
			case 3:
				r.setName(r.getOrgName().concat("-").concat(r.getTabName()));
				break;
			default:
				r.setName(r.getTabName());
				break;
			}
		}
		return array.toString();
	}
}
