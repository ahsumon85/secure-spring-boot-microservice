package com.ahasan.item.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.ahasan.item.common.messages.BaseResponse;
import com.ahasan.item.dto.ItemDTO;
import com.ahasan.item.dto.SalesDTO;
import com.ahasan.item.service.ItemService;

@Validated
@RestController
@RequestMapping("/item")
public class ItemController {

	@Autowired
	private ItemService itemService;

	@Autowired
	private RestTemplate restTemplate;

	@GetMapping(value = "/find")
	public ResponseEntity<List<ItemDTO>> getAllItems() {
		List<ItemDTO> list = itemService.findItemList();
		return new ResponseEntity<List<ItemDTO>>(list, HttpStatus.OK);
	}

	@GetMapping(value = "/find/by-id")
	public ResponseEntity<ItemDTO> getItemById(@RequestParam Long id) {
		ItemDTO list = new ItemDTO();
		SalesDTO salesDTO = new SalesDTO();
		try {
			String url = "http://sales-server/sales-api/sales/find/name/by-id?id=" + id;
			ResponseEntity<SalesDTO> response = restTemplate.getForEntity(url, SalesDTO.class);
			salesDTO = response.getBody();
		} catch (Exception e) {
			System.out.println(e);
		}
		list = itemService.findByItemId(id);
		list.setSales(salesDTO.getPrice());
		return new ResponseEntity<ItemDTO>(list, HttpStatus.OK);
	}

	@PostMapping(value = { "/add", "/update" })
	public ResponseEntity<BaseResponse> createOrUpdateItem(@Valid @RequestBody ItemDTO productDTO) {
		BaseResponse response = itemService.createOrUpdateItem(productDTO);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<BaseResponse> deleteItemById(@PathVariable("id") Long id) {
		BaseResponse response = itemService.deleteItem(id);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
