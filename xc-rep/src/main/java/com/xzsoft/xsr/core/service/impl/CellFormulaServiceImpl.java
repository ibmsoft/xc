package com.xzsoft.xsr.core.service.impl;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xzsoft.xsr.core.mapper.CellFormulaMapper;
import com.xzsoft.xsr.core.mapper.ModalsheetMapper;
import com.xzsoft.xsr.core.modal.CellData;
import com.xzsoft.xsr.core.modal.CellFormula;
import com.xzsoft.xsr.core.modal.Colitem;
import com.xzsoft.xsr.core.modal.DataDTO;
import com.xzsoft.xsr.core.modal.ModalSheet;
import com.xzsoft.xsr.core.modal.Rowitem;
import com.xzsoft.xsr.core.service.CellFormulaService;
import com.xzsoft.xsr.core.util.FormulaTranslate;
import com.xzsoft.xsr.core.util.JsonUtil;
import com.xzsoft.xsr.core.util.XSRUtil;

@Service
public class CellFormulaServiceImpl implements CellFormulaService {

	@Autowired
	CellFormulaMapper cellFormulaMapper;
	@Autowired
	ModalsheetMapper modalsheetMapper ; 

	/**
	 * 保存采集公式
	 * @param msFormatId
	 * @param sheetJson
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public String saveCellFormat(String entity_id, String sheetJson, String suitId,String userId,String dbType) throws Exception {
		//将页面的sheetJson数据转化为list
		List<Object> cells = JsonUtil.parseArray(sheetJson);
		Timestamp time = new Timestamp(new Date().getTime());

		//定义一个CellFormula的list,作为参数传入到mapper方法中
		List<CellFormula> list = new ArrayList<CellFormula>();
		//行列指标ID
		String commentArr[]=null;
		String rowitemId=null;
		String colitemId=null;
		Pattern p = Pattern.compile("^-+[ ]*-+$");
		for (int i = 0; i < cells.size(); i++) {
			Map<String, Object> cellMap = (Map<String, Object>) cells.get(i);
			commentArr=cellMap.get("comment").toString().split(",");
			if(commentArr.length==2){
				rowitemId=commentArr[0];
				colitemId=commentArr[1];
			}else{
				continue;
			}			
			CellFormula cellFormula=null;
			//判断数据库中公式是否存在
			String famulaId=cellFormulaMapper.selectIdByRC(rowitemId, colitemId,entity_id,suitId);

			if(famulaId!=null){//数据库中公式已存在，update
				if(cellMap.containsKey("result")){//如果单元格有值，表示有公式
					String formulaText=cellMap.get("result").toString();//公式
					Matcher m = p.matcher(formulaText);
					if(formulaText.equals("-")||m.matches()){
						formulaText="";
					}
					if(XSRUtil.replaceBlank(formulaText).length()>0){//去除回车换行空格和问号后，判断单元格公式是否设置
						cellFormula=new CellFormula();
						cellFormula.setFORMULA_ID(famulaId);
						cellFormula.setDATAFORMULA(formulaText);
						cellFormula.setFORMULA_DESC(translate(formulaText,suitId,dbType));//公式翻译
						cellFormula.setLAST_UPDATED_BY(userId);
						cellFormula.setLAST_UPDATE_DATE(time);
						if(formulaText.contains("APP")){
							cellFormula.setF_CALTYPE("APP");
						}else{
							cellFormula.setF_CALTYPE("REP");
						}
						cellFormulaMapper.updateByID(cellFormula);					
					}else{//删除数据库中公式
						cellFormulaMapper.deleteByRC(rowitemId, colitemId,entity_id,suitId);
					}
				}else{//如果单元格没有值，删除数据库中公式
					cellFormulaMapper.deleteByRC(rowitemId, colitemId,entity_id,suitId);
				}
			}else{//insert;如果result，则表示本单元格没有公式，不用插入数据库
				if(cellMap.containsKey("result")){
					String formulaText=cellMap.get("result").toString();//公式
					Matcher m = p.matcher(formulaText);
					if(formulaText.equals("-")||m.matches()){
						formulaText="";
					}
					if(XSRUtil.replaceBlank(formulaText).length()>0){
						cellFormula=new CellFormula();
						cellFormula.setDATAFORMULA(formulaText);
						cellFormula.setFORMULA_DESC(translate(formulaText,suitId,dbType));//公式翻译
						cellFormula.setFORMULA_ID(UUID.randomUUID().toString());
						cellFormula.setROWITEM_ID(rowitemId);
						cellFormula.setCOLITEM_ID(colitemId);
						cellFormula.setENTITY_ID(entity_id);
						cellFormula.setSUIT_ID(suitId);		
						if(formulaText.contains("APP")){
							cellFormula.setF_CALTYPE("APP");
						}else{
							cellFormula.setF_CALTYPE("REP");
						}
						cellFormula.setCREATED_BY(userId);
						cellFormula.setCREATION_DATE(time);
						cellFormula.setLAST_UPDATED_BY(userId);
						cellFormula.setLAST_UPDATE_DATE(time);
						list.add(cellFormula);	
					}	
				}
			}

		}

		if (list.size() > 0) {				
			//批量插入
			cellFormulaMapper.insertBatchCellFormat(list);
		}

		return null;	
	}
	/**
	 * 加载数据单元格
	 * @param msFormatId
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<CellData> loadValue(String msFormatId,String entity_id,String suitId,String dbType) throws Exception {
		List<CellData> list=cellFormulaMapper.loadValue(msFormatId,entity_id,suitId,dbType);
		return list;
	}


	/**
	 * 公式翻译
	 * @param formulaText 公式串
	 * @param suit_id
	 * @return
	 * @throws Exception
	 */
	@Override
	public String translate(String formulaText,String suit_id,String dbType) throws Exception {
		//事先将formulaText分解成好几个单个的公式项数组，比如()+(),分解成[(),()]
		if(formulaText.contains("GetRepVal")){
			//翻译所有的GetRepVal()公式
			String formulas1[]=new String[0];
			//取出公式串中的所有GetRepVal()公式
			String formulas[]=FormulaTranslate.getRepFormula(formulaText+" ", formulas1);
			String formula="";
			//循环翻译单个公式
			for(int i=0;i<formulas.length;i++){
				formula=formulas[i];
				//执行翻译单个Rep公式
				formulaText=formulaText.replace(formula, translateRep(formula, suit_id,dbType));
			}

		}else if(formulaText.contains("GetExpVal")){
			//或许以后需要翻译Exp公式。。。
			formulaText = formulaText.replaceAll("GetExpVal", "自定义取数");
		}
		return formulaText;
	}
	/**
	 * 翻译Rep公式
	 * @param formula  Rep公式
	 * @param suit_id
	 * @return
	 * @throws Exception
	 */
	@Override
	public String translateRep(String formula,String suit_id,String dbType) throws Exception {
		//获取科目、行指标和列指标code
		Map<String,String> map=FormulaTranslate.getAllCodes(formula);
		List<DataDTO> list=new ArrayList<DataDTO>();

		list.add(cellFormulaMapper.getPeriodByCode(suit_id, map.get("periodCode")));
		if(map.containsKey("entityCode")){
			if(!map.get("entityCode").equals("XZDW")){
				list.add(cellFormulaMapper.getEntityByCode(suit_id, map.get("entityCode")));
			}else{
				formula.replace("XZDW", "当前报表单位");
			}				
		}
		if(map.get("colCode").equals("<$$>")||map.get("rowCode").equals("<$$>")){
			list.add(new DataDTO("<$$>","|<$$>|"));
		}else{
		list.add(cellFormulaMapper.getRowByCode(suit_id,map.get("rowCode"),dbType));
		list.add(cellFormulaMapper.getColByCode(suit_id,map.get("colCode"),dbType));
		}
		if(map.containsKey("cnyCode")){
			list.add(cellFormulaMapper.getCnyByCode(suit_id,map.get("cnyCode")));
		}
		//创正则表达式的匹配串
		Object[] object	= FormulaTranslate.patternArray(list);

		String temp=formula;
		//如果公式为自定义取数公式，则不做翻译
		if(temp.indexOf("GetExpVal")>0){
			temp = temp.replaceAll("GetExpVal", "自定义取数");
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
		//end 
		temp = temp.replace("(", "【").replace(")", "】").replaceAll("\\|", "");
		return temp;
	}

	/**
	 * 试运算
	 * @param formulaText 公式串
	 * @param period_id 试运算期间
	 * @param entity_id 试运算公司
	 * @param suit_id
	 * @return
	 * @throws Exception
	 */
	@Override
	public String cellTest(String formulaText,String period_id, String entity_id,String suit_id,String cnyId,String ledgerId,String cnyCode) throws Exception {
		
		//返回公式运算式
		String equation=getArithmetic(formulaText, period_id, entity_id,suit_id,cnyId,ledgerId,cnyCode);
		//计算结果值
		String result=FormulaTranslate.evalFormula(equation);
		//返回表达式和结果值
		String resultData=equation+"="+result;
		return resultData;
	}
	/**
	 * 解析公式并,返回公式运算式
	 * @param formulaText 公式串
	 * @param period_id 试运算期间
	 * @param entity_id 试运算公司
	 * @param suit_id
	 * @return
	 * @throws Exception
	 */
	@Override
	public String getArithmetic(String formulaText,String period_id, String entity_id,String suit_id,String cnyId,String ledgerId,String cnyCode) throws Exception {
		String arr[]=new String[0];
		//指标间取数公式
		String repFormulas[]=FormulaTranslate.getRepFormula(formulaText+" ", arr);
		String repFormula="";
		String repValue="0";
		//自定义取数公式
		String expFormulas[]=FormulaTranslate.getExpFormula(formulaText+" ", arr);
		String expFormula="";
		String expValue="0";

		for(int i=0;i<repFormulas.length;i++){
			repFormula=repFormulas[i];
			repValue=getRepValue(repFormula,period_id,entity_id,suit_id,cnyId);
			repValue=repValue==null?"(0)":"("+repValue+")";
			formulaText=formulaText.replace(repFormula, repValue);
		}
		for(int i=0;i<expFormulas.length;i++){
			expFormula=expFormulas[i];
			expValue=getExpValue(expFormula,period_id,entity_id,suit_id,cnyId,ledgerId,cnyCode);
			expValue=expValue==null?"(0)":"("+expValue+")";
			formulaText=formulaText.replace(expFormula, expValue);
		}

		//四则运算表达式
		String equation=formulaText;
		
		return equation;
	}
	
	
	/**
	 * 试运算，查询Rep公式对应的数据库中的cellValue值
	 * @param formula  Rep公式
	 * @return
	 * @throws Exception
	 */
	public String getRepValue(String repFormula,String period_id, String entity_id,String suitId,String cnyId) throws Exception{
		Map<String,String> map=FormulaTranslate.getAllCodes(repFormula);
		if(map.containsKey("periodCode")){
			String periodCodeTest=cellFormulaMapper.getPeriodCodeById(period_id);
			String periodCode=null;
			/*1、如果公式里的期间是 相对值（比如 当前期间 、上月期间等），此时根据测试所选择的期间按照相对值去计算出新的期间
			 *2、如果公式里的期间是 具体指 （比如 2015-07），此时直接使用该期间，跟测试所选择的期间无关
			 */
			if(map.get("periodCode").length()<=5){
				periodCode=FormulaTranslate.getPeriodCode(period_id, periodCodeTest, map.get("periodCode"));
			}else{
				periodCode=map.get("periodCode");
			}				
			map.put("periodCode", periodCode);
			map.remove("period_id");
		}else{
			/*如果公式中不包含期间，此时单元格值的计算直接以【传递的期间】为准，而事实上这种情况会发生吗？
			 * 
			 */
			map.put("period_id", period_id);
			map.remove("periodCode");
		}
		if(!map.containsKey("entityCode")||"XZDW".equals(map.get("entityCode"))){
			map.put("entityId",entity_id);
			map.remove("entityCode");
		}		
		map.put("suitId", suitId);
		if(!map.containsKey("cnyCode")){//公式中没有币种的话，使用默认币种
			map.put("cnyId", cnyId);
			map.remove("cnyCode");
		}else{
			map.remove("cnyId");
		}
		String repValue=cellFormulaMapper.getRepValue(map);
		return repValue;
	}
	/**
	 * 试运算，查询Exp公式对应的数据库中的cellValue值
	 * @param formula  Exp公式
	 * @return
	 * @throws Exception
	 */
	public String getExpValue(String repFormula,String period_id, String entity_id,String suitId,String cnyId,String ledgerId,String cnyCode) throws Exception{
		Map<String,Object> map=FormulaTranslate.getAllParam(repFormula);

		String repValue="0";
		if(map!=null){			
			String funCode=map.get("funCode").toString();
			HashMap funMap=cellFormulaMapper.getFunInfo(funCode);//"com.xzsoft.xsr.core.util.GyhTest.Test1"
			String FUN_DB_SUB=funMap.get("FUN_DB_SUB").toString().trim();
			String funType=funMap.get("FUN_TYPE").toString();
			String FUN_CALTYPE = funMap.get("FUN_CALTYPE").toString();
			if(funType.equals("JAVA")){				
				String clzName=FUN_DB_SUB.substring(0, FUN_DB_SUB.lastIndexOf("."));//类名
				String methodName=FUN_DB_SUB.substring(FUN_DB_SUB.lastIndexOf(".")+1, FUN_DB_SUB.length());//方法名
//				if(map.containsKey("paramArr")){
//					String[] paramArr=(String[])map.get("paramArr");
//					paramArr=(String[])ArrayUtils.add(paramArr, entity_id);//参数数组添加公司ID
//					paramArr=(String[])ArrayUtils.add(paramArr, period_id);//参数数组添加期间ID
//					repValue=runJavaFun(clzName,methodName,paramArr);
//				}

				if(map.containsKey("paramMap")){
					Map paramMap=(HashMap)map.get("paramMap");
					if(FUN_CALTYPE.equals("APP")){
						String dcId = cellFormulaMapper.getDcIdByOrg(suitId,entity_id);
						if(dcId!=null){
							paramMap.put("DC_ID", dcId);
						}else{
							dcId = cellFormulaMapper.getDefaultDcId(suitId);
							paramMap.put("DC_ID",dcId);
						}
					}
					//如果函数参数中存在公司期间币种参数，则优选函数参数值
					if(!paramMap.containsKey("DW")||"XZDW".equals(paramMap.get("DW"))){
						paramMap.put("entity_id", entity_id);
						paramMap.remove("DW");
					}
					if(paramMap.containsKey("QJ")){
						String periodCodeTest=cellFormulaMapper.getPeriodCodeById(period_id);
						String periodCode=null;
						//获取确定期间...
						if(paramMap.get("QJ").toString().length()<=5){
							periodCode=FormulaTranslate.getPeriodCode(period_id, periodCodeTest, paramMap.get("QJ").toString());
						}else{
							periodCode=paramMap.get("QJ").toString();
						}				
						paramMap.put("periodCode", periodCode);
					}else{
						String periodCodeTest=cellFormulaMapper.getPeriodCodeById(period_id);
						paramMap.put("periodCode", periodCodeTest);
					}
					if(!paramMap.containsKey("BZ")){
						paramMap.put("cnyId", cnyId);
						paramMap.remove("BZ");
					}
					paramMap.put("cellFormulaMapper", cellFormulaMapper);
					
					// 处理云ＥＲＰ报表取数
					paramMap.put("ledgerId", ledgerId) ;
					paramMap.put("cnyCode", cnyCode) ;
					
					repValue=runJavaFun1(clzName,methodName,paramMap);
				}else{
					repValue="没有输入参数！";
				}				
			}else if(funType.equals("DB")){
				//获取当前数据库类型：mysql or orcla
				//执行存储过程。。。
				String funName=FUN_DB_SUB;
				//				if(map.containsKey("paramMap")){
				//					Map<String,String> paramMap1=new LinkedHashMap<String,String>();
				//					paramMap1.put("proName", proName);
				//					Map<String,String> paramMap2=(LinkedHashMap)map.get("paramMap");					
				//					paramMap2.put("entity_id", entity_id);	
				//					paramMap2.put("period_id", period_id);
				//					paramMap2.put("o_result", "a");
				//					Map<String,String> paramMap=new LinkedHashMap<String,String>();
				//					paramMap.putAll(paramMap1);
				//					paramMap.putAll(paramMap2);
				////					paramMap.put("o_result", "a");
				//					repValue=runDbPro(paramMap);
				//				}
				if(map.containsKey("params")){
					String params=map.get("params").toString();
					params=params+","+entity_id+","+period_id;
					Map<String,String> paramMap=new LinkedHashMap<String,String>();
					paramMap.put("funName", funName);
					paramMap.put("params", params);

					repValue=runDbPro(paramMap);
					repValue=paramMap.get("o_result").toString();
				}else{
					repValue="没有输入参数！";
				}	

			}				
		}		
		return repValue;
	}


	/**
	 * 执行java函数
	 * @param clzName 类名
	 * @param methodName 方法名
	 * @param paramArr 参数 数组类型
	 * @throws Exception 
	 */
	public String  runJavaFun(String clzName,String methodName,String[] paramArr) throws Exception{
		Class cls = Class.forName(clzName);  
		int len=paramArr.length;
		Class[] paramTypeArr=new Class[len];
		for(int i=0;i<len;i++){
			paramTypeArr[i]=String.class;
		}
		Method setMethod = cls.getDeclaredMethod(methodName,paramTypeArr);  
		String returnValue = (String)setMethod.invoke(cls.newInstance(), paramArr);
		return returnValue;
	}
	/**
	 * 执行java函数，第二种方式
	 * @param clzName 类名
	 * @param methodName 方法名
	 * @param paramMap 参数 map类型
	 * @throws Exception 
	 */
	public String  runJavaFun1(String clzName,String methodName,Map paramMap) throws Exception{
		Class cls = Class.forName(clzName);  
		Method setMethod = cls.getDeclaredMethod(methodName,Map.class);  
		String returnValue = (String)setMethod.invoke(cls.newInstance(), paramMap);
		return returnValue;
	}
	/**
	 * 执行存储过程函数
	 * @param paramMap 参数
	 * @throws Exception 
	 */
	public String  runDbPro(Map paramMap) throws Exception{
		String returnValue=cellFormulaMapper.testByProc(paramMap);
		return returnValue;
	}

	/**
	 * 通过行列ID获取行列指标name
	 * @param RCId 行列指标ID拼接的字符串
	 */
	@Override
	public String getRCName(String RCId,String suitId,String dbType) throws Exception {
		String idArr[]=RCId.split(",");
		String Rname=cellFormulaMapper.getRowNameById(suitId, idArr[0],dbType);
		String Cname=cellFormulaMapper.getColNameById(suitId, idArr[1],dbType);
		return Rname+"--"+Cname;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getRowAndColumnItemName</p> 
	 * <p>Description: 获取报表的行列指标名称 </p> 
	 * @param map
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xsr.core.service.CellFormulaService#getRowAndColumnItemName(java.util.HashMap)
	 */
	public String getRowAndColumnItemName(HashMap<String,String> map) throws Exception {
		// 模板基础信息
		ModalSheet modalsheet = modalsheetMapper.getModalsheetById(map.get("modalsheetId")) ;
		// 行指标信息
		Rowitem rowitem = cellFormulaMapper.getRowItemById(map.get("rowItemId")) ;
		// 列指标信息
		Colitem colitem = cellFormulaMapper.getColitemById(map.get("colItemId")) ;
		
		StringBuffer str = new StringBuffer() ;
		str.append("<b>当前单元格指标信息：</b><br/>")
		.append("<font color=blue>&nbsp;&nbsp;&nbsp;表   名：</font>").append(modalsheet.getName()).append("<br/>")
		.append("<font color=blue>&nbsp;&nbsp;&nbsp;行指标：</font>").append(rowitem.getROWITEM_NAME()).append("<br/>")
		.append("<font color=blue>&nbsp;&nbsp;&nbsp;列指标：</font>").append(colitem.getCOLITEM_NAME()).append("<br/>");
		
		return str.toString() ;
	}

}
