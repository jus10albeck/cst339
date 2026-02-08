package com.gcu;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gcu.business.CollectionsBusinessInterface;
import com.gcu.business.CollectionsBusinessService;

@Configuration
public class SpringConfig 
{
	@Bean(initMethod="init", destroyMethod="destroy")
	public CollectionsBusinessInterface getCollectionsBusiness()
	{
		return new CollectionsBusinessService();
	}
}
