package com.zerobase.commerce.user.controller;

import com.zerobase.commerce.domain.config.JwtAuthenticationProvider;
import com.zerobase.commerce.domain.domain.common.UserVo;
import com.zerobase.commerce.user.domain.model.Customer;
import com.zerobase.commerce.user.domain.model.CustomerDto;
import com.zerobase.commerce.user.service.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

	private final JwtAuthenticationProvider jwtAuthenticationProvider;
	private final CustomerService customerService;
	@GetMapping("/getInfo")
	public ResponseEntity<CustomerDto> getInfo(@RequestHeader(name = "X_AUTH_TOKEN") String token) {
		UserVo userVo = jwtAuthenticationProvider.getUserVo(token);
		Customer customer = customerService.findByIdAndEmail(userVo.getId(), userVo.getEmail());

		return ResponseEntity.ok(CustomerDto.from(customer));
	}
}
