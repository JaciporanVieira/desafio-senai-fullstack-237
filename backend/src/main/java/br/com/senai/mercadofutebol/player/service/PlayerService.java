
package br.com.senai.mercadofutebol.player.service;


import br.com.senai.mercadofutebol.player.PlayerBaseValueCalculator;
import br.com.senai.mercadofutebol.player.PlayerMarketValueCalculator;
import br.com.senai.mercadofutebol.player.PlayerOverallCalculator;

import br.com.senai.mercadofutebol.player.dto.PlayerCreateRequest;
import br.com.senai.mercadofutebol.player.dto.PlayerPageResponse;
import br.com.senai.mercadofutebol.player.dto.PlayerResponse;
import br.com.senai.mercadofutebol.player.dto.PlayerUpdateRequest;
import br.com.senai.mercadofutebol.player.dto.PlayerConditionRequest;

import br.com.senai.mercadofutebol.player.entity.PhysicalCondition;
import br.com.senai.mercadofutebol.player.entity.Player;

import br.com.senai.mercadofutebol.player.exception.BusinessRuleException;

import br.com.senai.mercadofutebol.player.repository.PlayerRepository;
import br.com.senai.mercadofutebol.player.repository.PlayerSpecifications;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;


@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Transactional
    public Player create(PlayerCreateRequest request) {
        if (request == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Os dados do jogador são obrigatórios.");
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

    @Transactional(readOnly = true)
    public PlayerPageResponse list(int page, int limit) {
        return list(
                page, limit,
                null, null, null, null, null,
                null, null, null, null, "id");
    }

    @Transactional(readOnly = true)
    public PlayerPageResponse list(
            int page,
            int limit,
            String search,
            String position,
            String condition,
            Boolean free,
            Long clubId,
            Integer minAge,
            Integer maxAge,
            BigDecimal minValue,
            BigDecimal maxValue,
            String sort) {

        if (page < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O parâmetro page não pode ser negativo.");
        }

        if (limit < 1 || limit > 100) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O parâmetro limit deve estar entre 1 e 100.");
        }

        if ((minAge != null && minAge < 16)
                || (maxAge != null && maxAge > 45)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A faixa etária deve estar entre 16 e 45 anos.");
        }

        if (minAge != null && maxAge != null && minAge > maxAge) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "min_age não pode ser maior que max_age.");
        }

        if ((minValue != null && minValue.signum() < 0)
                || (maxValue != null && maxValue.signum() < 0)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Os valores de mercado não podem ser negativos.");
        }

        if (minValue != null && maxValue != null
                && minValue.compareTo(maxValue) > 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "min_value não pode ser maior que max_value.");
        }

        String sortField = sort == null || sort.isBlank()
                ? "id"
                : sort.trim().toLowerCase(Locale.ROOT);

        if (!List.of(
                "id", "name", "age", "overall",
                "base_value", "market_value").contains(sortField)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Campo de ordenação inválido.");
        }

        try {
            var specification = PlayerSpecifications.filters(
                    search,
                    position,
                    condition,
                    free,
                    clubId,
                    minAge,
                    maxAge);

            List<Player> players = playerRepository.findAll(
                    specification,
                    Sort.by(Sort.Direction.ASC, "id"));

            List<Player> filtered = players.stream()
                    .filter(player -> {
                        BigDecimal marketValue =
                                PlayerMarketValueCalculator.calculate(player);

                        return (minValue == null
                                || marketValue.compareTo(minValue) >= 0)
                                && (maxValue == null
                                || marketValue.compareTo(maxValue) <= 0);
                    })
                    .sorted(comparatorFor(sortField))
                    .toList();

            long totalItems = filtered.size();

            int totalPages = (int) Math.ceil(
                    (double) totalItems / limit);

            int fromIndex = (int) Math.min(
                    (long) page * limit,
                    totalItems);

            int toIndex = Math.min(
                    fromIndex + limit,
                    filtered.size());

            List<PlayerResponse> data = filtered
                    .subList(fromIndex, toIndex)
                    .stream()
                    .map(PlayerResponse::from)
                    .toList();

            return new PlayerPageResponse(
                    data,
                    new PlayerPageResponse.Meta(
                            page,
                            limit,
                            totalItems,
                            totalPages));

        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    ex.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public PlayerResponse getById(Long id) {
        Player player = playerRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Jogador não encontrado."));

        return PlayerResponse.from(player);
    }

        @Transactional
public PlayerResponse update(Long id, PlayerUpdateRequest request) {
    Player player = playerRepository.findByIdAndDeletedFalse(id)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Jogador não encontrado."));

    String normalizedName = request.name()
            .trim()
            .replaceAll("\\s+", " ");

    if (normalizedName.split(" ").length < 2) {
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "O nome deve conter pelo menos duas palavras.");
    }

    player.setName(normalizedName);
    player.setPosition(request.position());
    player.setAgility(request.agility());
    player.setAge(request.age());

    validateUpdatedPlayer(player);

    player.setBaseValue(PlayerBaseValueCalculator.calculate(player));

    Player updated = playerRepository.save(player);
    return PlayerResponse.from(updated);
}

