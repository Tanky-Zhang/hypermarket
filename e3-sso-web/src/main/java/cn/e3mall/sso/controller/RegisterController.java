package cn.e3mall.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.util.E3Result;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.RegisterService;

@Controller
public class RegisterController {
	
	@Autowired
	RegisterService registerService;
	
	@RequestMapping("/page/register")
	public String showRegister(){
		
		return "register";
	}
	@RequestMapping("/user/check/{params}/{type}")
	@ResponseBody
	public E3Result checkRegister(@PathVariable String params,@PathVariable int type){
		
		E3Result e3 = registerService.checkRegister(params, type);
		return e3;
	}
	@RequestMapping(value="/user/register",method=RequestMethod.POST)
	@ResponseBody
	public E3Result register(TbUser tb){
		
		return registerService.register(tb);
	}

}
