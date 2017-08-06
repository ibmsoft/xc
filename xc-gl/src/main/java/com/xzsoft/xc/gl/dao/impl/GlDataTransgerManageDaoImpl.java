package com.xzsoft.xc.gl.dao.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.gl.dao.GlDataTransgerManageDao;
import com.xzsoft.xc.gl.mapper.GlDataTransgerManageMapper;
import com.xzsoft.xc.gl.mapper.GlTreelDataTransManageMapper;
import com.xzsoft.xc.gl.mapper.GlTreelDataValidMapper;
import com.xzsoft.xc.gl.modal.AccountTemplate;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.platform.util.PlatformUtil;
@Repository("glDataTransgerManageDao")
public class GlDataTransgerManageDaoImpl implements GlDataTransgerManageDao {
	@Resource
	private GlDataTransgerManageMapper glDataTransgerManageMapper;
	@Resource
	private GlTreelDataTransManageMapper glTreelDataTransManageMapper;
	@Resource
	private GlTreelDataValidMapper glTreelDataValidMapper;
	@Override
	public List<AccountTemplate> getTemplateByCode(String templateCode)
			throws Exception {
		return glDataTransgerManageMapper.getTemplateByCode(templateCode);
	}
	/*
	 *根据模板编码获取要导入表的校验数据 
	 */
	public List<HashMap> getTemplateDataByCode(String templateCode)
			throws Exception {
		return glDataTransgerManageMapper.getTemplateDataByCode(templateCode);
	}
	/*@title:checkImportValid
	 *@desc :对导入的数据进行校验
	 *<p>
	 *平行级数据 客户、产品、供应商、科目组、计量单位只需校验编码、名称的非重复性、非空性即可
	 *树形级别数据 项目、科目、现金流项目、自定义段，除校验非空、非重复性，还需校验上级编码为末级时，是否已经发生做账，若已经发生做账，则不允许导入!
	 *</p>
	 */
	@Override
	public int checkImportValid(String sessionId, String templateCode,HashMap<String, String> paramsMap)
			throws Exception {
		if("XC_AP_VENDORS".equalsIgnoreCase(templateCode)){
			glDataTransgerManageMapper.checkVendorDataValid(sessionId);
		}else if("XC_GL_DIMENSIONS".equalsIgnoreCase(templateCode)){
			glDataTransgerManageMapper.checkDimensionDataValid(sessionId);	
		}else if("XC_GL_PRODUCTS".equalsIgnoreCase(templateCode)){
			glDataTransgerManageMapper.checkProductDataValid(sessionId);
		}else if("XC_GL_ACC_GROUP".equalsIgnoreCase(templateCode)){
			glDataTransgerManageMapper.checkAccGroupDataValid(sessionId);
		}else if("XC_AR_CUSTOMERS".equalsIgnoreCase(templateCode)){
			glDataTransgerManageMapper.checkCustomerDataValid(sessionId);
		}else if("XC_PM_PROJECTS".equalsIgnoreCase(templateCode)){
			glTreelDataTransManageMapper.checkProjectDataValid(sessionId);
		}else if("XC_GL_ACCOUNTS".equalsIgnoreCase(templateCode)){
			glTreelDataTransManageMapper.checkAccountDataValid(sessionId);
		}else if("XC_GL_CASH_ITEMS".equalsIgnoreCase(templateCode)){
			glTreelDataTransManageMapper.checkCashDataValid(sessionId);
		}else if("XC_GL_LD_CUST_ASS_SEGVALS".equalsIgnoreCase(templateCode)){
			//自定义辅助段值校验
			glTreelDataTransManageMapper.checkSegValDataValid(sessionId);
		}else if("XC_BG_ITEMS".equalsIgnoreCase(templateCode)){
			//预算项目校验
			glTreelDataTransManageMapper.checkBgItemDataValid(sessionId);
		}else{
			//费用项目校验
			glTreelDataTransManageMapper.checkExItemDataValid(sessionId);
		}
		//其他类型
		String tableName = templateCode+"_TMP";
		if("XC_GL_LD_CUST_ASS_SEGVALS".equalsIgnoreCase(templateCode))
			tableName = "XC_GL_LD_CUST_SEGVALS_TMP";
		int count =  glDataTransgerManageMapper.getInvalidImportData(sessionId, tableName);
		if(count == 0){
			if("XC_PM_PROJECTS".equalsIgnoreCase(templateCode)){
				//校验上级科目是否已经发生业务数据
				glTreelDataTransManageMapper.checkUpProjectValid(sessionId);
				count =  glDataTransgerManageMapper.getInvalidImportData(sessionId, tableName);
			}else if("XC_GL_ACCOUNTS".equalsIgnoreCase(templateCode)){
				//校验上级科目是否已经发生业务数据
				glTreelDataTransManageMapper.checkUpAccountValid(sessionId);
				count =  glDataTransgerManageMapper.getInvalidImportData(sessionId, tableName);
			}else if("XC_GL_CASH_ITEMS".equalsIgnoreCase(templateCode)){
				//校验上级现金流是否已经发生业务数据
				glTreelDataTransManageMapper.checkUpCashValid(sessionId);
				count =  glDataTransgerManageMapper.getInvalidImportData(sessionId, tableName);
			}else if("XC_GL_LD_CUST_ASS_SEGVALS".equalsIgnoreCase(templateCode)){
				//校验上级自定义辅助段是否已经发生业务数据
				glTreelDataTransManageMapper.checkUpSegmentValid(sessionId,paramsMap.get("SEG_CODE"));
				count =  glDataTransgerManageMapper.getInvalidImportData(sessionId, tableName);
			}
			return count;
		}else{
			return count;
		}
	}
	/*
	 * @title:      insertTmpDataToFormal
	 * @description:插入数据到正式表后，删除临时表中导入的记录
	 * @author      tangxl
	 * @date        2016年5月9日
	 * @param       sessionId
	 * @param       templateCode
	 * @return
	 * @throws      Exception
	 */
	@Override
	public void insertTmpDataToFormal(String sessionId, String templateCode,HashMap<String, String> paramsMap)
			throws Exception {
		paramsMap.put("sessionId", sessionId);
		paramsMap.put("userId", CurrentSessionVar.getUserId());
		paramsMap.put("dbType", PlatformUtil.getDbType());
		String tableName = templateCode+"_TMP";
		//前五类
		if("XC_AP_VENDORS".equalsIgnoreCase(templateCode)){
			glDataTransgerManageMapper.insertVendorData(sessionId, PlatformUtil.getDbType(),CurrentSessionVar.getUserId());
		}else if("XC_GL_DIMENSIONS".equalsIgnoreCase(templateCode)){
			glDataTransgerManageMapper.insertDimensionData(sessionId, PlatformUtil.getDbType(),CurrentSessionVar.getUserId());
		}else if("XC_GL_PRODUCTS".equalsIgnoreCase(templateCode)){
			glDataTransgerManageMapper.insertProductData(sessionId, PlatformUtil.getDbType(),CurrentSessionVar.getUserId());
		}else if("XC_GL_ACC_GROUP".equalsIgnoreCase(templateCode)){
			glDataTransgerManageMapper.insertAccGroupData(sessionId, PlatformUtil.getDbType(),CurrentSessionVar.getUserId());
		}else if("XC_AR_CUSTOMERS".equalsIgnoreCase(templateCode)){
			glDataTransgerManageMapper.insertCustomerData(sessionId, PlatformUtil.getDbType(),CurrentSessionVar.getUserId());
		}else if("XC_PM_PROJECTS".equalsIgnoreCase(templateCode)){
			glTreelDataValidMapper.insertProjectData(paramsMap);
		}else if("XC_GL_ACCOUNTS".equalsIgnoreCase(templateCode)){
			glTreelDataValidMapper.insertAccountData(paramsMap);
		}else if("XC_GL_CASH_ITEMS".equalsIgnoreCase(templateCode)){
			glTreelDataValidMapper.insertCashItemData(paramsMap);
		}else if("XC_BG_ITEMS".equalsIgnoreCase(templateCode)){
			//预算项目
			glTreelDataValidMapper.insertBgItemData(paramsMap);
		}else if("XC_EX_ITEMS".equalsIgnoreCase(templateCode)){
			//费用项目
			glTreelDataValidMapper.insertExItemData(paramsMap);
		}
		else{
			tableName = "XC_GL_LD_CUST_SEGVALS_TMP";
			glTreelDataValidMapper.insertSegmentData(paramsMap);
		}
		//删除临时表中导入的记录
		glDataTransgerManageMapper.delTemplateModelData(sessionId,tableName);
	}
	/*
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
	@Override
	public List<HashMap> getCurrentImportData(String sessionId,
			String templateCode) throws Exception {
		List<HashMap> returnList = null;
		if("XC_PM_PROJECTS".equalsIgnoreCase(templateCode)){
			returnList = glTreelDataTransManageMapper.getProjectImportData(sessionId);
		}else if("XC_GL_ACCOUNTS".equalsIgnoreCase(templateCode)){
			//-----------------------到这里------------------------------
			returnList = glTreelDataTransManageMapper.getAccountImportData(sessionId);
		}else if("XC_GL_CASH_ITEMS".equalsIgnoreCase(templateCode)){
			returnList = glTreelDataTransManageMapper.getCashImportData(sessionId);
		}else if("XC_GL_LD_CUST_ASS_SEGVALS".equalsIgnoreCase(templateCode)){
			returnList = glTreelDataTransManageMapper.getSegmentImportData(sessionId);
		}
		return returnList;
	}
	/**
	 *@methodName:checkUpCodeValid
	 *@describe  :校验上级编码是否存在
	 */
	@Override
	public int checkUpCodeValid(String sessionId, String templateCode,
			HashMap<String, String> paramsMap) throws Exception {
		int count = 0;
		String tableName = templateCode+"_TMP";
		if("XC_GL_LD_CUST_ASS_SEGVALS".equalsIgnoreCase(templateCode))
			tableName = "XC_GL_LD_CUST_SEGVALS_TMP";
		if("XC_PM_PROJECTS".equalsIgnoreCase(templateCode)){
			//校验上级科目是否存在
			glTreelDataValidMapper.checkUpProjectIsValid(sessionId);
			count =  glDataTransgerManageMapper.getInvalidImportData(sessionId, tableName);
		}else if("XC_GL_ACCOUNTS".equalsIgnoreCase(templateCode)){
			//校验上级科目是否存在
			glTreelDataValidMapper.checkUpAccountIsValid(sessionId);
			count =  glDataTransgerManageMapper.getInvalidImportData(sessionId, tableName);
		}else if("XC_GL_CASH_ITEMS".equalsIgnoreCase(templateCode)){
			//校验上级现金流项目是否存在
			glTreelDataValidMapper.checkUpCashIsValid(sessionId);
			count =  glDataTransgerManageMapper.getInvalidImportData(sessionId, tableName);
		}else if("XC_GL_LD_CUST_ASS_SEGVALS".equalsIgnoreCase(templateCode)){
			//校验上级自定义辅助段是否已经发生业务数据
			glTreelDataValidMapper.checkUpSegmentIsValid(sessionId,paramsMap.get("SEG_CODE"));
			count =  glDataTransgerManageMapper.getInvalidImportData(sessionId, tableName);
		}else if("XC_BG_ITEMS".equalsIgnoreCase(templateCode)){
			//校验预算项目上级项目是否存在
			glTreelDataValidMapper.checkUpBgItemIsValid(sessionId);
			count =  glDataTransgerManageMapper.getInvalidImportData(sessionId, tableName);
		}else{
			//校验费用项目伤及项目是否存在
			glTreelDataValidMapper.checkUpExItemIsValid(sessionId);
			count =  glDataTransgerManageMapper.getInvalidImportData(sessionId, tableName);
		}
		return count;
	}
	/*
	 *@fileName: updateDataLevel
	 *@describe: 更新导入数据的层级、父级id、是否为叶子节点等属性
	 *@return  ： 更新后的所有导入记录 
	 */
	public List<HashMap> updateDataLevel(String sessionId, String templateCode,HashMap<String, String> paramsMap)
			throws Exception {
		int count = 1;
		String tableName = templateCode+"_TMP";
		if("XC_GL_LD_CUST_ASS_SEGVALS".equalsIgnoreCase(templateCode))
			tableName = "XC_GL_LD_CUST_SEGVALS_TMP";
		if("XC_PM_PROJECTS".equalsIgnoreCase(templateCode)){
			//更新项目的上级id
			glTreelDataValidMapper.updateUpProject(sessionId,paramsMap.get("LEDGER_ID"));
			while(count!=0){
				count =  glTreelDataValidMapper.updateProjectItself(sessionId,paramsMap.get("LEDGER_ID"));
			}
		}else if("XC_GL_ACCOUNTS".equalsIgnoreCase(templateCode)){
			//更新科目的层级和是否为叶子节点属性
			glTreelDataValidMapper.updateUpAccount(sessionId);
			while(count!=0){
				count =  glTreelDataValidMapper.updateUpAccountItself(sessionId);
			}
		}else if("XC_GL_CASH_ITEMS".equalsIgnoreCase(templateCode)){
			//更新现金流项目的层级和是否为叶子节点属性
			glTreelDataValidMapper.updateUpCashItem(sessionId);
			while(count!=0){
				count =  glTreelDataValidMapper.updateUpCashItemItself(sessionId);
			}
		}else if("XC_GL_LD_CUST_ASS_SEGVALS".equalsIgnoreCase(templateCode)){
			//更新自定义辅助段项目的上级id
			glTreelDataValidMapper.updateUpSegment(sessionId,paramsMap.get("LEDGER_ID"));
			while(count!=0){
				count =  glTreelDataValidMapper.updateUpSegmentItself(sessionId,paramsMap.get("LEDGER_ID"));
			}
		}else if("XC_BG_ITEMS".equalsIgnoreCase(templateCode)){
			//更新预算项目的上级id
			glTreelDataValidMapper.updateUpBgItem(sessionId,paramsMap.get("BG_HRCY_ID"));
			while(count!=0){
				count =  glTreelDataValidMapper.updateBgItemItself(sessionId,paramsMap.get("BG_HRCY_ID"));
			}
		}else if("XC_EX_ITEMS".equalsIgnoreCase(templateCode)){
			//更新费用项目的上级id
			glTreelDataValidMapper.updateUpExItem(sessionId,paramsMap.get("EX_HRCY_ID"));
			while(count!=0){
				count =  glTreelDataValidMapper.updateExItemItself(sessionId,paramsMap.get("EX_HRCY_ID"));
			}
		}
		//返回本次导入的所有记录
		return glTreelDataValidMapper.getAllImportData(sessionId,tableName);
	}
	@Override
	public int getMaxCashOrder() throws Exception {
		return glTreelDataValidMapper.getMaxCashOrder();
	}
	/* (non-Javadoc)
	 * @see com.xzsoft.xc.gl.dao.GlDataTransgerManageDao#getExportData(java.lang.String)
	 */
	@Override
	public List<HashMap> getExportData(String sql) throws Exception {
		//返回定义sql的执行结果
		return glDataTransgerManageMapper.getExportData(sql);
	}
}
