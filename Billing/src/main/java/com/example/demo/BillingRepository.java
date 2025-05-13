package com.example.demo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.enums.BillingStatus;

@Repository
public interface BillingRepository extends JpaRepository<Billing, Long>{

	List<Billing> findByStatus(BillingStatus status);
	
	List<Billing> findByAccountId(String accountId);

	List<Billing> findByBillingDateBetween(LocalDate startDate, LocalDate endDate);

	List<Billing> findByAccountIdAndStatus(String accountId, BillingStatus status);

}
