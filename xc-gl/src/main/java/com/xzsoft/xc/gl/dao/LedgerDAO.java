package com.xzsoft.xc.gl.dao;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.gl.modal.LedgerSecDTO;

/**
 * @ClassName: LedgerDAO 
 * @Description: 账簿、账簿权限、账簿建账/取消建账 数据层接口
 * @author linp
 * @date 2016年5月13日 下午3:03:39 
 *
 */
public interface LedgerDAO {

	/**
	 * @Title: getCreateFlag 
	 * @Description: 获取建账标志
	 * @param ledgerId
	 * @return
	 * @throws Exception    设定文件
	 */
	public HashMap<String,String> getCreateFlag(String ledgerId)throws Exception ;
	
	/**
	 * @Title: updateCreateFlag 
	 * @Description: 更新建账标志位
	 * @param ledgerId
	 * @param flag
	 * @throws Exception    设定文件
	 */
	public void updateCreateFlag(String ledgerId,String flag,String createType)throws Exception ;
	
	/**
	 * @Title: getLedgerSec 
	 * @Description: 取得账簿授权信息
	 * @param list
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<String> getLedgerSec(List<String> list) throws Exception ;
	
	/**
	 * @Title: addLedgerSec 
	 * @Description: 账簿授权
	 * @param list
	 * @throws Exception    设定文件
	 */
	public void addLedgerSec(List<LedgerSecDTO> list) throws Exception ;
	
	/**
	 * @Title: delLedgerSec 
	 * @Description: 取消账簿授权信息
	 * @param list
	 * @throws Exception    设定文件
	 */
	public void delLedgerSec(List<LedgerSecDTO> list) throws Exception ;
	
	/**
	 * @Title: batchDelSec 
	 * @Description: 批量删除授权信息
	 * @param list
	 * @throws Exception    设定文件
	 */
	public void batchDelSec(List<String> list) throws Exception ;

}
