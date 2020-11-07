package com.product.rest.controller;

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

import com.product.rest.common.messages.BaseResponse;
import com.product.rest.dto.ProductDTO;
import com.product.rest.dto.SalesDTO;
import com.product.rest.service.ProductService;

@Validated
@RestController
@RequestMapping("/product")
public class ProductController {

	@Autowired
	private ProductService productService;

	@Autowired
	private RestTemplate restTemplate;

	@GetMapping(value = "/find")
	public ResponseEntity<List<ProductDTO>> getAllProducts() {
		List<ProductDTO> list = productService.findProductList();
		return new ResponseEntity<List<ProductDTO>>(list, HttpStatus.OK);
	}

	@GetMapping(value = "/find/by-id")
	public ResponseEntity<ProductDTO> getProductById(@RequestParam Long id) {
		ProductDTO list = new ProductDTO();
		SalesDTO salesDTO = new SalesDTO();
		try {
			String url = "http://sales-server/sales-api/sales/find/name/by-id?id=" + id;
			ResponseEntity<SalesDTO> response = restTemplate.getForEntity(url, SalesDTO.class);
			salesDTO = response.getBody();
		} catch (Exception e) {
			System.out.println(e);
		}
		list = productService.findByProductId(id);
		list.setSales(salesDTO.getPrice());
		return new ResponseEntity<ProductDTO>(list, HttpStatus.OK);
	}

	@PostMapping(value = { "/add", "/update" })
	public ResponseEntity<BaseResponse> createOrUpdateProduct(@Valid @RequestBody ProductDTO productDTO) {
		BaseResponse response = productService.createOrUpdateProduct(productDTO);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<BaseResponse> deleteProductById(@PathVariable("id") Long id) {
		BaseResponse response = productService.deleteProduct(id);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
