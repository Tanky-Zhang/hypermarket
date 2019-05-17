package cn.e3mall.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.util.E3Result;
import cn.e3mall.common.util.IDUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.service.ItemService;


/**
 * 商品管理Controller
 * <p>Title: ItemController</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/item/{itemId}")
	@ResponseBody
	public TbItem getItemById(@PathVariable Long itemId) {
		TbItem tbItem = itemService.getItemById(itemId);
		return tbItem;
	}
	
	@RequestMapping("/item/list")
	@ResponseBody
	public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
		//调用服务查询商品列表
		EasyUIDataGridResult result = itemService.getItemList(page, rows);
		return result;
	}
	@RequestMapping("/item/cat/list")
	@ResponseBody
	public List<EasyUITreeNode> getCatList(@RequestParam(value="id",defaultValue = "0") long id) {
		// 调用服务查询商品列表
		// EasyUIDataGridResult result = itemService.getItemList(page, rows);
		// longid=0;
		List<EasyUITreeNode> list = itemService.selectTreeNode(id);
		return list;
	}
	@RequestMapping("/item/save")
	@ResponseBody
	public E3Result addItem(TbItem item,String desc){

		return itemService.addItem(item, desc);
	}

}
