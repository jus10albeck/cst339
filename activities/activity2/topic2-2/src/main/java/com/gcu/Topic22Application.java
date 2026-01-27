package com.gcu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.gcu"})
public class Topic22Application {

	public static void main(String[] args) {
		SpringApplication.run(Topic22Application.class, args);
	}

}
