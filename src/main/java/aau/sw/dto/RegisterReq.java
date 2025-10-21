package aau.sw.dto;

import jakarta.validation.constraints.*;

public record RegisterReq(
  @NotBlank @Email String email,
  @NotBlank String password,
  @NotBlank String name
) {}
