package com.york.test.springmvc;

import static org.junit.Assert.*;

import java.io.InvalidObjectException;
import java.util.Map;
import java.util.Set;

import com.york.springmvc.configuration.*;
import com.york.springmvc.model.Order;
import com.york.springmvc.service.OrderRepository;
import com.york.springmvc.service.OrderService;
import com.york.springmvc.service.OrderServiceImpl;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=SpringTestConfig.class,loader=AnnotationConfigContextLoader.class)
public class OrderServiceImplTest {
	   
	@Autowired
	OrderService orderService;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		assertNotNull(orderService);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testSendOrder() {
		Order order = new Order();
		String name="MyFirstProd";
		order.setProductName(name);
		order.setQuantity(1);
		orderService.sendOrder(order);
		Map<String, Order> orders=orderService.getAllOrders();
		
		Order order_= getOrderByname(name,orders);
		assertNotNull(order_);
		assertEquals(name, order_.getProductName());
	}

	public final Order getOrderByname(String Name,Map<String, Order> orders) {
		Set<String> keys=orders.keySet();
		for (String key : keys) {
			Order order_=orders.get(key);
			if (order_.getProductName().equals(Name)) return order_;
		}
		return null;
	}
		
	
	//@Test(expected = InvalidObjectException.class)
	public final void testUpdateOrder() {
		
		//fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetAllOrders() {
		Map<String, Order> orders=orderService.getAllOrders();
		assertNotNull(orders);
	}

}
