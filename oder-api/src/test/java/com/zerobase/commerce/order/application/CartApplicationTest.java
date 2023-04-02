package com.zerobase.commerce.order.application;

import static org.junit.jupiter.api.Assertions.*;

import com.zerobase.commerce.order.domain.model.Product;
import com.zerobase.commerce.order.domain.product.AddProductCartForm;
import com.zerobase.commerce.order.domain.product.AddProductForm;
import com.zerobase.commerce.order.domain.product.AddProductItemForm;
import com.zerobase.commerce.order.domain.redis.Cart;
import com.zerobase.commerce.order.domain.repository.ProductRepository;
import com.zerobase.commerce.order.service.ProductService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class CartApplicationTest {
	@Autowired
	private CartApplication cartApplication;
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductRepository productRepository;

	@Test
	void ADD_AND_REFRESH_TEST() {
		Long customerId = 1L;

		cartApplication.clearCart(customerId);
		Product p = add_product();
		Product result = productRepository.findById(p.getId()).get();

		assertNotNull(result);

		assertEquals(result.getName(), "나이키");
		assertEquals(result.getDescription(), "신발입니다.");
		assertEquals(result.getProductItems().size(), 3);
		assertEquals(result.getProductItems().get(0).getName(), p.getName() + 0);
		assertEquals(result.getProductItems().get(0).getPrice(), 10000);
		assertEquals(result.getProductItems().get(0).getCount(), 10);

		// 카트 생성
		Cart cart = cartApplication.addCart(customerId, makeAddForm(result));
		assertEquals(cart.getMessages().size(), 0);

		// refresh 카트
		cart = cartApplication.getRefreshCart(customerId);
		assertEquals(cart.getMessages().size(), 1);

		// get 카트
		cart = cartApplication.getCart(customerId);
		assertEquals(cart.getMessages().size(), 0);
	}

	AddProductCartForm makeAddForm(Product p) {
		AddProductCartForm.ProductItem productItem =
			AddProductCartForm.ProductItem.builder()
				.id(p.getProductItems().get(0).getId())
				.name(p.getProductItems().get(0).getName())
				.count(5)
				.price(200000)
				.build();
		AddProductCartForm addProductCartForm =
			AddProductCartForm.builder()
				.id(p.getId())
				.sellerId(p.getSellerId())
				.name(p.getName())
				.description(p.getDescription())
				.items(List.of(productItem)).build();

		return addProductCartForm;
	}

	Product add_product() {
		Long sellerId = 1L;
		AddProductForm form = makeProductForm("나이키", "신발입니다.", 3);
		return productService.addProduct(sellerId, form);
	}

	public static AddProductForm makeProductForm(String name, String description, int itemCount) {
		List<AddProductItemForm> itemForms = new ArrayList<>();
		for (int i = 0; i < itemCount; i++) {
			itemForms.add(makeProductItemForm(null, name + i));
		}

		return AddProductForm.builder()
			.name(name)
			.description(description)
			.items(itemForms)
			.build();

	}

	public static AddProductItemForm makeProductItemForm(Long productId, String name) {
		return AddProductItemForm.builder()
			.productId(productId)
			.name(name)
			.price(10000)
			.count(10)
			.build();
	}
}