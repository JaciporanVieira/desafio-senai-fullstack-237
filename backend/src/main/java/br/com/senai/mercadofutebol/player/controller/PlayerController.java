
package br.com.senai.mercadofutebol.player.controller;

import br.com.senai.mercadofutebol.player.dto.PlayerConditionRequest;
import br.com.senai.mercadofutebol.player.dto.PlayerCreateRequest;
import br.com.senai.mercadofutebol.player.dto.PlayerPageResponse;
import br.com.senai.mercadofutebol.player.dto.PlayerResponse;
import br.com.senai.mercadofutebol.player.dto.PlayerUpdateRequest;
import br.com.senai.mercadofutebol.player.entity.Player;
import br.com.senai.mercadofutebol.player.service.PlayerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
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

    @GetMapping
    public ResponseEntity<PlayerPageResponse> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String condition,
            @RequestParam(required = false) Boolean free,
            @RequestParam(name = "club_id", required = false) Integer clubId,
            @RequestParam(name = "min_age", required = false) Integer minAge,
            @RequestParam(name = "max_age", required = false) Integer maxAge,
            @RequestParam(name = "min_value", required = false) BigDecimal minValue,
            @RequestParam(name = "max_value", required = false) BigDecimal maxValue,
            @RequestParam(defaultValue = "id") String sort) {

        return ResponseEntity.ok(playerService.list(
                page, limit, search, position, condition, free, clubId,
                minAge, maxAge, minValue, maxValue, sort));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerResponse> getById(
            @PathVariable Integer id) {

        return ResponseEntity.ok(playerService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlayerResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody PlayerUpdateRequest request) {

        return ResponseEntity.ok(playerService.update(id, request));
    }

    @PatchMapping("/{id}/condition")
    public ResponseEntity<PlayerResponse> updateCondition(
            @PathVariable Integer id,
            @Valid @RequestBody PlayerConditionRequest request) {

        return ResponseEntity.ok(
                playerService.updateCondition(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        playerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
