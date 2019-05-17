package cn.e3mall.cart.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.dubbo.common.utils.StringUtils;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.util.CookieUtils;
import cn.e3mall.common.util.E3Result;
import cn.e3mall.common.util.JsonUtils;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.LoginService;

public class LoginIntercepter implements HandlerInterceptor{
	@Autowired
	private LoginService loginservice;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 执行之前的处理
		String token = CookieUtils.getCookieValue(request, "COOKIE_TOKEN_key");
		if(StringUtils.isBlank(token)){
			return true;
		}
		E3Result e3 = loginservice.getToken(token);
		if(e3.getStatus()!=200){
			return true;
		}
		TbUser user = (TbUser)e3.getData();
		request.setAttribute("user", user);				
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// 执行之后返回逻辑视图之前的处理
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// 执行之后返回逻辑视图之后的处理
		
	}

}
