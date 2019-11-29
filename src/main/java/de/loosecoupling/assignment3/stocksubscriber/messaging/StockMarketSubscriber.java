package de.loosecoupling.assignment3.stocksubscriber.messaging;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

@Component
public class StockMarketSubscriber implements MessageListener {

	private static final Logger LOG = LoggerFactory.getLogger(StockMarketSubscriber.class);

	@Override
	public void onMessage(Message message) {
		MapMessage mapMessage = (MapMessage) message;
		LOG.info("Got a StreamMessage from 'StockMarket'");
		try {
			LOG.info("Company Name: " + mapMessage.getStringProperty("CompanyName"));
			LOG.info("Company Value: " + mapMessage.getStringProperty("CompanyValue"));
		} catch (JMSException e) {
			LOG.error(e.getMessage(), e);
		}
		
	}
}
