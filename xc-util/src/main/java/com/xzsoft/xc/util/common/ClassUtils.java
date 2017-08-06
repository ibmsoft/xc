package com.xzsoft.xc.util.common;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @ClassName: ClassUtil 
 * @Description: POJO工具类
 * @author linp
 * @date 2016年10月20日 上午9:18:03 
 *
 */
public class ClassUtils {
	
	/**
	 * @Title: copyMapProperties 
	 * @Description: 将Map对象属性值复制到Object对象
	 * @param map
	 * @param object
	 * @return    设定文件
	 */
	public static void copyMapProperties(Map<String,Object> map, Object targetObject) throws Exception {
		BeanInfo beanInfo = Introspector.getBeanInfo(targetObject.getClass());  
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();  
		for (PropertyDescriptor property : propertyDescriptors) {  
		    String key = property.getName();  
		    if (map.containsKey(key)) {  
		        Object value = map.get(key);  
		        Method setter = property.getWriteMethod();  
		        setter.invoke(targetObject, value);  
		    }  
		} 
	}

}
