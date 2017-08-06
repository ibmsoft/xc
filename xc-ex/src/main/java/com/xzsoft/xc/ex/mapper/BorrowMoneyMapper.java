package com.xzsoft.xc.ex.mapper;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.ex.modal.IAFactBean;

/**
 * 
 * @ClassName: BorrowMoneyMapper 
 * @Description: 备用金明细处理Mapper
 * @author linp
 * @date 2016年3月10日 上午9:55:48 
 *
 */
public interface BorrowMoneyMapper {
	
	/**
	 * 
	 * @title:  queryUserImprest
	 * @description:  查询个人备用金
	 * @author  tangxl
	 * @date    2016年4月6日
	 * @param map
	 * @return
	 */
	public IAFactBean queryUserImprest(HashMap<String, String> map);
	/**
	 * @title:  addUserImprestMySQL
	 * @description:  增加个人备用金
	 * @author  tangxl
	 * @date    2016年4月6日
	 * @param map
	 * @return
	 */
	public void addUserImprestMySQL(IAFactBean iaFactBean);
	
	/**
	 * @Title: addUserImprestOracle 
	 * @Description: 增加个人备用金
	 * @param iaFactBean    设定文件
	 */
	public void addUserImprestOracle(IAFactBean iaFactBean);
	
	/**
	 * 
	 * @title:  getImprestByUpdateDate
	 * @description:   查询当前备用金明细记录以后的备用金记录<不包含当前备用金>
	 * @author  tangxl
	 * @date    2016年4月6日
	 * @param   iaFactBean
	 * @return
	 */
	public List<IAFactBean> getImprestByUpdateDate(IAFactBean iaFactBean);
	/**
	 * 
	 * @title:  getImprestByDocId
	 * @description:  根据单据id查询备用金信息
	 * @author  tangxl
	 * @date    2016年4月6日
	 * @param   iaFactBean
	 * @return
	 */
	public List<IAFactBean> getImprestByDocId(IAFactBean iaFactBean);
	/**
	 * 
	 * @title:  deleteImprest
	 * @description:   删除个人备用金记录
	 * @author  tangxl
	 * @date    2016年4月7日
	 * @param iaFactBean
	 */
	public void deleteImprest(IAFactBean iaFactBean);
	/**
	 * 
	 * @title:  batchUpdateImprestMysql
	 * @description:   修改备用金余额
	 * @author  tangxl
	 * @date    2016年4月7日
	 * @param beanList
	 */
	public void batchUpdateImprestMysql(List<IAFactBean> beanList);	
	/**
	 * 
	 * @title:  batchUpdateImprestOracle
	 * @description:   修改备用金余额
	 * @author  tangxl
	 * @date    2016年4月7日
	 * @param beanList
	 */
	public void batchUpdateImprestOracle(List<IAFactBean> beanList);

}
