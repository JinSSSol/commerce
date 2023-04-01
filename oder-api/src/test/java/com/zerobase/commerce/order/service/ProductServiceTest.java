package com.zerobase.commerce.order.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.zerobase.commerce.order.domain.model.Product;
import com.zerobase.commerce.order.domain.model.product.AddProductForm;
import com.zerobase.commerce.order.domain.model.product.AddProductItemForm;
import com.zerobase.commerce.order.domain.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductServiceTest {

	@Autowired
	private ProductService productService;
	@Autowired
	private ProductRepository productRepository;

	@Test
	void ADD_PRODUCT_TEST() {
		Long sellerId = 1L;

		AddProductForm form = makeProductForm("나이키", "신발입니다.", 3);
		Product p = productService.addProduct(sellerId, form);
		Product result = productRepository.findById(p.getId()).get();

		assertNotNull(result);

		assertEquals(p.getName(), "나이키");
		assertEquals(p.getDescription(), "신발입니다.");
		assertEquals(p.getProductItems().size(), 3);
		assertEquals(p.getProductItems().get(0).getName(), p.getName() + 0);
		assertEquals(p.getProductItems().get(0).getPrice(), 10000);
		assertEquals(p.getProductItems().get(0).getCount(), 1);


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
			.count(1)
			.build();
	}

}