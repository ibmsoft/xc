package com.xzsoft.xc.posost.dao.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.posost.dao.XcStTransDao;
import com.xzsoft.xc.posost.mapper.XcStTransMapper;
import com.xzsoft.xc.posost.modal.XcStTransBean;
import com.xzsoft.xip.platform.util.PlatformUtil;
/**
 * 
  * @ClassName XcStTransDaoImpl
  * @Description 处理库存交易明细模块的dao接口实现类
  * @author RenJianJian
  * @date 2016年12月20日 下午12:36:05
 */
@Repository("xcStTransDao")
public class XcStTransDaoImpl implements XcStTransDao {
	@Resource
	private XcStTransMapper xcStTransMapper;
	@Override
	public void insertXcStTrans(List<XcStTransBean> list) throws Exception {
		try {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", list);
			xcStTransMapper.insertXcStTrans(map);
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public void updateXcStTrans(List<XcStTransBean> list) throws Exception {
		try {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", list);
			xcStTransMapper.updateXcStTrans(map);
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public void deleteXcStTrans(List<XcStTransBean> list) throws Exception {
		try {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", list);
			xcStTransMapper.deleteXcStTrans(map);
		} catch (Exception e) {
			throw e;
		}
	}
}
