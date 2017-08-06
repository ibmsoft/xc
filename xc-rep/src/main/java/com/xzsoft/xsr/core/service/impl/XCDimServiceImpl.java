package com.xzsoft.xsr.core.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fr.base.core.UUID;
import com.xzsoft.xip.framework.util.SpringDBHelp;
import com.xzsoft.xsr.core.mapper.XCDimMapper;
import com.xzsoft.xsr.core.modal.DcAccount;
import com.xzsoft.xsr.core.modal.DcCaItem;
import com.xzsoft.xsr.core.modal.DcDimCus;
import com.xzsoft.xsr.core.modal.DcEtlKeyValue;

@Service
public class XCDimServiceImpl {
	private static Logger log = Logger.getLogger(XCDimServiceImpl.class
			.getName());
	@Autowired
	private XCDimMapper xcDimMapper;

	/**
	 * 接收科目维度
	 * 
	 * @param dataJson
	 * @param dcId
	 * @param synchronizedDate
	 * @param xsrDateValue
	 * @return
	 */
	public String receiveKMDim(Object dataJson, String dcId,
			String synchronizedDate, String xsrDateValue, String kmHrcyCode) {
		StringBuilder returnStr = new StringBuilder();
		Timestamp time = new Timestamp(new Date().getTime());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JSONArray json = JSONArray.fromObject(dataJson);
		List<DcAccount> accountList = (List<DcAccount>) JSONArray.toCollection(
				json, DcAccount.class);

		List<DcAccount> insertAccountList = new ArrayList<>();
		List<DcAccount> updateAccountList = new ArrayList<>();

		Connection con = null;

		try {
			if(accountList.size() > 0) {
				for (DcAccount account : accountList) {
					// 判断科目是否在本系统中存在
					account.setLedgerCode(kmHrcyCode); //将arr1字段记录为科目体系code
					DcAccount existDcAccount = xcDimMapper.getDcAccountByCode(dcId,
							account.getCode(), account.getLedgerCode());
					account.setACC_CLASS("BA");
					account.setLAST_UPDATE_DATE(time);
					account.setDC_ID(dcId);
					if("-1".equals(account.getUpCode())) {
						account.setUpCode("BA");
					}
					if (null != existDcAccount) {
						updateAccountList.add(account);
					} else {
						account.setACC_ID(UUID.randomUUID().toString());
						account.setCREATION_DATE(time);
						insertAccountList.add(account);
					}
				}
			}

			con = SpringDBHelp.getConnection();
			con.setAutoCommit(false);
/*System.out.println("开始时间： " + sdf.format(time));
System.out.println("updateAccountList.size: " + updateAccountList.size());
for (DcAccount d : updateAccountList) {
	System.out.println(d.toString());
} 
System.out.println("insertAccountList.size: "+ insertAccountList.size());
for (DcAccount d : insertAccountList) {
	System.out.println(d.toString());
}*/  
			// 批量更新科目
			if (updateAccountList.size() > 0) {
				bthUpdateAccount(con, updateAccountList);
			}

            //批量插入科目
			if (insertAccountList.size() > 0) {
				bthInsertAccount(con, insertAccountList);
			}
			// 科目操作完成后修改最新时间
			DcEtlKeyValue keyValue = new DcEtlKeyValue();
			keyValue.setDC_ID(dcId);
			keyValue.setENTITY_ID(kmHrcyCode);
			keyValue.setETL_KEY_TYPE("DATE");
			keyValue.setETL_KEY_MOD("KM");
			Timestamp dateValue = Timestamp.valueOf(synchronizedDate);
			keyValue.setDATE_VALUE(dateValue);
			keyValue.setLAST_UPDATE_DATE(time);

			if ("1900-01-01 00:00:00".equals(xsrDateValue)) {
				keyValue.setCREATION_DATE(time);
				keyValue.setETL_KEY_ID(UUID.randomUUID().toString());
				insertEtlKeyValue(con, keyValue);
			} else {
				updateEtlKeyValue(con, keyValue);
			}

			con.commit();
			
			returnStr.append("接收科目体系:").append(kmHrcyCode).append(" 下的科目成功!");
		} catch (Exception e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				log.error("接收科目体系"+kmHrcyCode+"异常,回滚错:", e1);
			}
			log.error("接收科目体系"+kmHrcyCode+"出现异常:", e);
			throw new RuntimeException(e);
		} finally {
			SpringDBHelp.releaseConnection(con);
		}
		return returnStr.toString();
	}

	/**
	 * 批量更新科目
	 * 
	 * @param con
	 * @param updateAccountList
	 * @throws Exception
	 */
	private void bthUpdateAccount(Connection con,
			List<DcAccount> updateAccountList) throws Exception {

		StringBuilder sql = new StringBuilder();
		sql.append(" update xsr_dc_account ")
		    .append(" set ACC_NAME = ? ,")
			.append(" PARENT_CODE = ? ,")
			.append(" ACC_CLASS = ? ,")
			.append(" LAST_UPDATE_DATE = ? ")
			.append(" where ACC_CODE = ? ")
			.append(" and DC_ID = ? ")
			.append(" and ATTR1 = ? ");
//System.out.println(sql.toString());
		PreparedStatement pst = con.prepareStatement(sql.toString());
		int i = 0;
		for (DcAccount a : updateAccountList) {
			pst.setString(1, a.getName()); // ACC_NAME
			pst.setString(2, a.getUpCode()); // PARENT_CODE
			pst.setString(3, a.getACC_CLASS()); // ACC_CLASS
			pst.setTimestamp(4, a.getLAST_UPDATE_DATE()); // LAST_UPDATE_DATE
			pst.setString(5, a.getCode()); // ACC_CODE
			pst.setString(6, a.getDC_ID()); // DC_ID
			pst.setString(7, a.getLedgerCode());// ATTR1

			pst.addBatch();
			i++;

			if (i % 1000 == 0) {
				pst.executeBatch();
			}
		}
		pst.executeBatch();

		pst.close();
	}

	/**
	 * 批量插入科目
	 * 
	 * @param con
	 * @param insertAccountList
	 * @throws Exception
	 */
	private void bthInsertAccount(Connection con,
			List<DcAccount> insertAccountList) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" insert into xsr_dc_account")
				.append("(ACC_ID, DC_ID, ACC_CODE, ACC_NAME, PARENT_CODE, ACC_CLASS, ATTR1, CREATION_DATE, LAST_UPDATE_DATE) ")
				.append("values(?, ?, ?, ?, ?, ?, ?, ?, ?)");
