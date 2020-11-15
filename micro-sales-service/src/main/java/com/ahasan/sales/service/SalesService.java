package com.ahasan.sales.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ahasan.sales.common.exceptions.RecordNotFoundException;
import com.ahasan.sales.common.messages.BaseResponse;
import com.ahasan.sales.common.messages.CustomMessage;
import com.ahasan.sales.common.utils.Topic;
import com.ahasan.sales.dto.SalesDTO;
import com.ahasan.sales.entity.SalesEntity;
import com.ahasan.sales.repo.SalesRepo;

@Service
@Transactional
public class SalesService {

	@Autowired
	private SalesRepo salesRepo;

	public List<SalesDTO> findSalesList() {
		return salesRepo.findAll().stream().map(this::copySalesEntityToDto).collect(Collectors.toList());
	}

	public SalesDTO findBySalesId(Long salesId) {
		SalesEntity userEntity = salesRepo.findById(salesId)
				.orElseThrow(() -> new RecordNotFoundException("Sales id '" + salesId + "' does not exist !"));
		return copySalesEntityToDto(userEntity);
	}

	public BaseResponse createOrUpdateSales(SalesDTO salesDTO) {
		SalesEntity salesEntity = copySalesDtoToEntity(salesDTO);
		salesRepo.save(salesEntity);
		return new BaseResponse(Topic.Sales.getName() + CustomMessage.SAVE_SUCCESS_MESSAGE, HttpStatus.CREATED.value());

	}

	public void updateSales(SalesDTO salesDTO) {
		SalesEntity salesEntity = copySalesDtoToEntity(salesDTO);
		salesRepo.save(salesEntity);
	}

	public BaseResponse deleteSales(Long salesId) {
		if (salesRepo.existsById(salesId)) {
			salesRepo.deleteById(salesId);
		} else {
			throw new RecordNotFoundException("No record found for given id: " + salesId);
		}
		return new BaseResponse(Topic.Sales.getName() + CustomMessage.DELETE_SUCCESS_MESSAGE, HttpStatus.OK.value());
	}


	private SalesDTO copySalesEntityToDto(SalesEntity salesEntity) {
		SalesDTO salesDTO = new SalesDTO();
		BeanUtils.copyProperties(salesEntity, salesDTO);
		return salesDTO;
	}

	private SalesEntity copySalesDtoToEntity(SalesDTO salesDTO) {
		SalesEntity userEntity = new SalesEntity();
		BeanUtils.copyProperties(salesDTO, userEntity);
		return userEntity;
	}

}
