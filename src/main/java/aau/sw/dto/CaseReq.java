package aau.sw.dto;

import aau.sw.model.Case.Status;

import jakarta.validation.constraints.NotBlank;

public record CaseReq (
  @NotBlank String title,
  @NotBlank Status status
) {}

