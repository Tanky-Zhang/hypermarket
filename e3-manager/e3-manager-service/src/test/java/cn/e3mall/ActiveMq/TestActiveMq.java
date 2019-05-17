package cn.e3mall.ActiveMq;

import java.io.IOException;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class TestActiveMq {
	
	@Test
	public void testQuePro() throws JMSException{
		
		ConnectionFactory connection=new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
		Connection conn=connection.createConnection();
		conn.start();
		Session session=conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue queue=session.createQueue("test=queue");
		MessageProducer producer=session.createProducer(queue);
		TextMessage message = session.createTextMessage("hello world");
		producer.send(message);
		producer.close();
		session.close();
		conn.close();
		
		
	}
	@Test
	public void testQuCus() throws JMSException, IOException{
		
		ConnectionFactory connectionfactory=new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
		Connection connection=connectionfactory.createConnection();
		connection.start();
		Session session=connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue queue=session.createQueue("spring-queue");
		MessageConsumer customer=session.createConsumer(queue);
		customer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				// TODO Auto-generated method stub
				TextMessage textmessage=(TextMessage)message;
				try {
					String text = textmessage.getText();
					System.out.println(text);
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		});
		
		System.in.read();
		customer.close();
		session.close();
		connection.close();
		
		
	}
	@Test
	public void testTopicPro() throws Exception{
		ConnectionFactory connectionfactory=new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
		Connection connection =connectionfactory.createConnection();
		connection.start();
		Session session=connection.createSession(false, Session.AUTO_ACKNOWLEDGE);		
		Topic topic=session.createTopic("test-topic");	
		MessageProducer producer=session.createProducer(topic);
		TextMessage message = session.createTextMessage("hello topic");
		producer.send(message);
		producer.close();
		session.close();
		connection.close();
		
		
	}
	@Test
	public void testTopicCus() throws JMSException, IOException{
		
		ConnectionFactory connectionfactory=new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
		Connection connection=connectionfactory.createConnection();
		connection.start();
		Session session=connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Topic topic=session.createTopic("test-topic");
		MessageConsumer customer=session.createConsumer(topic);
		customer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				// TODO Auto-generated method stub
				TextMessage textmessage=(TextMessage)message;
				try {
					String text = textmessage.getText();
					System.out.println(text);
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		});
		
		System.in.read();
		customer.close();
		session.close();
		connection.close();
		
		
	}
	@Test
	public void testActives(){
		
		ApplicationContext applicationcontext=new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
		JmsTemplate jms=applicationcontext.getBean(JmsTemplate.class);
		Destination destination=(Destination)applicationcontext.getBean("queueDestination");
		jms.send(destination,new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				// TODO Auto-generated method stub
				TextMessage textmessage=session.createTextMessage("hello spring activemq");
				return textmessage;
			}
		});
		
		
	}


}
