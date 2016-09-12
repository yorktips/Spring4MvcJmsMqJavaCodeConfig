package com.york.springmvc.service;

import java.util.Map;

import com.york.springmvc.model.InventoryResponse;
import com.york.springmvc.model.Order;

public interface OrderService {
	public void sendOrder(Order order);
	
	public void updateOrder(InventoryResponse response);
	
	public Map<String, Order> getAllOrders();
}
