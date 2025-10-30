package aau.sw.dto;

import jakarta.validation.constraints.NotBlank;

public record CaseReq (
  @NotBlank String title,
  @NotBlank String status
) {}

