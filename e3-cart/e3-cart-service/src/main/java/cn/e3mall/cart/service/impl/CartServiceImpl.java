package cn.e3mall.cart.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.StringUtils;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.util.E3Result;
import cn.e3mall.common.util.JsonUtils;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;

@Service
public class CartServiceImpl implements CartService {
	@Autowired
	private JedisClient jedis;
	@Autowired
	private TbItemMapper itemmapper;

	@Override
	public E3Result addcart(String string, Long itemId, Integer num) {
		String str = jedis.hget("CART:"+string, itemId + "");
		if (!StringUtils.isBlank(str)) {
			TbItem item = JsonUtils.jsonToPojo(str, TbItem.class);
			item.setNum(item.getNum() + num);
			jedis.hset("CART:"+string, itemId + "", JsonUtils.objectToJson(item));
			return E3Result.ok();
		}
		TbItem item = itemmapper.selectByPrimaryKey(itemId);
		String image = item.getImage();
		if (!StringUtils.isBlank(image)) {
			String[] st = image.split(",");
			item.setImage(st[0]);
		}
		item.setNum(num);
		jedis.hset("CART:"+string, itemId + "", JsonUtils.objectToJson(item));
		// jedis.hset(user.getId(), itemId+"", )
		return E3Result.ok();
	}

	@Override
	public E3Result merage(String string, List<TbItem> list) {
		// TODO Auto-generated method stub

		for (TbItem item : list) {
			//jedis.hset("CART:" + string, item.getId() + "", JsonUtils.objectToJson(item));
			addcart(string,item.getId(),item.getNum());
		}
		return E3Result.ok();
	}

	@Override
	public List<TbItem> getCart(String string) {
		// TODO Auto-generated method stub
		//String str = jedis.hget("CART:" + string, itemId + "");
		List<String> hvals = jedis.hvals("CART:"+string);
		List<TbItem> list=new ArrayList<>();
		for (String str : hvals) {
			TbItem item = JsonUtils.jsonToPojo(str, TbItem.class);
			String[] image = item.getImage().split(",");
			item.setImage(image[0]);
			list.add(item);
			
		
		}
		return list;
	}

	@Override
	public E3Result deleteCart(String string, Long cartId) {
		jedis.hdel("CART:" + string, cartId + "");
		/*if (!StringUtils.isBlank(str)) {
			TbItem item = JsonUtils.jsonToPojo(str, TbItem.class);
			item.setNum(item.getNum() + num);
			jedis.hset("CART:" + string, itemId + "", JsonUtils.objectToJson(item));
			return E3Result.ok();
		}
		TbItem item = itemmapper.selectByPrimaryKey(itemId);
		String image = item.getImage();
		if (!StringUtils.isBlank(image)) {
			String[] st = image.split(",");
			item.setImage(st[0]);
		}
		item.setNum(num);
		jedis.hset("CART:" + string, itemId + "", JsonUtils.objectToJson(item));
		// jedis.hset(user.getId(), itemId+"", )
*/		return E3Result.ok();
	}

	@Override
	public E3Result addnum(String string, Long itemId, Integer num) {
		String str = jedis.hget("CART:" + string, itemId+"");
		if(!StringUtils.isBlank(str)){
			TbItem item = JsonUtils.jsonToPojo(str, TbItem.class);
			item.setNum(num);
			jedis.hset("CART:" + string, itemId + "", JsonUtils.objectToJson(item));
		}	
		return E3Result.ok();
	}

}
