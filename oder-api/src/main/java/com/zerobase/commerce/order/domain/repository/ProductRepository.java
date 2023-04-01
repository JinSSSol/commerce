package com.zerobase.commerce.order.domain.repository;

import com.zerobase.commerce.order.domain.model.Product;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

	Optional<Product> findBySellerIdAndId(Long sellerId, Long id);

}
