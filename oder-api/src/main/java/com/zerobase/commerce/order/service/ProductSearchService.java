package com.zerobase.commerce.order.service;

import static com.zerobase.commerce.order.exception.ErrorCode.NOT_FOUND_PRODUCT;

import com.zerobase.commerce.order.domain.model.Product;
import com.zerobase.commerce.order.domain.repository.ProductRepository;
import com.zerobase.commerce.order.exception.CustomException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductSearchService {

	private final ProductRepository productRepository;

	public List<Product> searchByName(String name) {
		return productRepository.searchByName(name);
	}
	public Product getByProductId(Long productId) {
		return productRepository.findById(productId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_PRODUCT));
	}

	public List<Product> getListByProductIds(List<Long> productIds) {
		return productRepository.findAllById(productIds);
	}

}
