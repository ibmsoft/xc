package com.xzsoft.xc.gl.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JsonConfig;

import com.xzsoft.xc.gl.modal.VHead;
import com.xzsoft.xc.gl.modal.VLine;
import com.xzsoft.xip.framework.util.JSONUtil;
import com.xzsoft.xip.framework.util.JsonDateValueProcessor;

public class JsonUtil {

	public static void main(String[] args) {
/*		try {
			String jsonStr = "{\"test\":\"abc\",\"lines\":[{\"id\":1},{\"id\":2}]}" ;
			
			JSONObject jo = new JSONObject(jsonStr) ;
			System.out.println(jo.getString("test"));
			
			JSONArray ja = new JSONArray(jo.getString("lines")) ;
			for(int i=0; i<ja.length(); i++){
				JSONObject obj = ja.getJSONObject(i) ;
				System.out.println(obj.getString("id"));
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}*/
		
//		String jsonStr = "{\"attchTotal\":\"1abc\",\"lines\":[{\"accountDR\":1.09},{\"accountCR\":\"\"}]}" ;
//		VHead bean = JSONUtil.jsonToBean(jsonStr, VHead.class) ;
//		System.out.println(bean.getAttchTotal());
		
		
		VHead bean = new VHead() ;
		bean.setHeadId("abc"); 
		bean.setBookDate(new Date());
		
		VLine line1 = new VLine();
		line1.setLineId("12345"); 
		
		VLine line2 = new VLine();
		line2.setLineId("23456"); 
		
		List<VLine> lines = new ArrayList<VLine>() ;
		lines.add(line1) ;
		lines.add(line2) ;
		
		bean.setLines(lines);
		
		JsonConfig cfg = new JsonConfig() ;
		String[] excludes = new String[]{"accIdList","accList","addLines"} ;
		cfg.setExcludes(excludes);   
		cfg.setIgnoreDefaultExcludes(false); 
        // 日期转换
//        if(dateFormat != null){
		cfg.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd"));
//        }
		
		String jsonStr = JSONUtil.beanToJson(bean, cfg) ;
		System.out.println(jsonStr);
		
		
//		System.out.println(bean.getTotalDR() == 0);
//		System.out.println(1.09*2.12);
		
		
//		String ccidCode = "1001/00/1001/00" ;
//		System.out.println(ccidCode.substring(0, ccidCode.indexOf("/")));
		
		
//		List<String> list = new ArrayList<String>() ;
//		list.add("abc") ;
//		list.add("bcd") ;
//		System.out.println(list.toString());
	}

}
