package de.loosecoupling.assignment3.stocksubscriber.messaging;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.loosecoupling.assignment3.stocksubscriber.services.ProcessingService;

@Component
public class StockMarketSubscriber implements MessageListener {

	private static final Logger LOG = LoggerFactory.getLogger(StockMarketSubscriber.class);
	
	@Autowired
	ProcessingService processingService;

	@Override
	public void onMessage(Message message) {
		MapMessage mapMessage = (MapMessage) message;
		LOG.info("Got a StreamMessage from 'StockMarket'");
		try {
			processingService.processStockPrices(mapMessage);
		} catch (JMSException e) {
			LOG.error(e.getMessage(), e);
		}
		
	}
}
