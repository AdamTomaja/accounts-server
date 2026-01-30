package com.cydercode.mapper;

import com.cydercode.dto.query.QueryAccountResponse;
import com.cydercode.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {
  public QueryAccountResponse toQueryAccountResponse(Account account) {
    return QueryAccountResponse.builder()
        .username(account.getUsername())
        .email(account.getEmail())
        .createdAt(account.getCreatedAt())
        .updatedAt(account.getUpdatedAt())
        .emailVerified(account.getEmailVerifiedAt() != null)
        .build();
  }
}
