package com.xzsoft.xc.bg.dao;

import java.util.List;

import com.xzsoft.xc.bg.modal.BgDocDtl;
import com.xzsoft.xc.bg.modal.BgItemBean;
import com.xzsoft.xc.bg.modal.BgItemDTO;
import com.xzsoft.xc.bg.modal.BgLdItemBean;
import com.xzsoft.xc.util.modal.Department;
import com.xzsoft.xc.util.modal.Ledger;
import com.xzsoft.xc.util.modal.Project;

/**
 * @ClassName: XCBGCommonDao 
 * @Description: 预算信息处理Dao层
 * @author 任建建
 * @date 2016年3月29日 下午4:04:04
 *
 */
public interface XCBGCommonDao {
	
	/**
	 * @Title: getDocDtl 
	 * @Description: 根据预算单ID得到预算单明细信息
	 * @param bgDocId
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<BgDocDtl> getDocDtl(String bgDocId) throws Exception;
	
	/**
	 * @Title: getLedgerID 
	 * @Description: 据预算单ID得到账簿ID
	 * @param bgDocId
	 * @return
	 * @throws Exception    设定文件
	 */
	public String getLedgerID(String bgDocId) throws Exception;
	
	/**
	 * @Title: getBgItem 
	 * @Description: 根据预算项ID查询预算项信息(一条)
	 * @param bgItemId
	 * @return
	 * @throws Exception    设定文件
	 */
	public BgItemBean getBgItem(String bgItemId) throws Exception;
	
	/**
	 * @Title: getBgItems 
	 * @Description: 根据预算项ID查询预算项信息(多条)
	 * @param bgItemIds
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<BgItemBean> getBgItems(List<String> bgItemIds) throws Exception;
	
	/**
	 * @Title: getBgLdItem 
	 * @Description: 根据预算项ID查询账簿级预算项信息
	 * @param bgItemId
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<BgLdItemBean> getBgLdItem(String bgItemId) throws Exception;
	
	/**
	 * @Title: getProjectsByLedgerId 
	 * @Description: 根据账簿ID和项目ID信息查询账簿下项目信息
	 * @param projectIds
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<Project> getProjects(List<String> projectIds) throws Exception ;
	
	/**
	 * @Title: getBudgetItems 
	 * @Description: 查询预算项目信息
	 * @param bgItemIds
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<BgItemDTO> getBudgetItems(List<String> bgItemIds) throws Exception ;
	
	/**
	 * @Title: getDepts 
	 * @Description: 查询成本中心信息
	 * @param deptIds
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<Department> getDepts(List<String> deptIds) throws Exception ;
	
	/**
	 * @Title: getLedger 
	 * @Description: 查询账簿信息
	 * @param ledgerId
	 * @return
	 * @throws Exception    设定文件
	 */
	public Ledger getLedger(String ledgerId) throws Exception ;
}
