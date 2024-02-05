package com.springboot.LearnX.ExternalService;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.springboot.LearnX.DTO.AddressDTO;

@FeignClient(name = "LEARNX-ADDRESS-SERVICE")
public interface AddressService {

	@GetMapping("/LearnXAddressService/api/v1/addresses/{id}")
	AddressDTO getAddress(@PathVariable("id") int addressId);

}
