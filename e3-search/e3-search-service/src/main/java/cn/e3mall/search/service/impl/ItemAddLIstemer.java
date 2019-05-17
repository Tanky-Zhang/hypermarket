package cn.e3mall.search.service.impl;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;

import cn.e3mall.search.service.SearchItemService;

public class ItemAddLIstemer implements MessageListener{
	//要用接口去接收
	@Autowired
	private SearchItemService search;
//private SearchItemServiceImpl search =new SearchItemServiceImpl();
	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		
		try {
			TextMessage textmessage=(TextMessage)message;
			long id=Long.parseLong(textmessage.getText());
			search.itemaddsolr(id);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
