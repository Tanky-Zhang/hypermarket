package cn.e3mall.sso.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import cn.e3mall.common.util.E3Result;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.pojo.TbUserExample.Criteria;
import cn.e3mall.sso.service.RegisterService;
@Service
public class RegisterServiceImp implements RegisterService {
	@Autowired
	private TbUserMapper tbusermapper;

	@Override
	public E3Result checkRegister(String params, int type) {
	
		TbUserExample example=new TbUserExample();
		Criteria cr = example.createCriteria();
		//cr.andUsernameEqualTo(value)
		//1 2 3分别代表username iPhone email
		List<TbUser> list=new ArrayList<>();
		if(type==1){
			cr.andUsernameEqualTo(params);
			 list = tbusermapper.selectByExample(example);
			 if(list.size()>0){
					
					return E3Result.ok(false);
				}
		}else if(type==2){
			cr.andPhoneEqualTo(params);
			 list = tbusermapper.selectByExample(example);
			 if(!list.isEmpty()){
					
					return E3Result.ok(false);
				}
		}else if(type==3){
			cr.andEmailEqualTo(params);
			 list = tbusermapper.selectByExample(example);
			 if(!list.isEmpty()){
					
					return E3Result.ok(false);
				}
		}else{
			
			return E3Result.build(400, "传入的数据有误");
		}
		
		return E3Result.ok(true);
	}

	@Override
	public E3Result register(TbUser tb) {
		// TODO Auto-generated method stub
		if(StringUtils.isEmpty(tb.getUsername())){
			return E3Result.build(400, "您输入的用户名为空");
		}
		if(StringUtils.isEmpty(tb.getPhone())){
			return E3Result.build(400, "您输入的手机号为空");
		}
		if(StringUtils.isEmpty(tb.getPassword())){
			return E3Result.build(400, "您输入的密码为空");
		}
		String str=DigestUtils.md5DigestAsHex(tb.getPassword().getBytes()).toString();
		System.err.println(str);
		tb.setPassword(str);
		tb.setCreated(new Date());
		tb.setUpdated(new Date());
		tbusermapper.insert(tb);
		return E3Result.ok();
		
		//return null;
	}

}
