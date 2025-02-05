package com.wojciechbarwinski.demo.epic_board_games_shop.payments;


import lombok.Data;

@Data
public class PaymentDataDTO {
    private String orderId;
    private String redirectUri;
}