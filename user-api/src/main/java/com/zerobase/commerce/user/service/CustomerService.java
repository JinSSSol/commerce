package com.zerobase.commerce.user.service;

import static com.zerobase.commerce.user.exception.ErrorCode.LOGIN_CHECK_FAIL;

import com.zerobase.commerce.user.domain.model.Customer;
import com.zerobase.commerce.user.domain.repository.CustomerRepository;
import com.zerobase.commerce.user.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomerService {

	private final CustomerRepository customerRepository;
	public Customer findValidCustomer(String email, String password) {
		return customerRepository.findByEmailAndPassword(email, password)
			.filter(Customer::isVerify)
			.orElseThrow(() -> new CustomException(LOGIN_CHECK_FAIL));
	}

}
