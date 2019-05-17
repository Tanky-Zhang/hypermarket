package cn.e3mall.item.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import cn.e3mall.item.pojo.Item;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class GetHtmlcontrollerListener implements MessageListener {
	@Autowired
	private ItemService itemservice;
	@Autowired
	FreeMarkerConfigurer freeconfig;

	@Override
	public void onMessage(Message mess) {
		// TODO Auto-generated method stub
		
		
		try {
			TextMessage message=(TextMessage)mess;
			long id=Long.parseLong(message.getText());
			TbItem tbitem = itemservice.getItemById(id);
			Item item=new Item(tbitem);
			/*String images=item.getImage();
			if(images!=null&&images.equals("")){
			String[] image = images.split(",");
			//return image;
			item.setImages(image);
			}	*/		
			TbItemDesc itemDesc = itemservice.getItemdesc(id);
			Map map=new HashMap<>();
			map.put("item", item);
			map.put("itemDesc", itemDesc);
			Configuration configuration=freeconfig.getConfiguration();
			Template templement=configuration.getTemplate("item.ftl");
			Writer out=new FileWriter(new File("D:/tem/"+id+".html"));
			templement.process(map, out);
			out.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
