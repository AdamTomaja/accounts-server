package com.cydercode.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class RegisterException extends Exception {

    private final RegisterExceptionType type;
}
