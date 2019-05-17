package cn.e3mall.portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3mall.content.service.ContentService;
import cn.e3mall.pojo.TbContent;

@Controller
public class IndexController {
	@Autowired
	private ContentService contentservice;
	@RequestMapping("/index")
	public String showIndex(Model model){
		long id=89;
		List<TbContent> list=contentservice.showList(id);
		model.addAttribute("ad1List", list);
		return "index";
	}

}
