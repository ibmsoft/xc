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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbUtil {

	private DbUtil() {
	}

	public static void close(Connection conn) throws SQLException {
		if (conn != null) {
			conn.close();
		}
	}

	public static void close(ResultSet rs) throws SQLException {
		if (rs != null) {
			rs.close();
		}
	}

	public static void close(Statement stmt) throws SQLException {
		if (stmt != null) {
			stmt.close();
		}
	}

	public static void closeQuietly(Connection conn) {
		try {
			close(conn);
		} catch (SQLException e) { // NOPMD
			// quiet
		}
	}

	public static void closeQuietly(Connection conn, Statement stmt,
			ResultSet rs) {

		try {
			closeQuietly(rs);
		} finally {
			try {
				closeQuietly(stmt);
			} finally {
				closeQuietly(conn);
			}
		}

	}

	public static void closeQuietly(ResultSet rs) {
		try {
			close(rs);
		} catch (SQLException e) { // NOPMD
			// quiet
		}
	}

	public static void closeQuietly(Statement stmt) {
		try {
			close(stmt);
		} catch (SQLException e) { // NOPMD
			// quiet
		}
	}

	public static void commitAndClose(Connection conn) throws SQLException {
		if (conn != null) {
			try {
				conn.commit();
			} finally {
				conn.close();
			}
		}
	}

	public static void commitAndCloseQuietly(Connection conn) {
		try {
			commitAndClose(conn);
		} catch (SQLException e) { // NOPMD
			// quiet
		}
	}

	public static void rollback(Connection conn) throws SQLException {
		if (conn != null) {
			conn.rollback();
		}
	}

	public static void rollbackAndClose(Connection conn) throws SQLException {
		if (conn != null) {
			try {
				conn.rollback();
			} finally {
				conn.close();
			}
		}
	}

	public static void rollbackAndCloseQuietly(Connection conn) {
		try {
			rollbackAndClose(conn);
		} catch (SQLException e) { // NOPMD
			// quiet
		}
	}

}
