package com.gcu;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gcu.business.AnotherOrdersBusinessService;
import com.gcu.business.OrdersBusinessInterface;
import com.gcu.business.OrdersBusinessService;

@Configuration
public class SpringConfig 
{
	@Bean(name="ordersBusinessService")
	public OrdersBusinessInterface getOrdersBusiness()
	{
		return new OrdersBusinessService();
	}
	
	
}
