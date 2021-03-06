package cn.e3mall.search.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;


/**
 * 全局异常处理器
 * @author 孔旻
 *
 */
public class GlobalExceptionResolver implements HandlerExceptionResolver{

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionResolver.class); 
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		// TODO Auto-generated method stub
		
		//打印控制台
		ex.printStackTrace();
		//写日志
		logger.debug("测试输出的日志.....");
		logger.info("系统发生了异常。。。。。");
		logger.error("系统发生了异常",ex);
		
		//发邮件，发短信
		//使用jmail工具包，发短信使用第三方的webservice
		//显示错误信息
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("error/exception");
		return modelAndView;
	}

}