//System.out.println(sql.toString());
		PreparedStatement pst = con.prepareStatement(sql.toString());
		int i = 0;
		for (DcAccount a : insertAccountList) {
			pst.setString(1, a.getACC_ID()); // ACC_ID
			pst.setString(2, a.getDC_ID()); // DC_ID
			pst.setString(3, a.getCode()); // ACC_CODE
			pst.setString(4, a.getName()); // ACC_NAME
			pst.setString(5, a.getUpCode()); // PARENT_CODE
			pst.setString(6, a.getACC_CLASS()); // ACC_CLASS
			pst.setString(7, a.getLedgerCode()); // ATTR1
			pst.setTimestamp(8, a.getCREATION_DATE()); // CREATION_DATE
			pst.setTimestamp(9, a.getLAST_UPDATE_DATE()); // LAST_UPDATE_DATE

			pst.addBatch();
			i++;

			if (i % 1000 == 0) {
				pst.executeBatch();
			}
		}
		pst.executeBatch();

		pst.close();
	}

	/**
	 * 接收维度
	 * 
	 * @param json
	 * @return
	 */
	public String receiveCusDim(Object dataJson, String dcId, String entityId,
			String synchronizedDate, String xsrDateValue, String tableName,
			String keyMod) {
		StringBuilder returnStr = new StringBuilder();
		Timestamp time = new Timestamp(new Date().getTime());
		JSONArray json = JSONArray.fromObject(dataJson);
		List<DcDimCus> dimList = (List<DcDimCus>) JSONArray.toCollection(json,
				DcDimCus.class);
//System.out.println(keyMod + "的总记录数为：" + dimList.size());
		List<DcDimCus> insertDimList = new ArrayList<>();
		List<DcDimCus> updateDimList = new ArrayList<>();

		Connection con = null;

		try {
			con = SpringDBHelp.getConnection();
			con.setAutoCommit(false);
			if(dimList.size() > 0) {
				for (DcDimCus dim : dimList) {
					// 判断维度是否在本系统中存在
					DcDimCus existDcDim = xcDimMapper.getDcDimCusByCode(dcId,
							dim.getCode(), tableName, dim.getLedgerCode());
					dim.setLAST_UPDATE_DATE(time);
					dim.setDC_ID(dcId);
					if (null != existDcDim) {
						updateDimList.add(dim);
					} else {
						dim.setCREATION_DATE(time);
						dim.setDIM_CUS_ID(UUID.randomUUID().toString());
						insertDimList.add(dim);
					}
				}
			}

			// 批量更新维度
			if (updateDimList.size() > 0) {
				bthUpdateCusDim(con, updateDimList, tableName);
			}
			// 批量插入维度
			if (insertDimList.size() > 0) {
				bthInsertCusDim(con, insertDimList, tableName);
			}

			// 维度操作完成后修改最新时间
			DcEtlKeyValue keyValue = new DcEtlKeyValue();
			keyValue.setDC_ID(dcId);
			keyValue.setENTITY_ID(entityId);
			keyValue.setETL_KEY_TYPE("DATE");
			keyValue.setETL_KEY_MOD(keyMod);
			Timestamp dateValue = Timestamp.valueOf(synchronizedDate);
			keyValue.setDATE_VALUE(dateValue);
			keyValue.setLAST_UPDATE_DATE(time);
			if ("1900-01-01 00:00:00".equals(xsrDateValue)) {
				keyValue.setCREATION_DATE(time);
				keyValue.setETL_KEY_ID(UUID.randomUUID().toString());
				insertEtlKeyValue(con, keyValue);
			} else {
				updateEtlKeyValue(con, keyValue);
			}
			con.commit();
			
			returnStr.append("接收公司:").append(entityId).append(",维度:").append(keyMod).append(",成功!");
		} catch (Exception e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				log.error("接收公司"+entityId+"维度"+keyMod+"异常,回滚错:", e1);
			}
			log.error("接收公司"+entityId+"维度"+keyMod+"出现异常:", e);
			throw new RuntimeException(e);
		} finally {
			SpringDBHelp.releaseConnection(con);
		}
		return returnStr.toString();
	}

	/**
	 * 批量更新维度
	 * 
	 * @param con
	 * @param updateDimList
	 * @param tableName
	 * @throws Exception
	 */
	private void bthUpdateCusDim(Connection con, List<DcDimCus> updateDimList,
			String tableName) throws Exception {

		StringBuilder sql = new StringBuilder();
		sql.append(" update ").append(tableName)
			.append(" set DIM_CUS_NAME = ? ,")
			.append(" PARENT_CODE = ? ,")
			.append(" LAST_UPDATE_DATE = ? ")
			.append(" where DIM_CUS_CODE = ? ")
			.append(" and DC_ID = ? ")
			.append(" and ATTR1 = ? ");
//System.out.println(sql.toString());
		PreparedStatement pst = con.prepareStatement(sql.toString());
		int i = 0;
		for (DcDimCus d : updateDimList) {
			pst.setString(1, d.getName()); // DIM_CUS_NAME
			pst.setString(2, d.getLedgerCode()); // ATTR1
			pst.setTimestamp(3, d.getLAST_UPDATE_DATE()); // LAST_UPDATE_DATE
			pst.setString(4, d.getCode()); // DIM_CUS_CODE
			pst.setString(5, d.getDC_ID()); // DC_ID
			pst.setString(6, d.getLedgerCode()); // ATTR1

			pst.addBatch();
			i++;

			if (i % 1000 == 0) {
				pst.executeBatch();
			}
		}
		pst.executeBatch();

		pst.close();
	}

	/**
	 * 批量插入维度
	 * 
	 * @param con
	 * @param insertDimList
	 * @param tableName
	 * @throws Exception
	 */
	private void bthInsertCusDim(Connection con, List<DcDimCus> insertDimList,
			String tableName) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" insert into ")
				.append(tableName)
				.append("(DIM_CUS_ID, DC_ID, DIM_CUS_CODE, DIM_CUS_NAME, PARENT_CODE, ATTR1, CREATION_DATE, LAST_UPDATE_DATE) ")
				.append("values(?, ?, ?, ?, ?, ?, ?, ?)");
