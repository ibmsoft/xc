package com.xzsoft.xc.gl.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;


/**
 * @fileName GlTreelDataTransManageMapper
 * @desc (总账树形基础数据导入接口) 
 * @author tangxl
 * @date 2016年5月5日
 */
public interface GlTreelDataTransManageMapper {
	/**
	 * 
	 * @title:        checkProjectDataValid
	 * @description:  校验项目导入数据的合法性
	 * @author        tangxl
	 * @date          2016年5月9日
	 */
	public void checkProjectDataValid(@Param(value="sessionId")String sessionId);
	/**
	 * 
	 * @title:        checkAccountDataValid
	 * @description:  校验科目导入数据的合法性
	 * @author        tangxl
	 * @date          2016年5月9日
	 */
	public void checkAccountDataValid(@Param(value="sessionId")String sessionId);
	/**
	 * 
	 * @title:        checkCashDataValid
	 * @description:  校验现金流项目导入数据的合法性
	 * @author        tangxl
	 * @date          2016年5月9日
	 */
	public void checkCashDataValid(@Param(value="sessionId")String sessionId);
	/**
	 * 
	 * @title:        checkSegValDataValid
	 * @description:  校验自定义段导入数据的合法性
	 * @author        tangxl
	 * @date          2016年5月9日
	 */
	public void checkSegValDataValid(@Param(value="sessionId")String sessionId);
	/**
	 * 
	 * @title:        checkUpProjectValid
	 * @description:  校验上级项目上级是否已做账
	 * @author        tangxl
	 * @date          2016年5月9日
	 */
	public void checkUpProjectValid(@Param(value="sessionId")String sessionId);
	/**
	 * 
	 * @title:        checkUpAccountValid
	 * @description:  校验上级科目是否已做账
	 * @author        tangxl
	 * @date          2016年5月9日
	 */
	public void checkUpAccountValid(@Param(value="sessionId")String sessionId);
	/**
	 * 
	 * @title:        checkUpCashValid
	 * @description:  校验上级现金流项目是否已做账
	 * @author        tangxl
	 * @date          2016年5月9日
	 */
	public void checkUpCashValid(@Param(value="sessionId")String sessionId);
	/**
	 * 
	 * @title:        checkUpSegmentValid
	 * @description:  校验上级辅助自定义段是否已做账
	 * @author        tangxl
	 * @date          2016年5月9日
	 */
	public void checkUpSegmentValid(@Param(value="sessionId")String sessionId,@Param(value="segCode")String segCode);
	/**
	 * 
	 * @title:      getProjectImportData
	 * @description:获取项目导入数据的上级层次及是否为子节点信息
	 * @author      tangxl
	 * @date        2016年5月9日
	 * @param       sessionId
	 * @return      List<HashMap>
	 */
	public List<HashMap> getProjectImportData(@Param(value="sessionId")String sessionId);
	/**
	 * 
	 * @title:      getAccountImportData
	 * @description:获取科目导入数据的上级层次及是否为子节点信息
	 * @author      tangxl
	 * @date        2016年5月9日
	 * @param       sessionId
	 * @return      List<HashMap>
	 */
	public List<HashMap> getAccountImportData(@Param(value="sessionId")String sessionId);
	/**
	 * 
	 * @title:      getCashImportData
	 * @description:获取现金流导入数据的上级层次及是否为子节点信息
	 * @author      tangxl
	 * @date        2016年5月9日
	 * @param       sessionId
	 * @return      List<HashMap>
	 */
	public List<HashMap> getCashImportData(@Param(value="sessionId")String sessionId);
	/**
	 * 
	 * @title:      getSegmentImportData
	 * @description:获取自定义段导入数据的上级层次及是否为子节点信息
	 * @author      tangxl
	 * @date        2016年5月9日
	 * @param       sessionId
	 * @return      List<HashMap>
	 */
	public List<HashMap> getSegmentImportData(@Param(value="sessionId")String sessionId);
	/**
	 * 
	 * @methodName  checkBgItemDataValid
	 * @author      tangxl
	 * @date        2016年6月16日
	 * @describe    校验预算项目编码名称是否已存在
	 * @param sessionId
	 * @变动说明       
	 * @version     1.0
	 */
	public void checkBgItemDataValid(@Param(value="sessionId")String sessionId); 
	/**
	 * 
	 * @methodName  checkExItemDataValid
	 * @author      tangxl
	 * @date        2016年6月16日
	 * @describe    校验费用项目编码名称是否已存在
	 * @param sessionId
	 * @变动说明       
	 * @version     1.0
	 */
	public void checkExItemDataValid(@Param(value="sessionId")String sessionId);
}
