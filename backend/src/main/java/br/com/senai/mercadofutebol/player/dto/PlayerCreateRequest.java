package br.com.senai.mercadofutebol.player.dto;

import br.com.senai.mercadofutebol.player.entity.PlayerPosition;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PlayerCreateRequest(

        @NotBlank(message = "O nome é obrigatório.")
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
        @Pattern(
                regexp = "^[\\p{L}]+(?:[ '\\-][\\p{L}]+)*$",
                message = "O nome deve conter somente letras e separadores válidos, sem números."
        )
        String name,

        @NotNull(message = "A posição é obrigatória.")
        PlayerPosition position,

        @NotNull(message = "A agilidade é obrigatória.")
        @Min(value = 1, message = "A agilidade mínima é 1.")
        @Max(value = 100, message = "A agilidade máxima é 100.")
        Integer agility,

        @NotNull(message = "A idade é obrigatória.")
        @Min(value = 16, message = "A idade mínima é 16.")
        @Max(value = 45, message = "A idade máxima é 45.")
        Integer age,

        @NotNull(message = "A velocidade é obrigatória.")
        @Min(1) @Max(99)
        Integer pace,

        @NotNull(message = "O chute é obrigatório.")
        @Min(1) @Max(99)
        Integer shooting,

        @NotNull(message = "O passe é obrigatório.")
        @Min(1) @Max(99)
        Integer passing,

        @NotNull(message = "O drible é obrigatório.")
        @Min(1) @Max(99)
        Integer dribbling,

        @NotNull(message = "A defesa é obrigatória.")
        @Min(1) @Max(99)
        Integer defending,

        @NotNull(message = "O físico é obrigatório.")
        @Min(1) @Max(99)
        Integer physical,

        @NotNull(message = "O potencial é obrigatório.")
        @Min(1) @Max(99)
        Integer potential
) {
}
