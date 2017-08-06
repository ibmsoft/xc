package com.xzsoft.xsr.core.service.impl;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xzsoft.xip.framework.util.SpringDBHelp;
import com.xzsoft.xip.platform.util.PlatformUtil;
import com.xzsoft.xsr.core.controller.ModalsheetController;
import com.xzsoft.xsr.core.mapper.FjMapper;
import com.xzsoft.xsr.core.mapper.MakeReportMapper;
import com.xzsoft.xsr.core.mapper.CellFormulaMapper;
import com.xzsoft.xsr.core.modal.CellData;
import com.xzsoft.xsr.core.modal.CellFormula;
import com.xzsoft.xsr.core.modal.RepSheet;
import com.xzsoft.xsr.core.modal.CellValue;
import com.xzsoft.xsr.core.service.CellFormulaService;
import com.xzsoft.xsr.core.service.FjService;
import com.xzsoft.xsr.core.service.MakeReportService;
import com.xzsoft.xsr.core.util.FormulaTranslate;
import com.xzsoft.xsr.core.util.XSRUtil;
import com.xzsoft.xsr.core.util.JsonUtil;

import org.apache.log4j.Logger;

/**
 * 报表数据采集
 * 
 * @project 报表3.0
 * @version v1.0
 * @author songyh
 * @Date 2016-01-22
 * @desc 报表生成 页页相关的Service接口实现类
 */
@Service
public class MakeReportServiceImpl implements MakeReportService {

