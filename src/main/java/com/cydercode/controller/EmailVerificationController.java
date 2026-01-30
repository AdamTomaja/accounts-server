package com.cydercode.controller;

import com.cydercode.dto.emailVerification.VerifyEmailRequest;
import com.cydercode.dto.emailVerification.VerifyEmailResponse;
import com.cydercode.service.EmailVerificationService;
import com.mailjet.client.errors.MailjetException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailVerificationController {

  private final EmailVerificationService emailVerificationService;

  @PostMapping("/verify-email")
  public VerifyEmailResponse verifyEmail(@Valid @RequestBody VerifyEmailRequest request)
      throws MailjetException {
    emailVerificationService.verifyEmail(request.getVerificationToken());
    return new VerifyEmailResponse();
  }
}
