package com.zerobase.commerce.order.service;

import static com.zerobase.commerce.order.exception.ErrorCode.NOT_FOUND_PRODUCT;
import static com.zerobase.commerce.order.exception.ErrorCode.SAME_ITEM_NAME;

import com.zerobase.commerce.order.domain.model.Product;
import com.zerobase.commerce.order.domain.model.ProductItem;
import com.zerobase.commerce.order.domain.model.product.AddProductItemForm;
import com.zerobase.commerce.order.domain.repository.ProductItemRepository;
import com.zerobase.commerce.order.domain.repository.ProductRepository;
import com.zerobase.commerce.order.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductItemService {

	private final ProductRepository productRepository;
	private final ProductItemRepository productItemRepository;

	@Transactional
	public Product addProductItem(Long sellerId, AddProductItemForm form) {
		Product product = productRepository.findBySellerIdAndId(sellerId, form.getProductId())
			.orElseThrow(() -> new CustomException(NOT_FOUND_PRODUCT));

		if (product.getProductItems().stream()
			.anyMatch(item -> item.getName().equals(form.getName()))) {
			throw new CustomException(SAME_ITEM_NAME);
		}

		ProductItem productItem = ProductItem.of(sellerId, form);
		product.getProductItems().add(productItem);
		return product;
	}

}
