package com.zerobase.commerce.order.application;

import static com.zerobase.commerce.order.exception.ErrorCode.ORDER_FAIL_CHECK_CART;
import static com.zerobase.commerce.order.exception.ErrorCode.ORDER_FAIL_NO_MONEY;

import com.zerobase.commerce.order.client.MailgunClient;
import com.zerobase.commerce.order.client.UserClient;
import com.zerobase.commerce.order.client.mailgun.SendMailForm;
import com.zerobase.commerce.order.client.user.ChangeBalanceForm;
import com.zerobase.commerce.order.client.user.CustomerDto;
import com.zerobase.commerce.order.domain.model.ProductItem;
import com.zerobase.commerce.order.domain.redis.Cart;
import com.zerobase.commerce.order.exception.CustomException;
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
	private final MailgunClient mailgunClient;
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

		sendOrderMail(token, orderCart);
	}

	private Integer getTotalPrice(Cart cart) {

		return cart.getProducts().stream().flatMapToInt(
			product -> product.getItems().stream().flatMapToInt(
				productItem -> IntStream.of(productItem.getPrice() * productItem.getCount())))
			.sum();
	}

	public void sendOrderMail(String token, Cart orderCart) {
		StringBuilder builder = new StringBuilder();
		builder.append("제로베이스 구매가 완료되었습니다. 구매정보를 확인하세요!\n");
		for (Cart.Product product : orderCart.getProducts()) {
			builder.append("상풍명: " + product.getName() + "\n");

			for (Cart.ProductItem item : product.getItems()) {
				builder.append("옵션명: " + item.getName() + " ");
				builder.append("수량: " + item.getCount() + "\n");
			}
			builder.append("\n\n");
		}

		String message = builder.toString();

		SendMailForm form = SendMailForm.builder()
			.from("Excited User <mailgun@sandboxc062a3d34f4a4aaf96a13a31edb17caa.mailgun.org>")
			.to(userClient.getCustomerInfo(token).getBody().getEmail())
			.subject("제로베이스 결제 완료 !")
			.text(message)
			.build();

		mailgunClient.sendEmail(form);
	}
}
