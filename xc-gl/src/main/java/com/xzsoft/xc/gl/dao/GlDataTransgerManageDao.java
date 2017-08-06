package com.xzsoft.xc.gl.dao;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.gl.modal.AccountTemplate;

/**
 * @fileName GlDataTransgerManageDao
 * @desc (总账模板取数接口) 
 * @author tangxl
 * @date 2016年5月5日
 */
public interface GlDataTransgerManageDao {
	/**
	 * @title:  getTemplateByCode
	 * @description:   TODO
	 * @author  tangxl
	 * @date    2016年5月5日
	 * @param templateCode
	 * @return
	 * @throws Exception
	 */
	public List<AccountTemplate> getTemplateByCode(String templateCode) throws Exception; 
	/**
	 * 
	 * @title:       getTemplateDataByCode
	 * @description: 根据模板编码获取要导入表的数据
	 * @author       tangxl
	 * @date         2016年5月6日
	 * @param        templateCode
	 * @return       List<HashMap>
	 * @throws       Exception
	 */
	public List<HashMap> getTemplateDataByCode(String templateCode) throws Exception;
	/**
	 * 
	 * @title:      checkImportValid
	 * @description:校验导入数据的合法性
	 * @author      tangxl
	 * @date        2016年5月9日
	 * @param       sessionId
	 * @param       templateCode
	 * @return
	 * @throws      Exception
	 */
	public int checkImportValid(String sessionId,String templateCode,HashMap<String, String> paramsMap) throws Exception;
	/**
	 * 
	 * @title:      insertTmpDataToFormal
	 * @description:插入数据到正式表
	 * @author      tangxl
	 * @date        2016年5月9日
	 * @param       sessionId
	 * @param       templateCode
	 * @return
	 * @throws      Exception
	 */
	public void insertTmpDataToFormal(String sessionId,String templateCode,HashMap<String, String> paramsMap) throws Exception;
	/**
	 * 
	 * @title:      getCurrentImportData
	 * @description:获取本次导入数据的上级层次及是否为子节点信息
	 * @author      tangxl
	 * @date        2016年5月9日
	 * @param       sessionId
	 * @param       templateCode
	 * @return      List<HashMap>
	 * @throws      Exception
	 */
	public List<HashMap> getCurrentImportData(String sessionId,String templateCode) throws Exception;
	
	/**
	 * 
	 * @title:      checkUpCodeValid
	 * @description:校验导入数据上级编码的合法性
	 * @author      tangxl
	 * @date        2016年5月9日
	 * @param       sessionId
	 * @param       templateCode
	 * @return
	 * @throws      Exception
	 */
	public int checkUpCodeValid(String sessionId,String templateCode,HashMap<String, String> paramsMap) throws Exception;
	/**
	 * 
	 * @title:      updateDataLevel
	 * @description:更新数据层级
	 * @author      tangxl
	 * @date        2016年5月9日
	 * @param       sessionId
	 * @param       templateCode
	 * @return      List<HashMap>
	 * @throws      Exception
	 */
	public List<HashMap> updateDataLevel(String sessionId,String templateCode,HashMap<String, String> paramsMap) throws Exception;
	/**
	 * 
	 * @title:       getMaxCashOrder
	 * @description: 获取现金流的最大排序号
	 * @author       tangxl
	 * @date         2016年5月9日
	 * @param        sessionId
	 * @return       
	 * @throws       Exception
	 */
	public int getMaxCashOrder() throws Exception;
	/**
	 * 
	 * @title:       getExportData
	 * @description: 获取要导出的数据
	 * @author       tangxl
	 * @date         2016年5月23日
	 * @param        sql
	 * @return       
	 * @throws       Exception
	 */
	public List<HashMap> getExportData(String sql) throws Exception;
}
