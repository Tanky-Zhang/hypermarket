package cn.e3mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.util.E3Result;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.pojo.TbContent;

@Controller
public class ContentController {
	@Autowired
	private ContentService contentService;
	
	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<EasyUITreeNode> getCategory(@RequestParam(value="id",defaultValue = "0") long id){		
		return contentService.getCategory(id);
	}
	
	@RequestMapping("/content/category/create")
	@ResponseBody
	public E3Result addCategory(Long parentId,String name){
		
		//E3Result e3=
		//return contentService.updateCatgory(parentId,name);
		
		return contentService.updateCatgory(parentId, name);
	}
	@RequestMapping("/content/category/update")
	@ResponseBody
	public E3Result updateCatgory(Long id,String name){
		
		
		return contentService.updateCat(id, name);
	}
	@RequestMapping("/content/category/delete")
	@ResponseBody
	public E3Result deleteCat(Long id){
		
		E3Result e=contentService.deleteCat(id);
		return e;
	}
	@RequestMapping("/content/save")
	@ResponseBody
	public E3Result addContent(TbContent tb){
		
		//E3Result e=contentService.deleteCat(id);
		return contentService.addContent(tb);
	}

}
