package com.xzsoft.xc.gl.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xzsoft.xc.gl.modal.Balances;
import com.xzsoft.xc.gl.modal.GlJzTpl;
import com.xzsoft.xc.gl.modal.GlJzTplDtl;
import com.xzsoft.xc.gl.modal.Period;


/**
 * @ClassName: LedgerPeriodManageMapper 
 * @Description: 账簿追加期间管理接口
 * @author tangxl
 * @date 2015年12月29日 下午6:15:39 
 *
 */
public interface LedgerPeriodManageMapper {
	public List<Period> getLedgerNextPeriod(@Param(value="ledgerId")String ledgerId);
	/**
	 * @title:  计算追加账簿开始期间的余额值
	 * @desc:   TODO
	 * @author  tangxl
	 * @date    2016年3月20日
	 * @param ledgerId
	 */
	public void appendPeriodByStartPeriod(HashMap<String, String> map);
	/**
	 * @title:  appendPeriodToLedger
	 * @desc:   追加期间到账簿
	 * @author  tangxl
	 * @date    2016年3月20日
	 * @param map
	 */
	public void appendPeriodToLedger(HashMap<String, String> map);
	/**
	 * 
	 * @title:  getPeriodByCode
	 * @desc:   TODO
	 * @author  tangxl
	 * @date    2016年3月20日
	 * @param   periodCode
	 * @return
	 */
	public Period getPeriodByCode(@Param(value="periodCode")String periodCode);
	/**
	 * @title:  getLedgerBalances
	 * @desc:   TODO
	 * @author  tangxl
	 * @date    2016年3月20日
	 * @param   map
	 * @return
	 */
	public List<Balances> getLedgerBalances(HashMap<String, String> map);
	/**
	 * @title:  insertLedgerBalances
	 * @desc:   TODO
	 * @author  tangxl
	 * @date    2016年3月20日
	 * @param balancesList
	 */
	public void insertLedgerBalances(List<Balances> balancesList);
	/**
	 * @title:  getLedgerMaxPeriodCode
	 * @desc:   TODO
	 * @author  tangxl
	 * @date    2016年3月21日
	 * @param map
	 * @return
	 */
	public String getLedgerMaxPeriodCode(HashMap<String, String> map);
	/**
	 * 
	 * @methodName  getGlJzTplById
	 * @author      tangxl
	 * @date        2016年7月19日
	 * @describe    TODO
	 * @param       jzTplId
	 * @return
	 * @变动说明       
	 * @version     1.0
	 */
	public GlJzTpl getGlJzTplById(@Param(value="jzTplId")String jzTplId);
	/**
	 * 
	 * @methodName  getJzDtlByTplId
	 * @author      tangxl
	 * @date        2016年7月19日
	 * @describe    TODO
	 * @param       jzTplId
	 * @return
	 * @变动说明       
	 * @version     1.0
	 */
	public List<GlJzTplDtl> getJzDtlByTplId(@Param(value="jzTplId")String jzTplId);
	/**
	 * 
	 * @methodName  queryConvertAmount
	 * @author      tangxl
	 * @date        2016年7月19日
	 * @describe    取数科目余额信息查询<转入科目包含辅助段>
	 * @param       jzTplId
	 * @return
	 * @变动说明       
	 * @version     1.0
	 */
	public List<HashMap<String, Object>> queryConvertAmount(@Param(value="accId")String accId,@Param(value="periodCode")String periodCode,@Param(value="ledgerId")String ledgerId);
	/**
	 * 
	 * @methodName  querySumConvertAmount
	 * @author      tangxl
	 * @date        2016年7月19日
	 * @describe    取数科目余额信息汇总查询<转入科目不包含辅助段>
	 * @param       jzTplId
	 * @return
	 * @变动说明       
	 * @version     1.0
	 */
	public HashMap<String, Object> querySumConvertAmount(@Param(value="accId")String accId,@Param(value="periodCode")String periodCode,@Param(value="ledgerId")String ledgerId);
	/**
	 * 
	 * @methodName  getAccSegmentCode
	 * @author      tangxl
	 * @date        2016年7月19日
	 * @describe    获取账簿科目的启用辅助段
	 * @param       jzTplId
	 * @return
	 * @变动说明       
	 * @version     1.0
	 */
	public List<String> getAccSegmentCode(@Param(value="jzTplId")String jzTplId);
	/**
	 * 
	 * @methodName  insertCarryoverLog
	 * @author      tangxl
	 * @date        2016年7月20日
	 * @describe    记录模板结转日志
	 * @throws      Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public void insertCarryoverLog(HashMap<String, String> logMap);
	
}
