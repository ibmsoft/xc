package com.xzsoft.xsr.core.service.impl.repCheck;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.xzsoft.xsr.core.modal.CheckFormula;

/**
 * 解析稽核公式
 * redis查询key格式：
 * 固定行：表套&期间&公司&币种&行指标&列指标
 * 浮动行：表套&期间&公司&币种&行指标&列指标&模板id&浮动行号
 * params: 公司id,期间id,币种id,是否稽核浮动行,公司code,期间code,币种code,表套id,模板id
 */
public abstract class AbstractAnalyzeCheckFormula {
	private static Logger log = Logger.getLogger(AbstractAnalyzeCheckFormula.class.getName());
	
	/**
	 * 解析公式
	 * @param list
	 * @param params
	 * @return 
	 */
    public abstract List<CheckFormula> analyzeFormula(List<CheckFormula> list, String[] params);

    
    /**
	 * 根据特殊期间名称，返回相应期间code
	 * 
	 * 当前期间编码长度是4，
	 * 则 BNQC(本年年初数),SNTQ(上年同期数),SNQM(上年期末数),SNSY(上年同期数-上月),SNNQM(上年期末(年期间))等于
	 * PERIOD_CODE-1; 例如2014-1=2013
	 * 则SYBN(上月同期数-年内),SYTQ(上月同期数)等于
	 * PERIOD_CODE||'-11'; 例如2014'-11'=2014-11
	 * 
	 * 当前期间编码长度是7，
	 * 则SNNQM(上年期末(年期间))等于
	 * 截取(P_PERIOD_CODE, 1, 4)-1; 例如：2014-07=2014-1=2013
	 * 则SYTQ(上月同期数)等于
	 * 例如2014-07=2014-06
	 * 
	 * @param cf
	 * @param params
	 * @return 
	 * 未处理期间类型：
	 * SSYB(上上月周期)
	 * xsr_rpt_pkg.F_GETPERIOD中，非月期间未处理
	 */
    public String getPeriodCode(String periodDesc, String nowPeriodCode) {
    	int npLength = nowPeriodCode.length();
    	String periodCode = periodDesc;
    	SimpleDateFormat msf = new SimpleDateFormat("yyyy-MM");
    	SimpleDateFormat ysf = new SimpleDateFormat("yyyy");
    	GregorianCalendar gc=new GregorianCalendar(); 
    	
  		try {
  			switch(periodDesc) {
  				case "BNQC" : //本年年初数
					if(npLength==4) {
						Date date = ysf.parse(nowPeriodCode);
						gc.setTime(date); 
						gc.add(1, -1);
						periodCode = ysf.format(gc.getTime());
					} else if(npLength==7) {
						periodCode = nowPeriodCode.substring(0, 4) + "-01";
					} else if(npLength == 8) {
	    				StringBuilder pdc = new StringBuilder();
	    				String year = nowPeriodCode.substring(0, 4);
						periodCode = pdc.append(year).toString();
	    			}
					break;
					
  				case "SNTQ" : //上年同期数
	    			if(npLength==4) {
	    				Date date = ysf.parse(nowPeriodCode);
						gc.setTime(date); 
						gc.add(1, -1);
						periodCode = ysf.format(gc.getTime());
	    			} else if(npLength==7) {
	    				Date date = msf.parse(nowPeriodCode);
						gc.setTime(date); 
						gc.add(1, -1);
						periodCode = msf.format(gc.getTime());
	    			}
	    			break;
	    			
	    		case "SNQM" : //上年期末数
	    			if(npLength==4) {
	    				Date date = ysf.parse(nowPeriodCode);
						gc.setTime(date); 
						gc.add(1, -1);
						periodCode = ysf.format(gc.getTime());
	    			} else if(npLength==7) {
	    				Date date = ysf.parse(nowPeriodCode);
						gc.setTime(date); 
						gc.add(1, -1);
						periodCode = ysf.format(gc.getTime()) + "-12";
	    			}
	    			break;
	    			
			  	case "SNSY" : //上年同期数-上月
			  		if(npLength==4) {
			  			Date date = ysf.parse(nowPeriodCode);
						gc.setTime(date); 
						gc.add(1, -1);
						periodCode = ysf.format(gc.getTime());
			  		} else if(npLength==7) {
			  			Date date = msf.parse(nowPeriodCode);
						gc.setTime(date); 
						gc.add(1, -1);
						gc.add(2, -1);
						periodCode = msf.format(gc.getTime());
			  		}
			  		break;
			  		
	    		case "SNNQM" : //上年期末(年期间)
	    			if(npLength==4) {
	    				Date date = ysf.parse(nowPeriodCode);
						gc.setTime(date); 
						gc.add(1, -1);
						periodCode = ysf.format(gc.getTime());
	    			} else if(npLength==7) {
	    				Date date = ysf.parse(nowPeriodCode);
						gc.setTime(date); 
						gc.add(1, -1);
						periodCode = ysf.format(gc.getTime());
	    			}
	    			break;
	    			
	    		case "SYBN" : //上月同期数-年内
	    			if(npLength==4) {
	    				periodCode = nowPeriodCode+"-11";
	    			} else if(npLength==7) {
	    				if(nowPeriodCode.contains("-01")) {
	    					periodCode = "2099-01"; //取一月份对应sybn期间时,返回一不存在的期间,从而使公式采集数据为零.
	    				} else {
	    					Date date = msf.parse(nowPeriodCode);
							gc.setTime(date); 
							gc.add(2, -1);
							periodCode = msf.format(gc.getTime());
	    				}
	    			}
	    			break;	
	    			
	    		case "SYTQ" : //上月同期数
	    			if(npLength==4) {
	    				periodCode = nowPeriodCode+"-11";
	    			} else if(npLength==7) {
	    				Date date = msf.parse(nowPeriodCode);
						gc.setTime(date); 
						gc.add(2, -1);
						periodCode = msf.format(gc.getTime());
	    			}
				  	break;
				  	
	    		case "SZTQ" : //周期间： 上周同期
	    			if(npLength == 8) {
	    				StringBuilder pdc = new StringBuilder();
	    				String year = nowPeriodCode.substring(0, 4);
	    				pdc.append(year);
	    				String week = nowPeriodCode.substring(6);
	    				int nowWeek = Integer.parseInt(week) - 1;
						if(nowWeek <= 9) {
							periodCode = pdc.append("-W0").append(nowWeek).toString();
						} else {
							periodCode = pdc.append("-W").append(nowWeek).toString();
						}
	    			}
				  	break;
				  	
	    		case "SNTZ" : //周期间： 上年同周
	    			if(npLength == 8) {
	    				StringBuilder pdc = new StringBuilder();
	    				String year = nowPeriodCode.substring(0, 4);
	    				String week = nowPeriodCode.substring(6);
	    				int nowYear = Integer.parseInt(year) - 1;
						periodCode = pdc.append(nowYear).append("-W").append(week).toString();
	    			}
				  	break;

	    	}
  		} catch(Exception e) {
  			log.error("根据特殊期间名称，返回相应期间code，出错", e);
  			throw new RuntimeException(e.getMessage() + " 根据特殊期间名称，返回相应期间code，出错");
  		}
    	return periodCode;
    }
    
}
