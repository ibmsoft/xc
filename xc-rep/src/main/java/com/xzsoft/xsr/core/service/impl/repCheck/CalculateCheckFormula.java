package com.xzsoft.xsr.core.service.impl.repCheck;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xzsoft.xip.platform.util.PlatformUtil;
import com.xzsoft.xsr.core.mapper.RepChkMapper;
import com.xzsoft.xsr.core.modal.CheckFormula;

/**
 * 运算解析后的稽核公式
 * 校验公式是否成立
 * @author ZhouSuRong
 * @date 2016-3-3
 */
@Service
public class CalculateCheckFormula {
	private static Logger log = Logger.getLogger(CalculateCheckFormula.class.getName());
	
	@Autowired
	private RepChkMapper chkMapper;
	
	private static Map<String, String> periodModMap = new HashMap<String, String>();
	static {
		periodModMap.put("BNQC", "[本年年初]");
		periodModMap.put("SNNQM", "[上年期末(年期间)]");
		periodModMap.put("SNNTQ", "[上年年同期]");
		periodModMap.put("SNQM", "[上年期末]");
		periodModMap.put("SNTQ", "[上年同期]");
		periodModMap.put("SNTZ", "[上年同周]");
		periodModMap.put("SYBN", "[上月同期数-年内]");
		periodModMap.put("SYTQ", "[上月同期]");
		periodModMap.put("SZTQ", "[上周同期]");
	}
	
