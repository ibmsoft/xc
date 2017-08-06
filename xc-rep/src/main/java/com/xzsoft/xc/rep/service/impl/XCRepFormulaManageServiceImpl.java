/**
 * 
 */
package com.xzsoft.xc.rep.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xzsoft.xc.rep.dao.XCRepFormulaManageDao;
import com.xzsoft.xc.rep.modal.CellFormula;
import com.xzsoft.xc.rep.modal.CellPreFormula;
import com.xzsoft.xc.rep.service.XCRepFormulaManageService;
import com.xzsoft.xc.rep.util.JsonUtil;
import com.xzsoft.xc.rep.util.XCRepFormulaUtil;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.platform.util.PlatformUtil;

/**
 * @ClassName: XCRepFormulaManageServiceImpl 
 * @Description: 取数公式管理
 * @author tangxl
 * @date 2016年9月21日 上午10:55:21 
 *
 */
@Service("xCRepFormulaManageService")
public class XCRepFormulaManageServiceImpl implements XCRepFormulaManageService {
	@Resource
	private XCRepFormulaManageDao xCRepFormulaManageDao;

	/* (non-Javadoc)
	 * @name     saveCellFormula
	 * @author   tangxl
	 * @date     2016年8月4日
	 * @注释                  保存指标公式信息
	 * @version  1.0
	 * @see com.xzsoft.xc.rep.service.XCRepFormulaManageService#saveCellFormula(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void saveCellFormula(HashMap<String, String> map) throws Exception {
		String entityId = map.get("entityId"); 
		String accHrcyId = map.get("accHrcyId");
		String sheetJson = map.get("sheetJson");
		String ledgerId = map.get("ledgerId");
		String formulaCat = map.get("formulaCat");
		
		Map<String, Object> formulaMap = JsonUtil.getJsonObj(sheetJson);
		List<Object> cells = (List<Object>) formulaMap.get("cells");
		
		//解析公式设置数据
		List<CellFormula> list = new ArrayList<CellFormula>();
		List<CellFormula> updateList = new ArrayList<CellFormula>();
        List<HashMap<String, String>> mapList = new ArrayList<HashMap<String,String>>();
		//行列号、公式内容
		String commentArr[] = new String[3];
		String rowitemId=null;
		String colitemId=null;
		for (int i = 0; i < cells.size(); i++) {
			Map<String, Object> cellMap = (Map<String, Object>) cells.get(i);
			
			// 如果是表内公式单元格则跳过
			if(cellMap.containsKey("cal") && Boolean.parseBoolean(String.valueOf(cellMap.get("cal")))){
				continue ;
			}
			
			// 循环单元格公式信息
			if(cellMap.containsKey("json")){
				String json = String.valueOf(cellMap.get("json")) ;
				Map<String,Object> content = JsonUtil.fromJson(json, Map.class) ;
				if(content.containsKey("vname")){
					String[] arr = content.get("vname").toString().split("/");
					if(arr.length==2){
						rowitemId=arr[0];
						colitemId=arr[1];
						
						commentArr[0] = arr[0] ;
						commentArr[1] = arr[1] ;
						// 记录公式串
						if(content.containsKey("data")){
							commentArr[2] = String.valueOf(content.get("data")) ;
						}else{
							commentArr[2] = "" ;
						}
					}else{
						continue;
					}
			    }else{
			    	continue;
			    }
			}else{
				continue;
			}
			
			//判断数据库中公式是否存在
			String famulaId=xCRepFormulaManageDao.checkFormulaIsExist(rowitemId, colitemId, ledgerId, accHrcyId);
				//*-------------------------------更新报表的单元格公式-------------------------
			if(famulaId!=null){
				updateRepFormula(famulaId,commentArr,entityId,accHrcyId,updateList,mapList,ledgerId);
			}else{
				//*--------------------------------新增报表单元格公式----------------------------
				int order = xCRepFormulaManageDao.judgetFormulaType("","ORDER");
				insertRepFormula(cellMap,commentArr,entityId,accHrcyId,list,ledgerId,formulaCat,order+i+1);
			}
		}
		
		// 删除公式预处理信息
		xCRepFormulaManageDao.delPreCellFormula(updateList, mapList);
		
		// 保存公式信息
		xCRepFormulaManageDao.saveCellFormula(updateList,list,mapList);
		
		//模板单元格公式保存完毕后，将公式串解析成key-value<键值对>的形式，存储到数据表中
		List<CellPreFormula> preFormulaList = XCRepFormulaUtil.getInsertFormulaData(list,updateList, map);
		//插入预处理公式
		xCRepFormulaManageDao.savePreCellFormula(preFormulaList);
	}
	/**
	 * 
	 * @methodName  updateRepFormula
	 * @author      tangxl
	 * @date        2016年8月4日
	 * @describe    更新报表的公式设置信息
	 * <document>
	 *    <p>update公式时，对已经为null或者为空的excel单元格，删除其在公式表中的记录</p>
	 * </document>
	 * @param       famulaId
	 * @param       cellMap
	 * @param       commentArr
	 * @param 		entityId
	 * @param 		accHrcyId
	 * @param 		formulaList
	 * @param 		mapList
	 * @throws 		Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public void updateRepFormula(String famulaId,String commentArr[],String entityId,String accHrcyId,List<CellFormula> formulaList,List<HashMap<String, String>> mapList,String ledgerId) throws Exception{
		CellFormula cellFormula=null;
		// 记录待删除的公式信息
		HashMap<String, String> delMap = new HashMap<String, String>();
		// 公式串
		String formulaText = commentArr[2] ;
		//********************************************************************
		//----------------公式内容为空：该单元格的公式内容已经被删除------------------------
		//********************************************************************
		if(formulaText != null && !"".equals(formulaText)){
			cellFormula=new CellFormula();
			cellFormula.setFormulaId(famulaId);
			cellFormula.setFormula(formulaText);
			cellFormula.setFormulaDesc(XCRepFormulaUtil.formulaTranslate("-1",formulaText,accHrcyId,PlatformUtil.getDbType(),xCRepFormulaManageDao));
			cellFormula.setLastUpdatedBy(CurrentSessionVar.getUserId());
			cellFormula.setLastUpdateDate(new Date());
			//XCRepFormulaUtil.getExpFormula
			/*确定公式类型
			 *如果只有GetRepVal类型公式，公式的类型即为 REP
			 *如果是GetRepVal和GetExpVal混合，并且GetExpVal中的自定义函数类型全为REP，则公式类型为REP，否则公式类型为MIXED
			 *如果只有GetExpVal类型，此时若GetExpVal中的自定义函数类型全为REP,公式类型为REP;GetExpVal中的自定义函数类型全为APP;否则为MIXED
			 */
			if(formulaText.contains("GetExpVal")){
				//包含自定义函数时，根据自定义函数的类型决定公式的取数类型，只要有一个 数据中心取数类型即为 APP 否则为REP
				String arrs[] = new String[]{};
				arrs = XCRepFormulaUtil.getExpFormula(formulaText, arrs);
				//GetExpVal("getUrl>>orgId=102,orgName=内蒙古皓腾商贸有限公司")
				StringBuffer paramsBuffer = new StringBuffer();
				for(String exp:arrs){
					exp = exp.replace("GetExpVal(\"", "");
					exp = exp.substring(0, exp.indexOf(">>"));
					paramsBuffer.append("'").append(exp).append("',");
				}
				paramsBuffer.deleteCharAt(paramsBuffer.lastIndexOf(","));
				int judgetCount = xCRepFormulaManageDao.judgetFormulaType(paramsBuffer.toString(),"APP");
				if(judgetCount == 0){
					//函数没有APP类型，全为REP，此时公式的类型为REP
					cellFormula.setFormulaType("REP");
				}else{
					//函数中包含APP类型，再判断函数中是否包含REP类型函数，如果有REP类型，则公式类型为 MIXED；
					judgetCount = xCRepFormulaManageDao.judgetFormulaType(paramsBuffer.toString(),"REP");
					if(judgetCount != 0){
						cellFormula.setFormulaType("MIXED");
					}else{
					//如果函数中没有REP类型，判断整个公式串是否包含GetRepVal，如果包含则为MIXED，不包含则为APP
						if(formulaText.contains("GetRepVal")){
							cellFormula.setFormulaType("MIXED");
						}else{
							cellFormula.setFormulaType("APP");
						}
					}
				}
			}else{
				if(formulaText.contains("GetRepVal")){
					cellFormula.setFormulaType("REP");
				}else{
					cellFormula.setFormulaType("APP");
				}
			}
			//2、记录要修改的数据
			formulaList.add(cellFormula);
		}else{
			//3、删除公式
			delMap.put("rowitemId", commentArr[0]);
			delMap.put("colitemId", commentArr[1]);
			delMap.put("entityId", entityId);
			delMap.put("accHrcyId", accHrcyId);
			delMap.put("ledgerId", ledgerId);
			mapList.add(delMap);
		}
	}
	/**
	 * 
	 * @methodName  insertRepFormula
	 * @author      tangxl
	 * @date        2016年8月4日
	 * @describe    新增报表的公式设置信息
	 * @param       famulaId
	 * @param       cellMap
	 * @param       commentArr
	 * @param 		entityId
	 * @param 		suitId
	 * @param 		formulaList
	 * @param 		mapList
	 * @throws 		Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public void insertRepFormula(Map<String, Object> cellMap,String commentArr[],String entityId,
			String accHrcyId,List<CellFormula> formulaList, String ledgerId,String formulaCat,int order) throws Exception{			

		CellFormula cellFormula=null;
		// 公式串信息
		String formulaText = commentArr[2] ;
		
		if(formulaText != null && !"".equals(formulaText)){
			cellFormula=new CellFormula();
			cellFormula.setLedgerId(ledgerId);
			cellFormula.setFormula(formulaText);
			cellFormula.setFormulaDesc(XCRepFormulaUtil.formulaTranslate("-1",formulaText,accHrcyId,PlatformUtil.getDbType(),xCRepFormulaManageDao));
			cellFormula.setFormulaId(UUID.randomUUID().toString());
			cellFormula.setRowitemId(commentArr[0]);
			cellFormula.setColitemId(commentArr[1]);
			cellFormula.setAccHrcyId(accHrcyId);	
			cellFormula.setFormulaLevel(formulaCat);
			cellFormula.setOrderBy(order);
			if(formulaText.contains("GetExpVal")){
				//包含自定义函数时，根据自定义函数的类型决定公式的取数类型，只要有一个 数据中心取数类型即为 APP 否则为REP
				String arrs[] = new String[]{};
				arrs = XCRepFormulaUtil.getExpFormula(formulaText, arrs);
				//GetExpVal("getUrl>>orgId=102,orgName=内蒙古皓腾商贸有限公司")
				StringBuffer paramsBuffer = new StringBuffer();
				for(String exp:arrs){
					exp = exp.replace("GetExpVal(\"", "");
					exp = exp.substring(0, exp.indexOf(">>"));
					paramsBuffer.append("'").append(exp).append("',");
				}
				paramsBuffer.deleteCharAt(paramsBuffer.lastIndexOf(","));
				int judgetCount = xCRepFormulaManageDao.judgetFormulaType(paramsBuffer.toString(),"APP");
				if(judgetCount == 0){
					//函数没有APP类型，全为REP，此时公式的类型为REP
					cellFormula.setFormulaType("REP");
				}else{
					//函数中包含APP类型，再判断函数中是否包含REP类型函数，如果有REP类型，则公式类型为 MIXED；
					judgetCount = xCRepFormulaManageDao.judgetFormulaType(paramsBuffer.toString(),"REP");
					if(judgetCount != 0){
						cellFormula.setFormulaType("MIXED");
					}else{
					//如果函数中没有REP类型，判断整个公式串是否包含GetRepVal，如果包含则为MIXED，不包含则为APP
						if(formulaText.contains("GetRepVal")){
							cellFormula.setFormulaType("MIXED");
						}else{
							cellFormula.setFormulaType("APP");
						}
					}
				}
			}else{
				if(formulaText.contains("GetRepVal")){
					cellFormula.setFormulaType("REP");
				}else{
					cellFormula.setFormulaType("APP");
				}
			}
			cellFormula.setCreatedBy(CurrentSessionVar.getUserId());
			cellFormula.setCreationDate(new Date());
			cellFormula.setLastUpdatedBy(CurrentSessionVar.getUserId());
			cellFormula.setLastUpdateDate(new Date());
			formulaList.add(cellFormula);	
		}	
	}
}
