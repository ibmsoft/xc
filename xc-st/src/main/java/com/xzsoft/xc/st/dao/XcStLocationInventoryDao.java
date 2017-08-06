package com.xzsoft.xc.st.dao;
import com.xzsoft.xc.st.modal.Dto;
/**
 * 
  * @ClassName XcStLocationInventoryDao
  * @Description 货位台账处理记录
  * @author RenJianJian
  * @date 2017年3月24日 上午11:11:29
 */
public interface XcStLocationInventoryDao {
	/**
	 * 
	  * @Title getLocationInventoryByKey 方法名
	  * @Description 根据主键查询对应的货位台账处理记录
	  * @param pDto
	  * @return
	  * @throws Exception
	  * @return Dto 返回类型
	 */
	public Dto getLocationInventoryByKey(Dto pDto) throws Exception;
	/**
	 * 
	  * @Title insertLocationInventory 方法名
	  * @Description 保存货位台账处理记录
	  * @param pDto
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void insertLocationInventory(Dto pDto) throws Exception;
	/**
	 * 
	  * @Title updateLocationInventory 方法名
	  * @Description 修改货位台账处理记录
	  * @param pDto
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void updateLocationInventory(Dto pDto) throws Exception;
	/**
	 * 
	  * @Title deleteLocationInventory 方法名
	  * @Description 根据库存存事务处理记录主键删除记录
	  * @param pDto
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void deleteLocationInventory(Dto pDto) throws Exception;
	/**
	 * 
	  * @Title getLocationInventoryByWMSID 方法名
	  * @Description 根据仓库ID，物料ID、批次ID、货位ID查询货位汇总表是否存在
	  * @param pDto
	  * @return
	  * @throws Exception
	  * @return Dto 返回类型
	 */
	public Dto getLocationInventoryByWMSID(Dto pDto) throws Exception;
}
