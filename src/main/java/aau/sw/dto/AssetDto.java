package aau.sw.dto;

import aau.sw.model.Image;
import aau.sw.model.Order;
import aau.sw.model.Asset.Status;
import jakarta.validation.constraints.NotBlank;

public record AssetDto(
    @NotBlank String name,
    @NotBlank String registrationNumber,
    @NotBlank Status status,
    Image profilePicture,
    Order orderRef
) {}
