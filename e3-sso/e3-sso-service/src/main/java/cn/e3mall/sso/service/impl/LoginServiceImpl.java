package cn.e3mall.sso.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;


import cn.e3mall.comm.jedis.JedisClient;
import cn.e3mall.comm.util.E3Result;
import cn.e3mall.comm.util.JsonUtils;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.pojo.TbUserExample.Criteria;
import cn.e3mall.sso.service.LoginService;
@Service
public class LoginServiceImpl implements LoginService {
	@Autowired
	private TbUserMapper tbUserMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;
	@Override
	public E3Result userLogin(String username, String password) {
		// TODO Auto-generated method stub
		
		//* 1.判断用户和密码是否正确
		 //根据用户名查询用户信息
		  TbUserExample example = new TbUserExample();
		   Criteria criteria = example.createCriteria();
		   criteria.andUsernameEqualTo(username);
		   //执行查询
		   List<TbUser> list = tbUserMapper.selectByExample(example);
		   if(list==null||list.size()==0){
			   //返回登录失败
			   return E3Result.build(400,"用户名或者密码错误");
		   }
		   //取得用户信息
		   TbUser user = list.get(0);
		   //判断密码是否正确
		   if(DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())){
			   
			   //* 2.如果不正确，返回登录失败
			   return E3Result.build(400, "用户名或者密码错误");
		   }
		 //* 3.如果正确生成token
		   String token = UUID.randomUUID().toString();
		 //* 4.把用户信息写入redis,key:token value:用户信息
		   user.setPassword(null);
		   jedisClient.set("SESSION:"+token, JsonUtils.objectToJson(user));
		 //* 5.设置session的过期时间
		   jedisClient.expire("SESSION:"+token,SESSION_EXPIRE);
		 //* 6.把token返回
		return new E3Result().ok(token);
	}

}
