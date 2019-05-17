package cn.e3mall.item.controller;


import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Controller
public class testFree {
	@Autowired
	FreeMarkerConfigurer freeconf;
	
	@RequestMapping("/gethtml")
	@ResponseBody
   public String getFtl() throws Exception{
		Configuration configuration=freeconf.getConfiguration();
	   
		Template template=configuration.getTemplate("hello.ftl");
		Map map=new HashMap<>();
		map.put("hello", "hello freeMark");
		//model.addAttribute("hello", map);
		Writer out=new FileWriter(new File("D:/tem/hello.html"));
		template.process(map, out);
		out.close();
		return "ok";
	   
   }

}
