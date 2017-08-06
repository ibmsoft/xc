package com.xzsoft.xsr.core.service;

import com.xzsoft.xsr.core.modal.ReceiveBean;

public interface BatOpService {

	/**
	 * 批量导出-导出远光格式下发报表文件 多公司，多模板在一个文件中
	 * @param suitId
	 * @param xlsFullName
	 * @param entityCodeList
	 * @param entityNameList
	 * @param periodCode
	 * @param currencyCode
	 * @param mdlSheetList
	 * @param sheetNamelist
	 * @param mdlType
	 * @param repNum
	 * @param userId
	 * @param mdlshtWithEntity
	 * @throws Exception
	 */
	public void exportDataWithMoreCorp(String suitId, String xlsFullName,String entityCodeList,String entityNameList,String periodCode,
			String currencyCode,String mdlSheetList,String sheetNamelist,String mdlType,String repNum,String userId, String mdlshtWithEntity) throws Exception;
	/**
	 * 批量接收-数据解析，并插入数据信息
	 * @param sessionId
	 * @param userId
	 * @param suitId
	 * @param type
	 * @param filename
	 * @param modaltypeId
	 * @param modaltypeCode
	 * @param bean
	 * @param xlsFullPath
	 * @param xlsFullName
	 * @throws Exception
	 */
	public String parseAndAddImpData(String sessionId,String userId,String suitId,String type,String filename,String modaltypeId,String modaltypeCode,ReceiveBean bean,String xlsFullName) throws Exception;
	/**
	 * 批量接收-更新公司
	 * @param bean
	 * @param sessionId
	 */
	public void updateTempEntityData(ReceiveBean bean,String sessionId)throws Exception;
	/**
	 * 批量接收-更新期间
	 * @param bean
	 * @param sessionId
	 * @param suitId
	 */
	public void updateTempPeriodData(ReceiveBean bean,String sessionId,String suitId)throws Exception;
	/**
	 * 批量接收-更新币种
	 * @param suitId
	 * @param sessionId
	 * @throws Exception
	 */
	public void updateTempCurrencyData(String suitId,String sessionId)throws Exception;
	/**
	 * 批量接收-更新模板
	 * @param suitId
	 * @param sessionId
	 * @throws Exception
	 */
	public void updateTempModalData(String suitId,String sessionId,String modaltypeId)throws Exception;
	/**
	 * 批量接收-更新行指标
	 */
	public void updateTempRowitemData(String suitId,String sessionId,String modaltypeId)throws Exception;
	/**
	 * 批量接收-更新列指标
	 */
	public void updateTempColitemData(String suitId,String sessionId,String modaltypeId)throws Exception;
	/**
	 * 批量接收-校验行列指标是否合法
	 */
	public void checkTempRowAndCol(String suitId,String sessionId,String modaltypeId)throws Exception;
	/**
	 * 批量接收-报表数据接收时，判断报表是否已锁定
	 */
	public String isLock(String sessionId, String suitId, String cmStr) throws Exception;
	/**
	 * 根据条件删除固定行和浮动行数据，并进行插入
	 */
	public void receiveData(String suitId,String sessionId,String entityId,String periodId,String currencyId,String modaltypeId,String modalsheetId,String userId)throws Exception;
}
 