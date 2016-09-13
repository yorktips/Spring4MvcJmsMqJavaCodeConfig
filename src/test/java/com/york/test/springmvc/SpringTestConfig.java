package com.york.test.springmvc;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.york.springmvc.configuration.MessagingConfiguration;
import com.york.springmvc.configuration.MessagingListnerConfiguration;

@Configuration
@ComponentScan(basePackages = {"com.york.springmvc.model","com.york.springmvc.messaging","com.york.springmvc.service"})
@Import({MessagingConfiguration.class,MessagingListnerConfiguration.class})
public class SpringTestConfig {}
