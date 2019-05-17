package cn.e3mall.sso.service;

import cn.e3mall.common.util.E3Result;
import cn.e3mall.pojo.TbUser;

public interface RegisterService {

	public E3Result checkRegister(String params,int type);
	public E3Result register(TbUser tb);
}
