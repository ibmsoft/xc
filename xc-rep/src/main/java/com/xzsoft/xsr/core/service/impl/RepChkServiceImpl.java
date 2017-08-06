package com.xzsoft.xsr.core.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.json.JSONArray;

import com.xzsoft.xip.platform.util.PlatformUtil;
import com.xzsoft.xsr.core.mapper.RepChkMapper;
import com.xzsoft.xsr.core.modal.CheckFormula;
import com.xzsoft.xsr.core.modal.CheckFormulaCellData;
import com.xzsoft.xsr.core.service.RepChkService;
import com.xzsoft.xsr.core.service.impl.repCheck.AnalyzeFixedCheckFormula;
import com.xzsoft.xsr.core.service.impl.repCheck.AnalyzeFjCheckFormula;
import com.xzsoft.xsr.core.service.impl.repCheck.AnalyzeRedisCheckFormula;
import com.xzsoft.xsr.core.service.impl.repCheck.CalculateCheckFormula;
import com.xzsoft.xsr.core.service.impl.repCheck.CheckExceptionHandler;
import com.xzsoft.xsr.core.service.impl.repCheck.CheckThread;
import com.xzsoft.xsr.core.service.impl.repCheck.QueryCheckResult;

@Service
public class RepChkServiceImpl implements RepChkService{
	@Autowired
	private RepChkMapper chkMapper;
	@Autowired
	private CalculateCheckFormula calculateFormula;
	@Autowired
	private QueryCheckResult queryResult;
	
