package com.xzsoft.xc.bg.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xzsoft.xc.bg.modal.BgDocDtl;
import com.xzsoft.xc.bg.modal.BgItemBean;
import com.xzsoft.xc.bg.modal.BgItemDTO;
import com.xzsoft.xc.bg.modal.BgLdItemBean;
import com.xzsoft.xc.util.modal.Department;
import com.xzsoft.xc.util.modal.Ledger;
import com.xzsoft.xc.util.modal.Project;

/**
  * @ClassName: XCBGCommonMapper
  * @Description: 预算信息处理
  * @author 任建建
  * @date 2016年3月29日 下午4:04:01
 *
 */
public interface XCBGCommonMapper {
	
	/**
	 * @Title: getDocDtl 
	 * @Description: 根据预算单ID得到预算单明细信息
	 * @param bgDocId
	 * @return    设定文件
	 */
	public List<BgDocDtl> getDocDtl(@Param(value="bgDocId")String bgDocId);
	
	/**
	 * @Title: getLedgerID 
	 * @Description: 根据预算单ID得到账簿ID
	 * @param bgDocId
	 * @return    设定文件
	 */
	public String getLedgerID(String bgDocId);
	
	/**
	 * @Title: getBgItem 
	 * @Description: 根据预算项ID查询预算项信息(一条)
	 * @param bgItemId
	 * @return    设定文件
	 */
	public BgItemBean getBgItem(String bgItemId);
	
	/**
	 * @Title: getBgItems 
	 * @Description: 根据预算项ID查询预算项信息(多条)
	 * @param bgItemIds
	 * @return    设定文件
	 */
	public List<BgItemBean> getBgItems(List<String> bgItemIds);
	
	/**
	 * @Title: getBgLdItem 
	 * @Description: 根据预算项ID查询账簿级预算项信息
	 * @param bgItemId
	 * @return    设定文件
	 */
	public List<BgLdItemBean> getBgLdItem(String bgItemId);
	
	/**
	 * @Title: getProjects 
	 * @Description: 查询账簿项目信息
	 * @param projectIds
	 * @return    设定文件
	 */
	public List<Project> getProjects(List<String> projectIds) ;
	
	/**
	 * @Title: getBudgetItems 
	 * @Description: 查询预算项目信息
	 * @param items
	 * @return    设定文件
	 */
	public List<BgItemDTO> getBudgetItems(List<String> items) ;
	
	/**
	 * @Title: getDepts 
	 * @Description: 查询成本中心信息
	 * @param deptIds
	 * @return    设定文件
	 */
	public List<Department> getDepts(List<String> deptIds) ;
	
	/**
	 * @Title: getLedger 
	 * @Description: 查询账簿信息
	 * @param ledgerId
	 * @return    设定文件
	 */
	public Ledger getLedger(String ledgerId) ;
	
}
