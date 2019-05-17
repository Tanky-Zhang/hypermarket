package cn.e3mall.order.service;

import cn.e3mall.common.util.E3Result;
import cn.e3mall.order.pojo.OrderIfo;

public interface OrderService {

	E3Result insertOrdre(OrderIfo order);

}
