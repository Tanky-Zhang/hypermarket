package cn.e3mall.cart.service;

import java.util.List;

import cn.e3mall.common.util.E3Result;
import cn.e3mall.pojo.TbItem;

public interface CartService {

	E3Result addcart(String string, Long itemId, Integer num);

	E3Result merage(String string, List<TbItem> list);
	
	List<TbItem> getCart(String string);

	E3Result deleteCart(String string, Long cartId);

	E3Result addnum(String string, Long itemId, Integer num);

}
