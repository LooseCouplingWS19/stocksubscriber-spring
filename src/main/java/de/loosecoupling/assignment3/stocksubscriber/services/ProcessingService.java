package de.loosecoupling.assignment3.stocksubscriber.services;

import javax.jms.JMSException;
import javax.jms.MapMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import de.loosecoupling.assignment3.stocksubscriber.messaging.Sender;

@Service
public class ProcessingService {
	
	Logger LOG = LoggerFactory.getLogger(ProcessingService.class);
	
	@Value("${spring.application.name}")
    private String subscriberId;
	
	@Autowired
	private Sender sender;
	
	public void processStockPrices(MapMessage message) throws JMSException {
		String companyName = message.getString("CompanyName");
		int companyValue = message.getInt("CompanyValue");
		LOG.info("-------------------------------------------");
		LOG.info("Processing Stock for '" + companyName + "' which is worth " + companyValue + " milion euro");
		
		if (companyValue <= 10) {
			LOG.info("Buying stock");
			sender.buyStock(companyName, companyValue, subscriberId);
		}
		if (companyValue >= 100) {
			LOG.info("Selling stock");
			sender.sellStock(companyName, companyValue, subscriberId);
		}
	}
}
