package com.cydercode.dto.recovery;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RecoveryRequest {

  @NotBlank(message = "Email cannot be blank")
  @Size(min = 5, max = 50, message = "Email must be between 5 and 50 characters")
  @Email(message = "Invalid email format")
  private String email;
}
