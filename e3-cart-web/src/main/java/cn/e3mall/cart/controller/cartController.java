package cn.e3mall.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.common.utils.StringUtils;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.util.CookieUtils;
import cn.e3mall.common.util.E3Result;
import cn.e3mall.common.util.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.service.ItemService;

@Controller
public class cartController {
	@Autowired
	private CartService cartservice;
	@Autowired
	private ItemService itemservice;

	@RequestMapping("/cart/add/{itemId}")
	public String addCart(@PathVariable Long itemId, Integer num, HttpServletRequest request,
			HttpServletResponse response) {
		// 判断用户是否登陆
		TbUser user = (TbUser) request.getAttribute("user");
		// 已经登陆
		if (user != null) {
			E3Result e3 = cartservice.addcart(user.getId() + "", itemId, num);
			if (e3.getStatus() == 200) {
				return "cartSuccess";
			}
		}
		// 未登录状态下直接存放在cookie中
		List<TbItem> list = getCartList(request);
		boolean flag = false;
		if (list != null && list.size() > 0) {
			for (TbItem item : list) {
				// itemId已经是一个对象了，所以不能直接用===进行比较
				if (item.getId() == itemId.longValue()) {
					item.setNum(item.getNum() + num);
					flag = true;
					break;
				}
			}
		}
		if (flag == false) {

			TbItem item = itemservice.getItemById(itemId);
			// 需要对图片进行处理
			String image = item.getImage();
			if (!StringUtils.isBlank(image)) {
				String[] str = image.split(",");
				item.setImage(str[0]);
			}
			item.setNum(num);
			list.add(item);
			// 一定要是编码的因为有中文
		}
		// 防止出现一个商品展示两次但数量不变
		String json = JsonUtils.objectToJson(list);
		CookieUtils.setCookie(request, response, "TT_CART", json, true);

		return "cartSuccess";
	}

	public List<TbItem> getCartList(HttpServletRequest request) {

		String json = CookieUtils.getCookieValue(request, "TT_CART", true);
		if (!StringUtils.isBlank(json)) {
			List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
			return list;
		}
		// 不能返回空因为空的话在方法中无法给他添加值
		return new ArrayList<>();

	}

	@RequestMapping("/cart/cart")
	public String showCart(HttpServletRequest request, HttpServletResponse response, Model model) {
		
		List<TbItem> list=getCartList(request);
		
		TbUser user = (TbUser) request.getAttribute("user");
		// 已经登陆
		if (user != null) {
			/*String json = CookieUtils.getCookieValue(request, "TT_CART", true);
			List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);*/
		/*	if(list!=null||list.size()>0){*/
			E3Result e3 = cartservice.merage(user.getId() + "", list);
			if (e3.getStatus() == 200) {
				CookieUtils.deleteCookie(request, response, "TT_CART");
				List<TbItem> li = cartservice.getCart(user.getId() + "");
				model.addAttribute("cartList", li);
			}
		/*	}*/
			return "cart";
		}

		/*String str = CookieUtils.getCookieValue(request, "TT_CART", true);
		if (!StringUtils.isBlank(str)) {
			List<TbItem> list = JsonUtils.jsonToList(str, TbItem.class);
			
		}*/
		model.addAttribute("cartList", list);
		return "cart";
	}

	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public E3Result addnum(HttpServletResponse response, HttpServletRequest request, @PathVariable Long itemId,
			@PathVariable Integer num) {
		// 判断用户是否登陆
				TbUser user = (TbUser) request.getAttribute("user");
				// 已经登陆
				if (user != null) {
					E3Result e3 = cartservice.addnum(user.getId() + "", itemId,num);
					if (e3.getStatus() == 200) {
						return E3Result.ok();
					}
				}

		List<TbItem> list = getCartList(request);
		if (list != null && list.size() > 0) {
			for (TbItem item : list) {
				// itemId已经是一个对象了，所以不能直接用===进行比较
				if (item.getId() == itemId.longValue()) {
					item.setNum(num);
					break;
				}
			}
			CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(list), true);
		}

		// }

		return E3Result.ok();
	}

	@RequestMapping("/cart/delete/{cartId}")
	public String deleteCart(@PathVariable Long cartId, HttpServletResponse response, HttpServletRequest request) {
		// 判断用户是否登陆
		TbUser user = (TbUser) request.getAttribute("user");
		// 已经登陆
		if (user != null) {
			E3Result e3 = cartservice.deleteCart(user.getId() + "", cartId);
			if (e3.getStatus() == 200) {
				return "redirect:/cart/cart.html";
			}
		}

		List<TbItem> list = getCartList(request);
		if (list != null && list.size() > 0) {
			for (TbItem item : list) {
				// itemId已经是一个对象了，所以不能直接用===进行比较
				if (item.getId() == cartId.longValue()) {
					// item.setNum(num);
					list.remove(item);
					break;
				}
			}
			CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(list), true);
		}

		return "redirect:/cart/cart.html";
	}

}
