package cn.e3mall.sso.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.util.E3Result;
import cn.e3mall.common.util.JsonUtils;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.pojo.TbUserExample.Criteria;
import cn.e3mall.sso.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService {
	@Autowired
	private TbUserMapper tbusermapper;
	@Autowired
	private JedisClient jedis;

	@Override
	public E3Result login(String username, String password) {
		// TODO Auto-generated method stub
		/*
		 * if(tb==null){ return E3Result }
		 */
		TbUserExample ex = new TbUserExample();
		Criteria cr = ex.createCriteria();
		cr.andUsernameEqualTo(username);
		List<TbUser> list = tbusermapper.selectByExample(ex);
		TbUser user = new TbUser();
		if (list != null && !list.isEmpty()) {
			user = list.get(0);
		System.out.println(DigestUtils.md5DigestAsHex(password.getBytes()));
			if (!user.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))) {
				return E3Result.build(400, "用户名或密码错误！！！");

			} else {
				user.setPassword(null);
				String json = JsonUtils.objectToJson(user);
				String token = UUID.randomUUID().toString();
				jedis.set("USER" + token, json);
				return E3Result.ok(token);
			}
		}
		return E3Result.build(400, "用户名或密码错误！！！");
		// user.getPassword();

	}

	@Override
	public E3Result getToken(String token) {
		// TODO Auto-generated method stub
		String str = jedis.get("USER"+token);
		TbUser user=new TbUser();
		if(!StringUtils.isEmpty(str)){
		 user = JsonUtils.jsonToPojo(str, TbUser.class);
		}else{
			return E3Result.build(400, "用户登陆已经过期，请重新登陆");
		}
		return E3Result.ok(user);
	}


}
