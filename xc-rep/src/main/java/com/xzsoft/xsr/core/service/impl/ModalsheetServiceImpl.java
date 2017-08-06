package com.xzsoft.xsr.core.service.impl;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.xzsoft.xsr.core.mapper.ModalsheetMapper;
import com.xzsoft.xsr.core.modal.CellData;
import com.xzsoft.xsr.core.modal.Colitem;
import com.xzsoft.xsr.core.modal.Item_Property;
import com.xzsoft.xsr.core.modal.ModalSheetElement;
import com.xzsoft.xsr.core.modal.ModalSheetFormat;
import com.xzsoft.xsr.core.modal.ModalSheet;
import com.xzsoft.xsr.core.modal.Rowitem;
import com.xzsoft.xsr.core.service.ModalsheetService;
import com.xzsoft.xsr.core.util.JsonUtil;

@Service
public class ModalsheetServiceImpl implements ModalsheetService {

	@Autowired
	private ModalsheetMapper modalsheetMapper;

	/**
	 * 判断模板格式是否存在
	 * 
	 * @param msFormatId
	 * @return
	 * @throws Exception
	 */
	@Override
	public int isModaLFormatExist(String msFormatId) throws Exception {
		return modalsheetMapper.isModaLFormatExist(msFormatId);
	}

	/**
	 * 保存模板格式及模板元素 修改模板表上的表头物理行位置 修改浮动行列与列指标映射表的物理列位置
	 * 
	 * @param modalsheetId
	 * @param sheetJson
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String saveModalsheetFormat(String modalsheetId, String titleMaxRow, String msFormatId, String sheetJson,
			String userId, String colnoResult, String suitId, String modaltypeId, String rowitemWithX) throws Exception {

		if (modalsheetMapper.isModaLFormatExist(msFormatId) > 0) {
			// 如果模板格式已经存在，则删除模板格式，再插入
			modalsheetMapper.deleleModalsheetFormat(msFormatId);
			modalsheetMapper.deleleModalsheetElement(msFormatId);
		}
		// 将页面的sheetJson数据转化为map
		Map<String, Object> jsonObjMap = JsonUtil.getJsonObj(sheetJson);
		/**
		 * 从页面传来模板的json串后，jsonObjMap中的key有：name，sheets，cells，floatings，
		 * fileConfig； 根据cells信息，创建xsr_rep_modalsheet_format表信息
		 * 从jsonObjMap中get到cells部分,注意这里得到了一个List对象!!!!
		 */
		List<Object> cells = (List<Object>) jsonObjMap.get("cells");

