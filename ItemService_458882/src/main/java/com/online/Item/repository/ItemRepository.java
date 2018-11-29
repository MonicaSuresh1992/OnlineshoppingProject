package com.online.Item.repository;

import java.sql.ResultSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.online.Item.domain.Item;

@Repository
public class ItemRepository {

	private final JdbcTemplate jdbcTemplate;
	private final String GET_ALL_ITEMS = "Select * from Items_458882";
	private final String GET_ITEM_BY_NAME = "Select * from Items_458882 where name =?";
	
	@Autowired
	public ItemRepository(JdbcTemplate jdbcTemplate){
		
		this.jdbcTemplate =jdbcTemplate;
		
	}


	private final RowMapper<Item> rowMapper = (ResultSet rs, int row) -> {
		Item item = new Item();
		item.setItem_id(rs.getInt("id"));
		item.setItem_name(rs.getString("name"));
		item.setItem_desc(rs.getString("description"));
		item.setItem_price(rs.getDouble("price"));
		return item;
	};

	public List<Item> getAllItems() {

		return this.jdbcTemplate.query(GET_ALL_ITEMS, rowMapper);
	}

	public Item GetItembyName(String name) {
		
		return this.jdbcTemplate.queryForObject(GET_ITEM_BY_NAME, new Object[]{name}, rowMapper);
	}


}

