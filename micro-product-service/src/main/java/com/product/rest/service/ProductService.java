package com.product.rest.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.product.rest.common.exceptions.RecordNotFoundException;
import com.product.rest.common.messages.BaseResponse;
import com.product.rest.common.messages.CustomMessage;
import com.product.rest.common.utils.Topic;
import com.product.rest.dto.ProductDTO;
import com.product.rest.entity.ProductEntity;
import com.product.rest.repo.ProductRepo;

@Service
@Transactional
public class ProductService {

	@Autowired
	private ProductRepo productRepo;

	public List<ProductDTO> findProductList() {
		return productRepo.findAll().stream().map(this::copyProductEntityToDto).collect(Collectors.toList());
	}

	public ProductDTO findByProductId(Long productId) {
		ProductEntity productEntity = productRepo.findById(productId)
				.orElseThrow(() -> new RecordNotFoundException("product id '" + productId + "' does not exist !"));
		return copyProductEntityToDto(productEntity);
	}

	public BaseResponse createOrUpdateProduct(ProductDTO productDTO) {
		ProductEntity productEntity = copyProductDtoToEntity(productDTO);
		productRepo.save(productEntity);
		return new BaseResponse(Topic.PRODUCT.getName() + CustomMessage.SAVE_SUCCESS_MESSAGE, HttpStatus.CREATED.value());
	}

	public BaseResponse deleteProduct(Long prodId) {
		if (productRepo.existsById(prodId)) {
			productRepo.deleteById(prodId);
		} else {
			throw new RecordNotFoundException("No record found for given id: " + prodId);
		}
		return new BaseResponse(Topic.PRODUCT.getName() + CustomMessage.DELETE_SUCCESS_MESSAGE, HttpStatus.OK.value());
	}

	private ProductDTO copyProductEntityToDto(ProductEntity productEntity) {
		ProductDTO productDTO = new ProductDTO();
		BeanUtils.copyProperties(productEntity, productDTO);
		return productDTO;
	}

	private ProductEntity copyProductDtoToEntity(ProductDTO ProductDTO) {
		ProductEntity productEntity = new ProductEntity();
		BeanUtils.copyProperties(ProductDTO, productEntity);
		return productEntity;
	}

}
