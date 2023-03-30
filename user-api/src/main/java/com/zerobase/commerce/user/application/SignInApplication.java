package com.zerobase.commerce.user.application;

import com.zerobase.commerce.domain.config.JwtAuthenticationProvider;
import com.zerobase.commerce.domain.domain.common.UserType;
import com.zerobase.commerce.user.domain.SignInForm;
import com.zerobase.commerce.user.domain.model.Customer;
import com.zerobase.commerce.user.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignInApplication {

	private final CustomerService customerService;
	private final JwtAuthenticationProvider jwtAuthenticationProvider;

	public String customerLogInToken(SignInForm form) {

		Customer customer = customerService.findValidCustomer(form.getEmail(), form.getPassword());

		return jwtAuthenticationProvider.createToken(customer.getEmail(), customer.getId(),
			UserType.CUSTOMER);

	}

}
