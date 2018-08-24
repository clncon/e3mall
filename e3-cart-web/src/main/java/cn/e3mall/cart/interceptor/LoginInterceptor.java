package cn.e3mall.cart.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.e3mall.comm.util.CookieUtils;
import cn.e3mall.comm.util.E3Result;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;

/**
 * 用户登录处理拦截器
 * @author 孔旻
 *
 */
public class LoginInterceptor implements HandlerInterceptor {

	@Autowired
	private TokenService tokenService;
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {

	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse arg1, Object arg2) throws Exception {
		// TODO Auto-generated method stub
		//1.从cookie中取出token
		String token = CookieUtils.getCookieValue(request, "token");
	    //2.如果没有token,未登录状态，直接放行
		if(StringUtils.isBlank(token)){
			return true;
		}
		//3.取到token,需要调用sso系统的服务，根据token取用户信息
		E3Result result = tokenService.getUserByToken(token);
		//4.没有取到用户信息，登录过期了，直接放行
		if(result.getStatus()!=200){
			return true;
		}
		//5.取到用户信息。登录状态
		 TbUser user = (TbUser) result.getData();
		//6.把用户信息放到request域中，只需要在Controller中判断request中是否包含user信息，放行
		 request.setAttribute("user", user);
		return true;
	}

}
