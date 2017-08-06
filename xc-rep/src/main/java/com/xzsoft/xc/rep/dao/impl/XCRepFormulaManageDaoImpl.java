/**
 * 
 */
package com.xzsoft.xc.rep.dao.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.rep.dao.XCRepFormulaManageDao;
import com.xzsoft.xc.rep.mapper.XCRepFormulaMapper;
import com.xzsoft.xc.rep.modal.CellFormula;
import com.xzsoft.xc.rep.modal.CellPreFormula;
import com.xzsoft.xc.rep.modal.DataDTO;
import com.xzsoft.xip.platform.util.PlatformUtil;

@Repository("xCRepFormulaManageDao")
public class XCRepFormulaManageDaoImpl implements XCRepFormulaManageDao {
	@Resource
	private XCRepFormulaMapper xCRepFormulaMapper;
	/* (non-Javadoc)
	 * @name     checkFormulaIsExist
	 * @author   tangxl
	 * @date     2016年8月4日
	 * @注释                   TODO
	 * @version  1.0
	 * @see com.xzsoft.xc.rep.dao.XCRepFormulaManageDao#checkFormulaIsExist(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String checkFormulaIsExist(String rowitemId, String colitemId,
			String ledgerId, String accHrcyId) throws Exception {
		return xCRepFormulaMapper.checkFormulaIsExist(rowitemId, colitemId, ledgerId, accHrcyId);
	}

	/* (non-Javadoc)
	 * @name     方法名
	 * @author   tangxl
	 * @date     2016年8月4日
	 * @version  1.0
	 * @see com.xzsoft.xc.rep.dao.XCRepFormulaManageDao#getPeriodByCode(java.lang.String, java.lang.String)
	 */
	@Override
	public DataDTO getPeriodByCode(String accHrcyId, String code) throws Exception {
		return xCRepFormulaMapper.getPeriodByCode(accHrcyId, code);
	}

	/* (non-Javadoc)
	 * @name     方法名
	 * @author   tangxl
	 * @date     2016年8月4日
	 * @version  1.0
	 * @see com.xzsoft.xc.rep.dao.XCRepFormulaManageDao#getEntityByCode(java.lang.String, java.lang.String)
	 */
	@Override
	public DataDTO getEntityByCode(String accHrcyId, String entityCode)
			throws Exception {
		return xCRepFormulaMapper.getEntityByCode(accHrcyId, entityCode);
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: getLedgerByCode</p> 
	 * <p>Description: </p> 
	 * @param ledgerCode
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.XCRepFormulaManageDao#getLedgerByCode(java.lang.String)
	 */
	@Override
	public DataDTO getLedgerByCode(String ledgerCode)throws Exception {
		return xCRepFormulaMapper.getLedgerByCode(ledgerCode) ;
	}
	
	/* (non-Javadoc)
	 * @name     方法名
	 * @author   tangxl
	 * @date     2016年8月4日
	 * @注释                   TODO
	 * @version  1.0
	 * @see com.xzsoft.xc.rep.dao.XCRepFormulaManageDao#getRowByCode(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public DataDTO getRowByCode(String accHrcyId, String code, String dbType)
			throws Exception {
		return xCRepFormulaMapper.getRowByCode(accHrcyId, code, dbType);
	}

	/* (non-Javadoc)
	 * @name     方法名
	 * @author   tangxl
	 * @date     2016年8月4日
	 * @注释                   TODO
	 * @version  1.0
	 * @see com.xzsoft.xc.rep.dao.XCRepFormulaManageDao#getColByCode(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public DataDTO getColByCode(String accHrcyId, String code, String dbType)
			throws Exception {
		return xCRepFormulaMapper.getColByCode(accHrcyId, code, dbType);
	}

	/* (non-Javadoc)
	 * @name     方法名
	 * @author   tangxl
	 * @date     2016年8月4日
	 * @注释                   TODO
	 * @version  1.0
	 * @see com.xzsoft.xc.rep.dao.XCRepFormulaManageDao#getCnyByCode(java.lang.String, java.lang.String)
	 */
	@Override
	public DataDTO getCnyByCode(String accHrcyId, String code) throws Exception {
		return xCRepFormulaMapper.getCnyByCode(accHrcyId, code);
	}

	/* (non-Javadoc)
	 * @name     方法名
	 * @author   tangxl
	 * @date     2016年8月4日
	 * @注释                   TODO
	 * @version  1.0
	 * @see com.xzsoft.xc.rep.dao.XCRepFormulaManageDao#saveCellFormula(java.util.List, java.util.List, java.util.List)
	 */
	@Override
	public void saveCellFormula(List<CellFormula> updateList,
			List<CellFormula> insertList, List<HashMap<String, String>> delList)
			throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("dbType", PlatformUtil.getDbType());
		//新增公式
		if(insertList !=null && insertList.size()>0){
			map.put("list", insertList);
			xCRepFormulaMapper.insertCellFormula(map);
		}
		//修改公式
		if(updateList !=null && updateList.size()>0){
			map.put("list", updateList);
			xCRepFormulaMapper.updateCellFormula(map);
		}
		//删除公式
		if(delList !=null && delList.size()>0){
			map.put("list", delList);
			xCRepFormulaMapper.deleteCellFormula(map);
		}
	}

	/* (non-Javadoc)
	 * @name     方法名
	 * @author   tangxl
	 * @date     2016年8月5日
	 * @注释                   TODO
	 * @version  1.0
	 * @see com.xzsoft.xc.rep.dao.XCRepFormulaManageDao#savePreCellFormula(java.util.List, java.util.List, java.util.List)
	 */
	@Override
	public void savePreCellFormula(List<CellPreFormula> preFormulaList) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("dbType", PlatformUtil.getDbType());
		if(preFormulaList != null && preFormulaList.size()>0){
			map.put("list", preFormulaList);
			xCRepFormulaMapper.insertPreFormula(map);
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: delPreCellFormula</p> 
	 * <p>Description: 删除公式预处理信息 </p> 
	 * @param updateList
	 * @param delList
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.dao.XCRepFormulaManageDao#delPreCellFormula(java.util.List, java.util.List)
	 */
	@Override
	public void delPreCellFormula(List<CellFormula> updateList,List<HashMap<String, String>> delList) throws Exception{
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("dbType", PlatformUtil.getDbType());
		// 删除公式预处理信息
		if(updateList != null && updateList.size()>0){
			map.put("list", updateList);
			xCRepFormulaMapper.delPreFormulaByBean(map);
		}
		//删除公式预处理信息
		if(delList != null && delList.size()>0){
			map.put("list", delList);
			xCRepFormulaMapper.delPreFormulaByMap(map);
		}
	}
	
	/* (non-Javadoc)
	 * @name     方法名
	 * @author   tangxl
	 * @date     2016年8月11日
	 * @注释                   TODO
	 * @version  1.0
	 * @see com.xzsoft.xc.rep.dao.XCRepFormulaManageDao#judgetFormulaType(java.lang.String)
	 */
	@Override
	public int judgetFormulaType(String params,String judgeType) throws Exception {
		 return xCRepFormulaMapper.judgetFormulaType(params,judgeType);
	}

	/* (non-Javadoc)
	 * @name     方法名
	 * @author   tangxl
	 * @date     2016年10月18日
	 * @注释                   TODO
	 * @version  1.0
	 * @see com.xzsoft.xc.rep.dao.XCRepFormulaManageDao#queryTranslateName(java.lang.String)
	 */
	@Override
	public HashMap<String, String> queryTranslateName(String querySql)
			throws Exception {
		return xCRepFormulaMapper.queryTranslateName(querySql);
	}
}
