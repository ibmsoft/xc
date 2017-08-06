package com.xzsoft.xsr.core.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xzsoft.xsr.core.modal.CellData;
import com.xzsoft.xsr.core.modal.Colitem;
import com.xzsoft.xsr.core.modal.Item_Property;
import com.xzsoft.xsr.core.modal.ModalSheet;
import com.xzsoft.xsr.core.modal.ModalSheetElement;
import com.xzsoft.xsr.core.modal.ModalSheetFormat;
import com.xzsoft.xsr.core.modal.ModalType;
import com.xzsoft.xsr.core.modal.Rowitem;

public interface ModalsheetMapper {

	/**
	 * 判断模板格式是否存在
	 * @param msFormatId
	 * @return
	 * @throws Exception
	 */
	public int isModaLFormatExist(String msFormatId) throws Exception;
	/**
	 * 通过模板集id,模板编码判断模板是否存在
	 * @param modaltypeId
	 * @param modalsheetCode
	 * @return
	 * @throws Exception
	 */
	public int isModalsheetExistByCode(String modaltypeId, String modalsheetCode) throws Exception;
	/**
	 * 根据模板id删除模板格式
	 * @param msFormatId
	 * @throws Exception
	 */
	public void deleleModalsheetFormat(String msFormatId) throws Exception;
	/**
	 * 根据模板id删除模板元素
	 * @param msFormatId
	 * @throws Exception
	 */
	public void deleleModalsheetElement(String msFormatId) throws Exception;
	/**
	 * 批量插入模板格式单元格
	 * @param formatCells
	 * @throws Exception
	 */
	public void insertBatchFormatCell(@Param(value="formatCells") List<ModalSheetFormat> formatCells) throws Exception;
	/**
	 * 批量插入模板元素单元格
	 * @param elementCells
	 * @throws Exception
	 */
	public void insertBatchModalSheetElement(@Param(value="elementCells") List<ModalSheetElement> elementCells) throws Exception;
	/**
	 * 通过模板id获取模板格式
	 * @param msFormatId
	 * @throws Exception
	 */
	public List<ModalSheetFormat> getModalsheetFormatCellList(String msFormatId) throws Exception; 
	/**
	 * 通过模板id获取模板元素
	 * @param msFormatId
	 * @throws Exception
	 */
	public List<ModalSheetElement> getModalsheetElementList(String msFormatId) throws Exception; 
	/**
	 * 通过指标编码查询指标
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Rowitem getOneRowitem(HashMap<String, String> params) throws Exception;
	/**
	 * 通过列指标编码查询列指标
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Colitem getOneColitem(HashMap<String, String> params) throws Exception;
	/**
	 * 判断是否已经存在使用当前模板编码来作为行指标分类码的数据
	 * @param modalsheetCode
	 * @return
	 * @throws Exception
	 */
	public int isModalCodeUsedForRowUpcode(String modalsheetCode) throws Exception;
	/**
	 * 判断是否已经存在使用当前模板编码来作为列指标分类码的数据
	 * @param modalsheetCode
	 * @return
	 * @throws Exception
	 */
	public int isModalCodeUsedForColUpcode(String modalsheetCode) throws Exception;
	/**
	 * 插入行指标分类记录
	 * @param rowitem
	 * @throws Exception
	 */
	public void insertRowitemUpcode(@Param(value="rowitem") Rowitem rowitem) throws Exception;
	/**
	 * 插入列指标分类记录
	 * @param rowitem
	 * @throws Exception
	 */
	public void insertColitemUpcode(@Param(value="colitem") Colitem colitem) throws Exception;
	/**
	 * 根据模板id去修改模板的rowUpcoe字段
	 * @param upCode
	 * @param modalsheetId
	 * @throws Exception
	 */
	public void updateModalSheetRowUpcode(String upCode, String modalsheetId) throws Exception;
	/**
	 * 根据模板id去修改模板的colUpcoe字段
	 * @param upCode
	 * @param modalsheetId
	 * @throws Exception
	 */
	public void updateModalSheetColUpcode(String upCode, String modalsheetId) throws Exception;
	/**
	 * 查询以此upCode的行指标共有多少条记录
	 * @param upCode
	 * @return
	 * @throws Exception
	 */
	public int countOfRowitem(String upCode) throws Exception;
	/**
	 * 查询以此upCode的列指标共有多少条记录
	 * @param upCode
	 * @return
	 * @throws Exception
	 */
	public int countOfColitem(String upCode) throws Exception;
	/**
	 * 判断行指标是否已经存在
	 * @param rowitemCode
	 * @param suitId
	 * @return
	 * @throws Exception
	 */
	public String isRowitemCodeExist(String rowitemCode, String suitId) throws Exception;
	/**
	 * 判断列指标是否已经存在
	 * @param rowitemCode
	 * @param suitId
	 * @return
	 * @throws Exception
	 */
	public String isColitemCodeExist(String colitemCode, String suitId) throws Exception;
	/**
	 * 获取系统中以此upCode的行指标编码最大值
	 * @param upCode
	 * @return
	 * @throws Exception
	 */
	public String getMaxRowitmeCode(String upCode) throws Exception;
	/**
	 * 获取系统中以此upCode的列指标编码最大值
	 * @param upCode
	 * @return
	 * @throws Exception
	 */
	public String getMaxColitmeCode(String upCode) throws Exception;
	/**
	 * 插入行指标记录
	 * @param rowitem
	 * @throws Exception
	 */
	public void insertRowitem(@Param(value="rowitem") Rowitem rowitem) throws Exception;
	/**
	 * 批量插入行指标
	 * @param rowitem
	 * @throws Exception
	 */
	public void insertBatchRowitemList(@Param(value="rowitems") List<Rowitem> rowitems) throws Exception;
	/**
	 * 通过行指标编码和表套ID更新行指标
	 * @param rowitem
	 * @throws Exception
	 */
	public void updateRowitem(@Param(value="rowitem") Rowitem rowitem) throws Exception;
	/**
	 * 批量更新行指标
	 * @param rowitems
	 * @throws Exception
	 */
	public void updateBatchRowitemList(@Param(value="rowitems") List<Rowitem> rowitems) throws Exception;
	/**
	 * 插入列指标记录
	 * @param rowitem
	 * @throws Exception
	 */
	public void insertColitem(@Param(value="colitem") Colitem colitem) throws Exception;
	/**
	 * 批量插入列指标
	 * @param colitems
	 * @throws Exception
	 */
	public void insertBatchColitemList(@Param(value="colitems") List<Colitem> colitems) throws Exception;
	/**
	 * 更新列指标记录
	 * @param rowitem
	 * @throws Exception
	 */
	public void updateColitem(@Param(value="colitem") Colitem colitem) throws Exception;
	/**
	 * 批量更新列指标
	 * @param colitems
	 * @throws Exception
	 */
	public void updateBatchColitemList(@Param(value="colitems") List<Colitem> colitems) throws Exception;
	/**
	 * 通过模板id,栏号删除模板引用行指标
	 * @param modalsheetId
	 * @throws Exception
	 */
	public void deleteRowModalRef(String modalsheetId, Integer lanNo) throws Exception;
	/**
	 * 通过模板id删除模板引用行指标
	 * @param modalsheetId
	 * @throws Exception
	 */
	public void deleteRowModalRefByMID(String modalsheetId) throws Exception;
	/**
	 * 通过模板code,模板集code,表套ID删除模板引用行指标
	 * @param modalsheetCode
	 * @param modaltypeCode
	 * @param suitId
	 * @throws Exception
	 */
	public void deleteRowModalRefByCode(String modalsheetCode, String modaltypeCode, String suitId) throws Exception;
	/**
	 * 通过模板id删除模板引用列指标
	 * @param modalsheetId
	 * @throws Exception
	 */
	public void deleteColModalRef(String modalsheetId) throws Exception;
	/**
	 * 通过模板code,模板集code,表套ID删除模板引用列指标
	 * @param modalsheetId
	 * @throws Exception
	 */
	public void deleteColModalRefByCode(String modalsheetCode, String modaltypeCode, String suitId) throws Exception;
	/**
	 * 批量插入模板引用行指标
	 * @param rowitems
	 * @throws Exception
	 */
	public void insertBthRowitemRef(@Param(value="rowitems") List<Rowitem> rowitems) throws Exception;
	/**
	 * 批量插入模板引用列指标
	 * @param rowitems
	 * @throws Exception
	 */
	public void insertBthColitemRef(@Param(value="colitems") List<Colitem> colitems) throws Exception;
	/**
	 * 查询其他栏次的指标
	 * @param modalsheetId
	 * @param lanNo
	 * @return
	 * @throws Exception
	 */
	public List<Rowitem> getOtherRowitemList(String modalsheetId, String lanNo) throws Exception;
	/**
	 * 以模板id为条件,通过模板表关联行指标表,查询到行指标分类(upcode)
	 * @param modalsheetId
	 * @return
	 * @throws Exception
	 */
	public String getRowUpcodeByModalsheetId(String modalsheetId, String suitId) throws Exception;
	/**
	 * 按照模板ID删除浮动行表数据列与列指标映射关系
	 * @param modalsheetId
	 * @throws Exception
	 */
	public void deleteDataColSet(String modalsheetId) throws Exception;
	/**
	 * 批量插入浮动行表数据列与列指标映射关系
	 * @param colitems
	 * @throws Exception
	 */
	public void insertBthDataColSet(@Param(value="colitems") List<Colitem> colitems) throws Exception;
	/**
	 * 查询模板的浮动行与列指标目前设置的记录数
	 * @param modalsheetId
	 * @return
	 * @throws Exception
	 */
	public int getCountFjColSet(String modalsheetId) throws Exception;
	/**
	 * 根据模板id查询选中的模板信息
	 * @param modalsheetId
	 * @throws Exception
	 */
	public ModalSheet getModalsheetById(String modalsheetId) throws Exception;
	/**
	 * 新增一条与选中模板相同的模板信息
	 * @param modalSheet
	 * @throws Exception
	 */
	public void insertModalsheet(ModalSheet modalSheet) throws Exception;
	/**
	 * 获取模板表中最大值format_id+1
	 * @throws Exception
	 */
	public Integer getMaxModaLFormat() throws Exception;
	/**
	 * 根据模板集id和模板id查詢行指標引用信息
	 * @param modaltypeId
	 * @param modalsheetId
	 * @throws Exception
	 */
	public List<Rowitem> getRowmodalref(String suitId, String modaltypeId, String modalsheetId) throws Exception;
	/**
	 * 根据模板集id和模板id查詢列指標引用信息
	 * @param modaltypeId
	 * @param modalsheetId
	 * @throws Exception
	 */
	public List<Colitem> getColmodalref(String suitId, String modaltypeId,String modalsheetId) throws Exception;
	/**
	 * 通过模板集id获取到模板集
	 * @param modaltypeId
	 * @return
	 * @throws Exception
	 */
	public ModalType getModaltype(String modaltypeId) throws Exception;
	/**
	 * 查询一张模板上的所有的行指标
	 * @return
	 * @throws Exception
	 */
	public List<Rowitem> getRowitemList(String suitId, String modaltypeId, String modalsheetId) throws Exception;
	/**
	 * 查询一张模板上的所有的列指标
	 * @param suitId
	 * @param modaltypeId
	 * @param modalsheetId
	 * @return
	 * @throws Exception
	 */
	public List<Colitem> getColitemList(String suitId, String modaltypeId, String modalsheetId) throws Exception;
	/**
	 * 查询一张模板上的浮动行列与列指标的映射关系
	 * @param suitId
	 * @param modalsheetId
	 * @return
	 * @throws Exception
	 */
	public List<Colitem> getFjCoitemSetList(String suitId, String modalsheetId) throws Exception;
	/**
	 * 查询列指标是否存在映射
	 */
	public Colitem getFjCoitemSet(@Param(value="c") Colitem c) throws Exception;
	/**
	 * 更新模板列次单元格的物理列位置
	 * @param colNo
	 * @param col
	 * @param modalsheetId
	 * @throws Exception
	 */
	public void batUpdateFjColnoCol(@Param(value="colitems") List<Colitem> colitems) throws Exception;
	/**
	 * 通过行列指标编码，模板id获取行列指标id
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, String> getRowAndColitemId(HashMap<String, String> params) throws Exception;
	/**
	 * 修改模板表头物理行位置（模板的行次所在行）TITLE_MAX_ROW字段
	 * @param modalsheetId
	 * @param titleMaxRow
	 * @throws Exception
	 */
	public void updateTitleMaxRow(String modalsheetId, String titleMaxRow) throws Exception;
	/**
	 * 通过模板集编码，表套ID查询模板集是否存在
	 * @param modaltypeCode
	 * @param suitId
	 * @return
	 * @throws Exception
	 */
	public String isModaLTypeExist(String modaltypeCode, String suitId) throws Exception;
	/**
	 * 通过模板集编码，表套ID更新模板集
	 * @param modaltype
	 * @throws Exception
	 */
	public void updateModaltype(@Param(value="modaltype") ModalType modaltype) throws Exception;
	/**
	 * 新增模板集
	 * @param modaltype
	 * @throws Exception
	 */
	public void insertModaltype(@Param(value="modaltype") ModalType modaltype) throws Exception;
	/**
	 * 通过模板集id, suitId, 模板code判断模板是否存在
	 * @return
	 * @throws Exception
	 */
	public ModalSheet isModaLSheetExist(String modalsheetCode, String modaltypeId, String suitId) throws Exception;
	/**
	 * 通过模板集code, suitId, 模板code更新模板
	 * @param modalsheetCode
	 * @param modaltypeCode
	 * @param suitId
	 * @throws Exception
	 */
	public void updateModalsheet(@Param(value="modalsheet") ModalSheet modalsheet) throws Exception;
	/**
	 * 判断指标属性是否存在
	 * @return
	 * @throws Exception
	 */
	public int isItemProperty(String modalsheetId,String suitId,String pro_type) throws Exception;
	/**
	 * 删除指标属性
	 * @throws Exception
	 */
	public void deleleItemProperty(String modalsheetId,String suitId,String pro_type) throws Exception;
	/**
	 * 删除模板上所有类型的指标属性
	 * @throws Exception
	 */
	public void deleleItemPropertyAlltype(String modalsheetId, String suitId) throws Exception;
	/**
	 * 保存模版指标属性
	 * @return
	 * @throws Exception  loadItemProperty
	 */
	public void insertItemProperty(@Param(value="property") Item_Property item_Property) throws Exception;
	/**
	 * 批量插入模板指标属性
	 * @param itemPropertyList
	 * @throws Exception
	 */
	public void insertBthItemProperty(@Param(value="items") List<Item_Property> items) throws Exception;
	/**
	 * 加载模版指标属性
	 * @return
	 * @throws Exception  
	 */
	public List<CellData> loadItemProperty(String msFormatId,String suitId,String pro_type) throws Exception;
	/**
	 * 查询出模板上的所有指标属性
	 * @param modalsheetId
	 * @param suitId
	 * @return
	 * @throws Exception
	 */
	public List<Item_Property> getItemPropertyList(String modalsheetId, String suitId) throws Exception;
	/**
	 * 判断一个模板是否已经被生成了报表
	 * @param modalsheetId
	 * @return
	 * @throws Exception
	 */
	public int getSheetCountByModalsheetId(String modalsheetId) throws Exception;
	/**
	 * 删除模板
	 * @param modalsheetId
	 * @throws Exception
	 */
	public void deleteModalsheet(String modalsheetId) throws Exception;
	/**
	 * 批量修改行指标引用表ROW字段
	 * @throws Exception
	 */
	public void batUpdateRowitemRefRow(
			@Param(value="modalsheetId") String modalsheetId,
			@Param(value="rowitems") List<Rowitem> rowitemList) throws Exception;
}
