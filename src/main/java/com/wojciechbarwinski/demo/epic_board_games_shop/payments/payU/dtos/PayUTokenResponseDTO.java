package com.wojciechbarwinski.demo.epic_board_games_shop.payments.payU.dtos;


import lombok.Data;

@Data
public class PayUTokenResponseDTO {

    private String access_token;
    private String token_type;
    private int expires_in;
    private String grant_type;
}
