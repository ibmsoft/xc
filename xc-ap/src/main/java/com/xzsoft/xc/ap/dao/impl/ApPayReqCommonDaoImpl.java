package com.xzsoft.xc.ap.dao.impl;
/**
 * 
  * @ClassName: ApPayReqCommonDaoImpl
  * @Description: 操作付款申请单Dao实现类
  * @author 韦才文
  * @date 2016年6月14日 上午16:15:15
 */
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.ap.dao.ApPayReqCommonDao;
import com.xzsoft.xc.ap.mapper.ApPayReqCommonMapper;
import com.xzsoft.xc.ap.modal.ApPayReqHBean;
import com.xzsoft.xc.ap.modal.ApPayReqLBean;
import com.xzsoft.xc.apar.modal.ApInvTransBean;
import com.xzsoft.xip.platform.util.PlatformUtil;
@Repository("apPayReqCommonDao")
public class ApPayReqCommonDaoImpl implements ApPayReqCommonDao{
	@Resource
	private ApPayReqCommonMapper payReqMapper;
	/**
	 * @Title getPayReqHById
	 * @Description: 通过ID查询付款申请单主表信息 
	 * 参数格式:
	 * @param java.lang.String
	 * @return com.xzsoft.xc.ap.modal.ApPayReqHBean
	 * @throws 
	 */
	@Override
	public ApPayReqHBean getPayReqHById(String payReqHId) {
		return payReqMapper.getPayReqHById(payReqHId);
	}
	/**
	 * @Title addPayReqH
	 * @Description: 新增付款申请单主表信息
	 * 参数格式:
	 * @param com.xzsoft.xc.ap.modal.ApPayReqHBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void addPayReqH(ApPayReqHBean payH) {
		payReqMapper.addPayReqH(payH);
		
	}
	/**
	 * @Title editPayReqH
	 * @Description: 修改付款申请单主表信息
	 * 参数格式:
	 * @param com.xzsoft.xc.ap.modal.ApPayReqHBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void editPayReqH(ApPayReqHBean payH) {
		payReqMapper.editPayReqH(payH);
		
	}
	/**
	 * @Title delPayReqH
	 * @Description: 删除付款申请单主表信息
	 * 参数格式:
	 * @param java.lang.String
	 * @return 
	 * @throws 
	 */
	@Override
	public void delPayReqH(String payReqHId) {
		payReqMapper.delPayReqH(payReqHId);
		
	}
	/**
	 * @Title getPayReqL
	 * @Description: 查询付款申请单行表信息 
	 * 参数格式:
	 * @param java.lang.String
	 * @return com.xzsoft.xc.ap.modal.ApPayReqLBean
	 * @throws 
	 */
	@Override
	public ApPayReqLBean getPayReqL(String payReqLId){
		return payReqMapper.getPayReqL(payReqLId);
	}
	/**
	 * @Title getPayReqLByHId
	 * @Description:  通过付款申请单主表ID查询付款申请单行表信息 
	 * 参数格式:
	 * @param java.lang.String
	 * @return java.util.List
	 * @throws 
	 */
	@Override
	public List<HashMap<String, Object>> getPayReqLByHId(String payReqHId){
		return payReqMapper.getPayReqLByHId(payReqHId);
	}
	/**
	 * @Title addPayReqL
	 * @Description:  新增付款申请单行表信息 
	 * 参数格式:
	 * @param com.xzsoft.xc.ap.modal.ApPayReqLBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void addPayReqL(ApPayReqLBean payReqLBean) {
		payReqMapper.addPayReqL(payReqLBean);
		
	}
	/**
	 * @Title editPayReqL
	 * @Description:  修改付款申请单行表信息 
	 * 参数格式:
	 * @param com.xzsoft.xc.ap.modal.ApPayReqLBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void editPayReqL(ApPayReqLBean payReqLBean) {
		payReqMapper.editPayReqL(payReqLBean);
		
	}
	/**
	 * @Title delPayReqL
	 * @Description: 删除付款申请单行表信息
	 * 参数格式:
	 * @param java.lang.String
	 * @return 
	 * @throws 
	 */
	@Override
	public void delPayReqL(String payReqLId) {
		payReqMapper.delPayReqL(payReqLId);
		
	}
	/**
	 * @Title addInvTrans
	 * @Description:添加交易明细表信息 
	 * 参数格式:
	 * @param  com.xzsoft.xc.apar.modal.ApInvTransBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void addInvTrans(ApInvTransBean apInvTransBean) {
		payReqMapper.addInvTrans(apInvTransBean);
		
	}
	/**
	 * @Title delInvTrans
	 * @Description:删除交易明细表信息
	 * 参数格式:
	 * @param  java.util.HashMap
	 * @return 
	 * @throws 
	 */
	@Override
	public void delInvTrans(HashMap<String, String> map){
		payReqMapper.delInvTrans(map);
		
	}
	
