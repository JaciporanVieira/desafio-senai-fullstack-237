package br.com.senai.mercadofutebol.player.dto;

import br.com.senai.mercadofutebol.player.entity.PhysicalCondition;
import jakarta.validation.constraints.NotNull;

public record PlayerConditionRequest(
        @NotNull
        PhysicalCondition condition
) {
}