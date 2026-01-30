package com.cydercode.controller;

import com.cydercode.dto.query.QueryAccountResponse;
import com.cydercode.mapper.AccountMapper;
import com.cydercode.model.Account;
import com.cydercode.service.AccountQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountQueryController {

    private final AccountQueryService accountQueryService;
    private final AccountMapper accountMapper;

    @GetMapping("/accounts")
    public QueryAccountResponse queryAccount(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email) {
        Account account = accountQueryService.queryAccount(id, username, email);
        return accountMapper.toQueryAccountResponse(account);
    }
}
