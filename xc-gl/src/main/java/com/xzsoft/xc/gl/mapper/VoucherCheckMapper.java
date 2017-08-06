package com.xzsoft.xc.gl.mapper;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.gl.modal.VHead;
import com.xzsoft.xc.gl.modal.VLine;

/**
 * @ClassName: VoucherCheckMapper 
 * @Description: 凭证检查
 * @author linp
 * @date 2016年5月31日 下午2:06:13 
 *
 */
public interface VoucherCheckMapper {
	
	/**
	 * @Title: isSameV 
	 * @Description: 判断是否为同一凭证
	 * @param sessionId
	 * @return    设定文件
	 */
	public Integer isSameV(String sessionId) ; 
	
	/**
	 * @Title: delTmpVoucher 
	 * @Description: 删除临时表中的凭证信息
	 * @param sessionId    设定文件
	 */
	public void delTmpVoucher(String sessionId) ;
	
	/**
	 * @Title: checkAccIsExists 
	 * @Description: 判断科目编码是否存在
	 * @param map    设定文件
	 */
	public void checkAccIsExists(HashMap<String,Object> map) ;

	/**
	 * @Title: addAccId 
	 * @Description: 补全科目ID
	 * @param map    设定文件
	 */
	public void addAccId(HashMap<String,Object> map) ;
	
	/**
	 * @Title: isExistsAndEnabledAcc 
	 * @Description: 判断会计科目在账簿下未启用或已失效
	 * @param map    设定文件
	 */
	public void isExistsAndEnabledAcc(HashMap<String,Object> map) ;
	
	/**
	 * @Title: getVoucherAccInfo 
	 * @Description: 查询凭证科目信息
	 * @param map
	 * @return    设定文件
	 */
	public List<HashMap<String,String>> getVoucherAccInfo(HashMap<String,Object> map) ;
	
	/**
	 * @Title: updTemplateType 
	 * @Description: 补全凭证格式 
	 * @param map    设定文件
	 */
	public void updTemplateType(HashMap<String,Object> map) ;
	
	/**
	 * @Title: getInvalidRecord 
	 * @Description: 判断是否存在不合法记录
	 * @param map
	 * @return    设定文件
	 */
	public Integer getInvalidRecord(HashMap<String,Object> map) ;
	
	/**
	 * @Title: getVHead 
	 * @Description: 获取凭证头信息
	 * @param sessionId
	 * @return    设定文件
	 */
	public VHead getVHead(String sessionId) ;
	
	/**
	 * @Title: getVLines 
	 * @Description: 获取凭证分录行信息
	 * @param sessionId
	 * @return    设定文件
	 */
	public List<VLine> getVLines(String sessionId) ;
	
	/**
	 * @Title: getExpVoucher 
	 * @Description: 查询凭证信息
	 * @param headId
	 * @return    设定文件
	 */
	public List<HashMap<String,Object>> getExpVoucher(String headId) ;
	
	/**
	 * @Title: getBgCHKVoucher 
	 * @Description: 查询凭证预算校验信息
	 * @param map
	 * @return    设定文件
	 */
	public List<HashMap<String,Object>> getBgCHKVoucher(HashMap<String,String> map) ;
	
}
