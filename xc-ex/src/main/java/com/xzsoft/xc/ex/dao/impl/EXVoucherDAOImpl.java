package com.xzsoft.xc.ex.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.ex.dao.EXVoucherDAO;
import com.xzsoft.xc.ex.mapper.EXVoucherMapper;
import com.xzsoft.xc.ex.modal.EXDocDtlBean;
import com.xzsoft.xc.ex.modal.PayBankModeBean;
import com.xzsoft.xc.ex.modal.PayModeBean;
import com.xzsoft.xc.gl.modal.Account;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xip.platform.util.XipUtil;

/**
 * @ClassName: EXVoucherDAOImpl 
 * @Description: 单据复核、单据支付的凭证处理数据层接口实现类
 * @author linp
 * @date 2016年3月10日 下午4:55:52 
 *
 */
@Repository("exVoucherDAO")
public class EXVoucherDAOImpl implements EXVoucherDAO {
	@Resource
	private EXVoucherMapper exVoucherMapper ;

	/*
	 * (非 Javadoc) 
	 * <p>Title: getPayMode</p> 
	 * <p>Description: 查询账簿收付款方式 </p> 
	 * @param modeId
	 * @param ledgerId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.dao.EXVoucherDAO#getPayMode(java.lang.String, java.lang.String)
	 */
	@Override
	public PayModeBean getPayMode(String modeId,String ledgerId) throws Exception {
		HashMap<String,String> map = new HashMap<String,String>() ;
		map.put("ledgerId", ledgerId);
		map.put("modeId", modeId) ;
		
		return exVoucherMapper.getPayModeBean(map) ;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: getPayBank</p> 
	 * <p>Description: 查找账簿开户行信息 </p> 
	 * @param depositBankId
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.dao.EXVoucherDAO#getPayBank(java.lang.String)
	 */
	@Override
	public PayBankModeBean getPayBank(String depositBankId) throws Exception {
		return exVoucherMapper.getPayBank(depositBankId) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getExItemAccs</p> 
	 * <p>Description: 查询费用项目对应的科目信息 </p> 
	 * @param docDtls
	 * @param ledgerId
	 * @param docId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.dao.EXVoucherDAO#getExItemAccs(java.util.List, java.lang.String, java.lang.String)
	 */
	public HashMap<String,Object> getExItemAccs(List<EXDocDtlBean> docDtls,String ledgerId,String docId) throws Exception {
		 HashMap<String,Object> map = null ;
		 
		 // 费用项目ID
		 List<String> exItemIds = new ArrayList<String>();
		 HashMap<String,String> dtlMap = new HashMap<String,String>() ;
		 for(EXDocDtlBean dtlBean : docDtls){
			 String exItemId = dtlBean.getExItemId() ; 
			 if(!exItemIds.contains(exItemId)){
				 exItemIds.add(exItemId) ;
				 dtlMap.put(exItemId, dtlBean.getExItemCode().concat(dtlBean.getExItemName())) ;
			 }
		 }
		 
		 // 定义查询参数
		 HashMap<String,Object> paramMap = new  HashMap<String,Object>();
		 paramMap.put("ledgerId", ledgerId);
		 paramMap.put("docId", docId);
		 paramMap.put("list", exItemIds);
		 
		 // 执行查询并封装返回值
		 List<HashMap<String,String>> accList = exVoucherMapper.getExItemAccs(paramMap) ;
		 
		 // 费用项目对应的会计科目是否分配到当前账簿或已失效
		 if(accList != null && (exItemIds.size() != accList.size())){
			 for(HashMap<String,String> accMap : accList){
				 String exItemId = accMap.get("EX_ITEM_ID") ;
				 exItemIds.remove(exItemId) ;
			 }
			 if(exItemIds != null && exItemIds.size()>0){
				 StringBuffer str = new StringBuffer() ;
				 //str.append("费用项目:【") ; 
				 str.append(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_PROJECT", null)+"【") ; 
				 for(String id : exItemIds){
					 str.append(dtlMap.get(id)).append(", ") ;
				 }
				 //str.append("】对应会计科目有误！") ;
				 str.append("】"+XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DY_ACCOUNT_IS_ILLEGAL", null)) ;
				 //str.append("错误原因可能为：费用项目未指定对应会计科目 或者 费用项目对应会计科目未分配到当前账簿 或者  费用项目对应会计科目在当前账簿下已失效!") ;
				 str.append(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_ERROR_REASON", null)) ;
				 
				 throw new Exception(str.toString()) ;
			 }
		 }
		 
		 // 封装科目信息
		 if(accList != null && accList.size()>0){
			 map = new  HashMap<String,Object>();
			 for(HashMap<String,String> accMap : accList){
				 
				 String exItemId = accMap.get("EX_ITEM_ID") ;
				 
				 Account acc = new Account() ;
				 acc.setAccId(accMap.get("ACC_ID")); ;
				 acc.setAccCode(accMap.get("ACC_CODE")); 
				 acc.setIsBankAcc(accMap.get("IS_BANK_ACC"));
				 acc.setIsCashAcc(accMap.get("IS_CASH_ACC"));
				 
				 map.put(exItemId, acc) ;
			 }
		 }
		 
		 return map ;
	}

}
