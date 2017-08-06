package com.xzsoft.xc.gl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;

public class JunitTest {

	@Test
	public void Test1(){
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar =new GregorianCalendar();
//		String period_code=Integer.toString(2015)+"-"+"0"+Integer.toString(1);
//		System.out.println(period_code);
		
		Date start_date;
		try {
			start_date = sim.parse("1900-02-10");
			calendar.setTime(start_date);
			int num2 = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			System.out.println(num2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
}
