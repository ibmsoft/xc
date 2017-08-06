package com.xzsoft.xc.gl.service;

import javax.annotation.Resource;

import org.codehaus.jettison.json.JSONArray;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/spring/spring-*.xml"})
public class XCGLSumBalanceServiceTest {
	@Resource
	private XCGLSumBalanceService xcglSumBalanceService ;

	@Test
	public void testSumBalancesService(){
		try {
//			String json = "[{'V_HEAD_ID':'b4c1d3a7-0555-4b19-b2d4-b7d7742acbfe'}]" ;
//			JSONArray ja  = new JSONArray(json) ;
//			
//			xcglSumBalanceService.sumGLBalances(ja, "N", "ALL", null, "N") ;
			
//			HashMap<String,String> map = new HashMap<String,String>() ;
//			map.put("ledgerId", "654d90fa-0a76-4c2b-89e1-2365c067a536") ;
//			map.put("userId", "f92c688a-df48-d4e2-e040-a8c021824389") ;
//			map.put("isCanceled", "N") ;
			
			String jastr = "[{\"V_HEAD_ID\":\"3d95fd37-0abf-40c5-bc41-b2008b658a56\"}]" ;
			JSONArray ja = new JSONArray(jastr) ;
			
			xcglSumBalanceService.sumGLBalances(ja,"N","ALL","f92c688a-df48-d4e2-e040-a8c021824389","N") ;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
