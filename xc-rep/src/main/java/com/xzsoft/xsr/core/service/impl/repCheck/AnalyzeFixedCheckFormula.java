package com.xzsoft.xsr.core.service.impl.repCheck;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.xzsoft.xip.platform.util.PlatformUtil;
import com.xzsoft.xsr.core.mapper.RepChkMapper;
import com.xzsoft.xsr.core.modal.CheckFormula;
import com.xzsoft.xsr.core.modal.CheckFormulaCellData;

/**
 * 固定行：解析稽核公式
 * 
 * 适用于关系型数据库查询数据进行稽核。
 * 每次解析公式前，获取这表模板对应的报表数据。
 * 将数据存放在cellDataMap中，本表数据可以从map中取值。
 * 但是，固定行表在两种情况下，需要查询数据库获取数据
 * 情况一：期间类型不是 当前期间XZRQ
 * 情况二：公式右边指标不是本表指标
 * 
 * @date 2016-3-3
 * @author ZhouSuRong
 */
public class AnalyzeFixedCheckFormula extends AbstractAnalyzeCheckFormula{
	private static Logger log = Logger.getLogger(AnalyzeFixedCheckFormula.class.getName());
	
	private RepChkMapper chkMapper;
	private Set<String> itemSet; //本模板使用到的行列指标组合,格式:"行指标code|列指标code"
	
	public AnalyzeFixedCheckFormula(RepChkMapper chkMapper) {
		this.chkMapper = chkMapper;
	}
	
	public AnalyzeFixedCheckFormula(RepChkMapper chkMapper, Set<String> itemSet) {
		this.chkMapper = chkMapper;
		this.itemSet = itemSet;
	}
	
	public Set<String> getItemSet() {
		return itemSet;
	}

	public void setItemSet(Set<String> itemSet) {
		this.itemSet = itemSet;
	}

	/**
	 * 解析公式
	 * @param list
	 * @param params
	 * @return 
	 */
	@Override
	public List<CheckFormula> analyzeFormula(List<CheckFormula> list, String[] params) {
		//params: 公司id,期间id,币种id,是否稽核浮动行,公司code,期间code,币种code,表套id,模板id,公司名称
    	HashMap<String, String> param = new HashMap<>();
    	param.put("entityId", params[0]);
    	param.put("periodId", params[1]);
    	param.put("cnyId", params[2]);
    	param.put("suitId", params[7]);
    	param.put("modalsheetId", params[8]);
    	try {
	    	//查询该张报表的固定行数据
	    	List<CheckFormulaCellData> cellDataList = chkMapper.getFixedCellDataList(param);
	    	//报表的数据map,key为行指标code&列指标code,value为cellv或celltextv字段值
	    	HashMap<String, String> cellDataMap = new HashMap<>();
	    	StringBuilder key = new StringBuilder();
	    	//将该张报表的固定行数据存放到map中
	    	for(CheckFormulaCellData data : cellDataList) {
	    		key.setLength(0);
	    		key.append(data.getROWITEM_CODE()).append("&").append(data.getCOLITEM_CODE());
	    		String value = null;
	    		switch(data.getDATATYPE()) {
	    			case "1":
	    				value = data.getCELLTEXTV();
	    				break;
	    			case "3":
	    				value = data.getCELLV();
	    				break;
	    			case "5":
	    				value = data.getCELLTEXTV();
	    				break;
	    		}
	    		cellDataMap.put(key.toString(), value);	
	    	}
	    	//解析公式
        	for (CheckFormula cf : list) {
        		//公式左边值(同时也是公式左边解析后的表达式)
    			String leftValue = parseLeftFormula(cf, params, cellDataMap);
    			//公式右边表达式
    			String formulaRight = cf.getF_RIGHT();
    			//公式右边解析后的表达式
    			String rightValue = "";
    			if(formulaRight.contains("GetRepVal")){
    				rightValue = parseRightFormula(cf, params, cellDataMap);
    	        }else{
    	        	rightValue = formulaRight;
    	        }
    			cf.setLeftParsedStr(leftValue);
    			cf.setRightParsedStr(rightValue);
    			cf.setLeftValue(leftValue);
        	}
    	} catch(Exception e) {
    		log.error("解析公式出错", e);
    		throw new RuntimeException("解析公式失败！ ： " + e.getMessage());
    	}
    	return list;
	}
    
