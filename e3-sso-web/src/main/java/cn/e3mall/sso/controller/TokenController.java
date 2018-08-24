package cn.e3mall.sso.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.comm.util.E3Result;
import cn.e3mall.comm.util.JsonUtils;
import cn.e3mall.sso.service.TokenService;

/**
 * 根据token查看用户信息
 * @author 孔旻
 *
 */
@Controller
public class TokenController {
	@Autowired
	private TokenService tokenService;
	
	@RequestMapping(value="/user/token/{token}",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public Object  getUserByToken(@PathVariable String token,String callback){
		E3Result e3Result = tokenService.getUserByToken(token);
		//返回结果之前，判断是否是jsonp请求
		if(StringUtils.isNotBlank(callback)){
			//把结果封装成一个js语句的响应
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(e3Result);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
			//return callback+"("+JsonUtils.objectToJson(e3Result)+");";
		}
		return e3Result;
	}

}
