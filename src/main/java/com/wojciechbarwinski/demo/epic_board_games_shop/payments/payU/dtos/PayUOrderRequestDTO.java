package com.wojciechbarwinski.demo.epic_board_games_shop.payments.payU.dtos;

import lombok.Data;

import java.util.List;

@Data
public class PayUOrderRequestDTO {
    private String customerIp;
    private String merchantPosId;
    private String description;
    private String currencyCode;
    private long totalAmount;
    private List<PayUProductDTO> products;
}
