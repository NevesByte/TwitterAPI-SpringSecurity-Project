package tech.buildrun.springsecurity.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTweetsDto(
    @NotBlank(message = "Conteudo e obrigatorio") @Size(min = 1, max = 280) String content
) {
}
