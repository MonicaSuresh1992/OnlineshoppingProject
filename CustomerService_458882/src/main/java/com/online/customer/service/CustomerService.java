package com.online.customer.service;


import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.online.customer.domain.Customer;
import com.online.customer.repository.CustomerRepository;

public class CustomerService {

	@Autowired
	CustomerRepository customerRepository;
	private final RabbitTemplate template;

	@Autowired
	public CustomerService(RabbitTemplate template){
		this.template = template;
	}

	
	public List<Customer> getAllCustomers(){
		return customerRepository.getAllCustomers();
	}

	public Customer save(Customer customer) {
		System.out.println("<<<<<<<<<Inside Service Save function BEFORE REPOSITORY>>>>>>>>>>");
		customerRepository.save(customer);
		System.out.println("<<<<<<<<<Inside Service Save function AFTER REPOSITORY>>>>>>>>>>");
		String routingKey = "CustomerCreated";
		String message = "CustomerCreated";
		// rabbitTemplate.convertAndSend(exchange.getName(), routingKey, message);
		System.out.println("<<<<<<<<<Inside Service Save function BEFORE Convert and send>>>>>>>>>>");
		template.convertAndSend(customer);
		System.out.println("<<<<<<<<<Inside Service Save function AFTER Convert and send>>>>>>>>>>");
		return customer;

	}

}
