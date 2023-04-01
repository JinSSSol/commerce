package com.zerobase.commerce.order.domain.repository;

import com.zerobase.commerce.order.domain.model.Product;
import java.util.List;

public interface ProductRepositoryCustom {

	List<Product> searchByName(String name);
}
