/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */
package com.cubedrive.base.utils;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

import com.google.common.base.Strings;

public class MyBatisOgnlUtil {

	public static boolean isEmpty(Object o) {
		if (o == null)
			return true;

		if (o instanceof String) {
			if (((String) o).length() == 0) {
				return true;
			}
		} else if (o instanceof Collection) {
			if (((Collection) o).isEmpty()) {
				return true;
			}
		} else if (o.getClass().isArray()) {
			if (Array.getLength(o) == 0) {
				return true;
			}
		} else if (o instanceof Map) {
			if (((Map) o).isEmpty()) {
				return true;
			}
		} else {
			return false;
		}

		return false;
	}

	/**
	 * test for Map,Collection,String,Array isNotEmpty
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isNotEmpty(Object o) {
		return !isEmpty(o);
	}
	
	public static boolean isNotNullString(String o) {
		if (o == null) return false;	
		return true;
	}
	
	public static boolean jsNotEmpty(Object o){
	    if(o ==  null || "undefined".equals(o))
	        return false;
	    return true;
	}

	public static boolean isGreaterThanZero(Number n) {
		if (n == null)
			return false;
		if (n.intValue() <= 0)
			return false;
		return true;
	}
	
	public static boolean isStringEqual(String obj1, String obj2) {
		boolean result = false;
		if (obj1.equalsIgnoreCase(obj2)) result = true;
		return result;
	}
	
	public static boolean isNotZero(String obj1) {
		boolean result = true;
		if (obj1.equalsIgnoreCase("0")) result = false;
		return result;
	}
	
	public static boolean isBothNotNull(String obj1, String obj2) {
		boolean result = false;
		if (null != obj1 && null != obj2) result = true;
		return result;
	}
	
	public static boolean isFirstNull(String obj1, String obj2) {
		boolean result = false;
		if (null == obj1 && null != obj2) result = true;
		return result;
	}
	
	public static boolean isSecondNull(String obj1, String obj2) {
		boolean result = false;
		if (null != obj1 && null == obj2) result = true;
		return result;
	}
	
	public static String matchHead(String criteria){
		if(!Strings.isNullOrEmpty(criteria)){
			return criteria+"%";
		}
		return null;
	}
	
	public static String matchTail(String criteria){
		if(!Strings.isNullOrEmpty(criteria)){
			return "%"+criteria;
		}
		return null;
	}
	
	public static String matchAnywhere(String criteria){
		if(!Strings.isNullOrEmpty(criteria)){
			return "%"+criteria+"%";
		}
		return null;
	}
}
