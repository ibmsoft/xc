package com.xzsoft.xsr.core.mapper;

import java.util.HashMap;
import java.util.List;
public interface InternalDataMapper {
	 /**
	  * 删除临时表中固定行与浮动行数据，暂时不考虑合并抵消数据
	  * */
    @SuppressWarnings("rawtypes")
	public List<HashMap> getRepIds(HashMap params) throws Exception;
	 /**
	  * 删除固定行临时表中数据，暂时不考虑合并抵消数据
	  * */
     @SuppressWarnings("rawtypes")
	public void deleteInnerFixTable(HashMap params) throws Exception;
     /**
	  * 删除浮动行临时表中数据，暂时不考虑合并抵消数据
	  * */
     @SuppressWarnings("rawtypes")
	public void deleteInnerFloatTable(HashMap params) throws Exception;
     /**
	  * 保存固定行数据
	  * */
     @SuppressWarnings("rawtypes")
	public void saveInnerFixData(HashMap params)   throws Exception;
     /**
	  * 修改固定行指标
	  * */
     @SuppressWarnings("rawtypes")
	public void updateInnerFixItem(HashMap params)   throws Exception;
     /**
   	  * 修改浮动行指标
   	  * */
     @SuppressWarnings("rawtypes")
     public void updateInnerFloatItem(HashMap params) throws Exception;
}
