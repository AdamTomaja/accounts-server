package com.cydercode.service;

import com.cydercode.config.AppConfig;
import com.cydercode.exception.AuthenticationException;
import com.cydercode.exception.AuthenticationExceptionType;
import com.cydercode.exception.RecoveryException;
import com.cydercode.exception.RecoveryExceptionType;
import com.cydercode.exception.RegisterException;
import com.cydercode.exception.RegisterExceptionType;
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
public class AccountsService {

    public final static short MAX_AUTHENTICATION_ATTEMPTS = 5;
    public final static short RECOVERY_TOKEN_EXPIRATION_DAYS = 1;

    private final AccountsRepository accountsRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final AppConfig appConfig;

    @Transactional
    public long registerAccount(String username, String email, String password, String confirmPassword) throws RegisterException {
        log.info("Registering account with username: {}, email: {}", username, email);

        checkPreconditions(username, email);
        checkPassword(password, confirmPassword);
        log.info("Preconditions checked");
        Account account =
            Account.builder()
                .username(username)
                .email(email)
                .passwordHash(createPasswordHash(password))
                .emailVerificationToken(UUID.randomUUID().toString())
                .build();

        Account savedAccount = accountsRepository.save(account);
        log.info("Account registered with id: {}", savedAccount.getId());
        sendEmailVerification(savedAccount, account);
        log.info("Email verification sent to account with id: {}", savedAccount.getId());
        return savedAccount.getId();
    }

    private void checkPassword(String password, String confirmPassword) throws RegisterException {
        if( !StringUtils.equals(password, confirmPassword)) {
            throw new RegisterException(RegisterExceptionType.CONFIRM_PASSWORD_MISMATCH);
        }
    }

    private void sendEmailVerification(Account savedAccount, Account account) {
        log.info("Sending email verification to account with id: {}", savedAccount.getId());
        String activationLink = appConfig.getActivationBaseUrl() + savedAccount.getEmailVerificationToken();
        Map<String, Object> variables = Map.of(
                "username", savedAccount.getUsername(),
                "activationLink", activationLink);
        try {
            mailService.sendTemplate(savedAccount.getUsername(), savedAccount.getEmail(), "activation", variables);
        } catch (MailjetException e) {
            log.error("Error sending confirmation email", e);
            throw new RuntimeException(e);
        }
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

    @Transactional
    public Account checkCredentials(String email, String password) throws AuthenticationException {
        Account account = accountsRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new AuthenticationException(AuthenticationExceptionType.INVALID_CREDENTIALS));

        if(account.getEmailVerifiedAt() == null) {
            throw new AuthenticationException(AuthenticationExceptionType.EMAIL_NOT_VERIFIED);
        }

        if(account.getAuthenticationAttempts() >= MAX_AUTHENTICATION_ATTEMPTS) {
            throw new AuthenticationException(AuthenticationExceptionType.MAX_AUTHENTICATION_ATTEMPTS_LOCK);
        }

        if (!passwordEncoder.matches(password, account.getPasswordHash())) {
            log.warn("Invalid credentials for account with id: {}", account.getId());
            account.setAuthenticationAttempts((short) (account.getAuthenticationAttempts() + 1));
            throw new AuthenticationException(AuthenticationExceptionType.INVALID_CREDENTIALS);
        }

        log.info("Credentials checked for account with id: {}", account.getId());
        return account;
    }

    @Transactional
    public void startRecovery(String email) throws RecoveryException, MailjetException {
        log.info("Starting account recovery for email: {}", email);
        Account account = accountsRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        if(account.getResetPasswordExpiresAt() != null && Instant.now().isBefore(account.getResetPasswordExpiresAt())) {
            throw new RecoveryException(RecoveryExceptionType.ALREADY_EXISTING_RECOVERY);
        }

        account.setResetPasswordToken(UUID.randomUUID().toString());
        account.setResetPasswordExpiresAt(Instant.now().plus(RECOVERY_TOKEN_EXPIRATION_DAYS, ChronoUnit.DAYS));
        Account savedAccount = accountsRepository.save(account);
        sendRecoveryStartEmail(savedAccount);
    }

    private void sendRecoveryStartEmail(Account savedAccount) throws MailjetException {
        Map<String, Object> variables = Map.of(
                "username", savedAccount.getUsername(),
                "recoveryUrl", appConfig.getRecoveryUrl() + savedAccount.getResetPasswordToken());
        mailService.sendTemplate(savedAccount.getUsername(), savedAccount.getEmail(), "recovery-start", variables);
    }

    @Transactional
    public void proceedRecovery(String recoveryToken, String newPassword, String newPasswordConfirm)
            throws RecoveryException, MailjetException {
        Account account = accountsRepository.findByResetPasswordToken(recoveryToken)
                .orElseThrow(() -> new RecoveryException(RecoveryExceptionType.INVALID_TOKEN));

        log.info("Proceeding recovery for account with id: {}", account.getId());

        if(account.getResetPasswordExpiresAt().isBefore(Instant.now())) {
            log.warn("Recovery expired for account with id: {}", account.getId());
            throw new RecoveryException(RecoveryExceptionType.RECOVERY_EXPIRED);
        }

        if(!StringUtils.equals(newPassword, newPasswordConfirm)) {
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
                savedAccount.getUsername(),
                savedAccount.getEmail(),
                "recovery-success",
                variables);
    }
}
