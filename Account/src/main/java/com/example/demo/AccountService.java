package com.example.demo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import com.example.demo.enums.AccountStatus;
import com.example.demo.exceptions.AccountAlreadyExistsException;
import com.example.demo.exceptions.AccountNotFoundException;
import com.example.demo.exceptions.InValidAccountException;

public interface AccountService {

	/**
	 * Create a new account after validation.
	 * 
	 * @throws AccountAlreadyExistsException if accountNumber/email already exists.
	 * @throws InvalidAccountException       if input data is invalid.
	 */
	Account createAccount(Account account) throws AccountAlreadyExistsException, InValidAccountException;

	/**
	 * Update account details using ID.
	 * 
	 * @throws AccountNotFoundException if account ID doesn't exist.
	 * @throws InvalidAccountException  if provided data is invalid.
	 */
	Account updateAccount(String accountId, Account accountDetails)
			throws AccountNotFoundException, InValidAccountException;

	/**
	 * Delete an account by ID.
	 * 
	 * @throws AccountNotFoundException if ID not found.
	 */
	void deleteAccountById(Long id) throws AccountNotFoundException;

	/**
	 * Find accounts by their status (e.g., ACTIVE, SUSPENDED).
	 * 
	 * @throws InvalidAccountException if status is invalid/null.
	 */
	List<Account> findByStatus(AccountStatus accountStatus) throws InValidAccountException;

	/**
	 * Find account using unique account number.
	 * 
	 * @throws AccountNotFoundException if not found.
	 */
	Optional<Account> findByAccountId(String accountId) throws AccountNotFoundException;

	/**
	 * Find account using phone number.
	 * 
	 */
	Account findByPhoneNumber(String phoneNumber);

	/**
	 * Find account using email.
	 * 
	 * @throws AccountNotFoundException if not found.
	 */
	Account findByEmail(String email) throws AccountNotFoundException;

	/**
	 * Get all accounts created after a given date.
	 */
	List<Account> findAccountsCreatedAfter(LocalDateTime createdAt);

	/**
	 * Find accounts by both status and account number.
	 */
	List<Account> findByStatusAndAccountId(AccountStatus accountStatus, String accountId);

	/**
	 * Change the status of an account.
	 * 
	 * @throws AccountNotFoundException if ID not found.
	 */
	Account changeAccountStatus(Long id, AccountStatus accountStatus) throws AccountNotFoundException;

	/**
	 * Retrieve all existing accounts.
	 */
	List<Account> findAllAccounts();

	/**
	 * Get a single account by ID.
	 * 
	 * @throws AccountNotFoundException if not found.
	 */
	Account findAccountById(Long id) throws AccountNotFoundException;
	
	String getWelcomeMessage(String customerName);
}
