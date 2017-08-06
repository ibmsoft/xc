package com.xzsoft.xsr.core.mapper;

import java.util.ArrayList;
import java.util.HashMap;

import com.xzsoft.xsr.core.modal.Dict;

public interface DictMapper {

	/**
	 * 字典查询
	 * @param dictName
	 * @return
	 */
	ArrayList<Dict> selectDictByDictName(String dictName);

	/**
	 * 判断字典是否存在
	 * @param dICT_NAME
	 * @return
	 */
	int isDictExist(String dICT_NAME);

	/**
	 * 插入字典
	 * @param params
	 */
	void insertDict(HashMap<String, String> params);

}
