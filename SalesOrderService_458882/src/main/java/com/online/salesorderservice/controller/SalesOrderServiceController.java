package com.online.salesorderservice.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.online.salesorderservice.domain.Customer;
import com.online.salesorderservice.domain.Item;
import com.online.salesorderservice.domain.SalesOrderDetails;
import com.online.salesorderservice.entiry.CustomerSOS;
import com.online.salesorderservice.entiry.OrderLineItem;
import com.online.salesorderservice.entiry.SalesOrder;
import com.online.salesorderservice.repository.CustomerSOSRepository;
import com.online.salesorderservice.repository.OrderLineItemRepository;
import com.online.salesorderservice.repository.SalesOrderServiceRepository;

@RestController
public class SalesOrderServiceController {

	@Autowired
	SalesOrderServiceRepository salesOrderServiceRepository;

	@Autowired
	OrderLineItemRepository orderLineItemRepository;

	@Autowired
	CustomerSOSRepository customerSOSRepository;

	@Autowired
	private LoadBalancerClient loadBalancerClient;

	@PostMapping("/orders")
	@HystrixCommand(fallbackMethod = "customerOrItemFallBack")
	public String add(@RequestBody SalesOrderDetails salesOrderDetails) throws Exception {
		System.out.println("Inside Controller getOrderDesc >>>>" + salesOrderDetails.getOrderDescription());

		//Validate customer by verifying the table “customer_sos” with cust_id --- Starts
		boolean custExist;
		List<CustomerSOS> customerSOSList = customerSOSRepository.findAll();
		if (customerSOSList != null && customerSOSList.size() > 0){

			for (int ii=0 ; ii < customerSOSList.size() ; ii++) {
				System.out.println("customerSOSList.get(ii).getCustId()" + customerSOSList.get(ii).getCustId());
				System.out.println("salesOrderDetails.getCustId()" + salesOrderDetails.getCustId());
				if (customerSOSList.get(ii).getCustId() != null && salesOrderDetails.getCustId() != null){
					if (customerSOSList.get(ii).getCustId().equals(salesOrderDetails.getCustId())) {
						custExist = true;
					} else {

						System.out.println("<<<<<<<<<Customer not available 1111>>>>");
						salesOrderDetails.setWrongData("WrongCustId");
						throw new Exception();
					}
				} else {
					//Customer not available -- Exception to be thrown
					System.out.println("<<<<<<<<<Customer not available 22222>>>>");
					salesOrderDetails.setWrongData("WrongCustId");
					throw new Exception();
				}

			}
		}	else {
			salesOrderDetails.setWrongData("WrongCustId");
			throw new Exception();
		}

		//Validate customer by verifying the table “customer_sos” with cust_id --- Ends

		// REST call to validate items by calling item service with item name -- itemByName ---Starts 
		System.out.println("Before REST CALL >>>>");		
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Item> response = null;

		String itemName = "";
		double totalPrice = 0.0;
		Long orderId = 0L;
		List <Item> fetchedItemList = null;

		if (salesOrderDetails.getItemNameList() != null && salesOrderDetails.getItemNameList().size() >0 ) {
			fetchedItemList = new ArrayList<Item>();
			Item item = null;
			for (int i=0 ; i < salesOrderDetails.getItemNameList().size() ; i++) {
				item = new Item();
				itemName = salesOrderDetails.getItemNameList().get(i).getItemName();
				item.setItemName(itemName);
				System.out.println("<<<<<<<<<Item Name >>>"+itemName);
				response = restTemplate.getForEntity(fetchItemServiceUrl() + "/items/"+itemName, Item.class);
				System.out.println("After REST CALL >>>>"+response.getBody());
				itemName = response.getBody().getItemName();
				totalPrice = totalPrice + response.getBody().getItemPrice();
				fetchedItemList.add(item);
				if(itemName.equals(null) || itemName.equals("")){
					//Item details not available -- Exception to be thrown
					System.out.println("<<<<<<<<<Item details not available 1111111 >>>");
					salesOrderDetails.setWrongData("WrongItemName");
				}
			}
		} else {
			System.out.println("<<<<<<<<<Item details not available 1111111 >>>");
			salesOrderDetails.setWrongData("WrongItemName");
			//Item details not available -- Exception to be thrown
		}

		// REST call to validate items by calling item service with item name -- itemByName --- Ends

		// create order by inserting the order details in sales_order table --- Starts
		if(salesOrderDetails != null ) {

			SalesOrder salesOrder = new SalesOrder();
			if (salesOrderDetails.getOrderDate() != null ) {
				salesOrder.setOrderDate(salesOrderDetails.getOrderDate());
			}
			if (salesOrderDetails.getCustId() != null ) {
				salesOrder.setCustId(salesOrderDetails.getCustId() );
			}
			if (salesOrderDetails.getOrderDescription() != null ) {
				salesOrder.setOrderDesc(salesOrderDetails.getOrderDescription() );
			}
			if (totalPrice != 0.0 ) {
				salesOrder.setTotalPrice(totalPrice);
			}

			salesOrder = salesOrderServiceRepository.save(salesOrder);
			orderId = salesOrder.getOrderId();
			// create order by inserting the order details in sales_order table --- Ends

			assert orderId != null;

			// create order line by inserting the order details in order_line_item table --- Starts
			if (fetchedItemList != null && fetchedItemList.size() >0 ) {
				for (int i=0 ; i < fetchedItemList.size() ; i++) {
					if (fetchedItemList.get(i).getItemName() != null && fetchedItemList.get(i).getItemName() != ""){
						OrderLineItem orderLineItem = new OrderLineItem();
						orderLineItem.setItemName(fetchedItemList.get(i).getItemName());
						orderLineItem.setItemQuantity(fetchedItemList.size());// Check
						orderLineItem.setOrderId(orderId);

						orderLineItemRepository.save(orderLineItem);

					} else {
						//Item details not available -- Exception to be thrown
						System.out.println("<<<<<<<<<Item details not available 1 >>>");
						salesOrderDetails.setWrongData("WrongItemName");
						throw new Exception();
					}
				}
			}

			else {
				//Item details not available -- Exception to be thrown
				System.out.println("<<<<<<<<<Item details not available 2 >>>");
				salesOrderDetails.setWrongData("WrongItemName");
				throw new Exception();
			}
			// create order line by inserting the order details in order_line_item table --- Ends
		}
		System.out.println("<<<<<<<<<Order Id >>>"+orderId.toString());
//		return orderId.toString();

//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setLocation(ServletUriComponentsBuilder
//                .fromCurrentRequest().path("/" +orderId)
//                .buildAndExpand().toUri());

        return orderId.toString();

	}

