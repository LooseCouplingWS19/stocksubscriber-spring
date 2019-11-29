package de.loosecoupling.assignment3.stocksubscriber.messaging;

import javax.jms.JMSException;
import javax.jms.QueueSession;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageFactory {
	
	@Autowired
	QueueSession activeMQQueueSession;

	public TextMessage generateStockSubscriberMessage(String companyName, int companyValue, String subscriberId, String mode) throws JMSException {
		TextMessage message = activeMQQueueSession.createTextMessage();
		message.setText(subscriberId + " wants to " + mode + " " + companyName + " for " + companyValue + " milion euro");
		return message;
	}
}
