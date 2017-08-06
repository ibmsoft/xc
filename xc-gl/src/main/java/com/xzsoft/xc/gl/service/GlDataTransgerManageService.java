package com.xzsoft.xc.gl.service;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.gl.modal.AccountTemplate;


/**
 * @fileName:GlDataTransgerManageService
 * @desc (总账导入导出模板服务管理层接口) 
 * @author tangxl
 * @date 2016年5月5日
 * 
 */
public interface GlDataTransgerManageService {
	/**
	 * @title:       getTemplateByCode
	 * @description: 根据模板编码获取模板配置信息
	 * @author       tangxl
	 * @date         2016年5月5日
	 * @param        templateCode
	 * @return
	 * @throws       Exception
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
	 * @title:       insertDataIntoTmp
	 * @description: 插入数据到临时表中
	 * @author       tangxl
	 * @date         2016年5月9日
	 * @param        sessionId
	 * @param        dataList
	 * @param        templateCode
	 * @param        colNameList
	 * @throws       Exception
	 */
	public void insertDataIntoTmp(String sessionId,List<HashMap> dataList,String templateCode,List<String> colNameList,HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * @title:       checkImportValid
	 * @description: 校验导入数据的合法性 
	 * @author       tangxl
	 * @date         2016年5月9日
	 * @param        sessionId
	 * @return       String
	 * @throws       Exception
	 */
	public String checkImportValid(String sessionId,String templateCode,HashMap<String, String> paramsMap) throws Exception;
	/**
	 * 
	 * @title:       insertTmpDataToFormal
	 * @description: 将数据转存到正式表中 
	 * @author       tangxl
	 * @date         2016年5月9日
	 * @param        sessionId
	 * @return       
	 * @throws       Exception
	 */
	public void insertTmpDataToFormal(String sessionId,String templateCode,HashMap<String, String> paramsMap) throws Exception;
	/**
	 * 
	 * @title:       getCurrentImportData
	 * @description: 获取本次导入数据的上级层次及是否为子节点信息
	 * @author       tangxl
	 * @date         2016年5月9日
	 * @param        sessionId
	 * @return       
	 * @throws       Exception
	 */
	public List<HashMap> getCurrentImportData(String sessionId,String templateCode) throws Exception;
	/**
	 * 
	 * @title:       updateDataLevel
	 * @description: 更新数据层级
	 * @author       tangxl
	 * @date         2016年5月9日
	 * @param        sessionId
	 * @return       
	 * @throws       Exception
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
	 * @param        map
	 * @param        codeNameList
	 * @param        typeMap
	 * @param        codeOrderList
	 * @throws       Exception
	 */
	public List<HashMap> getExportData(HashMap<String, String> map,List<String> codeNameList,HashMap<String, String> typeMap,List<String> codeOrderList) throws Exception;
}
