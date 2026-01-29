package com.cydercode.service;

import com.cydercode.exception.RegisterException;
import com.cydercode.exception.RegisterExceptionType;
import com.cydercode.model.Account;
import com.cydercode.repository.AccountsRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountsService {

    private final AccountsRepository accountsRepository;
    private final PasswordEncoder passwordEncoder;

    public long registerAccount(String username, String email, String password) throws RegisterException {
        log.info("Registering account with username: {}, email: {}", username, email);

        checkPreconditions(username, email);

    Account account =
        Account.builder()
            .username(username)
            .email(email)
            .passwordHash(createPasswordHash(password))
            .emailVerificationToken(UUID.randomUUID().toString())
            .build();

        Account savedAccount = accountsRepository.save(account);
        log.info("Account registered with id: {}", savedAccount.getId());
        return savedAccount.getId();
    }

    private void checkPreconditions(String username, String email) throws RegisterException {
        if (checkUsernameExists(username)) {
            throw new RegisterException(RegisterExceptionType.USERNAME_EXISTS);
        }

        if (checkEmailExists(email)) {
            throw new RegisterException(RegisterExceptionType.EMAIL_EXISTS);
        }
    }

    private boolean checkUsernameExists(String username) {
      return accountsRepository.countByUsernameIgnoreCase(username) > 0;
    }

    private boolean checkEmailExists(String email) {
        return accountsRepository.countByEmailIgnoreCase(email) > 0;
    }

    private String createPasswordHash(String password) {
        return passwordEncoder.encode(password);
    }
}
