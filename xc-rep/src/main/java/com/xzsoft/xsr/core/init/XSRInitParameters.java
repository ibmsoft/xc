package com.xzsoft.xsr.core.init;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.xzsoft.xip.framework.util.SpringDBHelp;

/**
 * 初始化报表系统参数
 * 目前初始化：
 * 哪些功能启用redis及参数
 * @author ZhouSuRong
 * 2016-4-29
 */
public class XSRInitParameters {

	public static Map<String, Map<String, Map<String, String>>> parameterMap;
	
	public static void initParameters() {
		parameterMap = new HashMap<String, Map<String, Map<String, String>>>();
		
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
		sql.append(" select t.SUIT_ID, t.ARITH_CODE, t.ENABLE_FLAG, tp.ARITH_PARAM_CODE, tp.ARITH_PARAM_VALUE ")
		   .append(" from xsr_rep_arithmetic t left JOIN xsr_rep_arithmetic_param tp on t.ARITH_ID = tp.ARITH_ID ");
//		   .append(" from xsr_rep_arithmetic t, xsr_rep_arithmetic_param tp ")
//		   .append(" where t.ARITH_ID = tp.ARITH_ID ");
		
		try {
			con = SpringDBHelp.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(sql.toString());
			
			while(rs.next()) {
				String suitId = rs.getString("SUIT_ID");
				String arithCode = rs.getString("ARITH_CODE");
				String enableFlag = (null == rs.getString("ENABLE_FLAG"))? "N": "Y";
				String paramCode = rs.getString("ARITH_PARAM_CODE");
				String paramValue = rs.getString("ARITH_PARAM_VALUE");
				if(parameterMap.containsKey(suitId)) {
					Map<String, Map<String, String>> arithMap = parameterMap.get(suitId);
					if(arithMap.containsKey(arithCode)) {
						Map<String, String> arithParamMap = arithMap.get(arithCode);
						arithParamMap.put("ENABLE_FLAG", enableFlag);
						arithParamMap.put(paramCode, paramValue);
					} else {
						Map<String, String> arithParamMap = new HashMap<String, String>();
						arithParamMap.put("ENABLE_FLAG", enableFlag);
						arithParamMap.put(paramCode, paramValue);
						arithMap.put(arithCode, arithParamMap);
					}
				} else {
					//parameterMap中不存在此表套的参数
					Map<String, String> arithParamMap = new HashMap<String, String>();
					arithParamMap.put("ENABLE_FLAG", enableFlag);
					arithParamMap.put(paramCode, paramValue);
					Map<String, Map<String, String>> arithMap = new HashMap<String, Map<String, String>>();
					arithMap.put(arithCode, arithParamMap);
					parameterMap.put(suitId, arithMap);
				}
			}
			//测试
/*			Set<String> set = parameterMap.keySet();
			for(String s : set) {
				System.out.println(s+ " : ");
				Map<String, Map<String, String>> arithMap = parameterMap.get(s);
				Set<String> arith = arithMap.keySet();
				for(String a : arith) {
					System.out.println(a+ " : ");
					Map<String, String> arithParamMap = arithMap.get(a);
					Set<String> param = arithParamMap.keySet();
					for(String p : param) {
						System.out.println(p+ " : " + arithParamMap.get(p));
					}
				}
			} */
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			SpringDBHelp.releaseConnection(con);
		}
	}
}
