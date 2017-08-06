package com.xzsoft.xsr.core.util;



import com.alibaba.druid.filter.config.ConfigTools;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath*:/spring/spring-*.xml"})
public class PasswordUtil {

	public static void main(String[] args) {
		try {
			System.out.println(ConfigTools.encrypt("123456") );
			//Xzsoft2016`    XgcZ5QEOwJN6/XmcaEUyA/1w5dGQfueoJ3oT4wdLPIu3Bl6kjMmx6eEf65FKOEKQxNuUZNtZAIIFhiwiWbt8SA==
			//System.out.println(ConfigTools.decrypt("XgcZ5QEOwJN6/XmcaEUyA/1w5dGQfueoJ3oT4wdLPIu3Bl6kjMmx6eEf65FKOEKQxNuUZNtZAIIFhiwiWbt8SA=="));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
