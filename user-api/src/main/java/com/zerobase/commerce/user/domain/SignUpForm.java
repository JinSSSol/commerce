package com.zerobase.commerce.user.domain;

import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SignUpForm {

	@NotBlank(message = "이메일은 필수 값입니다.")
	@Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식이 아닙니다.")
	private String email;

	@NotBlank(message = "이름은 필수 값입니다.")
	private String name;

	@NotBlank(message = "비밀번호는 필수 값입니다.")
	@Size(min = 8, message = "비밀번호는 8자 이상부터 가능합니다.")
	private String password;
	private LocalDate birth;
	private String phone;

}
