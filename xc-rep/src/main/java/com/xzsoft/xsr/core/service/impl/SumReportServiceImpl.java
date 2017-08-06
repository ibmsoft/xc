package com.xzsoft.xsr.core.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xzsoft.xsr.core.mapper.MakeReportMapper;
import com.xzsoft.xsr.core.mapper.SumReportMapper;
import com.xzsoft.xsr.core.modal.CellFormula;
import com.xzsoft.xsr.core.modal.CellValue;
import com.xzsoft.xsr.core.service.CellFormulaService;
import com.xzsoft.xsr.core.service.SumReportService;
import com.xzsoft.xsr.core.util.FormulaTranslate;

@Service
public class SumReportServiceImpl implements SumReportService {
	@Autowired
	private SumReportMapper sumReportMapper;
	@Autowired
	private CellFormulaService cellFormulaService;
	@Autowired
	private MakeReportMapper makeReportMapper;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String SumReport(String userId, String suitId, String hrchyId, String entityId, String periodId,
			String mdltypeId, String mdlshtId, String cnyId, String corpIdList, String adjId) throws Exception {
		/* 拼接参与汇总单位 */
		String msg = "Y";
		String desc = "";
		String v_Entityid = "";
		String v_Descadj = "";
		String v_Sum_scope = "";
		if (adjId == null || adjId == "") {
			v_Entityid = entityId;
			v_Descadj = "参与汇总";
			v_Sum_scope = corpIdList;
		} else {
			v_Entityid = adjId;
			v_Descadj = "倒挤差额";
			/* 若是调整单位倒挤差额，则将差额单位从合并范围中去掉，将汇总单位加入合并范围 */
			v_Sum_scope = corpIdList.replace(adjId, entityId);
		}
		/* 查询公司名称 */
		List<String> corpNamelist = sumReportMapper.queryEntityName(corpIdList);
		/* 拼接报表描述字段 */
		for (int i = 0; i < corpNamelist.size(); i++) {
			desc = desc + ",<" + corpNamelist.get(i) + ">";
		}
		/* 去掉第一个， */
		desc = desc.substring(1);
		desc = "共(" + corpNamelist.size() + ")单位" + v_Descadj + ":" + desc;
		/* 根据ID查询CODE这里可以看看宋哥哥的方法 */
		HashMap params = new HashMap();
		params.put("userId", userId);
		params.put("SUIT_ID", suitId);
		params.put("ENTITY_ID", v_Entityid);
		params.put("S_ENTITY_ID", entityId);
		params.put("adjId", adjId);
		params.put("PERIOD_ID", periodId);
		params.put("CURRENCY_ID", cnyId);
		params.put("MODALTYPE_ID", mdltypeId);
		params.put("MODALSHEET_ID", mdlshtId);
		List<HashMap> repCodesMap = makeReportMapper.getRepCodes(params);
		if (repCodesMap.size() > 0) {
			String entityCode = repCodesMap.get(0).get("ENTITY_CODE").toString();
			String periodCode = repCodesMap.get(0).get("PERIOD_CODE").toString();
			String currencyCode = repCodesMap.get(0).get("CURRENCY_CODE").toString();
			String modalTypeCode = repCodesMap.get(0).get("MODALTYPE_CODE").toString();
			String modalsheetCode = repCodesMap.get(0).get("MODALSHEET_CODE").toString();

			params.put("ENTITY_CODE", entityCode);
			params.put("PERIOD_CODE", periodCode);
			params.put("CURRENCY_CODE", currencyCode);
			params.put("MODALTYPE_CODE", modalTypeCode);
			params.put("MODALSHEET_CODE", modalsheetCode);
		}
		params.put("DESCRIPTION", desc);
		params.put("SCOPE_LIST", v_Sum_scope);
		/* 判断报表是否已经存在 */
		int count = sumReportMapper.isReportExist(params);
		if (count > 0) {
			/* 如果存在，查询报表状态 */
			String status = sumReportMapper.queryReportStatus(params);
			/* 如果为 '已生成'状态 */
			if (status.equals("A") || status.equals("D")) {
				/* 汇总报表 */
				sumRepDate(params, adjId);
				/* 更新相应字段 */
				sumReportMapper.updateReportD(params);
			} else {
				/* 如果为 '审批中'或'通过'状态,则此报表不能再修改 */
				msg = "N";
			}
		} else {
			/* 插入报表记录 */
			params.put("SHEET_ID", UUID.randomUUID().toString());
			sumReportMapper.insertSumSheet(params);
			/* 汇总报表 */
			sumRepDate(params, adjId);
		}

		return msg;
	}

