package com.zerobase.commerce.user.domain;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SignUpForm {

	private String email;
	private String name;
	private String password;
	private LocalDate birth;
	private String phone;

}
