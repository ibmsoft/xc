package com.xzsoft.xc.st.common;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xzsoft.xc.st.modal.BaseDto;
import com.xzsoft.xc.st.modal.Dto;
import com.xzsoft.xip.framework.util.StringUtil;


/**
 * @ClassName: JsonHelper 
 * @Description: json数据格式化类
 * @author panghd
 * @date 2017年1月16日 下午15:15:43 
 *
 */
public class JsonHelper {
	private static Log log = LogFactory.getLog(JsonHelper.class);

	/**
	 * 将不含日期时间格式的Java对象系列化为Json资料格式
	 * 
	 * @param pObject
	 *            传入的Java对象
	 * @return
	 */
	public static final String encodeObject2Json(Object pObject) {
		String jsonString = "[]";
		if (StringUtil.isNullOrEmpty(pObject)) {
			// log.warn("传入的Java对象为空,不能将其序列化为Json资料格式.请检查!");
		} else {
			if (pObject instanceof ArrayList) {
				JSONArray jsonArray = JSONArray.fromObject(pObject);
				jsonString = jsonArray.toString();
			} else {
				JSONObject jsonObject = JSONObject.fromObject(pObject);
				jsonString = jsonObject.toString();
			}
		}
		if (log.isInfoEnabled()) {
			log.info("序列化后的JSON资料输出:\n" + jsonString);
		}
		return jsonString;
	}

	/**
	 * 将含有日期时间格式的Java对象系列化为Json资料格式<br>
	 * Json-Lib在处理日期时间格式是需要实现其JsonValueProcessor接口,所以在这里提供一个重载的方法对含有<br>
	 * 日期时间格式的Java对象进行序列化
	 * 
	 * @param pObject
	 *            传入的Java对象
	 * @return
	 */
	public static final String encodeObject2Json(Object pObject,
			String pFormatString) {
		String jsonString = "[]";
		if (StringUtil.isNullOrEmpty(pObject)) {
			// log.warn("传入的Java对象为空,不能将其序列化为Json资料格式.请检查!");
		} else {
			JsonConfig cfg = new JsonConfig();
			cfg.registerJsonValueProcessor(java.sql.Timestamp.class,
					new JsonValueProcessorImpl(pFormatString));
			cfg.registerJsonValueProcessor(java.util.Date.class,
					new JsonValueProcessorImpl(pFormatString));
			cfg.registerJsonValueProcessor(java.sql.Date.class,
					new JsonValueProcessorImpl(pFormatString));
			if (pObject instanceof ArrayList) {
				JSONArray jsonArray = JSONArray.fromObject(pObject, cfg);
				jsonString = jsonArray.toString();
			} else {
				JSONObject jsonObject = JSONObject.fromObject(pObject, cfg);
				jsonString = jsonObject.toString();
			}
		}
		if (log.isInfoEnabled()) {
			log.info("序列化后的JSON资料输出:\n" + jsonString);
		}
		return jsonString;
	}

	/**
	 * 将分页信息压入JSON字符串
	 * 
	 * @param JSON字符串
	 * @param totalCount
	 * @return 返回合并后的字符串
	 */
	public static String encodeJson2PageJson(String jsonString,
			Integer totalCount) {
		jsonString = "{TOTALCOUNT:" + totalCount + ", ROOT:" + jsonString + "}";
		if (log.isInfoEnabled()) {
			log.info("合并后的JSON资料输出:\n" + jsonString);
		}
		return jsonString;
	}

	/**
	 * 直接将List转为分页所需要的Json资料格式
	 * 
	 * @param list
	 *            需要编码的List对象
	 * @param totalCount
	 *            记录总数
	 * @param pDataFormat
	 *            时间日期格式化,传null则表明List不包含日期时间属性
	 */
	@SuppressWarnings("rawtypes")
	public static final String encodeList2PageJson(List list,
			Integer totalCount, String dataFormat) {
		String subJsonString = "";
		if (StringUtil.isNullOrEmpty(dataFormat)) {
			subJsonString = encodeObject2Json(list);
		} else {
			subJsonString = encodeObject2Json(list, dataFormat);
		}
		String jsonString = "{TOTALCOUNT:" + totalCount + ", ROOT:"
				+ subJsonString + "}";
		return jsonString;
	}

