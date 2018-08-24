package cn.e3mall.jedis;

import java.util.HashSet;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class JedisTest {
	
	
	@Test
	public void testJedis(){
		
		//创建一个连接Jedis对象，参数：host,port
		Jedis jedis = new Jedis("118.25.188.72",10000);
		//直接使用jedis操作redis,所有jedis命令都对应一个方法。
		jedis.set("test123","my first jedis test");
		String str = jedis.get("test123");
		System.out.println(str);
		
		jedis.close();
	}
	
	@Test
	public void testJedisPool(){
		//创建一个连接池对象，两个参数host,port
		JedisPool jedisPool = new JedisPool("118.25.188.72",6379);
		//从连接池获取一个连接，就是一个jedis对象
		Jedis jedis = jedisPool.getResource();
		//使用jedis操作redis
		String string = jedis.get("test123");
		System.out.println(string);
		//关闭连接，每次使用完毕后关闭连接。连接池回收资源
		jedis.close();
		//关闭连接池
		jedisPool.close();
	}
	
	@Test
	public void testJedisCluster(){
	
		 //创建一个JedisCluster对象。
		//有一个参数nodes是一个set类型。set中包含若干个HostAndPort对象。
		HashSet<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("118.25.188.72", 7001));
		nodes.add(new HostAndPort("118.25.188.72", 7002));
		nodes.add(new HostAndPort("118.25.188.72", 7003));
		nodes.add(new HostAndPort("118.25.188.72", 7004));
		nodes.add(new HostAndPort("118.25.188.72", 7005));
		nodes.add(new HostAndPort("118.25.188.72", 7006));
		
		JedisCluster jedisCluster = new JedisCluster(nodes);
		//直接使用JedisCluster对象操作redis
		 jedisCluster.set("test","123");
		 String string = jedisCluster.get("test");
		 System.out.println(string);
		 //关闭JedisCluster对象
		 jedisCluster.close();
		
	}

}
