package cn.e3mall.test;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.dubbo.config.ApplicationConfig;

import cn.e3mall.common.jedis.JedisClient;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class TestJedis {
	
	@Test
	public void testJedis(){
		
		Jedis jedis=new Jedis("192.168.25.144", 6379);
		String result=jedis.hget("key3", "map2");
		System.out.println(result);
		jedis.close();
	}
	@Test
	public void testJedisPool(){
		
		JedisPool jedis=new JedisPool("192.168.25.144",6379);
		Jedis je = jedis.getResource();
		String string = je.get("key1");
		System.out.println(string);
		je.close();
		jedis.close();
		
	}
	@Test
	public void testCluster(){
		
		Set<HostAndPort> nodes=new HashSet<HostAndPort>();
		nodes.add(new HostAndPort("192.168.25.144", 7001));
		nodes.add(new HostAndPort("192.168.25.144", 7002));
		nodes.add(new HostAndPort("192.168.25.144", 7003));
		nodes.add(new HostAndPort("192.168.25.144", 7004));
		nodes.add(new HostAndPort("192.168.25.144", 7005));
		nodes.add(new HostAndPort("192.168.25.144", 7006));
		JedisCluster jedis=new JedisCluster(nodes);
		String string = jedis.get("a");
		System.out.println(string);
		jedis.close();
	}
	@Test
	public void testJed(){
		
		ApplicationContext application =new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
		JedisClient jedis=application.getBean(JedisClient.class);
		jedis.set("hello","999");
		String string = jedis.get("hello");
		System.out.println(string);
		
		
		
	}

}
