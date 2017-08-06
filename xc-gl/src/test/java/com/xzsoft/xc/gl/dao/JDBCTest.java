package com.xzsoft.xc.gl.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCTest {

	public static final String url = "jdbc:mysql://mysql.xzsoft.cc:3306/xsr";  
	public static final String driver = "com.mysql.jdbc.Driver";  
	public static final String user = "prod";  
	public static final String password = "Xzsoft2016`";  
	
	public static void main(String[] args) throws SQLException {
		Connection con = null ;
		PreparedStatement ps = null ;
		ResultSet rs = null ;
		try {
			String sql = "SELECT t.VENDOR_ID,t.VENDOR_CODE,t.VENDOR_NAME,t.START_DATE,t.END_DATE FROM xc_ap_vendors T" ;
			
			
            Class.forName(driver);//指定连接类型  
            con = DriverManager.getConnection(url, user, password);//获取连接  
            ps = con.prepareStatement(sql);//准备执行语句 
            
            rs = ps.executeQuery() ;
			
			java.sql.ResultSetMetaData rsmd = rs.getMetaData() ;
			int size = rsmd.getColumnCount();
//			for(int i=1; i<=size; i++){
//				System.out.println(rsmd.getColumnName(i));
//			}
			
			while(rs.next()){
				for(int i=1; i<=size; i++){
					String key = rsmd.getColumnName(i) ;
					String value = rs.getString(i) ;
					System.out.print(key.concat("=").concat(value).concat("; "));
				}
				System.out.println("");
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			if(rs != null) rs.close();
			if(ps != null) ps.close();
			if(con != null) con.close();
		}
	}

}
