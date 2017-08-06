package com.xzsoft.xc.gl.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xzsoft.xc.gl.modal.AccountTemplate;

/**
 * @fileName GlDataTransgerManageMapper
 * @desc (总账模板取数管理接口) 
 * @author tangxl
 * @date 2016年5月5日
 */
public interface GlDataTransgerManageMapper {
	/**
	 * 
	 * @title:       getTemplateByCode
	 * @description: 获取模板列信息
	 * @author       tangxl
	 * @date         2016年5月6日
	 * @param        templateCode
	 * @return       List<AccountTemplate>
	 * @throws       Exception
	 */
	public List<AccountTemplate> getTemplateByCode(@Param(value="templateCode")String templateCode);
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
	public List<HashMap> getTemplateDataByCode(@Param(value="templateCode")String templateCode) throws Exception;
	/**
	 * 
	 * @title:        checkVendorDataValid
	 * @description:  校验供应商导入数据的合法性，校验的内容包括编码、名称是否为空，编码名称是否重复
	 * @author        tangxl
	 * @date          2016年5月9日
	 */
	public void checkVendorDataValid(@Param(value="sessionId")String sessionId);
	/**
	 * 
	 * @title:        checkDimensionDataValid
	 * @description:  校验量纲导入数据的合法性，校验的内容包括编码、名称是否为空，编码名称是否重复
	 * @author        tangxl
	 * @date          2016年5月9日
	 */
	public void checkDimensionDataValid(@Param(value="sessionId")String sessionId);
	/**
	 * 
	 * @title:        checkProductDataValid
	 * @description:  校验产品导入数据的合法性，校验的内容包括编码、名称是否为空，编码名称是否重复
	 * @author        tangxl
	 * @date          2016年5月9日
	 */
	public void checkProductDataValid(@Param(value="sessionId")String sessionId);
	/**
	 * 
	 * @title:        checkCustomerDataValid
	 * @description:  校验客户导入数据的合法性，校验的内容包括编码、名称是否为空，编码名称是否重复
	 * @author        tangxl
	 * @date          2016年5月9日
	 */
	public void checkCustomerDataValid(@Param(value="sessionId")String sessionId);
	/**
	 * 
	 * @title:        checkAccGroupDataValid
	 * @description:  校验客户分组导入数据的合法性，校验的内容包括编码、名称是否为空，编码名称是否重复
	 * @author        tangxl
	 * @date          2016年5月9日
	 */
	public void checkAccGroupDataValid(@Param(value="sessionId")String sessionId);
	/**
	 * @title:        getInvalidImportData
	 * @description:  获取不合法的供应商数据总数
	 * @author        tangxl
	 * @date          2016年5月9日
	 */
	public int getInvalidImportData(@Param(value="sessionId")String sessionId,@Param(value="tableName")String tableName);	
	/**
	 * @title:        insertDimensionData
	 * @description:  插入计量单位数据到正式表中
	 * @author        tangxl
	 * @date          2016年5月9日
	 */
	public void insertDimensionData(@Param(value="sessionId")String sessionId,@Param(value="dbType")String dbType,@Param(value="userId")String userId);
	/**
	 * @title:        insertVendorData
	 * @description:  插入供应商单位数据到正式表中
	 * @author        tangxl
	 * @date          2016年5月9日
	 */
	public void insertVendorData(@Param(value="sessionId")String sessionId,@Param(value="dbType")String dbType,@Param(value="userId")String userId);
	/**
	 * @title:        insertProductData
	 * @description:  插入产品数据到正式表中
	 * @author        tangxl
	 * @date          2016年5月9日
	 */
	public void insertProductData(@Param(value="sessionId")String sessionId,@Param(value="dbType")String dbType,@Param(value="userId")String userId);
	/**
	 * @title:        insertCustomerData
	 * @description:  插入客户数据到正式表中
	 * @author        tangxl
	 * @date          2016年5月9日
	 */
	public void insertCustomerData(@Param(value="sessionId")String sessionId,@Param(value="dbType")String dbType,@Param(value="userId")String userId);
	/**
	 * @title:        insertAccGroupData
	 * @description:  插入科目组数据到正式表中
	 * @author        tangxl
	 * @date          2016年5月9日
	 */
	public void insertAccGroupData(@Param(value="sessionId")String sessionId,@Param(value="dbType")String dbType,@Param(value="userId")String userId);
	/**
	 * @title:        delTemplateModelData
	 * @description:  删除临时表中的数据
	 * @author        tangxl
	 * @date          2016年5月9日
	 */
	public void delTemplateModelData(@Param(value="sessionId")String sessionId,@Param(value="tableName")String tableName);
	/**
	 * @title:        getExportData
	 * @description:  获取要导出的数据
	 * @author        tangxl
	 * @date          2016年5月9日
	 */
	public List<HashMap> getExportData(@Param(value="querySql")String querySql);
}