	public void insertCustomerSOS(Customer customer) {
		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<insertCustomerSOS>>>>>>>>>>>>>>>>>>>>>");
		CustomerSOS customerSOS  = new CustomerSOS();

		customerSOS.setCustId(customer.getId());
		customerSOS.setCustFirstName(customer.getFirstName());
		customerSOS.setCustLastName(customer.getLastName());
		customerSOS.setCustEmail(customer.getEmail());

		customerSOSRepository.save(customerSOS);
	}

	// This method is for implementing Ribbon - Client side load balancing
	private String fetchItemServiceUrl() {

		System.out.println("Inside fetchItemServiceUrl");


		ServiceInstance instance = loadBalancerClient.choose("item-service");

		System.out.println("After fetching instance in fetchItemServiceUrl");

		System.out.println("uri: {}"+ instance.getUri().toString());
		System.out.println("serviceId: {}"+ instance.getServiceId());

		return instance.getUri().toString();
	}

	public String customerOrItemFallBack(SalesOrderDetails salesOrderDetails){
		if (salesOrderDetails.getWrongData()!=null && salesOrderDetails.getWrongData().equalsIgnoreCase("WrongCustId")){
			return "Customer Id is not valid. Please enter the correct Id";
		} else {
			return "Iten Name is not valid. Please enter the correct Item";
		}
	}
}

