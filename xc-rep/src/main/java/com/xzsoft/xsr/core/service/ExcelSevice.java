package com.xzsoft.xsr.core.service;

public interface ExcelSevice {
	/**
	 * 导出远光格式下发报表文件 多公司，多模板在一个文件中
	 * added by zhousr 2012-11-14
	 * @param userId
	 * @param dutyId
	 * @param xlsFullName
	 * @param entityCodeList
	 * @param periodId
	 * @param currencyId
	 * @param mdlSheetList
	 * @param mdlType
	 * @param repNum
	 * @param userName
	 * @throws Exception
	 */
	public String exportDataWithMoreCorp(String suitId, String xlsFullName,String entityCodeList,String entityNameList,String periodCode,
			String currencyCode,String mdlSheetList,String sheetNamelist,String mdlType,String repNum,String userId, String mdlshtWithEntity) throws Exception;

}
