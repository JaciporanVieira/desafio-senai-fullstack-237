
package br.com.senai.mercadofutebol.player;

import br.com.senai.mercadofutebol.player.entity.Player;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class PlayerBaseValueCalculator {

    private static final BigDecimal MINIMUM_VALUE =
            new BigDecimal("1.00");

    private PlayerBaseValueCalculator() {
    }

    public static BigDecimal calculate(Player player) {
        if (player == null) {
            throw new IllegalArgumentException(
                    "O jogador não pode ser nulo."
            );
        }

        if (player.getAge() == null
                || player.getPotential() == null) {
            throw new IllegalArgumentException(
                    "A idade e o potencial são obrigatórios."
            );
        }

        int overall = PlayerOverallCalculator.calculate(player);
        int potential = player.getPotential();
        int age = player.getAge();

        BigDecimal value = calculateProgressiveValue(overall);

        if (age <= 21) {
            BigDecimal multiplier = BigDecimal.valueOf(
                    100L + potential - overall
            ).divide(
                    new BigDecimal("100"),
                    4,
                    RoundingMode.HALF_UP
            );

            value = value.multiply(multiplier);
        }

        value = value.setScale(2, RoundingMode.HALF_UP);

        return value.compareTo(MINIMUM_VALUE) < 0
                ? MINIMUM_VALUE
                : value;
    }

    private static BigDecimal calculateProgressiveValue(
            int overall
    ) {
        BigDecimal value = BigDecimal.ZERO;

        if (overall > 40) {
            int points = Math.min(overall, 60) - 40;
            value = value.add(
                    BigDecimal.valueOf(points)
                            .multiply(new BigDecimal("0.50"))
            );
        }

        if (overall > 60) {
            int points = Math.min(overall, 75) - 60;
            value = value.add(
                    BigDecimal.valueOf(points)
                            .multiply(new BigDecimal("2.00"))
            );
        }

        if (overall > 75) {
            int points = overall - 75;
            value = value.add(
                    BigDecimal.valueOf(points)
                            .multiply(new BigDecimal("6.00"))
            );
        }

        return value;
    }
}
