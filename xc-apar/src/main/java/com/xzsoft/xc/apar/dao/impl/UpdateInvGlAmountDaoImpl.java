package com.xzsoft.xc.apar.dao.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.apar.dao.UpdateInvGlAmountDao;
import com.xzsoft.xc.apar.mapper.UpdateInvGlAmountMapper;
import com.xzsoft.xc.apar.modal.ApDocumentHBean;
import com.xzsoft.xc.apar.modal.ArDocumentHBean;
import com.xzsoft.xip.platform.util.PlatformUtil;
/**
 * 
  * @ClassName UpdateInvGlAmountDaoImpl
  * @Description 对应付单和应收单中的金额金额处理
  * @author RenJianJian
  * @date 2016年9月29日 上午10:30:40
 */
@Repository("updateInvGlAmountDao")
public class UpdateInvGlAmountDaoImpl implements UpdateInvGlAmountDao {
	@Resource
	private UpdateInvGlAmountMapper updateInvGlAmountMapper;
	/*
	 * 
	  * <p>Title updateApInvGlAmount</p>
	  * <p>Description 更新应付单中的金额</p>
	  * @param apDocumentHList
	  * @throws Exception
	  * @see com.xzsoft.xc.apar.dao.UpdateInvGlAmountDao#updateApInvGlAmount(java.util.List)
	 */
	@Override
	public void updateApInvGlAmount(List<ApDocumentHBean> apDocumentHList) throws Exception {
		try {
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("list", apDocumentHList);
			map.put("dbType", PlatformUtil.getDbType());
			updateInvGlAmountMapper.updateApInvGlAmount(map);
		} catch (Exception e) {
			throw e;
		}
	}
	/*
	 * 
	  * <p>Title updateArInvGlAmount</p>
	  * <p>Description 更新应收单中的金额</p>
	  * @param arDocumentHList
	  * @throws Exception
	  * @see com.xzsoft.xc.apar.dao.UpdateInvGlAmountDao#updateArInvGlAmount(java.util.List)
	 */
	@Override
	public void updateArInvGlAmount(List<ArDocumentHBean> arDocumentHList)throws Exception {
		try {
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("list", arDocumentHList);
			map.put("dbType", PlatformUtil.getDbType());
			updateInvGlAmountMapper.updateArInvGlAmount(map);
		} catch (Exception e) {
			throw e;
		}
	}
}