package com.xzsoft.xsr.core.service.impl.repCheck;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.xzsoft.xip.framework.util.SpringDBHelp;
import com.xzsoft.xsr.core.mapper.RepChkMapper;
import com.xzsoft.xsr.core.modal.CheckFormula;
import com.xzsoft.xsr.core.modal.CheckFormulaCellData;
import com.xzsoft.xsr.core.service.impl.RepChkServiceImpl;

/**
 * 浮动行表：解析稽核公式
 * 浮动行表只有整列公式
 * 整列公式的公式右边都是本表内的指标
 * 
 * @date 2016-3-3
 * @author ZhouSuRong
 */
public class AnalyzeFjCheckFormula extends AbstractAnalyzeCheckFormula{
	private static Logger log = Logger.getLogger(AnalyzeFjCheckFormula.class.getName());
	
	private RepChkMapper chkMapper;
	private Map<String, String> fjColSetMap; // 获取浮动行data列与列指标的映射关系
	private Map<String, String> ColfjSetMap; //获取列指标与浮动行data列的映射关系
	private String fjsql; // 浮动行表的DATA列串
	
	public AnalyzeFjCheckFormula(RepChkMapper chkMapper) {
		this.chkMapper = chkMapper;
	}
	
	public Map<String, String> getFjColSetMap() {
		return fjColSetMap;
	}
	public void setFjColSetMap(Map<String, String> fjColSetMap) {
		this.fjColSetMap = fjColSetMap;
	}
	
	public Map<String, String> getColfjSetMap() {
		return ColfjSetMap;
	}

	public void setColfjSetMap(Map<String, String> colfjSetMap) {
		ColfjSetMap = colfjSetMap;
	}

	public String getFjsql() {
		return fjsql;
	}
	public void setFjsql(String fjsql) {
		this.fjsql = fjsql;
	}

