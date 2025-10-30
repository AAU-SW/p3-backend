package aau.sw.dto;
import jakarta.validation.constraints.NotBlank;

public record AssetReq (
    @NotBlank String name,
    @NotBlank String status,
    @NotBlank String registrationNumber
) {}