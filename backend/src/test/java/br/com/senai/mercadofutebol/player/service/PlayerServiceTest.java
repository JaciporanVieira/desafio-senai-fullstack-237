
package br.com.senai.mercadofutebol.player.service;

import br.com.senai.mercadofutebol.player.dto.PlayerUpdateRequest;
import br.com.senai.mercadofutebol.player.entity.Player;
import br.com.senai.mercadofutebol.player.entity.PlayerPosition;
import br.com.senai.mercadofutebol.player.exception.BusinessRuleException;
import br.com.senai.mercadofutebol.player.repository.PlayerRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerService playerService;

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player();
        player.setId(1);
        player.setName("Pedro Souza");
        player.setPosition(PlayerPosition.ATA);
        player.setAge(22);
        player.setAgility(75);
        player.setPace(80);
        player.setShooting(85);
        player.setPassing(65);
        player.setDribbling(78);
        player.setDefending(40);
        player.setPhysical(75);
        player.setPotential(90);
        player.setBaseValue(new BigDecimal("70.00"));
        player.setClubId(null);
        player.setDeletedAt(null);

        when(playerRepository.findByIdAndDeletedAtIsNull(1))
                .thenReturn(Optional.of(player));
    }

    @Test
    void deveRejeitarReducaoDeIdade() {
        PlayerUpdateRequest request = new PlayerUpdateRequest(
                "Pedro Souza", PlayerPosition.ATA, 75, 21);

        assertThrows(
                BusinessRuleException.class,
                () -> playerService.update(1, request));

        verify(playerRepository, never()).save(any(Player.class));
    }

    @Test
    void deveRejeitarMudancaDePosicaoDeJogadorVinculadoAClube() {
        player.setClubId(10);

        PlayerUpdateRequest request = new PlayerUpdateRequest(
                "Pedro Souza", PlayerPosition.ZAG, 75, 22);

        assertThrows(
                BusinessRuleException.class,
                () -> playerService.update(1, request));

        verify(playerRepository, never()).save(any(Player.class));
    }

    @Test
    void deveRejeitarAgilidadeAcimaDe85ParaJogadorCom35Anos() {
        player.setPotential(85);

        PlayerUpdateRequest request = new PlayerUpdateRequest(
                "Pedro Souza", PlayerPosition.ATA, 86, 35);

        assertThrows(
                ResponseStatusException.class,
                () -> playerService.update(1, request));

        verify(playerRepository, never()).save(any(Player.class));
    }

    @Test
    void devePermitirAumentarIdadePara33PreservandoPotencialEValorBase() {
        // O maior atributo é 85; para idade 33, o potencial deve ser 85.
        player.setPotential(85);

        PlayerUpdateRequest request = new PlayerUpdateRequest(
                "Pedro Souza", PlayerPosition.ATA, 76, 33);

        when(playerRepository.save(any(Player.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        playerService.update(1, request);

        assertEquals(33, player.getAge());
        assertEquals(85, player.getPotential());
        assertEquals(
                new BigDecimal("70.00"),
                player.getBaseValue());

        verify(playerRepository).save(player);
    }

    @Test
    void deveRejeitarPotencialDiferenteDoMaiorAtributoParaJogadorCom33Anos() {
        // Maior atributo: 85. Potencial: 90. A atualização deve ser rejeitada.
        PlayerUpdateRequest request = new PlayerUpdateRequest(
                "Pedro Souza", PlayerPosition.ATA, 76, 33);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> playerService.update(1, request));

        assertEquals(
                HttpStatus.BAD_REQUEST,
                exception.getStatusCode());

        verify(playerRepository, never()).save(any(Player.class));
    }

    @Test
    void devePreservarValorBaseAoEditarJogador() {
        PlayerUpdateRequest request = new PlayerUpdateRequest(
                "Pedro Souza", PlayerPosition.ATA, 76, 22);

        when(playerRepository.save(any(Player.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        playerService.update(1, request);

        assertEquals(
                new BigDecimal("70.00"),
                player.getBaseValue());

        verify(playerRepository).save(player);
    }
}