    /**
     * 解析公式左边,公式左边的数据可以从cellDataMap中获取
     * @param cf
     * @param params
     * @param cellDataMap
     * @return
     */
    public String parseLeftFormula(CheckFormula cf, String[] params, HashMap<String, String> cellDataMap) {
    	
    	String leftFormula = cf.getF_LEFT();

		int rStart = leftFormula.indexOf("|ROW|=|"); //行指标起始位置
		int rEnd = leftFormula.indexOf("|,|COL|=|"); //行指标结束位置
		int cEnd = leftFormula.indexOf("|\")"); //列指标结束位置

		String rowitemCode = leftFormula.substring(rStart+7, rEnd); 
		String colitemCode = leftFormula.substring(rEnd+9, cEnd); 
        
        StringBuffer searchKey = new StringBuffer();
        
        //params: 公司id,期间id,币种id,是否稽核浮动行,公司code,期间code,币种code,表套id,模板id
        searchKey.append(rowitemCode).append("&").append(colitemCode);
        // 用key去cellDataMap中取值
        String result = cellDataMap.get(searchKey.toString());
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
    public String parseRightFormula(CheckFormula cf, String[] params, HashMap<String, String> cellDataMap) throws Exception {
    	String fString = cf.getF_RIGHT();
        while (fString.contains("GetRepVal")) {
        	fString = parseDetail(fString, params, cf, cellDataMap);
	    }
        return fString;
	}
    
	/**
	 * 解析公式右边明细
	 * 固定行表在两种情况下，需要查询数据库获取数据
	 * 情况一：期间类型不是 当前期间XZRQ
	 * 情况二：公式右边指标不是本表指标
	 * @param cf
	 * @param params
	 * @return 
	 */
    public String parseDetail(String fString, String[] params, CheckFormula cf, HashMap<String, String> cellDataMap) throws Exception {
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
		
		String item = rowitemCode + "|" + colitemCode;
		
		StringBuffer searchKey = new StringBuffer();
		String result = null;

		//params: 公司id,期间id,币种id,是否稽核浮动行,公司code,期间code,币种code,表套id,模板id,公司名称
		HashMap<String, String> param = new HashMap<>();
		param.put("entityCode", params[4]);
		param.put("cnyCode", params[6]);
		param.put("rowitemCode", rowitemCode);
		param.put("colitemCode", colitemCode);
		param.put("suitId", params[7]);
		param.put("dbType", PlatformUtil.getDbType());
		if(!"XZRQ".equals(periodDesc)) {
			String periodCode = getPeriodCode(periodDesc, params[5]);
			param.put("periodCode", periodCode);
			CheckFormulaCellData cellData = chkMapper.getFixedCellDataByCode(param);
			result = (null == cellData)? "0": cellData.getCELLV();
//System.out.println("解析稽核公式-非本期间 公司:" + params[9] + " " + item + " " + periodDesc + " 值为： " + result);
		} else if(! itemSet.contains(item)) {
			param.put("periodCode", params[5]);
			CheckFormulaCellData cellData = chkMapper.getFixedCellDataByCode(param);
			result = (null == cellData)? "0": cellData.getCELLV();
//System.out.println("解析稽核公式-非本表指标 公司:" + params[9] + " " + item + " " + periodDesc + " 值为： " + result);
		} else {
			searchKey.append(rowitemCode).append("&").append(colitemCode);
			//用key去cellDataMap中取值
			result = cellDataMap.get(searchKey.toString());
//System.out.println("解析稽核公式-cellDataMap 公司:" + params[9] + " " + item + " " + periodDesc + " 值为： " + result);
		}
		
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
