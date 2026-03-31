package tech.buildrun.springsecurity.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
    @NotBlank(message = "Username e obrigatorio") String username,
    @NotBlank(message = "Password e obrigatorio") String password
) {
}
