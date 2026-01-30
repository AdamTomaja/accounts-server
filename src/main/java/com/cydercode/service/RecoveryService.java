package com.cydercode.service;

import com.cydercode.config.AppConfig;
import com.cydercode.exception.RecoveryException;
import com.cydercode.exception.RecoveryExceptionType;
import com.cydercode.model.Account;
import com.cydercode.repository.AccountsRepository;
import com.cydercode.service.email.MailService;
import com.mailjet.client.errors.MailjetException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecoveryService {

  public static final short RECOVERY_TOKEN_EXPIRATION_DAYS = 1;

  private final AccountsRepository accountsRepository;
  private final AppConfig appConfig;
  private final MailService mailService;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public void startRecovery(String email) throws RecoveryException, MailjetException {
    log.info("Starting account recovery for email: {}", email);
    Account account =
        accountsRepository
            .findByEmailIgnoreCase(email)
            .orElseThrow(() -> new RuntimeException("Account not found"));
    if (account.getResetPasswordExpiresAt() != null
        && Instant.now().isBefore(account.getResetPasswordExpiresAt())) {
      throw new RecoveryException(RecoveryExceptionType.ALREADY_EXISTING_RECOVERY);
    }

    account.setResetPasswordToken(UUID.randomUUID().toString());
    account.setResetPasswordExpiresAt(
        Instant.now().plus(RECOVERY_TOKEN_EXPIRATION_DAYS, ChronoUnit.DAYS));
    Account savedAccount = accountsRepository.save(account);
    sendRecoveryStartEmail(savedAccount);
  }

  private void sendRecoveryStartEmail(Account savedAccount) throws MailjetException {
    Map<String, Object> variables =
        Map.of(
            "username",
            savedAccount.getUsername(),
            "recoveryUrl",
            appConfig.getRecoveryUrl() + savedAccount.getResetPasswordToken());
    mailService.sendTemplate(
        savedAccount.getUsername(), savedAccount.getEmail(), "recovery-start", variables);
  }

  @Transactional
  public void proceedRecovery(String recoveryToken, String newPassword, String newPasswordConfirm)
      throws RecoveryException, MailjetException {
    Account account =
        accountsRepository
            .findByResetPasswordToken(recoveryToken)
            .orElseThrow(() -> new RecoveryException(RecoveryExceptionType.INVALID_TOKEN));

    log.info("Proceeding recovery for account with id: {}", account.getId());

    if (account.getResetPasswordExpiresAt().isBefore(Instant.now())) {
      log.warn("Recovery expired for account with id: {}", account.getId());
      throw new RecoveryException(RecoveryExceptionType.RECOVERY_EXPIRED);
    }

    if (!StringUtils.equals(newPassword, newPasswordConfirm)) {
      throw new RecoveryException(RecoveryExceptionType.CONFIRM_PASSWORD_MISMATCH);
    }

    account.setPasswordHash(createPasswordHash(newPassword));
    account.setResetPasswordToken(null);
    account.setResetPasswordExpiresAt(null);
    account.setAuthenticationAttempts((short) 0);
    Account savedAccount = accountsRepository.save(account);

    sendRecoverySuccessMail(savedAccount);
  }

  private void sendRecoverySuccessMail(Account savedAccount) throws MailjetException {
    Map<String, Object> variables = Map.of("username", savedAccount.getUsername());
    mailService.sendTemplate(
        savedAccount.getUsername(), savedAccount.getEmail(), "recovery-success", variables);
  }

  private String createPasswordHash(String password) {
    return passwordEncoder.encode(password);
  }
}
