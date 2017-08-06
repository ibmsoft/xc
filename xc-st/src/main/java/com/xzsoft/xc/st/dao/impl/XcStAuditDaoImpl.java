package com.xzsoft.xc.st.dao.impl;

import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.st.dao.XcStAuditDao;
import com.xzsoft.xc.st.mapper.XcStAuditMapper;
import com.xzsoft.xip.platform.util.PlatformUtil;
/**
 * 
  * @ClassName XcStAuditDaoImpl
  * @Description 流程信息Dao层实现类
  * @author RenJianJian
  * @date 2016年12月8日 下午3:25:08
 */
@Repository("xcStAuditDao")
public class XcStAuditDaoImpl implements XcStAuditDao {
	@Resource
	private XcStAuditMapper xcStAuditMapper;

	@Override
	public void updateXcStInsCode(String insCode, String bizId) throws Exception {
		HashMap<String,String> map = new HashMap<String, String>();
		map.put("insCode",insCode);
		map.put("bizId",bizId);
		xcStAuditMapper.updateXcStInsCode(map);
	}
	@Override
	public void updateXcStAuditStatus(String bizStatusCat, String bizStatusCatDesc, String bizId) throws Exception {
		HashMap<String,String> map = new HashMap<String, String>();
		map.put("bizId",bizId);
		map.put("bizStatusCat",bizStatusCat);
		map.put("bizStatusCatDesc",bizStatusCatDesc);
		xcStAuditMapper.updateXcStAuditStatus(map);
	}
	@Override
	public void updateXcStAuditDate(String bizId) throws Exception {
		HashMap<String,String> map = new HashMap<String, String>();
		map.put("dbType", PlatformUtil.getDbType());
		map.put("bizId",bizId);
		xcStAuditMapper.updateXcStAuditDate(map);
	}
	@Override
	public void revokeXcStCompleteProcess(String bizId) throws Exception {
		HashMap<String,String> map = new HashMap<String, String>();
		map.put("bizId",bizId);
		xcStAuditMapper.revokeXcStCompleteProcess(map);
	}
}