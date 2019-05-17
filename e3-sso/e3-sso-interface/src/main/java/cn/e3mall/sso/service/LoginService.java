package cn.e3mall.sso.service;

import cn.e3mall.common.util.E3Result;
import cn.e3mall.pojo.TbUser;

public interface LoginService {
	public E3Result login(String username,String password);

	public E3Result getToken(String token);
}
