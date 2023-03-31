package com.zerobase.commerce.user.controller;

import com.zerobase.commerce.domain.config.JwtAuthenticationProvider;
import com.zerobase.commerce.domain.domain.common.UserVo;
import com.zerobase.commerce.user.domain.customer.ChangeBalanceForm;
import com.zerobase.commerce.user.domain.customer.CustomerDto;
import com.zerobase.commerce.user.domain.model.Customer;
import com.zerobase.commerce.user.service.customer.CustomerBalanceService;
import com.zerobase.commerce.user.service.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

	private final JwtAuthenticationProvider jwtAuthenticationProvider;
	private final CustomerService customerService;
	private final CustomerBalanceService customerBalanceService;

	@GetMapping("/getInfo")
	public ResponseEntity<CustomerDto> getInfo(@RequestHeader(name = "X_AUTH_TOKEN") String token) {
		UserVo userVo = jwtAuthenticationProvider.getUserVo(token);
		Customer customer = customerService.findByIdAndEmail(userVo.getId(), userVo.getEmail());

		return ResponseEntity.ok(CustomerDto.from(customer));
	}

	@PostMapping("/balance")
	public ResponseEntity<Integer> changeBalance(@RequestHeader(name = "X_AUTH_TOKEN") String token,
		@RequestBody ChangeBalanceForm form) {

		UserVo vo = jwtAuthenticationProvider.getUserVo(token);

		return ResponseEntity.ok(
			customerBalanceService.changeBalance(vo.getId(), form).getCurrentMoney());

	}
}
