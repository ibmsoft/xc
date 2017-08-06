package com.xzsoft.xc.gl.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xzsoft.xc.gl.modal.LedgerSecDTO;

/**
 * @ClassName: LedgerMapper 
 * @Description: 账簿、账簿权限、账簿建账/取消建账 Mapper
 * @author linp
 * @date 2016年5月13日 下午2:55:53 
 *
 */
public interface LedgerMapper {

	/**
	 * @Title: getCreateFlag 
	 * @Description: 获取建账标志
	 * @param ledgerId
	 * @return    设定文件
	 */
	public HashMap<String,String> getCreateFlag(@Param(value="ledgerId")String ledgerId) ;
	
	/**
	 * @Title: updateCreateFlag 
	 * @Description: 更新建账标志位
	 * @param map    设定文件
	 */
	public void updateCreateFlag(HashMap<String,Object> map) ;
	
	/**
	 * @Title: getLedgerSec 
	 * @Description: 取得账簿授权信息 
	 * @param list
	 * @return    设定文件
	 */
	public List<LedgerSecDTO> getLedgerSec(List<String> list) ;
	
	/**
	 * @Title: saveLdSec 
	 * @Description: 账簿授权
	 * @param list    设定文件
	 */
	public void saveLdSec(HashMap<String,Object> map) ;
	
	/**
	 * @Title: delLdSec 
	 * @Description: 取消账簿授权信息
	 * @param list    设定文件
	 */
	public void delLdSec(HashMap<String,Object> map) ;
	
	/**
	 * @Title: batchDelSec 
	 * @Description: 批量删除授权信息
	 * @param map    设定文件
	 */
	public void batchDelSec(HashMap<String,Object> map) ;
	
}
