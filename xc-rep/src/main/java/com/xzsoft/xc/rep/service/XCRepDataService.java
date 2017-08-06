package com.xzsoft.xc.rep.service;

import java.util.Map;

import com.xzsoft.xc.rep.modal.ESTabBean;
import com.xzsoft.xc.rep.modal.RepSheetBean;

/**
 * @ClassName: XCRepDataService 
 * @Description: 云ERP报表模块数据处理服务接口类
 * @author linp
 * @date 2016年7月21日 下午5:57:05 
 *
 */
public interface XCRepDataService {


	/**
	 * @Title: getReportResult 
	 * @Description: 查询报表结果集信息
	 * @param queryParams
	 * <p>
	 * 	参数说明：
	 * 		{'accHrcyId':'','ledgerId':'','periodCode':'','orgId':'','cnyCode':'','sheetTabId':''}
	 * </p>
	 * @return
	 * @throws Exception    设定文件
	 */
	public Map<String, Object> getReportResult(String queryParams) throws Exception ;
	
	/**
	 * @Title: saveOrUpdateRepData 
	 * @Description: 保存报表指标数据信息
	 * @param paramJson 
	 * <p>
	 * 基础参数：
	 * {
	 *	  'ledgerId'   : '',
	 *	  'periodCode' : '',
	 *	  'orgId'      : '',
	 *	  'cnyCode'    : '',
	 *	  'accHrcyId'  : '',
	 *	  'sheetTabId' : ''
	 *	} 
	 * </p>
	 * @param dataJson
	 * <p>
	 * 报表参数：
	 * cells: [{
	 *	    sheet: 5, 
	 *	    row:1, 
	 *	    col:1, 
	 *	    cal:true, 
	 *	    json:{data:"=sum(1,2)"},
	 *	},....]
	 * </p>
	 * @throws Exception    设定文件
	 */
	public void saveOrUpdateRepData(String paramJson,String dataJson) throws Exception ;

	/**
	 * @Title: lockOrUnlockReports 
	 * @Description: 锁定报表
	 *  <p>
	 *  功能说明：此功能将报表设置为锁定状态，对锁定之后的报表所做的数据修改操作将不会生效。
	 *  参数说明:
	 *  	单表锁定参数格式：
	 *  		{'ledgerId':'','periodCode':'','accHrcyId':'','tabOrder':0,'opMode':'single','opType':'lock'} ,
	 *  	多表锁定参数格式：
	 *  		{'ledgerId':'','periodCode':'','accHrcyId':'','tabOrder':0,'opMode':'batch','opType':'lock','data':[{'sheetId':''},{'sheetId':''},...]}
	 *  </p>
	 * @param request
	 * @param response
	 * @throws Exception    设定文件
	 */
	public void lockOrUnlockReports(String jsonStr)throws Exception ;

	/**
	 * 
	 * @Title: cleanReports 
	 * @Description: 清除报表指标数据
	 * <p>
	 *  删除已经生成的报表的指标数据，包固定行和浮动行指标数据
	 *  参数说明:
	 *  	单表锁定参数格式：
	 *  		{'ledgerId':'','periodCode':'','accHrcyId':'','tabOrder':0,'opMode':'single'} ,
	 *  	多表锁定参数格式：
	 *  		{'ledgerId':'','periodCode':'','accHrcyId':'','tabOrder':0,'opMode':'batch','data':[{'sheetId':''},{'sheetId':''},...]}
	 * </p>
	 * @param request
	 * @param response
	 * @throws Exception    设定文件
	 */
	public void cleanReports(String jsonStr)throws Exception ;
	
	/**
	 * @Title: gatherRepData 
	 * @Description: 批量报表数据采集
	 * @param paramJson
	 * <p>
	 * 参数信息说明：
	 *  {
	 *  	'ledgerId'   : '',
	 *  	'accHrcyId'  : '',
	 *  	'periodCode' : '',
	 *  	'orgId'      : '',
	 *  	'cnyCode'    : '',
	 *  	'tabOrder'   : '',
	 *  	'opMode'     : 'single/batch'
	 *  	'data' : [{'tabId':''},...]
	 *  }
	 *  </p>
	 * @throws Exception    设定文件
	 */
	public void gatherRepData(String paramJson)throws Exception ;
	
	/**
	 * @Title: gatherSingleRepData 
	 * @Description: 采集单张报表
	 * @param sheetBean
	 * @param tabBean
	 * @throws Exception    设定文件
	 */
	public void gatherSingleRepData(RepSheetBean sheetBean, ESTabBean tabBean) throws Exception  ;
	
}
