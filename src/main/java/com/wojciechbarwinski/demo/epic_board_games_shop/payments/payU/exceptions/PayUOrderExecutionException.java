package com.wojciechbarwinski.demo.epic_board_games_shop.payments.payU.exceptions;

public class PayUOrderExecutionException extends PayUException{

    public PayUOrderExecutionException(int responseCode) {
        super(generateMessage(responseCode));
    }

    private static String generateMessage(int responseCode){
        return "Error: Unsuccessful response from PayU. HTTP status: " + responseCode;
    }
}
