package com.xzsoft.xc.gl.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.gl.dao.LedgerDAO;
import com.xzsoft.xc.gl.mapper.LedgerMapper;
import com.xzsoft.xc.gl.modal.LedgerSecDTO;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.platform.util.PlatformUtil;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;

/**
 * @ClassName: LedgerDAOImpl 
 * @Description: 账簿、账簿权限、账簿建账/取消建账 数据层接口实现类
 * @author linp
 * @date 2016年5月13日 下午2:56:45 
 *
 */
@Repository("ledgerDAO")
public class LedgerDAOImpl implements LedgerDAO {

	@Resource
	private LedgerMapper ledgerMapper ;
	
	/**
	 * @Title: getCreateFlag 
	 * @Description: 获取建账标志
	 * @param ledgerId
	 * @param type
	 * @return
	 * @throws Exception    设定文件
	 */
	public HashMap<String,String> getCreateFlag(String ledgerId)throws Exception {
		return ledgerMapper.getCreateFlag(ledgerId) ;
	}
	
	/**
	 * @Title: updateCreateFlag 
	 * @Description: 更新建账标志位
	 * @param ledgerId
	 * @param flag 1-已建账,0-未建账
	 * @throws Exception    设定文件
	 */
	public void updateCreateFlag(String ledgerId,String flag,String createType)throws Exception {
		HashMap<String,Object> map = new HashMap<String,Object>() ;
		map.put("ledgerId", ledgerId);
		map.put("flag", "1".equals(flag)?"Y":"N");
		map.put("createType", createType);
		map.put("cdate", CurrentUserUtil.getCurrentDate()) ;
		map.put("userId", CurrentSessionVar.getUserId()) ;
		
		ledgerMapper.updateCreateFlag(map);
	}	
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getLedgerSec</p> 
	 * <p>Description: 取得账簿授权信息 </p> 
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.LedgerDAO#getLedgerSec()
	 */
	@Override
	public List<String> getLedgerSec(List<String> list) throws Exception {
		List<LedgerSecDTO> oldSecList = ledgerMapper.getLedgerSec(list) ;
		
		List<String> secList = new ArrayList<String>() ;
		for(LedgerSecDTO dto : oldSecList){
			String secStr = dto.getLedgerId().concat("/").concat(dto.getUserId()).concat("/").concat(dto.getRoleId()) ;
			secList.add(secStr) ;
		}
		
		return secList ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: addLedgerSec</p> 
	 * <p>Description: 账簿授权 </p> 
	 * @param list
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.LedgerDAO#addLedgerSec(java.util.List)
	 */
	@Override
	public void addLedgerSec(List<LedgerSecDTO> list) throws Exception {
		try {
			if (list != null && list.size() >0) {
				HashMap<String,Object> map = new HashMap<String,Object>() ;
				map.put("dbType", PlatformUtil.getDbType()) ;
				map.put("list", list) ;
				
				// 批量保存授权信息
				ledgerMapper.saveLdSec(map);
			}
		} catch (Exception e) {
			throw e ;
		}
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: delLedgerSec</p> 
	 * <p>Description: 取消账簿授权信息 </p> 
	 * @param list
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.LedgerDAO#delLedgerSec(java.util.List)
	 */
	@Override
	public void delLedgerSec(List<LedgerSecDTO> list) throws Exception {
		try {
			if (list != null && list.size() >0) {
				HashMap<String,Object> map = new HashMap<String,Object>() ;
				map.put("dbType", PlatformUtil.getDbType()) ;
				map.put("list", list) ;
				
				ledgerMapper.delLdSec(map);
			}
		} catch (Exception e) {
			throw e ;
		}
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: batchDelSec</p> 
	 * <p>Description: 批量删除授权信息 </p> 
	 * @param list
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.LedgerDAO#batchDelSec(java.util.List)
	 */
	@Override
	public void batchDelSec(List<String> list) throws Exception {
		try {
			if (list != null && list.size() >0) {
				HashMap<String,Object> map = new HashMap<String,Object>() ;
				map.put("dbType", PlatformUtil.getDbType()) ;
				map.put("list", list) ;
				
				ledgerMapper.batchDelSec(map);
			}
		} catch (Exception e) {
			throw e ;
		}
	}
}
