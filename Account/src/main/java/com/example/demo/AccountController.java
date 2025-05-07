package com.example.demo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.enums.AccountStatus;
import com.example.demo.exceptions.AccountAlreadyExistsException;
import com.example.demo.exceptions.AccountNotFoundException;
import com.example.demo.exceptions.InValidAccountException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
	
	private final AccountService accountService;

	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}
	
	@PostMapping
	public ResponseEntity<Account> createAccount(@Valid @RequestBody Account account)throws AccountAlreadyExistsException, InValidAccountException{
		
		Account createdAccount = accountService.createAccount(account);
	    logger.info("✅ Account created successfully | AccountId: {}", createdAccount.getAccountId());
	    return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Account> updateAccount(@PathVariable("id")String accountId, @Valid @RequestBody Account accountDetails)
			throws AccountNotFoundException, InValidAccountException{
		Account updatedAccount = accountService.updateAccount(accountId, accountDetails);
		logger.info("✅Account updated successfully. ID: {}, Details: {}", accountId, accountDetails);
		return ResponseEntity.ok(updatedAccount);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteAccount(@PathVariable("id") Long id) throws AccountNotFoundException {
	    accountService.deleteAccountById(id);
	    logger.info("🗑️ Account deleted successfully with ID: {}", id);
	    return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/status")
	public ResponseEntity<List<Account>> findByStatus(@RequestParam AccountStatus accountStatus) throws InValidAccountException {
	    List<Account> statusUpdated = accountService.findByStatus(accountStatus);
	    logger.info("✅ Successfully retrieved accounts with status: {}", accountStatus);
	    return ResponseEntity.ok(statusUpdated);  // Returns HTTP 200
	}
}
