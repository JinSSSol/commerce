package com.zerobase.commerce.order.domain.product;

import java.util.List;
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
public class UpdateProductForm {
	private Long id;
	private String name;
	private String description;
	private List<UpdateProductItemForm> items;

}
