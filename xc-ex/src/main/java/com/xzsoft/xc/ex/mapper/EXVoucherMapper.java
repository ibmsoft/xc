package com.xzsoft.xc.ex.mapper;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.ex.modal.PayBankModeBean;
import com.xzsoft.xc.ex.modal.PayModeBean;

/**
 * @ClassName: EXVoucherMapper 
 * @Description: 单据复核、单据支付的凭证处理Mapper
 * @author linp
 * @date 2016年3月11日 下午4:03:54 
 *
 */
public interface EXVoucherMapper {
	
	/**
	 * @Title: getPayModeBean 
	 * @Description: 查询账簿收付款方式
	 * @param map
	 * @return    设定文件
	 */
	public PayModeBean getPayModeBean(HashMap<String,String> map) ;
	
	/**
	 * @Title: getPayModeBean 
	 * @Description: 查询账簿开户行信息
	 * @param depositBankId
	 * @return    设定文件
	 */
	public PayBankModeBean getPayBank(String depositBankId) ;
	
	/**
	 * @Title: getExItemAccs 
	 * @Description: 查询费用项目对应的科目信息
	 * @param paramMap
	 * @return    设定文件
	 */
	public List<HashMap<String,String>> getExItemAccs(HashMap<String,Object> paramMap) ;

}
