package com.zerobase.commerce.user.application;


import com.zerobase.commerce.user.client.MailgunClient;
import com.zerobase.commerce.user.client.mailgun.SendMailForm;
import com.zerobase.commerce.user.domain.SignUpForm;
import com.zerobase.commerce.user.domain.model.Customer;
import com.zerobase.commerce.user.exception.CustomException;
import com.zerobase.commerce.user.exception.ErrorCode;
import com.zerobase.commerce.user.service.SignUpCustomerService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignUpApplication {

	private final MailgunClient mailgunClient;
	private final SignUpCustomerService signUpCustomerService;

	public void customerVerify(String email, String code) {
		signUpCustomerService.verifyEmail(email, code);
	}

	public String customerSignUp(SignUpForm form) {
		if (signUpCustomerService.isEmailExist(form.getEmail())) {
			throw new CustomException(ErrorCode.ALREADY_REGISTER_USER);
		} else {
			Customer customer = signUpCustomerService.signUp(form);
			LocalDateTime now = LocalDateTime.now();

			String code = getRandomCode();
			SendMailForm sendMailForm = SendMailForm.builder()
				.from("Excited User <mailgun@sandboxc062a3d34f4a4aaf96a13a31edb17caa.mailgun.org>")
				.to(customer.getEmail())
				.subject("Verification Email")
				.text(getVerificationEmailBody(customer.getEmail(), customer.getName(), code))
				.build();

			mailgunClient.sendEmail(sendMailForm);
			signUpCustomerService.ChangeCustomerValidateEmail(customer.getId(), code);
			return "회원가입에 성공하였습니다.";
		}
	}

	private String getRandomCode() {
		return RandomStringUtils.random(10, true, true);
	}

	private String getVerificationEmailBody(String email, String name, String code) {
		StringBuilder builder = new StringBuilder();
		return builder.append("hello").append(name)
			.append("! Please Click link for verification.\n\n")
			.append("http://localhost:8080/signup/verify/customer?email=")
			.append(email)
			.append("&code=")
			.append(code).toString();
	}


}
