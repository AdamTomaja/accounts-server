package com.cydercode.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class RecoveryException extends Exception {

  private final RecoveryExceptionType type;
}
