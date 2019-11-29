package de.loosecoupling.assignment3.stocksubscriber.messaging;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Sender {
	
	@Autowired
	MessageProducer sellQueueProducer;
	@Autowired
	MessageProducer buyQueueProducer;
	@Autowired
	MessageFactory messageFactory;

	public void buyStock(String companyName, int companyValue, String subscriberId) throws JMSException {
		TextMessage message = messageFactory.generateStockSubscriberMessage(companyName, companyValue, subscriberId, "buy");
		buyQueueProducer.send(message);
	}
	
    public void sellStock(String companyName, int companyValue, String subscriberId) throws JMSException {
		TextMessage message = messageFactory.generateStockSubscriberMessage(companyName, companyValue, subscriberId, "sell");
		sellQueueProducer.send(message);
	}
	
}
