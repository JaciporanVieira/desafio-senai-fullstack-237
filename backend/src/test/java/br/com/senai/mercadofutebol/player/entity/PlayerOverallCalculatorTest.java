package br.com.senai.mercadofutebol.player.entity;

import br.com.senai.mercadofutebol.player.PlayerOverallCalculator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerOverallCalculatorTest {

    @Test
    void deveCalcularOverallDeMeia() {
        assertOverall(PlayerPosition.MEI, 68, 65, 80, 78, 52, 60, 73);
    }

    @Test
    void deveCalcularOverallDeAtacante() {
        assertOverall(PlayerPosition.ATA, 78, 75, 73, 82, 48, 68, 76);
    }

    @Test
    void deveCalcularOverallDeLateral() {
        assertOverall(PlayerPosition.LAT, 72, 70, 78, 76, 70, 66, 72);
    }

    @Test
    void deveArredondarEmpateDeZagueiroParaCima() {
        assertOverall(PlayerPosition.ZAG, 50, 90, 60, 40, 80, 75, 73);
    }

    @Test
    void deveCalcularOverallDeGoleiro() {
        assertOverall(PlayerPosition.GOL, 55, 30, 65, 40, 72, 68, 68);
    }

    private void assertOverall(
            PlayerPosition position,
            int pace,
            int shooting,
            int passing,
            int dribbling,
            int defending,
            int physical,
            int expected
    ) {
        Player player = new Player();
        player.setPosition(position);
        player.setPace(pace);
        player.setShooting(shooting);
        player.setPassing(passing);
        player.setDribbling(dribbling);
        player.setDefending(defending);
        player.setPhysical(physical);

        assertEquals(expected, PlayerOverallCalculator.calculate(player));
    }
}