	/**
	 * 常规稽核-从关系型数据库读取数据
	 * 常规稽核逻辑：
	 * 1. 查询此次稽核涉及到的所有模板的固定行公式，和浮动行公式。
	 *    固定行公式存放在fixedFormulaMap中；浮动行公式存放在fjFormulaMap中。
	 *    如果模板的固定行稽核公式存在，再去查询模板引用的行列指标组合;
	 *    如果模板的浮动行稽核公式存在，再去查询浮动行模板DATA列与列指标映射、fjdata字段。
	 * 2. 按照公司，模板循环，解析模板上的所有稽核公式（固定行和浮动行）。
	 *    公式从fixedFormulaMap，fjFormulaMap中获取，
	 *    一张报表解析一次。将解析后的公式放到parsedFormulaList中。
	 * 3. 运算解析后的稽核公式。parsedFormulaList包括所有公司的解析结果。
	 * 4. 将返回的稽核结果map，整理为json返回。
	 */
	@Override
	public String[] verifyRepDataUseDB(String[] params) throws Exception {
		String[] returnArray = new String[2];
		//参数
		String suitId = params[0];
		String periodId = params[1]; // 期间
		String cnyId = params[2]; // 币种
		String modaltypeId = params[3]; // 模板集
		String verifyCaseID = params[4]; // 稽核方案
		if("".equals(verifyCaseID)) {
			verifyCaseID = "ALL";
		}
		String isCheckFjRow = params[5]; // 是否稽核浮动行
		String entityWithSheetsArrays = params[6];
		String fjModalsheetList = params[7];
		Set<String> fjModalList = new HashSet<>(Arrays.asList(fjModalsheetList.split(",")));
		
		JSONArray jsonArray = JSONArray.fromObject(entityWithSheetsArrays);
        Object[] entityWithSheets = jsonArray.toArray();
        //entityWithSheets参数结构: 公司id&公司code&公司name&期间code&币种code&模板id#模板id#模板id......
        /**
         * 1. 查询本次稽核所有模板的公式
         */
		//本次稽核涉及到的所有模板的固定行稽核公式Map。key为模板id,value是List<CheckFormula>
		Map<String, List<CheckFormula>> fixedFormulaMap = new HashMap<>();
		//本次稽核涉及到的所有模板的浮动行稽核公式Map。key为模板id,value是List<CheckFormula>
		Map<String, List<CheckFormula>> fjFormulaMap = new HashMap<>();
		//本次稽核涉及到的模板,每个模板引用的行列指标组合Map。key为模板id,value是Set<行指标code|列指标code>
		Map<String, Set<String>> modalRefItemMap = new HashMap<>();
		//本次稽核涉及到的浮动行模板DATA列与列指标映射Map。key为模板id,value是Map<DATA列, 列指标code>
		Map<String, Map<String, String>> fjDataColSetMap = new HashMap<>();
		Map<String, Map<String, String>> ColfjDataSetMap = new HashMap<>();
		//本次稽核涉及到的浮动行模板,对应的fj_cellvalue，data字段。key为模板id,value是：DATA1,DATA2,DATA3,......
		Map<String, String> fjDataFeildMap = new HashMap<>(); 
        
        HashMap<String, String> sqlParam = new HashMap<>();
        for(Object o : entityWithSheets) {
        	String[] esArray = o.toString().split("&");
        	String[] sheetArray = esArray[5].split("#");
        	for(String modalsheetId : sheetArray) {
        		//固定行公式
        		if(!fixedFormulaMap.containsKey(modalsheetId)) {      			
            		//查询模板的固定行公式
            		sqlParam.remove("modalsheetId");
            		sqlParam.remove("verifyCaseID");
            		sqlParam.put("modalsheetId", modalsheetId);
            		sqlParam.put("verifyCaseID", verifyCaseID);
            		sqlParam.put("dbType", PlatformUtil.getDbType());
            		List<CheckFormula> formulaList = chkMapper.getFixedFormulaList(sqlParam);
            		//formulaList为null时，也可以put到map中。 如果模板没有稽核公式，也在map中put一次，避免其他公司再次查询
            		fixedFormulaMap.put(modalsheetId, formulaList);
            		if(null != formulaList) {
                    	//如果稽核公式存在，则查询模板上所有引用的指标
                    	List<String> itemList =  chkMapper.getModalItemList(modalsheetId, PlatformUtil.getDbType());
                    	//因为HashSet查找快，所以做了一次转换
                    	HashSet<String> itemSet = new HashSet<>(itemList);
                    	modalRefItemMap.put(modalsheetId, itemSet);
            		}
        		}
        		//是浮动行表
        		if(fjModalList.contains(modalsheetId)) {
        			if(!fjFormulaMap.containsKey(modalsheetId)) {
        				//查询模板的浮动行公式(整列公式稽核浮动行)
        				List<CheckFormula> fjFormulaList = chkMapper.getFjFormulaList(sqlParam);
            			fjFormulaMap.put(modalsheetId, fjFormulaList);
            			if(fjFormulaList.size() > 0) {
            				//如果稽核公式存在，则去查询浮动行date列与列指标的映射关系
            				List<HashMap<String, String>> fjColSet = chkMapper.getFjdataColSet(modalsheetId);
            				HashMap<String, String> fjColSetMap = new HashMap<>();
            				HashMap<String, String> ColfjSetMap = new HashMap<>();
            				StringBuilder fjsql = new StringBuilder();
            				for(HashMap<String, String> mp : fjColSet) {
            					fjColSetMap.put(mp.get("DATA_COL"), mp.get("COLITEM_CODE"));
            					ColfjSetMap.put(mp.get("COLITEM_CODE"), mp.get("DATA_COL"));
            					fjsql.append(",").append(mp.get("DATA_COL"));
            				}
            				fjDataColSetMap.put(modalsheetId, fjColSetMap);
            				ColfjDataSetMap.put(modalsheetId, ColfjSetMap);
            				fjDataFeildMap.put(modalsheetId, fjsql.toString());
            			}
        			}
        		}
        	}
        }    
		/**
		 * 2.解析稽核公式, 循环公司，再循环公司涉及的模板，解析稽核公式
		 */
        //存放解析后的稽核公式
        List<CheckFormula> parsedFormulaList = new ArrayList<>(); 
        //analyzepParams参数结构: 公司id,期间id,币种id,是否稽核浮动行,公司code,期间code,币种code,表套id,模板id,公司名称
        String[] analyzepParams = new String[10]; 
        analyzepParams[1] = periodId;
        analyzepParams[2] = cnyId;
        analyzepParams[3] = isCheckFjRow;
        analyzepParams[7] = suitId;
        
        AnalyzeFixedCheckFormula fixedAnalyzeFormula = new AnalyzeFixedCheckFormula(chkMapper);
        AnalyzeFjCheckFormula fjAnalyzeFormula = new AnalyzeFjCheckFormula(chkMapper);
        for(Object o : entityWithSheets) {
        	String[] esArray = o.toString().split("&");
        	analyzepParams[0] = esArray[0];//公司id
            analyzepParams[4] = esArray[1];//公司code
            analyzepParams[9] = esArray[2];//公司名称
            analyzepParams[5] = esArray[3];//期间code
            analyzepParams[6] = esArray[4];//币种code
        	String[] sheetArray = esArray[5].split("#");
        	for(String modalsheetId : sheetArray) {
        		analyzepParams[8] = modalsheetId;
        		List<CheckFormula> formulaList = new ArrayList<>();
        		if(fixedFormulaMap.get(modalsheetId).size() > 0) {
        			//获取固定行稽核公式
            		for(CheckFormula c : fixedFormulaMap.get(modalsheetId)) {
            			CheckFormula cf = generateNewFormula(c, suitId, esArray[0], esArray[2], -1);
            			formulaList.add(cf);
            		}
            		//解析固定行稽核公式
            		fixedAnalyzeFormula.setItemSet(modalRefItemMap.get(modalsheetId));
            		parsedFormulaList.addAll(fixedAnalyzeFormula.analyzeFormula(formulaList, analyzepParams));
        		}
        		// 如果 需要稽核浮动行 并且 这个模板是浮动行模板,解析浮动行公式
        		if("Y".equals(isCheckFjRow) && fjModalList.contains(modalsheetId)) {
        			
            		List<CheckFormula> fjFormulaList = new ArrayList<>();
            		if(fjFormulaMap.get(modalsheetId).size() > 0) {
            			//获取浮动行稽核公式
                		for(CheckFormula c : fjFormulaMap.get(modalsheetId)) {
                			CheckFormula cf = generateNewFormula(c, suitId, esArray[0], esArray[2], -1);
                			fjFormulaList.add(cf);
                		}
                		//解析浮动行稽核公式
                		fjAnalyzeFormula.setFjColSetMap(fjDataColSetMap.get(modalsheetId));
                		fjAnalyzeFormula.setColfjSetMap(ColfjDataSetMap.get(modalsheetId));
                		fjAnalyzeFormula.setFjsql(fjDataFeildMap.get(modalsheetId));
                		parsedFormulaList.addAll(fjAnalyzeFormula.analyzeFormula(fjFormulaList, analyzepParams));
            		}
        		}
        	}
        }
        /**
         * 3. 运算解析后的稽核公式表达式是否成立
         */
        Map<String, List<CheckFormula>> checkResultMap = calculateFormula.calculateFormula(parsedFormulaList, modaltypeId);
        /**
         * 4. 格式化稽核结果
         */
        if (null != checkResultMap && checkResultMap.size() > 0) {
			String chkFreeMarkerFilePath = params[8];
			returnArray = queryResult.getCheckResultList(checkResultMap, "", chkFreeMarkerFilePath);
		} else {
			returnArray[0] = "{flag:'0',msg:'报表数据稽核成功！'}";
		}
        return returnArray;
	}
	
