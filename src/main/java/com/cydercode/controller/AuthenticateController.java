package com.cydercode.controller;

import com.cydercode.dto.authenticate.AuthenticateRequest;
import com.cydercode.dto.query.QueryAccountResponse;
import com.cydercode.exception.AuthenticationException;
import com.cydercode.mapper.AccountMapper;
import com.cydercode.service.AccountsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticateController {

  private final AccountsService accountsService;
  private final AccountMapper accountMapper;

  @PostMapping("/accounts/authenticate")
  public QueryAccountResponse authenticate(@RequestBody @Valid AuthenticateRequest request)
      throws AuthenticationException {
    return accountMapper.toQueryAccountResponse(
        accountsService.checkCredentials(request.getEmail(), request.getPassword()));
  }
}
