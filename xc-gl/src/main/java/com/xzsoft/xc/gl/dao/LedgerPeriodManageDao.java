package com.xzsoft.xc.gl.dao;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.gl.modal.Balances;
import com.xzsoft.xc.gl.modal.GlJzTpl;
import com.xzsoft.xc.gl.modal.Period;


/**
 * 
* @Title: LedgerPeriodManageDao.java
* @Description:账簿追加会计期间取数接口
* @author tangxl  
* @date 2016年1月20日 上午9:43:49
* @version V1.0
 */
public interface LedgerPeriodManageDao {
	/**
	 * @title:  getLedgerNextPeriod
	 * @desc:   <判断下一期间是否已启用>
	 * @author  tangxl
	 * @date    2016年3月20日
	 * @param   ledgerId
	 * @param   currentPeriod
	 * @return
	 * @throws  Exception
	 */
	public List<Period> getLedgerNextPeriod(String ledgerId,String currentPeriod) throws Exception;
	/**
	 * @title:  appendPeriodByStartPeriod
	 * @desc:   (追加账簿的起始期间)
	 * @author  tangxl
	 * @date    2016年3月20日
	 * @param   ledgerId
	 * @throws  Exception
	 */
	public void appendPeriodByStartPeriod(String ledgerId,String periodCode) throws Exception;
	/**
	 * @title:  checkNextPeriodExists
	 * @desc:   检验期间是否已经开启
	 * @author  tangxl
	 * @date    2016年3月20日
	 * @param   periodCode
	 * @return
	 * @throws Exception
	 */
	public Period checkNextPeriodExists(String periodCode) throws Exception;
	/**
	 * @title:  getLedgerBalances
	 * @desc:   获取账簿某一期间下的余额记录
	 * @author  tangxl
	 * @date    2016年3月20日
	 * @param   map
	 * @return
	 * @throws  Exception
	 */
	public List<Balances> getLedgerBalances(HashMap<String, String> map) throws Exception;
	/**
	 * @title:  insertPeriodToLedger
	 * @desc:   插入期间到账簿表
	 * @author  tangxl
	 * @date    2016年3月20日
	 * @param   map
	 * @throws  Exception
	 */
	public void insertPeriodToLedger(HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * @title:  appendLedgerPeriod
	 * @desc:   追加账簿期间<保存追加的账簿信息以及账簿的余额信息>
	 * @author  tangxl
	 * @date    2016年3月20日
	 * @param balancesList
	 * @throws Exception
	 */
	public void appendLedgerPeriod(List<Balances> balancesList,HashMap<String, String> map) throws Exception;
	/**
	 * @title:  getLedgerMaxPeriodCode
	 * @desc:   查询当前账簿已追加的最大会计期
	 * @author  tangxl
	 * @date    2016年3月21日
	 * @param   ledgerId
	 * @return
	 * @throws Exception
	 */
	public String getLedgerMaxPeriodCode(String ledgerId) throws Exception;
	/**
	 * 
	 * @methodName  getGlJzTplById
	 * @author      tangxl
	 * @date        2016年7月19日
	 * @describe    获取模板数据
	 * @param       jzTplId
	 * @param       periodCode
	 * @return
	 * @throws      Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public GlJzTpl getGlJzTplById(String jzTplId,String periodCode) throws Exception;
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
	public void insertCarryoverLog(HashMap<String, String> logMap) throws Exception;
	
}
