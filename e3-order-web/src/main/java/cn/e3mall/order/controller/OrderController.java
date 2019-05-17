package cn.e3mall.order.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.util.E3Result;
import cn.e3mall.order.pojo.OrderIfo;
import cn.e3mall.order.service.OrderService;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;

@Controller
public class OrderController {
	@Autowired
	private CartService cartservice;
	
	@Autowired
	private OrderService orderservice;
	
	
	@RequestMapping("/order/order-cart")
	public String showOrder(HttpServletRequest request){
		
		TbUser user= (TbUser) request.getAttribute("user");
		List<TbItem> list = cartservice.getCart(user.getId()+"");
		request.setAttribute("cartList", list);
		return "order-cart";
	}
	
	@RequestMapping(value="/order/create")
	public String createOrder(OrderIfo orderIfo,HttpServletRequest request){
		
		TbUser user=(TbUser)request.getAttribute("user");
		orderIfo.setUserId(user.getId());
		orderIfo.setBuyerNick(user.getUsername());
		E3Result e3=  orderservice.insertOrdre(orderIfo);
		String orderid=e3.getData().toString();
		request.setAttribute("orderId", orderid);
		request.setAttribute("payment", orderIfo.getPayment());
		return "success";
	}
	
	
	

}
