package com.xzsoft.xc.gl.util;

import com.alibaba.druid.filter.config.ConfigTools;

public class createPwd {

	public static void main(String[] args) {
		try {
			System.out.println(ConfigTools.encrypt("XC"));
			System.out.println(ConfigTools.encrypt("root"));
//			System.out.println(ConfigTools.encrypt("K3C9KorAfUezxD") );
			
//			System.out.println(ConfigTools.encrypt("root") );
//			String s = ConfigTools.decrypt("XgcZ5QEOwJN6/XmcaEUyA/1w5dGQfueoJ3oT4wdLPIu3Bl6kjMmx6eEf65FKOEKQxNuUZNtZAIIFhiwiWbt8SA==") ;
//			System.out.println(s);
//			// XgcZ5QEOwJN6/XmcaEUyA/1w5dGQfueoJ3oT4wdLPIu3Bl6kjMmx6eEf65FKOEKQxNuUZNtZAIIFhiwiWbt8SA==   /  Xzsoft2016`
//			
//			String s1 = ConfigTools.encrypt("K3C9KorAfUezxD") ;
//			System.out.println("s1="+s1);
			
			System.out.println(ConfigTools.decrypt("P5xguuKhApoj22p9ml9tWYUyHQYCh6gbhJABK6CQF58Jo+Jf7Ri2UCSPBJxF5HWC+B6wAaZlnxEl5hhncVfg/A=="));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
