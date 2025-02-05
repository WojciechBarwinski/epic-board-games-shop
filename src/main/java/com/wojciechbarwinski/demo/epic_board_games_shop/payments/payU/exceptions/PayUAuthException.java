package com.wojciechbarwinski.demo.epic_board_games_shop.payments.payU.exceptions;

import feign.FeignException;

public class PayUAuthException extends PayUException {

    public PayUAuthException(FeignException e) {
        super(generateMessage(e));
    }

    private static String generateMessage(FeignException e){
        return "Failed to retrieve access token. HTTP status: " + e.status() + " - " + e.getMessage();
    }
}
