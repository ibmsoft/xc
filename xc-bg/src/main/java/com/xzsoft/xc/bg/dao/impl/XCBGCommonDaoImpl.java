package com.xzsoft.xc.bg.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.xzsoft.xc.bg.dao.XCBGCommonDao;
import com.xzsoft.xc.bg.mapper.XCBGCommonMapper;
import com.xzsoft.xc.bg.modal.BgDocDtl;
import com.xzsoft.xc.bg.modal.BgItemBean;
import com.xzsoft.xc.bg.modal.BgItemDTO;
import com.xzsoft.xc.bg.modal.BgLdItemBean;
import com.xzsoft.xc.util.modal.Department;
import com.xzsoft.xc.util.modal.Ledger;
import com.xzsoft.xc.util.modal.Project;

/**
 * 
  * @ClassName: BgDocInfoDaoImpl
  * @Description: 预算信息处理Dao层实现类
  * @author 任建建
  * @date 2016年3月29日 下午4:04:13
 */
@Repository("xcgbCommonDao")
public class XCBGCommonDaoImpl implements XCBGCommonDao {
	private static final Logger log = Logger.getLogger(XCBGCommonDaoImpl.class);
	@Resource
	private XCBGCommonMapper xcgbCommonMapper;
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getDocDtl</p> 
	 * <p>Description: 根据预算单ID得到预算单明细信息 </p> 
	 * @param bgDocId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.bg.dao.XCBGCommonDao#getDocDtl(java.lang.String)
	 */
	@Override
	public List<BgDocDtl> getDocDtl(String bgDocId) throws Exception{
		List<BgDocDtl> docDtlList = new ArrayList<BgDocDtl>();
		try {
			docDtlList = xcgbCommonMapper.getDocDtl(bgDocId);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return docDtlList;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getLedgerID</p> 
	 * <p>Description: 根据预算单ID得到账簿 </p> 
	 * @param bgDocId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.bg.dao.XCBGCommonDao#getLedgerID(java.lang.String)
	 */
	@Override
	public String getLedgerID(String bgDocId) throws Exception{
		String ledgerId = null;
		try {
			ledgerId = xcgbCommonMapper.getLedgerID(bgDocId);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return ledgerId;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getBgItem</p> 
	 * <p>Description: 根据预算项ID查询预算项信息(一条) </p> 
	 * @param bgItemId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.bg.dao.XCBGCommonDao#getBgItem(java.lang.String)
	 */
	@Override
	public BgItemBean getBgItem(String bgItemId) throws Exception {
		BgItemBean bgItemBean = new BgItemBean();
		try {
			bgItemBean = xcgbCommonMapper.getBgItem(bgItemId);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return bgItemBean;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getBgItems</p> 
	 * <p>Description: 根据预算项ID查询预算项信息(多条) </p> 
	 * @param bgItemIds
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.bg.dao.XCBGCommonDao#getBgItems(java.util.List)
	 */
	@Override
	public List<BgItemBean> getBgItems(List<String> bgItemIds) throws Exception {
		List<BgItemBean> bgItemList = new ArrayList<BgItemBean>();
		try {
			if(bgItemIds != null && bgItemIds.size() > 0){
				for(int i = 0;i < bgItemIds.size();i++){
					String bgItemId = bgItemIds.get(i);
					BgItemBean bgItemBean = this.getBgItem(bgItemId);
					bgItemList.add(i, bgItemBean);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return bgItemList;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getBgLdItem</p> 
	 * <p>Description: 根据预算项ID查询账簿级预算项信息 </p> 
	 * @param bgItemId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.bg.dao.XCBGCommonDao#getBgLdItem(java.lang.String)
	 */
	@Override
	public List<BgLdItemBean> getBgLdItem(String bgItemId) throws Exception {
		List<BgLdItemBean> ldItemLists = new ArrayList<BgLdItemBean>();
		try {
			ldItemLists = xcgbCommonMapper.getBgLdItem(bgItemId);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return ldItemLists;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getProjectsByLedgerId</p> 
	 * <p>Description: 查询项目信息 </p> 
	 * @param projectIds
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.bg.dao.XCBGCommonDao#getProjects(java.lang.String, java.util.List)
	 */
	@Override
	public List<Project> getProjects(List<String> projectIds) throws Exception {
		return xcgbCommonMapper.getProjects(projectIds) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getBudgetItems</p> 
	 * <p>Description: 查询预算项目信息 </p> 
	 * @param bgItemIds
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.bg.dao.XCBGCommonDao#getBudgetItems(java.util.List)
	 */
	@Override
	public List<BgItemDTO> getBudgetItems(List<String> bgItemIds) throws Exception {
		return xcgbCommonMapper.getBudgetItems(bgItemIds) ;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: getDepts</p> 
	 * <p>Description: 查询成本中心信息 </p> 
	 * @param deptIds
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.bg.dao.XCBGCommonDao#getDepts(java.util.List)
	 */
	@Override
	public List<Department> getDepts(List<String> deptIds) throws Exception {
		return xcgbCommonMapper.getDepts(deptIds) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getLedger</p> 
	 * <p>Description: 查询账簿信息 </p> 
	 * @param ledgerId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.bg.dao.XCBGCommonDao#getLedger(java.lang.String)
	 */
	@Override
	public Ledger getLedger(String ledgerId) throws Exception {
		return xcgbCommonMapper.getLedger(ledgerId) ;
	}
	
}