package com.xzsoft.xc.rep.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.xzsoft.xc.gl.dao.XCGLCommonDAO;
import com.xzsoft.xc.gl.modal.AccHrcy;
import com.xzsoft.xc.rep.dao.EnterpriseSheetDAO;
import com.xzsoft.xc.rep.dao.TabImpAndExpDAO;
import com.xzsoft.xc.rep.dao.XCRepCommonDAO;
import com.xzsoft.xc.rep.modal.CalFormulaBean;
import com.xzsoft.xc.rep.modal.ESColItemBean;
import com.xzsoft.xc.rep.modal.ESRowItemBean;
import com.xzsoft.xc.rep.modal.ESTabBean;
import com.xzsoft.xc.rep.modal.ESTabCellBean;
import com.xzsoft.xc.rep.modal.FuncBean;
import com.xzsoft.xc.rep.service.TabImpAndExpService;
import com.xzsoft.xc.rep.util.JsonUtil;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xc.util.common.XConstants;

/**
 * @ClassName: TabImpAndExpServiceImpl 
 * @Description: 模板导出与接收处理
 * @author linp
 * @date 2016年10月19日 下午4:23:24 
 *
 */
@Service("tabImpAndExpService")
public class TabImpAndExpServiceImpl implements TabImpAndExpService {
	@Autowired
	private XCRepCommonDAO xcRepCommonDAO ;
	@Autowired
	private XCGLCommonDAO xcglCommonDAO ;
	@Autowired
	private EnterpriseSheetDAO esDAO ;
	@Autowired
	private TabImpAndExpDAO tabImpAndExpDAO ;

