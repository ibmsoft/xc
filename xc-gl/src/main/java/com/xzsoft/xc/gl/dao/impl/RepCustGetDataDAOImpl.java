package com.xzsoft.xc.gl.dao.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.gl.dao.RepCustGetDataDAO;
import com.xzsoft.xc.gl.mapper.RepCustGetDataMapper;
import com.xzsoft.xip.platform.util.PlatformUtil;

/**
 * @ClassName: RepCustGetDataDAOImpl 
 * @Description: 云ERP报表模块自定义取数数据接口实现类
 * @author linp
 * @date 2016年7月22日 上午9:13:10 
 *
 */
@Repository("repCustGetDataDAO") 
public class RepCustGetDataDAOImpl implements RepCustGetDataDAO {

	@Resource
	private RepCustGetDataMapper repCustGetDataMapper ;
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getGLBalanceValue</p> 
	 * <p>Description: getGLBalanceValue </p> 
	 * @param map
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.RepCustGetDataDAO#getGLBalanceValue(java.util.Map)
	 */
	@Override
	public HashMap<String,Object> getGLBalanceValue(Map<String,Object> map) throws Exception {
		map.put("dbType", PlatformUtil.getDbType()) ;
		// 执行计算
		HashMap<String,Object> result = repCustGetDataMapper.getBalance(map) ;
		
		return result ;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: getGlCashSumValue</p> 
	 * <p>Description: 获取总账现金流量汇总余额 </p> 
	 * @param map
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.RepCustGetDataDAO#getGlCashSumValue(java.util.Map)
	 */
	@Override
	public HashMap<String,Object> getGlCashSumValue(Map<String,Object> map) throws Exception {
		// 返回值
		HashMap<String,Object> retMap = new HashMap<String,Object>() ;
		
		// 参数信息
		String caType = String.valueOf(map.get("CA_TYPE")) ;
		String periodCode = String.valueOf(map.get("periodCode")) ;
		String year = periodCode.substring(0, 4) ;
		
		// 查询数据
		if("CA_SUM_YTD".equals(caType) || "T_CA_SUM_YTD".equals(caType)){	// 本年数
			// 取现金流量本年数时，计算账期区间
			map.put("startPeriod", year.concat("-01")) ;
			retMap = repCustGetDataMapper.getCashYTD(map) ;
			
		}else if("CA_SUM_PTD".equals(caType) || "T_CA_SUM_PTD".equals(caType)){ // 本期数
			retMap = repCustGetDataMapper.getCashPTD(map) ;
			
		}else{
			retMap.put("VAL", 0) ;
		}
		
		return retMap ;
	}
}
