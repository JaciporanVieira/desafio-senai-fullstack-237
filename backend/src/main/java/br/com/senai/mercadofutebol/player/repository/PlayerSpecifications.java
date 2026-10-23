
package br.com.senai.mercadofutebol.player.repository;

import br.com.senai.mercadofutebol.player.entity.PhysicalCondition;
import br.com.senai.mercadofutebol.player.entity.Player;
import br.com.senai.mercadofutebol.player.entity.PlayerPosition;
import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;

public final class PlayerSpecifications {

    private PlayerSpecifications() {
    }

    public static Specification<Player> filters(
            String search,
            String position,
            String condition,
            Boolean free,
            Integer clubId,
            Integer minAge,
            Integer maxAge) {

        return (root, query, cb) -> {
            var predicates = cb.isNull(root.get("deletedAt"));

            if (search != null && !search.isBlank()) {
                String term = "%"
                        + search.trim().toLowerCase(Locale.ROOT)
                        + "%";

                predicates = cb.and(
                        predicates,
                        cb.like(cb.lower(root.get("name")), term));
            }

            if (position != null && !position.isBlank()) {
                PlayerPosition parsedPosition;

                try {
                    parsedPosition = PlayerPosition.valueOf(
                            position.trim().toUpperCase(Locale.ROOT));
                } catch (IllegalArgumentException ex) {
                    throw new IllegalArgumentException(
                            "Posição inválida: " + position);
                }

                predicates = cb.and(
                        predicates,
                        cb.equal(root.get("position"), parsedPosition));
            }

            if (condition != null && !condition.isBlank()) {
                PhysicalCondition parsedCondition;

                try {
                    parsedCondition = PhysicalCondition.valueOf(
                            condition.trim().toUpperCase(Locale.ROOT));
                } catch (IllegalArgumentException ex) {
                    throw new IllegalArgumentException(
                            "Condição física inválida: " + condition);
                }

                predicates = cb.and(
                        predicates,
                        cb.equal(root.get("condition"), parsedCondition));
            }

            if (Boolean.TRUE.equals(free)) {
                predicates = cb.and(
                        predicates,
                        cb.isNull(root.get("clubId")));
            } else if (Boolean.FALSE.equals(free)) {
                predicates = cb.and(
                        predicates,
                        cb.isNotNull(root.get("clubId")));
            }

            if (clubId != null) {
                predicates = cb.and(
                        predicates,
                        cb.equal(root.get("clubId"), clubId));
            }

            if (minAge != null) {
                predicates = cb.and(
                        predicates,
                        cb.greaterThanOrEqualTo(
                                root.get("age"), minAge));
            }

            if (maxAge != null) {
                predicates = cb.and(
                        predicates,
                        cb.lessThanOrEqualTo(
                                root.get("age"), maxAge));
            }

            return predicates;
        };
    }
}
