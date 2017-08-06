package com.xzsoft.xc.ap.dao.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.xzsoft.xc.ap.dao.ApVoucherDao;
import com.xzsoft.xc.ap.mapper.APVoucherMapper;
import com.xzsoft.xc.ap.modal.ApVoucherHandlerBean;
import com.xzsoft.xip.platform.util.PlatformUtil;
/**
 * 
  * @ClassName ApVoucherDaoImpl
  * @Description 应付模块凭证相关处理Dao层实现类
  * @author 任建建
  * @date 2016年4月6日 下午8:45:38
 */
@Repository("apVoucherDao")
public class ApVoucherDaoImpl implements ApVoucherDao {
	public static final Logger log = Logger.getLogger(ApVoucherDaoImpl.class);
	
	@Resource
	private APVoucherMapper apVoucherMapper;
	/*
	 * 
	  * <p>Title: saveNewApVoucher</p>
	  * <p>Description: 凭证保存更新应付应付模块单据的凭证信息</p>
	  * @param apVoucherHandler
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.ApVoucherDao#saveNewApVoucher(com.xzsoft.xc.ap.modal.ApVoucherHandlerBean)
	 */
	@Override
	public void saveNewApVoucher(ApVoucherHandlerBean apVoucherHandler) throws Exception {
		try {
			//保存新增--单据凭证信息
			apVoucherMapper.saveNewApVoucher(apVoucherHandler);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
	/*
	 * 
	  * <p>Title saveCheckApVoucher</p>
	  * <p>Description 凭证（审核：起草状态--提交状态--审核通过状态，取消审核：审核通过--提交--起草）时更新应付模块单据的凭证信息</p>
	  * @param avhbList
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.ApVoucherDao#saveCheckApVoucher(java.util.List)
	 */
	@Override
	public void saveCheckApVoucher(List<ApVoucherHandlerBean> avhbList) throws Exception {
		try {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", avhbList);
			//保存审核--单据凭证信息
			apVoucherMapper.saveCheckApVoucher(map);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
	/*
	 * 
	  * <p>Title saveSignApVoucher</p>
	  * <p>Description 凭证签字（取消签字）时更新应付模块单据的凭证信息</p>
	  * @param avhbList
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.ApVoucherDao#saveSignApVoucher(java.util.List)
	 */
	@Override
	public void saveSignApVoucher(List<ApVoucherHandlerBean> avhbList) throws Exception {
		try {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", avhbList);
			//保存签字--单据凭证信息
			apVoucherMapper.saveSignApVoucher(map);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
}