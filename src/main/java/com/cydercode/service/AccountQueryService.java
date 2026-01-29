package com.cydercode.service;

import com.cydercode.model.Account;
import com.cydercode.repository.AccountsRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountQueryService {

    private final AccountsRepository accountsRepository;

    public Account queryAccount(Long id, String username, String email) {
        if (id != null) {
            log.info("Querying account with id: {}", id);
            return accountsRepository.findById(id)
                    .orElseThrow( () -> getAccountNotFound());
        }

        if( StringUtils.isNotBlank(username)) {
            log.info("Querying account with username: {}", username);
            return accountsRepository.findByUsernameIgnoreCase(username)
                    .orElseThrow( () -> getAccountNotFound());
        }

        if( StringUtils.isNotBlank(email)) {
            log.info("Querying account with email: {}", email);
            return accountsRepository.findByEmailIgnoreCase(email)
                    .orElseThrow( () -> getAccountNotFound());
        }

        throw new RuntimeException("Query parameters are empty");
    }

    private static RuntimeException getAccountNotFound() {
        return new RuntimeException("Account not found");
    }
}