		/**
		 * 1. 根据cells信息，保存模板格式xsr_rep_modalsheet_format表信息
		 */
		List<ModalSheetFormat> listBatchCells = new ArrayList<ModalSheetFormat>();
		for (int i = 0; i < cells.size(); i++) {
			Map<String, Object> cellMap = (Map<String, Object>) cells.get(i);
			// 将每一个cell的json串转化为一个ModalSheetFormat bean
			listBatchCells.add(this.convertCellToModalSheetFormat(cellMap, userId));
		}
		if (listBatchCells.size() > 0) {
			// 批量插入模板格式单元格
			modalsheetMapper.insertBatchFormatCell(listBatchCells);
		}
		/**
		 * 2. 根据floatings信息，保存模板元素xsr_rep_modalsheet_element表信息
		 */
		List<Object> floatings = (List<Object>) jsonObjMap.get("floatings");
		List<ModalSheetElement> listBatchElements = new ArrayList<ModalSheetElement>();
		for (int i = 0; i < floatings.size(); i++) {
			Map<String, Object> floatingMap = (Map<String, Object>) floatings.get(i);
			// 将每一个floating的json串转化为一个ModalSheetElement bean
			listBatchElements.add(this.convertCellToModalSheetElement(floatingMap, userId));
		}
		if (listBatchElements.size() > 0) {
			// 批量插入模板元素单元格
			modalsheetMapper.insertBatchModalSheetElement(listBatchElements);
		}
		/**
		 * 3. 修改模板表上的表头物理行位置（模板的行次所在行）TITLE_MAX_ROW字段
		 */
		modalsheetMapper.updateTitleMaxRow(modalsheetId, titleMaxRow);
		/**
		 * 4. 如果是浮动行表时，修改浮动行列与列指标映射表的物理列位置。非浮动行表colnoResult=="" 根据列指标编码修改实际列位置
		 * colnoResult中的内容为：列指标编码&物理列,列指标编码&物理列,......
		 */
		if (colnoResult != null && !"".equals(colnoResult)) {
			List<Colitem> colrefList = modalsheetMapper.getColmodalref(suitId, modaltypeId, modalsheetId);
			if(null != colrefList) {
				Map<String,Colitem> colMap = new HashMap<String,Colitem>();
				for(Colitem c : colrefList) {
					colMap.put(c.getCOLITEM_CODE(), c);
				}
				String[] colnoArray = colnoResult.split(",");
				for (String colNo : colnoArray) {
					String[] colInfo = colNo.split("&"); // 列指标编码&物理列
					colMap.get(colInfo[0]).setCOL(Integer.parseInt(colInfo[1]));
				}
				insertFjDataColSet(colrefList, modalsheetId, "update");
			}
		}
		/**
		 * 5. 修改行指标引用表ROW字段。
		 * rowitemWithX中的内容为：行指标编码&物理行位置,行指标编码&物理行位置,......
		 */
		//System.out.println(rowitemWithX);
		if (rowitemWithX != null && !"".equals(rowitemWithX)) {
			List<Rowitem> rowitemList = new ArrayList<Rowitem>();
			String[] rowXArray = rowitemWithX.split(",");
			for (String rowX : rowXArray) {
				String[] rowXInfo = rowX.split("&"); // 行指标编码&物理行位置
				Rowitem r = new Rowitem();
				r.setROWITEM_CODE(rowXInfo[0]);
				r.setROW(Integer.parseInt(rowXInfo[1]));
				rowitemList.add(r);
			}
			modalsheetMapper.batUpdateRowitemRefRow(modalsheetId, rowitemList);
		}
		return null;
	}

	/**
	 * 加载模板格式及模板元素
	 * 
	 * @param msFormatId
	 * @param modalsheetName
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> loadModalsheetFormat(String msFormatId, String modalsheetId) throws Exception {
		List<ModalSheet> sheetList = new ArrayList<ModalSheet>();
		
		ModalSheet modalsheet = modalsheetMapper.getModalsheetById(modalsheetId) ;
		String modalsheetName = modalsheet.getName() ;
		
		ModalSheet ms = new ModalSheet(Integer.parseInt(msFormatId), modalsheetName);
		sheetList.add(ms);
		
		List<ModalSheetFormat> cellList = modalsheetMapper.getModalsheetFormatCellList(msFormatId);
		List<ModalSheetElement> elementList = modalsheetMapper.getModalsheetElementList(msFormatId);

		Map<String, Object> results = Maps.newHashMap();
		results.put("fileName", modalsheetName);
		results.put("sheets", sheetList);
		results.put("cells", cellList);
		results.put("floatings", elementList);

		return results;
	}

	/**
	 * 删除模板
	 * 
	 * @param modalsheetId
	 * @param msFormatId
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String deleteModalsheet(String modalsheetId, String msFormatId, String suitId) throws Exception {
		// 判断该模板是否已经生成报表
		int sheetCount = modalsheetMapper.getSheetCountByModalsheetId(modalsheetId);
		if (sheetCount > 0) {
			return "该模板已经生成报表,不可删除!";
		} else {
			/**
			 * 模板没有生成过报表，那么需要删除， 1.行指标引用，2.列指标引用，3.模板格式，4.模板元素，5.
			 * 浮动行列与列指标映射，6.模板属性，7.模板
			 */
			modalsheetMapper.deleteRowModalRefByMID(modalsheetId);
			modalsheetMapper.deleteColModalRef(modalsheetId);
			modalsheetMapper.deleleModalsheetFormat(msFormatId);
			modalsheetMapper.deleleModalsheetElement(msFormatId);
			modalsheetMapper.deleteDataColSet(modalsheetId);
			modalsheetMapper.deleleItemPropertyAlltype(modalsheetId, suitId);
			modalsheetMapper.deleteModalsheet(modalsheetId);
			return null;
		}
	}

	/**
	 * 通过指标编码查询指标
	 */
	@Override
	public Rowitem getOneRowitem(String suitId, String rowitemCode) throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("rowitemCode", rowitemCode);
		params.put("suitId", suitId);
		Rowitem rowitem = modalsheetMapper.getOneRowitem(params);
		if (null != rowitem) {
			if (null == rowitem.getISFJITEM())
				rowitem.setISFJITEM(false);
			if (null == rowitem.getDIRECTION())
				rowitem.setDIRECTION("JF");
		}
		return rowitem;
	}

	/**
	 * 通过列指标编码查询列指标
	 */
	@Override
	public Colitem getOneColitem(String suitId, String modalsheetId, String colitemCode) throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("suitId", suitId);
		params.put("modalsheetId", modalsheetId);
		params.put("colitemCode", colitemCode);
		Colitem colitem = modalsheetMapper.getOneColitem(params);
		return colitem;
	}

	/**
	 * 新增行指标分类; 更新模板的rowUpcode字段; 保存或更新行指标; 保存模板行指标引用;
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String updateRowitem(String[] params, List<Rowitem> list) throws Exception {
		Integer lanNo = list.get(0).getLANNO();
		String suitId = params[0];
		String userId = params[1];
		String modaltypeId = params[2];
		String modaltypeCode = params[3];
		String modalsheetId = params[4];
		String modalsheetCode = params[5];
		String modalsheetName = params[6];
		NumberFormat nf = new DecimalFormat("0000");
		StringBuilder data = new StringBuilder();
		data.append("[");
		StringBuilder upCode = new StringBuilder();
		Timestamp time = new Timestamp(new Date().getTime());
		/**
		 * 1. 获取行指标分类(upcode) 判断是否新增行指标分类(upcode)逻辑：
		 * 报表2.0逻辑：循环行指标list判断如果指标全部是新增的，则新增一个模板的行指标分类
		 * 因报表3.0采用分栏选取指标进行保存，所以采用与2.0不同的方式判断行指标分类(upcode)
		 * 报表3.0逻辑：以模板id为条件,通过模板表关联行指标表,查询到行指标分类(upcode)是否存在
		 */
		String rowupcode = modalsheetMapper.getRowUpcodeByModalsheetId(modalsheetId, suitId);

		if (null == rowupcode) {
			// 如果rowupcode为空，则新建一个模板的行指标分类
			// 判断是否已经存在使用当前模板编码来作为行指标分类码的数据
			int count = modalsheetMapper.isModalCodeUsedForRowUpcode(modalsheetCode);
			// 生成行指标分类
			upCode.append(modalsheetCode).append("-").append(nf.format(count + 1));
			Rowitem r = new Rowitem(UUID.randomUUID().toString(), suitId, upCode.toString(), modalsheetName,
					modalsheetName, "ROOT", false, time, userId, time, userId);
			// 在行指标表中插入行指标分类这条记录
			modalsheetMapper.insertRowitemUpcode(r);
			// 更新模板的行指标分类字段
			modalsheetMapper.updateModalSheetRowUpcode(upCode.toString(), modalsheetId);
		} else {
			upCode.append(rowupcode);
		}
		// 检测行指标upcode是否成功生成
		if ("".equals(upCode)) {
			throw new RuntimeException("生成行指标upcode不成功");
		}
		/**
		 * 2. 插入或更新rowitem表
		 */
		int i = 0;
		for (Rowitem rowitem : list) {
			if (rowitem.getISNEW()) {
				// 如果是新增的指标，则插入
				int rowCout = modalsheetMapper.countOfRowitem(upCode.toString());
				StringBuilder rowitemCode = new StringBuilder();
				rowitemCode.append(upCode).append("-").append(nf.format(rowCout + 1));
				// 判断这个指标在系统中是否已经存在
				String existRowitemId = modalsheetMapper.isRowitemCodeExist(rowitemCode.toString(), suitId);
				if (null != existRowitemId) {
					String rowCode = modalsheetMapper.getMaxRowitmeCode(upCode.toString());
					rowitemCode.delete(0, rowitemCode.length());
					rowitemCode.append(upCode).append("-")
							.append(nf.format(Integer.parseInt(rowCode.substring(upCode.length() + 1)) + 1));
				}
				// 插入新记录
				rowitem.setROWITEM_ID(UUID.randomUUID().toString());
				rowitem.setSUIT_ID(suitId);
				rowitem.setROWITEM_CODE(rowitemCode.toString());
				rowitem.setITEMALIAS(rowitem.getROWITEM_NAME());
				rowitem.setUPCODE(upCode.toString());
				rowitem.setISICP(false);
				rowitem.setORDERBY(nf.format(i));
				rowitem.setCREATION_DATE(time);
				rowitem.setCREATED_BY(userId);
				rowitem.setLAST_UPDATE_DATE(time);
				rowitem.setLAST_UPDATED_BY(userId);
				modalsheetMapper.insertRowitem(rowitem);

				rowitem.setROWREF_ID(UUID.randomUUID().toString());
				rowitem.setMODALTYPE_ID(modaltypeId);
				rowitem.setMODALTYPE_CODE(modaltypeCode);
				rowitem.setMODALSHEET_CODE(modalsheetCode);

				if ("[".equals(data.toString())) {
					data.append("{");
				} else {
					data.append(",{");
				}
				data.append("rowno:").append(i).append(",rowitemcode:'").append(rowitem.getROWITEM_CODE())
						.append("',rowitemid:'").append(rowitem.getROWITEM_ID()).append("',upcode:'")
						.append(rowitem.getUPCODE()).append("'}");
			} else {
				// 如果是已存在的指标，则更新
				rowitem.setSUIT_ID(suitId);
				rowitem.setITEMALIAS(rowitem.getROWITEM_NAME());
				rowitem.setORDERBY(nf.format(i));
				rowitem.setLAST_UPDATE_DATE(time);
				rowitem.setLAST_UPDATED_BY(userId);

				rowitem.setCREATION_DATE(time);
				rowitem.setCREATED_BY(userId);
				rowitem.setROWREF_ID(UUID.randomUUID().toString());
				rowitem.setMODALTYPE_ID(modaltypeId);
				rowitem.setMODALTYPE_CODE(modaltypeCode);
				rowitem.setMODALSHEET_CODE(modalsheetCode);

				modalsheetMapper.updateRowitem(rowitem);
			}
			i++;
		}
		/**
		 * 3. 插入rowmodalref表
		 */
		// 首先删除掉已有的行指标引用,因为行指标是分栏选择的，所以删除的时候需要加上栏次条件
		modalsheetMapper.deleteRowModalRef(modalsheetId, lanNo);
		// 插入行指标引用
		modalsheetMapper.insertBthRowitemRef(list);

		data.append("]");
		return data.toString();
	}

	/**
	 * 新增列指标分类; 更新模板的colUpcode字段; 保存或更新列指标; 保存模板列指标引用; 多栏表相同名字的列指标只保存一次;
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String updateColitem(String[] params, List<Colitem> list) throws Exception {
		String suitId = params[0];
		String userId = params[1];
		String modaltypeId = params[2];
		String modaltypeCode = params[3];
		String modalsheetId = params[4];
		String modalsheetCode = params[5];
		String modalsheetName = params[6];
		String isFjModal = params[7];
		NumberFormat nf = new DecimalFormat("0000");
		StringBuilder data = new StringBuilder();
		data.append("[");
		Timestamp time = new Timestamp(new Date().getTime());
		/**
		 * 1. 多栏的情况下，相同列指标在列指标表中保存一份 相同指标的判断依据：指标名称相同
		 */
		ArrayList<Colitem> oneGroupColitemList = new ArrayList<Colitem>();
		for (Colitem colitem : list) {
			String colitemName = colitem.getCOLITEM_NAME();
			String colitemCode = colitem.getCOLITEM_CODE();
			if (colitem.getISSEL()) {
				boolean isExists = false;
				// 与oneGroupColitemList中的列指标进行判断，设置列指标存在的标记isExists
				for (Colitem item : oneGroupColitemList) {
					isExists = false;
					if (colitemName.equals(item.getCOLITEM_NAME())) {
						isExists = true;
						if ((colitemCode != null) && (!"".equals(colitemCode))) {
							item.setCOLITEM_CODE(colitemCode);
						}
						break;
					}
				}
				if (!isExists) {
					// oneGroupColitemList中的对象与list中的对象引用相同的对象！！！
					oneGroupColitemList.add(colitem);
				}
			}
		}
		// System.out.println("页面传递的列指标list长度： " + list.size());
		// System.out.println("整理之后的oneGroupColitemList长度： " +
		// oneGroupColitemList.size());
		/**
		 * 2. 获取列指标upcode
		 */
		StringBuilder upCode = new StringBuilder();
		if (isColitemsNew(list)) {
			// 如果指标全部是新增的，则新建一个模板的列指标分类
			// 判断是否已经存在使用当前模板编码来作为列指标分类码的数据
			int count = modalsheetMapper.isModalCodeUsedForColUpcode(modalsheetCode);
			// 生成列指标分类
			upCode.append(modalsheetCode).append("-").append(nf.format(count + 1));
			Colitem c = new Colitem(UUID.randomUUID().toString(), suitId, upCode.toString(), modalsheetName,
					modalsheetName, "ROOT", time, userId, time, userId);
			// 在列指标表中插入列指标分类这条记录
			modalsheetMapper.insertColitemUpcode(c);
			// 更新模板的列指标分类字段
			modalsheetMapper.updateModalSheetColUpcode(upCode.toString(), modalsheetId);
		} else {
			upCode.append(getColUpcode(list));
		}
		// System.out.println("列指标upcode: " + upCode);
		/**
		 * 3. 新增或更新列指标
		 */
		int i = 0;
		StringBuilder colitemCode = new StringBuilder();
		for (Colitem colitem : oneGroupColitemList) {

			// 通过isNew判断指标是否为新增的，如果是新增的则插入，否则更新
			/*
			 * String itemCode = colitem.getCOLITEM_CODE(); if ((itemCode ==
			 * null) || ("".equals(itemCode))) {
			 */
			if (colitem.getISNEW()) {
				colitemCode.setLength(0);
				// 如果是新增的指标，则插入
				int colCout = modalsheetMapper.countOfColitem(upCode.toString());
				colitemCode.append(upCode).append("-").append(nf.format(colCout + 1));
				// 判断这个指标在系统中是否已经存在
				String existCowitemId = modalsheetMapper.isColitemCodeExist(colitemCode.toString(), suitId);
				if (null != existCowitemId) {
					String colCode = modalsheetMapper.getMaxColitmeCode(upCode.toString());
					colitemCode.delete(0, colitemCode.length());
					colitemCode.append(upCode).append("-")
							.append(nf.format(Integer.parseInt(colCode.substring(upCode.length() + 1)) + 1));
				}
				// 插入新记录
				colitem.setCOLITEM_ID(UUID.randomUUID().toString());
				colitem.setSUIT_ID(suitId);
				colitem.setCOLITEM_CODE(colitemCode.toString());
				colitem.setITEMALIAS(colitem.getCOLITEM_NAME());
				colitem.setUPCODE(upCode.toString());
				colitem.setORDERBY(nf.format(i++));
				colitem.setCREATION_DATE(time);
				colitem.setCREATED_BY(userId);
				colitem.setLAST_UPDATE_DATE(time);
				colitem.setLAST_UPDATED_BY(userId);
				modalsheetMapper.insertColitem(colitem);

				colitem.setCOLREF_ID(UUID.randomUUID().toString());
				colitem.setMODALTYPE_ID(modaltypeId);
				colitem.setMODALTYPE_CODE(modaltypeCode);
				colitem.setMODALSHEET_CODE(modalsheetCode);

				/*
				 * if ((itemCode != null) && (!"".equals(itemCode))) {
				 * colitem.setCOLITEM_CODE(itemCode); }
				 */
			} else {
				// 如果是已存在的指标，则更新
				colitem.setSUIT_ID(suitId);
				colitem.setITEMALIAS(colitem.getCOLITEM_NAME());
				colitem.setORDERBY(nf.format(i));
				colitem.setLAST_UPDATE_DATE(time);
				colitem.setLAST_UPDATED_BY(userId);

				colitem.setCREATION_DATE(time);
				colitem.setCREATED_BY(userId);
				colitem.setCOLREF_ID(UUID.randomUUID().toString());
				colitem.setMODALTYPE_ID(modaltypeId);
				colitem.setMODALTYPE_CODE(modaltypeCode);
				colitem.setMODALSHEET_CODE(modalsheetCode);

				modalsheetMapper.updateColitem(colitem);

			}
		}
		/**
		 * 4. 为list中列指标对象的列指标编码赋值，通过判断与oneGroupColitemList相同的列指标
		 */
		for (Colitem item : list) {
			String itemName = item.getCOLITEM_NAME();
			if (item.getISSEL()) {
				for (Colitem itemBean : oneGroupColitemList) {
					if ((itemName.equals(itemBean.getCOLITEM_NAME())) && (null == item.getLAST_UPDATED_BY())) {
						item.setCOLREF_ID(UUID.randomUUID().toString());
						item.setSUIT_ID(suitId);
						item.setMODALTYPE_ID(modaltypeId);
						item.setMODALTYPE_CODE(modaltypeCode);
						item.setMODALSHEET_ID(modalsheetId);
						item.setMODALSHEET_CODE(modalsheetCode);
						item.setCOLITEM_ID(itemBean.getCOLITEM_ID());
						item.setCOLITEM_CODE(itemBean.getCOLITEM_CODE());
						item.setCREATION_DATE(time);
						item.setCREATED_BY(userId);
						item.setLAST_UPDATE_DATE(time);
						item.setLAST_UPDATED_BY(userId);
					}
				}
			}
		}
		/**
		 * 5. 拼出向页面返回的json串
		 */
		int j = 0;
		for (Colitem item : list) {
			if (item.getISSEL()) {
				if ("[".equals(data.toString())) {
					data.append("{rowno:").append(j).append(",colitemcode:'").append(item.getCOLITEM_CODE())
							.append("'}");
				} else {
					data.append(",{rowno:").append(j).append(",colitemcode:'").append(item.getCOLITEM_CODE())
							.append("'}");
				}
			}
			j++;
		}
		/**
		 * 6. 插入colmodalref表
		 */
		List<Colitem> refList = new ArrayList<Colitem>();
		for (Colitem item : list) {
			if (item.getISSEL()) {
				refList.add(item);
			}
		}
		// 首先删除掉已有的列指标引用
		modalsheetMapper.deleteColModalRef(modalsheetId);
		// 插入列指标引用
		modalsheetMapper.insertBthColitemRef(refList);
		/**
		 * 7. 如果是浮动行表，则保存一下数据列与列指标映射关系。目前处理的浮动行表都是：单栏表！！！！ 只有新增指标时才会操作
		 */
		if ("Y".equals(isFjModal)) {
			insertFjDataColSet(oneGroupColitemList, modalsheetId, "insert");
		}
		data.append("]");
		return data.toString();
	}

	/**
	 * 查询其他栏次的行指标
	 */
	@Override
	public String getOtherRowitemList(String modalsheetId, String lanNo) throws Exception {
		StringBuilder data = new StringBuilder();
		data.append("[");
		List<Rowitem> rowitems = modalsheetMapper.getOtherRowitemList(modalsheetId, lanNo);
		if (rowitems.size() > 0) {
			for (Rowitem r : rowitems) {
				if ("[".equals(data.toString())) {
					data.append("{");
				} else {
					data.append(",{");
				}
				// rf.MODALSHEET_ID, rf.ROWITEM_ID, rf.ROWITEM_CODE,
				// rm.ROWITEM_NAME, rm.UPCODE, rf.ROWNO,
				// rf.LANNO, rm.ISFJITEM, rm.DIRECTION
				data.append("MODALSHEET_ID:'").append(r.getMODALSHEET_ID()).append("',ROWITEM_ID:'")
						.append(r.getROWITEM_ID()).append("',ROWITEM_CODE:'").append(r.getROWITEM_CODE())
						.append("',ROWITEM_NAME:'").append(r.getROWITEM_NAME()).append("',UPCODE:'")
						.append(r.getUPCODE()).append("',ROWNO:'").append(r.getROWNO()).append("',LANNO:'")
						.append(r.getLANNO()).append("',ISFJITEM:'").append(r.getISFJITEM()).append("',DIRECTION:'")
						.append(r.getDIRECTION()).append("'}");
			}
		}
		data.append("]");
		return data.toString();
	}

	/**
	 * 封装floatings中的每一个json串为ModalSheetElement
	 * 
	 * @param floatingMap
	 * @param ms
	 */
	private ModalSheetElement convertCellToModalSheetElement(Map<String, Object> floatingMap, String userId) {
		/**
		 * floatings格式:[{sheet=3, name=3$4$3$4$6, ftype=meg, json=[4,3,4,6]}]
		 */
		// String modalsheetId = floatingMap.get("sheet").toString();
		Integer sheetId = Integer.parseInt(floatingMap.get("sheet").toString());
		String name = (String) floatingMap.get("name");
		String ftype = (String) floatingMap.get("ftype");
		
		Object json = floatingMap.get("json") ;
		String content = String.valueOf(json) ;;
		// ModalSheetElement mse = new
		// ModalSheetElement(UUID.randomUUID().toString(), modalsheetId, name,
		// ftype, content, new Timestamp(new Date().getTime()), userId);
		ModalSheetElement mse = new ModalSheetElement(UUID.randomUUID().toString(), sheetId, name, ftype, content,
				new Timestamp(new Date().getTime()), userId);
		return mse;
	}

	/**
	 * 封装cells中的每一个json串为ModalSheetFormat
	 * 
	 * @param cellMaps
	 * @param ms
	 */
	private ModalSheetFormat convertCellToModalSheetFormat(Map<String, Object> cellMap, String userId) {
		/**
		 * {sheet=3, row=4, col=3, cal=true,
		 * json={"data":"=sum(E4:F4)+C6","cal":true,"arg":
		 * "overwriteplus(sum({\"span\":[\"\",0,2,0,3],\"type\":2}),{\"span\":[\"\",2,0,2,0],\"type\":2})"
		 * ,"refs":[["3",4,5,4,6],["3",6,3,6,3]],"value":0,"timestamp":
		 * "20151224102247661"}}
		 */
		Integer x = Integer.parseInt(cellMap.get("row").toString());
		Integer y = Integer.parseInt(cellMap.get("col").toString());
		// String modalsheetId = cellMap.get("sheet").toString();
		Integer sheetId = Integer.parseInt(cellMap.get("sheet").toString());
		String content = (String) cellMap.get("json");

		// 关于cal=true,是有表内公式的单元格
		Boolean cal = false;
		if (cellMap.get("cal") != null)
			cal = (Boolean) cellMap.get("cal");

		// ModalSheetFormat的cellComment,cellCommentType
		/*
		 * 字段现在先不进行设置，继续做下去看是否需要这个字段？？？？？ Map<String, Object> contentMap =
		 * JsonUtil.getJsonObj(content); String cellComment = null;
		 * if(contentMap.containsKey("comment")) { cellComment =
		 * contentMap.get("comment").toString(); }
		 */

		Map<String, Object> contentMap = JsonUtil.getJsonObj(content);
		String cellComment = null;
		String cellCommentType = null;
		if (contentMap.containsKey("comment")) {
			cellComment = contentMap.get("comment").toString();
			if (cellComment.indexOf(",") != -1) {
				cellCommentType = "DATA";
			}
		}
		/*
		 * ModalSheetFormat cell = new
		 * ModalSheetFormat(UUID.randomUUID().toString(), modalsheetId, x, y,
		 * content, cal, new Timestamp(new Date().getTime()), userId,
		 * cellComment, cellCommentType);
		 */
		ModalSheetFormat cell = new ModalSheetFormat(UUID.randomUUID().toString(), sheetId, x, y, content, cal,
				new Timestamp(new Date().getTime()), userId, cellComment, cellCommentType);
		return cell;
	}

	/**
	 * 判断列指标是否为新增的
	 * 
	 * @param list
	 * @return
	 */
	private boolean isColitemsNew(List<Colitem> list) {
		// 循环所有的列指标，如果有一个指标不是新增的就返回false
		for (int i = 0; i < list.size(); i++) {
			Colitem colitem = list.get(i);
			if(null != colitem.getISNEW()) {
				if (!colitem.getISNEW()) {
					return false;
				}
			} else {
				return false;
			}

		}
		// 循环完所有的列指标，都是新增的，则返回true
		return true;
	}

	/**
	 * 从已经存在的列指标中查找upcode
	 * 
	 * @param list
	 * @return
	 */
	private String getColUpcode(List<Colitem> list) {
		String upcode = null;
		// 循环所有的行指标，如果有一个指标不是新增的就返回false
		for (int i = 0; i < list.size(); i++) {
			Colitem colitem = list.get(i);
			if (!colitem.getISNEW()) {
				upcode = colitem.getUPCODE();
				return upcode;
			}
		}
		// 循环完所有的行指标，都是新增的，则返回true
		return upcode;
	}

	/**
	 * 根据模板id查询选中的模板; 新增一条与选中模板相同的模板信息，format_id保存最大值+1 根据format_id查询模板元素;
	 * 根据format_id查询模板格式; 保存模板行指标引用;
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String copyModalsheet(String[] params) throws Exception {
		String suitId = params[0];
		String userId = params[1];
		String srcModaltypeId = params[2];
		String modaltypeId = params[3];
		String modaltypeCode = params[4];
		String srcModalsheetId = params[5];
		String srcMsFormatId = params[6];
		// 根据模板id查询选中的模板信息
		ModalSheet modalSheet = modalsheetMapper.getModalsheetById(srcModalsheetId);
		int count = modalsheetMapper.isModalsheetExistByCode(modaltypeId, modalSheet.getCode());
		if (count > 0) {
			return modalSheet.getName();
		} else {
			// 新增一条与选中模板相同的模板信息
			Timestamp time = new Timestamp(new Date().getTime());
			String newModalsheetId = UUID.randomUUID().toString();
			modalSheet.setMODALSHEET_ID(newModalsheetId);
			modalSheet.setMODALTYPE_ID(modaltypeId);
			modalSheet.setMODALTYPE_CODE(modaltypeCode);
			modalSheet.setCREATION_DATE(time);
			modalSheet.setCREATED_BY(userId);
			modalSheet.setLAST_UPDATE_DATE(time);
			modalSheet.setLAST_UPDATED_BY(userId);
			Integer maxModalFormat = modalsheetMapper.getMaxModaLFormat();
			// System.out.println("生成的最大模板格式id为： " + maxModalFormat);
			modalSheet.setId(maxModalFormat);
			modalsheetMapper.insertModalsheet(modalSheet);
			// 根据选中模板的format_id查询元素表相应的信息
			List<ModalSheetElement> modalSheetElements = modalsheetMapper.getModalsheetElementList(srcMsFormatId);
			if (modalSheetElements.size() > 0) {
				for (int i = 0; i < modalSheetElements.size(); i++) {
					modalSheetElements.get(i).setElementId(UUID.randomUUID().toString());
					modalSheetElements.get(i).setSheet(Integer.parseInt(maxModalFormat.toString()));
					modalSheetElements.get(i).setCreationDate(time);
					modalSheetElements.get(i).setCreatedBy(userId);
				}
				// 批量插入模板元素
				modalsheetMapper.insertBatchModalSheetElement(modalSheetElements);
			}
			// 根据选中模板的format_id查询格式表相应的信息
			List<ModalSheetFormat> modalSheetFormats = modalsheetMapper.getModalsheetFormatCellList(srcMsFormatId);
			if (modalSheetFormats.size() > 0) {
				for (int i = 0; i < modalSheetFormats.size(); i++) {
					modalSheetFormats.get(i).setFormatId(UUID.randomUUID().toString());
					modalSheetFormats.get(i).setSheet(Integer.parseInt(maxModalFormat.toString()));
					modalSheetFormats.get(i).setCreationDate(time);
					modalSheetFormats.get(i).setCreatedBy(userId);
				}
				// 批量插入模板格式
				modalsheetMapper.insertBatchFormatCell(modalSheetFormats);
			}
			// 根据模板集id和模板id查询对应的行指标引用信息
			List<Rowitem> rowitems = modalsheetMapper.getRowmodalref(suitId, srcModaltypeId, srcModalsheetId);
			if (rowitems.size() > 0) {
				for (int j = 0; j < rowitems.size(); j++) {
					rowitems.get(j).setROWREF_ID(UUID.randomUUID().toString());
					rowitems.get(j).setMODALTYPE_ID(modaltypeId);
					rowitems.get(j).setMODALTYPE_CODE(modaltypeCode);
					rowitems.get(j).setMODALSHEET_ID(newModalsheetId);
					rowitems.get(j).setCREATED_BY(userId);
					rowitems.get(j).setCREATION_DATE(time);
					rowitems.get(j).setLAST_UPDATE_DATE(time);
					rowitems.get(j).setLAST_UPDATED_BY(userId);
				}
				modalsheetMapper.insertBthRowitemRef(rowitems);
			}
			// 根据模板集id和模板id查询对应的列指标引用信息
			List<Colitem> colitems = modalsheetMapper.getColmodalref(suitId, srcModaltypeId, srcModalsheetId);
			if (colitems.size() > 0) {
				for (int j = 0; j < colitems.size(); j++) {
					colitems.get(j).setCOLREF_ID(UUID.randomUUID().toString());
					colitems.get(j).setMODALTYPE_ID(modaltypeId);
					colitems.get(j).setMODALTYPE_CODE(modaltypeCode);
					colitems.get(j).setMODALSHEET_ID(newModalsheetId);
					colitems.get(j).setCREATED_BY(userId);
					colitems.get(j).setCREATION_DATE(time);
					colitems.get(j).setLAST_UPDATE_DATE(time);
					colitems.get(j).setLAST_UPDATED_BY(userId);
				}
				modalsheetMapper.insertBthColitemRef(colitems);
			}
			// 如果模板是浮动行模板，需要复制浮动行列与列指标映射
			if ("Y".equals(modalSheet.getFJ_FLAG())) {
				List<Colitem> fjColSetList = modalsheetMapper.getFjCoitemSetList(suitId, srcModalsheetId);
				if (fjColSetList.size() > 0) {
					for (Colitem c : fjColSetList) {
						c.setFJITEM_SET_ID(UUID.randomUUID().toString());
						c.setMODALSHEET_ID(newModalsheetId);
						c.setCREATED_BY(userId);
						c.setCREATION_DATE(time);
						c.setLAST_UPDATE_DATE(time);
						c.setLAST_UPDATED_BY(userId);
					}
					modalsheetMapper.insertBthDataColSet(fjColSetList);
				}
			}
			return null;
		}
	}

	/**
	 * 通过行列指标编码，模板id获取行列指标id
	 */
	@Override
	public String getRowAndColitemId(HashMap<String, String> params) throws Exception {
		String rcId = "";
		HashMap<String, String> rcMap = modalsheetMapper.getRowAndColitemId(params);
		if (null != rcMap) {
			rcId = rcMap.get("ROWITEM_ID") + "," + rcMap.get("COLITEM_ID");
		}
		return rcId;
	}

	/**
	 * 保存指标精度或汇总属性
	 * 
	 * @param modalsheetId
	 * @param sheetJson
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String saveItemProperty(String msFormatId, String reportData, String modaltypeId, String modalsheetId,
			String pro_type, String suitId) throws Exception {
		/*
		 * 判断模版指标精度是否存在，如果存在删除
		 */
		if (modalsheetMapper.isItemProperty(modalsheetId, suitId, pro_type) > 0) {
			modalsheetMapper.deleleItemProperty(modalsheetId, suitId, pro_type);
		}
		/*
		 * 从页面传来模板的json串后，jsonObjMap中的key有：sheetId，x，y，comment，result(单元格值)
		 * 根据cells信息，创建xsr_rep_modalsheet_format表信息
		 */
		List<Object> cells = JsonUtil.parseArray(reportData);
		String rowitemId = null;
		String colitemId = null;
		for (int i = 0; i < cells.size(); i++) {
			Map<String, Object> cellMap = (Map<String, Object>) cells.get(i);
			if (cellMap.get("comment").toString().split(",").length == 2) {
				rowitemId = cellMap.get("comment").toString().split(",")[0];
				colitemId = cellMap.get("comment").toString().split(",")[1];
				if (cellMap.containsKey("result")) {
					if (cellMap.get("result").toString() != "") {
						Item_Property item_property = new Item_Property();
						item_property.setITEM_PRO_ID(UUID.randomUUID().toString());
						item_property.setSUIT_ID(suitId);
						item_property.setMODALTYPE_ID(modaltypeId);
						item_property.setMODALSHEET_ID(modalsheetId);
						item_property.setROWITEM_ID(rowitemId);
						item_property.setCOLITEM_ID(colitemId);
						item_property.setPRO_TYPE(pro_type);
						item_property.setPRO_VALUE(cellMap.get("result").toString());
						modalsheetMapper.insertItemProperty(item_property);
					}
				}
			}
		}
		return null;
	}

	/**
	 * 加载指标精度或汇总属性
	 * 
	 * @param msFormatId
	 * @param sheetJson
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<CellData> loadItemProperty(String msFormatId, String pro_type, String suitId) throws Exception {

		List<CellData> list = modalsheetMapper.loadItemProperty(msFormatId, suitId, pro_type);
		return list;
	}
	/**
	 * 如果是浮动行表，则保存浮动行DATA列与列指标的映射关系
	 * 没有映射的列指标才会映射。
	 * 如果已经存在则不做任何修改。
	 */
	public void insertFjDataColSet(List<Colitem> oneGroupColitemList, String modalsheetId, String type) throws Exception {
		StringBuilder datacol = new StringBuilder();
		if (isColitemsNew(oneGroupColitemList)) {
			// 如果指标全部是新增的，则直接插入映射关系
			int k = 1;
			for (Colitem c : oneGroupColitemList) {
				datacol.setLength(0);
				datacol.append("DATA");
				datacol.append(k);
				c.setFJITEM_SET_ID(UUID.randomUUID().toString());
				c.setDATA_COL(datacol.toString());
				k++;
			}
			// 插入列指标映射
			modalsheetMapper.insertBthDataColSet(oneGroupColitemList);
		} else {
			//如果不是全部新增
			int fjColCount = modalsheetMapper.getCountFjColSet(modalsheetId);
			List<Colitem> insertList = new ArrayList<Colitem>();
			// 循环所有的列指标，将新增的列指标放入insertList中；
			// 如果不是新增的则判断是否已经存在映射，如果不存在映射，则放入insertList中。
			for (Colitem c : oneGroupColitemList) {
				if (null != c.getISNEW() && c.getISNEW()) {
					fjColCount++;
					datacol.setLength(0);
					datacol.append("DATA");
					datacol.append(fjColCount);
					c.setFJITEM_SET_ID(UUID.randomUUID().toString());
					c.setDATA_COL(datacol.toString());
					insertList.add(c);
				} else if(null == modalsheetMapper.getFjCoitemSet(c)) {
					fjColCount++;
					datacol.setLength(0);
					datacol.append("DATA");
					datacol.append(fjColCount);
					c.setFJITEM_SET_ID(UUID.randomUUID().toString());
					c.setDATA_COL(datacol.toString());
					insertList.add(c);
				}
			}
			if (insertList.size() > 0) {
				// 插入列指标映射
				modalsheetMapper.insertBthDataColSet(insertList);
			}
		}
		if("update".equals(type)) {
			modalsheetMapper.batUpdateFjColnoCol(oneGroupColitemList);
		}
	}

}