	@Autowired
	private MakeReportMapper makeReportMapper;
	@Autowired
	private CellFormulaMapper	cellFormulaMapper;
	@Autowired
	private CellFormulaService  cellFormulaService;
	@Autowired
	private FjService fjService;
	@Resource
	private FjMapper fjmapper;
	private static Logger log = Logger.getLogger(ModalsheetController.class.getName());
	/**
	 * 保存采集公式
	 * @param msFormatId
	 * @param sheetJson
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public String collectData(String suitId, String entityId, String periodId, String currencyId, String mdlShtIdList,
			String fetchFlag, String fjFlag, String fType, // 运算类型：REP－报表运算；APP－数据采集
			String userId,String ledgerId,String cnyCode) throws Exception {
		// 得到模板ID数组
		String[] modalsheetId = mdlShtIdList.split(",");
		// 获取db类型
		String dbType = PlatformUtil.getDbType();
		HashMap params = new HashMap();
		params.put("suitId", suitId);
		params.put("ENTITY_ID", entityId);
		params.put("PERIOD_ID", periodId);
		params.put("CURRENCY_ID", currencyId);
		params.put("fType", fType);
		params.put("dbType", dbType);
		// 定义数据连接
		Connection conn = SpringDBHelp.getConnection();
		// 循环报表，依次采集
		try {
			for (int i = 0; i < modalsheetId.length; i++) {
				log.warn("功能：报表生成 --数据采集，报表ID:" + modalsheetId[i] + "执行时间："
						+ DateFormat.getDateTimeInstance().format(new Date()));
				// 1.判断有无生成报表，若没生成，则先insert报表表;若已生成，则判断是否锁定，锁定的报表不执行采集
				params.put("MODALSHEET_ID", modalsheetId[i]);
				String appStatus = makeReportMapper.getReportStatus(params);
				Timestamp time = new Timestamp(new Date().getTime());
				// 根据ID获取各维度的code值
				String modalTypeId = null;
				String modalTypeCode = null;
				String entityCode = null;
				String periodCode = null;
				String currencyCode = null;
				String modalsheetCode = null;
				List<HashMap> repCodesMap = makeReportMapper.getRepCodes(params);
				if (repCodesMap.size() > 0) {
					modalTypeId = repCodesMap.get(0).get("MODALTYPE_ID").toString();
					modalTypeCode = repCodesMap.get(0).get("MODALTYPE_CODE").toString();
					entityCode = repCodesMap.get(0).get("ENTITY_CODE").toString();
					periodCode = repCodesMap.get(0).get("PERIOD_CODE").toString();
					currencyCode = repCodesMap.get(0).get("CURRENCY_CODE").toString();
					modalsheetCode = repCodesMap.get(0).get("MODALSHEET_CODE").toString();
				}
				//
				if (null == appStatus) { // 没有报表记录，则插入
					RepSheet repsheet = new RepSheet();
					repsheet.setSHEET_ID(UUID.randomUUID().toString());// 获取UUID
					repsheet.setSUIT_ID(suitId);
					repsheet.setENTITY_ID(entityId);
					repsheet.setPERIOD_ID(periodId);
					repsheet.setCURRENCY_ID(currencyId);
					repsheet.setMODALSHEET_ID(modalsheetId[i]);
					//
					repsheet.setMODALTYPE_ID(modalTypeId);
					repsheet.setMODALTYPE_CODE(modalTypeCode);
					repsheet.setENTITY_CODE(entityCode);
					repsheet.setPERIOD_CODE(periodCode);
					repsheet.setCURRENCY_CODE(currencyCode);
					repsheet.setMODALSHEET_CODE(modalsheetCode);
					//
					repsheet.setAPP_STATUS("A");
					repsheet.setCOMMIT_STATUS("N");
					repsheet.setVERIFY_STATUS("N");
					repsheet.setIMPTYPE("0");
					repsheet.setCREATION_DATE(time);
					repsheet.setLAST_UPDATE_DATE(time);
					repsheet.setLAST_UPDATED_BY(userId);
					repsheet.setCREATED_BY(userId);
					makeReportMapper.insertReport(repsheet);
				} else if ("A".equals(appStatus)) { // 选定条件有报表，并且状态为起草，则执行报表update
					params.put("LAST_UPDATE_DATE", time);
					params.put("LAST_UPDATED_BY", userId);
					makeReportMapper.updateReport(params);
				} else {
					log.warn("功能：报表生成 --数据采集，报表ID:" + modalsheetId[i] + "已锁定，禁止采集！");
					continue; // 继续下一张采集
				}
				// 2.循环模板得到固定行采集公式，执行数据采集，并将单元格采集的数据插入指标值表
				List<CellFormula> calFormulaList = cellFormulaMapper.getRepFormulaList(params);
				List<CellValue> listUpdate = new ArrayList<CellValue>();
				List<CellValue> listAdd = new ArrayList<CellValue>();
				String rowitemCode = "";
				String colitemCode = "";
				String dataFormula = "";
				String rowitemId = "";
				String colitemId = "";
				String type = "";
				String cellV = "";
				String cellTextV = "";
				// 如果存在公式进行解析
				if (calFormulaList.size() > 0) {
					// 2.1.循环公式，执行采集运算，并将运算结果结存于list中
					for (CellFormula formula : calFormulaList) {
						rowitemCode = formula.getROWITEM_CODE();
						colitemCode = formula.getCOLITEM_CODE();
						rowitemId = formula.getROWITEM_ID();
						colitemId = formula.getCOLITEM_ID();
						dataFormula = formula.getDATAFORMULA();
						type = formula.getF_CALTYPE();
						// 调用运算函数，获取运算结果
						// String equation=getArithmetic(formulaText, period_id,
						// entity_id,suit_id,cnyId);
						String equation = cellFormulaService.getArithmetic(dataFormula, periodId, entityId, suitId,
								currencyId,ledgerId,cnyCode);

						// 获取列指标类型
						int dataType = makeReportMapper.getColDataType(suitId, colitemId);
						// 将函数运算串，生成最终计算结果值
						if (dataType == 3) { // 数据值
							cellV = FormulaTranslate.evalFormula(equation);
							cellTextV = "";
						} else // 项目列或文本值列
						{
							cellV = "";
							cellTextV = FormulaTranslate.evalFormula(equation);
						}
						HashMap params_cell = new HashMap();
						params_cell.put("suitId", suitId);
						params_cell.put("entityId", entityId);
						params_cell.put("periodId", periodId);
						params_cell.put("currencyId", currencyId);
						params_cell.put("rowItemId", rowitemId);
						params_cell.put("colItemId", colitemId);
						int rsCount = makeReportMapper.countOfCellValue(params_cell);
						CellValue cellvalue = new CellValue();
						if (rsCount == 0) {
							cellvalue.setCELLV_ID(UUID.randomUUID().toString()); // 获取uuid
						}
						cellvalue.setSUIT_ID(suitId);
						cellvalue.setENTITY_ID(entityId);
						cellvalue.setPERIOD_ID(periodId);
						cellvalue.setROWITEM_ID(rowitemId);
						cellvalue.setCOLITEM_ID(colitemId);
						cellvalue.setCURRENCY_ID(currencyId);
						cellvalue.setENTITY_CODE(entityCode);
						cellvalue.setPERIOD_CODE(periodCode);
						cellvalue.setCURRENCY_CODE(currencyCode);
						cellvalue.setROWITEM_CODE(rowitemCode);
						cellvalue.setCOLITEM_CODE(colitemCode);
						cellvalue.setCREATION_DATE(time);
						cellvalue.setCREATED_BY(userId);
						cellvalue.setCELLV(Double.parseDouble(cellV));
						cellvalue.setCELLTEXTV(cellTextV);
						cellvalue.setLAST_UPDATE_DATE(time);
						cellvalue.setLAST_UPDATED_BY(userId);
						if (rsCount > 0) {
							// 需要更新的放在listupdate中
							listUpdate.add(cellvalue);
						} else { // 需要插入的放在listAdd中
							listAdd.add(cellvalue);
						}
					}
					// 2.2.执行指标值的批量插入与更新
					if (listUpdate.size() > 0) {
						makeReportMapper.updateCellValue(listUpdate);
					}
					if (listAdd.size() > 0) {
						makeReportMapper.insertCellValue(listAdd);
					}
				}

				// 若浮动行采集标志为N，则返回执行下一张固定报表采集
				if ("N".equals(fjFlag)) {
					continue;
				}

				// 若不是浮动行模板，则返回执行下一张固定报表采集
				String fj_Flag = makeReportMapper.getModalFjFlag(suitId, modalsheetId[i], dbType);
				if ("N".equals(fj_Flag)) {
					continue;
				}

				// 循环浮动行行指标，依次取数浮动行行指标上设置的浮动指标公式，进行采集运算，并插入到xsr_rep_fj_cellvalue表
				List<HashMap> fjRowMap = makeReportMapper.getFjRowItem(suitId, modalsheetId[i]);
				for (HashMap fjRow : fjRowMap) {
					String rowItemId = fjRow.get("ROWITEM_ID").toString();
					String rowItemCode = fjRow.get("ROWITEM_CODE").toString();
					String rowNo = fjRow.get("ROWNO").toString();
					// 3.判断有无浮动行（单位级）公式，若有，执行浮动行（单位级）公式采集
					// 浮动行（单位级）采集运算（by 柳杨）
					String flag = fjService.collectFjData(conn, suitId, modalsheetId[i], modalTypeId, rowItemId,
							entityId, periodId, currencyId, rowItemCode, entityCode, periodCode, currencyCode, userId,ledgerId,cnyCode);
					//
					if ("0".equals(flag)) {
						// 4.判断有无浮动行视图取数设置，若有，执行浮动行视图取数采集（若执行浮动行（单位级）采集，则不再执行视图采集）
						// 浮动行（视图）采集运算//
						List<HashMap> fjViewMap = makeReportMapper.getFjRowView(suitId, modalsheetId[i], rowItemId);
						if (fjViewMap.size() > 0) {
							// 执行视图采集前，先删除原浮动行数据
							int cnt = fjmapper.delFjData(suitId, entityId, modalsheetId[i], rowItemId, periodId,
									currencyId); // 删除原数据

							// r.DATASRC,r.PRE_PKG,r.AFT_PKG,r.WHERECASE,r.SRCTYPE
							String dataSrc = fjViewMap.get(0).get("DATASRC").toString();
							String prePkg="";
							String aftPkg ="";
							String whereCase="";
							if (fjViewMap.get(0).get("PRE_PKG")!=null) {
								 prePkg = fjViewMap.get(0).get("PRE_PKG").toString();
							}
							if (fjViewMap.get(0).get("AFT_PKG")!=null) {
								 aftPkg = fjViewMap.get(0).get("AFT_PKG").toString();
							}
							if(fjViewMap.get(0).get("WHERECASE")!=null) {
								 whereCase = fjViewMap.get(0).get("WHERECASE").toString();
							}
							String srcType = fjViewMap.get(0).get("SRCTYPE").toString();
							String dataColList = ""; // 浮动行表数据列
							String dataFieldList = ""; // 对应视图设置的视图字段
							// 得到列指标与视图字段的映射关系
							List<HashMap> fjColSetMap = makeReportMapper.getFjViewCol(suitId, modalsheetId[i],
									rowItemId);
							int iNum = 0;
							for (HashMap fjColSet : fjColSetMap) {
								// 得到指定列指标对应的浮动行数据列的值,r.COLITEM_ID,r.FIELD
								colitemId = fjColSet.get("COLITEM_ID").toString();
								String fieldV = fjColSet.get("FIELD").toString();
								String dataCol = makeReportMapper.getFjDataCol(suitId, modalsheetId[i], colitemId);
								dataColList = dataColList + "," + dataCol;
								dataFieldList = dataFieldList + "," + fieldV;
								iNum = iNum + 1;
							}
							// 有字段映射设置才执行插入操作
							if (iNum > 0) {
								dataColList = dataColList.substring(1, dataColList.length());
								dataFieldList = dataFieldList.substring(1, dataFieldList.length());
								// String uuid=
								// UUID.randomUUID().toString();//获取Java端uuid
								String uuidField = "uuid_short()"; // mysql
								if (dbType == "oracle") {
									uuidField = "xsr_rep_s.nextval";
								}
								int rowNumDtl = 1;// 列序号，应自动获取
								// 拼接上公司、期间、币种等字段列表
								dataColList = "FJ_CELLV_ID,SUIT_ID,ENTITY_ID,PERIOD_ID,CURRENCY_ID,ROWITEM_ID,MODALSHEET_ID,ENTITY_CODE,"
										+ "PERIOD_CODE,CURRENCY_CODE,ROWITEM_CODE,ROWNUMDTL,SAVEFLAG,SUBENTITY_ID,LAST_UPDATE_DATE,"
										+ "LAST_UPDATED_BY,CREATION_DATE,CREATED_BY," + dataColList;
								dataFieldList = uuidField + ",'" + suitId + "','" + entityId + "','" + periodId + "','"
										+ currencyId + "','" + rowItemId + "','" + modalsheetId[i] + "','" + entityCode
										+ "','" + periodCode + "','" + currencyCode + "','" + rowItemCode + "',"
										+ rowNumDtl + ",1,'','" + time + "','" + userId + "','" + time + "','" + userId
										+ "'," + dataFieldList;
								// 1.执行预处理过程
								if (!"".equals(prePkg) && null != prePkg) {
									// exec prePkg; -无参数的预先执行过程
									CallableStatement cst = conn.prepareCall("call " + prePkg);
									cst.execute();// 执行
									if (cst != null) {
										cst.close();
									}
								}

								// 2.拼写批量插入语句
								String sqlViewExec = "";
								if ("S".equals(srcType)) {
									dataSrc = " (" + dataSrc + ")";
								}
								// 拼写上公司、期间、币种等 公共条件
								if (!"".equals(whereCase)&&whereCase!=null) {
									whereCase = whereCase+" and ";
								}
								whereCase = whereCase + " period_id='" + periodId + "' and entity_id='" + entityId
										+ "' and currency_id='" + currencyId + "'";
								sqlViewExec = "insert into xsr_rep_fj_cellvalue (" + dataColList + ") ";
								sqlViewExec = sqlViewExec + "select " + dataFieldList + " from  " + dataSrc + " where "
										+ whereCase;
								// 3.执行插入,执行之前应该先删除当前记录！！
								/*
								 * String sqlDel=
								 * "delete xsr_rep_fj_cellvalue where suit_id='"
								 * +suitId+"' and modalsheet_id='"
								 * +modalsheetId[i]+"' and entity_id='"
								 * +entityId+"' and period_id='"+periodId+
								 * "' and currency_id='" +currencyId+
								 * "' and rowitem_id='"+rowItemId+"'";
								 */
								// exec sqlDel;
								// exec sqlViewExec
								PreparedStatement pst = conn.prepareStatement(sqlViewExec);
								pst.executeUpdate();
								if (pst != null) {
									pst.close();
								}
								// 4.执行后处理 过程
								if (!"".equals(aftPkg) && null != aftPkg) {
									// exec aftPkg; -无参数的预先执行过程
									CallableStatement cst = conn.prepareCall("call " + aftPkg);
									cst.execute();// 执行
									if (cst != null) {
										cst.close();
									}
								}
							}
						}
					}
					// 5.执行完毕后，将浮动行指标加总到固定表上
					listUpdate.clear();
					listAdd.clear();
					// 5.1.循环当前浮动行模板的模板引用列指标表，获取每一个列指标（只列示数据列的列指标）
					List<HashMap> fjColRefs = makeReportMapper.getColRefOnly3(suitId, modalsheetId[i]);
					for (HashMap fjColRef : fjColRefs) {
						// 得到指定列指标对应的浮动行数据列的值,r.COLITEM_ID,r.FIELD
						colitemId = fjColRef.get("COLITEM_ID").toString();
						colitemCode = fjColRef.get("COLITEM_CODE").toString();
						String dataCol = makeReportMapper.getFjDataCol(suitId, modalsheetId[i], colitemId);
						// 5.2.汇总当前浮动行行、列指标下的全部明细行数据（应排除非汇总属性的列指标，但未处理）
						String sqlSum = "select sum(" + dataCol + "+0.00) from xsr_rep_fj_cellvalue r "
								+ " where r.SUIT_ID=?" + " and r.ENTITY_ID=?" + " and r.PERIOD_ID=?"
								+ " and r.MODALSHEET_ID=?" + " and r.CURRENCY_ID=?" + " and r.ROWITEM_ID=?";
						PreparedStatement pst = conn.prepareStatement(sqlSum);
						pst.setString(1, suitId);
						pst.setString(2, entityId);
						pst.setString(3, periodId);
						pst.setString(4, modalsheetId[i]);
						pst.setString(5, currencyId);
						pst.setString(6, rowItemId);
						// 执行sql
						ResultSet rs = pst.executeQuery();
						double sumValue = 0D;
						while (rs.next()) {
							sumValue = rs.getDouble(1); // 获取汇总值
						}
						if (pst != null) {
							pst.close();
						}
						if (rs != null) {
							rs.close();
						}
						// 5.3.将汇总的返回值，插入到固定指标值表中，采集全部完成！
						HashMap params_cell = new HashMap();
						params_cell.put("suitId", suitId);
						params_cell.put("entityId", entityId);
						params_cell.put("periodId", periodId);
						params_cell.put("currencyId", currencyId);
						params_cell.put("rowItemId", rowItemId);
						params_cell.put("colItemId", colitemId);
						int rsCount = makeReportMapper.countOfCellValue(params_cell);
						CellValue cellvalue = new CellValue();
						if (rsCount == 0) {
							cellvalue.setCELLV_ID(UUID.randomUUID().toString()); // 获取uuid
						}
						cellvalue.setSUIT_ID(suitId);
						cellvalue.setENTITY_ID(entityId);
						cellvalue.setPERIOD_ID(periodId);
						cellvalue.setROWITEM_ID(rowItemId);
						cellvalue.setCOLITEM_ID(colitemId);
						cellvalue.setCURRENCY_ID(currencyId);
						cellvalue.setENTITY_CODE(entityCode);
						cellvalue.setPERIOD_CODE(periodCode);
						cellvalue.setCURRENCY_CODE(currencyCode);
						cellvalue.setROWITEM_CODE(rowItemCode);
						cellvalue.setCOLITEM_CODE(colitemCode);
						cellvalue.setCREATION_DATE(time);
						cellvalue.setCREATED_BY(userId);
						cellvalue.setCELLV(sumValue);
						cellvalue.setCELLTEXTV(""); // 文本字段赋空
						cellvalue.setLAST_UPDATE_DATE(time);
						cellvalue.setLAST_UPDATED_BY(userId);
						if (rsCount > 0) {
							// 需要更新的放在listupdate中
							listUpdate.add(cellvalue);
						} else { // 需要插入的放在listAdd中
							listAdd.add(cellvalue);
						}
					}
					// 执行指标值的批量插入与更新
					if (listUpdate.size() > 0) {
						makeReportMapper.updateCellValue(listUpdate);
					}
					if (listAdd.size() > 0) {
						makeReportMapper.insertCellValue(listAdd);
					}

				}

			}
		} catch (SQLException e)
		{
			throw e;
		} finally

		{
			// 数据库关闭链接}
			SpringDBHelp.releaseConnection(conn);
		}
		return null;

	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public String lockReport(String userId,
							String suitId,
							String entityId,
							String periodId,
							String currencyId,
							String mdlShtIdList,
							String lockType     
							) throws Exception {
		//得到模板ID数组
		 String[] modalsheetId = mdlShtIdList.split(",");
		 HashMap params = new HashMap();
		 params.put("suitId", suitId);
		 params.put("entityId", entityId);
		 params.put("periodId", periodId);
		 params.put("currencyId", currencyId);
		 params.put("lockType", lockType);
		 // 执行删除
		 for (int i = 0; i < modalsheetId.length; i++) {
			log.warn("功能：报表生成 --解锁/锁定报表，报表ID:" + modalsheetId[i] + "执行时间："
					+ DateFormat.getDateTimeInstance().format(new Date()));
			params.put("modalsheetId", modalsheetId[i]);
			makeReportMapper.lockReport(params);
		 }
		 //执行完毕，返回空值
		 return null;		
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public String clearData(String userId,
							String suitId,
							String entityId,
							String periodId,
							String currencyId,
							String mdlShtIdList     
							) throws Exception {
		 //得到模板ID数组
		 String[] modalsheetId = mdlShtIdList.split(",");
		 String dbType=PlatformUtil.getDbType();
		 HashMap params = new HashMap();
		 params.put("suitId", suitId);
		 params.put("ENTITY_ID", entityId);
		 params.put("PERIOD_ID", periodId);
		 params.put("CURRENCY_ID", currencyId);
		 //params.put("modalsheetId", modalsheetId);
		 params.put("dbType", dbType);
		 // 执行删除
		 for (int i = 0; i < modalsheetId.length; i++) {
			log.warn("功能：报表生成 --清空报表数据，报表ID:" + modalsheetId[i] + "执行时间："
					+ DateFormat.getDateTimeInstance().format(new Date()));
			params.put("MODALSHEET_ID", modalsheetId[i]);
			String appStatus=makeReportMapper.getReportStatus(params);
			if(null != appStatus) {
				if ("A".equals(appStatus)) {
					//只有未锁定状态表，执行数据清除
					//固定指标
					makeReportMapper.clearData(params);
					//浮动指标
					makeReportMapper.clearDataFj(params);
				}
			}
		 }
		 //执行完毕，返回空值
		 return null;	
	}
	/*
	 * 加载报表数据
	 * */
	@Override
	public List<CellData> loadDataValue(Map<String,String> map) throws Exception {
		String mark=map.get("mark");
		map.remove("mark");
		List<CellData> list=new ArrayList<CellData>();
		if(mark!=null&&!"".equals(mark)&&mark.equals("qry")){
			list=makeReportMapper.loadDataValue(map);
		}else{
			list=makeReportMapper.loadDataWithoutC(map);
		}
		return list;
	}
	/*
	 * 判断公司是否为汇总单位
	 * */
	@Override
	public boolean isHrchyEntity(String suitId,String entityId) throws Exception {
		int count=makeReportMapper.isHrchyEntity(suitId,entityId);
		if(count>0){
			return true;//是汇总单位
		}else{
			return false;
		}
		
	}
	/**
	 *  获取汇总单位下的所有直接单位
	 * @param suitId 表套ID
	 * @param entityId 汇总单位ID
	 * @param hrchyId 公司层次ID
	 */
	@Override
	public String getHrchySon(String suitId, String entityId, String hrchyId) throws Exception {
		List<String> list=makeReportMapper.getHrchySon(suitId,entityId,hrchyId);
		String entity_ids="";
		for(String id:list){
			entity_ids=entity_ids+id+",";
		}
		if(entity_ids.length()>1){
			entity_ids=entity_ids.substring(0, entity_ids.length()-1);
		}		
		return entity_ids;
	}
	
	
	
	/**
	 * 获取层次中所有的汇总节点
	 * @param suitId 表套ID
	 * @param entityId 汇总单位ID
	 * @param hrchyId 公司层次ID
	 */
	@Override
	public String getHrchySonNode(String suitId, String entityId, String hrchyId,String entity_ids) throws Exception {
		List<String> list=makeReportMapper.getHrchySon(suitId,entityId,hrchyId);
		String ccids=entity_ids;
		if(list.size()>0){
			ccids=ccids+entityId+",";
			for(String id:list){			
				ccids=getHrchySonNode(suitId,id,hrchyId,ccids);
			}
		}		
		

		return ccids;
	}
	
	/**
	 * 节点检查
	 * @param map 包含各种需要的参数 ：suitId,userId, hrchyId, entityId, periodId, cnyId, mdltypeId, mdlshtId
	 * @throws Exception
	 */
	@Override
	public String hrchyCheck(Map<String,String> map) throws Exception {
		//判断报表是否存在
		int count=makeReportMapper.isSheet(map);
		String checkFlag="true";
		if(count>0){
			//在节点检查前先删除相应检查结果数据
			makeReportMapper.deleteChectResult(map);
			//执行节点检查，通过返回true，有差额返回false
			if(makeReportMapper.hrchyCheck(map)>0){
				checkFlag="false";
				//有差额, 记录结果
				makeReportMapper.insertCheckResultMS(map);
				makeReportMapper.insertCheckResultItem(map);
			}
		}		
		return checkFlag;
	}
	/**
	 * 节点检查--层层检查
	 * @param map 
	 * @throws Exception
	 */
	@Override
	public String hrchyCheckAll(Map<String,String> map) throws Exception {
		String checkFlag="true";
		//获取层层检查单位(获取层次中所有的汇总节点)
		String ccids=getHrchySonNode(map.get("suitId"),map.get("entityId"),map.get("hrchyId"),"");
		String[] ccidsArr=ccids.split(",");
		Map map1=new HashMap();
		map1.putAll(map);
		//循环公司,执行检查
		for(int i=0;i<ccidsArr.length;i++){
			map1.put("entityId", ccidsArr[i]);
			if(hrchyCheck(map1).equals("false")){
				checkFlag="false";
			}
		}
		return checkFlag;
	}

	
	/**
	 * 数据校验
	 * @param map
	 * @throws Exception
	 */
	@Override
	public String verifyFj(Map<String,String> map) throws Exception {
		//判断报表是否存在
		int count=makeReportMapper.isSheet(map);
		// 校验不通过标志
		int flag = 0;
		String errorFlag="true";
		if(count>0){			
			Map fjRowMap=null;
			Map colMap=null;
			String rowitem_id = null;
			String rowitem_name = null;
			String rowno = null;
			String colitem_id = null;
			String colitem_name = null;
			String colno = null;


			// 判断当前报表是否存在浮动行
			List<HashMap> fjRowList = makeReportMapper.getFjRow(map.get("suitId"), map.get("mdlshtId"));
			if(fjRowList != null && fjRowList.size()>0){
				String sheetName = makeReportMapper.getSheetNameById(map.get("mdlshtId"));
				String entityName = makeReportMapper.getEntityNameById(map.get("entityId"));
				// 查询该模板下为数值类型的列指标信息
				List<HashMap> colitemList = makeReportMapper.getColitemList(map.get("suitId"),map.get("mdlshtId"));
				for(int j=0;j<fjRowList.size();j++){
					fjRowMap =  fjRowList.get(j);
					rowitem_id = (String)fjRowMap.get("ROWITEM_ID");
					rowitem_name =(String)fjRowMap.get("ROWITEM_NAME");					
					rowno = fjRowMap.get("ROWNO").toString();
					rowitem_name=XSRUtil.replaceBlank(rowitem_name)+"_"+rowno+"行";
					map.put("rowitemId", rowitem_id);
					// 判断列指标为数值类型的合计数是否与明细数一致
					if(colitemList != null && colitemList.size()>0){
						for(int k=0;k<colitemList.size()&&flag==0;k++){
							colMap = colitemList.get(k);
							colitem_id = (String)colMap.get("COLITEM_ID");
							colitem_name = (String)colMap.get("COLITEM_NAME");
							colno = colMap.get("COLNO").toString();
							colitem_name=XSRUtil.replaceBlank(colitem_name)+"_"+colno+"列";
							map.put("colitemId", colitem_id);
							// 执行校验
							String dataCol=makeReportMapper.getFjDataCol(map.get("suitId"),map.get("mdlshtId"),colitem_id);
							if("".equals(dataCol)||dataCol==null){
								errorFlag=sheetName+"浮动行数据列与列指标映射出错，请重新设置模板格式！";
								flag++;
								break;
							}
							map.put("dataCol", dataCol);
							String sumFj=makeReportMapper.getSumFj(map);
							sumFj=sumFj==null?"0":sumFj;
							double fj=Double.parseDouble(sumFj);
							String cellv=makeReportMapper.getCellV(map);
							cellv=cellv==null?"0":cellv;
							double cv=Double.parseDouble(cellv);
							//是否需要处理小数位。。。
							if(fj!=cv){
								//记录校验结果
								errorFlag="false";
								StringBuffer result=new StringBuffer();
								result.append("(").append(sumFj).append(")-(").append(cellv).append(")=(")
								.append(FormulaTranslate.evalFormula(cellv+"-"+sumFj)).append(")");
								Map map1=new HashMap();
								map1.putAll(map);
								map1.put("sheetName",sheetName);
								map1.put("entityName", entityName);
								map1.put("flag",flag);
								map1.put("rowitemName",rowitem_name);
								map1.put("colitemName", colitem_name);
								map1.put("result",result.toString());
								map1.put("cellv", Double.parseDouble(cellv));
								map1.put("sumFj", Double.parseDouble(sumFj));
								makeReportMapper.insertverifyFjResult(map1);
							}
						}
					}
				}
			}
		}
//		errorFlag = String.valueOf(flag);
		return errorFlag;
	}

	/**
	 * 删除数据校验结果
	 * @param map
	 * @throws Exception
	 */
	@Override
	public void delVerifyFjResult(String userId) throws Exception {
		makeReportMapper.delVerifyFjResult(userId);
	}
	
	/**
	 * 报表固定行数据保存--liuyang/20160222
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,noRollbackFor={Exception.class})
	public String saveRepData(String modaltypeId,String modalsheetId,String periodId,String cnyId,String ledgerId, String entityId, String sheetJson, 
			String userId,String suitId) throws Exception {
				operRep(suitId, modalsheetId, entityId, periodId, cnyId, userId);   //生成或更新报表
				//然后保存报表数据
				//将页面的sheetJson数据转化为list
				List<Object> cells = JsonUtil.parseArray(sheetJson);
				Timestamp time = new Timestamp(new Date().getTime());
				
				//定义CellValue的list集合,做批量增，删，改
				List<CellValue> insertList= new ArrayList<CellValue>();
				List<CellValue> updatetList = new ArrayList<CellValue>();
//				List<CellValue> deleteList = new ArrayList<CellValue>();
				
				String rowitemId=null;
				String colitemId=null;
				String rowitemCode=null;
				String colitemCode=null;
				int colitemType=0;
				String periodCode=null;
				String currencyCode=null;
				String entityCode=null;
				HashMap params=new HashMap();
				 params.put("ENTITY_ID", entityId);
				 params.put("PERIOD_ID", periodId);
				 params.put("CURRENCY_ID", cnyId);
				 params.put("MODALSHEET_ID",modalsheetId);
				List<HashMap> repCodesMap=makeReportMapper.getRepCodes(params);
				 if (repCodesMap.size()>0) {
						entityCode=repCodesMap.get(0).get("ENTITY_CODE").toString();
						periodCode = repCodesMap.get(0).get("PERIOD_CODE").toString();
						currencyCode=repCodesMap.get(0).get("CURRENCY_CODE").toString();
					}
				 //获取模版下的所有行指标
				 List<Map<String, String>> rowInfo=makeReportMapper.getRowitemInfo(suitId, modaltypeId, modalsheetId);
				 //获取模版下的所有列指标
				 List<Map<String, Object>> colInfo=makeReportMapper.getColitemInfo(suitId, modaltypeId, modalsheetId);
				 Pattern p = Pattern.compile("^-+[ ]*-+$");
				for (int i = 0; i < cells.size(); i++) {
					Map<String, Object> cellMap = (Map<String, Object>) cells.get(i);
					String comment=cellMap.get("comment").toString();
					if(comment.contains("FJITEM")){       
						continue;                //过滤行次列单元格
					}
					rowitemId=comment.split(",")[0];
					colitemId=comment.split(",")[1];
					for (Map<String, String> map : rowInfo) {
						if(map.get("ROWITEM_ID").equals(rowitemId)){
							rowitemCode=map.get("ROWITEM_CODE");     //找到对应的行指标编码
							break;
						}
					}
					for (Map<String, Object> map : colInfo) {
						if(map.get("COLITEM_ID").equals(colitemId)){
							colitemCode=(String) map.get("COLITEM_CODE");
							//colitemType=(Integer) map.get("DATATYPE"); 
							colitemType=Integer.parseInt(map.get("DATATYPE").toString()); 
							break;                                //找到对应的列指标编码和列类型
						}
					}
					CellValue cellvalue=null;
					//判断当前单元格在数据库中是否存有数据
					params.put("suitId", suitId);
					params.put("rowItemId", rowitemId);
					params.put("colItemId", colitemId);
					Map<String, Object> rec=makeReportMapper.isExitData(params);
					if(rec!=null){ //数据存在
						if(!cellMap.containsKey("result")){
							cellMap.put("result","");
						} 
				//		if(cellMap.containsKey("result")){      //如果result属性不存在，直接删除
							cellvalue=new CellValue();
							String value=cellMap.get("result").toString();
//							if(value!=null&&!"".equals(value)){              //如果result="",其实要做删除
								cellvalue.setSUIT_ID(suitId);
								cellvalue.setLEDGER_ID(ledgerId);
								cellvalue.setENTITY_ID(entityId);
								cellvalue.setPERIOD_ID(periodId);
								cellvalue.setCURRENCY_ID(cnyId);
								cellvalue.setROWITEM_ID(rowitemId);
								cellvalue.setCOLITEM_ID(colitemId);
								cellvalue.setLAST_UPDATE_DATE(time);
								cellvalue.setLAST_UPDATED_BY(userId);
								if(colitemType==3){                 //如果单元格的数据与数据库一致，不需要更新
									if(value==null||"".equals(value)){
										value="0";
									}
									Matcher m = p.matcher(value);
									if(value.equals("-")||m.matches()){
										value="-0";
									}
									if(Double.parseDouble(value)!=((BigDecimal)rec.get("CELLV")).doubleValue()){
										String itmP=makeReportMapper.selItemPro(suitId, modaltypeId, modalsheetId, rowitemId, colitemId);
										if(itmP!=null&&!"".equals(itmP)){
											BigDecimal b=new BigDecimal(value);
											double d1=b.setScale(Integer.parseInt(itmP), RoundingMode.HALF_UP).doubleValue();
											cellvalue.setCELLV(d1);
										}else{
											cellvalue.setCELLV(Double.parseDouble(value));
										}
										cellvalue.setCELLTEXTV("");
										updatetList.add(cellvalue);
									}
								}else{
									if(value==null){
										value="";
									}
									if(!value.equals(rec.get("CELLTEXTV"))){
										cellvalue.setCELLV((double)0);
										cellvalue.setCELLTEXTV(value);
										updatetList.add(cellvalue);
									}
								}
					}else{ //数据不存在，新增;如果不存在result属性，不保存
						if(cellMap.containsKey("result")){
							String value=cellMap.get("result").toString();
							if(value!=null&&!"".equals(value)&&!"0".equals(value)&&!"-".equals(value)){
								Matcher m = p.matcher(value);
								if(m.matches()){
									continue;
								}
								cellvalue=new CellValue();
								cellvalue.setCELLV_ID(UUID.randomUUID().toString());
								cellvalue.setSUIT_ID(suitId);
								cellvalue.setENTITY_ID(entityId);
								cellvalue.setPERIOD_ID(periodId);
								cellvalue.setROWITEM_ID(rowitemId);
								cellvalue.setCOLITEM_ID(colitemId);
								cellvalue.setCURRENCY_ID(cnyId);
								cellvalue.setENTITY_CODE(entityCode);
								cellvalue.setPERIOD_CODE(periodCode);
								cellvalue.setROWITEM_CODE(rowitemCode);
								cellvalue.setCOLITEM_CODE(colitemCode);
								cellvalue.setCURRENCY_CODE(currencyCode);
								if(colitemType==3){
									String itmP=makeReportMapper.selItemPro(suitId, modaltypeId, modalsheetId, rowitemId, colitemId);
									if(itmP!=null&&!"".equals(itmP)){
										BigDecimal b=new BigDecimal(value);
										double d1=b.setScale(Integer.parseInt(itmP), RoundingMode.HALF_UP).doubleValue();
										cellvalue.setCELLV(d1);
									}else{
										cellvalue.setCELLV(Double.parseDouble(value));
									}
									cellvalue.setCELLTEXTV("");
								}else{
									cellvalue.setCELLV((double) 0);
									cellvalue.setCELLTEXTV(value);
								}
								cellvalue.setFORMULAV("");
								cellvalue.setLAST_UPDATE_DATE(time);
								cellvalue.setLAST_UPDATED_BY(userId);
								cellvalue.setCREATION_DATE(time);
								cellvalue.setCREATED_BY(userId);
								cellvalue.setLEDGER_ID(ledgerId);
								
								insertList.add(cellvalue);
							}
						}
					}
				}
				
				if (insertList.size() > 0) {				
					//批量插入
					makeReportMapper.insertCellValue(insertList);
				}
				if(updatetList.size()>0){
					//批量更新
					makeReportMapper.updateCellValue(updatetList);
				}
				/*if(deleteList.size()>0){
					makeReportMapper.delRepCellValue(deleteList);
				}*/
				
				//保存浮动行明细数据
				List<Map<String, String>> fjRowInfo=getFjRowitemInfo(suitId, modaltypeId, modalsheetId);
				if(fjRowInfo.size()>0){
					fjService.instTempToFjData(userId, suitId, modalsheetId, modaltypeId, entityId, periodId, cnyId);
					System.out.println("浮动行明细数据保存成功----");
				}
				return null;
	}
	
	/**
	 * 查询模版下的浮动行---liuayng/20160225
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,noRollbackFor={Exception.class})
	public List<Map<String, String>> getFjRowitemInfo(String suitId,String modaltypeId, String modalsheetId) throws Exception {
		List<Map<String, String>>fjRowInfo=makeReportMapper.getFjRowitemInfo(suitId, modaltypeId, modalsheetId);
		return fjRowInfo;
	}
	/**
	 * 判断报表是否已生成
	 * @param map
	 * @throws Exception
	 */
	@Override
	public String isSheet(Map<String, String> map) throws Exception {
		//判断报表是否存在
		int count=makeReportMapper.isSheet(map);
		String flag=count>0?"true":"false";
		return flag;
	}
	
	/**
	 * 获取层次中的所有子孙节点
	 * @param suitId 表套ID
	 * @param entityId 汇总单位ID
	 * @param hrchyId 公司层次ID
	 */
	@Override
	public String getHrchyAllSon(String suitId, String entityId, String hrchyId,String entity_ids) throws Exception {
		List<String> list=makeReportMapper.getHrchySon(suitId,entityId,hrchyId);
		String ccids=entity_ids;
		for(String id:list){
			ccids=ccids+id+",";
			ccids=getHrchyAllSon(suitId,id,hrchyId,ccids);
		}
		return ccids;
	}
	
	/**
	 * 报表生成或更新--liuyang/20160229
	 */
	@Override
	public void operRep(String suitId, String modalsheetId, String entityId,String periodId, 
			String currencyId,String userId) throws Exception {
		log.warn("功能：报表生成 --填报编制，报表ID:" + modalsheetId+ "执行时间："
				+ DateFormat.getDateTimeInstance().format(new Date()));
		//1.判断有无生成报表，若没生成，则先insert报表表;若已生成
		//获取db类型
		String dbType=PlatformUtil.getDbType();
		Timestamp time = new Timestamp(new Date().getTime());
		HashMap params = new HashMap();
		 params.put("suitId", suitId);
		 params.put("ENTITY_ID", entityId);
		 params.put("PERIOD_ID", periodId);
		 params.put("CURRENCY_ID", currencyId);
		 params.put("MODALSHEET_ID", modalsheetId);
		 params.put("dbType", dbType);
		String appStatus=makeReportMapper.getReportStatus(params);
		params.put("MODALSHEET_ID", modalsheetId);
		//根据ID获取各维度的code值
		String modalTypeId =null;
		String modalTypeCode=null;
		String entityCode=null;
		String periodCode=null;
		String currencyCode=null;
		String modalsheetCode=null;
		List<HashMap> repCodesMap=makeReportMapper.getRepCodes(params);
		if (repCodesMap.size()>0) {
			modalTypeId = repCodesMap.get(0).get("MODALTYPE_ID").toString();
			modalTypeCode=repCodesMap.get(0).get("MODALTYPE_CODE").toString();
			entityCode=repCodesMap.get(0).get("ENTITY_CODE").toString();
			periodCode = repCodesMap.get(0).get("PERIOD_CODE").toString();
			currencyCode=repCodesMap.get(0).get("CURRENCY_CODE").toString();
			modalsheetCode=repCodesMap.get(0).get("MODALSHEET_CODE").toString();
		}
		//
		if(null == appStatus) 
		{	//没有报表记录，则插入
			RepSheet repsheet = new RepSheet();
			repsheet.setSHEET_ID( UUID.randomUUID().toString());//获取UUID
			repsheet.setSUIT_ID(suitId);
			repsheet.setENTITY_ID(entityId);
			repsheet.setPERIOD_ID(periodId);
			repsheet.setCURRENCY_ID(currencyId);
			repsheet.setMODALSHEET_ID(modalsheetId);
			//
			repsheet.setMODALTYPE_ID(modalTypeId);
			repsheet.setMODALTYPE_CODE(modalTypeCode);
			repsheet.setENTITY_CODE(entityCode);
			repsheet.setPERIOD_CODE(periodCode);
			repsheet.setCURRENCY_CODE(currencyCode);
			repsheet.setMODALSHEET_CODE(modalsheetCode);
			//
			repsheet.setAPP_STATUS("A");
			repsheet.setCOMMIT_STATUS("N");
			repsheet.setVERIFY_STATUS("N");
			repsheet.setIMPTYPE("0");
			repsheet.setCREATION_DATE(time);
			repsheet.setLAST_UPDATE_DATE(time);
			repsheet.setLAST_UPDATED_BY(userId);
			repsheet.setCREATED_BY(userId);
			makeReportMapper.insertReport(repsheet);
		}else if ("A".equals(appStatus))
		{	//选定条件有报表，并且状态为起草，则执行报表update
			params.put("LAST_UPDATE_DATE", time);
			params.put("LAST_UPDATED_BY", userId);
			makeReportMapper.updateReport(params);
		}
	}
	
	/**
	 * 报表数据上报（主表）--liuyang/20160425
	 */
	@Override
	public List<Map<String, Object>> dataUpload(Map<String, String> param)throws Exception{
		
		return makeReportMapper.dataUpload(param);
	}
}
