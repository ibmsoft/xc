package com.xzsoft.xc.ex.service.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xzsoft.xc.ex.dao.BorrowMoneyDAO;
import com.xzsoft.xc.ex.modal.IAFactBean;
import com.xzsoft.xc.ex.service.BorrowMoneyService;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xip.platform.util.XipUtil;

/**
 * @ClassName: BorrowMoneyServiceImpl 
 * @Description: 备用金明细处理服务接口实现类
 * @author linp
 * @date 2016年3月10日 上午9:55:00 
 *
 */
@Service("borrowMoneyService")
public class BorrowMoneyServiceImpl implements BorrowMoneyService {
	// 日志记录器
	private static final Logger log = Logger.getLogger(BorrowMoneyServiceImpl.class.getName()) ;
	@Resource
	private BorrowMoneyDAO borrowMoneyDAO ;
	
	/**
	 * 
	 * @title:       handlerUserImprest
	 * @description: 处理个人备用金，同步操作
	 * @author       tangxl
	 * @date         2016年4月6日
	 * @param        ledgerId  账簿id
	 * @param        billId 单据id，<如果是查询个人备用金，则传空> 
	 * @param        billAmount 单据金额 
	 * @param        billType 备用金类型：1-核销款，2-借款，3-还款
	 * @param        operationType 操作类型:1-查询备用金 ,2-更新备用金
	 * @param        userId 用户id
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public synchronized void handlerUserImprest(IAFactBean iaFactBean) throws Exception {
		String operationType = iaFactBean.getOperationType();
		if("1".equals(operationType)){
			//查询个人备用金
			try {
				IAFactBean factBean =borrowMoneyDAO.queryUserImprest(iaFactBean.getLedgerId(), iaFactBean.getExUserId());
				if(factBean != null){
					BeanUtils.copyProperties(factBean, iaFactBean);
				}
			} catch (Exception e) {
				log.error(e.getMessage());
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_handlerUserImprest_EXCEPTION_1", null)+e.getMessage());//查询个人备用金异常，异常信息：
			}
		}else if("2".equals(operationType)){
			//新增操作
			try {
				borrowMoneyDAO.addUserImprest(iaFactBean);
			} catch (Exception e) {
				log.error(e.getMessage());
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_handlerUserImprest_EXCEPTION_2", null)+e.getMessage());//新增个人备用金异常，异常信息：
			}
		}else{
			//新增操作
			try {
				borrowMoneyDAO.deleteUserImprest(iaFactBean);
			} catch (Exception e) {
				log.error(e.getMessage());
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_handlerUserImprest_EXCEPTION_3", null)+e.getMessage());//删除个人备用金异常，异常信息：
			}
		}
	}
}
