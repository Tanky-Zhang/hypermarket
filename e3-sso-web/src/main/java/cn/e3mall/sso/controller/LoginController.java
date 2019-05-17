package cn.e3mall.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.common.utils.StringUtils;

import cn.e3mall.common.util.CookieUtils;
import cn.e3mall.common.util.E3Result;
import cn.e3mall.common.util.JsonUtils;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.LoginService;

@Controller
public class LoginController {
	@Autowired
	private LoginService loginservice;
	
	@RequestMapping("/page/login")
	public String showLogin(String url,Model model){
		model.addAttribute("redirect", url);
		return "login";
	}
	
	@RequestMapping("/user/login")
	@ResponseBody
	public E3Result login(String username,String password,HttpServletRequest request,HttpServletResponse response){
		E3Result e3= loginservice.login(username, password);
		String token = (String)e3.getData();
		//CookieUtils.setCookie(request, response, "COOKIE_TOKEN", token);
		CookieUtils.setCookie(request, response, "COOKIE_TOKEN_key", token);		
		return e3;
	}
	
	@RequestMapping(value="/user/token/{token}",produces=MediaType.APPLICATION_JSON_VALUE+";charset=utf-8")
	@ResponseBody
	public String getToken(@PathVariable String token,String callback){
		E3Result e3 = loginservice.getToken(token);
		if(!StringUtils.isBlank(callback)){
			
			//TbUser data = (TbUser)e3.getData();
			String str=callback+"("+JsonUtils.objectToJson(e3)+")";
			return str;
			
		}
		
		
		return JsonUtils.objectToJson(e3);
		
		
	}

}
