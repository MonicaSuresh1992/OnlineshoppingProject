package com.online.Item.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.online.Item.domain.Item;
import com.online.Item.repository.ItemRepository;

@RestController
public class ItemController {

	@Autowired
	ItemRepository itemRepository;

	@GetMapping("/items")
	public List<Item> items() {
		return itemRepository.getAllItems();
	}
	@GetMapping("/items/{name}")
	public Item item(@PathVariable("name") String name) {
		return itemRepository.GetItembyName(name);
	}

}