	/**
	 * 解析公式
	 * @param list
	 * @param params
	 * @return 
	 */
	@Override
	public List<CheckFormula> analyzeFormula(List<CheckFormula> list, String[] params) {
		List<CheckFormula> fjFormulaList = new ArrayList<>();
		try {
			//获取浮动行数据Map,key为行指标code&列指标code&明细行号,value为值
			HashMap<String, String> fjCellDataMap = getFjDataMap(params);
			// 浮动行-行号
			HashMap<String, String> param = new HashMap<>();
			param.put("SUIT_ID", params[7]);
			param.put("ENTITY_ID", params[0]);
			param.put("PERIOD_ID", params[1]);
			param.put("CURRENCY_ID", params[2]);
			param.put("MODALSHEET_ID", params[8]);
			Map<String, String> fjRowNum = new HashMap<>();
			List<CheckFormulaCellData> fjRowNumList = chkMapper.getFjRowNum(param);
			for(CheckFormulaCellData d : fjRowNumList) {
				fjRowNum.put(d.getROWITEM_ID(), Integer.toString(d.getMAXNUM()));
			}
			//获取浮动行公式,浮动行公式需要根据对应的数据明细行数生成公式
			for(CheckFormula c : list) {
				String rowitemId = c.getROWITEM_ID();
				String fjNum = fjRowNum.get(rowitemId);
				if ((null != fjNum) && (! "".equals(fjNum))) {
					int num = Integer.parseInt(fjNum);
					for (int i = 1; i <= num; i++) {
						CheckFormula cf = RepChkServiceImpl.generateNewFormula(c, params[7], params[0], params[9], i);
						fjFormulaList.add(cf);
					}
				}
			}

    	//解析公式
//params: 公司id,期间id,币种id,是否稽核浮动行,公司code,期间code,币种code,表套id,模板id,公司名称
//System.out.println("浮动行-解析稽核公式，公司：" + params[9] + " 模板：" + params[8] + " 公式数：" + fjFormulaList.size());    	
        	for (CheckFormula cf : fjFormulaList) {
        		//公式左边值(同时也是公式左边解析后的表达式)
    			String leftValue = parseLeftFormula(cf, params, fjCellDataMap);
    			//公式右边表达式
    			String formulaRight = cf.getF_RIGHT();
    			//公式右边解析后的表达式
    			String rightValue = "";
    			if(formulaRight.contains("GetRepVal")){
    				rightValue = parseRightFormula(cf, params, fjCellDataMap);
    	        }else{
    	        	rightValue = formulaRight;
    	        }
    			cf.setLeftParsedStr(leftValue);
    			cf.setRightParsedStr(rightValue);
    			cf.setLeftValue(leftValue);
        	}
    	} catch(Exception e) {
    		log.error("浮动行解析公式出错", e);
    		throw new RuntimeException("浮动行解析公式失败！ ： " + e.getMessage());
    	}
    	return fjFormulaList;
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
        searchKey.append(rowitemCode).append("&").append(colitemCode).append("&").append(cf.getROWNUMDTL());
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
     * @param cellDataMap
     * @return
     * @throws Exception
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
     * @param fString
     * @param params
     * @param cf
     * @param cellDataMap
     * @return
     * @throws Exception
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
		
		//String item = rowitemCode + "|" + colitemCode + "|" + cf.getROWNUMDTL();
		
		StringBuffer searchKey = new StringBuffer();
		String result = null;
		/**
		 * 浮动行表只在一种情况下，需要查询数据库获取数据
		 * 情况一：期间类型不是 当前期间XZRQ
		 * （因为浮动行表只稽核整列公式，整列公式的指标肯定都是本表指标）
		 */
		//params: 公司id,期间id,币种id,是否稽核浮动行,公司code,期间code,币种code,表套id,模板id
		HashMap<String, String> param = new HashMap<>();
		param.put("entityCode", params[4]);
		param.put("cnyCode", params[6]);
		param.put("rowitemCode", rowitemCode);
		param.put("colitemCode", colitemCode);
		param.put("suitId", params[7]);
		param.put("MODALSHEET_ID", params[8]);
		if(!"XZRQ".equals(periodDesc)) {
			String periodCode = getPeriodCode(periodDesc, params[5]);
			param.put("periodCode", periodCode);
			param.put("rowNumDtl", cf.getROWNUMDTL().toString());
			result = getFjCellData(param);
//System.out.println("浮动行-解析稽核公式-非本期间 公司:" + params[9] + " " + item + " " + periodDesc + " 值为： " + result);
		} else {
			//params: 公司id,期间id,币种id,是否稽核浮动行,公司code,期间code,币种code,表套id,模板id
			searchKey.append(rowitemCode).append("&").append(colitemCode).append("&").append(cf.getROWNUMDTL());
			// 用key去cellDataMap中取值
			result = cellDataMap.get(searchKey.toString());
//System.out.println("浮动行-解析稽核公式-cellDataMap 公司:" + params[9] + " " + item + " " + periodDesc + " 值为： " + result);
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
    
    /**
     * 查询浮动行数据：参数 公司id,期间id,币种id,模板id,表套id
     * @param params
     * @return
     */
    public HashMap<String, String> getFjDataMap(String[] params) {
    	//params: 公司id,期间id,币种id,是否稽核浮动行,公司code,期间code,币种code,表套id,模板id,公司名称
    	StringBuilder fjDataKey = new StringBuilder();
    	HashMap<String, String> fjCellDataMap = new HashMap<>();
    	StringBuilder sql = new StringBuilder();
    	sql.append(" select t.ROWITEM_CODE , t.ROWNUMDTL ").append(fjsql)
    	   .append(" from xsr_rep_fj_cellvalue t ")
    	   .append(" where t.SUIT_ID = '").append(params[7])
    	   .append("' and t.ENTITY_ID = '").append(params[0])
    	   .append("' and t.PERIOD_ID = '").append(params[1])
    	   .append("' and t.CURRENCY_ID = '").append(params[2])
    	   .append("' and t.MODALSHEET_ID = '").append(params[8]).append("'");
    	Connection con = null;
    	Statement st = null;
    	ResultSet rs = null;
    	try {
    		con = SpringDBHelp.getConnection();
    		st = con.createStatement();
    		rs = st.executeQuery(sql.toString());
    		Set<String> dataSet = fjColSetMap.keySet();
    		while(rs.next()) {
    			for(String dataKey : dataSet) {
					fjDataKey.setLength(0);
					//行指标编码&列指标编码&浮动行明细号
					fjDataKey.append(rs.getString("ROWITEM_CODE")).append("&")
					         .append(fjColSetMap.get(dataKey)).append("&")
					         .append(rs.getString("ROWNUMDTL"));
					fjCellDataMap.put(fjDataKey.toString(), rs.getString(dataKey));
				}
    		}
    		
    	} catch(Exception e) {
    		log.error("报表稽核-查询浮动行数据出错", e);
    		throw new RuntimeException("报表稽核-查询浮动行数据出错: " + e.getMessage());
    	} finally {
    		try {
    			rs.close();
				st.close();
			} catch (SQLException e) {
				log.error("报表稽核-查询浮动行数据-关闭rs&st出错", e);
			}
    		SpringDBHelp.releaseConnection(con);
    	}
    	return fjCellDataMap;
    }

    /**
     * 获取浮动行一个单元格的数据
     * @param params
     * @return
     */
    public String getFjCellData(HashMap<String, String> param) {
    	StringBuilder sql = new StringBuilder();
    	sql.append(" select ").append(ColfjSetMap.get(param.get("colitemCode")))
    	   .append(" from xsr_rep_fj_cellvalue t ")
    	   .append(" where t.SUIT_ID = '").append(param.get("suitId"))
    	   .append("' and t.ENTITY_CODE = '").append(param.get("entityCode"))
    	   .append("' and t.PERIOD_CODE = '").append(param.get("periodCode"))
    	   .append("' and t.CURRENCY_CODE = '").append(param.get("cnyCode"))
    	   .append("' and t.MODALSHEET_ID = '").append(param.get("MODALSHEET_ID"))
    	   .append("' and t.ROWITEM_CODE = '").append(param.get("rowitemCode"))
    	   .append("' and t.ROWNUMDTL = ").append(param.get("rowNumDtl"));
    	Connection con = null;
    	Statement st = null;
    	ResultSet rs = null;
    	String value = null;
    	try {
    		con = SpringDBHelp.getConnection();
    		st = con.createStatement();
    		rs = st.executeQuery(sql.toString());
    		if(rs.next()) {
    			value = rs.getString(1);
    		}
    	} catch(Exception e) {
    		log.error("报表稽核-查询浮动行单元格数据出错", e);
    		throw new RuntimeException("报表稽核-查询浮动行单元格数据出错: " + e.getMessage());
    	} finally {
    		try {
    			rs.close();
				st.close();
			} catch (SQLException e) {
				log.error("报表稽核-查询浮动行单元格数据-关闭rs&st出错", e);
			}
    		SpringDBHelp.releaseConnection(con);
    	}
    	return value;
    }
}
