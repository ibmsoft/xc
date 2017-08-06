package com.xzsoft.xc.gl.service.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xzsoft.xc.gl.dao.GlDataTransgerManageDao;
import com.xzsoft.xc.gl.modal.AccountTemplate;
import com.xzsoft.xc.gl.service.GlDataTransgerManageService;
@Service("glDataTransgerManageService")
public class GlDataTransgerManageServiceImpl implements
		GlDataTransgerManageService {
	@Resource
	private GlDataTransgerManageDao glDataTransgerManageDao;
	@Resource
	private JdbcTemplate jdbcTemplate;
	@SuppressWarnings("serial")
	private final static HashMap<String, Integer> modelMap = new HashMap<String, Integer>(){
		//具有树形结构的模板，需要上级编码的模板UP_CODE,科目表中包含了UP_ACC_CODE
		{   put("XC_GL_ACCOUNTS", 1);
			put("XC_GL_CASH_ITEMS", 1);
			put("XC_PM_PROJECTS", 1);
			put("XC_GL_LD_CUST_ASS_SEGVALS", 1);
		    put("XC_EX_ITEMS", 1);
		    put("XC_BG_ITEMS", 1);
		}
	};
	@SuppressWarnings("serial")
	private final static HashMap<String, String> orderByMap = new HashMap<String, String>(){
		//具有树形结构的模板，需要上级编码的模板UP_CODE,科目表中包含了UP_ACC_CODE
		{   put("XC_GL_ACCOUNTS", "ACC_CODE");
		    put("XC_GL_CASH_ITEMS", "CA_CODE");
		    put("XC_PM_PROJECTS", "PROJECT_CODE");
		    put("XC_GL_LD_CUST_ASS_SEGVALS", "LD_CUST_SEGVAL_CODE");
		    put("XC_AP_VENDORS", "VENDOR_CODE");
		    put("XC_GL_ACC_GROUP", "ACC_GROUP_CODE");
		    put("XC_AR_CUSTOMERS", "CUSTOMER_CODE");
		    put("XC_GL_DIMENSIONS", "DIM_CODE");
		    put("XC_GL_PRODUCTS", "PRODUCT_CODE");
		    put("XC_EX_ITEMS", "EX_ITEM_CODE");
		    put("XC_BG_ITEMS", "BG_ITEM_CODE");
		}
	};
	@Override
	public List<AccountTemplate> getTemplateByCode(String templateCode)
			throws Exception {
		return glDataTransgerManageDao.getTemplateByCode(templateCode);
	}
	/*
	 *根据模板编码获取要导入表的校验数据 
	 */
	public List<HashMap> getTemplateDataByCode(String templateCode)
			throws Exception {
		return glDataTransgerManageDao.getTemplateDataByCode(templateCode);
	}
	/*
	 *插入数据到临时表中
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class,RuntimeException.class})
	public void insertDataIntoTmp(String sessionId,final List<HashMap> dataList, final String templateCode,final List<String> colNameList,final HashMap<String, String> map)
			throws Exception {
		//辅助段临时表表名与实际表名有出入
		String tmpTable = templateCode.toUpperCase()+"_TMP";
		if("XC_GL_LD_CUST_ASS_SEGVALS".equalsIgnoreCase(templateCode))
			tmpTable = "XC_GL_LD_CUST_SEGVALS_TMP";
		//插入数据到临时表临时表之前，先删除本次导入失败的数据
		String delSql = "delete from "+tmpTable+"  where SESSION_ID=?";
		jdbcTemplate.update(delSql, new Object[]{sessionId}, new int[]{java.sql.Types.VARCHAR});
		//批量插入数据到临时表
		StringBuffer insertSql = new StringBuffer();
		insertSql.append(" insert into ").append(tmpTable).append("(");
		StringBuffer valueSql = new StringBuffer();
		for(String t:colNameList){
			insertSql.append(t).append(",");
			valueSql.append("?,");
		}
		insertSql.deleteCharAt(insertSql.length()-1);
		valueSql.deleteCharAt(valueSql.length()-1);
		insertSql.append(")  values(").append(valueSql.toString()).append(")");
		jdbcTemplate.batchUpdate(insertSql.toString(), new BatchPreparedStatementSetter(){
			@Override
			public int getBatchSize() {
				return dataList.size();
			}
			//根据列的类型设置批量操作的参数
			@Override
			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				//取出每一行记录
				HashMap unitMap = dataList.get(i);
				//设置每一行的对应列的参数类型
				for(int j = 0;j<colNameList.size();j++){
					String colName = colNameList.get(j);
					String colType = map.get(colName);
					if("VARCHAR".equalsIgnoreCase(colType) || "NVARCHAR".equalsIgnoreCase(colType)){
						ps.setString(j+1, unitMap.get(colName).toString());
					}else if("NUMERIC".equalsIgnoreCase(colType) || "DECIMAL".equalsIgnoreCase(colType)){
						ps.setDouble(j+1, (double)unitMap.get(colName));
					}else if("TIMESTAMP".equalsIgnoreCase(colType) || "DATETIME".equalsIgnoreCase(colType) || "DATE".equalsIgnoreCase(colType)){
						Date date = (Date)unitMap.get(colName);
						ps.setDate(j+1, new java.sql.Date(date.getTime()));
					}else if("INTEGER".equalsIgnoreCase(colType)){
						ps.setInt(j+1,(int)unitMap.get(colName));
					}else{
						//当成字符串处理
						ps.setString(j+1, unitMap.get(colName).toString());
					}
				}
			}
		});
	}
	/*
	 *校验导入数据的合法性 
	 * 
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class,RuntimeException.class})
	public String checkImportValid(String sessionId,String templateCode,HashMap<String, String> paramsMap) throws Exception {
		//平级数据  只校验编码名称是否为空是否重复，树级数据 还需校验是否已经发生业务数据
		int invalidNum =  glDataTransgerManageDao.checkImportValid(sessionId, templateCode,paramsMap);
		if(invalidNum>0){
			//存在不合法数据，返回不合法信息
			return sessionId;
		}else if(modelMap.containsKey(templateCode)){
			//校验上级编码是否存在
			invalidNum =  glDataTransgerManageDao.checkUpCodeValid(sessionId, templateCode,paramsMap);
			 if(invalidNum>0){
				 return sessionId;
			 }else {
				return null;
			}
		}else{
			return null;
		}
	}
	/*
	 * 
	 * @title:       insertTmpDataToFormal
	 * @description: 将数据转存到正式表中 
	 * @author       tangxl
	 * @date         2016年5月9日
	 * @param        sessionId
	 * @return       void
	 * @throws       Exception
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class,RuntimeException.class})
	public void insertTmpDataToFormal(String sessionId, String templateCode,HashMap<String, String> paramsMap)
			throws Exception {
		glDataTransgerManageDao.insertTmpDataToFormal(sessionId, templateCode,paramsMap);
	}
	/*
	 * 
	 * @title:       getCurrentImportData
	 * @description: 获取本次导入数据的上级层次及是否为子节点信息
	 * @author       tangxl
	 * @date         2016年5月9日
	 * @param        sessionId
	 * @param        templateCode
	 * @return       void
	 * @throws       Exception
	 */
	public List<HashMap> getCurrentImportData(String sessionId,
			String templateCode) throws Exception {
		return glDataTransgerManageDao.getCurrentImportData(sessionId, templateCode);
	}
	/*
	 *更新数据层级  
	 */
	@Override
	public List<HashMap> updateDataLevel(String sessionId, String templateCode,HashMap<String, String> paramsMap)
			throws Exception {
		return glDataTransgerManageDao.updateDataLevel(sessionId, templateCode,paramsMap);
	}
	@Override
	public int getMaxCashOrder() throws Exception {
		// TODO Auto-generated method stub
		return glDataTransgerManageDao.getMaxCashOrder();
	}
	/* (non-Javadoc)
	 * @see com.xzsoft.xc.gl.service.GlDataTransgerManageService#getExportData(java.util.HashMap)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<HashMap> getExportData(HashMap<String, String> map,List<String> codeNameList,HashMap<String, String> typeMap,List<String> codeOrderList)
			throws Exception {
		//1、获取要导出模板的列
		List<AccountTemplate> columnList = glDataTransgerManageDao.getTemplateByCode(map.get("MODEL_CODE"));
		//2、拼接取数sql
		if(columnList!=null && columnList.size()>0){
			StringBuffer buffer = new StringBuffer("select ");
			for(AccountTemplate t:columnList){
				if(t.getColumnAlias() != null && !"".equals(t.getColumnAlias())){
					codeNameList.add(t.getColumnAlias());
				}else{
					codeNameList.add(t.getColumnName());
				}
				buffer.append("t.").append(t.getColumnCode()).append(", ");
				typeMap.put(t.getColumnCode(), t.getColumnType());
				codeOrderList.add(t.getColumnCode());
			}
		StringBuffer whereCauseBuffer = new StringBuffer();
		String subSql = "";
		//3、拼接条件
		  //3.0现金流项目 额外查询上级编码
		if("XC_GL_CASH_ITEMS".equalsIgnoreCase(map.get("MODEL_CODE"))){
			codeNameList.add("上级编码");
			typeMap.put("UP_CODE", "VARCHAR");
			codeOrderList.add("UP_CODE");
			subSql = " case  when (select p.CA_CODE from xc_gl_cash_items p where p.CA_ID = t.UP_ID) is null then '-1' else (select p.CA_CODE from xc_gl_cash_items p where p.CA_ID = t.UP_ID) end as \"UP_CODE\"";
			buffer.append(subSql);
		}
		  //3.1项目必选条件 账簿
		if("XC_PM_PROJECTS".equalsIgnoreCase(map.get("MODEL_CODE"))){
			codeNameList.add("上级编码");
			typeMap.put("UP_CODE", "VARCHAR");
			codeOrderList.add("UP_CODE");
			subSql = " case  when (select p.PROJECT_CODE from XC_PM_PROJECTS p where p.PROJECT_ID = t.UP_PROJECT_ID) is null then '-1' else (select p.PROJECT_CODE from XC_PM_PROJECTS p where p.PROJECT_ID = t.UP_PROJECT_ID) end as \"UP_CODE\"";
			buffer.append(subSql);
			whereCauseBuffer.append("  where t.LEDGER_ID = '").append(map.get("LEDGER_ID")).append("'");
			}
		  //3.2科目必选科目体系，科目分组和科目类别可选填,科目类别为0时，导出所有科目类别
		if("XC_GL_ACCOUNTS".equalsIgnoreCase(map.get("MODEL_CODE"))){
			whereCauseBuffer.append("  where t.ACC_HRCY_ID = '").append(map.get("ACC_HRCY_ID")).append("'");
			String accCategory = map.get("ACC_CATEGORY_CODE");
			if(!"".equals(accCategory) && !"0".equals(accCategory))
				whereCauseBuffer.append("  and t.ACC_CATEGORY_CODE = '").append(accCategory).append("'");
			String accGroupId = map.get("ACC_GROUP_ID");
			if(!"".equals(accGroupId))
				whereCauseBuffer.append("  and t.ACC_GROUP_ID = '").append(accGroupId).append("'");
		}
		  //3.3自定义辅助段，账簿以及自定义段值都是必选的
		if("XC_GL_LD_CUST_ASS_SEGVALS".equalsIgnoreCase(map.get("MODEL_CODE"))){
			codeNameList.add("上级编码");
			typeMap.put("UP_CODE", "VARCHAR");
			codeOrderList.add("UP_CODE");
			subSql = " case  when (select p.LD_CUST_SEGVAL_CODE from XC_GL_LD_CUST_ASS_SEGVALS p where p.LD_CUST_SEGVAL_ID = t.UP_CUST_SEGVAL_ID AND  p.LEDGER_ID = t.LEDGER_ID) is null then '-1' else (select p.LD_CUST_SEGVAL_CODE from XC_GL_LD_CUST_ASS_SEGVALS p where p.LD_CUST_SEGVAL_ID = t.UP_CUST_SEGVAL_ID AND  p.LEDGER_ID = t.LEDGER_ID) end as \"UP_CODE\"";
			buffer.append(subSql);
			whereCauseBuffer.append("  where t.LEDGER_ID = '").append(map.get("LEDGER_ID")).append("'");
			whereCauseBuffer.append("  and t.SEG_CODE = '").append(map.get("SEG_CODE")).append("'");
		   }
		//3.4报销-费用项目
		if("XC_EX_ITEMS".equalsIgnoreCase(map.get("MODEL_CODE"))){
			codeNameList.add("上级编码");
			typeMap.put("UP_CODE", "VARCHAR");
			codeOrderList.add("UP_CODE");
			subSql = " case  when (select p.EX_ITEM_CODE from xc_ex_items p where p.EX_ITEM_ID = t.EX_ITEM_UP_ID  and p.EX_HRCY_ID = t.EX_HRCY_ID) is null then '-1' else (select p.EX_ITEM_CODE from xc_ex_items p where p.EX_ITEM_ID = t.EX_ITEM_UP_ID  and p.EX_HRCY_ID = t.EX_HRCY_ID) end as \"UP_CODE\"";
			buffer.append(subSql);
			whereCauseBuffer.append("  where t.EX_HRCY_ID = '").append(map.get("EX_HRCY_ID")).append("'");
		}
		//3.5预算-预算项目
		if("XC_BG_ITEMS".equalsIgnoreCase(map.get("MODEL_CODE"))){
			codeNameList.add("上级编码");
			typeMap.put("UP_CODE", "VARCHAR");
			codeOrderList.add("UP_CODE");
			subSql = " case  when (select p.BG_ITEM_CODE from xc_bg_items p where p.BG_ITEM_ID = t.BG_ITEM_UP_ID  and p.BG_HRCY_ID = t.BG_HRCY_ID) is null then '-1' else (select p.BG_ITEM_CODE from xc_bg_items p where p.BG_ITEM_ID = t.BG_ITEM_UP_ID  and p.BG_HRCY_ID = t.BG_HRCY_ID) end as \"UP_CODE\"";
			buffer.append(subSql);
			whereCauseBuffer.append("  where t.BG_HRCY_ID = '").append(map.get("BG_HRCY_ID")).append("'");
		}
		//3.6非树形结构的数据或者有上级编码的数据，删除最后一个逗号，其他属性结构数据，需查出上级编码
		if("".equals(subSql))
			buffer.deleteCharAt(buffer.lastIndexOf(","));
		buffer.append("  from  ").append(map.get("MODEL_CODE")).append("  t");
		if(whereCauseBuffer!=null && !"".equals(whereCauseBuffer.toString())){
			buffer.append(whereCauseBuffer.toString());
		}
		buffer.append(" order by ").append("t.").append(orderByMap.get(map.get("MODEL_CODE"))).append("  asc");
		return glDataTransgerManageDao.getExportData(buffer.toString());
		}else{
			return null;
		}
	}
}
