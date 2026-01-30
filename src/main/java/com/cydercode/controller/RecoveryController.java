package com.cydercode.controller;

import com.cydercode.dto.recovery.ProceedRecoveryRequest;
import com.cydercode.dto.recovery.RecoveryRequest;
import com.cydercode.exception.RecoveryException;
import com.cydercode.service.AccountsService;
import com.mailjet.client.errors.MailjetException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RecoveryController {

    private final AccountsService accountsService;

    @PostMapping("/recovery")
    public void startAccountRecoveryProcess(@Valid @RequestBody RecoveryRequest request) throws
            RecoveryException,
            MailjetException {
        accountsService.startRecovery(request.getEmail());
    }

    @PostMapping("/recovery/proceed")
    public void proceedRecoveryProcess(@Valid @RequestBody ProceedRecoveryRequest request) throws RecoveryException, MailjetException {
        accountsService.proceedRecovery(request.getToken(), request.getNewPassword(), request.getNewPasswordConfirm());
    }
}
