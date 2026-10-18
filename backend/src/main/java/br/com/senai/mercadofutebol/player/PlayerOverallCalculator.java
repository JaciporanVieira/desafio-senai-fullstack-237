
package br.com.senai.mercadofutebol.player;

import br.com.senai.mercadofutebol.player.entity.Player;
import br.com.senai.mercadofutebol.player.entity.PlayerPosition;

public final class PlayerOverallCalculator {

    private PlayerOverallCalculator() {
    }

    public static int calculate(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("O jogador não pode ser nulo.");
        }

        PlayerPosition position = player.getPosition();

        if (position == null) {
            throw new IllegalArgumentException("A posição do jogador é obrigatória.");
        }

        int pace = requireAttribute(player.getPace(), "pace");
        int shooting = requireAttribute(player.getShooting(), "shooting");
        int passing = requireAttribute(player.getPassing(), "passing");
        int dribbling = requireAttribute(player.getDribbling(), "dribbling");
        int defending = requireAttribute(player.getDefending(), "defending");
        int physical = requireAttribute(player.getPhysical(), "physical");

        int weightedScore = switch (position) {
            case GOL -> pace * 10
                    + passing * 20
                    + defending * 40
                    + physical * 30;

            case ZAG -> pace * 10
                    + passing * 15
                    + defending * 45
                    + physical * 30;

            case LAT -> pace * 25
                    + passing * 20
                    + dribbling * 15
                    + defending * 25
                    + physical * 15;

            case MEI -> pace * 10
                    + shooting * 15
                    + passing * 35
                    + dribbling * 25
                    + defending * 5
                    + physical * 10;

            case ATA -> pace * 20
                    + shooting * 40
                    + passing * 10
                    + dribbling * 20
                    + physical * 10;
        };

        // Arredondamento para o inteiro mais próximo, com empate para cima.
        return (weightedScore + 50) / 100;
    }

    private static int requireAttribute(Integer value, String attributeName) {
        if (value == null) {
            throw new IllegalArgumentException(
                    "O atributo " + attributeName + " é obrigatório."
            );
        }

        return value;
    }
}
