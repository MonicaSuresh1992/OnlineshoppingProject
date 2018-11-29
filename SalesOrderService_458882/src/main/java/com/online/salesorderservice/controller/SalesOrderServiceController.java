package com.online.salesorderservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.online.salesorderservice.domain.SalesOrderDetails;
import com.online.salesorderservice.repository.SalesOrderServiceRepository;

@Controller
public class SalesOrderServiceController {
	
	@Autowired
	SalesOrderServiceRepository salesOrderServiceRepository;
	
	@PostMapping("/orders")
	 public ResponseEntity<?> add(@RequestBody SalesOrderDetails salesOrderDetails) {
		
		//Code to be inserted	
		
	   Integer orderId = salesOrderServiceRepository.save(salesOrderDetails);
       assert orderId != null;

       HttpHeaders httpHeaders = new HttpHeaders();
       httpHeaders.setLocation(ServletUriComponentsBuilder
               .fromCurrentRequest().path("/" + orderId)
               .buildAndExpand().toUri());

       return new ResponseEntity<>(orderId, httpHeaders, HttpStatus.CREATED);
   }

}
