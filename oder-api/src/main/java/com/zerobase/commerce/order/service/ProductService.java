package com.zerobase.commerce.order.service;

import static com.zerobase.commerce.order.exception.ErrorCode.NOT_FOUND_ITEM;

import com.zerobase.commerce.order.domain.model.Product;
import com.zerobase.commerce.order.domain.model.ProductItem;
import com.zerobase.commerce.order.domain.model.product.AddProductForm;
import com.zerobase.commerce.order.domain.model.product.UpdateProductForm;
import com.zerobase.commerce.order.domain.model.product.UpdateProductItemForm;
import com.zerobase.commerce.order.domain.repository.ProductRepository;
import com.zerobase.commerce.order.exception.CustomException;
import com.zerobase.commerce.order.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepository productRepository;

	@Transactional
	public Product addProduct(Long sellerId, AddProductForm form) {
		return productRepository.save(Product.of(sellerId, form));
	}

	@Transactional
	public Product updateProduct(Long sellerId, UpdateProductForm form) {
		Product product = productRepository.findBySellerIdAndId(sellerId, form.getId())
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT));

		product.setName(form.getName());
		product.setDescription(form.getDescription());

		for (UpdateProductItemForm itemForm: form.getItems()) {
			ProductItem item = product.getProductItems().stream()
				.filter(pi -> pi.getId().equals(itemForm.getId()))
				.findFirst().orElseThrow(() -> new CustomException(NOT_FOUND_ITEM));

			item.setName(itemForm.getName());
			item.setPrice(itemForm.getPrice());
			item.setCount(itemForm.getCount());
		}

		return product;
	}
}
