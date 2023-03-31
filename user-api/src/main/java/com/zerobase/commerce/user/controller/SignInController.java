package com.zerobase.commerce.user.controller;


import com.zerobase.commerce.user.application.SignInApplication;
import com.zerobase.commerce.user.domain.SignInForm;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signIn")
@RequiredArgsConstructor
public class SignInController {

	private final SignInApplication signInApplication;

	@PostMapping("/customer")
	public ResponseEntity<?> signInCustomer(@RequestBody @Valid SignInForm form) {

		return ResponseEntity.ok(signInApplication.customerLogInToken(form));
	}

	@PostMapping("/seller")
	public ResponseEntity<?> signInSeller(@RequestBody @Valid SignInForm form) {

		return ResponseEntity.ok(signInApplication.sellerLogInToken(form));
	}


}
