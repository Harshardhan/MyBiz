package com.example.demo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    private final OpenAIService openAIService;
    
    public AccountController(AccountService accountService, OpenAIService openAIService) {
        this.accountService = accountService;
		this.openAIService = openAIService;
    }

    
    
    @PostMapping("/generate")
    public ResponseEntity<?> generate(@RequestBody Map<String, String> request) {
        String prompt = request.get("prompt");
        String response = openAIService.generateResponse(prompt);
        return ResponseEntity.ok(Map.of("response", response));
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@Valid @RequestBody Account account)
            throws AccountAlreadyExistsException, InValidAccountException {

        Account createdAccount = accountService.createAccount(account);
        logger.info("✅ Account created successfully | AccountId: {}", createdAccount.getAccountId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable("id") String accountId, @Valid @RequestBody Account accountDetails)
            throws AccountNotFoundException, InValidAccountException {

        Account updatedAccount = accountService.updateAccount(accountId, accountDetails);
        logger.info("✅ Account updated successfully. ID: {}, Details: {}", accountId, accountDetails);
        return ResponseEntity.ok(updatedAccount);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable("id") Long id) throws AccountNotFoundException {
        accountService.deleteAccountById(id);
        logger.info("🗑️ Account deleted successfully with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/welcome/{customerName}")
    public ResponseEntity<String> getWelcomeMessage(@PathVariable String customerName) {
        String message = accountService.getWelcomeMessage(customerName);
        return ResponseEntity.ok(message);
    }


    @GetMapping("/status")
    public ResponseEntity<List<Account>> findByStatus(@RequestParam AccountStatus accountStatus) throws InValidAccountException {
        List<Account> statusAccounts = accountService.findByStatus(accountStatus);
        logger.info("✅ Retrieved {} accounts with status: {}", statusAccounts.size(), accountStatus);
        return ResponseEntity.ok(statusAccounts);
    }

    @GetMapping("/by-account-id/{accountId}")
    public ResponseEntity<Account> findByAccountId(@PathVariable String accountId) throws AccountNotFoundException {
        Optional<Account> accountOptional = accountService.findByAccountId(accountId);

        if (accountOptional.isEmpty()) {
            throw new AccountNotFoundException("Account with ID " + accountId + " not found");
        }

        logger.info("✅ Retrieved account by ID: {}", accountId);
        return ResponseEntity.ok(accountOptional.get());
    }

    @GetMapping("/by-phone")
    public ResponseEntity<Account> findByPhoneNumber(@RequestParam String phoneNumber) {
        Account account = accountService.findByPhoneNumber(phoneNumber);
        logger.info("✅ Retrieved account by phone number: {}", phoneNumber);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/by-email")
    public ResponseEntity<Account> findByEmail(@RequestParam String email) {
        Account account = accountService.findByEmail(email);
        logger.info("✅ Retrieved account by email: {}", email);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/created-after")
    public ResponseEntity<List<Account>> findAccountsCreatedAfter(@RequestParam LocalDateTime createdAt) {
        List<Account> accounts = accountService.findAccountsCreatedAfter(createdAt);
        logger.info("✅ Retrieved {} accounts created after: {}", accounts.size(), createdAt);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Account>> findByStatusAndAccountId(
            @RequestParam AccountStatus accountStatus,
            @RequestParam String accountId) {

        List<Account> accounts = accountService.findByStatusAndAccountId(accountStatus, accountId);
        logger.info("✅ Retrieved {} accounts for status: {} and accountId: {}", accounts.size(), accountStatus, accountId);
        return ResponseEntity.ok(accounts);
    }

    @PutMapping("/change-status/{id}")
    public ResponseEntity<Account> changeAccountStatus(@PathVariable Long id, @RequestParam AccountStatus accountStatus)
            throws AccountNotFoundException {

        Account updated = accountService.changeAccountStatus(id, accountStatus);
        logger.info("✅ Changed status of account ID: {} to {}", id, accountStatus);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Account>> findAllAccounts() {
        List<Account> accounts = accountService.findAllAccounts();
        logger.info("✅ Retrieved all accounts. Total count: {}", accounts.size());
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/by-id/{id}")
    public ResponseEntity<Account> findAccountById(@PathVariable Long id) {
        Account account = accountService.findAccountById(id);
        logger.info("✅ Retrieved account by numeric ID: {}", id);
        return ResponseEntity.ok(account);
    }
}
