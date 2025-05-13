package com.example.demo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.enums.BillingStatus;
import com.example.demo.exceptions.BillingAlreadyExistsException;
import com.example.demo.exceptions.BillingNotFoundException;
import com.example.demo.exceptions.InValidBillingException;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class BillingServiceImpl implements BillingService {

	private static final Logger logger = LoggerFactory.getLogger(BillingServiceImpl.class);

	private final BillingRepository billingRepository;

	@Autowired
	public BillingServiceImpl(BillingRepository billingRepository) {
		this.billingRepository = billingRepository;
	}

	@CircuitBreaker(name = "billingService", fallbackMethod = "generateBillFallback")
	@Retry(name = "billingService", fallbackMethod = "generateBillFallback")
	@RateLimiter(name = "billingService", fallbackMethod = "generateBillFallback")
	@Override
	@Transactional
	public Billing generateBill(Billing billing) throws BillingAlreadyExistsException, InValidBillingException {
		logger.info("Attempting to generate a new Bill: {}", billing);

		if (billing == null) {
			throw new InValidBillingException("Billing is null.");
		}

		if (billing.getAccountId() == null || billing.getAccountId().trim().isEmpty()) {
			throw new InValidBillingException("Billing account Id is empty.");
		}

		String normalizedAccountId = billing.getAccountId().trim().toLowerCase();
		billing.setAccountId(normalizedAccountId);

		List<Billing> existingBill = billingRepository.findByAccountId(normalizedAccountId);
		if (!existingBill.isEmpty()) {
			throw new BillingAlreadyExistsException(
					String.format("Billing with account Id %s already exists.", normalizedAccountId));
		}

		return billingRepository.save(billing);
	}

	public Billing generateBillFallback(Billing billing, Throwable throwable) {
		logger.error("Fallback for generateBill() triggered due to: {}", throwable.toString());

		Billing fallbackBill = new Billing();
		fallbackBill.setDescription("Fallback - Billing not processed");
		fallbackBill.setAccountId("N/A");
		fallbackBill.setAmount(BigDecimal.ZERO);
		fallbackBill.setBillingCycle("N/A");
		fallbackBill.setBillingDate(LocalDate.now());
		fallbackBill.setStatus(BillingStatus.PENDING);

		return fallbackBill;
	}

	@Override
	public Billing updateBill(Long id, Billing billingDetails)
			throws BillingNotFoundException, BillingAlreadyExistsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteBill(Long id) throws BillingNotFoundException {
		// TODO Auto-generated method stub

	}

	@Override
	public Optional<Billing> getBillById(Long id) throws BillingNotFoundException {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public List<Billing> getAllBills() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Billing> getBillsByStatus(BillingStatus status) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Billing> getBillsByAccountId(String accountId) {
		// TODO Auto-generated method stub
		return null;
	}

}
