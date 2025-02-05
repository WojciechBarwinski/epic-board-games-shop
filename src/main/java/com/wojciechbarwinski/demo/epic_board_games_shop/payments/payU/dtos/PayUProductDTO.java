package com.wojciechbarwinski.demo.epic_board_games_shop.payments.payU.dtos;


public record PayUProductDTO(
        String name,
        long unitPrice,
        int quantity
) {
}
