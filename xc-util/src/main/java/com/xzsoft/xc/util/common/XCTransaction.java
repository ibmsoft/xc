package com.xzsoft.xc.util.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Statement;

import com.xzsoft.xip.framework.util.SpringDBHelp;

/**
 * ClassName:XCTransaction
 * Function: 云ERP事务，每一条事务在xc_pub_locks对读对应一条记录。
 *
 * @author   GuoXiuFeng
 * @version  Ver 1.0
 * @since    Ver 1.0
 * @Date	 2016	2016年7月18日		上午11:05:18
 *
 */
public class XCTransaction {
	/**
	 * conn:TODO（数据库连接，用来实现对此库中的xc_pub_locks进行加锁）
	 *
	 * @since Ver 1.0
	 */
	private Connection conn;

	
	/**
	 * 根据默认数据源创建一个事务实例，一旦关闭事务，此实例也将关闭
	 *
	 * @throws SQLException
	 */
	public XCTransaction() throws SQLException {
		conn = SpringDBHelp.getConnection();
		conn.setAutoCommit(false);
	}

	
	/**
	 * 根据数据源名称创建一个事务实例，一旦关闭事务，此实例也将关闭
	 *
	 * @param dsName 数据源名称
	 * @throws SQLException
	 */
	public XCTransaction(String dsName) throws SQLException {
		conn = SpringDBHelp.getConnection(dsName);
		conn.setAutoCommit(false);
	}

	/**
	 * beginTransaction:(开始事务，针对xc_pub_locks表中一条数据进行加锁)
	 *
	 * @param lockCode 锁编码，此编码必须要在在xc_pub_locks中存在
	 * @throws Exception
	 * @author GuoXiuFeng
	 * @version Ver 1.0
	 * @since   Ver 1.0
	*/
	public void beginTransaction(String lockCode) throws Exception {
		Statement st = conn.createStatement();
		ResultSet rs = st
				.executeQuery("select * from XC_PUB_LOCKS where LOCK_CODE='"
						.concat(lockCode).concat("' for update"));
		if (rs.next()) {
			// do Nothing
		} else {
			throw new Exception("NO_LOCK_CODE_FOUND");
		}
	}
	/**
	 * endTransaction:(结束事务，就是回滚释放锁，并关闭连接)
	 *
	 * @throws SQLException
	 * @author GuoXiuFeng
	 * @version Ver 1.0
	 * @since   Ver 1.0
	*/
	public void endTransaction() throws SQLException {
		conn.rollback();
		conn.close();
	}
}
