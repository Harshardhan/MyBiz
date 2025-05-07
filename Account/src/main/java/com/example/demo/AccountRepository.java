package com.example.demo;

import com.example.demo.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.demo.enums.AccountStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    // Find an account by account ID
    Optional<Account> findByAccountId(String accountId);

    // Find an account by phone number
    Optional<Account> findByPhoneNumber(String phoneNumber);

    // Find accounts by status
    @Query("SELECT a FROM Account a WHERE a.status = :status")
    List<Account> findByStatus(@Param("accountStatus") AccountStatus accountStatus);

    // Find an account by email
    Optional<Account> findByEmail(String email);

    // Find accounts created after a specific date
    List<Account> findByCreatedAtAfter(LocalDateTime createdAt);
    
    // Custom query: Find all accounts with a specific status and account number
    List<Account> findByAccountStatusAndAccountId(AccountStatus accountStatus, String accountId);

}
