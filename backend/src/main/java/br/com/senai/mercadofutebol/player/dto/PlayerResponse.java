package br.com.senai.mercadofutebol.player.dto;

import br.com.senai.mercadofutebol.player.PlayerMarketValueCalculator;
import br.com.senai.mercadofutebol.player.PlayerOverallCalculator;
import br.com.senai.mercadofutebol.player.entity.PhysicalCondition;
import br.com.senai.mercadofutebol.player.entity.Player;
import br.com.senai.mercadofutebol.player.entity.PlayerPosition;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record PlayerResponse(
        Long id,
        String name,
        PlayerPosition position,
        Integer agility,
        Integer age,
        Integer pace,
        Integer shooting,
        Integer passing,
        Integer dribbling,
        Integer defending,
        Integer physical,
        Integer potential,
        PhysicalCondition condition,
        Long clubId,
        int overall,

        @JsonProperty("base_value")
        BigDecimal baseValue,

        @JsonProperty("market_value")
        BigDecimal marketValue
) {
    public static PlayerResponse from(Player player) {
        return new PlayerResponse(
                player.getId(),
                player.getName(),
                player.getPosition(),
                player.getAgility(),
                player.getAge(),
                player.getPace(),
                player.getShooting(),
                player.getPassing(),
                player.getDribbling(),
                player.getDefending(),
                player.getPhysical(),
                player.getPotential(),
                player.getCondition(),
                player.getClubId(),
                PlayerOverallCalculator.calculate(player),
                player.getBaseValue(),
                PlayerMarketValueCalculator.calculate(player)
        );
    }
}
