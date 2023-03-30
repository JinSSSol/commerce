package com.zerobase.commerce.user.service;

import com.zerobase.commerce.user.domain.SignUpForm;
import com.zerobase.commerce.user.domain.model.Customer;
import com.zerobase.commerce.user.service.customer.SignUpCustomerService;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SignUpCustomerServiceTest {

	@Autowired
	private SignUpCustomerService service;

	@Test
	void signUp() {
		SignUpForm form = SignUpForm.builder()
			.name("name")
			.birth(LocalDate.now())
			.email("zero1@gmail.com")
			.password("1")
			.phone("010-0000-0000")
			.build();
		Customer c = service.signUp(form);
		Assertions.assertNotNull(service.signUp(form).getId());

	}


}
