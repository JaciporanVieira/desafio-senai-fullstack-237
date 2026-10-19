package br.com.senai.mercadofutebol.player.repository;

import br.com.senai.mercadofutebol.player.entity.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findByIdAndDeletedFalse(Long id);

    Page<Player> findAllByDeletedFalse(Pageable pageable);
}