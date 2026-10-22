package br.com.senai.mercadofutebol.player;

import br.com.senai.mercadofutebol.player.entity.PhysicalCondition;
import br.com.senai.mercadofutebol.player.entity.Player;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class PlayerMarketValueCalculator {

    private static final BigDecimal MINIMUM_VALUE =
            new BigDecimal("1.00");

    private PlayerMarketValueCalculator() {
    }

    public static BigDecimal calculate(Player player) {
        if (player == null
                || player.getBaseValue() == null
                || player.getAge() == null
                || player.getAgility() == null
                || player.getCondition() == null) {
            throw new IllegalArgumentException(
                    "Os dados do jogador são obrigatórios para calcular o valor de mercado."
            );
        }

        int conditionDiscount = switch (player.getCondition()) {
            case APTO -> 0;
            case MAL_CONDICIONADO -> 15;
            case DOENTE -> 30;
        };

        if (player.getCondition() != PhysicalCondition.APTO) {
            if (player.getAgility() >= 80) {
                conditionDiscount /= 2;
            } else if (player.getAgility() <= 30) {
                conditionDiscount += 10;
            }
        }

        int ageAdjustment;

        if (player.getAge() <= 21) {
            ageAdjustment = 10;
        } else if (player.getAge() <= 31) {
            ageAdjustment = 0;
        } else {
            ageAdjustment = -10;
        }

        int totalPercentage = ageAdjustment - conditionDiscount;
        totalPercentage = Math.max(totalPercentage, -40);

        BigDecimal multiplier = BigDecimal.valueOf(
                100L + totalPercentage
        ).divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);

        BigDecimal marketValue = player.getBaseValue()
                .multiply(multiplier)
                .setScale(2, RoundingMode.HALF_UP);

        return marketValue.compareTo(MINIMUM_VALUE) < 0
                ? MINIMUM_VALUE
                : marketValue;
    }
}
