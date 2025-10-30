package aau.sw.dto;
import aau.sw.model.Asset.Status;

import jakarta.validation.constraints.NotBlank;

public record AssetReq (
    @NotBlank String name,
    @NotBlank Status status,
    @NotBlank String registrationNumber
) {}