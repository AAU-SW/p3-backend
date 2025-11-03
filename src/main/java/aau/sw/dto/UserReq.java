package aau.sw.dto;
import jakarta.validation.constraints.NotBlank;

public record UserReq (
    @NotBlank String name
){}
