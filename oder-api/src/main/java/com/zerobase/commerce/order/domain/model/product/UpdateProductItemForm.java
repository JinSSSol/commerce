package com.zerobase.commerce.order.domain.model.product;

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
public class UpdateProductItemForm {

	private Long id;
	private Long productId;
	private String name;
	private Integer price;
	private Integer count;

}
