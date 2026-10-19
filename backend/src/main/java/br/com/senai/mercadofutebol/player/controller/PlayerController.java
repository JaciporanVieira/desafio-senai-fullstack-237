package br.com.senai.mercadofutebol.player.controller;

import br.com.senai.mercadofutebol.player.dto.PlayerCreateRequest;
import br.com.senai.mercadofutebol.player.entity.Player;
import br.com.senai.mercadofutebol.player.service.PlayerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/players")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping
    public ResponseEntity<Player> create(
            @Valid @RequestBody PlayerCreateRequest request) {

        Player createdPlayer = playerService.create(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdPlayer.getId())
                .toUri();

        return ResponseEntity.created(location).body(createdPlayer);
    }
}
