package com.xzsoft.xc.st.common;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import com.xzsoft.xip.framework.util.StringUtil;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

/**
 * @ClassName: JsonValueProcessorImpl 
 * @Description: JsonValueProcessorImpl 工具类
 * @author panghd
 * @date 2017年1月16日 下午15:15:43 
 *
 */
public class JsonValueProcessorImpl implements JsonValueProcessor {
	/**
	 * 默认的格式
	 */
	private String format = "yyyy-MM-dd HH:mm:ss";

	public JsonValueProcessorImpl() {
	};

	public JsonValueProcessorImpl(String format) {
		this.format = format;
	}

	/**
	 * 格式化数组
	 */
	public Object processArrayValue(Object value, JsonConfig jsonConfig) {
		String[] obj = {};
		if (value instanceof java.util.Date[]) {
			SimpleDateFormat sf = new SimpleDateFormat(format);
			java.util.Date[] dates = (java.util.Date[]) value;
			obj = new String[dates.length];
			for (int i = 0; i < dates.length; i++) {
				obj[i] = sf.format(dates[i]);
			}
		}
		if (value instanceof Timestamp[]) {
			SimpleDateFormat sf = new SimpleDateFormat(format);
			Timestamp[] dates = (Timestamp[]) value;
			obj = new String[dates.length];
			for (int i = 0; i < dates.length; i++) {
				obj[i] = sf.format(dates[i]);
			}
		}
		if (value instanceof java.sql.Date[]) {
			SimpleDateFormat sf = new SimpleDateFormat(format);
			java.sql.Date[] dates = (java.sql.Date[]) value;
			obj = new String[dates.length];
			for (int i = 0; i < dates.length; i++) {
				obj[i] = sf.format(dates[i]);
			}
		}
		return obj;
	}

	/**
	 * 格式化单一对象
	 */
	public Object processObjectValue(String key, Object value,
			JsonConfig jsonConfig) {
		if (StringUtil.isNullOrEmpty(value))
			return "";
		if (value instanceof Timestamp) {
			String str = new SimpleDateFormat(format).format((Timestamp) value);
			return str;
		} else if (value instanceof java.util.Date) {
			String str = new SimpleDateFormat(format)
					.format((java.util.Date) value);
			return str;
		} else if (value instanceof java.sql.Date) {
			String str = new SimpleDateFormat(format)
					.format((java.sql.Date) value);
			return str;
		}
		return value.toString();
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}
}