	/**
	 * 将数据系列化为表单数据填充所需的Json格式
	 * 
	 * @param pObject
	 *            待系列化的对象
	 * @param pFormatString
	 *            日期时间格式化,如果为null则认为没有日期时间型字段
	 * @return
	 */
	public static String encodeDto2FormLoadJson(Dto pDto, String pFormatString) {
		String jsonString = "";
		String sunJsonString = "";
		if (StringUtil.isNullOrEmpty(pFormatString)) {
			sunJsonString = encodeObject2Json(pDto);
		} else {
			sunJsonString = encodeObject2Json(pDto, pFormatString);
		}
		jsonString = "{success:"
				+ (StringUtil.isNullOrEmpty(pDto.getAsString("success")) ? "true" : pDto
						.getAsString("success")) + ",data:" + sunJsonString
				+ "}";
		if (log.isInfoEnabled()) {
			log.info("序列化后的JSON资料输出:\n" + jsonString);
		}
		return jsonString;
	}

	/**
	 * 将数据系列化为表单数据填充所需的Json格式
	 * 
	 * @param pObject
	 *            待系列化的对象
	 * @param pFormatString
	 *            日期时间格式化,如果为null则认为没有日期时间型字段
	 * @return
	 */
	public static String encodeObject2FormLoadJson(Object pDto,
			String pFormatString, String ok) {
		String jsonString = "";
		String sunJsonString = "";
		if (StringUtil.isNullOrEmpty(pFormatString)) {
			sunJsonString = encodeObject2Json(pDto);
		} else {
			sunJsonString = encodeObject2Json(pDto, pFormatString);
		}
		jsonString = "{success:" + (StringUtil.isNullOrEmpty(ok) ? "true" : ok)
				+ ",data:" + sunJsonString + "}";
		if (log.isInfoEnabled()) {
			log.info("序列化后的JSON资料输出:\n" + jsonString);
		}
		return jsonString;
	}

	/**
	 * 将单一Json对象解析为DTOJava对象
	 * 
	 * @param jsonString
	 *            简单的Json对象
	 * @return dto
	 */
	public static Dto parseSingleJson2Dto(String jsonString) {
		Dto dto = new BaseDto();
		if (StringUtil.isNullOrEmpty(jsonString)) {
			return dto;
		}
		JSONObject jb = JSONObject.fromObject(jsonString);
		dto = (BaseDto) JSONObject.toBean(jb, BaseDto.class);
		return dto;
	}

	/**
	 * 将复杂Json资料格式转换为List对象
	 * 
	 * @param jsonString
	 *            复杂Json对象,格式必须符合如下契约<br>
	 *            {"1":{"name":"托尼.贾","age":"27"},
	 *            "2":{"name":"甄子丹","age":"72"}}
	 * @return List
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List parseJson2List(String jsonString) {
		List list = new ArrayList();
		JSONObject jbJsonObject = JSONObject.fromObject(jsonString);
		Iterator iterator = jbJsonObject.keySet().iterator();
		while (iterator.hasNext()) {
			Dto dto = parseSingleJson2Dto(jbJsonObject.getString(iterator
					.next().toString()));
			list.add(dto);
		}
		return list;
	}
	
	/**
	 * 将dto对象中的JSONNull转换成Null
	 * @param list
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void parseJSONNull2Null(ArrayList list){
		for(int i=0; i<list.size(); i++){
			Dto dto = (Dto) list.get(i);
			
			Iterator keyIterator = dto.keySet().iterator();
			while (keyIterator.hasNext()) {
				Object key = keyIterator.next();
				Object value = dto.get(key);
				
				if(value instanceof net.sf.json.JSONNull){
					dto.put(key.toString(), null);
				}
			}
		}
	}
}
