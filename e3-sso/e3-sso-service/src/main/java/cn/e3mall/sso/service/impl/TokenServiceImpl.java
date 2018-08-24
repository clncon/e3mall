package cn.e3mall.sso.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.comm.jedis.JedisClient;
import cn.e3mall.comm.util.E3Result;
import cn.e3mall.comm.util.JsonUtils;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;

/**
 * 根据token取用户信息
 * @author 孔旻
 *
 */
@Service
public class TokenServiceImpl implements TokenService {

	@Autowired
	private JedisClient jedisClient;
	
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;
	
	@Override
	public E3Result getUserByToken(String token) {
		// TODO Auto-generated method stub
		
		//根据token到redis中取用户信息
		String json = jedisClient.get("SESSION:"+token);
		if(StringUtils.isBlank(json)){
			//娶不到用户信息，登录已经过期，返回登录过期
            return E3Result.build(201,"登录已经过期");			
		}
		
		//取到用户信息，更新token的过期时间
		 jedisClient.expire("SESSION:"+token,SESSION_EXPIRE);
		TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
		//返回结果：E3Result其中包含TbUser对象
		return E3Result.ok(user);
	}

}