	/**
	 * 检验公式左边，公式右边逻辑关系是否成立
	 * 
	 * @param list
	 * @param modaltypeId
	 * @return 有错误的稽核公式 
	 * 返回值类型：Map<String, List<CheckFormula>> key为公司id,value为该公司的有错误的稽核公式list
	 */
	public Map<String, List<CheckFormula>> calculateFormula(List<CheckFormula> list,
			String modaltypeId) throws Exception {
		Map<String, List<CheckFormula>> resultMap = new HashMap<String, List<CheckFormula>>();
		int index;
		// 逻辑公式
		String v_iflogic1 = "";
		String v_iflogic2 = "";
		try {
			ScriptEngineManager manager = new ScriptEngineManager();
			ScriptEngine se = manager.getEngineByName("js");

			boolean isExist = false;
			double arg;
			for (CheckFormula checkformula : list) {
				if (checkformula.getLOGIC_NAME() != null 
					&& checkformula.getLOGIC_NAME().equals("IFLOGIC") 
					&& checkformula.getRightParsedStr().contains("<then>")) {
					
					index = checkformula.getRightParsedStr().indexOf("<then>");

					v_iflogic1 = checkformula.getRightParsedStr().substring(0, index);
					v_iflogic2 = checkformula.getRightParsedStr().substring(index + 6);
					if (!v_iflogic1.contains(">=") && !v_iflogic1.contains("<=") && v_iflogic1.contains("=")) {
						v_iflogic1 = v_iflogic1.replace("=", "==");
					} else if (v_iflogic1.contains("<>")) {
						v_iflogic1 = v_iflogic1.replace("<>", "!=");
					}
					
					try { 
						if ((Boolean) se.eval(v_iflogic1)) {
							if (!v_iflogic2.contains(">=") && !v_iflogic2.contains("<=") && v_iflogic2.contains("=")) {
								v_iflogic2 = v_iflogic2.replace("=", "==");
							} else if (v_iflogic2.contains("<>")) {
								v_iflogic2 = v_iflogic2.replace("<>", "!=");
							}
							
							try {
								if (!(Boolean) se.eval(v_iflogic2)) {
									checkformula.setRightValue("如果" + v_iflogic1 + "那么" + v_iflogic2);
									formulaDesc(checkformula, modaltypeId);
									
									String entityId = checkformula.getEntityId();
									if(resultMap.containsKey(entityId)) {
										resultMap.get(entityId).add(checkformula);
									} else {
										List<CheckFormula> rc = new ArrayList<CheckFormula>();
										rc.add(checkformula);
										resultMap.put(entityId, rc);
									}
								}
							} catch(Exception e) {
								checkformula.setRightValue("<font color=red>逻辑公式右边不能通过稽核，请检查公式，公式id为：" + checkformula.getCHK_FORMULA_ID()+ "</font>");
								formulaDesc(checkformula, modaltypeId);
								String entityId = checkformula.getEntityId();
								if(resultMap.containsKey(entityId)) {
									resultMap.get(entityId).add(checkformula);
								} else {
									List<CheckFormula> rc = new ArrayList<CheckFormula>();
									rc.add(checkformula);
									resultMap.put(entityId, rc);
								}
								log.error("运算: " + checkformula.getMODALSHEET_NAME() + " 的公式: " + checkformula.getCHK_FORMULA_ID() + " 右边失败，原因： " + e.getMessage());
							}
						}
					} catch(Exception e) {
						checkformula.setLeftValue("<font color=red>逻辑公式左边不能通过稽核，请检查公式，公式id为：" + checkformula.getCHK_FORMULA_ID() + "</font>");
						formulaDesc(checkformula, modaltypeId);
						String entityId = checkformula.getEntityId();
						if(resultMap.containsKey(entityId)) {
							resultMap.get(entityId).add(checkformula);
						} else {
							List<CheckFormula> rc = new ArrayList<CheckFormula>();
							rc.add(checkformula);
							resultMap.put(entityId, rc);
						}
						log.error("运算: "+checkformula.getMODALSHEET_NAME()+" 的公式: "+checkformula.getCHK_FORMULA_ID() + " 左边失败，原因： " + e.getMessage());
					}

				} else {
					if (checkformula.getLOGIC_NAME() != null && checkformula.getLOGIC_NAME().equals("GETLEN")) {
						checkformula.setLeftParsedStr(String.valueOf(checkformula.getLeftValue().length()));
					} else if (checkformula.getLOGIC_NAME() != null && checkformula.getLOGIC_NAME().equals("DECILEN")) {
						if (!isText(checkformula.getLeftValue())) {
							checkformula.setLeftParsedStr("logic失败-返回为文本值");
							break;
						} else {
							if (checkformula.getLeftValue().indexOf(".") == 0) {
								checkformula.setLeftParsedStr("0");
							} else {
								checkformula.setLeftParsedStr(String.valueOf(checkformula.getLeftValue().substring(checkformula.getLeftValue().indexOf(".") + 1).length()));
							}
						}
					} else if (checkformula.getLOGIC_NAME() != null && checkformula.getLOGIC_NAME().equals("ISBLANK")) {

						if (checkformula.getLeftValue().isEmpty() || "0".equals(checkformula.getLeftValue()) || "0.0".equals(checkformula.getLeftValue()) ) {
							checkformula.setLeftParsedStr("0");
						} else if (checkformula.getLeftValue().length() != 0) {
							checkformula.setLeftParsedStr("1");
						} else {
							checkformula.setLeftParsedStr("0");
						}
					}
					Object result = null;
					try {
						result = se.eval(checkformula.getRightParsedStr());
					} catch(Exception e) {
						checkformula.setRightValue("<font color=red>公式右边不能通过稽核，请检查公式，公式id为：" + checkformula.getCHK_FORMULA_ID() + "</font>");
						formulaDesc(checkformula, modaltypeId);
						String entityId = checkformula.getEntityId();
						if(resultMap.containsKey(entityId)) {
							resultMap.get(entityId).add(checkformula);
						} else {
							List<CheckFormula> rc = new ArrayList<CheckFormula>();
							rc.add(checkformula);
							resultMap.put(entityId, rc);
						}
						log.error("运算: "+checkformula.getMODALSHEET_NAME()+" 的公式: "+checkformula.getCHK_FORMULA_ID() + " 右边失败，原因： " + e.getMessage());
					}
					/**
					 * 1. 运算公式 0/0*1.17 ，得到结果为 NaN 2. 运算公式
					 * 232607368/0*1.17，得到结果为 Infinity 当除数是0时，将这样的公式结果排除掉了！！！
					 * **/
//System.out.println("公式id: " + checkformula.getCHK_FORMULA_ID() + " 左边: " + checkformula.getLeftParsedStr() + " 右边： " + checkformula.getRightParsedStr());  
//System.out.println("result: " + result);
					if(result != null) {
						if (!result.toString().contains("Infinity") && !result.toString().contains("NaN")) {
							/**
							 * 校验公式左右两边数据时，数字四舍五入保留两位小数后再做校验。
							 * 模式字符设置为#0.00，当数字为0.045时，数字格式化后不会变为.045
							 * DecimalFormat df = new java.text.DecimalFormat("#0.00");
							 * 如果不设置，当数字为0.045时，数字格式化后不会变成0.05
							 * df.setRoundingMode(RoundingMode.HALF_UP); 
							 * **/
							DecimalFormat df = new java.text.DecimalFormat("#0.00");
							df.setRoundingMode(RoundingMode.HALF_UP);
							
							arg = ((Double) result);
	                        double leftV = Double.parseDouble(checkformula.getLeftParsedStr());
	
//System.out.println("公式id: " + checkformula.getCHK_FORMULA_ID() + " 左边值: " + leftV + " 右边值： " + arg);  
	                      switch (checkformula.getF_EXP()) {
							case "=":
								if (Double.parseDouble(df.format(leftV)) == Double.parseDouble(df.format(arg)))
									isExist = true;
								else
									isExist = false;
								break;
							case ">":
								if (Double.parseDouble(df.format(leftV)) > Double.parseDouble(df.format(arg)))
									isExist = true;
								else
									isExist = false;
								break;
							case "<":
								if (Double.parseDouble(df.format(leftV)) < Double.parseDouble(df.format(arg)))	
									isExist = true;
								else
									isExist = false;
								break;
							case ">=":
								if (Double.parseDouble(df.format(leftV)) >= Double.parseDouble(df.format(arg)))
									isExist = true;
								else
									isExist = false;
								break;
							case "<=":
								if (Double.parseDouble(df.format(leftV)) <= Double.parseDouble(df.format(arg)))
									isExist = true;
								else
									isExist = false;
								break;
							case "<>":
								if (Double.parseDouble(df.format(leftV)) != Double.parseDouble(df.format(arg)))
									isExist = true;
								else
									isExist = false;
								break;
							}
	                      
							if (!isExist) {
								checkformula.setRightValue(String.valueOf(df.format(arg)));
								checkformula.setLeftValue(String.valueOf(df.format(leftV)));
								String banlance = df.format(leftV - arg);
								checkformula.setBanlance(banlance);

								formulaDesc(checkformula, modaltypeId);

								String entityId = checkformula.getEntityId();
								if(resultMap.containsKey(entityId)) {
									resultMap.get(entityId).add(checkformula);
								} else {
									List<CheckFormula> rc = new ArrayList<CheckFormula>();
									rc.add(checkformula);
									resultMap.put(entityId, rc);
								}
								
							}
						}
					}
					
				}
			}
  
			Set<String> keySet = resultMap.keySet();
			for(String key : keySet) {
				//System.out.println("公司： " + key + resultMap.get(key).get(0).getEntityName() + " 错误公式数量： " + resultMap.get(key).size());
				Collections.sort(resultMap.get(key));
			}
			
		} catch (Exception e) {
			log.error("运算稽核结果失败！原因：", e);
			throw new RuntimeException("运算稽核结果失败！原因："+e.getMessage());
		}

		return resultMap;
	}

