package com.cydercode.dto.recovery;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProceedRecoveryRequest {
  @NotBlank private String token;

  @NotBlank(message = "Password cannot be blank")
  @Size(min = 8, max = 30, message = "Password must be between 8 and 30 characters")
  private String newPassword;

  @NotBlank(message = "Password cannot be blank")
  @Size(min = 8, max = 30, message = "Password must be between 8 and 30 characters")
  private String newPasswordConfirm;
}