	/**
	 * 大数据稽核-从redis读取数据
	 * 大数据稽核逻辑：
	 * 1. 查询此次稽核涉及到的所有模板的固定行公式，和浮动行公式。
	 *    固定行公式存放在fixedFormulaMap中；浮动行公式存放在fjFormulaMap中。
	 * 2. 按照公司，模板循环，获取模板上的所有稽核公式（固定行和浮动行）。
	 *    公式从fixedFormulaMap，fjFormulaMap中获取，
	 *    获取到一家公司的所有稽核公式后解析一次（根据速度采用多线程解析）。将解析后的公式放到parsedFormulaList中。
	 * 3. 运算解析后的稽核公式。parsedFormulaList包括所有公司的解析结果。
	 * 4. 将返回的稽核结果map，整理为json返回。
	 */
	@Override
	public String[] verifyRepDataUseRedis(String[] params) throws Exception {
		String[] returnArray = new String[2];
		int listCount = Integer.parseInt(params[9]); 
		//参数
		String suitId = params[0];
		String periodId = params[1]; // 期间
		String cnyId = params[2]; // 币种
		String modaltypeId = params[3]; // 模板集
		String verifyCaseID = params[4]; // 稽核方案
		if("".equals(verifyCaseID)) {
			verifyCaseID = "ALL";
		}
		String isCheckFjRow = params[5]; // 是否稽核浮动行
		String entityWithSheetsArrays = params[6];
		String fjModalsheetList = params[7];
		Set<String> fjModalList = new HashSet<>(Arrays.asList(fjModalsheetList.split(",")));
		
		JSONArray jsonArray = JSONArray.fromObject(entityWithSheetsArrays);
        Object[] entityWithSheets = jsonArray.toArray();
        //entityWithSheets参数结构: 公司id&公司code&公司name&期间code&币种code&模板id#模板id#模板id......
        /**
         * 1. 查询本次稽核所有模板的公式
         */
        //本次稽核涉及到的所有模板的固定行稽核公式Map。key为模板id,value是List<CheckFormula>
		Map<String, List<CheckFormula>> fixedFormulaMap = new HashMap<>();
		//本次稽核涉及到的所有模板的浮动行稽核公式Map。key为模板id,value是List<CheckFormula>
		Map<String, List<CheckFormula>> fjFormulaMap = new HashMap<>();
        
        HashMap<String, String> sqlParam = new HashMap<>();
        for(Object o : entityWithSheets) {
        	String[] esArray = o.toString().split("&");
        	String[] sheetArray = esArray[5].split("#");
        	for(String modalsheetId : sheetArray) {
        		//固定行公式
        		if(!fixedFormulaMap.containsKey(modalsheetId)) {      			
            		//查询模板的固定行公式
            		sqlParam.remove("modalsheetId");
            		sqlParam.remove("verifyCaseID");
            		sqlParam.put("modalsheetId", modalsheetId);
            		sqlParam.put("verifyCaseID", verifyCaseID);
            		sqlParam.put("dbType", PlatformUtil.getDbType());
            		List<CheckFormula> formulaList = chkMapper.getFixedFormulaList(sqlParam);
            		fixedFormulaMap.put(modalsheetId, formulaList);
        		}
        		//是浮动行表
        		if(fjModalList.contains(modalsheetId)) {
        			if(!fjFormulaMap.containsKey(modalsheetId)) {
        				//查询模板的浮动行公式(整列公式稽核浮动行)
        				List<CheckFormula> fjFormulaList = chkMapper.getFjFormulaList(sqlParam);
            			fjFormulaMap.put(modalsheetId, fjFormulaList);
        			}
        		}
        	}
        }
		/**
		 * 2.解析稽核公式, 循环公司，再循环公司涉及的模板，解析稽核公式
		 */
        //存放解析后的稽核公式
        List<CheckFormula> parsedFormulaList = new ArrayList<>(); 
        //analyzepParams: 公司id,期间id,币种id,是否稽核浮动行,公司code,期间code,币种code,表套id,模板id,公司名称
        String[] analyzepParams = new String[10]; 
        analyzepParams[1] = periodId;
        analyzepParams[2] = cnyId;
        analyzepParams[3] = isCheckFjRow;
        analyzepParams[7] = suitId;
        
        for(Object o : entityWithSheets) {
        	String[] esArray = o.toString().split("&");
        	analyzepParams[0] = esArray[0];//公司id
            analyzepParams[4] = esArray[1];//公司code
            analyzepParams[9] = esArray[2];//公司名称
            analyzepParams[5] = esArray[3];//期间code
            analyzepParams[6] = esArray[4];//币种code
            
            //redis稽核时，是一家公司解析一次稽核公式
            List<CheckFormula> oneEntityFormulaList = new ArrayList<>();
            
        	String[] sheetArray = esArray[5].split("#");
        	for(String modalsheetId : sheetArray) {
        		analyzepParams[8] = modalsheetId;
        		
        		if(fixedFormulaMap.get(modalsheetId).size() > 0) {
        			//获取固定行稽核公式
            		for(CheckFormula c : fixedFormulaMap.get(modalsheetId)) {
            			CheckFormula cf = generateNewFormula(c, suitId, esArray[0], esArray[2], -1);
            			oneEntityFormulaList.add(cf);
            		}
        		}
        		// 如果 需要稽核浮动行 并且 这个模板是浮动行模板
        		if("Y".equals(isCheckFjRow) && fjModalList.contains(modalsheetId)) {
        			
            		if(fjFormulaMap.get(modalsheetId).size() > 0) {
            			// 浮动行-行号
            			HashMap<String, String> param = new HashMap<>();
            			param.put("SUIT_ID", suitId);
            			param.put("ENTITY_ID", esArray[0]);
            			param.put("PERIOD_ID", periodId);
            			param.put("CURRENCY_ID", cnyId);
            			param.put("MODALSHEET_ID", modalsheetId);
            			Map<String, String> fjRowNum = new HashMap<>();
            			List<CheckFormulaCellData> fjRowNumList = chkMapper.getFjRowNum(param);
            			for(CheckFormulaCellData d : fjRowNumList) {
            				fjRowNum.put(d.getROWITEM_ID(), Integer.toString(d.getMAXNUM()));
            			}
            			//获取浮动行公式
            			for(CheckFormula c : fjFormulaMap.get(modalsheetId)) {
            				String rowitemId = c.getROWITEM_ID();
            				String fjNum = fjRowNum.get(rowitemId);
            				if ((null != fjNum) && (! "".equals(fjNum))) {
            					int num = Integer.parseInt(fjNum);
            					for (int i = 1; i <= num; i++) {
            						CheckFormula cf = generateNewFormula(c, suitId, esArray[0], esArray[2], i);
            						oneEntityFormulaList.add(cf);
            					}
            				}
            			}
            		}
        		}
        	}
        	AnalyzeRedisCheckFormula analyzeFormula = new AnalyzeRedisCheckFormula();
    		// 解析公式
        	//不启用线程
        	/*if(null != oneEntityFormulaList && oneEntityFormulaList.size() > 0) {
        		parsedFormulaList.addAll(analyzeFormula.analyzeFormula(formulaList, params));	
        	}*/
    		// 启用多线程解析公式
    		if (null != oneEntityFormulaList && oneEntityFormulaList.size() > 0) {
    			// 计算出启动的线程数
    			int threadCount = 0; 
    			if (oneEntityFormulaList.size() < listCount) {
    				threadCount = 1;
    			} else {
    				threadCount = oneEntityFormulaList.size() / listCount;
    				if (oneEntityFormulaList.size() % listCount > 0) {
    					threadCount = threadCount + 1;
    				}
    			}
    			//CheckThreadMap中存放启动的线程，主要是为了后面判断线程是否全部运行结束使用
    			HashMap<Integer, CheckThread> CheckThreadMap = new HashMap<Integer, CheckThread>();
    			//循环过程中启动各个线程，每个线程稽核oneEntityFormulaList中的一部分公式
    			for (int i = 1; i <= threadCount; i++) {
    				List<CheckFormula> checkList = null;
    				if (i == threadCount) {
    					if (oneEntityFormulaList.size() < listCount) {
    						checkList = oneEntityFormulaList.subList((i - 1) * listCount, oneEntityFormulaList.size());
    					} else {
    						checkList = oneEntityFormulaList.subList((i - 2) * listCount + listCount, oneEntityFormulaList.size());
    					}
    				} else {
    					checkList = oneEntityFormulaList.subList((i - 1) * listCount, (i - 1) * listCount + listCount);
    				}
    				CheckThread checkThread = new CheckThread(analyzeFormula, checkList, params);
    				CheckExceptionHandler ch = new CheckExceptionHandler();
    				checkThread.setUncaughtExceptionHandler(ch);
    				CheckThreadMap.put(i, checkThread);
    				checkThread.start();
    			}
    			//while循环中判断各个线程是否运行完成
    			boolean flag = false;
    			Set<Integer> keySet = CheckThreadMap.keySet();
    			//keylist中存放所有已经运行完成的线程
    			ArrayList<Integer> keylist = new ArrayList<Integer>();
    			while (true) {
    				for (Integer k : keySet) {
    					//如果线程执行完成并且keylist不包括这个线程，则是一个新运行结束的线程
    					if (CheckThreadMap.get(k).flag && !keylist.contains(k)) {
    						parsedFormulaList.addAll(CheckThreadMap.get(k).resultList);
    						keylist.add(k);
    					}
    					//keySet与keylist的size相等，说明所有线程运行完成
    					if (keySet.size() == keylist.size()) {
    						flag = true;
    						break;
    					}
    				}
    				if (flag) {
    					break;
    				}
    			}
    		}
    		
        }
        /**
         * 3. 运算解析后的稽核公式表达式是否成立
         */
        Map<String, List<CheckFormula>> checkResultMap = calculateFormula.calculateFormula(parsedFormulaList, modaltypeId);
		
        /**
         * 4. 格式化稽核结果
         */
        if (null != checkResultMap && checkResultMap.size() > 0) {
			String chkFreeMarkerFilePath = params[8];
			returnArray = queryResult.getCheckResultList(checkResultMap, "", chkFreeMarkerFilePath);
		} else {
			returnArray[0] = "{flag:'0',msg:'报表数据稽核成功！'}";
		}
        return returnArray;
	}
	
