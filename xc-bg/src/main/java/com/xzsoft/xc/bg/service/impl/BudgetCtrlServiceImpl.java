package com.xzsoft.xc.bg.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.xzsoft.xc.bg.dao.BudgetCtrlDAO;
import com.xzsoft.xc.bg.dao.XCBGCommonDao;
import com.xzsoft.xc.bg.modal.BgFactBean;
import com.xzsoft.xc.bg.modal.BgFactDTO;
import com.xzsoft.xc.bg.modal.BgItemDTO;
import com.xzsoft.xc.bg.service.BudgetCtrlService;
import com.xzsoft.xc.util.modal.Department;
import com.xzsoft.xc.util.modal.Ledger;
import com.xzsoft.xc.util.modal.Project;

/**
 * @ClassName: BudgetCtrlServiceImpl 
 * @Description: 预算控制服务接口实现类
 * @author linp
 * @date 2016年3月10日 上午9:42:08 
 *
 */
@Service("budgetCtrlService")
public class BudgetCtrlServiceImpl implements BudgetCtrlService {
	@Resource
	private BudgetCtrlDAO budgetCtrlDAO ;
	@Resource
	private XCBGCommonDao xcbgCommonDao ;

	/*
	 * (非 Javadoc) 
	 * <p>Title: budgetCheck</p> 
	 * <p>Description: 预算检查  </p> 
	 * <p>
	 * 	返回值说明：
	 * 		flag   :  检查标记位：0-检查通过,1-检查不通过,2-检查警告,3-检查失败
	 * 		msg    :  如果flag为3, 则存储检查失败的原因
	 * 		result :  如果flag为0、1和2, 则记录预算检查结果描述信息
	 * </p>
	 * @param conditions
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.bg.service.BudgetCtrlService#budgetCheck(java.util.List)
	 */
	@Override
	public String budgetCheck(List<BgFactDTO> conditions) throws Exception {
		String jsonStr = null ;
		try {
			if(conditions != null && conditions.size() >0){
				// 封装参数
				List<BgFactBean> factBeans = new ArrayList<BgFactBean>() ;
				for(BgFactDTO dto : conditions){
					if(dto.getLedgerId() == null) throw new Exception("预算检查时账簿不能为空") ;
					if(dto.getBgItemId() == null) throw new Exception("预算检查时预算项目不能为空") ;
					if(dto.getFactDate()== null) throw new Exception("预算检查时业务日期不能为空") ;
					
					BgFactBean factBean = new BgFactBean() ;
					BeanUtils.copyProperties(dto, factBean);
					
					factBeans.add(factBean) ;
				}
				
				// 执行预算批量检查
				jsonStr = this.enoughBudgetCheck(factBeans) ;
			}
		} catch (Exception e) {
			JSONObject jo = new JSONObject();
			jo.put("flag","3");
			jo.put("msg","预算检查失败："+e.getMessage());
			
			JSONArray ja = new JSONArray() ;
			ja.put(jo) ;
			jsonStr = ja.toString();
		}
		return jsonStr ;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: enoughBudgetCheck</p> 
	 * <p>Description: 预算检查处理 </p> 
	 * @param factBeans
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.bg.service.BudgetCtrlService#enoughBudgetCheck(java.util.List)
	 */
	private String enoughBudgetCheck(List<BgFactBean> factBeans) throws Exception {
		JSONArray ja = new JSONArray() ;
		
		if(factBeans != null && factBeans.size() >0){
			// 查询账簿信息
			String ledgerId = factBeans.get(0).getLedgerId() ;
			Ledger ledger = xcbgCommonDao.getLedger(ledgerId) ;
			
			// 账簿执行预算检查
			if("Y".equals(ledger.getBgIsChk())){
				// 项目ID
				List<String> projectIds = new ArrayList<String>() ; 
				// 预算项目ID
				List<String> bgItemIds = new ArrayList<String>() ;	
				// 成本中心ID
				List<String> deptIds = new ArrayList<String>() ; 
				
				for(BgFactBean factBean : factBeans){
					String projectId= factBean.getProjectId() ;
					if(projectId != null && !"".equals(projectId) && !projectIds.contains(projectId)){
						projectIds.add(projectId) ;
					}
					
					String bgItemId = factBean.getBgItemId() ;
					if(bgItemId != null && !"".equals(bgItemId) && !bgItemIds.contains(bgItemId)){
						bgItemIds.add(bgItemId) ;
					}
					
					String deptId = factBean.getDeptId() ;
					if(deptId != null && !"".equals(deptId) && !deptIds.contains(deptId)){
						deptIds.add(deptId) ;
					}
				}
				
				// 查询总账项目信息
				HashMap<String,Project> prjMap = new HashMap<String,Project>() ;
				if(!projectIds.isEmpty()){
					List<Project> projects = xcbgCommonDao.getProjects(projectIds) ;
					if(projects.size() == projectIds.size()){
						for(Project project : projects){
							prjMap.put(project.getProjectId(), project) ;
						}
					}else{
						// 存在不合法的PA项目信息
					}
				}
				
				// 查询预算项目信息
				HashMap<String,BgItemDTO> bgItemMap = new HashMap<String,BgItemDTO>() ;
				if(!bgItemIds.isEmpty()){
					List<BgItemDTO> bgItems = xcbgCommonDao.getBudgetItems(bgItemIds) ;
					if(bgItems.size() == bgItemIds.size()){
						for(BgItemDTO item : bgItems){
							bgItemMap.put(item.getBgItemId(), item) ;
						}
					}else{
						// 存在不合法的预算项目
					}
				}
				
				// 查询成本中心信息
				HashMap<String,Department> deptMap = new HashMap<String,Department>() ;
				if(!deptIds.isEmpty()){
					List<Department> depts = xcbgCommonDao.getDepts(deptIds) ;
					if(depts.size() == deptIds.size()){
						for(Department dept : depts){
							deptMap.put(dept.getDeptId(), dept) ;
						}
					}else{
						// 存在不合法的成本中心
					}
				}
				
				// 开始执行预算检查处理
				for(BgFactBean factBean : factBeans){
					// 费用预算控制周期
					factBean.setCtrlPeriod(ledger.getBgExPeriod());
					// 预算检查
					JSONObject jo = this.execBudgetCheck(factBean, prjMap, bgItemMap, deptMap);
					
					ja.put(jo) ;
				}	
				
			}else{ 
				// 账簿不执行预算检查
				JSONObject jo = new JSONObject() ;
				jo.put("flag", "0") ;
				jo.put("msg", "") ;
				jo.put("result", "账簿【"+ledger.getLedgerName()+"】不需要执行预算检查") ;
				ja.put(jo) ;
			}
		}
		
		return ja.toString() ;
	}
	
	/**
	 * @Title: execBudgetCheck 
	 * @Description: 循环预算检查
	 * @param ledger
	 * @param factBean
	 * @param prjMap
	 * @param bgItemMap
	 * @param deptMap
	 * @throws Exception    设定文件
	 */
	private JSONObject execBudgetCheck(BgFactBean factBean,HashMap<String,Project> prjMap,
					HashMap<String,BgItemDTO> bgItemMap,HashMap<String,Department> deptMap) throws Exception {
		
		JSONObject jo = new JSONObject() ;
		
		try {
			// 总账项目信息
			String projectId = factBean.getProjectId() ;
			Project prj = prjMap.get(projectId) ;
			
			// 预算项目信息
			BgItemDTO bgItem = null ;
			String bgItemId = factBean.getBgItemId() ;
			if(bgItemId != null){
				bgItem = bgItemMap.get(bgItemId) ;
			}
			
			// 成本中心信息
			Department dept = null ;
			String deptId = factBean.getDeptId() ;
			if(deptId != null){
				dept = deptMap.get(deptId) ;
			}
			
			// 如果涉及PA项目则执行项目预算检查,不涉及PA项目则执行费用预算检查
			if(projectId != null && !"".equals(projectId)){
				// 执行项目预算检查处理
				jo = this.execPrjBgChk(factBean, prj, bgItem, dept);
				
			}else{
				// 执行费用预算检查处理
				jo = this.execCostBgChk(factBean, bgItem, dept);
			}
			
		} catch (Exception e) {
			
			jo.put("flag", "3") ;
			jo.put("msg", "预算检查失败："+e.getMessage()) ;
			jo.put("result", "") ;
		}
		
		return jo ;
	}
	
	/**
	 * @Title: execPrjBgChk 
	 * @Description: 执行项目预算控制
	 * @param factBean
	 * @param prj
	 * @param bgItem
	 * @param dept
	 * @return
	 * @throws Exception    设定文件
	 */
	private JSONObject execPrjBgChk(BgFactBean factBean,Project prj, BgItemDTO bgItem, Department dept) throws Exception {
		JSONObject jo = new JSONObject() ;
		
		try {
			// 判断项目是否需要执行预算控制
			if("Y".equals(prj.getIsBgCtrl())){
				
				// 控制方式：1-不控制，2-预警控制，3-绝对控制
				String ctrlMode = prj.getPrjCtrlMode() ;
				factBean.setCtrlMode(ctrlMode);
				
				// 控制维度：1-项目，2-项目+预算项目，3-项目+预算项目+成本中心
				String ctrlDim = prj.getPrjCtrlDim() ;
				factBean.setCtrlDim(ctrlDim);
				
				// 项目名称
				factBean.setPrjName(prj.getProjectName());
				// 预算项目名称
				if(bgItem != null){
					factBean.setBgItemName(bgItem.getBgItemName());
				}
				// 成本中心名称
				if(dept != null){
					factBean.setDeptName(dept.getDeptName());
				}
				
				if("1".equals(ctrlMode)){ // 不控制
					jo.put("flag", "0") ;
					jo.put("msg", "") ;
					jo.put("result", "预算控制方式：【不控制】, 项目：【"+prj.getProjectName()+"】") ;
					
				}else{ // 预警或绝对控制
					
					// 计算预算余额
					double bgBal = budgetCtrlDAO.getProjectBudgetBal(factBean) ;
					factBean.setBgBal(bgBal);
					
					if("1".equals(ctrlDim)){ // 项目
						jo = this.execPrjChk(factBean) ;
						
					}else if("2".equals(ctrlDim)){ // 项目+预算项目
						String bgItemId = factBean.getBgItemId() ;
						if(bgItemId != null && !"".equals(bgItemId)){
							jo = this.execPrjChk(factBean) ;
							
						}else{
							// 如果此记录未传入预算项目,则通过预算检查
							jo.put("flag", "0") ;
							jo.put("msg", "") ;
							jo.put("result", "控制维度：【项目+预算项目】, 项目：【"+prj.getProjectName()+"】, 预算项目为空") ;
						}
						
					}else if("3".equals(ctrlDim)){ // 项目+预算项目+成本中心
						String bgItemId = factBean.getBgItemId() ;
						String deptId = factBean.getDeptId() ;
						if(bgItemId != null && !"".equals(bgItemId) && deptId != null && !"".equals(deptId)){
							jo = this.execPrjChk(factBean) ;
							
						}else{
							// 如果此记录未传入预算项目或成本中心,则通过预算检查
							String bgItemStr = (bgItemId==null ? "为空" : "：【"+bgItem.getBgItemName()+"】") ;
							String deptStr = (deptId==null ? "为空" : ":【"+dept.getDeptName()+"】") ;
							
							jo.put("flag", "0") ;
							jo.put("msg", "") ;
							jo.put("result", "控制维度：【项目+预算项目+成本中心】, 项目：【"+prj.getProjectName()+"】, 预算项目"+bgItemStr+", 成功中心"+deptStr) ;
						}
					}else{ // 扩展使用
					}
				}
				
			}else{
				jo.put("flag", "0") ;
				jo.put("msg", "") ;
				jo.put("result", "项目【"+prj.getProjectName()+"】不执行预算检查") ;
			}
			
		} catch (Exception e) {
			throw e ;
		}
		
		return jo ;
	}
	
	/**
	 * @Title: execChk 
	 * @Description: 生成项目预算检查结果
	 * @param factBean
	 * @param prj
	 * @return
	 * @throws Exception    设定文件
	 */
	private JSONObject execPrjChk(BgFactBean factBean) throws Exception {
		JSONObject jo = new JSONObject() ;
		
		// 如果预算余额大于当前单据金额,则检查通过
		if(factBean.getBgBal() > factBean.getAmount()){
			jo.put("flag", "0") ;
			jo.put("msg", "") ;
		
		}else{ // 检查不通过
			
			if("2".equals(factBean.getCtrlMode())){ // 预警控制
				jo.put("flag", "2") ;
				jo.put("msg", "") ;
				
			}else if("3".equals(factBean.getCtrlMode())){ // 绝对控制
				jo.put("flag", "1") ;
				jo.put("msg", "") ;
				
			}else{
				// 扩展使用
			}
		}
		
		// 生成检查结果
		if("1".equals(factBean.getCtrlDim())){ // 控制维度：项目
			jo.put("result", "项目：【"+factBean.getPrjName()+"】, 预算余额："+factBean.getBgBal()+", 单据金额："+factBean.getAmount()) ;

		}else if("2".equals(factBean.getCtrlDim())){ // 控制维度：项目+预算项目
			jo.put("result", "项目：【"+factBean.getPrjName()+"】, 预算项目：【"+factBean.getBgItemName()+"】, 预算余额："+factBean.getBgBal()+", 单据金额："+factBean.getAmount()) ;

		}else if("3".equals(factBean.getCtrlDim())){ // 控制维度：项目+预算项目+成本中心
			jo.put("result", "项目：【"+factBean.getPrjName()+"】, 预算项目：【"+factBean.getBgItemName()+"】, 成功中心: 【"+factBean.getDeptName()+"】, 预算余额："+factBean.getBgBal()+", 单据金额："+factBean.getAmount()) ;

		}else{
		}
		
		return jo ;
	}
	
	
	/**
	 * @Title: execCostBgChk 
	 * @Description: 执行费用预算控制
	 * @param factBean
	 * @param prj
	 * @param bgItem
	 * @param dept
	 * @return
	 * @throws Exception    设定文件
	 */
	private JSONObject execCostBgChk(BgFactBean factBean,BgItemDTO bgItem, Department dept) throws Exception {
		JSONObject jo = new JSONObject() ;
		
		// 控制方式：1-不控制，2-预警控制，3-绝对控制
		String ctrlMode = bgItem.getBgCtrlMode() ;
		factBean.setCtrlMode(ctrlMode);
		
		// 控制维度：1-预算项目，2-预算项目+成本中心
		String ctrlDim = bgItem.getBgCtrlDim() ;
		factBean.setCtrlDim(ctrlDim);
		
		// 预算项目名称
		if(bgItem != null){
			factBean.setBgItemName(bgItem.getBgItemName());
		}
		// 成本中心名称
		if(dept != null){
			factBean.setDeptName(dept.getDeptName());
		}
		
		if("1".equals(ctrlMode)){ // 不控制
			jo.put("flag", "0") ;
			jo.put("msg", "") ;
			jo.put("result", "预算控制方式:【不控制】, 预算项目：【"+bgItem.getBgItemName()+"】") ;
			
		}else{ // 预警或绝对控制
			
			// 计算预算余额
			double bgBal = budgetCtrlDAO.getCostBudgetBal(factBean) ;
			factBean.setBgBal(bgBal);
			
			if("1".equals(ctrlDim)){ // 预算项目
				jo = this.execCostChk(factBean) ;
				
			}else if("2".equals(ctrlDim)){ // 预算项目+成本中心
				String deptId = factBean.getDeptId() ;
				if(deptId != null && !"".equals(deptId)){
					jo = this.execCostChk(factBean) ;
					
				}else{
					// 如果此记录未传入预算项目,则通过预算检查
					jo.put("flag", "0") ;
					jo.put("msg", "") ;
					jo.put("result", "控制维度：【预算项目+成本中心】, 预算项目：【"+bgItem.getBgItemName()+"】, 成本中心为空") ;
				}
				
			}else{ // 扩展使用
			}
		}		
		
		return jo ;
	}

	/**
	 * @Title: execCostChk 
	 * @Description: 生成费用预算检查结果
	 * @param factBean
	 * @return
	 * @throws Exception    设定文件
	 */
	private JSONObject execCostChk(BgFactBean factBean) throws Exception {
		JSONObject jo = new JSONObject() ;
		
		// 如果预算余额大于当前单据金额,则检查通过
		if(factBean.getBgBal() > factBean.getAmount()){
			jo.put("flag", "0") ;
			jo.put("msg", "") ;
		
		}else{ // 检查不通过
			
			if("2".equals(factBean.getCtrlMode())){ // 预警控制
				jo.put("flag", "2") ;
				jo.put("msg", "") ;
				
			}else if("3".equals(factBean.getCtrlMode())){ // 绝对控制
				jo.put("flag", "1") ;
				jo.put("msg", "") ;
			}else{
				// 扩展使用
			}
		}
		
		if("1".equals(factBean.getCtrlDim())){ // 控制维度：预算项目
			jo.put("result", "预算项目：【"+factBean.getBgItemName()+"】, 预算余额："+factBean.getBgBal()+", 单据金额："+factBean.getAmount()) ;
			
		}else if("2".equals(factBean.getCtrlDim())){ // 控制维度：预算项目+成本中心
			jo.put("result", "预算项目：【"+factBean.getBgItemName()+"】, 成功中心: 【"+factBean.getDeptName()+"】, 预算余额："+factBean.getBgBal()+", 单据金额："+factBean.getAmount()) ;
		
		}else{
		}
		
		return jo ;
	}
}
