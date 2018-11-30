package com.online.salesorderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online.salesorderservice.entiry.OrderLineItem;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long>{

}