	/**
	 * 根据传入的CheckFormula参数，生成一个CheckFormula的实例
	 * @param c
	 * @param suitId
	 * @param entityId
	 * @param entityName
	 * @param fjNum
	 * @return
	 */
	public static CheckFormula generateNewFormula(CheckFormula c, String suitId, String entityId, String entityName, int fjNum) {
		CheckFormula cf = new CheckFormula();
		
		cf.setCHK_FORMULA_ID(c.getCHK_FORMULA_ID());
		cf.setSUIT_ID(suitId);
		cf.setModalsheet_id(c.getModalsheet_id());
		cf.setMODALSHEET_NAME(c.getMODALSHEET_NAME());
		cf.setROWITEM_ID(c.getROWITEM_ID());
		cf.setCOLITEM_ID(c.getCOLITEM_ID());
		cf.setF_LEFT(c.getF_LEFT());
		cf.setF_RIGHT(c.getF_RIGHT());
		cf.setF_EXP(c.getF_EXP());
		cf.setF_SETTYPE(c.getF_SETTYPE());
		cf.setDESCRIPTION(c.getDESCRIPTION());
		cf.setRowitem_desc(c.getRowitem_desc());
		cf.setColitem_desc(c.getColitem_desc());
		cf.setLOGIC_NAME(c.getLOGIC_NAME());
		cf.setEntityId(entityId);
		cf.setEntityName(entityName);
		cf.setROWNUMDTL(fjNum);
		
		return cf;
	}

}
