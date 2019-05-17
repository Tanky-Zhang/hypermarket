package cn.e3mall.activemq;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class testMq {
	@Test
	public void testMq() throws Exception{
		ApplicationContext applicationcontext=new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
		System.in.read();		
	}

}
