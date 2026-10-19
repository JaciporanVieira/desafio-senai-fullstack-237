package br.com.senai.mercadofutebol.player.service;

import br.com.senai.mercadofutebol.player.PlayerBaseValueCalculator;
import br.com.senai.mercadofutebol.player.dto.PlayerCreateRequest;
import br.com.senai.mercadofutebol.player.entity.PhysicalCondition;
import br.com.senai.mercadofutebol.player.entity.Player;
import br.com.senai.mercadofutebol.player.exception.BusinessRuleException;
import br.com.senai.mercadofutebol.player.repository.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Transactional
    public Player create(PlayerCreateRequest request) {
        if (request == null) {
            throw new BusinessRuleException("Os dados do jogador são obrigatórios.");
        }

        validateBusinessRules(request);

        Player player = new Player();
        player.setName(request.name().trim().replaceAll("\\s+", " "));
        player.setPosition(request.position());
        player.setAgility(request.agility());
        player.setAge(request.age());
        player.setPace(request.pace());
        player.setShooting(request.shooting());
        player.setPassing(request.passing());
        player.setDribbling(request.dribbling());
        player.setDefending(request.defending());
        player.setPhysical(request.physical());
        player.setPotential(request.potential());
        player.setCondition(PhysicalCondition.APTO);
        player.setDeleted(false);
        player.setBaseValue(PlayerBaseValueCalculator.calculate(player));

        return playerRepository.save(player);
    }

    private void validateBusinessRules(PlayerCreateRequest request) {
        String name = request.name() == null
                ? ""
                : request.name().trim().replaceAll("\\s+", " ");

        if (name.split("\\s+").length < 2) {
            throw new BusinessRuleException(
                    "O nome do jogador deve conter pelo menos duas palavras."
            );
        }

        if (request.age() == null || request.agility() == null) {
            return;
        }

        if (request.age() >= 35 && request.agility() > 85) {
            throw new BusinessRuleException(
                    "Jogadores com 35 anos ou mais podem ter agilidade máxima de 85."
            );
        }

        if (request.pace() == null || request.shooting() == null
                || request.passing() == null || request.dribbling() == null
                || request.defending() == null || request.physical() == null
                || request.potential() == null) {
            return;
        }

        int sum = request.pace() + request.shooting() + request.passing()
                + request.dribbling() + request.defending() + request.physical();

        if (sum > 480) {
            throw new BusinessRuleException(
                    "A soma dos seis atributos não pode ultrapassar 480."
            );
        }

        int maxAttribute = Math.max(
                Math.max(Math.max(request.pace(), request.shooting()),
                        Math.max(request.passing(), request.dribbling())),
                Math.max(request.defending(), request.physical())
        );

        if (request.potential() < maxAttribute) {
            throw new BusinessRuleException(
                    "O potencial não pode ser inferior ao maior atributo."
            );
        }

        if (request.age() >= 33 && request.potential() != maxAttribute) {
            throw new BusinessRuleException(
                    "No cadastro de jogadores com 33 anos ou mais, o potencial deve ser igual ao maior atributo."
            );
        }
    }
}
