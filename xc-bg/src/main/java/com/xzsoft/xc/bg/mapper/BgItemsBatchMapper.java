/**
 * 
 */
package com.xzsoft.xc.bg.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xzsoft.xc.bg.modal.BgItemDTO;

/**
 * @author tangxl
 *
 */
public interface BgItemsBatchMapper {
	/**
	 * 
	 * @methodName  getBgItemsByLedger
	 * @author      tangxl
	 * @date        2016年7月25日
	 * @describe    根据ledgerId和bgItemName获取预算项目信息
	 * @param       ledgerId
	 * @param       bgItemName
	 * @return
	 * @变动说明       
	 * @version     1.0
	 */
	public List<BgItemDTO> getBgItemsByLedger(@Param(value="ledgerId")String ledgerId, @Param(value="bgItemName")String bgItemName);
	/**
	 * 
	 * @methodName  getExItemsByLedger
	 * @author      tangxl
	 * @date        2016年7月25日
	 * @describe    根据ledgerId和bgItemName获取费用项目信息
	 * @param       ledgerId
	 * @param       bgItemName
	 * @return
	 * @变动说明       
	 * @version     1.0
	 */
	public List<BgItemDTO> getExItemsByLedger(@Param(value="ledgerId")String ledgerId, @Param(value="bgItemName")String bgItemName);
	/**
	 * 
	 * @methodName  updateBgImportItems
	 * @author      tangxl
	 * @date        2016年7月26日
	 * @describe    导入数据修改
	 * @param 		itemList
	 * @return
	 * @throws 		Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public void insertBgImportItems(HashMap<String, Object> map);
	/**
	 * 
	 * @methodName  updateExImportItems
	 * @author      tangxl
	 * @date        2016年7月26日
	 * @describe    导入数据修改
	 * @param 		itemList
	 * @return
	 * @throws 		Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public void insertExImportItems(HashMap<String, Object> map);
	/**
	 * 
	 * @methodName  方法名
	 * @author      tangxl
	 * @date        2016年7月26日
	 * @describe    TODO
	 * @param ledgerId
	 * @param sessionId
	 * @变动说明       
	 * @version     1.0
	 */
	public void checkBgItemExist(@Param(value="ledgerId")String ledgerId,@Param(value="sessionId")String sessionId);
	/**
	 * 
	 * @methodName  modifyBudgetItems
	 * @author      tangxl
	 * @date        2016年7月26日
	 * @describe    校验导入数据的合法性
	 * @param 		ledgerId
	 * @param 		sessionId
	 * @变动说明       
	 * @version     1.0
	 */
	public void modifyBudgetItems(@Param(value="ledgerId")String ledgerId,@Param(value="sessionId")String sessionId);
	/**
	 * 
	 * @methodName  getInValidBgItem
	 * @author      tangxl
	 * @date        2016年7月26日
	 * @describe    校验导入数据的合法性
	 * @param 		ledgerId
	 * @param 		sessionId
	 * @变动说明       
	 * @version     1.0
	 */
	public int getInValidBgItem(@Param(value="ledgerId")String ledgerId,@Param(value="sessionId")String sessionId,@Param(value="billType")String billType);
	/**
	 * 
	 * @methodName  checkExImportValid
	 * @author      tangxl
	 * @date        2016年7月26日
	 * @describe    校验导入的费用项目是否合法
	 * @param 		ledgerId
	 * @param 		sessionId
	 * @变动说明       
	 * @version     1.0
	 */
	public void checkExImportValid(@Param(value="ledgerId")String ledgerId,@Param(value="sessionId")String sessionId);
	/**
	 * 
	 * @methodName  checkAccImportValid
	 * @author      tangxl
	 * @date        2016年7月26日
	 * @describe    校验导入的会计是否合法
	 * @param 		ledgerId
	 * @param 		sessionId
	 * @变动说明       
	 * @version     1.0
	 */
	public void checkAccImportValid(@Param(value="ledgerId")String ledgerId,@Param(value="sessionId")String sessionId);
	/**
	 * 
	 * @methodName  checkBgImportValid
	 * @author      tangxl
	 * @date        2016年7月26日
	 * @describe    校验导入的预算项目是否合法
	 * @param 		ledgerId
	 * @param 		sessionId
	 * @变动说明       
	 * @version     1.0
	 */
	public void checkBgImportValid(@Param(value="ledgerId")String ledgerId,@Param(value="sessionId")String sessionId);
	/**
	 * 
	 * @methodName  modifyFeeItems
	 * @author      tangxl
	 * @date        2016年7月26日
	 * @describe    修改导入的费用项目
	 * @param 		ledgerId
	 * @param 		sessionId
	 * @变动说明       
	 * @version     1.0
	 */
	public void modifyFeeItems(@Param(value="ledgerId")String ledgerId,@Param(value="sessionId")String sessionId);
	
	public void deleteTmpData(@Param(value="delSql")String delSql);
	/**
	 * 
	 * @methodName  updateNoValidData
	 * @author      tangxl
	 * @date        2016年7月28日
	 * @describe    忽略不需要校验的数据
	 * @param       ledgerId
	 * @param       sessionId
	 * @变动说明       
	 * @version     1.0
	 */
	public void updateNoValidData(@Param(value="ledgerId")String ledgerId,@Param(value="sessionId")String sessionId);
}
