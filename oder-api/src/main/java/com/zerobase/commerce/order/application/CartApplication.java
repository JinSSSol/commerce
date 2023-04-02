package com.zerobase.commerce.order.application;

import static com.zerobase.commerce.order.exception.ErrorCode.ITEM_COUNT_NOT_ENOUGH;
import static com.zerobase.commerce.order.exception.ErrorCode.NOT_FOUND_PRODUCT;

import com.zerobase.commerce.order.domain.model.Product;
import com.zerobase.commerce.order.domain.product.AddProductCartForm;
import com.zerobase.commerce.order.domain.redis.Cart;
import com.zerobase.commerce.order.exception.CustomException;
import com.zerobase.commerce.order.service.CartService;
import com.zerobase.commerce.order.service.ProductSearchService;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartApplication {
	private final ProductSearchService productSearchService;
	private final CartService cartService;

	public Cart addCart(Long customerId, AddProductCartForm form) {

		Product product = productSearchService.getByProductId(form.getId());
		if (product == null) {
			throw new CustomException(NOT_FOUND_PRODUCT);
		}
		Cart cart = cartService.getCart(customerId);

		if (cart != null && !addAble(cart, product, form)) {
			throw new CustomException(ITEM_COUNT_NOT_ENOUGH);

		}

		return cartService.addCart(customerId, form);
	}

	private boolean addAble(Cart cart, Product product, AddProductCartForm form) {
		Cart.Product cartProduct = cart.getProducts().stream().filter(p -> p.getId().equals(form.getId()))
			.findFirst().orElseThrow(() -> new CustomException(NOT_FOUND_PRODUCT));

		Map<Long, Integer> cartItemCountMap = cartProduct.getItems().stream()
			.collect(Collectors.toMap(Cart.ProductItem::getId, Cart.ProductItem::getCount));

		Map<Long, Integer> currentItemCountMap = product.getProductItems().stream()
				.collect(Collectors.toMap(com.zerobase.commerce.order.domain.model.ProductItem::getId,
					com.zerobase.commerce.order.domain.model.ProductItem::getCount));

		return form.getItems().stream().noneMatch(
			formItem -> {
				Integer cartCount = cartItemCountMap.get(formItem.getId());
				Integer currentCount = currentItemCountMap.get(formItem.getId());
				return formItem.getCount() + cartCount > currentCount;
			});
	}

}
