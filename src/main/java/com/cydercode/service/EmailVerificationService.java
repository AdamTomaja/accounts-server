package com.cydercode.service;

import com.cydercode.model.Account;
import com.cydercode.repository.AccountsRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailVerificationService {

    private final AccountsRepository accountsRepository;

    public void verifyEmail(String emailVerificationToken) {
        log.info("Verifying email with token: {}", emailVerificationToken);
        Account account = accountsRepository.findByEmailVerificationToken(emailVerificationToken)
                .orElseThrow(() -> new RuntimeException("Email verification token not found"));

        account.setEmailVerifiedAt(java.time.Instant.now());
        account.setEmailVerificationToken(null);
        accountsRepository.save(account);

        log.info("Email verified for account with id: {}, username: {}, email: {}",
                account.getId(),
                account.getUsername(),
                account.getEmail());
    }
}
