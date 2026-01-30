package com.cydercode.dto.emailVerification;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifyEmailRequest {

  @NotBlank private String verificationToken;
}
