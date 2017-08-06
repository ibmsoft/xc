package com.xzsoft.xsr.core.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xzsoft.xsr.core.modal.CellData;
import com.xzsoft.xsr.core.modal.CellValue;
import com.xzsoft.xsr.core.modal.RepCellValue;
import com.xzsoft.xsr.core.modal.RepSheet;
/**
 * 报表数据采集
 * 
 * @project 报表3.0
 * @version v1.0
 * @author songyh
 * @Date 2016-01-27
 * @desc 报表生成 相关的Mapper类
 */
public interface MakeReportMapper {
	/**
	 * 根据MSFORMAT_ID加载数据单元格
	 * @param msFormatId
	 * @return
	 * @throws Exception
	 */
	public List<CellData> loadDataValue(Map<String,String> map) throws Exception;
	/**
	 * 根据MSFORMAT_ID加载数据单元格(除去有表内公式的)----liuyang
	 * @param msFormatId
	 * @return
	 * @throws Exception
	 */
	public List<CellData> loadDataWithoutC(Map<String,String> map) throws Exception;
	/**
	 * 返回选定报表的浮动行公式
	 */
	public List<CellData> getRepFormulaFj(HashMap params) throws Exception;
	/**
	 * 返回选定报表的浮动行视图取数源
	 */
	public List<CellData> getRepViewFj(HashMap params) throws Exception;
	/**
	 * 返回选定报表状态
	 */
	public String getReportStatus(HashMap params) throws Exception;
	/**
	 * 清空报表数据-固定
	 */
	public void clearData(HashMap params)throws Exception;
	/**
	 * 清空报表数据－浮动
	 */
	public void clearDataFj(HashMap params)throws Exception;
	/**
	 * 解锁或锁定
	 */
	public void lockReport(HashMap params)throws Exception;
	/**
	 * insert 报表
	 */
	public void insertReport(@Param(value="repSheet") RepSheet repSheet)throws Exception;
	/**
	 * update 报表
	 */
	public void updateReport(HashMap params)throws Exception;
	/**
	 * insert 指标值表
	 */
	public void insertCellValue(@Param(value="cellList")  List<CellValue> cellList)throws Exception;
	/**
	 * update 指标值表
	 */
	public void updateCellValue(@Param(value="cellList") List<CellValue> cellList)throws Exception;
	/**
	 * delete 指标值表
	 */
	public void deleteCellValue(HashMap params)throws Exception;
	/**
	 * select 指标值表，有无数据判断
	 */
	public int countOfCellValue(HashMap params)throws Exception;
	
	/**
	 * 判断公司是否为汇总单位
	 * @param msFormatId
	 * @return
	 * @throws Exception
	 */
	public int isHrchyEntity(String suitId,String entity_id) throws Exception;

	/**
	 * 获取汇总单位直接下级单位
	 * @param entity_id 单位ID
	 * @param hrchy_id 公司层次ID
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<String> getHrchySon(String suitId,String entity_id,String hrchy_id) throws Exception;
	
	/**
	 * 删除相应检查结果数据
	 * @param map
	 */
	public void deleteChectResult(Map<String,String> map) throws Exception;
	/**
	 * 判断报表是否存在
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int isSheet(Map<String,String> map) throws Exception;
	
	/**
	 * 节点检查
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int hrchyCheck(Map<String,String> map) throws Exception;
	
	/**
	 * 插入节点检查结果
	 */
	public void insertCheckResultMS(Map<String,String> params)throws Exception;
	/**
	 * 插入节点检查结果明细
	 */
	public void insertCheckResultItem(Map<String,String> params)throws Exception;
	

