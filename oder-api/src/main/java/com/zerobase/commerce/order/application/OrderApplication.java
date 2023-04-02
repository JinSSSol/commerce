package com.zerobase.commerce.order.application;

import static com.zerobase.commerce.order.exception.ErrorCode.ORDER_FAIL_CHECK_CART;
import static com.zerobase.commerce.order.exception.ErrorCode.ORDER_FAIL_NO_MONEY;

import com.zerobase.commerce.order.client.UserClient;
import com.zerobase.commerce.order.client.user.ChangeBalanceForm;
import com.zerobase.commerce.order.client.user.CustomerDto;
import com.zerobase.commerce.order.domain.model.ProductItem;
import com.zerobase.commerce.order.domain.redis.Cart;
import com.zerobase.commerce.order.exception.CustomException;
import com.zerobase.commerce.order.exception.ErrorCode;
import com.zerobase.commerce.order.service.ProductItemService;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderApplication {

	private final CartApplication cartApplication;
	private final UserClient userClient;
	private final ProductItemService productItemService;

	@Transactional
	public void order(String token, Cart cart) {
		Cart orderCart = cartApplication.refreshCart(cart);
		if (orderCart.getMessages().size()>0) {
			throw new CustomException(ORDER_FAIL_CHECK_CART);
		}

		CustomerDto customerDto = userClient.getCustomerInfo(token).getBody();

		int totalPrice = getTotalPrice(cart);
		if (customerDto.getBalance() < totalPrice) {
			throw new CustomException(ORDER_FAIL_NO_MONEY);
		}

		userClient.changeBalance(token,
			ChangeBalanceForm.builder()
				.from("USER")
				.message("Order")
				.money(-totalPrice)
				.build());

		for (Cart.Product cartProduct : orderCart.getProducts()) {
			for (Cart.ProductItem cartItem : cartProduct.getItems()) {
				ProductItem productItem = productItemService.getProductItem(cartItem.getId());
				productItem.setCount(productItem.getCount() - cartItem.getCount());
			}
		}
	}

	private Integer getTotalPrice(Cart cart) {

		return cart.getProducts().stream().flatMapToInt(
			product -> product.getItems().stream().flatMapToInt(
				productItem -> IntStream.of(productItem.getPrice() * productItem.getCount())))
			.sum();
	}
}
