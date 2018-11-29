package com.online.customer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.online.customer.domain.Customer;
import com.online.customer.service.CustomerService;

@RestController
public class CustomerController {

	@Autowired
	CustomerService customerService;

	@GetMapping("/customers")
	public List<Customer> getAllCustomers(){
		return customerService.getAllCustomers();

	}
	@PostMapping("/customer")
	 public ResponseEntity<?> add(@RequestBody Customer customer) {
		System.out.println("<<<<<<<<<Inside Controller Save function BEFOREEE>>>>>>>>>>");
        Customer cust = customerService.save(customer);
        System.out.println("<<<<<<<<<Inside Controller Save function AFTER>>>>>>>>>>");
        assert cust != null;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder
                .fromCurrentRequest().path("/" + cust.getId())
                .buildAndExpand().toUri());

        return new ResponseEntity<>(cust, httpHeaders, HttpStatus.CREATED);
    }


}
