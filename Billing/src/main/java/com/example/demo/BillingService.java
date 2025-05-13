package com.example.demo;

import java.util.List;
import java.util.Optional;

import com.example.demo.enums.BillingStatus;
import com.example.demo.exceptions.BillingAlreadyExistsException;
import com.example.demo.exceptions.BillingNotFoundException;
import com.example.demo.exceptions.InValidBillingException;

public interface BillingService {

	Billing generateBill(Billing billing) throws BillingAlreadyExistsException, InValidBillingException;

	Billing updateBill(Long id, Billing billingDetails) throws BillingNotFoundException, BillingAlreadyExistsException;

	void deleteBill(Long id) throws BillingNotFoundException;

	Optional<Billing> getBillById(Long id) throws BillingNotFoundException;

	List<Billing> getAllBills();

	List<Billing> getBillsByStatus(BillingStatus status);

	List<Billing> getBillsByAccountId(String accountId);

}
