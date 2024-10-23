package com.itmaxglobal.billing;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class BillingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BillingApplication.class, args);
	}

}
