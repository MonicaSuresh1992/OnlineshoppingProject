package com.online.salesorderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online.salesorderservice.entiry.SalesOrder;

public interface SalesOrderServiceRepository extends JpaRepository<SalesOrder, Long>{


}
