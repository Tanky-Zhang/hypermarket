package cn.e3mall.order.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.util.E3Result;
import cn.e3mall.mapper.TbOrderItemMapper;
import cn.e3mall.mapper.TbOrderMapper;
import cn.e3mall.mapper.TbOrderShippingMapper;
import cn.e3mall.order.pojo.OrderIfo;
import cn.e3mall.order.service.OrderService;
import cn.e3mall.pojo.TbOrderItem;
import cn.e3mall.pojo.TbOrderShipping;
@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private JedisClient jedis;
	@Autowired
	private TbOrderMapper ordermapper;
	@Autowired
	private TbOrderShippingMapper shippingmapper;
	@Autowired
	private TbOrderItemMapper itemmapper;
	@Override
	public E3Result insertOrdre(OrderIfo order) {
		// TODO Auto-generated method stub
		if(!jedis.exists("ORDERID")){
			jedis.set("ORDERID", "100544");
		}
		//向订单表插入数据
		String orderid=jedis.incr("ORDERID").toString();
		order.setOrderId(orderid);
		//状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭"
		order.setStatus(1);
		Date date=new Date();
		order.setCreateTime(date);
		order.setUpdateTime(date);
		ordermapper.insert(order);
		//向物流表中插入数据
		TbOrderShipping ordershipping=order.getOrderShipping();
		ordershipping.setOrderId(orderid);
		ordershipping.setCreated(date);
		ordershipping.setUpdated(date);
		shippingmapper.insert(ordershipping);
		//向订单商品表中插入数据
		List<TbOrderItem> list=order.getOrderItems();
		for (TbOrderItem item : list) {
			//String str = UUID.randomUUID().toString();
			long id=jedis.incr("ORDERID");
			item.setId(id+"");
			item.setOrderId(orderid);
			itemmapper.insert(item);
		}
		return E3Result.ok(orderid);
	}

}
