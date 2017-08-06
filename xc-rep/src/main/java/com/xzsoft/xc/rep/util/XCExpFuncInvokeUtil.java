package com.xzsoft.xc.rep.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import com.xzsoft.xc.rep.modal.FuncBean;
import com.xzsoft.xc.rep.modal.RepSheetBean;

/**
 * @ClassName: XCRepInvokeUtil 
 * @Description: 云ERP报表模块远程调用类
 * <p>
 * 	调用类型包括：Java反射调用、WebService远程调用和数据库存储过程调用
 * </p>
 * @author linp
 * @date 2016年8月10日 下午4:04:17 
 *
 */
public class XCExpFuncInvokeUtil {
	
	/**
	 * @Title: invokeJava 
	 * @Description: java反射调用 
	 * @param funcBean
	 * 	<p> 
	 * 		自定义函数信息 
	 * 			com.xzsoft.xc.gl.api.impl.RepCustGetDataServiceImpl.getGLBalValue
	 * </p>
	 * @param params
	 * 	<p> 自定义函数参数值信息 </p>
	 * @param map
	 * @return
	 * @throws Exception    设定文件
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String invokeJava(FuncBean funcBean,String params, RepSheetBean sheetBean) throws Exception {
		String result = "(0)" ;
		
		try {
			// 获取Java类型和方法名
			String funcBody = funcBean.getFunc() ;
			int pos = funcBody.lastIndexOf(".") ;
			String clzName = funcBody.substring(0, pos) ;
			String methodName = funcBody.substring(pos+1, funcBody.length()) ;
			
			// 参数合并
			JSONObject paramJo = JSONObject.fromObject(params) ;
			HashMap<String,Object> map = (HashMap<String, Object>) JSONObject.toBean(paramJo, Map.class) ;
			map.put("ledgerId", sheetBean.getLedgerId()) ;
			map.put("periodCode", sheetBean.getPeriodCode()) ;
			map.put("cnyCode", sheetBean.getCnyCode()) ;
			map.put("accHrcyId", sheetBean.getAccHrcyId()) ;
			
			// 执行反射调用
			Class cls = Class.forName(clzName);  
			Method method = cls.getDeclaredMethod(methodName,Map.class);  
			result = (String)method.invoke(cls.newInstance(), map);
			
		} catch (Exception e) {
			throw e ;
		}
		
		return result ;
	}
	
	/**
	 * @Title: invokeProc 
	 * @Description: 数据库存储过程调用
	 * @param funcBean
	 * @param params
	 * @param sheetBean
	 * @return
	 * @throws Exception    设定文件
	 */
	public static String invokeProc(FuncBean funcBean,String params, RepSheetBean sheetBean) throws Exception {
		String result = "(0)" ;
		return result ;
	}
	
	/**
	 * @Title: invokeWebService 
	 * @Description: WebService远程调用
	 * @param funcBean
	 * @param params
	 * @param sheetBean
	 * @return
	 * @throws Exception    设定文件
	 */
	public static String invokeWebService(FuncBean funcBean,String params, RepSheetBean sheetBean) throws Exception {
		String result = "(0)" ;
		return result ;
	}
}
