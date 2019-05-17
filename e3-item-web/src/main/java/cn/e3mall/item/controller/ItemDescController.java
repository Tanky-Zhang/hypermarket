package cn.e3mall.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3mall.item.pojo.Item;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;
@Controller
public class ItemDescController {
	
	@Autowired
	private ItemService itemservice;
	
	@RequestMapping("/item/{itemId}")
	
	public String itemDesc(@PathVariable("itemId") Long itemId,Model model){
		
		TbItem item = itemservice.getItemById(itemId);
		Item item2=new Item(item);
		TbItemDesc itemdesc=itemservice.getItemdesc(itemId);
		model.addAttribute("item", item2);
		model.addAttribute("itemDesc", itemdesc);
		return "item";
	}
	
	

}
