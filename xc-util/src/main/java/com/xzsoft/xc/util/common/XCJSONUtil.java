package com.xzsoft.xc.util.common;

import java.util.ArrayList;
import java.util.List;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import com.xzsoft.xip.framework.util.JSONUtil;

/**
 * JSON处理通用类
 * @author lin
 *
 */
public class XCJSONUtil extends JSONUtil {

	/**
	 * 将JSON串转换为JavaBean
	 * @param jsonString
	 * @param beanCalss
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T jsonToBean(String jsonString, Class<T> beanCalss) {
        JSONObject jsonObject = JSONObject.fromObject(jsonString);
        // 处理时间函数 (由于JSONObject 不识别“yyyy-MM-dd”)
        JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[]{"yyyy-MM-dd","yyyy-MM-dd HH:mm:ss"}));
        T bean = (T) JSONObject.toBean(jsonObject, beanCalss);
        return bean;
    }  
	
	/**
	 * 将JSON串转换为JavaBean
	 * @param jsonString
	 * @param beanClass
	 * @return
	 */
    @SuppressWarnings("unchecked")
	public static <T> List<T> jsonToBeanList(String jsonString, Class<T> beanClass) {
        JSONArray jsonArray = JSONArray.fromObject(jsonString);
        JSONObject jsonObject;
        T bean;
        
        int size = jsonArray.size();
        
        List<T> list = new ArrayList<T>(size);
 
        for(int i = 0; i < size; i++){
            jsonObject = jsonArray.getJSONObject(i);
            // 处理时间函数 (由于JSONObject 不识别“yyyy-MM-dd”)
            JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[]{"yyyy-MM-dd","yyyy-MM-dd HH:mm:ss"}));
            bean = (T) JSONObject.toBean(jsonObject, beanClass);
            list.add(bean);
        }
        
        return list;
    }
}
