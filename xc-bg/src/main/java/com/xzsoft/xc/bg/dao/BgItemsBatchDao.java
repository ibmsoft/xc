/**
 * 
 */
package com.xzsoft.xc.bg.dao;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.bg.modal.BgItemDTO;

/**
 * @author tangxl
 *
 */
public interface BgItemsBatchDao {
	/**
	 * 
	 * @methodName  getBgItemsByLedger
	 * @author      tangxl
	 * @date        2016年7月25日
	 * @describe    根据账簿和预算项目名称获取预算项目信息
	 * @param 		ledgerId
	 * @param 		bgItemName
	 * @return
	 * @throws 		Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public List<BgItemDTO> getBgItemsByLedger(String ledgerId,String bgItemName,String exportModel) throws Exception;
	/**
	 * 
	 * @methodName  updateImportItems
	 * @author      tangxl
	 * @date        2016年7月26日
	 * @describe    导入数据修改
	 * @param      	ledgerId
	 * @param 		itemList
	 * @param 		importType
	 * @return
	 * @throws 		Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public String updateImportItems(String ledgerId,List<HashMap<String, String>> itemList,String importType) throws Exception;
	/**
	 * 
	 * @methodName  deleteTmpImport
	 * @author      tangxl
	 * @date        2016年7月28日
	 * @describe    TODO
	 * @param 		ledgerId
	 * @param 		sessionId
	 * @param 		importType
	 * @throws 		Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public void deleteTmpImport(String ledgerId,String sessionId,String importType) throws Exception;

}
