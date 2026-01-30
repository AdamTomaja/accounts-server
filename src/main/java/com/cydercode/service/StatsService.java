package com.cydercode.service;

import com.cydercode.dto.stats.StatsResponse;
import com.cydercode.repository.AccountsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatsService {
  private final AccountsRepository accountsRepository;

  public StatsResponse getStats() {
    return StatsResponse.builder()
        .totalAccounts(accountsRepository.count())
        .unverifiedAccounts(accountsRepository.countByEmailVerifiedAtIsNull())
        .verifiedAccounts(accountsRepository.countByEmailVerifiedAtIsNotNull())
        .lockedAccounts(
            accountsRepository.countByAuthenticationAttemptsIsGreaterThanEqual(
                AccountsService.MAX_AUTHENTICATION_ATTEMPTS))
        .build();
  }
}
