package com.xzsoft.xsr.core.util;



import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;

import com.xzsoft.xsr.core.modal.DataDTO;


/**
 *@描述:取数公式翻译类
 *@date：2016-1-12
 *@author: guoyh
 */
/**
 * @author win7
 *
 */
public class FormulaTranslate {
	private static final Logger log = Logger.getLogger(FormulaTranslate.class.getName());


	/**
	 * 按照 " 拆分字符串
	 * @param oldStr
	 * @return
	 */
	public static String[] spiltStrBySpace(String oldStr){
		Pattern pattern = Pattern.compile("[,\"\"]");
		String[] strs = pattern.split(oldStr);
		return strs;
	}

	/**
	 * 截取公司字符串
	 * @param oldStr
	 * @return
	 */
	public static boolean entityStart(String oldStr){
		Pattern pattern = Pattern.compile("^\\|DW\\|=\\|.*\\|");
		Matcher matcher = pattern.matcher(oldStr);
		boolean b= matcher.matches();
		return b;
	}
	/**
	 * 截取日期字符串
	 * @param oldStr
	 * @return
	 */
	public static boolean periodStart(String oldStr){
		Pattern pattern = Pattern.compile("^\\|QJ\\|=\\|.*\\|");
		Matcher matcher = pattern.matcher(oldStr);
		boolean b= matcher.matches();
		return b;
	}

	/**
	 * 截取科目字符串
	 * @param oldStr
	 * @return
	 */
	public static boolean kmStart(String oldStr){
		Pattern pattern = Pattern.compile("^\\|KM\\|=\\|.*\\|");
		Matcher matcher = pattern.matcher(oldStr);
		boolean b= matcher.matches();
		return b;
	}

	/**
	 * 截取往来单位字符串
	 * @param oldStr
	 * @return
	 */
	public static boolean wlStart(String oldStr){
		Pattern pattern = Pattern.compile("^\\|WL\\|=\\|.*\\|");
		Matcher matcher = pattern.matcher(oldStr);
		boolean b= matcher.matches();
		return b;
	}

	/**
	 * 截取往来币种字符串
	 * @param oldStr
	 * @return
	 */
	public static boolean bzStart(String oldStr){
		Pattern pattern = Pattern.compile("^\\|BZ\\|=\\|.*\\|");
		Matcher matcher = pattern.matcher(oldStr);
		boolean b= matcher.matches();
		return b;
	}

	/**
	 * 截取行指标字符串
	 * @param oldStr
	 * @return
	 */
	public static boolean rowStart(String oldStr){
		Pattern pattern = Pattern.compile("^\\|ROW\\|=\\|.*\\|");
		Matcher matcher = pattern.matcher(oldStr);
		boolean b= matcher.matches();
		return b;
	}

	/**
	 * 截取列指标字符串
	 * @param oldStr
	 * @return
	 */
	public static boolean colStart(String oldStr){
		Pattern pattern = Pattern.compile("^\\|COL\\|=\\|.*\\|");
		Matcher matcher = pattern.matcher(oldStr);
		boolean b= matcher.matches();
		return b;
	}

	/**
	 * 截取项目字符串
	 * @param oldStr
	 * @return
	 */
	public static boolean projectStart(String oldStr){
		Pattern pattern = Pattern.compile("^\\|XM\\|=\\|.*\\|");
		Matcher matcher = pattern.matcher(oldStr);
		boolean b= matcher.matches();
		return b;
	}

	/**
	 * 截取部门字符串
	 * @param oldStr
	 * @return
	 */
	public static boolean departmentStart(String oldStr){
		Pattern pattern = Pattern.compile("^\\|BM\\|=\\|.*\\|");
		Matcher matcher = pattern.matcher(oldStr);
		boolean b= matcher.matches();
		return b;
	}

