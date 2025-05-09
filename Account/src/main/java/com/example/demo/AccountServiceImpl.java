package com.example.demo;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.demo.dto.OpenAIMessage;
import com.example.demo.dto.OpenAIRequest;
import com.example.demo.dto.OpenAIResponse;
import com.example.demo.enums.AccountStatus;
import com.example.demo.exceptions.AccountAlreadyExistsException;
import com.example.demo.exceptions.AccountNotFoundException;
import com.example.demo.exceptions.InValidAccountException;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

	private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
	private final AccountRepository accountRepository;
	private final OpenAIClient openAIClient;
	private final WebClient webClient;

	public AccountServiceImpl(AccountRepository accountRepository, OpenAIClient openAIClient, WebClient webClient) {
	    this.accountRepository = accountRepository;
	    this.openAIClient = openAIClient;
		this.webClient = webClient;
	}

	@Override
	@Transactional
	@CircuitBreaker(name = "AccountService", fallbackMethod = "createAccountFallBack")
	@RateLimiter(name = "AccountService")
	@Retry(name = "AccountService")
	public Account createAccount(Account account) throws AccountAlreadyExistsException, InValidAccountException {
		if (account == null) {
			throw new InValidAccountException("Account is null.");
		}

		// Simulate AI check - autocorrect or auto-suggest (simple heuristic)

		if (account.getAccountId() == null || account.getAccountId().trim().isEmpty()) {
			throw new InValidAccountException("AI Suggestion: Account ID is empty. Did you mean to auto-generate?");
		}

		// Check if account already exists
		Optional<Account> existingAccount = accountRepository.findByAccountId(account.getAccountId());
		if (existingAccount.isPresent()) {
			throw new AccountAlreadyExistsException(
					"Account with number " + account.getAccountId() + " already exists.");
		}

		return accountRepository.save(account);
	}

	public Account createAccountFallBack(Account account, Throwable t) {
		logger.error("Fallback triggered while creating account. Reason: {}", t.getMessage());
		return account;
	}

	@Override
	@Transactional
	@CircuitBreaker(name = "AccountService", fallbackMethod = "updateAccountFallback")
	@Retry(name = "AccountService")
	@RateLimiter(name = "AccountService")

	public Account updateAccount(String accountId, Account accountDetails)
			throws AccountNotFoundException, InValidAccountException {

		Account existingAccount = accountRepository.findByAccountId(accountId)
				.orElseThrow(() -> new AccountNotFoundException("Account not found with accountId: " + accountId));

		Optional.ofNullable(accountDetails.getAccountStatus()).ifPresent(existingAccount::setAccountStatus);

		Optional.ofNullable(accountDetails.getCustomerName()).ifPresent(existingAccount::setCustomerName);

		Optional.ofNullable(accountDetails.getEmail()).ifPresent(existingAccount::setEmail);

		Optional.ofNullable(accountDetails.getAddress()).ifPresent(existingAccount::setAddress);

		Account updatedAccount = accountRepository.save(existingAccount);
		logger.info("Account successfully updated with accountId: {}", accountId);

		return updatedAccount;
	}
	
	public Account updateAccountFallback(String accountId, Account accountDetails, Throwable t) {
	    logger.error("Fallback triggered during updateAccount for accountId: {} - {}", accountId, t.getMessage());
	    return accountDetails; // or return null depending on your fallback policy
	}


	@Override
	@CircuitBreaker(name = "AccountService", fallbackMethod = "deleteAccountFallback")
	@Retry(name = "AccountService")
	@RateLimiter(name = "AccountService")
	public void deleteAccountById(Long id) throws AccountNotFoundException {
	    Optional<Account> deletedAccount = accountRepository.findById(id);
	    if (deletedAccount.isEmpty()) {
	        logger.error("Attempted to delete non-existing account with id {}", id);
	        throw new AccountNotFoundException("Account with id " + id + " not found");
	    }
	    accountRepository.deleteById(id);
	    logger.info("Successfully deleted account: {}", deletedAccount.get());
	
	}

	public void deleteAccountFallback(Long id, Throwable t) {
	    logger.error("Fallback triggered during deleteAccount for accountId: {} - {}", id, t.getMessage());
	}
	
	@Override
	public List<Account> findByStatus(AccountStatus accountStatus) throws InValidAccountException {
		List<Account> statusList = accountRepository.findByStatus(accountStatus);
		logger.info("Successfully retrieved accounts with status: {}", accountStatus);
		return statusList;
	}

	@Override
	@CircuitBreaker(name = "AccountService", fallbackMethod = "findAccountByIdFallback")
	@Retry(name = "AccountService")
	@RateLimiter(name = "AccountService")

	public Optional<Account> findByAccountId(String accountId) throws AccountNotFoundException {
		Optional<Account> getAccountId = accountRepository.findByAccountId(accountId);

		if (getAccountId.isEmpty()) {
			logger.error("Failed to retrieve account by accountId: {}", accountId);
			throw new AccountNotFoundException("Account not found for accountId: " + accountId);
		}

		logger.info("Successfully retrieved account with accountId: {}", accountId);
		return getAccountId;
	}
	public Optional<Account> findAccountByIdFallback(String accountId, Throwable t) {
	    logger.warn("Returning fallback dummy account for accountId: {} - {}", accountId, t.getMessage());
	    Account dummy = new Account();
	    dummy.setAccountId(accountId);
	    dummy.setCustomerName("Fallback User");
	    dummy.setAccountStatus(AccountStatus.ACTIVE);
	    return Optional.of(dummy);
	}


	@Override
	public Account findByPhoneNumber(String phoneNumber) {
		return accountRepository.findByPhoneNumber(phoneNumber).orElseThrow(() -> {
			logger.error("Account not found for phone number: {}", phoneNumber);
			return new AccountNotFoundException("Account not found for phone number: " + phoneNumber);
		});
	}

	@Override
	public Account findByEmail(String email) throws AccountNotFoundException {
		return accountRepository.findByEmail(email).orElseThrow(() -> {
			logger.error("Account not found for email:{}", email);
			return new AccountNotFoundException("Account not foud with email:" + email);
		});
	}

	@Override
	public List<Account> findAccountsCreatedAfter(LocalDateTime createdAt) {
		return accountRepository.findByCreatedAtAfter(createdAt);
	}

	@Override
	public List<Account> findByStatusAndAccountId(AccountStatus accountStatus, String accountId) {
		return accountRepository.findByAccountStatusAndAccountId(accountStatus, accountId);
	}

	@Override
	public Account changeAccountStatus(Long id, AccountStatus accountStatus) throws AccountNotFoundException {
		Account account = accountRepository.findById(id)
				.orElseThrow(() -> new AccountNotFoundException("Account not found for id: " + id));
		account.setAccountStatus(accountStatus);
		return accountRepository.save(account);
	}

	@Override
	public List<Account> findAllAccounts() {
		List<Account> getAllAccounts = accountRepository.findAll();
		logger.info("Successfully retrieved all accounts", getAllAccounts);
		return getAllAccounts;
	}

	@Override
	public Account findAccountById(Long id) {
		return accountRepository.findById(id)
				.orElseThrow(() -> new AccountNotFoundException("Account not found for id: " + id));
	}

	@Override
	public String getWelcomeMessage(String customerName) {
	    try {
	        String prompt = "Write a friendly welcome message for a new customer named " + customerName;

	        OpenAIRequest request = new OpenAIRequest("gpt-3.5-turbo",
	            List.of(new OpenAIMessage("user", prompt)));

	        OpenAIResponse response = webClient.post()
	            .uri("/chat/completions")
	            .header("Authorization", "Bearer " + prompt)
	            .bodyValue(request)
	            .retrieve()
	            .bodyToMono(OpenAIResponse.class)
	            .block();

	        return response.getChoices().get(0).getMessage().getContent();

	    } catch (Exception e) {
	        return "Failed to generate message: " + e.getMessage();
	    }

}
}
