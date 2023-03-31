package com.zerobase.commerce.user.service.seller;

import static com.zerobase.commerce.user.exception.ErrorCode.LOGIN_CHECK_FAIL;
import static com.zerobase.commerce.user.exception.ErrorCode.NOT_FOUND_USER;

import com.zerobase.commerce.user.domain.SignUpForm;
import com.zerobase.commerce.user.domain.model.Seller;
import com.zerobase.commerce.user.domain.repository.SellerRepository;
import com.zerobase.commerce.user.exception.CustomException;
import com.zerobase.commerce.user.exception.ErrorCode;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SellerService {

	private final SellerRepository sellerRepository;

	public Seller findByIdAndEmail(Long id, String email) {
		return sellerRepository.findByIdAndEmail(id, email)
			.orElseThrow(() -> new CustomException(NOT_FOUND_USER));
	}

	public Seller findValidCustomer(String email, String password) {
		return sellerRepository.findByEmailAndPassword(email, password)
			.filter(Seller::isVerify)
			.orElseThrow(() -> new CustomException(LOGIN_CHECK_FAIL));
	}

	public Seller signUp(SignUpForm form) {

		return sellerRepository.save(Seller.from(form));
	}

	public boolean isEmailExist(String email) {
		return sellerRepository.findByEmail(email)
			.isPresent();
	}

	@Transactional
	public void verifyEmail(String email, String code) {
		Seller seller = sellerRepository.findByEmail(email)
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
		if (seller.isVerify()) {
			throw new CustomException(ErrorCode.ALREADY_VERIFY);
		} else if (!seller.getVerificationCode().equals(code)) {
			throw new CustomException(ErrorCode.WRONG_VERIFICATION);
		} else if (seller.getVerifyExpiredAt().isBefore(LocalDateTime.now())) {
			throw new CustomException(ErrorCode.EXPIRE_CODE);
		}
		seller.setVerify(true);
	}

	@Transactional
	public LocalDateTime ChangeCustomerValidateEmail(Long sellerId, String verificationCode) {
		Seller seller = sellerRepository.findById(sellerId)
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

		seller.setVerificationCode(verificationCode);
		seller.setVerifyExpiredAt(LocalDateTime.now().plusDays(1));
		return seller.getVerifyExpiredAt();
	}

}