@Transactional
public PlayerResponse updateCondition(
        Long id,
        PlayerConditionRequest request) {

    Player player = playerRepository.findByIdAndDeletedFalse(id)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Jogador não encontrado."));

    if (player.getCondition() == PhysicalCondition.DOENTE
            && request.condition() == PhysicalCondition.APTO) {
        throw new BusinessRuleException(
                "Não é permitido alterar diretamente de DOENTE para APTO.");
    }

    player.setCondition(request.condition());

    Player updated = playerRepository.save(player);
    return PlayerResponse.from(updated);
}

@Transactional
public void delete(Long id) {
    Player player = playerRepository.findByIdAndDeletedFalse(id)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Jogador não encontrado."));

    if (player.getClubId() != null) {
        throw new BusinessRuleException(
                "Não é permitido excluir um jogador vinculado a um clube.");
    }

    player.setDeleted(true);
    playerRepository.save(player);
}

private void validateUpdatedPlayer(Player player) {
    int maxAttribute = Math.max(
            Math.max(player.getPace(), player.getShooting()),
            Math.max(
                    Math.max(player.getPassing(), player.getDribbling()),
                    Math.max(player.getDefending(), player.getPhysical())));

    if (player.getAge() >= 35 && player.getAgility() > 85) {
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Jogadores com 35 anos ou mais não podem ter agilidade acima de 85.");
    }

    if (player.getAge() >= 33 && player.getPotential() != maxAttribute) {
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Para jogadores com 33 anos ou mais, o potencial deve ser igual ao maior atributo.");
    }
}

    private Comparator<Player> comparatorFor(String field) {
        Comparator<Player> comparator = switch (field) {
            case "name" -> Comparator.comparing(
                    Player::getName,
                    String.CASE_INSENSITIVE_ORDER);

            case "age" -> Comparator.comparing(Player::getAge);

            case "overall" -> Comparator.comparingInt(
                    PlayerOverallCalculator::calculate);

            case "base_value" -> Comparator.comparing(
                    Player::getBaseValue);

            case "market_value" -> Comparator.comparing(
                    PlayerMarketValueCalculator::calculate);

            default -> Comparator.comparing(Player::getId);
        };

        return comparator.thenComparing(Player::getId);
    }

    private void validateBusinessRules(PlayerCreateRequest request) {
        String normalizedName = request.name()
                .trim()
                .replaceAll("\\s+", " ");

        if (normalizedName.split(" ").length < 2) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O nome deve conter pelo menos duas palavras.");
        }

        int maxAttribute = Math.max(
                Math.max(request.pace(), request.shooting()),
                Math.max(
                        Math.max(request.passing(), request.dribbling()),
                        Math.max(request.defending(), request.physical())));

        int attributeSum = request.pace()
                + request.shooting()
                + request.passing()
                + request.dribbling()
                + request.defending()
                + request.physical();

        if (request.age() >= 35 && request.agility() > 85) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Jogadores com 35 anos ou mais não podem ter agilidade acima de 85.");
        }

        if (attributeSum > 480) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A soma dos seis atributos não pode ultrapassar 480.");
        }

        if (request.potential() < maxAttribute) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O potencial não pode ser inferior ao maior atributo.");
        }

        if (request.age() >= 33
                && request.potential() != maxAttribute) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Para jogadores com 33 anos ou mais, o potencial deve ser igual ao maior atributo.");
        }
    }
}
