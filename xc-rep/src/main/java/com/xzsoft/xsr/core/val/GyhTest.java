package com.xzsoft.xsr.core.val;

import java.util.Iterator;
import java.util.Map;

public class GyhTest {
	
	/**
	 * 自定义取数函数，Java函数，每个函数最后两个参数为试运算公司ID和试运算期间ID
	 * @param param1
	 * @param param2
	 * @param param3
	 * @param param4
	 * @param entity_id
	 * @param period_id
	 * @return
	 */
	public String Test1(String param1,String param2,String param3,String param4,String entity_id,String period_id){
		System.out.println("param1===="+param1);
		System.out.println("param2===="+param2);
		System.out.println("param3===="+param3);
		System.out.println("param4===="+param4);
		System.out.println("entity_id===="+entity_id);
		System.out.println("period_id===="+period_id);
		int result=6;
		return "6";
	}
	
	/**
	 * 自定义取数函数，Java函数，每个函数最后两个参数为试运算公司ID和试运算期间ID
	 * @param param1
	 * @param param2
	 * @param param3
	 * @param param4
	 * @param entity_id
	 * @param period_id
	 * @return
	 */
	public String Test2(Map paramMap){
		Iterator it=paramMap.keySet().iterator();
		String key="";
		String value="";
		while(it.hasNext()){
			key=it.next().toString();
			value=paramMap.get(key).toString();
			System.out.println("key="+key+"<----->value="+value);
		}
		return "6";
	}
	/**
	 * 自定义取数函数，Java函数，每个函数最后两个参数为试运算公司ID和试运算期间ID
	 * @param param1
	 * @param param2
	 * @param param3
	 * @param param4
	 * @param entity_id
	 * @param period_id
	 * @return
	 */
	public String Test2(String param1,String entity_id,String period_id){
		System.out.println("param1===="+param1);
		System.out.println("entity_id===="+entity_id);
		System.out.println("period_id===="+period_id);
		int result=6;
		return "2";
	}

}
