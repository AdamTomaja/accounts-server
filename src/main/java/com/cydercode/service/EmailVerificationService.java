package com.cydercode.service;

import com.cydercode.model.Account;
import com.cydercode.repository.AccountsRepository;
import com.cydercode.service.email.MailService;
import com.mailjet.client.errors.MailjetException;
import jakarta.transaction.Transactional;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailVerificationService {

  private final AccountsRepository accountsRepository;
  private final MailService mailService;

  @Transactional
  public void verifyEmail(String emailVerificationToken) throws MailjetException {
    log.info("Verifying email with token: {}", emailVerificationToken);
    Account account =
        accountsRepository
            .findByEmailVerificationToken(emailVerificationToken)
            .orElseThrow(() -> new RuntimeException("Email verification token not found"));

    account.setEmailVerifiedAt(java.time.Instant.now());
    account.setEmailVerificationToken(null);
    Account savedAccount = accountsRepository.save(account);
    sendActivationSuccessEmail(savedAccount);

    log.info(
        "Email verified for account with id: {}, username: {}, email: {}",
        account.getId(),
        account.getUsername(),
        account.getEmail());
  }

  private void sendActivationSuccessEmail(Account savedAccount) throws MailjetException {
    mailService.sendTemplate(
        savedAccount.getUsername(),
        savedAccount.getEmail(),
        "activation-success",
        Map.of("username", savedAccount.getUsername()));
  }
}
