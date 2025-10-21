package aau.sw.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginReq(
  @NotBlank @Email String email,
  @NotBlank String password
) {}
