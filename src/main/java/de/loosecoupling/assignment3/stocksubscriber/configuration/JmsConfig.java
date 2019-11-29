package de.loosecoupling.assignment3.stocksubscriber.configuration;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
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
	public TopicConnectionFactory activeMQConnectionFactory() {
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
		activeMQConnectionFactory.setBrokerURL(brokerUrl);
		activeMQConnectionFactory.setUserName(brokerUser);
		activeMQConnectionFactory.setPassword(brokerPassword);

		return (TopicConnectionFactory) activeMQConnectionFactory;
	}
	
	@Bean
	public TopicConnection activeMQConnection(TopicConnectionFactory activeMQConnectionFactory) throws JMSException {
		TopicConnection activeMQConnection = activeMQConnectionFactory.createTopicConnection();
		activeMQConnection.setClientID(clientId);
		activeMQConnection.start();
		return activeMQConnection;
	}
	
	@Bean
	public TopicSession activeMQSession(TopicConnection activeMQConnection) throws JMSException {
		return activeMQConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
	}
	
	@Bean
	public Topic stockMarketTopic(TopicSession activeMQSession) throws JMSException {
		return activeMQSession.createTopic("StockMarket");
	}
	
	@Bean
	public TopicSubscriber topicSubscriber(TopicSession activeMQSession, Topic stockMarketTopic) throws JMSException {
		TopicSubscriber subscriber = activeMQSession.createDurableSubscriber(stockMarketTopic, "StockSubscriber");
		subscriber.setMessageListener(messageSubscriber);
		return subscriber;
	}
	
}
