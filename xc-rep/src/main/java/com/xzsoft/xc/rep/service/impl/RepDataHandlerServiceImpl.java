package com.xzsoft.xc.rep.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xzsoft.xc.rep.dao.XCRepCommonDAO;
import com.xzsoft.xc.rep.dao.XCRepDataDAO;
import com.xzsoft.xc.rep.modal.CalFormulaBean;
import com.xzsoft.xc.rep.modal.CellValueBean;
import com.xzsoft.xc.rep.modal.ESTabBean;
import com.xzsoft.xc.rep.modal.RepCurrencyBean;
import com.xzsoft.xc.rep.modal.RepSheetBean;
import com.xzsoft.xc.rep.service.AbstractRepDataHandlerService;
import com.xzsoft.xc.rep.util.XCRepCacheVarsUtil;
import com.xzsoft.xc.rep.util.XCRepConstants;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xc.util.common.XConstants;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;


/**
 * @ClassName: RepDataHandlerServiceImpl 
 * @Description: 报表数据处理服务实现类
 * @author linp
 * @date 2016年8月5日 上午9:48:33 
 *
 */
@Service("repDataHandlerServiceImpl")
public class RepDataHandlerServiceImpl extends AbstractRepDataHandlerService {
	@Autowired
	private XCRepDataDAO xcRepDataDAO ;
	@Autowired
	private XCRepCommonDAO xcRepCommonDAO ;
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: handlerRepSheet</p> 
	 * <p>Description: 处理报表基础信息 </p> 
	 * @param sheetBean
	 * @param tabBean
	 * @throws Exception 
	 */
	@Override
	public void handlerRepSheet(RepSheetBean sheetBean,ESTabBean tabBean) throws Exception {
		// 当前用户和时间
		String userId = CurrentSessionVar.getUserId() ;
		Date cdate = CurrentUserUtil.getCurrentDate() ;
		String language = XCUtil.getLang();
		// 查询报表信息
		sheetBean.setTabId(tabBean.getTabId());
		RepSheetBean repSheetBean = xcRepDataDAO.getSheetBeanByTabId(sheetBean) ;
		
		if(repSheetBean == null){ 
			// 报表未创建
			sheetBean.setSheetId(UUID.randomUUID().toString());
			sheetBean.setTabId(tabBean.getTabId());
			sheetBean.setSheetDesc(tabBean.getTabName());
			sheetBean.setAppStatus("A");	// 报表状态：A-已生成；C-审批中；D-驳回；E-已锁定
			sheetBean.setAuditStatus("A");
			if(XConstants.ZH.equalsIgnoreCase(language)){
				sheetBean.setAuditStatusDesc("起草");
			}else{
				sheetBean.setAuditStatusDesc("draft");
			}
			sheetBean.setCreatedBy(userId);
			sheetBean.setCreationDate(cdate);
			sheetBean.setLastUpdateDate(cdate);
			sheetBean.setLastUpdatedBy(userId);
			// 执行保存
			xcRepDataDAO.insertSheet(sheetBean);
			
		}else{ 
			// 报表已创建
			String appStatus = repSheetBean.getAppStatus() ;	
			// 报表状态：A-草稿, E-已锁定, 其他-未生成
			if("E".equals(appStatus)) {
				if(XConstants.ZH.equalsIgnoreCase(language)){
					throw new Exception(tabBean.getTabName()+"已锁定不能采集!") ;
				}else{
					throw new Exception("Can not execute data collecting,because "+tabBean.getTabName()+" is locked!") ;
				}
				
			}else{
				// 设置修改日期和修改人
				repSheetBean.setLastUpdateDate(cdate);
				repSheetBean.setLastUpdatedBy(userId);
				
				// 执行更新
				xcRepDataDAO.updateSheet(repSheetBean);
			}
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: handlerFixedCellValue</p> 
	 * <p>Description: 处理报表固定行指标值信息  </p> 
	 * @param sheetBean
	 * @param tabBean
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.service.AbstractRepDataHandlerService#handlerCellValue(com.xzsoft.xc.rep.modal.RepSheetBean, com.xzsoft.xc.rep.modal.ESTabBean)
	 */
	@Override
	public void handlerFixedCellValue(RepSheetBean sheetBean, ESTabBean tabBean) throws Exception {
		// 取数公式信息
		HashMap<String,List<CalFormulaBean>> formulas = xcRepCommonDAO.getRepAllCalFormulasByTplAndFType(sheetBean) ;
		
		if(formulas != null && formulas.size()>0){
			// 查询报表原指标值信息
			HashMap<String, CellValueBean> oldValMaps = xcRepDataDAO
					.getFixedCellValuesToMap(sheetBean.getLedgerId(),sheetBean.getPeriodCode(), tabBean.getTabId());
			
			// 纯APP类公式计算指标值
			List<CalFormulaBean> appFormulas = formulas.get("appFormulas") ;
			if(appFormulas != null && appFormulas.size()>0){
				this.saveFixedCellValues(appFormulas, sheetBean, oldValMaps);
			}
			// 混合类公式计算指标值
			List<CalFormulaBean> mixedFormulas = formulas.get("mixedFormulas") ;
			if(mixedFormulas != null && mixedFormulas.size()>0){
				this.saveFixedCellValues(mixedFormulas, sheetBean, oldValMaps);
			}
			// 纯REP类公式计算指标值
			List<CalFormulaBean> repFormulas = formulas.get("repFormulas") ;
			if(repFormulas != null && repFormulas.size()>0){
				this.saveFixedCellValues(repFormulas, sheetBean, oldValMaps);
			}
		}
	}

	/**
	 * @Title: saveFixedCellValues 
	 * @Description: 运算公式
	 * @param formulas
	 * @param map
	 * @param oldValMaps
	 * @param precisionMap
	 * @throws Exception    设定文件
	 */
	private void saveFixedCellValues(List<CalFormulaBean> formulas,
			RepSheetBean sheetBean,HashMap<String,CellValueBean> oldValMaps) throws Exception {
		
		// 当前用户ID和当前日期
		String userId = CurrentSessionVar.getUserId() ;
		Date cdate = CurrentUserUtil.getCurrentDate() ;
		
		// 币种信息
		RepCurrencyBean currency = xcRepCommonDAO.getRepCurrencyByCode(sheetBean.getCnyCode()) ;
		
		// 循环处理公式
		for(CalFormulaBean formula : formulas){
			// 计算公式指标值表达式
			String equation = this.getArithmeticByFormula(formula, sheetBean) ;
			
			// 处理指标值
			String cellV = null ;
			String cellTextV = null ;
			
			// 获取指标数据类型 : 1-项目,3-number,5-text	
			String colItemId = formula.getColItemId() ;
			String dataType = "3" ;	// 数值类型
			JSONObject colItemJo = XCRepCacheVarsUtil.getCachedData(XCRepConstants.XC_REP_COLITEM, sheetBean.getAccHrcyId(), colItemId) ;
			if(colItemJo != null && colItemJo.has("datatype")){
				dataType = colItemJo.getString("datatype") ;
			}
			switch(Integer.parseInt(dataType)){
			case 1 :
				cellTextV = equation ;
				break ;
			case 3 :
				cellV = this.evalFormula(equation) ;
				break ;
			case 5 :
				cellTextV = equation ;
				break ;
			default:
				break ;
			}
			
			// 原指标值
			String oldValKey = formula.getRowItemId().concat("/").concat(colItemId) ;
			CellValueBean oldValBean = null ;
			if(oldValMaps != null && oldValMaps.containsKey(oldValKey)){
				oldValBean = oldValMaps.get(oldValKey) ;
			}
			
			// 处理新指标值信息
			if((cellV==null || "".equals(cellV) || (cellV != null && Double.parseDouble(cellV) == 0))
					&& (cellTextV == null || "".equals(cellTextV))){
				// 如果新指标值为0或空且原指标值不为空时，则执行删除处理
				if(oldValBean != null){
					xcRepDataDAO.deleteItemValue(oldValBean);
				}
				
			}else{
				// 指标精度：默认值为2位小数，否则取币种的进度信息
				int itemPrecision = (currency.getPericison()==0 ? 2 : currency.getPericison()) ;

				// 处理指标值精度
				BigDecimal itemVal = new BigDecimal(cellV).setScale(itemPrecision,BigDecimal.ROUND_HALF_UP) ;
				
				if(oldValBean == null){
					// 新增指标值
					CellValueBean bean = new CellValueBean() ;
					bean.setValId(UUID.randomUUID().toString());
					bean.setLedgerId(sheetBean.getLedgerId());
					bean.setOrgId(sheetBean.getOrgId());
					bean.setRowItemId(formula.getRowItemId());
					bean.setColItemId(formula.getColItemId());
					bean.setPeriodCode(sheetBean.getPeriodCode());
					bean.setCnyCode(sheetBean.getCnyCode());
					bean.setCellV(itemVal.doubleValue());
					bean.setCellTextV(cellTextV);
					bean.setCreatedBy(userId);
					bean.setCreationDate(cdate);
					bean.setLastUpdatedBy(userId);
					bean.setLastUpdateDate(cdate);
					
					xcRepDataDAO.insertItemValue(bean) ;
					
				}else{
					// 文本值
					String oldCellTextV = oldValBean.getCellTextV() ;
					oldCellTextV = (oldCellTextV==null ? "" : oldCellTextV) ;
					
					String newCellTextV = (cellTextV==null ? "" : cellTextV) ;
					
					// 如果指标值未发生改变则不需要执行更新处理
					if(oldValBean.getCellV() == itemVal.doubleValue() && newCellTextV.equals(oldCellTextV)){
						continue ;
					}
					// 修改指标值
					oldValBean.setCellV(itemVal.doubleValue());
					oldValBean.setCellTextV(cellTextV);
					oldValBean.setLastUpdatedBy(userId);
					oldValBean.setLastUpdateDate(cdate);
					
					xcRepDataDAO.updateItemValue(oldValBean);
				}
			}
		}
	}

}
