package com.cydercode.controller;

import com.cydercode.dto.RegisterAccountRequest;
import com.cydercode.dto.RegisterAccountResponse;
import com.cydercode.exception.RegisterException;
import com.cydercode.service.AccountsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountsController {

    private final AccountsService accountsService;

    @PostMapping("/accounts")
    public RegisterAccountResponse registerAccount(@Valid @RequestBody RegisterAccountRequest request)
            throws RegisterException {
        return new RegisterAccountResponse(
                accountsService
                        .registerAccount(request.getUsername(), request.getEmail(), request.getPassword()));
    }
}
