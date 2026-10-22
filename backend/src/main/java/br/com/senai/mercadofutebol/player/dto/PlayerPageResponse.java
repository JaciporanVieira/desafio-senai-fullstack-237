package br.com.senai.mercadofutebol.player.dto;

import java.util.List;

public record PlayerPageResponse(
        List<PlayerResponse> data,
        Meta meta
) {
    public record Meta(
            int page,
            int limit,
            long total_items,
            int total_pages
    ) {
    }
}
