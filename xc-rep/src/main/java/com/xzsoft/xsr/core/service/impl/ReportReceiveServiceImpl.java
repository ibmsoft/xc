package com.xzsoft.xsr.core.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xzsoft.xsr.core.mapper.ReceiveNBDataMapper;
import com.xzsoft.xsr.core.service.ReportReceiveService;

@Service
public class ReportReceiveServiceImpl implements ReportReceiveService {
	@Autowired
	ReceiveNBDataMapper receiveNBDataMapper;

	public String recevieSelectedReport(HashMap<String, String> params) throws Exception {
		String json = "";
		String corpModalList = params.get("corpModalList");
		if (corpModalList != null && !"".equals(corpModalList)) {
			String[] array = corpModalList.split(",");
			for (int i = 0; i < array.length; i++) {
				String[] a = array[i].split("-");
				String entityId = a[0];
				String modalSheetId = a[1];
				params.put("entityId", entityId);
				params.put("modalSheetId", modalSheetId);
				json = synDataRecive(params);
			}
		}
		return json;
	};

	public String synDataRecive(HashMap<String, String> params) throws Exception {
		// 执行插入之前先判断临时表是否有数据 若无则可能已被其它用户接收
		String json = "";
		String msg = "";
		int count;
		List<HashMap> repCodesMap = receiveNBDataMapper.getRepCodeById(params);
		if (repCodesMap.size() > 0) {
			String modalTypeCode = repCodesMap.get(0).get("MODALTYPE_CODE").toString();
			String modalSheetCode = repCodesMap.get(0).get("MODALSHEET_CODE").toString();
			String entityCode = repCodesMap.get(0).get("ENTITY_CODE").toString();
			String periodCode = repCodesMap.get(0).get("PERIOD_CODE").toString();
			String cnyCode = repCodesMap.get(0).get("CURRENCY_CODE").toString();
			params.put("modalTypeCode", modalTypeCode);
			params.put("modalSheetCode", modalSheetCode);
			params.put("entityCode", entityCode);
			params.put("periodCode", periodCode);
			params.put("cnyCode", cnyCode);
		}
		count = receiveNBDataMapper.IsHaveTempDate(params);
		// 记录错误日志信息
		if (count > 0) {
			// 查询报表状态
			String status = receiveNBDataMapper.getSheetStatus(params);
			// 判读报表是否已经锁定
			if (status == "" || status == null) {
				receiveNBDataMapper.insertSheets(params);
			} else {
				if (status.equals("E")) {
					msg = "接收失败：报表已锁定不能执行接收！";
					params.put("msg", msg);
					receiveNBDataMapper.insertImpLog(params);
					json = "{'flag':'1','msg':'报表已锁定不能执行接收！'}";
					return json;
				}
				// 处理报表头信息 报表不存在插入 报表存在,则修改状态和最后更新时间
				if (!status.equals("E")) {
					receiveNBDataMapper.updateSheets(params);
				}
			}
			// 删除固定行指标数据
			receiveNBDataMapper.deleteCellValue(params);
			receiveNBDataMapper.deleteFJCellValue(params);
			receiveNBDataMapper.insertCellValue(params);
			receiveNBDataMapper.insertFJCellValue(params);
			receiveNBDataMapper.deleteTempDate(params);
			receiveNBDataMapper.deleteFJTempDate(params);
			json = "{'flag':'0','msg':'报表数据接收成功！'}";
		} else {
			msg = "接收失败：选定报表中间表数据为空,可能已被其它用户接收！";
			params.put("msg", msg);
			receiveNBDataMapper.insertImpLog(params);
			json = "{'flag':'1','msg':'选定报表中间表数据为空,可能已被其它用户接收！'}";
			return json;
		}
		return json;
	}
}
