package com.zerobase.commerce.user.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CustomerDto {

	private Long id;
	private String email;

	public static CustomerDto from(Customer customer) {
		return new CustomerDto(customer.getId(), customer.getEmail());
	}

}
