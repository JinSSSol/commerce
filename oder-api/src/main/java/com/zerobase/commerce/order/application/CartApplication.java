package com.zerobase.commerce.order.application;

import static com.zerobase.commerce.order.exception.ErrorCode.ITEM_COUNT_NOT_ENOUGH;
import static com.zerobase.commerce.order.exception.ErrorCode.NOT_FOUND_PRODUCT;

import com.zerobase.commerce.order.domain.model.Product;
import com.zerobase.commerce.order.domain.model.ProductItem;
import com.zerobase.commerce.order.domain.product.AddProductCartForm;
import com.zerobase.commerce.order.domain.redis.Cart;
import com.zerobase.commerce.order.exception.CustomException;
import com.zerobase.commerce.order.service.CartService;
import com.zerobase.commerce.order.service.ProductSearchService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

	// 카트 메시지 조회하기 위함
	public Cart getRefreshCart(Long customerId) {
		return  refreshCart(cartService.getCart(customerId));
	}
	public Cart getCart(Long customerId) {
		Cart cart = getRefreshCart(customerId);
		Cart returnCart = new Cart();
		returnCart.setCustomerId(customerId);
		returnCart.setProducts(cart.getProducts());
		returnCart.setMessages(new ArrayList<>());
		cartService.putCart(customerId, cart);
		return returnCart;
	}

	public void clearCart(Long customerId) {
		cartService.putCart(customerId, null);
	}

	private Cart refreshCart(Cart cart) {
		// 1. 상품이나 상품 아이템 옵션이 변경되는지 체크
		// 2. 그에 맞는 알람 제공 (메시지)
		// 3. 상품의 수량, 가격을 임의로 변경

		// 카트에 등록되어있는 실제 Products
		Map<Long, Product> productMap = productSearchService.getListByProductIds(
			cart.getProducts().stream().map(Cart.Product::getId).collect(Collectors.toList()))
			.stream()
			.collect(Collectors.toMap(Product::getId, product -> product));

		for (int i = 0; i < cart.getProducts().size(); i++) {

			Cart.Product cartProduct = cart.getProducts().get(i);

			Product p = productMap.get(cartProduct.getId());

			// 상품이 삭제 되었을 때
			if (p == null) {
				cart.getProducts().remove(cartProduct);
				i--;
				cart.addMessage(cartProduct.getName()+" 상품이 삭제되었습니다.");
				continue;
			}

			Map<Long, ProductItem> productItemMap = p.getProductItems().stream()
				.collect(Collectors.toMap(ProductItem::getId, productItem -> productItem));

			// 상품의 아이템들 검사
			List<String> tempMessages = new ArrayList<>();
			for (int j = 0; j < cartProduct.getItems().size(); j++) {
				Cart.ProductItem cartProductItem = cartProduct.getItems().get(i);
				ProductItem pi = productItemMap.get(cartProductItem.getId());

				if (pi == null) {
					cartProduct.getItems().remove(cartProductItem);
					j--;
					cart.addMessage(cartProduct.getName()+"옵션이 삭제되었습니다.");
					continue;
				}

				boolean isPriceChanged = false, isCountNotEnough = false;
				if(!cartProductItem.getPrice().equals(pi.getPrice())) {
					isPriceChanged = true;
					cartProductItem.setPrice(pi.getPrice());
				}
				if (cartProductItem.getCount() > pi.getCount()) {
					isCountNotEnough = true;
					cartProductItem.setCount(pi.getCount());
				}
				if (isPriceChanged && isCountNotEnough) {
					tempMessages.add(cartProductItem.getName()+" 가격변동, 수량이 부족하여 구매 가능한 최대치로 변경되었습니다.");
				} else if (isPriceChanged) {
					tempMessages.add(cartProductItem.getName()+" 가격이 변동되었습니다.");
				} else if (isCountNotEnough) {
					tempMessages.add(cartProductItem.getName()+" 수량이 부족하여 구매 가능한 최대치로 변경되었습니다.");
				}

				if (cartProduct.getItems().size() == 0) {
					cart.getProducts().remove(cartProduct);
					i--;
					cart.addMessage(cartProduct.getName()+" 상품의 옵션이 모두 없어져 구매가 불가능합니다.");
					continue;
				}
				else if (tempMessages.size() > 0) {
					StringBuilder builder = new StringBuilder();
					builder.append(cartProduct.getName()+ " 상품의 변동 사항 : ");
					for (String message: tempMessages) {
						builder.append(message);
						builder.append(", ");
					}
					cart.addMessage(builder.toString());
				}

			}
		}
		cartService.putCart(cart.getCustomerId(), cart);
		return cart;
	}
	private boolean addAble(Cart cart, Product product, AddProductCartForm form) {
		Cart.Product cartProduct = cart.getProducts().stream().filter(p -> p.getId().equals(form.getId()))
			.findFirst().orElse(Cart.Product.builder().id(product.getId())
				.items(Collections.emptyList()).build());

		Map<Long, Integer> cartItemCountMap = cartProduct.getItems().stream()
			.collect(Collectors.toMap(Cart.ProductItem::getId, Cart.ProductItem::getCount));

		Map<Long, Integer> currentItemCountMap = product.getProductItems().stream()
				.collect(Collectors.toMap(com.zerobase.commerce.order.domain.model.ProductItem::getId,
					com.zerobase.commerce.order.domain.model.ProductItem::getCount));

		return form.getItems().stream().noneMatch(
			formItem -> {
				Integer cartCount = cartItemCountMap.get(formItem.getId());
				Integer currentCount = currentItemCountMap.get(formItem.getId());
				if (cartCount == null) cartCount = 0;
				return formItem.getCount() + cartCount > currentCount;
			});
	}

}
