package com.xzsoft.xc.gl.util;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class DateUtilTest {

	public static void main(String[] args) {
		String val = "2015-10-01" ;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;
		System.out.println(sdf.format(Date.valueOf(val)));
	}

}
