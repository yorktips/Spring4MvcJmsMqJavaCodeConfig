package com.york.springmvc.configuration;

import java.util.Arrays;

import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.core.JmsTemplate;

@Configuration
@PropertySource("classpath:application.properties")
public class MessagingConfiguration {

	@Value("${mdp.jms.url}")
	private String DEFAULT_BROKER_URL;
	
	@Value("${myApp.jms.inbound.queue.name}")
	private String ORDER_QUEUE;
	
	@Bean
	public ActiveMQConnectionFactory connectionFactory(){

		System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES","com.york.springmvc");

		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
		connectionFactory.setBrokerURL(DEFAULT_BROKER_URL);
		connectionFactory.setTrustedPackages(Arrays.asList("com.york.springmvc"));
		
		RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
		redeliveryPolicy.setInitialRedeliveryDelay(0);
		redeliveryPolicy.setRedeliveryDelay(3000);
		redeliveryPolicy.setUseExponentialBackOff(false);
		redeliveryPolicy.setMaximumRedeliveries(3);
		connectionFactory.setRedeliveryPolicy(redeliveryPolicy);
		
		//connectionFactory.setTrustAllPackages(true);
		return connectionFactory;
	}
	
	@Bean 
	public JmsTemplate jmsTemplate(){
		long receiveTimeout=1000l;
		String CLIENT_ACKNOWLEDGE="CLIENT_ACKNOWLEDGE";
		
		JmsTemplate template = new JmsTemplate();
		template.setConnectionFactory(connectionFactory());
		template.setDefaultDestinationName(ORDER_QUEUE);
		template.setReceiveTimeout(receiveTimeout);
		template.setSessionAcknowledgeModeName(CLIENT_ACKNOWLEDGE);
		//template.setSessionTransacted(true);
		
		return template;
	}
	
}
