
package br.com.senai.mercadofutebol.player.entity;

import br.com.senai.mercadofutebol.player.PlayerBaseValueCalculator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerBaseValueCalculatorTest {

    @Test
    void deveCalcularValorBaseDeJogadorComOverall75() {
        assertBaseValue(75, 86, 25, "40.00");
    }

    @Test
    void deveCalcularValorBaseDeJogadorComOverall60() {
        assertBaseValue(60, 70, 30, "10.00");
    }

    @Test
    void deveCalcularValorBaseDeJogadorComOverall61() {
        assertBaseValue(61, 70, 30, "12.00");
    }

    @Test
    void deveCalcularValorBaseDeJogadorComOverall76() {
        assertBaseValue(76, 80, 22, "46.00");
    }

    @Test
    void deveAplicarMultiplicadorParaJogadorJovem() {
        assertBaseValue(70, 84, 21, "34.20");
    }

    @Test
    void deveAplicarValorMinimo() {
        assertBaseValue(40, 66, 36, "1.00");
    }

    @Test
    void deveArredondarParaDuasCasasDecimais() {
        assertBaseValue(45, 90, 18, "3.63");
    }

    private void assertBaseValue(
            int overall,
            int potential,
            int age,
            String expectedValue
    ) {
        Player player = new Player();
        player.setPosition(PlayerPosition.MEI);
        player.setAge(age);
        player.setPotential(potential);

        player.setPace(overall);
        player.setShooting(overall);
        player.setPassing(overall);
        player.setDribbling(overall);
        player.setDefending(overall);
        player.setPhysical(overall);

        assertEquals(
                new BigDecimal(expectedValue),
                PlayerBaseValueCalculator.calculate(player)
        );
    }
}