	/*
	 * (非 Javadoc) 
	 * <p>Title: expTabs</p> 
	 * <p>Description: 模板导出 </p> 
	 * @param accHrcyId
	 * @param tabs
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.service.TabImpAndExpService#expTabs(java.lang.String, java.util.List)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> expTabs(String accHrcyId, List<String> tabs) throws Exception {
		Map<String,Object> retMap = Maps.newHashMap() ;
		
		// 科目体系信息
		AccHrcy accHrcy = xcglCommonDAO.getAccHrcyById(accHrcyId) ;
		
		// 循环处理
		retMap.put("accHrcyCode", accHrcy.getAccHrcyCode()) ;
		retMap.put("accHrcyName", accHrcy.getAccHrcyName()) ;
		
		// 行指标信息
		List<ESRowItemBean> rowitems = xcRepCommonDAO.getRowItemsByTabs(tabs) ;
		HashMap<String,ESRowItemBean> rowitemMap = new HashMap<String,ESRowItemBean>() ;
		if(rowitems != null && rowitems.size() >0){
			retMap.put("rowitems", rowitems) ;
			for(ESRowItemBean bean : rowitems){
				rowitemMap.put(bean.getRowitemId(), bean) ;
			}
		}
		// 列指标信息
		List<ESColItemBean> colitems = xcRepCommonDAO.getColItemsByTabs(tabs) ;
		HashMap<String,ESColItemBean> colitemMap = new HashMap<String,ESColItemBean>() ;
		if(colitems != null && colitems.size() >0){
			retMap.put("colitems", colitems) ;
			for(ESColItemBean bean : colitems){
				colitemMap.put(bean.getColitemId(), bean) ;
			}
		}
		// 自定义函数信息
		List<FuncBean> functions = xcRepCommonDAO.getAllFuncBean() ;
		if(functions != null && functions.size() >0){
			retMap.put("functions", functions) ;
		}
		//公式信息
		List<CalFormulaBean> formulas = xcRepCommonDAO.getCommonFormulasByTab(accHrcyId, tabs) ;
		if(formulas != null && formulas.size() >0){
			retMap.put("formulas", formulas) ;
		}
		// 模板信息
		List<ESTabBean> tabBeans = esDAO.getESTabBeanByTabs(accHrcyId, tabs) ;
		if(tabBeans != null && tabBeans.size() >0){
			retMap.put("tabs", tabBeans) ;
			
			// 处理单元格批注信息
			for(ESTabBean tabBean : tabBeans){
				List<ESTabCellBean> cells = tabBean.getCells() ;
				if(cells != null && cells.size() >0){
					for(ESTabCellBean cell : cells){
						// 单元格类型
						String cellType = cell.getCellType() ;
						if(cellType == null || "".equals(cellType)) continue ;
						// 单元格批注
						String comment = cell.getComment() ;
						// 单元格内容
						String content = cell.getContent() ;
						Map<String,Object> map = JsonUtil.fromJson(content, Map.class) ;
						// 行列指标ID
						String[] arr = comment.split("/") ;
						String rowitemId = null ;
						String colitemId = null ;
						
						switch(cellType){
						case "row" :	// 行指标
							rowitemId = arr[1] ;
							// 单元格批注
							comment = comment.replace(rowitemId, "") ;
							// 内容批注
							String rc = String.valueOf(map.get("comment")) ;
							map.put("comment", rc.replace(rowitemId, "")) ;
							break ;
						
						case "col" :	// 列指标
							colitemId = arr[1] ;
							// 单元格批注
							comment = comment.replace(colitemId, "") ;
							// 内容批注
							String cc = String.valueOf(map.get("comment")) ;
							map.put("comment", cc.replace(colitemId, "")) ;
							break ;
						
						case "data" :	// 数据区
							rowitemId = arr[0] ;
							colitemId = arr[1] ;
							// 指标编码
							String rowCode = rowitemMap.get(rowitemId).getRowitemCode() ;
							String colCode = colitemMap.get(colitemId).getColitemCode() ;
							// 单元格批注
							comment = comment.replace(rowitemId, rowCode).replace(colitemId, colCode) ;
							// 内容批注
							String _comment = String.valueOf(map.get("comment")) ;
							_comment = _comment.replace(rowitemId, rowCode).replace(colitemId, colCode) ;
							map.put("comment", _comment) ;
							// 内容变量
							String _vname = String.valueOf(map.get("vname")) ;
							_vname = _vname.replace(rowitemId, rowCode).replace(colitemId, colCode) ;
							map.put("vname", _vname) ;
							break ;
						
						default :	// 其他
							break ;
						}
						cell.setComment(comment);
						cell.setContent(JsonUtil.toJson(map));
					}
				}
			}
		}
		
		return retMap ;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: impTabs</p> 
	 * <p>Description: 模板接收 </p> 
	 * @param map
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.service.TabImpAndExpService#impTabs(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	@Override
	public void impTabs(Map<String, Object> map) throws Exception {
		// 科目体系信息
		String accHrcyId = String.valueOf(map.get("accHrcyId")) ;
		String language = XCUtil.getLang();
		AccHrcy accHrcy = new AccHrcy() ;
		if(accHrcyId != null && !"".equals(accHrcyId)){
			accHrcy.setAccHrcyId(accHrcyId);
			
		}else{
			String accHrcyCode = String.valueOf(map.get("accHrcyCode")) ;
			String accHrcyName = String.valueOf(map.get("accHrcyName")) ;
			accHrcy = xcglCommonDAO.getAccHrcyByCode(accHrcyCode) ;
			if(accHrcy == null) {
				if(XConstants.ZH.equalsIgnoreCase(language)){
					throw new Exception("科目体系不存在； 编码："+accHrcyCode+",名称："+accHrcyName) ;
				}else{
					throw new Exception("Accounting items system is not existed,the code is："+accHrcyCode+",and the name is："+accHrcyName) ;
				}
				
			}
		}
		
		// 扩展函数信息
		List<Object> functions = (List<Object>) map.get("functions") ;
		tabImpAndExpDAO.saveOrUpdateFunctions(functions);
		
		// 行指标信息
		List<Object> rowitems = (List<Object>) map.get("rowitems") ;
		tabImpAndExpDAO.saveOrUpdateRowitems(rowitems,accHrcy);
		
		// 列指标信息
		List<Object> colitems = (List<Object>) map.get("colitems") ;
		tabImpAndExpDAO.saveOrUpdateColitems(colitems,accHrcy);
		
		// 公共级公式信息
		List<Object> formulas = (List<Object>) map.get("formulas") ;
		tabImpAndExpDAO.saveOrUpdateFormulas(formulas,accHrcy);
		
		// 模板信息
		List<Object> tabs = (List<Object>) map.get("tabs") ;
		tabImpAndExpDAO.saveOrUpdateTabs(tabs,accHrcy);
	}

}
