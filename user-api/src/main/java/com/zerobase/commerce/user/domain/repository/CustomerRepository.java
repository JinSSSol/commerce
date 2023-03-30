package com.zerobase.commerce.user.domain.repository;

import com.zerobase.commerce.user.domain.model.Customer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	Optional<Customer> findByEmail(String email);
	Optional<Customer> findByEmailAndPassword(String email, String password);

}
