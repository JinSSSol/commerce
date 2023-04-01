package com.zerobase.commerce.order.domain.model.product;

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
public class AddProductForm {
	private String name;
	private String description;
	private List<AddProductItemForm> items;

}