	// 报表数据汇总
	@SuppressWarnings("rawtypes")
	public String sumRepDate(HashMap params, String adjId) throws Exception {
		// 删除已汇总的报表的指标值
		sumReportMapper.deleteSumItemValue(params);
		// 批量插入的插入数据到cellValue表中
		sumReportMapper.insertSumCellValue(params);
		// 汇总或差额倒挤后,执行公司级公式采集(处理单价等不需要数据汇总指标)
		// 查询公司级取数公式
		List<CellFormula> corpFormula = sumReportMapper.queryCorpFormula(params);
		// 循环公式，执行采集运算，并将运算结果结存于list中
		String rowitemCode = "";
		String colitemCode = "";
		String dataFormula = "";
		String rowitemId = "";
		String colitemId = "";
		String cellV = "";
		String cellTextV = "";
		List<CellValue> listUpdate = new ArrayList<CellValue>();
		List<CellValue> listAdd = new ArrayList<CellValue>();// 获取列指标类型
		Timestamp time = new Timestamp(new Date().getTime());
		if (corpFormula.size() > 0) {
			for (CellFormula formula : corpFormula) {
				rowitemCode = formula.getROWITEM_CODE();
				colitemCode = formula.getCOLITEM_CODE();
				rowitemId = formula.getROWITEM_ID();
				colitemId = formula.getCOLITEM_ID();
				dataFormula = formula.getDATAFORMULA();
				// 调用运算函数，获取运算结果
				
				String equation = cellFormulaService.getArithmetic(dataFormula, params.get("PERIOD_ID").toString(),
						params.get("ENTITY_ID").toString(), params.get("SUIT_ID").toString(),
						params.get("CURRENCY_ID").toString(),
						params.get("LEDGER_ID").toString(),params.get("CURRENCY_CODE").toString());

				int dataType = makeReportMapper.getColDataType(params.get("SUIT_ID").toString(), colitemId);
				// 将函数运算串，生成最终计算结果值
				if (dataType == 3) { // 数据值
					cellV = FormulaTranslate.evalFormula(equation);
					cellTextV = "";
				} else // 项目列或文本值列
				{
					cellV = "";
					cellTextV = FormulaTranslate.evalFormula(equation);
				}
				int rsCount = makeReportMapper.countOfCellValue(params);
				CellValue cellvalue = new CellValue();
				cellvalue.setSUIT_ID(params.get("SUIT_ID").toString());
				cellvalue.setENTITY_ID(params.get("ENTITY_ID").toString());
				cellvalue.setPERIOD_ID(params.get("PERIOD_ID").toString());
				cellvalue.setROWITEM_ID(rowitemId);
				cellvalue.setCOLITEM_ID(colitemId);
				cellvalue.setCURRENCY_ID(params.get("CURRENCY_ID").toString());
				cellvalue.setENTITY_CODE(params.get("ENTITY_CODE").toString());
				cellvalue.setPERIOD_CODE(params.get("PERIOD_CODE").toString());
				cellvalue.setCURRENCY_CODE(params.get("CURRENCY_CODE").toString());
				cellvalue.setROWITEM_CODE(rowitemCode);
				cellvalue.setCOLITEM_CODE(colitemCode);
				cellvalue.setCELLV(Double.parseDouble(cellV));
				cellvalue.setCELLTEXTV(cellTextV);
				cellvalue.setCREATION_DATE(time);
				cellvalue.setLAST_UPDATE_DATE(time);
				cellvalue.setLAST_UPDATED_BY(params.get("userId").toString());
				cellvalue.setCREATED_BY(params.get("userId").toString());
				if (rsCount > 0) {
					// 需要更新的放在listupdate中
					listUpdate.add(cellvalue);
				} else {
					// 需要插入的放在listAdd中
					listAdd.add(cellvalue);
				}
			}
			// 执行指标值的批量插入与更新
			makeReportMapper.updateCellValue(listUpdate);
			makeReportMapper.insertCellValue(listAdd);
		}
		// 将附加行中子公司的附加数据copy一份为父公司的数据
		if (adjId == null || adjId == "") {
			sumReportMapper.deleteFJCellValue(params);
			sumReportMapper.insertFJCellValue(params);
		}
		return null;
	}
}
