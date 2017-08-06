package com.xzsoft.xc.util.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xzsoft.xc.util.dao.XCWFDAO;
import com.xzsoft.xc.util.service.XCWFService;

/**
 * @ClassName: XCWFServiceImpl 
 * @Description: 云ERP对工作流操作服务实现类
 * @author linp
 * @date 2016年6月28日 下午12:02:26 
 *
 */
@Service("xcwfService")
public class XCWFServiceImpl implements XCWFService {

	@Resource
	private XCWFDAO xcwfDAO ;

	/*
	 * (非 Javadoc) 
	 * <p>Title: copyArchInstance</p> 
	 * <p>Description: 复制归档实例信息 </p> 
	 * @param insId
	 * @throws Exception 
	 * @see com.xzsoft.xc.util.service.XCWFService#copyArchInstance(java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void copyArchInstance(String insId) throws Exception {
		xcwfDAO.copyArchInstance(insId);
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: delArchInstance</p> 
	 * <p>Description: 删除归档实例信息  </p> 
	 * @param insId
	 * @throws Exception 
	 * @see com.xzsoft.xc.util.service.XCWFService#delArchInstance(java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void delArchInstance(String insId) throws Exception {
		xcwfDAO.delArchInstance(insId);
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: convertCompletedInstance2Reject</p> 
	 * <p>Description: 将审批完成的的实例转为驳回状态 </p> 
	 * @param insId
	 * @throws Exception 
	 * @see com.xzsoft.xc.util.service.XCWFService#convertCompletedInstance2Reject(java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void convertCompletedInstance2Reject(String insId,String cancelReason) throws Exception {
		xcwfDAO.convertCompletedInstance2Reject(insId,cancelReason);
	}
	
}