//System.out.println(sql.toString());
		PreparedStatement pst = con.prepareStatement(sql.toString());
		int i = 0;
		for (DcDimCus d : insertDimList) {
			pst.setString(1, d.getDIM_CUS_ID()); // DIM_CUS_ID
			pst.setString(2, d.getDC_ID()); // DC_ID
			pst.setString(3, d.getCode()); // DIM_CUS_CODE
			pst.setString(4, d.getName()); // DIM_CUS_NAME
			pst.setString(5, d.getUpCode()); // PARENT_CODE
			pst.setString(6, d.getLedgerCode()); // ATTR1
			pst.setTimestamp(7, d.getCREATION_DATE()); // CREATION_DATE
			pst.setTimestamp(8, d.getLAST_UPDATE_DATE()); // LAST_UPDATE_DATE

			pst.addBatch();
			i++;

			if (i % 1000 == 0) {
				pst.executeBatch();
			}
		}
		pst.executeBatch();

		pst.close();
	}

	/**
	 * 插入增量时间表
	 * 
	 * @param con
	 * @param keyValue
	 * @throws Exception
	 */
	private void insertEtlKeyValue(Connection con, DcEtlKeyValue keyValue)
			throws Exception {

		StringBuilder sql = new StringBuilder();
		sql.append(" insert into xsr_dc_etl_key_value")
				.append("(ETL_KEY_ID, DC_ID, ENTITY_ID, ETL_KEY_TYPE, ETL_KEY_MOD, DATE_VALUE, CREATION_DATE, LAST_UPDATE_DATE) ")
				.append("values(?,?,?,?,?,?,?,?)");
//System.out.println(sql.toString());

		PreparedStatement pst = con.prepareStatement(sql.toString());
		pst.setString(1, keyValue.getETL_KEY_ID());
		pst.setString(2, keyValue.getDC_ID());
		pst.setString(3, keyValue.getENTITY_ID());
		pst.setString(4, keyValue.getETL_KEY_TYPE());
		pst.setString(5, keyValue.getETL_KEY_MOD());
		pst.setTimestamp(6, keyValue.getDATE_VALUE());
		pst.setTimestamp(7, keyValue.getCREATION_DATE());
		pst.setTimestamp(8, keyValue.getLAST_UPDATE_DATE());
		pst.execute();

		pst.close();
	}

	/**
	 * 更新增量时间表
	 * 
	 * @param con
	 * @param keyValue
	 * @throws Exception
	 */
	private void updateEtlKeyValue(Connection con, DcEtlKeyValue keyValue)
			throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" update xsr_dc_etl_key_value ")
				.append(" set DATE_VALUE = ?, LAST_UPDATE_DATE = ? ")
				.append(" where DC_ID = ? and ENTITY_ID = ? and ETL_KEY_TYPE = ? and ETL_KEY_MOD = ? ");
