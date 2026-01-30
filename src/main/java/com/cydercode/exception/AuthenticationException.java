package com.cydercode.exception;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@ToString
public class AuthenticationException extends Exception {

  private final AuthenticationExceptionType type;
}
