package cn.e3mall.activemq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class testMessageListener  implements MessageListener{

	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		TextMessage textmessage=(TextMessage)message;
		try {
			String text=textmessage.getText();
			System.out.println(text);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	

}
