package cn.e3mall.sso.service;

import cn.e3mall.comm.util.E3Result;

/**
 * 根据token查询用户信息
 * @author 孔旻
 *
 */
public interface TokenService {

	E3Result getUserByToken(String token);
}
