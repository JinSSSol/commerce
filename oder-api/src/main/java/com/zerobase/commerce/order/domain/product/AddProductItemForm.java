package com.zerobase.commerce.order.domain.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddProductItemForm {
	private Long productId;
	private String name;
	private Integer price;
	private Integer count;

}
