package com.online.salesorderservice.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.online.salesorderservice.domain.SalesOrderDetails;

@Repository
public class SalesOrderServiceRepository {

	private final JdbcTemplate jdbcTemplate;

	private final String INSERT_ORDER = "insert into Order_458882(order_date,cust_id,order_desc,total_price) values(?,?,?,?)";

	@Autowired
	public SalesOrderServiceRepository(JdbcTemplate jdbcTemplate){
		this.jdbcTemplate = jdbcTemplate;
	}
	public Integer save(SalesOrderDetails salesOrderDetails) {
		
		assert salesOrderDetails.getOrderDescription()!= null;
		assert salesOrderDetails.getOrderDate()!= null;
		assert salesOrderDetails.getCustId()!= null;
		assert salesOrderDetails.getItemList()!= null;
		
		this.jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(INSERT_ORDER);
			
			ps.setString(2, salesOrderDetails.getOrderDate());
			ps.setInt(3, salesOrderDetails.getCustId());
			ps.setString(4, salesOrderDetails.getOrderDescription());
			ps.setDouble(5, salesOrderDetails.getTotalPrice());
			
//			ResultSet rs=ps.getGeneratedKeys();
//		    if (rs.next()) {
//		    	salesOrderDetails.setOrderId(rs.getInt(1));   
//		               System.out.println("Auto Generated Primary Key " + salesOrderDetails.getOrderId()); 
//		    }
			return ps;
		});

		return salesOrderDetails.getOrderId();
	}




}
