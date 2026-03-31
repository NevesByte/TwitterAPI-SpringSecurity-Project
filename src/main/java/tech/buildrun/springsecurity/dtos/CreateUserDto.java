package tech.buildrun.springsecurity.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserDto(
    @NotBlank(message = "Username e obrigatorio") @Size(min = 3, max = 40) String username,
    @NotBlank(message = "Password e obrigatorio") @Size(min = 6, max = 100) String password
) {
}