	/**
	 * 有问题的稽核公式，需要获取公式中的相关中文描述 向errorFormulaDescMap(稽核结果相关公式的中文描述map)中存放中文描述及获取
	 * key:公式id&行指标id value:公式对应的中文描述
	 * 
	 * @param checkformula
	 * @param errorFormulaDescMap
	 * @param modaltypeId
	 * @param ps
	 * @return
	 */
	public CheckFormula formulaDesc(CheckFormula checkformula, String modaltypeId) throws Exception {
		/**
		 * modified by zhousr2016-2-3 整行、整列公式最后获取中文翻译时，都是一条单独的公式
		 * 需要单独获取中文翻译
		 */
		String formulaRight = checkformula.getF_RIGHT();
		if (formulaRight.contains("GetRepVal")) {
			String desc = parseRightFormula(formulaRight, modaltypeId);
			desc = desc.replace("<then>", ", 则   ");
			checkformula.setF_DESC(desc);
		} else {
			checkformula.setF_DESC(checkformula.getF_RIGHT());
		}
		return checkformula;
	}

	/**
	 * 解析公式右边,返回带有中文描述的公式串
	 * 
	 * @param formula
	 * @param modaltypeId
	 * @param ps
	 * @return
	 */
	public String parseRightFormula(String formula, String modaltypeId) throws Exception {
		String fString = formula.replaceAll("[\\t\\n\\r]", "");
		while (fString.contains("GetRepVal")) {
			fString = parseDetail(fString, modaltypeId);
		}
		return fString;
	}

	/**
	 * 解析公式右边明细,得到期间，行指标_行次，列指标_列次 的中文描述
	 * 
	 * @param formula
	 * @param modaltypeId
	 * @return
	 */
	public String parseDetail(String formula, String modaltypeId) throws Exception {
		String fString = formula;
		String fStringTemp = formula;

		StringBuffer itemDesc = new StringBuffer();

		int formulaStart = fString.indexOf("GetRepVal"); // 公式的起始位置
		int rStart = fString.indexOf("|ROW|=|"); // 行指标起始位置
		int rEnd = fString.indexOf("|,|COL|=|");
		String lastFormula = fString.substring(rEnd + 9);
		int cEnd = lastFormula.indexOf("|");
		int formulaEnd = fString.indexOf("|\")");

		int pStart = fString.indexOf("|QJ|=|"); // 期间开始位置
		String pLastFormula = fString.substring(pStart + 6); // 截取期间剩余串
		int pEnd = pLastFormula.indexOf("|"); // 期间结束位置

		String periodDesc = pLastFormula.substring(0, pEnd); // 期间名称

		String rowitemCode = fString.substring(rStart + 7, rEnd);
		String colitemCode = lastFormula.substring(0, cEnd);
		String needReplace = fString.substring(formulaStart, formulaEnd + 3);

		if (!"XZRQ".equals(periodDesc)) {
			itemDesc.append(periodModMap.containsKey(periodDesc) ? periodModMap
					.get(periodDesc) : periodDesc);
		}
		// 根据code取值
		HashMap<String, String> param = new HashMap<>();
		param.put("rowitemCode", rowitemCode);
		param.put("colitemCode", colitemCode);
		param.put("modaltypeId", modaltypeId);
		param.put("dbType", PlatformUtil.getDbType());
		try {
			itemDesc.append(chkMapper.getCheckFormulaZHDesc(param));
		} catch (Exception e) {
			log.error("解析公式右边明细,出错,原因：", e);
			throw new RuntimeException(e.getMessage() + " 解析公式右边明细,出错,原因：" + e.getMessage());
		}

		fStringTemp = fStringTemp.replace(needReplace, itemDesc);
		return fStringTemp;
	}

	private boolean isText(String leftValue) {
		try {
			Double.parseDouble(leftValue);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
