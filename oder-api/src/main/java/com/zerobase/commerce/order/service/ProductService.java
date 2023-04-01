package com.zerobase.commerce.order.service;

import com.zerobase.commerce.order.domain.model.Product;
import com.zerobase.commerce.order.domain.model.product.AddProductForm;
import com.zerobase.commerce.order.domain.repository.ProductRepository;
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

}
