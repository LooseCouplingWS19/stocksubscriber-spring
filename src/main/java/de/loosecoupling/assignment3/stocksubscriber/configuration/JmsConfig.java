package de.loosecoupling.assignment3.stocksubscriber.configuration;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.loosecoupling.assignment3.stocksubscriber.messaging.StockMarketSubscriber;

@Configuration
public class JmsConfig {

	@Value("${spring.activemq.broker-url}")
	private String brokerUrl;
	@Value("${spring.activemq.user}")
	private String brokerUser;
	@Value("${spring.activemq.password}")
	private String brokerPassword;
	
	@Autowired
	private StockMarketSubscriber messageSubscriber;
	
	private final String clientId = "StockSubscriber-1";

	@Bean
	public ConnectionFactory activeMQConnectionFactory() {
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
		activeMQConnectionFactory.setBrokerURL(brokerUrl);
		activeMQConnectionFactory.setUserName(brokerUser);
		activeMQConnectionFactory.setPassword(brokerPassword);

		return (ConnectionFactory) activeMQConnectionFactory;
	}
	
	@Bean
	public Connection activeMQConnection(ConnectionFactory activeMQConnectionFactory) throws JMSException {
		Connection activeMQConnection = activeMQConnectionFactory.createConnection();
		activeMQConnection.setClientID(clientId);
		activeMQConnection.start();
		return activeMQConnection;
	}
	
	@Bean
	public TopicSession activeMQTopicSession(Connection activeMQConnection) throws JMSException {
		TopicConnection topicConnection = (TopicConnection) activeMQConnection;
		return topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
	}
	
	@Bean
	public QueueSession activeMQQueueSession(Connection activeMQConnection) throws JMSException {
		QueueConnection queueConnection = (QueueConnection) activeMQConnection;
		return queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
	}
	
	@Bean
	public Topic stockMarketTopic(TopicSession activeMQTopicSession) throws JMSException {
		return activeMQTopicSession.createTopic("StockMarket");
	}
	
	@Bean
	public TopicSubscriber topicSubscriber(TopicSession activeMQTopicSession, Topic stockMarketTopic) throws JMSException {
		TopicSubscriber subscriber = activeMQTopicSession.createDurableSubscriber(stockMarketTopic, "StockSubscriber");
		subscriber.setMessageListener(messageSubscriber);
		return subscriber;
	}
	
	@Bean
	public Queue buyStockQueue(QueueSession activeMQQueueSession) throws JMSException {
		return activeMQQueueSession.createQueue("Buy");
	}
	
	@Bean
	public Queue sellStockQueue(QueueSession activeMQQueueSession) throws JMSException {
		return activeMQQueueSession.createQueue("Sell");
	}
	
	@Bean
	public MessageProducer buyQueueProducer(QueueSession activeMQQueueSession, Queue buyStockQueue) throws JMSException {
		MessageProducer producer = activeMQQueueSession.createProducer(buyStockQueue);
		producer.setDeliveryMode(DeliveryMode.PERSISTENT);
		return producer;
	}
	
	@Bean
	public MessageProducer sellQueueProducer(QueueSession activeMQQueueSession, Queue sellStockQueue) throws JMSException {
		MessageProducer producer = activeMQQueueSession.createProducer(sellStockQueue);
		producer.setDeliveryMode(DeliveryMode.PERSISTENT);
		return producer;
	}
	
}
