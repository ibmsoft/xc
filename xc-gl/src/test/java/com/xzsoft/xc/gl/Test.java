package com.xzsoft.xc.gl;

import java.util.HashMap;

public class Test {

	public static void main(String[] args) {
		
//		HashMap<String,String> map1 = new HashMap<String,String>() ;
//		map1.put("flag", "abc") ;
//		
//		String flag = map1.get("flag") ;
		
//		HashMap<String,String> map2 = (HashMap<String, String>) map1.clone() ;
//		map2.put("flag", "abcd") ;
		
//		map1.put("flag", "abcd") ;
		
//		System.out.println(map1.get("flag")+"; "+flag);
		
//		String periodCode = "2015-04" ;
//		System.out.println(periodCode.trim().substring(0,4));
//		System.out.println(periodCode.trim().substring(5,7));
//		System.out.println();
		
		String str = "a.b.c.1" ;
		String hrcyId = str.substring(str.lastIndexOf(".")+1, str.length()) ;
		System.out.println(hrcyId);
		
		
	}
}