	/**
	 * 去除回车换行空格和问号
	 * @param oldStr
	 * @return
	 * 注
	 * \n 回车(\u000a)
	 * \t 水平制表符(\u0009) 
	 * \s 空格(\u0008) 
	 * \r 换行(\u000d)     
	 */
	public static String replaceStr(String oldStr){
		if (oldStr!=null) {   
			Pattern p = Pattern.compile("[\\s]|[\t]|[\r]|[\n]|[?]|[^\\p{ASCII}]");    
			Matcher m = p.matcher(oldStr);   
			return m.replaceAll("");
		}    
		return null; 
	}
	/**
	 * 取出公式串中的所有GetRepVal()公式
	 * @param oldStr
	 * @return
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
	 * 取出公式串中的所有GetExpVal()公式
	 * @param oldStr
	 * @return
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
	 * 获取科目、行指标和列指标代码指
	 * 如|ROW|=|HZCFZ01|,则提取HZCFZ01
	 * 如|COL|=|LZCFZ001|,则提取LZCFZ001
	 * @param oldStr为单个公式项，比如：GetRepVal("|QJ|=|XZRQ:1|,|DW|=|XZDW|,|ROW|=|zcfzb-test-0001-0005|,|COL|=|zcfzb-test-0001-0001|")
	 * @return
	 */
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
	 * 获取自定义取数函数的类型和函数名，还有所有参数
	 * 如param1=HALFYEAR,则提取HALFYEAR
	 * @param oldStr为单个公式项，比如：GetExpVal("gyh_test>>param1=HALFYEAR,param2={22},param3=SUIT_CODE1")
	 * @return
	 */
	public static Map<String,Object> getAllParam(String oldStr){
		if(oldStr.contains("GetExpVal")){
			Map<String,Object> map=new HashMap<String,Object>();
			String famulaArr[]=oldStr.split(">>");
			String funText=famulaArr[0];//GetExpVal("FunCode
			String funCode=funText.substring(funText.indexOf("(\"")+2,funText.length());//FunCode
//			String funArr[]=funText.split("#");
//			String funType=funArr[0];//函数类型JAVA
//			map.put("funType", funType);
//			String funCalType=funArr[1];//函数取数运算类型：REP-表间取数;APP-数据中心取数;EXP-直取业务系统
//			map.put("funCalType", funCalType);
//			String funCode=funArr[2];//函数编码FunCode
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

	//反射机制执行自定义取数函数
	public static String runJavaMethod(String clzName,String MethodName,String[] parameters) throws  Exception{
		Class c = Class.forName(clzName);
		Object obj = c.newInstance();
		Class[] parameterType = (Class[])null;
		Object[] args = (Object[])null;


		int len = parameters.length;
		parameterType = new Class[len];
		args = new Object[len];
		for (int i = 0; i < len; i++) {
			String p = parameters[i];
			parameterType[i] = String.class;
			args[i] = ("".equals(p) ? null : p);
		}
		Method m = c.getMethod(MethodName, parameterType);
		String returnValue = (String)m.invoke(obj, args);
		System.out.println(returnValue+"------------------------------");
		return returnValue;
	}

	/**
	 * 拼装正则表达式匹配对象数组
	 * @return
	 */
	public static Object[] patternArray(List<DataDTO> list){

		String[][] object = new String[17][1];
		object[0] = new String[]{"GetAppVal",""};
		object[1] = new String[]{"GetRepVal",""};
		object[2] = new String[]{"\\|QJ\\|","期间"};
		object[3] = new String[]{"\\|DW\\|","单位"};
		object[4] = new String[]{"\\|XZDW\\|","当前单位"};
		object[5] = new String[]{"\\|YE\\|","余额类型"};
		object[6] = new String[]{"\\|KM\\|","科目"};
		object[7] = new String[]{"\\|ROW\\|","行指标"};
		object[8] = new String[]{"\\|COL\\|","列指标"};
		object[9] = new String[]{"\\|WL\\|=","往来单位="};//by liuwl 2015-03-16
		object[10] = new String[]{"\\|BZ\\|","币种"};
		object[11] = new String[]{"\\|CP\\|","产品"};
		object[12] = new String[]{"\\|XM\\|","项目"};
		object[13] = new String[]{"\\|BM\\|","部门"};
		object[14] = new String[]{"GetCmp","组合公式取数"};
		object[15] = new String[]{"GetExpVal","自定义取数"};
		object[16] = new String[]{"\\|XZRQ","当前报表日期"};

		//期间、公司、行指标、列指标、币种
		for(int i=0;i<list.size();i++){
			DataDTO data=list.get(i);
			object=(String[][]) ArrayUtils.add(object,new String[]{"\\|"+data.getCode()+"\\|",data.getName()});
		}
		return object;
	}

	/**
	 * 计算公式
	 * @param equation 四则运算表达式
	 * @return
	 */
	public static String evalFormula(String equation){
		String result="";
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("javascript");
		try {
			Object obj = engine.eval(equation);
			result=obj.toString();
		} catch (ScriptException e) {
			result="公式有误!";
		}
		return result;
	}

	/**
	 * 根据期间传递的关键字,得到对应实际期间
	 * @param periodIdTest 试运算期间Id
	 * @param periodCodeTest 试运算期间code
	 * @param QJCode 公式中的期间字典code
	 * @return 
	 */
	public static String getPeriodCode(String periodIdTest,String periodCodeTest,String QJCode){
		String periodCode=periodCodeTest;
		int len=periodCodeTest.length();
		//增加SNNTQ的期间类型：1－11月份取上年同期，12月或年期间取上年的年期间
		if(QJCode.equals("SNNTQ")){
			if(len==4||periodCodeTest.substring(4, 7).equals("-12")){
				QJCode="SNNQM";
			}else{
				QJCode="SNTQ";
			}
		}
		String QJCodeTemp=","+QJCode+",";
		//针对年期间的操作：期间长度为4
		if(len==4){				
			if(",BNQC,SNTQ,SNQM,SNSY,SNNQM,".indexOf(QJCodeTemp)>0){
				int year=Integer.parseInt(periodCodeTest);
				year=year-1;
				periodCode=year+"";
			}else if(",SYBN,SYTQ,".indexOf(QJCodeTemp)>0){//上月本年跟上月同期,则返回当年11月
				periodCode=periodCodeTest+"-11";
			}
		}else if(len==7){//针对月期间,增加"SNNQM":类型,上年期末(年期间)
			if("SNNQM".equals(QJCode)){
				 int year=Integer.parseInt(periodCodeTest.substring(0, 4));
				 year=year-1;
				 periodCode=year+periodCodeTest.substring(5, 7);
			}
		}
		if(len!=7){
			if(len==8&&",SZTQ,SNTZ,".indexOf(QJCodeTemp)>0){//二种周期间类型处理
				int year=Integer.parseInt(periodCodeTest.substring(0, 4));
				int week=Integer.parseInt(periodCodeTest.substring(6));
				if("SZTQ".equals(QJCode)){
					int weekno=week-1;
					if(weekno<=9){
						periodCode=year+"-W0"+weekno;
					}else{
						periodCode=year+"-W"+weekno;
					}
				}else{
					int yearno=year-1;
					periodCode=yearno+"-W"+periodCodeTest.substring(6);
				}
			}
			//增加对本年年初
			if("BNQC".equals(QJCode)){
				periodCode=periodCodeTest.substring(0, 4);
			}			
		}
		
		switch(QJCode){
			case "XZRQ":
				periodCode=periodCodeTest;
				break;
			case "SYTQ":
				if(len==7){
					int year=Integer.parseInt(periodCodeTest.substring(0, 4));
					int month=Integer.parseInt(periodCodeTest.substring(5, 7));
					periodCode=getMonth(year,month,1);
				}
				break;
			case "SNTQ":
				if(len==7){
					int year=Integer.parseInt(periodCodeTest.substring(0, 4));
					year=year-1;
					periodCode=year+periodCodeTest.substring(5, 7);
				}
				break;
			case "SNQM":
				if(len==7){
					int year=Integer.parseInt(periodCodeTest.substring(0, 4));
					int month=1;
					periodCode=getMonth(year,month,1);
				}
				break;
			case "BNQC":
				if(len==7){
					int year=Integer.parseInt(periodCodeTest.substring(0, 4));
					periodCode=year+"01";
				}
				break;
			case "SYBN":
				if(len==7){
					int year=Integer.parseInt(periodCodeTest.substring(0, 4));
					int month=Integer.parseInt(periodCodeTest.substring(5, 7));
					if(month!=1){
						periodCode=getMonth(year,month,1);
					}else{
						periodCode="2099-01"; //取一月份对应sybn期间时,返回一不存在的期间,从而使公式采集数据为零.
					}					
				}
				break;
			case "SSYB":
				if(len==7){
					int year=Integer.parseInt(periodCodeTest.substring(0, 4));
					int month=Integer.parseInt(periodCodeTest.substring(5, 7));
					if(month!=1&&month!=2){
						periodCode=getMonth(year,month,2);
					}else{
						periodCode="2099-01"; //取一月份对应sybn期间时,返回一不存在的期间,从而使公式采集数据为零.
					}					
				}
				break;
			case "SNSY":
				if(len==7){
					int year=Integer.parseInt(periodCodeTest.substring(0, 4));
					int month=Integer.parseInt(periodCodeTest.substring(5, 7));
					periodCode=getMonth(year,month,13);				
				}
				break;
			default:
				break;
				
		}
		return periodCode;
	}
	/**
	 * 得到当前时间的前n个月
	 * @param year
	 * @param month
	 * @return
	 */
	public static String getMonth(int year,int month,int n){
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);
		c.add(Calendar.MONTH, -n-1);
		return sdf.format(c.getTime());
	}
}
