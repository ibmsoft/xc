package com.xzsoft.xsr.core.val;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xzsoft.xsr.core.mapper.CellFormulaMapper;
import com.xzsoft.xsr.core.service.CellFormulaService;

/*
 * 自定义取数函数
 * */
@Component
public class FetchDc {
	/**
	 * 报表与云ERP系统-总账GL_BA的取数函数
	 * @param args
	 * @return
	 * @throws Exception 
	 */
	public String  getDcVal(Map paramMap) throws Exception{
		CellFormulaMapper cellFormulaMapper=(CellFormulaMapper)paramMap.get("cellFormulaMapper");
		Iterator it=paramMap.keySet().iterator();
		Map map=new HashMap();
		String key="";
		String value="";
		String cloumn="";
		String DC_ID=paramMap.get("DC_ID").toString();
		while(it.hasNext()){
			key=it.next().toString();
			value=paramMap.get(key).toString();
			cloumn=cellFormulaMapper.getDimCodeCloumn(key,DC_ID);
			if(cloumn!=null){
				map.put(cloumn,value);
			}
		}
		map.putAll(paramMap);
		String result=cellFormulaMapper.getDcVal((HashMap)map);
		//System.out.println(result);
		result=result==null?"0":result;
		return result;
	}
	/**
	 * 报表与云ERP系统-现金流量的取数函数
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public String  getDcCashVal(Map paramMap) throws Exception{
		//{cashCode=X, entity_id=20210841, periodCode=2015-12, cnyId=1, cellFormulaMapper=org.apache.ibatis.binding.MapperProxy@11893f8}
		CellFormulaMapper cellFormulaMapper=(CellFormulaMapper)paramMap.get("cellFormulaMapper");
		Map map=new HashMap();
		map.putAll(paramMap);
		StringBuffer periodSql = new StringBuffer();
		if(paramMap.containsKey("BA_TYPE")) {
			if("YTD_JE".equals(paramMap.get("BA_TYPE"))) {
				String periodCode = paramMap.get("periodCode").toString();
				String periodStart = "";
				String periodEnd = "";
				if(periodCode.length() == 4) {
					periodStart = periodCode + "-01";
					periodEnd = periodCode + "-12";
					map.put("periodCode", periodEnd);
					map.put("periodStart", periodStart);
				} else if(periodCode.length() > 4) {
					periodStart = periodCode.substring(0, periodCode.indexOf("-")) + "-01";
					map.put("periodStart", periodStart);
				}
			}
		}
		String result = cellFormulaMapper.getDcCashVal((HashMap)map);
		result=result==null?"0":result;
		return result;
	}

}
