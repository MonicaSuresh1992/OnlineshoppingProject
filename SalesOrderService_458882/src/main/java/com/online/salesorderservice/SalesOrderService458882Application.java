package com.online.salesorderservice;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.online.salesorderservice.domain.Customer;

@SpringBootApplication
public class SalesOrderService458882Application {

	public static void main(String[] args) {
		SpringApplication.run(SalesOrderService458882Application.class, args);
	}
	
	@RabbitListener(queues = "CustomerCreated")
	public void receiveMessage(Customer customer) {
		System.out.println("Customer Details" + customer.getId());
	}
}
	