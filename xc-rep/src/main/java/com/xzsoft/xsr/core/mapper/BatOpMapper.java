package com.xzsoft.xsr.core.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xzsoft.xsr.core.modal.CellValue;
import com.xzsoft.xsr.core.modal.Colitem;
import com.xzsoft.xsr.core.modal.Fraq;
import com.xzsoft.xsr.core.modal.IMPDataBean;
import com.xzsoft.xsr.core.modal.ModalType;
import com.xzsoft.xsr.core.modal.ReceiveBean;
import com.xzsoft.xsr.core.modal.Rowitem;

public interface BatOpMapper {

	/**
	 *批量导出- 获取远光数据
	 * @param msFormatId
	 * @return
	 * @throws Exception
	 */
	public List<CellValue> expYgRepData(String suitId,String entityCode,String periodId,String currencyId,String mdlTypeCode,String mdlSheetCode) throws Exception;
	/**
	 * 批量导出-获取浮动行模版物理列与DATA列的对应
	 */
	public List<Colitem> getFjColitemList(String suitId,String modalsheetCode,String modaltypeCode) throws Exception;
	/**
	 * 批量导出-获取映射表中使用大datacol
	 */
	public List<Colitem> getFJDatacol()throws Exception;
	/**
	 * 批量导出-查询固定行的值相关信息
	 */
	public List<CellValue> getCellValues(String suitId,String entityCode,String periodCode,String currencyCode)throws Exception;
	/**
	 * 批量导出-获取当前登录人的姓名
	 */
	public String getUserName(String userId)throws Exception;
	/**
	 * 批量接收-删除sessionid下的数据信息
	 */
	public void delTempMsg(String userId,String sessionId)throws Exception;
	/**
	 *  批量接收-查询模板集相关信息
	 */
	public ModalType selMdlTypeById(String modaltypeId)throws Exception;
	/**
	 *  批量接收-批量插入数据信息
	 */
	public void batchInsertImpData(@Param(value="dataList") List<IMPDataBean> dataList)throws Exception;
	/**
	 * 批量接收-更新公司-校验单位映射是否唯一
	 */
	public void IsOneTempEntity(String sessionId)throws Exception;
	/**
	 * 批量接收-更新公司-校验公司对照关系
	 */
	public void IsExistTempEntity(String sessionId)throws Exception;
	/**
	 * 批量接收-更新公司-同时更新公司的id和code
	 */
	public void updateTempEntityMsg(String sessionId)throws Exception;
	/**
	 * 批量接收-更新期间-查询上报频率
	 */
	public List<Fraq> getFraq()throws Exception;
	/**
	 * 批量接收-更新期间-查询期间信息
	 */
	public List<ReceiveBean> selTempPeriodMsg(String sessionId) throws Exception;
	/**
	 * 批量接收-更新期间-查询期间是否在兴竹系统中设置
	 */
	public void isExistTempPeriod(String sessionId,String periodCode)throws Exception;
	/**
	 * 批量接收-更新期间-同时更新期间id和报表时间
	 */
	public void updateTempPeriodMsg(String suitId,String sessionId,String periodCode)throws Exception;
	/**
	 * 批量接收-更新期间-更新币种
	 */
	public void updateTempCurrencyMsg(String suitId,String sessionId)throws Exception;
	/**
	 * 批量接收-更新模板-校验模板对照关系
	 */
	public void IsExistTempModal(String sessionId,String modaltypeId)throws Exception;
	/**
	 * 批量接收-更新模板-检验一个远光报表与系统中多个报表映射
	 */
	public void IsOneTempModal(String sessionId,String modaltypeId)throws Exception;
	/**
	 * 批量接收-更新模板-同时更新模板id和code
	 */
	public void updateTempModalMsg(String suitId,String sessionId,String modaltypeId)throws Exception;
	/**
	 * 批量接收-更新行指标-查询行次
	 */
	public List<Rowitem> selRowno(String sessionId)throws Exception;
	/**
	 * 批量接收-更新行指标-判断行次是否为数值类型
	 */
	public void isRowNumber(String sessionId,@Param(value="errList") List<String> errList)throws Exception;
	/**
	 * 批量接收-更新行指标-行指标不存在
	 */
	public void isExistTempRowitem(String suitId,String sessionId,String modaltypeId)throws Exception;
	/**
	 * 批量接收-更新行指标-检查行指标引用关系是否正确
	 */
	public void IsOneTempRowitem(String suitId,String sessionId,String modaltypeId)throws Exception;
	/**
	 * 批量接收-更新行指标-同时更新行指标的id和code
	 */
	public void updateTempRowitemMsg(String suitId,String sessionId,String modaltypeId)throws Exception;
	/**
	 * 批量接收-更新列指标-查询列次
	 */
	public List<Colitem> selColno(String sessionId)throws Exception;
	/**
	 * 批量接收-更新列指标-判断列次是否为数值类型
	 */
	public void isColitemNumber(String sessionId,@Param(value="colnoList") List<String> colnoList)throws Exception;
	/**
	 * 批量接收-更新列指标-列指标不存在
	 */
	public void isExistTempColitem(String suitId,String sessionId,String modaltypeId)throws Exception;
	/**
	 * 批量接收-更新列指标-检查列指标引用关系是否正确
	 */
	public void IsOneTempColitem(String suitId,String sessionId,String modaltypeId)throws Exception;
	/**
	 * 批量接收-更新列指标-同时更新列指标的id和code
	 */
	public void updateTempColitemMsg(String suitId,String sessionId,String modaltypeId)throws Exception;
	/**
	 * 批量接收-校验行列指标是否合法-定义校验行列组合不合法的数据
	 */
	public void checkTempRowAndCol(String suitId,String sessionId,String modaltypeId)throws Exception;
	/**
	 * 批量接收-校验行列指标是否合法- 查询同一表中固定行数据中重复的行列组合
	 */
	public void checkTempFixRowAndCol(String sessionId)throws Exception;
	/**
	 * 批量接收-校验行列指标是否合法-  查询同一表中浮动行数据中重复的行列组合
	 */
	public void checkTempFJRowAndCol(String sessionId)throws Exception;
	/**
	 * 批量接收-查询临时表中相关信息
	 */
	public List<IMPDataBean> getTempData(String suitId,String sessionId,@Param(value="modalsheetList") List<String> modalsheetList,@Param(value="entityIdList") List<String> entityIdList)throws Exception;
	/**
	 * 批量接收-获取报表状态
	 */
	public String getAppStatus(String suitId,String entityId,String periodId,String currencyId,String modaltypeId,String modalsheetId)throws Exception;
	/**
	 * 删除固定行指标数据
	 */
	public void delRepData(String suitId,String entityId,String periodId,String currencyId,String modaltypeId,String modalsheetId)throws Exception;
	/**
	 * 删除浮动行指标数据
	 */
	public void delFJData(String suitId,String entityId,String periodId,String currencyId,String modaltypeId,String modalsheetId)throws Exception;
	/**
	 * 查询临时导入表中固定行数据
	 */
	public List<IMPDataBean> getImpMsg(String suitId,String sessionId,String entityId,String periodId,String currencyId,String modaltypeId,String modalsheetId)throws Exception;
	/**
	 * 插入固定行数据
	 */
	public void insertCellvalue(@Param(value="impDataBeanList") List<IMPDataBean> impDataBeanList)throws Exception;
	/**
	 * 查询导入临时表中浮动行行指标、no、和cellvue
	 */
	public List<IMPDataBean> getFJMsg(String suitId,String sessionId,String entityId,String periodId,String currencyId,String modaltypeId,String modalsheetId)throws Exception;
	/**
	 * 获取浮动行模版物理列与DATA列的对应--通过code
	 */
	public List<Colitem> getFjColitemListById(String suitId,String modaltypeId,String modalsheetId) throws Exception;
	/**
	 * 删除临时表信息
	 * 
	 */
	public void deleteTempImpData(String suitId,String sessionId,String entityId,String periodId,String currencyId,String modaltypeId,String modalsheetId)throws Exception;
}
