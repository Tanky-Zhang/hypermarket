package cn.e3mall.order.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.dubbo.common.utils.StringUtils;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.util.CookieUtils;
import cn.e3mall.common.util.E3Result;
import cn.e3mall.common.util.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.LoginService;

public class OrderIntecepter implements HandlerInterceptor {
	@Autowired
	private LoginService loginservice;
	
	@Autowired
	private CartService cartservice;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		String str = CookieUtils.getCookieValue(request, "COOKIE_TOKEN_key");
		if(StringUtils.isBlank(str)){
			//跳转到登陆页面
			response.sendRedirect("http://localhost:8087/page/login?url="+request.getRequestURL());
			return false;
		}
		//不等于空的情况要去调用服务，不要使用jedis直接去查
		E3Result e3 = loginservice.getToken(str);
		if(e3.getStatus()!=200){
			
			response.sendRedirect("http://localhost:8087/page/login?url="+request.getRequestURL());
			return false;
		}
		TbUser user = (TbUser)e3.getData();
		//cartservice.getCart(user.getId()+"");
		request.setAttribute("user", user);
		String json = CookieUtils.getCookieValue(request, "TT_CART",true);
		List<TbItem> list=new ArrayList<TbItem>();
		if(!StringUtils.isBlank(json)){
		 list = JsonUtils.jsonToList(json, TbItem.class);
		}
		//合并购物车
		cartservice.merage(user.getId()+"", list);	
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
