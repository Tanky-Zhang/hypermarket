package cn.e3mall.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

//import com.alibaba.dubbo.config.support.Parameter;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.service.SearchItemService;

@Controller
public class SearchController {
	@Autowired
	private SearchItemService searchservice;

	@RequestMapping("/search")
	public String search(@RequestParam(defaultValue = "1") int page, String keyword, Model model) throws Exception {
		keyword=new String(keyword.getBytes("iso8859-1"),"utf-8");
		SearchResult search = searchservice.search(page, 10, keyword);
		model.addAttribute("query", keyword);
		model.addAttribute("totalPages", search.getTotalPages());
		model.addAttribute("recourdCount", search.getTotalcount());
		model.addAttribute("itemList", search.getList());
		model.addAttribute("page", page);
		return "search";
	}

}