	/**
	 * 获取列指标数据类型,songyh 2016-01-28
	 * @param msFormatId
	 * @return
	 * @throws Exception
	 */
	public int getColDataType(String suitId,String colitemId) throws Exception;
	/**
	 * 根据ID获取各维度信息的code,songyh 2016-01-28
	 */
	public List<HashMap> getRepCodes(HashMap params) throws Exception;
	/**
	 * 获取模板类型，是否浮动行，,songyh 2016-01-28
	 */
	public String getModalFjFlag(@Param("suitId")String suitId,@Param("modalsheetId")String modalsheetId,@Param("dbType")String dbType) throws Exception;
	/**
	 * 获取选定模板下所有浮动行行记录,songyh 2016-01-28
	 */
	public List<HashMap> getFjRowItem(String suitId,String modalsheetId) throws Exception;
	/**
	 * 获取选定模板浮动行的取数视图注册信息,songyh 2016-01-29
	 */
	public List<HashMap> getFjRowView(String suitId,String modalsheetId,String rowitemId) throws Exception;
	/**
	 * 获取选定模板列指标对应的浮动行表数据列，如DATA1\DATA2...songyh 2016-01-29
	 */
	public String getFjDataCol(String suitId,String modalsheetId,String colitemId) throws Exception;
	/**
	 * 获取选定模板列浮动行设置的视图字段，songyh 2016-01-29
	 */
	public List<HashMap> getFjViewCol(String suitId,String modalsheetId,String rowitemId) throws Exception;
	
	/**
	 * 获取选定模板、选定行指标下的全部数据列的列指标引用,songyh 2016-02-18
	 */
	public List<HashMap> getColRefOnly3(String suitId,String modalsheetId) throws Exception;
	
	/**
	 * 获取浮动行列指标对应DATA_COL
	 */
	public String getDataColBycolId(Map<String,String> map) throws Exception;
	
	/**
	 * 删除相应数据校验结果数据
	 * @param map
	 */
	public void delVerifyFjResult(String userId) throws Exception;
	/**
	 * 判读当前报表是否存在浮动行 ,存在返回浮动行信息
	 */
	public List<HashMap> getFjRow(String suitId,String modalsheetId) throws Exception;
	
	/**
	 * 根据ID获取模板名称
	 */
	public String getSheetNameById(String modalsheetId) throws Exception;
	
	/**
	 * 根据ID获取公司名称
	 */
	public String getEntityNameById(String entityId) throws Exception;
	/**
	 * 查询模板下列指标信息
	 */
	public List<HashMap> getColitemList(String suitId,String modalsheetId) throws Exception;
	/**
	 *查询浮动行汇总合计 
	 */
	public String getSumFj(Map<String,String> map) throws Exception;
	/**
	 *查询固定行值
	 */
	public String getCellV(Map<String,String> map) throws Exception;
	/**
	 * 插入数据校验结果
	 */
	public void insertverifyFjResult(Map map) throws Exception;
	
	/**
	 * 查询某一模版集下，模版的所有关联行---liuyang
	 */
	public List<Map<String, String>> getRowitemInfo(String suitId,String modaltypeId,String modalsheetId)throws Exception;
	/**
	 * 查询某一模版集下，模版的所有浮动行---liuyang/20160225
	 */
	public List<Map<String, String>> getFjRowitemInfo(String suitId,String modaltypeId,String modalsheetId)throws Exception;
	/**
	 * 查询某一模版集下，模版的所有关联列---liuyang
	 */
	public List<Map<String, Object>> getColitemInfo(String suitId,String modaltypeId,String modalsheetId)throws Exception; 
	/**
	 * 判断当前单元格数据是否存在---liuyang/20160224
	 */
	public Map<String, Object> isExitData(Map<String, String> param)throws Exception;
	/**
	 * 批量删除报表固定行数据---liuyang/20160224
	 */
	public int delRepCellValue(List<CellValue> cellValueList)throws Exception;
	/**
	 * 固定行保存数据时，查询指标精度---liuyang/20160325
	 */
	public String selItemPro(String suitId,String modaltypeId,String modalsheetId,String rowitemId,String colitemId)throws Exception;
	/**
	 * 报表数据上报（主表）---liuyang/20160425
	 */
	public List<Map<String, Object>> dataUpload(Map<String, String> param)throws Exception;
	
	/**
	 * 数据上报成功后，更新报表状态信息---liuyang/20160428
	 */
	public int upRepInfo(Map<String, Object> param)throws Exception;
	}
