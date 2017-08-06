package com.xzsoft.xc.ex.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.ex.dao.BorrowMoneyDAO;
import com.xzsoft.xc.ex.mapper.BorrowMoneyMapper;
import com.xzsoft.xc.ex.modal.IAFactBean;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.platform.util.PlatformUtil;
import com.xzsoft.xip.platform.util.XipUtil;

/**
 * @ClassName: BorrowMoneyDAOImpl 
 * @Description: 备用金明细数据层接口实现类
 * @author linp
 * @date 2016年3月10日 上午9:55:24 
 *
 */
@Repository("borrowMoneyDAO")
public class BorrowMoneyDAOImpl implements BorrowMoneyDAO {
	@Resource
	private BorrowMoneyMapper borrowMoneyMapper ;
	
	/**
	 * 
	 * @title:         queryUserImprest
	 * @description:   查询个人备用金
	 * @author         tangxl
	 * @date           2016年4月6日
	 * @param          ledgerId
	 * @param          userId
	 * @return
	 * @throws Exception
	 */
	@Override
	public IAFactBean queryUserImprest(String ledgerId, String userId)
			throws Exception {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("ledgerId", ledgerId);
		map.put("userId", userId);
		return  borrowMoneyMapper.queryUserImprest(map);
	}
	/**
	 * 
	 * @title:         addUserImprest
	 * @description:   新增个人备用金
	 * @author         tangxl
	 * @date           2016年4月6日
	 * @param          iaFactBean
	 * @throws Exception
	 */
	@Override
	public void addUserImprest(IAFactBean iaFactBean) throws Exception {
		//获取单据类型
		String billType = iaFactBean.getExIAType();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("ledgerId", iaFactBean.getLedgerId());
		map.put("userId", iaFactBean.getExUserId());
		IAFactBean maxBean = borrowMoneyMapper.queryUserImprest(map);
		if(billType ==null || "".equals(billType)){
			throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_addUserImprest_EXCEPTION_1", null));//备用金类型为空！
		}else if("1".equals(billType) || "3".equals(billType)){ // 核销款和还款
			//核销、还款减少个人备用金数值
			if(maxBean !=null){
				double maxImprest = maxBean.getIABal();
				if(maxImprest<iaFactBean.getIAFactAmt()){
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_addUserImprest_EXCEPTION_2", null));//单据金额大于账簿备用金余额！
				}else{
					iaFactBean.setIABal(maxImprest-iaFactBean.getIAFactAmt());
				}
			}
		}else{ // 借款
			//借款，增加个人备用金
			if(maxBean !=null){
				double maxImprest = maxBean.getIABal();
                iaFactBean.setIABal(maxImprest+iaFactBean.getIAFactAmt());
			}else{
				iaFactBean.setIABal(iaFactBean.getIAFactAmt());
			}
		}
		//执行插入操作
		iaFactBean.setCreationDate(new Date());
		iaFactBean.setCreatedBy(CurrentSessionVar.getUserId());
		iaFactBean.setLastUpdateDate(new Date());
		iaFactBean.setLastUpdatedBy(CurrentSessionVar.getUserId());
		
		if("mysql".equals(PlatformUtil.getDbType())){
			borrowMoneyMapper.addUserImprestMySQL(iaFactBean);
			
		}else if("oracle".equals(PlatformUtil.getDbType())){
			borrowMoneyMapper.addUserImprestOracle(iaFactBean);
			
		}else{
		}
	}
	
	/**
	 * 
	 * @title:         deleteUserImprest
	 * @description:   删除个人备用金--根据单据来删除备用金，财务复核单据通过后才会生成备用金明细记录，理论上一个单据只会有一条备用金明细记录
	 *                 复核通过的借款单不允许取消复核操作，其他类型的单据<差旅报销、费用报销、还款单>允许取消单据复核操作，取消复核即删除已经
	 *                 生成的备用金记录，重新计算余额。
	 * @author         tangxl
	 * @date           2016年4月6日
	 * @param          iaFactBean ledgerId--账簿id,exUserId--所属人id,docId--单据id
	 * @throws         Exception
	 */
	@Override
	public void deleteUserImprest(IAFactBean iaFactBean) throws Exception {
		// 不允许反向减少备用金的操作，不允许删除借款单记录或者修改借款单记录
		if ("2".equals(iaFactBean.getExIAType())){
			throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_deleteUserImprest_EXCEPTION_1", null));//无法取消借款单复核操作！
		}else{
			//根据单据ID备用金明细记录
			List<IAFactBean> beanList = borrowMoneyMapper.getImprestByDocId(iaFactBean);
			if (beanList !=null && beanList.size() > 0){
				// 删除报销、还款单，增加备用金金额,理论上只有一条记录
				// 如果有多条，先处理备用金序号较小的余额累加操作，然后再执行备用金序号较大的余额累加操作
				for (IAFactBean t : beanList){
					double billAmount = t.getIAFactAmt();
					List<IAFactBean> afterList = borrowMoneyMapper.getImprestByUpdateDate(t);
					for(IAFactBean tmp:afterList){
						tmp.setIABal(tmp.getIABal() + billAmount);
						tmp.setLastUpdateDate(new Date());
						tmp.setLastUpdatedBy(CurrentSessionVar.getUserId());
					}
					//执行删除、更新操作
					borrowMoneyMapper.deleteImprest(t);
					if(afterList != null && afterList.size()>0){
						if("mysql".equalsIgnoreCase(PlatformUtil.getDbType())){
							borrowMoneyMapper.batchUpdateImprestMysql(afterList);
						}else{
							borrowMoneyMapper.batchUpdateImprestOracle(afterList);
						}
					}
				}
			}
		}
	}
}