//System.out.println(sql.toString());

		PreparedStatement pst = con.prepareStatement(sql.toString());
		pst.setTimestamp(1, keyValue.getDATE_VALUE());
		pst.setTimestamp(2, keyValue.getLAST_UPDATE_DATE());
		pst.setString(3, keyValue.getDC_ID());
		pst.setString(4, keyValue.getENTITY_ID());
		pst.setString(5, keyValue.getETL_KEY_TYPE());
		pst.setString(6, keyValue.getETL_KEY_MOD());
		pst.execute();

		pst.close();
	}
	
	/**
	 * 接收现金流量项目
	 * 
	 * @param dataJson
	 * @param dcId
	 * @param synchronizedDate
	 * @param xsrDateValue
	 * @return
	 */
	public String receiveCaItem(Object dataJson, String dcId,
			String synchronizedDate, String xsrDateValue) {
		StringBuilder returnStr = new StringBuilder();
		Timestamp time = new Timestamp(new Date().getTime());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JSONArray json = JSONArray.fromObject(dataJson);
		List<DcCaItem> caitemList = (List<DcCaItem>) JSONArray.toCollection(
				json, DcCaItem.class);

		List<DcCaItem> insertItemList = new ArrayList<>();
		List<DcCaItem> updateItemList = new ArrayList<>();

		Connection con = null;

		try {
			if(caitemList.size() > 0) {
				for (DcCaItem item : caitemList) {
					// 判断科目是否在本系统中存在
					item.setLedgerCode("-1"); //现金流量项目都将arr1字段记录为-1
					DcCaItem existDcCaItem = xcDimMapper.getDcCaItemByCode(dcId, item.getCaCode());
					item.setACC_CLASS("CA");
					item.setLAST_UPDATE_DATE(time);
					item.setDC_ID(dcId);
					if("-1".equals(item.getUpCode()) || "".equals(item.getUpCode())) {
						item.setUpCode("CA");
					}
					if (null != existDcCaItem) {
						updateItemList.add(item);
					} else {
						item.setACC_ID(UUID.randomUUID().toString());
						item.setCREATION_DATE(time);
						insertItemList.add(item);
					}
				}
			}

			con = SpringDBHelp.getConnection();
			con.setAutoCommit(false);
/*System.out.println("开始时间： " + sdf.format(time));
System.out.println("updateItemList.size: " + updateItemList.size());
for (DcCaItem d : updateItemList) {
	System.out.println(d.toString());
}
System.out.println("insertItemList.size: " + insertItemList.size());
for (DcCaItem d : insertItemList) {
	System.out.println(d.toString());
}*/
			// 批量更新现金流量
			if (updateItemList.size() > 0) {
				bthUpdateCaItem(con, updateItemList);
			}

			// 批量插入科目
			if (insertItemList.size() > 0) {
				bthInsertCaItem(con, insertItemList);
			}
			// 科目操作完成后修改最新时间
			DcEtlKeyValue keyValue = new DcEtlKeyValue();
			keyValue.setDC_ID(dcId);
			keyValue.setENTITY_ID("-1");
			keyValue.setETL_KEY_TYPE("DATE");
			keyValue.setETL_KEY_MOD("CA_KM");
			Timestamp dateValue = Timestamp.valueOf(synchronizedDate);
			keyValue.setDATE_VALUE(dateValue);
			keyValue.setLAST_UPDATE_DATE(time);

			if ("1900-01-01 00:00:00".equals(xsrDateValue)) {
				keyValue.setCREATION_DATE(time);
				keyValue.setETL_KEY_ID(UUID.randomUUID().toString());
				insertEtlKeyValue(con, keyValue);
			} else {
				updateEtlKeyValue(con, keyValue);
			}

			con.commit();
			
			returnStr.append("接收现金流量项目成功!");
		} catch (Exception e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				log.error("接收现金流量项目出现异常,回滚错:", e1);
			}
			log.error("接收现金流量项目出现异常:", e);
			throw new RuntimeException(e);
		} finally {
			SpringDBHelp.releaseConnection(con);
		}
		return returnStr.toString();
	}

	/**
	 * 批量更新科目
	 * 
	 * @param con
	 * @param updateAccountList
	 * @throws Exception
	 */
	private void bthUpdateCaItem(Connection con,
			List<DcCaItem> updateItemList) throws Exception {

		StringBuilder sql = new StringBuilder();
		sql.append(" update xsr_dc_account ")
		    .append(" set ACC_NAME = ? ,")
			.append(" PARENT_CODE = ? ,")
			.append(" ACC_CLASS = ? ,")
			.append(" LAST_UPDATE_DATE = ? ,")
			.append(" DESCRIPTION = ? ,")
			.append(" ATTR2 = ? ,")
			.append(" ATTR3 = ? ,")
			.append(" ATTR4 = ? ,")
			.append(" ATTR5 = ? ")
			.append(" where ACC_CODE = ? ")
			.append(" and DC_ID = ? ")
			.append(" and ATTR1 = '-1' ");
		
//System.out.println(sql.toString());
		PreparedStatement pst = con.prepareStatement(sql.toString());
		int i = 0;
		for (DcCaItem a : updateItemList) {
			pst.setString(1, a.getCaName()); // ACC_NAME
			pst.setString(2, a.getUpCode()); // PARENT_CODE
			pst.setString(3, a.getACC_CLASS()); // ACC_CLASS
			pst.setTimestamp(4, a.getLAST_UPDATE_DATE()); // LAST_UPDATE_DATE
			pst.setString(5, a.getCaDesc()); //DESCRIPTION
			pst.setString(6, a.getCaDirection()); //ATTR2
			pst.setString(7, a.getIsLeaf()); //ATTR3
			pst.setString(8, a.getOrderby()); //ATTR4
			pst.setString(9, a.getCaLevel()); //ATTR5
			pst.setString(10, a.getCaCode()); // ACC_CODE
			pst.setString(11, a.getDC_ID()); // DC_ID

			pst.addBatch();
			i++;

			if (i % 1000 == 0) {
				pst.executeBatch();
			}
		}
		pst.executeBatch();

		pst.close();
	}

	/**
	 * 批量插入科目
	 * 
	 * @param con
	 * @param insertAccountList
	 * @throws Exception
	 */
	private void bthInsertCaItem(Connection con, List<DcCaItem> insertItemList) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" insert into xsr_dc_account")
				.append("(ACC_ID, DC_ID, ACC_CODE, ACC_NAME, PARENT_CODE, ACC_CLASS, ATTR1,")
				.append(" CREATION_DATE, LAST_UPDATE_DATE, DESCRIPTION, ATTR2, ATTR3, ATTR4, ATTR5) ")
				.append("values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

//System.out.println(sql.toString());
		PreparedStatement pst = con.prepareStatement(sql.toString());
		int i = 0;
		for (DcCaItem a : insertItemList) {
			pst.setString(1, a.getACC_ID()); // ACC_ID
			pst.setString(2, a.getDC_ID()); // DC_ID
			pst.setString(3, a.getCaCode()); // ACC_CODE
			pst.setString(4, a.getCaName()); // ACC_NAME
			pst.setString(5, a.getUpCode()); // PARENT_CODE
			pst.setString(6, a.getACC_CLASS()); // ACC_CLASS
			pst.setString(7, a.getLedgerCode()); // ATTR1
			pst.setTimestamp(8, a.getCREATION_DATE()); // CREATION_DATE
			pst.setTimestamp(9, a.getLAST_UPDATE_DATE()); // LAST_UPDATE_DATE
			pst.setString(10, a.getCaDesc()); //DESCRIPTION
			pst.setString(11, a.getCaDirection()); //ATTR2
			pst.setString(12, a.getIsLeaf()); //ATTR3
			pst.setString(13, a.getOrderby()); //ATTR4
			pst.setString(14, a.getCaLevel()); //ATTR5

			pst.addBatch();
			i++;

			if (i % 1000 == 0) {
				pst.executeBatch();
			}
		}
		pst.executeBatch();

		pst.close();
	}
}