	/**
	 * @Title judgeAmount
	 * @Description:判断付款申请金额是否大于未付金额
	 * 参数格式:
	 * @param  java.util.HashMap
	 * @return java.lang.String
	 * @throws 
	 */
	@Override
	public String judgeAmount(HashMap<String, Object> map) {
		return payReqMapper.judgeAmount(map);
	}
	/*
	 * (非 Javadoc) 
	 * <p>Title: getLedgerCatInfo</p> 
	 * <p>Description: 获取账簿级费用单据类型信息 </p> 
	 * @param ledgerId
	 * @param docCat
	 * @return
	 * @throws Exception 
	 * 
	 */
	@Override
	public HashMap<String, String> getLedgerCatInfo(String ledgerId,String docCat) throws Exception {
		HashMap<String,String>  catMap = null ;
				
		try {
			// 查询账簿级费用单据类型信息
			HashMap<String, String> map = new HashMap<String, String>() ;
			map.put("ledgerId", ledgerId) ;
			map.put("docCat", docCat) ;
			
			catMap = payReqMapper.getLdCatInfo(map) ;
			
		} catch (Exception e) {
			throw e ;
		}
		
		return catMap;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: getGlobalCatInfo</p> 
	 * <p>Description: 获取全局费用单据类型信息 </p> 
	 * @param docCat
	 * @return
	 * @throws Exception 
	 * 
	 */
	@Override
	public HashMap<String, String> getGlobalCatInfo(String docCat) throws Exception {
		return payReqMapper.getGbCatInfo(docCat) ;
	}
	/**
	 * @Title editTrans
	 * @Description:修改交易明细表信息
	 * 参数格式:
	 * @param  java.util.HashMap
	 * @return 
	 * @throws 
	 */
	@Override
	public void editTrans(HashMap<String, Object> map) {
		payReqMapper.editTrans(map);
		
	}
	/**
	 * @Title updatePayReqByClose
	 * @Description:更新申请单（单据关闭时）
	 * 参数格式:
	 * @param  java.util.HashMap
	 * @return 
	 * @throws 
	 */
	@Override
	public void updatePayReqByClose(HashMap<String, Object> map) {
		payReqMapper.updatePayReqByClose(map);
		
	}
	/**
	 * @Title updateReqHAmout
	 * @Description:更新付款主表金额
	 * 参数格式:
	 * @param  java.util.List
	 * @return 
	 * @throws 
	 */
	@Override
	public void updateReqHAmout(List<ApPayReqHBean> reqHBeanList){
		if(reqHBeanList!=null&&reqHBeanList.size()>0){
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", reqHBeanList);
			payReqMapper.updateReqHAmout(map);
		}
		
	}
	/**
	 * @Title updateReqLAmount
	 * @Description:更新付款行表金额
	 * 参数格式:
	 * @param  java.util.List
	 * @return 
	 * @throws 
	 */
	@Override
	public void updateReqLAmount(List<ApPayReqLBean> reqLBeanList){
		if(reqLBeanList!=null&&reqLBeanList.size()>0){
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", reqLBeanList);
			payReqMapper.updateReqLAmount(map);
		}
	}

}
