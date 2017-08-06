package com.xzsoft.xsr.core.service.impl.repCheck;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.xzsoft.xsr.core.modal.CheckFormula;

/**
 * 使用redis的解析稽核公式
 * redis查询key格式：
 * 固定行：表套&期间&公司&币种&行指标&列指标
 * 浮动行：表套&期间&公司&币种&行指标&列指标&模板id&浮动行号
 * @author ZhouSuRong
 * @date 2016-3-3
 */
public class AnalyzeRedisCheckFormula extends AbstractAnalyzeCheckFormula{
	private static Logger log = Logger.getLogger(AnalyzeRedisCheckFormula.class.getName());
	//操作redis数据类
	OperateRedisData redisOperate = new OperateRedisData();
	/**
	 * 解析公式
	 * @param list
	 * @param params
	 * @return 
	 */
	@Override
	public List<CheckFormula> analyzeFormula(List<CheckFormula> list, String[] params) {
    	List<CheckFormula> array = new ArrayList<CheckFormula>();
    	try {
        	for (int i = 0; i < list.size(); i++) {
        		CheckFormula cf = list.get(i);
        		//公式左边值(同时也是公式左边解析后的表达式)
        		String leftValue = parseLeftFormula(cf, params);
        		//公式右边表达式
    			String formulaRight = cf.getF_RIGHT();
    			//公式右边解析后的表达式
    			String rightValue = "";
    			if(formulaRight.contains("GetRepVal")){
    				rightValue = parseRightFormula(cf, params);
    	        }else{
    	        	rightValue = formulaRight;
    	        }
    			
    			cf.setLeftParsedStr(leftValue);
    			cf.setRightParsedStr(rightValue);
    			cf.setLeftValue(leftValue);
    			
    			array.add(cf);
        	}
    	} catch(Exception e) {
    		log.error("redis解析公式出错", e);
    		throw new RuntimeException("redis解析公式失败！ ： " + e.getMessage());
    	} finally {
    		//XsrRedisPool.returnResource(jedis);
    	}
    	return array;
	}

	/**
	 * 解析公式左边,从redis中取数返回
	 * @param cf
	 * @param params
	 * @return 
	 */
    public String parseLeftFormula(CheckFormula cf, String[] params) {
    	
    	String leftFormula = cf.getF_LEFT();

		int rStart = leftFormula.indexOf("|ROW|=|"); //行指标起始位置
		int rEnd = leftFormula.indexOf("|,|COL|=|"); //行指标结束位置
		int cEnd = leftFormula.indexOf("|\")"); //列指标结束位置

		String rowitemCode = leftFormula.substring(rStart+7, rEnd); 
		String colitemCode = leftFormula.substring(rEnd+9, cEnd); 
        
        StringBuffer searchKey = new StringBuffer();
        
        //公司id,期间id,币种id,是否稽核浮动行,公司code,期间code,币种code,表套id
        searchKey.append(params[7]).append("&").append(params[5]).append("&")
                 .append(params[4]).append("&").append(params[6]).append("&")
                 .append(rowitemCode).append("&").append(colitemCode);
        //浮动行数据key
        if(-1 != cf.getROWNUMDTL()) {
        	searchKey.append("&").append(cf.getModalsheet_id())
        	         .append("&").append(cf.getROWNUMDTL());
        }
        // 用key去redis中取值
        String result = redisOperate.get(searchKey.toString());
        if (result == null){
			result = "0";
		}
        // 2016-1-19处理数据"-"，如果获取到的数据只是"-"，那么将其替换为0
        if("-".equals(result)) {
        	result = "0";
        }
        return result;
 	}

	/**
	 * 解析公式右边,返回带有具体数据的公式串
	 * @param cf
	 * @param params
	 * @return 
	 */
    public String parseRightFormula(CheckFormula cf, String[] params) {
    	String fString = cf.getF_RIGHT();
        while (fString.contains("GetRepVal")) {
        	fString = parseDetail(fString, params, cf);
	    }
        return fString;
	}
    
	/**
	 * 解析公式右边明细,从redis中取数返回
	 * @param cf
	 * @param params
	 * @return 
	 */
    public String parseDetail(String fString, String[] params, CheckFormula cf) {
        String fStringTemp = fString;

        int formulaStart = fString.indexOf("GetRepVal"); //公式的起始位置
		int rStart = fString.indexOf("|ROW|=|"); //行指标起始位置
		int rEnd = fString.indexOf("|,|COL|=|"); //行指标结束位置
		
		String lastFormula = fString.substring(rEnd+9); //截取列指标串
		int cEnd = lastFormula.indexOf("|"); //列指标结束位置
		
		int formulaEnd = fString.indexOf("|\")"); //公式的结束位置
		
		int pStart = fString.indexOf("|QJ|=|"); //期间开始位置
		String pLastFormula = fString.substring(pStart+6); //截取期间剩余串
		int pEnd = pLastFormula.indexOf("|"); //期间结束位置

		String periodDesc = pLastFormula.substring(0, pEnd); //期间名称
		String rowitemCode = fString.substring(rStart+7, rEnd); //得到行指标编码
		String colitemCode = lastFormula.substring(0, cEnd); //得到列指标编码
		String needReplace = fString.substring(formulaStart, formulaEnd+3); //截取出需要替换的公式串
		
		StringBuffer searchKey = new StringBuffer();
		searchKey.append(params[7]).append("&");

		if(!"XZRQ".equals(periodDesc)) {
			searchKey.append(getPeriodCode(periodDesc, params[5])).append("&");
		} else {
			searchKey.append(params[5]).append("&");
		}
		searchKey.append(params[4]).append("&").append(params[6]).append("&")
		         .append(rowitemCode).append("&").append(colitemCode);
		//浮动行数据key
        if(-1 != cf.getROWNUMDTL()) {
        	searchKey.append("&").append(cf.getModalsheet_id()).append("&").append(cf.getROWNUMDTL());
        }
        // 用key去redis中取值
        String result = redisOperate.get(searchKey.toString());
        if (result == null){
			result = "0";
		} else if (result.contains("-")) {
	        // 2016-1-19处理数据"-"，如果获取到的数据仅是"-"，那么将结果设为0
	        if("-".equals(result)) {
	        	result = "0";
	        } else {
	        	result ="(0"+result+")";
	        }
		}
        fStringTemp = fStringTemp.replace(needReplace, result);
        
        return fStringTemp;
 	}
    
}
