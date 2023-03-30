package com.zerobase.commerce.user.client.service;

import feign.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailSendServiceTest {

	@Autowired
	private EmailSendService emailSendService;

	@Test
	public void EmailTest() {
		Response response = emailSendService.sendEmail();
		System.out.println(response);
	}
}