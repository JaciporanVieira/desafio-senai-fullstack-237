package br.com.senai.mercadofutebol.player.dto;

import br.com.senai.mercadofutebol.player.entity.PlayerPosition;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PlayerUpdateRequest(
        @NotBlank
        @Size(min = 3, max = 100)
        @Pattern(regexp = "^[\\p{L}]+(?:[ '\\-][\\p{L}]+)*$")
        String name,

        @NotNull
        PlayerPosition position,

        @NotNull
        @Min(1)
        @Max(100)
        Integer agility,

        @NotNull
        @Min(16)
        @Max(45)
        Integer age
) {
}