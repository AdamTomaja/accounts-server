package com.cydercode.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class RegisterException extends Exception {

  private final RegisterExceptionType type;
}
