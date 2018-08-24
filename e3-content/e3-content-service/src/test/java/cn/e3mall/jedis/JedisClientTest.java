/*package cn.e3mall.jedis;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.e3mall.comm.jedis.JedisClient;

public class JedisClientTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//初始化spring容器
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
		
		//从容器中获取JedisClient对象
		JedisClient jedisClient = applicationContext.getBean(JedisClient.class);
		
		jedisClient.set("ll", "jedisClientCluster");
		
		String string = jedisClient.get("ll");
		System.out.println(string);
	}

}
*/