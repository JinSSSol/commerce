package com.zerobase.commerce.user.domain.repository;

import com.zerobase.commerce.user.domain.model.CustomerBalanceHistory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerBalanceHistoryRepository extends
	JpaRepository<CustomerBalanceHistory, Long> {

	Optional<CustomerBalanceHistory> findFirstByCustomer_IdOrderByIdDesc(Long customerId);
}
