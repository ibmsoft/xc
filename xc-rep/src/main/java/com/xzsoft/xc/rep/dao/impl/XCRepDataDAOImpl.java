package com.xzsoft.xc.rep.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.fr.base.core.UUID;
import com.xzsoft.xc.rep.dao.XCRepDataDAO;
import com.xzsoft.xc.rep.mapper.XCRepDataMapper;
import com.xzsoft.xc.rep.modal.CellValueBean;
import com.xzsoft.xc.rep.modal.RepSheetBean;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;

/**
 * @ClassName: XCRepDataDAOImpl 
 * @Description: 报表数据处理数据层接口实现类
 * @author linp
 * @date 2016年8月3日 上午9:39:39 
 *
 */
@Repository("xcRepDataDAO")
public class XCRepDataDAOImpl implements XCRepDataDAO {
	@Autowired
	private XCRepDataMapper xcRepDataMapper ;
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate ;
	@Autowired
	private JdbcTemplate jdbcTemplate ;

	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getSheetBaseInfo</p> 
	 * <p>Description: 查询报表基础信息 </p> 
	 * @param ledgerId
	 * @param periodCode
	 * @param accHrcyId
	 * @param tabOrder
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.XCRepDataDAO#getSheetBaseInfo(java.lang.String, java.lang.String, java.lang.String, int)
	 */
	@Override
	public HashMap<String,String> getSheetBaseInfo(String ledgerId,String periodCode,String accHrcyId,int tabOrder)throws Exception {
		RepSheetBean sheetBean = new RepSheetBean() ;
		sheetBean.setLedgerId(ledgerId);
		sheetBean.setPeriodCode(periodCode);
		sheetBean.setAccHrcyId(accHrcyId);
		sheetBean.setTabOrder(tabOrder);
		
		return xcRepDataMapper.getSheetBaseInfo(sheetBean) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getSheetBean</p> 
	 * <p>Description: 查询单张报表信息 </p> 
	 * @param map
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.XCRepDataDAO#getSheet(java.util.HashMap)
	 */
	@Override
	public RepSheetBean getSheetBeanByTabId(RepSheetBean sheetBean)throws Exception {
		RepSheetBean bean = xcRepDataMapper.getRepSheetByTabId(sheetBean) ;
		return bean ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getSheetBeanByTabOrder</p> 
	 * <p>Description: 模板序号查询单张报表信息 </p> 
	 * @param sheetBean
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.XCRepDataDAO#getSheetBeanByTabOrder(com.xzsoft.xc.rep.modal.RepSheetBean)
	 */
	@Override
	public RepSheetBean getSheetBeanByTabOrder(RepSheetBean sheetBean)throws Exception {
		return xcRepDataMapper.getRepSheetByTabOrder(sheetBean) ; 
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getSheetsById</p> 
	 * <p>Description: 根据报表ID查询报表基础信息 </p> 
	 * @param list
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.XCRepDataDAO#getSheetsById(java.util.List)
	 */
	@Override
	public List<RepSheetBean> getSheetsByIds(List<String> list) throws Exception {
		return xcRepDataMapper.getSheetsByIds(list) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getFixedCellValuesToList</p> 
	 * <p>Description: 按模板查询固定行指标数据 </p> 
	 * @param ledgerId
	 * @param periodCode
	 * @param tabId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.XCRepDataDAO#getFixedCellValuesToList(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<CellValueBean> getFixedCellValuesToList(String ledgerId,String periodCode,String tabId)throws Exception {
		
		HashMap<String,String> params = new HashMap<String,String>() ;
		params.put("ledgerId", ledgerId) ;
		params.put("periodCode", periodCode) ;
		params.put("tabId", tabId) ;
		
		return xcRepDataMapper.getFixedCellValuesByTab(params) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getFixedCellValuesToMap</p> 
	 * <p>Description: 按模板查询固定行指标数据，Map格式 </p> 
	 * @param ledgerId
	 * @param periodCode
	 * @param tabId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.XCRepDataDAO#getFixedCellValuesToMap(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public HashMap<String,CellValueBean> getFixedCellValuesToMap(String ledgerId,String periodCode,String tabId)throws Exception {
		HashMap<String,CellValueBean> maps = null ;
		
		List<CellValueBean> cellvalues = this.getFixedCellValuesToList(ledgerId, periodCode, tabId) ;
		
		if(cellvalues != null && cellvalues.size() >0){
			maps = new HashMap<String,CellValueBean>() ;
			for(CellValueBean cellvalue : cellvalues){
				String key = cellvalue.getRowItemId().concat("/").concat(cellvalue.getColItemId()) ;
				maps.put(key, cellvalue) ;
			}
		}
		return maps ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: saveOrUpdateRepData</p> 
	 * <p>Description: 保存或更新报表数据信息 </p> 
	 * @param sheetBean
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.XCRepDataDAO#saveOrUpdateRepData(com.xzsoft.xc.rep.modal.RepSheetBean)
	 */
	@Override
	public void saveOrUpdateRepData(RepSheetBean sheetBean, List<CellValueBean> fixedCellvalues) throws Exception {
		// 1.处理报表基础信息
		if(sheetBean.getSheetId() == null){
			// 创建报表
			sheetBean.setSheetId(UUID.randomUUID().toString());
			sheetBean.setAppStatus("A"); 
			sheetBean.setAuditStatus("A");
			sheetBean.setAuditStatusDesc("起草");
			xcRepDataMapper.insertSheet(sheetBean);
			
		}else{
			// 更新报表
			xcRepDataMapper.updateSheet(sheetBean);
		}
		
		// 2.删除报表指标数据
		xcRepDataMapper.deleteCellValues(sheetBean);
		
		// 3.批量插入指标数据信息
		if(fixedCellvalues !=null && fixedCellvalues.size()>0){
			// 定义SQL
			StringBuffer str = new StringBuffer() ;
			str.append(" INSERT INTO xc_rep_cellvalues ( ") 
				.append(" VAL_ID, LEDGER_ID, ORG_ID, PERIOD_CODE, CNY_CODE, ROWITEM_ID, COLITEM_ID, CELLV, CELLTEXTV, ") 
				.append(" CREATION_DATE, CREATED_BY, LAST_UPDATE_DATE, LAST_UPDATED_BY  ") 
				.append(" ) VALUES ( ")
				.append(" :valId, :ledgerId, :orgId, :periodCode, :cnyCode, :rowItemId, :colItemId, :cellV, :cellTextV, ")
				.append(" :creationDate, :createdBy, :lastUpdateDate, :lastUpdatedBy ")
				.append(" )");
			String sql = str.toString() ;
			
			// 定义参数
			SqlParameterSource[] batchArgs = new SqlParameterSource[fixedCellvalues.size()];
			int i = 0;
			for (CellValueBean bean : fixedCellvalues) {
				batchArgs[i] = new BeanPropertySqlParameterSource(bean);
				i++ ;
			}
			// 批量保存固定行指标数据
			namedParameterJdbcTemplate.batchUpdate(sql, batchArgs);
		}
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: batchUpdRepStatus</p> 
	 * <p>Description: 批量修改报表状态 </p> 
	 * @param sheetBean
	 * @param sheetIds
	 * @param opMode
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.XCRepDataDAO#batchUpdRepStatus(com.xzsoft.xc.rep.modal.RepSheetBean, java.util.List, java.lang.String)
	 */
	@Override
	public void batchUpdRepStatus(RepSheetBean sheetBean, List<String> sheetIds, String opMode)throws Exception {
		Date cdate = CurrentUserUtil.getCurrentDate() ;
		String userId = CurrentSessionVar.getUserId() ;
		
		switch(opMode){
		case "single" :
			sheetBean.setLastUpdateDate(cdate);
			sheetBean.setLastUpdatedBy(userId);
			
			RepSheetBean sheet = xcRepDataMapper.getRepSheetByTabOrder(sheetBean) ;
			if(sheet == null){
				throw new Exception("报表未创建不能执行锁定/解锁处理") ;
			}
			xcRepDataMapper.updRepStatus(sheetBean);
			break ;
			
		case "batch" :  // 批量修改
			HashMap<String,Object> map = new HashMap<String,Object>() ;
			map.put("appStatus", sheetBean.getAppStatus()) ;
			map.put("oldAppStatus", sheetBean.getOldAppStatus()) ;
			map.put("list", sheetIds) ;
			map.put("lastUpdatedBy", userId) ;
			map.put("lastUpdateDate", cdate) ;
			
			xcRepDataMapper.batchUpdRepStatus(map);
			break ;
			
		default :
			break ;
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: cleanReports</p> 
	 * <p>Description: 清空报表 </p> 
	 * @param sheetBean
	 * @param sheetIds
	 * @param opMode
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.XCRepDataDAO#cleanReports(com.xzsoft.xc.rep.modal.RepSheetBean, java.util.List, java.lang.String)
	 */
	@Override
	public void cleanReports(RepSheetBean sheetBean, final List<String> sheetIds, String opMode)throws Exception {
		try {
			switch(opMode){
			case "single":
				xcRepDataMapper.deleteCellValues(sheetBean);
				break ;
				
			case "batch" :
				// 定义SQL
				StringBuffer str = new StringBuffer() ;
				str.append(" delete from xc_rep_cellvalues ")
				.append(" where LEDGER_ID = '").append(sheetBean.getLedgerId()).append("'")
				.append("   and PERIOD_CODE = '").append(sheetBean.getPeriodCode()).append("'")
				.append("   and exists (select 1 from xc_rep_rm_ref rr where xc_rep_cellvalues.ROWITEM_ID = rr.ROWITEM_ID") 
				.append("   and rr.TAB_ID = (select s.TAB_ID from xc_rep_sheets s where s.SHEET_ID = ? and s.APP_STATUS = 'A') ").append(")")
				.append("   and exists (select 1 from xc_rep_cm_ref cr where xc_rep_cellvalues.COLITEM_ID = cr.COLITEM_ID")
				.append("   and cr.TAB_ID = (select s.TAB_ID from xc_rep_sheets s where s.SHEET_ID = ? and s.APP_STATUS = 'A') ").append(")")  ;
				String sql = str.toString() ;
				
				// 执行批量删除
				jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						String sheetId = sheetIds.get(i) ;
						ps.setString(1, sheetId);
						ps.setString(2, sheetId);
					}
					@Override
					public int getBatchSize() {
						return sheetIds.size();
					}
				}) ;
				break ;
				
			default :
				break ;
			}
		} catch (Exception e) {
			throw e ;
		}
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: insertSheet</p> 
	 * <p>Description: 新增报表基础表信息 </p> 
	 * @param sheetBean
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.XCRepDataDAO#insertSheet(com.xzsoft.xc.rep.modal.RepSheetBean)
	 */
	@Override
	public void insertSheet(RepSheetBean sheetBean) throws Exception {
		xcRepDataMapper.insertSheet(sheetBean);
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: updateSheet</p> 
	 * <p>Description: 更新报表基础信息 </p> 
	 * @param sheetBean
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.XCRepDataDAO#updateSheet(com.xzsoft.xc.rep.modal.RepSheetBean)
	 */
	@Override
	public void updateSheet(RepSheetBean sheetBean) throws Exception {
		xcRepDataMapper.updateSheet(sheetBean);
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: insertItemValue</p> 
	 * <p>Description: 新增指标数据 </p> 
	 * @param bean
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.XCRepDataDAO#insertItemValue(com.xzsoft.xc.rep.modal.CellValueBean)
	 */
	@Override
	public void insertItemValue(CellValueBean bean) throws Exception {
		xcRepDataMapper.insertCellValue(bean);
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: updateItemValue</p> 
	 * <p>Description: 更新指标数据 </p> 
	 * @param bean
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.XCRepDataDAO#updateItemValue(com.xzsoft.xc.rep.modal.CellValueBean)
	 */
	@Override
	public void updateItemValue(CellValueBean bean) throws Exception {
		xcRepDataMapper.updateCellValue(bean);
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: deleteItemValue</p> 
	 * <p>Description: 删除一个指标值  </p> 
	 * @param bean
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.XCRepDataDAO#deleteItemValue(com.xzsoft.xc.rep.modal.CellValueBean)
	 */
	@Override
	public void deleteItemValue(CellValueBean bean) throws Exception {
		xcRepDataMapper.deleteCellValue(bean);
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getCellValueBean</p> 
	 * <p>Description: 查询指标值信息 </p> 
	 * @param bean
	 * @return 
	 * @see com.xzsoft.xc.rep.dao.XCRepDataDAO#getCellValueBean(com.xzsoft.xc.rep.modal.CellValueBean)
	 */
	@Override
	public CellValueBean getCellValueBean(CellValueBean bean) {
		return xcRepDataMapper.getCellValueBean(bean) ;
	}
}
