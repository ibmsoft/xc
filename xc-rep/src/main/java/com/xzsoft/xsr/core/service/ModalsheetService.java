package com.xzsoft.xsr.core.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xzsoft.xsr.core.modal.CellData;
import com.xzsoft.xsr.core.modal.Colitem;
import com.xzsoft.xsr.core.modal.Rowitem;

public interface ModalsheetService {

	/**
	 * 判断模板格式是否存在
	 * 
	 * @param msFormatId
	 * @return
	 * @throws Exception
	 */
	public int isModaLFormatExist(String msFormatId) throws Exception;

	/**
	 * 保存模板格式及模板元素
	 * 
	 * @param msFormatId
	 * @param sheetJson
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public String saveModalsheetFormat(String modalsheetId, String titleMaxRow,
			String msFormatId, String sheetJson, String userId,
			String colnoResult, String suitId, String modaltypeId, String rowitemWithX) throws Exception;

	/**
	 * 加载模板格式及模板元素
	 * 
	 * @param msFormatId
	 * @param modalsheetId
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> loadModalsheetFormat(String msFormatId, String modalsheetId) throws Exception;
	/**
	 * 删除模板
	 * @param modalsheetId
	 * @param msFormatId
	 * @return
	 * @throws Exception
	 */
	public String deleteModalsheet(String modalsheetId, String msFormatId, String suitId) throws Exception;
	/**
	 * 通过指标编码查询指标
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Rowitem getOneRowitem(String suitId, String rowitemCode)
			throws Exception;

	/**
	 * 通过列指标编码查询列指标
	 * 
	 * @param suitId
	 * @param modalsheetId
	 * @param colitemCode
	 * @return
	 * @throws Exception
	 */
	public Colitem getOneColitem(String suitId, String modalsheetId,
			String colitemCode) throws Exception;

	/**
	 * 保存行指标
	 * 
	 * @param suitId
	 * @param userId
	 * @param json
	 * @throws Exception
	 */
	public String updateRowitem(String[] params, List<Rowitem> list)
			throws Exception;

	/**
	 * 查询其他栏次的行指标
	 * 
	 * @param modalsheetId
	 * @param lanNo
	 * @return
	 * @throws Exception
	 */
	public String getOtherRowitemList(String modalsheetId, String lanNo)
			throws Exception;

	/**
	 * 保存列指标
	 * 
	 * @param params
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public String updateColitem(String[] params, List<Colitem> list)
			throws Exception;

	/**
	 * 复制模板
	 * 
	 * @param params
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public String copyModalsheet(String[] params) throws Exception;

	/**
	 * 通过行列指标编码，模板id获取行列指标id
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String getRowAndColitemId(HashMap<String, String> params)
			throws Exception;

	/**
	 * 保存指标精度或汇总属性
	 * 
	 * @param msFormatId
	 * @param sheetJson
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public String saveItemProperty(String msFormatId, String sheetJson,
			String modaltypeId, String modalsheetId, String pro_type,
			String suitId) throws Exception;

	/**
	 * 加载指标精度或汇总属性
	 * 
	 * @param msFormatId
	 * @param sheetJson
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<CellData> loadItemProperty(String msFormatId, String pro_type,
			String suitId) throws Exception;
}
