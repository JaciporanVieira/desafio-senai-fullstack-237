
package br.com.senai.mercadofutebol.player.repository;

import br.com.senai.mercadofutebol.player.entity.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface PlayerRepository extends
        JpaRepository<Player, Integer>,
        JpaSpecificationExecutor<Player> {

    Optional<Player> findByIdAndDeletedAtIsNull(Integer id);

    Page<Player> findAllByDeletedAtIsNull(Pageable pageable);

    Page<Player> findAll(
            Specification<Player> specification,
            Pageable pageable
    );
}
