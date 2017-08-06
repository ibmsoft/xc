/**
 * EnterpriseSheet - online spreadsheet solution
 * Copyright (c) FeyaSoft Inc 2014. All right reserved.
 * http://www.enterpriseSheet.com
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY
 * KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY,FITNESS FOR A PARTICULAR PURPOSE
 * AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.cubedrive.sheet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cubedrive.base.utils.DbUtil;
import com.google.common.collect.Maps;

public class SheetTableSequence {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private static final SheetTableSequence _instance = new SheetTableSequence();

	private final static String sql_create_tab_seq_table = "CREATE TABLE sheet_tab_seq "
			+ "( name VARCHAR(32) NOT NULL, seq INT(10) UNSIGNED DEFAULT '1' NOT NULL, PRIMARY KEY (name) ) ENGINE=InnoDB DEFAULT CHARSET=utf8";

	private final static String sql_exist_tab_seq_table = "select count(*) from sheet_tab_seq";

	private static final String sql_insert_or_update_seq = "insert into sheet_tab_seq(name,seq) values(?,?) ON DUPLICATE KEY UPDATE seq=?";

	private static final String sql_select_all = "select name,seq from sheet_tab_seq";

	private static final String sql_select_seq_by_name = "select seq from sheet_tab_seq where name=?";

	private static final String seq_name_spreadsheet_cell_table = "sheet_cell_table";

	private DataSource dataSource;

	private ScheduledExecutorService scheduleExecutor = Executors.newSingleThreadScheduledExecutor();

	private final Map<String, AtomicInteger> seqs = Maps.newHashMap();
	
	private SheetTableSequence(){
		
	}

	void init(DataSource dataSource) {
		this.dataSource = dataSource;
		prepare();
		buildScheduleTask();
	}

	void destroy() {
		scheduleExecutor.shutdown();
	}

	private void buildScheduleTask() {
		scheduleExecutor.scheduleAtFixedRate(new CheckTableSizeTask(), 0L, 5, TimeUnit.MINUTES);
	}

	public String sheetCellTable() {
		return SheetAppConfiguration.instance().getSheetCellTablePrefix() + seqs.get(seq_name_spreadsheet_cell_table).get();
	}

	private void prepare() {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			try{
			    checkTableExists(conn);
			}catch(SQLException ex){
			    createSheetCell1Table(conn);
			}
			initMultiTabSeqTable(conn);
			initSeqMap(conn);
		} catch (SQLException ex) {
		} finally {
			DbUtil.closeQuietly(conn);
		}
	}

	private void checkTableExists(Connection conn) throws SQLException {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			stmt.executeQuery("select count(1) from sheet_cell_1");
		} catch (SQLException ex) {
		    throw ex;
		} finally {
			DbUtil.closeQuietly(stmt);
		}
	}
	
	private void createSheetCell1Table(Connection conn){
	    Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt = conn.createStatement();
            stmt.execute("create table if not exists sheet_cell_1 like " +  SheetAppConfiguration.instance().getSheetCellTableTemplate());
            stmt.execute("alter table sheet_cell_1 add constraint foreign key (tab_id) references sheet_tab (tab_id) on delete cascade");
        } catch (SQLException ex) {
            
        }finally{
            DbUtil.closeQuietly(stmt);
        }
	}

	private void initMultiTabSeqTable(Connection conn) {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			stmt.executeQuery(sql_exist_tab_seq_table);
		} catch (SQLException ex) {
			try {
				stmt.execute(sql_create_tab_seq_table);
				stmt.executeQuery(sql_exist_tab_seq_table);
				initSpreadSheetCellTableSeq(conn);
			} catch (SQLException e) {
				throw new RuntimeException(
						"tab_seq table can't create,please check your database");
			} finally {
				DbUtil.closeQuietly(stmt);
			}
		}

	}

	private void initSeqMap(Connection conn) {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql_select_all);
			while (rs.next()) {
				seqs.put(rs.getString("name"), new AtomicInteger(rs.getInt("seq")));
			}
		} catch (SQLException ex) {
		} finally {
			DbUtil.closeQuietly(rs);
			DbUtil.closeQuietly(stmt);
		}
		if (seqs.isEmpty()) {
			throw new RuntimeException("sheet_tab_seq table is empty table");
		}

	}

	private void initSpreadSheetCellTableSeq(Connection conn) throws SQLException{
		PreparedStatement ps = null;
		try{
			ps = conn.prepareStatement(sql_insert_or_update_seq);
			ps.setString(1, seq_name_spreadsheet_cell_table);
            ps.setInt(2, 1);
            ps.setInt(3, 1);
			ps.executeUpdate();
		}finally{
			DbUtil.closeQuietly(ps);
		}		
	}

	private int seqByName(Connection conn, String name) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql_select_seq_by_name);
			ps.setString(1, name);
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt("seq");
			} else {
				throw new RuntimeException(
						"The table sheet_tab_seq has no this name:" + name);
			}
		} finally {
			DbUtil.closeQuietly(rs);
			DbUtil.closeQuietly(ps);
		}
	}

	private class CheckTableSizeTask implements Runnable {

		@Override
		public void run() {
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				conn.setAutoCommit(false);
				checkSheetCellTableSize(conn);
				conn.commit();
			} catch (SQLException ex) {
				log.error("", ex);
				if(conn != null){
					try {
						conn.rollback();
					} catch (SQLException e) {
					}
				}
			} finally {
				try {
					conn.setAutoCommit(true);
				} catch (SQLException e) {
					
				}
				DbUtil.closeQuietly(conn);
			}

		}

		private void checkSheetCellTableSize(Connection conn)
				throws SQLException {
			int currentSheetCellSeq = seqs.get(seq_name_spreadsheet_cell_table).get();
			String sheetCellTable = SheetAppConfiguration.instance().getSheetCellTablePrefix() + currentSheetCellSeq;
			String tableSizeSql = "select count(*) from " + sheetCellTable;
			Statement stmt = null;
			ResultSet rs = null;
			try {
				stmt = conn.createStatement();
				rs = stmt.executeQuery(tableSizeSql);
				int tableSize = 0;
				if (rs.next()) {
					tableSize = rs.getInt(1);
				}
				if (tableSize >= SheetAppConfiguration.instance().getSheetCellTableSize()) {
					int nextSheetTableSeq = currentSheetCellSeq + 1;
					String newSheetCellTable = SheetAppConfiguration.instance().getSheetCellTablePrefix() + nextSheetTableSeq;
					stmt.execute("create table if not exists " + newSheetCellTable + " like " +  SheetAppConfiguration.instance().getSheetCellTableTemplate());
					stmt.execute("alter table " + newSheetCellTable + " add constraint foreign key (tab_id) references sheet_tab (tab_id) on delete cascade");
					int effectRow = stmt.executeUpdate("update sheet_tab_seq set seq=" + nextSheetTableSeq + " where name='" + seq_name_spreadsheet_cell_table + "' and seq=" + currentSheetCellSeq);
					Integer resultSeq;
					if (effectRow == 1) {
						resultSeq = nextSheetTableSeq;
					} else {
						resultSeq = seqByName(conn,	seq_name_spreadsheet_cell_table);
					}
					seqs.get(seq_name_spreadsheet_cell_table).set(resultSeq);
				}
			} finally {
				DbUtil.closeQuietly(rs);
				DbUtil.closeQuietly(stmt);
			}
		}

	}

	public static SheetTableSequence instance() {
		return _instance;
	}

}
